package academy.javaengineering.concurrency.threadbasics.memory;

public class ThreadStackMemory {
    public static void main(String[] args) {
        System.out.println("Thread Stack Memory Analysis");
        System.out.println("============================");

        Thread t = new Thread(() -> {
            long stackSize = Thread.currentThread().getStackTrace().length * 1024L;
            System.out.println("Stack depth: " + Thread.currentThread().getStackTrace().length + " frames");

            recursiveMethod(0);
        });

        t.start();
        try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.println("\nDefault stack size: -Xss (typically 512KB-1MB)");
        System.out.println("Each method call adds a frame to the stack");
        System.out.println("StackOverflowError occurs when stack is full");
    }

    static void recursiveMethod(int depth) {
        if (depth < 10) {
            System.out.println("  Depth: " + depth);
            recursiveMethod(depth + 1);
        }
    }
}
