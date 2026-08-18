package academy.javaengineering.concurrency.examples;

import java.util.concurrent.TimeUnit;

public class ThreadLocalExamples {

    private static ThreadLocal<String> userContext = ThreadLocal.withInitial(() -> "default-user");
    private static ThreadLocal<Integer> threadLocalCounter = ThreadLocal.withInitial(() -> 0);
    private static InheritableThreadLocal<String> inheritableContext = new InheritableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        example1_BasicThreadLocal();
        example2_ThreadLocalPerThread();
        example3_InheritableThreadLocal();
        example4_ThreadLocalCleanup();
        example5_ThreadLocalWithExecutor();
    }

    // Example 1: Basic ThreadLocal usage
    static void example1_BasicThreadLocal() throws InterruptedException {
        System.out.println("=== Example 1: Basic ThreadLocal ===");

        // Each thread gets its own copy
        Runnable task = () -> {
            String name = Thread.currentThread().getName();
            userContext.set(name + "-context");
            System.out.println(name + " set context: " + userContext.get());
        };

        Thread t1 = new Thread(task, "User-A");
        Thread t2 = new Thread(task, "User-B");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Main thread has its own value
        System.out.println("Main thread context: " + userContext.get());

        System.out.println();
    }

    // Example 2: ThreadLocal with per-thread state
    static void example2_ThreadLocalPerThread() throws InterruptedException {
        System.out.println("=== Example 2: Per-Thread State ===");

        // Counter is independent per thread
        Runnable counterTask = () -> {
            for (int i = 0; i < 5; i++) {
                threadLocalCounter.set(threadLocalCounter.get() + 1);
                System.out.println(Thread.currentThread().getName() +
                        " counter: " + threadLocalCounter.get());
            }
        };

        Thread t1 = new Thread(counterTask, "Counter-A");
        Thread t2 = new Thread(counterTask, "Counter-B");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Main thread counter is still 0
        System.out.println("Main counter: " + threadLocalCounter.get());

        System.out.println();
    }

    // Example 3: InheritableThreadLocal
    static void example3_InheritableThreadLocal() throws InterruptedException {
        System.out.println("=== Example 3: InheritableThreadLocal ===");

        // Parent sets value
        inheritableContext.set("parent-value");

        Thread child = new Thread(() -> {
            // Child inherits parent's value
            System.out.println("Child sees: " + inheritableContext.get());

            // Child can override
            inheritableContext.set("child-value");
            System.out.println("Child set: " + inheritableContext.get());
        }, "ChildThread");

        child.start();
        child.join();

        // Parent still has its own value
        System.out.println("Parent still has: " + inheritableContext.get());

        // Override InheritableThreadLocal for custom behavior
        InheritableThreadLocal<Long> timestampInherited = new InheritableThreadLocal<Long>() {
            @Override
            protected Long initialValue() {
                return System.currentTimeMillis();
            }

            @Override
            protected Long childValue(Long parentValue) {
                // Custom child value - could be parent value + offset, etc.
                return parentValue + 1000;
            }
        };

        timestampInherited.set(1000L);
        Thread childThread = new Thread(() -> {
            System.out.println("Child timestamp: " + timestampInherited.get() +
                    " (parent + 1000)");
        });

        childThread.start();
        childThread.join();

        System.out.println();
    }

    // Example 4: ThreadLocal cleanup (important for thread pools!)
    static void example4_ThreadLocalCleanup() throws InterruptedException {
        System.out.println("=== Example 4: ThreadLocal Cleanup ===");

        ThreadLocal<byte[]> memoryLeak = ThreadLocal.withInitial(() -> new byte[1024 * 1024]); // 1MB

        Thread t = new Thread(() -> {
            System.out.println("Thread allocated 1MB via ThreadLocal");
            System.out.println("Value present: " + (memoryLeak.get() != null));

            // Always clean up when done, especially in thread pools!
            memoryLeak.remove(); // Releases the value
            System.out.println("After remove(): " + memoryLeak.get()); // null
        });

        t.start();
        t.join();

        System.out.println("Without remove(), ThreadLocal values persist until thread dies!");
        System.out.println("In thread pools, threads are reused -> memory leaks!");

        System.out.println();
    }

    // Example 5: ThreadLocal with ExecutorService
    static void example5_ThreadLocalWithExecutor() throws InterruptedException {
        System.out.println("=== Example 5: ThreadLocal with Executor ===");

        java.util.concurrent.ExecutorService executor =
                java.util.concurrent.Executors.newFixedThreadPool(2);

        ThreadLocal<String> executorLocal = ThreadLocal.withInitial(() -> "unset");

        // Task 1
        executor.submit(() -> {
            executorLocal.set("task1-data");
            System.out.println("Task 1 set: " + executorLocal.get());
            TimeUnit.MILLISECONDS.sleep(100);
            System.out.println("Task 1 after sleep: " + executorLocal.get());
            executorLocal.remove(); // Clean up!
        });

        // Task 2
        executor.submit(() -> {
            executorLocal.set("task2-data");
            System.out.println("Task 2 set: " + executorLocal.get());
            executorLocal.remove();
        });

        // Same thread might run another task - ThreadLocal is reset!
        executor.submit(() -> {
            System.out.println("Task 3 (reuses thread): " + executorLocal.get());
            executorLocal.remove();
        });

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println();
    }
}
