package academy.javaengineering.patterns.enterprise.object_pool;

/**
 * Concrete object pool for database connections.
 * Limits the number of concurrent database connections.
 */
public class DatabaseConnectionPool implements Pool<DatabaseConnection> {

    private final ObjectPool<DatabaseConnection> pool;

    public DatabaseConnectionPool(int maxConnections) {
        this.pool = new ObjectPool<>(maxConnections, DatabaseConnection::new);
        System.out.println("Created DatabaseConnectionPool with max size: " + maxConnections);
    }

    @Override
    public DatabaseConnection borrowObject() {
        DatabaseConnection conn = pool.borrowObject();
        System.out.println("Borrowed: " + conn);
        return conn;
    }

    @Override
    public void returnObject(DatabaseConnection conn) {
        if (conn != null) {
            conn.reset();
            System.out.println("Returned: " + conn);
        }
        pool.returnObject(conn);
    }

    @Override
    public int getAvailableCount() {
        return pool.getAvailableCount();
    }

    @Override
    public int getTotalCount() {
        return pool.getTotalCount();
    }

    @Override
    public void shutdown() {
        pool.shutdown();
        System.out.println("Connection pool shut down");
    }
}
