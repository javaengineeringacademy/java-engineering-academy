package academy.javaengineering.collections.examples;

import java.util.*;

public class SetExamples {
    public static void main(String[] args) {
        System.out.println("=== Set Examples ===\n");

        // HashSet - unordered, no duplicates
        System.out.println("--- HashSet ---");
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("Java");
        hashSet.add("Python");
        hashSet.add("Java"); // duplicate ignored
        hashSet.add("C++");
        System.out.println("Set: " + hashSet);
        System.out.println("Contains Java: " + hashSet.contains("Java"));

        // Set operations
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(4, 5, 6, 7, 8));

        // Union
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("\nSet1: " + set1);
        System.out.println("Set2: " + set2);
        System.out.println("Union: " + union);

        // Intersection
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);

        // Difference
        Set<Integer> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("Difference: " + difference);

        // LinkedHashSet - maintains insertion order
        System.out.println("\n--- LinkedHashSet ---");
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("First");
        linkedHashSet.add("Second");
        linkedHashSet.add("Third");
        linkedHashSet.add("First"); // duplicate ignored
        System.out.println("Insertion order maintained: " + linkedHashSet);

        // TreeSet - sorted
        System.out.println("\n--- TreeSet ---");
        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(5);
        treeSet.add(1);
        treeSet.add(3);
        treeSet.add(2);
        treeSet.add(4);
        System.out.println("Sorted: " + treeSet);
        System.out.println("First: " + treeSet.first());
        System.out.println("Last: " + treeSet.last());
        System.out.println("Lower(3): " + treeSet.lower(3));
        System.out.println("Higher(3): " + treeSet.higher(3));

        // EnumSet
        System.out.println("\n--- EnumSet ---");
        Set<Day> weekdays = EnumSet.range(Day.MONDAY, Day.FRIDAY);
        System.out.println("Weekdays: " + weekdays);
        Set<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);
        System.out.println("Weekend: " + weekend);
    }

    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}
