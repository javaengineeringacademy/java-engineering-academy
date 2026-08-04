# REST Fundamentals

## Comprehensive Guide to REST Architecture

REST (Representational State Transfer) is an architectural style for designing networked applications. This guide covers REST constraints, HTTP methods, status codes, and resource modeling.

---

## Table of Contents

1. [REST Constraints](#rest-constraints)
2. [HTTP Methods](#http-methods)
3. [Status Codes](#status-codes)
4. [Resource Modeling](#resource-modeling)
5. [Request/Response](#requestresponse)
6. [Best Practices](#best-practices)

---

## REST Constraints

### Six Constraints of REST

1. **Client-Server Architecture**: Separation of concerns between UI and data storage
2. **Stateless**: Each request contains all information needed to process it
3. **Cacheable**: Responses must define themselves as cacheable or not
4. **Uniform Interface**: Standardized way of communicating
5. **Layered System**: Client cannot tell if connected directly to server
6. **Code on Demand** (optional): Server can extend client functionality

### Stateless Implementation

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // Stateless - Each request is independent
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        // No session state maintained
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
    
    // Stateless with authentication
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserWithAuth(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        // Token provides all necessary context
        User user = userService.findById(id, token);
        return ResponseEntity.ok(user);
    }
}
```

### Uniform Interface

```java
// Resource identification
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);
}

// Resource manipulation through representations
@PutMapping("/users/{id}")
public User updateUser(@PathVariable Long id, @RequestBody User user) {
    return userService.update(id, user);
}

// Self-descriptive messages
@GetMapping("/users")
public ResponseEntity<List<User>> getUsers(
        @RequestHeader("Accept") String accept) {
    List<User> users = userService.findAll();
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(accept))
        .body(users);
}

// HATEOAS
@GetMapping("/users/{id}")
public EntityModel<User> getUserWithLinks(@PathVariable Long id) {
    User user = userService.findById(id);
    EntityModel<User> resource = EntityModel.of(user);
    resource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
    resource.add(linkTo(methodOn(UserController.class).getUsers()).withRel("users"));
    return resource;
}
```

---

## HTTP Methods

### GET - Retrieve Resources

```java
@GetMapping
public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.findAll();
    return ResponseEntity.ok(users);
}

@GetMapping("/{id}")
public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return userService.findById(id)
        .map(user -> ResponseEntity.ok(user))
        .orElse(ResponseEntity.notFound().build());
}
```

### POST - Create Resources

```PostMapping
public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
    User created = userService.create(user);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getId())
        .toUri();
    return ResponseEntity.created(location).body(created);
}
```

### PUT - Replace Resources

```java
@PutMapping("/{id}")
public ResponseEntity<User> updateUser(
        @PathVariable Long id,
        @RequestBody @Valid User user) {
    User updated = userService.update(id, user);
    return ResponseEntity.ok(updated);
}
```

### PATCH - Partial Update

```java
@PatchMapping("/{id}")
public ResponseEntity<User> patchUser(
        @PathVariable Long id,
        @RequestBody Map<String, Object> updates) {
    User patched = userService.patch(id, updates);
    return ResponseEntity.ok(patched);
}
```

### DELETE - Remove Resources

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
}
```

### HEAD - Check Resource Existence

```java
@RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
public ResponseEntity<Void> checkUserExists(@PathVariable Long id) {
    if (userService.existsById(id)) {
        return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
}
```

### OPTIONS - Get Supported Methods

```java
@RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
public ResponseEntity<Void> optionsUser(@PathVariable Long id) {
    return ResponseEntity.ok()
        .allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
        .build();
}
```

---

## Status Codes

### 2xx Success

```java
// 200 OK
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    return ResponseEntity.ok(user);
}

// 201 Created
@PostMapping
public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
    User created = userService.create(user);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getId())
        .toUri();
    return ResponseEntity.created(location).body(created);
}

// 204 No Content
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
}

// 206 Partial Content
@GetMapping(value = "/{id}/avatar", produces = MediaType.IMAGE_PNG_VALUE)
public ResponseEntity<Resource> getUserAvatar(@PathVariable Long id,
                                              @RequestHeader("Range") String range) {
    Resource resource = userService.getAvatar(id);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_RANGE, "bytes 0-999/1000")
        .body(resource);
}
```

### 3xx Redirection

```java
// 301 Moved Permanently
@GetMapping("/old-path")
public ResponseEntity<Void> oldEndpoint() {
    return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
        .location(URI.create("/api/new-path"))
        .build();
}

// 304 Not Modified
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id,
                                    @RequestHeader("If-None-Match") String etag) {
    User user = userService.findById(id);
    String currentEtag = generateEtag(user);
    
    if (currentEtag.equals(etag)) {
        return ResponseEntity.notModified().build();
    }
    
    return ResponseEntity.ok()
        .eTag(currentEtag)
        .body(user);
}
```

### 4xx Client Errors

```java
// 400 Bad Request
@PostMapping
public ResponseEntity<ErrorResponse> createUser(@RequestBody @Valid User user) {
    try {
        User created = userService.create(user);
        return ResponseEntity.ok().body(created);
    } catch (ValidationException e) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("VALIDATION_ERROR", e.getMessage()));
    }
}

// 401 Unauthorized
@GetMapping("/protected")
public ResponseEntity<Void> protectedEndpoint() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .header("WWW-Authenticate", "Bearer")
        .build();
}

// 403 Forbidden
@GetMapping("/admin")
public ResponseEntity<Void> adminEndpoint() {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ErrorResponse("FORBIDDEN", "Insufficient permissions"));
}

// 404 Not Found
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    return userService.findById(id)
        .map(user -> ResponseEntity.ok(user))
        .orElse(ResponseEntity.notFound().build());
}

// 405 Method Not Allowed
@RequestMapping(value = "/{id}", method = RequestMethod.POST)
public ResponseEntity<Void> methodNotAllowed() {
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
        .build();
}

// 409 Conflict
@PostMapping
public ResponseEntity<ErrorResponse> createUserConflict(@RequestBody User user) {
    try {
        User created = userService.create(user);
        return ResponseEntity.ok().body(created);
    } catch (DuplicateResourceException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("CONFLICT", e.getMessage()));
    }
}

// 429 Too Many Requests
@GetMapping
public ResponseEntity<List<User>> getUsers() {
    if (rateLimiter.isExceeded()) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .header("Retry-After", "60")
            .build();
    }
    return ResponseEntity.ok(userService.findAll());
}
```

### 5xx Server Errors

```java
// 500 Internal Server Error
@GetMapping("/{id}")
public ResponseEntity<ErrorResponse> getUserInternalError(@PathVariable Long id) {
    try {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    } catch (Exception e) {
        log.error("Internal error retrieving user: {}", id, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"));
    }
}

// 503 Service Unavailable
@GetMapping
public ResponseEntity<List<User>> getUsersServiceUnavailable() {
    if (!serviceRegistry.isAvailable("user-service")) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .header("Retry-After", "30")
            .build();
    }
    return ResponseEntity.ok(userService.findAll());
}
```

---

## Resource Modeling

### Single Resource

```java
// Resource class
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// Resource representation
@Data
@Builder
public class UserResource {
    private Long id;
    private String username;
    private String email;
    private List<Link> links;
    
    public static UserResource from(User user) {
        return UserResource.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }
}
```

### Collection Resource

```java
// Collection response
@Data
@Builder
public class UserCollection {
    private List<UserResource> users;
    private int totalElements;
    private int totalPages;
    private int currentPage;
    private List<Link> links;
}

// Collection endpoint
@GetMapping
public ResponseEntity<UserCollection> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    Page<User> users = userService.findAll(PageRequest.of(page, size));
    
    UserCollection collection = UserCollection.builder()
        .users(users.getContent().stream()
            .map(UserResource::from)
            .collect(Collectors.toList()))
        .totalElements((int) users.getTotalElements())
        .totalPages(users.getTotalPages())
        .currentPage(page)
        .build();
    
    return ResponseEntity.ok(collection);
}
```

### Related Resources

```java
// Nested resource
@GetMapping("/{userId}/posts")
public ResponseEntity<List<PostResource>> getUserPosts(@PathVariable Long userId) {
    List<Post> posts = postService.findByUserId(userId);
    return ResponseEntity.ok(posts.stream()
        .map(PostResource::from)
        .collect(Collectors.toList()));
}

// Related resource with link
@GetMapping("/{id}")
public EntityModel<User> getUserWithLinks(@PathVariable Long id) {
    User user = userService.findById(id);
    EntityModel<User> resource = EntityModel.of(user);
    
    resource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
    resource.add(linkTo(methodOn(UserController.class).getUserPosts(id)).withRel("posts"));
    
    return resource;
}
```

### Sub-Resource

```java
@RestController
@RequestMapping("/api/users/{userId}/addresses")
public class UserAddressController {
    
    @GetMapping
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable Long userId) {
        List<Address> addresses = addressService.findByUserId(userId);
        return ResponseEntity.ok(addresses);
    }
    
    @PostMapping
    public ResponseEntity<Address> createUserAddress(
            @PathVariable Long userId,
            @RequestBody @Valid Address address) {
        Address created = addressService.create(userId, address);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(location).body(created);
    }
    
    @GetMapping("/{addressId}")
    public ResponseEntity<Address> getUserAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {
        return addressService.findByUserIdAndId(userId, addressId)
            .map(address -> ResponseEntity.ok(address))
            .orElse(ResponseEntity.notFound().build());
    }
}
```

---

## Request/Response

### Request Headers

```java
@GetMapping
public ResponseEntity<List<User>> getUsers(
        @RequestHeader(value = "Accept", defaultValue = "application/json") String accept,
        @RequestHeader(value = "Accept-Language", defaultValue = "en") String language,
        @RequestHeader(value = "X-Request-Id", required = false) String requestId) {
    
    List<User> users = userService.findAll();
    
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(accept))
        .header("X-Request-Id", requestId)
        .body(users);
}
```

### Response Headers

```java
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .cacheControl(CacheControl.maxAge(Duration.ofMinutes(30)))
        .eTag(generateEtag(user))
        .lastModified(user.getUpdatedAt())
        .header("X-Total-Count", String.valueOf(1))
        .body(user);
}
```

### Content Negotiation

```java
@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public ResponseEntity<List<User>> getUsers(
        @RequestHeader("Accept") String accept) {
    List<User> users = userService.findAll();
    
    if (accept.contains(MediaType.APPLICATION_XML_VALUE)) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .body(users);
    }
    
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(users);
}
```

### Request Body

```java
@PostMapping
public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
    User created = userService.create(user);
    return ResponseEntity.ok(created);
}

// With different content types
@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<User> createUserJson(@RequestBody @Valid User user) {
    return ResponseEntity.ok(userService.create(user));
}

@PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
public ResponseEntity<User> createUserXml(@RequestBody @Valid User user) {
    return ResponseEntity.ok(userService.create(user));
}
```

---

## Best Practices

### 1. Use Nouns for Resources

```java
// Good
@GetMapping("/users")
@GetMapping("/users/{id}")
@GetMapping("/orders")
@GetMapping("/products")

// Bad
@GetMapping("/getUsers")
@GetMapping("/getUserById")
@GetMapping("/createUser")
@GetMapping("/deleteUser")
```

### 2. Use HTTP Methods Correctly

```java
// GET - Read
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) { ... }

// POST - Create
@PostMapping
public User createUser(@RequestBody User user) { ... }

// PUT - Update entire resource
@PutMapping("/{id}")
public User updateUser(@PathVariable Long id, @RequestBody User user) { ... }

// PATCH - Partial update
@PatchMapping("/{id}")
public User patchUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) { ... }

// DELETE - Remove
@DeleteMapping("/{id}")
public void deleteUser(@PathVariable Long id) { ... }
```

### 3. Use Proper Status Codes

```java
// 200 OK - Successful GET, PUT, PATCH
// 201 Created - Successful POST
// 204 No Content - Successful DELETE
// 400 Bad Request - Invalid input
// 401 Unauthorized - Authentication required
// 403 Forbidden - Insufficient permissions
// 404 Not Found - Resource doesn't exist
// 409 Conflict - Resource already exists
// 422 Unprocessable Entity - Validation error
// 429 Too Many Requests - Rate limit exceeded
// 500 Internal Server Error - Server error
// 503 Service Unavailable - Service down
```

### 4. Use HATEOAS

```java
@GetMapping("/{id}")
public EntityModel<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    EntityModel<User> resource = EntityModel.of(user);
    
    resource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
    resource.add(linkTo(methodOn(UserController.class).getUsers()).withRel("users"));
    resource.add(linkTo(methodOn(UserAddressController.class).getUserAddresses(id))
        .withRel("addresses"));
    
    return resource;
}
```

### 5. Version Your API

```java
// URI versioning
@RequestMapping("/api/v1/users")
public class UserV1Controller { ... }

@RequestMapping("/api/v2/users")
public class UserV2Controller { ... }

// Header versioning
@GetMapping(value = "/users", headers = "X-API-Version=1")
public List<UserV1> getUsersV1() { ... }

@GetMapping(value = "/users", headers = "X-API-Version=2")
public List<UserV2> getUsersV2() { ... }
```

---

## Common Pitfalls

### 1. Using Verbs Instead of Nouns

```java
// Bad
@GetMapping("/getUser")
@PostMapping("/createUser")
@DeleteMapping("/deleteUser")

// Good
@GetMapping("/users")
@PostMapping("/users")
@DeleteMapping("/users/{id}")
```

### 2. Not Using Proper Status Codes

```java
// Bad
@GetMapping("/{id}")
public ResponseEntity<?> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    if (user == null) {
        return ResponseEntity.ok("User not found"); // Wrong!
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

### 3. Ignoring Idempotency

```java
// Bad - POST should not be idempotent for different bodies
@PostMapping
public User createUser(@RequestBody User user) {
    return userService.create(user);
}

// Good - PUT should be idempotent
@PutMapping("/{id}")
public User updateUser(@PathVariable Long id, @RequestBody User user) {
    return userService.update(id, user);
}
```

---

## Further Reading

- [REST API Design - Resource Modeling](https://restfulapi.net/resource-modeling/)
- [HTTP Status Codes](https://httpstatuses.com/)
- [Richardson Maturity Model](https://martinfowler.com/articles/richardsonMaturityModel.html)
- [Microsoft REST API Guidelines](https://github.com/microsoft/api-guidelines)
