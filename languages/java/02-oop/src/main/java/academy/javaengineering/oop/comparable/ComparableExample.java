package academy.javaengineering.oop.comparable;

import java.util.*;

/**
 * Demonstrates Comparable interface - natural ordering.
 * Comparable defines the natural ordering of objects.
 * Used with Collections.sort() and Arrays.sort().
 */
public class ComparableExample {

    // Implementing Comparable for natural ordering
    static class Student implements Comparable<Student> {
        private String name;
        private double gpa;

        public Student(String name, double gpa) {
            this.name = name;
            this.gpa = gpa;
        }

        public String getName() { return name; }
        public double getGpa() { return gpa; }

        // Natural ordering by GPA (descending)
        @Override
        public int compareTo(Student other) {
            return Double.compare(other.gpa, this.gpa); // Descending
        }

        @Override
        public String toString() {
            return name + " (GPA: " + gpa + ")";
        }
    }

    // Natural ordering by name (ascending)
    static class Employee implements Comparable<Employee> {
        private String name;
        private int age;
        private double salary;

        public Employee(String name, int age, double salary) {
            this.name = name;
            this.age = age;
            this.salary = salary;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
        public double getSalary() { return salary; }

        // Natural ordering by name
        @Override
        public int compareTo(Employee other) {
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name + " (Age: " + age + ", Salary: $" + salary + ")";
        }
    }

    public static void main(String[] args) {
        // Comparable Example - Natural Ordering
        System.out.println("=== Comparable Interface ===");
        
        List<Student> students = new ArrayList<>(Arrays.asList(
            new Student("Alice", 3.8),
            new Student("Bob", 3.5),
            new Student("Charlie", 3.9),
            new Student("Diana", 3.7)
        ));

        System.out.println("Before sorting: " + students);
        Collections.sort(students); // Uses compareTo()
        System.out.println("After sorting (by GPA desc): " + students);

        // Employee natural ordering by name
        List<Employee> employees = new ArrayList<>(Arrays.asList(
            new Employee("Charlie", 30, 75000),
            new Employee("Alice", 25, 65000),
            new Employee("Bob", 28, 70000),
            new Employee("Diana", 35, 85000)
        ));

        System.out.println("\nBefore sorting: " + employees);
        Collections.sort(employees); // Uses compareTo()
        System.out.println("After sorting (by name): " + employees);
    }
}
