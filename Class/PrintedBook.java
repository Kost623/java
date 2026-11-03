package Class;

import Exceptions.DomainException;

import java.util.Objects;

/*Паперова книга: is-a Book. Сама має підклас Textbook (3-й рівень).*/
public sealed class PrintedBook extends Book permits Textbook
{
    private int pages;
    private boolean hasHardcover;

    // Повний конструктор
    public PrintedBook(String name, int yearRelease, String author, int recommendedYear, double price, int number,
                       String isbn, String genre, String publisher, double rating,
                       int pages, boolean hasHardcover) throws DomainException
    {
        super(name, yearRelease, author, recommendedYear, price, number, isbn, genre, publisher, rating);
        if (pages <= 0)
        {
            throw new IllegalArgumentException("Кількість сторінок має бути додатною: " + pages);
        }
        this.pages = pages;
        this.hasHardcover = hasHardcover;

        trace("PrintedBook: додано сторінки й тип обкладинки");
    }

    // Скорочений конструктор: делегує повному через this(...)
    public PrintedBook(String name, int yearRelease, String author, int recommendedYear, double price,
                       int pages) throws DomainException
    {
        this(name, yearRelease, author, recommendedYear, price, 1, UNKNOWN, UNKNOWN, UNKNOWN, 0.0, pages, false);
        trace("PrintedBook: скорочений конструктор (делегував через this(...))");
    }

    public int getPages() { return pages; }
    public boolean hasHardcover() { return hasHardcover; }

    @Override
    public String getFormatName()
    {
        return "Паперова книга";
    }

    @Override
    public String getSizeInfo()
    {
        return pages + " стор., " + (hasHardcover ? "тверда" : "м'яка") + " обкладинка";
    }

    @Override
    public String toString()
    {
        return super.toString() + String.format(", сторінок: %d, тверда обкладинка: %b", pages, hasHardcover);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!super.equals(other)) return false;
        var that = (PrintedBook) other;
        return pages == that.pages && hasHardcover == that.hasHardcover;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), pages, hasHardcover);
    }
}