import Class.AudioBook;
import Class.BaseEntity;
import Class.Book;
import Class.EBook;
import Class.PrintedBook;
import Class.Reader;
import Exceptions.DomainException;
import Service.CRUDService;
import Service.EntityUtils;
import Service.InMemoryBookService;
import Service.InMemoryReaderService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/*Демонстрація ЛР7-9, рівень 3: findAllSorted(Comparator<? super T>), default-метод, printAll з wildcard.*/
public class GenericsDemo
{
    public static void run() throws DomainException
    {
        IO.println("\n=== ЛР7-9: узагальнення та wildcard (рівень 3) ===");

        InMemoryBookService bookService = new InMemoryBookService();
        bookService.save(new PrintedBook("Кобзар", 1990, "Т. Шевченко", 12, 250.0, 5,
                "978-966-03-1111-1", "Поезія", "Дніпро", 4.8, 320, true));
        bookService.save(new EBook("Тіні забутих предків", 2015, "М. Коцюбинський", 14, 90.0, 100,
                "978-966-03-2222-2", "Повість", "Віват", 4.5, 2.4, "EPUB"));
        bookService.save(new AudioBook("Intermezzo", 2007, "М. Коцюбинський", 16, 120.0, 30,
                "978-966-03-3333-3", "Новела", "Яблуко", 4.6, 95, "А. Хостікоєв"));

        InMemoryReaderService readerService = new InMemoryReaderService();
        readerService.save(new Reader("Михайло", "C-002", "+380502223344", 1999, "mike@example.com", "2026-02-20", true));
        readerService.save(new Reader("Катерина", "C-001", "+380501112233", 2003, "kate@example.com", "2026-01-15", true));
        readerService.save(new Reader("Анна", "C-003", "+380503334455", 2001, "anna@example.com", "2026-03-10", true));

        // ---------- 1. default-метод інтерфейсу ----------
        IO.println("\n1) findAllSortedByName() - default-метод CRUDService, доступний через тип інтерфейсу:");
        CRUDService<Book> booksAsInterface = bookService;
        EntityUtils.printAll(booksAsInterface.findAllSortedByName());

        // ---------- 2. findAllSorted: довільний компаратор ----------
        IO.println("\n2) findAllSorted(...) з власним компаратором, ціна за спаданням:");
        List<Book> byPriceDesc = bookService.findAllSorted(Comparator.comparingDouble(Book::getPrice).reversed());
        for (Book b : byPriceDesc)
        {
            System.out.printf("   %-22s %8.2f грн%n", b.getName(), b.getPrice());
        }

        // ---------- 3. Comparator<? super T>: один компаратор BaseEntity для обох сервісів ----------
        IO.println("\n3) Один Comparator<BaseEntity> працює і для книг, і для читачів (? super T):");
        Comparator<BaseEntity> byIdDescending = Comparator.comparingLong(BaseEntity::getId).reversed();
        IO.println("   книги за id спаданням:");
        EntityUtils.printAll(bookService.findAllSorted(byIdDescending));
        IO.println("   читачі за id спаданням:");
        EntityUtils.printAll(readerService.findAllSorted(byIdDescending));

        // ---------- 4. Методи рівня 2 тепер виконуються через findAllSorted ----------
        IO.println("\n4) findAllSortedByNameAndByYearOfBirth() (реалізовано через findAllSorted):");
        EntityUtils.printAll(readerService.findAllSortedByNameAndByYearOfBirth());

        // ---------- 5. printAll і wildcard ----------
        IO.println("\n5) printAll(Collection<? extends BaseEntity>) приймає будь-яку колекцію сутностей:");
        IO.println("   List<Book>:");
        EntityUtils.printAll(bookService.findAll());

        IO.println("   Set<Reader>:");
        Set<Reader> readerSet = new LinkedHashSet<>(readerService.findAll());
        EntityUtils.printAll(readerSet);

        IO.println("   List<BaseEntity> (книги й читачі разом):");
        List<BaseEntity> mixed = new ArrayList<>();
        mixed.addAll(bookService.findAll());
        mixed.addAll(readerService.findAll());
        EntityUtils.printAll(mixed);

        // Інваріантність: List<Book> не є List<BaseEntity>, хоча Book є BaseEntity
        // List<BaseEntity> wrong = bookService.findAll();   // помилка компіляції: incompatible types
    }
}