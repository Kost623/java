package test;

import Class.Reader;
import Service.InMemoryReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Узгодженість компаратора з equals (за id)")
class EqualsComparatorConsistencyTest
{
    private Reader a;
    private Reader b;

    @BeforeEach
    void setUp()
    {
        a = TestData.reader("Олена", 2000);
        b = TestData.reader("Олена", 2000);
    }

    @Test
    @DisplayName("різні читачі з однаковими іменем і роком: компаратор дає 0, equals дає false")
    void comparatorIsNotConsistentWithEquals()
    {
        assertEquals(0, InMemoryReaderService.NAME_THEN_YEAR.compare(a, b));
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("HashSet (equals/hashCode за id) зберігає обох")
    void hashSetKeepsBoth()
    {
        Set<Reader> set = new HashSet<>(List.of(a, b));

        assertEquals(2, set.size());
    }

    @Test
    @DisplayName("TreeSet з NAME_THEN_YEAR губить другого читача")
    void treeSetWithInconsistentComparatorLosesElement()
    {
        Set<Reader> set = new TreeSet<>(InMemoryReaderService.NAME_THEN_YEAR);
        set.add(a);
        set.add(b);

        assertEquals(1, set.size());
    }

    @Test
    @DisplayName("додавання id останнім ключем робить порядок узгодженим з equals")
    void idTieBreakMakesComparatorConsistent()
    {
        Comparator<Reader> consistent = InMemoryReaderService.NAME_THEN_YEAR.thenComparingLong(Reader::getId);
        Set<Reader> set = new TreeSet<>(consistent);
        set.add(a);
        set.add(b);

        assertEquals(2, set.size());
        assertEquals(0, consistent.compare(a, a));
        assertNotEquals(0, consistent.compare(a, b));
    }

    @Test
    @DisplayName("той самий об'єкт: equals true, компаратор 0")
    void sameInstanceIsConsistent()
    {
        assertEquals(a, a);
        assertEquals(0, InMemoryReaderService.NAME_THEN_YEAR.compare(a, a));
    }
}