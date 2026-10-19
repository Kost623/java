package Context;

import Interfaces.DiscountStrategy;

/*Контекстний клас патерну Strategy.
Приймає інтерфейс-стратегію через конструктор, дозволяє змінювати стратегію
під час виконання програми через сетер і делегує їй виконання поведінки.*/
public class DiscountContext
{
    private DiscountStrategy strategy;

    public DiscountContext(DiscountStrategy strategy)
    {
        this.strategy = strategy;
    }

    /*Сеттер для зміни стратегії під час виконання програми (runtime).*/
    public void setStrategy(DiscountStrategy strategy)
    {
        this.strategy = strategy;
    }

    /*Геттер для отримання поточної стратегії.*/
    public DiscountStrategy getStrategy()
    {
        return strategy;
    }

    /*Делегує обчислення знижки поточній стратегії.*/
    public double executeDiscount(double price, int yearRelease)
    {
        return strategy.applyDiscount(price, yearRelease);
    }
}
