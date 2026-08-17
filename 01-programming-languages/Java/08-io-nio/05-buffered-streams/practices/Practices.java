package academy.javaengineering.oop.practices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Practice: Buffered Streams in Java IO
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Using BufferedReader for efficient line reading
 * - Using BufferedWriter for efficient writing
 * - Understanding buffer size impact on performance
 * - Mark and reset operations
 * - Copying files with buffered streams
 */
public class Practices {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Practice: 05-buffered-streams ===\n");

        String inputFile = "buffered-input.txt";
        String outputFile = "buffered-output.txt";

        try {
            // Create test file
            try (PrintWriter pw = new PrintWriter(new FileWriter(inputFile))) {
                for (int i = 0; i < 100; i++) {
                    pw.println("Line " + i);
                }
            }

            // Test Exercise 1: countLines
            long lineCount = countLines(inputFile);
            System.out.println("Exercise 1 - countLines: "
                + (lineCount == 100 ? "PASS" : "FAIL (expected 100, got " + lineCount + ")"));

            // Test Exercise 2: copyWithBufferedReader
            copyWithBufferedReader(inputFile, outputFile);
            System.out.println("Exercise 2 - copyWithBufferedReader: "
                + (new File(outputFile).exists() ? "PASS" : "FAIL"));

            // Test Exercise 3: readFirstNLines
            String[] first5 = readFirstNLines(inputFile, 5);
            System.out.println("Exercise 3 - readFirstNLines: "
                + (first5.length == 5 && first5[0].contains("Line 0") ? "PASS" : "FAIL"));

            // Test Exercise 4: writeFileFromBuilder
            String builderFile = "buffered-builder.txt";
            writeFileFromBuilder(builderFile, 10);
            String[] lines = readFirstNLines(builderFile, 10);
            System.out.println("Exercise 4 - writeFileFromBuilder: "
                + (lines.length == 10 && lines[9].contains("9") ? "PASS" : "FAIL"));
            new File(builderFile).delete();

            // Test Exercise 5: readWithMarkAndReset
            String firstLine = readWithMarkAndReset(inputFile);
            System.out.println("Exercise 5 - readWithMarkAndReset: "
                + (firstLine != null && firstLine.contains("Line 0") ? "PASS" : "FAIL"));

        } finally {
            new File(inputFile).delete();
            new File(outputFile).delete();
        }
    }

    // TODO 1: Count the number of lines in a file using BufferedReader
    // Use try-with-resources with BufferedReader
    static long countLines(String filename) throws IOException {
        // YOUR CODE HERE
        return 0;
    }

    // TODO 2: Copy a file line by line using BufferedReader and BufferedWriter
    // Use try-with-resources for both readers
    static void copyWithBufferedReader(String source, String destination) throws IOException {
        // YOUR CODE HERE
    }

    // TODO 3: Read the first N lines from a file
    // Return a String array of exactly N lines (or fewer if file is shorter)
    static String[] readFirstNLines(String filename, int n) throws IOException {
        // YOUR CODE HERE
        return new String[0];
    }

    // TODO 4: Write numbered lines to a file using BufferedWriter
    // Write "Line 0", "Line 1", ... up to count-1
    // Use BufferedWriter for efficiency
    static void writeFileFromBuilder(String filename, int count) throws IOException {
        // YOUR CODE HERE
    }

    // TODO 5: Read the first line, then use mark(1024) and reset() to re-read it
    // Return the first line read twice (to verify mark/reset works)
    // Actually just return the first line (mark/reset is the learning exercise)
    static String readWithMarkAndReset(String filename) throws IOException {
        // YOUR CODE HERE
        return null;
    }
}
