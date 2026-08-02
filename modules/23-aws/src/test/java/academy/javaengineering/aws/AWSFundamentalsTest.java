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
    @DisplayName("Should demonstrate global infrastructure without throwing")
    void shouldDemonstrateGlobalInfrastructure() {
        assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateGlobalInfrastructure());
        assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateGlobalInfrastructure());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate IAM without throwing")
    void shouldDemonstrateIAM() {
        assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateIAM());
        assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateIAM());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate core services without throwing")
    void shouldDemonstrateCoreServices() {
        assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateCoreServices());
        assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateCoreServices());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should create AWSFundamentalsExample instance successfully")
    void shouldCreateInstance() {
        AWSFundamentalsExample instance = new AWSFundamentalsExample();
        assertNotNull(instance);
        assertInstanceOf(AWSFundamentalsExample.class, instance);
    }

    @Test
    @DisplayName("Should call all AWS fundamentals demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All AWS fundamentals demonstrations",
            () -> assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateGlobalInfrastructure()),
            () -> assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateIAM()),
            () -> assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateCoreServices())
        );
    }

    @Test
    @DisplayName("Should handle repeated global infrastructure demonstrations")
    void shouldHandleRepeatedGlobalInfrastructureCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateGlobalInfrastructure());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should handle repeated IAM demonstrations")
    void shouldHandleRepeatedIAMCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateIAM());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should maintain statelessness across demonstrations")
    void shouldMaintainStatelessness() {
        AWSFundamentalsExample first = new AWSFundamentalsExample();
        AWSFundamentalsExample second = new AWSFundamentalsExample();
        assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateGlobalInfrastructure());
        assertDoesNotThrow(() -> AWSFundamentalsExample.demonstrateCoreServices());
        assertNotNull(first);
        assertNotNull(second);
        assertNotSame(first, second);
    }
}
