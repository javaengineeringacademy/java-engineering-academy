package academy.javaengineering.patterns.enterprise.object_pool;

/**
 * Tests for the Object Pool pattern.
 */
public class ObjectPoolTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Object Pool Pattern Tests ===\n");

        testBorrowObject();
        testReturnObject();
        testPoolCapacity();
        testBorrowAll();
        testReuse();
        testShutdown();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    private static void testBorrowObject() {
        Pool<DatabaseConnection> pool = new DatabaseConnectionPool(2);
        DatabaseConnection conn = pool.borrowObject();
        assertTest("Borrow returns non-null", conn != null);
        assertTest("Borrowed connection is active", conn.isActive());
        pool.shutdown();
    }

    private static void testReturnObject() {
        Pool<DatabaseConnection> pool = new DatabaseConnectionPool(2);
        DatabaseConnection conn = pool.borrowObject();
        pool.returnObject(conn);
        assertTest("Return increases available", pool.getAvailableCount() == 1);
        pool.shutdown();
    }

    private static void testPoolCapacity() {
        Pool<DatabaseConnection> pool = new DatabaseConnectionPool(3);
        DatabaseConnection c1 = pool.borrowObject();
        DatabaseConnection c2 = pool.borrowObject();
        DatabaseConnection c3 = pool.borrowObject();
        assertTest("Pool tracks total", pool.getTotalCount() == 3);
        assertTest("Pool tracks available", pool.getAvailableCount() == 0);
        pool.returnObject(c1);
        pool.returnObject(c2);
        pool.returnObject(c3);
        pool.shutdown();
    }

    private static void testBorrowAll() {
        Pool<DatabaseConnection> pool = new DatabaseConnectionPool(2);
        pool.borrowObject();
        pool.borrowObject();
        assertTest("Borrow all available", pool.getAvailableCount() == 0);
        pool.shutdown();
    }

    private static void testReuse() {
        Pool<DatabaseConnection> pool = new DatabaseConnectionPool(1);
        DatabaseConnection first = pool.borrowObject();
        int firstId = first.getId();
        pool.returnObject(first);
        DatabaseConnection second = pool.borrowObject();
        assertTest("Reuses same object", second.getId() == firstId);
        pool.returnObject(second);
        pool.shutdown();
    }

    private static void testShutdown() {
        Pool<DatabaseConnection> pool = new DatabaseConnectionPool(2);
        pool.borrowObject();
        pool.shutdown();
        try {
            pool.borrowObject();
            assertTest("Shutdown prevents borrow", false);
        } catch (IllegalStateException e) {
            assertTest("Shutdown prevents borrow", true);
        }
    }

    private static void assertTest(String name, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + name);
            passed++;
        } else {
            System.out.println("  FAIL: " + name);
            failed++;
        }
    }
}
