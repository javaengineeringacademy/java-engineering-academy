package academy.javaengineering.concurrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTuning {

    private static final AtomicInteger taskIdCounter = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        fixedThreadPoolCpuBound();
        cachedThreadPoolIoBound();
        scheduledThreadPoolPeriodic();
        forkJoinPoolDivideConquer();
        customThreadPoolExecutor();
        monitorPoolMetrics();
    }

    private static void fixedThreadPoolCpuBound() {
        System.out.println("=== Fixed Thread Pool (CPU-Bound) ===");
        int cpuCores = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(cpuCores + 1);

        long start = System.currentTimeMillis();
        for (int i = 0; i < cpuCores * 2; i++) {
            pool.submit(() -> {
                long sum = 0;
                for (int j = 0; j < 1_000_000; j++) sum += j;
                System.out.println("CPU task " + Thread.currentThread().getName() + " result=" + sum);
            });
        }
        pool.shutdown();
        try { pool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); System.err.println("Pool shutdown interrupted: " + e.getMessage()); }
        System.out.println("CPU-bound completed in " + (System.currentTimeMillis() - start) + "ms");
    }

    private static void cachedThreadPoolIoBound() {
        System.out.println("\n=== Cached Thread Pool (IO-Bound) ===");
        ExecutorService pool = Executors.newCachedThreadPool();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            final int taskId = taskIdCounter.incrementAndGet();
            pool.submit(() -> {
                try {
                    System.out.println("IO task " + taskId + " starting on " + Thread.currentThread().getName());
                    Thread.sleep(200);
                    System.out.println("IO task " + taskId + " completed");
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); System.err.println("IO task interrupted: " + e.getMessage()); }
            });
        }
        pool.shutdown();
        try { pool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); System.err.println("Pool shutdown interrupted: " + e.getMessage()); }
        System.out.println("IO-bound completed in " + (System.currentTimeMillis() - start) + "ms");
    }

    private static void scheduledThreadPoolPeriodic() {
        System.out.println("\n=== Scheduled Thread Pool (Periodic Tasks) ===");
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

        AtomicInteger counter = new AtomicInteger(0);
        ScheduledFuture<?> periodic = pool.scheduleAtFixedRate(() -> {
            int count = counter.incrementAndGet();
            System.out.println("Periodic task #" + count + " - " + Thread.currentThread().getName());
            if (count >= 5) {
                periodic.cancel(false);
                System.out.println("Periodic task cancelled after 5 executions");
            }
        }, 0, 300, TimeUnit.MILLISECONDS);

        ScheduledFuture<String> oneTime = pool.schedule(() -> {
            return "One-time task completed on " + Thread.currentThread().getName();
        }, 200, TimeUnit.MILLISECONDS);

        try {
            System.out.println("One-time result: " + oneTime.get());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        pool.shutdown();
        try { pool.awaitTermination(3, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); System.err.println("Scheduled pool shutdown interrupted: " + e.getMessage()); }
    }

    private static void forkJoinPoolDivideConquer() {
        System.out.println("\n=== ForkJoinPool (Divide and Conquer) ===");
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        long start = System.currentTimeMillis();
        Long result = forkJoinPool.invoke(new RecursiveTask<Long>() {
            private static final int THRESHOLD = 10;
            private final long n;

            RecursiveTask(long n) { this.n = n; }

            @Override
            protected Long compute() {
                if (n <= THRESHOLD) {
                    long sum = 0;
                    for (long i = 1; i <= n; i++) sum += i;
                    return sum;
                }
                RecursiveTask<Long> left = new RecursiveTask<Long>(n / 2) {
                    @Override protected Long compute() {
                        long s = 0; for (long i = 1; i <= n / 2; i++) s += i; return s;
                    }
                };
                RecursiveTask<Long> right = new RecursiveTask<Long>(n - n / 2) {
                    @Override protected Long compute() {
                        long s = 0; for (long i = n / 2 + 1; i <= n; i++) s += i; return s;
                    }
                };
                left.fork();
                long rightResult = right.compute();
                long leftResult = left.join();
                return leftResult + rightResult;
            }
        });

        System.out.println("ForkJoin sum 1..1000 = " + result + " in " + (System.currentTimeMillis() - start) + "ms");
    }

    private static void customThreadPoolExecutor() {
        System.out.println("\n=== Custom ThreadPoolExecutor ===");
        ThreadPoolExecutor customPool = new ThreadPoolExecutor(
            2,                      // core pool size
            4,                      // max pool size
            30, TimeUnit.SECONDS,   // keep alive time
            new ArrayBlockingQueue<>(5), // bounded queue
            new ThreadPoolExecutor.CallerRunsPolicy() // rejection handler
        );

        customPool.allowCoreThreadTimeOut(true);

        for (int i = 0; i < 12; i++) {
            final int id = taskIdCounter.incrementAndGet();
            customPool.execute(() -> {
                System.out.println("Custom task " + id + " on " + Thread.currentThread().getName());
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); System.err.println("Custom task interrupted: " + e.getMessage()); }
            });
        }

        System.out.println("Pool size: " + customPool.getPoolSize());
        System.out.println("Queue size: " + customPool.getQueue().size());
        System.out.println("Completed tasks: " + customPool.getCompletedTaskCount());

        customPool.shutdown();
        try { customPool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); System.err.println("Custom pool shutdown interrupted: " + e.getMessage()); }
    }

    private static void monitorPoolMetrics() {
        System.out.println("\n=== Monitor Pool Metrics ===");
        ThreadPoolExecutor monitorPool = new ThreadPoolExecutor(
            2, 4, 30L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10)
        );

        for (int i = 0; i < 8; i++) {
            final int id = taskIdCounter.incrementAndGet();
            monitorPool.execute(() -> {
                try {
                    System.out.println("Monitored task " + id + " running");
                    Thread.sleep(100);
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); System.err.println("Monitored task interrupted: " + e.getMessage()); }
            });

            if (i % 2 == 0) {
                System.out.println("  [Metrics] Active: " + monitorPool.getActiveCount()
                    + ", Pool: " + monitorPool.getPoolSize()
                    + ", Queue: " + monitorPool.getQueue().size()
                    + ", Completed: " + monitorPool.getCompletedTaskCount());
            }
        }

        monitorPool.shutdown();
        try { monitorPool.awaitTermination(3, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); System.err.println("Monitor pool shutdown interrupted: " + e.getMessage()); }

        System.out.println("Final metrics:");
        System.out.println("  Pool size: " + monitorPool.getPoolSize());
        System.out.println("  Largest pool size: " + monitorPool.getLargestPoolSize());
        System.out.println("  Completed tasks: " + monitorPool.getCompletedTaskCount());
        System.out.println("  Task count: " + monitorPool.getTaskCount());
    }
}
