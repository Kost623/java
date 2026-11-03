package test;

import Exceptions.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EntityNotFoundException")
class EntityNotFoundExceptionTest
{
    @Test
    @DisplayName("це перевірений виняток (Exception, але не RuntimeException)")
    void isCheckedException()
    {
        assertTrue(Exception.class.isAssignableFrom(EntityNotFoundException.class));
        assertFalse(RuntimeException.class.isAssignableFrom(EntityNotFoundException.class));
    }

    @Test
    @DisplayName("зберігає id та має інформативне повідомлення")
    void carriesIdAndMessage()
    {
        EntityNotFoundException e = new EntityNotFoundException("Книга", 77);

        assertEquals(77, e.getId());
        assertTrue(e.getMessage().contains("77"));
        assertTrue(e.getMessage().contains("Книга"));
    }
}