import java.util.*;
import java.util.stream.Collectors;

/**
 * Comprehensive comparison of all iteration methods in Java.
 * Covers traditional loops, enhanced for-each, lambda, iterator, streams, and recursion.
 * Includes performance comparison and guidance on when to use each approach.
 */
public class IterationDemo {

    public static void main(String[] args) {
        demonstrateForLoop();
        demonstrateForEachLoop();
        demonstrateForEachWithLambda();
        demonstrateForEachWithMethodRef();
        demonstrateIteratorPattern();
        demonstrateStreamForEach();
        demonstrateRecursion();
        comparePerformance();
        demonstrateModificationCapabilities();
        demonstrateEarlyBreakCapabilities();
    }

    /**
     * Traditional for loop with index access.
     * Provides index-based access and full control over iteration.
     */
    private static void demonstrateForLoop() {
        System.out.println("=== Traditional For Loop ===");

        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie", "Diana", "Eve"));

        for (int i = 0; i < names.size(); i++) {
            System.out.printf("  [%d] = %s%n", i, names.get(i));
        }

        // Reverse iteration
        System.out.print("  Reverse: ");
        for (int i = names.size() - 1; i >= 0; i--) {
            System.out.print(names.get(i) + " ");
        }
        System.out.println();

        // Step iteration
        System.out.print("  Every 2nd: ");
        for (int i = 0; i < names.size(); i += 2) {
            System.out.print(names.get(i) + " ");
        }
        System.out.println("\n");
    }

    /**
     * Enhanced for-each loop (syntactic sugar over Iterator).
     * Cleanest syntax but limited control.
     */
    private static void demonstrateForEachLoop() {
        System.out.println("=== Enhanced For-Each Loop ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");

        for (String name : names) {
            System.out.println("  " + name);
        }

        // Works with any Iterable
        Set<Integer> numbers = new LinkedHashSet<>(Set.of(10, 20, 30, 40, 50));
        System.out.print("  Set: ");
        for (int num : numbers) {
            System.out.print(num + " ");
        }
        System.out.println("\n");
    }

    /**
     * forEach with lambda expression.
     * Functional approach, cannot break early or access index.
     */
    private static void demonstrateForEachWithLambda() {
        System.out.println("=== forEach with Lambda ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");

        names.forEach(name -> System.out.println("  " + name));

        // Lambda with multi-line body
        System.out.println("  Uppercase names:");
        names.forEach(name -> {
            String upper = name.toUpperCase();
            System.out.println("    " + upper);
        });
        System.out.println();
    }

    /**
     * forEach with method reference.
     * Most concise form when just calling a single method.
     */
    private static void demonstrateForEachWithMethodRef() {
        System.out.println("=== forEach with Method Reference ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");

        names.forEach(System.out::println);

        // Method reference to instance method
        List<StringBuilder> builders = new ArrayList<>();
        builders.add(new StringBuilder("Hello"));
        builders.add(new StringBuilder("World"));
        builders.forEach(StringBuilder::reverse);

        // Method reference with custom method
        names.forEach(IterationDemo::processName);
        System.out.println();
    }

    private static void processName(String name) {
        System.out.println("  Processing: " + name);
    }

    /**
     * Iterator pattern with hasNext(), next(), and remove().
     * The only safe way to modify a collection during iteration.
     */
    private static void demonstrateIteratorPattern() {
        System.out.println("=== Iterator Pattern ===");

        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie", "Diana", "Eve"));

        // Forward iteration with Iterator
        Iterator<String> it = names.iterator();
        System.out.print("  Forward: ");
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        // Safe removal during iteration
        Iterator<String> removeIt = names.iterator();
        while (removeIt.hasNext()) {
            String name = removeIt.next();
            if (name.length() <= 3) {
                removeIt.remove();
            }
        }
        System.out.println("  After removing short names: " + names);

        // ListIterator for bidirectional traversal
        LinkedList<Integer> numbers = new LinkedList<>(List.of(1, 2, 3, 4, 5));
        System.out.print("  Backward: ");
        ListIterator<Integer> backwardIt = numbers.listIterator(numbers.size());
        while (backwardIt.hasPrevious()) {
            System.out.print(backwardIt.previous() + " ");
        }
        System.out.println("\n");
    }

    /**
     * Stream forEach for functional-style iteration.
     * Supports parallel processing and chaining with other stream operations.
     */
    private static void demonstrateStreamForEach() {
        System.out.println("=== Stream forEach ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");

        // Sequential stream
        System.out.println("  Sequential:");
        names.stream()
                .filter(name -> name.length() > 3)
                .map(String::toUpperCase)
                .forEach(name -> System.out.println("    " + name));

        // Parallel stream
        System.out.println("  Parallel:");
        names.parallelStream()
                .forEach(name -> System.out.println("    " + Thread.currentThread().getName() + ": " + name));

        // Stream with collect
        List<String> result = names.stream()
                .filter(name -> name.startsWith("A") || name.startsWith("D"))
                .collect(Collectors.toList());
        System.out.println("  Filtered (A or D): " + result);
        System.out.println();
    }

    /**
     * Recursive iteration for tree-like structures.
     * Elegant for hierarchical data but risks stack overflow.
     */
    private static void demonstrateRecursion() {
        System.out.println("=== Recursion ===");

        // Simple factorial
        System.out.println("  Factorial of 5: " + factorial(5));

        // Tree traversal
        TreeNode root = buildSampleTree();
        System.out.print("  Pre-order: ");
        preOrderTraversal(root);
        System.out.println();

        System.out.print("  In-order: ");
        inOrderTraversal(root);
        System.out.println();

        System.out.print("  Post-order: ");
        postOrderTraversal(root);
        System.out.println();

        // Fibonacci
        System.out.println("  Fibonacci(10): " + fibonacci(10));
        System.out.println();
    }

    private static long factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    private static long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    private static TreeNode buildSampleTree() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);
        return root;
    }

    private static void preOrderTraversal(TreeNode node) {
        if (node == null) return;
        System.out.print(node.value + " ");
        preOrderTraversal(node.left);
        preOrderTraversal(node.right);
    }

    private static void inOrderTraversal(TreeNode node) {
        if (node == null) return;
        inOrderTraversal(node.left);
        System.out.print(node.value + " ");
        inOrderTraversal(node.right);
    }

    private static void postOrderTraversal(TreeNode node) {
        if (node == null) return;
        postOrderTraversal(node.left);
        postOrderTraversal(node.right);
        System.out.print(node.value + " ");
    }

    /**
     * Compares performance of different iteration approaches.
     */
    private static void comparePerformance() {
        System.out.println("=== Performance Comparison ===");

        int size = 1_000_000;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        long start, end;

        // Traditional for loop
        start = System.nanoTime();
        long sum1 = 0;
        for (int i = 0; i < list.size(); i++) {
            sum1 += list.get(i);
        }
        end = System.nanoTime();
        System.out.printf("  For loop:        %,d ms (sum=%d)%n", (end - start) / 1_000_000, sum1);

        // Enhanced for-each
        start = System.nanoTime();
        long sum2 = 0;
        for (int num : list) {
            sum2 += num;
        }
        end = System.nanoTime();
        System.out.printf("  For-each:        %,d ms (sum=%d)%n", (end - start) / 1_000_000, sum2);

        // Iterator
        start = System.nanoTime();
        long sum3 = 0;
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            sum3 += it.next();
        }
        end = System.nanoTime();
        System.out.printf("  Iterator:        %,d ms (sum=%d)%n", (end - start) / 1_000_000, sum3);

        // forEach with lambda
        start = System.nanoTime();
        long[] sum4 = {0};
        list.forEach(num -> sum4[0] += num);
        end = System.nanoTime();
        System.out.printf("  forEach lambda:  %,d ms (sum=%d)%n", (end - start) / 1_000_000, sum4[0]);

        // Stream forEach
        start = System.nanoTime();
        long[] sum5 = {0};
        list.stream().forEach(num -> sum5[0] += num);
        end = System.nanoTime();
        System.out.printf("  Stream forEach:  %,d ms (sum=%d)%n", (end - start) / 1_000_000, sum5[0]);

        // Parallel stream
        start = System.nanoTime();
        long sum6 = list.parallelStream().mapToLong(Integer::longValue).sum();
        end = System.nanoTime();
        System.out.printf("  Parallel stream: %,d ms (sum=%d)%n", (end - start) / 1_000_000, sum6);

        System.out.println();
    }

    /**
     * Demonstrates which iteration methods can modify the collection.
     */
    private static void demonstrateModificationCapabilities() {
        System.out.println("=== Modification Capabilities ===");

        System.out.println("  FOR LOOP:     Can modify via set(index, value) or add/remove by index");
        System.out.println("  FOR-EACH:     Cannot modify (ConcurrentModificationException if list.remove())");
        System.out.println("  forEach():    Cannot modify (ConcurrentModificationException)");
        System.out.println("  ITERATOR:     Can safely modify via it.remove(), it.add(), it.set()");
        System.out.println("  STREAM:       Cannot modify source; collect to new collection");
        System.out.println("  RECURSION:    Can modify via passed collection reference");

        // Example: Safe removal with Iterator
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().length() <= 1) {
                it.remove();
            }
        }
        System.out.println("  After Iterator.remove(): " + list);
        System.out.println();
    }

    /**
     * Demonstrates which iteration methods support early break/termination.
     */
    private static void demonstrateEarlyBreakCapabilities() {
        System.out.println("=== Early Break Capabilities ===");

        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");

        // For loop - can break
        for (String name : names) {
            if (name.equals("Charlie")) {
                System.out.println("  For loop stopped at: " + name);
                break;
            }
        }

        // Enhanced for - can break
        for (String name : names) {
            if (name.equals("Diana")) {
                System.out.println("  For-each stopped at: " + name);
                break;
            }
        }

        // forEach lambda - CANNOT break (must use stream instead)
        // forEach uses a Consumer which has no mechanism to stop

        // Iterator - can break
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            if (it.next().equals("Bob")) {
                System.out.println("  Iterator stopped at: Bob");
                break;
            }
        }

        // Stream - can use findFirst(), anyMatch(), or limit()
        Optional<String> found = names.stream()
                .filter(name -> name.startsWith("D"))
                .findFirst();
        found.ifPresent(name -> System.out.println("  Stream found: " + name));

        // Stream with limit for early termination
        System.out.print("  Stream limit(3): ");
        names.stream()
                .limit(3)
                .forEach(name -> System.out.print(name + " "));
        System.out.println("\n");
    }

    /**
     * Simple tree node class for recursion demonstrations.
     */
    static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;

        TreeNode(int value) {
            this.value = value;
        }
    }
}
