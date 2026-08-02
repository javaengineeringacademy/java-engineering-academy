package academy.javaengineering.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PreparedStatementTest {

    @Test
    void shouldCreateBatchProcessor() {
        PreparedStatementExample.BatchProcessor processor =
                new PreparedStatementExample.BatchProcessor("jdbc:h2:mem:test");
        assertNotNull(processor);
    }
}
