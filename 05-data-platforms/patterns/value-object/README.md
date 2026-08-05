# Value Object Pattern

## Overview

A Value Object is an immutable object that represents a descriptive aspect of the domain with no conceptual identity. Value objects are defined by their attributes rather than a unique identifier. Two value objects with the same attributes are considered equal.

Value objects enforce invariants through their constructors and do not change state after creation. They simplify domain models by replacing primitive obsession with meaningful, type-safe abstractions.

## When to Use

- Modeling concepts defined by their attributes (money, addresses, dates)
- Replacing primitive types to add validation and behavior
- Ensuring immutability for thread safety and predictable behavior
- Comparing objects by value rather than identity
- Encapsulating validation rules in a single place

## Implementation

### TypeScript

```typescript
class Money {
  constructor(
    public readonly amount: number,
    public readonly currency: string
  ) {
    if (amount < 0) throw new Error('Amount cannot be negative');
    if (!currency) throw new Error('Currency is required');
  }

  equals(other: Money): boolean {
    return this.amount === other.amount && this.currency === other.currency;
  }

  add(other: Money): Money {
    if (this.currency !== other.currency) {
      throw new Error('Cannot add different currencies');
    }
    return new Money(this.amount + other.amount, this.currency);
  }

  multiply(factor: number): Money {
    return new Money(this.amount * factor, this.currency);
  }

  toString(): string {
    return `${this.currency} ${this.amount.toFixed(2)}`;
  }
}

class Email {
  constructor(public readonly value: string) {
    if (!this.isValid(value)) {
      throw new Error(`Invalid email: ${value}`);
    }
  }

  private isValid(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }

  equals(other: Email): boolean {
    return this.value.toLowerCase() === other.value.toLowerCase();
  }

  getDomain(): string {
    return this.value.split('@')[1];
  }
}
```

### Java

```java
public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money multiply(BigDecimal factor) {
        return new Money(this.amount.multiply(factor), this.currency);
    }
}

public record Email(String value) {
    public Email {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid email: " + value);
        }
    }

    private boolean isValid(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public String getDomain() {
        return value.split("@")[1];
    }
}
```

### Python

```python
from dataclasses import dataclass
import re

@dataclass(frozen=True)
class Money:
    amount: float
    currency: str

    def __post_init__(self):
        if self.amount < 0:
            raise ValueError('Amount cannot be negative')

    def add(self, other: 'Money') -> 'Money':
        if self.currency != other.currency:
            raise ValueError('Cannot add different currencies')
        return Money(self.amount + other.amount, self.currency)

    def multiply(self, factor: float) -> 'Money':
        return Money(self.amount * factor, self.currency)

@dataclass(frozen=True)
class Email:
    value: str

    def __post_init__(self):
        if not re.match(r'^[^\s@]+@[^\s@]+\.[^\s@]+$', self.value):
            raise ValueError(f'Invalid email: {self.value}')

    def get_domain(self) -> str:
        return self.value.split('@')[1]
```

### C\#

```csharp
public record Money {
    public decimal Amount { get; }
    public string Currency { get; }

    public Money(decimal amount, string currency) {
        if (amount < 0) throw new ArgumentException("Amount cannot be negative");
        Amount = amount;
        Currency = currency;
    }

    public Money Add(Money other) {
        if (Currency != other.Currency)
            throw new ArgumentException("Cannot add different currencies");
        return new Money(Amount + other.Amount, Currency);
    }
}

public record Email {
    public string Value { get; }

    public Email(string value) {
        if (!IsValid(value)) throw new ArgumentException($"Invalid email: {value}");
        Value = value;
    }

    public string Domain => Value.Split('@')[1];

    private static bool IsValid(string email) =>
        Regex.IsMatch(email, @"^[^@\s]+@[^@\s]+\.[^@\s]+$");
}
```

## Best Practices

- Make value objects immutable using records, final classes, or frozen dataclasses
- Implement equality based on all attributes, not identity
- Keep value objects small and focused on a single concept
- Validate invariants in constructors or factory methods
- Use value objects to replace primitive obsession
- Consider value objects for API response fields where identity is irrelevant

## Interview Questions

1. What is the difference between a Value Object and an Entity?
2. How do you handle value object equality in different languages?
3. Can value objects contain other value objects?
4. How do value objects relate to the concept of primitive obsession?
5. When should you convert a value object back to a primitive for persistence?

## References

- Evans, Eric. *Domain-Driven Design*, Value Objects chapter
- Vernon, Vaughn. *Implementing Domain-Driven Design*
- Martin Fowler. *Value Object*
- Eric Evans. *Blue Book - Value Objects*
