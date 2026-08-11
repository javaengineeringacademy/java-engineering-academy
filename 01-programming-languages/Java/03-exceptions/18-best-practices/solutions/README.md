# Solutions: Exception Handling Best Practices

> Work through these solutions to validate your understanding.

---

## Solution 1: Anti-Pattern Identification

### Anti-Patterns Found:

1. **Catching generic Exception** in `processOrder()` - hides specific failures
2. **Empty catch block** in `reserveInventory()` - silently swallows inventory failures
3. **Returning null** in `chargePayment()` - defers NPE to caller
4. **Logging without exception** in `sendConfirmation()` - loses stack trace
5. **Poor error message** in `chargePayment()` - "Payment failed" is unhelpful

### Fixed Code:

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
            throw e;  // Let caller handle validation errors
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
                "Order must contain at least one item");
        }
        // Additional validation logic
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
                "Payment declined: " + result.getDeclineReason());
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

### Key Improvements:

- Specific exception types for different failure modes
- Meaningful error messages with context (order ID)
- No swallowed exceptions - all failures communicated
- Proper logging with exception stack traces
- Different handling for critical vs non-critical failures

---

## Solution 2: Resource Management

### Fixed Code:

```java
public class DataExporter {
    
    private static final Logger log = Logger.getLogger(DataExporter.class);
    
    public void exportUsers(String outputPath) throws ExportException {
        Objects.requireNonNull(outputPath, "Output path must not be null");
        
        String sql = "SELECT id, name, email FROM users";
        
        // try-with-resources: all resources auto-closed in reverse order
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery();
             FileWriter writer = new FileWriter(outputPath);
             BufferedWriter bw = new BufferedWriter(writer)) {
            
            int rowCount = 0;
            
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                
                String line = String.format("%s,%s%n", name, email);
                bw.write(line);
                rowCount++;
            }
            
            bw.flush();
            log.info("Exported {} users to {}", rowCount, outputPath);
            
        } catch (SQLException e) {
            throw new ExportException(
                "Database error during user export", e);
        } catch (IOException e) {
            throw new ExportException(
                "File write error during user export to: " + outputPath, e);
        }
    }
}
```

### Key Improvements:

- Single try-with-resources block for all resources
- Automatic cleanup in reverse declaration order
- Specific exception handling for SQL vs IO errors
- Meaningful error messages with context
- Resources declared in dependency order

---

## Solution 3: Exception Translation

### New Exception Classes:

```java
/**
 * Base exception for user repository operations.
 */
public class UserRepositoryException extends RuntimeException {
    
    public UserRepositoryException(String message) {
        super(message);
    }
    
    public UserRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * Thrown when a user is not found.
 */
public class UserNotFoundException extends UserRepositoryException {
    
    private final String userId;
    
    public UserNotFoundException(String userId) {
        super("User not found: " + userId);
        this.userId = userId;
    }
    
    public UserNotFoundException(String userId, Throwable cause) {
        super("User not found: " + userId, cause);
        this.userId = userId;
    }
    
    public String getUserId() {
        return userId;
    }
}

/**
 * Thrown when a user already exists.
 */
public class DuplicateUserException extends UserRepositoryException {
    
    private final String duplicateField;
    private final String fieldValue;
    
    public DuplicateUserException(String duplicateField, String fieldValue) {
        super("User already exists with " + duplicateField + ": " + fieldValue);
        this.duplicateField = duplicateField;
        this.fieldValue = fieldValue;
    }
    
    public String getDuplicateField() {
        return duplicateField;
    }
    
    public String getFieldValue() {
        return fieldValue;
    }
}
```

### Refactored Repository:

```java
/**
 * Repository for user data access.
 * 
 * <p>All database exceptions are translated to domain-specific exceptions.
 * Original exceptions are preserved as causes for debugging.</p>
 */
public class UserRepository {
    
    private static final Logger log = Logger.getLogger(UserRepository.class);
    
    private final JdbcTemplate jdbcTemplate;
    
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * Find a user by ID.
     * 
     * @param id the user ID
     * @return the user
     * @throws UserNotFoundException if user doesn't exist
     * @throws UserRepositoryException if database access fails
     */
    public User findById(String id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id, e);
        } catch (DataAccessException e) {
            throw new UserRepositoryException(
                "Failed to find user by id: " + id, e);
        }
    }
    
    /**
     * Find users by email.
     * 
     * @param email the email address
     * @return list of users with matching email
     * @throws UserRepositoryException if database access fails
     */
    public List<User> findByEmail(String email) {
        try {
            return jdbcTemplate.query(
                "SELECT * FROM users WHERE email = ?",
                userRowMapper, email);
        } catch (DataAccessException e) {
            throw new UserRepositoryException(
                "Failed to find users by email: " + email, e);
        }
    }
    
    /**
     * Save a new user.
     * 
     * @param user the user to save
     * @throws DuplicateUserException if user with same email exists
     * @throws UserRepositoryException if database access fails
     */
    public void save(User user) {
        try {
            jdbcTemplate.update(
                "INSERT INTO users (id, name, email) VALUES (?, ?, ?)",
                user.getId(), user.getName(), user.getEmail());
        } catch (DuplicateKeyException e) {
            throw new DuplicateUserException("email", user.getEmail(), e);
        } catch (DataAccessException e) {
            throw new UserRepositoryException(
                "Failed to save user: " + user.getId(), e);
        }
    }
    
    /**
     * Delete a user by ID.
     * 
     * @param id the user ID
     * @throws UserNotFoundException if user doesn't exist
     * @throws UserRepositoryException if database access fails
     */
    public void delete(String id) {
        try {
            int rowsAffected = jdbcTemplate.update(
                "DELETE FROM users WHERE id = ?", id);
            
            if (rowsAffected == 0) {
                throw new UserNotFoundException(id);
            }
        } catch (UserNotFoundException e) {
            throw e;  // Re-throw domain exception
        } catch (DataAccessException e) {
            throw new UserRepositoryException(
                "Failed to delete user: " + id, e);
        }
    }
}
```

### Key Improvements:

- Custom exception hierarchy for different failure types
- Original exceptions preserved as causes
- Meaningful exception messages with context
- Repository exceptions are unchecked (programming errors)
- Proper Javadoc for all public methods

---

## Solution 4: Logging Quality

### Improved Code:

```java
public class PaymentProcessor {
    
    private static final Logger log = Logger.getLogger(PaymentProcessor.class);
    
    // Mask for sensitive data
    private static final Pattern CARD_PATTERN = Pattern.compile("\\d{12}(\\d{4})");
    
    public PaymentResult processPayment(PaymentRequest request) {
        Objects.requireNonNull(request, "Payment request must not be null");
        
        String requestId = request.getRequestId();
        String maskedCard = maskCardNumber(request.getCardNumber());
        
        log.debug("Processing payment: requestId={}, card={}, amount={}, currency={}", 
            requestId, maskedCard, request.getAmount(), request.getCurrency());
        
        try {
            validateRequest(request);
            
            PaymentResult result = paymentGateway.charge(
                request.getCardNumber(),
                request.getAmount(),
                request.getCurrency()
            );
            
            log.info("Payment processed successfully: requestId={}, result={}", 
                requestId, result.getStatus());
            return result;
            
        } catch (ValidationException e) {
            log.warn("Payment validation failed: requestId={}, reason={}", 
                requestId, e.getMessage());
            return PaymentResult.validationError(e.getMessage());
            
        } catch (PaymentDeclinedException e) {
            log.warn("Payment declined: requestId={}, card={}, reason={}", 
                requestId, maskedCard, e.getDeclineReason());
            return PaymentResult.declined(e.getDeclineReason());
            
        } catch (PaymentGatewayException e) {
            log.error("Payment gateway error: requestId={}, card={}, amount={}: {}", 
                requestId, maskedCard, request.getAmount(), e.getMessage(), e);
            return PaymentResult.gatewayError("Payment processing unavailable");
            
        } catch (Exception e) {
            log.error("Unexpected error processing payment: requestId={}, card={}, amount={}: {}", 
                requestId, maskedCard, request.getAmount(), e.getMessage(), e);
            return PaymentResult.internalError("An unexpected error occurred");
        }
    }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null) {
            return "null";
        }
        Matcher matcher = CARD_PATTERN.matcher(cardNumber);
        if (matcher.find()) {
            return "****" + matcher.group(1);
        }
        return "****";
    }
}
```

### Key Improvements:

- Each exception type logged at appropriate level
- All log messages include relevant context (requestId, masked card)
- Exception object included in log calls for stack trace
- No sensitive data logged (card numbers masked)
- Different log levels: DEBUG for entry, WARN for expected failures, ERROR for unexpected

---

## Solution 5: Exception Hierarchy Design

### Exception Hierarchy Diagram:

```
Exception
├── RuntimeException
│   ├── IllegalArgumentException
│   ├── IllegalStateException
│   └── ECommerceException (base for all app exceptions)
│       ├── ValidationException
│       │   ├── InvalidEmailException
│       │   ├── InvalidAddressException
│       │   └── InvalidPaymentException
│       ├── OrderException
│       │   ├── OrderNotFoundException
│       │   ├── OrderAlreadyProcessedException
│       │   └── InsufficientInventoryException
│       ├── PaymentException
│       │   ├── PaymentDeclinedException
│       │   ├── PaymentGatewayException
│       │   └── InvalidCardException
│       ├── UserException
│       │   ├── UserNotFoundException
│       │   └── DuplicateUserException
│       └── InventoryException
│           ├── ProductNotFoundException
│           └── OutOfStockException
├── IOException
│   └── DatabaseAccessException
└── Exception (checked)
    └── ExternalServiceException
```

### Exception Classes:

```java
// Base exception for all e-commerce domain exceptions
public class ECommerceException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public ECommerceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public ECommerceException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

// Validation exceptions (unchecked - programming errors)
public class ValidationException extends ECommerceException {
    
    private final String field;
    
    public ValidationException(String field, String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
        this.field = field;
    }
    
    public String getField() {
        return field;
    }
}

// Order exceptions
public class OrderNotFoundException extends ECommerceException {
    
    public OrderNotFoundException(String orderId) {
        super(ErrorCode.ORDER_NOT_FOUND, "Order not found: " + orderId);
    }
}

// Payment exceptions
public class PaymentDeclinedException extends ECommerceException {
    
    private final String declineReason;
    
    public PaymentDeclinedException(String declineReason) {
        super(ErrorCode.PAYMENT_DECLINED, "Payment declined: " + declineReason);
        this.declineReason = declineReason;
    }
    
    public String getDeclineReason() {
        return declineReason;
    }
}

// External service exceptions (checked - caller must handle)
public class ExternalServiceException extends Exception {
    
    private final String serviceName;
    
    public ExternalServiceException(String serviceName, String message, Throwable cause) {
        super(message, cause);
        this.serviceName = serviceName;
    }
    
    public String getServiceName() {
        return serviceName;
    }
}
```

### Usage Examples:

```java
// Validation - unchecked (programming error if request is invalid)
public void validateOrder(CreateOrderRequest request) {
    if (request.getCustomerId() == null) {
        throw new ValidationException("customerId", "Customer ID is required");
    }
}

// Business rule violation - unchecked
public void processOrder(String orderId) {
    Order order = orderRepository.findById(orderId);
    if (order == null) {
        throw new OrderNotFoundException(orderId);
    }
}

// External service failure - checked (caller must handle)
public User fetchUserFromExternalService(String id) throws ExternalServiceException {
    try {
        return externalUserService.getUser(id);
    } catch (java.io.IOException e) {
        throw new ExternalServiceException("UserService", "Failed to fetch user", e);
    }
}
```

---

## Solution 6: Testing Exception Handling

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository repository;
    
    @Mock
    private EmailService emailService;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("John", "john@example.com");
        when(repository.existsByEmail("john@example.com")).thenReturn(false);
        
        // Act
        User result = userService.createUser(request);
        
        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(repository).save(any(User.class));
        verify(emailService).sendWelcomeEmail(any(User.class));
    }
    
    @Test
    @DisplayName("Should throw when email is null")
    void shouldThrowWhenEmailIsNull() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("John", null);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.createUser(request)
        );
        
        assertTrue(exception.getMessage().contains("Email"));
        verify(repository, never()).save(any());
        verify(emailService, never()).sendWelcomeEmail(any());
    }
    
    @Test
    @DisplayName("Should throw when email already exists")
    void shouldThrowWhenEmailAlreadyExists() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("John", "existing@example.com");
        when(repository.existsByEmail("existing@example.com")).thenReturn(true);
        
        // Act & Assert
        DuplicateEmailException exception = assertThrows(
            DuplicateEmailException.class,
            () -> userService.createUser(request)
        );
        
        assertEquals("existing@example.com", exception.getEmail());
        verify(repository, never()).save(any());
        verify(emailService, never()).sendWelcomeEmail(any());
    }
    
    @Test
    @DisplayName("Should throw when repository fails")
    void shouldThrowWhenRepositoryFails() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("John", "john@example.com");
        when(repository.existsByEmail("john@example.com")).thenReturn(false);
        when(repository.save(any())).thenThrow(new DataAccessException("DB error") {});
        
        // Act & Assert
        assertThrows(
            DataAccessException.class,
            () -> userService.createUser(request)
        );
        verify(emailService, never()).sendWelcomeEmail(any());
    }
    
    @Test
    @DisplayName("Should handle email service failure gracefully")
    void shouldHandleEmailServiceFailureGracefully() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("John", "john@example.com");
        when(repository.existsByEmail("john@example.com")).thenReturn(false);
        doThrow(new EmailException("Email service down"))
            .when(emailService).sendWelcomeEmail(any());
        
        // Act
        User result = userService.createUser(request);
        
        // Assert: User should still be created even if email fails
        assertNotNull(result);
        verify(repository).save(any(User.class));
    }
    
    @Test
    @DisplayName("Should verify email sent after user saved")
    void shouldVerifyEmailSentAfterUserSaved() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("John", "john@example.com");
        when(repository.existsByEmail("john@example.com")).thenReturn(false);
        
        InOrder inOrder = inOrder(repository, emailService);
        
        // Act
        userService.createUser(request);
        
        // Assert: User saved before email sent
        inOrder.verify(repository).save(any(User.class));
        inOrder.verify(emailService).sendWelcomeEmail(any(User.class));
    }
}
```

### Test Coverage Summary:

| Scenario | Test Method | Expected Outcome |
|----------|-------------|------------------|
| Happy path | `shouldCreateUserSuccessfully` | User created, email sent |
| Null email | `shouldThrowWhenEmailIsNull` | IllegalArgumentException, no save |
| Duplicate email | `shouldThrowWhenEmailAlreadyExists` | DuplicateEmailException, no save |
| Repository failure | `shouldThrowWhenRepositoryFails` | DataAccessException, no email |
| Email failure | `shouldHandleEmailServiceFailureGracefully` | User created, email failure logged |
| Operation order | `shouldVerifyEmailSentAfterUserSaved` | Save before email verified |

---

## Solution 7: Production Readiness Review

### Issues Found:

1. **Generic Exception catch** - catches everything, masks specific failures
2. **No exception in log** - loses stack trace information
3. **Generic error message** - "Internal error" doesn't help debugging
4. **No resource management** - external service connection not closed
5. **No timeout handling** - external service could hang
6. **No retry logic** - transient failures not handled
7. **No circuit breaker** - cascading failures possible
8. **No request context** - cannot trace request through system
9. **Implementation details exposed** - error type visible to caller

### Fixed Code:

```java
public class ApiService {
    
    private static final Logger log = Logger.getLogger(ApiService.class);
    
    private final ExternalServiceClient externalClient;
    private final CircuitBreaker circuitBreaker;
    private final RequestIdGenerator idGenerator;
    
    public ApiResponse handleRequest(ApiRequest request) {
        String requestId = idGenerator.generate();
        
        log.debug("Processing request: requestId={}, type={}", 
            requestId, request.getType());
        
        try {
            Object result = processRequest(request, requestId);
            return ApiResponse.success(result);
            
        } catch (ValidationException e) {
            log.warn("Validation failed: requestId={}, reason={}", 
                requestId, e.getMessage());
            return ApiResponse.validationError(e.getMessage());
            
        } catch (ExternalServiceException e) {
            log.error("External service failed: requestId={}, service={}: {}", 
                requestId, e.getServiceName(), e.getMessage(), e);
            return ApiResponse.serviceError(
                "Service temporarily unavailable");
            
        } catch (ApiException e) {
            log.error("API error: requestId={}, code={}: {}", 
                requestId, e.getErrorCode(), e.getMessage(), e);
            return ApiResponse.error(e.getErrorCode(), e.getMessage());
            
        } catch (Exception e) {
            log.error("Unexpected error: requestId={}: {}", 
                requestId, e.getMessage(), e);
            return ApiResponse.internalError(
                "An unexpected error occurred");
        }
    }
    
    private Object processRequest(ApiRequest request, String requestId) 
            throws ExternalServiceException, ApiException {
        
        // Validate request
        validateRequest(request);
        
        // Call external service with circuit breaker
        ExternalResponse response = circuitBreaker.execute(() -> 
            externalClient.call(request, requestId)
        );
        
        // Transform response
        return transform(response, requestId);
    }
    
    private void validateRequest(ApiRequest request) throws ValidationException {
        if (request == null) {
            throw new ValidationException("Request must not be null");
        }
        if (request.getType() == null) {
            throw new ValidationException("Request type is required");
        }
    }
    
    private Object transform(ExternalResponse response, String requestId) 
            throws ApiException {
        if (response == null) {
            throw new ApiException(ErrorCode.NULL_RESPONSE, 
                "External service returned null response");
        }
        
        try {
            return transformer.transform(response);
        } catch (TransformationException e) {
            throw new ApiException(ErrorCode.TRANSFORMATION_FAILED, 
                "Failed to transform response", e);
        }
    }
}
```

### Additional Production Considerations:

```java
// Configuration for resilience
@Configuration
public class ResilienceConfig {
    
    @Bean
    public CircuitBreaker circuitBreaker() {
        return CircuitBreaker.builder()
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofSeconds(30))
            .ringBufferSizeInHalfOpenState(5)
            .ringBufferSizeInClosedState(10)
            .build();
    }
    
    @Bean
    public Retry retry() {
        return Retry.builder()
            .maxAttempts(3)
            .waitDuration(Duration.ofMillis(500))
            .retryOnException(ExternalServiceException.class)
            .build();
    }
}
```

### Production Checklist Items Addressed:

- [x] Specific exception types for different failures
- [x] Informative error messages with request context
- [x] No sensitive data exposed in responses
- [x] Complete logging with request tracing
- [x] Resource management with circuit breaker
- [x] External service failures handled
- [x] Timeout handling via circuit breaker
- [x] Retry logic for transient failures
- [x] Response doesn't leak implementation details
- [x] Monitoring possible via request IDs

---

*Return to [Examples](../examples/README.md)*
