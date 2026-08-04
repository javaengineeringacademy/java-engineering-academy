# REST API Design

## Comprehensive Guide to REST API Design

This guide covers API design principles, naming conventions, versioning, pagination, filtering, and HATEOAS implementation.

---

## Table of Contents

1. [API Design Principles](#api-design-principles)
2. [Naming Conventions](#naming-conventions)
3. [Versioning](#versioning)
4. [Pagination](#pagination)
5. [Filtering](#filtering)
6. [HATEOAS](#hateoas)
7. [Best Practices](#best-practices)

---

## API Design Principles

### Consistent API Structure

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @GetMapping
    public ResponseEntity<List<UserResource>> getUsers() { ... }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResource> getUser(@PathVariable Long id) { ... }
    
    @PostMapping
    public ResponseEntity<UserResource> createUser(@RequestBody @Valid CreateUserRequest request) { ... }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResource> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) { ... }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) { ... }
}
```

### Resource Naming

```java
// Good - Nouns, plural, lowercase
@GetMapping("/users")
@GetMapping("/users/{id}")
@GetMapping("/users/{id}/orders")
@GetMapping("/orders/{id}/items")

// Bad - Verbs, singular, mixed case
@GetMapping("/getUsers")
@GetMapping("/getUser")
@GetMapping("/userOrders")
@GetMapping("/orderItems")
```

### URL Structure

```java
// Collection
GET /api/v1/users
POST /api/v1/users

// Individual resource
GET /api/v1/users/{id}
PUT /api/v1/users/{id}
DELETE /api/v1/users/{id}

// Nested resources
GET /api/v1/users/{userId}/orders
POST /api/v1/users/{userId}/orders
GET /api/v1/users/{userId}/orders/{orderId}

// Query parameters for filtering
GET /api/v1/users?status=active&sort=name&page=0&size=10
```

---

## Naming Conventions

### Resource Names

```java
// Use plural nouns for collections
@GetMapping("/users") // Collection of users
@GetMapping("/orders") // Collection of orders
@GetMapping("/products") // Collection of products

// Use singular for individual resources
@GetMapping("/users/{id}") // Single user
@GetMapping("/orders/{id}") // Single order
@GetMapping("/products/{id}") // Single product
```

### HTTP Methods

```java
// GET - Read
@GetMapping // Read all
@GetMapping("/{id}") // Read one

// POST - Create
@PostMapping // Create new

// PUT - Update (full replace)
@PutMapping("/{id}") // Update one

// PATCH - Update (partial)
@PatchMapping("/{id}") // Partial update one

// DELETE - Remove
@DeleteMapping("/{id}") // Delete one
```

### Status Codes

```java
// Success
200 OK - Successful GET, PUT, PATCH
201 Created - Successful POST
204 No Content - Successful DELETE

// Client Error
400 Bad Request - Invalid input
401 Unauthorized - Authentication required
403 Forbidden - Insufficient permissions
404 Not Found - Resource doesn't exist
409 Conflict - Resource already exists
422 Unprocessable Entity - Validation error
429 Too Many Requests - Rate limit exceeded

// Server Error
500 Internal Server Error - Server error
503 Service Unavailable - Service down
```

### Request/Response Naming

```java
// Request DTOs
public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
}

public class UpdateUserRequest {
    private String username;
    private String email;
}

// Response DTOs
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}

public class UserListResponse {
    private List<UserResponse> users;
    private int totalElements;
    private int totalPages;
}
```

---

## Versioning

### URI Versioning

```java
// Version 1
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller {
    
    @GetMapping
    public ResponseEntity<List<UserV1>> getUsers() { ... }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserV1> getUser(@PathVariable Long id) { ... }
}

// Version 2
@RestController
@RequestMapping("/api/v2/users")
public class UserV2Controller {
    
    @GetMapping
    public ResponseEntity<List<UserV2>> getUsers() { ... }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserV2> getUser(@PathVariable Long id) { ... }
}
```

### Header Versioning

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(headers = "X-API-Version=1")
    public ResponseEntity<List<UserV1>> getUsersV1() { ... }
    
    @GetMapping(headers = "X-API-Version=2")
    public ResponseEntity<List<UserV2>> getUsersV2() { ... }
}
```

### Query Parameter Versioning

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(params = "version=1")
    public ResponseEntity<List<UserV1>> getUsersV1() { ... }
    
    @GetMapping(params = "version=2")
    public ResponseEntity<List<UserV2>> getUsersV2() { ... }
}
```

### Content Negotiation Versioning

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(produces = "application/vnd.myapp.v1+json")
    public ResponseEntity<List<UserV1>> getUsersV1() { ... }
    
    @GetMapping(produces = "application/vnd.myapp.v2+json")
    public ResponseEntity<List<UserV2>> getUsersV2() { ... }
}
```

---

## Pagination

### Offset-Based Pagination

```java
@GetMapping
public ResponseEntity<Page<User>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDirection) {
    
    Sort sort = sortDirection.equalsIgnoreCase("desc") ? 
        Sort.by(sortBy).descending() : 
        Sort.by(sortBy).ascending();
    
    PageRequest pageRequest = PageRequest.of(page, size, sort);
    Page<User> users = userService.findAll(pageRequest);
    
    return ResponseEntity.ok(users);
}
```

### Cursor-Based Pagination

```java
@GetMapping
public ResponseEntity<CursorPage<User>> getUsers(
        @RequestParam(required = false) String cursor,
        @RequestParam(defaultValue = "10") int limit) {
    
    List<User> users;
    String nextCursor;
    
    if (cursor == null) {
        users = userService.findFirstN(limit);
        nextCursor = users.isEmpty() ? null : 
            encodeCursor(users.get(users.size() - 1).getId());
    } else {
        Long lastId = decodeCursor(cursor);
        users = userService.findByIdAfter(lastId, limit);
        nextCursor = users.isEmpty() ? null : 
            encodeCursor(users.get(users.size() - 1).getId());
    }
    
    return ResponseEntity.ok(new CursorPage<>(users, nextCursor, limit));
}
```

### Keyset Pagination

```java
@GetMapping
public ResponseEntity<List<User>> getUsers(
        @RequestParam(required = false) Long lastId,
        @RequestParam(defaultValue = "10") int limit) {
    
    List<User> users;
    
    if (lastId == null) {
        users = userService.findFirstN(limit);
    } else {
        users = userService.findByIdGreaterThan(lastId, limit);
    }
    
    return ResponseEntity.ok(users);
}
```

### Pagination Response

```java
@Data
@Builder
public class PaginatedResponse<T> {
    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private Link next;
    private Link previous;
    private Link first;
    private Link last;
}

// Usage
@GetMapping
public ResponseEntity<PaginatedResponse<User>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    Page<User> userPage = userService.findAll(PageRequest.of(page, size));
    
    PaginatedResponse<User> response = PaginatedResponse.<User>builder()
        .data(userPage.getContent())
        .page(page)
        .size(size)
        .totalElements(userPage.getTotalElements())
        .totalPages(userPage.getTotalPages())
        .hasNext(userPage.hasNext())
        .hasPrevious(userPage.hasPrevious())
        .next(userPage.hasNext() ? Link.of("/api/users?page=" + (page + 1) + "&size=" + size) : null)
        .previous(userPage.hasPrevious() ? Link.of("/api/users?page=" + (page - 1) + "&size=" + size) : null)
        .first(Link.of("/api/users?page=0&size=" + size))
        .last(Link.of("/api/users?page=" + (userPage.getTotalPages() - 1) + "&size=" + size))
        .build();
    
    return ResponseEntity.ok(response);
}
```

---

## Filtering

### Basic Filtering

```java
@GetMapping
public ResponseEntity<List<User>> getUsers(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) LocalDateTime createdAfter,
        @RequestParam(required = false) LocalDateTime createdBefore) {
    
    UserFilter filter = UserFilter.builder()
        .username(username)
        .email(email)
        .status(status)
        .createdAfter(createdAfter)
        .createdBefore(createdBefore)
        .build();
    
    List<User> users = userService.findAll(filter);
    return ResponseEntity.ok(users);
}
```

### Dynamic Filtering

```java
@GetMapping
public ResponseEntity<List<Map<String, Object>>> getUsers(
        @RequestParam(required = false) Map<String, String> filters,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    List<Map<String, Object>> users = userService.findWithDynamicFilters(filters, page, size);
    return ResponseEntity.ok(users);
}
```

### Sorting

```java
@GetMapping
public ResponseEntity<List<User>> getUsers(
        @RequestParam(defaultValue = "id,asc") String[] sort) {
    
    List<Sort.Order> orders = new ArrayList<>();
    
    for (String s : sort) {
        String[] parts = s.split(",");
        orders.add(new Sort.Order(
            Sort.Direction.fromString(parts[1]), 
            parts[0]));
    }
    
    Sort sortObj = Sort.by(orders);
    List<User> users = userService.findAll(sortObj);
    return ResponseEntity.ok(users);
}
```

### Field Selection

```java
@GetMapping
public ResponseEntity<List<Map<String, Object>>> getUsers(
        @RequestParam(required = false) String fields) {
    
    List<Map<String, Object>> users;
    
    if (fields != null) {
        String[] fieldArray = fields.split(",");
        users = userService.findAllWithFields(fieldArray);
    } else {
        users = userService.findAll();
    }
    
    return ResponseEntity.ok(users);
}
```

### Search

```java
@GetMapping("/search")
public ResponseEntity<List<User>> searchUsers(
        @RequestParam String q,
        @RequestParam(required = false) String field) {
    
    List<User> users;
    
    if (field != null) {
        users = userService.searchByField(field, q);
    } else {
        users = userService.search(q);
    }
    
    return ResponseEntity.ok(users);
}
```

---

## HATEOAS

### Basic HATEOAS

```java
@GetMapping("/{id}")
public EntityModel<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    
    EntityModel<User> resource = EntityModel.of(user);
    resource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
    resource.add(linkTo(methodOn(UserController.class).getUsers()).withRel("users"));
    
    return resource;
}
```

### HATEOAS with Collection

```java
@GetMapping
public CollectionModel<EntityModel<User>> getUsers() {
    List<EntityModel<User>> users = userService.findAll().stream()
        .map(user -> EntityModel.of(user,
            linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel(),
            linkTo(methodOn(UserController.class).getUsers()).withRel("users")))
        .collect(Collectors.toList());
    
    return CollectionModel.of(users,
        linkTo(methodOn(UserController.class).getUsers()).withSelfRel());
}
```

### HATEOAS with Related Resources

```java
@GetMapping("/{id}")
public EntityModel<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    
    EntityModel<User> resource = EntityModel.of(user);
    resource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
    resource.add(linkTo(methodOn(UserController.class).getUsers()).withRel("users"));
    resource.add(linkTo(methodOn(UserAddressController.class).getUserAddresses(id))
        .withRel("addresses"));
    resource.add(linkTo(methodOn(UserOrderController.class).getUserOrders(id))
        .withRel("orders"));
    
    return resource;
}
```

### HATEOAS with Pagination

```java
@GetMapping
public ResponseEntity<CollectionModel<EntityModel<User>>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    Page<User> userPage = userService.findAll(PageRequest.of(page, size));
    
    List<EntityModel<User>> users = userPage.getContent().stream()
        .map(user -> EntityModel.of(user,
            linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel()))
        .collect(Collectors.toList());
    
    CollectionModel<EntityModel<User>> collectionModel = 
        CollectionModel.of(users);
    
    collectionModel.add(linkTo(methodOn(UserController.class)
        .getUsers(page, size)).withSelfRel());
    
    if (userPage.hasNext()) {
        collectionModel.add(linkTo(methodOn(UserController.class)
            .getUsers(page + 1, size)).withRel("next"));
    }
    
    if (userPage.hasPrevious()) {
        collectionModel.add(linkTo(methodOn(UserController.class)
            .getUsers(page - 1, size)).withRel("prev"));
    }
    
    return ResponseEntity.ok(collectionModel);
}
```

### HATEOAS Link Builder

```java
@Component
public class UserLinkBuilder {
    
    public EntityModel<User> toModel(User user) {
        EntityModel<User> resource = EntityModel.of(user);
        
        resource.add(linkTo(methodOn(UserController.class).getUser(user.getId()))
            .withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).getUsers())
            .withRel("users"));
        resource.add(linkTo(methodOn(UserAddressController.class)
            .getUserAddresses(user.getId())).withRel("addresses"));
        resource.add(linkTo(methodOn(UserOrderController.class)
            .getUserOrders(user.getId())).withRel("orders"));
        
        return resource;
    }
    
    public CollectionModel<EntityModel<User>> toCollectionModel(List<User> users) {
        List<EntityModel<User>> userResources = users.stream()
            .map(this::toModel)
            .collect(Collectors.toList());
        
        return CollectionModel.of(userResources,
            linkTo(methodOn(UserController.class).getUsers()).withSelfRel());
    }
}
```

---

## Best Practices

### 1. Use Consistent Naming

```java
// Good
@GetMapping("/users")
@GetMapping("/users/{id}")
@GetMapping("/users/{id}/orders")
@GetMapping("/orders/{id}/items")

// Bad
@GetMapping("/getUsers")
@GetMapping("/getUserById")
@GetMapping("/userOrders")
@GetMapping("/orderItems")
```

### 2. Use Proper HTTP Methods

```java
// Good
@GetMapping // Read
@PostMapping // Create
@PutMapping // Update (full)
@PatchMapping // Update (partial)
@DeleteMapping // Delete

// Bad
@PostMapping("/getUser") // Should be GET
@PostMapping("/createUser") // Should be POST
@PostMapping("/deleteUser") // Should be DELETE
```

### 3. Use Proper Status Codes

```java
// Good
@GetMapping // 200 OK
@PostMapping // 201 Created
@PutMapping // 200 OK
@DeleteMapping // 204 No Content

// Bad
@GetMapping // 200 OK with error message in body
@PostMapping // 200 OK instead of 201 Created
@DeleteMapping // 200 OK instead of 204 No Content
```

### 4. Use Pagination for Collections

```java
// Good
@GetMapping
public ResponseEntity<Page<User>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(userService.findAll(PageRequest.of(page, size)));
}

// Bad - Returns all records
@GetMapping
public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok(userService.findAll());
}
```

### 5. Use HATEOAS for Discoverability

```java
// Good - With links
@GetMapping("/{id}")
public EntityModel<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    EntityModel<User> resource = EntityModel.of(user);
    resource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
    return resource;
}

// Bad - Without links
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);
}
```

---

## Common Pitfalls

### 1. Not Using Proper HTTP Methods

```java
// Bad
@PostMapping("/getUser")
public User getUser(@RequestBody GetUserRequest request) {
    return userService.findById(request.getId());
}

// Good
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);
}
```

### 2. Not Using Proper Status Codes

```java
// Bad
@GetMapping("/{id}")
public ResponseEntity<?> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    if (user == null) {
        return ResponseEntity.ok("User not found");
    }
    return ResponseEntity.ok(user);
}

// Good
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    return userService.findById(id)
        .map(user -> ResponseEntity.ok(user))
        .orElse(ResponseEntity.notFound().build());
}
```

### 3. Not Using Pagination

```java
// Bad - Returns all records
@GetMapping
public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok(userService.findAll());
}

// Good - With pagination
@GetMapping
public ResponseEntity<Page<User>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(userService.findAll(PageRequest.of(page, size)));
}
```

---

## Further Reading

- [Microsoft REST API Guidelines](https://github.com/microsoft/api-guidelines)
- [Google API Design Guide](https://cloud.google.com/apis/design)
- [RESTful API Design - Best Practices](https://restfulapi.net/)
- [HATEOAS](https://en.wikipedia.org/wiki/HATEOAS)
