package academy.javaengineering.springboot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ActuatorTest {

    @Test
    void shouldReturnUPWhenAllHealthy() {
        ActuatorExample.HealthEndpoint health = new ActuatorExample.HealthEndpoint();
        health.addComponent("db", "UP");
        health.addComponent("redis", "UP");
        assertEquals("UP", health.getHealth());
    }

    @Test
    void shouldReturnDOWNWhenAnyUnhealthy() {
        ActuatorExample.HealthEndpoint health = new ActuatorExample.HealthEndpoint();
        health.addComponent("db", "UP");
        health.addComponent("redis", "DOWN");
        assertEquals("DOWN", health.getHealth());
    }
}
