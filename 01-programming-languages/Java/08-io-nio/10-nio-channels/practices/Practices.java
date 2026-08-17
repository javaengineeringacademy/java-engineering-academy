package academy.javaengineering.oop.practices;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Practice: NIO Channels in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Using FileChannel for efficient file operations
 * - ByteBuffer operations (allocate, put, flip, get)
 * - Reading and writing through channels
 * - Understanding buffer flip() and clear() lifecycle
 * - Zero-copy transfers with transferTo
 */
public class Practices {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Practice: 10-nio-channels ===\n");

        Path tempFile = Files.createTempFile("nio-practice", ".txt");
        try {
            // Test Exercise 1: writeWithChannel
            writeWithChannel(tempFile, "Hello NIO!");
            String content = new String(Files.readAllBytes(tempFile), StandardCharsets.UTF_8);
            System.out.println("Exercise 1 - writeWithChannel: "
                + ("Hello NIO!".equals(content) ? "PASS" : "FAIL"));

            // Test Exercise 2: readWithChannel
            byte[] data = readWithChannel(tempFile);
            System.out.println("Exercise 2 - readWithChannel: "
                + (data != null && data.length > 0 ? "PASS" : "FAIL"));

            // Test Exercise 3: copyWithChannel
            Path copyFile = Files.createTempFile("nio-copy", ".txt");
            long copied = copyWithChannel(tempFile, copyFile);
            System.out.println("Exercise 3 - copyWithChannel: "
                + (copied == 10 ? "PASS" : "FAIL (expected 10, got " + copied + ")"));
            Files.deleteIfExists(copyFile);

            // Test Exercise 4: readLineWithChannel
            Files.writeString(tempFile, "Line 1\nLine 2\nLine 3");
            String firstLine = readFirstLineWithChannel(tempFile);
            System.out.println("Exercise 4 - readFirstLineWithChannel: "
                + ("Line 1".equals(firstLine) ? "PASS" : "FAIL"));

            // Test Exercise 5: getFileSize
            long size = getFileSize(tempFile);
            System.out.println("Exercise 5 - getFileSize: "
                + (size > 0 ? "PASS" : "FAIL"));

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    // TODO 1: Write a string to a file using FileChannel
    // Open the channel, create a ByteBuffer, put the bytes, write to channel
    static void writeWithChannel(Path path, String content) throws IOException {
        // YOUR CODE HERE
    }

    // TODO 2: Read all bytes from a file using FileChannel
    // Open the channel, allocate a ByteBuffer, read into it, return the bytes
    static byte[] readWithChannel(Path path) throws IOException {
        // YOUR CODE HERE
        return null;
    }

    // TODO 3: Copy file contents using FileChannel.transferTo() (zero-copy)
    // Return the number of bytes transferred
    static long copyWithChannel(Path source, Path destination) throws IOException {
        // YOUR CODE HERE
        return 0;
    }

    // TODO 4: Read the first line from a file using FileChannel
    // Read bytes, convert to string, return text up to the first \n
    static String readFirstLineWithChannel(Path path) throws IOException {
        // YOUR CODE HERE
        return null;
    }

    // TODO 5: Get the file size using FileChannel.size()
    static long getFileSize(Path path) throws IOException {
        // YOUR CODE HERE
        return 0;
    }
}
