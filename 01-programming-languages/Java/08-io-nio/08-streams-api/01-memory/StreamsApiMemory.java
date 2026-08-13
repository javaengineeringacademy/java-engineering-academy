package academy.javaengineering.io.memory;

import java.util.stream.*;
import java.util.*;

public class StreamsApiMemory {

    public static void main(String[] args) {
        System.out.println("=== Streams API Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Stream Object Size
        System.out.println("--- Stream Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Stream object: " + (after - before) + " bytes");

        // 2. Lazy Evaluation
        System.out.println("\n--- Lazy Evaluation ---");
        System.out.println("Intermediate ops: not executed until terminal");
        System.out.println("Memory: only processes needed elements");
        System.out.println("Pipeline: no intermediate storage");

        // 3. Memory Efficiency
        System.out.println("\n--- Memory Efficiency ---");
        System.out.println("No intermediate collection created");
        System.out.println("Process elements one at a time");
        System.out.println("Constant memory for infinite streams");
    }
}
