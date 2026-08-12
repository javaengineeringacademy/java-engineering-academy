package academy.javaengineering.generics.generic-types.exercises;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for generic class exercises including Box and Pair implementations.
 *
 * <p>Complexity: O(1) for most operations</p>
 * <p>Thread-safety: Not thread-safe (JUnit test class)</p>
 * <p>Key characteristics: Tests Box and Pair generic classes with equals, hashCode, and content operations</p>
 */
class GenericClassTest {

    @Test
    void testBoxEmpty() {
        Box<String> box = new Box<>();
        assertTrue(box.isEmpty());
        assertNull(box.getContent());
    }

    @Test
    void testBoxWithContent() {
        Box<Integer> box = new Box<>(42);
        assertFalse(box.isEmpty());
        assertEquals(42, box.getContent());
    }

    @Test
    void testBoxSetContent() {
        Box<String> box = new Box<>();
        box.setContent("Hello");
        assertEquals("Hello", box.getContent());
    }

    @Test
    void testBoxEquals() {
        Box<String> box1 = new Box<>("Same");
        Box<String> box2 = new Box<>("Same");
        Box<String> box3 = new Box<>("Different");
        assertEquals(box1, box2);
        assertNotEquals(box1, box3);
    }

    @Test
    void testPair() {
        Pair<String, Integer> pair = new Pair<>("age", 30);
        assertEquals("age", pair.getKey());
        assertEquals(30, pair.getValue());
    }

    @Test
    void testPairOf() {
        Pair<String, Boolean> pair = Pair.of("active", true);
        assertEquals("active", pair.getKey());
        assertTrue(pair.getValue());
    }

    @Test
    void testPairEquals() {
        Pair<String, Integer> p1 = new Pair<>("a", 1);
        Pair<String, Integer> p2 = new Pair<>("a", 1);
        assertEquals(p1, p2);
    }
}
