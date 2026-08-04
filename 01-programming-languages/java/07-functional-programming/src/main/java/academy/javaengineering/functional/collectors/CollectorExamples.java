package academy.javaengineering.functional.collectors;

import java.util.*;
import java.util.stream.*;

/**
 * Comprehensive examples of Collectors in Java 21.
 *
 * <p>This class demonstrates all aspects of collectors including built-in
 * collectors, downstream collectors, and custom collector implementation.
 * Each example is self-contained and can be run independently.</p>
 *
 * <p>Topics covered:</p>
 * <ul>
 *   <li>Built-in collectors (toList, toSet, toMap, joining)</li>
 *   <li>Grouping and partitioning collectors</li>
 *   <li>Downstream collectors</li>
 *   <li>Custom collector implementation</li>
 *   <li>Performance considerations</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since 1.0
 */
public final class CollectorExamples {

    private CollectorExamples() {
        // Utility class - no instantiation
    }

    /**
     * Demonstrates basic collectors.
     */
    public static void basicCollectors() {
        System.out.println("=== Basic Collectors ===\n");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");

        // toList
        List<String> list = names.stream()
            .filter(name -> name.length() > 3)
            .collect(Collectors.toList());
        System.out.println("List: " + list);

        // toUnmodifiableList
        List<String> unmodifiable = names.stream()
            .collect(Collectors.toUnmodifiableList());
        System.out.println("Unmodifiable: " + unmodifiable);

        // toSet
        Set<String> set = names.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        System.out.println("Set: " + set);

        // toMap
        Map<String, Integer> nameLengths = names.stream()
            .collect(Collectors.toMap(
                name -> name,
                String::length
            ));
        System.out.println("Name lengths: " + nameLengths);

        // joining
        String joined = names.stream()
            .collect(Collectors.joining(", "));
        System.out.println("Joined: " + joined);

        // joining with prefix/suffix
        String joinedWithBrackets = names.stream()
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("Joined with brackets: " + joinedWithBrackets);
    }

    /**
     * Demonstrates grouping collectors.
     */
    public static void groupingCollectors() {
        System.out.println("\n=== Grouping Collectors ===\n");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");

        // Group by first letter
        Map<Character, List<String>> byFirstLetter = names.stream()
            .collect(Collectors.groupingBy(name -> name.charAt(0)));
        System.out.println("By first letter: " + byFirstLetter);

        // Count by first letter
        Map<Character, Long> countByLetter = names.stream()
            .collect(Collectors.groupingBy(
                name -> name.charAt(0),
                Collectors.counting()
            ));
        System.out.println("Count by letter: " + countByLetter);

        // Group by length
        Map<Integer, List<String>> byLength = names.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("By length: " + byLength);

        // Partition by length > 3
        Map<Boolean, List<String>> partitioned = names.stream()
            .collect(Collectors.partitioningBy(name -> name.length() > 3));
        System.out.println("Long names: " + partitioned.get(true));
        System.out.println("Short names: " + partitioned.get(false));
    }

    /**
     * Demonstrates downstream collectors.
     */
    public static void downstreamCollectors() {
        System.out.println("\n=== Downstream Collectors ===\n");

        record Student(String name, String department, double gpa) {}

        List<Student> students = List.of(
            new Student("Alice", "CS", 3.8),
            new Student("Bob", "CS", 3.5),
            new Student("Charlie", "Math", 3.9),
            new Student("Diana", "Math", 3.7),
            new Student("Eve", "CS", 3.6)
        );

        // Count by department
        Map<String, Long> countByDept = students.stream()
            .collect(Collectors.groupingBy(
                Student::department,
                Collectors.counting()
            ));
        System.out.println("Count by dept: " + countByDept);

        // Average GPA by department
        Map<String, Double> avgGpaByDept = students.stream()
            .collect(Collectors.groupingBy(
                Student::department,
                Collectors.averagingDouble(Student::gpa)
            ));
        System.out.println("Avg GPA by dept: " + avgGpaByDept);

        // Names by department
        Map<String, List<String>> namesByDept = students.stream()
            .collect(Collectors.groupingBy(
                Student::department,
                Collectors.mapping(Student::name, Collectors.toList())
            ));
        System.out.println("Names by dept: " + namesByDept);

        // Summary statistics by department
        Map<String, IntSummaryStatistics> statsByDept = students.stream()
            .collect(Collectors.groupingBy(
                Student::department,
                Collectors.summarizingInt(s -> (int) (s.gpa() * 10))
            ));
        System.out.println("Stats by dept: " + statsByDept);
    }

    /**
     * Demonstrates custom collectors.
     */
    public static void customCollectors() {
        System.out.println("\n=== Custom Collectors ===\n");

        List<Integer> numbers = Arrays.asList(5, 3, 1, 4, 2, 8, 7, 6);

        // Custom sorted list collector
        Collector<Integer, ArrayList<Integer>, List<Integer>> toSortedList = Collector.of(
            ArrayList::new,
            List::add,
            (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            },
            list -> {
                list.sort(Comparator.naturalOrder());
                return Collections.unmodifiableList(list);
            }
        );

        List<Integer> sorted = numbers.stream()
            .collect(toSortedList);
        System.out.println("Sorted: " + sorted);

        // Custom joining collector
        Collector<String, ?, String> joiningWithLimit = Collector.of(
            StringBuilder::new,
            (sb, s) -> {
                if (sb.length() > 0) sb.append(", ");
                sb.append(s);
                if (sb.length() > 20) sb.setLength(20);
            },
            (sb1, sb2) -> {
                if (sb1.length() > 0 && sb2.length() > 0) sb1.append(", ");
                sb1.append(sb2);
                if (sb1.length() > 20) sb1.setLength(20);
                return sb1;
            },
            StringBuilder::toString
        );

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");
        String joined = names.stream()
            .collect(joiningWithLimit);
        System.out.println("Joined with limit: " + joined);
    }

    /**
     * Main method to run all examples.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        basicCollectors();
        groupingCollectors();
        downstreamCollectors();
        customCollectors();

        System.out.println("\n=== Summary ===");
        System.out.println("Key takeaways:");
        System.out.println("1. Built-in collectors: toList, toSet, toMap, joining");
        System.out.println("2. Grouping: groupingBy, partitioningBy");
        System.out.println("3. Downstream collectors: counting, averaging, mapping");
        System.out.println("4. Custom collectors: Implement Collector interface");
        System.out.println("5. Use unmodifiable collectors for thread safety");
    }
}
