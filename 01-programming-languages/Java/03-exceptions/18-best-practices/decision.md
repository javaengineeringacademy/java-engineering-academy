# Exception Handling Decision Guide

> A systematic approach to exception design decisions.

---

## 1. Exception Selection Decision Tree

### Top-Level Decision Flow

```
                        +---------------------------------+
                        |    Exception Handling Needed?    |
                        +----------------+----------------+
                                         |
                    +--------------------v---------------------+
                    | Is this a programming error or a         |
                    | recoverable failure?                     |
                    +--------------------+---------------------+
                                         |
              +--------------------------+--------------------------+
              |                                                     |
     +--------v--------+                                  +--------v--------+
     | PROGRAMMING ERROR|                                  | RECOVERABLE      |
     | (bug in code)    |                                  | FAILURE          |
     +--------+--------+                                  +--------+--------+
              |                                                     |
     +--------v--------+                                  +--------v--------+
     | Use UNCHECKED    |                                  | Can caller      |
     | exception        |                                  | recover?        |
     +--------+--------+                                  +--------+--------+
              |                                                     |
     +--------v--------+                                  +--------v--------+
     | IllegalArgumentException      |                      | YES -> CHECKED  |
     | IllegalStateException         |                      | NO  -> UNCHECKED|
     | NullPointerException           |                      +--------+--------+
     | UnsupportedOperationException |                               |
     +-------------------+                                +--------v--------+
                                                                | Does caller |
                                                                | know how to |
                                                                | recover?    |
                                                                +--------+----+
                                                                         |
                                                            +------------+------------+
                                                            |                         |
                                                     +------v------+          +-------v------+
                                                     |  YES         |          |  NO          |
                                                     |  CHECKED     |          |  UNCHECKED   |
                                                     |  (explicit)  |          |  (wrap and   |
                                                     +------+-------+          |   rethrow)   |
                                                            |                  +-------+------+
                                                     +------v------+                  |
                                                     | Caller      |          +-------v------+
                                                     | handles or  |          | Wrap in      |
                                                     | delegates    |          | RuntimeException|
                                                     +-------------+          +--------------+
```

### Checked vs Unchecked Quick Reference

```
+-------------------------------------------+-------------------------------------------+
|          CHECKED EXCEPTION                |          UNCHECKED EXCEPTION              |
+-------------------------------------------+-------------------------------------------+
| Caller CAN recover                        | Caller CANNOT recover                     |
| External system failure (network, DB)     | Programming error (NPE, IAE)             |
| Declared in method signature              | Not declared in method signature          |
| Forces caller to handle                   | Caller handles optionally                 |
| Use sparingly                             | Default choice                            |
| Example: IOException, SQLException        | Example: IllegalArgumentException         |
| Framework: try-catch, throws              | Framework: @ExceptionHandler, AOP         |
+-------------------------------------------+-------------------------------------------+
```

---

## 2. Naming Convention Decision Matrix

### Naming Rules Decision Table

```
+------------------+---------------------------+----------------------------------------+
| Rule             | Decision Criterion        | Example                                |
+------------------+---------------------------+----------------------------------------+
| Suffix           | Always end with Exception | PaymentFailedException (not PaymentFail)|
| Tense            | Past tense for events     | ConnectionLostException                 |
|                  | Present tense for state   | InvalidOrderException                   |
| Specificity      | Name the condition        | CardDeclinedException                   |
|                  | not the cause             | (not PaymentGatewayException)          |
| Scope            | Domain prefix if needed   | com.app.order.OrderNotFoundException    |
|                  | No generic prefixes       | (not MyAppOrderNotFoundException)      |
| Hierarchy        | Base + specific subtypes  | RepositoryException                     |
|                  |                           | -> UserNotFoundException               |
|                  |                           | -> DuplicateUserException              |
+------------------+---------------------------+----------------------------------------+
```

### Naming by Failure Type

```
+------------------+---------------------------+----------------------------------------+
| Failure Type     | Pattern                   | Examples                               |
+------------------+---------------------------+----------------------------------------+
| Validation       | Invalid*Exception         | InvalidEmailException                  |
|                  | Missing*Exception         | MissingFieldException                  |
|                  | *ConstraintException      | PasswordConstraintException            |
+------------------+---------------------------+----------------------------------------+
| Not Found        | *NotFoundException        | UserNotFoundException                  |
|                  | *MissingException         | ResourceMissingException               |
+------------------+---------------------------+----------------------------------------+
| Already Exists   | Duplicate*Exception       | DuplicateEmailException                |
|                  | *AlreadyExistsException   | AccountAlreadyExistsException          |
+------------------+---------------------------+----------------------------------------+
| State            | Invalid*StateException    | InvalidOrderStateException             |
|                  | *NotReadyException        | ServiceNotReadyException               |
+------------------+---------------------------+----------------------------------------+
| External         | *ConnectionException      | DatabaseConnectionException            |
|                  | *TimeoutException         | ExternalServiceTimeoutException        |
|                  | *UnavailableException     | PaymentGatewayUnavailableException     |
+------------------+---------------------------+----------------------------------------+
| Business         | Insufficient*Exception    | InsufficientFundsException             |
|                  | *LimitExceededException   | RateLimitExceededException             |
|                  | *ViolationException       | BusinessRuleViolationException         |
+------------------+---------------------------+----------------------------------------+
```

---

## 3. Custom vs Standard Exception Decision

### Decision Flowchart

```
                    +----------------------------------+
                    | Need to throw an exception?      |
                    +-----------------+----------------+
                                      |
                    +-----------------v----------------+
                    | Does java.lang or java.io have   |
                    | a fitting exception?              |
                    +-----------------+----------------+
                                      |
              +-----------------------+-----------------------+
              |                                               |
     +--------v--------+                            +--------v--------+
     | YES              |                            | NO               |
     +--------+--------+                            +--------+--------+
              |                                               |
     +--------v--------+                            +--------v--------+
     | Is the exception |                            | Create custom   |
     | semantically     |                            | exception       |
     | correct?         |                            +--------+--------+
     +--------+--------+                                     |
              |                                     +--------v--------+
     +--------v--------+                            | Does it need    |
     | YES              |                            | additional data?|
     +--------+--------+                            +--------+--------+
              |                                               |
     +--------v--------+                            +--------v--------+
     | Use standard     |                            | YES: Add fields |
     | exception        |                            | NO:  Simple     |
     +------------------+                            +-----------------+
```

### When to Create Custom Exceptions

```
CREATE CUSTOM WHEN:                      USE STANDARD WHEN:
--------------------------------------  --------------------------------------
Domain-specific failure                 Standard Java failure
Need to carry domain data               No additional data needed
Multiple subtypes expected              Single well-defined case
API contract requires it                Internal implementation detail
Caller needs to distinguish             Generic handling acceptable

Examples:                               Examples:
- UserNotFoundException                 - IllegalArgumentException
- PaymentDeclinedException              - NullPointerException
- OrderProcessingException              - IOException
- InsufficientInventoryException        - IllegalStateException
```

### Standard Java Exceptions Quick Reference

```
+---------------------------+-------------------------------------------+
| Exception                 | Use When                                  |
+---------------------------+-------------------------------------------+
| IllegalArgumentException  | Method received invalid argument           |
| IllegalStateException     | Object in wrong state for operation       |
| UnsupportedOperationException | Operation not supported by implementation |
| NullPointerException      | Object reference is null unexpectedly     |
| IndexOutOfBoundsException | Index out of valid range                  |
| ClassCastException        | Object not of expected type               |
| ArithmeticException      | Illegal arithmetic (divide by zero)       |
| ConcurrentModification    | Collection modified during iteration      |
| IOException               | I/O operation failed                      |
| FileNotFoundException     | File does not exist                       |
| SocketException           | Socket operation failed                   |
| TimeoutException          | Operation timed out                       |
+---------------------------+-------------------------------------------+
```

---

## 4. Exception Message Quality Guidelines

### Message Structure Decision Tree

```
                    +----------------------------------+
                    | Writing exception message?       |
                    +-----------------+----------------+
                                      |
                    +-----------------v----------------+
                    | Does it answer: What happened?   |
                    +-----------------+----------------+
                                      |
              +-----------------------+-----------------------+
              |                                               |
     +--------v--------+                            +--------v--------+
     | YES              |                            | NO: Add "Failed  |
     +--------+--------+                            | to [verb]..."    |
              |                                     +--------+--------+
     +--------v--------+                                     |
     | Does it answer: Why?                                  |
     +--------+--------+                                     |
              |                                     +--------v--------+
     +--------v--------+                            | Add reason or    |
     | YES              |                            | context          |
     +--------+--------+                            +-----------------+
              |
     +--------v--------+
     | Does it answer: |
     | What was the     |
     | context?         |
     +--------+--------+
              |
     +--------v--------+
     | YES              |
     +--------+--------+
              |
     +--------v--------+
     | Include entity   |
     | ID or key value  |
     +------------------+
```

### Message Quality Checklist

```
+----------------------------------+------+------------------------------------+
| Criterion                        | Pass | Example                            |
+----------------------------------+------+------------------------------------+
| Starts with verb?                |  [ ] | "Failed to..."                     |
| Includes what went wrong?        |  [ ] | "...connect to database..."        |
| Includes entity context?         |  [ ] | "...for user USR-001..."           |
| Includes safe values?            |  [ ] | "...email: user@example.com"       |
| No secrets in message?           |  [ ] | "...card ****4242"                 |
| Under 200 characters?            |  [ ] | Single line in logs                |
| Readable without source?         |  [ ] | Plain language, no jargon          |
| Actionable for debugging?        |  [ ] | Enough info to investigate         |
+----------------------------------+------+------------------------------------+
```

### What to Include vs Exclude

```
INCLUDE (safe):                    EXCLUDE (dangerous):
---------------------------------  ---------------------------------
Entity IDs (USR-001, ORD-4523)    Passwords
Field names (email, amount)        API keys
Counts (5 items, expected 3)       Full credit card numbers
Ranges (value 150, max 100)        Social security numbers
Status codes (HTTP 404)            Private keys
Resource names (table: users)      Tokens / session IDs
Operation context (during checkout) Internal system details
```

---

## 5. Common Code Review Comments for Exception Handling

### Anti-Pattern Detection Checklist

```
+----------------------------------+------+------------------------------------+
| Anti-Pattern                     | Flag | Severity                           |
+----------------------------------+------+------------------------------------+
| catch (Exception e)              | [ ]  | High - masks specific failures     |
| catch (Throwable t)              | [ ]  | Critical - catches Errors too      |
| Empty catch block                | [ ]  | Critical - swallows failures       |
| catch + printStackTrace()        | [ ]  | High - wrong output stream         |
| catch + return null               | [ ]  | High - defers NPE to caller        |
| catch + return empty collection  | [ ]  | Medium - hides empty result cause  |
| String concatenation in message  | [ ]  | Low - use message formatting       |
| e.toString() in log              | [ ]  | Medium - loses stack trace         |
| No exception in log.error()      | [ ]  | High - loses stack trace           |
| Generic exception message        | [ ]  | Medium - not debuggable            |
| Catching exceptions you throw    | [ ]  | Low - unnecessary                  |
| Multi-line catch blocks          | [ ]  | Low - consider extraction          |
+----------------------------------+------+------------------------------------+
```

### Code Review Phrases

```
ISSUE:                               SUGGESTION:
-----------------------------------  -----------------------------------
"Generic catch hides failures"       "Catch specific exception type"
"Swallowed exception"                "Log and rethrow or wrap"
"Missing exception context"          "Include entity ID in message"
"Logging without stack trace"        "Pass exception as last argument"
"Returning null for failure"         "Throw exception or return Optional"
"Sensitive data in message"          "Mask sensitive values"
"Overly broad catch"                 "Narrow to expected exception type"
"Missing finally or try-with"        "Use try-with-resources"
"Exception used for control flow"    "Use return value or Optional"
```

---

## 6. Common Production Mistakes

### Production Failure Patterns

```
+---------------------------+---------------------------+---------------------------+
| Mistake                   | Production Impact         | Prevention                |
+---------------------------+---------------------------+---------------------------+
| Swallowed exceptions      | Silent data loss,         | Always log or rethrow     |
|                           | undetectable failures     |                           |
+---------------------------+---------------------------+---------------------------+
| Generic catch blocks      | Impossible to diagnose,   | Catch specific types      |
|                           | root cause hidden         |                           |
+---------------------------+---------------------------+---------------------------+
| Missing stack trace       | Debug time 10x longer,    | Always pass exception     |
| in logs                   | cannot find root cause    | object to logger          |
+---------------------------+---------------------------+---------------------------+
| Exception in message      | Information leak to       | Use safe context only     |
|                           | client, security risk     |                           |
+---------------------------+---------------------------+---------------------------+
| No resource cleanup       | Connection leaks,         | Use try-with-resources    |
|                           | file handle exhaustion    |                           |
+---------------------------+---------------------------+---------------------------|
| Threading interrupts      | Deadlocks, hangs,         | Handle InterruptedException|
| swallowed                 | zombie threads            | properly                  |
+---------------------------+---------------------------+---------------------------+
| Catching Error            | JVM instability,          | Never catch Error or      |
|                           | cannot recover            | Throwable                 |
+---------------------------+---------------------------+---------------------------+
| Exception for flow control| Performance degradation,  | Use Optional or return    |
|                           | unreadable code           | values                    |
+---------------------------+---------------------------+---------------------------+
```

### Production Readiness Checklist

```
+----------------------------------+------+------------------------------------+
| Criterion                        | Pass | Notes                              |
+----------------------------------+------+------------------------------------+
| No generic Exception catches     |  [ ] | Specific types only                |
| No empty catch blocks            |  [ ] | Log or rethrow                     |
| All exceptions logged with trace |  [ ] | Exception object in log call       |
| No sensitive data in messages    |  [ ] | Card numbers, passwords masked     |
| try-with-resources for resources |  [ ] | DB, files, streams                 |
| InterruptedException handled    |  [ ] | Restore interrupt flag             |
| Custom exceptions documented     |  [ ] | Javadoc with @throws               |
| Error messages include context   |  [ ] | Entity IDs, field names            |
| Exception hierarchy is clear     |  [ ] | Base + specific subtypes           |
| Tests cover exception paths      |  [ ] | Happy + sad paths tested           |
+----------------------------------+------+------------------------------------+
```

---

## 7. Exception Translation Decision Framework

### When to Translate

```
                    +----------------------------------+
                    | Exception from lower layer?      |
                    +-----------------+----------------+
                                      |
                    +-----------------v----------------+
                    | Is the lower-level exception     |
                    | meaningful to caller?             |
                    +-----------------+----------------+
                                      |
              +-----------------------+-----------------------+
              |                                               |
     +--------v--------+                            +--------v--------+
     | YES              |                            | NO               |
     +--------+--------+                            +--------+--------+
              |                                               |
     +--------v--------+                            +--------v--------+
     | Re-throw as-is   |                            | Translate to     |
     | (preserve        |                            | domain exception |
     |  original)       |                            | (wrap as cause)  |
     +------------------+                            +------------------+
```

### Translation Pattern

```
LAYER BOUNDARY: Repository -> Service -> Controller

Repository throws:       Service translates:           Controller receives:
----------------------   --------------------------   -------------------------
SQLException             UserRepositoryException      (service handles)
DataAccessException     UserNotFoundException        (service handles)
EmptyResultData...       DuplicateUserException       (service handles)

All original exceptions preserved as causes for debugging.
```

---

## Summary Decision Matrix

```
+---------------------------+---------------------------+---------------------------+
| Decision                  | Consider                   | Default                  |
+---------------------------+---------------------------+---------------------------+
| Checked vs Unchecked      | Can caller recover?        | Unchecked                |
| Custom vs Standard        | Domain-specific?           | Standard (if exists)     |
| Message quality           | Debuggable at 2 AM?        | Verb + entity + context  |
| Translation needed?       | Lower layer exposed?       | Yes, at layer boundary   |
| Log or rethrow?           | Caller can handle?         | Log + rethrow            |
| Null vs Exception         | Expected absence?          | Null / Optional          |
| Exception vs return code  | Can fail multiple ways?    | Exception                |
+---------------------------+---------------------------+---------------------------+
```

---

*See also: [Examples](00-examples/README.md) | [Exercises](01-exercises/README.md) | [Solutions](02-solutions/README.md)*
