import java.util.*;
import java.util.stream.*;

/**
 * Stream Operations Solutions
 * Complete implementations for all exercises
 */
public class solutions {

    public static void main(String[] args) {
        System.out.println("=== Stream Operations Solutions ===\n");

        System.out.println("Exercise 1: " + exercise1(List.of(1, 2, 3, 4, 5, 6)));
        System.out.println("Exercise 2: " + exercise2(List.of("hello", "world")));
        System.out.println("Exercise 3: " + exercise3(List.of("a", "bb", "ccc", "dddd"), 2));
        System.out.println("Exercise 4: " + exercise4(List.of(1, 2, 3, 4, 5, 6)));
        System.out.println("Exercise 5: " + exercise5(List.of("apple", "banana", "avocado", "blueberry")));
        System.out.println("Exercise 6: " + exercise6());
        System.out.println("Exercise 7: " + exercise7(List.of(List.of(1, 2), List.of(3, 4), List.of(5, 6))));
        System.out.println("Exercise 8: " + exercise8(List.of(5, 3, 9, 1, 7)));
        System.out.println("Exercise 9: " + exercise9(List.of(1.0, 2.0, 3.0, 4.0, 5.0)));
        System.out.println("Exercise 10: " + exercise10(List.of("hello", "world")));
    }

    // Exercise 1: Filter even numbers
    public static List<Integer> exercise1(List<Integer> numbers) {
        return numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
    }

    // Exercise 2: Transform to uppercase
    public static List<String> exercise2(List<String> words) {
        return words.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    }

    // Exercise 3: Count strings longer than N
    public static long exercise3(List<String> words, int n) {
        return words.stream()
            .filter(w -> w.length() > n)
            .count();
    }

    // Exercise 4: Find sum of even numbers
    public static int exercise4(List<Integer> numbers) {
        return numbers.stream()
            .filter(n -> n % 2 == 0)
            .reduce(0, Integer::sum);
    }

    // Exercise 5: Group strings by first letter
    public static Map<Character, List<String>> exercise5(List<String> words) {
        return words.stream()
            .collect(Collectors.groupingBy(w -> w.charAt(0)));
    }

    // Exercise 6: Sort by multiple criteria
    public static List<String> exercise6() {
        record Person(String name, int age) {}
        List<Person> people = List.of(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 30),
            new Person("David", 25)
        );
        return people.stream()
            .sorted(Comparator.comparingInt(Person::age)
                .thenComparing(Person::name))
            .map(Person::name)
            .collect(Collectors.toList());
    }

    // Exercise 7: Flatten nested lists
    public static List<Integer> exercise7(List<List<Integer>> nested) {
        return nested.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    // Exercise 8: Find second largest
    public static Optional<Integer> exercise8(List<Integer> numbers) {
        return numbers.stream()
            .distinct()
            .sorted(Comparator.reverseOrder())
            .skip(1)
            .findFirst();
    }

    // Exercise 9: Running average
    public static List<Double> exercise9(List<Double> numbers) {
        List<Double> result = new ArrayList<>();
        double sum = 0;
        for (int i = 0; i < numbers.size(); i++) {
            sum += numbers.get(i);
            result.add(sum / (i + 1));
        }
        return result;
    }

    // Exercise 10: Join with transformation
    public static String exercise10(List<String> words) {
        return words.stream()
            .map(String::toUpperCase)
            .collect(Collectors.joining(", "));
    }
}
