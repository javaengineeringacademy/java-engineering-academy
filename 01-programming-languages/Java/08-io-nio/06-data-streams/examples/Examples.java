package academy.javaengineering.io.examples;

import java.io.*;
import java.nio.file.*;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== 06-data-streams Examples ===\n");

        // WHY: File I/O is fundamental for data persistence and processing
        // INTERNAL: Java I/O uses streams for sequential access, NIO for buffer-based operations
        // ENGINEERING: Use try-with-resources, prefer NIO for large files

        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("demo", ".txt");
            Files.writeString(tempFile, "Hello from 06-data-streams!");
            String content = Files.readString(tempFile);
            System.out.println("Content: " + content);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (tempFile != null) {
                try { Files.deleteIfExists(tempFile); } catch (IOException e) {}
            }
        }

        // TRADE-OFF: Traditional I/O vs NIO
        // Traditional: simple, blocking, good for small files
        // NIO: buffer-based, non-blocking, good for large files
    }
}
