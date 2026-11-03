package Service;

import Class.Book;

import java.util.Comparator;
import java.util.List;

public class InMemoryBookService extends InMemoryCrudService<Book> implements BookService
{
    // Компаратор, а не Comparable: порядок «назва, потім ціна» потрібен лише цьому сервісу
    public static final Comparator<Book> NAME_THEN_PRICE =
            Comparator.comparing(Book::getName, EntityUtils.NAME_ORDER)
                    .thenComparingDouble(Book::getPrice);

    public InMemoryBookService()
    {
        super("Книга");
    }

    @Override
    public List<Book> findAllSortedByNameAndByPrice()
    {
        return findAllSorted(NAME_THEN_PRICE);
    }
}