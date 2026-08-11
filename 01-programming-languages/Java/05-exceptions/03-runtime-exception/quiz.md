# Quiz: RuntimeException

Test your understanding of `RuntimeException` with these questions.

## Questions

1. What class does `RuntimeException` extend?
2. Is `RuntimeException` checked or unchecked?
3. Does the compiler require you to catch `RuntimeException`?
4. What does a `RuntimeException` typically represent?
5. Name the exception thrown when you call a method on a `null` reference.
6. Name the exception thrown when you access an array with an index that is too large.
7. What exception should you throw when a method receives a null argument and null is not allowed?
8. What exception should you throw when a method is called on an object that is not in the correct state?
9. What is the difference between `IllegalArgumentException` and `IllegalStateException`?
10. Name the exception thrown when you try to cast an object to an incompatible type.
11. What exception is thrown when a `for` loop modifies a list while iterating over it?
12. Why should you not use `RuntimeException` for control flow?
13. What is a better alternative to catching `RuntimeException` broadly?
14. Is `StackOverflowError` a `RuntimeException`?
15. What is the recommended way to handle `RuntimeException` in a production application?
16. What is the difference between `RuntimeException` and `Error`?
17. Name a way to safely check for null before dereferencing, avoiding `NullPointerException`.
18. What is the cost of creating a `RuntimeException` with a full stack trace?
19. When should you create a custom `RuntimeException` subtype?
20. What method can you use to chain one exception as the cause of another?

## Answers

1. `Exception`.
2. Unchecked.
3. No.
4. A programming bug.
5. `NullPointerException`.
6. `ArrayIndexOutOfBoundsException`.
7. `IllegalArgumentException`.
8. `IllegalStateException`.
9. `IllegalArgumentException` is for bad arguments; `IllegalStateException` is for bad object state.
10. `ClassCastException`.
11. `ConcurrentModificationException`.
12. It hides bugs, uses exceptions for logic, and is slower than explicit checks.
13. Catch specific subtypes, or let the exception propagate to a global handler.
14. No, it is an `Error`, which extends `Throwable` directly (not `Exception`).
15. Register a global `UncaughtExceptionHandler` to log and report the exception.
16. `RuntimeException` represents application programming bugs; `Error` represents JVM-level failures that are usually unrecoverable.
17. Use `Objects.requireNonNull()` or an explicit `if (obj == null)` check before using the object.
18. Filling in the stack trace is expensive; in tight loops, this can cause significant performance degradation.
19. When the exception represents a domain-specific programming error that is not covered by the standard `RuntimeException` subtypes.
20. Use the constructor that accepts a `Throwable` cause, or call `initCause()` on the exception.

## Self-Test

Try answering each question before looking at the answers. If you cannot answer a question confidently, review the corresponding section in the README.

When you are ready, verify your understanding by writing the code examples from each section of the README from memory.

## Code Challenges

Write the following code from memory, then compare with the examples in the README:

1. A method that validates a port number (1-65535) and throws `IllegalArgumentException` if invalid.
2. A connection class that throws `IllegalStateException` if you try to close an already-closed connection.
3. A safe list accessor that throws `IndexOutOfBoundsException` with a descriptive message.
4. A method that chains a checked exception into an unchecked `RuntimeException`.
