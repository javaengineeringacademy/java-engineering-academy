package academy.javaengineering.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ConnectionPoolingTest {

    private ConnectionPoolingExample.SimpleConnectionPool pool;

    @BeforeEach
    void setUp() {
        pool = new ConnectionPoolingExample.SimpleConnectionPool(5);
    }

    @Test
    void shouldInitializeWithMaxConnections() {
        assertEquals(5, pool.getAvailableConnections());
    }

    @Test
    void shouldBorrowConnection() throws InterruptedException {
        String conn = pool.borrowConnection();
        assertNotNull(conn);
        assertEquals(4, pool.getAvailableConnections());
    }

    @Test
    void shouldReturnConnection() throws InterruptedException {
        String conn = pool.borrowConnection();
        pool.returnConnection(conn);
        assertEquals(5, pool.getAvailableConnections());
    }

    @Test
    void shouldHandleMultipleBorrows() throws InterruptedException {
        String conn1 = pool.borrowConnection();
        String conn2 = pool.borrowConnection();
        assertEquals(3, pool.getAvailableConnections());
        pool.returnConnection(conn1);
        pool.returnConnection(conn2);
        assertEquals(5, pool.getAvailableConnections());
    }
}
