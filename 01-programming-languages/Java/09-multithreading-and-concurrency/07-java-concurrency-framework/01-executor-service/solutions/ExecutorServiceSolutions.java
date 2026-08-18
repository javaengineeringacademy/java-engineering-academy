package academy.javaengineering.concurrency.framework.executor.solutions;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

public class ExecutorServiceSolutions {
    public static void main(String[] args) throws Exception {
        // Solution 1
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++) {
            final int id = i;
            executor.submit(() -> System.out.println("Task " + id + " on " + Thread.currentThread().getName()));
        }

        // Solution 2: Graceful shutdown
        executor.shutdown();
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            System.out.println("Forced shutdown");
        }

        // Solution 3: invokeAll
        ExecutorService exec2 = Executors.newFixedThreadPool(3);
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int id = i;
            tasks.add(() -> "Result-" + id);
        }
        List<Future<String>> futures = exec2.invokeAll(tasks);
        for (Future<String> f : futures) System.out.println(f.get());
        exec2.shutdown();
    }
}
