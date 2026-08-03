package academy.javaengineering.oop.comparison;

import java.util.*;

/**
 * Demonstrates Comparable vs Comparator comparison and advanced usage.
 */
public class ComparableVsComparator {

    // Domain object implementing Comparable
    static class Employee implements Comparable<Employee> {
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

        // Natural ordering: by name
        @Override
        public int compareTo(Employee other) {
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return String.format("%s [%s] $%.0f", name, department, salary);
        }
    }

    // External Comparators for different orderings
    static class EmployeeComparators {
        // By salary (descending)
        public static final Comparator<Employee> BY_SALARY_DESC = 
            Comparator.comparingDouble(Employee::getSalary).reversed();

        // By department, then by name
        public static final Comparator<Employee> BY_DEPT_AND_NAME = 
            Comparator.comparing(Employee::getDepartment)
                      .thenComparing(Employee::getName);

        // By salary range
        public static Comparator<Employee> bySalaryRange(double min, double max) {
            return Comparator.comparingDouble(Employee::getSalary)
                            .filter(e -> e.getSalary() >= min && e.getSalary() <= max);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Comparable vs Comparator ===");
        System.out.println("\nComparable: Natural ordering (single, inside class)");
        System.out.println("Comparator: Custom ordering (multiple, external)\n");

        List<Employee> employees = new ArrayList<>(Arrays.asList(
            new Employee("Alice", "Engineering", 95000),
            new Employee("Bob", "Marketing", 75000),
            new Employee("Charlie", "Engineering", 105000),
            new Employee("Diana", "Marketing", 85000),
            new Employee("Eve", "Engineering", 88000)
        ));

        // Natural ordering (Comparable)
        System.out.println("--- Natural Ordering (Comparable - by name) ---");
        Collections.sort(employees);
        employees.forEach(System.out::println);

        // Custom ordering (Comparator)
        System.out.println("\n--- Custom Ordering (Comparator - by salary desc) ---");
        employees.sort(EmployeeComparators.BY_SALARY_DESC);
        employees.forEach(System.out::println);

        System.out.println("\n--- Custom Ordering (Comparator - by department, then name) ---");
        employees.sort(EmployeeComparators.BY_DEPT_AND_NAME);
        employees.forEach(System.out::println);

        // Chaining Comparators
        System.out.println("\n--- Chained Comparators ---");
        employees.sort(
            Comparator.comparing(Employee::getDepartment)
                      .thenComparing(Employee::getSalary).reversed()
        );
        employees.forEach(System.out::println);

        // Null-safe Comparator
        System.out.println("\n--- Null-Safe Comparator ---");
        List<String> names = new ArrayList<>(Arrays.asList("Charlie", null, "Alice", null, "Bob"));
        names.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        System.out.println(names);
    }
}
