# Exception Handling Best Practices: Solutions (Part 1)

> Solutions 1–3. See [Part 2](README-Part2.md) for Solutions 4–6.

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
