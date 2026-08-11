package academy.javaengineering.exceptions.trywithresources.solutions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Solutions for try-with-resources exercises.
 */
public class TryWithResourcesSolutions {

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
     * Solution 1: Basic TWR
     */
    static void exercise1() {
        System.out.println("=== Exercise 1: Basic TWR ===");
        try (ExerciseResource resource = new ExerciseResource("basic")) {
            System.out.println("Resource name: " + resource.getName());
        }
        System.out.println();
    }

    /**
     * Solution 2: Multiple Resources
     */
    static void exercise2() {
        System.out.println("=== Exercise 2: Multiple Resources ===");
        try (ExerciseResource first = new ExerciseResource("first");
             ExerciseResource second = new ExerciseResource("second")) {
            System.out.println("First: " + first.getName());
            System.out.println("Second: " + second.getName());
        }
        System.out.println();
    }

    /**
     * Solution 3: Exception Handling
     */
    static void exercise3() {
        System.out.println("=== Exercise 3: Exception Handling ===");
        try (ThrowingResource resource = new ThrowingResource("close failed")) {
            System.out.println("Body executing");
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Solution 4: Suppressed Exceptions
     */
    static void exercise4() {
        System.out.println("=== Exercise 4: Suppressed Exceptions ===");
        try (ThrowingResource resource = new ThrowingResource("close error")) {
            throw new RuntimeException("body error");
        } catch (RuntimeException e) {
            System.out.println("Primary: " + e.getMessage());
            System.out.println("Suppressed count: " + e.getSuppressed().length);
            for (Throwable t : e.getSuppressed()) {
                System.out.println("  Suppressed: " + t);
            }
        }
        System.out.println();
    }

    /**
     * Solution 5: Resource Lifecycle
     */
    static void exercise5() {
        System.out.println("=== Exercise 5: Resource Lifecycle ===");
        ExerciseResource resource = new ExerciseResource("lifecycle");
        System.out.println("Before TWR: closed=" + resource.isClosed());

        try (resource) {
            System.out.println("Inside TWR: closed=" + resource.isClosed());
        }

        System.out.println("After TWR: closed=" + resource.isClosed());
        System.out.println();
    }

    /**
     * Solution 6: BufferedReader TWR
     */
    static void exercise6() throws IOException {
        System.out.println("=== Exercise 6: BufferedReader TWR ===");
        String content = "Line1\nLine2\nLine3";
        try (BufferedReader reader = new BufferedReader(new StringReader(content))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        System.out.println();
    }

    /**
     * Solution 7: Nested TWR
     */
    static void exercise7() {
        System.out.println("=== Exercise 7: Nested TWR ===");
        try (ExerciseResource outer = new ExerciseResource("outer")) {
            try (ExerciseResource inner = new ExerciseResource("inner")) {
                System.out.println("Both open: " + outer.getName() + ", " + inner.getName());
            }
        }
        System.out.println();
    }

    /**
     * Solution 8: TWR with Finally
     */
    static void exercise8() {
        System.out.println("=== Exercise 8: TWR with Finally ===");
        try (ExerciseResource resource = new ExerciseResource("with-finally")) {
            System.out.println("Using resource: " + resource.getName());
        } finally {
            System.out.println("Finally executed");
        }
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
