package academy.javaengineering.io.memory;

import java.io.*;

public class DataStreamsMemory {

    public static void main(String[] args) {
        System.out.println("=== Data Streams Memory Analysis ===\n");

        // 1. Primitive Sizes
        System.out.println("--- Primitive Sizes ---");
        System.out.println("byte: 1 byte");
        System.out.println("short: 2 bytes");
        System.out.println("int: 4 bytes");
        System.out.println("long: 8 bytes");
        System.out.println("float: 4 bytes");
        System.out.println("double: 8 bytes");

        // 2. UTF String Size
        System.out.println("\n--- UTF String Size ---");
        System.out.println("writeUTF: 2 bytes length + chars");
        System.out.println("Modified UTF-8 encoding");
        System.out.println("Max string length: 65535 bytes");

        // 3. Memory Considerations
        System.out.println("\n--- Memory Considerations ---");
        System.out.println("Primitive data: fixed size");
        System.out.println("Strings: variable size");
        System.out.println("Alignment: no padding needed");
    }
}
