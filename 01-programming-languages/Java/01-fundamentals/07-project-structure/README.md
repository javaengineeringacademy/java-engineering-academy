# Java Project Structure & Packages

As your Java programs grow beyond a single file, you need a way to organize them. Packages are Java's mechanism for grouping related classes and interfaces into namespaces — similar to how folders organize files on your computer.

---

## What Is a Package?

A **package** is a namespace that groups related classes, interfaces, and sub-packages together. Every Java file begins with a package declaration that tells the compiler which package the class belongs to.

```java
package com.company.project.model;

public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

The package declaration (`package com.company.project.model;`) must be the first line in your file (after comments). It maps directly to the directory structure on your filesystem.

---

## Package Naming Conventions

Java uses **reverse domain name** notation for package names to avoid naming conflicts across organizations.

```
com.company.project.module
│       │       │      │
│       │       │      └── Specific module or feature
│       │       └── Project name
│       └── Company or organization name
└── Top-level domain (com, org, net, edu)
```

**Rules:**
- All lowercase letters
- No spaces or special characters
- Use underscores only when necessary (e.g., `com.example.my_project`)
- Each segment corresponds to a directory in your project

**Common patterns:**
```
com.company.project            → General project code
com.company.project.model      → Data classes (POJOs, entities)
com.company.project.service    → Business logic
com.company.project.repository → Database access
com.company.project.controller → API endpoints
com.company.project.util       → Helper/utility classes
com.company.project.config     → Configuration classes
org.openjsse.tls              → Third-party library
edu.university.course         → Educational projects
```

---

## Package Declaration and Import Statements

Every Java file has two key sections at the top: the package declaration and imports.

```java
// 1. Package declaration (always first)
package com.company.project.service;

// 2. Import statements
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import com.company.project.model.User;

public class UserService {
    // ... class body
}
```

**Package declaration:** Declares which package this class belongs to. One per file, must be first.

**Import statements:** Bring other classes into scope so you can use their short names instead of fully qualified names.

Without imports, you'd have to write:
```java
java.util.List<java.lang.String> names = new java.util.ArrayList<>();
```

With imports, it becomes:
```java
List<String> names = new ArrayList<>();
```

---

## How Packages Relate to Directory Structure

The package name **must match** the directory structure relative to the source root (`src/main/java`).

```
src/main/java/
└── com/
    └── company/
        └── project/
            ├── model/
            │   ├── User.java
            │   └── Order.java
            ├── service/
            │   ├── UserService.java
            │   └── OrderService.java
            ├── repository/
            │   └── UserRepository.java
            ├── controller/
            │   └── UserController.java
            └── util/
                └── StringUtils.java
```

Each `.java` file starts with the full package path:

```java
// In src/main/java/com/company/project/model/User.java
package com.company.project.model;

public class User {
    // ...
}
```

```java
// In src/main/java/com/company/project/service/UserService.java
package com.company.project.service;

import com.company.project.model.User;  // Must import from other packages

public class UserService {
    // ...
}
```

---

## Access Modifiers and Packages

Java has four access modifiers that control visibility. Packages play a key role in two of them.

### The Four Access Levels

| Modifier | Same Class | Same Package | Subclass | Everywhere |
|----------|-----------|--------------|----------|------------|
| `public` | Yes | Yes | Yes | Yes |
| `protected` | Yes | Yes | Yes | No |
| default (no modifier) | Yes | Yes | No | No |
| `private` | Yes | No | No | No |

### Default Access (Package-Private)

When you omit the access modifier, the class or member has **default** (package-private) access — visible only within the same package.

```java
package com.company.project.model;

public class User {
    private String name;

    // Package-private — only visible to classes in com.company.project.model
    String internalId;

    public String getName() {
        return name;
    }
}
```

### Protected Access

`protected` members are visible within the same package AND in subclasses (even across packages).

```java
package com.company.project.model;

public class BaseEntity {
    protected Long id;  // Visible to subclasses anywhere

    protected void beforeSave() {
        // Subclasses can override this
    }
}
```

```java
package com.company.project.model;

public class User extends BaseEntity {
    public void save() {
        beforeSave();  // OK — inherited protected method
        this.id = 1L;  // OK — protected field from parent
    }
}
```

### Practical Example: Layered Architecture

```java
// repository/UserRepository.java — package-private, internal to repository layer
package com.company.project.repository;

import com.company.project.model.User;

class UserRepository {  // default access — not public
    User findById(Long id) {
        // database query
        return new User("Alice");
    }
}
```

```java
// service/UserService.java — public, uses UserRepository
package com.company.project.service;

import com.company.project.model.User;
// Cannot import com.company.project.repository.UserRepository
// because it's not public

import com.company.project.repository.UserRepository;  // This works if UserRepository is public

public class UserService {
    private UserRepository repo = new UserRepository();

    public User getUser(Long id) {
        return repo.findById(id);
    }
}
```

---

## Static Imports

Static imports let you use static members without qualifying them with the class name.

```java
// Without static import
Math.sqrt(16)        // 4.0
Math.PI              // 3.14159...
Math.max(10, 20)     // 20

// With static import
import static java.lang.Math.sqrt;
import static java.lang.Math.PI;
import static java.lang.Math.max;

sqrt(16)             // 4.0
PI                   // 3.14159...
max(10, 20)          // 20
```

**When to use static imports:**
- Constants: `import static java.util.concurrent.TimeUnit.SECONDS;`
- Utility methods: `import static org.junit.Assert.assertEquals;`
- Enum values: `import static java.lang.Thread.State.*;`

**Avoid overusing them** — they can reduce readability if the class name is no longer visible.

---

## Wildcard Imports vs. Specific Imports

### Specific Imports (Preferred)

```java
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
```

**Pros:**
- Clear which classes you're using
- No ambiguity if two packages have classes with the same name
- IDEs can optimize unused imports
- Easier to read and maintain

### Wildcard Imports

```java
import java.util.*;
```

This imports all public classes from the `java.util` package.

**Pros:**
- Less typing
- Auto-includes new classes added to the package

**Cons:**
- Hides which classes you're actually using
- Can cause ambiguity: both `java.util.Date` and `java.sql.Date` exist
- Considered bad practice in most style guides

**When wildcards are acceptable:**
- Test files with many imports
- When resolving ambiguity with fully qualified names

```java
import java.util.Date;       // Specific import
import java.sql.Date;        // Specific import

// Now you must disambiguate
java.util.Date utilDate = new java.util.Date();
java.Date sqlDate = java.Date.valueOf("2024-01-15");
```

---

## Organizing Classes Within a Package

### Classes in the Same Package

Classes in the same package can access each other's default and protected members:

```java
package com.company.project.model;

public class Order {
    double totalAmount;  // package-private

    void calculateTotal() {
        // Can access package-private members from other classes
    }
}
```

```java
package com.company.project.model;

public class OrderItem {
    Order order;  // package-private — can reference Order directly

    void updateOrder() {
        order.calculateTotal();  // OK — same package
    }
}
```

### Subpackages Are Separate Packages

Subpackages do **not** inherit access from parent packages:

```
com.company.project.model       → Package A
com.company.project.model.user  → Package B (separate!)
```

```java
// com/company/project/model/Order.java
package com.company.project.model;

class Order {
    String internalField;  // package-private
}
```

```java
// com/company/project/model/user/UserProfile.java
package com.company.project.model.user;

import com.company.project.model.Order;

class UserProfile {
    void process() {
        Order order = new Order();
        // order.internalField;  // ERROR! Different package, no access
    }
}
```

---

## Why Packages Matter

### 1. Organization
Packages group related code together, making your project navigable:
```
com.company.project
├── model/       → Data structures
├── service/     → Business logic
├── repository/  → Data access
├── controller/  → API endpoints
└── util/        → Shared utilities
```

### 2. Naming Conflicts
Without packages, every class name would need to be globally unique. With packages:
```java
com.company.Date      // Your custom Date class
com.otherorg.Date     // Another org's Date class
java.util.Date        // Standard library Date
```

### 3. Access Control
Packages enable the default access modifier, allowing you to hide implementation details:
```java
// Public API
public class UserService {
    public User findUser(Long id) { ... }
}

// Internal implementation — not exposed to other packages
class UserMapper {
    User mapFromDatabase(ResultSet rs) { ... }
}
```

### 4. Reusability
Well-packaged code can be extracted into libraries and reused across projects.

### 5. Maintainability
Clear package boundaries make it easier to understand where changes should be made and what code affects what.

---

## Quick Reference

| Concept | Syntax | Purpose |
|---------|--------|---------|
| Package declaration | `package com.example;` | Declares which package a class belongs to |
| Import | `import com.example.MyClass;` | Brings a specific class into scope |
| Wildcard import | `import com.example.*;` | Imports all public classes from a package |
| Static import | `import static com.example.MyClass.method;` | Imports a static member directly |
| Default access | `class Foo { }` or `void bar() { }` | Visible only within the same package |
| `public` access | `public class Foo { }` | Visible everywhere |
| `protected` access | `protected void bar() { }` | Visible in same package + subclasses |
| `private` access | `private void bar() { }` | Visible only within the same class |

---

## What's Next

Now that you understand how to organize code with packages, learn about [Maven](../08-maven/) — the build tool that manages project structure, dependencies, and build processes.
