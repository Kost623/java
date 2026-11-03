import Class.AudioBook;
import Class.Book;
import Class.EBook;
import Class.PrintedBook;
import Class.Reader;
import Exceptions.DomainException;
import Exceptions.EntityNotFoundException;
import Service.InMemoryCrudService;

/*Демонстрація ЛР7-9, рівень 1: той самий InMemoryCrudService працює і з Book, і з Reader.*/
public class CrudDemo
{
    public static void run() throws DomainException
    {
        IO.println("\n=== ЛР7-9: CRUD-сервіс (рівень 1) ===");

        InMemoryCrudService<Book> bookService = new InMemoryCrudService<>("Книга");
        InMemoryCrudService<Reader> readerService = new InMemoryCrudService<>("Читач");

        // ---------- 1. Додавання (Create) ----------
        IO.println("\n1) Додавання книг:");
        Book kobzar = new PrintedBook("Кобзар", 1990, "Т. Шевченко", 12, 250.0, 5,
                "978-966-03-1111-1", "Поезія", "Дніпро", 4.8, 320, true);
        Book shadows = new EBook("Тіні забутих предків", 2015, "М. Коцюбинський", 14, 90.0, 100,
                "978-966-03-2222-2", "Повість", "Віват", 4.5, 2.4, "EPUB");
        Book intermezzo = new AudioBook("Intermezzo", 2007, "М. Коцюбинський", 16, 120.0, 30,
                "978-966-03-3333-3", "Новела", "Яблуко", 4.6, 95, "А. Хостікоєв");

        bookService.save(kobzar);
        bookService.save(shadows);
        bookService.save(intermezzo);
        System.out.println("   збережено, записів у сервісі: " + bookService.size());
        for (Book b : bookService.findAll())
        {
            System.out.println("   id=" + b.getId() + " -> " + b.getShortInfo());
        }

        // ---------- 2. Оновлення наявної сутності (Update) ----------
        IO.println("\n2) Оновлення: той самий id, кількість записів не зростає:");
        try
        {
            Book found = bookService.findById(kobzar.getId());
            found.setPrice(300.0);
            bookService.save(found);
            System.out.println("   після save() записів: " + bookService.size()
                    + ", ціна \"" + bookService.findById(kobzar.getId()).getName() + "\": "
                    + bookService.findById(kobzar.getId()).getPrice());
        }
        catch (EntityNotFoundException e)
        {
            System.out.println("   неочікувано: " + e.getMessage());
        }

        // ---------- 3. Успішний і неуспішний delete ----------
        IO.println("\n3) Видалення:");
        System.out.println("   delete(id=" + shadows.getId() + "): " + bookService.delete(shadows.getId())
                + ", записів: " + bookService.size());
        System.out.println("   delete(id=" + shadows.getId() + ") вдруге: " + bookService.delete(shadows.getId())
                + ", записів: " + bookService.size());

        // ---------- 4. Пошук неіснуючого id ----------
        IO.println("\n4) Пошук неіснуючого id:");
        try
        {
            bookService.findById(999);
            System.out.println("   знайдено (неочікувано)");
        }
        catch (EntityNotFoundException e)
        {
            System.out.println("   виняток: " + e.getMessage() + " (getId() = " + e.getId() + ")");
        }

        // ---------- 5. Той самий клас для іншої сутності ----------
        IO.println("\n5) Той самий InMemoryCrudService, але для Reader:");
        Reader kate = new Reader("Катерина", "C-001", "+380501112233", 2003,
                "kate@example.com", "2026-01-15", true);
        Reader mike = new Reader("Михайло", "C-002", "+380502223344", 1999,
                "mike@example.com", "2026-02-20", true);

        readerService.save(kate);
        readerService.save(mike);
        System.out.println("   збережено читачів: " + readerService.size());

        kate.isActive = false;
        readerService.save(kate);
        System.out.println("   після оновлення записів: " + readerService.size()
                + ", активна Катерина: " + kate.isActive);

        System.out.println("   delete(Михайло): " + readerService.delete(mike.getId())
                + ", записів: " + readerService.size());
        try
        {
            readerService.findById(mike.getId());
        }
        catch (EntityNotFoundException e)
        {
            System.out.println("   виняток: " + e.getMessage());
        }

        // Типізація перевіряється на етапі компіляції: читача в сервіс книг покласти неможливо
        // bookService.save(kate);   // помилка компіляції: Reader не є Book
    }
}