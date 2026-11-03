package Service;

import Class.BaseEntity;
import Exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*Узагальнений in-memory сервіс: той самий код працює для будь-якої сутності BaseEntity.
Сховище: LinkedHashMap<Long, T>, бо потрібен пошук за id за O(1), а save з наявним id
має замінювати запис (а не додавати другий); LinkedHashMap ще й зберігає порядок вставки.*/
public class InMemoryCrudService<T extends BaseEntity> implements CRUDService<T>
{
    private final String entityName; // лише для інформативних повідомлень про помилки
    private final Map<Long, T> storage = new LinkedHashMap<>();

    public InMemoryCrudService(String entityName)
    {
        this.entityName = entityName;
    }

    @Override
    public T findById(long id) throws EntityNotFoundException
    {
        T entity = storage.get(id);
        if (entity == null)
        {
            throw new EntityNotFoundException(entityName, id);
        }
        return entity;
    }

    // Новий id додає запис, наявний id замінює його: кількість записів при оновленні не зростає
    @Override
    public T save(T entity)
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("Сутність не може бути null");
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    // true, якщо запис існував і був видалений; false, якщо такого id не було
    @Override
    public boolean delete(long id)
    {
        return storage.remove(id) != null;
    }

    // Нова колекція: зміна її складу чи порядку не торкається сховища
    public List<T> findAll()
    {
        return new ArrayList<>(storage.values());
    }

    // Єдина точка сортування: усі інші методи сортування викликають саме її.
    // List.sort стабільний: сутності, рівні за компаратором, зберігають порядок вставки.
    @Override
    public List<T> findAllSorted(Comparator<? super T> comparator)
    {
        List<T> copy = findAll();
        copy.sort(comparator);
        return copy;
    }

    public int size()
    {
        return storage.size();
    }
}