# Try-Catch

## Introduction

The try-catch block is Java's primary mechanism for handling exceptions. Code that might throw an exception goes in the try block, and the handling code goes in the catch block.

## Learning Objectives

- Write proper try-catch blocks
- Handle multiple exception types
- Use multi-catch blocks (Java 7+)
- Understand exception flow control

## Prerequisites

- Introduction to Exception Handling
- Basic understanding of Java types

## Why This Matters

Try-catch blocks allow your program to continue running even when errors occur, preventing crashes and providing better user experience.

## Syntax

```java
// Basic try-catch
try {
    // Risky code
} catch (ExceptionType e) {
    // Handling code
}

// Multi-catch (Java 7+)
try {
    // Risky code
} catch (Type1 | Type2 e) {
    // Handling code for both types
}

// Multiple catch blocks
try {
    // Risky code
} catch (Type1 e) {
    // Handle Type1
} catch (Type2 e) {
    // Handle Type2
}
```

## Examples

```java
// Example 1: Multiple exception handling
public class MultipleCatch {
    public static void main(String[] args) {
        try {
            int[] numbers = {1, 2, 3};
            System.out.println(numbers[5]);
            int result = 10 / 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array index out of bounds: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Arithmetic error: " + e.getMessage());
        }
    }
}

// Example 2: Multi-catch
public class MultiCatch {
    public static void main(String[] args) {
        try {
            String str = null;
            str.length();
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

// Example 3: Exception flow
public class ExceptionFlow {
    public static void main(String[] args) {
        try {
            System.out.println("Before exception");
            int result = 10 / 0;
            System.out.println("After exception");  // Skipped
        } catch (ArithmeticException e) {
            System.out.println("In catch block");
        }
        System.out.println("After try-catch");  // Executed
    }
}
```

## Exercises

1. Write a program that reads an integer from user input and handles InputMismatchException.
2. Create a method that divides two numbers and handles all possible exceptions.
3. What is the output when multiple exceptions could be thrown but only one occurs?

## Interview Questions

- What is the difference between `catch (Exception e)` and `catch (IOException e)`?
- Can you have code between try and catch blocks?
- What happens if no exception is thrown in the try block?

## Common Pitfalls

- Catching exceptions in the wrong order (more specific after more general)
- Placing unreachable catch blocks
- Not using multi-catch when appropriate

## Best Practices

- Order catch blocks from most specific to most general
- Use multi-catch for handling multiple exception types the same way
- Keep try blocks as small as possible

## Real World Applications

- Parsing user input with validation
- Reading files that might not exist
- Making network calls that might fail
- Database operations

## References

- [Oracle: Catching and Handling Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html)
- [Multi-catch Documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/language/catch-multiple.html)

## Summary

In this topic, you learned how to use try-catch blocks to handle exceptions, including multiple catch blocks and the multi-catch syntax. Practice with the exercises before moving to the finally block.
