package academy.javaengineering.patterns.enterprise.object_pool;

/**
 * Demonstrates the Object Pool pattern with database connections.
 */
public class ObjectPoolExample {

    public static void main(String[] args) {
        System.out.println("=== Object Pool Pattern Demo ===\n");

        DatabaseConnectionPool pool = new DatabaseConnectionPool(3);

        System.out.println("\n--- Borrow connections ---");
        DatabaseConnection conn1 = pool.borrowObject();
        DatabaseConnection conn2 = pool.borrowObject();
        System.out.println("Available: " + pool.getAvailableCount() + "/" + pool.getTotalCount());

        System.out.println("\n--- Use connections ---");
        conn1.execute("SELECT * FROM users");
        conn2.execute("INSERT INTO logs VALUES (1, 'test')");

        System.out.println("\n--- Return one connection ---");
        pool.returnObject(conn1);
        System.out.println("Available: " + pool.getAvailableCount() + "/" + pool.getTotalCount());

        System.out.println("\n--- Borrow again (reused) ---");
        DatabaseConnection conn3 = pool.borrowObject();
        System.out.println("Borrowed: " + conn3);
        conn3.execute("UPDATE users SET active = true");

        System.out.println("\n--- Cleanup ---");
        pool.returnObject(conn2);
        pool.returnObject(conn3);
        pool.shutdown();
    }
}
