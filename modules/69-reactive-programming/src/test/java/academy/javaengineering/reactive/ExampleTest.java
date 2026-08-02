package academy.javaengineering.reactive;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Reactive Programming.
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
