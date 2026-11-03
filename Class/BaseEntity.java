package Class;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/*Базовий клас усіх сутностей, якими керує CRUD-сервіс: ідентифікатор і назва.*/
public abstract class BaseEntity
{
    private static final AtomicLong ID_SEQUENCE = new AtomicLong(0);

    private final long id;
    private String name;

    protected BaseEntity(long id, String name)
    {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name не може бути null");
    }

    // Наступний вільний id: сутності отримують унікальний id автоматично під час створення
    protected static long nextId()
    {
        return ID_SEQUENCE.incrementAndGet();
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    // Рівність сутностей за id: однаковий id (і однаковий клас) означає той самий запис
    @Override
    public boolean equals(Object other)
    {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        return id == ((BaseEntity) other).id;
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(id);
    }
}