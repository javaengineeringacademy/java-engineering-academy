import java.util.*;

/**
 * Cache Locality Test
 * Tests cache performance characteristics of different collection types.
 */
public class CacheLocalityTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Cache Locality Test ===\n");

        testArrayListSequentialAccess();
        testLinkedListSequentialAccess();
        testRandomAccessSlower();
        testPrimitiveArrayFaster();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    private static void testArrayListSequentialAccess() {
        int size = 100_000;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) list.add(i);

        long start = System.nanoTime();
        long sum = 0;
        for (int val : list) sum += val;
        long elapsed = System.nanoTime() - start;

        assertEquals((long) size * (size - 1) / 2, sum, "ArrayList sequential sum correct");
        assertTrue(elapsed < 1_000_000_000L, "ArrayList sequential access under 1s");
    }

    private static void testLinkedListSequentialAccess() {
        int size = 100_000;
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < size; i++) list.add(i);

        long sum = 0;
        for (int val : list) sum += val;
        assertEquals((long) size * (size - 1) / 2, sum, "LinkedList sequential sum correct");
    }

    private static void testRandomAccessSlower() {
        int size = 100_000;
        int[] array = new int[size];
        for (int i = 0; i < size; i++) array[i] = i;

        long start = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < size; i++) sum += array[i];
        long seqTime = System.nanoTime() - start;

        Random random = new Random(42);
        int[] indices = new int[size];
        for (int i = 0; i < size; i++) indices[i] = random.nextInt(size);

        start = System.nanoTime();
        sum = 0;
        for (int i = 0; i < size; i++) sum += array[indices[i]];
        long randTime = System.nanoTime() - start;

        assertTrue(randTime > seqTime, "Random access should be slower than sequential");
    }

    private static void testPrimitiveArrayFaster() {
        int size = 100_000;
        int[] primitiveArray = new int[size];
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            primitiveArray[i] = i;
            arrayList.add(i);
        }

        long start = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < size; i++) sum += primitiveArray[i];
        long primitiveTime = System.nanoTime() - start;

        start = System.nanoTime();
        sum = 0;
        for (int val : arrayList) sum += val;
        long boxedTime = System.nanoTime() - start;

        assertTrue(primitiveTime < boxedTime, "Primitive array should be faster than ArrayList<Integer>");
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
