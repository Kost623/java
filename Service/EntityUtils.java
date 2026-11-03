package Service;

import Class.BaseEntity;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

/*Утиліти для роботи із сутностями: порядок назв і друк будь-якої колекції сутностей.*/
public final class EntityUtils
{
    // Порівняння назв за правилами української мови (Є, І, Ї на своїх місцях), без урахування регістру
    public static final Comparator<String> NAME_ORDER = createNameOrder();

    private EntityUtils()
    {
    }

    private static Comparator<String> createNameOrder()
    {
        Collator collator = Collator.getInstance(Locale.forLanguageTag("uk-UA"));
        collator.setStrength(Collator.SECONDARY); 
        return collator::compare;
    }

    /*Колекція лише ЧИТАЄТЬСЯ (producer), тому ? extends BaseEntity: підходить Collection<Book>,
    List<Reader>, Set<Reader>, List<BaseEntity>, але додати в неї елемент усередині методу не можна.*/
    public static void printAll(Collection<? extends BaseEntity> items)
    {
        Objects.requireNonNull(items, "items не може бути null");
        for (BaseEntity item : items)
        {
            System.out.printf("   %s: id=%d, назва=\"%s\"%n",
                    item.getClass().getSimpleName(), item.getId(), item.getName());
        }
    }
}