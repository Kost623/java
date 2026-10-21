package Observer;

/*Спостерігач, який виводить події у консоль.*/
public class ConsoleLoggerObserver implements LibraryObserver
{
    private final String name;

    public ConsoleLoggerObserver(String name)
    {
        this.name = name;
    }

    @Override
    public void update(String event, Object source)
    {
        System.out.println("[ConsoleLogger: " + name + "] Подія: " + event + " | Джерело: " + source);
    }
}
