# Throws Declaration

## Introduction

The throws keyword is used in method declarations to indicate that the method might抛出 certain exceptions. It's part of Java's exception contract between callers and callees.

## Learning Objectives

- Understand when to use throws in method declarations
- Differentiate between checked and unchecked exceptions
- Declare multiple exceptions in a method signature
- Handle or declare exceptions properly

## Prerequisites

- Throw keyword
- Understanding of exception types
- Method declarations

## Why This Matters

The throws declaration informs callers about potential exceptions, allowing them to handle or propagate errors appropriately.

## Syntax

```java
// Single exception
public void method() throws ExceptionType {
    // Code that might throw
}

// Multiple exceptions
public void method() throws Type1, Type2, Type3 {
    // Code that might throw
}

// Calling a method that throws
try {
    method();
} catch (ExceptionType e) {
    // Handle
}
```

## Examples

```java
// Example 1: Throws declaration
public class ThrowsBasic {
    public static void readFile(String filename) throws FileNotFoundException {
        FileReader file = new FileReader(filename);
        // Process file
    }

    public static void main(String[] args) {
        try {
            readFile("data.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
}

// Example 2: Multiple exceptions
public class MultiThrows {
    public static void processData(String filename)
            throws FileNotFoundException, IOException {
        FileReader file = new FileReader(filename);
        BufferedReader reader = new BufferedReader(file);
        String line = reader.readLine();
        // Process data
    }
}

// Example 3: Exception chaining
public class ExceptionChaining {
    public static void process(int value) throws CustomException {
        try {
            if (value < 0) {
                throw new IllegalArgumentException("Negative value");
            }
            // Process value
        } catch (IllegalArgumentException e) {
            throw new CustomException("Processing failed", e);
        }
    }
}
```

## Exercises

1. Create a method that reads a file and declares the appropriate exceptions.
2. Write a method that performs division and declares ArithmeticException.
3. Create a method that chains exceptions and preserves the original cause.

## Interview Questions

- What is the difference between checked and unchecked exceptions?
- When should you use throws vs try-catch?
- Can you override a method and change its throws declaration?

## Common Pitfalls

- Declaring too many exceptions in throws
- Using throws to avoid handling exceptions
- Not considering exception chaining

## Best Practices

- Declare only exceptions that callers should handle
- Use exception chaining to preserve context
- Document exceptions in Javadoc

## Real World Applications

- API method contracts
- Library method declarations
- Resource cleanup methods
- Transaction management methods

## References

- [Oracle: Throws Declaration](https://docs.oracle.com/javase/tutorial/essential/exceptions/declaring.html)
- [Checked vs Unchecked Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/catchOrDeclare.html)

## Summary

In this topic, you learned about the throws keyword and how to declare exceptions in method signatures. Practice with the exercises before creating custom exceptions.
