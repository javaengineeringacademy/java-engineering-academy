package academy.javaengineering.concurrency.basics.methods;

/**
 * Critical difference between start() and run()
 */
public class StartVsRun {

    public static void main(String[] args) {
        System.out.println("=== Start vs Run Demo ===\n");

        Thread thread1 = new Thread(() -> {
            System.out.println("run() executed in: " + Thread.currentThread().getName());
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            System.out.println("run() executed in: " + Thread.currentThread().getName());
        }, "Thread-2");

        // Using start() - Creates new thread
        System.out.println("--- Using start() ---");
        thread1.start();
        System.out.println("Main thread continues after start()");

        // Using run() - Executes in CURRENT thread (no new thread!)
        System.out.println("\n--- Using run() ---");
        thread2.run(); // No new thread created!
        System.out.println("Main thread after run()");

        System.out.println("\n=== Key Difference ===");
        System.out.println("start() → New thread created, run() executes in new thread");
        System.out.println("run()   → No new thread, executes in CURRENT thread");
    }
}
