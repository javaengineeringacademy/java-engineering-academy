package academy.javaengineering.testing.mockito.advanced.practices;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Exercise 3: Custom Answer
 *
 * Tasks:
 * 1. Use doAnswer() to capture and process arguments
 * 2. Use thenAnswer() for dynamic return values
 * 3. Implement answer that transforms input
 */
@ExtendWith(MockitoExtension.class)
class Exercise3CustomAnswer {

    interface PriceCalculator {
        double calculate(double basePrice, String discountCode);
    }

    @Mock
    private PriceCalculator priceCalculator;

    @Test
    void shouldApplyDiscountBasedOnCode() {
        // Arrange: stub calculate with answer
        // If code is "VIP", apply 20% discount
        // If code is "STUDENT", apply 10% discount
        // Otherwise, no discount

        // Act & Assert
    }
}
