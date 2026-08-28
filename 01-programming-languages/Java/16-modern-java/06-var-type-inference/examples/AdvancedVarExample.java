package academy.javaengineering.modern.vartype;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Advanced var usage patterns.
 */
public class AdvancedVarExample {

    public record Person(String name, int age) {}

    public static void main(String[] args) {
        // var with method calls
        System.out.println("=== var with Method Calls ===");
        var people = getPeople();
        var names = people.stream()
            .map(Person::name)
            .collect(Collectors.toList());
        System.out.println("Names: " + names);

        // var with lambda expressions
        System.out.println("\n=== var with Lambdas ===");
        var lengthFunc = (java.util.function.Function<String, Integer>) s -> s.length();
        var upperFunc = (java.util.function.Function<String, String>) String::toUpperCase;
        System.out.println("Length of 'Hello': " + lengthFunc.apply("Hello"));
        System.out.println("Upper of 'hello': " + upperFunc.apply("hello"));

        // var in try-with-resources
        System.out.println("\n=== var in try-with-resources ===");
        // Note: try-with-resources requires explicit type in some cases
        // var reader = new java.io.BufferedReader(new java.io.StringReader("test"));

        // var with conditional expressions
        System.out.println("\n=== var with Conditionals ===");
        var condition = true;
        var value = condition ? "True value" : "False value";
        System.out.println("Conditional value: " + value);

        // var with complex expressions
        System.out.println("\n=== var with Complex Expressions ===");
        var numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        var sum = numbers.stream()
            .mapToInt(Integer::intValue)
            .sum();
        var average = numbers.stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0);
        System.out.println("Sum: " + sum);
        System.out.println("Average: " + average);

        // var with nested collections
        System.out.println("\n=== var with Nested Collections ===");
        var nestedMap = Map.of(
            "group1", List.of(1, 2, 3),
            "group2", List.of(4, 5, 6),
            "group3", List.of(7, 8, 9)
        );
        System.out.println("Nested map: " + nestedMap);

        // var with streams and grouping
        System.out.println("\n=== var with Streams ===");
        var grouped = people.stream()
            .collect(Collectors.groupingBy(p -> p.age() > 25 ? "Senior" : "Junior"));
        System.out.println("Grouped by age: " + grouped);
    }

    static List<Person> getPeople() {
        return List.of(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35)
        );
    }
}
