# 04 - Finally Block

## Scope

This topic covers Java's `finally` block — the guaranteed execution construct used for cleanup in exception handling. You will learn what `finally` is, why it exists, how execution order works across all control flow paths, and the dangerous interactions between `finally` and `return` statements. The material progresses from basic syntax to advanced pitfalls, production patterns, and a thorough comparison with try-with-resources.

## Why It Exists

Java's exception handling separates the "happy path" (`try`) from error recovery (`catch`). But some cleanup must happen **regardless** of whether an exception occurred — closing files, releasing locks, rolling back transactions, logging completion. Without `finally`, developers had to duplicate cleanup code in every `catch` block and after the `try`:

```java
// Fragile — cleanup must be duplicated everywhere
try {
    FileInputStream fis = new FileInputStream("data.txt");
    // ... work ...
    fis.close();
} catch (IOException e) {
    // handle error
    fis.close(); // duplicated!
} catch (RuntimeException e) {
    // handle runtime error
    fis.close(); // duplicated again!
}
```

`finally` solves this by providing a single block that executes **no matter how** the `try` block exits — normally, via exception, or via `return`/`break`/`continue`.

## Design Rationale

The Java Language Specification guarantees that a `finally` block executes in these scenarios:

1. **Normal completion** — `try` completes without exception
2. **Exception thrown and caught** — `catch` executes, then `finally`
3. **Exception thrown and not caught** — `finally` executes, then exception propagates
4. **`return` statement in `try`** — `finally` executes **before** the method actually returns
5. **`break`, `continue`, or `throw` in `try`** — `finally` executes before the control transfer

This guarantee is the core design contract. The JVM enforces it at the bytecode level, and the compiler inserts `finally` code into every exit path.

## What Is finally

A `finally` block is an optional block that follows a `try` or `try-catch` statement. It contains cleanup code that must run after the `try` body completes, regardless of the outcome.

```java
try {
    // work
} catch (Exception e) {
    // handle error
} finally {
    // cleanup — ALWAYS runs
}
```

### Basic Execution Order

There are three valid forms of try-catch-finally:

**1. try → finally (no catch)**

```java
try {
    System.out.println("try");
} finally {
    System.out.println("finally");
}
// Output: try, finally
```

**2. try-catch → finally**

```java
try {
    System.out.println("try");
    throw new RuntimeException("boom");
} catch (RuntimeException e) {
    System.out.println("catch: " + e.getMessage());
} finally {
    System.out.println("finally");
}
// Output: try, catch: boom, finally
```

**3. try-catch-finally (full form)**

```java
try {
    System.out.println("try");
} catch (Exception e) {
    System.out.println("catch");
} finally {
    System.out.println("finally");
}
// Output: try, finally
```

## try-catch-finally Execution Order

```
┌──────────────────────────────────────┐
│           try block                  │
└──────────────┬───────────────────────┘
               │
        ┌──────┴──────┐
        │ Exception?  │
        └──────┬──────┘
       No      │      Yes
    ┌──────────┘      └──────────┐
    ▼                            ▼
┌──────────────┐       ┌──────────────────┐
│ (skip catch) │       │  catch block     │
└──────┬───────┘       │  (if type matches)│
       │               └────────┬─────────┘
       │                        │
       └───────────┬────────────┘
                   ▼
         ┌──────────────────┐
         │  finally block   │
         │  (ALWAYS RUNS)   │
         └────────┬─────────┘
                  │
           ┌──────┴──────┐
           │ Return/throw│
           │ in finally? │
           └──────┬──────┘
            Yes   │   No
        ┌─────────┘    └─────────┐
        ▼                        ▼
  ┌──────────┐          ┌──────────────┐
  │ Override │          │ Continue     │
  │ return   │          │ normally     │
  └──────────┘          └──────────────┘
```

### Exception in catch

```java
try {
    throw new IOException("io");
} catch (IOException e) {
    System.out.println("caught: " + e.getMessage());
    throw new RuntimeException("runtime");
} finally {
    System.out.println("finally");
}
// Output: caught: io, finally
// RuntimeException propagates after finally
```

### Multiple catch blocks

```java
try {
    throw new IllegalArgumentException("bad arg");
} catch (IllegalArgumentException e) {
    System.out.println("illegal arg: " + e.getMessage());
} catch (Exception e) {
    System.out.println("general: " + e.getMessage());
} finally {
    System.out.println("finally");
}
// Output: illegal arg: bad arg, finally
```

## finally vs Try-with-Resources

Since Java 7, try-with-resources (TWR) has replaced `finally` for most resource cleanup. Here is a direct comparison:

| Aspect | `finally` | try-with-resources |
|--------|-----------|-------------------|
| Syntax | Verbose, manual | Concise, declarative |
| Close exceptions | May mask original exception | Suppressed, never lost |
| Null checks | Required | Not needed |
| Ordering | Manual (reverse open order) | Automatic |
| Readability | Imperative cleanup | Declarative resource lifecycle |
| Scope | Any cleanup | Only `AutoCloseable` resources |

### When to use finally over TWR

- **Non-Closeable cleanup** — Releasing a `Lock.lock()`, clearing a `ThreadLocal`, resetting a flag
- **Legacy code** — Pre-Java 7 codebases
- **Multiple cleanup points** — When cleanup spans different scopes

```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock(); // not AutoCloseable
}
```

### When to use TWR over finally

- **`AutoCloseable` resources** — Files, connections, streams, sockets
- **Any new code** — TWR is the modern idiomatic approach

## finally with Return Statements

This is the most dangerous `finally` pitfall. When a `try` block contains a `return` statement, the `finally` block executes **before** the method actually returns. If `finally` also contains a `return` statement, it **overrides** the return value from `try`.

```java
static int dangerousReturn() {
    try {
        return 1;
    } finally {
        return 2; // overrides the return from try
    }
}
// Returns 2, not 1!
```

This is almost always a bug. The compiler may warn about it, but it compiles and runs. The behavior is unintuitive and makes code extremely hard to reason about.

### The bytecode explanation

The JVM pushes return values onto the operand stack. When `try` executes `return 1`, the value `1` is placed on the stack. But before the method returns, `finally` runs. If `finally` executes `return 2`, it places `2` on the stack, **replacing** the `1`. The method then returns `2`.

### Compound return values

The same override behavior applies to reference types:

```java
static List<String> dangerousList() {
    List<String> list = new ArrayList<>();
    list.add("try");
    try {
        return list;
    } finally {
        list = new ArrayList<>();
        list.add("finally");
        return list; // overrides
    }
}
// Returns [finally], not [try]
```

### The compiler warning

Most Java compilers emit a warning: *"finally clause cannot complete normally"*. This warning should be treated as an error in practice. **Never write a `return` statement in a `finally` block.**

## finally and Exception Interaction

When both `try` and `finally` throw exceptions, the `finally` exception **overrides** the `try` exception. The original exception is **lost** — it is not chained, not suppressed, just gone.

```java
static void exceptionInteraction() {
    try {
        throw new RuntimeException("original");
    } finally {
        throw new RuntimeException("finally");
    }
    // RuntimeException("finally") propagates
    // RuntimeException("original") is LOST
}
```

This is different from TWR, where close exceptions are **suppressed** and attached to the primary exception. With `finally`, you must manually preserve the original:

```java
static void preserveException() {
    RuntimeException original = null;
    try {
        throw new RuntimeException("original");
    } catch (RuntimeException e) {
        original = e;
    } finally {
        try {
            throw new RuntimeException("finally");
        } catch (RuntimeException e) {
            original.addSuppressed(e);
        }
    }
}
```

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | `finally` block introduced for guaranteed cleanup |
| JDK 1.4 | Exception chaining preserved cause through finally |
| JDK 7 | Try-with-resources introduced as preferred alternative for AutoCloseable |
| JDK 7 | Suppressed exceptions prevent finally from masking original exceptions |

## Summary

| Concept | Key Point |
|---------|-----------|
| Finally Block | Guaranteed execution for cleanup regardless of how try exits |
| Execution Order | try → catch (if exception) → finally (always); finally runs before return |
| Try-with-Resources | Modern alternative to finally for AutoCloseable resources (Java 7+) |
| Return Override | `return` in finally overrides try's return value — avoid this dangerous pattern |
| Exception Interaction | Exception in finally overrides try's exception; original is lost |
| When to Use Finally | Non-Closeable cleanup (locks, ThreadLocal), legacy code, multiple cleanup points |
| Three Valid Forms | try-finally, try-catch-finally, try-catch (without finally) |
| JVM Enforcement | Compiler inserts finally code into every exit path at bytecode level |

---
**Continue:** [Part 2](README-Part2.md)
