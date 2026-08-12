package academy.javaengineering.collections.list.memory;

import java.util.*;

public class ListMemory {

    public static void main(String[] args) {
        System.out.println("=== List Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();
        rt.gc(); // Suggest GC

        // 1. ArrayList Capacity vs Size
        System.out.println("--- ArrayList Capacity vs Size ---");
        ArrayList<Integer> list = new ArrayList<>();
        long before = rt.totalMemory() - rt.freeMemory();
        for (int i = 0; i < 1000; i++) list.add(i);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("1000 Integers: " + (after - before) + " bytes");
        System.out.println("Internal array capacity: " + getCapacity(list));

        // 2. LinkedList overhead per element
        System.out.println("\n--- LinkedList Overhead ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        LinkedList<Integer> linked = new LinkedList<>();
        for (int i = 0; i < 1000; i++) linked.add(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("1000 Integers LinkedList: " + (after - before) + " bytes");
        System.out.println("Overhead: ~40 bytes per node (prev + next + item)");

        // 3. Initial capacity optimization
        System.out.println("\n--- Initial Capacity Optimization ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        ArrayList<Integer> optimized = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) optimized.add(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Pre-allocated 1000: " + (after - before) + " bytes");

        // 4. trimToSize() memory savings
        System.out.println("\n--- trimToSize() Savings ---");
        ArrayList<String> large = new ArrayList<>(10000);
        for (int i = 0; i < 100; i++) large.add("item");
        System.out.println("Before trimToSize(): capacity=" + getCapacity(large));
        large.trimToSize();
        System.out.println("After trimToSize(): capacity=" + getCapacity(large));

        // 5. Integer boxing memory
        System.out.println("\n--- Integer Boxing Memory ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        List<Integer> boxed = new ArrayList<>();
        for (int i = 0; i < 10000; i++) boxed.add(Integer.valueOf(i));
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("10K boxed Integers: " + (after - before) + " bytes");
        System.out.println("Each Integer object: ~16 bytes");
    }

    private static int getCapacity(ArrayList<?> list) {
        try {
            java.lang.reflect.Field field = ArrayList.class.getDeclaredField("elementData");
            field.setAccessible(true);
            Object[] elementData = (Object[]) field.get(list);
            return elementData.length;
        } catch (Exception e) {
            return -1;
        }
    }
}
