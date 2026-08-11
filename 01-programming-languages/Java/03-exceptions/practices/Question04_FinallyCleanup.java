package academy.javaengineering.exceptions.questions;

import java.io.StringWriter;

/**
 * Question 4: Finally block for cleanup
 *
 * Task: Complete the method using try-finally to guarantee resource cleanup.
 * The StringWriter must be closed regardless of whether an exception occurs.
 */
public class Question04_FinallyCleanup {

    public static String writeSafely(String content) {
        StringWriter writer = new StringWriter();
        // TODO: Use try-finally to guarantee writer.close() is called
        // Write content to writer
        // Return the writer's toString() result
        return null;
    }

    public static void main(String[] args) {
        System.out.println("Test 1: " + writeSafely("Hello"));
        System.out.println("Test 2: " + writeSafely(null));
    }
}
