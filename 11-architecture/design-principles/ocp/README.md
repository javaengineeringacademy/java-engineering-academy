# OCP - Open/Closed Principle

## Overview

Software entities should be open for extension but closed for modification. You should be able to add new functionality without changing existing code.

## Violations

### Modification Required
```java
// BAD - Must modify for new types
public class DiscountCalculator {
    public BigDecimal calculateDiscount(Order order) {
        if (order.getType() == OrderType.STANDARD) {
            return order.getTotal().multiply(BigDecimal.valueOf(0.05));
        } else if (order.getType() == OrderType.PREMIUM) {
            return order.getTotal().multiply(BigDecimal.valueOf(0.10));
        } else if (order.getType() == OrderType.VIP) {
            return order.getTotal().multiply(BigDecimal.valueOf(0.15));
        }
        return BigDecimal.ZERO;
    }
}
```

### Open for Extension
```java
// GOOD - New types without modifying existing code
public interface DiscountStrategy {
    BigDecimal calculateDiscount(BigDecimal total);
}

public class StandardDiscount implements DiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(BigDecimal total) {
        return total.multiply(BigDecimal.valueOf(0.05));
    }
}

public class PremiumDiscount implements DiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(BigDecimal total) {
        return total.multiply(BigDecimal.valueOf(0.10));
    }
}

public class VipDiscount implements DiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(BigDecimal total) {
        return total.multiply(BigDecimal.valueOf(0.15));
    }
}

public class DiscountCalculator {
    private final Map<OrderType, DiscountStrategy> strategies;
    
    public DiscountCalculator(List<DiscountStrategy> strategies) {
        this.strategies = strategies.stream()
            .collect(Collectors.toMap(DiscountStrategy::getType, Function.identity()));
    }
    
    public BigDecimal calculateDiscount(Order order) {
        DiscountStrategy strategy = strategies.get(order.getType());
        return strategy.calculateDiscount(order.getTotal());
    }
}
```

## Patterns Supporting OCP

### Strategy Pattern
```java
public interface PaymentStrategy {
    void pay(BigDecimal amount);
}

@Component
public class CreditCardPayment implements PaymentStrategy { /* ... */ }

@Component
public class PayPalPayment implements PaymentStrategy { /* ... */ }

@Component
public class BankTransferPayment implements PaymentStrategy { /* ... */ }
```

### Template Method
```java
public abstract class ReportGenerator {
    public final Report generate(Data data) {
        validate(data);
        Report report = createReport(data);
        format(report);
        return report;
    }
    
    protected abstract Report createReport(Data data);
    protected abstract void format(Report report);
}

public class PDFReportGenerator extends ReportGenerator {
    @Override
    protected Report createReport(Data data) {
        return new PDFReport(data);
    }
    
    @Override
    protected void format(Report report) {
        // PDF-specific formatting
    }
}
```

### Decorator Pattern
```java
public interface Notifier {
    void send(String message);
}

public class EmailNotifier implements Notifier {
    @Override
    public void send(String message) {
        // Send email
    }
}

public class SMSDecorator implements Notifier {
    private final Notifier wrapped;
    
    public SMSDecorator(Notifier wrapped) {
        this.wrapped = wrapped;
    }
    
    @Override
    public void send(String message) {
        wrapped.send(message);
        sendSMS(message);
    }
}
```

## Best Practices

1. Identify points of variation
2. Define stable abstractions
3. Use dependency injection
4. Apply design patterns
5. Keep abstractions minimal
6. Test extensibility
7. Document extension points
8. Review for modification opportunities
