# Custom Exceptions

## 1. Introduction

Custom exceptions allow you to create exception classes tailored to your application's specific error conditions. They provide meaningful error types, carry domain-specific information, and enable better error handling and recovery. This lesson covers the design, implementation, and best practices for custom exceptions.

## 2. Learning Objectives

By the end of this lesson, you will be able to:

- Design custom exception hierarchies
- Create checked and unchecked custom exceptions
- Add domain-specific information to exceptions
- Implement exception factories and builders
- Use custom exceptions effectively in applications
- Follow custom exception best practices
- Debug custom exception issues

## 3. Prerequisites

- Understanding of exception hierarchy
- Knowledge of checked vs unchecked exceptions
- Familiarity with OOP concepts
- Understanding of throw and throws keywords

## 4. Why This Concept Exists

### The Problem

Generic exceptions don't provide domain-specific context:

```java
public void processOrder(Order order) throws Exception {
    if (order == null) throw new Exception("Invalid order");
    if (order.getTotal() <= 0) throw new Exception("Invalid total");
    if (!order.hasItems()) throw new Exception("Empty order");
    // Which exception was it? Hard to handle differently
}
```

### The Solution

Custom exceptions provide meaningful context:

```java
public void processOrder(Order order) throws OrderException {
    if (order == null) throw new NullPointerException("Order cannot be null");
    if (order.getTotal() <= 0) 
        throw new InvalidOrderException("Order total must be positive: " + order.getTotal());
    if (!order.hasItems()) 
        throw new EmptyOrderException("Order must contain at least one item");
    // Each exception type can be handled differently
}
```

## 5. Problem Statement

### Challenge 1: Meaningful Error Types

How do you create exceptions that clearly communicate what went wrong?

### Challenge 2: Error Context

How do you include relevant information for error diagnosis and recovery?

### Challenge 3: Exception Hierarchy

How do you design an exception hierarchy that supports different handling strategies?

### Challenge 4: Exception Factory

How do you create exceptions consistently with proper context?

## 6. Theory

### Custom Exception Types

**Checked Custom Exceptions:**
- Extend `Exception`
- Must be declared or caught
- Use for recoverable conditions

**Unchecked Custom Exceptions:**
- Extend `RuntimeException`
- Don't need declaration
- Use for programming errors

### Exception Hierarchy Design

```
BaseApplicationException (abstract)
├── ValidationException
│   ├── InvalidFieldException
│   └── MissingFieldException
├── ResourceException
│   ├── NotFoundException
│   ├── AlreadyExistsException
│   └── AccessDeniedException
└── SystemException
    ├── ConfigurationException
    └── IntegrationException
```

### Information to Include

- Error code (for programmatic handling)
- Timestamp
- Request/context information
- Recovery suggestions
- Nested cause chain

## 7. Internal Working

### Exception Object Creation

When creating custom exceptions:
1. Constructor is called
2. Message and cause are set
3. Stack trace is filled (lazy)
4. Custom fields are initialized

### JVM Behavior

The JVM treats custom exceptions the same as standard exceptions:
- Same propagation rules
- Same catch mechanisms
- Same finally block behavior

## 8. JVM Perspective

### Class Loading

Custom exception classes are loaded by the classloader just like any other class. The JVM checks:
- Class hierarchy (must extend Throwable)
- Constructor signature
- Serialization compatibility (if applicable)

### Exception Table

Custom exceptions are stored in the exception table just like standard exceptions:
```
Exception Table:
from    to  target  type
  0    12    15   Class com/example/MyCustomException
```

## 9. Memory Representation

### Custom Exception Object

```
Custom Exception Object
├── Object Header
├── Standard Exception Fields
│   ├── message
│   ├── cause
│   ├── stackTrace
│   └── suppressedExceptions
├. Custom Fields
│   ├── errorCode
│   ├── timestamp
│   ├── context
│   └. recoverySuggestion
```

### Inheritance Chain

```
Object
└── Throwable
    └── Exception
        └── BaseApplicationException
            └── CustomException
```

## 10. Syntax

### Basic Custom Exception

```java
public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }
    
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### Custom Exception with Error Code

```java
public class BusinessException extends Exception {
    private final String errorCode;
    
    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
```

### Unchecked Custom Exception

```java
public class ValidationException extends RuntimeException {
    private final String field;
    
    public ValidationException(String message, String field) {
        super(message);
        this.field = field;
    }
    
    public String getField() {
        return field;
    }
}
```

## 11. Easy Example

### Simple Custom Exception

```java
public class InsufficientFundsException extends Exception {
    private final double balance;
    private final double amount;
    
    public InsufficientFundsException(double balance, double amount) {
        super(String.format("Insufficient funds: balance=%.2f, amount=%.2f", 
            balance, amount));
        this.balance = balance;
        this.amount = amount;
    }
    
    public double getBalance() { return balance; }
    public double getAmount() { return amount; }
    public double getDeficit() { return amount - balance; }
}

// Usage
public class BankAccount {
    private double balance;
    
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(balance, amount);
        }
        balance -= amount;
    }
    
    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        account.balance = 100;
        
        try {
            account.withdraw(150);
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
            System.out.printf("You need %.2f more%n", e.getDeficit());
        }
    }
}
```

### Simple Validation Exception

```java
public class ValidationException extends RuntimeException {
    private final String fieldName;
    private final Object invalidValue;
    
    public ValidationException(String fieldName, Object invalidValue, String message) {
        super(String.format("Validation failed for field '%s': %s (value: %s)", 
            fieldName, message, invalidValue));
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }
    
    public String getFieldName() { return fieldName; }
    public Object getInvalidValue() { return invalidValue; }
}

// Usage
public class UserValidator {
    public void validate(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new ValidationException("name", user.getName(), "cannot be empty");
        }
        if (user.getAge() < 0 || user.getAge() > 150) {
            throw new ValidationException("age", user.getAge(), "must be between 0 and 150");
        }
    }
}
```

## 12. Medium Example

### Exception Hierarchy

```java
public abstract class ApplicationException extends Exception {
    private final String errorCode;
    private final Instant timestamp;
    
    protected ApplicationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
    }
    
    public String getErrorCode() { return errorCode; }
    public Instant getTimestamp() { return timestamp; }
    
    public abstract String getRecoverySuggestion();
    
    @Override
    public String toString() {
        return String.format("%s{errorCode='%s', message='%s', timestamp=%s}", 
            getClass().getSimpleName(), errorCode, getMessage(), timestamp);
    }
}

public class ValidationException extends ApplicationException {
    private final String fieldName;
    
    public ValidationException(String fieldName, String message, Throwable cause) {
        super(message, "VALIDATION_ERROR", cause);
        this.fieldName = fieldName;
    }
    
    public String getFieldName() { return fieldName; }
    
    @Override
    public String getRecoverySuggestion() {
        return String.format("Please check the value for field '%s'", fieldName);
    }
}

public class ResourceNotFoundException extends ApplicationException {
    private final String resourceType;
    private final Object resourceId;
    
    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(String.format("%s not found with id: %s", resourceType, resourceId), 
            "RESOURCE_NOT_FOUND", null);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
    
    public String getResourceType() { return resourceType; }
    public Object getResourceId() { return resourceId; }
    
    @Override
    public String getRecoverySuggestion() {
        return String.format("Verify the %s with id '%s' exists", resourceType, resourceId);
    }
}
```

### Exception Factory

```java
public class ExceptionFactory {
    
    public static ValidationException validationError(String field, String value, String reason) {
        return new ValidationException(field, 
            String.format("Invalid value '%s' for field '%s': %s", value, field, reason), 
            null);
    }
    
    public static ResourceNotFoundException notFound(String resourceType, Object id) {
        return new ResourceNotFoundException(resourceType, id);
    }
    
    public static BusinessException businessError(String code, String message) {
        return new BusinessException(message, code);
    }
    
    public static <T extends Exception> T wrap(String message, Exception cause, 
            Class<T> exceptionClass) {
        try {
            return exceptionClass.getConstructor(String.class, Throwable.class)
                .newInstance(message, cause);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create exception", e);
        }
    }
}

// Usage
public class UserService {
    public User findById(Long id) throws ResourceNotFoundException {
        User user = repository.findById(id);
        if (user == null) {
            throw ExceptionFactory.notFound("User", id);
        }
        return user;
    }
    
    public void createUser(User user) throws ValidationException {
        if (user.getEmail() == null) {
            throw ExceptionFactory.validationError("email", null, "cannot be null");
        }
    }
}
```

## 13. Hard Example

### Exception Builder Pattern

```java
import java.util.*;
import java.time.Instant;

public class ExceptionBuilder {
    private String message;
    private Throwable cause;
    private ErrorCode code;
    private Map<String, Object> context;
    private String recoverySuggestion;
    private Severity severity;
    
    private ExceptionBuilder() {
        this.context = new HashMap<>();
        this.severity = Severity.MEDIUM;
    }
    
    public static ExceptionBuilder create() {
        return new ExceptionBuilder();
    }
    
    public ExceptionBuilder message(String message) {
        this.message = message;
        return this;
    }
    
    public ExceptionBuilder cause(Throwable cause) {
        this.cause = cause;
        return this;
    }
    
    public ExceptionBuilder code(ErrorCode code) {
        this.code = code;
        return this;
    }
    
    public ExceptionBuilder context(String key, Object value) {
        this.context.put(key, value);
        return this;
    }
    
    public ExceptionBuilder recovery(String suggestion) {
        this.recoverySuggestion = suggestion;
        return this;
    }
    
    public ExceptionBuilder severity(Severity severity) {
        this.severity = severity;
        return this;
    }
    
    public ApplicationException build() {
        return new ApplicationException(message, code, cause, 
            context, recoverySuggestion, severity);
    }
    
    public void throwIt() throws ApplicationException {
        throw build();
    }
    
    public enum ErrorCode {
        VALIDATION_ERROR("VAL", "Validation Failed"),
        NOT_FOUND("NF", "Resource Not Found"),
        ALREADY_EXISTS("AE", "Resource Already Exists"),
        AUTHORIZATION_ERROR("AUTH", "Authorization Failed"),
        INTERNAL_ERROR("INT", "Internal Error");
        
        private final String code;
        private final String description;
        
        ErrorCode(String code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public String getCode() { return code; }
        public String getDescription() { return description; }
    }
    
    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    static class ApplicationException extends Exception {
        private final ErrorCode errorCode;
        private final Map<String, Object> context;
        private final String recoverySuggestion;
        private final Severity severity;
        private final Instant timestamp;
        
        ApplicationException(String message, ErrorCode errorCode, Throwable cause,
                           Map<String, Object> context, String recoverySuggestion,
                           Severity severity) {
            super(message, cause);
            this.errorCode = errorCode;
            this.context = new HashMap<>(context);
            this.recoverySuggestion = recoverySuggestion;
            this.severity = severity;
            this.timestamp = Instant.now();
        }
        
        public ErrorCode getErrorCode() { return errorCode; }
        public Map<String, Object> getContext() { return context; }
        public String getRecoverySuggestion() { return recoverySuggestion; }
        public Severity getSeverity() { return severity; }
        public Instant getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getSimpleName()).append("{");
            sb.append("code=").append(errorCode.getCode());
            sb.append(", message='").append(getMessage()).append("'");
            if (!context.isEmpty()) {
                sb.append(", context=").append(context);
            }
            if (recoverySuggestion != null) {
                sb.append(", recovery='").append(recoverySuggestion).append("'");
            }
            sb.append(", severity=").append(severity);
            sb.append(", timestamp=").append(timestamp);
            sb.append("}");
            return sb.toString();
        }
    }
    
    // Usage
    public static void main(String[] args) {
        try {
            String email = "invalid-email";
            
            if (!email.contains("@")) {
                ExceptionBuilder.create()
                    .message("Invalid email format")
                    .code(ErrorCode.VALIDATION_ERROR)
                    .context("email", email)
                    .context("field", "email")
                    .severity(Severity.LOW)
                    .recovery("Please provide a valid email address")
                    .throwIt();
            }
        } catch (ApplicationException e) {
            System.out.println(e);
            System.out.println("Recovery: " + e.getRecoverySuggestion());
        }
    }
}
```

### Exception Registry

```java
import java.util.*;
import java.util.function.Supplier;

public class ExceptionRegistry {
    private final Map<String, ExceptionDefinition> definitions;
    private final Map<String, Supplier<? extends Exception>> factories;
    
    public ExceptionRegistry() {
        this.definitions = new ConcurrentHashMap<>();
        this.factories = new ConcurrentHashMap<>();
        registerDefaults();
    }
    
    private void registerDefaults() {
        register("VALIDATION", new ExceptionDefinition(
            "VAL", Severity.LOW, "Validation failed"));
        register("NOT_FOUND", new ExceptionDefinition(
            "NF", Severity.MEDIUM, "Resource not found"));
        register("CONFLICT", new ExceptionDefinition(
            "CON", Severity.MEDIUM, "Resource conflict"));
        register("INTERNAL", new ExceptionDefinition(
            "INT", Severity.HIGH, "Internal error"));
    }
    
    public void register(String type, ExceptionDefinition definition) {
        definitions.put(type, definition);
    }
    
    public void registerFactory(String type, Supplier<? extends Exception> factory) {
        factories.put(type, factory);
    }
    
    public Exception create(String type, String message, Object... context) {
        ExceptionDefinition def = definitions.get(type);
        if (def == null) {
            throw new IllegalArgumentException("Unknown exception type: " + type);
        }
        
        // Create exception with context
        return new RegistryException(message, def, context);
    }
    
    public void throwException(String type, String message, Object... context) throws Exception {
        throw create(type, message, context);
    }
    
    static class ExceptionDefinition {
        final String code;
        final Severity severity;
        final String description;
        
        ExceptionDefinition(String code, Severity severity, String description) {
            this.code = code;
            this.severity = severity;
            this.description = description;
        }
    }
    
    static class RegistryException extends Exception {
        private final ExceptionDefinition definition;
        private final Map<String, Object> context;
        private final Instant timestamp;
        
        RegistryException(String message, ExceptionDefinition definition, Object[] contextArray) {
            super(message);
            this.definition = definition;
            this.context = new HashMap<>();
            this.timestamp = Instant.now();
            
            // Parse context array into map
            for (int i = 0; i < contextArray.length - 1; i += 2) {
                this.context.put(String.valueOf(contextArray[i]), contextArray[i + 1]);
            }
        }
        
        public String getCode() { return definition.code; }
        public Severity getSeverity() { return definition.severity; }
        public Map<String, Object> getContext() { return context; }
        public Instant getTimestamp() { return timestamp; }
    }
    
    enum Severity { LOW, MEDIUM, HIGH, CRITICAL }
    
    // Usage
    public static void main(String[] args) {
        ExceptionRegistry registry = new ExceptionRegistry();
        
        try {
            registry.throwException("VALIDATION", 
                "Invalid input", "field", "email", "value", "test");
        } catch (Exception e) {
            RegistryException re = (RegistryException) e;
            System.out.println("Code: " + re.getCode());
            System.out.println("Severity: " + re.getSeverity());
            System.out.println("Context: " + re.getContext());
        }
    }
}
```

## 14. Performance

### Custom Exception Performance

**Same as standard exceptions:**
- Object creation: ~1-5 microseconds
- Stack trace filling: ~10-100 microseconds
- Propagation: ~1-2 microseconds per frame

### Best Practices

1. **Cache frequently thrown exceptions**
```java
public class ValidationException extends RuntimeException {
    private static final ValidationException EMPTY_NAME = 
        new ValidationException("name", "cannot be empty");
    
    public static ValidationException emptyName() {
        return EMPTY_NAME; // Reuse instance
    }
}
```

2. **Use static factory methods**
```java
public class UserNotFoundException extends Exception {
    public static UserNotFoundException withId(Long id) {
        return new UserNotFoundException("User not found with id: " + id);
    }
}
```

3. **Lazy stack trace filling**
```java
// Stack trace is filled lazily
Exception e = new Exception("Message");
// Stack trace not filled until printed
e.printStackTrace(); // Now filled
```

## 15. Best Practices

### Custom Exception Guidelines

1. **Follow Naming Conventions**
```java
// Good - descriptive names
ValidationException
ResourceNotFoundException
InsufficientFundsException

// Bad - generic names
AppException
MyException
CustomError
```

2. **Provide Constructors**
```java
public class CustomException extends Exception {
    // Message only
    public CustomException(String message) {
        super(message);
    }
    
    // Message + cause
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Cause only
    public CustomException(Throwable cause) {
        super(cause);
    }
}
```

3. **Add Contextual Information**
```java
public class ValidationException extends RuntimeException {
    private final String fieldName;
    private final Object invalidValue;
    
    public ValidationException(String fieldName, Object invalidValue, String message) {
        super(message);
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }
}
```

4. **Override toString()**
```java
@Override
public String toString() {
    return String.format("%s{code='%s', message='%s', context=%s}", 
        getClass().getSimpleName(), errorCode, getMessage(), context);
}
```

5. **Document Custom Exceptions**
```java
/**
 * Thrown when a user account is not found.
 * 
 * <p>This exception is thrown when attempting to access or modify
 * a user account that does not exist in the system.</p>
 * 
 * <p>Recovery suggestion: Verify the user ID or create a new account.</p>
 */
public class UserNotFoundException extends Exception {
    // Implementation
}
```

## 16. Common Mistakes

### Mistake 1: Too Many Constructors

```java
// Bad - unnecessary constructors
public class CustomException extends Exception {
    public CustomException() { super(); }
    public CustomException(String message) { super(message); }
    public CustomException(String message, Throwable cause) { super(message, cause); }
    public CustomException(Throwable cause) { super(cause); }
    public CustomException(String message, Throwable cause, boolean enableSuppression, 
                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

// Good - only needed constructors
public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }
    
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### Mistake 2: Not Preserving Cause

```java
// Bad - loses original exception
try {
    riskyOperation();
} catch (Exception e) {
    throw new CustomException("Failed"); // Cause lost!
}

// Good - preserves cause
try {
    riskyOperation();
} catch (Exception e) {
    throw new CustomException("Failed", e); // Cause preserved
}
```

### Mistake 3: Throwing Custom Exceptions Incorrectly

```java
// Bad - throwing checked exception from lambda
list.forEach(item -> {
    process(item); // If process throws checked exception
});

// Good - wrap in unchecked
list.forEach(item -> {
    try {
        process(item);
    } catch (CustomCheckedException e) {
        throw new RuntimeException(e);
    }
});
```

## 17. Pitfalls

### Pitfall 1: Exception Hierarchy Too Deep

```java
// Bad - too many levels
BaseException
├── SystemException
│   ├── DatabaseException
│   │   ├── ConnectionException
│   │   ├── QueryException
│   │   └── TransactionException
│   └── NetworkException
│       ├── TimeoutException
│       └── ConnectionException

// Good - reasonable depth
BaseException
├── DatabaseException
├── NetworkException
└── ValidationException
```

### Pitfall 2: Custom Exceptions Without Context

```java
// Bad - no useful information
public class ProcessingException extends Exception {
    public ProcessingException(String message) {
        super(message);
    }
}

// Good - includes context
public class ProcessingException extends Exception {
    private final String itemId;
    private final String operation;
    
    public ProcessingException(String itemId, String operation, String message, Throwable cause) {
        super(message, cause);
        this.itemId = itemId;
        this.operation = operation;
    }
}
```

### Pitfall 3: Not Using Exception Hierarchy

```java
// Bad - all exceptions same type
throw new Exception("Validation failed");
throw new Exception("Not found");
throw new Exception("Permission denied");

// Good - different types for different errors
throw new ValidationException("Invalid email");
throw new ResourceNotFoundException("User", userId);
throw new AccessDeniedException("Insufficient permissions");
```

## 18. Debugging Tips

### Debugging Custom Exceptions

1. **Add Logging in Constructors**
```java
public class CustomException extends Exception {
    private static final Logger logger = Logger.getLogger(CustomException.class.getName());
    
    public CustomException(String message) {
        super(message);
        logger.fine("CustomException created: " + message);
    }
}
```

2. **Use Exception Filters**
```java
// In IDE, set exception breakpoint
// Condition: exception instanceof CustomException
```

3. **Print Full Stack Trace**
```java
catch (CustomException e) {
    e.printStackTrace(); // Full stack trace
    // Or use logger
    logger.error("Custom exception", e);
}
```

4. **Check Exception Context**
```java
catch (CustomException e) {
    System.out.println("Error code: " + e.getErrorCode());
    System.out.println("Context: " + e.getContext());
    System.out.println("Recovery: " + e.getRecoverySuggestion());
}
```

## 19. Comparison Table

### Custom Exception Types

| Type | Extends | Declaration | Use Case | Example |
|------|---------|-------------|----------|---------|
| Checked | Exception | Required | Recoverable | IOException |
| Unchecked | RuntimeException | Optional | Programming | IllegalArgumentException |
| Error | Error | Optional | System | OutOfMemoryError |

### Exception Information

| Information | Purpose | When to Include |
|-------------|---------|-----------------|
| Message | Human-readable description | Always |
| Error Code | Programmatic handling | Always |
| Cause | Original exception | When wrapping |
| Context | Relevant data | When available |
| Recovery | Suggestion for fix | When known |

## 20. Decision Tree

### When to Create Custom Exceptions

```
Does existing exception fit?
├── Yes → Use existing exception
└── No
    ├── Is it recoverable?
    │   ├── Yes → Create checked custom exception
    │   └── No → Create unchecked custom exception
    ├── Does it need special handling?
    │   ├── Yes → Create custom exception with methods
    │   └── No → Use generic custom exception
    └── Is it domain-specific?
        ├── Yes → Create domain exception
        └── No → Use generic exception
```

### Exception Hierarchy Design

```
What's the base category?
├── Validation → ValidationException
├── Resource → ResourceException
├── Security → SecurityException
└. System → SystemException
    └── Subcategories as needed
```

## 21. Interview Questions

### Q1: When should you create custom exceptions?

**Answer:**
When:
- You need to distinguish between different error types
- You want to add domain-specific information
- You need custom behavior (logging, recovery)
- Standard exceptions don't fit your domain

### Q2: Checked vs unchecked custom exceptions?

**Answer:**
- Checked: Recoverable conditions, caller must handle
- Unchecked: Programming errors, optional handling

### Q3: How to design an exception hierarchy?

**Answer:**
- Start with base exception class
- Create subclasses for different error categories
- Keep hierarchy shallow (2-3 levels max)
- Include useful methods and fields

### Q4: What information should custom exceptions include?

**Answer:**
- Message (human-readable)
- Error code (programmatic)
- Cause (original exception)
- Context (relevant data)
- Recovery suggestion (if known)

### Q5: How to make custom exceptions serializable?

**Answer:**
Extend Exception (which implements Serializable). Add serialVersionUID if needed:
```java
public class CustomException extends Exception {
    private static final long serialVersionUID = 1L;
}
```

## 22. Exercises

### Exercise 1: Create Exception Hierarchy

Create a custom exception hierarchy for an e-commerce system with:
- Base exception
- Validation exceptions
- Resource exceptions
- Business rule exceptions

### Exercise 2: Add Context Information

Enhance your exceptions with:
- Error codes
- Timestamps
- Context maps
- Recovery suggestions

### Exercise 3: Exception Factory

Create an exception factory that:
- Provides static factory methods
- Creates exceptions with consistent context
- Supports exception chaining

## 23. Assignments

### Assignment 1: Library Management System

Create custom exceptions for:
- Book not found
- Member not found
- Overdue fine
- Reservation conflict

### Assignment 2: Banking System

Create custom exceptions for:
- Insufficient funds
- Account not found
- Invalid transaction
- Security violation

### Assignment 3: API Framework

Create custom exceptions for:
- Validation errors
- Authentication errors
- Authorization errors
- Rate limiting

## 24. Mini Project

### Custom Exception Framework

Create a framework that:
1. Provides base exception classes
2. Includes exception builders
3. Supports exception factories
4. Handles exception chaining
5. Generates exception documentation

## 25. Summary

### Key Takeaways

- Custom exceptions provide domain-specific error handling
- Design clear exception hierarchies
- Include useful context information
- Use static factory methods for common exceptions
- Override toString() for better debugging
- Document exceptions in Javadoc
- Don't over-engineer exception hierarchies
- Consider both checked and unchecked exceptions

### Design Checklist

- [ ] Clear, descriptive names
- [ ] Appropriate constructors
- [ ] Contextual information
- [ ] Error codes (if needed)
- [ ] Recovery suggestions
- [ ] toString() override
- [ ] Javadoc documentation
- [ ] Proper serialization (if needed)

## 26. References

### Official Documentation
- [Java SE Custom Exceptions](https://docs.oracle.com/en/java/javase/21/essential/exceptions/)
- [Java SE Serializable](https://docs.oracle.com/en/java/javase/21/essential/io/objectstreams.html)

### Books
- "Effective Java" by Joshua Bloch
- "Clean Code" by Robert Martin

### Online Resources
- [Baeldung - Custom Exceptions](https://www.baeldung.com/java-custom-exceptions)
- [Baeldung - Exception Handling](https://www.baeldung.com/java-exceptions)

## 27. Next Steps

Now that you understand custom exceptions, proceed to:
- **07-best-practices**: Learn about exception handling best practices
