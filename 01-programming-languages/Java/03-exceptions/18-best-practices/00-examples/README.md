# Exception Handling Best Practices: Code Examples

> Practical examples demonstrating good and bad exception handling patterns.

---

## Example 1: Good vs Bad Exception Patterns

### Bad: Generic Catch + Swallowed Exception

```java
public class BadOrderService {

    public void processOrder(Order order) {
        try {
            validate(order);
            paymentService.charge(order);
            inventoryService.reserve(order);
            notificationService.send(order);
        } catch (Exception e) {
            // Problem 1: catches everything including Errors
            // Problem 2: empty catch block swallows failure
            // Problem 3: caller has no idea order failed
        }
    }
}
```

**Why this fails in production:** The order silently fails. Inventory is reserved but not charged. Customer receives no notification. No logs exist. The only evidence is an orphaned inventory reservation that nobody knows about.

### Good: Specific Exceptions + Proper Handling

```java
public class GoodOrderService {

    private static final Logger log = Logger.getLogger(GoodOrderService.class);

    public void processOrder(Order order) {
        Objects.requireNonNull(order, "Order must not be null");

        try {
            validate(order);
            paymentService.charge(order);
            inventoryService.reserve(order);
            notificationService.send(order);
        } catch (ValidationException e) {
            // Caller can fix validation errors
            throw e;
        } catch (PaymentException e) {
            // Payment failed - order cannot proceed
            throw new OrderProcessingException(
                "Payment failed for order: " + order.getId(), e);
        } catch (InventoryException e) {
            // Inventory reservation failed - refund payment
            paymentService.refund(order);
            throw new OrderProcessingException(
                "Inventory unavailable for order: " + order.getId(), e);
        } catch (NotificationException e) {
            // Non-critical: log and continue
            log.warn("Failed to send notification for order {}: {}",
                order.getId(), e.getMessage());
        }
    }
}
```

**Why this works:** Each failure type has a specific handler. Critical failures propagate with context. Non-critical failures are logged. The caller receives actionable exceptions.

---

## Example 2: Proper Exception Logging

### Bad: Logging Without Stack Trace

```java
public class BadPaymentProcessor {

    private static final Logger log = Logger.getLogger(BadPaymentProcessor.class);

    public PaymentResult process(PaymentRequest request) {
        try {
            return gateway.charge(request);
        } catch (PaymentException e) {
            // Problem 1: e.toString() loses stack trace
            // Problem 2: no request context
            // Problem 3: wrong log level for expected failure
            log.error("Payment failed: " + e.toString());
            return null;
        } catch (Exception e) {
            // Problem 4: generic message, no exception object
            log.error("Error processing payment");
            return null;
        }
    }
}
```

### Good: Complete Logging Practice

```java
public class GoodPaymentProcessor {

    private static final Logger log = Logger.getLogger(GoodPaymentProcessor.class);
    private static final Pattern CARD_PATTERN = Pattern.compile("\\d{12}(\\d{4})");

    public PaymentResult process(PaymentRequest request) {
        Objects.requireNonNull(request, "Request must not be null");

        String requestId = request.getRequestId();
        String maskedCard = maskCardNumber(request.getCardNumber());

        log.debug("Processing payment: requestId={}, card={}, amount={}",
            requestId, maskedCard, request.getAmount());

        try {
            PaymentResult result = gateway.charge(request);

            log.info("Payment processed: requestId={}, status={}",
                requestId, result.getStatus());
            return result;

        } catch (PaymentDeclinedException e) {
            // Expected failure - WARN level, no stack trace needed
            log.warn("Payment declined: requestId={}, card={}, reason={}",
                requestId, maskedCard, e.getDeclineReason());
            return PaymentResult.declined(e.getDeclineReason());

        } catch (PaymentException e) {
            // Unexpected failure - ERROR level with stack trace
            log.error("Payment gateway error: requestId={}, card={}: {}",
                requestId, maskedCard, e.getMessage(), e);
            return PaymentResult.error("Payment service unavailable");

        } catch (Exception e) {
            // Catch-all - always include exception object
            log.error("Unexpected error: requestId={}: {}",
                requestId, e.getMessage(), e);
            return PaymentResult.error("Internal error");
        }
    }

    private String maskCardNumber(String card) {
        if (card == null || card.length() < 4) return "****";
        Matcher matcher = CARD_PATTERN.matcher(card);
        return matcher.find() ? "****" + matcher.group(1) : "****";
    }
}
```

**Key differences:** Request ID in every log line. Masked card numbers (no sensitive data). Exception object passed as last argument (preserves stack trace). Appropriate log levels for each failure type.

---

## Example 3: Exception Translation

### Bad: Exposing Low-Level Exceptions

```java
public class BadUserRepository {

    private JdbcTemplate jdbcTemplate;

    // Problem: callers must handle SQLException - a low-level detail
    public User findById(String id) throws SQLException {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE id = ?",
            userRowMapper, id);
    }

    public void save(User user) throws SQLException {
        jdbcTemplate.update(
            "INSERT INTO users (id, name, email) VALUES (?, ?, ?)",
            user.getId(), user.getName(), user.getEmail());
    }
}
```

**Why this is wrong:** Service layer must catch `SQLException` and translate it anyway. Every caller must declare `throws SQLException`. The exception type tells callers nothing about what went wrong in domain terms.

### Good: Exception Translation at Layer Boundary

```java
public class GoodUserRepository {

    private static final Logger log = Logger.getLogger(GoodUserRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public GoodUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Find a user by ID.
     *
     * @param id the user ID
     * @return the user
     * @throws UserNotFoundException if user does not exist
     * @throws DataAccessException if database access fails
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
}
```

**Why this works:** Callers receive domain-specific exceptions (`UserNotFoundException`, `DuplicateUserException`). Original exceptions preserved as causes. Service layer does not need to catch `SQLException`. Repository contract is clear.

---

## Example 4: Testing Exception Handling

### Bad: Not Testing Exception Paths

```java
class UserServiceTest {

    @Test
    void testCreateUser() {
        // Only tests happy path
        // Does not verify:
        // - What happens when email is null
        // - What happens when email already exists
        // - What happens when repository fails
        // - What happens when email service fails
        User user = service.createUser(new CreateUserRequest("John", "john@example.com"));
        assertNotNull(user);
    }
}
```

### Good: Comprehensive Exception Testing

```java
@ExtendWith(MockitoExtension.class)
class GoodUserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should throw when email is null")
    void shouldThrowWhenEmailIsNull() {
        CreateUserRequest request = new CreateUserRequest("John", null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.createUser(request)
        );

        assertTrue(exception.getMessage().contains("email"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when email already exists")
    void shouldThrowWhenEmailAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest("John", "existing@example.com");
        when(repository.existsByEmail("existing@example.com")).thenReturn(true);

        DuplicateEmailException exception = assertThrows(
            DuplicateEmailException.class,
            () -> userService.createUser(request)
        );

        assertEquals("existing@example.com", exception.getEmail());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should create user even when email service fails")
    void shouldCreateUserEvenWhenEmailFails() {
        CreateUserRequest request = new CreateUserRequest("John", "john@example.com");
        when(repository.existsByEmail("john@example.com")).thenReturn(false);
        doThrow(new EmailException("Service down"))
            .when(emailService).sendWelcomeEmail(any());

        User result = userService.createUser(request);

        assertNotNull(result);
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Should verify save before email")
    void shouldVerifySaveBeforeEmail() {
        CreateUserRequest request = new CreateUserRequest("John", "john@example.com");
        when(repository.existsByEmail("john@example.com")).thenReturn(false);

        InOrder inOrder = inOrder(repository, emailService);
        userService.createUser(request);

        inOrder.verify(repository).save(any(User.class));
        inOrder.verify(emailService).sendWelcomeEmail(any(User.class));
    }
}
```

**Key testing patterns:** Test each exception path independently. Verify no side effects occur after exception. Use `assertThrows` with specific exception type. Verify ordering when operations must be sequential.

---

## Example 5: Resource Management

### Bad: Manual Resource Cleanup

```java
public class BadDataExporter {

    public void export(String path) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        FileWriter writer = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users");
            writer = new FileWriter(path);

            while (rs.next()) {
                writer.write(rs.getString("name") + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Wrong output stream
        } finally {
            // Problem: each close needs its own try-catch
            // Problem: if first close throws, remaining resources leak
            // Problem: 30+ lines of boilerplate
            try { if (rs != null) rs.close(); } catch (Exception e) { }
            try { if (stmt != null) stmt.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
            try { if (writer != null) writer.close(); } catch (Exception e) { }
        }
    }
}
```

### Good: try-with-resources

```java
public class GoodDataExporter {

    private static final Logger log = Logger.getLogger(GoodDataExporter.class);

    public void export(String path) throws ExportException {
        Objects.requireNonNull(path, "Export path must not be null");

        String sql = "SELECT id, name, email FROM users";

        // Resources auto-closed in reverse declaration order
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery();
             BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {

            int count = 0;
            while (rs.next()) {
                writer.write(String.format("%s,%s%n",
                    rs.getString("name"),
                    rs.getString("email")));
                count++;
            }

            writer.flush();
            log.info("Exported {} users to {}", count, path);

        } catch (SQLException e) {
            throw new ExportException("Database error during export", e);
        } catch (IOException e) {
            throw new ExportException(
                "File write error during export to: " + path, e);
        }
    }
}
```

**Why this works:** Single try block manages all resources. Automatic cleanup in reverse order. Resources declared in dependency order. Each exception type handled specifically. Clean, readable code.

---

## Example 6: Production-Grade Global Exception Handler

### Bad: Catch-All in Controller

```java
@RestController
public class BadUserController {

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // Problem 1: exposes stack trace to client
            // Problem 2: no request context
            // Problem 3: all errors look the same
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
```

### Good: Structured Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(ValidationException e, HttpServletRequest request) {
        log.warn("Validation failed: path={}, reason={}",
            request.getRequestURI(), e.getMessage());

        return ErrorResponse.builder()
            .code("VALIDATION_ERROR")
            .message(e.getMessage())
            .field(e.getField())
            .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        log.warn("Resource not found: path={}, resource={}",
            request.getRequestURI(), e.getResourceId());

        return ErrorResponse.builder()
            .code("NOT_FOUND")
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicate(DuplicateResourceException e, HttpServletRequest request) {
        log.warn("Duplicate resource: path={}, field={}",
            request.getRequestURI(), e.getField());

        return ErrorResponse.builder()
            .code("DUPLICATE")
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleService(ServiceException e, HttpServletRequest request) {
        log.error("Service error: path={}: {}",
            request.getRequestURI(), e.getMessage(), e);

        return ErrorResponse.builder()
            .code("INTERNAL_ERROR")
            .message("An unexpected error occurred")
            .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpected(Exception e, HttpServletRequest request) {
        log.error("Unexpected error: path={}: {}",
            request.getRequestURI(), e.getMessage(), e);

        return ErrorResponse.builder()
            .code("INTERNAL_ERROR")
            .message("An unexpected error occurred")
            .build();
    }
}
```

**Why this works:** Each exception type mapped to appropriate HTTP status. Request context in every log line. Client receives safe error messages (no implementation details). Stack traces logged server-side only. Centralized, testable, maintainable.

---

*See also: [Decision Guide](../decision.md) | [Exercises](../01-exercises/README.md) | [Solutions](../02-solutions/README.md)*
