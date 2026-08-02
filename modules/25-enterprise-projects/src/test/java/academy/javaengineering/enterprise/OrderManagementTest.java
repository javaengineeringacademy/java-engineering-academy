package academy.javaengineering.enterprise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order Management Tests")
class OrderManagementTest {

    @Test
    @DisplayName("Should demonstrate saga pattern without throwing")
    void shouldDemonstrateSagaPattern() {
        assertDoesNotThrow(() -> OrderManagementExample.demonstrateSagaPattern());
        assertDoesNotThrow(() -> OrderManagementExample.demonstrateSagaPattern());
    }

    @Test
    @DisplayName("Should demonstrate order states without throwing")
    void shouldDemonstrateOrderStates() {
        assertDoesNotThrow(() -> OrderManagementExample.demonstrateOrderStates());
        assertDoesNotThrow(() -> OrderManagementExample.demonstrateOrderStates());
    }

    @Test
    @DisplayName("Should call all order management demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All order management demonstrations",
            () -> assertDoesNotThrow(() -> OrderManagementExample.demonstrateSagaPattern()),
            () -> assertDoesNotThrow(() -> OrderManagementExample.demonstrateOrderStates())
        );
    }

    @Test
    @DisplayName("Should handle repeated saga pattern demonstrations")
    void shouldHandleRepeatedSagaCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> OrderManagementExample.demonstrateSagaPattern());
        }
    }

    @Test
    @DisplayName("Should handle repeated order states demonstrations")
    void shouldHandleRepeatedOrderStatesCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> OrderManagementExample.demonstrateOrderStates());
        }
    }

    @Test
    @DisplayName("Should demonstrate saga pattern before order states in sequence")
    void shouldDemonstrateInCorrectOrder() {
        assertDoesNotThrow(() -> OrderManagementExample.demonstrateSagaPattern());
        assertDoesNotThrow(() -> OrderManagementExample.demonstrateOrderStates());
    }

    @Test
    @DisplayName("Should handle rapid alternating saga and order states calls")
    void shouldHandleAlternatingCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> OrderManagementExample.demonstrateSagaPattern());
            assertDoesNotThrow(() -> OrderManagementExample.demonstrateOrderStates());
        }
    }

    @Test
    @DisplayName("Should be callable multiple rounds without degradation")
    void shouldHandleMultipleRounds() {
        for (int round = 0; round < 3; round++) {
            assertDoesNotThrow(() -> OrderManagementExample.demonstrateSagaPattern());
            assertDoesNotThrow(() -> OrderManagementExample.demonstrateOrderStates());
        }
    }
}
