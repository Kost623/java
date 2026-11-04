package Service;

import Class.BaseEntity;
import Exceptions.EntityNotFoundException;

import java.util.Comparator;
import java.util.List;

/*Контракт типізованого CRUD-сервісу. Обмеження T extends BaseEntity гарантує,
що кожна сутність має getId() і getName(): без нього сервіс не зміг би її ідентифікувати.*/
public interface CRUDService<T extends BaseEntity>
{
    T findById(long id) throws EntityNotFoundException;

    T save(T entity);

    boolean delete(long id);

    // Нова відсортована колекція; сховище не змінюється.
    // Comparator<? super T>: компаратор лише ПРИЙМАЄ елементи T, тому підійде й Comparator<BaseEntity>.
    List<T> findAllSorted(Comparator<? super T> comparator);

    // Назва є в кожного T (BaseEntity), тому метод можна реалізувати прямо в інтерфейсі
    default List<T> findAllSortedByName()
    {
        return findAllSorted(Comparator.comparing(BaseEntity::getName, EntityUtils.NAME_ORDER));
    }
}