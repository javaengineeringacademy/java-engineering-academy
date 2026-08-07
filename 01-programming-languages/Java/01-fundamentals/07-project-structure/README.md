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

## Important Java Packages

### java.lang (Automatically Imported)

The `java.lang` package is imported automatically — you never need to import it.

| Class | Purpose | When You Use It |
|-------|---------|-----------------|
| `String` | Text | Everywhere |
| `Integer`, `Long`, `Double`, `Float`, `Boolean` | Wrapper types | Collections, primitives → objects |
| `Math` | Math operations | `Math.random()`, `Math.max()`, `Math.sqrt()` |
| `System` | System I/O, env vars | `System.out.println()`, `System.getenv()` |
| `Thread` | Multithreading | `Thread.sleep()`, `Thread.currentThread()` |
| `Runnable` | Thread interface | `new Thread(() -> ...)` |
| `Exception` | Base exception | All exceptions extend this |
| `Object` | Base class | All classes extend this |
| `Class` | Runtime type info | `getClass()`, `forName()` |
| `Enum` | Base enum | All enums extend this |
| `StringBuilder` | Mutable strings | String concatenation in loops |
| `Character` | Single chars | `Character.isDigit()`, `Character.toUpperCase()` |
| `Throwable` | Base error | Parent of Exception and Error |
| `Number` | Numeric base | Parent of Integer, Long, Double |
| `Package` | Package metadata | `getClass().getPackage()` |

### java.util (Collections and Utilities)

| Class/Interface | Purpose | When You Use It |
|-----------------|---------|-----------------|
| `List` (ArrayList, LinkedList) | Ordered collection | Most common data structure |
| `Set` (HashSet, TreeSet) | Unique elements | Remove duplicates |
| `Map` (HashMap, TreeMap) | Key-value pairs | Fast lookups |
| `Queue` (PriorityQueue, ArrayDeque) | FIFO processing | BFS, task scheduling |
| `Stack` (legacy) | LIFO | Avoid — use Deque instead |
| `Date`, `Calendar` | Old date API | Legacy code only |
| `LocalDate`, `LocalTime`, `LocalDateTime` | Modern date API | New code — always use this |
| `Optional` | Null handling | Return types that may be empty |
| `Random` | Random numbers | `Random.nextInt()` |
| `Collections` | Utility methods | `Collections.sort()`, `Collections.unmodifiableList()` |
| `Arrays` | Array utilities | `Arrays.sort()`, `Arrays.asList()` |
| `Timer`, `TimerTask` | Scheduled tasks | Simple scheduling (use ScheduledExecutor instead) |
| `UUID` | Unique IDs | `UUID.randomUUID()` |
| `Scanner` | User input | Console apps, file reading |
| `Properties` | Key-value config | Config files, system properties |
| `BitSet` | Bit manipulation | Large boolean sets, Bloom filters |
| `WeakHashMap` | Memory-sensitive cache | Cache with auto-cleanup |

### java.io (Input/Output)

| Class | Purpose | When You Use It |
|-------|---------|-----------------|
| `File` | File metadata | Check existence, size, delete |
| `FileInputStream/FileOutputStream` | Byte streams | Binary data |
| `BufferedReader/BufferedWriter` | Buffered I/O | Text files (faster) |
| `PrintWriter` | Formatted output | Writing text files |
| `ObjectInputStream/ObjectOutputStream` | Serialization | Save/load objects |
| `FileReader/FileWriter` | Character streams | Text files (simpler) |
| `ByteArrayInputStream/ByteArrayOutputStream` | In-memory I/O | Testing, temporary data |
| `DataInputStream/DataOutputStream` | Primitive I/O | Binary protocols |

### java.nio (New I/O — Modern)

| Class | Purpose | When You Use It |
|-------|---------|-----------------|
| `Path` | File paths | Modern alternative to File |
| `Files` | File operations | Read, write, copy, delete |
| `ByteBuffer` | Buffer operations | NIO channels |
| `FileChannel` | Channel I/O | High-performance file I/O |
| `Selector` | Non-blocking I/O | Network servers |
| `StandardOpenOption` | File open modes | `READ`, `WRITE`, `CREATE` |

### java.math (Mathematical Operations)

| Class | Purpose | When You Use It |
|-------|---------|-----------------|
| `BigInteger` | Arbitrary precision integers | Cryptography, large numbers |
| `BigDecimal` | Arbitrary precision decimals | Money, financial calculations |
| `MathContext` | Precision control | Rounding modes |

### java.time (Date/Time — Java 8+)

| Class | Purpose | When You Use It |
|-------|---------|-----------------|
| `LocalDate` | Date only | Birthdays, deadlines |
| `LocalTime` | Time only | Business hours |
| `LocalDateTime` | Date + time | Timestamps (no timezone) |
| `ZonedDateTime` | Date + time + timezone | Global applications |
| `Instant` | Epoch timestamps | Logging, APIs |
| `Duration` | Time amounts | `Duration.ofHours(2)` |
| `Period` | Date amounts | `Period.ofDays(30)` |
| `DateTimeFormatter` | Formatting | `format(DateTimeFormatter.ISO_DATE)` |

### java.net (Networking)

| Class | Purpose | When You Use It |
|-------|---------|-----------------|
| `URL` | URL handling | `new URL("https://...")` |
| `HttpURLConnection` | HTTP requests | Legacy HTTP (use HttpClient instead) |
| `HttpClient` | HTTP client (Java 11+) | Modern HTTP calls |
| `ServerSocket` | TCP server | Simple servers |
| `Socket` | TCP client | Network connections |
| `InetAddress` | IP addresses | `InetAddress.getByName()` |

### java.util.concurrent (Concurrency)

| Class | Purpose | When You Use It |
|-------|---------|-----------------|
| `ExecutorService` | Thread pool | Task execution |
| `ScheduledExecutorService` | Scheduled tasks | Periodic jobs |
| `CompletableFuture` | Async composition | Chaining async operations |
| `ConcurrentHashMap` | Thread-safe map | Concurrent access |
| `CopyOnWriteArrayList` | Thread-safe list | Read-heavy, write-light |
| `BlockingQueue` | Producer-consumer | Thread communication |
| `CountDownLatch` | Synchronization | Wait for N threads |
| `CyclicBarrier` | Synchronization | Wait at checkpoint |
| `Semaphore` | Rate limiting | Limit concurrent access |
| `AtomicInteger`, `AtomicLong` | Lock-free counters | Thread-safe counters |
| `Lock` (ReentrantLock) | Explicit locks | More flexible than synchronized |

### java.util.regex (Regular Expressions)

| Class | Purpose | When You Use It |
|-------|---------|-----------------|
| `Pattern` | Compiled regex | `Pattern.compile("\\d+")` |
| `Matcher` | Match execution | `matcher.find()`, `matcher.group()` |

### java.text (Text Processing)

| Class | Purpose | When You Use It |
|-------|---------|-----------------|
| `NumberFormat` | Number formatting | `NumberFormat.getCurrencyInstance()` |
| `DateFormat` | Date formatting | Legacy (use DateTimeFormatter) |
| `MessageFormat` | String formatting | `MessageFormat.format("Hello {0}", name)` |
| `DecimalFormat` | Custom number format | `new DecimalFormat("#,###.##")` |
| `Collator` | Locale-aware sorting | `Collator.getInstance(Locale.US)` |

### java.security (Security)

| Class | Purpose | When You Use It |
|-------|---------|-----------------|
| `MessageDigest` | Hashing | `MessageDigest.getInstance("SHA-256")` |
| `SecureRandom` | Cryptographic random | Token generation |
| `KeyStore` | Certificate storage | SSL/TLS |
| `Signature` | Digital signatures | Verify authenticity |

---

## Common Myths

### ❌ Myth 1: You must import java.lang classes
**Reality:** java.lang is imported automatically. You never need `import java.lang.String;`.

### ❌ Myth 2: import statements slow down your program
**Reality:** Imports are resolved at compile time. They have zero runtime cost.

### ❌ Myth 3: Wildcard imports are always bad
**Reality:** Acceptable in test files with many imports. Just avoid in production code.

### ❌ Myth 4: Package-private access is useless
**Reality:** It's essential for hiding implementation details within a module.

### ❌ Myth 5: Subpackages inherit parent package access
**Reality:** Subpackages are separate packages. `com.foo.bar` cannot access package-private members in `com.foo`.

---

## Production Checklist

### ✅ Before using packages in production:

☐ I know which package each class belongs to
☐ I use specific imports (not wildcards)
☐ I don't import unused classes
☐ I understand package-private access
☐ I know that subpackages are separate packages
☐ I use package-info.java for package documentation
☐ I follow reverse domain name convention

---

## Engineering Maturity Levels

### Level 1: Can Use
- Knows how to declare a package
- Can use import statements

### Level 2: Understands
- Knows naming conventions
- Understands directory structure mapping

### Level 3: Deep Knowledge
- Knows access modifiers and package-private
- Understands subpackages are separate

### Level 4: Expert
- Designs package architecture for large systems
- Knows when to use package-private vs public

### Level 5: Master
- Designs modular systems with package boundaries
- Can refactor packages in legacy codebases

---

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Organize code into namespaces |
| Convention | Reverse domain name (com.company.project) |
| Declaration | First line of file: `package com.example;` |
| Import | `import com.example.MyClass;` |
| Static Import | `import static com.example.MyClass.method;` |
| Wildcard | `import com.example.*;` (avoid in production) |
| java.lang | Auto-imported, never need to import |
| Default Access | Package-private (no modifier) |
| Subpackages | Separate packages, no inherited access |

---

## Related Topics

- **[Access Modifiers](../../02-oop/39-access-modifiers/)** — Deep dive into public, protected, default, private
- **[Maven](../08-maven/)** — Build tool that manages package structure
- **[Encapsulation](../../02-oop/08-encapsulation/)** — How packages enable access control

---

## What's Next

Now that you understand how to organize code with packages, learn about [Maven](../08-maven/) — the build tool that manages project structure, dependencies, and build processes.
