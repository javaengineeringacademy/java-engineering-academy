# 18 - Exception Handling Best Practices (Part 3)

[← Part 2](README-Part2.md) | [Part 4 →](README-Part4.md)

---

# 18 - Exception Handling Best Practices (Part 2)

[Part 1](README-Part1.md)

---

## 7. Resource Cleanup Patterns

### Try-with-resources (AutoCloseable)

The try-with-resources statement, introduced in Java 7, is the standard way to
manage resources that implement `AutoCloseable`. Resources are automatically
closed in reverse declaration order when the try block exits.

```java
// GOOD: Try-with-resources -- resources auto-close
public List<String> readLines(Path path) {
    try (BufferedReader reader = Files.newBufferedReader(path);
         Stream<String> lines = reader.lines()) {
        return lines.collect(Collectors.toList());
    } catch (IOException e) {
        throw new FileAccessException(
            "Failed to read lines from: " + path, e);
    }
    // reader is closed here, even if an exception occurs
}
```

### Multi-Resource Cleanup Order

```
  Declaration order:            Cleanup order (reverse):
  +---------------------------+  +---------------------------+
  | 1. Connection conn        |  | 3. Connection conn (last) |
  | 2. PreparedStatement stmt |  | 2. PreparedStatement stmt |
  | 3. ResultSet rs           |  | 1. ResultSet rs (first)   |
  +---------------------------+  +---------------------------+

  Dependencies flow:
  conn -> creates stmt -> creates rs
  Cleanup: rs -> stmt -> conn  (reverse of creation)
```

```java
// GOOD: Resources declared in dependency order, cleaned up in reverse
public Map<String, Object> queryUser(String sql, String userId) {
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, userId);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return extractRow(rs);
            }
            return Collections.emptyMap();
        }
        // rs closed first
    }
    // stmt closed second, conn closed last
    catch (SQLException e) {
        throw new DataAccessException(
            "Query failed for user: " + userId, e);
    }
}
```

### Decision Matrix: When to Use Each Pattern

| Scenario                                | Pattern                  | Why                                      |
|-----------------------------------------|--------------------------|------------------------------------------|
| Stream implements AutoCloseable          | try-with-resources       | Guaranteed cleanup, clean syntax         |
| Multiple dependent resources            | try-with-resources       | Reverse cleanup order handled automatically|
| Custom cleanup that may fail             | try-with-resources       | Suppressed exceptions preserved          |
| Resource from external library (no AC)  | try-finally              | No AutoCloseable interface               |
| Cleanup must happen conditionally       | try-finally with checks  | Logic in finally block                   |
| Non-Closeable resource (e.g., thread)  | try-finally              | Manual interrupt/release in finally      |
| Parallel resources (independent)        | try-with-resources       | All closed even if one fails             |

### Handling Suppressed Exceptions

```java
// When both the try block and close() throw exceptions
// The close() exception becomes a suppressed exception
public void demonstrateSuppressed() {
    try (AutoCloseableResource res = new AutoCloseableResource()) {
        throw new IOException("Original error");
        // res.close() also throws -- both are preserved
    } catch (Exception e) {
        System.out.println("Primary: " + e.getMessage());
        for (Throwable suppressed : e.getSuppressed()) {
            System.out.println("Suppressed: " + suppressed.getMessage());
        }
    }
}
```

---

## 8. Exception Translation Patterns

Exception translation wraps low-level exceptions into domain-specific exceptions,
hiding implementation details from callers.

### Translation Flow Diagram

```
  Low-Level Layer                    High-Level Layer
  +---------------------------+      +---------------------------+
  |                           |      |                           |
  |  SQLException caught      | ---> |  DataAccessException      |
  |  (JDBC detail)            |      |  (domain exception)       |
  |                           |      |                           |
  +---------------------------+      +---------------------------+
                                           |
                                           | original exception
                                           | preserved as cause
                                           v
                                  +---------------------------+
                                  |  Callers catch ONLY       |
                                  |  domain exceptions        |
                                  +---------------------------+
```

### Code Example

```java
// BAD: Low-level exception leaks into API
public User findUser(String id) throws SQLException {
    return jdbcTemplate.queryForObject(
        "SELECT * FROM users WHERE id = ?",
        userRowMapper, id);
}
// Caller must handle JDBC-specific SQLException -- why does they care?

// GOOD: Exception translation
public User findUser(String id) {
    try {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE id = ?",
            userRowMapper, id);
    } catch (EmptyResultDataAccessException e) {
        throw new UserNotFoundException("User not found: " + id, e);
    } catch (DataAccessException e) {
        throw new UserServiceException(
            "Failed to find user: " + id, e);
    }
    // Callers catch UserNotFoundException or UserServiceException only
}
```

### Translation Rules

| Rule                              | Implementation                                  |
|-----------------------------------|-------------------------------------------------|
| Preserve the original exception   | Pass low-level exception as `cause` parameter   |
| Translate to domain exception     | Wrap in a higher-level exception type           |
| Include relevant context          | Add entity IDs, operation names to message      |
| Do not over-translate             | One level of translation per layer              |
| Let unchecked propagate naturally | Do not catch programming errors (NPE, etc.)     |
| One catch per exception type      | Each low-level type maps to one domain type     |
| Document the translation          | Javadoc should list translated exceptions       |

---

## 9. Testing Exception Handling

### Testing Pyramid for Exception Handling



---

## 9. Testing Exception Handling

### Testing Pyramid for Exception Handling

```
                    +-----------+
                    |   E2E     |   Full integration paths
                    |   Tests   |   that exercise exception
                    +-----+-----+   flows end-to-end
                          |
                  +-------v-------+
                  | Integration   |   Database exceptions,
                  | Tests         |   HTTP failures, timeouts
                  +-------+-------+
                          |
            +-------------v-------------+
            |      Unit Tests           |   Business logic exceptions,
            |   (majority of effort)    |   validation, null checks
            +---------------------------+
```

### Unit Testing Code Examples

```java
// TEST: Verify exception is thrown with correct message
@Test
void shouldThrowWhenAmountIsNegative() {
    PaymentRequest req = new PaymentRequest(
        "ORD-001", BigDecimal.valueOf(-10), "4242424242424242");

    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> paymentService.processPayment(req));

    assertTrue(ex.getMessage().contains("must be positive"));
    assertTrue(ex.getMessage().contains("-10"));
}

// TEST: Verify specific exception type in chain
@Test
void shouldWrapDataAccessException() {
    when(userRepository.findById("USR-999"))
        .thenThrow(new DataAccessException("DB down") {});

    UserServiceException ex = assertThrows(
        UserServiceException.class,
        () -> userService.findUser("USR-999"));

    assertTrue(ex.getCause() instanceof DataAccessException);
    assertTrue(ex.getMessage().contains("USR-999"));
}

// TEST: Verify no exception on happy path
@Test
void shouldReturnEmptyOptionalWhenUserNotFound() {
    when(userRepository.findById("USR-999")).thenReturn(null);

    Optional<User> result = userService.findUser("USR-999");

    assertTrue(result.isEmpty());
}

// TEST: Verify exception propagation (not swallowed)
@Test
void shouldPropagateInventoryException() {
    Order order = new Order("ORD-001", List.of(
        new OrderItem("PROD-1", 5)));

    when(inventoryService.decrement("PROD-1", 5))
        .thenThrow(new InsufficientStockException("PROD-1", 5, 2));

    OrderProcessingException ex = assertThrows(
