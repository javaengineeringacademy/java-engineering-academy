package academy.javaengineering.concurrency.framework.callable;

import java.util.concurrent.*;
import java.util.*;

public class AllOfExample {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("=== 1. allOf() - 3 Parallel Tasks ===");
        CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> {
            sleep(300);
            System.out.println("  [User] loaded");
            return "Alice";
        });
        CompletableFuture<Integer> ageFuture = CompletableFuture.supplyAsync(() -> {
            sleep(200);
            System.out.println("  [Age] loaded");
            return 30;
        });
        CompletableFuture<String> emailFuture = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            System.out.println("  [Email] loaded");
            return "alice@example.com";
        });

        CompletableFuture<Void> allDone = CompletableFuture.allOf(userFuture, ageFuture, emailFuture);
        allDone.join();
        System.out.println("All loaded: " + userFuture.get() + ", " + ageFuture.get() + ", " + emailFuture.get());

        System.out.println("\n=== 2. Collect Results After allOf ===");
        CompletableFuture<String> r1 = CompletableFuture.supplyAsync(() -> { sleep(200); return "A"; });
        CompletableFuture<String> r2 = CompletableFuture.supplyAsync(() -> { sleep(150); return "B"; });
        CompletableFuture<String> r3 = CompletableFuture.supplyAsync(() -> { sleep(100); return "C"; });

        CompletableFuture.allOf(r1, r2, r3).join();
        List<String> results = List.of(r1.get(), r2.get(), r3.get());
        System.out.println("Results: " + results);

        System.out.println("\n=== 3. invokeAll() Pattern ===");
        List<Callable<String>> tasks = List.of(
            () -> { sleep(300); return "Task-A"; },
            () -> { sleep(200); return "Task-B"; },
            () -> { sleep(100); return "Task-C"; }
        );

        List<Future<String>> futures = executor.invokeAll(tasks);
        List<String> collected = new ArrayList<>();
        for (Future<String> f : futures) {
            collected.add(f.get());
        }
        System.out.println("invokeAll results: " + collected);

        System.out.println("\n=== 4. allOf with Timeout ===");
        CompletableFuture<String> t1 = CompletableFuture.supplyAsync(() -> { sleep(200); return "T1"; });
        CompletableFuture<String> t2 = CompletableFuture.supplyAsync(() -> { sleep(100); return "T2"; });

        try {
            CompletableFuture.allOf(t1, t2).get(1, TimeUnit.SECONDS);
            System.out.println("Completed within timeout: " + t1.get() + ", " + t2.get());
        } catch (TimeoutException e) {
            System.out.println("Timed out!");
        }

        System.out.println("\n=== 5. allOf with Error Handling ===");
        CompletableFuture<String> s1 = CompletableFuture.supplyAsync(() -> "OK");
        CompletableFuture<String> s2 = CompletableFuture.supplyAsync(() -> { throw new RuntimeException("Fail!"); });
        CompletableFuture<String> s3 = CompletableFuture.supplyAsync(() -> "OK2");

        CompletableFuture<String> safe1 = s1.exceptionally(ex -> "error");
        CompletableFuture<String> safe2 = s2.exceptionally(ex -> "error");
        CompletableFuture<String> safe3 = s3.exceptionally(ex -> "error");

        CompletableFuture.allOf(safe1, safe2, safe3).join();
        System.out.println("Results: " + safe1.get() + ", " + safe2.get() + ", " + safe3.get());

        System.out.println("\n=== 6. allOf for Aggregation ===");
        CompletableFuture<Integer> p1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> p2 = CompletableFuture.supplyAsync(() -> 20);
        CompletableFuture<Integer> p3 = CompletableFuture.supplyAsync(() -> 30);

        CompletableFuture.allOf(p1, p2, p3).join();
        int sum = p1.get() + p2.get() + p3.get();
        System.out.println("Sum of all: " + sum);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
