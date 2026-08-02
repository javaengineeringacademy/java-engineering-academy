package academy.javaengineering.history;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Java Version History.
 */
class ExampleTest {

    @Test
    void shouldDemonstrateConcepts() {
        assertDoesNotThrow(Example::demonstrate);
    }

    @Test
    void shouldCreateInstance() {
        Example example = new Example();
        assertNotNull(example);
    }
}
