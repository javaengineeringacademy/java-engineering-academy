package academy.javaengineering.concurrency.examples;

public class ThreadBasicsExamples {

    public static void main(String[] args) throws InterruptedException {
        example1_ExtendThread();
        example2_ImplementRunnable();
        example3_LambdaRunnable();
        example4_ThreadNaming();
        example5_DaemonThreads();
    }

    // Example 1: Extending Thread class
    static void example1_ExtendThread() throws InterruptedException {
        System.out.println("=== Example 1: Extending Thread Class ===");

        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("Thread running: " + Thread.currentThread().getName());
                System.out.println("Thread ID: " + Thread.currentThread().getId());
            }
        };
        thread.setName("MyCustomThread");
        thread.start();
        thread.join();

        System.out.println();
    }

    // Example 2: Implementing Runnable interface
    static void example2_ImplementRunnable() throws InterruptedException {
        System.out.println("=== Example 2: Implementing Runnable ===");

        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("Runnable task - Step " + i + " [" + Thread.currentThread().getName() + "]");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        };

        Thread thread = new Thread(task, "RunnableThread");
        thread.start();
        thread.join();

        System.out.println();
    }

    // Example 3: Lambda Runnable (concise syntax)
    static void example3_LambdaRunnable() throws InterruptedException {
        System.out.println("=== Example 3: Lambda Runnable ===");

        Thread lambdaThread = new Thread(() -> {
            System.out.println("Lambda thread: " + Thread.currentThread().getName());
            System.out.println("Is daemon? " + Thread.currentThread().isDaemon());
        }, "LambdaThread");

        lambdaThread.start();
        lambdaThread.join();

        // Runnable as lambda with loop
        Runnable countingTask = () -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("Count: " + i);
            }
        };

        Thread counter = new Thread(countingTask);
        counter.start();
        counter.join();

        System.out.println();
    }

    // Example 4: Thread naming and identification
    static void example4_ThreadNaming() throws InterruptedException {
        System.out.println("=== Example 4: Thread Naming ===");

        // Default naming
        Thread t1 = new Thread(() -> System.out.println("Default name: " + Thread.currentThread().getName()));
        t1.start();
        t1.join();

        // Custom naming
        Thread t2 = new Thread(() -> System.out.println("Custom name: " + Thread.currentThread().getName()), "Worker-1");
        t2.start();
        t2.join();

        // Thread ID
        System.out.println("Main thread ID: " + Thread.currentThread().getId());
        System.out.println("Main thread name: " + Thread.currentThread().getName());
        System.out.println("Main thread priority: " + Thread.currentThread().getPriority());
        System.out.println("Main thread state: " + Thread.currentThread().getState());

        System.out.println();
    }

    // Example 5: Daemon threads
    static void example5_DaemonThreads() throws InterruptedException {
        System.out.println("=== Example 5: Daemon Threads ===");

        Thread daemonThread = new Thread(() -> {
            int count = 0;
            while (count < 5) {
                System.out.println("Daemon working... count=" + count);
                count++;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "DaemonThread");

        daemonThread.setDaemon(true);
        daemonThread.setPriority(Thread.MIN_PRIORITY);

        System.out.println("Is daemon before start? " + daemonThread.isDaemon());
        daemonThread.start();

        // Main thread does some work
        Thread.sleep(500);
        System.out.println("Main thread finishing - daemon will be stopped if JVM exits");
        // daemonThread will be killed when all non-daemon threads finish

        System.out.println();
    }
}
