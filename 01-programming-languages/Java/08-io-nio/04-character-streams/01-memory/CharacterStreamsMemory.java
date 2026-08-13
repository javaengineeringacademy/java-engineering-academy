package academy.javaengineering.io.memory;

import java.io.*;

public class CharacterStreamsMemory {

    public static void main(String[] args) {
        System.out.println("=== Character Streams Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Character Size
        System.out.println("--- Character Size ---");
        System.out.println("char: 2 bytes (16 bits)");
        System.out.println("UTF-8: 1-4 bytes per character");
        System.out.println("Memory depends on encoding");

        // 2. Buffer Memory
        System.out.println("\n--- Buffer Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        char[] buffer = new char[1024];
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("1K char buffer: " + (after - before) + " bytes");

        // 3. String vs Stream
        System.out.println("\n--- String vs Stream ---");
        System.out.println("String: entire content in memory");
        System.out.println("Stream: process character by character");
        System.out.println("Stream: constant memory usage");
    }
}
