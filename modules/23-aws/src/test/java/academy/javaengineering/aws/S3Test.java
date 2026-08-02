package academy.javaengineering.aws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("S3 Tests")
class S3Test {

    private S3Example example;

    @BeforeEach
    void setUp() {
        example = new S3Example();
    }

    @Test
    @DisplayName("Should demonstrate storage classes without throwing")
    void shouldDemonstrateStorageClasses() {
        assertDoesNotThrow(() -> S3Example.demonstrateStorageClasses());
        assertDoesNotThrow(() -> S3Example.demonstrateStorageClasses());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate operations without throwing")
    void shouldDemonstrateOperations() {
        assertDoesNotThrow(() -> S3Example.demonstrateOperations());
        assertDoesNotThrow(() -> S3Example.demonstrateOperations());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate security without throwing")
    void shouldDemonstrateSecurity() {
        assertDoesNotThrow(() -> S3Example.demonstrateSecurity());
        assertDoesNotThrow(() -> S3Example.demonstrateSecurity());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should create S3Example instance successfully")
    void shouldCreateInstance() {
        S3Example instance = new S3Example();
        assertNotNull(instance);
        assertInstanceOf(S3Example.class, instance);
    }

    @Test
    @DisplayName("Should call all S3 demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All S3 demonstrations",
            () -> assertDoesNotThrow(() -> S3Example.demonstrateStorageClasses()),
            () -> assertDoesNotThrow(() -> S3Example.demonstrateOperations()),
            () -> assertDoesNotThrow(() -> S3Example.demonstrateSecurity())
        );
    }

    @Test
    @DisplayName("Should handle repeated storage class demonstrations")
    void shouldHandleRepeatedStorageClassCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> S3Example.demonstrateStorageClasses());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should handle repeated S3 security demonstrations")
    void shouldHandleRepeatedSecurityCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> S3Example.demonstrateSecurity());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should handle rapid operations demonstrations")
    void shouldHandleRapidOperationsCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> S3Example.demonstrateOperations());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should maintain consistent behavior across instances")
    void shouldMaintainConsistentBehavior() {
        S3Example instance1 = new S3Example();
        S3Example instance2 = new S3Example();
        assertDoesNotThrow(() -> S3Example.demonstrateStorageClasses());
        assertDoesNotThrow(() -> S3Example.demonstrateSecurity());
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertNotSame(instance1, instance2);
    }
}
