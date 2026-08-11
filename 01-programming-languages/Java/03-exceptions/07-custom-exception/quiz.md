# Quiz: Custom Exceptions

## Questions

### Q1: What is the recommended base class for a custom exception that represents a programming error?
**Answer:** C) `RuntimeException` — Programming errors should be unchecked; `Exception` is for recoverable errors.

### Q2: Which naming convention is correct for custom exceptions?
**Answer:** C) `PaymentDeclinedException` — Always suffix with `Exception`.

### Q3: Which constructors should a custom exception always provide?
**Answer:** B) Message, message+cause, and cause-only — This covers all standard cases for exception chaining.

### Q4: What happens if you don't pass the original exception to the `super` constructor?
**Answer:** B) The original cause is lost — This makes debugging harder because the causal chain is broken.

### Q5: Which scenario BEST warrants a custom exception?
**Answer:** B) A user account is not found — Domain-specific errors benefit from custom exceptions; standard exceptions work for the others.

### Q6: When is `serialVersionUID` required on a custom exception?
**Answer:** A) Always, for all exceptions — Best practice is to include it for all serializable exceptions.

### Q7: What is the primary benefit of including error codes in custom exceptions?
**Answer:** C) Programmatic error handling and monitoring — Error codes enable programmatic handling and log monitoring.

### Q8: For a large enterprise application, what is the recommended maximum hierarchy depth?
**Answer:** C) 3 levels — Keeps hierarchies manageable while allowing necessary abstraction.

### Q9: Why use an exception factory (static methods) instead of constructors directly?
**Answer:** B) Factories hide constructor complexity and improve readability — They can also provide default values.

### Q10: What is wrong with using exceptions for normal flow control?
**Answer:** B) It reduces performance and readability — Exception handling is significantly slower than conditional checks.

### Q11: What is the difference between checked and unchecked custom exceptions?
**Answer:** Checked custom exceptions extend `Exception` and must be declared, while unchecked ones extend `RuntimeException` and do not require explicit handling.

### Q12: When should you create a custom exception versus using a standard one?
**Answer:** When the error has specific meaning in your domain and you need to carry domain-specific data.

### Q13: Why should custom exceptions not use generic `Exception` as a base?
**Answer:** It makes the exception too broad, making it difficult to catch specific error conditions.

### Q14: What happens when a custom exception is thrown and not caught?
**Answer:** The JVM terminates the thread with a stack trace, unless caught by a higher-level handler.