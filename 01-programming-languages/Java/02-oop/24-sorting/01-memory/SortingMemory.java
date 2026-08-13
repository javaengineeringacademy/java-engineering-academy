package academy.javaengineering.oop.memory;

import java.util.*;

public class SortingMemory {

    static class Student implements Comparable<Student> {
        String name;
        double gpa;
        Student(String name, double gpa) { this.name = name; this.gpa = gpa; }
        @Override
        public int compareTo(Student other) { return Double.compare(this.gpa, other.gpa); }
    }

    public static void main(String[] args) {
        System.out.println("=== Sorting Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. In-Place vs Copy Sort
        System.out.println("--- In-Place vs Copy ---");
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) list.add(new Student("S" + i, 3.0 + (i % 10) * 0.1));

        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Collections.sort(list);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("TimSort: " + (after - before) + " bytes");
        System.out.println("Uses temp array of size n/2");

        // 2. Comparator Memory
        System.out.println("\n--- Comparator Memory ---");
        System.out.println("Lambda: ~24 bytes per instance");
        System.out.println("Method reference: shared via invokedynamic");
        System.out.println("No per-element overhead");

        // 3. Stable Sort
        System.out.println("\n--- Stable Sort ---");
        System.out.println("TimSort is stable");
        System.out.println("Preserves order of equal elements");
        System.out.println("No extra memory for stability");
    }
}
