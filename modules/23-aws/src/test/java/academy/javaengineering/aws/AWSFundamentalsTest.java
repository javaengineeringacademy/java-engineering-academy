package academy.javaengineering.aws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AWS Fundamentals Tests")
class AWSFundamentalsTest {

    private AWSFundamentalsExample example;

    @BeforeEach
    void setUp() {
        example = new AWSFundamentalsExample();
    }

    @Test
    @DisplayName("Should demonstrate global infrastructure")
    void shouldDemonstrateGlobalInfrastructure() {
        assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateGlobalInfrastructure());
    }

    @Test
    @DisplayName("Should demonstrate IAM")
    void shouldDemonstrateIAM() {
        assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateIAM());
    }

    @Test
    @DisplayName("Should demonstrate core services")
    void shouldDemonstrateCoreServices() {
        assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateCoreServices());
    }
}
