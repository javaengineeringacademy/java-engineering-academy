package academy.javaengineering.jvm.diagnostics;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 2: Heap Dump Generation and Analysis
 *
 * Task: Create a memory leak, generate a heap dump, and analyze it.
 *
 * Generate dump: jmap -dump:live,format=b,file=heap.hprof <pid>
 */
public class Exercise2 {

    private static final List<Object> leak = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Heap Dump Analysis ===\n");
        System.out.println("PID: " + ProcessHandle.current().pid());

        // TODO: Create objects that will leak
        // TODO: Trigger a heap dump
        // TODO: Analyze the dump with Eclipse MAT or VisualVM

        for (int i = 0; i < 10000; i++) {
            leak.add(new byte[1024 * 10]);
            if (i % 1000 == 0) {
                System.out.printf("Iteration %d, leak size: %d%n", i, leak.size());
            }
        }
        System.out.println("\nHeap growing. Generate heap dump now.");
        Thread.sleep(60000);
    }
}
