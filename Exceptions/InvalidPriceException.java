package Exceptions;

public class InvalidPriceException extends DomainException
{
    private double invalidValue;

    public InvalidPriceException(String message, double invalidValue)
    {
        super(message);
        this.invalidValue = invalidValue;
    }

    public double getInvalidValue()
    {
        return invalidValue;
    }
}
