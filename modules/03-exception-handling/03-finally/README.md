# Finally Block

## Introduction

The finally block contains code that always executes, regardless of whether an exception was thrown or caught. It's essential for cleanup operations like closing resources.

## Learning Objectives

- Understand the purpose of the finally block
- Know when finally executes and when it doesn't
- Use finally for resource cleanup
- Understand try-with-resources as an alternative

## Prerequisites

- Try-Catch blocks
- Basic understanding of resources (files, connections)

## Why This Matters

The finally block ensures critical cleanup code runs, preventing resource leaks like unclosed files or database connections.

## Syntax

```java
// try-catch-finally
try {
    // Risky code
} catch (Exception e) {
    // Handle exception
} finally {
    // Always executes
}

// try-finally (no catch)
try {
    // Code
} finally {
    // Cleanup
}
```

## Examples

```java
// Example 1: Finally always executes
public class FinallyBasic {
    public static void main(String[] args) {
        try {
            System.out.println("In try");
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("In catch");
        } finally {
            System.out.println("In finally");
        }
        // Output: In try, In catch, In finally
    }
}

// Example 2: Finally with return
public class FinallyReturn {
    public static int divide(int a, int b) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            return -1;
        } finally {
            System.out.println("Finally executed");
        }
    }
}

// Example 3: Resource cleanup
public class ResourceCleanup {
    public static void main(String[] args) {
        FileReader reader = null;
        try {
            reader = new FileReader("file.txt");
            // Read file
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error closing file");
                }
            }
        }
    }
}
```

## Exercises

1. Write a method that demonstrates finally executing after a return statement.
2. Create a program that opens a file, processes it, and ensures the file is closed in finally.
3. What happens when finally contains a return statement?

## Interview Questions

- When does the finally block NOT execute?
- What is the difference between finally and try-with-resources?
- Can you have try-catch-finally without a catch block?

## Common Pitfalls

- Forgetting to close resources in finally
- Throwing exceptions from finally blocks
- Not handling exceptions in finally's cleanup code

## Best Practices

- Use try-with-resources for AutoCloseable resources
- Keep finally blocks simple and focused on cleanup
- Always close resources in finally when not using try-with-resources

## Real World Applications

- Database connection cleanup
- File stream closing
- Network socket closing
- Lock releasing in concurrent programming

## References

- [Oracle: Finally Block](https://docs.oracle.com/javase/tutorial/essential/exceptions/finally.html)
- [Try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html)

## Summary

In this topic, you learned about the finally block and its role in ensuring cleanup code always executes. Practice with the exercises before learning about the throw keyword.
