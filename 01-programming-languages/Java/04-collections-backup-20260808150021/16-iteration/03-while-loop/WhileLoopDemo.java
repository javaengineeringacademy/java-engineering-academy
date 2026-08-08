import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Demonstrates while loop iteration patterns.
 */
public class WhileLoopDemo {

    public static void main(String[] args) {
        basicWhile();
        doWhileExample();
        sentinelValue();
        retryPattern();
        listTraversal();
    }

    static void basicWhile() {
        System.out.println("=== Basic While Loop ===");
        int count = 0;
        while (count < 5) {
            System.out.println("Count: " + count);
            count++;
        }
        System.out.println();
    }

    static void doWhileExample() {
        System.out.println("=== Do-While Loop ===");
        int i = 10;
        do {
            System.out.println("Value: " + i);
            i++;
        } while (i < 5);  // Executes once even though condition is false
        System.out.println();
    }

    static void sentinelValue() {
        System.out.println("=== Sentinel Value Pattern ===");
        List<String> inputs = List.of("hello", "world", "quit", "extra");
        int index = 0;

        while (index < inputs.size()) {
            String input = inputs.get(index);
            if (input.equals("quit")) break;
            System.out.println("Processing: " + input);
            index++;
        }
        System.out.println();
    }

    static void retryPattern() {
        System.out.println("=== Retry with Backoff ===");
        int attempts = 0;
        int maxAttempts = 3;

        while (attempts < maxAttempts) {
            try {
                System.out.println("Attempt " + (attempts + 1));
                if (attempts == 2) {
                    System.out.println("Success!");
                    break;
                }
                throw new RuntimeException("Simulated failure");
            } catch (Exception e) {
                attempts++;
                System.out.println("Failed: " + e.getMessage());
            }
        }
        System.out.println();
    }

    static void listTraversal() {
        System.out.println("=== While with Iterator ===");
        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));
        java.util.Iterator<String> it = names.iterator();

        while (it.hasNext()) {
            System.out.println("Name: " + it.next());
        }
        System.out.println();
    }
}
