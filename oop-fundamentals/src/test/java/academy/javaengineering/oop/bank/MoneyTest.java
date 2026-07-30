package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void testMoneyCreation() {
        Money money = new Money("100.50", "USD");
        assertEquals(new BigDecimal("100.50"), money.amount());
        assertEquals("USD", money.currency().getCurrencyCode());
    }

    @Test
    void testMoneyZero() {
        Money money = Money.zero("USD");
        assertEquals(BigDecimal.ZERO, money.amount());
        assertEquals("USD", money.currency().getCurrencyCode());
    }

    @Test
    void testMoneyAdd() {
        Money a = new Money("10.00", "USD");
        Money b = new Money("5.50", "USD");
        Money result = a.add(b);
        assertEquals(new Money("15.50", "USD"), result);
    }

    @Test
    void testMoneyAddDifferentCurrencyThrows() {
        Money a = new Money("10.00", "USD");
        Money b = new Money("5.00", "EUR");
        assertThrows(IllegalArgumentException.class, () -> a.add(b));
    }

    @Test
    void testMoneyIsZero() {
        assertTrue(Money.zero("USD").isZero());
        assertFalse(new Money("1.00", "USD").isZero());
    }

    @Test
    void testMoneyIsGreaterThanOrEqualTo() {
        assertTrue(new Money("10.00", "USD").isGreaterThanOrEqualTo(new Money("5.00", "USD")));
        assertTrue(new Money("10.00", "USD").isGreaterThanOrEqualTo(new Money("10.00", "USD")));
        assertFalse(new Money("5.00", "USD").isGreaterThanOrEqualTo(new Money("10.00", "USD")));
    }

    @Test
    void testMoneyMinus() {
        Money a = new Money("20.00", "USD");
        Money b = new Money("8.00", "USD");
        Money result = a.minus(b);
        assertEquals(new Money("12.00", "USD"), result);
    }

    @Test
    void testMoneyMultipliedBy() {
        Money a = new Money("10.00", "USD");
        BigDecimal rate = BigDecimal.valueOf(0.12);
        Money result = a.multipliedBy(rate);
        assertEquals(new Money("1.20", "USD"), result);
    }

    @Test
    void testMoneyEqualsAndHashCode() {
        Money a = new Money("10.00", "USD");
        Money b = new Money("10.00", "USD");
        Money c = new Money("10.00", "EUR");
        
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertEquals(a.hashCode(), b.hashCode());
    }
}