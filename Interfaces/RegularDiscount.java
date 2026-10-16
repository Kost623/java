package Interfaces;

import Abstract.AbstractDiscountStrategy;

public class RegularDiscount extends AbstractDiscountStrategy
{
    @Override
    public double applyDiscount(double price, int yearRelease)
    {
        if (!isValidPrice(price) || !isValidYear(yearRelease))
        {
            return price;
        }
        if (yearRelease <= 2007)
        {
            return price * 0.95;
        }
        return price;
    }
}
