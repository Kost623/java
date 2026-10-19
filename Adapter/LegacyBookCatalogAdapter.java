package Adapter;

import Class.Book;
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
        legacyCatalog.addLegacyBook(book.name, book.yearRelease, book.price);
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
                books.add(new Book(title, 0, "Невідомо", 0, 0.0, 0, "Невідомо", "Невідомо", "Невідомо", 0.0));
            }
            catch (Exception e)
            {
                System.err.println("Помилка створення книги через адаптер: " + e.getMessage());
            }
        }
        return books;
    }
}
