# Encapsulation

## What is Encapsulation?
Bundling data (fields) and methods that operate on that data, restricting direct access to internal state.

## Access Modifiers
| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `private` | ✓ | ✗ | ✗ | ✗ |
| default (package) | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

## Encapsulation Pattern
```java
public class BankAccount {
    private BigDecimal balance;  // Hidden

    public BigDecimal getBalance() {  // Controlled read
        return balance;
    }

    public void deposit(BigDecimal amount) {  // Controlled write
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance = balance.add(amount);
    }
}
```

## Benefits
- **Control**: Validate before setting
- **Flexibility**: Change implementation without breaking clients
- **Security**: Protect invariants
- **Maintainability**: Single point of change

## Immutable Objects
```java
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount >= 0");
        }
    }
    // No setters - immutable!
    public Money add(Money other) { ... }
}
```