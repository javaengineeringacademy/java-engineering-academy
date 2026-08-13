package academy.javaengineering.io.memory;

import java.io.*;
import java.nio.file.*;

public class FileOperationsMemory {

    public static void main(String[] args) {
        System.out.println("=== File Operations Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. File Object Size
        System.out.println("--- File Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        File file = new File("/tmp/test.txt");
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("File object: " + (after - before) + " bytes");

        // 2. Path Object Size
        System.out.println("\n--- Path Object Size ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Path path = Path.of("/tmp/test.txt");
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Path object: " + (after - before) + " bytes");

        // 3. Memory Considerations
        System.out.println("\n--- Memory Considerations ---");
        System.out.println("Large files: use streaming, not readAllBytes()");
        System.out.println("Memory-mapped files: efficient for large files");
        System.out.println("Buffer size affects memory usage");
    }
}
