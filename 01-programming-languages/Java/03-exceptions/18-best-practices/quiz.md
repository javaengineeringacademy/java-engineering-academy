# Quiz: Exception Handling Best Practices

> Test your understanding of exception handling best practices.

---

## Questions

### Question 1

What is the primary problem with catching generic `Exception` instead of specific exception types?

- A) It causes a compilation error
- B) It masks the specific failure type and makes debugging harder
- C) It has worse performance than catching specific exceptions
- D) It cannot be caught at runtime

---

### Question 2

Which of the following is an anti-pattern in exception handling?

- A) Catching `IOException` after `FileNotFoundException`
- B) Using try-with-resources for `AutoCloseable` objects
- C) Empty catch blocks
- D) Logging exceptions with context

---

### Question 3

What happens if an exception is thrown in a `finally` block?

- A) The original exception is automatically retried
- B) The original exception is lost if the finally exception is not caught
- C) Both exceptions are thrown simultaneously
- D) The finally exception is ignored

---

### Question 4

Why should you avoid returning `null` in a catch block?

- A) It causes a `NullPointerException` immediately
- B) It defers the error to a later point, making debugging harder
- C) It violates the Single Responsibility Principle
- D) It cannot be caught by callers

---

### Question 5

What is the correct order for cleaning up multiple resources in try-with-resources?

- A) In declaration order (first declared, first closed)
- B) In reverse declaration order (last declared, first closed)
- C) In alphabetical order by variable name
- D) Order doesn't matter

---

### Question 6

When should you use checked exceptions instead of unchecked exceptions?

- A) For all exceptions
- B) When the caller can reasonably be expected to recover
- C) For programming errors like `NullPointerException`
- D) Never, checked exceptions are deprecated

---

### Question 7

Which exception message is most helpful for debugging?

- A) `"Error"`
- B) `"Invalid value"`
- C) `"OrderService.calculateTotal: Invalid quantity: -1"`
- D) `"Something went wrong"`

---

### Question 8

What is the purpose of exception translation?

- A) To convert all exceptions to `RuntimeException`
- B) To hide all exceptions from callers
- C) To wrap low-level exceptions in domain-specific exceptions
- D) To automatically retry failed operations

---

### Question 9

What log level should be used for expected business rule violations?

- A) ERROR
- B) WARN
- C) INFO
- D) DEBUG

---

### Question 10

Which practice should be avoided when logging exceptions?

- A) Including the request ID
- B) Logging the exception object for stack trace
- C) Logging sensitive data like passwords
- D) Using parameterized logging

---

### Question 11

What is the primary benefit of using custom exceptions instead of generic ones?

- A) They have better performance
- B) They allow callers to handle different failure types appropriately
- C) They automatically log themselves
- D) They are required by the Java compiler

---

### Question 12

When is it appropriate to catch and ignore an exception?

- A) When you don't know how to handle it
- B) When the operation is truly optional and failure has no impact
- C) When the code is too complex to handle it
- D) Never, all exceptions should be handled or rethrown

---

### Question 13

What is the purpose of the `cause` parameter in exception constructors?

- A) To create a new exception type
- B) To preserve the original exception's stack trace and context
- C) To automatically log the exception
- D) To retry the failed operation

---

### Question 14

Which pattern is most appropriate for handling transient failures in external services?

- A) Catch and ignore
- B) Retry with exponential backoff
- C) Return null
- D) Throw a checked exception

---

### Question 15

What should a finally block contain?

- A) Business logic that might fail
- B) Resource cleanup code (closing connections, files, etc.)
- C) Exception handling for other try blocks
- D) Logging statements for debugging

---

## Answer Key

1. **B** - Catching generic `Exception` masks the specific failure type
2. **C** - Empty catch blocks silently swallow exceptions
3. **B** - The finally exception can mask the original exception
4. **B** - Null returns defer errors, making debugging harder
5. **B** - Resources are closed in reverse declaration order
6. **B** - Checked exceptions are for recoverable conditions
7. **C** - Context-rich messages help identify what went wrong
8. **C** - Translation wraps low-level exceptions in domain exceptions
9. **B** - WARN level for expected business rule violations
10. **C** - Never log sensitive data like passwords
11. **B** - Custom exceptions enable appropriate handling
12. **B** - Only ignore when failure has no impact
13. **B** - The cause preserves the original exception's context
14. **B** - Retry with backoff handles transient failures
15. **B** - Finally blocks should contain resource cleanup code

---

## Scoring

| Score | Interpretation |
|-------|----------------|
| 13-15 | Excellent - You have a strong understanding of exception handling best practices |
| 10-12 | Good - Review the areas you missed and study the patterns |
| 7-9 | Fair - Re-read the best practices section and try the exercises |
| 0-6 | Needs Review - Start with the main README and work through examples |

---

*Return to [Main README](../README.md)*
