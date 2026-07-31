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
- One public class per file
- Logical grouping by feature/layer
- Max 3-4 nesting levels
- Use domain name (reverse)