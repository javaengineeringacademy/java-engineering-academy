package strings;

/**
 * StringBuilderDemo - Demonstrates StringBuilder vs StringBuffer
 *
 * Key differences:
 * - StringBuilder: Not thread-safe, faster, for single-threaded use
 * - StringBuffer: Thread-safe (synchronized), slower, for multi-threaded use
 * - Both are mutable, unlike String
 */
public class StringBuilderDemo {

    public static void main(String[] args) {
        System.out.println("=== StringBuilder Basics ===");
        stringBuilderBasics();

        System.out.println("\n=== StringBuilder Methods ===");
        stringBuilderMethods();

        System.out.println("\n=== Performance Comparison ===");
        performanceComparison();

        System.out.println("\n=== StringBuffer (Thread-Safe) ===");
        stringBufferDemo();

        System.out.println("\n=== Chaining Methods ===");
        methodChaining();
    }

    static void stringBuilderBasics() {
        // Creating StringBuilder
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder("Initial");
        StringBuilder sb3 = new StringBuilder(50); // with capacity

        System.out.println("Empty SB: " + sb1);
        System.out.println("With initial: " + sb2);
        System.out.println("Capacity: " + sb3.capacity());

        // Append - adds to end
        sb1.append("Hello");
        sb1.append(' ');
        sb1.append("World");
        System.out.println("After append: " + sb1);

        // Insert - adds at position
        sb1.insert(5, ",");
        System.out.println("After insert: " + sb1);

        // Delete - removes characters
        sb1.delete(5, 6);
        System.out.println("After delete: " + sb1);

        // Replace - replaces characters
        sb1.replace(6, 11, "Java");
        System.out.println("After replace: " + sb1);

        // Reverse - reverses the string
        StringBuilder reversed = new StringBuilder("Hello");
        reversed.reverse();
        System.out.println("Reversed: " + reversed);
    }

    static void stringBuilderMethods() {
        StringBuilder sb = new StringBuilder("Hello, World!");

        // charAt and length
        System.out.println("charAt(0): " + sb.charAt(0));
        System.out.println("length(): " + sb.length());

        // substring
        System.out.println("substring(7): " + sb.substring(7));
        System.out.println("substring(0, 5): " + sb.substring(0, 5));

        // indexOf
        System.out.println("indexOf('World'): " + sb.indexOf("World"));

        // capacity vs length
        System.out.println("capacity(): " + sb.capacity());
        System.out.println("length(): " + sb.length());

        // Ensure capacity
        sb.ensureCapacity(100);
        System.out.println("After ensureCapacity(100): " + sb.capacity());

        // Set length - truncates or pads with null chars
        sb.setLength(5);
        System.out.println("After setLength(5): " + sb);

        // toString
        String result = sb.toString();
        System.out.println("toString(): " + result);
    }

    static void performanceComparison() {
        int iterations = 100000;

        // String concatenation (slow - creates many objects)
        long startTime = System.currentTimeMillis();
        String str = "";
        for (int i = 0; i < iterations; i++) {
            str += "a";
        }
        long stringTime = System.currentTimeMillis() - startTime;

        // StringBuilder (fast - mutable)
        startTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        String sbResult = sb.toString();
        long sbTime = System.currentTimeMillis() - startTime;

        System.out.println("String concatenation: " + stringTime + "ms");
        System.out.println("StringBuilder: " + sbTime + "ms");
        System.out.println("StringBuilder is " + (stringTime / Math.max(sbTime, 1)) + "x faster");
    }

    static void stringBufferDemo() {
        // StringBuffer is synchronized (thread-safe)
        StringBuffer buffer = new StringBuffer("Thread-Safe ");

        // Same methods as StringBuilder
        buffer.append("String Buffer");
        buffer.insert(12, "Content: ");
        buffer.delete(0, 12);

        System.out.println("StringBuffer: " + buffer);

        // Thread safety demonstration
        StringBuffer sharedBuffer = new StringBuffer();

        // Simulating multiple threads (conceptual)
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                sharedBuffer.append("A");
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                sharedBuffer.append("B");
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("StringBuffer length (thread-safe): " + sharedBuffer.length());
    }

    static void methodChaining() {
        // StringBuilder supports method chaining
        String result = new StringBuilder()
            .append("Name: ")
            .append("John")
            .append(", Age: ")
            .append(30)
            .append(", City: ")
            .append("New York")
            .toString();

        System.out.println("Chained result: " + result);

        // Building a query string
        String query = new StringBuilder()
            .append("SELECT * FROM users")
            .append(" WHERE age > ")
            .append(18)
            .append(" AND status = 'active'")
            .append(" ORDER BY name")
            .toString();

        System.out.println("Query: " + query);
    }
}