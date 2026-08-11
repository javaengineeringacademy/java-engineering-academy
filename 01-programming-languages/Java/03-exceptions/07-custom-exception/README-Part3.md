# 07 - Custom Exception (Part 3)
**Previous:** [Part 2](README-Part2.md)

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
