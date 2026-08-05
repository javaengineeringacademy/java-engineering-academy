package academy.javaengineering.patterns.behavioral.iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IteratorTest {

    private BookCollection collection;

    @BeforeEach
    void setUp() {
        collection = new BookCollection();
        collection.addBook("Book1");
        collection.addBook("Book2");
        collection.addBook("Book3");
    }

    @Test
    void iteratorShouldTraverseAllElements() {
        Iterator<String> iterator = collection.createIterator();
        assertTrue(iterator.hasNext());
        assertEquals("Book1", iterator.next());
        assertEquals("Book2", iterator.next());
        assertEquals("Book3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void iteratorShouldReset() {
        Iterator<String> iterator = collection.createIterator();
        iterator.next();
        iterator.next();
        iterator.reset();
        assertEquals("Book1", iterator.next());
    }

    @Test
    void collectionShouldReturnCorrectSize() {
        assertEquals(3, collection.size());
    }

    @Test
    void iteratorShouldThrowWhenNoMoreElements() {
        Iterator<String> iterator = collection.createIterator();
        iterator.next();
        iterator.next();
        iterator.next();
        assertThrows(java.util.NoSuchElementException.class, iterator::next);
    }

    @Test
    void iteratorShouldReturnCorrectPosition() {
        BookIterator iterator = (BookIterator) collection.createIterator();
        assertEquals(0, iterator.getPosition());
        iterator.next();
        assertEquals(1, iterator.getPosition());
    }
}
