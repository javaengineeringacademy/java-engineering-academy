# Strategy Pattern

The Strategy pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable. It lets the algorithm vary independently from clients that use it.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Strategy](#basic-strategy)
3. [Sorting Strategies](#sorting-strategies)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Strategy?

Strategy defines a family of algorithms and makes them interchangeable.

```
Context ──▶ Strategy (algorithm)
              │
         StrategyA
         StrategyB
         StrategyC
```

### When to Use

- Multiple algorithms for same operation
- Algorithm selection at runtime
- Avoid conditional statements

---

## Basic Strategy

### Payment Processing

```java
// Strategy interface
public interface PaymentStrategy {
    boolean pay(double amount);
}

// Concrete strategies
public class CreditCardPayment implements PaymentStrategy {
    private final String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Credit card charged: $" + amount);
        return true;
    }
}

public class PayPalPayment implements PaymentStrategy {
    private final String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("PayPal charged: $" + amount);
        return true;
    }
}

public class CryptoPayment implements PaymentStrategy {
    @Override
    public boolean pay(double amount) {
        System.out.println("Crypto charged: $" + amount);
        return true;
    }
}

// Context
public class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public boolean checkout(double total) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("No payment strategy set");
        }
        return paymentStrategy.pay(total);
    }
}

// Usage
Cart cart = new ShoppingCart();
cart.setPaymentStrategy(new CreditCardPayment("1234-5678-9012-3456"));
cart.checkout(99.99);

cart.setPaymentStrategy(new PayPalPayment("user@example.com"));
cart.checkout(49.99);
```

---

## Sorting Strategies

### Comparator Strategy

```java
// Strategy interface
@FunctionalInterface
public interface SortStrategy<T> {
    void sort(List<T> list);
}

// Context
public class Sorter<T> {
    private SortStrategy<T> strategy;

    public void setStrategy(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void sort(List<T> list) {
        strategy.sort(list);
    }
}

// Usage with lambdas
Sorter<String> sorter = new Sorter<>();

sorter.setStrategy(list -> Collections.sort(list));           // Natural order
sorter.setStrategy(list -> list.sort(Comparator.reverseOrder())); // Reverse
sorter.setStrategy(list -> list.sort(Comparator.comparingInt(String::length))); // By length

List<String> names = new ArrayList<>(List.of("Charlie", "Alice", "Bob"));
sorter.sort(names);
```

---

## Best Practices

### Do

```java
// 1. Program to interface
public class Context {
    private final Strategy strategy;
    public Context(Strategy strategy) { this.strategy = strategy; }
}

// 2. Use lambdas for simple strategies
cart.setPaymentStrategy(amount -> processCrypto(amount));
```

### Don't

```java
// 1. Don't create strategies for one-off cases
// Use lambda or method reference instead

// 2. Don't let strategy access context internals
// Keep interface clean
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Strategy** | Family of interchangeable algorithms |
| **Context** | Uses strategy |
| **Runtime Selection** | Change algorithm at runtime |
| **vs Conditionals** | More flexible than if/else |
| **Lambdas** | Simple strategies as lambdas |
| **Use Cases** | Sorting, payment, validation |
