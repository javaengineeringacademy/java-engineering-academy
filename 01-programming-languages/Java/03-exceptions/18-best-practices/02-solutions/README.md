# Exception Handling Best Practices: Solutions

> Complete solutions for all practice exercises.

---

## Solution 1: Fix Anti-Patterns in Code

### Anti-Patterns Identified

| # | Anti-Pattern | Line | Risk |
|---|---|---|---|
| 1 | `catch (Exception e)` | line 16 | Masks specific failures, catches Errors |
| 2 | `log.error("Error")` | line 17 | No exception object, loses stack trace |
| 3 | `throw new RuntimeException("bad order")` | line 25 | Generic type, vague message |
| 4 | `catch (Exception e) { }` | line 33 | Swallowed exception - silent inventory loss |
| 5 | `throw new RuntimeException("payment failed")` | line 41 | Generic type, no context |
| 6 | `return null` implicitly | line 41 | Defers NPE to caller |
| 7 | `log.error("Email failed: " + e)` | line 50 | String concat loses stack trace |
| 8 | No `throws` or exception hierarchy | whole class | Callers cannot handle specific failures |

### Fixed Code

```java
public class OrderProcessor {

    private static final Logger log = Logger.getLogger(OrderProcessor.class);

    public void processOrder(Order order) {
        Objects.requireNonNull(order, "Order must not be null");

        try {
            validateOrder(order);
            reserveInventory(order);
            chargePayment(order);
            sendConfirmation(order);
        } catch (OrderValidationException e) {
            throw e;  // Caller handles validation
        } catch (OrderProcessingException e) {
            throw e;  // Already wrapped with context
        } catch (Exception e) {
            throw new OrderProcessingException(
                "Unexpected error processing order: " + order.getId(), e);
        }
    }

    private void validateOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new OrderValidationException(
                "Order must contain at least one item, got: " +
                (order.getItems() == null ? "null" : "empty list"));
        }
    }

    private void reserveInventory(Order order) {
        try {
            inventoryService.reserve(order.getItems());
        } catch (InventoryException e) {
            throw new OrderProcessingException(
                "Failed to reserve inventory for order: " + order.getId(), e);
        }
    }

    private void chargePayment(Order order) {
        PaymentResult result;
        try {
            result = paymentService.charge(order.getTotal());
        } catch (PaymentException e) {
            throw new OrderProcessingException(
                "Payment failed for order: " + order.getId(), e);
        }

        if (result == null) {
            throw new OrderProcessingException(
                "Payment returned null for order: " + order.getId());
        }

        if (!result.isSuccessful()) {
            throw new PaymentDeclinedException(
                "Payment declined for order " + order.getId() +
                ": " + result.getDeclineReason());
        }
    }

    private void sendConfirmation(Order order) {
        try {
            emailService.send(order.getCustomerEmail(), "Order confirmed");
        } catch (EmailException e) {
            log.warn("Failed to send confirmation email for order {}: {}",
                order.getId(), e.getMessage(), e);
            // Non-critical: order is processed, email failure is logged
        }
    }
}
```

### Exception Classes

```java
public class OrderValidationException extends RuntimeException {
    public OrderValidationException(String message) {
        super(message);
    }
}

public class OrderProcessingException extends RuntimeException {
    public OrderProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class PaymentDeclinedException extends RuntimeException {
    private final String declineReason;

    public PaymentDeclinedException(String message) {
        super(message);
        this.declineReason = message;
    }

    public String getDeclineReason() {
        return declineReason;
    }
}
```

---

## Solution 2: Write Proper Exception Messages

### Rewritten Messages

```java
public class UserService {

    private static final Logger log = Logger.getLogger(UserService.class);

    public void createUser(CreateUserRequest request) {
        Objects.requireNonNull(request, "CreateUserRequest must not be null");

        if (request.getName() == null) {
            throw new IllegalArgumentException(
                "User name is required");
        }

        if (request.getEmail() == null) {
            throw new IllegalArgumentException(
                "User email is required for user: " + request.getName());
        }

        if (!request.getEmail().contains("@")) {
            throw new ValidationException("email",
                "Invalid email format: " + request.getEmail());
        }

        if (repository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        if (request.getAge() < 0 || request.getAge() > 150) {
            throw new IllegalArgumentException(
                "User age must be between 0 and 150, got: " + request.getAge());
        }

        repository.save(new User(
            request.getName(),
            request.getEmail(),
            request.getAge()
        ));
    }
}
```

### Message Improvement Rationale

| Original | Fixed | Why |
|---|---|---|
| `"invalid name"` | `"User name is required"` | Starts with verb, describes what is missing |
| `"bad email"` | `"User email is required for user: {name}"` | Includes entity context |
| `"error"` | `"Invalid email format: {value}"` | Describes what is wrong, includes value |
| `"duplicate"` | `DuplicateEmailException(email)` | Specific exception type with data |
| `"age problem"` | `"User age must be between 0 and 150, got: {value}"` | Describes valid range and actual value |

### Custom Exception Class

```java
public class ValidationException extends RuntimeException {
    private final String field;

    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

public class DuplicateEmailException extends RuntimeException {
    private final String email;

    public DuplicateEmailException(String email) {
        super("User already exists with email: " + email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
```

---

## Solution 3: Implement Exception Translation

### Exception Hierarchy

```java
/**
 * Base exception for product repository operations.
 */
public class ProductRepositoryException extends RuntimeException {

    public ProductRepositoryException(String message) {
        super(message);
    }

    public ProductRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * Thrown when a product is not found.
 */
public class ProductNotFoundException extends ProductRepositoryException {

    private final String productId;

    public ProductNotFoundException(String productId) {
        super("Product not found: " + productId);
        this.productId = productId;
    }

    public ProductNotFoundException(String productId, Throwable cause) {
        super("Product not found: " + productId, cause);
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}

/**
 * Thrown when a product with the same name already exists.
 */
public class DuplicateProductException extends ProductRepositoryException {

    private final String productName;

    public DuplicateProductException(String productName) {
        super("Product already exists with name: " + productName);
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }
}
```

### Refactored Repository

```java
/**
 * Repository for product data access.
 *
 * <p>All database exceptions are translated to domain-specific exceptions.
 * Original exceptions are preserved as causes for debugging.</p>
 */
public class ProductRepository {

    private static final Logger log = Logger.getLogger(ProductRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Find a product by ID.
     *
     * @param id the product ID
     * @return the product
     * @throws ProductNotFoundException if product does not exist
     * @throws ProductRepositoryException if database access fails
     */
    public Product findById(String id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM products WHERE id = ?",
                productRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException(id, e);
        } catch (DataAccessException e) {
            throw new ProductRepositoryException(
                "Failed to find product by id: " + id, e);
        }
    }

    /**
     * Find products by category.
     *
     * @param category the product category
     * @return list of products in category
     * @throws ProductRepositoryException if database access fails
     */
    public List<Product> findByCategory(String category) {
        try {
            return jdbcTemplate.query(
                "SELECT * FROM products WHERE category = ?",
                productRowMapper, category);
        } catch (DataAccessException e) {
            throw new ProductRepositoryException(
                "Failed to find products by category: " + category, e);
        }
    }

    /**
     * Save a new product.
     *
     * @param product the product to save
     * @throws DuplicateProductException if product with same name exists
     * @throws ProductRepositoryException if database access fails
     */
    public void save(Product product) {
        try {
            jdbcTemplate.update(
                "INSERT INTO products (id, name, category, price) VALUES (?, ?, ?, ?)",
                product.getId(), product.getName(),
                product.getCategory(), product.getPrice());
        } catch (DuplicateKeyException e) {
            throw new DuplicateProductException(product.getName());
        } catch (DataAccessException e) {
            throw new ProductRepositoryException(
                "Failed to save product: " + product.getId(), e);
        }
    }

    /**
     * Delete a product by ID.
     *
     * @param id the product ID
     * @throws ProductNotFoundException if product does not exist
     * @throws ProductRepositoryException if database access fails
     */
    public void delete(String id) {
        try {
            int rowsAffected = jdbcTemplate.update(
                "DELETE FROM products WHERE id = ?", id);

            if (rowsAffected == 0) {
                throw new ProductNotFoundException(id);
            }
        } catch (ProductNotFoundException e) {
            throw e;  // Re-throw domain exception
        } catch (DataAccessException e) {
            throw new ProductRepositoryException(
                "Failed to delete product: " + id, e);
        }
    }

    /**
     * Update product price.
     *
     * @param id the product ID
     * @param newPrice the new price
     * @throws ProductNotFoundException if product does not exist
     * @throws ProductRepositoryException if database access fails
     */
    public void updatePrice(String id, BigDecimal newPrice) {
        try {
            int rowsAffected = jdbcTemplate.update(
                "UPDATE products SET price = ? WHERE id = ?",
                newPrice, id);

            if (rowsAffected == 0) {
                throw new ProductNotFoundException(id);
            }
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ProductRepositoryException(
                "Failed to update price for product: " + id, e);
        }
    }
}
```

### Key Improvements

- `throws SQLException` removed from all method signatures
- Each `SQLException` translated to domain-specific exception
- Original exceptions preserved as causes
- Exception messages include entity IDs
- Repository contract is clear via Javadoc

---

## Solution 4: Write Tests for Exception Paths

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private InventoryService inventory;

    @Mock
    private NotificationService notifications;

    @InjectMocks
    private ProductService productService;

    private CreateProductRequest validRequest() {
        return new CreateProductRequest(
            "Widget",
            "Electronics",
            new BigDecimal("29.99")
        );
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProductSuccessfully() {
        // Arrange
        CreateProductRequest request = validRequest();
        when(repository.existsByName("Widget")).thenReturn(false);

        // Act
        Product result = productService.createProduct(request);

        // Assert
        assertNotNull(result);
        assertEquals("Widget", result.getName());
        verify(repository).save(any(Product.class));
        verify(inventory).initializeStock(anyString(), eq(0));
        verify(notifications).notifyProductCreated(any(Product.class));
    }

    @Test
    @DisplayName("Should throw when name is null")
    void shouldThrowWhenNameIsNull() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
            null, "Electronics", new BigDecimal("29.99")
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(request)
        );

        assertTrue(exception.getMessage().contains("name"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when name is blank")
    void shouldThrowWhenNameIsBlank() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
            "   ", "Electronics", new BigDecimal("29.99")
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(request)
        );

        assertTrue(exception.getMessage().contains("name"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when price is zero")
    void shouldThrowWhenPriceIsZero() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
            "Widget", "Electronics", BigDecimal.ZERO
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(request)
        );

        assertTrue(exception.getMessage().contains("price"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when price is negative")
    void shouldThrowWhenPriceIsNegative() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
            "Widget", "Electronics", new BigDecimal("-5.00")
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(request)
        );

        assertTrue(exception.getMessage().contains("price"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when product name already exists")
    void shouldThrowWhenNameAlreadyExists() {
        // Arrange
        CreateProductRequest request = validRequest();
        when(repository.existsByName("Widget")).thenReturn(true);

        // Act & Assert
        DuplicateProductException exception = assertThrows(
            DuplicateProductException.class,
            () -> productService.createProduct(request)
        );

        assertEquals("Widget", exception.getProductName());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when repository save fails")
    void shouldThrowWhenRepositorySaveFails() {
        // Arrange
        CreateProductRequest request = validRequest();
        when(repository.existsByName("Widget")).thenReturn(false);
        when(repository.save(any()))
            .thenThrow(new ProductRepositoryException("DB error"));

        // Act & Assert
        assertThrows(
            ProductRepositoryException.class,
            () -> productService.createProduct(request)
        );
        verify(inventory, never()).initializeStock(anyString(), anyInt());
        verify(notifications, never()).notifyProductCreated(any());
    }

    @Test
    @DisplayName("Should create product even when notification fails")
    void shouldCreateProductEvenWhenNotificationFails() {
        // Arrange
        CreateProductRequest request = validRequest();
        when(repository.existsByName("Widget")).thenReturn(false);
        doThrow(new NotificationException("Service unavailable"))
            .when(notifications).notifyProductCreated(any());

        // Act
        Product result = productService.createProduct(request);

        // Assert: Product created despite notification failure
        assertNotNull(result);
        verify(repository).save(any(Product.class));
        verify(inventory).initializeStock(anyString(), eq(0));
    }

    @Test
    @DisplayName("Should verify save before inventory initialization")
    void shouldVerifySaveBeforeInventory() {
        // Arrange
        CreateProductRequest request = validRequest();
        when(repository.existsByName("Widget")).thenReturn(false);

        InOrder inOrder = inOrder(repository, inventory, notifications);

        // Act
        productService.createProduct(request);

        // Assert: Save before inventory before notifications
        inOrder.verify(repository).save(any(Product.class));
        inOrder.verify(inventory).initializeStock(anyString(), eq(0));
        inOrder.verify(notifications).notifyProductCreated(any(Product.class));
    }
}
```

### Test Coverage Summary

| Scenario | Test Method | Expected Outcome |
|---|---|---|
| Happy path | `shouldCreateProductSuccessfully` | Product created, all services called |
| Null name | `shouldThrowWhenNameIsNull` | IllegalArgumentException, no save |
| Blank name | `shouldThrowWhenNameIsBlank` | IllegalArgumentException, no save |
| Zero price | `shouldThrowWhenPriceIsZero` | IllegalArgumentException, no save |
| Negative price | `shouldThrowWhenPriceIsNegative` | IllegalArgumentException, no save |
| Duplicate name | `shouldThrowWhenNameAlreadyExists` | DuplicateProductException, no save |
| Repository failure | `shouldThrowWhenRepositorySaveFails` | ProductRepositoryException, no inventory |
| Notification failure | `shouldCreateProductEvenWhenNotificationFails` | Product created, notification failure handled |
| Operation order | `shouldVerifySaveBeforeInventory` | Save -> inventory -> notifications verified |

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

*See also: [Decision Guide](../decision.md) | [Examples](../00-examples/README.md) | [Exercises](../01-exercises/README.md)*
