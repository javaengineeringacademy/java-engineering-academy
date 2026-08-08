package academy.javaengineering.collections.examples;

import java.util.*;
import java.util.stream.*;

public class CombinedExamples {
    public static void main(String[] args) {
        System.out.println("=== Combined Examples ===\n");

        // Real-world: Employee processing
        System.out.println("--- Employee Processing ---");
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "Engineering", 95000),
            new Employee("Bob", "Engineering", 87000),
            new Employee("Charlie", "HR", 72000),
            new Employee("David", "HR", 78000),
            new Employee("Eve", "Marketing", 82000),
            new Employee("Frank", "Marketing", 79000)
        );

        // Group by department
        Map<String, List<Employee>> byDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment));
        System.out.println("By Department: " + byDept);

        // Average salary by department
        Map<String, Double> avgSalary = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.averagingDouble(Employee::getSalary)
            ));
        System.out.println("Avg Salary: " + avgSalary);

        // Top earner
        Employee topEarner = employees.stream()
            .max(Comparator.comparingDouble(Employee::getSalary))
            .orElse(null);
        System.out.println("Top Earner: " + topEarner);

        // Real-world: Word frequency
        System.out.println("\n--- Word Frequency ---");
        String text = "the quick brown fox jumps over the lazy dog the fox";
        Map<String, Long> wordFreq = Arrays.stream(text.split("\\s+"))
            .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        System.out.println("Word frequency: " + wordFreq);

        // Find most common
        String mostCommon = wordFreq.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("None");
        System.out.println("Most common: " + mostCommon);

        // Real-world: Data transformation pipeline
        System.out.println("\n--- Data Pipeline ---");
        List<String> raw = Arrays.asList("  Java  ", " PYTHON ", " c++ ", "JavaScript  ");
        List<String> processed = raw.stream()
            .map(String::trim)
            .map(String::toUpperCase)
            .filter(s -> s.length() > 3)
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Processed: " + processed);

        // Real-world: Map manipulation
        System.out.println("\n--- Map Manipulation ---");
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("apples", 50);
        inventory.put("bananas", 30);
        inventory.put("oranges", 25);

        // Filter items > 30
        Map<String, Integer> filtered = inventory.entrySet().stream()
            .filter(e -> e.getValue() > 30)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("Items > 30: " + filtered);

        // Sort by value
        Map<String, Integer> sorted = inventory.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
        System.out.println("Sorted by value: " + sorted);
    }

    static class Employee {
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
}
