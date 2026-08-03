package academy.javaengineering.patterns.strategy;

import academy.javaengineering.patterns.strategy.StrategyExample.ShoppingCart;
import academy.javaengineering.patterns.strategy.StrategyExample.PaymentStrategy;
import academy.javaengineering.patterns.strategy.StrategyExample.CreditCardPayment;
import academy.javaengineering.patterns.strategy.StrategyExample.PayPalPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrategyPatternTest {

    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
    }

    @Test
    @DisplayName("Should process payment with credit card strategy")
    void shouldProcessCreditCardPayment() {
        List<Double> amounts = new ArrayList<>();
        PaymentStrategy creditCard = amount -> amounts.add(amount);
        cart.setPaymentStrategy(creditCard);
        cart.checkout(100.0);
        assertEquals(1, amounts.size());
        assertEquals(100.0, amounts.get(0));
    }

    @Test
    @DisplayName("Should process payment with PayPal strategy")
    void shouldProcessPayPalPayment() {
        List<Double> amounts = new ArrayList<>();
        PaymentStrategy paypal = amount -> amounts.add(amount);
        cart.setPaymentStrategy(paypal);
        cart.checkout(50.0);
        assertEquals(1, amounts.size());
        assertEquals(50.0, amounts.get(0));
    }

    @Test
    @DisplayName("Should switch strategy at runtime")
    void shouldSwitchStrategy() {
        List<String> methods = new ArrayList<>();
        PaymentStrategy creditCard = amount -> methods.add("credit-card");
        PaymentStrategy paypal = amount -> methods.add("paypal");

        cart.setPaymentStrategy(creditCard);
        cart.checkout(10.0);
        cart.setPaymentStrategy(paypal);
        cart.checkout(20.0);

        assertEquals(List.of("credit-card", "paypal"), methods);
    }

    @Test
    @DisplayName("Should pass correct amount to strategy")
    void shouldPassCorrectAmount() {
        List<Double> captured = new ArrayList<>();
        PaymentStrategy spy = captured::add;
        cart.setPaymentStrategy(spy);

        cart.checkout(99.99);
        assertEquals(99.99, captured.get(0), 0.001);
    }

    @Test
    @DisplayName("Should handle zero amount")
    void shouldHandleZeroAmount() {
        List<Double> captured = new ArrayList<>();
        PaymentStrategy spy = captured::add;
        cart.setPaymentStrategy(spy);

        cart.checkout(0.0);
        assertEquals(0.0, captured.get(0));
    }

    @Test
    @DisplayName("Should throw when no strategy is set")
    void shouldThrowWhenNoStrategySet() {
        assertThrows(NullPointerException.class,
                () -> cart.checkout(10.0),
                "Checkout without strategy should throw NullPointerException");
    }

    @Test
    @DisplayName("Both concrete strategies should implement PaymentStrategy")
    void concreteStrategiesShouldImplementInterface() {
        assertInstanceOf(PaymentStrategy.class, new CreditCardPayment());
        assertInstanceOf(PaymentStrategy.class, new PayPalPayment());
    }

    @Test
    @DisplayName("Should work with lambda-based custom strategy")
    void shouldWorkWithCustomStrategy() {
        List<String> log = new ArrayList<>();
        PaymentStrategy custom = amount -> log.add("Custom: " + amount);
        cart.setPaymentStrategy(custom);
        cart.checkout(42.5);
        assertEquals("Custom: 42.5", log.get(0));
    }
}
