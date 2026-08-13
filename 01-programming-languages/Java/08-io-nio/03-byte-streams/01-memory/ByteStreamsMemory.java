package academy.javaengineering.io.memory;

import java.io.*;

public class ByteStreamsMemory {

    public static void main(String[] args) {
        System.out.println("=== Byte Streams Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Buffer Sizes
        System.out.println("--- Buffer Sizes ---");
        System.out.println("Default buffer: 8192 bytes (8KB)");
        System.out.println("Larger buffers: fewer I/O operations");
        System.out.println("Smaller buffers: less memory usage");

        // 2. Memory Impact
        System.out.println("\n--- Memory Impact ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        byte[] buffer = new byte[8192];
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("8KB buffer: " + (after - before) + " bytes");

        // 3. Streaming vs Loading
        System.out.println("\n--- Streaming vs Loading ---");
        System.out.println("Streaming: process chunk by chunk");
        System.out.println("Loading: read entire file into memory");
        System.out.println("Streaming: constant memory usage");
        System.out.println("Loading: memory = file size");
    }
}
