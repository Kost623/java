package Adapter;

import Class.Book;
import java.util.List;

/*Цільовий інтерфейс, який очікує наш код.
Конкретні реалізації каталогів повинні реалізувати цей контракт.*/
public interface BookCatalog
{
    void addBook(Book book);

    double getPrice(String title);

    List<Book> getAllBooks();
}
