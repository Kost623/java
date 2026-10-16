package Class;

import Interfaces.DiscountStrategy;
import Interfaces.Sortable;

/*Клас, який реалізує кілька інтерфейсів одночасно:
- DiscountStrategy — бізнес-поведінка (розрахунок знижки)
- Sortable — поведінка сортування
Це демонструє, що Java-клас може наслідувати багато інтерфейсів.*/
public class BookWithSort implements DiscountStrategy, Sortable
{
    String name;
    int yearRelease;
    double price;

    public BookWithSort(String name, int yearRelease, double price)
    {
        this.name = name;
        this.yearRelease = yearRelease;
        this.price = price;
    }

    /*Реалізація DiscountStrategy — звичайна знижка для старих книг.*/
    @Override
    public double applyDiscount(double price, int yearRelease)
    {
        if (yearRelease <= 2007)
        {
            return price * 0.95;
        }
        return price;
    }

    /*Реалізація Sortable — ключ сортування — ціна книги.*/
    @Override
    public double getSortKey()
    {
        return price;
    }

    @Override
    public String toString()
    {
        return String.format("Книга: \"%s\", рік: %d, ціна: %.2f грн", name, yearRelease, price);
    }
}
