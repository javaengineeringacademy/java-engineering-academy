# Quiz: Exception Handling Best Practices

> Test your understanding of exception handling best practices.

---

## Questions

### Q1: What is the primary problem with catching generic `Exception` instead of specific exception types?
A) It causes a compilation error
B) It masks the specific failure type and makes debugging harder
C) It has worse performance than catching specific exceptions
D) It cannot be caught at runtime

**Answer:** B

### Q2: Which of the following is an anti-pattern in exception handling?
A) Catching `IOException` after `FileNotFoundException`
B) Using try-with-resources for `AutoCloseable` objects
C) Empty catch blocks
D) Logging exceptions with context

**Answer:** C

### Q3: What happens if an exception is thrown in a `finally` block?
A) The original exception is automatically retried
B) The original exception is lost if the finally exception is not caught
C) Both exceptions are thrown simultaneously
D) The finally exception is ignored

**Answer:** B

### Q4: Why should you avoid returning `null` in a catch block?
A) It causes a `NullPointerException` immediately
B) It defers the error to a later point, making debugging harder
C) It violates the Single Responsibility Principle
D) It cannot be caught by callers

**Answer:** B

### Q5: What is the correct order for cleaning up multiple resources in try-with-resources?
A) In declaration order (first declared, first closed)
B) In reverse declaration order (last declared, first closed)
C) In alphabetical order by variable name
D) Order doesn't matter

**Answer:** B

### Q6: When should you use checked exceptions instead of unchecked exceptions?
A) For all exceptions
B) When the caller can reasonably be expected to recover
C) For programming errors like `NullPointerException`
D) Never, checked exceptions are deprecated

**Answer:** B

### Q7: Which exception message is most helpful for debugging?
A) `"Error"`
B) `"Invalid value"`
C) `"OrderService.calculateTotal: Invalid quantity: -1"`
D) `"Something went wrong"`

**Answer:** C

### Q8: What is the purpose of exception translation?
A) To convert all exceptions to `RuntimeException`
B) To hide all exceptions from callers
C) To wrap low-level exceptions in domain-specific exceptions
D) To automatically retry failed operations

**Answer:** C

### Q9: What log level should be used for expected business rule violations?
A) ERROR
B) WARN
C) INFO
D) DEBUG

**Answer:** B

### Q10: Which practice should be avoided when logging exceptions?
A) Including the request ID
B) Logging the exception object for stack trace
C) Logging sensitive data like passwords
D) Using parameterized logging

**Answer:** C

### Q11: What is the primary benefit of using custom exceptions instead of generic ones?
A) They have better performance
B) They allow callers to handle different failure types appropriately
C) They automatically log themselves
D) They are required by the Java compiler

**Answer:** B

### Q12: When is it appropriate to catch and ignore an exception?
A) When you don't know how to handle it
B) When the operation is truly optional and failure has no impact
C) When the code is too complex to handle it
D) Never, all exceptions should be handled or rethrown

**Answer:** B

### Q13: What is the purpose of the `cause` parameter in exception constructors?
A) To create a new exception type
B) To preserve the original exception's stack trace and context
C) To automatically log the exception
D) To retry the failed operation

**Answer:** B

### Q14: Which pattern is most appropriate for handling transient failures in external services?
A) Catch and ignore
B) Retry with exponential backoff
C) Return null
D) Throw a checked exception

**Answer:** B

### Q15: What should a finally block contain?
A) Business logic that might fail
B) Resource cleanup code (closing connections, files, etc.)
C) Exception handling for other try blocks
D) Logging statements for debugging

**Answer:** B

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
