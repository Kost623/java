package test;
import Class.BaseEntity;
import Class.Book;
import Class.Reader;
import Exceptions.DomainException;
import Service.BookService;
import Service.CRUDService;
import Service.EntityUtils;
import Service.InMemoryBookService;
import Service.InMemoryReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Сортування: findAllSorted, findAllSortedByName, ...ByNameAndBy<Поле>")
class SortingTest
{
    private InMemoryBookService bookService;
    private InMemoryReaderService readerService;

    @BeforeEach
    void setUp() throws DomainException
    {
        bookService = new InMemoryBookService();
        for (Book b : TestData.sampleBooks())
        {
            bookService.save(b);
        }

        readerService = new InMemoryReaderService();
        readerService.save(TestData.reader("Михайло", 1999));
        readerService.save(TestData.reader("Катерина", 2003));
        readerService.save(TestData.reader("Анна", 2001));
        readerService.save(TestData.reader("Катерина", 1998));
    }

    private static List<String> names(List<? extends BaseEntity> items)
    {
        return items.stream().map(BaseEntity::getName).toList();
    }

    // ---------- findAllSortedByName ----------

    @Test
    @DisplayName("findAllSortedByName сортує за назвою за українським алфавітом")
    void sortedByName()
    {
        assertEquals(List.of("Intermezzo", "Алгебра", "Кобзар", "Кобзар", "Тіні забутих предків"),
                names(bookService.findAllSortedByName()));
    }

    @Test
    @DisplayName("findAllSortedByName стабільний: однакові назви лишаються в порядку вставки")
    void sortedByNameIsStable()
    {
        List<Book> sorted = bookService.findAllSortedByName();

        assertEquals(250.0, sorted.get(2).getPrice()); // «Кобзар» 250 вставлено раніше за «Кобзар» 90
        assertEquals(90.0, sorted.get(3).getPrice());
    }

    @Test
    @DisplayName("findAllSortedByName є default-методом інтерфейсу CRUDService")
    void sortedByNameIsDefaultMethod() throws NoSuchMethodException
    {
        assertTrue(CRUDService.class.getMethod("findAllSortedByName").isDefault());
    }

    @Test
    @DisplayName("findAllSortedByName доступний через тип інтерфейсу CRUDService<Book>")
    void sortedByNameViaInterface()
    {
        CRUDService<Book> service = bookService;

        assertEquals("Intermezzo", service.findAllSortedByName().get(0).getName());
    }

    // ---------- findAllSortedByNameAndBy<Поле> ----------

    @Test
    @DisplayName("книги: при збігу назв друге поле (ціна) розв'язує порядок")
    void booksSortedByNameAndPrice()
    {
        List<Book> sorted = bookService.findAllSortedByNameAndByPrice();

        assertEquals(List.of("Intermezzo", "Алгебра", "Кобзар", "Кобзар", "Тіні забутих предків"), names(sorted));
        assertEquals(90.0, sorted.get(2).getPrice());
        assertEquals(250.0, sorted.get(3).getPrice());
    }

    @Test
    @DisplayName("читачі: при збігу імен друге поле (рік народження) розв'язує порядок")
    void readersSortedByNameAndYear()
    {
        List<Reader> sorted = readerService.findAllSortedByNameAndByYearOfBirth();

        assertEquals(List.of("Анна", "Катерина", "Катерина", "Михайло"), names(sorted));
        assertEquals(1998, sorted.get(1).yearOfBirth);
        assertEquals(2003, sorted.get(2).yearOfBirth);
    }

    @Test
    @DisplayName("інтерфейс BookService розширює CRUDService<Book>, а клас успадковує InMemoryCrudService")
    void interfacesAndInheritance()
    {
        assertTrue(CRUDService.class.isAssignableFrom(BookService.class));
        assertTrue(Service.InMemoryCrudService.class.isAssignableFrom(InMemoryBookService.class));
        assertTrue(BookService.class.isAssignableFrom(InMemoryBookService.class));
    }

    // ---------- Незмінність сховища ----------

    @Test
    @DisplayName("сортування не змінює порядок і склад сховища")
    void storageUnchangedAfterSorting()
    {
        List<Long> before = bookService.findAll().stream().map(Book::getId).toList();

        bookService.findAllSortedByName();
        bookService.findAllSortedByNameAndByPrice();
        bookService.findAllSorted(Comparator.comparingDouble(Book::getPrice));

        assertEquals(before, bookService.findAll().stream().map(Book::getId).toList());
        assertEquals(5, bookService.size());
    }

    @Test
    @DisplayName("відсортований список є новою колекцією: його зміна не торкається сховища")
    void sortedListIsIndependent()
    {
        List<Book> sorted = bookService.findAllSortedByNameAndByPrice();
        sorted.clear();

        assertEquals(5, bookService.size());
    }

    @Test
    @DisplayName("сортування порожнього сервісу повертає порожній список")
    void sortEmptyService()
    {
        assertTrue(new InMemoryBookService().findAllSortedByName().isEmpty());
    }

    // ---------- findAllSorted: довільний компаратор і Comparator<? super T> ----------

    @Test
    @DisplayName("findAllSorted приймає власний компаратор (ціна за спаданням)")
    void findAllSortedWithCustomComparator()
    {
        List<Book> sorted = bookService.findAllSorted(Comparator.comparingDouble(Book::getPrice).reversed());

        assertEquals(250.0, sorted.get(0).getPrice());
        assertEquals(90.0, sorted.get(4).getPrice());
    }

    @Test
    @DisplayName("Comparator<BaseEntity> підходить і для книг, і для читачів (? super T)")
    void superTypeComparatorAccepted()
    {
        Comparator<BaseEntity> byIdDescending = Comparator.comparingLong(BaseEntity::getId).reversed();

        List<Book> books = bookService.findAllSorted(byIdDescending);
        List<Reader> readers = readerService.findAllSorted(byIdDescending);

        assertTrue(books.get(0).getId() > books.get(1).getId());
        assertTrue(readers.get(0).getId() > readers.get(1).getId());
    }

    // ---------- Порядок назв ----------

    @Test
    @DisplayName("NAME_ORDER ставить Є, І, Ї на свої місця в українському алфавіті")
    void ukrainianAlphabetOrder()
    {
        Comparator<String> order = EntityUtils.NAME_ORDER;

        assertTrue(order.compare("Ера", "Єва") < 0);
        assertTrue(order.compare("Єва", "Жук") < 0);
        assertTrue(order.compare("Иван", "Іван") < 0);
        assertTrue(order.compare("Іван", "Їжак") < 0);
        assertTrue(order.compare("Їжак", "Кобзар") < 0);
        // природний порядок String ставив би Є перед А
        assertTrue("Єва".compareTo("Алгебра") < 0);
        assertTrue(order.compare("Єва", "Алгебра") > 0);
    }

    @Test
    @DisplayName("NAME_ORDER не розрізняє регістр: «кобзар» і «Кобзар» однакові")
    void nameOrderIgnoresCase()
    {
        assertEquals(0, EntityUtils.NAME_ORDER.compare("кобзар", "Кобзар"));
    }
}