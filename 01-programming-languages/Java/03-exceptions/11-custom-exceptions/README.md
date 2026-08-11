# 11 - Custom Exceptions in Java

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

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | Custom exceptions possible by extending `Exception` or `RuntimeException` |
| JDK 1.4 | Cause chaining added (`initCause()`, constructors with cause) |
| JDK 7 | Suppressed exceptions available for custom `AutoCloseable` implementations |
| JDK 7 | `serialVersionUID` best practices for exception serialization |

## Summary

| Concept | Key Point |
|---------|-----------|
| Custom Exceptions | User-defined exception classes for domain-specific error conditions |
| Checked vs Unchecked | Choose checked when caller must recover; unchecked for programming errors |
| Naming Convention | Always suffix with `Exception`; never use `Error` suffix |
| Design Steps | Choose type, name, add fields, provide constructors, implement serialVersionUID |
| Anatomy | Include message, cause (chaining), error codes, and structured fields |
| Exception Chaining | Preserve original cause when wrapping exceptions for debugging |
| Package Placement | Place custom exceptions near the code that throws them |
| Error Codes | Use structured error codes for logging and monitoring |

---

**Continue:** [Part 2](README-Part2.md) | [Part 3](README-Part3.md)
