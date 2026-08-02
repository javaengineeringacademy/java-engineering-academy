package academy.javaengineering.enterprise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reporting System Tests")
class ReportingSystemTest {
    @Test @DisplayName("Should demonstrate ETL")
    void shouldDemonstrateETL() {
        assertDoesNotThrow(() -> ReportingSystemExample.demonstrateETL());
    }
    @Test @DisplayName("Should demonstrate components")
    void shouldDemonstrateComponents() {
        assertDoesNotThrow(() -> ReportingSystemExample.demonstrateComponents());
    }
}
