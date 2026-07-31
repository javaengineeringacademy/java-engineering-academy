package academy.javaengineering.oop.strings;

/**
 * Demonstrates StringBuilder and StringBuffer.
 * 
 * <p><b>String vs StringBuilder vs StringBuffer:</b>
 * <table border="1">
 * <tr><th>Class</th><th>Mutable</th><th>Thread-Safe</th><th>Performance</th></tr>
 * <tr><td>String</td><td>No</td><td>Yes (immutable)</td><td>Fast for reads</td></tr>
 * <tr><td>StringBuilder</td><td>Yes</td><td>No</td><td>Fastest for modifications</td></tr>
 * <tr><td>StringBuffer</td><td>Yes</td><td>Yes</td><td>Slower (synchronized)</td></tr>
 * </table>
 * 
 * <p><b>Real-world analogy:</b>
 * - String = Written in stone (immutable)
 * - StringBuilder = Whiteboard (mutable, single user)
 * - StringBuffer = Whiteboard with lock (mutable, shared)
 * 
 * <p><b>Best Practice:</b> Use StringBuilder for single-threaded string building.
 * Use StringBuffer only for legacy thread-safe code.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class StringBuilderDemo {

    private StringBuilderDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== StringBuilder & StringBuffer ===\n");

        // StringBuilder basics
        System.out.println("--- StringBuilder Basics ---");
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        System.out.println("Result: " + sb); // Hello World

        // Chaining
        sb = new StringBuilder("Start");
        sb.append(" -> ").append("Middle").append(" -> ").append("End");
        System.out.println("Chained: " + sb);

        // Capacity management
        System.out.println("\n--- Capacity ---");
        sb = new StringBuilder();
        System.out.println("Initial capacity: " + sb.capacity()); // 16
        sb.append("a".repeat(20));
        System.out.println("After 20 chars: " + sb.capacity()); // 34 (2*16+2)
        
        sb = new StringBuilder(100); // Pre-allocate
        System.out.println("Pre-allocated: " + sb.capacity()); // 100

        // Common operations
        System.out.println("\n--- Common Operations ---");
        sb = new StringBuilder("Hello World");
        sb.reverse();
        System.out.println("Reversed: " + sb); // dlroW olleH
        
        sb = new StringBuilder("Hello World");
        sb.delete(5, 6); // Delete space
        System.out.println("Delete space: " + sb); // HelloWorld
        
        sb = new StringBuilder("Hello World");
        sb.insert(5, " ");
        System.out.println("Insert space: " + sb); // Hello World
        
        sb = new StringBuilder("Java");
        sb.replace(0, 4, "Python"); // Replace all
        System.out.println("Replace: " + sb); // Python

        // Substring and char access
        System.out.println("\n--- Access ---");
        StringBuilder text = new StringBuilder("Java");
        System.out.println("charAt(0): " + text.charAt(0)); // J
        System.out.println("substring(1, 3): " + text.substring(1, 3)); // av
        System.out.println("indexOf('a'): " + text.indexOf("a")); // 1
        System.out.println("lastIndexOf('a'): " + text.lastIndexOf("a")); // 3

        // Performance: StringBuilder vs String concatenation
        System.out.println("\n--- Performance Demo ---");
        int iterations = 10000;
        
        // String concatenation (creates new objects)
        long start = System.nanoTime();
        String str = "";
        for (int i = 0; i < iterations; i++) {
            str += i;
        }
        long stringTime = System.nanoTime() - start;
        
        // StringBuilder
        start = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append(i);
        }
        long builderTime = System.nanoTime() - start;
        
        System.out.println("String += : " + stringTime / 1_000_000 + " ms");
        System.out.println("StringBuilder: " + builderTime / 1_000_000 + " ms");
        System.out.println("Speedup: " + (stringTime / builderTime) + "x faster");

        // StringBuffer (thread-safe, slower)
        System.out.println("\n--- StringBuffer ---");
        StringBuffer sbf = new StringBuffer("Thread-safe");
        sbf.append(" buffer");
        System.out.println("StringBuffer: " + sbf);
        
        // Show synchronization difference
        // StringBuffer methods are synchronized - only use when sharing between threads

        // Expected output shows all StringBuilder operations and performance difference
    }
}