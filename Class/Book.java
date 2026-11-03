package Class;

import Exceptions.DomainException;
import Exceptions.InvalidPriceException;
import Exceptions.InvalidAgeException;

import java.util.Objects;

/*Запечатаний абстрактний базовий клас: «просто книги» без формату в бібліотеці не буває,
а нових форматів поза переліком permits бути не може.*/
public abstract sealed class Book permits PrintedBook, EBook, AudioBook, LegacyBook
{
    protected static final String UNKNOWN = "не вказано";

    // Трасування ланцюжка конструкторів: вмикається лише в демонстрації (пункт меню 15)
    private static boolean traceConstructors = false;

    public static void setTraceConstructors(boolean enabled)
    {
        traceConstructors = enabled;
    }

    protected static void trace(String message)
    {
        if (traceConstructors)
        {
            System.out.println("   [ctor] " + message);
        }
    }

    private String name;
    private int yearRelease;
    private String author;
    private int recommendedYear;
    private double price;
    private String status;
    private int number;
    private String isbn;
    private String genre;
    private String publisher;
    private double rating;

    // Повний конструктор. protected: екземпляр абстрактного класу напряму все одно не створити
    protected Book(String name, int yearRelease, String author, int recommendedYear, double price, int number,
                   String isbn, String genre, String publisher, double rating) throws DomainException
    {
        if (price < 0)
        {
            throw new InvalidPriceException("Ціна книги не може бути від'ємною", price);
        }
        if (recommendedYear < 0 || recommendedYear > 100)
        {
            throw new InvalidAgeException("Рекомендований вік поза допустимим діапазоном (0-100)", recommendedYear);
        }
        if (number < 0)
        {
            throw new IllegalArgumentException("Кількість книг не може бути від'ємною: " + number);
        }
        if (rating < 0.0 || rating > 5.0)
        {
            throw new IllegalArgumentException("Рейтинг книги має бути від 0.0 до 5.0: " + rating);
        }

        this.name = name;
        this.yearRelease = yearRelease;
        this.author = author;
        this.recommendedYear = recommendedYear;
        this.price = price;
        this.number = number;
        this.status = updateStatus();
        this.isbn = isbn;
        this.genre = genre;
        this.publisher = publisher;
        this.rating = rating;

        trace("Book: спільні поля ініціалізовано (\"" + name + "\")");
    }

    // Скорочений конструктор: делегує повному через this(...), без дублювання коду
    protected Book(String name, int yearRelease, String author, int recommendedYear, double price) throws DomainException
    {
        this(name, yearRelease, author, recommendedYear, price, 1, UNKNOWN, UNKNOWN, UNKNOWN, 0.0);
        trace("Book: скорочений конструктор (делегував через this(...))");
    }

    private String updateStatus()
    {
        return number > 0 ? "В наявності" : "Закінчився";
    }

    // ---------- Геттери ----------
    public String getName() { return name; }
    public int getYearRelease() { return yearRelease; }
    public String getAuthor() { return author; }
    public int getRecommendedYear() { return recommendedYear; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public int getNumber() { return number; }
    public String getIsbn() { return isbn; }
    public String getGenre() { return genre; }
    public String getPublisher() { return publisher; }
    public double getRating() { return rating; }

    // ---------- Сеттери ----------
    public void setNumber(int newNumber)
    {
        if (newNumber < 0)
        {
            throw new IllegalArgumentException("Кількість книг не може бути від'ємною: " + newNumber);
        }
        this.number = newNumber;
        this.status = updateStatus();
    }

    public void setPrice(double newPrice) throws DomainException
    {
        if (newPrice < 0)
        {
            throw new InvalidPriceException("Ціна книги не може бути від'ємною", newPrice);
        }
        this.price = newPrice;
    }

    // ---------- Абстрактні методи: кожен підклас зобов'язаний їх реалізувати ----------
    public abstract String getFormatName();

    public abstract String getSizeInfo();

    // ---------- Звичайний метод зі значенням за замовчуванням ----------
    public int getLoanDays()
    {
        return 14;
    }

    // ---------- final: єдиний формат короткого опису для логів і каталогів, підкласи його не змінюють ----------
    public final String getShortInfo()
    {
        return "[" + getFormatName() + "] " + name + " (" + yearRelease + ")";
    }

    @Override
    public String toString()
    {
        return String.format("Книга: \"%s\", рік видання: %d, Автор: \"%s\", рекомендований вік: %d, ціна: %.2f грн, кількість: %d, статус: %s, ISBN: \"%s\", жанр: \"%s\", видавець: \"%s\", рейтинг: %.1f",
                name, yearRelease, author, recommendedYear, price, number, status, isbn, genre, publisher, rating);
    }

    // equals через getClass(): книги різних підкласів ніколи не рівні, тому симетрія зберігається
    @Override
    public boolean equals(Object other)
    {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        var book = (Book) other;
        return this.name.equals(book.name)
                && this.yearRelease == book.yearRelease
                && this.author.equals(book.author)
                && this.recommendedYear == book.recommendedYear
                && Double.compare(this.price, book.price) == 0
                && this.number == book.number
                && this.status.equals(book.status)
                && this.isbn.equals(book.isbn)
                && this.genre.equals(book.genre)
                && this.publisher.equals(book.publisher)
                && Double.compare(this.rating, book.rating) == 0;
    }

    // hashCode лише за незмінними полями: price і number мають сеттери, і зміна ціни
    // зламала б пошук у HashSet/HashMap (рівні об'єкти мають давати однаковий hashCode)
    @Override
    public int hashCode()
    {
        return Objects.hash(name, yearRelease, author, isbn);
    }
}