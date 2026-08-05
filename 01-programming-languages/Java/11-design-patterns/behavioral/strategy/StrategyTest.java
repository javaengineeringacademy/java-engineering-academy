package academy.javaengineering.patterns.behavioral.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategyTest {

    @Test
    void bubbleSortShouldSortCorrectly() {
        SortStrategy sort = new BubbleSort();
        int[] input = {5, 3, 8, 1, 2};
        int[] expected = {1, 2, 3, 5, 8};
        assertArrayEquals(expected, sort.sort(input));
    }

    @Test
    void quickSortShouldSortCorrectly() {
        SortStrategy sort = new QuickSort();
        int[] input = {5, 3, 8, 1, 2};
        int[] expected = {1, 2, 3, 5, 8};
        assertArrayEquals(expected, sort.sort(input));
    }

    @Test
    void creditCardPaymentShouldProcess() {
        PaymentStrategy payment = new CreditCardPayment("1234567890123456", "John");
        assertTrue(payment.pay(100.00));
    }

    @Test
    void payPalPaymentShouldProcess() {
        PaymentStrategy payment = new PayPalPayment("test@email.com");
        assertTrue(payment.pay(50.00));
    }

    @Test
    void contextShouldSwitchStrategies() {
        Context context = new Context(new BubbleSort());
        String result1 = context.executeStrategy("test");
        assertTrue(result1.contains("BubbleSort"));

        context.setStrategy(new QuickSort());
        String result2 = context.executeStrategy("test");
        assertTrue(result2.contains("QuickSort"));
    }
}
