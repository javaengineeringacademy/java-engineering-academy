package academy.javaengineering.modern;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Demonstrates records with collections and stream operations.
 */
public class RecordsWithCollections {

    public record Employee(String name, String department, double salary) {}

    public static void main(String[] args) {
        List<Employee> employees = List.of(
            new Employee("Alice", "Engineering", 95000),
            new Employee("Bob", "Marketing", 72000),
            new Employee("Charlie", "Engineering", 105000),
            new Employee("Diana", "HR", 68000),
            new Employee("Eve", "Engineering", 112000)
        );

        // Filter and collect using records
        List<String> engineeringNames = employees.stream()
            .filter(e -> e.department().equals("Engineering"))
            .map(Employee::name)
            .collect(Collectors.toList());

        System.out.println("Engineering employees: " + engineeringNames);

        // Group by department using record accessor
        var grouped = employees.stream()
            .collect(Collectors.groupingBy(Employee::department));
        System.out.println("\nGrouped by department:");
        grouped.forEach((dept, emps) -> 
            System.out.println("  " + dept + ": " + emps.size()));

        // Calculate average salary using record fields
        double avgSalary = employees.stream()
            .mapToDouble(Employee::salary)
            .average()
            .orElse(0);
        System.out.println("\nAverage salary: $" + avgSalary);

        // Find highest paid using record comparison
        Employee highest = employees.stream()
            .max((e1, e2) -> Double.compare(e1.salary(), e2.salary()))
            .orElse(null);
        System.out.println("Highest paid: " + highest);
    }
}
