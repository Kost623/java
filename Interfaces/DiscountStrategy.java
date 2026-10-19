package Interfaces;

public interface DiscountStrategy 
{
    /*Обчислює нову ціну книги після застосування знижки.*/
    double applyDiscount(double price, int yearRelease);

    /*Метод за замовчуванням. Успадковується усіма реалізаціями, але може бути перевизначений там, де це потрібно.*/
    default String getDescription()
    {
        return "Стандартна стратегія знижки";
    }
}
