# API Versioning Strategies

## Comprehensive Guide to API Versioning

API versioning allows you to evolve your API without breaking existing clients. This guide covers URI, header, and content negotiation versioning strategies.

---

## Table of Contents

1. [Versioning Overview](#versioning-overview)
2. [URI Versioning](#uri-versioning)
3. [Header Versioning](#header-versioning)
4. [Content Negotiation](#content-negotiation)
5. [Deprecation](#deprecation)
6. [Best Practices](#best-practices)

---

## Versioning Overview

### When to Version

```
Version When:
- Breaking changes to response structure
- Removing fields or endpoints
- Changing field types
- Changing authentication requirements
- Major business logic changes

Don't Version For:
- Adding new optional fields
- Adding new endpoints
- Bug fixes
- Security patches
```

### Versioning Strategies

```
1. URI Versioning: /api/v1/users
2. Header Versioning: X-API-Version: 1
3. Query Parameter: /api/users?version=1
4. Content Negotiation: Accept: application/vnd.api.v1+json
```

---

## URI Versioning

### Implementation

```java
// v1 Controller
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users v1")
public class UserV1Controller {

    @GetMapping("/{id}")
    public ResponseEntity<UserV1> getUser(@PathVariable Long id) {
        UserV1 user = userServiceV1.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserV1> createUser(@RequestBody CreateUserV1Request request) {
        UserV1 user = userServiceV1.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}

// v2 Controller
@RestController
@RequestMapping("/api/v2/users")
@Tag(name = "Users v2")
public class UserV2Controller {

    @GetMapping("/{id}")
    public ResponseEntity<UserV2> getUser(@PathVariable Long id) {
        UserV2 user = userServiceV2.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserV2> createUser(@RequestBody CreateUserV2Request request) {
        UserV2 user = userServiceV2.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
```

### Version Models

```java
// V1 Model
@Schema(description = "User (v1)")
public class UserV1 {
    private Long id;
    private String name;
    private String email;
}

// V2 Model - added fields
@Schema(description = "User (v2)")
public class UserV2 {
    private String id;  // Changed from Long to String (UUID)
    private String name;
    private String email;
    private UserProfile profile;  // New field
    private LocalDateTime createdAt;
}
```

### Pros and Cons

```
Pros:
+ Simple to implement
+ Easy to test
+ Clear in documentation
+ Browser-cacheable

Cons:
- URL pollution
- URI breaks on version change
- Not RESTful (resource should be same)
```

---

## Header Versioning

### Implementation

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-API-Version",
                defaultValue = "1") int version) {

        return switch (version) {
            case 1 -> ResponseEntity.ok(userServiceV1.findById(id));
            case 2 -> ResponseEntity.ok(userServiceV2.findById(id));
            default -> ResponseEntity.badRequest()
                .body(Map.of("error", "Unsupported version"));
        };
    }
}
```

### Version Resolver

```java
@Component
public class ApiVersionResolver {

    @Value("${api.default-version:1}")
    private int defaultVersion;

    public int resolveVersion(HttpServletRequest request) {
        // Check header
        String headerVersion = request.getHeader("X-API-Version");
        if (headerVersion != null) {
            try {
                return Integer.parseInt(headerVersion);
            } catch (NumberFormatException e) {
                throw new InvalidApiVersionException(headerVersion);
            }
        }

        // Check query parameter
        String queryVersion = request.getParameter("version");
        if (queryVersion != null) {
            try {
                return Integer.parseInt(queryVersion);
            } catch (NumberFormatException e) {
                throw new InvalidApiVersionException(queryVersion);
            }
        }

        return defaultVersion;
    }
}
```

### Interceptor

```java
@Component
public class ApiVersionInterceptor implements HandlerInterceptor {

    private final ApiVersionResolver versionResolver;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        int version = versionResolver.resolveVersion(request);
        request.setAttribute("apiVersion", version);
        return true;
    }
}
```

### Pros and Cons

```
Pros:
+ Clean URLs
+ Resource stays same
+ Flexible

Cons:
- Harder to test
- Not browser-cacheable
- Hidden from users
```

---

## Content Negotiation

### Implementation

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}", produces = {
        "application/vnd.company.user.v1+json",
        "application/vnd.company.user.v2+json"
    })
    public ResponseEntity<?> getUser(
            @PathVariable Long id,
            @RequestHeader("Accept") String accept) {

        if (accept.contains("v2")) {
            return ResponseEntity.ok(userServiceV2.findById(id));
        }

        return ResponseEntity.ok(userServiceV1.findById(id));
    }
}
```

### Content Negotiation Config

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer config) {
        config.favorParameter(false)
            .favorPathExtension(false)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("v1", MediaType.parseMediaType("application/vnd.company.user.v1+json"))
            .mediaType("v2", MediaType.parseMediaType("application/vnd.company.user.v2+json"));
    }
}
```

### Pros and Cons

```
Pros:
+ Most RESTful
+ Resource stays same
+ Proper content types

Cons:
- Complex to implement
- Hard to test
- Not browser-cacheable
```

---

## Deprecation

### Deprecation Header

```java
@GetMapping("/{id}")
public ResponseEntity<UserV1> getUser(@PathVariable Long id) {
    UserV1 user = userService.findById(id);

    return ResponseEntity.ok()
        .header("Deprecation", "true")
        .header("Sunset", "2025-01-01")
        .header("Link", "</api/v2/users/" + id + ">; rel=\"successor-version\"")
        .body(user);
}
```

### Deprecation Annotations

```java
@Deprecated(since = "2.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller { }
```

### Deprecation Configuration

```yaml
api:
  versioning:
    v1:
      status: deprecated
      sunset: 2025-01-01
      successor: v2
```

---

## Best Practices

### 1. Support Multiple Versions

```java
// Support at least 2 versions
@RequestMapping("/api/v1/users")
public class UserV1Controller { }

@RequestMapping("/api/v2/users")
public class UserV2Controller { }
```

### 2. Document Versions

```yaml
openapi: 3.0.3
info:
  version: 1.0.0
  description: |
    ## Version History
    - v2: Current version (released 2024)
    - v1: Deprecated (sunset 2025-01-01)

servers:
  - url: https://api.example.com/v2
    description: Current version (v2)
  - url: https://api.example.com/v1
    description: Deprecated version (v1)
```

### 3. Use Semantic Versioning

```
Version Format: MAJOR.MINOR.PATCH

MAJOR: Breaking changes
MINOR: New features (backward compatible)
PATCH: Bug fixes

Examples:
1.0.0 -> 1.0.1 (bug fix)
1.0.1 -> 1.1.0 (new feature)
1.1.0 -> 2.0.0 (breaking change)
```

### 4. Communicate Changes

```java
@GetMapping("/{id}")
public ResponseEntity<UserV1> getUser(@PathVariable Long id) {
    UserV1 user = userService.findById(id);

    HttpHeaders headers = new HttpHeaders();
    headers.add("X-API-Deprecated", "true");
    headers.add("X-API-Sunset", "2025-01-01");
    headers.add("X-API-Change-Log", "See /docs/changelog#v1");

    return ResponseEntity.ok().headers(headers).body(user);
}
```

### 5. Maintain Backward Compatibility

```java
// Add new fields as optional
@Schema(description = "User (v2)")
public class UserV2 {
    private String id;
    private String name;
    private String email;
    private UserProfile profile;  // Optional in v2
}
```

---

## Further Reading

- [Microsoft REST API Guidelines - Versioning](https://github.com/microsoft/api-guidelines/blob/vNext/azure/Guidelines.md#versioning)
- [Stripe API Versioning](https://stripe.com/blog/api-versioning)
- [Google API Versioning](https://cloud.google.com/apis/design/versioning)
- [API Evolution](https://martinfowler.com/articles/api-evolution.html)
