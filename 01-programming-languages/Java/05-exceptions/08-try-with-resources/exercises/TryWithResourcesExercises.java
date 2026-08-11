package academy.javaengineering.exceptions.trywithresources.exercises;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Exercises for try-with-resources.
 * Complete each exercise by implementing the TODO sections.
 */
public class TryWithResourcesExercises {

    // Resource for exercises
    static class ExerciseResource implements AutoCloseable {
        private final String name;
        private boolean closed = false;

        ExerciseResource(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        boolean isClosed() {
            return closed;
        }

        @Override
        public void close() {
            closed = true;
        }
    }

    // Resource that throws on close
    static class ThrowingResource implements AutoCloseable {
        private final String errorMessage;

        ThrowingResource(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public void close() throws Exception {
            throw new IOException(errorMessage);
        }
    }

    /**
     * Exercise 1: Basic TWR
     * Create a try-with-resources that opens ExerciseResource("basic")
     * and prints its name.
     */
    static void exercise1() {
        System.out.println("=== Exercise 1: Basic TWR ===");
        // TODO: Implement try-with-resources
        // Create ExerciseResource with name "basic"
        // Print the resource's name using getName()
        // Resource should be automatically closed

        System.out.println();
    }

    /**
     * Exercise 2: Multiple Resources
     * Create a try-with-resources with two ExerciseResources
     * named "first" and "second". Print both names.
     */
    static void exercise2() {
        System.out.println("=== Exercise 2: Multiple Resources ===");
        // TODO: Implement try-with-resources with two resources
        // Resource 1: ExerciseResource("first")
        // Resource 2: ExerciseResource("second")
        // Print both names

        System.out.println();
    }

    /**
     * Exercise 3: Exception Handling
     * Use TWR with ThrowingResource that throws "close failed".
     * Catch the exception and print the error message.
     */
    static void exercise3() {
        System.out.println("=== Exercise 3: Exception Handling ===");
        // TODO: Implement try-with-resources with ThrowingResource
        // The resource should throw "close failed" on close
        // Catch the exception and print its message

        System.out.println();
    }

    /**
     * Exercise 4: Suppressed Exceptions
     * Use TWR with ThrowingResource. In the body, throw a
     * RuntimeException("body error"). Catch the exception
     * and print the number of suppressed exceptions.
     */
    static void exercise4() {
        System.out.println("=== Exercise 4: Suppressed Exceptions ===");
        // TODO: Implement try-with-resources
        // Resource: ThrowingResource("close error")
        // Body: throw new RuntimeException("body error")
        // Catch RuntimeException and print suppressed exception count

        System.out.println();
    }

    /**
     * Exercise 5: Resource Lifecycle
     * Create ExerciseResource("lifecycle") outside the TWR.
     * Use it in TWR. After TWR, verify it is closed.
     */
    static void exercise5() {
        System.out.println("=== Exercise 5: Resource Lifecycle ===");
        // TODO: Create ExerciseResource("lifecycle")
        // Use it in try-with-resources
        // After TWR, check and print isClosed()

        System.out.println();
    }

    /**
     * Exercise 6: BufferedReader TWR
     * Use TWR with BufferedReader to read from a StringReader
     * containing "Line1\nLine2\nLine3". Print each line.
     */
    static void exercise6() throws IOException {
        System.out.println("=== Exercise 6: BufferedReader TWR ===");
        // TODO: Create BufferedReader wrapping StringReader
        // Content: "Line1\nLine2\nLine3"
        // Read and print each line

        System.out.println();
    }

    /**
     * Exercise 7: Nested TWR
     * Use nested try-with-resources:
     * Outer: ExerciseResource("outer")
     * Inner: ExerciseResource("inner")
     * Print "Both open" inside inner block.
     */
    static void exercise7() {
        System.out.println("=== Exercise 7: Nested TWR ===");
        // TODO: Implement nested try-with-resources
        // Outer resource: ExerciseResource("outer")
        // Inner resource: ExerciseResource("inner")
        // Print "Both open" inside inner try

        System.out.println();
    }

    /**
     * Exercise 8: TWR with Finally
     * Use TWR with ExerciseResource("with-finally").
     * Add a finally block that prints "Finally executed".
     */
    static void exercise8() {
        System.out.println("=== Exercise 8: TWR with Finally ===");
        // TODO: Implement try-with-resources with finally block
        // Resource: ExerciseResource("with-finally")
        // Finally block should print "Finally executed"

        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
        exercise6();
        exercise7();
        exercise8();
    }
}
