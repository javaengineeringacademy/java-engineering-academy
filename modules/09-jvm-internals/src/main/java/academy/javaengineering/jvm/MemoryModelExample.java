package academy.javaengineering.jvm;

import java.util.Arrays;

/**
 * Memory Model - JVM Memory Structure, Heap, Stack, Method Area.
 */
public class MemoryModelExample {

    public static class HeapDemo {
        private int[] largeArray;

        public void allocateLargeArray(int size) {
            largeArray = new int[size];
            System.out.println("Allocated array of size: " + size);
        }

        public void printMemoryUsage() {
            Runtime runtime = Runtime.getRuntime();
            System.out.println("Used Memory: " + (runtime.totalMemory() - runtime.freeMemory()) / 1024 + " KB");
        }
    }

    public static class StackDemo {
        public int recursiveMethod(int n) {
            if (n <= 0) return 0;
            int[] localArray = new int[100];
            return n + recursiveMethod(n - 1);
        }

        public void demonstrateStackOverflow(int depth) {
            if (depth > 0) {
                demonstrateStackOverflow(depth + 1);
            }
        }
    }

    public static class StringPoolDemo {
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

    public static void main(String[] args) {
        System.out.println("=== Memory Model Demo ===");

        HeapDemo heapDemo = new HeapDemo();
        heapDemo.printMemoryUsage();
        heapDemo.allocateLargeArray(1024 * 1024);
        heapDemo.printMemoryUsage();

        StackDemo stackDemo = new StackDemo();
        System.out.println("\nRecursive sum: " + stackDemo.recursiveMethod(10));

        StringPoolDemo stringPool = new StringPoolDemo();
        System.out.println("\n=== String Pool Demo ===");
        stringPool.demonstrateStringInterning();
    }
}
