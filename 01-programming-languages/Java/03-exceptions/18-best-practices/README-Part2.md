# 18 - Exception Handling Best Practices (Part 2)

[← Part 1](README-Part1.md) | [Part 3 →](README-Part3.md)

---

| Situation                | What to Log                                      | Level     |
|--------------------------|--------------------------------------------------|-----------|
| Exception caught         | Message + context + stack trace                  | ERROR     |
| Retry attempt            | Attempt number, delay, original exception        | WARN      |
| Validation failure       | Field name, value (safe), rule violated          | WARN      |
| Resource cleanup failure | Resource type, cleanup action, exception         | WARN      |
| Transient failure        | Operation name, retryable flag, exception        | WARN      |
| System state at error    | Relevant IDs, status, parameters                 | ERROR     |
| Fallback used            | Primary failure reason, fallback source          | INFO      |

### What NOT to Log

| What                | Why                                        | Alternative                              |
|---------------------|--------------------------------------------|------------------------------------------|
| Passwords           | Security / compliance (PCI, GDPR)          | Log "password provided" only             |
| Full credit cards   | PCI DSS violation                          | Log last 4 digits only                   |
| SSN / national ID   | Identity theft risk                        | Log partial or none                      |
| API keys / tokens   | Unauthorized access risk                   | Log key prefix / last 4 chars            |
| Full stack traces   | Performance / storage overhead             | Log at ERROR level only                  |
| SQL with parameters | SQL injection risk in logs                 | Log query template, bind separately      |
| PII in messages     | Privacy regulation violations              | Use anonymized identifiers               |

### Logging with Exceptions: Code Examples

```java
// BAD: Exception swallowed, no stack trace
try {
    processOrder(order);
} catch (Exception e) {
    log.error("Order processing failed");  // No context, no stack trace!
}

// BAD: Stack trace logged as string (loses searchability)
try {
    processOrder(order);
} catch (Exception e) {
    log.error("Order failed: " + e.getMessage());  // No stack trace
}

// BAD: Exception logged twice (once in catch, once by framework)
try {
    processOrder(order);
} catch (Exception e) {
    log.error("Order failed", e);  // If framework also logs, duplicate!
    throw e;
}

// GOOD: Log once, with context, pass exception to logging framework
try {
    processOrder(order);
} catch (OrderProcessingException e) {
    log.error("Failed to process order ORD-{} for customer CUST-{}: {}",
        order.getId(), order.getCustomerId(), e.getMessage(), e);
    throw new ServiceException("Order processing failed", e);
}

// GOOD: Use structured logging for searchability
try {
    paymentGateway.charge(request);
} catch (CardDeclinedException e) {
    log.error("Payment declined: orderId={}, cardLast4={}, amount={}, reason={}",
        request.getOrderId(),
        request.getCardLast4(),
        request.getAmount(),
        e.getDeclineReason(),
        e);
}
```

### Logging Decision Tree

```
  Exception occurs
       |
       v
  +--- Is it expected (business rule)? ---+
  |                                       |
  YES                                     NO
  |                                       |
  v                                       v
  Log at WARN level               +--- Is it transient? ---+
  Include business context        |                        |
  No stack trace needed           YES                      NO
  Example: "Insufficient funds"   |                        |
                                  v                        v
                            Retry available?          Log at ERROR level
                            |            |           Include full context
                           YES           NO          Include stack trace
                            |            |          Include all safe params
                            v            v
                      Log at INFO   Log at WARN
                      "Retrying"    "Non-retryable"
```

---

## 6. Exception Handling Anti-Patterns

### Anti-Pattern 1: Swallowing Exceptions

```java
// BAD: Exception silently swallowed
public void updateUserProfile(User user) {
    try {
        userRepository.save(user);
        emailService.sendConfirmation(user);
    } catch (Exception e) {
        // Nothing here -- exception disappears!
    }
}

// GOOD: Re-throw or handle with awareness
public void updateUserProfile(User user) {
    try {
        userRepository.save(user);
    } catch (DataAccessException e) {
        throw new UserServiceException(
            "Failed to save profile for user: " + user.getId(), e);
    }

    try {
        emailService.sendConfirmation(user);
    } catch (EmailException e) {
        log.warn("Confirmation email failed for user {}: {}",
            user.getId(), e.getMessage());
        // Profile saved, email failure is non-critical
    }
}
```

### Anti-Pattern 2: Catching Too Broad

```java
// BAD: Catches everything including programming errors
public void processOrder(Order order) {
    try {
        validate(order);
        paymentService.charge(order);
        inventoryService.reserve(order);
        shippingService.schedule(order);
    } catch (Exception e) {
        throw new OrderException("Processing failed", e);
    }
}

// GOOD: Catch specific exceptions you can handle
public void processOrder(Order order) {
    validate(order);  // Throws IllegalArgumentException -- propagates naturally

    try {
        paymentService.charge(order);
    } catch (PaymentDeclinedException e) {
        throw new OrderException(
            "Payment failed for order: " + order.getId(), e);
    }

    try {
        inventoryService.reserve(order);
    } catch (InsufficientStockException e) {
        paymentService.refund(order);  // Compensating transaction
        throw new OrderException(
            "Insufficient stock for order: " + order.getId(), e);
    }

    try {
        shippingService.schedule(order);
    } catch (ShippingException e) {
        log.warn("Shipping scheduling failed, order saved: {}", order.getId());
        // Non-critical -- order is saved, shipping can be retried
    }
}
```

### Anti-Pattern 3: Exception in Finally Block

```java
// BAD: Exception in finally masks original exception
public byte[] readFile(String path) {
    InputStream is = null;
    try {
        is = new FileInputStream(path);
        return is.readAllBytes();
    } finally {
        if (is != null) {
            is.close();  // If this throws, original exception is LOST
        }
    }
}

// GOOD: Safe finally block
public byte[] readFile(String path) {
    try (InputStream is = new FileInputStream(path)) {
        return is.readAllBytes();
    }
    // No finally needed -- try-with-resources handles it safely
}

// ALTERNATIVE: If you must use finally manually
public byte[] readFileManual(String path) {
    InputStream is = null;
    try {
        is = new FileInputStream(path);
        return is.readAllBytes();
    } finally {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                log.warn("Failed to close input stream for: {}", path, e);
                // Original exception is NOT masked
            }
        }
    }
}
```

### Anti-Pattern 4: Return null in Catch Block

```java
// BAD: Returning null hides the error
public User findUser(String id) {
    try {
        return httpClient.get("/users/" + id, User.class);
    } catch (Exception e) {
        log.error("Find user failed");
        return null;  // Caller will NPE later with no context!
    }
}

// Caller then does:
User user = findUser("USR-001");
String name = user.getName();  // NullPointerException! Where? Why?

// GOOD: Throw or return Optional
public Optional<User> findUser(String id) {
    try {
        return Optional.ofNullable(
            httpClient.get("/users/" + id, User.class));
    } catch (IOException e) {
        throw new UserServiceException(
            "Failed to fetch user: " + id, e);
    }
}
```

### Anti-Pattern 5: Catching and Rethrowing Unchanged

```java
// BAD: Pointless catch -- just adds noise
public void saveData(Data data) {
    try {
        repository.save(data);
    } catch (DataAccessException e) {
        throw e;  // No added value -- why catch at all?
    }
}

// GOOD: Either add context or don't catch
// Option A: Add context
public void saveData(Data data) {
    try {
        repository.save(data);
    } catch (DataAccessException e) {
        throw new DataException(
            "Failed to save data entity: " + data.getId(), e);
    }
}

// Option B: Don't catch -- let it propagate
public void saveData(Data data) {
    repository.save(data);  // DataAccessException propagates naturally
}
```

### Anti-Pattern Summary Table

| Anti-Pattern                       | Problem                              | Fix                                   |
|------------------------------------|--------------------------------------|---------------------------------------|
| Swallowing exceptions              | Bugs hide, data corrupts silently    | Re-throw or log and handle            |
| Catching `Exception` or `Throwable`| Programming errors hidden            | Catch specific exceptions             |
| Exception in finally block         | Original exception lost              | Use try-with-resources                |
| Return null in catch               | NPE at distant call site             | Throw exception or return Optional    |
| Catch and rethrow unchanged        | Code noise, no added value           | Add context or remove catch           |
| Empty catch block                  | Silent failure, impossible to debug  | At minimum, log the exception         |
| Logging and rethrowing             | Duplicate log entries                | Log once OR rethrow, not both         |
| Catching checked as unchecked      | Wrapping in RuntimeException loses   | Preserve exception type               |
|                                    | recoverable semantics                |                                       |

---

## Key Takeaways

1. **Choose checked vs unchecked** based on whether the caller can recover
2. **Name exceptions precisely** -- the name should explain the failure without reading the message
3. **Write messages that include** action, entity ID, and safe context values
4. **Never log sensitive data** -- mask cards, omit passwords and tokens
5. **Log at the right level** -- ERROR for failures requiring attention, WARN for expected/recoverable
6. **Pass the exception object** to the logging framework so stack traces are captured
7. **Avoid swallowing exceptions** -- bugs that are caught and ignored become the hardest to find
8. **Don't catch too broad** -- catch only what you can handle meaningfully
9. **Never return null** to hide an error -- throw an exception or return Optional
10. **Use try-with-resources** to avoid exceptions in finally blocks masking originals

---

[Continue to Part 2 ->](README-Part2.md)
