package academy.javaengineering.concurrency.threadcreation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ThreadCreationInternals - Demonstrates internal mechanisms of thread creation.
 */
public class ThreadCreationInternals {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Object Structure ===");
        threadObjectStructure();

        System.out.println("\n=== Runnable vs Thread Internals ===");
        runnableVsThread();

        System.out.println("\n=== Callable and Future Internals ===");
        callableInternals();

        System.out.println("\n=== Virtual Thread Internals ===");
        virtualThreadInternals();
    }

    static void threadObjectStructure() {
        Thread t = new Thread(() -> {
            System.out.println("  Thread ID: " + Thread.currentThread().getId());
            System.out.println("  Thread name: " + Thread.currentThread().getName());
        }, "InternalDemo");

        System.out.println("  Before start - state: " + t.getState());
        System.out.println("  Thread object: " + t);
        System.out.println("  Class: " + t.getClass().getName());

        t.start();
        try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    static void runnableVsThread() throws InterruptedException {
        // Runnable approach - task is separate from thread
        Runnable task = () -> {
            System.out.println("  Runnable task executing on: " +
                Thread.currentThread().getName());
        };
        Thread t1 = new Thread(task, "RunnableThread");

        // Thread extension - task is embedded in thread
        Thread t2 = new Thread() {
            @Override
            public void run() {
                System.out.println("  Thread subclass executing on: " +
                    Thread.currentThread().getName());
            }
        };
        t2.setName("SubclassThread");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static void callableInternals() throws InterruptedException {
        Callable<Integer> callable = () -> {
            System.out.println("  Callable executing on: " +
                Thread.currentThread().getName());
            Thread.sleep(100);
            return 42;
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(callable);

        System.out.println("  Future created, task submitted");
        System.out.println("  Future is done: " + future.isDone());

        try {
            Integer result = future.get();
            System.out.println("  Future result: " + result);
            System.out.println("  Future is done: " + future.isDone());
        } catch (ExecutionException e) {
            System.out.println("  Task threw exception: " + e.getCause());
        }

        executor.shutdown();
    }

    static void virtualThreadInternals() {
        try {
            Thread vt = Thread.ofVirtual()
                .name("my-virtual-thread")
                .start(() -> {
                    System.out.println("  Virtual thread: " +
                        Thread.currentThread().getName());
                    System.out.println("  Is virtual: " +
                        Thread.currentThread().isVirtual());
                    System.out.println("  Is daemon: " +
                        Thread.currentThread().isDaemon());
                });

            vt.join();
            System.out.println("  Virtual thread completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
