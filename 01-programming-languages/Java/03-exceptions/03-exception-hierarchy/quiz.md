# Quiz: Java Exception Hierarchy

## Questions

### Q1: What is the base class for all exceptions and errors in Java?
**Answer:** C) `Throwable` — It's the root of the exception hierarchy, from which both `Exception` and `Error` extend.

### Q2: Which of the following is an unchecked exception?
**Answer:** C) `NullPointerException` — It extends `RuntimeException`, which is unchecked.

### Q3: Which of the following is a checked exception?
**Answer:** C) `FileNotFoundException` — It extends `Exception` directly, making it checked.

### Q4: What is the correct order for catching exceptions?
**Answer:** B) Specific first, then general — If you catch `Exception` before `IOException`, the `IOException` handler becomes unreachable.

### Q5: Which of the following is an Error (not an Exception)?
**Answer:** C) `OutOfMemoryError` — Errors represent serious JVM-level failures, not recoverable conditions.

### Q6: What happens if you catch `Exception` before `IOException`?
**Answer:** C) `IOException` handler is unreachable — The compiler will flag this as an error.

### Q7: What is the purpose of the `Error` class?
**Answer:** B) Serious JVM-level failures — Errors indicate conditions that applications should not try to catch.

### Q8: Which of the following is NOT a subclass of `RuntimeException`?
**Answer:** B) `IOException` — It extends `Exception` directly, making it a checked exception.

### Q9: What does `Throwable.getCause()` return?
**Answer:** B) The original exception that caused this one — It provides the causal chain for debugging.

### Q10: Should you catch `Error` in application code?
**Answer:** C) Generally no, unless there's a specific reason — Errors are typically unrecoverable.

### Q11: What is the difference between checked and unchecked exceptions?
**Answer:** Checked exceptions must be declared in the method signature or caught, while unchecked exceptions (subclasses of `RuntimeException`) do not require explicit handling.

### Q12: What happens when a `RuntimeException` is thrown but not caught?
**Answer:** The JVM terminates the thread, printing the stack trace to the console.

### Q13: Why is catching `Throwable` dangerous in application code?
**Answer:** It catches both `Exception` and `Error`, potentially masking serious JVM issues that should not be handled.

### Q14: What is the recommended way to handle `OutOfMemoryError`?
**Answer:** It should not be caught in normal code; instead, adjust JVM memory settings or fix memory leaks.

### Q15: When would you create a custom exception extending `Error`?
**Answer:** When you need to represent a serious system-level failure that should not be caught, such as a fatal configuration error.