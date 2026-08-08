package collections.comparable.exercises;

import java.util.*;

/**
 * COMPARABLE & COMPARATOR EXERCISES
 *
 * Complete each TODO. Run tests to verify your solutions.
 */
public class exercises {

    // =========================================================================
    // EXERCISE 1: Comparable — Student Rank by GPA
    // =========================================================================
    /**
     * Create a Student class that implements Comparable<Student>.
     * Students are ranked by GPA (descending). If GPAs are equal,
     * rank alphabetically by name (ascending).
     *
     * Given a list of students, sort them and return as a list.
     *
     * TODO: Implement the Student class and this method
     */
    public static class Student implements Comparable<Student> {
        private String name;
        private double gpa;

        public Student(String name, double gpa) {
            this.name = name;
            this.gpa = gpa;
        }

        public String getName() { return name; }
        public double getGpa() { return gpa; }

        @Override
        public int compareTo(Student other) {
            // TODO: Your code here
            return 0;
        }

        @Override
        public String toString() {
            return name + "(" + gpa + ")";
        }
    }

    public static List<Student> rankStudents(List<Student> students) {
        List<Student> sorted = new ArrayList<>(students);
        Collections.sort(sorted);
        return sorted;
    }

    // =========================================================================
    // EXERCISE 2: Comparator — Sort by Multiple Fields
    // =========================================================================
    /**
     * Given a list of Employee objects (name, department, salary),
     * sort first by department (ascending), then by salary (descending)
     * within each department. Use Comparator.comparing and .reversed().
     *
     * TODO: Implement this method
     */
    public static class Employee {
        private String name;
        private String department;
        private double salary;

        public Employee(String name, String department, double salary) {
            this.name = name;
            this.department = department;
            this.salary = salary;
        }

        public String getName() { return name; }
        public String getDepartment() { return department; }
        public double getSalary() { return salary; }

        @Override
        public String toString() {
            return name + "(" + department + ", $" + salary + ")";
        }
    }

    public static List<Employee> sortEmployees(List<Employee> employees) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 3: Comparator — Custom String Length Sort
    // =========================================================================
    /**
     * Given a list of strings, sort them by length (shortest first).
     * If lengths are equal, sort alphabetically. Use Comparator chaining.
     *
     * TODO: Implement this method
     */
    public static List<String> sortByLengthThenAlpha(List<String> words) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 4: Comparable — Price Comparison for Products
    // =========================================================================
    /**
     * Create a Product class implementing Comparable<Product>.
     * Products are compared by price (ascending). If prices are equal,
     * compare by name alphabetically.
     *
     * TODO: Implement the Product class and this method
     */
    public static class Product implements Comparable<Product> {
        private String name;
        private double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }

        @Override
        public int compareTo(Product other) {
            // TODO: Your code here
            return 0;
        }

        @Override
        public String toString() {
            return name + "($" + price + ")";
        }
    }

    public static List<Product> sortProducts(List<Product> products) {
        List<Product> sorted = new ArrayList<>(products);
        Collections.sort(sorted);
        return sorted;
    }

    // =========================================================================
    // EXERCISE 5: Comparator — Nulls Last Sorting
    // =========================================================================
    /**
     * Given a list of strings that may contain null values, sort them
     * using Comparator.nullsLast() so nulls appear at the end.
     * Non-null strings should be sorted alphabetically.
     *
     * TODO: Implement this method
     */
    public static List<String> sortWithNullsLast(List<String> items) {
        // TODO: Your code here
        return null;
    }
}
