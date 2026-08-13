package academy.javaengineering.io.memory;

import java.io.*;
import java.nio.*;
import java.nio.file.*;

public class IntroductionMemory {

    public static void main(String[] args) {
        System.out.println("=== I/O & NIO Introduction Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Stream Buffer Memory
        System.out.println("--- Stream Buffer Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        byte[] buffer = new byte[1024];
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("1KB buffer: " + (after - before) + " bytes");

        // 2. NIO Buffer Memory
        System.out.println("\n--- NIO Buffer Memory ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        ByteBuffer nioBuffer = ByteBuffer.allocate(1024);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("NIO ByteBuffer: " + (after - before) + " bytes");
        System.out.println("Buffer capacity: " + nioBuffer.capacity());
        System.out.println("Buffer position: " + nioBuffer.position());
        System.out.println("Buffer limit: " + nioBuffer.limit());

        // 3. Memory-mapped Files
        System.out.println("\n--- Memory-mapped Files ---");
        System.out.println("FileChannel.map() creates memory-mapped file");
        System.out.println("OS manages file memory directly");
        System.out.println("Faster for large file operations");
    }
}
