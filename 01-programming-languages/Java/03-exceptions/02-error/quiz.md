# Quiz: Java Error

## Questions

### Q1: What class does `Error` extend in Java?
**Answer:** `Error` extends `Throwable` directly.

### Q2: Is `Error` a checked or unchecked type?
**Answer:** Unchecked. The compiler does not require `try-catch` or `throws` declarations.

### Q3: Name three subclasses of `VirtualMachineError`.
**Answer:** `OutOfMemoryError`, `StackOverflowError`, `InternalError`, `UnknownError`, `ClassCircularityError`, `ClassFormatError`.

### Q4: What is the difference between `OutOfMemoryError` and `GC overhead limit exceeded`?
**Answer:** `OutOfMemoryError` occurs when the JVM cannot allocate memory. `GC overhead limit exceeded` occurs when the JVM spends more than 98% of time in garbage collection while recovering less than 2% of heap.

### Q5: When does `NoClassDefFoundError` occur?
**Answer:** When the JVM tries to load a class definition at runtime but cannot find the corresponding class file. This typically happens when a dependency is missing from the classpath at runtime.

### Q6: What is the difference between `NoClassDefFoundError` and `ClassNotFoundException`?
**Answer:** `NoClassDefFoundError` is unchecked and thrown by the JVM when it cannot find a class definition at runtime. `ClassNotFoundException` is checked and thrown explicitly by `Class.forName()` or classloader methods when a class is not found.

### Q7: Why is `ThreadDeath` thrown, and is it deprecated?
**Answer:** `ThreadDeath` is thrown when a thread is forcibly stopped using `Thread.stop()`. The `stop()` method is deprecated since JDK 1.2 because it is inherently unsafe.

### Q8: Should application code catch `OutOfMemoryError`? Explain.
**Answer:** No. Application code should not catch `OutOfMemoryError`. The JVM has exhausted memory, and continuing may lead to corrupted state. Only container/framework code should catch it for graceful shutdown.

### Q9: What JVM flag enables heap dumps on `OutOfMemoryError`?
**Answer:** `-XX:+HeapDumpOnOutOfMemoryError`

### Q10: What is `AssertionError` used for?
**Answer:** `AssertionError` is thrown when an `assert` statement evaluates to false. It is used for debugging and enforcing invariants.

### Q11: Name two acceptable scenarios where catching `Error` is justified.
**Answer:** (1) Container/framework code that needs to log the error and shut down gracefully. (2) Cache implementations that can safely evict data and retry after `OutOfMemoryError`.

### Q12: What is the difference between `VirtualMachineError` and `LinkageError`?
**Answer:** `VirtualMachineError` indicates the JVM has broken its contract or exhausted resources. `LinkageError` indicates a class dependency issue during linking — the class exists but cannot be linked due to missing or incompatible dependencies.

### Q13: What happens if a `StackOverflowError` is not caught?
**Answer:** The thread terminates with the error. If it is the main thread, the JVM exits. If it is a non-daemon thread, other threads continue.

### Q14: How does `ExceptionInInitializerError` relate to static initialization?
**Answer:** `ExceptionInInitializerError` is thrown when a static initializer (`static { }` block or static field initialization) throws an exception. It wraps the original exception as the cause.

### Q15: What should you do when an `Error` occurs in production?
**Answer:** Log the error with full context, capture a thread dump and heap dump (if OOM), trigger graceful shutdown, and alert the operations team. Do not attempt to continue execution.