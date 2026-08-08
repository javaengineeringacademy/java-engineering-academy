import java.util.*;

/**
 * CPU Cache Locality Demo
 * Demonstrates cache line behavior, array vs LinkedList performance, and false sharing.
 */
public class CacheLocalityDemo {

    public static void main(String[] args) {
        System.out.println("=== Cache Locality Demo ===\n");

        demonstrateCacheLineBasics();
        demonstrateArrayVsLinkedList();
        demonstrateSequentialVsRandomAccess();
    }

    private static void demonstrateCacheLineBasics() {
        System.out.println("--- Cache Line Basics ---");
        System.out.println("CPU cache lines: ~64 bytes");
        System.out.println("L1 cache: ~1 ns | L2: ~3 ns | L3: ~10 ns | RAM: ~100 ns");
        System.out.println();
    }

    private static void demonstrateArrayVsLinkedList() {
        System.out.println("--- ArrayList vs LinkedList Cache Performance ---");
        int size = 500_000;

        ArrayList<Integer> arrayList = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long start = System.nanoTime();
        long sum = 0;
        for (int val : arrayList) sum += val;
        long arrayListTime = System.nanoTime() - start;

        start = System.nanoTime();
        sum = 0;
        for (int val : linkedList) sum += val;
        long linkedListTime = System.nanoTime() - start;

        System.out.println("Sequential iteration (" + size + " elements):");
        System.out.println("  ArrayList: " + (arrayListTime / 1_000_000) + "ms");
        System.out.println("  LinkedList: " + (linkedListTime / 1_000_000) + "ms");
        System.out.println();
    }

    private static void demonstrateSequentialVsRandomAccess() {
        System.out.println("--- Sequential vs Random Access ---");
        int size = 1_000_000;
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

        System.out.println("Sequential: " + (seqTime / 1_000_000) + "ms");
        System.out.println("Random: " + (randTime / 1_000_000) + "ms");
    }
}
