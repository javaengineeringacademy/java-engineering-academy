package collecting;

import java.util.*;
import java.util.stream.*;

/**
 * Collecting Operations Examples
 * Demonstrates toList(), toMap(), joining(), groupingBy(), partitioningBy()
 */
public class CollectingExample {

    record Employee(String name, String department, double salary) {}
    record Person(String name, int age, String city) {}

    public static void main(String[] args) {
        System.out.println("=== Collecting Operations ===\n");

        toListExample();
        toSetExample();
        toMapExample();
        joiningExample();
        groupingByExample();
        partitioningByExample();
        summarizingExample();
        practicalExamples();
    }

    // --- toList() Examples ---

    static void toListExample() {
        System.out.println("--- toList() Examples ---");

        List<Integer> numbers = List.of(1, 2, 2, 3, 3, 3);

        // Collect to list (keeps duplicates)
        List<Integer> list = numbers.stream()
            .filter(n -> n > 1)
            .collect(Collectors.toList());
        System.out.println("List: " + list);

        // Collect to unmodifiable list
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<String> unmodifiable = names.stream()
            .collect(Collectors.toUnmodifiableList());
        System.out.println("Unmodifiable: " + unmodifiable);

        System.out.println();
    }

    // --- toSet() Examples ---

    static void toSetExample() {
        System.out.println("--- toSet() Examples ---");

        List<Integer> numbers = List.of(1, 2, 2, 3, 3, 3);

        // Collect to set (removes duplicates)
        Set<Integer> set = numbers.stream()
            .collect(Collectors.toSet());
        System.out.println("Set: " + set);

        System.out.println();
    }

    // --- toMap() Examples ---

    static void toMapExample() {
        System.out.println("--- toMap() Examples ---");

        List<String> names = List.of("Alice", "Bob", "Charlie");

        // Map name to length
        Map<String, Integer> nameLengths = names.stream()
            .collect(Collectors.toMap(
                name -> name,
                String::length
            ));
        System.out.println("Name lengths: " + nameLengths);

        // Map with merge function
        List<String> moreNames = List.of("Alice", "Bob", "Alice", "Charlie");
        Map<String, Integer> withMerge = moreNames.stream()
            .collect(Collectors.toMap(
                name -> name,
                String::length,
                (existing, replacement) -> existing
            ));
        System.out.println("With merge: " + withMerge);

        // Map using method references
        Map<Character, String> firstByChar = names.stream()
            .collect(Collectors.toMap(
                name -> name.charAt(0),
                name -> name
            ));
        System.out.println("By first char: " + firstByChar);

        System.out.println();
    }

    // --- joining() Examples ---

    static void joiningExample() {
        System.out.println("--- joining() Examples ---");

        List<String> names = List.of("Alice", "Bob", "Charlie");

        // Simple join
        String joined = names.stream()
            .collect(Collectors.joining());
        System.out.println("Joined: " + joined);

        // Join with delimiter
        String withDelimiter = names.stream()
            .collect(Collectors.joining(", "));
        System.out.println("With delimiter: " + withDelimiter);

        // Join with prefix and suffix
        String withFixes = names.stream()
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("With fixes: " + withFixes);

        System.out.println();
    }

    // --- groupingBy() Examples ---

    static void groupingByExample() {
        System.out.println("--- groupingBy() Examples ---");

        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");

        // Group by first letter
        Map<Character, List<String>> byFirstLetter = names.stream()
            .collect(Collectors.groupingBy(name -> name.charAt(0)));
        System.out.println("By first letter: " + byFirstLetter);

        // Group by length
        Map<Integer, List<String>> byLength = names.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("By length: " + byLength);

        // Group with counting
        Map<Integer, Long> countByLength = names.stream()
            .collect(Collectors.groupingBy(
                String::length,
                Collectors.counting()
            ));
        System.out.println("Count by length: " + countByLength);

        // Group with joining
        Map<Character, String> joinedByLetter = names.stream()
            .collect(Collectors.groupingBy(
                name -> name.charAt(0),
                Collectors.joining(", ")
            ));
        System.out.println("Joined by letter: " + joinedByLetter);

        // Group employees by department
        List<Employee> employees = List.of(
            new Employee("Alice", "Engineering", 90000),
            new Employee("Bob", "Marketing", 70000),
            new Employee("Charlie", "Engineering", 95000),
            new Employee("David", "Marketing", 75000)
        );

        Map<String, List<Employee>> byDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::department));
        System.out.println("By department: " + byDept);

        // Average salary by department
        Map<String, Double> avgSalary = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::department,
                Collectors.averagingDouble(Employee::salary)
            ));
        System.out.println("Avg salary by dept: " + avgSalary);

        System.out.println();
    }

    // --- partitioningBy() Examples ---

    static void partitioningByExample() {
        System.out.println("--- partitioningBy() Examples ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Partition evens and odds
        Map<Boolean, List<Integer>> partitioned = numbers.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("Evens/Odds: " + partitioned);

        // Partition with counting
        Map<Boolean, Long> counts = numbers.stream()
            .collect(Collectors.partitioningBy(
                n -> n % 2 == 0,
                Collectors.counting()
            ));
        System.out.println("Counts: " + counts);

        // Partition with summing
        Map<Boolean, Integer> sums = numbers.stream()
            .collect(Collectors.partitioningBy(
                n -> n % 2 == 0,
                Collectors.summingInt(Integer::intValue)
            ));
        System.out.println("Sums: " + sums);

        // Partition people by age
        List<Person> people = List.of(
            new Person("Alice", 30, "New York"),
            new Person("Bob", 17, "Chicago"),
            new Person("Charlie", 25, "New York"),
            new Person("David", 15, "Chicago")
        );

        Map<Boolean, List<Person>> adults = people.stream()
            .collect(Collectors.partitioningBy(p -> p.age() >= 18));
        System.out.println("Adults: " + adults.get(true));
        System.out.println("Minors: " + adults.get(false));

        System.out.println();
    }

    // --- summarizingInt/Long/Double Examples ---

    static void summarizingExample() {
        System.out.println("--- summarizing Examples ---");

        List<String> names = List.of("Alice", "Bob", "Charlie", "David");

        // Get statistics
        IntSummaryStatistics lengthStats = names.stream()
            .collect(Collectors.summarizingInt(String::length));

        System.out.println("Count: " + lengthStats.getCount());
        System.out.println("Sum: " + lengthStats.getSum());
        System.out.println("Min: " + lengthStats.getMin());
        System.out.println("Max: " + lengthStats.getMax());
        System.out.println("Average: " + lengthStats.getAverage());

        // Can also use mapToInt
        IntSummaryStatistics stats2 = names.stream()
            .mapToInt(String::length)
            .summaryStatistics();
        System.out.println("Stats2: " + stats2);

        System.out.println();
    }

    // --- Practical Examples ---

    static void practicalExamples() {
        System.out.println("--- Practical Examples ---");

        // Example 1: Word frequency
        String text = "hello world hello java world hello";
        Map<String, Long> wordFreq = Arrays.stream(text.split(" "))
            .collect(Collectors.groupingBy(
                word -> word,
                Collectors.counting()
            ));
        System.out.println("Word frequency: " + wordFreq);

        // Example 2: Create lookup map
        List<String> names = List.of("Alice", "Bob", "Charlie");
        Map<String, Integer> nameToIndex = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            nameToIndex.put(names.get(i), i);
        }
        // Or using stream
        Map<String, Integer> nameToIndex2 = IntStream.range(0, names.size())
            .boxed()
            .collect(Collectors.toMap(
                names::get,
                i -> i
            ));
        System.out.println("Name to index: " + nameToIndex2);

        // Example 3: Statistics report
        List<Integer> numbers = List.of(10, 20, 30, 40, 50);
        IntSummaryStatistics stats = numbers.stream()
            .collect(Collectors.summarizingInt(Integer::intValue));
        System.out.printf("Stats: count=%d, sum=%d, avg=%.2f%n",
            stats.getCount(), stats.getSum(), stats.getAverage());

        System.out.println();
    }
}
