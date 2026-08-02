package academy.javaengineering.enterprise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reporting System Tests")
class ReportingSystemTest {

    @Test
    @DisplayName("Should demonstrate ETL without throwing")
    void shouldDemonstrateETL() {
        assertDoesNotThrow(() -> ReportingSystemExample.demonstrateETL());
        assertDoesNotThrow(() -> ReportingSystemExample.demonstrateETL());
    }

    @Test
    @DisplayName("Should demonstrate components without throwing")
    void shouldDemonstrateComponents() {
        assertDoesNotThrow(() -> ReportingSystemExample.demonstrateComponents());
        assertDoesNotThrow(() -> ReportingSystemExample.demonstrateComponents());
    }

    @Test
    @DisplayName("Should call all reporting system demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All reporting system demonstrations",
            () -> assertDoesNotThrow(() -> ReportingSystemExample.demonstrateETL()),
            () -> assertDoesNotThrow(() -> ReportingSystemExample.demonstrateComponents())
        );
    }

    @Test
    @DisplayName("Should handle repeated ETL demonstrations")
    void shouldHandleRepeatedETLCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> ReportingSystemExample.demonstrateETL());
        }
    }

    @Test
    @DisplayName("Should handle repeated component demonstrations")
    void shouldHandleRepeatedComponentCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> ReportingSystemExample.demonstrateComponents());
        }
    }

    @Test
    @DisplayName("Should demonstrate ETL before components in sequence")
    void shouldDemonstrateInCorrectOrder() {
        assertDoesNotThrow(() -> ReportingSystemExample.demonstrateETL());
        assertDoesNotThrow(() -> ReportingSystemExample.demonstrateComponents());
    }

    @Test
    @DisplayName("Should handle rapid alternating ETL and component calls")
    void shouldHandleAlternatingCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> ReportingSystemExample.demonstrateETL());
            assertDoesNotThrow(() -> ReportingSystemExample.demonstrateComponents());
        }
    }

    @Test
    @DisplayName("Should be callable multiple rounds without degradation")
    void shouldHandleMultipleRounds() {
        for (int round = 0; round < 3; round++) {
            assertDoesNotThrow(() -> ReportingSystemExample.demonstrateETL());
            assertDoesNotThrow(() -> ReportingSystemExample.demonstrateComponents());
        }
    }
}
