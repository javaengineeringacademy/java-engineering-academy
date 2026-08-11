package academy.javaengineering.exceptions.finallyblock.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Solutions for the finally block exercises.
 */
public class FinallySolutions {

    /**
     * Solution 1: Basic try-finally
     */
    static void exercise1() {
        System.out.println("=== Exercise 1: Basic try-finally ===");
        try {
            System.out.println("try");
        } finally {
            System.out.println("finally");
        }
        System.out.println();
    }

    /**
     * Solution 2: Try-catch-finally
     */
    static void exercise2() {
        System.out.println("=== Exercise 2: Try-catch-finally ===");
        try {
            throw new RuntimeException("error");
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("cleanup");
        }
        System.out.println();
    }

    /**
     * Solution 3: Finally on return
     */
    static String exercise3() {
        System.out.println("=== Exercise 3: Finally on return ===");
        try {
            return "done";
        } finally {
            System.out.println("before return");
        }
    }

    /**
     * Solution 4: Resource cleanup pattern
     */
    static void exercise4() {
        System.out.println("=== Exercise 4: Resource cleanup ===");
        boolean resourceAcquired = false;
        try {
            resourceAcquired = true;
            System.out.println("Using resource");
        } finally {
            if (resourceAcquired) {
                resourceAcquired = false;
                System.out.println("Released");
            }
        }
        System.out.println("Final state: acquired=" + resourceAcquired);
        System.out.println();
    }

    /**
     * Solution 5: Multiple finally blocks
     */
    static void exercise5() {
        System.out.println("=== Exercise 5: Nested finally ===");
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
     * Solution 6: Finally with loop break
     */
    static void exercise6() {
        System.out.println("=== Exercise 6: Finally with break ===");
        for (int i = 0; i < 5; i++) {
            try {
                System.out.println("i = " + i);
                if (i == 3) {
                    break;
                }
            } finally {
                System.out.println("finally i=" + i);
            }
        }
        System.out.println();
    }

    /**
     * Solution 7: Safe cleanup with exception handling in finally
     */
    static void exercise7() {
        System.out.println("=== Exercise 7: Safe cleanup ===");
        List<String> resources = new ArrayList<>();
        try {
            resources.add("item-1");
            resources.add("item-2");
            throw new RuntimeException("work failed");
        } finally {
            try {
                resources.clear();
                System.out.println("cleaned");
            } catch (Exception e) {
                System.err.println("cleanup failed: " + e.getMessage());
            }
        }
        System.out.println("Resources size: " + resources.size());
        System.out.println();
    }

    /**
     * Solution 8: Conditional cleanup
     */
    static void exercise8(boolean succeed) {
        System.out.println("=== Exercise 8: Conditional cleanup ===");
        boolean hasResource = false;
        try {
            hasResource = true;
            System.out.println("working");
            if (!succeed) {
                throw new RuntimeException("operation failed");
            }
        } catch (RuntimeException e) {
            System.out.println("failed: " + e.getMessage());
        } finally {
            if (hasResource) {
                System.out.println("cleanup");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        exercise1();
        exercise2();
        System.out.println("Exercise 3 result: " + exercise3());
        exercise4();
        exercise5();
        exercise6();
        exercise7();
        exercise8(true);
        exercise8(false);
    }
}
