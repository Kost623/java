public class Book
{
    String name;
    int yearRelease;
    String author;
    int recommendedYear;
    double price;

    public Book(String name, int yearRelease, String author, int recommendedYear, double price) throws DomainException
    {
        if (price < 0)
        {
            throw new InvalidPriceException("Ціна книги не може бути від'ємною", price);
        }
        if (recommendedYear < 0 || recommendedYear > 100)
        {
            throw new InvalidAgeException("Рекомендований вік поза допустимим діапазоном (0-100)", recommendedYear);
        }

        this.name = name;
        this.yearRelease = yearRelease;
        this.author = author;
        this.recommendedYear = recommendedYear;
        this.price = price;
    }

    @Override
    public String toString()
    {
        return String.format("Книга: \"%s\", рік видання: %d, Автор: \"%s\", рекомендований вік: %d, ціна: %.2f грн",
                name, yearRelease, author, recommendedYear, price);
    }

    public void applyDiscount()
    {
        if (yearRelease <= 2007)
        {
            price = price * 1.05;
        }
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
                && this.price == book.price;
    }
}