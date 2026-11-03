package Factory;

import Interfaces.DiscountStrategy;
import Interfaces.RegularDiscount;
import Interfaces.NewReleaseDiscount;
import Interfaces.FirstLetterMatchDiscount;


public class DiscountFactory
{
    /*Створює об'єкт стратегії знижки за заданним типом (кодом).*/
    public static DiscountStrategy createDiscount(String type)
    {
        if (type == null)
        {
            throw new IllegalArgumentException("Тип знижки не може бути null");
        }

        switch (type.toUpperCase())
        {
            case "REGULAR":
                return new RegularDiscount();
            case "NEW_RELEASE":
                return new NewReleaseDiscount();
            case "FIRST_LETTER":
                return new FirstLetterMatchDiscount(null);
            default:
                throw new IllegalArgumentException("Невідомий тип знижки: " + type);
        }
    }

    /*Створює стратегію збігу першої букви для конкретного читача.*/
    public static FirstLetterMatchDiscount createFirstLetterDiscount(String readerName)
    {
        return new FirstLetterMatchDiscount(readerName);
    }
}
