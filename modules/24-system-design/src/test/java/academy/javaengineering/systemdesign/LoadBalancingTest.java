package academy.javaengineering.systemdesign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Load Balancing Tests")
class LoadBalancingTest {
    private LoadBalancingExample lb;

    @BeforeEach
    void setUp() { lb = new LoadBalancingExample(); }

    @Test @DisplayName("Should distribute requests via round robin")
    void shouldDistributeRequests() {
        assertDoesNotThrow(() -> lb.demonstrateRoundRobin());
    }
    @Test @DisplayName("Should cycle through servers")
    void shouldCycleServers() {
        String s1 = lb.getNextServer();
        String s2 = lb.getNextServer();
        assertNotEquals(s1, s2);
    }
}
