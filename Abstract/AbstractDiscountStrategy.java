package Abstract;

import Interfaces.DiscountStrategy;

/*Абстрактний клас, що імплементує інтерфейс частково.
Задає спільну логіку (валідацію ціни та року), але не реалізує саму формулу applyDiscount.
Конкретні класи наслідують його і дописують лише власну формулу знижки.*/
public abstract class AbstractDiscountStrategy implements DiscountStrategy
{
    /*Спільна валідація: ціна не може бути від'ємною.*/
    protected boolean isValidPrice(double price)
    {
        return price >= 0;
    }

    /*Спільна валідація: рік видання має бути у допустимому діапазоні.*/
    protected boolean isValidYear(int yearRelease)
    {
        return yearRelease >= 1450 && yearRelease <= 2026;
    }

    /*Заповнюємо метод за замовчуванням з інтерфейсу — можливо перевизначення.*/
    @Override
    public String getDescription()
    {
        return "Абстрактна стратегія знижки (базова)";
    }
}
