package academy.javaengineering.modern.vartype;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Solutions for var Type Inference practice exercises.
 */
public class VarTypeSolutions {

    // Exercise 1: Collection Processing
    public static List<String> processNames(List<String> names) {
        var result = names.stream()
            .filter(name -> name.startsWith("A"))
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        return result;
    }

    // Exercise 2: Map Operations
    public static String findMostFrequent(List<String> words) {
        var frequencyMap = words.stream()
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            ));

        var mostFrequent = frequencyMap.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("None");

        return mostFrequent;
    }

    public static Map<Integer, List<String>> groupByLength(List<String> words) {
        var grouped = words.stream()
            .collect(Collectors.groupingBy(String::length));
        return grouped;
    }

    // Exercise 3: Stream Pipeline
    public static int processNumbers(List<Integer> numbers) {
        var result = numbers.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * n)
            .mapToInt(Integer::intValue)
            .sum();
        return result;
    }

    // Exercise 4: Complex Type Inference
    public static void demonstrateComplexTypes() {
        // Nested collections
        var nestedList = List.of(
            List.of(1, 2, 3),
            List.of(4, 5, 6),
            List.of(7, 8, 9)
        );
        System.out.println("Nested list: " + nestedList);

        // Lambda expressions
        var stringLength = (Function<String, Integer>) String::length;
        var upperCase = (Function<String, String>) String::toUpperCase;
        System.out.println("Length of 'Hello': " + stringLength.apply("Hello"));
        System.out.println("Upper of 'hello': " + upperCase.apply("hello"));

        // Complex generic types
        var complexMap = new java.util.HashMap<String, List<Map<String, Integer>>>();
        complexMap.put("data", List.of(
            Map.of("key1", 1, "key2", 2),
            Map.of("key3", 3, "key4", 4)
        ));
        System.out.println("Complex map: " + complexMap);

        // Method references
        var listOfIntegers = List.of(1, 2, 3, 4, 5);
        var sum = listOfIntegers.stream()
            .mapToInt(Integer::intValue)
            .sum();
        System.out.println("Sum: " + sum);
    }

    public static void main(String[] args) {
        // Test Exercise 1
        System.out.println("--- Exercise 1: Collection Processing ---");
        var names = List.of("Alice", "Bob", "Charlie", "Anna", "David");
        var filteredNames = processNames(names);
        System.out.println("Filtered names: " + filteredNames);

        // Test Exercise 2
        System.out.println("\n--- Exercise 2: Map Operations ---");
        var words = List.of("apple", "banana", "apple", "cherry", "banana", "apple");
        var mostFrequent = findMostFrequent(words);
        System.out.println("Most frequent: " + mostFrequent);
        var groupedByLength = groupByLength(words);
        System.out.println("Grouped by length: " + groupedByLength);

        // Test Exercise 3
        System.out.println("\n--- Exercise 3: Stream Pipeline ---");
        var numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        var sumOfSquares = processNumbers(numbers);
        System.out.println("Sum of squares of evens: " + sumOfSquares);

        // Test Exercise 4
        System.out.println("\n--- Exercise 4: Complex Types ---");
        demonstrateComplexTypes();
    }
}
