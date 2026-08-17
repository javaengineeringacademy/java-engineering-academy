package academy.javaengineering.oop.practices;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Practice: Byte Streams in Java IO
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Reading and writing raw bytes with FileInputStream/FileOutputStream
 * - Using try-with-resources for stream management
 * - Reading into byte arrays and processing chunks
 * - Copying binary data between streams
 * - Understanding the Decorator pattern with stream wrappers
 */
public class Practices {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Practice: 03-byte-streams ===\n");

        String testFile = "test-byte-practice.bin";
        String copyFile = "test-byte-copy.bin";

        try {
            // Test Exercise 1: writeBytes
            byte[] data = {72, 101, 108, 108, 111}; // "Hello"
            writeBytes(testFile, data);
            System.out.println("Exercise 1 - writeBytes: "
                + (new java.io.File(testFile).exists() ? "PASS" : "FAIL"));

            // Test Exercise 2: readAllBytes
            byte[] read = readAllBytes(testFile);
            System.out.println("Exercise 2 - readAllBytes: "
                + (read.length == 5 && read[0] == 72 && read[4] == 111 ? "PASS" : "FAIL"));

            // Test Exercise 3: copyBytes
            long copied = copyBytes(testFile, copyFile);
            System.out.println("Exercise 3 - copyBytes: "
                + (copied == 5 ? "PASS" : "FAIL"));

            // Test Exercise 4: calculateChecksum
            int checksum = calculateChecksum(testFile);
            System.out.println("Exercise 4 - calculateChecksum: "
                + (checksum != 0 ? "PASS" : "FAIL"));

            // Test Exercise 5: findByteSequence
            byte[] needle = {101, 108}; // "el"
            long pos = findByteSequence(testFile, needle);
            System.out.println("Exercise 5 - findByteSequence: "
                + (pos == 1 ? "PASS" : "FAIL (expected 1, got " + pos + ")"));

        } finally {
            new java.io.File(testFile).delete();
            new java.io.File(copyFile).delete();
        }
    }

    // TODO 1: Write a byte array to a file using FileOutputStream
    // Use try-with-resources
    static void writeBytes(String filename, byte[] data) throws IOException {
        // YOUR CODE HERE
    }

    // TODO 2: Read all bytes from a file using FileInputStream
    // Read byte by byte into a ByteArrayOutputStream, then toByteArray()
    static byte[] readAllBytes(String filename) throws IOException {
        // YOUR CODE HERE
        return null;
    }

    // TODO 3: Copy bytes from source file to destination file
    // Use FileInputStream and FileOutputStream with try-with-resources
    // Return the number of bytes copied
    static long copyBytes(String source, String destination) throws IOException {
        // YOUR CODE HERE
        return 0;
    }

    // TODO 4: Calculate a simple XOR checksum of a file
    // XOR all bytes together, start with 0
    static int calculateChecksum(String filename) throws IOException {
        // YOUR CODE HERE
        return 0;
    }

    // TODO 5: Find the first position of a byte sequence in a file
    // Return the byte offset if found, -1 if not found
    static long findByteSequence(String filename, byte[] sequence) throws IOException {
        // YOUR CODE HERE
        return -1;
    }
}
