import java.lang.reflect.Field;
import java.util.*;

/**
 * HashMap Internals Test
 * Tests internal table structure, hash function, and load factor behavior.
 */
public class HashMapInternalsTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("=== HashMap Internals Test ===\n");

        testInitialTableIsNull();
        testTableGrowsOnPut();
        testCapacityIsPowerOf2();
        testDefaultLoadFactor();
        testCollisionHandling();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    private static void testInitialTableIsNull() throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        Field tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);
        Object[] table = (Object[]) tableField.get(map);
        assertEquals(null, table, "Initial table should be null (lazy initialization)");
    }

    private static void testTableGrowsOnPut() throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        Field tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);

        for (int i = 0; i < 12; i++) map.put("key" + i, i);
        Object[] table = (Object[]) tableField.get(map);
        assertTrue(table != null && table.length >= 16, "Table should grow after exceeding threshold");
    }

    private static void testCapacityIsPowerOf2() throws Exception {
        HashMap<String, Integer> map = new HashMap<>(30);
        Field tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);
        Object[] table = (Object[]) tableField.get(map);
        int length = table.length;
        assertTrue(length > 0 && (length & (length - 1)) == 0, "Capacity should be power of 2");
    }

    private static void testDefaultLoadFactor() throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        Field loadFactorField = HashMap.class.getDeclaredField("loadFactor");
        loadFactorField.setAccessible(true);
        float loadFactor = (float) loadFactorField.get(map);
        assertEquals(0.75f, loadFactor, "Default load factor should be 0.75");
    }

    private static void testCollisionHandling() {
        HashMap<Integer, String> map = new HashMap<>(4);
        for (int i = 0; i < 10; i++) map.put(i, "val" + i);
        assertEquals(10, map.size(), "All entries should be stored despite collisions");
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
