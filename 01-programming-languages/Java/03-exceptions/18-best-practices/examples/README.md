# Practice Exercises: Exception Handling Best Practices

> Work through these exercises to identify and fix exception handling anti-patterns.

---

## Exercise 1: Identify the Anti-Patterns

Review the following code and identify all exception handling anti-patterns.

```java
public class OrderProcessor {
    
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
        // Validation logic
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
            throw new RuntimeException("Payment failed");
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

### Questions:

1. How many anti-patterns can you find?
2. For each anti-pattern, explain why it's problematic.
3. What would you change to fix each issue?

---

## Exercise 2: Fix the Resource Management

This code has multiple resource management issues. Rewrite it properly.

```java
public class DataExporter {
    
    public void exportUsers(String outputPath) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        FileWriter writer = null;
        BufferedWriter bw = null;
        
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users");
            
            writer = new FileWriter(outputPath);
            bw = new BufferedWriter(writer);
            
            while (rs.next()) {
                String line = rs.getString("name") + "," + 
                             rs.getString("email") + "\n";
                bw.write(line);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try { bw.close(); } catch (Exception e) { }
            }
            if (writer != null) {
                try { writer.close(); } catch (Exception e) { }
            }
            if (rs != null) {
                try { rs.close(); } catch (Exception e) { }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (Exception e) { }
            }
            if (conn != null) {
                try { conn.close(); } catch (Exception e) { }
            }
        }
    }
}
```

### Requirements:

1. Rewrite using try-with-resources
2. Handle each exception type appropriately
3. Add meaningful error messages
4. Ensure proper cleanup order

---

## Exercise 3: Exception Translation

The following code exposes low-level exceptions to callers. Refactor to use exception translation.

```java
public class UserRepository {
    
    private JdbcTemplate jdbcTemplate;
    
    public User findById(String id) throws SQLException {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE id = ?",
            userRowMapper, id);
    }
    
    public List<User> findByEmail(String email) throws SQLException {
        return jdbcTemplate.query(
            "SELECT * FROM users WHERE email = ?",
            userRowMapper, email);
    }
    
    public void save(User user) throws SQLException {
        jdbcTemplate.update(
            "INSERT INTO users (id, name, email) VALUES (?, ?, ?)",
            user.getId(), user.getName(), user.getEmail());
    }
    
    public void delete(String id) throws SQLException {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
}
```

### Requirements:

1. Create appropriate domain exceptions
2. Translate low-level exceptions in each method
3. Preserve original exceptions as causes
4. Write Javadoc for the new exceptions

---

## Exercise 4: Logging Quality

This code has poor logging practices. Improve all logging statements.

```java
public class PaymentProcessor {
    
    private static final Logger log = Logger.getLogger(PaymentProcessor.class);
    
    public PaymentResult processPayment(PaymentRequest request) {
        try {
            validateRequest(request);
            
            PaymentResult result = paymentGateway.charge(
                request.getCardNumber(),
                request.getAmount(),
                request.getCurrency()
            );
            
            log.info("Payment processed");
            return result;
            
        } catch (ValidationException e) {
            log.error("Validation error");
            return null;
        } catch (PaymentDeclinedException e) {
            log.error("Payment declined: " + e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Error processing payment");
            return null;
        }
    }
}
```

### Requirements:

1. Add relevant context to each log message
2. Include the exception in log calls where appropriate
3. Use correct log levels
4. Ensure no sensitive data is logged

---

## Exercise 5: Design Exception Hierarchy

Design an exception hierarchy for an e-commerce application. Consider:

- Different layers (presentation, service, repository)
- Different failure types (validation, business rule, infrastructure)
- Checked vs unchecked decisions

### Requirements:

1. Draw an exception hierarchy diagram
2. Create Java classes for each exception
3. Explain your checked vs unchecked decisions
4. Write usage examples for each exception type

---

## Exercise 6: Test Exception Handling

Write comprehensive tests for this service class:

```java
public class UserService {
    
    private final UserRepository repository;
    private final EmailService emailService;
    
    public UserService(UserRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }
    
    public User createUser(CreateUserRequest request) {
        if (request.getEmail() == null) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (repository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }
        
        User user = new User(request.getName(), request.getEmail());
        repository.save(user);
        
        emailService.sendWelcomeEmail(user);
        
        return user;
    }
}
```

### Test Scenarios to Cover:

1. Successful user creation
2. Null email validation
3. Duplicate email handling
4. Repository failure
5. Email service failure
6. Verify email is sent on success
7. Verify user is saved before email is sent

---

## Exercise 7: Production Readiness Review

This exception handling code needs a production readiness review. Identify all issues:

```java
public class ApiService {
    
    public ApiResponse handleRequest(ApiRequest request) {
        try {
            // Process the request
            Object result = processRequest(request);
            return ApiResponse.success(result);
            
        } catch (Exception e) {
            log.error("Error");
            return ApiResponse.error("Internal error");
        }
    }
    
    private Object processRequest(ApiRequest request) {
        // Call external service
        ExternalResponse response = externalService.call(request);
        
        // Transform response
        return transform(response);
    }
    
    private Object transform(ExternalResponse response) {
        // Complex transformation logic
        // ...
        return result;
    }
}
```

### Review Checklist:

- [ ] Exception types are appropriate
- [ ] Error messages are informative
- [ ] No sensitive data is exposed
- [ ] Logging is complete
- [ ] Resources are managed
- [ ] External failures are handled
- [ ] Response doesn't leak implementation details
- [ ] Monitoring is possible

---

## Submission Guidelines

For each exercise:

1. Write your solution in a separate file
2. Include comments explaining your decisions
3. Note any trade-offs you considered
4. Be prepared to discuss alternatives

---

## Next Steps

After completing these exercises:

1. Review the solutions in `../solutions/`
2. Compare your approach to the recommended solutions
3. Identify patterns you can apply to your own code
4. Create a personal checklist for code reviews

---

*Return to [Main README](../README.md)*
