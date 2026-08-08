import java.lang.reflect.Field;
import java.util.*;

/**
 * ArrayList Internals Test
 * Tests internal array structure, growth strategy, and memory overhead.
 */
public class ArrayListInternalsTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("=== ArrayList Internals Test ===\n");

        testInitialCapacity();
        testGrowthStrategy();
        testTrimToSize();
        testSizeMatchesListSize();
        testNullHandling();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    private static void testInitialCapacity() throws Exception {
        ArrayList<String> list = new ArrayList<>();
        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        elementDataField.setAccessible(true);
        Object[] array = (Object[]) elementDataField.get(list);
        assertEquals(10, array.length, "Initial capacity should be 10");
    }

    private static void testGrowthStrategy() throws Exception {
        ArrayList<Integer> list = new ArrayList<>();
        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        elementDataField.setAccessible(true);

        for (int i = 0; i < 11; i++) list.add(i);
        Object[] array = (Object[]) elementDataField.get(list);
        assertEquals(15, array.length, "Capacity after 11 elements should be 15 (10 + 10>>1)");
    }

    private static void testTrimToSize() throws Exception {
        ArrayList<String> list = new ArrayList<>(100);
        for (int i = 0; i < 10; i++) list.add("item");
        list.trimToSize();

        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        elementDataField.setAccessible(true);
        Object[] array = (Object[]) elementDataField.get(list);
        assertEquals(10, array.length, "After trimToSize capacity should equal size");
    }

    private static void testSizeMatchesListSize() throws Exception {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) list.add("item" + i);

        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        elementDataField.setAccessible(true);
        Object[] array = (Object[]) elementDataField.get(list);

        assertTrue(array.length >= list.size(), "Array capacity should be >= list size");
    }

    private static void testNullHandling() {
        ArrayList<String> list = new ArrayList<>();
        list.add(null);
        list.add("test");
        list.add(null);
        assertEquals(3, list.size(), "List should contain nulls");
        assertEquals(null, list.get(0), "First element should be null");
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (Objects.equals(expected, actual)) {
            System.out.println("PASS: " + message);
            passed++;
        } else {
            System.out.println("FAIL: " + message + " (expected=" + expected + ", actual=" + actual + ")");
            failed++;
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("PASS: " + message);
            passed++;
        } else {
            System.out.println("FAIL: " + message);
            failed++;
        }
    }
}
