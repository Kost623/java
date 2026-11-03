package Service;

import Class.Reader;

import java.util.Comparator;
import java.util.List;

public class InMemoryReaderService extends InMemoryCrudService<Reader> implements ReaderService
{
    public static final Comparator<Reader> NAME_THEN_YEAR =
            Comparator.comparing(Reader::getName, EntityUtils.NAME_ORDER)
                    .thenComparingInt(r -> r.yearOfBirth);

    public InMemoryReaderService()
    {
        super("Читач");
    }

    @Override
    public List<Reader> findAllSortedByNameAndByYearOfBirth()
    {
        return findAllSorted(NAME_THEN_YEAR);
    }
}