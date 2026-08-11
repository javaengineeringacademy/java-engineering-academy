/**
 * Demonstrates thread exception handling patterns including uncaught
 * exception handlers, execute vs submit differences, Future.get()
 * exception handling, and CompletableFuture exceptionally/handle/chain
 * propagation.
 *
 * <p><b>Complexity:</b> O(1) per operation unless noted.</p>
 * <p><b>Thread-safety:</b> Not thread-safe — uses static mutable state.</p>
 * <p><b>Key characteristics:</b> Covers uncaught exception handlers,
 * executor exception handling, and CompletableFuture patterns.</p>
 */
package academy.javaengineering.exceptions.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Demonstrates thread exception handling patterns.
 */
public class ThreadExceptions {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Thread Exception Handling ===\n");

        demoUncaughtExceptionHandler();
        demoDefaultUncaughtExceptionHandler();
        demoExecuteVsSubmit();
        demoFutureGetException();
        demoCompletableFutureExceptionally();
        demoCompletableFutureHandle();
        demoCompletableFutureChain();
    }

    static void demoUncaughtExceptionHandler() throws InterruptedException {
        System.out.println("--- UncaughtExceptionHandler ---");

        Thread thread = new Thread(() -> {
            throw new RuntimeException("Thread error occurred");
        });

        thread.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("Handler caught: " + e.getMessage()
                + " in thread: " + t.getName());
        });

        thread.start();
        thread.join();
        System.out.println();
    }

    static void demoDefaultUncaughtExceptionHandler() throws InterruptedException {
        System.out.println("--- Default UncaughtExceptionHandler ---");

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println("Default handler: " + e.getMessage()
                + " in thread: " + t.getName());
        });

        Thread thread = new Thread(() -> {
            throw new RuntimeException("Default handler test");
        });

        thread.start();
        thread.join();
        System.out.println();
    }

    static void demoExecuteVsSubmit() throws Exception {
        System.out.println("--- execute() vs submit() ---");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // execute() — exception goes to UncaughtExceptionHandler
        executor.execute(() -> {
            throw new RuntimeException("execute() exception");
        });

        // submit() — exception captured in Future
        Future<?> future = executor.submit(() -> {
            throw new RuntimeException("submit() exception");
        });

        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            System.out.println("Captured from submit(): " + e.getCause().getMessage());
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    static void demoFutureGetException() throws Exception {
        System.out.println("--- Future.get() Exception Handling ---");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            Thread.sleep(100);
            throw new RuntimeException("Task failed");
        });

        try {
            String result = future.get(5, TimeUnit.SECONDS);
            System.out.println("Result: " + result);
        } catch (ExecutionException e) {
            System.out.println("ExecutionException cause: " + e.getCause().getMessage());
        } catch (TimeoutException e) {
            System.out.println("Task timed out");
            future.cancel(true);
        }

        executor.shutdown();
        System.out.println();
    }

    static void demoCompletableFutureExceptionally() {
        System.out.println("--- CompletableFuture.exceptionally() ---");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Random failure");
            }
            return "Success";
        }).exceptionally(ex -> {
            System.out.println("Exceptionally caught: " + ex.getMessage());
            return "Fallback";
        });

        System.out.println("Result: " + future.join());
        System.out.println();
    }

    static void demoCompletableFutureHandle() {
        System.out.println("--- CompletableFuture.handle() ---");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Handle test");
        }).handle((result, ex) -> {
            if (ex != null) {
                return "Error handled: " + ex.getMessage();
            }
            return "Success: " + result;
        });

        System.out.println("Result: " + future.join());
        System.out.println();
    }

    static void demoCompletableFutureChain() {
        System.out.println("--- CompletableFuture Chain Exception Propagation ---");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Step 1: Starting");
            return "Step 1 complete";
        })
        .thenApply(result -> {
            System.out.println("Step 2: Starting");
            if (Math.random() > 0.3) {
                throw new RuntimeException("Step 2 failed");
            }
            return result + " -> Step 2 complete";
        })
        .thenApply(result -> {
            System.out.println("Step 3: Starting (should be skipped if step 2 fails)");
            return result + " -> Step 3 complete";
        })
        .exceptionally(ex -> {
            System.out.println("Caught in exceptionally: " + ex.getMessage());
            return "Recovered from chain";
        });

        System.out.println("Final result: " + future.join());
    }
}
