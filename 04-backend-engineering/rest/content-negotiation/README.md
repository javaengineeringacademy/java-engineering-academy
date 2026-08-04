# Content Negotiation

## Comprehensive Guide to REST Content Negotiation

Content negotiation allows clients and servers to agree on the best representation for a resource. This guide covers JSON, XML, YAML, content types, and the Accept header.

---

## Table of Contents

1. [Content Negotiation Basics](#content-negotiation-basics)
2. [JSON](#json)
3. [XML](#xml)
4. [YAML](#yaml)
5. [Accept Header](#accept-header)
6. [Content-Type Header](#content-type-header)
7. [Best Practices](#best-practices)

---

## Content Negotiation Basics

### How Content Negotiation Works

```
Client Request:
GET /api/users/1
Accept: application/json

Server Response:
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
}
```

### Content Negotiation Process

```
1. Client sends Accept header
2. Server checks supported media types
3. Server selects best match
4. Server responds with Content-Type header
5. Client processes response
```

### Configuration

```java
@Configuration
public class ContentNegotiationConfig implements WebMvcConfigurer {
    
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("yaml", MediaType.parseMediaType("application/x-yaml"))
            .favorParameter(true)
            .parameterName("format")
            .ignoreAcceptHeader(false)
            .useRegisteredExtensionsOnly(false);
    }
    
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Add custom message converters if needed
    }
}
```

---

## JSON

### Basic JSON Support

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(user);
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User created = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(created);
    }
}
```

### JSON with Jackson

```java
// Custom Jackson configuration
@Configuration
public class JacksonConfig {
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Configure serialization
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        
        // Configure deserialization
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        
        // Configure date format
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.setTimeZone(TimeZone.getDefault());
        
        // Register modules
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        return mapper;
    }
}
```

### JSON Response Formatting

```java
@Data
@Builder
public class User {
    
    @JsonProperty("user_id")
    private Long id;
    
    @JsonProperty("full_name")
    private String name;
    
    @JsonProperty("email_address")
    private String email;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdAt;
    
    @JsonIgnore
    private String password;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> roles;
}
```

### JSON with Generic Types

```java
// Response with generic type
@GetMapping
public ResponseEntity<PaginatedResponse<User>> getUsers() {
    PaginatedResponse<User> response = userService.findAllPaginated();
    return ResponseEntity.ok(response);
}

// Generic response class
@Data
public class PaginatedResponse<T> {
    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
```

---

## XML

### Basic XML Support

```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<List<User>> getUsersXml() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .body(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .body(user);
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User created = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_XML)
            .body(created);
    }
}
```

### XML with JAXB Annotations

```java
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class User {
    
    @XmlElement(name = "user_id")
    private Long id;
    
    @XmlElement(name = "full_name")
    private String name;
    
    @XmlElement(name = "email_address")
    private String email;
    
    @XmlElement(name = "created_at")
    @XmlSchemaType(name = "dateTime")
    private LocalDateTime createdAt;
    
    @XmlTransient
    private String password;
}
```

### XML Response

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

---

## YAML

### Basic YAML Support

```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-yaml</artifactId>
</dependency>
```

```java
@Configuration
public class YamlConfig {
    
    @Bean
    public YamlMapper yamlMapper() {
        return new YamlMapper();
    }
}
```

### YAML Response

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(produces = "application/x-yaml")
    public ResponseEntity<String> getUsersYaml() {
        List<User> users = userService.findAll();
        
        YamlMapper yamlMapper = new YamlMapper();
        String yaml = yamlMapper.writeValueAsString(users);
        
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/x-yaml"))
            .body(yaml);
    }
    
    @GetMapping(value = "/{id}", produces = "application/x-yaml")
    public ResponseEntity<String> getUserYaml(@PathVariable Long id) {
        User user = userService.findById(id);
        
        YamlMapper yamlMapper = new YamlMapper();
        String yaml = yamlMapper.writeValueAsString(user);
        
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/x-yaml"))
            .body(yaml);
    }
}
```

### YAML with Spring

```yaml
# application.yml
spring:
  jackson:
    serialization:
      write-dates-as-timestamps: false
    default-property-inclusion: non_null
  
  mvc:
    content-negotiation:
      favor-parameter: true
      parameter-name: format
      media-types:
        json: application/json
        xml: application/xml
        yaml: application/x-yaml
```

---

## Accept Header

### Basic Accept Header

```java
@GetMapping
public ResponseEntity<?> getUsers(@RequestHeader("Accept") String accept) {
    if (accept.contains(MediaType.APPLICATION_XML_VALUE)) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .body(userService.findAll());
    }
    
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(userService.findAll());
}
```

### Accept Header with Quality Values

```java
// Client request:
// Accept: application/json;q=0.9, application/xml;q=0.8, */*;q=0.7

@GetMapping
public ResponseEntity<?> getUsers(@RequestHeader("Accept") String accept) {
    // Parse quality values
    List<MediaType> acceptableTypes = MediaType.parseMediaTypes(accept);
    
    // Sort by quality
    acceptableTypes.sort((a, b) -> Double.compare(b.getQualityValue(), a.getQualityValue()));
    
    for (MediaType type : acceptableTypes) {
        if (type.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.findAll());
        }
        
        if (type.isCompatibleWith(MediaType.APPLICATION_XML)) {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(userService.findAll());
        }
    }
    
    return ResponseEntity.badRequest().build();
}
```

### Accept Header with Wildcards

```java
// Client request:
// Accept: */*

@GetMapping
public ResponseEntity<?> getUsers(@RequestHeader("Accept") String accept) {
    if (accept.equals("*/*")) {
        // Default to JSON
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.findAll());
    }
    
    // Handle specific media types
    return handleMediaType(accept);
}
```

### Accept Header with Multiple Types

```java
// Client request:
// Accept: application/json, application/xml

@GetMapping
public ResponseEntity<?> getUsers(@RequestHeader("Accept") String accept) {
    List<MediaType> acceptableTypes = MediaType.parseMediaTypes(accept);
    
    for (MediaType type : acceptableTypes) {
        if (type.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.findAll());
        }
    }
    
    for (MediaType type : acceptableTypes) {
        if (type.isCompatibleWith(MediaType.APPLICATION_XML)) {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(userService.findAll());
        }
    }
    
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
}
```

---

## Content-Type Header

### Setting Content-Type

```java
@GetMapping
public ResponseEntity<List<User>> getUsers() {
    List<User> users = userService.findAll();
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(users);
}

@PostMapping
public ResponseEntity<User> createUser(@RequestBody User user) {
    User created = userService.create(user);
    return ResponseEntity.status(HttpStatus.CREATED)
        .contentType(MediaType.APPLICATION_JSON)
        .body(created);
}
```

### Content-Type with Parameters

```java
@GetMapping
public ResponseEntity<List<User>> getUsers() {
    List<User> users = userService.findAll();
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
        .body(users);
}
```

### Content-Type Negotiation

```java
@GetMapping
public ResponseEntity<?> getUsers(@RequestHeader("Accept") String accept) {
    List<User> users = userService.findAll();
    
    // Check for JSON
    if (accept.contains("application/json")) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(users);
    }
    
    // Check for XML
    if (accept.contains("application/xml")) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .body(users);
    }
    
    // Check for YAML
    if (accept.contains("application/x-yaml")) {
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/x-yaml"))
            .body(toYaml(users));
    }
    
    // Default to JSON
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(users);
}
```

---

## Best Practices

### 1. Use Standard Media Types

```java
// Good - Standard media types
MediaType.APPLICATION_JSON
MediaType.APPLICATION_XML
MediaType.parseMediaType("application/x-yaml")

// Bad - Custom media types without proper registration
MediaType.parseMediaType("application/myformat")
```

### 2. Default to JSON

```java
// Good - Default to JSON
@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok(userService.findAll());
}
```

### 3. Handle Unsupported Media Types

```java
// Good - Handle unsupported media types
@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public ResponseEntity<?> getUsers(@RequestHeader("Accept") String accept) {
    List<User> users = userService.findAll();
    
    if (accept.contains(MediaType.APPLICATION_XML_VALUE)) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .body(users);
    }
    
    if (accept.contains(MediaType.APPLICATION_JSON_VALUE)) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(users);
    }
    
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
        .body("Supported media types: application/json, application/xml");
}
```

### 4. Use Content Negotiation in Configuration

```java
// Good - Configure content negotiation
@Configuration
public class ContentNegotiationConfig implements WebMvcConfigurer {
    
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("yaml", MediaType.parseMediaType("application/x-yaml"));
    }
}
```

### 5. Document Supported Media Types

```java
// Good - Document supported media types
@Operation(
    summary = "Get all users",
    description = "Returns a list of users",
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class))),
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                mediaType = "application/xml",
                schema = @Schema(implementation = User.class)))
    })
@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok(userService.findAll());
}
```

---

## Common Pitfalls

### 1. Not Handling Accept Header

```java
// Bad - Not handling Accept header
@GetMapping
public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok(userService.findAll());
}

// Good - Handling Accept header
@GetMapping
public ResponseEntity<?> getUsers(@RequestHeader("Accept") String accept) {
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

### 2. Not Setting Content-Type

```java
// Bad - Not setting Content-Type
@GetMapping
public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok(userService.findAll());
}

// Good - Setting Content-Type
@GetMapping
public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(userService.findAll());
}
```

### 3. Not Handling 406 Not Acceptable

```java
// Bad - Not handling 406
@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok(userService.findAll());
}

// Good - Handling 406
@GetMapping
public ResponseEntity<?> getUsers(@RequestHeader("Accept") String accept) {
    List<User> users = userService.findAll();
    
    if (accept.contains(MediaType.APPLICATION_XML_VALUE)) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .body(users);
    }
    
    if (accept.contains(MediaType.APPLICATION_JSON_VALUE)) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(users);
    }
    
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
}
```

---

## Further Reading

- [HTTP Content Negotiation](https://developer.mozilla.org/en-US/docs/Web/HTTP/Content_negotiation)
- [Accept Header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept)
- [Content-Type Header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Type)
- [Spring MVC Content Negotiation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config/content-negotiation.html)
