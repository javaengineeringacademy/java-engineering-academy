package academy.javaengineering.io.internals;

import java.io.*;

public class DataStreamsInternals {

    public static void main(String[] args) {
        System.out.println("=== Data Streams Internals ===\n");

        // 1. DataInputStream
        System.out.println("--- DataInputStream ---");
        System.out.println("Reads primitive data types");
        System.out.println("Methods: readInt(), readDouble(), readUTF()");
        System.out.println("Binary format, not human-readable");

        // 2. DataOutputStream
        System.out.println("\n--- DataOutputStream ---");
        System.out.println("Writes primitive data types");
        System.out.println("Methods: writeInt(), writeDouble(), writeUTF()");
        System.out.println("Must match read order");

        // 3. Use Cases
        System.out.println("\n--- Use Cases ---");
        System.out.println("Binary file formats");
        System.out.println("Network protocols");
        System.out.println("Serialized data storage");
    }
}
