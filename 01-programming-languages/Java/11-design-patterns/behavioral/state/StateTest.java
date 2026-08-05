package academy.javaengineering.patterns.behavioral.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order("TEST-001");
    }

    @Test
    void orderShouldStartInNewState() {
        assertTrue(order.getCurrentState() instanceof NewState);
        assertEquals("NEW", order.getCurrentState().getStateName());
    }

    @Test
    void processShouldTransitionToProcessedState() {
        order.process();
        assertTrue(order.getCurrentState() instanceof ProcessedState);
        assertEquals("PROCESSED", order.getCurrentState().getStateName());
    }

    @Test
    void processShouldTransitionToShippedState() {
        order.process();
        order.process();
        assertTrue(order.getCurrentState() instanceof ShippedState);
        assertEquals("SHIPPED", order.getCurrentState().getStateName());
    }

    @Test
    void orderShouldCompleteFullLifecycle() {
        order.process();
        order.process();
        order.process();
        assertTrue(order.getCurrentState() instanceof ShippedState);
    }

    @Test
    void orderShouldReturnCorrectId() {
        assertEquals("TEST-001", order.getOrderId());
    }
}
