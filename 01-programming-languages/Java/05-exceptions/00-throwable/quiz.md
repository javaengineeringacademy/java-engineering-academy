# Quiz: Throwable

## Questions

### Q1
What class is the root of the Java exception hierarchy?

### Q2
What interface does `Throwable` implement?

### Q3
What method returns the human-readable description of a Throwable?

### Q4
What method returns the underlying cause of a Throwable?

### Q5
Is `Throwable` a checked or unchecked type?

### Q6
What native method captures the stack trace when a Throwable is created?

### Q7
Since which JDK version can you add suppressed exceptions to a Throwable?

### Q8
What does `initCause()` do, and what happens if you call it twice?

### Q9
What is the difference between `getMessage()` and `getLocalizedMessage()`?

### Q10
Why is catching `Throwable` in application code generally a bad idea?

### Q11
What does `StackTraceElement` contain?

### Q12
In a stack trace, which element represents where the exception was actually thrown?

### Q13
What is the performance cost of creating a Throwable?

### Q14
Can you override `addSuppressed()` in a subclass?

### Q15
What is the `serialVersionUID` of Throwable used for?

---

## Answers

### A1
`java.lang.Throwable`. Every exception and error in Java is a subclass of Throwable.

### A2
`Serializable`. This makes Throwable transmissible across JVM boundaries via RMI and serialization.

### A3
`getMessage()`. Returns the `detailMessage` field, which may be `null`.

### A4
`getCause()`. Returns the Throwable that caused this one, or `null` if no cause was set.

### A5
Checked. The compiler forces you to catch or declare it in a `throws` clause.

### A6
`fillInStackTrace()`. It is a `synchronized native` method that walks the Java call stack.

### A7
JDK 7. The `addSuppressed()` and `getSuppressed()` methods were added to support try-with-resources.

### A8
`initCause()` sets the underlying cause of the Throwable. It may only be called once; a second call throws `IllegalStateException`.

### A9
`getMessage()` returns the raw detail message. `getLocalizedMessage()` is overridable and defaults to `getMessage()`. Subclasses can override it to provide locale-specific messages.

### A10
Catching `Throwable` also catches `Error` subclasses like `OutOfMemoryError` and `StackOverflowError`, which are typically unrecoverable. This masks fatal JVM failures and can lead to undefined behavior.

### A11
Four fields: `declaringClass` (fully qualified class name), `methodName`, `fileName`, and `lineNumber`.

### A12
The topmost element in the stack trace array. It is the most recent call on the stack when the exception was created.

### A13
The primary cost is capturing the stack trace via the native `fillInStackTrace()` method. For deep stacks, this can take microseconds to milliseconds.

### A14
No. `addSuppressed()` is declared `final` in `Throwable`.

### A15
It is used during serialization to verify that a class being deserialized is compatible with the serialized form. It prevents version mismatches.
