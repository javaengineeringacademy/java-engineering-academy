# Custom Exceptions in Java

## Table of Contents

1. [What Are Custom Exceptions](#what-are-custom-exceptions)
2. [Why Custom Exceptions Exist](#why-custom-exceptions-exist)
3. [Design Rationale](#design-rationale)
4. [Design Steps](#design-steps)
5. [Custom Exception Anatomy](#custom-exception-anatomy)
6. [Examples](#examples)
7. [When to Create Custom Exceptions](#when-to-create-custom-exceptions)
8. [Exception Hierarchy Design](#exception-hierarchy-design)
9. [Common Pitfalls](#common-pitfalls)
10. [Production Patterns](#production-patterns)

---

## What Are Custom Exceptions

Custom exceptions (user-defined exceptions) are exception classes created by
developers to represent domain-specific error conditions. They extend either
`Exception` (checked) or `RuntimeException` (unchecked).

```java
// Checked custom exception
public class InsufficientFundsException extends Exception {
    private final double amount;

    public InsufficientFundsException(double amount) {
        super("Insufficient funds: requested " + amount);
        this.amount = amount;
    }
}

// Unchecked custom exception
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
```

---

## Why Custom Exceptions Exist

### Domain-Specific Error Modeling

Standard exceptions (`IOException`, `NullPointerException`) are too generic for
business logic. Custom exceptions express domain concepts precisely.

```java
// Without custom exceptions — unclear intent
public void transfer(Account from, Account to, double amount) {
    if (from.getBalance() < amount) {
        throw new IllegalArgumentException("Not enough money");
    }
}

// With custom exceptions — clear business intent
public void transfer(Account from, Account to, double amount) {
    if (from.getBalance() < amount) {
        throw new InsufficientFundsException(amount);
    }
}
```

### API Contract Clarity

Custom exceptions document what can go wrong. Callers know exactly which errors
to handle.

```java
public class BankService {
    /**
     * @throws InsufficientFundsException if balance is too low
     * @throws AccountFrozenException     if the account is frozen
     */
    public void transfer(Account from, Account to, double amount)
            throws InsufficientFundsException, AccountFrozenException {
        // ...
    }
}
```

### Error Code Association

Custom exceptions carry structured error codes for logging and monitoring.

### Catch Specificity

Catch blocks can target custom exceptions without relying on message strings.

---

## Design Rationale

### Checked vs Unchecked Decision

| Factor | Checked (`extends Exception`) | Unchecked (`extends RuntimeException`) |
|--------|-------------------------------|----------------------------------------|
| Caller must handle | Yes | No |
| Recoverable errors | Yes | No |
| Programming errors | No | Yes |
| API contracts | Yes | No |
| Typical use | IO, network, validation | Null checks, state violations |

**Rule of thumb:** Make custom exceptions unchecked by default. Only use checked
when callers must recover from the error.

### Naming Convention

Always suffix with `Exception`:
- `InsufficientFundsException`
- `PaymentDeclinedException`
- `UserNotFoundException`

Never use `Error` suffix — reserve that for JVM-level problems.

### Package Placement

Place custom exceptions near the code that throws them:

```
com.example.bank/
├── Account.java
├── BankService.java
└── exception/
    ├── InsufficientFundsException.java
    └── AccountFrozenException.java
```

---

## Design Steps

### Step 1: Choose Checked or Unchecked

```
Is the error recoverable by the caller?
├── Yes → Checked (extends Exception)
│   └── Can caller reasonably handle it?
│       ├── Yes → Checked
│       └── No  → Unchecked
└── No  → Unchecked (extends RuntimeException)
```

### Step 2: Name with Exception Suffix

```java
public class PaymentDeclinedException extends RuntimeException {
    // ✓ Correct naming
}

public class PaymentDeclinedError extends RuntimeException {
    // ✗ Wrong — don't use Error suffix for custom exceptions
}
```

### Step 3: Add Meaningful Fields

```java
public class InsufficientFundsException extends RuntimeException {
    private final double requestedAmount;
    private final double availableBalance;
    private final String errorCode;

    public InsufficientFundsException(
            double requestedAmount, double availableBalance) {
        super(String.format(
            "Insufficient funds: requested %.2f, available %.2f",
            requestedAmount, availableBalance));
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
        this.errorCode = "INSUFFICIENT_FUNDS";
    }
}
```

### Step 4: Provide Constructors

At minimum, provide:
1. Message-only constructor
2. Message + cause constructor
3. Cause-only constructor

### Step 5: Implement `serialVersionUID` (Checked Exceptions)

```java
public class PaymentDeclinedException extends Exception {
    private static final long serialVersionUID = 1L;
    // ...
}
```

---

## Custom Exception Anatomy

### Basic Structure

```java
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String fieldName;
    private final String errorCode;

    // Message-only
    public ValidationException(String message) {
        super(message);
        this.fieldName = null;
        this.errorCode = "VALIDATION_ERROR";
    }

    // Message + cause
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.fieldName = null;
        this.errorCode = "VALIDATION_ERROR";
    }

    // Full constructor
    public ValidationException(String fieldName, String message, Throwable cause) {
        super(message, cause);
        this.fieldName = fieldName;
        this.errorCode = "VALIDATION_ERROR";
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
```

### Message

The message should be human-readable and describe the error clearly.

```java
// Bad
throw new ValidationException("error");

// Good
throw new ValidationException("Email address is not valid: " + email);
```

### Cause (Exception Chaining)

Always preserve the original cause when wrapping exceptions.

```java
try {
    repository.save(entity);
} catch (SQLException e) {
    throw new DataAccessException("Failed to save entity", e);
}
```

### Error Codes

Error codes enable programmatic error handling and monitoring.

```java
public enum ErrorCode {
    INSUFFICIENT_FUNDS("INSUF_001"),
    ACCOUNT_FROZEN("ACCT_002"),
    VALIDATION_FAILED("VAL_003");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
```

### Structured Fields

Custom exceptions carry domain-relevant data:

```java
public class InsufficientFundsException extends RuntimeException {
    private final double requestedAmount;
    private final double availableBalance;
    private final String accountId;

    // getters, constructors...
}
```

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

## Production Patterns

### Pattern 1: Exception Factory

```java
public final class Exceptions {

    private Exceptions() {}

    public static InsufficientFundsException insufficientFunds(
            String accountId, double requested, double available) {
        return new InsufficientFundsException(accountId, requested, available);
    }

    public static ValidationException validationFailed(
            Map<String, String> errors) {
        return new ValidationException(errors);
    }

    public static ValidationException validationFailed(
            String field, String message) {
        return new ValidationException(field, message);
    }
}

// Usage
throw Exceptions.insufficientFunds(accountId, 100.0, 50.0);
throw Exceptions.validationFailed("email", "Invalid email");
```

### Pattern 2: Error Code Enum

```java
public enum ErrorCode {
    INSUFFICIENT_FUNDS("BANK_001", "Insufficient funds for transaction"),
    ACCOUNT_FROZEN("BANK_002", "Account is frozen"),
    VALIDATION_FAILED("SYS_001", "Input validation failed"),
    DATA_ACCESS("INF_001", "Data access error");

    private final String code;
    private final String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
```

### Pattern 3: Base Exception with Error Code

```java
public abstract class AppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ErrorCode errorCode;
    private final Instant timestamp;

    protected AppException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
    }

    protected AppException(
            ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
```

### Pattern 4: Builder Pattern for Complex Exceptions

```java
public class OrderException extends DomainException {

    private String orderId;
    private String customerId;
    private OrderStatus currentStatus;
    private OrderStatus attemptedStatus;

    // private constructor
    private OrderException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static Builder builder(ErrorCode errorCode) {
        return new Builder(errorCode);
    }

    public static class Builder {
        private final ErrorCode errorCode;
        private String message;
        private String orderId;
        private String customerId;
        private OrderStatus currentStatus;
        private OrderStatus attemptedStatus;

        Builder(ErrorCode errorCode) {
            this.errorCode = errorCode;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder currentStatus(OrderStatus status) {
            this.currentStatus = status;
            return this;
        }

        public Builder attemptedStatus(OrderStatus status) {
            this.attemptedStatus = status;
            return this;
        }

        public OrderException build() {
            OrderException ex = new OrderException(errorCode, message);
            ex.orderId = orderId;
            ex.customerId = customerId;
            ex.currentStatus = currentStatus;
            ex.attemptedStatus = attemptedStatus;
            return ex;
        }
    }
}

// Usage
throw OrderException.builder(ErrorCode.ORDER_CANNOT_CANCEL)
    .orderId("ORD-123")
    .customerId("CUST-456")
    .currentStatus(OrderStatus.SHIPPED)
    .attemptedStatus(OrderStatus.CANCELLED)
    .message("Cannot cancel order in SHIPPED status")
    .build();
```

### Pattern 5: Exception Mapping

```java
public class ExceptionMapper {

    private static final Map<Class<? extends AppException>, HttpStatus> STATUS_MAP =
        Map.of(
            InsufficientFundsException.class, HttpStatus.BAD_REQUEST,
            AccountFrozenException.class, HttpStatus.FORBIDDEN,
            UserNotFoundException.class, HttpStatus.NOT_FOUND,
            ValidationException.class, HttpStatus.UNPROCESSABLE_ENTITY
        );

    public static HttpStatus mapToStatus(AppException exception) {
        return STATUS_MAP.getOrDefault(
            exception.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

---

## Summary

| Concept | Recommendation |
|---------|---------------|
| Default to unchecked | Extend `RuntimeException` unless recovery is needed |
| Name with Exception suffix | `XxxException`, never `XxxError` |
| Provide all constructors | Message, message+cause, cause-only |
| Add `serialVersionUID` | For checked exceptions at minimum |
| Carry structured data | Error codes, field names, domain IDs |
| Preserve cause chain | Always pass cause to super constructor |
| Use exception factories | Static methods for common exception creation |
| Limit hierarchy depth | 2-3 levels maximum |
| Don't overuse | Standard exceptions for simple cases |

---

## Key Takeaways

1. Custom exceptions model domain-specific errors precisely
2. Choose checked vs unchecked based on recoverability
3. Always preserve the exception cause chain
4. Carry meaningful fields and error codes
5. Use factories and builders for complex exceptions
6. Keep hierarchies shallow and well-organized
7. Avoid creating custom exceptions when standard ones suffice
