# 18 - Exception Handling Best Practices (Part 4)

[← Part 3](README-Part3.md)

---

        OrderProcessingException.class,
        () -> orderService.processOrder(order));

    assertEquals("ORD-001", ex.getOrderId());
    assertTrue(ex.getCause() instanceof InsufficientStockException);
}

// TEST: Verify suppressed exceptions are accessible
@Test
void shouldPreserveSuppressedExceptions() {
    try (MockCloseable resource = new MockCloseable()) {
        resource.setCloseException(new IOException("close failed"));
        assertThrows(IOException.class, resource::execute);
    } catch (Exception e) {
        assertEquals("primary error", e.getMessage());
        assertEquals(1, e.getSuppressed().length);
        assertEquals("close failed",
            e.getSuppressed()[0].getMessage());
    }
}
```

### Testing Scenarios Table

| Scenario                          | Test Assertion                                  |
|-----------------------------------|-------------------------------------------------|
| Happy path                        | No exception thrown, correct return value       |
| Null input                        | NullPointerException or IllegalArgumentException |
| Invalid value                     | Specific exception with descriptive message     |
| Resource unavailable              | Appropriate exception, resource cleaned up      |
| Timeout                           | TimeoutException, retry logic triggered         |
| Partial failure                   | Exception propagated, partial work not committed|
| Concurrent access                 | No data corruption, appropriate locking error   |
| Exception chain                   | Root cause preserved in cause chain             |
| Suppressed exceptions             | Both primary and suppressed accessible          |
| Recovery path                     | Fallback executes, exception logged at WARN     |

---

## 10. Code Review Checklist

Use this checklist during code reviews for any code that handles exceptions.

### Exception Selection

- [ ] Checked exceptions used only when caller can recover
- [ ] Unchecked exceptions used for programming errors
- [ ] Each exception type is specific (no generic `Exception` catches)
- [ ] Custom exceptions extend appropriate base class
- [ ] Exception hierarchy is flat (no more than 3 levels deep)

### Exception Messages

- [ ] Messages start with a verb describing the failed action
- [ ] Messages include entity IDs or safe identifiers
- [ ] Messages do NOT contain passwords, tokens, or full card numbers
- [ ] Messages are under 200 characters
- [ ] Messages use plain language, no cryptic codes

### Exception Handling

- [ ] No empty catch blocks
- [ ] No catch blocks that only log and do nothing else
- [ ] Exceptions not swallowed (lost silently)
- [ ] No `catch (Exception e)` or `catch (Throwable t)` without good reason
- [ ] finally blocks do not throw exceptions
- [ ] try-with-resources used for AutoCloseable resources
- [ ] Resources cleaned up in correct order

### Logging

- [ ] Exception object passed to logger (not just message string)
- [ ] Log level appropriate (ERROR vs WARN vs INFO)
- [ ] Context included in log message (IDs, parameters)
- [ ] No duplicate logging (log once OR rethrow, not both)
- [ ] No sensitive data in log messages

### Exception Translation

- [ ] Low-level exceptions wrapped in domain exceptions
- [ ] Original exception passed as `cause` parameter
- [ ] Domain-specific exception types used at API boundaries
- [ ] One level of translation per layer

### Testing

- [ ] Happy path tested (no exception)
- [ ] Expected exceptions tested with correct type and message
- [ ] Edge cases tested (null, empty, boundary values)
- [ ] Exception chain verified (cause is correct)
- [ ] Recovery/fallback paths tested

---

## 11. Production Checklist

### Pre-Deployment Checklist

| Item                                                  | Owner     | Done |
|-------------------------------------------------------|-----------|------|
| All custom exceptions have descriptive messages       | Dev       | [ ]  |
| No sensitive data in exception messages               | Dev       | [ ]  |
| Logging configured for all exception levels           | Dev       | [ ]  |
| Alerting set up for ERROR-level exceptions            | DevOps    | [ ]  |
| Circuit breakers configured for external calls        | DevOps    | [ ]  |
| Retry policies defined for transient failures         | Dev       | [ ]  |
| Resource limits (connection pools, thread pools) set  | DevOps    | [ ]  |
| Exception monitoring dashboard deployed               | DevOps    | [ ]  |
| Runbooks exist for common exception scenarios         | Ops       | [ ]  |
| Error budget / SLO defined for error rates            | SRE       | [ ]  |

### Monitoring Dashboard Diagram

```
  +------------------------------------------------------------------+
  |                Exception Monitoring Dashboard                     |
  +------------------------------------------------------------------+
  |                                                                  |
  |  Error Rate (5 min)          Exceptions by Type                  |
  |  +------------------+        +------------------+                |
  |  |    0.3% (OK)     |        | ServiceException | 45%            |
  |  |  [=========  ]   |        | PaymentException | 30%            |
  |  |   < 1% threshold |        | ValidationExc    | 15%            |
  |  +------------------+        | Other            | 10%            |
  |                              +------------------+                |
  |                                                                  |
  |  Errors Over Time             Top Affected Endpoints             |
  |  +------------------+        +------------------+                |
  |  |   /\    /\       |        | /api/payments    | 120 errors    |
  |  |  /  \  /  \      |        | /api/orders      |  85 errors    |
  |  | /    \/    \     |        | /api/users       |  30 errors    |
  |  |/            \    |        | /api/inventory   |  10 errors    |
  |  +------------------+        +------------------+                |
  |                                                                  |
  +------------------------------------------------------------------+
```

### Alert Thresholds

| Metric                | Warning    | Critical   | Action                           |
|-----------------------|------------|------------|----------------------------------|
| Error rate (5 min)    | > 1%       | > 5%       | Page on-call engineer            |
| Error rate (1 hour)   | > 0.5%     | > 2%       | Create incident ticket           |
| New exception type    | N/A        | Any        | Log and investigate              |
| Exception spike       | 2x normal  | 5x normal  | Auto-scaling + alert             |
| Exception latency     | > 500ms    | > 2000ms   | Check downstream services        |
| P99 latency           | > 1s       | > 5s       | Check resource saturation        |

---

## 12. Common Code Review Comments

| Review Comment                                    | Code Issue                                    | Suggested Fix                                            |
|---------------------------------------------------|-----------------------------------------------|----------------------------------------------------------|
| "Why are you catching Exception?"                 | `catch (Exception e)` hides bugs              | Catch specific: `catch (IOException e)`                  |
| "This catch block is empty"                       | Silent failure, undetectable bugs             | At minimum, log: `log.warn("...", e)`                    |
| "This is swallowed"                               | Exception caught and ignored                 | Re-throw or handle with compensating action              |
| "You're losing the stack trace"                   | `new Exception(original.getMessage())`        | Pass cause: `new Exception(msg, original)`               |
| "Don't log and rethrow"                           | Duplicate log entries                        | Pick one: log once OR rethrow, not both                  |
| "Use try-with-resources"                          | Manual close() in finally                    | Use `try (Resource r = ...)` for AutoCloseable           |
| "This message is too vague"                       | `throw new Exception("error")`               | Add context: `throw new Exception("Failed to...")`       |
| "Sensitive data in exception message"             | Passwords, cards, tokens in message          | Mask data: `card ending in ****4242`                     |
| "Why return null here?"                           | Forces null checks, hides failures           | Return Optional or throw exception                       |
| "This exception type is too broad"               | Custom exception with no specifics           | Create specific types per failure mode                   |
| "finally block can throw"                         | close() exception masks original             | Wrap in try-catch inside finally                         |
| "Missing cause in translation"                    | `new DomainException(msg)`                   | Add cause: `new DomainException(msg, originalException)` |
| "Log level seems wrong"                           | ERROR for expected validation failure        | Use WARN for expected, ERROR for unexpected               |
| "This should be unchecked"                        | Checked exception for programming error      | Use RuntimeException subclass                            |
| "Missing null check"                              | NPE risk before using parameter              | Add `Objects.requireNonNull(param, "message")`           |

### Example Fixes

```java
// BEFORE: Problem code
public void saveUser(User user) {
    try {
        repository.save(user);
    } catch (Exception e) {
        System.out.println("error");  // Review comment: log + stack trace lost
        return null;                   // Review comment: return null
    }
}

// AFTER: Fixed code
public void saveUser(User user) {
    Objects.requireNonNull(user, "User must not be null");
    try {
        repository.save(user);
    } catch (DataAccessException e) {
        throw new UserServiceException(
            "Failed to save user: " + user.getId(), e);
    }
}
```

---

## 13. Summary Table

| Practice                      | Rule                                         | Priority |
|-------------------------------|----------------------------------------------|----------|
| Exception selection           | Checked = recoverable, Unchecked = bug       | Critical |
| Naming                        | Specific, past-tense, no generic prefixes    | High     |
| Messages                      | Verb + entity ID + safe context, no secrets  | Critical |
| Logging                       | Pass exception object, appropriate level     | High     |
| No swallowing                 | Always handle or propagate                   | Critical |
| No broad catches              | Catch only what you can handle               | High     |
| Resource cleanup              | try-with-resources for AutoCloseable         | High     |
| Exception translation         | Wrap low-level, preserve cause               | Medium   |
| Testing                       | Assert type, message, and cause chain        | High     |
| Code review                   | Use checklists, never approve empty catches  | Medium   |
| Monitoring                    | Alert on error rate spikes, track new types  | High     |
| Security                      | Never log passwords, cards, or tokens        | Critical |

---

## 14. Version History

| Version | Date       | Changes                                         |
|---------|------------|-------------------------------------------------|
| 1.5     | 2026-01-15 | Initial comprehensive best practices guide      |
| 1.4     | 2025-10-01 | Added exception translation patterns            |
| 1.3     | 2025-06-15 | Added production monitoring checklist           |
| 1.2     | 2025-03-01 | Expanded testing section with scenarios         |
| 1.1     | 2024-11-15 | Added code review checklist and common comments |
| 1.0     | 2024-08-01 | Initial best practices document                 |

---

## Key Takeaways

1. **Select exceptions intentionally** -- checked for recoverable, unchecked for bugs
2. **Name exceptions precisely** -- the name should be self-documenting
3. **Write messages with context** -- what failed, which entity, why (no secrets)
4. **Log once with the exception object** -- never log AND rethrow
5. **Never swallow exceptions** -- propagate or handle with compensating action
6. **Use try-with-resources** -- clean up resources safely without finally risks
7. **Translate low-level exceptions** -- wrap and preserve the cause
8. **Test exception paths** -- assert type, message content, and cause chain
9. **Review for anti-patterns** -- empty catches, broad catches, return null
10. **Monitor in production** -- alert on error rates, track new exception types

---

[Part 1](README-Part1.md)
