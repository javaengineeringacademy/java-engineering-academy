package academy.javaengineering.generics;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoundedTypeTest {

    @Test
    void testSumWithIntegers() {
        List<Integer> integers = List.of(1, 2, 3, 4, 5);
        assertEquals(15.0, BoundedTypeDemo.sum(integers));
    }

    @Test
    void testSumWithDoubles() {
        List<Double> doubles = List.of(1.5, 2.5, 3.0);
        assertEquals(7.0, BoundedTypeDemo.sum(doubles));
    }

    @Test
    void testFindMaxInteger() {
        List<Integer> list = List.of(5, 2, 8, 1, 9);
        assertEquals(9, BoundedTypeDemo.findMax(list));
    }

    @Test
    void testFindMaxString() {
        List<String> list = List.of("Apple", "Banana", "Cherry");
        assertEquals("Cherry", BoundedTypeDemo.findMax(list));
    }

    @Test
    void testFindMaxEmptyList() {
        assertThrows(IllegalArgumentException.class, () -> {
            BoundedTypeDemo.findMax(List.of());
        });
    }

    @Test
    void testFindMaxValue() {
        List<Double> list = List.of(2.5, 8.3, 4.1, 9.7);
        assertEquals(9.7, BoundedTypeDemo.findMaxValue(list));
    }

    @Test
    void testFilterGreaterThan() {
        List<Integer> list = List.of(1, 5, 10, 15, 20);
        List<Integer> result = BoundedTypeDemo.filterGreaterThan(list, 10);
        assertEquals(List.of(15, 20), result);
    }

    @Test
    void testSumEmptyList() {
        List<Integer> empty = List.of();
        assertEquals(0.0, BoundedTypeDemo.sum(empty));
    }
}
