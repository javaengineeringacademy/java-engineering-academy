package academy.javaengineering.cleancode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@DisplayName("Clean Code Tests")
class CleanCodeTest {

    @Test
    @DisplayName("CleanFunctions should validate order ID")
    void testValidateOrderId() {
        var cleaner = new CleanFunctions();
        assertThrows(IllegalArgumentException.class, 
            () -> cleaner.processOrderGood(null));
    }

    @Test
    @DisplayName("CleanComments should calculate total correctly")
    void testCalculateTotal() {
        var calculator = new CleanComments();
        List<Double> items = List.of(10.0, 20.0, 30.0);
        double result = calculator.calculateTotal(items, 10.0, 5.0);
        assertEquals(71.0, result, 0.01);
    }

    @Test
    @DisplayName("CleanComments should throw for empty items")
    void testCalculateTotalEmpty() {
        var calculator = new CleanComments();
        assertThrows(IllegalArgumentException.class, 
            () -> calculator.calculateTotal(List.of(), 10.0, 5.0));
    }
}
