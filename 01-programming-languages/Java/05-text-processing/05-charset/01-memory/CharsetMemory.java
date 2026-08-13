package academy.javaengineering.text.memory;

import java.nio.charset.*;

public class CharsetMemory {

    public static void main(String[] args) {
        System.out.println("=== Charset Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Charset Object Size
        System.out.println("--- Charset Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Charset utf8 = StandardCharsets.UTF_8;
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Charset: " + (after - before) + " bytes");
        System.out.println("Shared instance (singleton)");

        // 2. Encoding Memory
        System.out.println("\n--- Encoding Memory ---");
        String text = "Hello, World!";
        byte[] utf8Bytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] latin1Bytes = text.getBytes(StandardCharsets.ISO_8859_1);
        System.out.println("UTF-8: " + utf8Bytes.length + " bytes");
        System.out.println("ISO-8859-1: " + latin1Bytes.length + " bytes");
        System.out.println("UTF-8: variable length");
        System.out.println("ISO-8859-1: fixed 1 byte");
    }
}
