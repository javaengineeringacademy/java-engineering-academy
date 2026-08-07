# Encapsulation in Java

## Overview
Encapsulation is the bundling of data with methods that operate on that data, restricting direct access to some components.

## History

| Version | Change |
|---------|--------|
| JDK 1.0 | Encapsulation supported via private/protected/public access modifiers — Java enforced visibility control to protect object state from uncontrolled access |
| JDK 16 | Records added — encapsulation without boilerplate for immutable data carriers |

## Learning Objectives

By the end of this topic you will be able to:

• Explain why encapsulation matters for maintainability
• Design classes with proper access modifiers
• Choose between getters/setters and immutable objects
• Identify when encapsulation hurts (DTOs, records)
• Apply defensive copying to protect internal state

## Internal Working

Encapsulation works by restricting direct access to class fields and forcing interaction through methods.

Without encapsulation:
```java
public class BankAccount {
    public double balance;  // Anyone can modify
}

account.balance = -1000;  // No validation, no control
```

With encapsulation:
```java
public class BankAccount {
    private double balance;  // Only this class can access

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
    }
}

account.deposit(-1000);  // Rejected by validation logic
```

The key insight: encapsulation lets you change internal representation without breaking external code.

## When to Use
- To protect object invariants and ensure data integrity
- To reduce coupling between components
- To provide controlled access through validated getters/setters

## Code Example
See `src/main/java/academy/javaengineering/oop/encapsulation/BankAccount.java`

```java
BankAccount account = new BankAccount("ACC-001", "Alice", BigDecimal.valueOf(1000));
account.deposit(BigDecimal.valueOf(500));  // Validated
account.withdraw(BigDecimal.valueOf(200)); // Checked
```

## When NOT to Use Encapsulation

Encapsulation isn't always the right choice:

**DTOs and data carriers:**
```java
// Records are better here — no getters/setters boilerplate
public record Point(int x, int y) {}
```

**Performance-critical code:**
```java
// Direct field access is faster than method calls in hot paths
public class Vec3 {
    public double x, y, z;  // Public for performance
}
```

**Framework requirements:**
```java
// JPA entities need setters (or at least package-private)
@Entity
public class User {
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }  // Required by JPA
}
```

## Alternatives

| Approach | Boilerplate | Immutability | Validation | Use When |
|----------|-------------|--------------|------------|----------|
| Public fields | None | No | No | DTOs, performance |
| Getters/Setters | High | No | Yes | Traditional Java |
| Records | Low | Yes | Limited | Data carriers |
| Builder pattern | High | Yes | Yes | Complex construction |
| Lombok | Low | Configurable | Yes | Reduce boilerplate |

## Trade-offs

Encapsulation gives you control but costs:
- Boilerplate: Getters/setters add lines of code
- Performance: Method calls have overhead vs direct field access
- Complexity: More code to maintain

Use encapsulation when:
- You have invariants to protect (balance must be positive)
- You might change internal representation
- You need validation on set
- You're building domain objects

Skip encapsulation when:
- It's a simple data carrier (use records)
- Performance is critical (inner loops)
- The object has no invariants

## Best Practices

1. **Prefer immutable objects** — If the field never changes after construction, make it final and don't provide a setter.

2. **Use defensive copying** — When returning mutable objects, return copies:
```java
public List<String> getNames() {
    return new ArrayList<>(names);  // Defensive copy
}
```

3. **Validate in setters** — Never trust external input:
```java
public void setAge(int age) {
    if (age < 0 || age > 150) throw new IllegalArgumentException("Invalid age");
    this.age = age;
}
```

4. **Consider records first** — For data carriers, records give you encapsulation with less code.

5. **Package-private for testing** — Sometimes package-private access is better than private (for unit testing internal methods).

## Common Mistakes

### Mistake 1: Exposing mutable internal state
```java
// BAD — external code can modify your internal list
public List<String> getNames() { return names; }

// GOOD — return a copy
public List<String> getNames() { return new ArrayList<>(names); }
```

### Mistake 2: Getters/setters for everything
```java
// BAD — anemic domain model
public class User {
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// GOOD — behavior-rich domain model
public class User {
    private String name;
    public void rename(String newName) {
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException();
        this.name = newName;
    }
}
```

### Mistake 3: Forgetting that records are immutable
```java
// Records give you encapsulation automatically
public record User(String name, int age) {}

// But you can't do this:
User u = new User("Alice", 30);
// u.name = "Bob";  // Compile error — records are immutable
```

## Engineering Decision Framework

### ✅ Use Encapsulation when:
- Protecting object invariants and business rules
- Designing public APIs that hide internal complexity
- Enforcing validation on field assignments
- Reducing coupling between components
- Creating domain objects with behavior

### ❌ Avoid Encapsulation when:
- Simple data carriers (POJOs, DTOs) are sufficient
- Performance-critical code with tight memory layout
- Records can provide immutability with less boilerplate
- Testing requires direct field access (use package-private)

### Better Alternatives

| Alternative | When to use |
|-------------|-------------|
| Records | Immutable data carriers (Java 16+) |
| Sealed classes | Restricting class hierarchies |
| Lombok @Data | Reducing boilerplate for simple POJOs |
| Builder pattern | Complex object construction |

### Production Examples
- Bank account with balance validation rules
- User entity with password hashing
- Order object with state machine transitions
- Configuration objects with immutable defaults
- API request/response DTOs

### Common Production Mistakes
- Exposing internal collection references (defensive copying needed)
- Creating getters for all fields without business logic
- Not using final fields where immutability is desired
- Mixing data and behavior in anemic domain models
- Over-encapsulating simple value objects

## Production Incidents

### Incident 1: Exposing Mutable Internal State

**Problem:** A user management service allowed unauthorized balance modifications. Users could set their own account balance to any value.
**Cause:** A `BankAccount` class had a `getBalance()` method that returned a direct reference to the internal `BigDecimal` field. Since `BigDecimal` is immutable, this wasn't the issue. However, a `getTransactions()` method returned the internal `List<Transaction>` directly. Callers could add/remove transactions, bypassing validation logic.
**Impact:** 3 users exploited this to modify transaction history. Audit revealed unauthorized balance adjustments totaling $15,000.
**Detection:** Anomaly detection system flagged unusual transaction patterns.
**Solution:** Return unmodifiable views: `Collections.unmodifiableList(transactions)`. Add defensive copying in getters for mutable objects.
**Prevention:** Never return references to internal mutable objects. Use `Collections.unmodifiable*()` or defensive copies. Add static analysis rules for mutable getter returns.

### Incident 2: Anemic Domain Model Causing Business Logic Bugs

**Problem:** An e-commerce order system applied discounts incorrectly, overcharging customers by 10-15% on discounted items.
**Cause:** The `Order` class was an anemic domain model with only getters/setters. Discount calculation logic was scattered across 5 different service classes. Each service calculated discounts slightly differently due to rounding and tax handling inconsistencies.
**Impact:** 2,000+ customers overcharged. Refund processing cost $50K in operational overhead. Customer trust damaged.
**Solution:** Move discount calculation into the `Order` class as a business method: `order.applyDiscount(DiscountRule rule)`. Centralize all business logic in domain objects.
**Prevention:** Follow "Tell, Don't Ask" principle. Domain objects should encapsulate business behavior, not just hold data. Use code review to flag anemic domain models.

## Production Checklist

### ✅ Before using Encapsulation in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands thread safety

### Level 3: Deep Knowledge
- Knows internal implementation
- Understands edge cases

### Level 4: Expert
- Knows resize/rehash algorithms
- Can optimize for specific use cases

### Level 5: Master
- Can debug in production
- Can explain trade-offs to team
- Can design custom implementations

## Interview Questions

1. **What is encapsulation?**
   Hiding internal state and forcing interaction through methods.

2. **When would you NOT use encapsulation?**
   DTOs, performance-critical code, records.

3. **What is defensive copying?**
   Returning copies of mutable objects to prevent external modification.

4. **What's the difference between a record and a class with getters?**
   Records are immutable, have auto-generated equals/hashCode/toString, and use compact constructors.

5. **How does encapsulation relate to the Open/Closed Principle?**
   Encapsulated code can change internally without breaking external callers.

## Common Myths

### ❌ Myth 1: Private fields are always best
**Reality:** Records and DTOs often use public final fields. Context determines the right approach.

### ❌ Myth 2: Getters/setters are always needed
**Reality:** Immutable objects don't need setters. Records provide accessors automatically.

### ❌ Myth 3: Encapsulation means private everything
**Reality:** Package-private exists for a reason. Not everything needs to be private.
