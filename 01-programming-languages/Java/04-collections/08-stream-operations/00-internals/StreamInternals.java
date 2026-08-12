package academy.javaengineering.collections.stream.internals;

import java.util.*;
import java.util.stream.*;

public class StreamInternals {

    public static void main(String[] args) {
        System.out.println("=== Stream API Internals ===\n");

        // 1. Stream creation
        System.out.println("--- Stream Creation ---");
        List<String> list = Arrays.asList("Java", "Python", "JavaScript");
        Stream<String> fromList = list.stream();
        Stream<String> fromArray = Arrays.stream(new String[]{"A", "B", "C"});
        Stream<Integer> generated = Stream.iterate(0, n -> n + 2).limit(5);
        System.out.println("list.stream(), Arrays.stream(), Stream.iterate()");

        // 2. Intermediate operations (lazy)
        System.out.println("\n--- Intermediate Operations (Lazy) ---");
        Stream<String> intermediate = list.stream()
            .filter(s -> s.length() > 3)
            .map(String::toUpperCase)
            .sorted();
        System.out.println("filter, map, sorted are lazy");
        System.out.println("No computation until terminal op");

        // 3. Terminal operations
        System.out.println("\n--- Terminal Operations ---");
        List<String> result = list.stream()
            .filter(s -> s.length() > 3)
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("Result: " + result);

        // 4. Reduction operations
        System.out.println("\n--- Reduction Operations ---");
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
        int sum = nums.stream().reduce(0, Integer::sum);
        System.out.println("reduce(0, sum): " + sum);

        // 5. Collector internals
        System.out.println("\n--- Collector Internals ---");
        Map<Boolean, List<Integer>> partitioned = nums.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("partitioningBy: " + partitioned);

        // 6. Parallel streams
        System.out.println("\n--- Parallel Streams ---");
        long count = IntStream.range(1, 1000000)
            .parallel()
            .filter(n -> n % 2 == 0)
            .count();
        System.out.println("Parallel count: " + count);
        System.out.println("Uses ForkJoinPool.commonPool()");
    }
}
