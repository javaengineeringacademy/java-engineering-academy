# 18 - Exception Handling Best Practices (Part 1)

> "A best practice is a technique or methodology that, through experience and research,
> has proven to reliably lead to a desired outcome."

---

## 1. Why Best Practices Matter

Exception handling best practices are not arbitrary style guidelines. They are
patterns that emerged from decades of production failures, debugging sessions,
and architectural evolution.

```
The Cost of Poor Exception Handling
=====================================

    Code Without Best Practices
    +---------------------------------------------+
    |  Production incident -> 4-hour outage       |
    |  Root cause: swallowed exception             |
    |  Debug time: 6 hours (no stack trace logged) |
    |  Data corruption: 200 orphaned records       |
    |  Total cost: ~$45,000                        |
    +---------------------------------------------+

    Code With Best Practices
    +---------------------------------------------+
    |  Same scenario -> caught and logged properly |
    |  Alert triggered in 2 minutes                |
    |  Auto-rollback completed                     |
    |  Debug time: 15 minutes                      |
    |  Total cost: ~$200                           |
    +---------------------------------------------+
```

Best practices provide:

1. **Predictability** -- Other developers know how to read and maintain your code
2. **Debuggability** -- Failures are diagnosable from logs alone
3. **Reliability** -- Bugs propagate immediately instead of hiding
4. **Security** -- Sensitive data never leaks into logs or stack traces
5. **Maintainability** -- Code handles edge cases without special-casing every call

---

## 2. Exception Selection Guidelines

### Checked vs Unchecked Decision Flowchart

```
                      +----------------------------+
                      |   New Exception Needed?     |
                      +-------------+--------------+
                                    |
                       +------------v------------+
                       | Can the caller          |
                       | reasonably recover?     |
                       +------------+------------+
                                    |
                       +------------v------------+
                       |  YES           NO       |
                       |   |            |        |
                       v   v            v        v
                 +-----------+    +------------------+
                 |  Checked  |    |   Unchecked      |
                 | Exception |    |   Exception      |
                 |           |    |                  |
                 | Caller    |    | Programming error |
                 | MUST      |    | or unrecoverable  |
                 | handle it |    | condition         |
                 +-----------+    +------------------+
```

### When to Use Checked Exceptions

Use checked exceptions when the caller **can and should** take corrective action:

```java
// CHECKED: File not found -- caller can prompt for another file
public void loadConfig(String path) throws FileNotFoundException {
    Files.readAllLines(Paths.get(path));
}

// CHECKED: Connection failure -- caller can retry or use fallback
public User fetchUser(String id) throws ServiceException {
    return serviceClient.get("/users/" + id);
}

// CHECKED: Invalid input -- caller can validate and correct
public Order createOrder(CreateOrderRequest req)
        throws ValidationException {
    validator.validate(req);
}
```

### When to Use Unchecked Exceptions

Use unchecked exceptions for programming errors and unrecoverable conditions:

```java
// UNCHECKED: Programming error -- null passed where not allowed
public User findUser(String id) {
    Objects.requireNonNull(id, "User ID must not be null");
    return userRepository.findById(id);
}

// UNCHECKED: Invalid state -- method called at wrong time
public void processPayment() {
    if (currentOrder == null) {
        throw new IllegalStateException(
            "No order set. Call setOrder() first.");
    }
}

// UNCHECKED: Logic error -- division by zero
public double calculateAverage(int[] numbers) {
    if (numbers == null || numbers.length == 0) {
        throw new IllegalArgumentException(
            "Array must not be null or empty");
    }
    // ...
}
```

### Comparison Table

| Factor                    | Checked                              | Unchecked                         |
|---------------------------|--------------------------------------|-----------------------------------|
| Caller can recover?       | Yes                                  | No                                |
| Example                   | File not found                       | Null pointer                      |
| Java keyword              | `throws` declaration                 | None required                     |
| Callers forced to handle? | Yes (compile error if not)           | No                                |
| Use for                   | API contracts                        | Programming errors                |
| Framework support         | `try-catch`                          | `@ExceptionHandler`               |
| Typical package           | `com.app.exceptions`                 | `java.lang.*`                     |

---

## 3. Naming Conventions

Exception names should be self-documenting. A good name tells the reader
exactly what went wrong without reading the message.

### Rules

1. **Suffix**: Always end with `Exception` (e.g., `PaymentFailedException`)
2. **Tense**: Use past tense for events that occurred (e.g., `ConnectionLostException`)
3. **Clarity**: Name the condition, not the cause (e.g., `InvalidOrderException`, not `ValidationException`)
4. **Specificity**: Be as specific as possible (e.g., `CardDeclinedException`, not `PaymentException`)
5. **No prefixes**: Avoid `My`, `App`, or `Custom` prefixes (e.g., `UserNotFoundException`, not `MyUserNotFoundException`)

### Good vs Bad Names

| Bad Name            | Problem                                    | Good Name                          |
|---------------------|--------------------------------------------|-------------------------------------|
| `MyException`       | Generic, no context                        | `OrderProcessingException`          |
| `Error`             | Too vague, conflicts with `java.lang.Error`| `DataValidationException`           |
| `FailedException`   | Failed what?                               | `ConnectionTimeoutException`        |
| `Exception1`        | Not descriptive                            | `InvalidStateException`             |
| `BadDataException`  | What about the data?                       | `MalformedRequestException`         |
| `OopsException`     | Unprofessional, uninformative              | `UnexpectedEndOfFileException`      |
| `FileError`         | Not an exception, too vague                | `FileNotFoundException`             |

### Naming Examples by Category

```java
// -- Network -----------------------------------------------------------
public class ConnectionTimeoutException extends IOException { }
public class DnsResolutionException extends IOException { }
public class TlsHandshakeException extends IOException { }

// -- Validation --------------------------------------------------------
public class MissingFieldException extends ValidationException { }
public class OutOfRangeException extends ValidationException { }
public class DuplicateKeyException extends ValidationException { }

// -- Database ----------------------------------------------------------
public class DeadlockDetectedException extends DataAccessException { }
public class QueryTimeoutException extends DataAccessException { }
public class ConnectionPoolExhaustedException extends DataAccessException { }

// -- Business ----------------------------------------------------------
public class InsufficientFundsException extends BusinessException { }
public class AccountLockedException extends BusinessException { }
public class OrderAlreadyCancelledException extends BusinessException { }
```

---

## 4. Exception Message Quality

An exception message is the first thing a developer reads at 2 AM during a
production incident. It must answer three questions:

1. **What** happened?
2. **Why** did it happen?
3. **What** was the context?

### Anatomy of a Good Exception Message

```
+-----------------------------------------------------------------------+
|  EXCEPTION MESSAGE ANATOMY                                             |
+-----------------------------------------------------------------------+
|                                                                       |
|  "Failed to charge card for order ORD-4523: card ending in 4242      |
|   was declined by processor with code DECLINED_INSUFFICIENT_FUNDS"   |
|                                                                       |
|  +----------+  +-----------------------------+  +------------------+  |
|  |  ACTION   |  |          CONTEXT            |  |    DETAILS       |  |
|  |  (what)   |  |       (which entity)        |  |  (why)           |  |
|  +----------+  +-----------------------------+  +------------------+  |
|                                                                       |
+-----------------------------------------------------------------------+
```

### Writing Rules

| Rule                      | Example                                    | Why                                  |
|---------------------------|--------------------------------------------|--------------------------------------|
| Start with a verb         | "Failed to..." / "Could not..."            | Describes the action that failed     |
| Include the entity ID     | "User USR-001" / "Order ORD-4523"          | Makes it searchable in logs          |
| Include the value (safe)  | "email: user@example.com"                  | Helps identify the problem           |
| Never include secrets     | "card: ****4242" not full number           | Security requirement                 |
| Use plain language        | "Connection timed out" not "EAGAIN"        | Debuggable without source            |
| Keep under 200 chars      | One line in log viewers                    | Readability in dashboards            |

### What to Include vs Exclude

```
  INCLUDE                              EXCLUDE
  -------------------------------      -------------------------------
  Entity IDs (USR-001, ORD-4523)       Passwords
  Field names (email, amount)           API keys
  Counts ("5 items, expected 3")        Full credit card numbers
  Ranges ("value 150, max 100")         Social security numbers
  Status codes (HTTP 404, DB error)     Private keys
  Resource names ("table users")        Tokens / session IDs
```

### Code Example

```java
public class PaymentService {

    // BAD: Vague, no context
    public void processPaymentBad(PaymentRequest req) {
        if (req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    // GOOD: Clear, contextual, safe
    public void processPaymentGood(PaymentRequest req) {
        Objects.requireNonNull(req, "Payment request must not be null");
        Objects.requireNonNull(req.getAmount(), "Payment amount must not be null");

        if (req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Payment amount must be positive, got: " + req.getAmount());
        }

        if (req.getCardNumber() == null || req.getCardNumber().isBlank()) {
            throw new IllegalArgumentException(
                "Payment card number is required for order: " + req.getOrderId());
        }

        // Never do this -- exposes sensitive data:
        // throw new IllegalArgumentException(
        //     "Invalid card: " + req.getCardNumber());  // SECURITY RISK!

        // Correct approach -- mask sensitive data:
        String masked = maskCardNumber(req.getCardNumber());
        log.info("Processing payment for card: {}, amount: {}",
            masked, req.getAmount());
    }

    private String maskCardNumber(String card) {
        if (card == null || card.length() < 4) return "****";
        return "****-****-****-" + card.substring(card.length() - 4);
    }
}
```

### Null vs Exception: When to Return null vs Throw

```
  Return null                           Throw exception
  ---------------------------           ---------------------------
  Expected absence of value             Unexpected failure
  Caller checks for null anyway         Caller cannot proceed
  "Optional" result                     "Required" result
  Simple getter pattern                 Operation with side effects

  Example:                              Example:
  findById() returns null               findById() fails due to DB error
  when record does not exist            when DB connection is lost
```

---

## 5. Logging Best Practices

### What to Log

