package academy.javaengineering.concurrency.framework.scheduled.solutions;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduledSolutions {
    public static void main(String[] args) throws Exception {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Solution 1: Fixed number of executions
        AtomicInteger count = new AtomicInteger(0);
        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
            int c = count.incrementAndGet();
            System.out.println("Execution " + c);
            if (c >= 5) scheduler.shutdown();
        }, 0, 2, TimeUnit.SECONDS);

        // Wait for completion
        scheduler.awaitTermination(15, TimeUnit.SECONDS);
        System.out.println("Completed 5 executions");
    }
}
