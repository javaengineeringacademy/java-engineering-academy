# Packages

## Objective
Organize classes into namespaces for better code organization and access control.

## Package Declaration
```java
package com.company.project.module;

public class MyClass { }
```

## Naming Conventions
| Convention | Example |
|------------|---------|
| All lowercase | `com.company.project.module` |
| Reverse domain | `com.google.guava` |
| Single word per segment | `java.util.concurrent` |

## Package Structure
```
src/
└── main/
    └── java/
        └── com/
            └── company/
                └── project/
                    ├── model/
                    │   ├── User.java
                    │   └── Order.java
                    ├── service/
                    │   └── UserService.java
                    └── repository/
                        └── UserRepository.java
```

## Access Modifiers with Packages
| Modifier | Same Class | Same Package | Subclass | World |
|----------|------------|--------------|----------|-------|
| `private` | ✓ | ✗ | ✗ | ✗ |
| (package) | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

## Import Statements
```java
// Single import
import com.company.model.User;

// Wildcard (avoid in production)
import com.company.model.*;

// Static import
import static java.lang.Math.*;
import static com.company.utils.Constants.*;
```

## Package-Private (Default) Access
```java
// Package-private class
class InternalHelper { }

// Package-private field/method
class Service {
    String config = "default";  // package-private
    void internalMethod() { }
}
```

## Module System (Java 9+)
```java
// module-info.java
module com.company.project {
    exports com.company.api;
    requires java.sql;
    requires transitive java.logging;
}
```

## Best Practices

1. **One public class per file** — File name must match the public class name.
2. **Logical grouping by feature or layer** — Group related classes into cohesive packages.
3. **Max 3-4 nesting levels** — Deep package hierarchies reduce readability.
4. **Use reverse domain name** — Prevents naming collisions (`com.company.project`).
5. **Prefer specific imports** — Avoid wildcard imports (`import com.company.*`); they obscure dependencies and cause conflicts.
6. **Keep related classes close** — A class should only reference classes in the same package or one level up.
7. **Avoid circular dependencies** — If package A depends on B, B should not depend on A.
8. **Use `module-info.java`** — Encapsulate internal APIs and expose only what is necessary (Java 9+).
9. **Package-private for internals** — Use package-private access for helper classes and methods that should not leak outside the package.
10. **Consistent naming** — Follow `com.company.project.module.submodule` pattern throughout the codebase.

## References

- [JLS §7.4 — Package Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-7.html#jls-7.4)
- [JLS §7.5 — Import Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-7.html#jls-7.5)
- [JLS §8 — Classes (Access Modifiers)](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html)
- [Java Platform Module System](https://docs.oracle.com/javase/9/modules/)
- [Google Java Style Guide — Package Statements](https://google.github.io/styleguide/javaguide.html#s3-package-statements)