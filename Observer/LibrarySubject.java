package Observer;

import java.util.ArrayList;
import java.util.List;

/*Клас-суб'єкт (Subject).
Зберігає список підписників та сповіщає їх при зміні стану об'єкта (наприклад, зміна кількості, ціни або статусу книги).*/
public class LibrarySubject
{
    private final List<LibraryObserver> observers = new ArrayList<>();

    public void subscribe(LibraryObserver observer)
    {
        if (observer != null && !observers.contains(observer))
        {
            observers.add(observer);
        }
    }

    public void unsubscribe(LibraryObserver observer)
    {
        observers.remove(observer);
    }

    public void notifyObservers(String event, Object source)
    {
        for (LibraryObserver observer : observers)
        {
            observer.update(event, source);
        }
    }
}
