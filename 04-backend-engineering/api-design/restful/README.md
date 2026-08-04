# RESTful API Design Principles

## Comprehensive Guide to RESTful API Design

RESTful API design follows established principles for building scalable, maintainable web services. This guide covers URI design, HTTP methods, status codes, and best practices.

---

## Table of Contents

1. [REST Principles](#rest-principles)
2. [URI Design](#uri-design)
3. [HTTP Methods](#http-methods)
4. [Status Codes](#status-codes)
5. [Request/Response Design](#requestresponse-design)
6. [Best Practices](#best-practices)

---

## REST Principles

### Six Constraints

```
1. Client-Server: Separation of concerns
2. Stateless: No session state on server
3. Cacheable: Responses must define cacheability
4. Uniform Interface: Standardized communication
5. Layered System: Client unaware of direct connection
6. Code on Demand (optional): Server extends client
```

### Resource Identification

```java
// Good - Resources are nouns
GET /users
GET /users/123
GET /users/123/orders
POST /users

// Bad - Verbs in URIs
GET /getUsers
POST /createUser
DELETE /deleteUser/123
```

---

## URI Design

### Naming Conventions

```
# Use plural nouns
GET /users
GET /users/123

# Use lowercase
GET /users
GET /users/123

# Use hyphens for multi-word
GET /user-profiles
GET /order-items

# Use query parameters for filtering
GET /users?status=active&role=admin

# Avoid file extensions
GET /users        (not /users.json)
```

### Resource Hierarchy

```
# Flat structure
GET /users
GET /users/123
GET /orders
GET /orders/456

# Nested resources
GET /users/123/orders
GET /users/123/orders/456
GET /orders/456/items

# Use nesting sparingly (max 2 levels)
GET /users/123/orders        (OK)
GET /users/123/orders/456    (OK)
GET /users/123/orders/456/items (Avoid)
```

### Examples

```
# User resource
GET    /users              List users
GET    /users/123          Get user
POST   /users              Create user
PUT    /users/123          Update user
DELETE /users/123          Delete user

# User's orders
GET    /users/123/orders   List user's orders
POST   /users/123/orders   Create order for user

# Order resource
GET    /orders             List orders
GET    /orders/456         Get order
PUT    /orders/456         Update order
DELETE /orders/456         Delete order

# Order items
GET    /orders/456/items   List order items
POST   /orders/456/items   Add item to order
```

---

## HTTP Methods

### Method Semantics

| Method | Idempotent | Safe | Purpose |
|--------|------------|------|---------|
| GET | Yes | Yes | Read resource |
| POST | No | No | Create resource |
| PUT | Yes | No | Replace resource |
| PATCH | No | No | Partial update |
| DELETE | Yes | No | Remove resource |
| HEAD | Yes | Yes | Get metadata |
| OPTIONS | Yes | Yes | Get allowed methods |

### GET

```java
// List resources
@GetMapping("/users")
public ResponseEntity<List<User>> listUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
    return ResponseEntity.ok(userService.findAll(page, size));
}

// Get single resource
@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    return userService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}
```

### POST

```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request) {
    User user = userService.create(request);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(user.getId())
        .toUri();
    return ResponseEntity.created(location).body(user);
}
```

### PUT

```java
@PutMapping("/users/{id}")
public ResponseEntity<User> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request) {
    return ResponseEntity.ok(userService.update(id, request));
}
```

### PATCH

```java
@PatchMapping("/users/{id}")
public ResponseEntity<User> patchUser(
        @PathVariable Long id,
        @RequestBody Map<String, Object> updates) {
    return ResponseEntity.ok(userService.patch(id, updates));
}
```

### DELETE

```java
@DeleteMapping("/users/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
}
```

---

## Status Codes

### Success Codes

```java
// 200 OK
@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    return ResponseEntity.ok(user); // 200
}

// 201 Created
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody User user) {
    User created = userService.create(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201
}

// 204 No Content
@DeleteMapping("/users/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build(); // 204
}
```

### Error Codes

```java
// 400 Bad Request
@PostMapping("/users")
public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
    // Validation errors automatically return 400
    return ResponseEntity.ok(userService.create(user));
}

// 401 Unauthorized
@GetMapping("/protected")
public ResponseEntity<?> protectedEndpoint() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("error", "Unauthorized"));
}

// 403 Forbidden
@GetMapping("/admin")
public ResponseEntity<?> adminEndpoint() {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(Map.of("error", "Forbidden"));
}

// 404 Not Found
@GetMapping("/users/{id}")
public ResponseEntity<?> getUser(@PathVariable Long id) {
    return userService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build()); // 404
}

// 409 Conflict
@PostMapping("/users")
public ResponseEntity<?> createUser(@RequestBody User user) {
    if (userService.existsByEmail(user.getEmail())) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", "Email already exists"));
    }
    return ResponseEntity.ok(userService.create(user));
}

// 429 Too Many Requests
@GetMapping("/users")
public ResponseEntity<?> listUsers() {
    if (rateLimiter.isExceeded()) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .header("Retry-After", "60")
            .body(Map.of("error", "Rate limit exceeded"));
    }
    return ResponseEntity.ok(userService.findAll());
}
```

---

## Request/Response Design

### Request Headers

```java
@GetMapping("/users")
public ResponseEntity<List<User>> getUsers(
        @RequestHeader("Accept") String accept,
        @RequestHeader("Accept-Language") String language,
        @RequestHeader(value = "X-Request-Id", required = false) String requestId) {
    return ResponseEntity.ok(userService.findAll());
}
```

### Response Headers

```java
@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    return ResponseEntity.ok()
        .header("Cache-Control", "max-age=3600")
        .header("ETag", generateEtag(user))
        .header("X-Request-Id", requestId)
        .body(user);
}
```

### Pagination

```java
@GetMapping("/users")
public ResponseEntity<Map<String, Object>> listUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {

    Page<User> users = userService.findAll(PageRequest.of(page, size));

    Map<String, Object> response = new HashMap<>();
    response.put("data", users.getContent());
    response.put("pagination", Map.of(
        "total", users.getTotalElements(),
        "page", users.getNumber(),
        "size", users.getSize(),
        "pages", users.getTotalPages()
    ));

    return ResponseEntity.ok(response);
}
```

### Filtering

```java
@GetMapping("/users")
public ResponseEntity<List<User>> listUsers(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String role,
        @RequestParam(required = false) String search) {

    return ResponseEntity.ok(userService.findAll(status, role, search));
}
```

---

## Best Practices

### 1. Use Consistent Naming

```
GET /users
GET /users/123
GET /users/123/orders
GET /orders
GET /orders/456
```

### 2. Version Your API

```java
// URI versioning
@RequestMapping("/api/v1/users")
public class UserV1Controller { }

@RequestMapping("/api/v2/users")
public class UserV2Controller { }
```

### 3. Use HATEOAS

```java
@GetMapping("/users/{id}")
public EntityModel<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    EntityModel<User> resource = EntityModel.of(user);
    resource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
    resource.add(linkTo(methodOn(UserController.class).listUsers()).withRel("users"));
    return resource;
}
```

### 4. Document Your API

```java
@Operation(summary = "List users", description = "Returns a paginated list of users")
@GetMapping("/users")
public ResponseEntity<List<User>> listUsers() { }
```

### 5. Handle Errors Consistently

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException e) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("VALIDATION_ERROR", e.getMessage()));
    }
}
```

### 6. Use Proper Content Types

```java
@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<List<User>> listUsers() { }

@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<User> createUser(@RequestBody User user) { }
```

---

## Further Reading

- [REST API Design Resource Modeling](https://restfulapi.net/resource-naming/)
- [Microsoft REST API Guidelines](https://github.com/microsoft/api-guidelines)
- [Google API Design Guide](https://cloud.google.com/apis/design)
- [Richardson Maturity Model](https://martinfowler.com/articles/richardsonMaturityModel.html)
