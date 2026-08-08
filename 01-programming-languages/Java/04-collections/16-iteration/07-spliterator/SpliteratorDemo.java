import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Demonstrates Spliterator parallel processing patterns.
 */
public class SpliteratorDemo {

    public static void main(String[] args) {
        basicSpliterator();
        trySplitDemo();
        characteristics();
        forEachRemaining();
    }

    static void basicSpliterator() {
        System.out.println("=== Basic Spliterator ===");
        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana");
        Spliterator<String> spliterator = names.spliterator();

        System.out.println("Estimated size: " + spliterator.estimateSize());
        System.out.println("Exact size: " + spliterator.getExactSizeIfKnown());

        while (spliterator.tryAdvance(System.out::println)) {
            // Processed inside tryAdvance
        }
        System.out.println();
    }

    static void trySplitDemo() {
        System.out.println("=== trySplit Demo ===");
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            numbers.add(i);
        }

        Spliterator<Integer> original = numbers.spliterator();
        Spliterator<Integer> left = original.trySplit();

        if (left != null) {
            System.out.print("Left half: ");
            left.forEachRemaining(n -> System.out.print(n + " "));
            System.out.println();
        }

        System.out.print("Right half: ");
        original.forEachRemaining(n -> System.out.print(n + " "));
        System.out.println();
        System.out.println();
    }

    static void characteristics() {
        System.out.println("=== Characteristics ===");
        List<String> list = List.of("A", "B", "C");
        Spliterator<String> spliterator = list.spliterator();

        int chars = spliterator.characteristics();
        System.out.println("ORDERED: " + ((chars & Spliterator.ORDERED) != 0));
        System.out.println("SIZED: " + ((chars & Spliterator.SIZED) != 0));
        System.out.println("IMMUTABLE: " + ((chars & Spliterator.IMMUTABLE) != 0));
        System.out.println("NONNULL: " + ((chars & Spliterator.NONNULL) != 0));
        System.out.println();
    }

    static void forEachRemaining() {
        System.out.println("=== forEachRemaining ===");
        List<String> names = List.of("Alice", "Bob", "Charlie");
        Spliterator<String> spliterator = names.spliterator();

        spliterator.tryAdvance(name -> System.out.println("First: " + name));
        System.out.println("Remaining:");
        spliterator.forEachRemaining(name -> System.out.println("  " + name));
        System.out.println();
    }
}
