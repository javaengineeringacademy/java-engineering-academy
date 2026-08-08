import java.util.*;
import java.util.stream.*;

/**
 * Iterator Internals
 * Implementation details in ArrayList, LinkedList, HashMap.
 * fail-fast vs fail-safe, Spliterator.
 */
public class IteratorInternals {

    public static void main(String[] args) {
        System.out.println("=== Iterator Internals ===\n");

        iteratorInArrayList();
        iteratorInLinkedList();
        iteratorInHashMap();
        failFastVsFailSafe();
        spliteratorDemo();

        System.out.println("\n=== Complete ===");
    }

    // --- Iterator in ArrayList ---
    static void iteratorInArrayList() {
        System.out.println("--- Iterator in ArrayList ---");

        ArrayList<String> list = new ArrayList<>(List.of("a", "b", "c", "d", "e"));

        System.out.println("ArrayList Iterator structure:");
        System.out.println("  - int cursor (next element index)");
        System.out.println("  - int lastRet (last returned index, -1 if none)");
        System.out.println("  - int expectedModCount (for fail-fast)");
        System.out.println("  - ArrayList outerRef");

        System.out.println("\nIteration pattern:");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String elem = it.next();
            System.out.print(elem + " ");
        }
        System.out.println();

        // Concurrent modification
        System.out.println("\nConcurrent modification detection:");
        try {
            Iterator<String> it2 = list.iterator();
            while (it2.hasNext()) {
                it2.next();
                list.add("f"); // Concurrent modification!
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("  Caught: " + e.getClass().getSimpleName());
        }

        System.out.println();
    }

    // --- Iterator in LinkedList ---
    static void iteratorInLinkedList() {
        System.out.println("--- Iterator in LinkedList ---");

        LinkedList<Integer> list = new LinkedList<>(List.of(1, 2, 3, 4, 5));

        System.out.println("LinkedList Iterator structure:");
        System.out.println("  - Node current (next node to return)");
        System.out.println("  - Node lastReturned (last returned node)");
        System.out.println("  - int nextIndex");
        System.out.println("  - int expectedModCount");

        System.out.println("\nNode structure:");
        System.out.println("  - Node prev");
        System.out.println("  - Node next");
        System.out.println("  - E item");

        System.out.println("\nIteration:");
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        // remove() during iteration
        System.out.println("\nRemove during iteration:");
        Iterator<Integer> it2 = list.iterator();
        while (it2.hasNext()) {
            int val = it2.next();
            if (val % 2 == 0) {
                it2.remove(); // Safe removal
            }
        }
        System.out.println("After removing evens: " + list);

        System.out.println();
    }

    // --- Iterator in HashMap ---
    static void iteratorInHashMap() {
        System.out.println("--- Iterator in HashMap ---");

        HashMap<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        System.out.println("HashMap Iterator structure:");
        System.out.println("  - int index (current bucket index)");
        System.out.println("  - Node current (current node in bucket)");
        System.out.println("  - Node next (next node to return)");
        System.out.println("  - int expectedModCount");

        System.out.println("\nIteration order: unpredictable (hash-based)");
        System.out.println("  - Iterate over entrySet(), keySet(), or values()");

        System.out.println("\nEntry iteration:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        // ListIterator (bidirectional)
        System.out.println("\nListIterator (ArrayList only):");
        ArrayList<String> arrayList = new ArrayList<>(List.of("a", "b", "c"));
        ListIterator<String> listIt = arrayList.listIterator();

        System.out.println("Forward:");
        while (listIt.hasNext()) {
            System.out.print(listIt.nextIndex() + ":" + listIt.next() + " ");
        }
        System.out.println();

        System.out.println("Backward:");
        while (listIt.hasPrevious()) {
            System.out.print(listIt.previousIndex() + ":" + listIt.previous() + " ");
        }
        System.out.println();

        System.out.println();
    }

    // --- fail-fast vs fail-safe ---
    static void failFastVsFailSafe() {
        System.out.println("--- fail-fast vs fail-safe ---");

        System.out.println("fail-fast iterators (ArrayList, LinkedList, HashMap):");
        System.out.println("  - Throw ConcurrentModificationException");
        System.out.println("  - Check modCount before each operation");
        System.out.println("  - Detect structural modification");

        System.out.println("\nfail-safe iterators (CopyOnWriteArrayList, ConcurrentHashMap):");
        System.out.println("  - Never throw ConcurrentModificationException");
        System.out.println("  - Iterate over snapshot or thread-safe copy");
        System.out.println("  - May miss concurrent modifications");

        // Demo: CopyOnWriteArrayList
        System.out.println("\nCopyOnWriteArrayList example:");
        List<String> cowList = new java.util.concurrent.CopyOnWriteArrayList<>(
            List.of("a", "b", "c"));

        Iterator<String> cowIt = cowList.iterator();
        cowList.add("d"); // No exception!

        System.out.println("  Iterator sees: ");
        while (cowIt.hasNext()) {
            System.out.print(cowIt.next() + " ");
        }
        System.out.println();
        System.out.println("  List now: " + cowList);

        // ConcurrentHashMap
        System.out.println("\nConcurrentHashMap:");
        Map<String, Integer> chm = new java.util.concurrent.ConcurrentHashMap<>();
        chm.put("a", 1);
        chm.put("b", 2);

        Iterator<Map.Entry<String, Integer>> chmIt = chm.entrySet().iterator();
        chm.put("c", 3); // No exception

        System.out.println("  Iterator sees: ");
        while (chmIt.hasNext()) {
            System.out.print(chmIt.next() + " ");
        }
        System.out.println();

        System.out.println();
    }

    // --- Spliterator ---
    static void spliteratorDemo() {
        System.out.println("--- Spliterator (Parallel Iteration) ---");

        System.out.println("Spliterator interface methods:");
        System.out.println("  - tryAdvance(Consumer): Process next element");
        System.out.println("  - trySplit(): Split for parallel processing");
        System.out.println("  - estimateSize(): Estimated remaining elements");
        System.out.println("  - characteristics(): Spliterator traits");

        System.out.println("\nSpliterator characteristics:");
        System.out.println("  - ORDERED: Elements have defined order");
        System.out.println("  - DISTINCT: No duplicate elements");
        System.out.println("  - SORTED: Elements sorted");
        System.out.println("  - SIZED: Exact size known");
        System.out.println("  - NONNULL: No null elements");
        System.out.println("  - IMMUTABLE: No structural modifications");
        System.out.println("  - CONCURRENT: Concurrent modification safe");
        System.out.println("  - SUBSIZED: Split results are sized");

        // Demo
        System.out.println("\nParallel stream processing:");
        List<Integer> list = IntStream.rangeClosed(1, 100).boxed().toList();

        long start = System.nanoTime();
        int parallelSum = list.parallelStream().mapToInt(Integer::intValue).sum();
        long parallelTime = System.nanoTime() - start;

        start = System.nanoTime();
        int sequentialSum = list.stream().mapToInt(Integer::intValue).sum();
        long sequentialTime = System.nanoTime() - start;

        System.out.println("  Parallel sum: " + parallelSum +
            " (" + (parallelTime / 1_000_000) + "ms)");
        System.out.println("  Sequential sum: " + sequentialSum +
            " (" + (sequentialTime / 1_000_000) + "ms)");

        // Custom Spliterator
        System.out.println("\nCustom Spliterator example:");
        Spliterator<Integer> spliterator = list.spliterator();
        System.out.println("  Characteristics: " + spliterator.characteristics());
        System.out.println("  Estimated size: " + spliterator.estimateSize());

        // Split
        Spliterator<Integer> half1 = spliterator.trySplit();
        if (half1 != null) {
            System.out.println("  After split:");
            System.out.println("    First half size: " + half1.estimateSize());
            System.out.println("    Second half size: " + spliterator.estimateSize());
        }

        System.out.println("\n=== Complete ===");
    }
}
