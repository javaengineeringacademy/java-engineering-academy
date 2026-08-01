# Introduction to Exception Handling

## Introduction

Exception handling is a critical mechanism in Java that allows programs to respond to runtime errors gracefully. Instead of crashing, well-designed programs catch exceptions and take appropriate recovery actions.

## Learning Objectives

- Understand what exceptions are and why they occur
- Differentiate between errors and exceptions
- Learn the exception class hierarchy
- Recognize the importance of exception handling

## Prerequisites

- Basic Java syntax and program structure
- Understanding of program execution flow

## Why This Matters

Without proper exception handling, programs crash unexpectedly, data can be lost, and users have poor experiences. Exception handling ensures your applications are robust and reliable.

## Syntax

```java
// Basic structure of exception handling
try {
    // Code that might throw an exception
} catch (ExceptionType e) {
    // Code to handle the exception
}
```

## Examples

```java
// Example 1: Unhandled exception
public class UnhandledExample {
    public static void main(String[] args) {
        int result = 10 / 0;  // ArithmeticException
        System.out.println(result);  // Never reached
    }
}

// Example 2: Basic exception handling
public class HandledExample {
    public static void main(String[] args) {
        try {
            int result = 10 / 0;
            System.out.println(result);
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero!");
        }
    }
}
```

## Exercises

1. Write a program that attempts to access an array element outside its bounds. Handle the exception.
2. Create a program that reads user input and handles invalid input gracefully.
3. What happens when you divide by zero without a try-catch block?

## Interview Questions

- What is the difference between an error and an exception?
- Name the top-level classes in the Java exception hierarchy.
- Why should you not catch generic `Exception` in production code?

## Common Pitfalls

- Ignoring exceptions with empty catch blocks
- Catching too broad an exception type
- Using exceptions for normal flow control

## Best Practices

- Catch specific exceptions rather than generic ones
- Always provide meaningful error messages
- Log exceptions for debugging purposes

## Real World Applications

- File I/O operations (FileNotFoundException)
- Network communication (IOException)
- Database operations (SQLException)
- User input validation (InputMismatchException)

## References

- [Oracle: Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/)
- [Java Exception Hierarchy](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html)

## Summary

In this topic, you learned what exceptions are, why they matter, and the basic structure of Java's exception handling mechanism. Practice with the exercises before moving to try-catch blocks.
