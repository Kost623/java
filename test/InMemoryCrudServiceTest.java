package test;

import Class.Book;
import Class.Reader;
import Exceptions.DomainException;
import Exceptions.EntityNotFoundException;
import Service.InMemoryBookService;
import Service.InMemoryCrudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryCrudService: save, findById, delete")
class InMemoryCrudServiceTest
{
    private InMemoryCrudService<TestEntity> service;

    @BeforeEach
    void setUp()
    {
        service = new InMemoryCrudService<>("Тест");
    }

    @Test
    @DisplayName("save нової сутності збільшує кількість записів")
    void saveNewIncreasesSize()
    {
        service.save(new TestEntity(1, "A"));
        service.save(new TestEntity(2, "B"));

        assertEquals(2, service.size());
    }

    @Test
    @DisplayName("save повертає збережену сутність")
    void saveReturnsEntity()
    {
        TestEntity entity = new TestEntity(1, "A");

        assertSame(entity, service.save(entity));
    }

    @Test
    @DisplayName("save з наявним id замінює запис, кількість не зростає")
    void saveExistingIdReplaces() throws EntityNotFoundException
    {
        service.save(new TestEntity(1, "Стара"));
        service.save(new TestEntity(1, "Нова"));

        assertEquals(1, service.size());
        assertEquals("Нова", service.findById(1).getName());
    }

    @Test
    @DisplayName("save(null) кидає IllegalArgumentException")
    void saveNullThrows()
    {
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
    }

    @Test
    @DisplayName("findById повертає збережену сутність")
    void findByIdReturnsSaved() throws EntityNotFoundException
    {
        TestEntity entity = new TestEntity(7, "A");
        service.save(entity);

        assertSame(entity, service.findById(7));
    }

    @Test
    @DisplayName("findById неіснуючого id кидає EntityNotFoundException з id та повідомленням")
    void findByIdMissingThrows()
    {
        EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () -> service.findById(999));

        assertEquals(999, e.getId());
        assertTrue(e.getMessage().contains("999"));
        assertTrue(e.getMessage().contains("Тест"));
    }

    @Test
    @DisplayName("delete наявного id повертає true, повторний delete повертає false")
    void deleteExistingThenAgain()
    {
        service.save(new TestEntity(1, "A"));

        assertTrue(service.delete(1));
        assertEquals(0, service.size());
        assertFalse(service.delete(1));
    }

    @Test
    @DisplayName("delete неіснуючого id повертає false і не чіпає сховище")
    void deleteMissingReturnsFalse()
    {
        service.save(new TestEntity(1, "A"));

        assertFalse(service.delete(2));
        assertEquals(1, service.size());
    }

    @Test
    @DisplayName("після delete findById кидає виняток")
    void findAfterDeleteThrows()
    {
        service.save(new TestEntity(1, "A"));
        service.delete(1);

        assertThrows(EntityNotFoundException.class, () -> service.findById(1));
    }

    @Test
    @DisplayName("findAll зберігає порядок вставки")
    void findAllKeepsInsertionOrder()
    {
        service.save(new TestEntity(3, "C"));
        service.save(new TestEntity(1, "A"));
        service.save(new TestEntity(2, "B"));

        List<Long> ids = service.findAll().stream().map(TestEntity::getId).toList();

        assertEquals(List.of(3L, 1L, 2L), ids);
    }

    @Test
    @DisplayName("findAll повертає копію: зміна копії не торкається сховища")
    void findAllReturnsCopy()
    {
        service.save(new TestEntity(1, "A"));

        List<TestEntity> copy = service.findAll();
        copy.clear();

        assertEquals(1, service.size());
    }

    @Test
    @DisplayName("той самий клас працює з Book: оновлення наявної книги не збільшує кількість")
    void sameClassWorksForBook() throws DomainException, EntityNotFoundException
    {
        InMemoryCrudService<Book> books = new InMemoryCrudService<>("Книга");
        Book book = TestData.printedBook("Кобзар", 250.0);
        books.save(book);
        books.save(TestData.ebook("Тіні", 90.0));

        Book found = books.findById(book.getId());
        found.setPrice(300.0);
        books.save(found);

        assertEquals(2, books.size());
        assertEquals(300.0, books.findById(book.getId()).getPrice());
    }

    @Test
    @DisplayName("той самий клас працює з Reader: save, оновлення, delete, пошук")
    void sameClassWorksForReader() throws EntityNotFoundException
    {
        InMemoryCrudService<Reader> readers = new InMemoryCrudService<>("Читач");
        Reader kate = TestData.reader("Катерина", 2003);
        Reader mike = TestData.reader("Михайло", 1999);
        readers.save(kate);
        readers.save(mike);

        kate.isActive = false;
        readers.save(kate);

        assertEquals(2, readers.size());
        assertFalse(readers.findById(kate.getId()).isActive);
        assertTrue(readers.delete(mike.getId()));
        assertThrows(EntityNotFoundException.class, () -> readers.findById(mike.getId()));
    }

    @Test
    @DisplayName("сервіс книг не плутає id з сервісом читачів: чужий id не знаходиться")
    void servicesAreIndependent() throws DomainException
    {
        InMemoryBookService bookService = new InMemoryBookService();
        InMemoryCrudService<Reader> readerService = new InMemoryCrudService<>("Читач");
        Book book = TestData.printedBook("Кобзар", 100.0);
        bookService.save(book);

        assertThrows(EntityNotFoundException.class, () -> readerService.findById(book.getId()));
    }
}