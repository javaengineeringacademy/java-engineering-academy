package academy.javaengineering.reactive;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reactive Programming Tests")
class ReactiveTest {

    @Test
    @DisplayName("Should have reactive principles")
    void testReactivePrinciples() {
        var principles = ReactiveConcepts.getReactivePrinciples();
        assertEquals(4, principles.size());
        assertTrue(principles.stream().anyMatch(p -> p.contains("Responsive")));
    }

    @Test
    @DisplayName("Should have reactive operators")
    void testOperators() {
        var operators = ReactiveConcepts.getOperators();
        assertFalse(operators.isEmpty());
        assertTrue(operators.stream().anyMatch(o -> o.name().equals("map")));
    }
}
