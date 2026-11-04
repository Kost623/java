
import Class.BaseEntity;
import Class.Book;
import Class.PrintedBook;
import Class.Reader;
import Exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BaseEntity: id, name, equals/hashCode за id")
class BaseEntityTest
{
    @Test
    @DisplayName("гетери повертають id та name")
    void gettersReturnValues()
    {
        TestEntity entity = new TestEntity(42, "Назва");

        assertEquals(42, entity.getId());
        assertEquals("Назва", entity.getName());
    }

    @Test
    @DisplayName("поле id оголошено final, сеттера id немає")
    void idIsImmutable() throws NoSuchFieldException
    {
        assertTrue(Modifier.isFinal(BaseEntity.class.getDeclaredField("id").getModifiers()));
        assertThrows(NoSuchMethodException.class, () -> BaseEntity.class.getMethod("setId", long.class));
    }

    @Test
    @DisplayName("name не може бути null")
    void nameCannotBeNull()
    {
        assertThrows(NullPointerException.class, () -> new TestEntity(1, null));
    }

    @Test
    @DisplayName("BaseEntity абстрактний")
    void baseEntityIsAbstract()
    {
        assertTrue(Modifier.isAbstract(BaseEntity.class.getModifiers()));
    }

    @Test
    @DisplayName("Book і Reader успадковують BaseEntity та отримують різні id")
    void entitiesGetUniqueIds() throws DomainException
    {
        Book book = TestData.printedBook("Кобзар", 100.0);
        Reader reader1 = TestData.reader("Катерина", 2003);
        Reader reader2 = TestData.reader("Катерина", 2003);

        assertInstanceOf(BaseEntity.class, book);
        assertInstanceOf(BaseEntity.class, reader1);
        assertEquals("Кобзар", book.getName());
        assertEquals("Катерина", reader1.getName());
        assertNotEquals(reader1.getId(), reader2.getId());
        assertNotEquals(book.getId(), reader1.getId());
    }

    @Test
    @DisplayName("equals: однаковий id і клас дає рівність, навіть якщо назви різні")
    void equalsBySameIdAndClass()
    {
        TestEntity a = new TestEntity(5, "A");
        TestEntity b = new TestEntity(5, "B");

        assertEquals(a, b);
        assertEquals(b, a);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("equals: різний id дає нерівність")
    void notEqualForDifferentId()
    {
        assertNotEquals(new TestEntity(5, "A"), new TestEntity(6, "A"));
    }

    @Test
    @DisplayName("equals: однаковий id у різних класів не робить об'єкти рівними")
    void notEqualForDifferentClassWithSameId()
    {
        TestEntity a = new TestEntity(5, "A");
        OtherTestEntity b = new OtherTestEntity(5, "A");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    @DisplayName("equals: рефлексивність, null і чужий тип")
    void equalsReflexiveAndNull()
    {
        TestEntity a = new TestEntity(5, "A");

        assertEquals(a, a);
        assertNotEquals(null, a);
        assertNotEquals("рядок", a);
    }

    @Test
    @DisplayName("Reader успадковує рівність за id: однакові дані, але різні id означають різні об'єкти")
    void readerEqualityIsById()
    {
        Reader a = TestData.reader("Олена", 2000);
        Reader b = TestData.reader("Олена", 2000);

        assertNotEquals(a, b);
        assertEquals(a, a);
    }

    @Test
    @DisplayName("ієрархія Book зберігає значеннєвий equals з ЛР6 (id в ньому не бере участі)")
    void bookKeepsValueEquality() throws DomainException
    {
        PrintedBook a = new PrintedBook("Кобзар", 1990, "Т. Шевченко", 12, 250.0, 5,
                "978-966-03-1111-1", "Поезія", "Дніпро", 4.8, 320, true);
        PrintedBook b = new PrintedBook("Кобзар", 1990, "Т. Шевченко", 12, 250.0, 5,
                "978-966-03-1111-1", "Поезія", "Дніпро", 4.8, 320, true);

        assertNotEquals(a.getId(), b.getId());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}