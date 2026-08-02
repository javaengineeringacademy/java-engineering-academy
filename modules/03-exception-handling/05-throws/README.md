# Throws Keyword

## 1. Introduction

The `throws` keyword in Java is used to declare that a method might抛出 one or more exceptions. It serves as a contract between the method and its callers, indicating what exceptions can be expected and should be handled. This lesson covers the proper use of throws, exception declaration, and how it interacts with method overriding and inheritance.

## 2. Learning Objectives

By the end of this lesson, you will be able to:

- Use the throws keyword correctly in method signatures
- Understand exception declaration requirements
- Implement proper exception propagation
- Handle method overriding with throws
- Apply throws in inheritance hierarchies
- Follow throws best practices
- Debug throws-related issues

## 3. Prerequisites

- Understanding of throw keyword
- Knowledge of checked vs unchecked exceptions
- Familiarity with method signatures
- Basic OOP concepts including inheritance

## 4. Why This Concept Exists

### The Problem

Without explicit declaration, callers don't know what exceptions to expect:

```java
// Caller has no idea what might go wrong
public void processFile(String filename) {
    // Does this throw any exceptions? Unknown!
    readFile(filename);
    parseData();
    saveResults();
}
```

### The Solution

Throws declares expected exceptions:

```java
// Clear contract - caller knows what to handle
public void processFile(String filename) 
        throws FileNotFoundException, ParseException, IOException {
    readFile(filename);
    parseData();
    saveResults();
}
```

## 5. Problem Statement

### Challenge 1: Exception Declaration

How do you declare that a method might抛出 exceptions?

### Challenge 2: Method Overriding

How do you handle throws when overriding methods in subclasses?

### Challenge 3: Exception Propagation

How do you declare exceptions when calling methods that throw?

### Challenge 4: Interface Contracts

How do you define exception contracts in interfaces?

## 6. Theory

### Throws Syntax

```java
public void method() throws ExceptionType1, ExceptionType2 {
    // Method body
}
```

### Checked vs Unchecked

- **Checked exceptions**: Must be declared or caught
- **Unchecked exceptions**: Don't need to be declared (but can be)
- **Errors**: Should not be declared (JVM handles them)

### Exception Propagation

When a method throws:
1. Exception is created
2. Method terminates
3. Exception propagates to caller
4. Caller must handle or declare

### Method Overriding Rules

When overriding a method:
- Can declare fewer exceptions than parent
- Can declare same exceptions as parent
- Can declare subclasses of parent's exceptions
- Cannot declare new checked exceptions
- Cannot declare broader exceptions

## 7. Internal Working

### Bytecode Implementation

The throws clause is encoded in the method's `Exceptions` attribute in the class file:

```
Exceptions:
  #1 = Class java/io/IOException
  #2 = Class java/sql/SQLException
```

### JVM Behavior

1. JVM checks the exception table for the current method
2. If no handler found, checks the throws clause
3. If exception is declared, propagates to caller
4. If not declared, wraps in RuntimeException or Error

## 8. JVM Perspective

### Exception Attribute

The `Exceptions` attribute in the class file contains:
- Number of exceptions
- Array of exception class indices

### Method Resolution

When calling a method:
1. JVM checks if the method's throws clause allows the exception
2. If calling code doesn't handle it, propagates up
3. If no handler found, invokes uncaught exception handler

## 9. Memory Representation

### Throws in Method Metadata

```
Method Object
├── Name
├── Descriptor
├── Access Flags
├── Attributes
│   ├── Code (bytecode)
│   ├── Exceptions (throws clause)
│   │   ├── Count
│   │   └── Exception indices
│   └── Other attributes
```

### Stack Frame During Propagation

```
Stack Frame
├── Local Variables
├.Operand Stack
├. Return Address
└. Reference to calling method
```

## 10. Syntax

### Basic Throws

```java
public void method() throws IOException {
    // Method body
}
```

### Multiple Exceptions

```java
public void method() throws IOException, SQLException {
    // Method body
}
```

### With Try-Catch

```java
public void method() throws IOException {
    try {
        riskyOperation();
    } catch (IOException e) {
        // Handle or rethrow
        throw e;
    }
}
```

### Constructor Throws

```java
public class MyClass {
    public MyClass() throws IOException {
        // Constructor body
    }
}
```

## 11. Easy Example

### Basic Throws Usage

```java
import java.io.*;

public class BasicThrows {
    public static void main(String[] args) {
        try {
            readFile("test.txt");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    static void readFile(String filename) throws IOException {
        FileReader reader = new FileReader(filename);
        // Process file
        reader.close();
    }
}
```

### Throws with Multiple Exceptions

```java
import java.io.*;
import java.sql.*;

public class MultipleThrows {
    public static void main(String[] args) {
        try {
            processData();
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    
    static void processData() throws IOException, SQLException {
        readData();
        saveToDatabase();
    }
    
    static void readData() throws IOException {
        // Implementation
    }
    
    static void saveToDatabase() throws SQLException {
        // Implementation
    }
}
```

## 12. Medium Example

### Method Overriding with Throws

```java
import java.io.*;

public class ThrowsInheritance {
    
    static class Parent {
        // Parent declares multiple exceptions
        public void process() throws IOException, SQLException {
            // Implementation
        }
        
        public void readData() throws IOException {
            // Implementation
        }
    }
    
    static class Child extends Parent {
        // Can declare same or fewer exceptions
        @Override
        public void process() throws IOException {
            // Only declares IOException - valid
        }
        
        // Can declare subclass of parent's exception
        @Override
        public void readData() throws FileNotFoundException {
            // FileNotFoundException is subclass of IOException - valid
        }
        
        // INVALID - cannot declare new checked exception
        // @Override
        // public void process() throws ClassNotFoundException {
        //     // This would be a compile error
        // }
    }
    
    public static void main(String[] args) {
        Parent p = new Child();
        try {
            p.process();
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }
    }
}
```

### Interface with Throws

```java
import java.io.*;

public interface DataProcessor {
    // Interface method declares exceptions
    void processData(String data) throws InvalidDataException, IOException;
    
    // Default method can also throw
    default void processDataWithRetry(String data, int retries) 
            throws InvalidDataException, IOException {
        for (int i = 0; i < retries; i++) {
            try {
                processData(data);
                return;
            } catch (IOException e) {
                if (i == retries - 1) {
                    throw e;
                }
                System.out.println("Retry " + (i + 1) + " failed");
            }
        }
    }
    
    static class InvalidDataException extends Exception {
        InvalidDataException(String message) {
            super(message);
        }
    }
    
    // Implementation
    static class CSVProcessor implements DataProcessor {
        @Override
        public void processData(String data) 
                throws InvalidDataException, IOException {
            if (data == null || data.isEmpty()) {
                throw new InvalidDataException("Data cannot be empty");
            }
            // Process CSV data
            System.out.println("Processing: " + data);
        }
    }
    
    public static void main(String[] args) {
        DataProcessor processor = new CSVProcessor();
        try {
            processor.processData("test,data");
            processor.processData("");
        } catch (InvalidDataException e) {
            System.out.println("Invalid data: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
    }
}
```

## 13. Hard Example

### Exception Declaration Pattern

```java
import java.io.*;
import java.util.*;
import java.util.function.*;

public class ExceptionDeclarationPatterns {
    
    // Pattern 1: Exception hierarchy for different failure modes
    interface ServiceOperation<T> {
        T execute() throws ServiceException;
    }
    
    static class ServiceException extends Exception {
        private final ErrorCode code;
        
        ServiceException(String message, ErrorCode code) {
            super(message);
            this.code = code;
        }
        
        ServiceException(String message, ErrorCode code, Throwable cause) {
            super(message, cause);
            this.code = code;
        }
        
        ErrorCode getCode() { return code; }
    }
    
    enum ErrorCode {
        VALIDATION_FAILED,
        NOT_FOUND,
        CONFLICT,
        INTERNAL_ERROR
    }
    
    // Pattern 2: Builder for exception declaration
    static class ExceptionBuilder {
        private final List<Class<? extends Exception>> exceptions;
        
        ExceptionBuilder() {
            this.exceptions = new ArrayList<>();
        }
        
        ExceptionBuilder add(Class<? extends Exception> exceptionClass) {
            exceptions.add(exceptionClass);
            return this;
        }
        
        String buildThrowsClause() {
            return exceptions.stream()
                .map(Class::getSimpleName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Exception");
        }
        
        List<Class<? extends Exception>> getExceptions() {
            return Collections.unmodifiableList(exceptions);
        }
    }
    
    // Pattern 3: Exception wrapper for functional interfaces
    @FunctionalInterface
    interface ThrowingRunnable {
        void run() throws Exception;
    }
    
    static Runnable wrap(ThrowingRunnable throwingRunnable) {
        return () -> {
            try {
                throwingRunnable.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
    
    // Pattern 4: Checked exception handling in streams
    static <T> Consumer<T> checkedConsumer(ThrowingConsumer<T> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
    
    @FunctionalInterface
    interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }
    
    // Usage examples
    public static void main(String[] args) {
        // Pattern 1 usage
        ServiceOperation<String> operation = () -> {
            if (Math.random() > 0.5) {
                throw new ServiceException("Random failure", ErrorCode.INTERNAL_ERROR);
            }
            return "Success";
        };
        
        try {
            String result = operation.execute();
            System.out.println(result);
        } catch (ServiceException e) {
            System.out.println("Error: " + e.getMessage() + " [" + e.getCode() + "]");
        }
        
        // Pattern 2 usage
        ExceptionBuilder builder = new ExceptionBuilder()
            .add(IOException.class)
            .add(SQLException.class);
        System.out.println("Throws: " + builder.buildThrowsClause());
        
        // Pattern 3 usage
        List<String> files = Arrays.asList("file1.txt", "file2.txt");
        files.forEach(wrap(file -> {
            // This can throw checked exceptions
            new FileInputStream(file);
        }));
        
        // Pattern 4 usage
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        names.stream()
            .forEach(checkedConsumer(name -> {
                if (name.length() < 3) {
                    throw new IllegalArgumentException("Name too short: " + name);
                }
                System.out.println("Valid name: " + name);
            }));
    }
}
```

### Generic Exception Handling

```java
import java.util.*;
import java.util.function.*;

public class GenericExceptionHandling {
    
    // Generic exception handler
    static <T, E extends Exception> T executeWithException(
            Supplier<T> operation, 
            Function<Exception, E> exceptionMapper) throws E {
        try {
            return operation.get();
        } catch (Exception e) {
            throw exceptionMapper.apply(e);
        }
    }
    
    // Generic retry mechanism
    static <T> T executeWithRetry(
            Supplier<T> operation,
            int maxRetries,
            long delayMs,
            Class<? extends Exception> retryableException) throws Exception {
        
        Exception lastException = null;
        
        for (int i = 0; i <= maxRetries; i++) {
            try {
                return operation.get();
            } catch (Exception e) {
                lastException = e;
                
                if (!retryableException.isInstance(e) || i == maxRetries) {
                    throw e;
                }
                
                Thread.sleep(delayMs * (i + 1));
            }
        }
        
        throw lastException;
    }
    
    // Generic exception handler with recovery
    static <T> T executeWithRecovery(
            Supplier<T> operation,
            Function<Exception, T> recoveryFunction) {
        try {
            return operation.get();
        } catch (Exception e) {
            return recoveryFunction.apply(e);
        }
    }
    
    // Usage
    public static void main(String[] args) {
        // Execute with exception mapping
        try {
            String result = executeWithException(
                () -> {
                    if (Math.random() > 0.5) {
                        throw new RuntimeException("Random failure");
                    }
                    return "Success";
                },
                e -> new CustomException("Mapped: " + e.getMessage(), e)
            );
            System.out.println(result);
        } catch (CustomException e) {
            System.out.println("Custom error: " + e.getMessage());
        }
        
        // Execute with retry
        try {
            String result = executeWithRetry(
                () -> {
                    if (Math.random() > 0.7) {
                        throw new RuntimeException("Transient failure");
                    }
                    return "Success";
                },
                3, 1000, RuntimeException.class
            );
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Failed after retries: " + e.getMessage());
        }
        
        // Execute with recovery
        String result = executeWithRecovery(
            () -> {
                if (Math.random() > 0.5) {
                    throw new RuntimeException("Failure");
                }
                return "Success";
            },
            e -> "Default value due to: " + e.getMessage()
        );
        System.out.println(result);
    }
    
    static class CustomException extends Exception {
        CustomException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
```

## 14. Performance

### Throws Performance

**Compile-time:**
- Minimal overhead
- Just metadata in class file

**Runtime:**
- No overhead until exception thrown
- Then same as throw performance

### Best Practices

1. **Declare only necessary exceptions**
```java
// Bad - declares too many
public void process() throws Exception {
    // Only throws IOException
}

// Good - declares only what's needed
public void process() throws IOException {
    // Only throws IOException
}
```

2. **Use exception hierarchy**
```java
// Bad - declares multiple specific exceptions
public void process() throws FileNotFoundException, IOException, MalformedURLException {
    // All are IOExceptions
}

// Good - uses parent exception
public void process() throws IOException {
    // Callers can catch specific or general
}
```

3. **Don't declare unchecked exceptions**
```java
// Bad - unnecessary declaration
public void process() throws IllegalArgumentException {
    // Unchecked - don't need to declare
}

// Good - no declaration needed
public void process() {
    // Unchecked exceptions don't need declaration
}
```

## 15. Best Practices

### Throws Guidelines

1. **Be Specific**
```java
// Bad
public void process() throws Exception;

// Good
public void process() throws IOException;
```

2. **Use Exception Hierarchy**
```java
// Bad
public void process() throws FileNotFoundException, SocketException;

// Good
public void process() throws IOException;
```

3. **Document Throws**
```java
/**
 * Reads data from file.
 * @param filename the file to read
 * @return the file contents
 * @throws FileNotFoundException if file doesn't exist
 * @throws IOException if read fails
 */
public String readData(String filename) throws FileNotFoundException, IOException {
    // Implementation
}
```

4. **Consider Custom Exceptions**
```java
// For domain-specific errors
public void transferMoney(Account from, Account to, BigDecimal amount) 
        throws InsufficientFundsException, AccountNotFoundException {
    // Implementation
}
```

5. **Don't Declare Exceptions You Catch**
```java
// Bad
public void process() throws IOException {
    try {
        riskyOperation();
    } catch (IOException e) {
        // Handle it - don't redeclare
        log(e);
    }
}
```

## 16. Common Mistakes

### Mistake 1: Declaring Unchecked Exceptions

```java
// Bad - unnecessary
public void process() throws NullPointerException {
    // Unchecked - no need to declare
}

// Good
public void process() {
    // Unchecked exceptions don't need declaration
}
```

### Mistake 2: Declaring Too Many Exceptions

```java
// Bad
public void process() throws IOException, SQLException, ClassNotFoundException {
    // Use hierarchy instead
}

// Good
public void process() throws Exception {
    // Or use specific exception if possible
}
```

### Mistake 3: Not Declaring Checked Exceptions

```java
// Bad - compile error
public void process() {
    new FileReader("file.txt"); // FileNotFoundException not declared
}

// Good
public void process() throws FileNotFoundException {
    new FileReader("file.txt");
}
```

## 17. Pitfalls

### Pitfall 1: Throws in Interface

```java
// Bad - forces all implementations to handle
interface Service {
    void process() throws IOException;
}

// Better - let implementations decide
interface Service {
    void process();
}
```

### Pitfall 2: Throws with Generics

```java
// Can't throw checked exceptions from generic methods
// Bad
public <T> T process(Supplier<T> supplier) throws Exception {
    return supplier.get(); // Exception not declared
}

// Good
public <T> T process(Supplier<T> supplier) {
    try {
        return supplier.get();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```

### Pitfall 3: Throws with Lambdas

```java
// Can't throw checked exceptions from lambdas
// Bad
list.forEach(item -> {
    process(item); // If process throws checked exception
});

// Good
list.forEach(item -> {
    try {
        process(item);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
});
```

## 18. Debugging Tips

### Debugging Throws Issues

1. **Check Method Signature**
```java
// Verify what exceptions are declared
Method method = clazz.getMethod("process");
Class<?>[] exceptions = method.getExceptionTypes();
```

2. **Trace Exception Propagation**
```java
try {
    method();
} catch (Exception e) {
    e.printStackTrace();
    // Check stack trace for propagation path
}
```

3. **Verify Override Compatibility**
```java
// Check if override declares compatible exceptions
@Override
public void process() throws IOException {
    // Must be same or narrower than parent
}
```

## 19. Comparison Table

### Throws vs Try-Catch

| Aspect | Throws | Try-Catch |
|--------|--------|-----------|
| Location | Method signature | Method body |
| Purpose | Declare exceptions | Handle exceptions |
| Propagation | Defers to caller | Handles locally |
| Inheritance | Inherited | Not inherited |
| Multiple | Multiple declarations | Multiple catch blocks |

### Exception Declaration Approaches

| Approach | Pros | Cons | When to Use |
|----------|------|------|-------------|
| Specific exceptions | Clear contract | Verbose | API design |
| Parent exception | Concise | Less specific | Internal code |
| No declaration | Flexible | Caller unaware | Unchecked only |
| Custom exceptions | Domain-specific | More classes | Business logic |

## 20. Decision Tree

### When to Declare Exceptions

```
Does method throw checked exceptions?
├── Yes
│   ├── Are they recoverable?
│   │   ├── Yes → Declare them
│   │   └── No → Consider wrapping in RuntimeException
│   └── Are they from same hierarchy?
│       ├── Yes → Declare parent
│       └── No → Declare each
└── No
    └── Does it throw unchecked exceptions?
        ├── Yes → No declaration needed
        └── No → No throws clause needed
```

## 21. Interview Questions

### Q1: What is the difference between throw and throws?

**Answer:**
- `throw` is used in method body to actually抛出 an exception
- `throws` is used in method signature to declare exceptions

### Q2: Can you override a method with broader throws?

**Answer:**
No. Overriding methods can declare the same exceptions, subclasses, or no exceptions. They cannot declare new or broader checked exceptions.

### Q3: Do you need to declare unchecked exceptions?

**Answer:**
No. Unchecked exceptions (RuntimeException and its subclasses) don't need to be declared. You can declare them, but it's not required.

### Q4: Can a constructor throw exceptions?

**Answer:**
Yes. Constructors can throw checked exceptions. This is useful when object initialization can fail.

### Q5: What happens if you don't declare or catch a checked exception?

**Answer:**
Compile-time error. Java requires checked exceptions to be either caught or declared.

## 22. Exercises

### Exercise 1: Exception Declaration

Create a class with methods that:
- Declare single checked exceptions
- Declare multiple checked exceptions
- Declare no exceptions (unchecked only)
- Override methods with compatible throws

### Exercise 2: Interface Design

Design an interface with:
- Methods that declare exceptions
- Default methods with throws
- Generic methods with throws

### Exercise 3: Inheritance Hierarchy

Create an inheritance hierarchy where:
- Parent declares exceptions
- Child overrides with fewer exceptions
- Child overrides with subclass exceptions

## 23. Assignments

### Assignment 1: API Design

Create a service interface that:
- Declares appropriate exceptions
- Uses custom exception hierarchy
- Follows exception best practices

### Assignment 2: Exception Propagation

Build a system where:
- Exceptions propagate through layers
- Each layer adds context
- Final handler processes appropriately

### Assignment 3: Generic Exception Handler

Create a generic handler that:
- Works with different exception types
- Uses throws declarations properly
- Handles checked and unchecked exceptions

## 24. Mini Project

### Exception Declaration Framework

Create a framework that:
1. Validates exception declarations
2. Checks override compatibility
3. Generates throws clauses
4. Documents exception contracts
5. Validates exception propagation

## 25. Summary

### Key Takeaways

- Use throws to declare checked exceptions
- Be specific in exception declarations
- Use exception hierarchy for conciseness
- Don't declare unchecked exceptions
- Document throws in Javadoc
- Verify override compatibility
- Consider custom exceptions for domain errors
- Don't declare exceptions you catch

## 26. References

### Official Documentation
- [Java SE Throws](https://docs.oracle.com/javase/tutorial/essential/exceptions/declaring.html)
- [Java SE Method Overriding](https://docs.oracle.com/javase/tutorial/java/IandI/override.html)

### Books
- "Effective Java" by Joshua Bloch
- "Clean Code" by Robert Martin

### Online Resources
- [Baeldung - Java Throws](https://www.baeldung.com/java-exceptions)
- [Baeldung - Method Overriding](https://www.baeldung.com/java-method-overriding)

## 27. Next Steps

Now that you understand the throws keyword, proceed to:
- **06-custom-exceptions**: Learn about creating custom exception classes
