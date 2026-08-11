package academy.javaengineering.exceptions.thread.internals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates JVM internals of thread exception handling.
 */
public class ThreadExceptionsInternals {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Thread Exception Internals ===\n");

        demoThreadIsolation();
        demoExceptionUnwinding();
        demoStackWalkerWithException();
        demoFutureTaskInternals();
    }

    static void demoThreadIsolation() throws InterruptedException {
        System.out.println("--- Thread Isolation ---");

        CountDownLatch latch = new CountDownLatch(2);

        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(50);
                throw new RuntimeException("Thread 1 error");
            } catch (Exception e) {
                System.out.println("Thread 1 caught: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(100);
                System.out.println("Thread 2: Still running after Thread 1 exception");
            } catch (Exception e) {
                System.out.println("Thread 2 error: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        thread1.start();
        thread2.start();
        latch.await(5, TimeUnit.SECONDS);
        System.out.println();
    }

    static void demoExceptionUnwinding() {
        System.out.println("--- Exception Stack Unwinding ---");

        try {
            methodA();
        } catch (Exception e) {
            System.out.println("Caught at top: " + e.getMessage());
            System.out.println("Stack trace:");
            for (StackTraceElement frame : e.getStackTrace()) {
                System.out.println("  at " + frame);
            }
        }
        System.out.println();
    }

    static void methodA() {
        methodB();
    }

    static void methodB() {
        methodC();
    }

    static void methodC() {
        throw new RuntimeException("Unwinding from methodC");
    }

    static void demoStackWalkerWithException() {
        System.out.println("--- StackWalker During Exception ---");

        try {
            throw new RuntimeException("For stack walking");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println("StackWalker output:");

            StackWalker walker = StackWalker.getInstance();
            walker.walk(stackFrame -> {
                stackFrame.forEach(frame -> {
                    System.out.println("  " + frame.getClassName()
                        + "." + frame.getMethodName()
                        + "(" + frame.getFileName()
                        + ":" + frame.getLineNumber() + ")");
                });
                return null;
            });
        }
        System.out.println();
    }

    static void demoFutureTaskInternals() {
        System.out.println("--- FutureTask Exception Capture ---");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        var future = executor.submit(() -> {
            Thread.sleep(50);
            throw new RuntimeException("FutureTask exception");
        });

        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("Exception type: " + e.getClass().getSimpleName());
            System.out.println("Cause type: " + e.getCause().getClass().getSimpleName());
            System.out.println("Cause message: " + e.getCause().getMessage());
        }

        executor.shutdown();

        System.out.println("\nCompletableFuture chain exception propagation:");
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "start")
            .thenApply(s -> {
                throw new RuntimeException("CF chain error");
            })
            .thenApply(s -> "never reached")
            .exceptionally(ex -> {
                System.out.println("CF caught: " + ex.getMessage());
                return "recovered";
            });

        System.out.println("Result: " + cf.join());
    }
}
