package academy.javaengineering.concurrency.introduction.memory;

public class ThreadMemoryLayout {
    public static void main(String[] args) {
        System.out.println("Thread Memory Layout:");
        System.out.println("====================");
        System.out.println("Each thread has:");
        System.out.println("  - Program Counter (PC): current instruction");
        System.out.println("  - Stack: local variables, method call frames");
        System.out.println("  - Registers: thread-specific CPU registers");
        System.out.println();
        System.out.println("Threads share:");
        System.out.println("  - Heap: all objects, arrays, instance variables");
        System.out.println("  - Code segment: class bytecode");
        System.out.println("  - Static variables: in method area");
        System.out.println();

        int threadCount = 10;
        long stackSize = Runtime.getRuntime().freeMemory() / threadCount;
        System.out.println("Estimated stack per thread: ~" + (stackSize / 1024) + " KB");

        Runtime rt = Runtime.getRuntime();
        System.out.println("\nJVM Memory:");
        System.out.println("  Max memory: " + (rt.maxMemory() / 1024 / 1024) + " MB");
        System.out.println("  Total memory: " + (rt.totalMemory() / 1024 / 1024) + " MB");
        System.out.println("  Free memory: " + (rt.freeMemory() / 1024 / 1024) + " MB");
    }
}
