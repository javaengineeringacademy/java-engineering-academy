package academy.javaengineering.concurrency.threadlocal.examples;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class ThreadLocalExamples {
    private static final ThreadLocal<SimpleDateFormat> dateFormat =
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 6; i++) {
            final int id = i;
            executor.submit(() -> {
                String formatted = dateFormat.get().format(new Date());
                System.out.println("Thread " + id + ": " + formatted +
                    " (thread: " + Thread.currentThread().getName() + ")");
            });
        }

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);

        // User context pattern
        ThreadLocal<String> user = new ThreadLocal<>();
        Runnable task = () -> {
            try {
                user.set("User-" + Thread.currentThread().getId());
                processRequest();
                processRequest();
            } finally {
                user.remove(); // cleanup
            }
        };

        new Thread(task).start();
        new Thread(task).start();
        Thread.sleep(500);
    }

    static void processRequest() {
        System.out.println("Processing for: " +
            ThreadLocal.withInitial(() -> "default").get());
    }
}
