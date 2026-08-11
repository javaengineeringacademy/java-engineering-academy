# Quiz: Production Exception Patterns

## Questions

### Q1: What annotation creates a centralized exception handler in Spring Boot?
**Answer:** `@RestControllerAdvice` — it combines `@ControllerAdvice` and `@ResponseBody`.

### Q2: Which HTTP status code indicates a resource was not found?
**Answer:** 404 Not Found.

### Q3: What is the primary purpose of a circuit breaker?
**Answer:** Prevent cascading failures by stopping requests to failing dependencies.

### Q4: Which exception should NOT be retried?
**Answer:** `ValidationException` — it indicates invalid input; retrying won't fix it.

### Q5: What field should every structured error response include?
**Answer:** `traceId` — it enables correlating logs across distributed systems.

### Q6: What is the correct logging approach for exceptions?
**Answer:** `log.error("message", ex)` — pass exception as last argument to preserve stack trace.

### Q7: Which tool provides distributed tracing for exception monitoring?
**Answer:** DataDog provides distributed tracing and exception monitoring.

### Q8: What does the circuit breaker HALF_OPEN state indicate?
**Answer:** Service is testing recovery — the circuit breaker allows test requests to check if the dependency has recovered.

### Q9: What is graceful degradation?
**Answer:** Providing reduced functionality when a dependency fails, instead of crashing completely.

### Q10: Why are correlation IDs important?
**Answer:** They trace requests across multiple services, linking related log entries for debugging.

### Q11: What happens when you log only `ex.getMessage()` instead of the full exception?
**Answer:** You lose the stack trace, which is critical for diagnosing where and why the error occurred.

### Q12: Why should exceptions never be logged and rethrown in the same place?
**Answer:** It creates duplicate log entries for the same error, making it harder to trace and analyze.

### Q13: What is the difference between a circuit breaker and a retry pattern?
**Answer:** A retry pattern attempts the same operation multiple times. A circuit breaker stops trying after repeated failures to prevent overloading a failing service.

### Q14: Why is it important to include the exception object in structured error responses?
**Answer:** It enables downstream consumers and monitoring tools to categorize and analyze errors programmatically, rather than parsing free-text messages.

### Q15: Why should you use custom exception classes instead of generic ones?
**Answer:** Custom exceptions provide meaningful names that make error handling clearer, allow targeted catch blocks, and improve code readability.
