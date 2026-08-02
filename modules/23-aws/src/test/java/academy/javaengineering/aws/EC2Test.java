package academy.javaengineering.aws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EC2 Tests")
class EC2Test {

    private EC2Example example;

    @BeforeEach
    void setUp() {
        example = new EC2Example();
    }

    @Test
    @DisplayName("Should demonstrate instance types")
    void shouldDemonstrateInstanceTypes() {
        assertDoesNotThrow(() -> EC2Example.demonstrateInstanceTypes());
    }

    @Test
    @DisplayName("Should demonstrate security groups")
    void shouldDemonstrateSecurityGroups() {
        assertDoesNotThrow(() -> EC2Example.demonstrateSecurityGroups());
    }

    @Test
    @DisplayName("Should demonstrate launch configuration")
    void shouldDemonstrateLaunch() {
        assertDoesNotThrow(() -> EC2Example.demonstrateLaunch());
    }
}
