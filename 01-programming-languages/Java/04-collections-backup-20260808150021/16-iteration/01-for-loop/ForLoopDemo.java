import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates for loop iteration patterns over Java Collections.
 */
public class ForLoopDemo {

    public static void main(String[] args) {
        basicForLoop();
        reverseIteration();
        skipElements();
        nestedLoops();
        modificationDuringIteration();
    }

    static void basicForLoop() {
        System.out.println("=== Basic For Loop ===");
        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana");

        for (int i = 0; i < names.size(); i++) {
            System.out.println("Index " + i + ": " + names.get(i));
        }
        System.out.println();
    }

    static void reverseIteration() {
        System.out.println("=== Reverse Iteration ===");
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        for (int i = numbers.size() - 1; i >= 0; i--) {
            System.out.println("Reverse: " + numbers.get(i));
        }
        System.out.println();
    }

    static void skipElements() {
        System.out.println("=== Every Nth Element ===");
        int[] array = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

        System.out.println("Every 3rd element:");
        for (int i = 0; i < array.length; i += 3) {
            System.out.println("  array[" + i + "] = " + array[i]);
        }
        System.out.println();
    }

    static void nestedLoops() {
        System.out.println("=== Nested Loops ===");
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    static void modificationDuringIteration() {
        System.out.println("=== Safe Removal (Backwards) ===");
        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie", "Diana"));

        for (int i = names.size() - 1; i >= 0; i--) {
            if (names.get(i).length() <= 3) {
                System.out.println("Removing: " + names.get(i));
                names.remove(i);
            }
        }
        System.out.println("Remaining: " + names);
        System.out.println();
    }
}
