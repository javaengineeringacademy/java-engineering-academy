package academy.javaengineering.io.internals;

import java.nio.*;

public class NioBuffersInternals {

    public static void main(String[] args) {
        System.out.println("=== NIO Buffers Internals ===\n");

        // 1. Buffer Concepts
        System.out.println("--- Buffer Concepts ---");
        System.out.println("Capacity: maximum size");
        System.out.println("Position: current read/write position");
        System.out.println("Limit: end of readable/writable data");

        // 2. Buffer Types
        System.out.println("\n--- Buffer Types ---");
        System.out.println("ByteBuffer: most common");
        System.out.println("CharBuffer, IntBuffer, etc.");
        System.out.println("Direct vs Heap buffers");

        // 3. Buffer Operations
        System.out.println("\n--- Buffer Operations ---");
        System.out.println("put(): write data to buffer");
        System.out.println("get(): read data from buffer");
        System.out.println("flip(): prepare for reading");
        System.out.println("clear()/rewind(): prepare for writing");
    }
}
