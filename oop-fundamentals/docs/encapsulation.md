# Encapsulation

## Objective
Master the principle of bundling data with methods that operate on that data, and restricting direct access.

## Theory

### What is Encapsulation?
**Encapsulation** is the bundling of data (fields) and methods that operate on that data into a single unit (class), with restricted access to internal state.

### Access Modifiers

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `private` | ✓ | ✗ | ✗ | ✗ |
| (package) | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

## Implementation Pattern

```java
public class BankAccount {
    private BigDecimal balance;  // Private field

    public BankAccount(BigDecimal initialBalance) {
        this.balance = Objects.requireNonNull(initialBalance);
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance >= 0");
        }
    }

    // Controlled access
    public BigDecimal getBalance() {
        return balance;
    }

    // Controlled modification
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount > 0");
        }
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount > 0");
        }
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance = balance.subtract(amount);
    }
}
```

## Immutable Objects

```java
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(amount.add(other.amount), currency);  // Returns new instance
    }
}
```

## Benefits

| Benefit | Description |
|---------|-------------|
| **Control** | Validate before setting |
| **Flexibility** | Change internals without breaking clients |
| **Security** | Protect invariants |
| **Maintainability** | Single point of change |

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Public fields | Use private + getters/setters |
| Setters without validation | Validate in setters |
| Returning mutable internal state | Return defensive copies |
| Mutable objects as fields | Use immutable types or defensive copies |

## Defensive Copying

```java
public class Person {
    private final Date birthDate;

    public Person(Date birthDate) {
        this.birthDate = new Date(birthDate.getTime());  // Defensive copy
    }

    public Date getBirthDate() {
        return new Date(birthDate.getTime());  // Return copy
    }
}
```

## Interview Questions

1. **Why encapsulate?** Control, validation, flexibility
2. **Mutable vs Immutable?** Immutable = thread-safe, predictable
3. **When to use defensive copy?** Mutable fields returned or passed in