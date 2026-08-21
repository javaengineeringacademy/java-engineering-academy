package academy.javaengineering.logging.mdc.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Example: MDC propagation to child threads.
 */
public class MdcPropagationExample {

    private static final Logger logger = LoggerFactory.getLogger(MdcPropagationExample.class);
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws Exception {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        MDC.put("userId", "user-123");

        try {
            logger.info("Starting request processing");

            // Method 1: Manual copy/restore
            CompletableFuture<Void> future1 = CompletableFuture.runAsync(
                withMdc(() -> {
                    logger.info("Task 1 executing");
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    logger.info("Task 1 completed");
                })
            );

            // Method 2: Supply and restore
            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(
                withMdcSupplier(() -> {
                    logger.info("Task 2 executing");
                    return "result-2";
                })
            );

            CompletableFuture.allOf(future1, future2).join();
            logger.info("All tasks completed");

        } finally {
            MDC.clear();
            executor.shutdown();
        }
    }

    private static Runnable withMdc(Runnable task) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            if (contextMap != null) {
                MDC.setContextMap(contextMap);
            }
            try {
                task.run();
            } finally {
                MDC.clear();
            }
        };
    }

    private static <T> java.util.function.Supplier<T> withMdcSupplier(java.util.function.Supplier<T> task) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            if (contextMap != null) {
                MDC.setContextMap(contextMap);
            }
            try {
                return task.get();
            } finally {
                MDC.clear();
            }
        };
    }
}
