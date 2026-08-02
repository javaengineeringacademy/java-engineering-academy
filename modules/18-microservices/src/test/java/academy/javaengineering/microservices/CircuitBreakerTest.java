package academy.javaengineering.microservices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircuitBreakerTest {

    @Test
    void testInitialState() {
        CircuitBreakerExample cb = new CircuitBreakerExample(3, 2, 1000);
        assertEquals(CircuitBreakerExample.State.CLOSED, cb.getState());
    }

    @Test
    void testSuccessfulCall() {
        CircuitBreakerExample cb = new CircuitBreakerExample(3, 2, 1000);
        String result = cb.execute(() -> "Success", () -> "Fallback");
        assertEquals("Success", result);
        assertEquals(CircuitBreakerExample.State.CLOSED, cb.getState());
    }

    @Test
    void testFallbackOnFailure() {
        CircuitBreakerExample cb = new CircuitBreakerExample(3, 2, 1000);
        String result = cb.execute(() -> {
            throw new RuntimeException("Failure");
        }, () -> "Fallback");
        assertEquals("Fallback", result);
    }

    @Test
    void testCircuitOpens() {
        CircuitBreakerExample cb = new CircuitBreakerExample(3, 2, 1000);
        for (int i = 0; i < 3; i++) {
            cb.execute(() -> {
                throw new RuntimeException("Failure");
            }, () -> "Fallback");
        }
        assertEquals(CircuitBreakerExample.State.OPEN, cb.getState());
    }

    @Test
    void testCircuitHalfOpen() throws InterruptedException {
        CircuitBreakerExample cb = new CircuitBreakerExample(3, 2, 100);
        for (int i = 0; i < 3; i++) {
            cb.execute(() -> {
                throw new RuntimeException("Failure");
            }, () -> "Fallback");
        }
        assertEquals(CircuitBreakerExample.State.OPEN, cb.getState());
        Thread.sleep(150);
        cb.execute(() -> "Success", () -> "Fallback");
        assertEquals(CircuitBreakerExample.State.HALF_OPEN, cb.getState());
    }
}
