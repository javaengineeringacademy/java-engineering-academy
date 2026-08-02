# Module 64: OpenAPI / Swagger

## Overview
OpenAPI Specification (formerly Swagger) defines REST API standards. It enables automatic documentation, client generation, and server stubs for API-first development.

## Learning Objectives
- Understand OpenAPI specification
- Document REST APIs
- Generate client code
- Validate API contracts
- Apply API-first design

## Prerequisites
- REST API basics
- JSON/YAML knowledge
- Spring Boot experience

## Why This Concept Exists
REST APIs need:
- Clear documentation
- Client generation
- Contract validation
- Testing support

OpenAPI provides:
- Standardized documentation
- Code generation
- Contract testing
- API design tools

## Problem Statement
How do you document and standardize REST APIs effectively?

## Theory

### OpenAPI Structure

| Component | Description |
|-----------|-------------|
| Info | API metadata |
| Servers | API endpoints |
| Paths | API operations |
| Components | Reusable schemas |
| Security | Authentication |

### API Documentation

| Section | Purpose |
|---------|---------|
| Summary | Brief description |
| Description | Detailed explanation |
| Parameters | Input parameters |
| Responses | Possible responses |
| Examples | Sample data |

## Enterprise Example

```java
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.parameters.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "CRUD operations for users")
public class UserApiController {
    
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }
    
    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }
    
    @Operation(summary = "Create user")
    @PostMapping
    public UserDTO createUser(@RequestBody @Schema(description = "User to create") CreateUserRequest request) {
        return userService.create(request);
    }
    
    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }
    
    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

// Schema definitions
@Schema(description = "User response object")
public record UserDTO(
    @Schema(description = "User ID", example = "1") Long id,
    @Schema(description = "User name", example = "John Doe") String name,
    @Schema(description = "Email address", example = "john@example.com") String email,
    @Schema(description = "Account status", example = "ACTIVE") UserStatus status
) {}

@Schema(description = "Create user request")
public record CreateUserRequest(
    @Schema(description = "User name", required = true, example = "John Doe") String name,
    @Schema(description = "Email address", required = true, example = "john@example.com") String email,
    @Schema(description = "Password", required = true) String password
) {}
```

## Performance Considerations
- Cache API documentation
- Generate clients at build time
- Use contract testing
- Validate requests/responses

## Best Practices
1. Use API-first design
2. Version your API
3. Provide examples
4. Document errors
5. Use contracts

## Interview Questions

### Q1: What is OpenAPI?
**Answer:** Standard specification for REST API documentation.

### Q2: What is Swagger?
**Answer:** Tools for implementing OpenAPI specification.

### Q3: What is API-first design?
**Answer:** Designing API before implementation.

### Q4: What is contract testing?
**Answer:** Testing that API meets its specification.

### Q5: What is code generation?
**Answer:** Automatically creating client/server code from OpenAPI spec.

## Summary
OpenAPI/Swagger provides standardized API documentation and enables code generation.

## References
- OpenAPI Specification
- SpringDoc Documentation
- Swagger UI
