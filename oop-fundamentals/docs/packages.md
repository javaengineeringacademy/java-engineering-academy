# Packages in Java

## 1. Introduction

Packages are Java's mechanism for organizing classes into namespaces. They prevent naming conflicts, control access, and provide a logical grouping of related code. A package is essentially a directory structure that maps to the class file hierarchy.

Java 21 supports the module system (JPMS, introduced in Java 9) which adds a higher level of encapsulation on top of packages. This document covers package naming, structure, the module system, and access control.

## 2. Learning Objectives

- Understand how packages organize Java code
- Follow naming conventions for packages
- Design effective package structures for projects
- Use the Java Platform Module System (JPMS) with `module-info.java`
- Control access using packages and access modifiers
- Import classes effectively using single, wildcard, and static imports
- Avoid common package-related pitfalls

## 3. Prerequisites

- Basic Java syntax and class structure
- Understanding of access modifiers (public, private, protected)
- Familiarity with the Java compiler (`javac`) and package directories
- Basic knowledge of the file system and directory structure

## 4. Why This Concept Exists

Without packages, all classes would exist in a single global namespace. As projects grow, naming collisions become inevitable. Two developers might independently create a `User` class, causing conflicts when combining code.

Packages solve this by providing unique namespaces. `com.company.a.User` and `com.company.b.User` can coexist without conflict. Packages also enable logical grouping, making code easier to navigate and understand.

## 5. Problem Statement

Consider a project with multiple `Utils` classes:

```java
// Without packages: naming conflict!
class Utils { /* general utilities */ }
class Utils { /* string utilities */ } // Compile error: duplicate class
```

With packages, these can coexist:

```java
package com.company.general;
public class Utils { }

package com.company.string;
public class Utils { }
```

The package declaration must match the directory structure. A class declared as `com.company.project.UserService` must reside in `com/company/project/UserService.java`.

## 6. Theory

### Package Naming Conventions

| Convention | Example | Use Case |
|------------|---------|----------|
| All lowercase | `com.company.project` | Standard |
| Reverse domain name | `com.google.guava` | Prevents naming conflicts |
| Single word per segment | `java.util.concurrent` | Clarity |
| Company.project.module | `com.acme.ecommerce.orders` | Enterprise |

### Package Structure Patterns

**Layer-based structure:**
```
com.company.project/
├── model/          # Domain entities
├── service/        # Business logic
├── repository/     # Data access
├── controller/     # API endpoints
└── config/         # Configuration
```

**Feature-based structure:**
```
com.company.project/
├── user/
│   ├── model/
│   ├── service/
│   └── repository/
├── order/
│   ├── model/
│   ├── service/
│   └── repository/
└── payment/
    ├── model/
    ├── service/
    └── repository/
```

### Module System (JPMS)

The Java Platform Module System (JPMS) introduced in Java 9 provides a higher level of encapsulation than packages alone:

```
module com.company.project {
    exports com.company.api;              // Public API
    requires java.sql;                    // Depends on java.sql module
    requires transitive java.logging;     // Transitive dependency
    opens com.company.internal to reflection; // Allow reflection access
}
```

## 7. Internal Working

### How the JVM Resolves Packages

When the JVM encounters a class reference, it follows this resolution process:

1. **Class Loader Delegation**: The class loader first delegates to its parent loader.
2. **Package Mapping**: Package names map directly to directory paths.
3. **Class Loading**: The class loader reads the `.class` file from the matching directory.

```
com.company.project.UserService
    │
    ▼
com/company/project/UserService.class
    │
    ▼
Loaded by Application ClassLoader
```

### Package Access at Runtime

The JVM uses the package name internally to determine access permissions:

```java
package com.company.internal;

class InternalHelper { // Package-private
    // Accessible only within com.company.internal
}

public class PublicService {
    InternalHelper helper = new InternalHelper(); // OK: same package
}
```

### Module Resolution

When modules are involved, the JVM uses the module system to:
1. Resolve module dependencies (`requires`)
2. Verify exported packages (`exports`)
3. Enforce encapsulation boundaries

## 8. JVM Perspective

### Class Loading and Packages

The JVM uses three class loaders in the delegation model:

```
Bootstrap ClassLoader
├── Loads JDK core classes (java.lang, java.util, etc.)
└── Loaded from $JAVA_HOME/lib

Platform ClassLoader (Java 9+)
├── Loads Java EE modules (java.sql, java.xml, etc.)
└── Loaded from $JAVA_HOME/lib/modules

Application ClassLoader
├── Loads application classes from classpath
└── Loaded from -cp or classpath directories
```

### Package Naming in the JVM

The JVM uses the internal form of class names where dots become slashes:
- Source: `com.company.project.UserService`
- Internal: `com/company/project/UserService`
- File: `com/company/project/UserService.class`

### Module System Internals

The JVM stores module information in the `Modules` data structure of each class loader. Module boundaries are enforced at runtime through access checks:

```java
// module-info.java
module com.company.api {
    exports com.company.api.model;  // Public to all modules
    // com.company.api.internal NOT exported - encapsulated
}
```

Modules not explicitly exported are inaccessible from other modules, even via reflection (unless `opens` is specified).

## 9. Memory Representation

### Package Overhead

Packages have zero runtime memory overhead. The package declaration is purely a compile-time construct. Once compiled, only class names (including package) exist in the constant pool of `.class` files.

```
.class File Constant Pool:
├── Constant_Class_info (class name)
│   └── "com/company/project/UserService" (UTF-8 string)
├── Constant_Package_info (package, if module system used)
```

### Module Data in Memory

When modules are used, the JVM stores module metadata:

| Component | Storage | Size |
|-----------|---------|------|
| Module name | Module data structure | ~50 bytes |
| Package exports | Module data structure | ~20 bytes per export |
| Module requires | Module data structure | ~30 bytes per require |
| Module resolution | Class loader data | Dynamic |

### String Interning for Package Names

Package name strings are interned by the JVM, meaning identical package name strings share the same memory address. This reduces memory usage in applications with many classes.

## 10. Architecture Diagram

### Package and Module Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                      Java Application                            │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │                    Module: com.company.app                  │ │
│  │                                                             │ │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │ │
│  │  │ com.company  │  │ com.company  │  │ com.company  │    │ │
│  │  │ .api         │  │ .service     │  │ .repository  │    │ │
│  │  │ (exported)   │  │ (exported)   │  │ (not exported)│   │ │
│  │  │              │  │              │  │              │    │ │
│  │  │ - UserAPI    │  │ - UserService│  │ - UserRepo   │    │ │
│  │  │ - OrderAPI   │  │ - OrderSvc   │  │ - OrderRepo  │    │ │
│  │  └──────────────┘  └──────────────┘  └──────────────┘    │ │
│  └────────────────────────────────────────────────────────────┘ │
│                              │                                    │
│                              ▼                                    │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │                Module: com.company.infra                    │ │
│  │                                                             │ │
│  │  ┌──────────────┐  ┌──────────────┐                       │ │
│  │  │ com.company  │  │ com.company  │                       │ │
│  │  │ .db          │  │ .config      │                       │ │
│  │  │              │  │              │                       │ │
│  │  │ - DatabaseMgr│  │ - AppConfig  │                       │ │
│  │  └──────────────┘  └──────────────┘                       │ │
│  └────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### Access Control Flow

```
Class in com.company.api wants to access com.company.repository.UserRepo
    │
    ▼
Is com.company.repository exported?
├── Yes → Is UserRepo public?
│         ├── Yes → Access granted
│         └── No → Access denied (package-private)
└── No → Access denied (module boundary)
```

## 11. Flow Diagram

### Package Declaration Flow

```
Writing a new class
    │
    ▼
Determine the package
├── Follow reverse domain convention
├── Group by feature or layer
└── Check for existing package structure
    │
    ▼
Declare package (first line of file)
│   package com.company.project.module;
    │
    ▼
Ensure directory structure matches
│   com/company/project/module/MyClass.java
    │
    ▼
Import dependencies
├── Specific import: import com.company.other.Class;
├── Wildcard import: import com.company.other.*; (avoid)
└── Static import: import static java.lang.Math.PI;
    │
    ▼
Compile with package-aware javac
│   javac -d out com/company/project/module/MyClass.java
    │
    ▼
Run with package-qualified name
│   java com.company.project.module.MyClass
```

### Module Resolution Flow

```
Application starts
    │
    ▼
JVM reads module-info.class files
    │
    ▼
Resolve module graph
├── Read requires dependencies
├── Verify all modules accessible
└── Check for split packages (error)
    │
    ▼
Initialize modules in order
├── Platform modules first
├── Library modules second
└── Application modules last
    │
    ▼
Application classes loaded
    │
    ▼
Access checks enforced at runtime
```

## 12. Syntax

### Package Declaration

```java
// Must be first non-comment line in file
package com.company.project.module;

public class MyClass {
    // Class body
}
```

### Import Statements

```java
// Single-type import (preferred)
import com.company.project.model.User;
import com.company.project.service.UserService;

// Type-import-on-demand (avoid in production)
import com.company.project.model.*;

// Static import
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
import static java.util.Objects.requireNonNull;

// Static import on-demand (avoid)
import static java.util.stream.Collectors.*;
```

### Module Declaration (Java 9+)

```java
// module-info.java (placed in root source directory)
module com.company.project {
    // Export public API
    exports com.company.api;
    exports com.company.api.model;

    // Declare dependencies
    requires java.sql;
    requires java.logging;
    requires transitive java.desktop;  // Transitive: dependents also get this

    // Open for reflection (frameworks)
    opens com.company.model to hibernate.orm, jackson.databind;

    // Provide service implementations
    provides com.company.spi.NotificationService
        with com.company.internal.EmailNotificationService;
}
```

### Package-Private Access

```java
package com.company.internal;

// Package-private class (no public modifier)
class InternalHelper {
    // Package-private field
    static final String CONFIG = "default";

    // Package-private method
    void process() { }
}

public class PublicClass {
    // Can access InternalHelper because same package
    private InternalHelper helper = new InternalHelper();
}
```

## 13. Easy Example

### Basic Package Usage

```java
// File: src/main/java/com/example/model/User.java
package com.example.model;

import java.util.Objects;

public record User(String name, String email) {
    public User {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(email, "email must not be null");
    }
}
```

```java
// File: src/main/java/com/example/service/UserService.java
package com.example.service;

import com.example.model.User;
import java.util.Optional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public void register(User user) {
        users.put(user.email(), user);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }
}
```

```java
// File: src/main/java/com/example/App.java
package com.example;

import com.example.model.User;
import com.example.service.UserService;

public class App {
    public static void main(String[] args) {
        UserService service = new UserService();
        service.register(new User("Alice", "alice@example.com"));
        service.findByEmail("alice@example.com")
            .ifPresent(user -> System.out.println("Found: " + user));
    }
}
```

**Directory Structure:**
```
src/main/java/
└── com/
    └── example/
        ├── App.java
        ├── model/
        │   └── User.java
        └── service/
            └── UserService.java
```

## 14. Medium Example

### Package-Private Design Pattern

```java
// File: src/main/java/com/example/cache/CacheBuilder.java
package com.example.cache;

import java.time.Duration;

// Public API: users create caches through this builder
public class CacheBuilder<K, V> {
    private int maxSize = 1000;
    private Duration ttl = Duration.ofMinutes(5);

    public CacheBuilder<K, V> maxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public CacheBuilder<K, V> ttl(Duration ttl) {
        this.ttl = ttl;
        return this;
    }

    public Cache<K, V> build() {
        return new InMemoryCache<>(maxSize, ttl);
    }
}
```

```java
// File: src/main/java/com/example/cache/Cache.java
package com.example.cache;

import java.util.Optional;

// Package-private interface: only accessible within com.example.cache
interface Cache<K, V> {
    void put(K key, V value);
    Optional<V> get(K key);
    void invalidate(K key);
    int size();
}
```

```java
// File: src/main/java/com/example/cache/InMemoryCache.java
package com.example.cache;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

// Package-private implementation
class InMemoryCache<K, V> implements Cache<K, V> {
    private final int maxSize;
    private final Duration ttl;
    private final Map<K, CacheEntry<V>> store;

    InMemoryCache(int maxSize, Duration ttl) {
        this.maxSize = maxSize;
        this.ttl = ttl;
        this.store = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > InMemoryCache.this.maxSize;
            }
        };
    }

    @Override
    public void put(K key, V value) {
        store.put(key, new CacheEntry<>(value, Instant.now()));
    }

    @Override
    public Optional<V> get(K key) {
        CacheEntry<V> entry = store.get(key);
        if (entry == null || entry.isExpired(ttl)) {
            store.remove(key);
            return Optional.empty();
        }
        return Optional.of(entry.value());
    }

    @Override
    public void invalidate(K key) {
        store.remove(key);
    }

    @Override
    public int size() {
        return store.size();
    }

    private record CacheEntry<V>(V value, Instant createdAt) {
        boolean isExpired(Duration ttl) {
            return Instant.now().isAfter(createdAt.plus(ttl));
        }
    }
}
```

**Key points:**
- `Cache` and `InMemoryCache` are package-private
- Only `CacheBuilder` is public
- Users cannot depend on internal implementation classes
- Internal classes can be refactored without breaking external code

## 15. Hard Example

### Multi-Module Project with Module System

```
project/
├── api-module/
│   ├── src/main/java/
│   │   ├── module-info.java
│   │   └── com/company/api/
│   │       ├── model/
│   │       │   ├── User.java
│   │       │   └── Order.java
│   │       └── service/
│   │           ├── UserService.java
│   │           └── OrderService.java
│   └── pom.xml
│
├── core-module/
│   ├── src/main/java/
│   │   ├── module-info.java
│   │   └── com/company/core/
│   │       ├── repository/
│   │       │   ├── UserRepository.java (interface)
│   │       │   └── OrderRepository.java (interface)
│   │       └── event/
│   │           ├── DomainEvent.java
│   │           └── EventPublisher.java
│   └── pom.xml
│
└── infra-module/
    ├── src/main/java/
    │   ├── module-info.java
    │   └── com/company/infra/
    │       ├── persistence/
    │       │   ├── JpaUserRepository.java
    │       │   └── JpaOrderRepository.java
    │       └── messaging/
    │           └── KafkaEventPublisher.java
    └── pom.xml
```

**api-module/module-info.java:**
```java
module com.company.api {
    exports com.company.api.model;
    exports com.company.api.service;

    requires transitive com.company.core;
}
```

**core-module/module-info.java:**
```java
module com.company.core {
    exports com.company.core.repository;
    exports com.company.core.event;

    requires java.persistence;
}
```

**infra-module/module-info.java:**
```java
module com.company.infra {
    provides com.company.core.repository.UserRepository
        with com.company.infra.persistence.JpaUserRepository;
    provides com.company.core.repository.OrderRepository
        with com.company.infra.persistence.JpaOrderRepository;
    provides com.company.core.event.EventPublisher
        with com.company.infra.messaging.KafkaEventPublisher;

    requires com.company.core;
    requires java.persistence;
    requires kafka.clients;
}
```

## 16. Enterprise Example

### Enterprise Package Structure

```
com.company.ecommerce/
├── module-info.java
│
├── api/                          # Public API layer
│   ├── rest/
│   │   ├── UserController.java
│   │   ├── OrderController.java
│   │   └── dto/
│   │       ├── CreateUserRequest.java (record)
│   │       ├── OrderResponse.java (record)
│   │       └── ErrorResponse.java (record)
│   └── graph/
│       ├── UserQuery.java
│       └── OrderMutation.java
│
├── domain/                       # Core domain layer
│   ├── model/
│   │   ├── User.java
│   │   ├── Order.java
│   │   └── Money.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── OrderService.java
│   │   └── PricingService.java
│   ├── repository/
│   │   ├── UserRepository.java (interface)
│   │   └── OrderRepository.java (interface)
│   └── event/
│       ├── DomainEvent.java (sealed interface)
│       ├── OrderPlacedEvent.java (record)
│       └── UserRegisteredEvent.java (record)
│
├── infrastructure/               # External concerns
│   ├── persistence/
│   │   ├── JpaUserRepository.java
│   │   ├── JpaOrderRepository.java
│   │   └── entity/
│   │       ├── UserEntity.java
│   │       └── OrderEntity.java
│   ├── messaging/
│   │   ├── KafkaEventPublisher.java
│   │   └── KafkaEventListener.java
│   └── config/
│       ├── DataSourceConfig.java
│       └── SecurityConfig.java
│
└── shared/                       # Shared utilities
    ├── exception/
    │   ├── DomainException.java
    │   ├── NotFoundException.java
    │   └── ValidationException.java
    └── util/
        ├── Preconditions.java
        └── Result.java
```

**module-info.java:**
```java
module com.company.ecommerce {
    // Public API
    exports com.company.api.rest.dto;
    exports com.company.domain.model;
    exports com.company.domain.service;
    exports com.company.domain.repository;
    exports com.company.domain.event;
    exports com.company.shared.exception;

    // Internal packages (not exported)
    // com.company.api.rest - Controllers (wired by framework)
    // com.company.infrastructure - Implementations

    // Dependencies
    requires java.net.http;
    requires spring.web;
    requires spring.context;
    requires spring.data.jpa;
    requires kafka.clients;
    requires jakarta.persistence;

    // Opens for JPA/Hibernate reflection
    opens com.company.infrastructure.persistence.entity to hibernate.orm;

    // Service provider interface
    provides com.company.domain.repository.UserRepository
        with com.company.infrastructure.persistence.JpaUserRepository;
}
```

## 17. Performance

### Package Performance Characteristics

| Aspect | Performance Impact | Notes |
|--------|-------------------|-------|
| Package declaration | Zero runtime cost | Compile-time only |
| Import statements | Zero runtime cost | Resolved at compile time |
| Module system | Minimal overhead | One-time module resolution |
| Package-private access | Same as public | JVM inlines access checks |
| Wildcard imports | No runtime difference | Only affects compilation speed |

### Module System Overhead

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| Module resolution | O(modules × packages) | One-time at startup |
| Access check | O(1) | Cached per class loader |
| Service lookup | O(providers) | One-time per service |
| Reflection access check | O(1) | Cached after first check |

### Compilation Performance

Wildcard imports can slow compilation because the compiler must search for classes:

```java
// SLOW: compiler searches entire package
import com.company.project.model.*;

// FAST: compiler knows exact class
import com.company.project.model.User;
```

## 18. Time Complexity

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| Package declaration | O(1) | Single statement |
| Single import resolution | O(1) | Direct lookup |
| Wildcard import resolution | O(n) | Search all classes in package |
| Module resolution | O(M × P) | M = modules, P = packages per module |
| Class loading | O(1) amortized | Cached after first load |
| Package-private access check | O(1) | Constant-time comparison |

## 19. Space Complexity

| Component | Space Overhead | Notes |
|-----------|---------------|-------|
| Package declaration | 0 bytes (runtime) | Removed after compilation |
| Import statements | 0 bytes (runtime) | Removed after compilation |
| Module-info.class | ~200-500 bytes | Per module |
| Package name in class file | ~20-100 bytes | In constant pool |
| Module exports table | ~10-30 bytes per export | In module data |

## 20. Thread Safety

### Thread Safety of Package-Private Access

Package-private access is inherently thread-safe because:
1. The access check is read-only (no shared state modified)
2. The JVM caches the result of access checks
3. Package-private methods have the same synchronization semantics as public methods

```java
package com.company.shared;

// Thread-safe: package-private class with synchronized methods
class SharedCache {
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    synchronized void put(String key, Object value) {
        cache.put(key, value);
    }

    synchronized Object get(String key) {
        return cache.get(key);
    }
}
```

### Module System Thread Safety

Module resolution is thread-safe. The JVM uses a lock-free algorithm for module lookup after initial resolution. The module graph is immutable after startup.

### Class Loading Thread Safety

Class loading is thread-safe. The JVM ensures that a class is loaded exactly once, even when multiple threads attempt to load it simultaneously. This is guaranteed by the class loader's internal locking mechanism.

## 21. Best Practices

### Package Naming

1. **All lowercase**: `com.company.project`, not `com.Company.Project`
2. **Reverse domain name**: `com.google.common`, not `com.google`
3. **Single word per segment**: `java.util.concurrent`, not `java.utilConcurrent`
4. **Avoid sun/com prefix**: Don't use `sun.*` or `com.sun.*` (reserved)
5. **Company.project.module**: `com.acme.ecommerce.orders`

### Package Structure

1. **One public class per file**: File name matches public class name
2. **Group by feature or layer**: Choose one pattern and be consistent
3. **Max 3-4 nesting levels**: Deep hierarchies reduce readability
4. **Keep related classes close**: A class should reference only same or one-level-up packages
5. **Avoid circular dependencies**: If A depends on B, B should not depend on A

### Import Guidelines

1. **Prefer specific imports**: `import com.company.User` over `import com.company.*`
2. **Group imports**: java.* first, then javax.*, then third-party, then project
3. **Static imports sparingly**: Use for constants (`PI`, `MAX_VALUE`) and utility methods (`sqrt`)
4. **No wildcard imports in production**: They obscure dependencies and cause conflicts

### Module System

1. **Export only public API**: Keep internal packages unexported
2. **Use requires transitive**: When dependents need access to your dependencies
3. **Opens for reflection**: Only when frameworks require it (Hibernate, Jackson)
4. **Provides with**: For service provider interface pattern

## 22. Common Mistakes

### Mistake 1: Package Name Doesn't Match Directory

```java
// File: src/UserService.java
package com.company.project; // WRONG: directory doesn't match
public class UserService { }
```

```bash
# This will fail to compile
javac src/UserService.java
# Error: file does not contain class UserService in package com.company.project
```

**Fix**: Ensure directory structure matches package declaration exactly.

### Mistake 2: Wildcard Import Conflict

```java
import java.util.Date;
import java.sql.Date; // Compile error: ambiguous

public class MyClass {
    Date date; // Which Date? java.util or java.sql?
}
```

**Fix**: Use specific imports and avoid wildcard imports.

### Mistake 3: Cyclic Package Dependencies

```java
// package A depends on B
package com.company.a;
import com.company.b.ServiceB;

// package B depends on A (cycle!)
package com.company.b;
import com.company.a.ServiceA;
```

**Fix**: Extract shared code to a third package, or use dependency inversion.

### Mistake 4: Split Package in Module System

```
module-info.java
module com.company.app {
    exports com.company.shared;
}

// Two different modules both have com.company.shared
// This causes a "split package" error
```

**Fix**: Ensure each package belongs to exactly one module.

### Mistake 5: Over-Exporting

```java
module com.company.app {
    exports com.company.internal; // BAD: exposing internal API
}
```

**Fix**: Only export packages that form the public API.

## 23. Pitfalls

| Pitfall | Description | Impact | Solution |
|---------|-------------|--------|----------|
| **Default access too open** | Package-private accessible to all same-package classes | Unintended coupling | Use modules or separate packages |
| **Split packages** | Same package in multiple modules | Module system error | One package per module |
| **Deep nesting** | `com.company.project.module.submodule.feature` | Hard to read | Keep to 3-4 levels |
| **Wildcard imports** | `import java.util.*` | Name conflicts, obscured deps | Use specific imports |
| **Missing module-info** | No module declaration | Classpath mode (no encapsulation) | Add module-info.java |
| **Cyclic dependencies** | Package A depends on B, B depends on A | Compilation issues, tight coupling | Refactor with DI |
| **Package renaming** | Renaming breaks all imports | Large-scale changes | Use IDE refactoring |

## 24. Debugging Tips

### Common Package Errors and Solutions

```bash
# Error: class not found in package
javac -d out src/com/company/MyClass.java
# Solution: Verify directory structure matches package declaration

# Error: package does not exist
java com.company.MyClass
# Solution: Ensure classpath includes the root directory

# Error: split package detected (Java 9+)
# Solution: Ensure each package belongs to exactly one module
```

### Debugging Module Issues

```bash
# List all modules in the module path
java --list-modules

# Show module descriptor
java --describe-module com.company.app

# Show module content
jar --describe-module --file=app.jar

# Debug module resolution
java -Xdiag:module com.company.app.Main

# Show module graph
java --module-path mods -Xdiag:module -version
```

### IDE Package Exploration

In IntelliJ IDEA:
1. Use "Project" view (not "Packages" view) to see actual file structure
2. Use "Navigate → Package" to quickly jump to packages
3. Use "Find Usages" to detect package coupling
4. Use "Analyze → Module Dependencies" to visualize dependencies

In Eclipse:
1. Use "Package Explorer" view
2. Right-click package → "Open in New Window"
3. Use "Open Resource" (Ctrl+Shift+R) to find classes by name

## 25. Comparison Table

### Package Organization Patterns

| Pattern | Structure | Best For | Drawbacks |
|---------|-----------|----------|-----------|
| **Layer-based** | `model/`, `service/`, `controller/` | Small-medium projects | Cross-cutting concerns |
| **Feature-based** | `user/`, `order/`, `payment/` | Large projects | Shared code duplication |
| **Hexagonal** | `domain/`, `adapters/`, `ports/` | Clean architecture | Steeper learning curve |
| **Domain-driven** | `bounded-context/` | Microservices | More complex structure |

### Import Types

| Type | Syntax | When to Use | Example |
|------|--------|-------------|---------|
| **Single** | `import com.x.Y;` | Most classes | `import java.util.List;` |
| **Wildcard** | `import com.x.*;` | Avoid in production | `import java.util.*;` |
| **Static** | `import static com.x.Y.z;` | Constants, utility methods | `import static java.lang.Math.PI;` |
| **Static wildcard** | `import static com.x.*;` | Rarely | `import static java.util.stream.Collectors.*;` |

### Module System vs Packages

| Feature | Packages | Modules |
|---------|----------|---------|
| **Encapsulation** | Access modifiers only | Module boundaries |
| **Dependency** | Implicit (classpath) | Explicit (requires) |
| **Versioning** | Not supported | Not supported (use build tools) |
| **Runtime** | Zero cost | Minimal cost (one-time resolution) |
| **Maturity** | Since Java 1.0 | Since Java 9 |

## 26. Decision Tree

### How to Organize Packages?

```
Project size?
├── Small (< 20 classes)
│   └── Simple layer-based: model/, service/, util/
├── Medium (20-100 classes)
│   └── Feature-based or layer-based
└── Large (100+ classes)
    ├── Feature-based with shared module
    └── Or DDD bounded contexts
```

### Should I Use the Module System?

```
Application type?
├── Library/JAR
│   ├── Want to encapsulate internals? → Yes → Use modules
│   └── Need maximum compatibility? → Yes → Skip modules
├── Application
│   ├── Java 9+ required? → Yes → Use modules
│   └── Using Spring Boot? → Modules optional (Spring supports both)
└── Microservice
    └── Modules rarely needed (each service is small)
```

### Import Style Decision?

```
Class frequency of use?
├── Used in every method → Static import (e.g., PI, requireNonNull)
├── Used in most files → Specific import
├── Used once → Specific import
└── Testing with many classes → Wildcard acceptable (test code only)
```

## 27. Interview Questions

### Basic

1. **What is a package in Java?**
   A package is a namespace that organizes related classes and interfaces. It prevents naming conflicts, controls access, and provides a logical grouping. Packages map directly to directory structures.

2. **What is the difference between `import` and `package` declarations?**
   `package` declares which package the current class belongs to. `import` brings classes from other packages into scope so they can be referenced without full qualification.

3. **Can a Java file have multiple public classes?**
   No. A Java file can have at most one public class, and the file name must match the public class name.

### Intermediate

4. **What is package-private access?**
   Package-private (default) access allows a class or member to be accessed only within the same package. It provides a middle ground between private and protected access.

5. **What is the Java Module System (JPMS)?**
   JPMS (Java Platform Module System, Java 9+) provides a higher level of encapsulation than packages alone. Modules can export specific packages and declare dependencies, enforcing boundaries at runtime.

6. **What is a split package and why is it forbidden?**
   A split package is when the same package name exists in multiple modules. It is forbidden because it breaks module encapsulation and makes access control ambiguous.

### Advanced

7. **How does the module system affect classpath-based code?**
   Without `module-info.java`, code runs on the classpath (unnamed module). This provides backward compatibility but no module-level encapsulation. Libraries without module-info can still be used by modular code.

8. **What is `requires transitive` and when should you use it?**
   `requires transitive` makes a dependency available to modules that depend on the current module. Use it when your public API exposes types from the transitive dependency.

9. **What is the `opens` directive in module-info?**
   `opens` allows deep reflection (via `setAccessible`) on a package, typically for frameworks like Hibernate and Jackson that need to access private fields at runtime.

10. **How do you handle package naming in a multi-organization project?**
    Use a common root package (e.g., `org.`) with organization-specific sub-packages. For open-source, use reverse domain (e.g., `com.github.username`). Avoid prefixes reserved by Oracle (`com.sun.*`, `java.*`).

## 28. Exercises

### Exercise 1: Package Creation (Beginner)

Create a project with the following package structure:
```
com.example.calculator/
├── model/      # Calculator, Operation
├── service/    # CalculatorService
└── util/       # MathUtils
```

Write classes in each package and demonstrate inter-package imports.

### Exercise 2: Package-Private Design (Intermediate)

Design a cache library where:
- The public API is `CacheBuilder` (public class)
- `Cache` interface is package-private
- Multiple implementations are package-private
- Users cannot directly instantiate implementations

### Exercise 3: Module System (Intermediate)

Create a two-module project:
- `com.example.api` (exports `com.example.api.model`)
- `com.example.impl` (provides implementation of `com.example.api.service`)

Demonstrate that the implementation package is not accessible from outside.

### Exercise 4: Access Modifier Analysis (Advanced)

Analyze the following code and determine which accesses are legal:

```java
package com.company.a;

public class ClassA {
    public int publicField = 1;
    protected int protectedField = 2;
    int packageField = 3;
    private int privateField = 4;
    
    public void publicMethod() {}
    protected void protectedMethod() {}
    void packageMethod() {}
    private void privateMethod() {}
}
```

```java
package com.company.a;

public class ClassB {
    // Which fields/methods of ClassA can be accessed here?
}
```

```java
package com.company.b;

import com.company.a.ClassA;

public class ClassC extends ClassA {
    // Which fields/methods of ClassA can be accessed here?
}
```

## 29. Assignments

### Assignment 1: Package Restructuring

Given a flat package structure with 20+ classes, reorganize it into a feature-based structure. Document the before/after structure and explain your decisions.

### Assignment 2: Module Migration

Take an existing classpath-based project and convert it to use JPMS modules:
1. Create `module-info.java` for each module
2. Define exports and requires
3. Handle service provider interfaces
4. Ensure all tests still pass

### Assignment 3: Library Design

Design a reusable utility library as a module:
- Export only the public API
- Keep internal classes unexported
- Provide a service interface for extension
- Write comprehensive documentation

### Assignment 4: Access Control Audit

Audit an existing codebase for access modifier usage:
1. Identify classes with incorrect access levels
2. Find fields that should be private but are not
3. Detect circular package dependencies
4. Propose improvements

## 30. Mini Project: Package Organizer CLI Tool

### Project Description

Build a command-line tool that analyzes a Java project and provides package organization insights.

### Requirements

1. **Scan Source Files**: Parse `.java` files to extract package declarations and imports
2. **Dependency Graph**: Build a graph of package dependencies
3. **Circular Detection**: Find circular dependencies between packages
4. **Complexity Metrics**: Calculate package coupling and cohesion metrics
5. **Report Generation**: Output a formatted report with recommendations

### Implementation Structure

```
package-analyzer/
├── src/main/java/
│   └── com/example/analyzer/
│       ├── model/
│       │   ├── JavaFile.java (record)
│       │   ├── PackageInfo.java (record)
│       │   └── DependencyGraph.java
│       ├── scanner/
│       │   ├── SourceScanner.java
│       │   └── ImportParser.java
│       ├── analysis/
│       │   ├── DependencyAnalyzer.java
│       │   ├── CycleDetector.java
│       │   └── MetricsCalculator.java
│       └── report/
│           ├── ConsoleReporter.java
│           └── HtmlReporter.java
└── pom.xml
```

### Evaluation Criteria

- Correct parsing of Java source files
- Accurate dependency graph construction
- Effective cycle detection algorithm
- Clear and actionable report output
- Handling of edge cases (wildcard imports, static imports)

## 31. Summary

| Concept | Key Takeaway |
|---------|--------------|
| **Package** | Namespace for organizing classes; maps to directory structure |
| **Package naming** | All lowercase, reverse domain, single word per segment |
| **Package-private** | Default access; accessible within same package only |
| **Import** | Specific (preferred), wildcard (avoid), static (sparingly) |
| **Module System** | Higher-level encapsulation; exports/requires/opens directives |
| **Module resolution** | One-time at startup; thread-safe; minimal overhead |
| **Best practices** | Max 4 nesting levels, avoid cycles, export only public API |

### Quick Reference

```java
// Package declaration
package com.company.project;

// Imports
import com.company.project.model.User;        // Specific
import com.company.project.model.*;            // Wildcard (avoid)
import static java.lang.Math.PI;              // Static (sparingly)

// module-info.java
module com.company.project {
    exports com.company.api;
    requires java.sql;
    requires transitive java.logging;
    opens com.company.model to hibernate.orm;
    provides com.company.spi.Service
        with com.company.impl.ServiceImpl;
}
```

## 32. References

- [JLS §7 — Packages](https://docs.oracle.com/javase/specs/jls/se21/html/jls-7.html)
- [JLS §7.4 — Package Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-7.html#jls-7.4)
- [JLS §7.5 — Import Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-7.html#jls-7.5)
- [Java Platform Module System](https://docs.oracle.com/javase/9/modules/)
- [JEP 261: Module System](https://openjdk.org/jeps/261)
- [Google Java Style Guide — Package Statements](https://google.github.io/styleguide/javaguide.html#s3-package-statements)
- [Effective Java, 3rd Edition — Item 20: Prefer interfaces to abstract classes](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Java 21 Documentation](https://openjdk.org/projects/jdk/21/)
- [JPMS Tutorial](https://www.oracle.com/java/technologies/javase-intro-jpms.html)
- [Module System Example Projects](https://github.com/forax/modules-training)
