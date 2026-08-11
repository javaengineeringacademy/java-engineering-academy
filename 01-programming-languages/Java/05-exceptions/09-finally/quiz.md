# Finally Block Quiz

## Question 1: Basic Execution

What is the output?

```java
try {
    System.out.println("try");
} finally {
    System.out.println("finally");
}
```

A) `try`
B) `finally`
C) `try`, `finally`
D) `finally`, `try`

## Question 2: Exception Path

What is the output?

```java
try {
    System.out.println("try");
    throw new RuntimeException("boom");
} catch (RuntimeException e) {
    System.out.println("catch");
} finally {
    System.out.println("finally");
}
```

A) `try`, `catch`, `finally`
B) `try`, `finally`
C) `try`, `catch`
D) `catch`, `finally`

## Question 3: Return Override

What does this method return?

```java
static int value() {
    try {
        return 1;
    } finally {
        return 2;
    }
}
```

A) 1
B) 2
C) Compilation error
D) Throws exception at runtime

## Question 4: Exception Masking

What is the output?

```java
try {
    throw new RuntimeException("original");
} finally {
    throw new RuntimeException("finally");
}
```

A) `RuntimeException("original")` propagates
B) `RuntimeException("finally")` propagates
C) Both propagate
D) Compilation error

## Question 5: Missing Catch

What happens?

```java
try {
    throw new RuntimeException("boom");
} finally {
    System.out.println("cleanup");
}
```

A) Compilation error — catch is required
B) `cleanup` prints, then `RuntimeException` propagates
C) Exception is swallowed
D) `cleanup` prints, then program exits

## Question 6: Finally After Return

What does this method return?

```java
static String build() {
    StringBuilder sb = new StringBuilder();
    try {
        sb.append("hello");
        return sb.toString();
    } finally {
        sb.append(" world");
    }
}
```

A) `"hello world"`
B) `"hello"`
C) `""`
D) Compilation error

## Question 7: Nested Try-Finally

What is the output?

```java
try {
    System.out.println("outer try");
    try {
        System.out.println("inner try");
    } finally {
        System.out.println("inner finally");
    }
} finally {
    System.out.println("outer finally");
}
```

A) `outer try`, `inner try`, `outer finally`
B) `outer try`, `inner try`, `inner finally`, `outer finally`
C) `inner try`, `inner finally`, `outer try`, `outer finally`
D) `inner try`, `outer try`, `inner finally`, `outer finally`

## Question 8: Finally vs TWR

Which is preferred for closing a `FileInputStream`?

A) `finally` with `fis.close()`
B) try-with-resources
C) Both are equally good
D) Neither — let GC handle it

## Question 9: Catch Exception in Finally

What is the output?

```java
try {
    System.out.println("try");
} finally {
    try {
        System.out.println("finally");
        throw new RuntimeException("in finally");
    } catch (RuntimeException e) {
        System.out.println("caught: " + e.getMessage());
    }
}
System.out.println("after");
```

A) `try`, `finally`, `after`
B) `try`, `finally`, `caught: in finally`, `after`
C) `try`, `finally`, `caught: in finally`
D) Compilation error

## Question 10: When to Use Finally

Which is the best use case for `finally`?

A) Closing a `BufferedReader`
B) Releasing a `ReentrantLock`
C) Closing a `Socket`
D) Disposing a `Graphics` object

---

## Answers

1. C (both execute in order)
2. A (catch handles exception, finally always runs)
3. B (finally return overrides try return)
4. B (finally exception replaces try exception)
5. B (finally runs, then uncaught exception propagates)
6. B (return value captured before finally mutates StringBuilder)
7. B (inner finally runs before outer finally)
8. B (TWR is the modern idiomatic approach)
9. B (exception in finally is caught, then execution continues)
10. B (Lock is not AutoCloseable, so finally is appropriate)
