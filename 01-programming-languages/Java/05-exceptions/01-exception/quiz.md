# Quiz: Exception

## Questions

**1. What class does `Exception` extend?**

**2. Is `Exception` checked or unchecked? Explain.**

**3. Name four constructors of `Exception`.**

**4. What does `fillInStackTrace()` return?**

**5. What is the difference between `getMessage()` and `getLocalizedMessage()`?**

**6. Why should you never catch generic `Exception` in application code?**

**7. Name three common exception types that extend `Exception` directly (not `RuntimeException`).**

**8. What happens if you call `initCause()` twice on the same exception?**

**9. What is exception chaining and why is it useful?**

**10. In what scenario is it legitimate to catch generic `Exception`?**

**11. What is the relationship between `Exception` and `RuntimeException`?**

**12. Why is `catch (Throwable t)` almost always wrong?**

**13. What does the `throws` declaration in a method signature mean for callers?**

---

## Answers

**1.** `Exception` extends `java.lang.Throwable`.

**2.** Checked. All subclasses of `Exception` (that are not subclasses of `RuntimeException`) must be caught or declared in a `throws` clause. The compiler enforces this.

**3.** `Exception()`, `Exception(String message)`, `Exception(String message, Throwable cause)`, `Exception(Throwable cause)`.

**4.** It returns the same throwable (`this`) with a completed stack trace. It is called automatically in the constructor but can be called again to re-fill the trace.

**5.** `getMessage()` returns the detail message string. `getLocalizedMessage()` returns a locale-specific message. The default implementation of `getLocalizedMessage()` simply calls `getMessage()`, so you must override it for localization.

**6.** It masks the specific error type, may accidentally catch unchecked exceptions, makes debugging harder, and violates the checked exception contract that provides callers with actionable information.

**7.** `IOException`, `SQLException`, `InterruptedException`, `ReflectiveOperationException`. (Any four of: IOException, SQLException, InterruptedException, CloneNotSupportedException, ReflectiveOperationException, AWTException.)

**8.** It throws `IllegalStateException`. `initCause()` can only be called once. This prevents accidentally overwriting the original cause.

**9.** Exception chaining preserves the original exception as the cause of a new exception. This is useful at architectural boundaries where you want to translate an exception type while preserving diagnostic information about the root cause.

**10.** Framework-level catch-all handlers at the top of a call stack (servlet containers, thread pool task runners, main methods) where you must handle any possible exception to prevent the thread from dying silently.

**11.** `RuntimeException` is a direct subclass of `Exception`. It is the superclass for all unchecked exceptions. Subclasses of `Exception` that are not subclasses of `RuntimeException` are checked.

**12.** `Throwable` also catches `Error` and its subclasses (`OutOfMemoryError`, `StackOverflowError`). Errors represent JVM-level problems that applications cannot recover from. Catching them can mask fatal conditions.

**13.** It declares that the method may throw the specified exception types. Callers must either catch those types or declare them in their own `throws` clause, propagating the contract up the call stack.
