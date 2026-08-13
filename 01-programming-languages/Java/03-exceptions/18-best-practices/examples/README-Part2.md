# Exception Handling Best Practices: Code Examples (Part 2)

> Examples 5–6. See [Part 1](README-Part1.md) for Examples 1–4.

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

*See also: [Decision Guide](../decision.md) | [Part 1: Examples 1–4](README-Part1.md) | [Solutions](../02-solutions/README.md)*
