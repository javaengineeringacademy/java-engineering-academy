import java.lang.reflect.Field;
import java.util.*;

/**
 * ArrayList Internals Demo
 * Demonstrates internal Object[] array, growth strategy, and memory overhead.
 */
public class ArrayListInternalsDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== ArrayList Internals Demo ===\n");

        demonstrateInternalArray();
        demonstrateGrowthStrategy();
        demonstrateTrimToSize();
        demonstrateMemoryOverhead();
    }

    private static void demonstrateInternalArray() throws Exception {
        System.out.println("--- Internal Object[] Array ---");
        ArrayList<String> list = new ArrayList<>();
        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        elementDataField.setAccessible(true);

        Object[] array = (Object[]) elementDataField.get(list);
        System.out.println("Initial capacity: " + array.length);

        for (int i = 0; i < 5; i++) {
            list.add("Item " + i);
        }

        array = (Object[]) elementDataField.get(list);
        System.out.println("After 5 elements - size: " + list.size() + ", capacity: " + array.length);
        System.out.println();
    }

    private static void demonstrateGrowthStrategy() throws Exception {
        System.out.println("--- Growth Strategy (1.5x) ---");
        ArrayList<Integer> list = new ArrayList<>();
        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        elementDataField.setAccessible(true);

        int oldCapacity = 0;
        for (int i = 0; i < 50; i++) {
            list.add(i);
            Object[] array = (Object[]) elementDataField.get(list);
            if (array.length != oldCapacity) {
                System.out.println("Size " + (i + 1) + ": capacity " + oldCapacity + " -> " + array.length);
                oldCapacity = array.length;
            }
        }
        System.out.println();
    }

    private static void demonstrateTrimToSize() throws Exception {
        System.out.println("--- trimToSize() ---");
        ArrayList<String> list = new ArrayList<>(100);
        for (int i = 0; i < 20; i++) list.add("Item " + i);

        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        elementDataField.setAccessible(true);

        Object[] array = (Object[]) elementDataField.get(list);
        System.out.println("Before trim - capacity: " + array.length);

        list.trimToSize();
        array = (Object[]) elementDataField.get(list);
        System.out.println("After trim - capacity: " + array.length);
        System.out.println();
    }

    private static void demonstrateMemoryOverhead() {
        System.out.println("--- Memory Overhead ---");
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) list.add(i);

        int totalEstimate = 16 + 16 + 1000 * 4 + 1000 * 16;
        System.out.println("ArrayList<Integer>(1000): ~" + totalEstimate + " bytes");
        System.out.println("int[1000]: " + (16 + 1000 * 4) + " bytes");
    }
}
