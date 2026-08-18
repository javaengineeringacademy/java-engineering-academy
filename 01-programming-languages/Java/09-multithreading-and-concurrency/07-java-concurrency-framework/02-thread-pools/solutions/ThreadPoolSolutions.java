package academy.javaengineering.concurrency.framework.pools.solutions;

import java.util.concurrent.*;

public class ThreadPoolSolutions {
    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2, 4, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(5),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        Thread monitor = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.printf("Pool: %d, Active: %d, Queue: %d, Completed: %d%n",
                    pool.getPoolSize(), pool.getActiveCount(),
                    pool.getQueue().size(), pool.getCompletedTaskCount());
                try { Thread.sleep(500); } catch (InterruptedException e) { break; }
            }
        });
        monitor.setDaemon(true);
        monitor.start();

        for (int i = 0; i < 20; i++) {
            final int id = i;
            pool.execute(() -> {
                System.out.println("Task " + id);
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        monitor.interrupt();
    }
}
