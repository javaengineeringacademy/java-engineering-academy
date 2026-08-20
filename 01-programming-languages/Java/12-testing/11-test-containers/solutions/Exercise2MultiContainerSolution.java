package academy.javaengineering.testing.testcontainers.solutions;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class Exercise2MultiContainerSolution {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7"))
        .withExposedPorts(6379);

    @Test
    void shouldStartRedis() {
        assertTrue(redis.isRunning());
        assertNotNull(redis.getMappedPort(6379));
    }
}
