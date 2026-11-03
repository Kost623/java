import Class.AudioBook;
import Class.Book;
import Class.EBook;
import Class.PrintedBook;
import Class.Reader;
import Class.Textbook;
import Exceptions.DomainException;
import Service.InMemoryBookService;
import Service.InMemoryReaderService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/*Демонстрація ЛР7-9, рівень 2: сортування через Comparator, незмінність сховища, узгодженість з equals.*/
public class SortingDemo
{
    public static void run() throws DomainException
    {
        IO.println("\n=== ЛР7-9: сортування (рівень 2) ===");

        // ---------- Книги: дві з назвою «Кобзар», вставлені дорожчою першою ----------
        InMemoryBookService bookService = new InMemoryBookService();
        bookService.save(new PrintedBook("Кобзар", 1990, "Т. Шевченко", 12, 250.0, 5,
                "978-966-03-1111-1", "Поезія", "Дніпро", 4.8, 320, true));
        bookService.save(new EBook("Тіні забутих предків", 2015, "М. Коцюбинський", 14, 90.0, 100,
                "978-966-03-2222-2", "Повість", "Віват", 4.5, 2.4, "EPUB"));
        bookService.save(new Textbook("Алгебра, 9 клас", 2019, "О. Істер", 14, 180.0, 40,
                "978-966-11-4444-4", "Підручник", "Генеза", 4.2, 288, true, "Математика", 9));
        bookService.save(new EBook("Кобзар", 2020, "Т. Шевченко", 12, 90.0, 200,
                "978-966-03-1112-8", "Поезія", "Дніпро", 4.7, 3.1, "PDF"));
        bookService.save(new AudioBook("Intermezzo", 2007, "М. Коцюбинський", 16, 120.0, 30,
                "978-966-03-3333-3", "Новела", "Яблуко", 4.6, 95, "А. Хостікоєв"));

        IO.println("\n1) Книги у сховищі до сортування (порядок вставки):");
        printBooks(bookService.findAll());

        IO.println("\n2) findAllSortedByName(): однакові назви лишаються в порядку вставки:");
        printBooks(bookService.findAllSortedByName());

        IO.println("\n3) findAllSortedByNameAndByPrice(): при збігу назв дешевша книга йде першою:");
        List<Book> sorted = bookService.findAllSortedByNameAndByPrice();
        printBooks(sorted);

        IO.println("\n4) Сховище після сортування (склад і порядок не змінилися):");
        printBooks(bookService.findAll());
        sorted.clear();
        System.out.println("   після sorted.clear() у сховищі все ще " + bookService.size() + " записів: "
                + "відсортований список є окремою колекцією");

        // ---------- Читачі: дві «Катерини» з різним роком народження ----------
        InMemoryReaderService readerService = new InMemoryReaderService();
        readerService.save(new Reader("Михайло", "C-002", "+380502223344", 1999, "mike@example.com", "2026-02-20", true));
        readerService.save(new Reader("Катерина", "C-001", "+380501112233", 2003, "kate@example.com", "2026-01-15", true));
        readerService.save(new Reader("Анна", "C-003", "+380503334455", 2001, "anna@example.com", "2026-03-10", true));
        readerService.save(new Reader("Катерина", "C-004", "+380504445566", 1998, "kate2@example.com", "2026-04-01", true));

        IO.println("\n5) Читачі: до сортування, за іменем і за іменем + роком народження:");
        printReaders("до сортування:", readerService.findAll());
        printReaders("findAllSortedByName():", readerService.findAllSortedByName());
        printReaders("findAllSortedByNameAndByYearOfBirth():", readerService.findAllSortedByNameAndByYearOfBirth());
        printReaders("сховище після сортування:", readerService.findAll());

        // ---------- Узгодженість компаратора з equals (за id) ----------
        IO.println("\n6) Узгодженість компаратора з equals:");
        Reader a = new Reader("Олена", "C-101", "+380505556677", 2000, "a@example.com", "2026-05-01", true);
        Reader b = new Reader("Олена", "C-102", "+380505556688", 2000, "b@example.com", "2026-05-02", true);
        System.out.println("   a.equals(b): " + a.equals(b) + " (id " + a.getId() + " і " + b.getId() + ")");
        System.out.println("   NAME_THEN_YEAR.compare(a, b): " + InMemoryReaderService.NAME_THEN_YEAR.compare(a, b));

        Set<Reader> hashSet = new HashSet<>(List.of(a, b));
        Set<Reader> treeSet = new TreeSet<>(InMemoryReaderService.NAME_THEN_YEAR);
        treeSet.add(a);
        treeSet.add(b);
        Set<Reader> treeSetWithId = new TreeSet<>(InMemoryReaderService.NAME_THEN_YEAR.thenComparingLong(Reader::getId));
        treeSetWithId.add(a);
        treeSetWithId.add(b);

        System.out.println("   HashSet (equals/hashCode за id):            " + hashSet.size() + " елементи");
        System.out.println("   TreeSet з NAME_THEN_YEAR:                   " + treeSet.size() + " елемент (другого втрачено!)");
        System.out.println("   TreeSet з NAME_THEN_YEAR + порівняння id:   " + treeSetWithId.size() + " елементи");
    }

    private static void printBooks(List<Book> books)
    {
        for (Book b : books)
        {
            System.out.printf("   id=%-3d %-22s %8.2f грн  (%s)%n",
                    b.getId(), b.getName(), b.getPrice(), b.getClass().getSimpleName());
        }
    }

    private static void printReaders(String title, List<Reader> readers)
    {
        System.out.println("   " + title);
        for (Reader r : readers)
        {
            System.out.printf("      id=%-3d %-10s рік народження: %d%n", r.getId(), r.getName(), r.yearOfBirth);
        }
    }
}