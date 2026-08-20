package academy.javaengineering.testing.mockito.advanced.solutions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Exercise3CustomAnswerSolution {

    interface PriceCalculator {
        double calculate(double basePrice, String discountCode);
    }

    @Mock
    private PriceCalculator priceCalculator;

    @Test
    void shouldApplyDiscountBasedOnCode() {
        when(priceCalculator.calculate(anyDouble(), anyString())).thenAnswer(invocation -> {
            double basePrice = invocation.getArgument(0);
            String code = invocation.getArgument(1);
            return switch (code) {
                case "VIP" -> basePrice * 0.8;
                case "STUDENT" -> basePrice * 0.9;
                default -> basePrice;
            };
        });

        assertEquals(80.0, priceCalculator.calculate(100, "VIP"), 0.01);
        assertEquals(90.0, priceCalculator.calculate(100, "STUDENT"), 0.01);
        assertEquals(100.0, priceCalculator.calculate(100, "NONE"), 0.01);
    }
}
