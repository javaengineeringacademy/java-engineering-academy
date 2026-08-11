# Exception Handling Best Practices: Practice Exercises (Part 2)

> Exercises 5–6. See [Part 1](README-Part1.md) for Exercises 1–4.

---

## Exercise 5: Design a Global Exception Handler

### Problem Statement

Design and implement a global exception handler for a REST API. The handler should map domain exceptions to appropriate HTTP responses, log correctly, and never expose implementation details.

### API Endpoints

```
POST   /api/users          - Create user
GET    /api/users/{id}     - Get user
PUT    /api/users/{id}     - Update user
DELETE /api/users/{id}     - Delete user
POST   /api/orders         - Create order
GET    /api/orders/{id}    - Get order
```

### Exception Types to Handle

```java
// Domain exceptions (unchecked)
public class UserNotFoundException extends RuntimeException { }
public class DuplicateEmailException extends RuntimeException { }
public class OrderNotFoundException extends RuntimeException { }
public class InsufficientInventoryException extends RuntimeException { }
public class PaymentDeclinedException extends RuntimeException { }
public class ValidationException extends RuntimeException {
    private final String field;
}
public class ServiceException extends RuntimeException { }

// Framework exceptions
public class HttpRequestMethodNotSupportedException extends RuntimeException { }
public class HttpMediaTypeNotSupportedException extends RuntimeException { }
```

### Requirements

1. Create `@RestControllerAdvice` class
2. Map each exception to appropriate HTTP status code
3. Return structured error response (not raw exception message)
4. Log at appropriate levels (WARN for client errors, ERROR for server errors)
5. Include request context in logs (path, method)
6. Never expose stack traces or internal details to client
7. Handle unexpected exceptions gracefully

### Expected HTTP Status Mapping

```
UserNotFoundException       -> 404 Not Found
DuplicateEmailException     -> 409 Conflict
OrderNotFoundException      -> 404 Not Found
InsufficientInventoryException -> 409 Conflict
PaymentDeclinedException    -> 402 Payment Required
ValidationException         -> 400 Bad Request
ServiceException            -> 500 Internal Server Error
Unexpected Exception        -> 500 Internal Server Error
```

### Hints

- Use `@ExceptionHandler(ExceptionType.class)` for each exception
- Use `@ResponseStatus` or return `ResponseEntity`
- Create an `ErrorResponse` record or class for consistent response format
- Use `HttpServletRequest` parameter to get request context
- Log the exception object as the last argument to preserve stack trace
- Consider using `@ControllerAdvice(basePackages = "com.app.api")` to scope

---

## Exercise 6: Production Readiness Review

### Problem Statement

Perform a production readiness review on the following code. Create a checklist of all issues, then fix each one.

```java
@Service
public class PaymentService {

    private static final Logger log = Logger.getLogger(PaymentService.class);

    private PaymentGateway gateway;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;

    public PaymentResult processPayment(PaymentRequest request) {
        try {
            User user = userRepository.findById(request.getUserId());
            BigDecimal balance = gateway.getBalance(user.getId());

            if (balance.compareTo(request.getAmount()) < 0) {
                return PaymentResult.insufficientFunds();
            }

            Transaction tx = gateway.charge(user.getId(), request.getAmount());
            transactionRepository.save(tx);

            return PaymentResult.success(tx.getId());

        } catch (Exception e) {
            log.error("Payment failed");
            return PaymentResult.error("Something went wrong");
        }
    }

    public void refund(String transactionId) {
        try {
            Transaction tx = transactionRepository.findById(transactionId);
            gateway.refund(tx.getId(), tx.getAmount());
        } catch (Exception e) {
            log.error("Refund failed: " + e);
        }
    }
}
```

### Review Checklist

Evaluate each criterion and fix if needed:

```
+----------------------------------+------+------------------------------------+
| Criterion                        | Pass | Fix Required?                      |
+----------------------------------+------+------------------------------------+
| No generic Exception catches     |  [ ] |                                   |
| Specific exception types used    |  [ ] |                                   |
| Error messages include context   |  [ ] |                                   |
| No sensitive data in messages    |  [ ] |                                   |
| Logging includes stack trace     |  [ ] |                                   |
| Logging includes request context |  [ ] |                                   |
| Appropriate log levels           |  [ ] |                                   |
| No swallowed exceptions          |  [ ] |                                   |
| Resource management (if needed)  |  [ ] |                                   |
| No null returns for errors       |  [ ] |                                   |
| Idempotency considered           |  [ ] |                                   |
| Timeout handling                 |  [ ] |                                   |
| Retry logic for transient errors |  [ ] |                                   |
+----------------------------------+------+------------------------------------+
```

### Additional Requirements

After fixing the code:

1. Add timeout handling for gateway calls
2. Add idempotency key support for payment processing
3. Ensure refund is idempotent
4. Add circuit breaker pattern for gateway calls
5. Write a brief explanation of each production concern addressed

### Hints

- Consider what happens if the gateway call hangs
- Consider what happens if the process crashes after charge but before save
- Consider what happens if refund is called twice for the same transaction
- Consider what happens if the database is temporarily unavailable
- Consider what happens if the gateway is temporarily unavailable

---

*See also: [Decision Guide](../decision.md) | [Part 1: Exercises 1–4](README-Part1.md) | [Solutions](../02-solutions/README.md)*
