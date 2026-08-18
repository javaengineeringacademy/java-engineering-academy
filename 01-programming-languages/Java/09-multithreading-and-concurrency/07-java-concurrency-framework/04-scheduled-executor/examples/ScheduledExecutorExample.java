package academy.javaengineering.concurrency.framework.scheduled;

import java.util.concurrent.*;

public class ScheduledExecutorExample {
    public static void main(String[] args) throws Exception {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // One-shot delay
        scheduler.schedule(
            () -> System.out.println("Delayed task"),
            1, TimeUnit.SECONDS
        );

        // Fixed rate (period from start of each execution)
        scheduler.scheduleAtFixedRate(
            () -> System.out.println("Rate: " + System.currentTimeMillis()),
            0, 500, TimeUnit.MILLISECONDS
        );

        // Fixed delay (delay from end of each execution)
        scheduler.scheduleWithFixedDelay(
            () -> {
                System.out.println("Delay start");
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                System.out.println("Delay end");
            },
            0, 500, TimeUnit.MILLISECONDS
        );

        Thread.sleep(3000);
        scheduler.shutdownNow();
    }
}
