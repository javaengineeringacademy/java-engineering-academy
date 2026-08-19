package academy.javaengineering.patterns.enterprise.object_pool;

/**
 * Interface for an object pool that manages reusable objects.
 *
 * @param <T> the type of pooled object
 */
public interface Pool<T> {

    /**
     * Borrow an object from the pool.
     * Blocks if no objects are available and pool is at max capacity.
     *
     * @return a pooled object
     */
    T borrowObject();

    /**
     * Return an object to the pool for reuse.
     *
     * @param object the object to return
     */
    void returnObject(T object);

    /**
     * Get the number of objects currently available in the pool.
     *
     * @return available object count
     */
    int getAvailableCount();

    /**
     * Get the total number of objects managed by the pool.
     *
     * @return total object count
     */
    int getTotalCount();

    /**
     * Shut down the pool and release all resources.
     */
    void shutdown();
}
