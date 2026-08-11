package academy.javaengineering.exceptions.finallyblock.examples;

import java.util.ArrayList;
import java.util.List;

/**
 * Practical examples of the finally block in real-world scenarios.
 */
public class FinallyExample {

    /**
     * Example 1: Lock release pattern.
     */
    static void lockRelease() {
        System.out.println("=== Lock Release Pattern ===");
        boolean locked = false;
        try {
            System.out.println("Acquiring lock...");
            locked = true;
            System.out.println("Critical section executing");
        } finally {
            if (locked) {
                System.out.println("Releasing lock in finally");
                locked = false;
            }
        }
        System.out.println();
    }

    /**
     * Example 2: File-like resource with manual cleanup.
     */
    static void manualResourceCleanup() {
        System.out.println("=== Manual Resource Cleanup ===");
        List<String> buffer = new ArrayList<>();
        try {
            System.out.println("Opening resource");
            buffer.add("data-1");
            buffer.add("data-2");
            System.out.println("Processing: " + buffer);
        } finally {
            System.out.println("Flushing buffer (" + buffer.size() + " items)");
            buffer.clear();
            System.out.println("Resource closed");
        }
        System.out.println();
    }

    /**
     * Example 3: Exception in finally demonstrates masking.
     */
    static void exceptionMasking() {
        System.out.println("=== Exception Masking Demo ===");
        try {
            System.out.println("try: about to throw");
            throw new RuntimeException("original error");
        } finally {
            System.out.println("finally: about to throw");
            throw new RuntimeException("finally error — original lost!");
        }
    }

    /**
     * Example 4: Multiple try-finally blocks.
     */
    static void multipleFinallyBlocks() {
        System.out.println("=== Multiple Finally Blocks ===");
        try {
            System.out.println("outer try");
            try {
                System.out.println("inner try");
            } finally {
                System.out.println("inner finally");
            }
        } finally {
            System.out.println("outer finally");
        }
        System.out.println();
    }

    /**
     * Example 5: Finally with conditional return.
     */
    static int conditionalReturn(int value) {
        try {
            System.out.println("try: returning " + value);
            return value;
        } finally {
            System.out.println("finally: always runs");
        }
    }

    /**
     * Example 6: Finally with break in loop.
     */
    static void finallyWithBreak() {
        System.out.println("=== Finally with Break ===");
        for (int i = 0; i < 5; i++) {
            try {
                System.out.println("i = " + i);
                if (i == 3) {
                    break;
                }
            } finally {
                System.out.println("finally for i = " + i);
            }
        }
        System.out.println();
    }

    /**
     * Example 7: Finally in recursive method.
     */
    static int factorial(int n) {
        try {
            if (n <= 1) {
                return 1;
            }
            return n * factorial(n - 1);
        } finally {
            System.out.println("finally for n=" + n);
        }
    }

    /**
     * Example 8: Safe cleanup with exception catching in finally.
     */
    static void safeCleanup() {
        System.out.println("=== Safe Cleanup with Exception Handling ===");
        List<String> resources = new ArrayList<>();
        try {
            resources.add("resource-1");
            resources.add("resource-2");
            System.out.println("Working with " + resources.size() + " resources");
            throw new RuntimeException("work failed");
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            try {
                resources.clear();
                System.out.println("Cleanup successful");
            } catch (Exception e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        lockRelease();
        manualResourceCleanup();
        // exceptionMasking(); // uncomment to see exception masking

        multipleFinallyBlocks();
        System.out.println("Conditional returns:");
        conditionalReturn(10);
        conditionalReturn(20);

        finallyWithBreak();
        System.out.println("Factorial(5) = " + factorial(5));
        safeCleanup();
    }
}
