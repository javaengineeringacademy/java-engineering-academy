package academy.javaengineering.generics;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenericMethodTest {

    @Test
    void testPrintArray() {
        Integer[] arr = {1, 2, 3};
        assertDoesNotThrow(() -> GenericMethods.printArray(arr));
    }

    @Test
    void testSwap() {
        String[] arr = {"A", "B", "C"};
        GenericMethods.swap(arr, 0, 2);
        assertArrayEquals(new String[]{"C", "B", "A"}, arr);
    }

    @Test
    void testSwapInvalidIndices() {
        String[] arr = {"A", "B"};
        assertThrows(IllegalArgumentException.class, () -> {
            GenericMethods.swap(arr, 0, 5);
        });
    }

    @Test
    void testFindMaxInteger() {
        Integer[] arr = {5, 2, 8, 1, 9};
        assertEquals(9, GenericMethods.findMax(arr));
    }

    @Test
    void testFindMaxString() {
        String[] arr = {"Apple", "Banana", "Cherry"};
        assertEquals("Cherry", GenericMethods.findMax(arr));
    }

    @Test
    void testFindMin() {
        Integer[] arr = {5, 2, 8, 1, 9};
        assertEquals(1, GenericMethods.findMin(arr));
    }

    @Test
    void testAsList() {
        List<String> list = GenericMethods.asList("a", "b", "c");
        assertEquals(3, list.size());
    }

    @Test
    void testCountOccurrences() {
        Integer[] arr = {1, 2, 3, 2, 4, 2};
        assertEquals(3, GenericMethods.countOccurrences(arr, 2));
    }

    @Test
    void testConcatenate() {
        Integer[] first = {1, 2};
        Integer[] second = {3, 4};
        Integer[] result = GenericMethods.concatenate(first, second);
        assertArrayEquals(new Integer[]{1, 2, 3, 4}, result);
    }
}
