package Observer;

/*Інтерфейс спостерігача (Observer).
Клас, що реалізує цей інтерфейс, отримує сповіщення при зміні стану суб'єкта (наприклад, книги в бібліотеці).*/
public interface LibraryObserver
{
    void update(String event, Object source);
}
