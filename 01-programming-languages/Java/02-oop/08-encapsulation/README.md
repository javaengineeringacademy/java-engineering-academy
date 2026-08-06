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

## Interview Questions
1. What is encapsulation and why is it important?
2. What is the difference between a POJO, JavaBean, and a domain object?
3. When should you NOT use encapsulation?
4. What is the Hollywood Principle and how does it relate to encapsulation?
5. How do you handle immutable objects with encapsulation?
