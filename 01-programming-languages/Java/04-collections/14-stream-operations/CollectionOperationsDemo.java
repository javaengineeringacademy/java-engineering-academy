import java.util.*;
import java.util.stream.Collectors;

/**
 * Demonstrates common collection operations using Lambda expressions and Stream API.
 * Covers filter, map, reduce, collect, find, match, flatMap, grouping, partitioning,
 * joining, counting, and numeric operations.
 */
public class CollectionOperationsDemo {

    public static void main(String[] args) {
        demonstrateFilter();
        demonstrateMap();
        demonstrateReduce();
        demonstrateCollect();
        demonstrateFindFirstFindAny();
        demonstrateMatchOperations();
        demonstrateFlatMap();
        demonstrateGroupingBy();
        demonstratePartitioningBy();
        demonstrateJoining();
        demonstrateCounting();
        demonstrateNumericOperations();
        demonstrateChainedOperations();
    }

    /**
     * Filtering elements based on a predicate.
     */
    private static void demonstrateFilter() {
        System.out.println("=== Filter ===");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Filter even numbers
        List<Integer> even = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("  Even numbers: " + even);

        // Filter strings by length
        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");
        List<String> longNames = names.stream()
                .filter(name -> name.length() > 4)
                .collect(Collectors.toList());
        System.out.println("  Names longer than 4: " + longNames);

        // Filter non-null
        List<String> mixed = List.of("Alice", null, "Bob", null, "Charlie");
        List<String> nonNull = mixed.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        System.out.println("  Non-null: " + nonNull);
        System.out.println();
    }

    /**
     * Transforming elements using map.
     */
    private static void demonstrateMap() {
        System.out.println("=== Map ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");

        // Transform to uppercase
        List<String> upper = names.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("  Uppercase: " + upper);

        // Transform to lengths
        List<Integer> lengths = names.stream()
                .map(String::length)
                .collect(Collectors.toList());
        System.out.println("  Lengths: " + lengths);

        // Transform with custom function
        List<String> greetings = names.stream()
                .map(name -> "Hello, " + name + "!")
                .collect(Collectors.toList());
        System.out.println("  Greetings: " + greetings);

        // Map to objects
        record Person(String name, int age) {}
        List<Integer> ages = List.of(25, 30, 35, 28, 42);
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            people.add(new Person(names.get(i), ages.get(i)));
        }
        System.out.println("  People: " + people);
        System.out.println();
    }

    /**
     * Reducing a collection to a single value.
     */
    private static void demonstrateReduce() {
        System.out.println("=== Reduce ===");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // Sum with reduce
        int sum = numbers.stream()
                .reduce(0, Integer::sum);
        System.out.println("  Sum: " + sum);

        // Product with reduce
        int product = numbers.stream()
                .reduce(1, (a, b) -> a * b);
        System.out.println("  Product: " + product);

        // Find max
        Optional<Integer> max = numbers.stream()
                .reduce(Integer::max);
        max.ifPresent(m -> System.out.println("  Max: " + m));

        // Find min
        Optional<Integer> min = numbers.stream()
                .reduce(Integer::min);
        min.ifPresent(m -> System.out.println("  Min: " + m));

        // Concatenate strings
        List<String> words = List.of("Java", "is", "awesome");
        String sentence = words.stream()
                .reduce("", (a, b) -> a.isEmpty() ? b : a + " " + b);
        System.out.println("  Sentence: " + sentence);
        System.out.println();
    }

    /**
     * Collecting stream results into collections.
     */
    private static void demonstrateCollect() {
        System.out.println("=== Collect ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");

        // Collect to List
        List<String> list = names.stream()
                .filter(name -> name.length() > 3)
                .collect(Collectors.toList());
        System.out.println("  To List: " + list);

        // Collect to Set
        List<String> duplicates = List.of("A", "B", "A", "C", "B", "D");
        Set<String> unique = duplicates.stream()
                .collect(Collectors.toSet());
        System.out.println("  To Set (unique): " + unique);

        // Collect to unmodifiable list
        List<String> unmodifiable = names.stream()
                .collect(Collectors.toUnmodifiableList());
        System.out.println("  Unmodifiable: " + unmodifiable);

        // Collect to Map
        Map<String, Integer> nameLengths = names.stream()
                .collect(Collectors.toMap(
                        name -> name,
                        String::length
                ));
        System.out.println("  To Map (name->length): " + nameLengths);

        // Collect with summarizing
        IntSummaryStatistics stats = names.stream()
                .collect(Collectors.summarizingInt(String::length));
        System.out.println("  Stats: " + stats);
        System.out.println();
    }

    /**
     * Finding elements in a collection.
     */
    private static void demonstrateFindFirstFindAny() {
        System.out.println("=== FindFirst / FindAny ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");

        // findFirst
        Optional<String> first = names.stream()
                .filter(name -> name.length() > 4)
                .findFirst();
        first.ifPresent(name -> System.out.println("  First long name: " + name));

        // findAny (useful in parallel streams)
        Optional<String> any = names.stream()
                .filter(name -> name.startsWith("D"))
                .findAny();
        any.ifPresent(name -> System.out.println("  Any starting with D: " + name));

        // findFirst with orElse
        String result = names.stream()
                .filter(name -> name.startsWith("Z"))
                .findFirst()
                .orElse("Not found");
        System.out.println("  Starting with Z: " + result);
        System.out.println();
    }

    /**
     * Matching operations: anyMatch, allMatch, noneMatch.
     */
    private static void demonstrateMatchOperations() {
        System.out.println("=== Match Operations ===");

        List<Integer> numbers = List.of(2, 4, 6, 8, 10);

        // anyMatch - at least one matches
        boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        boolean hasOdd = numbers.stream().anyMatch(n -> n % 2 != 0);
        System.out.println("  anyMatch even: " + hasEven);
        System.out.println("  anyMatch odd: " + hasOdd);

        // allMatch - all match
        boolean allEven = numbers.stream().allMatch(n -> n % 2 == 0);
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        System.out.println("  allMatch even: " + allEven);
        System.out.println("  allMatch positive: " + allPositive);

        // noneMatch - none match
        boolean noNegatives = numbers.stream().noneMatch(n -> n < 0);
        boolean noOdd = numbers.stream().noneMatch(n -> n % 2 != 0);
        System.out.println("  noneMatch negative: " + noNegatives);
        System.out.println("  noneMatch odd: " + noOdd);
        System.out.println();
    }

    /**
     * Flattening nested collections.
     */
    private static void demonstrateFlatMap() {
        System.out.println("=== FlatMap ===");

        // Flatten nested lists
        List<List<Integer>> nested = List.of(
                List.of(1, 2, 3),
                List.of(4, 5, 6),
                List.of(7, 8, 9)
        );

        List<Integer> flat = nested.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println("  Flattened: " + flat);

        // Flatten strings to characters
        List<String> words = List.of("Hello", "World");
        List<Character> chars = words.stream()
                .flatMap(word -> word.chars()
                        .mapToObj(c -> (char) c))
                .collect(Collectors.toList());
        System.out.println("  Characters: " + chars);

        // Flatten with split
        List<String> sentences = List.of("Java is great", "Streams are powerful");
        List<String> allWords = sentences.stream()
                .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
                .collect(Collectors.toList());
        System.out.println("  All words: " + allWords);
        System.out.println();
    }

    /**
     * Grouping elements by a classifier.
     */
    private static void demonstrateGroupingBy() {
        System.out.println("=== GroupingBy ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve", "Adam", "Cathy");

        // Group by first letter
        Map<Character, List<String>> byFirstLetter = names.stream()
                .collect(Collectors.groupingBy(name -> name.charAt(0)));
        System.out.println("  By first letter: " + byFirstLetter);

        // Group by length
        Map<Integer, List<String>> byLength = names.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println("  By length: " + byLength);

        // Group by with counting
        Map<Character, Long> countByLetter = names.stream()
                .collect(Collectors.groupingBy(
                        name -> name.charAt(0),
                        Collectors.counting()
                ));
        System.out.println("  Count by letter: " + countByLetter);

        // Group by with downstream mapping
        Map<Character, List<Integer>> lengthsByLetter = names.stream()
                .collect(Collectors.groupingBy(
                        name -> name.charAt(0),
                        Collectors.mapping(String::length, Collectors.toList())
                ));
        System.out.println("  Lengths by letter: " + lengthsByLetter);
        System.out.println();
    }

    /**
     * Partitioning elements into two groups (true/false).
     */
    private static void demonstratePartitioningBy() {
        System.out.println("=== PartitioningBy ===");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Partition even/odd
        Map<Boolean, List<Integer>> partitioned = numbers.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("  Even: " + partitioned.get(true));
        System.out.println("  Odd: " + partitioned.get(false));

        // Partition by length threshold
        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");
        Map<Boolean, List<String>> byLength = names.stream()
                .collect(Collectors.partitioningBy(name -> name.length() > 3));
        System.out.println("  Long names: " + byLength.get(true));
        System.out.println("  Short names: " + byLength.get(false));

        // Partition with downstream counting
        Map<Boolean, Long> counts = numbers.stream()
                .collect(Collectors.partitioningBy(
                        n -> n > 5,
                        Collectors.counting()
                ));
        System.out.println("  Count >5: " + counts.get(true) + ", <=5: " + counts.get(false));
        System.out.println();
    }

    /**
     * Joining strings.
     */
    private static void demonstrateJoining() {
        System.out.println("=== Joining ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");

        // Simple join
        String joined = names.stream()
                .collect(Collectors.joining(", "));
        System.out.println("  Joined: " + joined);

        // Join with prefix and suffix
        String formatted = names.stream()
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("  Formatted: " + formatted);

        // Join after transformation
        String upperJoined = names.stream()
                .map(String::toUpperCase)
                .collect(Collectors.joining(" | "));
        System.out.println("  Uppercase: " + upperJoined);
        System.out.println();
    }

    /**
     * Counting elements.
     */
    private static void demonstrateCounting() {
        System.out.println("=== Counting ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");

        // Count all
        long count = names.stream().count();
        System.out.println("  Total: " + count);

        // Count filtered
        long longNames = names.stream()
                .filter(name -> name.length() > 4)
                .count();
        System.out.println("  Longer than 4: " + longNames);

        // Count with grouping
        Map<Character, Long> byFirstLetter = names.stream()
                .collect(Collectors.groupingBy(
                        name -> name.charAt(0),
                        Collectors.counting()
                ));
        System.out.println("  By first letter: " + byFirstLetter);
        System.out.println();
    }

    /**
     * Numeric operations: summing, averaging, summarizing.
     */
    private static void demonstrateNumericOperations() {
        System.out.println("=== Numeric Operations ===");

        List<Integer> numbers = List.of(10, 20, 30, 40, 50);

        // Sum
        int sum = numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println("  Sum: " + sum);

        // Average
        OptionalDouble average = numbers.stream()
                .mapToInt(Integer::intValue)
                .average();
        average.ifPresent(a -> System.out.println("  Average: " + a));

        // Min and Max
        OptionalInt min = numbers.stream()
                .mapToInt(Integer::intValue)
                .min();
        OptionalInt max = numbers.stream()
                .mapToInt(Integer::intValue)
                .max();
        min.ifPresent(m -> System.out.println("  Min: " + m));
        max.ifPresent(m -> System.out.println("  Max: " + m));

        // Summary statistics
        IntSummaryStatistics stats = numbers.stream()
                .mapToInt(Integer::intValue)
                .summaryStatistics();
        System.out.println("  Stats: " + stats);

        // Summing with collectors
        int sumCollect = numbers.stream()
                .collect(Collectors.summingInt(Integer::intValue));
        System.out.println("  Sum (collector): " + sumCollect);

        // Averaging with collectors
        Double avgCollect = numbers.stream()
                .collect(Collectors.averagingInt(Integer::intValue));
        System.out.println("  Average (collector): " + avgCollect);
        System.out.println();
    }

    /**
     * Demonstrates chaining multiple operations together.
     */
    private static void demonstrateChainedOperations() {
        System.out.println("=== Chained Operations ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve", "Adam", "Cathy");

        // Complex query: group long names by first letter, sorted
        Map<Character, List<String>> result = names.stream()
                .filter(name -> name.length() > 3)
                .sorted()
                .collect(Collectors.groupingBy(
                        name -> name.charAt(0),
                        TreeMap::new,
                        Collectors.toList()
                ));
        System.out.println("  Long names grouped: " + result);

        // Pipeline: filter, transform, reduce
        String longestName = names.stream()
                .filter(name -> name.length() > 3)
                .reduce("", (a, b) -> a.length() > b.length() ? a : b);
        System.out.println("  Longest name: " + longestName);

        // Pipeline: flat, filter, collect
        List<Character> vowels = names.stream()
                .flatMap(word -> word.chars().mapToObj(c -> (char) c))
                .filter(c -> "AEIOU".contains(c.toString()))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("  Unique vowels: " + vowels);
    }
}
