# API Documentation

## Comprehensive Guide to Swagger UI and ReDoc

API documentation tools like Swagger UI and ReDoc make it easy to explore and test REST APIs. This guide covers setup, customization, and best practices.

---

## Table of Contents

1. [Documentation Overview](#documentation-overview)
2. [Swagger UI](#swagger-ui)
3. [ReDoc](#redoc)
4. [SpringDoc OpenAPI](#springdoc-openapi)
5. [Customization](#customization)
6. [Best Practices](#best-practices)

---

## Documentation Overview

### Why API Documentation Matters

```
API Specification (OpenAPI)
          |
          v
+-------------------+
| Documentation     |
| Tools             |
+-------------------+
          |
          +---> Interactive Testing
          +---> Client Generation
          +---> SDK Documentation
          +---> API Discovery
```

---

## Swagger UI

### Setup with Spring Boot

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### Configuration

```yaml
# application.yml
springdoc:
  api-docs:
    path: /api-docs
    enabled: true
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    tags-sorter: alpha
    operations-sorter: alpha
    filter: true
    try-it-out-enabled: true
  packages-to-scan: com.example.api
```

### Java Configuration

```java
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("User Service API")
                .description("API for managing user accounts")
                .version("1.0.0")
                .contact(new Contact()
                    .name("API Support")
                    .email("support@example.com"))
                .license(new License()
                    .name("MIT")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server().url("http://localhost:8080")
                    .description("Local development"),
                new Server().url("https://api.example.com")
                    .description("Production")))
            .tags(List.of(
                new Tag().name("Users")
                    .description("User management operations")))
            .components(new Components()
                .securitySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/api/**")
            .packagesToScan("com.example.api")
            .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
            .group("admin")
            .pathsToMatch("/admin/**")
            .packagesToScan("com.example.admin")
            .build();
    }
}
```

### Swagger UI Features

```
Swagger UI Features:
- Interactive API explorer
- Try-it-out functionality
- Request/response examples
- Schema visualization
- Authentication testing
- Filter and search
- Download OpenAPI spec
- Expand/collapse operations
```

### Custom Index Page

```html
<!-- src/main/resources/static/swagger-ui/index.html -->
<!DOCTYPE html>
<html>
<head>
    <title>API Documentation</title>
    <link rel="stylesheet" href="swagger-ui.css">
</head>
<body>
    <div id="swagger-ui"></div>
    <script src="swagger-ui-bundle.js"></script>
    <script>
        SwaggerUIBundle({
            url: '/api-docs',
            dom_id: '#swagger-ui',
            deepLinking: true,
            presets: [
                SwaggerUIBundle.presets.apis,
                SwaggerUIStandalonePreset
            ],
            layout: "BaseLayout",
            tryItOutEnabled: true,
            filter: true,
            tagsSorter: 'alpha',
            operationsSorter: 'alpha',
            defaultModelsExpandDepth: -1,
            defaultModelExpandDepth: 1
        });
    </script>
</body>
</html>
```

---

## ReDoc

### Setup with Spring Boot

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### Configuration

```yaml
# application.yml
springdoc:
  swagger-ui:
    enabled: false
  redoc:
    enabled: true
    path: /docs
```

### ReDoc Features

```
ReDoc Features:
- Clean, modern UI
- Responsive design
- Search functionality
- Three-panel layout
- Code samples
- Schema documentation
- Custom CSS support
- Print-friendly
```

### Custom ReDoc Page

```html
<!-- src/main/resources/static/docs/index.html -->
<!DOCTYPE html>
<html>
<head>
    <title>API Documentation</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:300,400,700|Roboto:300,400,700" rel="stylesheet">
    <style>
        body { margin: 0; padding: 0; }
    </style>
</head>
<body>
    <redoc spec-url='/api-docs'></redoc>
    <script src="https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js"></script>
</body>
</html>
```

---

## SpringDoc OpenAPI

### Annotations

```java
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management operations")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Operation(
        summary = "List users",
        description = "Returns a paginated list of users",
        tags = {"Users"}
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = User.class))
            )),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    public ResponseEntity<List<User>> listUsers(
        @Parameter(description = "Maximum items to return")
        @RequestParam(defaultValue = "20") Integer limit,

        @Parameter(description = "Items to skip")
        @RequestParam(defaultValue = "0") Integer offset
    ) {
        return ResponseEntity.ok(userService.findAll(limit, offset));
    }

    @Operation(
        summary = "Get user by ID",
        description = "Returns a single user"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(
        @Parameter(description = "User ID", required = true,
            example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable String userId
    ) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @Operation(
        summary = "Create user",
        description = "Creates a new user account"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping
    public ResponseEntity<User> createUser(
        @Parameter(description = "User to create", required = true)
        @Valid @RequestBody CreateUserRequest request
    ) {
        User user = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
```

### Schema Annotations

```java
@Schema(description = "User entity")
public class User {

    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000",
        accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "User name", requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 1, maxLength = 100, example = "John Doe")
    private String name;

    @Schema(description = "User email", format = "email",
        example = "john@example.com")
    private String email;

    @Schema(description = "User profile")
    private UserProfile profile;

    @Schema(description = "User roles", implementation = UserRole.class)
    private List<UserRole> roles;

    @Schema(description = "Account creation timestamp",
        accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}

@Schema(description = "User profile details")
public class UserProfile {

    @Schema(description = "Biography", maxLength = 500)
    private String bio;

    @Schema(description = "Age", minimum = "0", maximum = "150")
    private Integer age;

    @Schema(description = "Avatar URL", format = "uri")
    private String avatarUrl;
}
```

### Grouped APIs

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/api/**")
            .packagesToScan("com.example.api")
            .addOpenApiCustomizer(openApi -> openApi
                .info(new Info()
                    .title("Public API")
                    .version("1.0.0")))
            .build();
    }

    @Bean
    public GroupedOpenApi internalApi() {
        return GroupedOpenApi.builder()
            .group("internal")
            .pathsToMatch("/internal/**")
            .packagesToScan("com.example.internal")
            .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
            .group("admin")
            .pathsToMatch("/admin/**")
            .packagesToScan("com.example.admin")
            .build();
    }
}
```

---

## Customization

### Custom CSS

```css
/* swagger-ui/custom.css */
.swagger-ui .topbar {
    background-color: #1a1a2e;
}

.swagger-ui .info .title {
    color: #16213e;
}

.swagger-ui .scheme-container {
    background-color: #f8f9fa;
}
```

### Custom JavaScript

```javascript
// swagger-ui/custom.js
window.onload = function() {
    const ui = SwaggerUIBundle({
        url: '/api-docs',
        dom_id: '#swagger-ui',
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ],
        plugins: [
            // Custom plugin
            function(system) {
                return {
                    statePlugins: {
                        spec: {
                            wrapActions: {
                                // Add custom behavior
                            }
                        }
                    }
                };
            }
        ]
    });
};
```

### Environment Selector

```html
<select id="environment-selector">
    <option value="http://localhost:8080">Local</option>
    <option value="https://staging-api.example.com">Staging</option>
    <option value="https://api.example.com">Production</option>
</select>

<script>
document.getElementById('environment-selector').addEventListener('change', function(e) {
    const ui = SwaggerUIBundle({
        url: e.target.value + '/api-docs',
        dom_id: '#swagger-ui',
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ]
    });
});
</script>
```

---

## Best Practices

### 1. Document Everything

```java
@Operation(summary = "Create user",
    description = """
        Creates a new user account with the provided details.
        
        ### Requirements
        - Email must be unique
        - Password must be at least 8 characters
        - Name is required
        
        ### Rate Limits
        - 10 requests per minute per IP
        """)
```

### 2. Provide Examples

```java
@Schema(example = """
    {
        "name": "John Doe",
        "email": "john@example.com",
        "password": "securePassword123"
    }
    """)
public class CreateUserRequest { }
```

### 3. Use Tags for Organization

```java
@RestController
@Tag(name = "Users", description = "User management")
public class UserController { }

@RestController
@Tag(name = "Orders", description = "Order processing")
public class OrderController { }
```

### 4. Document Security

```java
@Operation(security = @SecurityRequirement(name = "bearerAuth"))
@GetMapping("/protected")
public ResponseEntity<String> protectedEndpoint() {
    return ResponseEntity.ok("Protected data");
}
```

### 5. Version Your API

```yaml
openapi: 3.0.3
info:
  version: 1.0.0

servers:
  - url: https://api.example.com/v1
    description: Version 1
```

---

## Further Reading

- [SpringDoc OpenAPI](https://springdoc.org/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [ReDoc](https://redocly.com/redoc)
- [OpenAPI Best Practices](https://swagger.io/docs/open-source/community-guides/)
