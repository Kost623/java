package test;

import Class.AudioBook;
import Class.Book;
import Class.EBook;
import Class.PrintedBook;
import Class.Reader;
import Exceptions.DomainException;

/*Фабрика тестових даних: короткі виклики замість довгих конструкторів.*/
final class TestData
{
    private TestData()
    {
    }

    static PrintedBook printedBook(String name, double price) throws DomainException
    {
        return new PrintedBook(name, 1990, "Автор", 12, price, 5,
                "isbn-p-" + name + price, "Жанр", "Видавець", 4.0, 320, true);
    }

    static EBook ebook(String name, double price) throws DomainException
    {
        return new EBook(name, 2015, "Автор", 14, price, 100,
                "isbn-e-" + name + price, "Жанр", "Видавець", 4.5, 2.4, "EPUB");
    }

    static AudioBook audioBook(String name, double price) throws DomainException
    {
        return new AudioBook(name, 2007, "Автор", 16, price, 30,
                "isbn-a-" + name + price, "Жанр", "Видавець", 4.6, 95, "Диктор");
    }

    static Reader reader(String name, int yearOfBirth)
    {
        return new Reader(name, "C-001", "+380501112233", yearOfBirth,
                "reader@example.com", "2026-01-15", true);
    }

    static Book[] sampleBooks() throws DomainException
    {
        // Порядок вставки навмисне невпорядкований; дві книги мають назву «Кобзар»
        return new Book[]
        {
            printedBook("Кобзар", 250.0),
            ebook("Тіні забутих предків", 90.0),
            printedBook("Алгебра", 180.0),
            ebook("Кобзар", 90.0),
            audioBook("Intermezzo", 120.0)
        };
    }
}