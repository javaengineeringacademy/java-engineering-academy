package academy.javaengineering.database;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Connection Pooling - HikariCP, Connection Pool Tuning.
 */
public class ConnectionPoolingExample {

    public static class SimpleConnectionPool {
        private final BlockingQueue<String> pool;
        private final int maxSize;

        public SimpleConnectionPool(int maxSize) {
            this.maxSize = maxSize;
            this.pool = new ArrayBlockingQueue<>(maxSize);
            for (int i = 0; i < maxSize; i++) {
                pool.offer("Connection-" + i);
            }
        }

        public String borrowConnection() throws InterruptedException {
            return pool.poll(5, TimeUnit.SECONDS);
        }

        public void returnConnection(String connection) {
            pool.offer(connection);
        }

        public int getAvailableConnections() { return pool.size(); }
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleConnectionPool pool = new SimpleConnectionPool(5);
        System.out.println("Available: " + pool.getAvailableConnections());
        String conn = pool.borrowConnection();
        System.out.println("Borrowed: " + conn);
        System.out.println("Available after borrow: " + pool.getAvailableConnections());
        pool.returnConnection(conn);
        System.out.println("Available after return: " + pool.getAvailableConnections());
    }
}
