import java.util.*;
import java.util.stream.*;

/**
 * Stream Exercises - 5 challenges
 *
 * Complete each method body. Each exercise uses Java Streams.
 * Run tests after completing to verify your solutions.
 */
public class StreamExercises {

    /**
     * Exercise 1: Convert For Loop to Stream
     * Filter even numbers, multiply by 2, and collect as list.
     *
     * Example: transform([1, 2, 3, 4, 5]) → [4, 8]
     */
    public static List<Integer> transform(int[] array) {
        // TODO: Implement using Stream
        return new ArrayList<>();
    }

    /**
     * Exercise 2: Stream.iterate Sequence
     * Generate the first n powers of 2 using Stream.iterate.
     *
     * Example: powersOfTwo(5) → [1, 2, 4, 8, 16]
     */
    public static List<Long> powersOfTwo(int n) {
        // TODO: Implement using Stream.iterate
        return new ArrayList<>();
    }

    /**
     * Exercise 3: Parallel Word Count
     * Count the total number of words in all strings using parallel stream.
     *
     * Example: parallelWordCount(["hello world", "foo bar baz"]) → 5
     */
    public static int parallelWordCount(List<String> strings) {
        // TODO: Implement using parallel stream
        return 0;
    }

    /**
     * Exercise 4: Stream.generate Infinite
     * Generate a stream of n random integers between 1 and 100.
     *
     * Example: randomNumbers(5, 42) → [list of 5 numbers]
     */
    public static List<Integer> randomNumbers(int n, long seed) {
        // TODO: Implement using Stream.generate
        return new ArrayList<>();
    }

    /**
     * Exercise 5: Stream.concat
     * Concatenate two streams and collect unique sorted elements.
     *
     * Example: concatSorted([1, 3, 5], [2, 3, 6]) → [1, 2, 3, 5, 6]
     */
    public static List<Integer> concatSorted(List<Integer> list1, List<Integer> list2) {
        // TODO: Implement using Stream.concat
        return new ArrayList<>();
    }
}
