# Quiz: Exception

## Questions

### Q1: What class does `Exception` extend?
**Answer:** `Exception` extends `java.lang.Throwable`.

### Q2: Is `Exception` checked or unchecked? Explain.
**Answer:** Checked. All subclasses of `Exception` (that are not subclasses of `RuntimeException`) must be caught or declared in a `throws` clause. The compiler enforces this.

### Q3: Name four constructors of `Exception`.
**Answer:** `Exception()`, `Exception(String message)`, `Exception(String message, Throwable cause)`, `Exception(Throwable cause)`.

### Q4: What does `fillInStackTrace()` return?
**Answer:** It returns the same throwable (`this`) with a completed stack trace. It is called automatically in the constructor but can be called again to re-fill the trace.

### Q5: What is the difference between `getMessage()` and `getLocalizedMessage()`?
**Answer:** `getMessage()` returns the detail message string. `getLocalizedMessage()` returns a locale-specific message. The default implementation of `getLocalizedMessage()` simply calls `getMessage()`, so you must override it for localization.

### Q6: Why should you never catch generic `Exception` in application code?
**Answer:** It masks the specific error type, may accidentally catch unchecked exceptions, makes debugging harder, and violates the checked exception contract that provides callers with actionable information.

### Q7: Name three common exception types that extend `Exception` directly (not `RuntimeException`).
**Answer:** `IOException`, `SQLException`, `InterruptedException`, `ReflectiveOperationException`. (Any four of: IOException, SQLException, InterruptedException, CloneNotSupportedException, ReflectiveOperationException, AWTException.)

### Q8: What happens if you call `initCause()` twice on the same exception?
**Answer:** It throws `IllegalStateException`. `initCause()` can only be called once. This prevents accidentally overwriting the original cause.

### Q9: What is exception chaining and why is it useful?
**Answer:** Exception chaining preserves the original exception as the cause of a new exception. This is useful at architectural boundaries where you want to translate an exception type while preserving diagnostic information about the root cause.

### Q10: In what scenario is it legitimate to catch generic `Exception`?
**Answer:** Framework-level catch-all handlers at the top of a call stack (servlet containers, thread pool task runners, main methods) where you must handle any possible exception to prevent the thread from dying silently.

### Q11: What is the relationship between `Exception` and `RuntimeException`?
**Answer:** `RuntimeException` is a direct subclass of `Exception`. It is the superclass for all unchecked exceptions. Subclasses of `Exception` that are not subclasses of `RuntimeException` are checked.

### Q12: Why is `catch (Throwable t)` almost always wrong?
**Answer:** `Throwable` also catches `Error` and its subclasses (`OutOfMemoryError`, `StackOverflowError`). Errors represent JVM-level problems that applications cannot recover from. Catching them can mask fatal conditions.

### Q13: What does the `throws` declaration in a method signature mean for callers?
**Answer:** It declares that the method may throw the specified exception types. Callers must either catch those types or declare them in their own `throws` clause, propagating the contract up the call stack.