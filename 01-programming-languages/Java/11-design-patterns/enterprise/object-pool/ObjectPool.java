package academy.javaengineering.patterns.enterprise.object_pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Generic thread-safe object pool implementation using a blocking queue.
 * Objects are created lazily up to the maximum pool size.
 *
 * @param <T> the type of pooled object
 */
public class ObjectPool<T> implements Pool<T> {

    private final LinkedBlockingQueue<T> pool;
    private final Supplier<T> factory;
    private final int maxSize;
    private final AtomicInteger totalCreated = new AtomicInteger(0);
    private volatile boolean shutdown = false;

    /**
     * Create a new object pool.
     *
     * @param maxSize maximum number of objects in the pool
     * @param factory supplier that creates new objects when needed
     */
    public ObjectPool(int maxSize, Supplier<T> factory) {
        this.maxSize = maxSize;
        this.factory = factory;
        this.pool = new LinkedBlockingQueue<>(maxSize);
    }

    @Override
    public T borrowObject() {
        if (shutdown) {
            throw new IllegalStateException("Pool is shut down");
        }

        T object = pool.poll();
        if (object == null && totalCreated.get() < maxSize) {
            object = factory.get();
            totalCreated.incrementAndGet();
        }
        if (object == null) {
            try {
                object = pool.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for pooled object", e);
            }
        }
        if (object == null) {
            throw new IllegalStateException("No objects available in pool");
        }
        return object;
    }

    @Override
    public void returnObject(T object) {
        if (object == null || shutdown) {
            return;
        }
        if (!pool.offer(object)) {
            // Pool is full, discard the object
            totalCreated.decrementAndGet();
        }
    }

    @Override
    public int getAvailableCount() {
        return pool.size();
    }

    @Override
    public int getTotalCount() {
        return totalCreated.get();
    }

    @Override
    public void shutdown() {
        shutdown = true;
        pool.clear();
    }
}
