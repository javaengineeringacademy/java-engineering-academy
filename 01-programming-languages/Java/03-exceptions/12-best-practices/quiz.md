# Quiz: Exception Best Practices

## Questions

### Q1: Which of the following is the preferred way to handle a missing required argument?
**Answer:** B) `throw new IllegalArgumentException("Missing argument: " + name)` — IllegalArgumentException is the standard unchecked exception for invalid arguments.

### Q2: Why should you chain exceptions?
**Answer:** B) To preserve the original cause for debugging — Chaining preserves the full causal chain, making root-cause analysis possible.

### Q3: What is wrong with this code?
```java
try {
    processOrder(order);
} catch (Exception e) {
    // handled
}
```
**Answer:** B) The exception is swallowed — no logging, no rethrowing — At minimum, log it. Ideally, rethrow or chain it.

### Q4: When is it acceptable to catch `Throwable`?
**Answer:** C) At infrastructure entry points (servlets, schedulers) to prevent process crashes — Infrastructure boundaries need a safety net.

### Q5: Should file-not-found be a checked or unchecked exception?
**Answer:** B) Checked — it's an external failure the caller can recover from — The caller can meaningfully recover (use default, prompt user, etc.).

### Q6: What is the primary reason not to use exceptions for control flow?
**Answer:** B) It's significantly slower and obscures intent — Exception handling is orders of magnitude slower than a conditional check.

### Q7: Which practice is recommended for resource management?
**Answer:** C) try-with-resources for AutoCloseable types — It is guaranteed correct, handles multiple resources, and is the idiomatic approach.

### Q8: What should a well-written exception message include?
**Answer:** B) Enough context to diagnose the problem without reading the code — The message should describe what went wrong, including relevant values.

### Q9: You have a custom exception hierarchy. Where should HTTP status codes be mapped?
**Answer:** B) At the API boundary (controller, global handler), not in business logic — HTTP status codes are presentation concerns.

### Q10: What is the purpose of a domain exception's error code field?
**Answer:** B) To provide a machine-readable identifier for API responses and logging — Error codes allow API consumers and monitoring systems to programmatically identify error types.

### Q11: Why should you not catch generic `Exception` in application code?
**Answer:** It can mask specific errors and make debugging difficult; always catch specific exceptions when possible.

### Q12: What happens when you throw an exception without a message?
**Answer:** The exception is still valid, but it provides no context for debugging.

### Q13: Why is it important to log exceptions before rethrowing them?
**Answer:** It ensures the exception is recorded even if the rethrow is caught and handled elsewhere.

### Q14: What is the benefit of using custom exceptions over standard ones?
**Answer:** Custom exceptions provide domain-specific context and can carry additional data for better error handling.