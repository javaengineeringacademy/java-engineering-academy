package academy.javaengineering.oop.internals;

import java.util.*;

public class SortingInternals {

    static class Student implements Comparable<Student> {
        String name;
        double gpa;

        Student(String name, double gpa) {
            this.name = name;
            this.gpa = gpa;
        }

        @Override
        public int compareTo(Student other) {
            return Double.compare(this.gpa, other.gpa);
        }

        @Override
        public String toString() {
            return name + " (GPA: " + gpa + ")";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Sorting Internals ===\n");

        // 1. Comparable Interface
        System.out.println("--- Comparable ---");
        List<Student> students = new ArrayList<>(Arrays.asList(
            new Student("Alice", 3.8),
            new Student("Bob", 3.5),
            new Student("Charlie", 3.9)
        ));
        Collections.sort(students);
        System.out.println("Natural order (by GPA): " + students);

        // 2. Comparator Interface
        System.out.println("\n--- Comparator ---");
        students.sort((s1, s2) -> s1.name.compareTo(s2.name));
        System.out.println("By name: " + students);

        // 3. Custom Sorting
        System.out.println("\n--- Custom Sorting ---");
        students.sort(Comparator.comparingDouble((Student s) -> s.gpa).reversed());
        System.out.println("By GPA descending: " + students);
    }
}
