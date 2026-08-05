package academy.javaengineering.patterns.behavioral.strategy;

import java.util.Arrays;

/**
 * Real-world example demonstrating the Strategy pattern.
 * Shows sorting and payment strategy implementations.
 */
public class StrategyExample {

    public static void main(String[] args) {
        int[] data = {64, 34, 25, 12, 22, 11, 90};

        System.out.println("=== Sorting Strategies ===");
        System.out.println("Original: " + Arrays.toString(data));

        SortStrategy bubbleSort = new BubbleSort();
        int[] bubbleResult = bubbleSort.sort(data);
        System.out.println("BubbleSort: " + Arrays.toString(bubbleResult));

        SortStrategy quickSort = new QuickSort();
        int[] quickResult = quickSort.sort(data);
        System.out.println("QuickSort: " + Arrays.toString(quickResult));

        System.out.println("\n=== Payment Strategies ===");
        Context creditCardContext = new Context(new CreditCardPayment("1234567890123456", "John Doe"));
        creditCardContext.executePayment(99.99);

        Context payPalContext = new Context(new PayPalPayment("john@example.com"));
        payPalContext.executePayment(49.99);

        System.out.println("\n=== Dynamic Strategy Switching ===");
        Context dynamicContext = new Context(new BubbleSort());
        System.out.println(dynamicContext.executeStrategy("data"));
        dynamicContext.setStrategy(new QuickSort());
        System.out.println(dynamicContext.executeStrategy("data"));
    }
}
