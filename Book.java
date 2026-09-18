public class Book 
{
    String name;
    int year_release;
    String author;
    int recommended_year;
    double price;

    public Book(String name, int year_release, String author, int recommended_year, double price) 
    {
        this.name = name;
        this.year_release = year_release;
        this.author = author;
        this.recommended_year=recommended_year;
        this.price = price;
    }
    @Override
    public String toString()
    {
        return String.format("Книга: \"%s\", рік видання: %d, Автор: \"%s\",рекомандований вік: %d, ціна: %.2f грн",
                name, year_release, author, recommended_year, price);
    }
    public void applyDiscount()
    {
        if (year_release <= 2007)
        {
            price = price * 1.05;
        }
    }
    public boolean equalsBook(Book other)
    {
        return this.name.equals(other.name)
                && this.year_release == other.year_release
                && this.author.equals(other.author)
                && this.recommended_year == other.recommended_year
                && this.price == other.price;
    }
}
