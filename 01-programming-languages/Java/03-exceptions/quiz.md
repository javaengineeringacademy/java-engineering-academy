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

### Q16: What is the primary purpose of the `throw` keyword in Java?
A) To declare exceptions in a method signature
B) To explicitly throw an exception from within a method
C) To catch an exception
D) To suppress an exception

**Answer:** B

### Q17: Which of the following is true about `throws` in a method declaration?
A) It is used to throw an exception
B) It lists the checked exceptions that a method might propagate to its caller
C) It automatically catches exceptions
D) It is optional for all methods

**Answer:** B

### Q18: What happens if a method declares `throws IOException` but does not throw one?
A) Compiler error
B) The method can still be called without handling IOException
C) Runtime exception
D) The throws clause is ignored

**Answer:** B

### Q19: In a try-catch-finally block, which block is guaranteed to execute even if an exception is thrown and not caught?
A) try
B) catch
C) finally
D) None

**Answer:** C

### Q20: What is the output of the following code?
try {
    System.out.println("Try");
} finally {
    System.out.println("Finally");
}
A) Try only
B) Finally only
C) Try then Finally
D) Compilation error

**Answer:** C

### Q21: Can a `finally` block be skipped in any scenario?
A) Yes, if System.exit() is called
B) No, always executes
C) Only in try-with-resources
D) If the JVM crashes

**Answer:** A

### Q22: What must a resource implement to be used in try-with-resources?
A) Runnable
B) AutoCloseable
C) Closeable
D) Serializable

**Answer:** B

### Q23: In try-with-resources, if both the try block and the close method throw an exception, which exception is primarily thrown?
A) Exception from try block
B) Exception from close method
C) The first exception encountered
D) Both are thrown as suppressed

**Answer:** D

### Q24: What is the order of resource closing in try-with-resources when multiple resources are declared?
A) In declaration order (first declared, first closed)
B) In reverse declaration order (last declared, first closed)
C) Random order
D) They are closed simultaneously

**Answer:** B

### Q25: What is required to create a custom checked exception?
A) Extend RuntimeException
B) Extend Exception
C) Implement Throwable
D) No requirement, just create a class

**Answer:** B

### Q26: Which is a common practice when creating a custom unchecked exception?
A) Extend Exception
B) Extend RuntimeException
C) Extend Error
D) Extend Throwable directly

**Answer:** B

### Q27: Why should custom exceptions provide multiple constructors?
A) For compatibility with older Java versions
B) To allow passing different types of messages and causes
C) It's required by the compiler
D) To prevent serialization issues

**Answer:** B

### Q28: How do you preserve the original exception when catching and rethrowing a new one?
A) Use `throw new Exception(cause)`
B) Use `throw new Exception().initCause(cause)`
C) Both A and B
D) Neither, original exception is lost

**Answer:** C

### Q29: What does `getCause()` return in an exception?
A) The exception message
B) The stack trace
C) The original exception that triggered this one
D) The exception type

**Answer:** C

### Q30: Which constructor of Exception is typically used for exception chaining?
A) Exception(String message)
B) Exception(Throwable cause)
C) Exception(String message, Throwable cause)
D) All of the above

**Answer:** C

### Q31: How do you retrieve the stack trace of an exception?
A) exception.printStackTrace()
B) exception.getStackTrace()
C) exception.getTrace()
D) Both A and B

**Answer:** D

### Q32: What does `StackTraceElement` contain?
A) Class name, method name, file name, and line number
B) Only class name
C) Only method name
D) The exception message

**Answer:** A

### Q33: How can you programmatically analyze an exception's stack trace?
A) Use exception.getMessage()
B) Use exception.getStackTrace() to get an array of StackTraceElement
C) Use exception.toString()
D) Stack trace is not accessible

**Answer:** B

### Q34: What is a suppressed exception in Java?
A) An exception that is ignored by the catch block
B) An exception thrown during resource cleanup and attached to the primary exception
C) An exception that is not thrown
D) An exception that is caught but not logged

**Answer:** B

### Q35: How do you retrieve suppressed exceptions from a primary exception?
A) exception.getSuppressed()
B) exception.suppressedExceptions()
C) exception.getSuppressedExceptions()
D) They are not retrievable

**Answer:** A

### Q36: In which construct are suppressed exceptions automatically added?
A) try-catch
B) try-finally
C) try-with-resources
D) synchronized block

**Answer:** C

### Q37: What happens when an exception occurs in a thread's run method?
A) It crashes the JVM
B) The exception is caught by the thread's uncaught exception handler
C) It terminates only that thread
D) The main thread catches it

**Answer:** B

### Q38: How do you set a global uncaught exception handler for all threads?
A) Thread.setDefaultUncaughtExceptionHandler()
B) Thread.setUncaughtExceptionHandler()
C) Runtime.setExceptionHandler()
D) ExceptionHandler.global()

**Answer:** A

### Q39: What is the purpose of `UncaughtExceptionHandler`?
A) To catch exceptions in the main thread only
B) To handle exceptions that are not caught by any catch block in a thread
C) To suppress exceptions
D) To log exceptions to a file

**Answer:** B

### Q40: In production, why should you avoid logging and rethrowing the same exception?
A) It duplicates the log entry
B) It loses the stack trace
C) It creates a new exception object unnecessarily
D) Both A and C

**Answer:** D

### Q41: What is a common pattern for handling exceptions in a layered architecture?
A) Catch and wrap exceptions at each layer
B) Let exceptions propagate without handling
C) Only handle exceptions at the presentation layer
D) Ignore exceptions

**Answer:** A

### Q42: Why is it recommended to use specific exception types rather than generic Exception?
A) For better performance
B) To allow more precise handling
C) It's required by the Java compiler
D) To prevent exceptions from being thrown

**Answer:** B

### Q43: What is the "fail-fast" principle in exception handling?
A) Throwing exceptions immediately when an error occurs
B) Catching all exceptions
C) Ignoring exceptions
D) Logging exceptions and continuing

**Answer:** A

### Q44: Which of the following is a best practice when throwing exceptions?
A) Include descriptive messages
B) Throw new exceptions without messages
C) Reuse exception instances
D) Throw checked exceptions for everything

**Answer:** A

### Q45: Why should you avoid using `e.printStackTrace()` in production?
A) It's deprecated
B) It outputs to stderr, which might not be captured by logging
C) It's too slow
D) It doesn't provide stack trace

**Answer:** B

### Q46: What is the relationship between RuntimeException and unchecked exceptions?
A) RuntimeException is the superclass of all unchecked exceptions
B) Unchecked exceptions are a subclass of RuntimeException
C) They are unrelated
D) RuntimeException is checked

**Answer:** A

### Q47: Which of the following is NOT an unchecked exception?
A) NullPointerException
B) ArrayIndexOutOfBoundsException
C) IOException
D) IllegalArgumentException

**Answer:** C

### Q48: In which scenario might `finally` block not execute in try-with-resources?
A) If the resource's close method throws an exception
B) If the try block throws an exception
C) If the JVM shuts down abruptly
D) If the resource is null

**Answer:** C

### Q49: What is the key advantage of try-with-resources over traditional try-finally?
A) Automatic resource closing
B) Better performance
C) No need for catch blocks
D) It handles only unchecked exceptions

**Answer:** A

### Q50: Consider the following code:
try {
    throw new IOException("Error");
} finally {
    System.out.println("Finally");
}
What happens?
A) Prints "Finally" and then throws IOException
B) Throws IOException and finally is skipped
C) Compilation error
D) Prints "Finally" and swallows the exception

**Answer:** A

### Q51: Which exception is thrown when a thread is interrupted while waiting?
A) InterruptedException
B) InterruptedIOException
C) RuntimeException
D) Error

**Answer:** A

### Q52: What is the result of this code?
try {
    int x = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Caught");
} finally {
    System.out.println("Finally");
}
A) Caught then Finally
B) Finally only
C) Compilation error
D) Caught only

**Answer:** A

### Q53: In a try-with-resources with multiple resources, if the first resource's close method throws an exception, what happens to the second resource?
A) It is not closed
B) It is still closed, and its exception is added as suppressed
C) The program terminates
D) The exception is ignored

**Answer:** B

### Q54: What is the purpose of the `initCause()` method?
A) To set the cause of an exception after construction
B) To initialize the exception message
C) To create a new exception
D) To throw the exception

**Answer:** A

### Q55: When should you use `Error` instead of `Exception`?
A) For recoverable conditions
B) For programming errors that should not be caught
C) For all exceptions
D) Never

**Answer:** B
