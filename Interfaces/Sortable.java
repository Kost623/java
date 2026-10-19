package Interfaces;

/*Другий інтерфейс — поведінка сортування.
Клас може реалізувати кілька інтерфейсів одночасно,
наприклад окремо бізнес-поведінку (знижку) і окремо сортування.*/
public interface Sortable
{
    /*Повертає ключ, за яким буде відбуватися сортування.*/
    double getSortKey();

    /*Метод за замовчуванням для порівняння двох об'єктів за ключем сортування.*/
    default int compareByKey(Sortable other)
    {
        return Double.compare(this.getSortKey(), other.getSortKey());
    }
}
