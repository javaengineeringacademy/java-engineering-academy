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
    @DisplayName("Should demonstrate storage classes")
    void shouldDemonstrateStorageClasses() {
        assertDoesNotThrow(() -> S3Example.demonstrateStorageClasses());
    }

    @Test
    @DisplayName("Should demonstrate operations")
    void shouldDemonstrateOperations() {
        assertDoesNotThrow(() -> S3Example.demonstrateOperations());
    }

    @Test
    @DisplayName("Should demonstrate security")
    void shouldDemonstrateSecurity() {
        assertDoesNotThrow(() -> S3Example.demonstrateSecurity());
    }
}
