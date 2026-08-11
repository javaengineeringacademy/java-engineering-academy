package academy.javaengineering.exceptions.finallyblock.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercises for the finally block.
 * Complete each exercise by implementing the TODO sections.
 */
public class FinallyExercises {

    /**
     * Exercise 1: Basic try-finally
     * Write a try-finally that prints "try" in the try block
     * and "finally" in the finally block.
     */
    static void exercise1() {
        System.out.println("=== Exercise 1: Basic try-finally ===");
        // TODO: Implement try-finally
        // try block: print "try"
        // finally block: print "finally"

        System.out.println();
    }

    /**
     * Exercise 2: Try-catch-finally
     * Write a try-catch-finally that throws RuntimeException("error")
     * in try, catches it, and prints "cleanup" in finally.
     */
    static void exercise2() {
        System.out.println("=== Exercise 2: Try-catch-finally ===");
        // TODO: Implement try-catch-finally
        // try block: throw new RuntimeException("error")
        // catch block: print "Caught: " + exception message
        // finally block: print "cleanup"

        System.out.println();
    }

    /**
     * Exercise 3: Finally on normal return
     * Implement a method that returns a string.
     * The finally block should print "before return".
     * Verify that finally runs before the return value is used.
     */
    static String exercise3() {
        System.out.println("=== Exercise 3: Finally on return ===");
        // TODO: Implement try-finally
        // try block: return "done"
        // finally block: print "before return"

        return null; // replace with your implementation
    }

    /**
     * Exercise 4: Resource cleanup pattern
     * Simulate resource acquisition and cleanup.
     * Acquire a resource (set a flag to true).
     * Use try-finally to ensure cleanup (set flag back to false).
     */
    static void exercise4() {
        System.out.println("=== Exercise 4: Resource cleanup ===");
        boolean resourceAcquired = false;
        // TODO: Implement try-finally
        // try block: set resourceAcquired = true, print "Using resource"
        // finally block: if resourceAcquired, set false, print "Released"

        System.out.println("Final state: acquired=" + resourceAcquired);
        System.out.println();
    }

    /**
     * Exercise 5: Multiple finally blocks
     * Implement nested try-finally blocks that print:
     * "outer try", "inner try", "inner finally", "outer finally"
     */
    static void exercise5() {
        System.out.println("=== Exercise 5: Nested finally ===");
        // TODO: Implement nested try-finally
        // Outer try: print "outer try"
        // Inner try: print "inner try"
        // Inner finally: print "inner finally"
        // Outer finally: print "outer finally"

        System.out.println();
    }

    /**
     * Exercise 6: Finally with loop break
     * Write a for loop (i=0 to 4) with try-finally.
     * Print i in try, and "finally i=X" in finally.
     * Break when i == 3. Verify finally runs for each iteration.
     */
    static void exercise6() {
        System.out.println("=== Exercise 6: Finally with break ===");
        // TODO: Implement loop with try-finally and break

        System.out.println();
    }

    /**
     * Exercise 7: Safe cleanup with exception handling in finally
     * Use a List to track resources. Add items in try.
     * In finally, wrap cleanup in try-catch and print "cleaned"
     * even if cleanup might fail.
     */
    static void exercise7() {
        System.out.println("=== Exercise 7: Safe cleanup ===");
        List<String> resources = new ArrayList<>();
        // TODO: Implement try-finally with safe cleanup
        // try block: add "item-1" and "item-2", throw RuntimeException
        // finally block: wrap in try-catch, clear list, print "cleaned"

        System.out.println("Resources size: " + resources.size());
        System.out.println();
    }

    /**
     * Exercise 8: Conditional cleanup
     * Implement a method that takes a boolean succeed.
     * In try, print "working" and if succeed is false, throw exception.
     * In finally, print "cleanup" only if a flag indicates resource was acquired.
     */
    static void exercise8(boolean succeed) {
        System.out.println("=== Exercise 8: Conditional cleanup ===");
        boolean hasResource = false;
        // TODO: Implement try-catch-finally
        // try block: set hasResource=true, print "working", throw if !succeed
        // catch: print "failed"
        // finally: if hasResource, print "cleanup"

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
