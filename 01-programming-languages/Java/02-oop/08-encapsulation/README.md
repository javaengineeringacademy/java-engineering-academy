# Encapsulation in Java

## Overview
Encapsulation is the bundling of data with methods that operate on that data, restricting direct access to some components.

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

## Common Mistakes
1. Making fields public instead of private
2. Creating getters for everything (anemic domain model)
3. Not validating in setters
4. Returning mutable objects directly from getters

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

## Alternatives

| Approach | Immutability | Boilerplate | Pattern Matching | Use When |
|----------|-------------|-------------|-----------------|----------|
| Encapsulation (getters/setters) | Manual | High | No | Mutable state, complex behavior |
| Records (Java 16+) | Yes | Low | Yes | Immutable data carriers |
| Sealed classes | N/A | Low | Yes | Restricting type hierarchies |
| Lombok @Data | No | Low | No | Reducing POJO boilerplate |
| Builder pattern | Optional | High | No | Complex object construction |

## Trade-offs

Encapsulation protects invariants because it:
- Adds boilerplate for simple data carriers (use Records instead)
- Returning mutable objects from getters leaks internal state (use defensive copies)
- Anemic domain models with only getters/setters lose behavior (put logic in domain objects)
- Over-encapsulation of value objects adds unnecessary complexity (use Records or public final fields)
- Testability requires package-private access or frameworks (use @VisibleForTesting)

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
1. What is encapsulation and why is it important?
2. What is the difference between a POJO, JavaBean, and a domain object?
3. When should you NOT use encapsulation?
4. What is the Hollywood Principle and how does it relate to encapsulation?
5. How do you handle immutable objects with encapsulation?

## Common Myths

### ❌ Myth 1: Private fields are always best
**Reality:** Records and DTOs often use public final fields. Context determines the right approach.

### ❌ Myth 2: Getters/setters are always needed
**Reality:** Immutable objects don't need setters. Records provide accessors automatically.

### ❌ Myth 3: Encapsulation means private everything
**Reality:** Package-private exists for a reason. Not everything needs to be private.
