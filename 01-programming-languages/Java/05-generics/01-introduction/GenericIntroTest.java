import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenericIntroTest {

    @Test
    void testMaximumInteger() {
        assertEquals(30, GenericIntro.maximum(10, 20, 30));
    }

    @Test
    void testMaximumString() {
        assertEquals("Cherry", GenericIntro.maximum("Apple", "Banana", "Cherry"));
    }

    @Test
    void testMaximumSameValues() {
        assertEquals(5, GenericIntro.maximum(5, 5, 5));
    }

    @Test
    void testPrintArray() {
        Integer[] array = {1, 2, 3, 4, 5};
        assertDoesNotThrow(() -> GenericIntro.printArray(array));
    }

    @Test
    void testCountOccurrences() {
        Integer[] array = {1, 2, 3, 2, 4, 2};
        assertEquals(3, GenericIntro.countOccurrences(array, 2));
    }

    @Test
    void testCountOccurrencesNotFound() {
        Integer[] array = {1, 2, 3};
        assertEquals(0, GenericIntro.countOccurrences(array, 5));
    }

    @Test
    void testMaximumDouble() {
        assertEquals(3.14, GenericIntro.maximum(1.0, 2.5, 3.14));
    }

    @Test
    void testGenericMethodTypeSafety() {
        Integer result = GenericIntro.maximum(1, 2, 3);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }
}
