package academy.javaengineering.io.memory;

import java.io.*;

public class BufferedStreamsMemory {

    public static void main(String[] args) {
        System.out.println("=== Buffered Streams Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Default Buffer
        System.out.println("--- Default Buffer ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        byte[] buffer = new byte[8192];
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("8KB buffer: " + (after - before) + " bytes");

        // 2. Custom Buffer Sizes
        System.out.println("\n--- Custom Buffer Sizes ---");
        System.out.println("Small buffer: 512 bytes (less memory)");
        System.out.println("Medium buffer: 4096 bytes (balanced)");
        System.out.println("Large buffer: 16384 bytes (better performance)");

        // 3. Memory vs Performance
        System.out.println("\n--- Memory vs Performance ---");
        System.out.println("Larger buffer: fewer I/O ops, more memory");
        System.out.println("Smaller buffer: more I/O ops, less memory");
        System.out.println("Choose based on use case");
    }
}
