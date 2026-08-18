package academy.javaengineering.concurrency.framework.executor;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

public class ExecutorServiceExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Submit Runnable (no result)
        executor.execute(() -> System.out.println("Task executed"));

        // Submit Callable (with result)
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(200);
            return 42;
        });
        System.out.println("Result: " + future.get());

        // Multiple tasks
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int id = i;
            tasks.add(() -> {
                Thread.sleep(100 * (5 - id));
                return "Task-" + id;
            });
        }

        List<Future<String>> futures = executor.invokeAll(tasks);
        for (Future<String> f : futures) {
            System.out.println("Completed: " + f.get());
        }

        // Graceful shutdown
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("All tasks completed");
    }
}
