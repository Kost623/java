public class Book 
{
    String name;
    int yearRelease;
    String author;
    int recommendedYear;
    double price;

    public Book(String name, int yearRelease, String author, int recommendedYear, double price) 
    {
        this.name = name;
        this.yearRelease = yearRelease;
        this.author = author;
        this.recommendedYear=recommendedYear;
        this.price = price;
    }

    @Override
    public String toString()
    {
        return String.format("Книга: \"%s\", рік видання: %d, Автор: \"%s\",рекомандований вік: %d, ціна: %.2f грн",
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
        var book = (Book)other;
        return this.name.equals(book.name)
                && this.yearRelease == book.yearRelease
                && this.author.equals(book.author)
                && this.recommendedYear == book.recommendedYear
                && this.price == book.price;
    }
}
