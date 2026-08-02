package academy.javaengineering.systemdesign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Load Balancing Tests")
class LoadBalancingTest {

    private LoadBalancingExample lb;

    @BeforeEach
    void setUp() {
        lb = new LoadBalancingExample();
    }

    @Test
    @DisplayName("Should distribute requests via round robin without throwing")
    void shouldDistributeRequests() {
        assertDoesNotThrow(() -> lb.demonstrateRoundRobin());
        assertNotNull(lb);
    }

    @Test
    @DisplayName("Should cycle through servers in round robin")
    void shouldCycleServers() {
        String s1 = lb.getNextServer();
        String s2 = lb.getNextServer();
        assertNotEquals(s1, s2);
        assertNotNull(s1);
        assertNotNull(s2);
    }

    @Test
    @DisplayName("Should create LoadBalancingExample instance successfully")
    void shouldCreateInstance() {
        LoadBalancingExample instance = new LoadBalancingExample();
        assertNotNull(instance);
        assertInstanceOf(LoadBalancingExample.class, instance);
    }

    @Test
    @DisplayName("Should return non-null server names from getNextServer")
    void shouldReturnNonNullServers() {
        for (int i = 0; i < 10; i++) {
            String server = lb.getNextServer();
            assertNotNull(server);
            assertFalse(server.isEmpty());
        }
    }

    @Test
    @DisplayName("Should return valid server format from getNextServer")
    void shouldReturnValidServerFormat() {
        String server = lb.getNextServer();
        assertNotNull(server);
        assertTrue(server.contains(":"));
        assertTrue(server.contains("8080"));
    }

    @Test
    @DisplayName("Should cycle through all servers in order")
    void shouldCycleThroughAllServers() {
        String server1 = lb.getNextServer();
        String server2 = lb.getNextServer();
        String server3 = lb.getNextServer();
        String server4 = lb.getNextServer();
        assertNotNull(server1);
        assertNotNull(server2);
        assertNotNull(server3);
        assertNotNull(server4);
        assertNotEquals(server1, server2);
        assertNotEquals(server2, server3);
        assertEquals(server1, server4);
    }

    @Test
    @DisplayName("Should handle many round robin cycles")
    void shouldHandleManyCycles() {
        for (int i = 0; i < 100; i++) {
            String server = lb.getNextServer();
            assertNotNull(server);
            assertFalse(server.isEmpty());
        }
    }

    @Test
    @DisplayName("Should maintain consistent cycling pattern across instances")
    void shouldMaintainConsistentPattern() {
        LoadBalancingExample lb1 = new LoadBalancingExample();
        LoadBalancingExample lb2 = new LoadBalancingExample();
        assertEquals(lb1.getNextServer(), lb2.getNextServer());
        assertEquals(lb1.getNextServer(), lb2.getNextServer());
    }
}
