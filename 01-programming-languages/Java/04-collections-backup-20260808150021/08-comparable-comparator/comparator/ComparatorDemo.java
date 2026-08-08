import java.util.*;

/**
 * Demonstrates Comparator interface for custom ordering.
 *
 * <p>The Comparator interface defines external comparison logic for objects.
 * It allows multiple sort orders without modifying the original class.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>External comparison — separate from the class being compared</li>
 *   <li>Multiple sort orders for the same class</li>
 *   <li>Comparator.comparing(), .reversed(), .thenComparing()</li>
 *   <li>Static factory methods: nullsFirst, nullsLast</li>
 *   <li>Functional interface — can use lambda expressions</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class ComparatorDemo {

    public static void main(String[] args) {
        demonstrateBasicComparator();
        demonstrateComparatorChaining();
        demonstrateNullHandling();
        demonstrateLambdaComparators();
    }

    /**
     * Demonstrates basic Comparator usage.
     */
    private static void demonstrateBasicComparator() {
        System.out.println("=== Basic Comparator ===");

        List<String> names = new ArrayList<>(List.of("Charlie", "Alice", "Bob"));

        // Sort alphabetically (default)
        names.sort(Comparator.naturalOrder());
        System.out.println("Natural order: " + names);

        // Sort reverse alphabetically
        names.sort(Comparator.reverseOrder());
        System.out.println("Reverse order: " + names);

        // Sort by length
        names.sort(Comparator.comparingInt(String::length));
        System.out.println("By length: " + names);
        System.out.println();
    }

    /**
     * Demonstrates Comparator chaining.
     */
    private static void demonstrateComparatorChaining() {
        System.out.println("=== Comparator Chaining ===");

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Alice", "Engineering", 95000));
        employees.add(new Employee("Bob", "Marketing", 75000));
        employees.add(new Employee("Charlie", "Engineering", 85000));
        employees.add(new Employee("Diana", "Marketing", 80000));

        // Sort by department, then by salary descending
        Comparator<Employee> byDeptThenSalary = Comparator
                .comparing(Employee::getDepartment)
                .thenComparing(Employee::getSalary, Comparator.reverseOrder());

        employees.sort(byDeptThenSalary);
        System.out.println("Sorted by department, then salary desc:");
        for (Employee e : employees) {
            System.out.println("  " + e);
        }
        System.out.println();
    }

    /**
     * Demonstrates null handling with Comparator.
     */
    private static void demonstrateNullHandling() {
        System.out.println("=== Null Handling ===");

        List<String> names = new ArrayList<>(List.of("Charlie", null, "Alice", null, "Bob"));

        // Nulls first
        names.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println("Nulls first: " + names);

        // Nulls last
        names.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        System.out.println("Nulls last: " + names);
        System.out.println();
    }

    /**
     * Demonstrates lambda-based Comparators.
     */
    private static void demonstrateLambdaComparators() {
        System.out.println("=== Lambda Comparators ===");

        List<String> words = new ArrayList<>(List.of("Banana", "apple", "Cherry"));

        // Case-insensitive sort
        words.sort((a, b) -> a.compareToIgnoreCase(b));
        System.out.println("Case-insensitive: " + words);

        // Sort by last character
        words.sort(Comparator.comparing(s -> s.charAt(s.length() - 1)));
        System.out.println("By last char: " + words);

        // Sort by vowel count
        words.sort(Comparator.comparingInt(s -> {
            int count = 0;
            for (char c : s.toLowerCase().toCharArray()) {
                if ("aeiou".indexOf(c) >= 0) count++;
            }
            return count;
        }));
        System.out.println("By vowel count: " + words);
    }

    /**
     * Employee class for demonstration.
     */
    static class Employee {
        private final String name;
        private final String department;
        private final double salary;

        Employee(String name, String department, double salary) {
            this.name = name;
            this.department = department;
            this.salary = salary;
        }

        String getDepartment() { return department; }
        double getSalary() { return salary; }

        @Override
        public String toString() {
            return name + " (" + department + ", $" + salary + ")";
        }
    }
}
