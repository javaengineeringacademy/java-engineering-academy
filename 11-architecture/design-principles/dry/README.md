# DRY - Don't Repeat Yourself

## Overview

DRY states that every piece of knowledge must have a single, unambiguous, authoritative representation within a system.

## Violations

### Duplicated Code
```java
// BAD - Same logic in multiple places
public class OrderService {
    public void processOrder(Order order) {
        if (order.getTotal() > 100) {
            order.setDiscount(order.getTotal() * 0.1);
        }
    }
}

public class DiscountService {
    public void calculateDiscount(Order order) {
        if (order.getTotal() > 100) {
            order.setDiscount(order.getTotal() * 0.1);
        }
    }
}

// GOOD - Single source of truth
public class DiscountCalculator {
    public static final BigDecimal THRESHOLD = new BigDecimal("100");
    public static final double DISCOUNT_RATE = 0.1;
    
    public BigDecimal calculateDiscount(BigDecimal total) {
        if (total.compareTo(THRESHOLD) > 0) {
            return total.multiply(BigDecimal.valueOf(DISCOUNT_RATE));
        }
        return BigDecimal.ZERO;
    }
}
```

### Duplicated Configuration
```yaml
# BAD - Same config in multiple places
server:
  port: 8080

# Multiple files with same config

# GOOD - Externalized configuration
# application.yml
server:
  port: ${SERVER_PORT:8080}
```

### Duplicated Database Queries
```java
// BAD - Same query in multiple repositories
public class UserRepository {
    public List<User> findActiveUsers() {
        return jdbcTemplate.query("SELECT * FROM users WHERE active = true", ...);
    }
}

public class ReportRepository {
    public List<User> findActiveUsers() {
        return jdbcTemplate.query("SELECT * FROM users WHERE active = true", ...);
    }
}

// GOOD - Shared query
@Repository
public class UserQueries {
    public static final String ACTIVE_USERS = "SELECT * FROM users WHERE active = true";
}

public class UserRepository {
    public List<User> findActiveUsers() {
        return jdbcTemplate.query(UserQueries.ACTIVE_USERS, ...);
    }
}
```

## Patterns to Avoid Duplication

### Template Method
```java
public abstract class DataProcessor {
    public final void process() {
        validate();
        transform();
        save();
        notify();
    }
    
    protected abstract void validate();
    protected abstract void transform();
    protected abstract void save();
    protected void notify() { /* default */ }
}
```

### Strategy Pattern
```java
public interface PricingStrategy {
    BigDecimal calculatePrice(Order order);
}

@Component
public class StandardPricing implements PricingStrategy {
    @Override
    public BigDecimal calculatePrice(Order order) {
        return order.getBasePrice();
    }
}

@Component
public class PremiumPricing implements PricingStrategy {
    @Override
    public BigDecimal calculatePrice(Order order) {
        return order.getBasePrice().multiply(BigDecimal.valueOf(0.9));
    }
}
```

## Best Practices

1. Extract common logic into shared components
2. Use configuration for variable values
3. Create shared libraries for reusable code
4. Use abstract classes for common behavior
5. Apply the Single Responsibility Principle
6. Regularly refactor duplicated code
7. Document decisions about intentional duplication
8. Test refactored code thoroughly
