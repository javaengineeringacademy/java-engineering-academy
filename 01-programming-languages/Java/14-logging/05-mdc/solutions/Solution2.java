package academy.javaengineering.logging.mdc.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Solution 2: MDC-aware executor service.
 */
public class Solution2 {

    private static final Logger logger = LoggerFactory.getLogger(Solution2.class);

    public static class MdcExecutorService extends ExecutorServiceWrapper {
        
        public MdcExecutorService(ExecutorService delegate) {
            super(delegate);
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return super.submit(() -> {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                try {
                    return task.call();
                } finally {
                    MDC.clear();
                }
            });
        }

        @Override
        public Future<?> submit(Runnable task) {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return super.submit(() -> {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                try {
                    task.run();
                } finally {
                    MDC.clear();
                }
            });
        }
    }

    // Abstract wrapper to avoid implementing all methods
    public static abstract class ExecutorServiceWrapper implements ExecutorService {
        protected final ExecutorService delegate;

        ExecutorServiceWrapper(ExecutorService delegate) {
            this.delegate = delegate;
        }

        @Override public void execute(Runnable command) { delegate.execute(command); }
        @Override public void shutdown() { delegate.shutdown(); }
        @Override public java.util.List<Runnable> shutdownNow() { return delegate.shutdownNow(); }
        @Override public boolean isShutdown() { return delegate.isShutdown(); }
        @Override public boolean isTerminated() { return delegate.isTerminated(); }
        @Override public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return delegate.awaitTermination(timeout, unit);
        }
        @Override public <T> java.util.List<Future<T>> invokeAll(java.util.Collection<? extends Callable<T>> tasks) throws InterruptedException {
            return delegate.invokeAll(tasks);
        }
        @Override public <T> java.util.List<Future<T>> invokeAll(java.util.Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            return delegate.invokeAll(tasks, timeout, unit);
        }
        @Override public <T> T invokeAny(java.util.Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            return delegate.invokeAny(tasks);
        }
        @Override public <T> T invokeAny(java.util.Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return delegate.invokeAny(tasks, timeout, unit);
        }
        @Override public <T> Future<T> submit(Callable<T> task) { throw new UnsupportedOperationException(); }
        @Override public <T> Future<T> submit(Runnable task, T result) { throw new UnsupportedOperationException(); }
        @Override public Future<?> submit(Runnable task) { throw new UnsupportedOperationException(); }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executor = new MdcExecutorService(Executors.newFixedThreadPool(3));
        
        MDC.put("requestId", "test-request");
        MDC.put("userId", "user-123");
        
        try {
            for (int i = 0; i < 5; i++) {
                final int index = i;
                executor.submit(() -> {
                    logger.info("Task {} executing", index);
                    logger.debug("MDC available: requestId={}", MDC.get("requestId"));
                });
            }
        } finally {
            MDC.clear();
            executor.shutdown();
        }
    }
}
