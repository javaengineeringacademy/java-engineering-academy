import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Wildcard Practice Exercises
 * Complete each exercise using the appropriate wildcard type.
 */
public class WildcardPractice {

    // ============================================================
    // Exercise 1: Sum Method
    // ============================================================
    // Create a method that:
    // - Takes a List<? extends Number>
    // - Returns the sum of all elements as a double
    // - Uses wildcard with upper bound to accept any Number subtype

    // TODO: Implement the sum method here


    // ============================================================
    // Exercise 2: AddAll Method
    // ============================================================
    // Create a method that:
    // - Takes a List<T> destination and List<? extends T> source
    // - Adds all elements from source to destination
    // - Uses wildcard with upper bound for source parameter

    // TODO: Implement the addAll method here


    // ============================================================
    // Exercise 3: Copy Method
    // ============================================================
    // Create a method that:
    // - Takes two List<? super T> parameters (source and destination)
    // - Copies all elements from source to destination
    // - Uses wildcard with lower bound for both parameters

    // TODO: Implement the copy method here


    // ============================================================
    // Exercise 4: Min Method
    // ============================================================
    // Create a method that:
    // - Takes a List<T> where T implements Comparable<T>
    // - Returns the minimum element
    // - Does not use wildcards but uses bounded type parameter

    // TODO: Implement the min method here


    // ============================================================
    // Exercise 5: PrintAll Method
    // ============================================================
    // Create a method that:
    // - Takes a List<?> (unbounded wildcard)
    // - Prints all elements to the console
    // - Works with any type of list

    // TODO: Implement the printAll method here


    // ============================================================
    // Test your implementations
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Exercise 1: Sum Method ===");
        // TODO: Uncomment and test your sum method
        // List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        // List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);
        // System.out.println("Sum of integers: " + sum(integers));
        // System.out.println("Sum of doubles: " + sum(doubles));

        System.out.println("\n=== Exercise 2: AddAll Method ===");
        // TODO: Uncomment and test your addAll method
        // List<Number> numbers = new ArrayList<>(Arrays.asList(1, 2, 3));
        // List<Integer> moreNumbers = Arrays.asList(4, 5, 6);
        // addAll(numbers, moreNumbers);
        // System.out.println("Combined: " + numbers);

        System.out.println("\n=== Exercise 3: Copy Method ===");
        // TODO: Uncomment and test your copy method
        // List<Object> destination = new ArrayList<>();
        // List<String> source = Arrays.asList("hello", "world");
        // copy(source, destination);
        // System.out.println("Copied: " + destination);

        System.out.println("\n=== Exercise 4: Min Method ===");
        // TODO: Uncomment and test your min method
        // List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9);
        // List<String> words = Arrays.asList("cherry", "apple", "banana");
        // System.out.println("Min number: " + min(numbers));
        // System.out.println("Min word: " + min(words));

        System.out.println("\n=== Exercise 5: PrintAll Method ===");
        // TODO: Uncomment and test your printAll method
        // List<String> strings = Arrays.asList("a", "b", "c");
        // List<Integer> numbers = Arrays.asList(1, 2, 3);
        // printAll(strings);
        // printAll(numbers);
    }
}
