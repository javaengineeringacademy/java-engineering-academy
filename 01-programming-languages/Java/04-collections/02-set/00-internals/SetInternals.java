package academy.javaengineering.collections.set.internals;

import java.util.*;
import java.util.stream.*;

public class SetInternals {

    public static void main(String[] args) {
        System.out.println("=== Set Interface Internals ===\n");

        // 1. HashSet internal HashMap
        System.out.println("--- HashSet Uses HashMap ---");
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("Java");
        hashSet.add("Python");
        hashSet.add("Java"); // Duplicate ignored
        System.out.println("HashSet internally uses HashMap");
        System.out.println("Elements: " + hashSet);
        System.out.println("Size: " + hashSet.size() + " (duplicates removed)");

        // 2. LinkedHashSet preserves insertion order
        System.out.println("\n--- LinkedHashSet Order ---");
        LinkedHashSet<String> linked = new LinkedHashSet<>();
        linked.add("C");
        linked.add("A");
        linked.add("B");
        System.out.println("Insertion order preserved: " + linked);

        // 3. TreeSet sorted order
        System.out.println("\n--- TreeSet Sorted ---");
        TreeSet<Integer> tree = new TreeSet<>();
        tree.add(5);
        tree.add(1);
        tree.add(3);
        tree.add(2);
        System.out.println("Sorted order: " + tree);
        System.out.println("First: " + tree.first());
        System.out.println("Last: " + tree.last());

        // 4. EnumSet efficient representation
        System.out.println("\n--- EnumSet Efficiency ---");
        EnumSet<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);
        EnumSet<Day> weekdays = EnumSet.range(Day.MONDAY, Day.FRIDAY);
        System.out.println("Weekend: " + weekend);
        System.out.println("Weekdays: " + weekdays);

        // 5. Set operations
        System.out.println("\n--- Set Operations ---");
        Set<Integer> setA = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> setB = new HashSet<>(Arrays.asList(3, 4, 5, 6));

        Set<Integer> union = new HashSet<>(setA);
        union.addAll(setB);
        System.out.println("Union: " + union);

        Set<Integer> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);
        System.out.println("Intersection: " + intersection);

        Set<Integer> diff = new HashSet<>(setA);
        diff.removeAll(setB);
        System.out.println("Difference (A-B): " + diff);

        // 6. CopyOnWriteArraySet
        System.out.println("\n--- CopyOnWriteArraySet ---");
        CopyOnWriteArraySet<String> cowSet = new CopyOnWriteArraySet<>();
        cowSet.add("Thread");
        cowSet.add("Safe");
        cowSet.add("Thread"); // Duplicate ignored
        System.out.println("Thread-safe, no duplicates: " + cowSet);
    }

    enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
}
