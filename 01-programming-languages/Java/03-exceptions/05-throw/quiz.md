# Quiz: The throw Keyword

## Questions

### Q1: What is the output of this code?
```java
public static void main(String[] args) {
    try {
        throw new RuntimeException("boom");
    } catch (RuntimeException e) {
        System.out.println(e.getMessage());
    }
}
```
**Answer:** A) `boom` — The throw statement raises the exception, which is caught by the catch block.

### Q2: Which of the following is a valid throw statement?
**Answer:** C) `throw new IllegalArgumentException("bad");` — throw requires an expression that evaluates to a Throwable object.

### Q3: What happens when you throw null?
```java
RuntimeException e = null;
throw e;
```
**Answer:** B) NullPointerException is thrown — The JVM wraps null in a NullPointerException because it is not a valid Throwable.

### Q4: What is the output of this code?
```java
try {
    throw new RuntimeException("original");
} finally {
    throw new RuntimeException("finally");
}
```
**Answer:** B) RuntimeException("finally") propagates — An exception thrown in finally replaces the original exception from try.

### Q5: Which is correct about `throw` and `throws`?
**Answer:** B) `throw` is a statement that raises an exception; `throws` is a declaration in the method signature.

### Q6: What does exception chaining accomplish?
```java
throw new ServiceException("failed", originalException);
```
**Answer:** A) It preserves the root cause by wrapping the original exception as the cause of the new one.

### Q7: What is the output of this code?
```java
try {
    try {
        throw new RuntimeException("inner");
    } finally {
        throw new RuntimeException("finally");
    }
} catch (RuntimeException e) {
    System.out.println(e.getMessage());
}
```
**Answer:** A) `finally` — The inner finally exception replaces the inner try exception; it is then caught by the outer catch.

### Q8: Which pattern is considered an anti-pattern?
**Answer:** C) Using exceptions for normal control flow (e.g., throwing and catching to simulate an if-else).

### Q9: What is the purpose of `fillInStackTrace()` when rethrowing?
**Answer:** B) It records the current call stack so the stack trace reflects the rethrow location.

### Q10: What is the correct order when multiple catch blocks are present?
```java
try {
    throw new IllegalArgumentException("test");
} catch (RuntimeException e) {
    System.out.println("runtime");
} catch (Exception e) {
    System.out.println("exception");
}
```
**Answer:** A) `runtime` — The catch blocks are checked in order; the first matching type handles the exception.
