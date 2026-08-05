# Java Anti-Patterns

## 1. God Object
**Description:** A class that knows too much or does too much, violating the Single Responsibility Principle.

**Why it's bad:** Hard to maintain, test, and understand. Changes in one part affect the entire class.

**Example (bad code):**
```java
public class UserManager {
    public void createUser(String name, String email) { /* ... */ }
    public void sendEmail(String to, String subject) { /* ... */ }
    public void generateReport() { /* ... */ }
    public void connectToDatabase() { /* ... */ }
    public void logActivity(String message) { /* ... */ }
}
```

**Better approach:** Split into focused classes: `UserService`, `EmailService`, `ReportGenerator`, `DatabaseConnector`, `Logger`.

**Impact:** Reduces coupling, improves testability, makes code easier to modify.

---

## 2. Magic Numbers and Strings
**Description:** Hardcoded values used directly in code without explanation.

**Why it's bad:** Reduces readability, makes maintenance difficult, error-prone when values need to change.

**Example (bad code):**
```java
if (user.getAge() > 18) {
    discount = price * 0.15;
}
if (status == 3) {
    throw new Exception("Invalid");
}
```

**Better approach:** Use named constants:
```java
private static final int LEGAL_AGE = 18;
private static final double MEMBER_DISCOUNT = 0.15;
private static final int STATUS_INVALID = 3;
```

**Impact:** Improves readability, reduces errors, easier maintenance.

---

## 3. Null Abuse
**Description:** Using null to represent absence of value, leading to NullPointerExceptions.

**Why it's bad:** Causes runtime crashes, forces defensive null checks everywhere, obscures intent.

**Example (bad code):**
```java
String name = user.getName();
if (name != null) {
    System.out.println(name.toUpperCase());
}
```

**Better approach:** Use Optional, null objects, or default values:
```java
Optional<String> name = Optional.ofNullable(user.getName());
name.ifPresent(n -> System.out.println(n.toUpperCase()));
```

**Impact:** Eliminates NullPointerExceptions, makes code more expressive.

---

## 4. String Concatenation in Loops
**Description:** Using `+` operator to concatenate strings in loops.

**Why it's bad:** Creates new String objects each iteration, O(n^2) time complexity.

**Example (bad code):**
```java
String result = "";
for (String item : list) {
    result += item + ", ";
}
```

**Better approach:** Use StringBuilder:
```java
StringBuilder sb = new StringBuilder();
for (String item : list) {
    sb.append(item).append(", ");
}
```

**Impact:** Reduces memory allocation, improves performance significantly.

---

## 5. Catching All Exceptions
**Description:** Using `catch (Exception e)` or `catch (Throwable t)` to handle all errors.

**Why it's bad:** Hides specific errors, makes debugging difficult, may swallow important exceptions.

**Example (bad code):**
```java
try {
    // complex logic
} catch (Exception e) {
    e.printStackTrace();
}
```

**Better approach:** Catch specific exceptions:
```java
try {
    // complex logic
} catch (FileNotFoundException e) {
    // handle file not found
} catch (IOException e) {
    // handle IO error
}
```

**Impact:** Better error handling, easier debugging, more robust applications.

---

## 6. Resource Leaks
**Description:** Not closing resources like streams, connections, or readers.

**Why it's bad:** Causes memory leaks, exhausts system resources, can crash applications.

**Example (bad code):**
```java
FileInputStream fis = new FileInputStream("file.txt");
// use fis
// forgot to close
```

**Better approach:** Use try-with-resources:
```java
try (FileInputStream fis = new FileInputStream("file.txt")) {
    // use fis
}
```

**Impact:** Ensures proper resource cleanup, prevents memory leaks.

---

## 7. Overusing Static Methods
**Description:** Making everything static for convenience.

**Why it's bad:** Makes testing difficult, reduces flexibility, couples code tightly.

**Example (bad code):**
```java
public class Utility {
    public static void process() { /* ... */ }
    public static void validate() { /* ... */ }
    public static void save() { /* ... */ }
}
```

**Better approach:** Use instance methods with dependency injection where appropriate.

**Impact:** Improves testability, enables polymorphism, reduces coupling.

---

## 8. Premature Optimization
**Description:** Optimizing code before profiling shows it's necessary.

**Why it's bad:** Adds complexity, may introduce bugs, often targets the wrong areas.

**Example (bad code):**
```java
// Complex caching for a method that's called once a day
public class CacheManager {
    private Map<String, Object> cache = new ConcurrentHashMap<>();
    // complex caching logic
}
```

**Better approach:** Write clean code first, profile, then optimize hotspots.

**Impact:** Simpler code, easier maintenance, optimizations targeted where needed.

---

## 9. Using Raw Types
**Description:** Using collections without generics (pre-Java 5 style).

**Why it's bad:** Loses type safety, requires casting, can cause ClassCastExceptions.

**Example (bad code):**
```java
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);
```

**Better approach:** Use parameterized types:
```java
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);
```

**Impact:** Type safety, no casting needed, better compile-time checking.

---

## 10. Tight Coupling
**Description:** Classes depending directly on concrete implementations.

**Why it's bad:** Hard to change implementations, difficult to test, reduces flexibility.

**Example (bad code):**
```java
public class OrderService {
    private MySQLOrderRepository repository = new MySQLOrderRepository();
}
```

**Better approach:** Depend on interfaces:
```java
public class OrderService {
    private final OrderRepository repository;
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }
}
```

**Impact:** Enables swapping implementations, easier testing, more flexible architecture.

---

## 11. Excessive Synchronization
**Description:** Overusing synchronized blocks or methods.

**Why it's bad:** Can cause deadlocks, reduces performance, makes code harder to reason about.

**Example (bad code):**
```java
public synchronized void method1() { /* ... */ }
public synchronized void method2() { /* ... */ }
public synchronized void method3() { /* ... */ }
```

**Better approach:** Use fine-grained locking, concurrent data structures, or lock-free algorithms.

**Impact:** Better performance, reduced deadlock risk, more scalable code.

---

## 12. Ignoring checked exceptions
**Description:** Catching and immediately rethrowing or swallowing checked exceptions.

**Why it's bad:** Hides errors, makes debugging difficult, violates exception handling contracts.

**Example (bad code):**
```java
try {
    riskyOperation();
} catch (IOException e) {
    throw new RuntimeException(e);
}
```

**Better approach:** Handle exceptions meaningfully or let them propagate with proper context.

**Impact:** Better error handling, clearer failure modes, easier debugging.