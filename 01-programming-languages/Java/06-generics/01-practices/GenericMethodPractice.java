package academy.javaengineering.generics.practices;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Generic Method Practice Exercises
 *
 * <p>Complexity: O(n) for most operations</p>
 * <p>Thread-safety: Not thread-safe</p>
 * <p>Key characteristics: Practice exercises for identity, asList, swap, max, and filter generic methods</p>
 */
public class GenericMethodPractice {

    // ============================================================
    // Exercise 1: Identity Method
    // ============================================================
    // Create a generic method that:
    // - Takes a parameter of type T
    // - Returns the same value unchanged
    // - The method signature should be: public static <T> T identity(T value)

    // TODO: Implement the identity method here


    // ============================================================
    // Exercise 2: AsList Method
    // ============================================================
    // Create a generic method that:
    // - Takes a varargs of type T
    // - Returns a List<T> containing all the elements
    // - The method signature should be: public static <T> List<T> asList(T... elements)

    // TODO: Implement the asList method here


    // ============================================================
    // Exercise 3: Swap Method
    // ============================================================
    // Create a generic method that:
    // - Takes a List<T> and two indices (int i, int j)
    // - Swaps the elements at the given indices
    // - Returns void (modifies the list in place)
    // - Should handle index out of bounds gracefully

    // TODO: Implement the swap method here


    // ============================================================
    // Exercise 4: Max Method
    // ============================================================
    // Create a generic method that:
    // - Takes a List<T> where T implements Comparable<T>
    // - Returns the maximum element in the list
    // - Throws IllegalArgumentException if the list is null or empty
    // - The method signature should be: public static <T extends Comparable<T>> T max(List<T> list)

    // TODO: Implement the max method here


    // ============================================================
    // Exercise 5: Filter Method
    // ============================================================
    // Create a generic method that:
    // - Takes a List<T> and a Predicate<T>
    // - Returns a new List<T> containing only elements that match the predicate
    // - Does not modify the original list
    // - The method signature should be:
    //   public static <T> List<T> filter(List<T> list, java.util.function.Predicate<T> predicate)

    // TODO: Implement the filter method here


    // ============================================================
    // Test your implementations
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Exercise 1: Identity Method ===");
        // TODO: Uncomment and test your identity method
        // String str = identity("hello");
        // Integer num = identity(42);
        // System.out.println(str);
        // System.out.println(num);

        System.out.println("\n=== Exercise 2: AsList Method ===");
        // TODO: Uncomment and test your asList method
        // List<String> strings = asList("a", "b", "c");
        // List<Integer> numbers = asList(1, 2, 3, 4, 5);
        // System.out.println(strings);
        // System.out.println(numbers);

        System.out.println("\n=== Exercise 3: Swap Method ===");
        // TODO: Uncomment and test your swap method
        // List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        // System.out.println("Before swap: " + list);
        // swap(list, 0, 3);
        // System.out.println("After swap: " + list);

        System.out.println("\n=== Exercise 4: Max Method ===");
        // TODO: Uncomment and test your max method
        // List<Integer> numbers = Arrays.asList(3, 7, 2, 8, 1);
        // List<String> words = Arrays.asList("apple", "banana", "cherry");
        // System.out.println("Max number: " + max(numbers));
        // System.out.println("Max word: " + max(words));

        System.out.println("\n=== Exercise 5: Filter Method ===");
        // TODO: Uncomment and test your filter method
        // List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        // List<Integer> even = filter(numbers, n -> n % 2 == 0);
        // List<Integer> greaterThanFour = filter(numbers, n -> n > 4);
        // System.out.println("Even: " + even);
        // System.out.println("Greater than 4: " + greaterThanFour);
    }
}
