public class InvalidAgeException extends DomainException
{
    private int invalidValue;

    public InvalidAgeException(String message, int invalidValue)
    {
        super(message);
        this.invalidValue = invalidValue;
    }

    public int getInvalidValue()
    {
        return invalidValue;
    }
}