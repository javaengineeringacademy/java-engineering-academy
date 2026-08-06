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

## 📑 Continue Reading

**Part 1** of 3 | [Part 2](README-part2.md) | [Part 3](README-part3.md)

