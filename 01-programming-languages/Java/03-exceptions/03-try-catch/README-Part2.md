# 03 - Try-Catch Exception Handling (Part 2)

[← Part 1](README-Part1.md)

> "Good judgment comes from experience, and experience comes from bad judgment."

---

## Table of Contents

1. [Multi-catch (Java 7+) — Deep Dive](#1-multi-catch-java-7--deep-dive)
2. [Nested try-catch](#2-nested-try-catch)
3. [Rethrowing Exceptions](#3-rethrowing-exceptions)
4. [Common Mistakes](#4-common-mistakes)
5. [Best Practices](#5-best-practices)
6. [Production Examples](#6-production-examples)
7. [Version History](#7-version-history)
8. [Summary](#8-summary)
9. [Key Takeaways](#9-key-takeaways)

---

## 1. Multi-catch (Java 7+) — Deep Dive

Part 1 introduced multi-catch syntax. This section covers the rules, bytecode behavior, and edge cases that matter in practice.

### Syntax

```java
try {
    // risky code
} catch (IOException | SQLException | TimeoutException e) {
    // one handler for all three types
    log("Error: " + e.getMessage());
}
```

### Rules

**Rule 1: No inheritance relationships**

The types in a multi-catch cannot be related by inheritance. The compiler rejects this because one type would be a subtype of the other, making one branch unreachable.

```java
// COMPILE ERROR — IOException is a superclass of FileNotFoundException
catch (IOException | FileNotFoundException e) { }

// COMPILE ERROR — Exception is a superclass of IOException
catch (Exception | IOException e) { }
```

**Rule 2: Implicitly final parameter**

The catch parameter in a multi-catch is treated as `final`. You cannot reassign it inside the block.

```java
catch (IOException | SQLException e) {
    e = new IOException("nope");  // COMPILE ERROR
}
```

This restriction exists because the JVM uses a synthetic variable to represent the caught exception. Reassignment would break the contract.

**Rule 3: Mixing multi-catch with single-catch**

You can combine them freely. The compiler checks catch blocks in order.

```java
try {
    // risky code
} catch (FileNotFoundException e) {
    // handle missing file specifically
} catch (IOException | SQLException e) {
    // handle other I/O and SQL errors together
} catch (Exception e) {
    // catch everything else
}
```

**Rule 4: Exception variable access**

Inside the multi-catch block, the variable `e` is typed as the common supertype of all listed exceptions.

```java
catch (IOException | SQLException e) {
    // e's compile-time type: Exception (common supertype)
    // At runtime, e is the actual thrown exception
    System.out.println(e.getClass().getName());
}
```

### Effectively Final

The parameter in multi-catch is effectively final by design. This interacts with lambda expressions and anonymous classes.

```java
// This works — e is final, can be used in lambdas
catch (IOException | SQLException e) {
    Runnable r = () -> System.out.println(e.getMessage());  // OK
}
```

### Bytecode Behavior

The Java compiler generates different bytecode for multi-catch versus multiple catch blocks.

```
Multiple Catch Blocks              Multi-catch Block
+--------------------------+      +--------------------------+
| catch IOException:       |      | catch IOException:       |
|   goto handler_1         |      |   goto handler_combined  |
| catch SQLException:      |      | catch SQLException:      |
|   goto handler_2         |      |   goto handler_combined  |
| handler_1:               |      | handler_combined:        |
|   // handle IOException  |      |   // handle both         |
| handler_2:               |      |                          |
|   // handle SQLException |      |                          |
+--------------------------+      +--------------------------+
  More bytecode                     Less bytecode
  Separate handlers                 Single handler
```

Multi-catch produces slightly smaller bytecode because the JVM only needs one handler entry instead of separate entries per exception type.

### When Multi-catch is Not Appropriate

```java
// DON'T use multi-catch if you need different handling
catch (FileNotFoundException e) {
    // prompt user to select a different file
} catch (IOException e) {
    // log and retry the operation
}

// DO use multi-catch only when the handling is identical
catch (FileNotFoundException | IOException e) {
    // same response for both
    logAndRetry(e);
}
```

---

## 2. Nested try-catch

A try-catch block can be placed inside another try or catch block. The inner block handles exceptions locally; unhandled exceptions propagate outward.

### Inner Exception Flow

```java
try {                                   // outer
    String data = readFromFile("data.txt");
    try {                               // inner
        int value = Integer.parseInt(data);
        int[] arr = new int[10];
        arr[value] = 42;
    } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("Index out of range");
    } catch (NumberFormatException e) {
        System.out.println("Not a valid number");
    }
} catch (IOException e) {
    System.out.println("Could not read file");
}
```

**Flow diagram:**

```
+------------------- Outer try -------------------+
|                                                  |
|  readFromFile("data.txt")                        |
|       |                                          |
|       +-- IOException --> outer catch handles    |
|       |                                          |
|       v                                          |
|  +-- Inner try -----------------------+          |
|  |                                     |          |
|  |  Integer.parseInt(data)             |          |
|  |       |                             |          |
|  |       +-- NumberFormatException     |          |
|  |       |        +-- inner catch #2   |          |
|  |       |                             |          |
|  |       v                             |          |
|  |  arr[value] = 42                    |          |
|  |       |                             |          |
|  |       +-- ArrayIndexOutOfBounds     |          |
|  |       |        +-- inner catch #1   |          |
|  |       |                             |          |
|  |       v                             |          |
|  |  (success - inner try completes)    |          |
|  +-------------------------------------+          |
|                                                  |
+--------------------------------------------------+
```

### When Unhandled Exceptions Propagate

If the inner catch does not handle the exception, it propagates to the outer catch.

```java
try {                                   // outer
    try {                               // inner
        throw new RuntimeException("inner");
    } catch (NullPointerException e) {  // does not match RuntimeException
        System.out.println("inner catch");
    }
    // RuntimeException propagates here, then to outer
} catch (RuntimeException e) {
    System.out.println("outer catch: " + e.getMessage());
}
```

Output:
```
outer catch: inner
```

### Nested try without Outer catch

```java
try {
    try {
        int result = 10 / 0;
    } catch (NullPointerException e) {
        // does not match ArithmeticException
    }
} catch (ArithmeticException e) {
    System.out.println("Caught by outer: " + e.getMessage());
}
```

### Try-with-Resources Inside try-catch

This is the most common nesting pattern in real code.

```java
try {
    BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
    try {
        String line = reader.readLine();
        System.out.println(line);
    } catch (IOException e) {
        System.out.println("Error reading line");
    }
    reader.close();
} catch (FileNotFoundException e) {
    System.out.println("File not found");
}
```

Modern Java replaces this with try-with-resources:

```java
try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
    String line = reader.readLine();
    System.out.println(line);
} catch (IOException e) {
    System.out.println("Error: " + e.getMessage());
}
```

### Deeply Nested Exceptions

Deep nesting hurts readability. Refactor into separate methods instead.

```java
// BAD — hard to follow
try {
    try {
        try {
            // do something
        } catch (IOException e) {
            // handle
        }
    } catch (SQLException e) {
        // handle
    }
} catch (Exception e) {
    // handle
}

// BETTER — extract methods
try {
    String data = readFile("input.txt");
    processDatabase(data);
} catch (IOException e) {
    handleIOError(e);
} catch (SQLException e) {
    handleDBError(e);
}
```

---

## 3. Rethrowing Exceptions

Sometimes you catch an exception, do something with it, and then throw it again — or throw a different one.

### Rethrow the Original

```java
try {
    readFile("data.txt");
} catch (IOException e) {
    logger.error("Failed to read file", e);
    throw e;  // rethrow the same exception
}
```

