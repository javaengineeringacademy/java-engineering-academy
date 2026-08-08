package set.enumset.examples;

import java.util.*;

public class EnumSetExample {

    enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
    enum Color { RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA }

    public static void main(String[] args) {
        example1_BasicEnumSet();
        example2_RangeOfEnums();
        example3_SetOperations();
        example4_EnumSetVsHashSet();
        example5_EnumSetComplement();
    }

    static void example1_BasicEnumSet() {
        System.out.println("=== Example 1: Basic EnumSet ===");
        EnumSet<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);
        System.out.println("Weekend: " + weekend);
        EnumSet<Day> weekdays = EnumSet.range(Day.MONDAY, Day.FRIDAY);
        System.out.println("Weekdays: " + weekdays);
    }

    static void example2_RangeOfEnums() {
        System.out.println("\n=== Example 2: Range of Enums ===");
        EnumSet<Day> firstHalf = EnumSet.range(Day.MONDAY, Day.WEDNESDAY);
        EnumSet<Day> secondHalf = EnumSet.range(Day.THURSDAY, Day.SUNDAY);
        System.out.println("First half: " + firstHalf);
        System.out.println("Second half: " + secondHalf);
    }

    static void example3_SetOperations() {
        System.out.println("\n=== Example 3: Set Operations ===");
        EnumSet<Day> set1 = EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY);
        EnumSet<Day> set2 = EnumSet.of(Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY);
        EnumSet<Day> union = EnumSet.copyOf(set1);
        union.addAll(set2);
        System.out.println("Union: " + union);
        EnumSet<Day> intersection = EnumSet.copyOf(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);
    }

    static void example4_EnumSetVsHashSet() {
        System.out.println("\n=== Example 4: EnumSet Performance ===");
        long start = System.nanoTime();
        EnumSet<Day> enumSet = EnumSet.allOf(Day.class);
        long enumTime = System.nanoTime() - start;

        start = System.nanoTime();
        HashSet<Day> hashSet = new HashSet<>(Arrays.asList(Day.values()));
        long hashTime = System.nanoTime() - start;

        System.out.println("EnumSet creation: " + enumTime + " ns");
        System.out.println("HashSet creation: " + hashTime + " ns");
        System.out.println("EnumSet is much more efficient for enums.");
    }

    static void example5_EnumSetComplement() {
        System.out.println("\n=== Example 5: Complement of EnumSet ===");
        EnumSet<Day> notWeekend = EnumSet.complementOf(EnumSet.of(Day.SATURDAY, Day.SUNDAY));
        System.out.println("Not weekend: " + notWeekend);
    }
}
