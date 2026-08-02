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
    @DisplayName("Should demonstrate instance types without throwing")
    void shouldDemonstrateInstanceTypes() {
        assertDoesNotThrow(() -> EC2Example.demonstrateInstanceTypes());
        assertDoesNotThrow(() -> EC2Example.demonstrateInstanceTypes());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate security groups without throwing")
    void shouldDemonstrateSecurityGroups() {
        assertDoesNotThrow(() -> EC2Example.demonstrateSecurityGroups());
        assertDoesNotThrow(() -> EC2Example.demonstrateSecurityGroups());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate launch configuration without throwing")
    void shouldDemonstrateLaunch() {
        assertDoesNotThrow(() -> EC2Example.demonstrateLaunch());
        assertDoesNotThrow(() -> EC2Example.demonstrateLaunch());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should create EC2Example instance successfully")
    void shouldCreateInstance() {
        EC2Example instance = new EC2Example();
        assertNotNull(instance);
        assertInstanceOf(EC2Example.class, instance);
    }

    @Test
    @DisplayName("Should call all EC2 demonstrations in order")
    void shouldCallAllDemonstrationsInOrder() {
        assertAll("All EC2 demonstrations",
            () -> assertDoesNotThrow(() -> EC2Example.demonstrateInstanceTypes()),
            () -> assertDoesNotThrow(() -> EC2Example.demonstrateSecurityGroups()),
            () -> assertDoesNotThrow(() -> EC2Example.demonstrateLaunch())
        );
    }

    @Test
    @DisplayName("Should handle repeated instance type demonstrations")
    void shouldHandleRepeatedInstanceTypeCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> EC2Example.demonstrateInstanceTypes());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should handle repeated security group demonstrations")
    void shouldHandleRepeatedSecurityGroupCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> EC2Example.demonstrateSecurityGroups());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should maintain statelessness across EC2 demonstrations")
    void shouldMaintainStatelessness() {
        EC2Example first = new EC2Example();
        EC2Example second = new EC2Example();
        assertDoesNotThrow(() -> EC2Example.demonstrateInstanceTypes());
        assertDoesNotThrow(() -> EC2Example.demonstrateSecurityGroups());
        assertNotNull(first);
        assertNotNull(second);
        assertNotSame(first, second);
    }
}
