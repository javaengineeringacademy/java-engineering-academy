# Dependency Injection

## What is DI?
Providing dependencies from outside rather than creating them internally.

## Types
```java
// Constructor Injection (Recommended)
public class OrderService {
    private final PaymentProcessor processor;

    public OrderService(PaymentProcessor processor) {
        this.processor = processor;
    }
}

// Setter Injection
public class OrderService {
    private PaymentProcessor processor;

    public void setPaymentProcessor(PaymentProcessor processor) {
        this.processor = processor;
    }
}

// Field Injection (Avoid - hard to test)
public class OrderService {
    @Autowired
    private PaymentProcessor processor;
}
```

## Benefits
- **Testability**: Easy to mock dependencies
- **Flexibility**: Swap implementations
- **Loose Coupling**: Classes don't create dependencies
- **Single Responsibility**: Creation delegated