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

## Interview Questions
1. What is encapsulation and why is it important?
2. What is the difference between a POJO, JavaBean, and a domain object?
3. When should you NOT use encapsulation?
4. What is the Hollywood Principle and how does it relate to encapsulation?
5. How do you handle immutable objects with encapsulation?
