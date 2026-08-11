# Quiz: Exception Chaining

## Questions

### Q1: What is exception chaining?
**Answer:** B) Wrapping one exception inside another to preserve the cause — This maintains the causal chain for debugging.

### Q2: Which constructor is used for exception chaining?
**Answer:** C) `new Exception(String message, Throwable cause)` — This constructor allows you to specify both a message and the original cause.

### Q3: What does `getCause()` return?
**Answer:** C) The cause exception (may be null) — It returns the exception that caused this one.

### Q4: What happens if you call `initCause()` twice?
**Answer:** C) An `IllegalStateException` is thrown — You can only set the cause once.

### Q5: What is the exception translation pattern?
**Answer:** B) Catching a low-level exception and wrapping it in a higher-level exception — This provides meaningful context to callers.

### Q6: What is the root cause of an exception?
**Answer:** C) The exception that originally triggered the chain — It's the first exception in the chain.

### Q7: How do you print the full exception chain?
**Answer:** C) `exception.printStackTrace()` — This prints the full stack trace including all causes.

### Q8: Which of the following is a common pitfall?
**Answer:** B) Creating circular cause chains — This can cause infinite loops when traversing the chain.

### Q9: When should you wrap an exception?
**Answer:** B) When the exception crosses a layer boundary — This provides appropriate context for each layer.

### Q10: What is the purpose of exception chaining?
**Answer:** B) To preserve the causal chain of exceptions — This enables root-cause analysis.

### Q11: Which exception type should be used in the service layer?
**Answer:** C) `ServiceException` — It provides domain-specific context for the service layer.

### Q12: How do you avoid losing the root cause when wrapping exceptions?
**Answer:** B) Pass the original exception as the cause when creating the new exception — This preserves the chain.

### Q13: What is double wrapping?
**Answer:** B) Wrapping an exception that is already wrapped — This can obscure the original cause.

### Q14: Which method is used to traverse the cause chain?
**Answer:** B) `getCause()` — It returns the exception that caused the current one.

### Q15: What is the recommended approach for exception chaining?
**Answer:** B) Always use constructors over `initCause()` — Constructors are more concise and less error-prone.