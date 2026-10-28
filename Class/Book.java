package Class;

import Exceptions.DomainException;
import Exceptions.InvalidPriceException;
import Exceptions.InvalidAgeException;

/*Клас книги з доданими полями isbn, жанру, видавця та рейтингу.*/
public class Book
{
    public String name;
    public int yearRelease;
    public String author;
    public int recommendedYear;
    public double price;
    public String status;
    public int number;
    public String isbn;
    public String genre;
    public String publisher;
    public double rating;

    public Book(String name, int yearRelease, String author, int recommendedYear, double price, int number,
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
    }

    private String updateStatus()
    {
        return number > 0 ? "В наявності" : "Закінчився";
    }

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

    @Override
    public String toString()
    {
        return String.format("Книга: \"%s\", рік видання: %d, Автор: \"%s\", рекомендований вік: %d, ціна: %.2f грн, кількість: %d, статус: %s, ISBN: \"%s\", жанр: \"%s\", видавець: \"%s\", рейтинг: %.1f",
                name, yearRelease, author, recommendedYear, price, number, status, isbn, genre, publisher, rating);
    }

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
                && this.price == book.price
                && this.number == book.number
                && this.status.equals(book.status)
                && this.isbn.equals(book.isbn)
                && this.genre.equals(book.genre)
                && this.publisher.equals(book.publisher)
                && this.rating == book.rating;
    }
}
