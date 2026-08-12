package academy.javaengineering.collections.set.memory;

import java.util.*;

public class SetMemory {

    public static void main(String[] args) {
        System.out.println("=== Set Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. HashSet vs LinkedHashSet vs TreeSet
        System.out.println("--- Set Implementations Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Set<Integer> hash = new HashSet<>();
        for (int i = 0; i < 10000; i++) hash.add(i);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("HashSet 10K: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Set<Integer> linked = new LinkedHashSet<>();
        for (int i = 0; i < 10000; i++) linked.add(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("LinkedHashSet 10K: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Set<Integer> tree = new TreeSet<>();
        for (int i = 0; i < 10000; i++) tree.add(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("TreeSet 10K: " + (after - before) + " bytes");

        // 2. EnumSet efficiency
        System.out.println("\n--- EnumSet Efficiency ---");
        EnumSet<Size> enumSet = EnumSet.allOf(Size.class);
        System.out.println("EnumSet uses bit vector: ~" + (enumSet.size() / 8 + 1) + " bytes");
        System.out.println("vs HashSet: " + (enumSet.size() * 48) + " bytes approx");

        // 3. Initial capacity impact
        System.out.println("\n--- Initial Capacity Impact ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Set<String> defaultCap = new HashSet<>();
        for (int i = 0; i < 1000; i++) defaultCap.add("item" + i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Default capacity: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Set<String> preallocated = new HashSet<>(2048);
        for (int i = 0; i < 1000; i++) preallocated.add("item" + i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Pre-allocated 2048: " + (after - before) + " bytes");
    }

    enum Size { XS, S, M, L, XL, XXL }
}
