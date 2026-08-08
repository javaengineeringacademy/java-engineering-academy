import java.lang.reflect.Field;
import java.util.*;

/**
 * ArrayList Internals
 * Internal Object[] array, growth strategy, memory overhead.
 */
public class ArrayListInternals {

    public static void main(String[] args) throws Exception {
        System.out.println("=== ArrayList Internals ===\n");

        internalArrayStructure();
        growthStrategy();
        trimToSizeDemo();
        memoryOverhead();
        arrayVsLinkedList();

        System.out.println("\n=== Complete ===");
    }

    // --- Internal Object[] array ---
    static void internalArrayStructure() throws Exception {
        System.out.println("--- Internal Object[] Array ---");

        ArrayList<String> list = new ArrayList<>();

        // Access internal array via reflection
        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        elementDataField.setAccessible(true);

        Object[] array = (Object[]) elementDataField.get(list);
        System.out.println("Initial capacity: " + array.length); // 10 (default)

        // Add elements
        for (int i = 0; i < 5; i++) {
            list.add("Item " + i);
        }

        array = (Object[]) elementDataField.get(list);
        System.out.println("After adding 5 elements:");
        System.out.println("  Size: " + list.size());
        System.out.println("  Array length: " + array.length);

        // Show internal array contents
        System.out.print("  Array contents: [");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] == null ? "null" : array[i]);
            if (i < array.length - 1) System.out.print(", ");
        }
        System.out.println("]");

        System.out.println();
    }

    // --- Growth strategy (1.5x) ---
    static void growthStrategy() throws Exception {
        System.out.println("--- Growth Strategy (1.5x) ---");

        ArrayList<Integer> list = new ArrayList<>();
        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        elementDataField.setAccessible(true);

        int oldCapacity = 0;
        int growCount = 0;

        for (int i = 0; i < 50; i++) {
            list.add(i);
            Object[] array = (Object[]) elementDataField.get(list);

            if (array.length != oldCapacity) {
                System.out.println("Size " + (i + 1) +
                    ": capacity grew from " + oldCapacity +
                    " to " + array.length);
                oldCapacity = array.length;
                growCount++;
            }
        }

        System.out.println("Total growth operations: " + growCount);

        // Manual growth calculation
        System.out.println("\nGrowth formula: newCapacity = oldCapacity + (oldCapacity >> 1)");
        int cap = 10;
        for (int i = 0; i < 6; i++) {
            int newCap = cap + (cap >> 1);
            System.out.println(cap + " -> " + newCap);
            cap = newCap;
        }

        System.out.println();
    }

    // --- trimToSize() ---
    static void trimToSizeDemo() throws Exception {
        System.out.println("--- trimToSize() Impact ---");

        ArrayList<String> list = new ArrayList<>(100);
        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        elementDataField.setAccessible(true);

        Object[] array = (Object[]) elementDataField.get(list);
        System.out.println("Before adding - capacity: " + array.length);

        for (int i = 0; i < 20; i++) {
            list.add("Item " + i);
        }

        array = (Object[]) elementDataField.get(list);
        System.out.println("After adding 20 - capacity: " + array.length);

        list.trimToSize();
        array = (Object[]) elementDataField.get(list);
        System.out.println("After trimToSize - capacity: " + array.length);

        // When to use trimToSize:
        // - When list won't grow further
        // - Memory-sensitive applications
        // - After bulk operations

        System.out.println();
    }

    // --- Memory overhead ---
    static void memoryOverhead() {
        System.out.println("--- Memory Overhead ---");

        // Object header: 12 bytes (compressed oops)
        // Reference: 4 bytes (compressed oops)
        // Integer object: 12 + 4 = 16 bytes

        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        // Approximate memory calculation
        int objectHeader = 16; // ArrayList object header
        int arrayHeader = 16;  // Internal array header
        int elementRefs = 1000 * 4; // References to Integer objects
        int intObjects = 1000 * 16; // Integer objects themselves

        long totalEstimate = objectHeader + arrayHeader + elementRefs + intObjects;
        System.out.println("ArrayList<Integer>(1000) approximate memory:");
        System.out.println("  ArrayList object: " + objectHeader + " bytes");
        System.out.println("  Internal array: " + arrayHeader + " bytes");
        System.out.println("  Element refs: " + elementRefs + " bytes");
        System.out.println("  Integer objects: " + intObjects + " bytes");
        System.out.println("  Total: ~" + totalEstimate + " bytes");

        // vs int[] primitive array
        int[] primitiveArray = new int[1000];
        long primitiveMemory = 16 + (1000 * 4); // header + 4 bytes per int
        System.out.println("\nint[1000] memory: " + primitiveMemory + " bytes");
        System.out.println("Overhead of boxing: " +
            (totalEstimate - primitiveMemory) + " bytes");

        System.out.println();
    }

    // --- ArrayList vs LinkedList ---
    static void arrayVsLinkedList() {
        System.out.println("--- ArrayList vs LinkedList ---");

        int size = 100_000;

        // ArrayList - contiguous array
        ArrayList<Integer> arrayList = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
        }
        long arrayListTime = System.nanoTime() - start;

        // LinkedList - node-based
        LinkedList<Integer> linkedList = new LinkedList<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            linkedList.add(i);
        }
        long linkedListTime = System.nanoTime() - start;

        System.out.println("Add " + size + " elements:");
        System.out.println("  ArrayList: " + (arrayListTime / 1_000_000) + "ms");
        System.out.println("  LinkedList: " + (linkedListTime / 1_000_000) + "ms");

        // Random access
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            arrayList.get(i);
        }
        long arrayListAccess = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            linkedList.get(i);
        }
        long linkedListAccess = System.nanoTime() - start;

        System.out.println("\nRandom access " + size + " elements:");
        System.out.println("  ArrayList: " + (arrayListAccess / 1_000_000) + "ms");
        System.out.println("  LinkedList: " + (linkedListAccess / 1_000_000) + "ms");

        System.out.println("\nUse ArrayList when:");
        System.out.println("  - Random access needed");
        System.out.println("  - Iterating frequently");
        System.out.println("  - Memory matters");

        System.out.println("\nUse LinkedList when:");
        System.out.println("  - Frequent insert/delete at head/tail");
        System.out.println("  - Used as Queue/Deque");
        System.out.println("  - Random access not needed");
    }
}
