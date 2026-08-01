# Throw Keyword

## Introduction

The throw keyword is used to explicitly抛出 an exception. This allows you to create and throw exceptions when your code detects an error condition.

## Learning Objectives

- Use the throw keyword to抛出 exceptions
- Understand the difference between throw and throws
- Create exception instances for throwing
- Throw exceptions from methods

## Prerequisites

- Try-Catch blocks
- Finally block
- Understanding of exception types

## Why This Matters

Throwing exceptions allows you to signal error conditions in your code, making your methods communicate failures clearly to callers.

## Syntax

```java
// Throwing an exception
throw new ExceptionType("Error message");

// Throwing in a method
public void validate(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative");
    }
}
```

## Examples

```java
// Example 1: Basic throw usage
public class ThrowBasic {
    public static void main(String[] args) {
        try {
            throw new ArithmeticException("Custom error");
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}

// Example 2: Validating input
public class AgeValidator {
    public static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        if (age > 150) {
            throw new IllegalArgumentException("Age seems unrealistic: " + age);
        }
        System.out.println("Valid age: " + age);
    }

    public static void main(String[] args) {
        try {
            validateAge(25);
            validateAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }
}

// Example 3: Throw with custom logic
public class BankAccount {
    private double balance;

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        balance -= amount;
    }
}
```

## Exercises

1. Create a method that validates a password and throws exceptions for invalid passwords.
2. Write a divide method that throws ArithmeticException for division by zero with a custom message.
3. Create a temperature converter that throws exceptions for impossible temperatures.

## Interview Questions

- What is the difference between `throw` and `throws`?
- Can you throw multiple exceptions in a single method?
- What happens if you throw an exception without catching it?

## Common Pitfalls

- Using throw without a message (makes debugging harder)
- Throwing checked exceptions without declaring them
- Throwing exceptions in constructors without proper handling

## Best Practices

- Always provide meaningful exception messages
- Throw the most specific exception type possible
- Validate parameters early and throw exceptions immediately

## Real World Applications

- Input validation in forms
- Business rule enforcement
- API parameter validation
- Data integrity checks

## References

- [Oracle: Throw Statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/branch.html)
- [Creating Exception Classes](https://docs.oracle.com/javase/tutorial/essential/exceptions/creating.html)

## Summary

In this topic, you learned how to use the throw keyword to explicitly抛出 exceptions in your code. Practice with the exercises before learning about the throws declaration.
