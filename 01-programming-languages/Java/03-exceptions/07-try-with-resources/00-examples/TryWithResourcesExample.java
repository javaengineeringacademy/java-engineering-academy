package academy.javaengineering.exceptions.trywithresources.examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Practical examples of try-with-resources.
 */
public class TryWithResourcesExample {

    /**
     * Example 1: File processing with BufferedReader.
     */
    static void fileProcessing() throws IOException {
        System.out.println("=== File Processing ===");
        String content = "First line\nSecond line\nThird line";

        try (BufferedReader reader = new BufferedReader(new StringReader(content))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println(lineNumber++ + ": " + line);
            }
        }
        System.out.println();
    }

    /**
     * Example 2: Multiple file operations.
     */
    static void multipleFileOperations() throws IOException {
        System.out.println("=== Multiple Files ===");
        String input = "Hello World";
        StringBuilder output = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new StringReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line.toUpperCase()).append("\n");
            }
        }
        System.out.println("Output: " + output.toString().trim());
        System.out.println();
    }

    /**
     * Example 3: Database operations (simulated).
     */
    static void databaseExample() {
        System.out.println("=== Database Operations ===");
        System.out.println("Note: This is a simulated example — no actual database");

        // Simulated pattern
        System.out.println("Connection acquired");
        System.out.println("PreparedStatement created");
        System.out.println("ResultSet processed");
        System.out.println("ResultSet closed");
        System.out.println("PreparedStatement closed");
        System.out.println("Connection closed");
        System.out.println();
    }

    /**
     * Example 4: Stream processing with files.
     */
    static void streamProcessing() throws IOException {
        System.out.println("=== Stream Processing ===");
        String data = "apple,banana,cherry,date";

        try (BufferedReader reader = new BufferedReader(new StringReader(data))) {
            reader.lines()
                   .flatMap(line -> java.util.Arrays.stream(line.split(",")))
                   .map(String::trim)
                   .filter(s -> !s.isEmpty())
                   .forEach(fruit -> System.out.println("Fruit: " + fruit));
        }
        System.out.println();
    }

    /**
     * Example 5: Exception handling with TWR.
     */
    static void exceptionHandling() {
        System.out.println("=== Exception Handling ===");
        try (AutoCloseable resource = new AutoCloseable() {
            @Override
            public void close() throws Exception {
                System.out.println("Close called");
            }
        }) {
            System.out.println("Body executing");
            throw new RuntimeException("Body exception");
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Suppressed: " + e.getSuppressed().length);
        }
        System.out.println();
    }

    /**
     * Example 6: Custom resource pattern.
     */
    static void customResourcePattern() {
        System.out.println("=== Custom Resource ===");

        class Timer implements AutoCloseable {
            private final long start;

            Timer(String name) {
                start = System.nanoTime();
                System.out.println("Timer started: " + name);
            }

            @Override
            public void close() {
                long duration = System.nanoTime() - start;
                System.out.println("Timer closed — duration: " + duration + "ns");
            }
        }

        try (Timer timer = new Timer("operation")) {
            // Simulate work
            for (int i = 0; i < 1000000; i++) {
                Math.sqrt(i);
            }
        }
        System.out.println();
    }

    /**
     * Example 7: Resource cleanup in loops.
     */
    static void resourceInLoop() {
        System.out.println("=== Resource in Loop ===");
        String[] inputs = {"first", "second", "third"};

        for (String input : inputs) {
            try (BufferedReader reader = new BufferedReader(new StringReader(input))) {
                System.out.println("Read: " + reader.readLine());
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        System.out.println();
    }

    /**
     * Example 8: Nested TWR for dependent resources.
     */
    static void nestedResources() {
        System.out.println("=== Nested Resources ===");

        class Outer implements AutoCloseable {
            Outer() { System.out.println("Outer opened"); }
            @Override
            public void close() { System.out.println("Outer closed"); }
        }

        class Inner implements AutoCloseable {
            Inner() { System.out.println("Inner opened"); }
            @Override
            public void close() { System.out.println("Inner closed"); }
        }

        try (Outer outer = new Outer()) {
            try (Inner inner = new Inner()) {
                System.out.println("Both open");
            }
            System.out.println("Only outer open");
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        fileProcessing();
        multipleFileOperations();
        databaseExample();
        streamProcessing();
        exceptionHandling();
        customResourcePattern();
        resourceInLoop();
        nestedResources();
    }
}
