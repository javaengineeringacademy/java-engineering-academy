# Quiz: Exception Module

## Questions

### Q1: What is the root class of all exceptions in Java?
**Answer:** `Throwable`. Both `Exception` and `Error` extend `Throwable`.

### Q2: What is the difference between checked and unchecked exceptions?
**Answer:** Checked exceptions are verified at compile time — the compiler forces you to catch or declare them. Unchecked exceptions (RuntimeException and its subclasses) are not checked at compile time.

### Q3: When should you use a checked exception vs an unchecked exception?
**Answer:** Use checked exceptions for recoverable conditions (file not found, network timeout). Use unchecked exceptions for programming bugs (null pointer, illegal argument).

### Q4: What is the purpose of the `finally` block?
**Answer:** `finally` executes regardless of whether an exception is thrown or caught. It's used for cleanup code — closing resources, releasing locks, etc.

### Q5: What happens if an exception is thrown in a `finally` block?
**Answer:** It overrides any exception from the `try` block. The original exception is lost (unless chained as a suppressed exception in Java 7+).

### Q6: Can you catch an `Error` in Java?
**Answer:** Technically yes, but you shouldn't. Errors represent unrecoverable JVM failures (OutOfMemoryError, StackOverflowError). Let them propagate.

### Q7: What is try-with-resources?
**Answer:** A Java 7 feature that automatically closes resources implementing `AutoCloseable`. The resource is closed after the try block, even if an exception occurs.

### Q8: What is exception chaining?
**Answer:** Wrapping one exception inside another to preserve the original cause. Use `new Exception("message", cause)` or `initCause()`.

### Q9: Can a `finally` block run without a `catch` block?
**Answer:** Yes. `try-finally` is valid. The finally block runs after the try block completes, whether normally or via exception.

### Q10: What is a suppressed exception?
**Answer:** An exception thrown during cleanup (in a try-with-resources) that is automatically attached to the primary exception via `addSuppressed()`.

### Q11: Why shouldn't you use exceptions for control flow?
**Answer:** Creating exceptions is expensive — it captures the full stack trace. Use conditionals for expected logic branches. Exception-based flow is 10-100x slower.

### Q12: What is the difference between `throw` and `throws`?
**Answer:** `throw` is a statement that throws an exception. `throws` is a declaration in the method signature listing exceptions the method might throw.

### Q13: What happens if you don't catch a checked exception?
**Answer:** The compiler reports an error. You must either catch it with `try-catch` or declare it in the method signature with `throws`.

### Q14: Can a `finally` block have a `return` statement?
**Answer:** Yes, but it's dangerous. The `finally` return overrides the `try`/`catch` return, silently swallowing the original result.

### Q15: What is the `ExceptionInInitializerError`?
**Answer:** An `Error` thrown when a static initializer fails. The original exception is wrapped as the cause. The class becomes unusable after this.
