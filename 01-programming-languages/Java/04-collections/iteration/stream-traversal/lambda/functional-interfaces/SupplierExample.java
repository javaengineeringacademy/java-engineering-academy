import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class SupplierExample {

    public static void main(String[] args) {
        // Basic supplier usage
        Supplier<String> helloSupplier = () -> "Hello, World!";
        System.out.println("Supplier result: " + helloSupplier.get());

        // Supplier with Stream.generate
        Supplier<Double> randomSupplier = Math::random;
        List<Double> randoms = Stream.generate(randomSupplier)
            .limit(5)
            .collect(Collectors.toList());
        System.out.println("Random numbers: " + randoms);

        // Supplier for factory pattern
        Supplier<ArrayList<String>> listFactory = ArrayList::new;
        ArrayList<String> newList = listFactory.get();
        newList.add("created by supplier");
        System.out.println("Factory created list: " + newList);

        // Supplier with different collections
        Supplier<HashSet<Integer>> setFactory = HashSet::new;
        Supplier<LinkedList<String>> linkedListFactory = LinkedList::new;
        Supplier<TreeMap<String, Integer>> treeMapFactory = TreeMap::new;

        HashSet<Integer> set = setFactory.get();
        LinkedList<String> linkedList = linkedListFactory.get();
        TreeMap<String, Integer> treeMap = treeMapFactory.get();

        set.add(1);
        linkedList.add("first");
        treeMap.put("key", 1);

        System.out.println("Set: " + set);
        System.out.println("LinkedList: " + linkedList);
        System.out.println("TreeMap: " + treeMap);

        // Supplier for lazy initialization
        Supplier<String> lazyValue = () -> {
            System.out.println("  Computing value...");
            return "expensive computation result";
        };

        System.out.println("Before get:");
        String value = lazyValue.get(); // computation happens here
        System.out.println("After get: " + value);

        // Supplier with Stream.iterate
        Supplier<Integer> counter = new Supplier<Integer>() {
            private int count = 0;
            @Override
            public Integer get() {
                return count++;
            }
        };

        // Note: stateful suppliers should be used with caution
        // This example demonstrates the concept
        System.out.println("\nCounter values:");
        for (int i = 0; i < 5; i++) {
            System.out.print(counter.get() + " ");
        }
        System.out.println();

        // Supplier for creating default objects
        Supplier<Map<String, List<Integer>>> mapFactory = () -> new HashMap<>();
        Map<String, List<Integer>> grouped = mapFactory.get();

        // Use supplier in stream collection
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
        Map<Integer, List<String>> byLength = names.stream()
            .collect(Collectors.groupingBy(
                String::length,
                mapFactory,
                Collectors.toList()
            ));
        System.out.println("\nGrouped by length: " + byLength);

        // Supplier for conditional creation
        Supplier<String> greeting = () -> {
            int hour = java.time.LocalTime.now().getHour();
            if (hour < 12) return "Good Morning";
            if (hour < 17) return "Good Afternoon";
            return "Good Evening";
        };
        System.out.println("\nGreeting: " + greeting.get());

        // Supplier with Optional
        Supplier<Optional<String>> optionalFactory = () -> Optional.of("present");
        Optional<String> optional = optionalFactory.get();
        System.out.println("Optional value: " + optional.orElse("empty"));

        // Supplier for generating sequences
        Supplier<List<Integer>> numberSequence = () -> {
            List<Integer> nums = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                nums.add(i * i);
            }
            return nums;
        };

        List<Integer> squares = numberSequence.get();
        System.out.println("Squares: " + squares);

        // Supplier with memoization concept
        class MemoizedSupplier<T> implements Supplier<T> {
            private final Supplier<T> delegate;
            private T cached;
            private boolean computed = false;

            MemoizedSupplier(Supplier<T> delegate) {
                this.delegate = delegate;
            }

            @Override
            public synchronized T get() {
                if (!computed) {
                    cached = delegate.get();
                    computed = true;
                }
                return cached;
            }
        }

        Supplier<Integer> expensiveComputation = () -> {
            System.out.println("  Running expensive computation...");
            return 42;
        };

        MemoizedSupplier<Integer> memoized = new MemoizedSupplier<>(expensiveComputation);
        System.out.println("\nFirst call:");
        System.out.println("Result: " + memoized.get());
        System.out.println("Second call (cached):");
        System.out.println("Result: " + memoized.get());
    }
}
