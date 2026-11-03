package Class;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*Бібліотека МІСТИТЬ книги (композиція), а не Є списком книг (наслідування від ArrayList).
Інваріант «ISBN унікальний» тримається в одному місці: addBook().*/
public class Library
{
    private final String name;
    private final List<Book> books = new ArrayList<>();

    public Library(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void addBook(Book book)
    {
        if (book == null)
        {
            throw new IllegalArgumentException("Книга не може бути null");
        }
        if (containsIsbn(book.getIsbn()))
        {
            throw new IllegalArgumentException("Книга з ISBN " + book.getIsbn() + " уже є в бібліотеці");
        }
        books.add(book);
    }

    public boolean removeBook(String isbn)
    {
        return books.removeIf(b -> b.getIsbn().equals(isbn));
    }

    public List<Book> findByAuthor(String author)
    {
        List<Book> result = new ArrayList<>();
        for (Book b : books)
        {
            if (b.getAuthor().equals(author))
            {
                result.add(b);
            }
        }
        return result;
    }

    // Зовні віддаємо лише перегляд: змінити внутрішній список в обхід addBook неможливо
    public List<Book> getBooks()
    {
        return Collections.unmodifiableList(books);
    }

    public int size()
    {
        return books.size();
    }

    private boolean containsIsbn(String isbn)
    {
        for (Book b : books)
        {
            if (b.getIsbn().equals(isbn))
            {
                return true;
            }
        }
        return false;
    }
}