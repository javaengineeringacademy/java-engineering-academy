package academy.javaengineering.concurrency.examples;

import java.util.concurrent.TimeUnit;

public class ThreadMethodsExamples {

    public static void main(String[] args) throws InterruptedException {
        example1_StartVsRun();
        example2_JoinMethods();
        example3_SleepAndInterrupt();
        example4_YieldAndPriority();
        example5_ThreadStateMonitoring();
    }

    // Example 1: start() vs run()
    static void example1_StartVsRun() throws InterruptedException {
        System.out.println("=== Example 1: start() vs run() ===");

        Thread thread1 = new Thread(() -> {
            System.out.println("Actual thread: " + Thread.currentThread().getName());
        }, "Thread-1");

        // start() creates a new thread
        thread1.start();
        thread1.join();

        Thread thread2 = new Thread(() -> {
            System.out.println("Same thread as caller: " + Thread.currentThread().getName());
        }, "Thread-2");

        // run() executes in the calling thread (NOT a new thread)
        System.out.println("Calling run() directly:");
        thread2.run(); // This runs on main thread

        System.out.println();
    }

    // Example 2: join() methods
    static void example2_JoinMethods() throws InterruptedException {
        System.out.println("=== Example 2: join() Methods ===");

        // Basic join - wait for thread to finish
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Thread-1 finished");
        }, "JoinThread-1");

        System.out.println("Thread-1 state before start: " + t1.getState());
        t1.start();
        System.out.println("Thread-1 state after start: " + t1.getState());

        System.out.println("Main thread waiting for Thread-1...");
        t1.join(); // Blocks until t1 completes
        System.out.println("Thread-1 state after join: " + t1.getState());

        // join(timeout) - wait at most specified time
        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Thread-2 finished");
        }, "JoinThread-2");

        t2.start();
        System.out.println("Joining Thread-2 with 1 second timeout...");
        t2.join(1000); // Wait at most 1 second
        System.out.println("Thread-2 is alive after timeout? " + t2.isAlive());

        // join(millis, nanos) - more precise timeout
        t2.join(0, 500000000); // Wait 0.5 seconds more
        System.out.println("Thread-2 is alive after extra wait? " + t2.isAlive());

        System.out.println();
    }

    // Example 3: sleep() and interrupt()
    static void example3_SleepAndInterrupt() throws InterruptedException {
        System.out.println("=== Example 3: sleep() and interrupt() ===");

        Thread sleepingThread = new Thread(() -> {
            System.out.println("Sleeping thread started");
            try {
                System.out.println("Sleeping for 5 seconds...");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("Sleep completed normally");
            } catch (InterruptedException e) {
                System.out.println("Sleep was interrupted! Cleaning up...");
                Thread.currentThread().interrupt(); // Restore interrupt flag
            }
        }, "SleepingThread");

        sleepingThread.start();
        TimeUnit.MILLISECONDS.sleep(500); // Let it start sleeping

        System.out.println("Interrupting sleeping thread...");
        sleepingThread.interrupt();
        sleepingThread.join();

        // isInterrupted() vs interrupted()
        System.out.println("isInterrupted() on sleepingThread: " + sleepingThread.isInterrupted());

        // interrupted() clears the flag
        System.out.println("interrupted() on main: " + Thread.interrupted());
        System.out.println("interrupted() again on main: " + Thread.interrupted()); // false - flag was cleared

        System.out.println();
    }

    // Example 4: yield() and priority
    static void example4_YieldAndPriority() throws InterruptedException {
        System.out.println("=== Example 4: yield() and Priority ===");

        // yield() - hints scheduler to give other threads a chance
        Runnable yieldTask = () -> {
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName() + " - iteration " + i);
                Thread.yield(); // Hint to scheduler
            }
        };

        Thread lowPriority = new Thread(yieldTask, "LowPriority");
        Thread highPriority = new Thread(yieldTask, "HighPriority");

        lowPriority.setPriority(Thread.MIN_PRIORITY);  // 1
        highPriority.setPriority(Thread.MAX_PRIORITY); // 10

        System.out.println("Main priority: " + Thread.currentThread().getPriority());
        System.out.println("Low priority: " + lowPriority.getPriority());
        System.out.println("High priority: " + highPriority.getPriority());

        lowPriority.start();
        highPriority.start();

        lowPriority.join();
        highPriority.join();

        System.out.println();
    }

    // Example 5: Thread state monitoring
    static void example5_ThreadStateMonitoring() throws InterruptedException {
        System.out.println("=== Example 5: Thread State Monitoring ===");

        Thread monitoredThread = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "MonitoredThread");

        System.out.println("Before start: " + monitoredThread.getState()); // NEW
        monitoredThread.start();
        System.out.println("After start: " + monitoredThread.getState()); // RUNNABLE or TIMED_WAITING

        TimeUnit.MILLISECONDS.sleep(50);
        System.out.println("During sleep: " + monitoredThread.getState()); // TIMED_WAITING

        monitoredThread.join();
        System.out.println("After join: " + monitoredThread.getState()); // TERMINATED

        // isAlive() checks
        System.out.println("Is alive after join? " + monitoredThread.isAlive()); // false

        // Count active threads
        System.out.println("Active thread count: " + Thread.activeCount());

        ThreadGroup group = Thread.currentThread().getThreadGroup();
        System.out.println("Thread group active count: " + group.activeCount());

        System.out.println();
    }
}
