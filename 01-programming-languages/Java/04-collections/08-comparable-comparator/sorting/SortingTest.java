import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class SortingTest {

    @Test
    void testCollectionsSortNaturalOrder() {
        List<String> list = new ArrayList<>(List.of("C", "A", "B"));
        Collections.sort(list);
        assertEquals(List.of("A", "B", "C"), list);
    }

    @Test
    void testCollectionsSortCustomComparator() {
        List<String> list = new ArrayList<>(List.of("Banana", "Apple", "Cherry"));
        list.sort(Comparator.comparingInt(String::length));
        assertEquals(List.of("Apple", "Banana", "Cherry"), list);
    }

    @Test
    void testArraysSortPrimitives() {
        int[] arr = {5, 2, 8, 1, 9};
        Arrays.sort(arr);
        assertArrayEquals(new int[]{1, 2, 5, 8, 9}, arr);
    }

    @Test
    void testArraysSortObjects() {
        String[] arr = {"Banana", "Apple", "Cherry"};
        Arrays.sort(arr);
        assertArrayEquals(new String[]{"Apple", "Banana", "Cherry"}, arr);
    }

    @Test
    void testArraysSortPartial() {
        int[] arr = {5, 2, 8, 1, 9, 3};
        Arrays.sort(arr, 1, 4);
        assertArrayEquals(new int[]{5, 1, 2, 8, 9, 3}, arr);
    }

    @Test
    void testStableSortPreservesOrder() {
        record Item(String name, int group) {}
        List<Item> items = new ArrayList<>();
        items.add(new Item("A1", 2));
        items.add(new Item("B1", 1));
        items.add(new Item("A2", 2));

        items.sort(Comparator.comparingInt(Item::group));

        assertEquals("B1", items.get(0).name());
        assertEquals("A1", items.get(1).name());
        assertEquals("A2", items.get(2).name());
    }

    @Test
    void testSortEmptyList() {
        List<String> list = new ArrayList<>();
        Collections.sort(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void testSortSingleElement() {
        List<Integer> list = new ArrayList<>(List.of(42));
        Collections.sort(list);
        assertEquals(List.of(42), list);
    }
}
