import java.util.*;

/**
 * Sequenced Collections (Java 21)
 *
 * SequencedCollection, SequencedSet, and SequencedMap provide uniform
 * API for accessing elements at both ends of a collection.
 *
 * New methods:
 * - getFirst() / getLast() - access两端 elements
 * - addFirst() / addLast() - add to两端
 * - removeFirst() / removeLast() - remove from两端
 * - reversed() - returns reversed view
 * - getFirst() replaces get(0), getLast() replaces get(size()-1)
 *
 * Expected output:
 * === SequencedCollection (ArrayList) ===
 * First: Alice, Last: Charlie
 * After addFirst: [Zara, Alice, Bob, Charlie]
 * After addLast: [Zara, Alice, Bob, Charlie, Dana]
 * Reversed: [Dana, Charlie, Bob, Alice, Zara]
 *
 * === SequencedSet (LinkedHashSet) ===
 * First: 1, Last: 5
 * Reversed: [5, 4, 3, 2, 1]
 *
 * === SequencedMap (LinkedHashMap) ===
 * First: a=1, Last: c=3
 * Reversed: {c=3, b=2, a=1}
 */
public class SequencedCollectionsDemo {

    public static void main(String[] args) {
        sequencedCollection();
        sequencedSet();
        sequencedMap();
        beforeVsAfter();
    }

    // =========================================================
    // 1. SEQUENCED COLLECTION (ArrayList)
    // =========================================================
    static void sequencedCollection() {
        System.out.println("=== SequencedCollection (ArrayList) ===\n");

        // --- Before Java 21: manual两端 access ---
        // List<String> list = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));
        // String first = list.get(0);           // Risk of IndexOutOfBoundsException
        // String last = list.get(list.size()-1); // Verbose
        // list.add(0, "Zara");                   // addFirst - verbose
        // list.add(list.size(), "Dana");          // addLast - verbose

        // --- With Java 21: SequencedCollection API ---
        SequencedCollection<String> list = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));

        // getFirst() / getLast()
        System.out.println("First: " + list.getFirst());
        System.out.println("Last: " + list.getLast());

        // addFirst() / addLast()
        list.addFirst("Zara");
        list.addLast("Dana");
        System.out.println("After addFirst/Last: " + list);

        // removeFirst() / removeLast()
        String removed = list.removeFirst();
        System.out.println("Removed first: " + removed);
        System.out.println("After removeFirst: " + list);

        // reversed() - returns a view, not a copy
        SequencedCollection<String> reversed = list.reversed();
        System.out.println("Reversed: " + reversed);
        System.out.println("Original unchanged: " + list);

        // Stream operations on reversed
        System.out.print("Reversed stream: ");
        list.reversed().stream()
                .limit(2)
                .forEach(s -> System.out.print(s + " "));
        System.out.println();

        System.out.println();
    }

    // =========================================================
    // 2. SEQUENCED SET (LinkedHashSet)
    // =========================================================
    static void sequencedSet() {
        System.out.println("=== SequencedSet (LinkedHashSet) ===\n");

        // --- Before Java 21: manual两端 access for sets ---
        // Set<Integer> set = new LinkedHashSet<>(List.of(1, 2, 3, 4, 5));
        // Integer first = set.iterator().next(); // Verbose
        // No direct way to get last element

        // --- With Java 21: SequencedSet API ---
        SequencedSet<Integer> set = new LinkedHashSet<>(List.of(1, 2, 3, 4, 5));

        // getFirst() / getLast()
        System.out.println("First: " + set.getFirst());
        System.out.println("Last: " + set.getLast());

        // addFirst() / addLast()
        set.addFirst(0);
        set.addLast(6);
        System.out.println("After addFirst/Last: " + set);

        // reversed() - returns SequencedSet
        SequencedSet<Integer> reversed = set.reversed();
        System.out.println("Reversed: " + reversed);

        // removeFirst() / removeLast()
        set.removeFirst();
        set.removeLast();
        System.out.println("After removeFirst/Last: " + set);

        System.out.println();
    }

    // =========================================================
    // 3. SEQUENCED MAP (LinkedHashMap)
    // =========================================================
    static void sequencedMap() {
        System.out.println("=== SequencedMap (LinkedHashMap) ===\n");

        // --- Before Java 21: manual两端 access for maps ---
        // Map<String, Integer> map = new LinkedHashMap<>();
        // map.put("a", 1); map.put("b", 2); map.put("c", 3);
        // Map.Entry<String, Integer> first = map.entrySet().iterator().next(); // Verbose
        // No direct way to get last entry

        // --- With Java 21: SequencedMap API ---
        SequencedMap<String, Integer> map = new LinkedHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        // firstEntry() / lastEntry()
        Map.Entry<String, Integer> first = map.firstEntry();
        Map.Entry<String, Integer> last = map.lastEntry();
        System.out.println("First: " + first);
        System.out.println("Last: " + last);

        // putFirst() / putLast()
        map.putFirst("z", 26);
        map.putLast("d", 4);
        System.out.println("After putFirst/Last: " + map);

        // reversed() - returns SequencedMap
        SequencedMap<String, Integer> reversed = map.reversed();
        System.out.println("Reversed: " + reversed);

        // pollFirstEntry() / pollLastEntry()
        Map.Entry<String, Integer> polled = map.pollFirstEntry();
        System.out.println("Polled first: " + polled);
        System.out.println("After pollFirst: " + map);

        System.out.println();
    }

    // =========================================================
    // 4. BEFORE VS AFTER COMPARISON
    // =========================================================
    static void beforeVsAfter() {
        System.out.println("=== Before Java 21 vs With Java 21 ===\n");

        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));

        // Before Java 21
        System.out.println("--- Before Java 21 ---");
        String firstOld = list.get(0);
        String lastOld = list.get(list.size() - 1);
        System.out.println("First: " + firstOld + ", Last: " + lastOld);

        list.add(0, "Z");
        System.out.println("After add(0, Z): " + list);

        list.remove(list.size() - 1);
        System.out.println("After remove last: " + list);

        List<String> reversedOld = new ArrayList<>(list);
        Collections.reverse(reversedOld);
        System.out.println("Reversed (copy): " + reversedOld);

        // With Java 21
        System.out.println("\n--- With Java 21 ---");
        SequencedCollection<String> seqList = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        System.out.println("First: " + seqList.getFirst() + ", Last: " + seqList.getLast());

        seqList.addFirst("Z");
        System.out.println("After addFirst(Z): " + seqList);

        seqList.removeLast();
        System.out.println("After removeLast: " + seqList);

        SequencedCollection<String> reversedNew = seqList.reversed();
        System.out.println("Reversed (view): " + reversedNew);

        System.out.println("\nKey benefits:");
        System.out.println("  - More readable code");
        System.out.println("  - Safer (no index out of bounds)");
        System.out.println("  - reversed() returns a view, not a copy (memory efficient)");
        System.out.println("  - Consistent API across List, Set, and Map");
        System.out.println();
    }
}
