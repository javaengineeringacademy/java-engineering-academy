package academy.javaengineering.systemdesign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Database Design Tests")
class DatabaseDesignTest {
    @Test @DisplayName("Should demonstrate scaling techniques")
    void shouldDemonstrateTechniques() {
        assertDoesNotThrow(() -> DatabaseDesignExample.demonstrateTechniques());
    }
}
