package academy.javaengineering.jvm.diagnostics;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution 2: Heap Dump Generation and Analysis
 */
public class Solution2 {

    private static final List<byte[]> leak = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Heap Dump Analysis ===\n");
        System.out.println("PID: " + ProcessHandle.current().pid());
        System.out.println("Generate heap dump with:");
        System.out.println("  jmap -dump:live,format=b,file=heap.hprof " + ProcessHandle.current().pid());
        System.out.println("Then analyze with Eclipse MAT or VisualVM.\n");

        for (int i = 0; i < 10000; i++) {
            leak.add(new byte[1024 * 10]);
            if (i % 1000 == 0) {
                System.out.printf("Iteration %d, leak size: %d, memory: %d MB%n",
                    i, leak.size(),
                    (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
            }
        }
        System.out.println("\nHeap dump analysis tips:");
        System.out.println("  1. Look at Dominator Tree for largest objects");
        System.out.println("  2. Check GC roots for why objects can't be collected");
        System.out.println("  3. Use Leak Suspects report for automated analysis");
        Thread.sleep(60000);
    }
}
