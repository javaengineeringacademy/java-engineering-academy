# OOP Anti-Patterns

## Why NOT Use Null

### Use Optional Instead

```java
// Bad: Null is ambiguous
public String findUser(String id) {
    if (exists(id)) {
        return getUser(id);
    }
    return null; // What does null mean?
}

// Good: Optional is explicit
public Optional<String> findUser(String id) {
    if (exists(id)) {
        return Optional.of(getUser(id));
       return Optional.empty(); // Explicit empty
}
```

**Problems with null:**
- NullPointerException (1 billion per year in Java apps)
- Ambiguous meaning (null = not found? error? default?)
- Breaks type safety
- Cannot be used in collections

**Benefits of Optional:**
- Explicit handling of missing values
- No NPE
- Functional composition
- Self-documenting code

---

## Why NOT Use Checked Exceptions Everywhere

### Prefer Unchecked Exceptions

```java
// Bad: Checked exception for everything
public void readFile(String path) throws IOException, FileNotFoundException {
    // Forces caller to handle
}

// Good: Unchecked for programming errors
public void processUser(String id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
}
```

**Problems with checked exceptions:**
- Verbosity (try-catch everywhere)
- Forces error handling at wrong level
- Breaks method signatures
- Hinders functional programming

**When to use checked exceptions:**
- Recoverable errors (file not found, network timeout)
- External system failures
- When caller must handle error

**When to use unchecked exceptions:**
- Programming errors (null, index out of bounds)
- Invalid state
- Cannot recover from error

---

## Why NOT Use Final on Everything

### Use Sparingly

```java
// Bad: Final on everything
public final class User {
    private final String name;
    private final int age;
    
    public final String getName() { return name; }
    public final int getAge() { return age; }
}

// Good: Final only when needed
public class User {
    private final String name; // Immutable field
    private int age; // Mutable field
    
    public String getName() { return name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
```

**Problems with excessive final:**
- Reduces flexibility
- Hinders testing (cannot mock)
- Prevents subclassing when needed
- Over-constrains design

**When to use final:**
- Immutable fields
- Preventing method override
- Preventing subclassing
- Thread safety

---

## Why NOT Use Static Methods Everywhere

### Prefer Instance Methods

```java
// Bad: Static methods everywhere
public class UserValidator {
    public static boolean validate(User user) { ... }
    public static boolean isEmailValid(String email) { ... }
}

// Good: Instance methods with dependency injection
public class UserValidator {
    private final EmailValidator emailValidator;
    
    public UserValidator(EmailValidator emailValidator) {
        this.emailValidator = emailValidator;
    }
    
    public boolean validate(User user) { ... }
}
```

**Problems with static methods:**
- Hard to test (cannot mock)
- Tight coupling
- No polymorphism
- Breaks dependency injection

**When to use static methods:**
- Utility methods (no state)
- Factory methods
- Mathematical operations
- Constants

---

## Why NOT Use Global State

### Prefer Dependency Injection

```java
// Bad: Global state
public class UserService {
    public void processUser() {
        User user = GlobalConfig.getCurrentUser(); // Global state
        Database db = GlobalDatabase.getConnection(); // Global state
    }
}

// Good: Dependency injection
public class UserService {
    private final UserContext userContext;
    private final Database database;
    
    public UserService(UserContext userContext, Database database) {
        this.userContext = userContext;
        this.database = database;
    }
    
    public void processUser() {
        User user = userContext.getCurrentUser();
        // ...
    }
}
```

**Problems with global state:**
- Hidden dependencies
- Hard to test
- Thread safety issues
- Tight coupling
- Unpredictable behavior

**When global state is acceptable:**
- Configuration (be careful)
- Logging
- Constants

---

## Why NOT Use God Objects

### Follow Single Responsibility Principle

```java
// Bad: God object doing everything
public class UserManager {
    public void createUser() { ... }
    public void deleteUser() { ... }
    public void sendEmail() { ... }
    public void generateReport() { ... }
    public void connectDatabase() { ... }
    public void validateInput() { ... }
}

// Good: Separate concerns
public class UserService {
    public void createUser() { ... }
    public void deleteUser() { ... }
}

public class EmailService {
    public void sendEmail() { ... }
}

public class ReportService {
    public void generateReport() { ... }
}
```

**Problems with God objects:**
- Violates Single Responsibility Principle
- Hard to understand
- Hard to test
- Hard to maintain
- Changes affect many areas

**How to identify:**
- More than 10 methods
- Multiple responsibilities
- Hard to name class
- Changes require understanding whole class

---

## Summary

| Anti-Pattern | Better Alternative |
|--------------|-------------------|
| Null | Optional |
| Checked exceptions everywhere | Unchecked for programming errors |
| Final on everything | Final only when needed |
| Static methods everywhere | Instance methods with DI |
| Global state | Dependency injection |
| God objects | Single Responsibility |

## Key Takeaways

1. Use Optional instead of null
2. Use unchecked exceptions for programming errors
3. Use final sparingly (immutable fields, prevent override)
4. Prefer instance methods for testability
5. Avoid global state; use dependency injection
6. Follow Single Responsibility Principle
