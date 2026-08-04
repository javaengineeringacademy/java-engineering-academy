# Layered Architecture

Layered architecture organizes code into horizontal layers, each with a specific responsibility. It's the most common architectural pattern.

## Table of Contents

1. [Concepts](#concepts)
2. [Classic Layers](#classic-layers)
3. [Implementation](#implementation)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Layered Architecture?

System is divided into layers, each depending only on the layer directly below it.

```
┌─────────────────────┐
│  Presentation       │  ← UI, Controllers
├─────────────────────┤
│  Business Logic     │  ← Services, Domain
├─────────────────────┤
│  Data Access        │  ← Repositories, DAOs
├─────────────────────┤
│  Database           │  ← Persistence
└─────────────────────┘
```

### Benefits

- **Separation of Concerns** - each layer has one job
- **Testability** - layers can be tested independently
- **Reusability** - business logic reusable across UIs
- **Maintainability** - changes isolated to layers

---

## Classic Layers

### Four-Layer Architecture

```java
// Presentation Layer
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}

// Business Logic Layer
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found"));
        return toDto(user);
    }
}

// Data Access Layer
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// Domain Layer
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
}
```

---

## Implementation

### Layer Dependencies

```java
// Presentation depends on Business Logic
public class ProductController {
    private final ProductService productService;
    // Only calls service methods
}

// Business Logic depends on Data Access
@Service
public class ProductService {
    private final ProductRepository productRepository;
    // Only calls repository methods
}

// Data Access depends on Domain
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Only uses domain entities
}
```

### Layer Communication

```java
// DTOs for layer boundaries
public record UserDto(Long id, String name, String email) {}

// Domain entities stay in domain layer
@Entity
public class User {
    private Long id;
    private String name;
    private String email;
}

// Service converts between layers
public UserDto getUser(Long id) {
    User user = userRepository.findById(id).orElseThrow();
    return new UserDto(user.getId(), user.getName(), user.getEmail());
}
```

---

## Best Practices

### Do

```java
// 1. Define clear layer boundaries
// Presentation → Business → Data Access → Database

// 2. Use DTOs between layers
public record OrderDto(String id, double total) {}

// 3. Dependency injection for layer communication
@Service
public class OrderService {
    private final OrderRepository repository;
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }
}
```

### Don't

```java
// 1. Don't skip layers
// Controller should not call repository directly

// 2. Don't leak domain entities to presentation
// Use DTOs

// 3. Don't add business logic to controllers
// Keep controllers thin
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Layers** | Horizontal separation of concerns |
| **Presentation** | UI and controllers |
| **Business Logic** | Services and domain rules |
| **Data Access** | Repositories and data mapping |
| **Database** | Persistence |
| **Dependencies** | Each layer depends on layer below |
| **DTOs** | Data transfer between layers |
| **Common** | Most widely used architecture |
