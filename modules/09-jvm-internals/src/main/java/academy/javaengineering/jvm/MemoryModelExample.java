package academy.javaengineering.jvm;

/**
 * Demonstrates JVM memory structure including Heap, Stack, and String Pool.
 *
 * <p>This class shows how different memory areas are used during program execution,
 * including object allocation on the Heap, method frames on the Stack, and String
 * interning in the String Pool.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Heap memory for object allocation</li>
 *   <li>Stack memory for method frames and local variables</li>
 *   <li>String Pool for string interning</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class MemoryModelExample {

    /**
     * Demonstrates Heap memory allocation and usage.
     */
    public static class HeapDemo {
        private int[] largeArray;

        /**
         * Allocates a large array on the Heap.
         *
         * @param size the array size
         */
        public void allocateLargeArray(int size) {
            largeArray = new int[size];
            System.out.println("Allocated array of size: " + size);
        }

        /**
         * Prints current memory usage statistics.
         */
        public void printMemoryUsage() {
            Runtime runtime = Runtime.getRuntime();
            System.out.println("Used Memory: " + (runtime.totalMemory() - runtime.freeMemory()) / 1024 + " KB");
        }
    }

    /**
     * Demonstrates Stack memory usage with recursion.
     */
    public static class StackDemo {
        /**
         * Recursive method demonstrating stack frame creation.
         *
         * @param n the input number
         * @return the recursive sum
         */
        public int recursiveMethod(int n) {
            if (n <= 0) return 0;
            return n + recursiveMethod(n - 1);
        }
    }

    /**
     * Demonstrates String Pool and string interning.
     */
    public static class StringPoolDemo {
        /**
         * Shows string interning behavior in the String Pool.
         */
        public void demonstrateStringInterning() {
            String s1 = "Hello";
            String s2 = "Hello";
            String s3 = new String("Hello");
            String s4 = s3.intern();
            System.out.println("s1 == s2: " + (s1 == s2));
            System.out.println("s1 == s3: " + (s1 == s3));
            System.out.println("s1 == s4: " + (s1 == s4));
        }
    }

    /**
     * Demonstrates JVM memory model concepts.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Memory Model Demo ===");
        HeapDemo heapDemo = new HeapDemo();
        heapDemo.printMemoryUsage();
        heapDemo.allocateLargeArray(1024 * 1024);
        heapDemo.printMemoryUsage();
        System.out.println("Recursive sum: " + new StackDemo().recursiveMethod(10));
        new StringPoolDemo().demonstrateStringInterning();
    }
}
