# 03 - Try-Catch Exception Handling

> "The only way to avoid mistakes is to do nothing. That's the biggest mistake of all."
> — The JVM has opinions about errors. Let's learn how to listen.

---

## Table of Contents

1. [Why Exception Handling is Needed](#1-why-exception-handling-is-needed)
2. [try Block](#2-try-block)
3. [catch Block](#3-catch-block)
4. [Execution Flow](#4-execution-flow)
5. [Single Catch](#5-single-catch)
6. [Multiple Catch](#6-multiple-catch)
7. [Multi-catch (Java 7+)](#7-multi-catch-java-7)
8. [Nested try-catch](#8-nested-try-catch)
9. [Rethrowing Exceptions](#9-rethrowing-exceptions)
10. [Common Mistakes](#10-common-mistakes)
11. [Best Practices](#11-best-practices)
12. [Production Examples](#12-production-examples)
13. [Version History](#version-history)
14. [Summary](#summary)
15. [Key Takeaways](#key-takeaways)

---

## 1. Why Exception Handling is Needed

Java programs crash without exception handling. A single bad input, a missing file, or a null reference can terminate your entire application. Exception handling lets you catch problems at runtime and decide what to do — log them, recover gracefully, or fail with a clear message.

**Without exception handling:**

```java
public static void main(String[] args) {
    int result = 10 / 0;  // ArithmeticException — program crashes here
    System.out.println("This never prints");
}
```

Output:
```
Exception in thread "main" java.lang.ArithmeticException: / by zero
```

**With exception handling:**

```java
public static void main(String[] args) {
    try {
        int result = 10 / 0;
    } catch (ArithmeticException e) {
        System.out.println("Cannot divide by zero");
    }
    System.out.println("Program continues");
}
```

Output:
```
Cannot divide by zero
Program continues
```

The second version logs the problem, keeps running, and lets the caller know something went wrong. That's the difference between production software and a demo that crashes on bad input.

---

## 2. try Block

The `try` block wraps code that might throw an exception. Nothing fancy — you put risky code inside the curly braces.

```java
try {
    // Code that might throw an exception
    FileInputStream file = new FileInputStream("data.txt");
    String line = file.readLine();
    System.out.println(line);
} catch (IOException e) {
    // Handle the exception
}
```

**Rules:**
- A `try` block must be followed by at least one `catch` or `finally` block
- You cannot have a `try` block alone
- The `try` block is the entry point for exception handling

**What belongs in a try block:**
- File I/O operations
- Network requests
- Database queries
- Parsing user input
- Array/collection access with dynamic indices
- Division and arithmetic with unknown values

```java
// Only put code that can actually throw exceptions
try {
    int[] numbers = {1, 2, 3};
    System.out.println(numbers[5]);  // ArrayIndexOutOfBoundsException
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Index out of range");
}
```

---

## 3. catch Block

The `catch` block handles the exception thrown by the `try` block. You specify which exception type you want to catch.

```java
try {
    int[] arr = new int[5];
    arr[10] = 42;
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Index " + e.getMessage() + " is invalid");
    e.printStackTrace();
}
```

**The catch parameter:**
- The variable `e` holds the exception object
- You can call methods on it: `getMessage()`, `printStackTrace()`, `getLocalizedMessage()`
- The type determines what exceptions this block catches

**Multi-variable catch (Java 7+):**

```java
// Old style — Java 6 and earlier
try {
    // risky code
} catch (IOException e) {
    handleIOException(e);
} catch (SQLException e) {
    handleSQLException(e);
}

// Java 7+ — catch multiple exception types in one block
try {
    // risky code
} catch (IOException | SQLException e) {
    handleGeneralIO(e);
}
```

---

## 4. Execution Flow

Understanding execution flow is key. Here's how the JVM processes try-catch:

```
┌─────────────────────────────────────────────────────────┐
│                    Main Thread                          │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  try {                                                  │
│    ├── Statement 1 ──executes──► Statement 2            │
│    │                                                     │
│    ├── Statement 3 ──throws!──► Exception object        │
│    │                                    │               │
│    │                                    ▼               │
│    │                          catch (ExceptionType)     │
│    │                                    │               │
│    └── Statement 4 ──SKIPPED           ▼               │
│                                      Handler code       │
│                                        │               │
│                                        ▼               │
│                                   Statement after       │
│                                   try-catch block       │
│                                        │               │
│                                        ▼               │
│                                      END                │
└─────────────────────────────────────────────────────────┘
```

**Key rules:**
1. If no exception: all statements in `try` execute, then execution continues after the catch blocks
2. If exception matches a catch: the remaining try statements are skipped, that catch block runs, then execution continues after the entire try-catch
3. If exception doesn't match any catch: the method throws the exception up to the caller
4. Catch blocks are checked in order — the first matching catch runs

**Example:**

```java
try {
    System.out.println("A");
    int result = 10 / 0;  // ArithmeticException here
    System.out.println("B");  // SKIPPED
} catch (ArithmeticException e) {
    System.out.println("C");  // This runs
}
System.out.println("D");  // This runs
```

Output:
```
A
C
D
```

---

## 5. Single Catch

One catch block handles one exception type. This is the simplest form.

```java
try {
    String text = null;
    text.length();  // NullPointerException
} catch (NullPointerException e) {
    System.out.println("Text was null: " + e.getMessage());
}
```

**When to use single catch:**
- You know exactly what can go wrong
- You handle one specific failure case
- The error handling is the same for all scenarios

```java
try {
    BufferedReader reader = new BufferedReader(new FileReader("config.properties"));
    String value = reader.readLine();
    System.out.println("Config: " + value);
    reader.close();
} catch (IOException e) {
    System.out.println("Could not read config file");
    // Use default config instead
}
```

**Single catch limitations:**
- Can't handle different exception types differently
- If the exception isn't the type you caught, it propagates up
- One block can become a dumping ground for multiple error types

---

## 6. Multiple Catch

Multiple catch blocks let you handle different exception types differently.

```java
try {
    String input = "abc";
    int number = Integer.parseInt(input);
    int[] arr = new int[5];
    arr[number] = 100;
} catch (NumberFormatException e) {
    System.out.println("Not a valid number: " + e.getMessage());
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Index out of range: " + e.getMessage());
} catch (Exception e) {
    System.out.println("Something else went wrong");
}
```

**Order matters:**

```java
// WRONG ORDER — compile error
try {
    // risky code
} catch (Exception e) {          // catches everything
    System.out.println("general");
} catch (FileNotFoundException e) { // unreachable — never executes
    System.out.println("file not found");
}

// CORRECT ORDER — most specific first
try {
    // risky code
} catch (FileNotFoundException e) {
    System.out.println("file not found");
} catch (Exception e) {
    System.out.println("general error");
}
```

**Single catch vs Multiple catch:**

| Aspect | Single Catch | Multiple Catch |
|--------|-------------|----------------|
| Exception types | One type | Multiple types |
| Handling | Same handler for all | Different handlers per type |
| Specificity | Low | High |
| Code length | Short | Longer |
| Readability | Simple | More structured |

**Example — different handling per type:**

```java
try {
    processData(userInput);
} catch (NumberFormatException e) {
    logger.warn("Bad input format: " + e.getMessage());
    promptUserForCorrectInput();
} catch (IOException e) {
    logger.error("I/O failure: " + e.getMessage());
    retryOperation();
} catch (SecurityException e) {
    logger.fatal("Access denied: " + e.getMessage());
    alertAdmin();
}
```

---

## 7. Multi-catch (Java 7+)

Multi-catch handles multiple exception types with one handler. Use `|` to separate exception types.

```java
// Old style (Java 6)
try {
    String data = readFile("data.txt");
    process(data);
} catch (FileNotFoundException e) {
    handleMissing(e);
} catch (IOException e) {
    handleIOFailure(e);
}

// Java 7+ — same handling for both
try {
    String data = readFile("data.txt");
    process(data);
} catch (FileNotFoundException | IOException e) {
    handleMissingOrIO(e);
}
```

**Multi-catch comparison:**

```
Multiple Catch (Java 6+)              Multi-catch (Java 7+)
┌─────────────────────────┐          ┌─────────────────────────┐
│ try {                   │          │ try {                   │
│   // risky code         │          │   // risky code         │
│ } catch (IOException e) │          │ } catch (IOException    │
│                         │          │        | SecurityEx) {  │
│ } catch (SecurityEx e)  │          │                         │
│                         │          │   // same handler       │
│ } catch (SQLException e)│          │ } catch (SQLException   │
│   // one more...        │          │        | RuntimeException)│
│ }                       │          │   // different handler  │
│                         │          │ }                       │
│ Total blocks: 3         │          │ Total blocks: 2         │
└─────────────────────────┘          └─────────────────────────┘
```

**Rules for multi-catch:**
- Exception types cannot be related by inheritance (one can't extend another)
- The catch parameter is implicitly `final` (can't reassign it)
- You can mix multi-catch with single-catch

```java
// COMPILE ERROR — IOException extends Exception
catch (IOException | Exception e) { }

// COMPILE ERROR — IOException and FileAlreadyExistsException
// FileAlreadyExistsException extends IOException
catch (IOException | FileAlreadyExistsException e) { }

// COMPILE ERROR — cannot reassign e
catch (IOException | SQLException e) {
    e = new IOException("new");  // error: e is implicitly final
}
```

**When to use multi-catch:**
- Multiple exceptions need the same recovery strategy
- You want to reduce catch block duplication
- The exception types are unrelated (no inheritance relationship)

```java
// Good use: same recovery for all connection issues
try {
    connectToService(endpoint);
} catch (ConnectTimeoutException | SocketException | UnknownHostException e) {
    logger.warn("Connection failed, will retry: " + e.getMessage());
    retryWithBackoff();
}
```

---

[Continue to Part 2 →](README-Part2.md)
