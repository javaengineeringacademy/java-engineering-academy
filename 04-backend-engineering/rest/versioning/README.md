# REST API Versioning

## Comprehensive Guide to REST API Versioning

API versioning is crucial for maintaining backward compatibility while introducing new features. This guide covers URI, header, query parameter, and content negotiation versioning strategies.

---

## Table of Contents

1. [Versioning Strategies](#versioning-strategies)
2. [URI Versioning](#uri-versioning)
3. [Header Versioning](#header-versioning)
4. [Query Parameter Versioning](#query-parameter-versioning)
5. [Content Negotiation Versioning](#content-negotiation-versioning)
6. [Breaking Changes](#breaking-changes)
7. [Best Practices](#best-practices)

---

## Versioning Strategies

### Strategy Comparison

| Strategy | Pros | Cons |
|----------|------|------|
| URI | Explicit, easy to test, cacheable | URL pollution, not RESTful |
| Header | Clean URLs, RESTful | Hard to test, less discoverable |
| Query Parameter | Easy to implement, discoverable | Not clean URLs, cacheable issues |
| Content Negotiation | Most RESTful, clean URLs | Complex, hard to test |

### When to Version

```java
// Version when:
// 1. Breaking changes are introduced
// 2. Major functionality changes
// 3. Response structure changes significantly
// 4. Authentication/authorization changes

// Don't version when:
// 1. Adding new optional fields
// 2. Adding new endpoints
// 3. Bug fixes
// 4. Documentation updates
```

---

## URI Versioning

### Basic URI Versioning

```java
// Version 1
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller {
    
    @GetMapping
    public ResponseEntity<List<UserV1>> getUsers() {
        List<UserV1> users = userService.findAllV1();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserV1> getUser(@PathVariable Long id) {
        UserV1 user = userService.findByIdV1(id);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    public ResponseEntity<UserV1> createUser(@RequestBody @Valid CreateUserV1Request request) {
        UserV1 user = userService.createV1(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}

// Version 2
@RestController
@RequestMapping("/api/v2/users")
public class UserV2Controller {
    
    @GetMapping
    public ResponseEntity<List<UserV2>> getUsers() {
        List<UserV2> users = userService.findAllV2();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserV2> getUser(@PathVariable Long id) {
        UserV2 user = userService.findByIdV2(id);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    public ResponseEntity<UserV2> createUser(@RequestBody @Valid CreateUserV2Request request) {
        UserV2 user = userService.createV2(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
```

### URI Versioning with Delegates

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller {
    
    private final UserDelegate delegate;
    
    public UserV1Controller(UserDelegate delegate) {
        this.delegate = delegate;
    }
    
    @GetMapping
    public ResponseEntity<List<UserV1>> getUsers() {
        return delegate.getUsersV1();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserV1> getUser(@PathVariable Long id) {
        return delegate.getUserV1(id);
    }
}

@RestController
@RequestMapping("/api/v2/users")
public class UserV2Controller {
    
    private final UserDelegate delegate;
    
    public UserV2Controller(UserDelegate delegate) {
        this.delegate = delegate;
    }
    
    @GetMapping
    public ResponseEntity<List<UserV2>> getUsers() {
        return delegate.getUsersV2();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserV2> getUser(@PathVariable Long id) {
        return delegate.getUserV2(id);
    }
}

@Component
public class UserDelegate {
    
    public ResponseEntity<List<UserV1>> getUsersV1() {
        // V1 logic
    }
    
    public ResponseEntity<UserV1> getUserV1(Long id) {
        // V1 logic
    }
    
    public ResponseEntity<List<UserV2>> getUsersV2() {
        // V2 logic
    }
    
    public ResponseEntity<UserV2> getUserV2(Long id) {
        // V2 logic
    }
}
```

### URI Versioning with Mappings

```java
@RestController
@RequestMapping({"/api/v1/users", "/api/v2/users"})
public class UserController {
    
    @GetMapping
    public ResponseEntity<?> getUsers(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        if (requestUri.contains("/v2/")) {
            return ResponseEntity.ok(getUsersV2());
        }
        
        return ResponseEntity.ok(getUsersV1());
    }
    
    private List<UserV1> getUsersV1() {
        // V1 logic
    }
    
    private List<UserV2> getUsersV2() {
        // V2 logic
    }
}
```

---

## Header Versioning

### Basic Header Versioning

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(headers = "X-API-Version=1")
    public ResponseEntity<List<UserV1>> getUsersV1() {
        List<UserV1> users = userService.findAllV1();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping(headers = "X-API-Version=2")
    public ResponseEntity<List<UserV2>> getUsersV2() {
        List<UserV2> users = userService.findAllV2();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestHeader(value = "X-API-Version", defaultValue = "1") String version) {
        
        if ("2".equals(version)) {
            return ResponseEntity.ok(getUsersV2());
        }
        
        return ResponseEntity.ok(getUsersV1());
    }
    
    private List<UserV1> getUsersV1() {
        // V1 logic
    }
    
    private List<UserV2> getUsersV2() {
        // V2 logic
    }
}
```

### Header Versioning with Custom Header

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(headers = "Api-Version=1")
    public ResponseEntity<List<UserV1>> getUsersV1() {
        return ResponseEntity.ok(userService.findAllV1());
    }
    
    @GetMapping(headers = "Api-Version=2")
    public ResponseEntity<List<UserV2>> getUsersV2() {
        return ResponseEntity.ok(userService.findAllV2());
    }
    
    @GetMapping(headers = "Api-Version=3")
    public ResponseEntity<List<UserV3>> getUsersV3() {
        return ResponseEntity.ok(userService.findAllV3());
    }
}
```

### Header Versioning with Accept Header

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(produces = "application/vnd.myapp.v1+json")
    public ResponseEntity<List<UserV1>> getUsersV1() {
        return ResponseEntity.ok(userService.findAllV1());
    }
    
    @GetMapping(produces = "application/vnd.myapp.v2+json")
    public ResponseEntity<List<UserV2>> getUsersV2() {
        return ResponseEntity.ok(userService.findAllV2());
    }
}
```

### Header Versioning Filter

```java
@Component
public class VersionFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String version = httpRequest.getHeader("X-API-Version");
        
        if (version == null) {
            version = "1"; // Default version
        }
        
        httpRequest.setAttribute("apiVersion", version);
        chain.doFilter(request, response);
    }
}

// Usage in controller
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping
    public ResponseEntity<?> getUsers(HttpServletRequest request) {
        String version = (String) request.getAttribute("apiVersion");
        
        if ("2".equals(version)) {
            return ResponseEntity.ok(getUsersV2());
        }
        
        return ResponseEntity.ok(getUsersV1());
    }
}
```

---

## Query Parameter Versioning

### Basic Query Parameter Versioning

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(params = "version=1")
    public ResponseEntity<List<UserV1>> getUsersV1() {
        return ResponseEntity.ok(userService.findAllV1());
    }
    
    @GetMapping(params = "version=2")
    public ResponseEntity<List<UserV2>> getUsersV2() {
        return ResponseEntity.ok(userService.findAllV2());
    }
    
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "1") String version) {
        
        if ("2".equals(version)) {
            return ResponseEntity.ok(getUsersV2());
        }
        
        return ResponseEntity.ok(getUsersV1());
    }
    
    private List<UserV1> getUsersV1() {
        // V1 logic
    }
    
    private List<UserV2> getUsersV2() {
        // V2 logic
    }
}
```

### Query Parameter Versioning with Multiple Parameters

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(params = {"version=1", "format=json"})
    public ResponseEntity<List<UserV1>> getUsersV1Json() {
        return ResponseEntity.ok(userService.findAllV1());
    }
    
    @GetMapping(params = {"version=2", "format=json"})
    public ResponseEntity<List<UserV2>> getUsersV2Json() {
        return ResponseEntity.ok(userService.findAllV2());
    }
    
    @GetMapping(params = {"version=1", "format=xml"})
    public ResponseEntity<List<UserV1>> getUsersV1Xml() {
        return ResponseEntity.ok(userService.findAllV1());
    }
}
```

### Query Parameter Versioning with Default

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(value = "version", defaultValue = "1") String version,
            @RequestParam(value = "format", defaultValue = "json") String format) {
        
        if ("2".equals(version)) {
            return ResponseEntity.ok(getUsersV2(format));
        }
        
        return ResponseEntity.ok(getUsersV1(format));
    }
    
    private Object getUsersV1(String format) {
        if ("xml".equals(format)) {
            return userService.findAllV1Xml();
        }
        return userService.findAllV1();
    }
    
    private Object getUsersV2(String format) {
        if ("xml".equals(format)) {
            return userService.findAllV2Xml();
        }
        return userService.findAllV2();
    }
}
```

---

## Content Negotiation Versioning

### Basic Content Negotiation

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(produces = "application/vnd.myapp.v1+json")
    public ResponseEntity<List<UserV1>> getUsersV1() {
        return ResponseEntity.ok(userService.findAllV1());
    }
    
    @GetMapping(produces = "application/vnd.myapp.v2+json")
    public ResponseEntity<List<UserV2>> getUsersV2() {
        return ResponseEntity.ok(userService.findAllV2());
    }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserV1>> getUsersDefault() {
        return ResponseEntity.ok(userService.findAllV1());
    }
}
```

### Content Negotiation with Custom Media Types

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(produces = "application/vnd.company.user.v1+json")
    public ResponseEntity<List<UserV1>> getUsersV1() {
        return ResponseEntity.ok(userService.findAllV1());
    }
    
    @GetMapping(produces = "application/vnd.company.user.v2+json")
    public ResponseEntity<List<UserV2>> getUsersV2() {
        return ResponseEntity.ok(userService.findAllV2());
    }
    
    @GetMapping(produces = "application/vnd.company.user.v3+json")
    public ResponseEntity<List<UserV3>> getUsersV3() {
        return ResponseEntity.ok(userService.findAllV3());
    }
}
```

### Content Negotiation with Vendor Media Type

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(produces = "application/vnd.myapp.user+json;version=1")
    public ResponseEntity<List<UserV1>> getUsersV1() {
        return ResponseEntity.ok(userService.findAllV1());
    }
    
    @GetMapping(produces = "application/vnd.myapp.user+json;version=2")
    public ResponseEntity<List<UserV2>> getUsersV2() {
        return ResponseEntity.ok(userService.findAllV2());
    }
}
```

### Content Negotiation Configuration

```java
@Configuration
public class ContentNegotiationConfig implements WebMvcConfigurer {
    
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .favorParameter(true)
            .parameterName("format")
            .ignoreAcceptHeader(false)
            .useRegisteredExtensionsOnly(false);
    }
}
```

---

## Breaking Changes

### Identifying Breaking Changes

```java
// Breaking Changes:
// 1. Removing fields from response
// 2. Changing field types
// 3. Changing field names
// 4. Changing endpoint URLs
// 5. Changing HTTP methods
// 6. Changing authentication requirements
// 7. Changing validation rules

// Non-Breaking Changes:
// 1. Adding new fields to response
// 2. Adding new endpoints
// 3. Adding new query parameters
// 4. Adding new headers
// 5. Adding new media types
```

### Handling Breaking Changes

```java
// Strategy 1: Version the endpoint
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller {
    // V1 implementation
}

@RestController
@RequestMapping("/api/v2/users")
public class UserV2Controller {
    // V2 implementation with breaking changes
}

// Strategy 2: Deprecate old endpoint
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(headers = "X-API-Version=1")
    @Deprecated
    public ResponseEntity<List<UserV1>> getUsersV1() {
        // Deprecated but still works
    }
    
    @GetMapping(headers = "X-API-Version=2")
    public ResponseEntity<List<UserV2>> getUsersV2() {
        // New version
    }
}
```

### Deprecation Strategy

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(headers = "X-API-Version=1")
    @Deprecated(since = "2.0", forRemoval = true)
    public ResponseEntity<List<UserV1>> getUsersV1() {
        // Add deprecation headers
        return ResponseEntity.ok()
            .header("Deprecation", "true")
            .header("Sunset", "Sat, 01 Jan 2025 00:00:00 GMT")
            .header("Link", "</api/v2/users>; rel=\"successor-version\"")
            .body(userService.findAllV1());
    }
    
    @GetMapping(headers = "X-API-Version=2")
    public ResponseEntity<List<UserV2>> getUsersV2() {
        return ResponseEntity.ok(userService.findAllV2());
    }
}
```

---

## Best Practices

### 1. Use URI Versioning for Public APIs

```java
// Good - URI versioning for public APIs
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller { ... }

@RestController
@RequestMapping("/api/v2/users")
public class UserV2Controller { ... }
```

### 2. Use Header Versioning for Internal APIs

```java
// Good - Header versioning for internal APIs
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(headers = "X-API-Version=1")
    public ResponseEntity<List<UserV1>> getUsersV1() { ... }
    
    @GetMapping(headers = "X-API-Version=2")
    public ResponseEntity<List<UserV2>> getUsersV2() { ... }
}
```

### 3. Maintain Old Versions

```java
// Good - Maintain old versions for backward compatibility
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller {
    // Keep V1 working even after V2 is released
}

@RestController
@RequestMapping("/api/v2/users")
public class UserV2Controller {
    // New version with improvements
}
```

### 4. Document Versioning Strategy

```java
// Good - Document versioning in API documentation
@OpenAPIDefinition(info = @Info(
    title = "User API",
    version = "1.0",
    description = "User API with versioning support. " +
                  "Use X-API-Version header to specify version. " +
                  "Default version is 1."))
@RestController
@RequestMapping("/api/users")
public class UserController { ... }
```

### 5. Use Semantic Versioning

```java
// Good - Semantic versioning
// MAJOR.MINOR.PATCH
// MAJOR: Breaking changes
// MINOR: New features (backward compatible)
// PATCH: Bug fixes (backward compatible)

// Example:
// v1.0.0 - Initial release
// v1.1.0 - Add new field
// v1.1.1 - Fix bug
// v2.0.0 - Breaking change
```

---

## Common Pitfalls

### 1. Not Versioning Breaking Changes

```java
// Bad - Not versioning breaking changes
@PutMapping("/{id}")
public User updateUser(@PathVariable Long id, @RequestBody User user) {
    // Changed response structure without versioning
    return userService.update(id, user);
}

// Good - Versioning breaking changes
@PutMapping("/{id}", headers = "X-API-Version=1")
public UserV1 updateUserV1(@PathVariable Long id, @RequestBody UserV1 user) {
    return userService.updateV1(id, user);
}

@PutMapping("/{id}", headers = "X-API-Version=2")
public UserV2 updateUserV2(@PathVariable Long id, @RequestBody UserV2 user) {
    return userService.updateV2(id, user);
}
```

### 2. Removing Old Versions Too Quickly

```java
// Bad - Removing old version too quickly
@RestController
@RequestMapping("/api/v2/users") // V1 removed after 1 month
public class UserV2Controller { ... }

// Good - Maintaining old versions
@RestController
@RequestMapping("/api/v1/users") // Keep for at least 6-12 months
public class UserV1Controller { ... }

@RestController
@RequestMapping("/api/v2/users")
public class UserV2Controller { ... }
```

### 3. Not Documenting Versioning

```java
// Bad - Not documenting versioning
@RestController
@RequestMapping("/api/users")
public class UserController { ... }

// Good - Documenting versioning
@OpenAPIDefinition(info = @Info(
    title = "User API",
    version = "2.0",
    description = "User API v2. Use X-API-Version header to specify version."))
@RestController
@RequestMapping("/api/users")
public class UserController { ... }
```

---

## Further Reading

- [API Versioning Best Practices](https://www.postman.com/api-platform/api-versioning-best-practices/)
- [Microsoft REST API Guidelines - Versioning](https://github.com/microsoft/api-guidelines/blob/vNext/azure/Guidelines.md#versioning)
- [Stripe API Versioning](https://stripe.com/blog/api-versioning)
- [GitHub API Versioning](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#api-versioning)
