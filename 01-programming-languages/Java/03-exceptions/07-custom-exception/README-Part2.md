# 07 - Custom Exception (Part 2)
**Previous:** [Part 1](README.md) | **Next:** [Part 3](README-Part3.md)

---

## Examples

### Example 1: InsufficientFundsException

```java
public class InsufficientFundsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String accountId;
    private final double requested;
    private final double available;

    public InsufficientFundsException(
            String accountId, double requested, double available) {
        super(String.format(
            "Account %s: requested $%.2f, available $%.2f",
            accountId, requested, available));
        this.accountId = accountId;
        this.requested = requested;
        this.available = available;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getRequested() {
        return requested;
    }

    public double getAvailable() {
        return available;
    }

    public double getDeficit() {
        return requested - available;
    }
}

// Usage
public void withdraw(String accountId, double amount) {
    double balance = getBalance(accountId);
    if (balance < amount) {
        throw new InsufficientFundsException(accountId, amount, balance);
    }
    // proceed with withdrawal
}
```

### Example 2: ValidationException

```java
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Map<String, String> fieldErrors;

    public ValidationException(Map<String, String> fieldErrors) {
        super("Validation failed: " + fieldErrors);
        this.fieldErrors = Map.copyOf(fieldErrors);
    }

    public ValidationException(String field, String message) {
        this(Map.of(field, message));
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public boolean hasFieldError(String field) {
        return fieldErrors.containsKey(field);
    }
}

// Usage
public void createUser(CreateUserRequest request) {
    Map<String, String> errors = new HashMap<>();

    if (request.getEmail() == null || !request.getEmail().contains("@")) {
        errors.put("email", "Must be a valid email address");
    }
    if (request.getName() == null || request.getName().isBlank()) {
        errors.put("name", "Name is required");
    }

    if (!errors.isEmpty()) {
        throw new ValidationException(errors);
    }
}
```

### Example 3: DomainException (Base Class)

```java
public abstract class DomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String errorCode;

    protected DomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected DomainException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

// Specialized domain exceptions
public class InsufficientFundsException extends DomainException {
    // ...
}

public class AccountFrozenException extends DomainException {
    // ...
}
```

---

## When to Create Custom Exceptions

### Create When:

1. **Domain-specific errors** — Business rules have unique failure modes
2. **API contracts** — Public APIs need documented, typed errors
3. **Error codes required** — Logging/monitoring needs structured codes
4. **Multiple error fields** — Errors carry data beyond a message string
5. **Catch specificity** — Callers need to distinguish error types

### Don't Create When:

1. **Generic errors suffice** — `IllegalArgumentException` is enough
2. **Simple message** — No additional data needed
3. **Internal use only** — `IllegalStateException` works fine
4. **Temporary code** — Standard exceptions are clearer

### Decision Matrix

| Scenario | Use Custom? | Example |
|----------|-------------|---------|
| User not found | Yes | `UserNotFoundException` |
| Invalid argument | No | `IllegalArgumentException` |
| Payment failed | Yes | `PaymentDeclinedException` |
| Null parameter | No | `NullPointerException` |
| Network timeout | No | `SocketTimeoutException` |
| Business rule violation | Yes | `OrderCannotBeCancelledException` |

---

## Exception Hierarchy Design

### Flat Hierarchy

For small projects with few exception types:

```
RuntimeException
├── InsufficientFundsException
├── AccountFrozenException
└── PaymentDeclinedException
```

### Layered Hierarchy

For larger projects with domain groupings:

```
DomainException (abstract, extends RuntimeException)
├── AccountException (abstract)
│   ├── InsufficientFundsException
│   └── AccountFrozenException
├── PaymentException (abstract)
│   ├── PaymentDeclinedException
│   └── PaymentTimeoutException
└── ValidationException
```

### Multi-Layer Hierarchy

For enterprise applications:

```
AppException (abstract, extends RuntimeException)
├── InfrastructureException (abstract)
│   ├── DataAccessException
│   └── ExternalServiceException
├── DomainException (abstract)
│   ├── AccountException
│   └── OrderException
└── PresentationException (abstract)
    └── ValidationException
```

### Best Practices for Hierarchies

1. Keep depth to 2-3 levels maximum
2. Use abstract intermediate classes for grouping
3. Each leaf exception should represent a specific error
4. Base exception should carry common fields (errorCode, timestamp)

---

## Common Pitfalls

### Pitfall 1: Too Many Custom Exceptions

```java
// Bad — one exception per tiny variation
public class EmailTooShortException extends RuntimeException {}
public class EmailNoAtSignException extends RuntimeException {}
public class EmailNoDotException extends RuntimeException {}

// Good — single exception with field details
public class InvalidEmailException extends RuntimeException {
    private final String email;
    private final String reason;
}
```

### Pitfall 2: Not Serializable

```java
// Bad — missing serialVersionUID
public class MyException extends Exception {
    private String detail;
}

// Good — proper serialization
public class MyException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String detail;
}
```

### Pitfall 3: Missing Constructors

Always provide at minimum:
1. `MyException(String message)`
2. `MyException(String message, Throwable cause)`
3. `MyException(Throwable cause)`

### Pitfall 4: Swallowing Cause

```java
// Bad — original cause lost
try {
    repository.save(entity);
} catch (SQLException e) {
    throw new DataAccessException("Save failed");
}

// Good — cause preserved
try {
    repository.save(entity);
} catch (SQLException e) {
    throw new DataAccessException("Save failed", e);
}
```

### Pitfall 5: Using Exceptions for Flow Control

```java
// Bad — exception used for normal flow
try {
    user = findUser(id);
} catch (UserNotFoundException e) {
    user = createDefaultUser();
}

// Good — check first
Optional<User> user = findUser(id);
if (user.isEmpty()) {
    user = createDefaultUser();
}
```

### Pitfall 6: Catching Custom Exceptions Too Broadly

```java
// Bad — catches everything, loses type safety
catch (DomainException e) {
    log.error("Domain error", e);
    return fallback();
}

// Good — handle specific subtypes
catch (InsufficientFundsException e) {
    return notifyUser("Insufficient funds: " + e.getDeficit());
} catch (AccountFrozenException e) {
    return notifyAdmin("Account frozen: " + e.getAccountId());
}
```

---
