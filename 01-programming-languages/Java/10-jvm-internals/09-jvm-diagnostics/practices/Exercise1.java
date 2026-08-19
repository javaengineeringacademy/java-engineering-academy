package academy.javaengineering.jvm.diagnostics;

/**
 * Exercise 1: Thread Dump Capture and Analysis
 *
 * Task: Create a program that demonstrates different thread states
 * and analyze the thread dump output.
 *
 * Capture dump: jstack <pid> or kill -3 <pid>
 */
public class Exercise1 {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Dump Analysis ===\n");
        System.out.println("PID: " + ProcessHandle.current().pid());
        System.out.println("Capture thread dump while program runs.\n");

        // TODO: Create threads in different states:
        // - RUNNABLE (doing CPU work)
        // - WAITING (Object.wait())
        // - TIMED_WAITING (Thread.sleep())
        // - BLOCKED (waiting for lock)
        // - DEADLOCK (circular lock dependency)

        System.out.println("Threads started. Capture thread dump now.");
        Thread.sleep(60000);
    }
}
