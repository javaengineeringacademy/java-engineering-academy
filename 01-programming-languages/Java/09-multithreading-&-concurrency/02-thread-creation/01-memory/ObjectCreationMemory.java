package academy.javaengineering.concurrency.threadcreation.memory;

public class ObjectCreationMemory {
    public static void main(String[] args) {
        System.out.println("Thread Object Memory Analysis");
        System.out.println("=============================");

        Runtime rt = Runtime.getRuntime();
        long before = rt.freeMemory();

        Thread t = new Thread(() -> {});
        long after = rt.freeMemory();
        System.out.println("Thread object creation cost: ~" + (before - after) + " bytes");
        System.out.println("(Actual OS thread allocation happens at start())");

        before = rt.freeMemory();
        t.start();
        after = rt.freeMemory();
        System.out.println("start() allocation: ~" + (before - after) + " bytes");
        System.out.println("(Plus OS thread stack, typically 512KB-1MB)");

        try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.println("\nRunnable vs Thread subclass:");
        System.out.println("  Runnable: single object + thread");
        System.out.println("  Thread subclass: single object (thread is both task and executor)");
    }
}
