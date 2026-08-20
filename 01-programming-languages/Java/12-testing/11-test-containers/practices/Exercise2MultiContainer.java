package academy.javaengineering.testing.testcontainers.practices;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 2: Multiple Containers
 *
 * Tasks:
 * 1. Start PostgreSQL and Redis containers
 * 2. Test interaction between services
 * 3. Verify both containers run
 */
@Testcontainers
class Exercise2MultiContainer {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7"))
        .withExposedPorts(6379);

    @Test
    @DisplayName("should start Redis container")
    void shouldStartRedis() {
        // Assert Redis is running
    }
}
