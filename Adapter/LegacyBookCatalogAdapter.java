package Adapter;

import Class.Book;
import Class.LegacyBook;
import Legacy.LegacyBookCatalog;
import java.util.ArrayList;
import java.util.List;

/*Адаптер: перекладає виклики цільового інтерфейсу BookCatalog на методи
ззовнішнього класу LegacyBookCatalog, не змінюючи сам цей клас.*/
public class LegacyBookCatalogAdapter implements BookCatalog
{
    private final LegacyBookCatalog legacyCatalog;

    public LegacyBookCatalogAdapter(LegacyBookCatalog legacyCatalog)
    {
        this.legacyCatalog = legacyCatalog;
    }

    @Override
    public void addBook(Book book)
    {
        // book може бути будь-яким підкласом: PrintedBook, Textbook, EBook, AudioBook
        legacyCatalog.addLegacyBook(book.getName(), book.getYearRelease(), book.getPrice());
    }

    @Override
    public double getPrice(String title)
    {
        return legacyCatalog.getLegacyCost(title);
    }

    @Override
    public List<Book> getAllBooks()
    {
        String[] titles = legacyCatalog.listAllLegacyTitles();
        List<Book> books = new ArrayList<>();
        for (String title : titles)
        {
            try
            {
                // Легасі-каталог не зберігає формат, тому записи відновлюємо як LegacyBook
                books.add(new LegacyBook(title));
            }
            catch (Exception e)
            {
                System.err.println("Помилка створення книги через адаптер: " + e.getMessage());
            }
        }
        return books;
    }
}