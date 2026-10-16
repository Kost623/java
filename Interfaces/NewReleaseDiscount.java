package Interfaces;

import Abstract.AbstractDiscountStrategy;

public class NewReleaseDiscount extends AbstractDiscountStrategy
{
    @Override
    public double applyDiscount(double price, int yearRelease)
    {
        if (!isValidPrice(price) || !isValidYear(yearRelease))
        {
            return price;
        }
        if (yearRelease > 2007)
        {
            return price * 0.90;
        }
        return price;
    }
}
