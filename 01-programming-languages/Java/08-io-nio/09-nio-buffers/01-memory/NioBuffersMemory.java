package academy.javaengineering.io.memory;

import java.nio.*;

public class NioBuffersMemory {

    public static void main(String[] args) {
        System.out.println("=== NIO Buffers Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Heap Buffer
        System.out.println("--- Heap Buffer ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        ByteBuffer heapBuffer = ByteBuffer.allocate(1024);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Heap buffer: " + (after - before) + " bytes");
        System.out.println("Stored in JVM heap");
        System.out.println("Garbage collected");

        // 2. Direct Buffer
        System.out.println("\n--- Direct Buffer ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Direct buffer: " + (after - before) + " bytes");
        System.out.println("Stored outside JVM heap");
        System.out.println("Not garbage collected");

        // 3. Memory Comparison
        System.out.println("\n--- Memory Comparison ---");
        System.out.println("Heap: managed by JVM");
        System.out.println("Direct: managed by OS");
        System.out.println("Direct: faster I/O, slower allocation");
    }
}
