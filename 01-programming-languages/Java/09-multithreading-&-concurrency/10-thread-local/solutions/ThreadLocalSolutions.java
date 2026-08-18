package academy.javaengineering.concurrency.threadlocal.solutions;

import java.util.concurrent.*;

public class ThreadLocalSolutions {
    public static void main(String[] args) throws Exception {
        // Solution 1: ThreadLocal random
        ThreadLocal<java.util.Random> random = ThreadLocal.withInitial(java.util.Random::new);

        Runnable task = () -> {
            try {
                int value = random.get().nextInt(100);
                System.out.println(Thread.currentThread().getName() + ": " + value);
            } finally {
                random.remove();
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 4; i++) executor.submit(task);
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
    }
}
