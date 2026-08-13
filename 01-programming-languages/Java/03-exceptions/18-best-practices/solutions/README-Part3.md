# Exception Handling Best Practices: Solutions (Part 3)

> Solutions 5–6. See [Part 1](README-Part1.md) for Solutions 1–3, [Part 2](README-Part2.md) for Solution 4.

---

## Solution 5: Design a Global Exception Handler

```java
@RestControllerAdvice(basePackages = "com.app.api")
public class GlobalExceptionHandler {

    private static final Logger log = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(
            UserNotFoundException e, HttpServletRequest request) {
        log.warn("User not found: path={}, userId={}",
            request.getRequestURI(), e.getUserId());

        return ErrorResponse.builder()
            .code("USER_NOT_FOUND")
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateEmail(
            DuplicateEmailException e, HttpServletRequest request) {
        log.warn("Duplicate email: path={}, email={}",
            request.getRequestURI(), e.getEmail());

        return ErrorResponse.builder()
            .code("DUPLICATE_EMAIL")
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOrderNotFound(
            OrderNotFoundException e, HttpServletRequest request) {
        log.warn("Order not found: path={}, orderId={}",
            request.getRequestURI(), e.getOrderId());

        return ErrorResponse.builder()
            .code("ORDER_NOT_FOUND")
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(InsufficientInventoryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleInsufficientInventory(
            InsufficientInventoryException e, HttpServletRequest request) {
        log.warn("Insufficient inventory: path={}, productId={}, requested={}, available={}",
            request.getRequestURI(), e.getProductId(),
            e.getRequestedQuantity(), e.getAvailableQuantity());

        return ErrorResponse.builder()
            .code("INSUFFICIENT_INVENTORY")
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(PaymentDeclinedException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ErrorResponse handlePaymentDeclined(
            PaymentDeclinedException e, HttpServletRequest request) {
        log.warn("Payment declined: path={}, reason={}",
            request.getRequestURI(), e.getDeclineReason());

        return ErrorResponse.builder()
            .code("PAYMENT_DECLINED")
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(
            ValidationException e, HttpServletRequest request) {
        log.warn("Validation failed: path={}, field={}, reason={}",
            request.getRequestURI(), e.getField(), e.getMessage());

        return ErrorResponse.builder()
            .code("VALIDATION_ERROR")
            .message(e.getMessage())
            .field(e.getField())
            .build();
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServiceException(
            ServiceException e, HttpServletRequest request) {
        log.error("Service error: path={}: {}",
            request.getRequestURI(), e.getMessage(), e);

        return ErrorResponse.builder()
            .code("INTERNAL_ERROR")
            .message("An unexpected error occurred")
            .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("Method not allowed: path={}, method={}",
            request.getRequestURI(), request.getMethod());

        return ErrorResponse.builder()
            .code("METHOD_NOT_ALLOWED")
            .message("HTTP method not supported: " + request.getMethod())
            .build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        log.warn("Media type not supported: path={}, type={}",
            request.getRequestURI(), e.getContentType());

        return ErrorResponse.builder()
            .code("UNSUPPORTED_MEDIA_TYPE")
            .message("Content type not supported: " + e.getContentType())
            .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpected(
            Exception e, HttpServletRequest request) {
        log.error("Unexpected error: path={}: {}",
            request.getRequestURI(), e.getMessage(), e);

        return ErrorResponse.builder()
            .code("INTERNAL_ERROR")
            .message("An unexpected error occurred")
            .build();
    }
}
```

### Error Response Class

```java
public class ErrorResponse {

    private final String code;
    private final String message;
    private final String field;
    private final Instant timestamp;

    private ErrorResponse(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.field = builder.field;
        this.timestamp = Instant.now();
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getField() { return field; }
    public Instant getTimestamp() { return timestamp; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String code;
        private String message;
        private String field;

        public Builder code(String code) { this.code = code; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder field(String field) { this.field = field; return this; }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
```

### HTTP Status Mapping

| Exception | HTTP Status | Reason |
|---|---|---|
| `UserNotFoundException` | 404 | Resource does not exist |
| `DuplicateEmailException` | 409 | Conflict with existing resource |
| `OrderNotFoundException` | 404 | Resource does not exist |
| `InsufficientInventoryException` | 409 | Conflict with inventory state |
| `PaymentDeclinedException` | 402 | Payment required |
| `ValidationException` | 400 | Client error |
| `ServiceException` | 500 | Server error |
| `HttpRequestMethodNotSupportedException` | 405 | Method not allowed |
| `HttpMediaTypeNotSupportedException` | 415 | Unsupported media type |
| `Exception` (catch-all) | 500 | Server error |

---

## Solution 6: Production Readiness Review

### Issues Found

| # | Issue | Severity | Fix |
|---|---|---|---|
| 1 | `catch (Exception e)` in `processPayment` | High | Catch specific exception types |
| 2 | `log.error("Payment failed")` without exception | High | Add exception object to log call |
| 3 | No request context in logs | Medium | Add request ID or user context |
| 4 | `return null` for errors | High | Return explicit error result |
| 5 | `catch (Exception e)` in `refund` | High | Catch specific exception types |
| 6 | `log.error("Refund failed: " + e)` | Medium | Use message formatting, pass exception |
| 7 | Swallowed refund failure | Critical | Log and handle appropriately |
| 8 | No timeout handling | High | Add timeout configuration |
| 9 | No idempotency | High | Add idempotency key support |
| 10 | No retry for transient errors | Medium | Add retry with backoff |

### Fixed Code

```java
@Service
public class PaymentService {

    private static final Logger log = Logger.getLogger(PaymentService.class);

    private final PaymentGateway gateway;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final IdempotencyStore idempotencyStore;

    private static final Duration GATEWAY_TIMEOUT = Duration.ofSeconds(30);
    private static final int MAX_RETRIES = 3;

    public PaymentResult processPayment(PaymentRequest request) {
        Objects.requireNonNull(request, "Payment request must not be null");

        String idempotencyKey = request.getIdempotencyKey();

        // Check idempotency
        if (idempotencyKey != null) {
            PaymentResult cached = idempotencyStore.getResult(idempotencyKey);
            if (cached != null) {
                log.info("Returning cached result for idempotency key: {}",
                    idempotencyKey);
                return cached;
            }
        }

        try {
            User user = userRepository.findById(request.getUserId());

            BigDecimal balance = gateway.getBalance(user.getId());
            if (balance.compareTo(request.getAmount()) < 0) {
                PaymentResult result = PaymentResult.insufficientFunds();
                cacheResult(idempotencyKey, result);
                return result;
            }

            Transaction tx = executeWithRetry(() ->
                gateway.charge(user.getId(), request.getAmount())
            );

            transactionRepository.save(tx);

            PaymentResult result = PaymentResult.success(tx.getId());
            cacheResult(idempotencyKey, result);
            return result;

        } catch (UserNotFoundException e) {
            log.warn("User not found for payment: userId={}", request.getUserId());
            return PaymentResult.error("User not found");

        } catch (PaymentGatewayException e) {
            log.error("Payment gateway error: userId={}, amount={}: {}",
                request.getUserId(), request.getAmount(), e.getMessage(), e);
            return PaymentResult.error("Payment service unavailable");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Payment interrupted: userId={}", request.getUserId(), e);
            return PaymentResult.error("Payment processing interrupted");

        } catch (Exception e) {
            log.error("Unexpected error processing payment: userId={}, amount={}: {}",
                request.getUserId(), request.getAmount(), e.getMessage(), e);
            return PaymentResult.error("An unexpected error occurred");
        }
    }

    public RefundResult refund(String transactionId) {
        Objects.requireNonNull(transactionId, "Transaction ID must not be null");

        try {
            Transaction tx = transactionRepository.findById(transactionId);

            // Check if already refunded
            if (tx.isRefunded()) {
                log.info("Transaction already refunded: {}", transactionId);
                return RefundResult.alreadyRefunded();
            }

            executeWithRetry(() -> gateway.refund(tx.getId(), tx.getAmount()));

            tx.markRefunded();
            transactionRepository.save(tx);

            log.info("Refund processed: transactionId={}, amount={}",
                transactionId, tx.getAmount());
            return RefundResult.success();

        } catch (TransactionNotFoundException e) {
            log.warn("Transaction not found for refund: {}", transactionId);
            return RefundResult.notFound();

        } catch (PaymentGatewayException e) {
            log.error("Refund gateway error: transactionId={}: {}",
                transactionId, e.getMessage(), e);
            return RefundResult.error("Refund service unavailable");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Refund interrupted: transactionId={}", transactionId, e);
            return RefundResult.error("Refund processing interrupted");

        } catch (Exception e) {
            log.error("Unexpected error processing refund: transactionId={}: {}",
                transactionId, e.getMessage(), e);
            return RefundResult.error("An unexpected error occurred");
        }
    }

    private <T> T executeWithRetry(Callable<T> operation) throws Exception {
        InterruptedException lastInterrupted = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                return operation.call();
            } catch (PaymentGatewayException e) {
                if (!e.isTransient() || attempt == MAX_RETRIES) {
                    throw e;
                }
                log.warn("Transient gateway error, attempt {}/{}: {}",
                    attempt, MAX_RETRIES, e.getMessage());
                Thread.sleep((long) Math.pow(2, attempt) * 100);
            } catch (InterruptedException e) {
                lastInterrupted = e;
                break;
            }
        }

        if (lastInterrupted != null) {
            Thread.currentThread().interrupt();
            throw lastInterrupted;
        }

        throw new PaymentGatewayException("Max retries exceeded");
    }

    private void cacheResult(String idempotencyKey, PaymentResult result) {
        if (idempotencyKey != null) {
            idempotencyStore.cache(idempotencyKey, result, Duration.ofHours(24));
        }
    }
}
```

### Production Concerns Addressed

| Concern | Solution |
|---|---|
| Gateway hangs | Timeout via `GATEWAY_TIMEOUT` configuration |
| Crash after charge, before save | Idempotency key allows safe retry |
| Duplicate refund calls | `isRefunded()` check prevents double refund |
| Transient gateway errors | Exponential backoff retry (max 3 attempts) |
| Thread interruption | `InterruptedException` handled, interrupt flag restored |
| No context in logs | User ID, amount, transaction ID in all log messages |
| Generic catch-all | Specific exception types caught individually |
| No error result caching | `cacheResult()` ensures idempotent responses |

---

*See also: [Decision Guide](../decision.md) | [Part 1: Solutions 1–3](README-Part1.md) | [Part 2: Solution 4](README-Part2.md)*
