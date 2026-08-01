# Custom Exceptions

## Introduction

Custom exceptions allow you to create application-specific exception types that convey meaningful error information unique to your domain.

## Learning Objectives

- Create custom checked exceptions
- Create custom unchecked exceptions
- Add custom fields and methods to exceptions
- Use exception hierarchies for your application

## Prerequisites

- Throw and throws keywords
- Understanding of exception hierarchy
- Class inheritance

## Why This Matters

Custom exceptions make your code more readable and maintainable by providing domain-specific error types that clearly communicate what went wrong.

## Syntax

```java
// Custom checked exception
public class MyCheckedException extends Exception {
    public MyCheckedException(String message) {
        super(message);
    }
}

// Custom unchecked exception
public class MyUncheckedException extends RuntimeException {
    public MyUncheckedException(String message) {
        super(message);
    }
}

// Custom exception with fields
public class OrderException extends Exception {
    private String orderId;

    public OrderException(String message, String orderId) {
        super(message);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
```

## Examples

```java
// Example 1: Simple custom exception
public class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) {
        super(message);
    }
}

public class AgeValidator {
    public static void validate(int age) throws InvalidAgeException {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException("Invalid age: " + age);
        }
    }

    public static void main(String[] args) {
        try {
            validate(200);
        } catch (InvalidAgeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

// Example 2: Exception with additional data
public class InsufficientFundsException extends Exception {
    private double currentBalance;
    private double withdrawAmount;

    public InsufficientFundsException(double balance, double amount) {
        super("Cannot withdraw " + amount + " from balance " + balance);
        this.currentBalance = balance;
        this.withdrawAmount = amount;
    }

    public double getDeficit() {
        return withdrawAmount - currentBalance;
    }
}

// Example 3: Exception hierarchy
public class ServiceException extends Exception {
    public ServiceException(String message) { super(message); }
    public ServiceException(String message, Throwable cause) { super(message, cause); }
}

public class DatabaseException extends ServiceException {
    public DatabaseException(String message) { super(message); }
}

public class ConnectionException extends DatabaseException {
    public ConnectionException(String message) { super(message); }
}
```

## Exercises

1. Create a custom exception for an e-commerce application that tracks invalid product quantities.
2. Build an exception hierarchy for a banking application (InsufficientFundsException, InvalidAccountException).
3. Create a custom exception that includes the timestamp when the error occurred.

## Interview Questions

- When should you create a custom exception vs using existing ones?
- What's the difference between extending Exception vs RuntimeException?
- How do you preserve the original exception when wrapping?

## Common Pitfalls

- Creating too many custom exceptions
- Not including useful information in exceptions
- Forgetting to extend the correct base class

## Best Practices

- Use existing exceptions when they fit your needs
- Include relevant context in custom exceptions
- Create an exception hierarchy for your application
- Document when to use each custom exception

## Real World Applications

- Domain-specific errors (OrderException, PaymentException)
- Validation errors (InvalidEmailException, WeakPasswordException)
- Integration errors (APIException, TimeoutException)
- Business logic errors (InsufficientInventoryException)

## References

- [Creating Custom Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/creating.html)
- [Exception Hierarchy Design](https://www.oracle.com/technical-resources/articles/java/exception-handling.html)

## Summary

In this topic, you learned how to create custom exceptions to make your error handling more meaningful and domain-specific. Practice with the exercises before learning best practices.
