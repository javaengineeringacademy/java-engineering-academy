import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class examples {

    public static void main(String[] args) {
        filterEvenNumbers();
        mapStringsToUppercase();
        reduceToSum();
        collectToMap();
        flatMapNestedLists();
        partitionByPredicate();
        groupByClassifier();
        findFirstMatching();
        sortWithComparator();
        parallelProcessing();
    }

    // Example 1: Filter even numbers
    static void filterEvenNumbers() {
        System.out.println("=== Example 1: Filter Even Numbers ===");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());

        System.out.println("Input: " + numbers);
        System.out.println("Even numbers: " + evens);
        System.out.println();
    }

    // Example 2: Map strings to uppercase
    static void mapStringsToUppercase() {
        System.out.println("=== Example 2: Map Strings to Uppercase ===");
        List<String> words = Arrays.asList("hello", "world", "java", "streams");

        List<String> upperWords = words.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());

        System.out.println("Input: " + words);
        System.out.println("Uppercase: " + upperWords);
        System.out.println();
    }

    // Example 3: Reduce to sum
    static void reduceToSum() {
        System.out.println("=== Example 3: Reduce to Sum ===");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        int sum = numbers.stream()
            .reduce(0, Integer::sum);

        System.out.println("Input: " + numbers);
        System.out.println("Sum: " + sum);

        // Reduce with different initial value
        int sumFromFive = numbers.stream()
            .reduce(5, Integer::sum);
        System.out.println("Sum from 5: " + sumFromFive);
        System.out.println();
    }

    // Example 4: Collect to map
    static void collectToMap() {
        System.out.println("=== Example 4: Collect to Map ===");
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        Map<String, Integer> nameLengths = names.stream()
            .collect(Collectors.toMap(
                name -> name,
                String::length
            ));

        System.out.println("Input: " + names);
        System.out.println("Name to length map: " + nameLengths);

        // Collect with merge function for duplicate keys
        List<String> words = Arrays.asList("apple", "banana", "apricot", "blueberry");
        Map<Character, List<String>> byFirstLetter = words.stream()
            .collect(Collectors.groupingBy(w -> w.charAt(0)));
        System.out.println("Grouped by first letter: " + byFirstLetter);
        System.out.println();
    }

    // Example 5: FlatMap nested lists
    static void flatMapNestedLists() {
        System.out.println("=== Example 5: FlatMap Nested Lists ===");
        List<List<Integer>> nested = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(4, 5, 6),
            Arrays.asList(7, 8, 9)
        );

        List<Integer> flat = nested.stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

        System.out.println("Nested: " + nested);
        System.out.println("Flattened: " + flat);

        // FlatMap with strings
        List<String> sentences = Arrays.asList("Hello World", "Java Streams");
        List<String> words = sentences.stream()
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .collect(Collectors.toList());
        System.out.println("Words from sentences: " + words);
        System.out.println();
    }

    // Example 6: Partition by predicate
    static void partitionByPredicate() {
        System.out.println("=== Example 6: Partition by Predicate ===");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Map<Boolean, List<Integer>> partitioned = numbers.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));

        System.out.println("Input: " + numbers);
        System.out.println("Even: " + partitioned.get(true));
        System.out.println("Odd: " + partitioned.get(false));

        // Partition strings by length
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");
        Map<Boolean, List<String>> byLength = names.stream()
            .collect(Collectors.partitioningBy(s -> s.length() > 3));
        System.out.println("Long names: " + byLength.get(true));
        System.out.println("Short names: " + byLength.get(false));
        System.out.println();
    }

    // Example 7: Group by classifier
    static void groupByClassifier() {
        System.out.println("=== Example 7: Group by Classifier ===");
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve", "Frank");

        Map<Integer, List<String>> byLength = names.stream()
            .collect(Collectors.groupingBy(String::length));

        System.out.println("Input: " + names);
        System.out.println("Grouped by length: " + byLength);

        // Group with downstream collector
        Map<Integer, Long> countByLength = names.stream()
            .collect(Collectors.groupingBy(
                String::length,
                Collectors.counting()
            ));
        System.out.println("Count by length: " + countByLength);

        // Group and join
        Map<Integer, String> joinedByLength = names.stream()
            .collect(Collectors.groupingBy(
                String::length,
                Collectors.joining(", ")
            ));
        System.out.println("Joined by length: " + joinedByLength);
        System.out.println();
    }

    // Example 8: Find first matching
    static void findFirstMatching() {
        System.out.println("=== Example 8: Find First Matching ===");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Optional<Integer> firstEven = numbers.stream()
            .filter(n -> n % 2 == 0)
            .findFirst();
        System.out.println("First even: " + firstEven.orElse(-1));

        Optional<Integer> firstGreaterThanFive = numbers.stream()
            .filter(n -> n > 5)
            .findFirst();
        System.out.println("First > 5: " + firstGreaterThanFive.orElse(-1));

        // findAny for parallel streams
        Optional<Integer> anyEven = numbers.parallelStream()
            .filter(n -> n % 2 == 0)
            .findAny();
        System.out.println("Any even (parallel): " + anyEven.orElse(-1));

        // with strings
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
        Optional<String> firstStartingWithC = names.stream()
            .filter(s -> s.startsWith("C"))
            .findFirst();
        System.out.println("First starting with C: " + firstStartingWithC.orElse("none"));
        System.out.println();
    }

    // Example 9: Sort with comparator
    static void sortWithComparator() {
        System.out.println("=== Example 9: Sort with Comparator ===");
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob", "David", "Eve");

        // Natural order
        List<String> sorted = names.stream()
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Natural order: " + sorted);

        // Reverse order
        List<String> reverseSorted = names.stream()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        System.out.println("Reverse order: " + reverseSorted);

        // Sort by length
        List<String> byLength = names.stream()
            .sorted(Comparator.comparingInt(String::length))
            .collect(Collectors.toList());
        System.out.println("By length: " + byLength);

        // Sort by length, then alphabetically
        List<String> byLengthThenName = names.stream()
            .sorted(Comparator.comparingInt(String::length)
                .thenComparing(Comparator.naturalOrder()))
            .collect(Collectors.toList());
        System.out.println("By length then name: " + byLengthThenName);

        // Sort custom objects
        List<Person> people = Arrays.asList(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35),
            new Person("David", 25)
        );

        List<Person> byAge = people.stream()
            .sorted(Comparator.comparingInt(Person::getAge))
            .collect(Collectors.toList());
        System.out.println("People by age: " + byAge);

        List<Person> byName = people.stream()
            .sorted(Comparator.comparing(Person::getName))
            .collect(Collectors.toList());
        System.out.println("People by name: " + byName);
        System.out.println();
    }

    // Example 10: Parallel processing
    static void parallelProcessing() {
        System.out.println("=== Example 10: Parallel Processing ===");
        List<Integer> numbers = IntStream.rangeClosed(1, 1000000)
            .boxed()
            .collect(Collectors.toList());

        // Sequential sum
        long startTime = System.nanoTime();
        int seqSum = numbers.stream()
            .reduce(0, Integer::sum);
        long seqTime = System.nanoTime() - startTime;

        // Parallel sum
        startTime = System.nanoTime();
        int parSum = numbers.parallelStream()
            .reduce(0, Integer::sum);
        long parTime = System.nanoTime() - startTime;

        System.out.println("Sequential sum: " + seqSum + " (" + seqTime / 1000000 + "ms)");
        System.out.println("Parallel sum: " + parSum + " (" + parTime / 1000000 + "ms)");

        // Parallel with custom combiner
        String result = IntStream.rangeClosed(1, 10)
            .parallel()
            .mapToObj(Integer::toString)
            .reduce("", (a, b) -> a.isEmpty() ? b : a + "," + b);
        System.out.println("Parallel concatenation: " + result);

        // Parallel forEach (note: order not guaranteed)
        System.out.print("Parallel forEach: ");
        IntStream.rangeClosed(1, 10)
            .parallel()
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Parallel with stateful operations (use with caution)
        List<Integer> squared = IntStream.rangeClosed(1, 10)
            .parallel()
            .map(n -> n * n)
            .boxed()
            .collect(Collectors.toList());
        System.out.println("Parallel squared: " + squared);
    }

    static class Person {
        private String name;
        private int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public int getAge() { return age; }

        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }
}
