package Exceptions;

/*Перевірений виняток: сутність з указаним id відсутня в сервісі.*/
public class EntityNotFoundException extends Exception
{
    private final long id;

    public EntityNotFoundException(String entityName, long id)
    {
        super(entityName + " з id=" + id + " не знайдено");
        this.id = id;
    }

    public long getId()
    {
        return id;
    }
}