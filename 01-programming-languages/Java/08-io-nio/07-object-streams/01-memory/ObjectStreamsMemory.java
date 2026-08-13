package academy.javaengineering.io.memory;

import java.io.*;

public class ObjectStreamsMemory {

    public static void main(String[] args) {
        System.out.println("=== Object Streams Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Object Overhead
        System.out.println("--- Object Overhead ---");
        System.out.println("Object header: 12-16 bytes");
        System.out.println("References: 4 bytes each (compressed)");
        System.out.println("Primitives: fixed sizes");

        // 2. Serialization Memory
        System.out.println("\n--- Serialization Memory ---");
        System.out.println("Serialized form: object header + field data");
        System.out.println("String: 2 bytes length + char data");
        System.out.println("Arrays: 16 bytes header + elements");

        // 3. Memory Considerations
        System.out.println("\n--- Memory Considerations ---");
        System.out.println("Large object graphs: high memory usage");
        System.out.println("Use transient for large non-essential fields");
        System.out.println("Consider externalizable for efficiency");
    }
}
