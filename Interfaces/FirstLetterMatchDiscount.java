package Interfaces;

/*Стратегія: якщо перша буква імені читача співпадає з першою буквою назви книги,
ціна книги піднімається на 50% (price * 1.5).*/
public class FirstLetterMatchDiscount implements DiscountStrategy
{
    private String readerName;

    public FirstLetterMatchDiscount(String readerName)
    {
        this.readerName = readerName;
    }

    @Override
    public double applyDiscount(double price, int yearRelease)
    {
        if (readerName == null || readerName.isEmpty())
        {
            return price;
        }

        /*Ця стратегія використовується для окремої книги, тому перевіряємо збіг букв
        тут. У Main буде викликатися для пари (читач, книга).*/
        return price;
    }

    /*Перевіряє, чи потрібно підвищити ціну для даної книги та читача.*/
    public boolean isMatch(String bookName)
    {
        if (readerName == null || readerName.isEmpty() || bookName == null || bookName.isEmpty())
        {
            return false;
        }
        char firstLetterUser = Character.toUpperCase(readerName.charAt(0));
        char firstLetterBook = Character.toUpperCase(bookName.charAt(0));
        return firstLetterUser == firstLetterBook;
    }

    /*Підвищує ціну на 50%.*/
    public double applyPriceIncrease(double price)
    {
        return price * 1.5;
    }

    @Override
    public String getDescription()
    {
        return "Підвищення ціни на 50%, якщо перша буква імені читача співпадає з першою буквою назви книги";
    }
}
