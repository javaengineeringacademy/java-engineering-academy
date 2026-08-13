package academy.javaengineering.io.internals;

import java.io.*;

public class BufferedStreamsInternals {

    public static void main(String[] args) {
        System.out.println("=== Buffered Streams Internals ===\n");

        // 1. Why Buffering
        System.out.println("--- Why Buffering ---");
        System.out.println("Disk I/O is slow");
        System.out.println("Buffering reduces disk accesses");
        System.out.println("Data read/written in chunks");

        // 2. BufferedInputStream
        System.out.println("\n--- BufferedInputStream ---");
        System.out.println("Wraps InputStream with buffer");
        System.out.println("Default buffer size: 8192 bytes");
        System.out.println("Mark/reset support");

        // 3. BufferedReader
        System.out.println("\n--- BufferedReader ---");
        System.out.println("Wraps Reader with buffer");
        System.out.println("readLine() method");
        System.out.println("Much faster than FileReader alone");
    }
}
