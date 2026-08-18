package academy.javaengineering.concurrency.framework.introduction;

import java.util.concurrent.*;

public class ExecutorHierarchy {
    public static void main(String[] args) throws Exception {
        // Executor → execute(Runnable)
        Executor executor = Runnable::run;
        executor.execute(() -> System.out.println("Executor: task executed"));

        // ExecutorService → submit(Callable) + lifecycle
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<String> future = service.submit(() -> "Hello from ExecutorService");
        System.out.println("ExecutorService: " + future.get());
        service.shutdown();

        // ScheduledExecutorService → delayed/periodic
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
        scheduled.schedule(() -> System.out.println("Scheduled: after 500ms"), 500, TimeUnit.MILLISECONDS);
        scheduled.scheduleAtFixedRate(() -> System.out.println("Periodic"), 0, 1000, TimeUnit.MILLISECONDS);
        Thread.sleep(2500);
        scheduled.shutdownNow();
    }
}
