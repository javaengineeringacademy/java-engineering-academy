# Quiz: Finally Block

## Questions

### Q1: What is the output of this code?
```java
try {
    System.out.println("try");
} finally {
    System.out.println("finally");
}
```
**Answer:** C) `try`, `finally` — The finally block always executes after the try block.

### Q2: What is the output of this code?
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
**Answer:** A) `try`, `catch`, `finally` — The catch block handles the exception, then finally executes.

### Q3: What does this method return?
```java
static int value() {
    try {
        return 1;
    } finally {
        return 2;
    }
}
```
**Answer:** B) 2 — The finally block's return overrides the try block's return.

### Q4: What is the output of this code?
```java
try {
    throw new RuntimeException("original");
} finally {
    throw new RuntimeException("finally");
}
```
**Answer:** B) `RuntimeException("finally")` propagates — The finally exception replaces the try exception.

### Q5: What happens in this code?
```java
try {
    throw new RuntimeException("boom");
} finally {
    System.out.println("cleanup");
}
```
**Answer:** B) `cleanup` prints, then `RuntimeException` propagates — Finally runs regardless of exceptions.

### Q6: What does this method return?
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
**Answer:** B) `"hello"` — The return value is captured before finally mutates the StringBuilder.

### Q7: What is the output of this code?
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
**Answer:** B) `outer try`, `inner try`, `inner finally`, `outer finally` — Inner finally runs before outer finally.

### Q8: Which is preferred for closing a `FileInputStream`?
**Answer:** B) try-with-resources — TWR is the modern idiomatic approach.

### Q9: What is the output of this code?
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
**Answer:** B) `try`, `finally`, `caught: in finally`, `after` — Exception in finally is caught, then execution continues.

### Q10: Which is the best use case for `finally`?
**Answer:** B) Releasing a `ReentrantLock` — Lock is not AutoCloseable, so finally is appropriate.

### Q11: Why does finally execute even when an exception is thrown?
**Answer:** Finally is designed to guarantee cleanup code runs regardless of whether an exception occurs.

### Q12: What happens if you have both catch and finally blocks?
**Answer:** The catch block handles the exception first, then the finally block executes.

### Q13: Can finally block suppress an exception?
**Answer:** Yes, if an exception is thrown in finally, it replaces the original exception from the try block.

### Q14: When is finally NOT executed?
**Answer:** When the JVM exits, the thread is killed, or `System.exit()` is called.