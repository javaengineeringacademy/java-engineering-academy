package academy.javaengineering.concurrency.atomic.memory;

import java.util.concurrent.atomic.*;

public class AtomicMemory {
    public static void main(String[] args) {
        System.out.println("Atomic Classes Memory");
        System.out.println("=====================");

        Runtime rt = Runtime.getRuntime();

        long before = rt.freeMemory();
        AtomicInteger ai = new AtomicInteger();
        long after = rt.freeMemory();
        System.out.println("AtomicInteger: ~" + (before - after) + " bytes");

        before = rt.freeMemory();
        AtomicReference<String> ar = new AtomicReference<>();
        after = rt.freeMemory();
        System.out.println("AtomicReference: ~" + (before - after) + " bytes");

        System.out.println("\nCAS uses CPU-level atomic instruction (CMPXCHG on x86)");
        System.out.println("No lock overhead, but may retry under contention");
    }
}
