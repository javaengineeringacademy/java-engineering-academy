package academy.javaengineering.concurrency.virtualthreads.memory;

public class VirtualThreadMemory {
    public static void main(String[] args) throws Exception {
        System.out.println("Virtual Thread Memory Comparison");
        System.out.println("===============================");

        Runtime rt = Runtime.getRuntime();

        // Platform thread memory
        long before = rt.freeMemory();
        Thread pt = Thread.ofPlatform().start(() -> {});
        pt.join();
        long after = rt.freeMemory();
        System.out.println("Platform thread creation: ~" + (before - after) + " bytes");
        System.out.println("  + ~1MB stack space reserved");

        // Virtual thread memory
        before = rt.freeMemory();
        Thread vt = Thread.ofVirtual().start(() -> {});
        vt.join();
        after = rt.freeMemory();
        System.out.println("Virtual thread creation: ~" + (before - after) + " bytes");
        System.out.println("  + ~1KB stack (grows on demand)");

        System.out.println("\nComparison for 100,000 threads:");
        System.out.println("  Platform: ~100GB (impossible)");
        System.out.println("  Virtual:  ~100MB (feasible)");
    }
}
