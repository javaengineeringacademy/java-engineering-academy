import java.util.*;
import java.util.stream.*;

/**
 * Stream Operations Exercises
 * Complete each exercise by implementing the method body.
 *
 * Difficulty levels:
 * - Beginner: Exercises 1-3
 * - Intermediate: Exercises 4-7
 * - Advanced: Exercises 8-10
 */
public class exercises {

    public static void main(String[] args) {
        System.out.println("=== Stream Operations Exercises ===\n");
        System.out.println("Implement each method and run to verify.\n");

        // Uncomment each exercise as you complete it

        // Exercise 1: Filter even numbers
        // System.out.println("Exercise 1: " + exercise1(List.of(1, 2, 3, 4, 5, 6)));

        // Exercise 2: Transform to uppercase
        // System.out.println("Exercise 2: " + exercise2(List.of("hello", "world")));

        // Exercise 3: Count strings longer than N
        // System.out.println("Exercise 3: " + exercise3(List.of("a", "bb", "ccc", "dddd"), 2));

        // Exercise 4: Find sum of even numbers
        // System.out.println("Exercise 4: " + exercise4(List.of(1, 2, 3, 4, 5, 6)));

        // Exercise 5: Group strings by first letter
        // System.out.println("Exercise 5: " + exercise5(List.of("apple", "banana", "avocado", "blueberry")));

        // Exercise 6: Sort by multiple criteria
        // System.out.println("Exercise 6: " + exercise6());

        // Exercise 7: Flatten nested lists
        // System.out.println("Exercise 7: " + exercise7(List.of(List.of(1, 2), List.of(3, 4), List.of(5, 6))));

        // Exercise 8: Find second largest
        // System.out.println("Exercise 8: " + exercise8(List.of(5, 3, 9, 1, 7)));

        // Exercise 9: Running average
        // System.out.println("Exercise 9: " + exercise9(List.of(1.0, 2.0, 3.0, 4.0, 5.0)));

        // Exercise 10: Custom collector - join with transformation
        // System.out.println("Exercise 10: " + exercise10(List.of("hello", "world")));
    }

    /**
     * Exercise 1 (Beginner): Filter even numbers
     * Given a list of integers, return only the even numbers.
     *
     * Example: [1, 2, 3, 4, 5, 6] -> [2, 4, 6]
     */
    public static List<Integer> exercise1(List<Integer> numbers) {
        // TODO: Implement using stream filter
        return List.of();
    }

    /**
     * Exercise 2 (Beginner): Transform to uppercase
     * Given a list of strings, return them all in uppercase.
     *
     * Example: ["hello", "world"] -> ["HELLO", "WORLD"]
     */
    public static List<String> exercise2(List<String> words) {
        // TODO: Implement using stream map
        return List.of();
    }

    /**
     * Exercise 3 (Beginner): Count strings longer than N
     * Given a list of strings and a length N, count how many are longer than N.
     *
     * Example: (["a", "bb", "ccc", "dddd"], 2) -> 2
     */
    public static long exercise3(List<String> words, int n) {
        // TODO: Implement using stream filter and count
        return 0;
    }

    /**
     * Exercise 4 (Intermediate): Find sum of even numbers
     * Given a list of integers, find the sum of all even numbers.
     *
     * Example: [1, 2, 3, 4, 5, 6] -> 12
     */
    public static int exercise4(List<Integer> numbers) {
        // TODO: Implement using stream filter and reduce/sum
        return 0;
    }

    /**
     * Exercise 5 (Intermediate): Group strings by first letter
     * Given a list of strings, group them by their first character.
     *
     * Example: ["apple", "banana", "avocado", "blueberry"]
     *          -> {a=[apple, avocado], b=[banana, blueberry]}
     */
    public static Map<Character, List<String>> exercise5(List<String> words) {
        // TODO: Implement using stream collect and groupingBy
        return Map.of();
    }

    /**
     * Exercise 6 (Intermediate): Sort people by age then name
     * Create a list of Person objects, sort by age (ascending) then name.
     *
     * Return sorted list of person names.
     */
    public static List<String> exercise6() {
        record Person(String name, int age) {}
        List<Person> people = List.of(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 30),
            new Person("David", 25)
        );
        // TODO: Implement sorting with Comparator.comparing and thenComparing
        return List.of();
    }

    /**
     * Exercise 7 (Intermediate): Flatten nested lists
     * Given a list of lists of integers, flatten into a single list.
     *
     * Example: [[1,2], [3,4], [5,6]] -> [1, 2, 3, 4, 5, 6]
     */
    public static List<Integer> exercise7(List<List<Integer>> nested) {
        // TODO: Implement using stream flatMap
        return List.of();
    }

    /**
     * Exercise 8 (Advanced): Find second largest
     * Given a list of integers, find the second largest distinct value.
     * Return Optional.empty if not possible.
     *
     * Example: [5, 3, 9, 1, 7] -> Optional[7]
     */
    public static Optional<Integer> exercise8(List<Integer> numbers) {
        // TODO: Implement using stream distinct, sorted, skip, findFirst
        return Optional.empty();
    }

    /**
     * Exercise 9 (Advanced): Running average
     * Given a list of doubles, compute the running average.
     *
     * Example: [1.0, 2.0, 3.0, 4.0, 5.0] -> [1.0, 1.5, 2.0, 2.5, 3.0]
     */
    public static List<Double> exercise9(List<Double> numbers) {
        // TODO: Implement using stream with stateful accumulation
        return List.of();
    }

    /**
     * Exercise 10 (Advanced): Custom collector - join with transformation
     * Given a list of strings, join them with commas but uppercase each word first.
     *
     * Example: ["hello", "world"] -> "HELLO, WORLD"
     */
    public static String exercise10(List<String> words) {
        // TODO: Implement using stream map and Collectors.joining
        return "";
    }
}
