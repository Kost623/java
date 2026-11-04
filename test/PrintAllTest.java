
import Class.BaseEntity;
import Class.Book;
import Class.Reader;
import Exceptions.DomainException;
import Service.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EntityUtils.printAll(Collection<? extends BaseEntity>)")
class PrintAllTest
{
    private PrintStream originalOut;
    private ByteArrayOutputStream captured;

    @BeforeEach
    void captureOutput()
    {
        originalOut = System.out;
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreOutput()
    {
        System.setOut(originalOut);
    }

    private String output()
    {
        return captured.toString(StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("друкує по рядку на сутність: клас, id і назва")
    void printsOneLinePerEntity() throws DomainException
    {
        Book book = TestData.printedBook("Кобзар", 100.0);

        EntityUtils.printAll(List.of(book));

        assertEquals(1, output().lines().count());
        assertTrue(output().contains("PrintedBook"));
        assertTrue(output().contains("id=" + book.getId()));
        assertTrue(output().contains("Кобзар"));
    }

    @Test
    @DisplayName("приймає List<Book>")
    void acceptsListOfBooks() throws DomainException
    {
        List<Book> books = List.of(TestData.printedBook("A", 1.0), TestData.ebook("B", 2.0));

        EntityUtils.printAll(books);

        assertEquals(2, output().lines().count());
    }

    @Test
    @DisplayName("приймає Set<Reader>")
    void acceptsSetOfReaders()
    {
        Set<Reader> readers = new LinkedHashSet<>(List.of(TestData.reader("Анна", 2001), TestData.reader("Михайло", 1999)));

        EntityUtils.printAll(readers);

        assertEquals(2, output().lines().count());
        assertTrue(output().contains("Reader"));
    }

    @Test
    @DisplayName("приймає List<BaseEntity> з книгами й читачами разом")
    void acceptsMixedList() throws DomainException
    {
        List<BaseEntity> mixed = new ArrayList<>();
        mixed.add(TestData.printedBook("Кобзар", 100.0));
        mixed.add(TestData.reader("Катерина", 2003));

        EntityUtils.printAll(mixed);

        assertEquals(2, output().lines().count());
        assertTrue(output().contains("PrintedBook"));
        assertTrue(output().contains("Reader"));
    }

    @Test
    @DisplayName("не змінює колекцію")
    void doesNotModifyCollection() throws DomainException
    {
        List<Book> books = new ArrayList<>(List.of(TestData.printedBook("A", 1.0), TestData.ebook("B", 2.0)));

        EntityUtils.printAll(books);

        assertEquals(2, books.size());
    }

    @Test
    @DisplayName("порожня колекція нічого не друкує")
    void emptyCollectionPrintsNothing()
    {
        EntityUtils.printAll(List.of());

        assertEquals("", output());
    }

    @Test
    @DisplayName("null кидає NullPointerException")
    void nullThrows()
    {
        assertThrows(NullPointerException.class, () -> EntityUtils.printAll(null));
    }
}