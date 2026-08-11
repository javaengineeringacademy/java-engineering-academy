# Exception Handling Best Practices: Practice Exercises

> Work through these exercises to apply exception handling best practices.

---

## Exercise 1: Fix Anti-Patterns in Code

### Problem Statement

The following code contains 8 exception handling anti-patterns. Identify all of them, explain why each is problematic, and rewrite the code following best practices.

```java
public class OrderProcessor {

    private static final Logger log = Logger.getLogger(OrderProcessor.class);

    public void processOrder(Order order) {
        try {
            validateOrder(order);
            reserveInventory(order);
            chargePayment(order);
            sendConfirmation(order);
        } catch (Exception e) {
            log.error("Error");
        }
    }

    private void validateOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("bad order");
        }
    }

    private void reserveInventory(Order order) {
        try {
            inventoryService.reserve(order.getItems());
        } catch (Exception e) {
            // do nothing
        }
    }

    private void chargePayment(Order order) {
        PaymentResult result = paymentService.charge(order.getTotal());
        if (result == null) {
            throw new RuntimeException("payment failed");
        }
    }

    private void sendConfirmation(Order order) {
        try {
            emailService.send(order.getCustomerEmail(), "Order confirmed");
        } catch (Exception e) {
            log.error("Email failed: " + e);
        }
    }
}
```

### Requirements

1. Identify all 8 anti-patterns
2. For each anti-pattern, explain the production risk
3. Rewrite each method with proper exception handling
4. Add appropriate custom exception types
5. Ensure all exception messages include context

### Hints

- Count the `catch` blocks and evaluate each one
- Look at exception messages - are they debuggable?
- Check what happens to the stack trace in each catch
- Consider what the caller learns when each exception occurs
- Think about which failures are critical vs non-critical

---

## Exercise 2: Write Proper Exception Messages

### Problem Statement

The following exception messages are poor. Rewrite each one to be production-quality. For each, explain what information is missing and why it matters.

```java
public class UserService {

    public void createUser(CreateUserRequest request) {
        if (request.getName() == null) {
            throw new IllegalArgumentException("invalid name");
        }

        if (request.getEmail() == null) {
            throw new IllegalArgumentException("bad email");
        }

        if (!request.getEmail().contains("@")) {
            throw new ValidationException("error");
        }

        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("duplicate");
        }

        if (request.getAge() < 0 || request.getAge() > 150) {
            throw new IllegalArgumentException("age problem");
        }

        repository.save(new User(request.getName(), request.getEmail(), request.getAge()));
    }
}
```

### Requirements

1. Rewrite each exception message following the anatomy: verb + context + details
2. Include relevant entity IDs or field values (safe ones only)
3. Ensure each message is under 200 characters
4. Verify no sensitive data would be included
5. Add a brief explanation for each change

### Hints

- Messages should start with "Failed to..." or "Cannot..."
- Include the field name and the invalid value
- Never log raw user input - consider what's safe to include
- Think about what a developer at 2 AM needs to debug this
- Consider whether the exception type itself needs to change

---

## Exercise 3: Implement Exception Translation

### Problem Statement

The following repository exposes low-level database exceptions to callers. Refactor it to use exception translation at the layer boundary.

```java
public class ProductRepository {

    private JdbcTemplate jdbcTemplate;

    public Product findById(String id) throws SQLException {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM products WHERE id = ?",
            productRowMapper, id);
    }

    public List<Product> findByCategory(String category) throws SQLException {
        return jdbcTemplate.query(
            "SELECT * FROM products WHERE category = ?",
            productRowMapper, category);
    }

    public void save(Product product) throws SQLException {
        jdbcTemplate.update(
            "INSERT INTO products (id, name, category, price) VALUES (?, ?, ?, ?)",
            product.getId(), product.getName(),
            product.getCategory(), product.getPrice());
    }

    public void delete(String id) throws SQLException {
        jdbcTemplate.update("DELETE FROM products WHERE id = ?", id);
    }

    public void updatePrice(String id, BigDecimal newPrice) throws SQLException {
        jdbcTemplate.update(
            "UPDATE products SET price = ? WHERE id = ?",
            newPrice, id);
    }
}
```

### Requirements

1. Create a domain exception hierarchy for the product repository
2. Include at minimum: base exception, not found, duplicate, validation
3. Translate each `SQLException` to the appropriate domain exception
4. Preserve original exceptions as causes
5. Remove `throws SQLException` from method signatures
6. Add Javadoc documenting the new exceptions

### Hints

- `EmptyResultDataAccessException` maps to "not found"
- `DuplicateKeyException` maps to "duplicate"
- Consider `DataIntegrityViolationException` for constraint violations
- Each domain exception should carry relevant data (product ID, duplicate field)
- The base exception should be unchecked (programming error if caller ignores it)

---

## Exercise 4: Write Tests for Exception Paths

### Problem Statement

Write comprehensive tests for the following service class. Cover all exception paths, verify no side effects after failures, and ensure correct operation ordering.

```java
public class ProductService {

    private final ProductRepository repository;
    private final InventoryService inventory;
    private final NotificationService notifications;

    public ProductService(ProductRepository repository,
                         InventoryService inventory,
                         NotificationService notifications) {
        this.repository = repository;
        this.inventory = inventory;
        this.notifications = notifications;
    }

    public Product createProduct(CreateProductRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Price must be positive, got: " + request.getPrice());
        }

        if (repository.existsByName(request.getName())) {
            throw new DuplicateProductException(request.getName());
        }

        Product product = new Product(
            request.getName(),
            request.getCategory(),
            request.getPrice()
        );

        repository.save(product);
        inventory.initializeStock(product.getId(), 0);
        notifications.notifyProductCreated(product);

        return product;
    }
}
```

### Test Scenarios to Cover

1. Successful product creation (happy path)
2. Null name validation
3. Blank name validation
4. Zero price validation
5. Negative price validation
6. Duplicate name handling
7. Repository failure during save
8. Inventory service failure after save
9. Notification service failure (non-critical)
10. Verify inventory not initialized if save fails
11. Verify notifications not sent if save fails

### Hints

- Use `@ExtendWith(MockitoExtension.class)` with Mockito
- Use `assertThrows` for exception verification
- Use `verify(..., never()).method(...)` to check no side effects
- Use `InOrder` to verify operation sequencing
- Test that notification failure does not prevent product creation

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

*See also: [Decision Guide](../decision.md) | [Examples](../00-examples/README.md) | [Solutions](../02-solutions/README.md)*
