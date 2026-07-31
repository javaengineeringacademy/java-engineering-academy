package academy.javaengineering.oop.encapsulation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for immutable Money class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class MoneyTest {

    @Test
    void shouldCreateMoneyWithValidAmount() {
        Money money = new Money(100.50, "USD");
        assertEquals(100.50, money.getAmount(), 0.001);
        assertEquals("USD", money.getCurrency());
    }

    @Test
    void shouldThrowOnNegativeAmount() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Money(-50, "USD"));
    }

    @Test
    void shouldAddSameCurrency() {
        Money m1 = new Money(100, "USD");
        Money m2 = new Money(50, "USD");
        Money result = m1.add(m2);
        
        assertEquals(150.0, result.getAmount(), 0.001);
        assertEquals("USD", result.getCurrency());
    }

    @Test
    void shouldThrowOnDifferentCurrencyAdd() {
        Money usd = new Money(100, "USD");
        Money eur = new Money(50, "EUR");
        
        assertThrows(IllegalArgumentException.class, () -> usd.add(eur));
    }

    @Test
    void shouldSubtractSameCurrency() {
        Money m1 = new Money(100, "USD");
        Money m2 = new Money(30, "USD");
        Money result = m1.subtract(m2);
        
        assertEquals(70.0, result.getAmount(), 0.001);
    }

    @Test
    void shouldMultiply() {
        Money money = new Money(100, "USD");
        Money result = money.multiply(2.5);
        
        assertEquals(250.0, result.getAmount(), 0.001);
    }

    @Test
    void shouldBeEqualForSameValues() {
        Money m1 = new Money(100, "USD");
        Money m2 = new Money(100, "USD");
        
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentValues() {
        Money m1 = new Money(100, "USD");
        Money m2 = new Money(200, "USD");
        
        assertNotEquals(m1, m2);
    }

    @Test
    void shouldBeImmutable() {
        Money money = new Money(100, "USD");
        money.add(new Money(50, "USD")); // Result discarded
        
        assertEquals(100.0, money.getAmount(), 0.001); // Unchanged
    }
}