# Quiz: Java Exception Handling Module

55 questions — 35 Multiple Choice + 20 True/False

---

## Multiple Choice Questions

### Q1: What is the root class of all exceptions in Java?
A) Exception
B) Error
C) Throwable
D) RuntimeException

**Answer:** C

### Q2: What is the difference between checked and unchecked exceptions?
A) Checked are runtime, unchecked are compile-time
B) Checked are compile-time, unchecked are runtime
C) Both are compile-time
D) Both are runtime

**Answer:** B

### Q3: When should you use a checked exception?
A) For programming bugs
B) For recoverable conditions the caller can handle
C) For JVM errors
D) Never

**Answer:** B

### Q4: What is the purpose of the `finally` block?
A) To catch exceptions
B) To declare exceptions
C) To execute cleanup code regardless of exception outcome
D) To throw exceptions

**Answer:** C

### Q5: What happens if an exception is thrown in a `finally` block?
A) It is ignored
B) It overrides any exception from the try block
C) The try block exception is rethrown
D) Both exceptions are thrown

**Answer:** B

### Q6: Can you catch an `Error` in Java?
A) No, the compiler prevents it
B) Yes, but you shouldn't — errors are unrecoverable
C) Only with try-with-resources
D) Only if declared in throws

**Answer:** B

### Q7: What must a resource implement to be used in try-with-resources?
A) Runnable
B) Serializable
C) AutoCloseable
D) Closeable

**Answer:** C

### Q8: What is exception chaining?
A) Catching multiple exceptions in sequence
B) Wrapping one exception inside another to preserve the cause
C) Throwing exceptions in a loop
D) Using multiple catch blocks

**Answer:** B

### Q9: Can a `finally` block run without a `catch` block?
A) No, catch is always required
B) Yes, try-finally is valid
C) Only with try-with-resources
D) Only if the method declares throws

**Answer:** B

### Q10: What is a suppressed exception?
A) An exception caught but not logged
B) An exception thrown during cleanup and attached to the primary exception
C) An exception that is never thrown
D) An exception ignored by the compiler

**Answer:** B

### Q11: Why should you avoid using exceptions for control flow?
A) The compiler forbids it
B) It is 10-100x slower than conditionals due to stack trace capture
C) It causes memory leaks
D) It only works with checked exceptions

**Answer:** B

### Q12: What is the difference between `throw` and `throws`?
A) throw is a declaration, throws is a statement
B) throw explicitly raises an exception, throws declares exceptions in a method signature
C) They are interchangeable
D) throw is for checked, throws is for unchecked

**Answer:** B

### Q13: What happens if you don't catch a checked exception?
A) The program runs normally
B) The compiler reports an error
C) It becomes an unchecked exception
D) The JVM catches it automatically

**Answer:** B

### Q14: Can a `finally` block have a `return` statement?
A) No, it causes a compiler error
B) Yes, but it overrides the try/catch return value silently
C) Yes, and it takes precedence over try/catch
D) Only if the method returns void

**Answer:** B

### Q15: What is `ExceptionInInitializerError`?
A) A checked exception for bad imports
B) An Error thrown when a static initializer fails
C) A RuntimeException for null fields
D) A compilation error

**Answer:** B

### Q16: What is the order of resource closing in try-with-resources with multiple resources?
A) First declared, first closed
B) Last declared, first closed
C) Random order
D) Simultaneous

**Answer:** B

### Q17: In try-with-resources, if both try and close throw exceptions, which is primarily thrown?
A) Exception from try block
B) Exception from close method
C) A new RuntimeException
D) Both are thrown as suppressed

**Answer:** A

### Q18: What is required to create a custom checked exception?
A) Extend RuntimeException
B) Extend Exception
C) Implement Throwable
D) Annotate with @Checked

**Answer:** B

### Q19: Which should you extend for a custom unchecked exception?
A) Exception
B) RuntimeException
C) Error
D) Throwable

**Answer:** B

### Q20: How do you preserve the original exception when wrapping?
A) `new Exception(cause)`
B) `new Exception().initCause(cause)`
C) Both A and B
D) The original is always lost

**Answer:** C

### Q21: What does `getCause()` return?
A) The exception message
B) The stack trace
C) The original exception that triggered this one
D) The exception class name

**Answer:** C

### Q22: How do you retrieve the stack trace of an exception?
A) exception.printStackTrace()
B) exception.getStackTrace()
C) exception.getTrace()
D) Both A and B

**Answer:** D

### Q23: What does `StackTraceElement` contain?
A) Only class name and method name
B) Class name, method name, file name, and line number
C) Only the line number
D) The exception message

**Answer:** B

### Q24: How do you retrieve suppressed exceptions?
A) exception.getSuppressed()
B) exception.suppressedExceptions()
C) exception.getSuppressedExceptions()
D) They are not retrievable

**Answer:** A

### Q25: In which construct are suppressed exceptions automatically added?
A) try-catch
B) try-finally
C) try-with-resources
D) synchronized block

**Answer:** C

### Q26: What happens when an exception occurs in a thread's run method?
A) It crashes the JVM
B) The thread's uncaught exception handler is invoked
C) The main thread catches it
D) The exception is silently ignored

**Answer:** B

### Q27: How do you set a global uncaught exception handler for all threads?
A) Thread.setDefaultUncaughtExceptionHandler()
B) Thread.setUncaughtExceptionHandler()
C) Runtime.setExceptionHandler()
D) ExceptionHandler.global()

**Answer:** A

### Q28: In production, why should you avoid logging and rethrowing the same exception?
A) It duplicates the log entry and creates unnecessary objects
B) It loses the stack trace
C) It is deprecated
D) It only works with unchecked exceptions

**Answer:** A

### Q29: What is a common pattern for exception handling in layered architecture?
A) Catch and wrap exceptions at each layer
B) Let exceptions propagate without handling
C) Only handle at the presentation layer
D) Ignore exceptions in lower layers

**Answer:** A

### Q30: Why use specific exception types instead of generic Exception?
A) Better performance
B) More precise handling and clearer intent
C) Required by the compiler
D) Prevents exceptions from being thrown

**Answer:** B

### Q31: What is the "fail-fast" principle?
A) Catching all exceptions immediately
B) Throwing exceptions immediately when an error occurs
C) Ignoring exceptions to continue execution
D) Logging and continuing

**Answer:** B

### Q32: Why avoid `e.printStackTrace()` in production?
A) It is deprecated
B) It outputs to stderr, not captured by logging frameworks
C) It is too slow
D) It doesn't show the full trace

**Answer:** B

### Q33: Which is NOT an unchecked exception?
A) NullPointerException
B) ArrayIndexOutOfBoundsException
C) IOException
D) IllegalArgumentException

**Answer:** C

### Q34: What happens in this code?
```java
try {
    throw new IOException("Error");
} finally {
    System.out.println("Finally");
}
```
A) Prints "Finally" then throws IOException
B) Throws IOException, finally is skipped
C) Compilation error
D) Prints "Finally" and swallows exception

**Answer:** A

### Q35: Which exception is thrown when a thread is interrupted while waiting?
A) InterruptedException
B) InterruptedIOException
C) RuntimeException
D) ThreadDeath

**Answer:** A

---

## True / False Questions

### Q36: `Throwable` is the parent class of both `Exception` and `Error`.
**Answer:** True

### Q37: Checked exceptions must be caught or declared in the method signature.
**Answer:** True

### Q38: The `finally` block is optional and has no effect if omitted.
**Answer:** True — finally is optional. When omitted, cleanup only happens in catch blocks or after try-catch.

### Q39: You can have a `try` block without `catch` or `finally`.
**Answer:** False — a try block must be followed by at least one catch or finally block.

### Q40: `RuntimeException` and its subclasses are checked at compile time.
**Answer:** False — RuntimeException and its subclasses are unchecked.

### Q41: `Error` should be caught in application code whenever possible.
**Answer:** False — errors represent unrecoverable JVM failures and should generally propagate.

### Q42: Resources in try-with-resources are closed in reverse declaration order.
**Answer:** True

### Q43: `AutoCloseable` was introduced in Java 7.
**Answer:** True

### Q44: A method can declare `throws` for exceptions it never actually throws.
**Answer:** True — the compiler allows this. It's legal but considered poor practice.

### Q45: `throw` and `throws` are interchangeable keywords.
**Answer:** False — throw raises an exception, throws declares exceptions in a method signature.

### Q46: Exception chaining preserves the original cause of an error.
**Answer:** True

### Q47: `printStackTrace()` should be used in production logging.
**Answer:** False — it outputs to stderr, not to logging frameworks.

### Q48: `finally` block always executes, even if `System.exit()` is called.
**Answer:** False — finally does not execute if System.exit() or JVM crashes.

### Q49: Suppressed exceptions can only occur in try-with-resources.
**Answer:** True — only try-with-resources automatically adds suppressed exceptions.

### Q50: `UncaughtExceptionHandler` is invoked when an exception escapes a thread's run method.
**Answer:** True

### Q51: Custom exceptions must always provide multiple constructors.
**Answer:** False — it's recommended but not required by the compiler.

### Q52: Using exceptions for control flow has no performance impact.
**Answer:** False — creating exceptions captures stack traces, making it 10-100x slower than conditionals.

### Q53: `NullPointerException` is a checked exception.
**Answer:** False — NullPointerException is an unchecked exception (extends RuntimeException).

### Q54: `initCause()` can only be called once on an exception.
**Answer:** True — calling it again throws IllegalStateException.

### Q55: try-with-resources can manage any object, regardless of interface implementation.
**Answer:** False — the resource must implement AutoCloseable (or Closeable, which extends it).
