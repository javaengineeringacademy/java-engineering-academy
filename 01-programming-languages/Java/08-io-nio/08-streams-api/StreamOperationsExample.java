import java.util.*;
import java.util.stream.*;

/**
 * Java Streams API - Comprehensive examples of stream operations.
 *
 * <p>This class demonstrates stream creation, intermediate operations,
 * terminal operations, collectors, and parallel processing.</p>
 *
 * @author JavaEngineering Academy
 * @version 1.0
 */
public final class StreamOperationsExample {

    private StreamOperationsExample() {}

    public record Employee(String name, String department,
                           double salary, int years) {}

    public static void main(String[] args) {
        System.out.println("=== Java Streams API Demo ===\n");

        List<Employee> employees = List.of(
            new Employee("Alice", "Engineering", 95000, 5),
            new Employee("Bob", "Marketing", 65000, 3),
            new Employee("Charlie", "Engineering", 105000, 8),
            new Employee("David", "Marketing", 70000, 4),
            new Employee("Eve", "Engineering", 110000, 10),
            new Employee("Frank", "Sales", 55000, 2),
            new Employee("Grace", "Sales", 60000, 6),
            new Employee("Henry", "Engineering", 98000, 7),
            new Employee("Ivy", "Marketing", 72000, 5),
            new Employee("Jack", "Sales", 58000, 3)
        );

        demonstrateCreation();
        demonstrateIntermediateOps(employees);
        demonstrateTerminalOps(employees);
        demonstrateCollectors(employees);
        demonstrateParallel(employees);
    }

    private static void demonstrateCreation() {
        System.out.println("--- Stream Creation ---");

        // From collection
        Stream<String> fromList = List.of("a", "b", "c").stream();
        System.out.println("From list: " + fromList.count());

        // From array
        IntStream fromArray = Arrays.stream(new int[]{1, 2, 3});
        System.out.println("From array: " + fromArray.sum());

        // From values
        long fromValues = Stream.of(10, 20, 30).count();
        System.out.println("From values: " + fromValues);

        // From range
        int rangeSum = IntStream.rangeClosed(1, 10).sum();
        System.out.println("Range 1-10 sum: " + rangeSum);

        // From generator
        long generated = Stream.generate(Math::random)
            .limit(3).count();
        System.out.println("Generated 3 elements: " + generated);

        // Using builder
        Stream<String> built = Stream.<String>builder()
            .add("x").add("y").add("z")
            .build();
        System.out.println("Built stream: " + built.count());
    }

    private static void demonstrateIntermediateOps(List<Employee> employees) {
        System.out.println("\n--- Intermediate Operations ---");

        // filter
        List<Employee> engineers = employees.stream()
            .filter(e -> e.department().equals("Engineering"))
            .toList();
        System.out.println("Engineers: " + engineers.size());

        // map
        List<String> names = employees.stream()
            .map(Employee::name)
            .toList();
        System.out.println("Names: " + names);

        // flatMap
        List<String> deptChars = employees.stream()
            .map(Employee::department)
            .flatMap(dept -> dept.chars()
                .mapToObj(c -> String.valueOf((char) c)))
            .distinct()
            .sorted()
            .toList();
        System.out.println("Unique dept chars: " + deptChars);

        // sorted
        List<Employee> sorted = employees.stream()
            .sorted(Comparator.comparingDouble(Employee::salary).reversed())
            .limit(3)
            .toList();
        System.out.println("Top 3 earners:");
        sorted.forEach(e ->
            System.out.printf("  %s: $%,.0f%n", e.name(), e.salary()));

        // distinct
        List<String> depts = employees.stream()
            .map(Employee::department)
            .distinct()
            .toList();
        System.out.println("Departments: " + depts);

        // peek (for debugging)
        long count = employees.stream()
            .filter(e -> e.salary() > 70000)
            .peek(e -> System.out.println("  Processing: " + e.name()))
            .count();
        System.out.println("Employees > $70K: " + count);

        // limit and skip
        List<String> middle = employees.stream()
            .map(Employee::name)
            .skip(2)
            .limit(4)
            .toList();
        System.out.println("Middle names: " + middle);
    }

    private static void demonstrateTerminalOps(List<Employee> employees) {
        System.out.println("\n--- Terminal Operations ---");

        // forEach
        System.out.print("Names: ");
        employees.stream().map(Employee::name)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // reduce
        OptionalDouble avgSalary = employees.stream()
            .mapToDouble(Employee::salary)
            .average();
        System.out.printf("Average salary: $%,.2f%n",
            avgSalary.orElse(0));

        int totalYears = employees.stream()
            .mapToInt(Employee::years)
            .sum();
        System.out.println("Total years: " + totalYears);

        // count
        long engineeringCount = employees.stream()
            .filter(e -> e.department().equals("Engineering"))
            .count();
        System.out.println("Engineers: " + engineeringCount);

        // min / max
        Optional<Employee> highestPaid = employees.stream()
            .max(Comparator.comparingDouble(Employee::salary));
        highestPaid.ifPresent(e ->
            System.out.printf("Highest paid: %s ($%,.0f)%n",
                e.name(), e.salary()));

        // findFirst
        Optional<Employee> firstEngineer = employees.stream()
            .filter(e -> e.department().equals("Engineering"))
            .findFirst();
        firstEngineer.ifPresent(e ->
            System.out.println("First engineer: " + e.name()));

        // anyMatch / allMatch / noneMatch
        boolean hasHighEarner = employees.stream()
            .anyMatch(e -> e.salary() > 100000);
        boolean allPositive = employees.stream()
            .allMatch(e -> e.salary() > 0);
        boolean noInterns = employees.stream()
            .noneMatch(e -> e.name().startsWith("Z"));

        System.out.println("Has >$100K earner: " + hasHighEarner);
        System.out.println("All salaries positive: " + allPositive);
        System.out.println("No interns: " + noInterns);
    }

    private static void demonstrateCollectors(List<Employee> employees) {
        System.out.println("\n--- Collectors ---");

        // toList
        List<String> names = employees.stream()
            .map(Employee::name)
            .sorted()
            .toList();
        System.out.println("Sorted names: " + names);

        // toMap
        Map<String, Double> salaryMap = employees.stream()
            .collect(Collectors.toMap(
                Employee::name,
                Employee::salary
            ));
        System.out.println("Salary map size: " + salaryMap.size());

        // groupingBy
        Map<String, List<Employee>> byDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::department));
        System.out.println("Groups:");
        byDept.forEach((dept, list) ->
            System.out.printf("  %s: %d employees%n", dept, list.size()));

        // groupingBy with downstream collector
        Map<String, Double> avgByDept = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::department,
                Collectors.averagingDouble(Employee::salary)
            ));
        System.out.println("Average salary by dept:");
        avgByDept.forEach((dept, avg) ->
            System.out.printf("  %s: $%,.0f%n", dept, avg));

        // partitioningBy
        Map<Boolean, List<Employee>> partitioned = employees.stream()
            .collect(Collectors.partitioningBy(
                e -> e.salary() > 70000
            ));
        System.out.println("Above $70K: " + partitioned.get(true).size());
        System.out.println("Below $70K: " + partitioned.get(false).size());

        // joining
        String joined = employees.stream()
            .map(Employee::name)
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("Joined: " + joined);

        // summarizingDouble
        DoubleSummaryStatistics stats = employees.stream()
            .mapToDouble(Employee::salary)
            .summaryStatistics();
        System.out.printf("Stats: min=$%,.0f max=$%,.0f avg=$%,.0f%n",
            stats.getMin(), stats.getMax(), stats.getAverage());

        // counting by department
        Map<String, Long> countByDept = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::department,
                Collectors.counting()
            ));
        System.out.println("Count by dept: " + countByDept);
    }

    private static void demonstrateParallel(List<Employee> employees) {
        System.out.println("\n--- Parallel Streams ---");

        // Parallel sum
        long start = System.currentTimeMillis();
        double parallelSum = employees.parallelStream()
            .mapToDouble(Employee::salary)
            .sum();
        long parallelTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        double sequentialSum = employees.stream()
            .mapToDouble(Employee::salary)
            .sum();
        long sequentialTime = System.currentTimeMillis() - start;

        System.out.printf("Parallel sum: $%,.0f (%dms)%n",
            parallelSum, parallelTime);
        System.out.printf("Sequential sum: $%,.0f (%dms)%n",
            sequentialSum, sequentialTime);

        // Parallel grouping (ConcurrentHashMap)
        Map<String, Long> parallelCounts = employees.parallelStream()
            .collect(
                Collectors.groupingByConcurrent(
                    Employee::department,
                    Collectors.counting()
                )
            );
        System.out.println("Parallel dept counts: " + parallelCounts);

        // Parallel reduction
        Optional<String> longestName = employees.parallelStream()
            .map(Employee::name)
            .reduce((a, b) -> a.length() >= b.length() ? a : b);
        longestName.ifPresent(name ->
            System.out.println("Longest name: " + name));
    }
}
