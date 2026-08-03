# Method Overloading

## Introduction

Method overloading is a powerful feature in Java that allows a class to have multiple methods with the same name but different parameter lists, enabling developers to create more intuitive and readable APIs where related operations share a common name but vary in their input parameters. This compile-time polymorphism (also known as static polymorphism or ad-hoc polymorphism) allows methods to be distinguished by their signature—the combination of method name and parameter types—rather than requiring unique names for each variation. Method overloading is resolved at compile time through a process called static binding, where the compiler determines which method to call based on the arguments provided at the call site. This mechanism enhances code readability by grouping related functionality under a single method name while maintaining type safety and enabling the compiler to catch parameter mismatches during compilation.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the concept of method overloading and how it differs from method overriding
- [ ] Implement overloaded methods with different parameter types, order, and count
- [ ] Recognize how the compiler resolves overloaded method calls through static binding
- [ ] Apply method overloading effectively to create clean, readable, and maintainable APIs

## Prerequisites

- [05-methods](../05-methods/README.md) - Understanding method declaration, parameters, and return types
- [02-classes](../02-classes/README.md) - Class structure and member organization
- [06-this-keyword](../06-this-keyword/README.md) - Using this to distinguish between instance variables and parameters
- [07-static-members](../07-static-members/README.md) - Understanding static context and method invocation

## Why This Concept Exists

### The Problem

In programming, you often need to perform similar operations with different types of input data. Without method overloading, you would need to create uniquely named methods for each variation:

```java
// Without overloading - verbose and confusing
class Calculator {
    public int addIntegers(int a, int b) { return a + b; }
    public double addDoubles(double a, double b) { return a + b; }
    public int addThreeIntegers(int a, int b, int c) { return a + b + c; }
    public String addStrings(String a, String b) { return a + b; }
}
```

This approach has several problems:

1. **Poor readability**: Method names become cluttered with type information
2. ** cognitive load**: Developers must remember multiple method names for similar operations
3. **API bloat**: Class interfaces become larger and harder to navigate
4. **Inconsistency**: Different developers might choose different naming conventions

### The Solution

Method overloading allows you to use the same method name for related operations:

```java
// With overloading - clean and intuitive
class Calculator {
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
    public String add(String a, String b) { return a + b; }
}
```

This provides:

- **Intuitive API**: Related operations share a common name
- **Reduced cognitive load**: Developers only need to remember one method name
- **Type safety**: The compiler selects the appropriate overload based on arguments
- **Cleaner code**: Method calls are more concise and readable

### Real-World Analogy

Think of method overloading as the word **"open"** in everyday language. You can:

- **Open** a door (requires a door object)
- **Open** a file (requires a file path)
- **Open** a conversation (requires a topic)
- **Open** a bank account (requires account details)

The word "open" remains the same, but what you're opening and what parameters you need change based on context. Similarly, overloaded methods share the same name but adapt their behavior based on the parameters provided.

## Internal Working

### JVM Perspective

Method overloading is resolved entirely at compile time through static binding:

1. **Compilation Phase**: The compiler analyzes each method call and matches it to the most specific overload based on:
   - Number of parameters
   - Types of parameters
   - Order of parameters

2. **Method Signature**: Each overloaded method has a unique signature consisting of:
   - Method name
   - Parameter types in order
   - (Return type is NOT part of the signature)

3. **Static Binding**: The compiler generates bytecode that directly invokes the resolved method, with no runtime overhead.

4. **Overload Resolution Algorithm**: Java uses a specific algorithm to resolve overloaded methods:
   - Phase 1: Match without allowing widening
   - Phase 2: Match allowing widening
   - Phase 3: Match allowing autoboxing and varargs

### Memory Representation

Method overloading does not create additional memory overhead at runtime:

```
Class Metadata in Method Area:
┌─────────────────────────────────────┐
│ Calculator Class                    │
├─────────────────────────────────────┤
│ Method Table:                       │
│ ├── add(int, int) → bytecode offset│
│ ├── add(double, double) → offset   │
│ ├── add(int, int, int) → offset    │
│ └── add(String, String) → offset   │
└─────────────────────────────────────┘

At Compile Time:
Method Call: calc.add(5, 10)
↓
Compiler resolves: add(int, int)
↓
Direct invocation at runtime
```

### Method Resolution Order

The compiler uses this priority for overload resolution:

1. **Exact match**: Method with exact parameter types
2. **Widening**: Method with widened parameter types (int → long)
3. **Autoboxing**: Method with autoboxed types (int → Integer)
4. **Varargs**: Method with variable arguments

## Syntax

### Basic Method Overloading

```java
class Calculator {
    // Overloaded methods with different parameter types
    public int add(int a, int b) {
        return a + b;
    }

    public double add(double a, double b) {
        return a + b;
    }

    public String add(String a, String b) {
        return a + b;
    }
}
```

### Overloading with Different Parameter Count

```java
class Printer {
    public void print(String message) {
        System.out.println(message);
    }

    public void print(String message, int copies) {
        for (int i = 0; i < copies; i++) {
            System.out.println(message);
        }
    }

    public void print(String message, int copies, boolean numbered) {
        for (int i = 0; i < copies; i++) {
            if (numbered) {
                System.out.println((i + 1) + ": " + message);
            } else {
                System.out.println(message);
            }
        }
    }
}
```

### Overloading with Different Parameter Order

```java
class Formatter {
    public String format(String template, String value) {
        return template.replace("{}", value);
    }

    public String format(String value, String template) {
        return template.replace("{}", value);
    }
}
```

### Static Methods Can Be Overloaded

```java
class MathUtils {
    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static int max(int a, int b, int c) {
        return max(max(a, b), c);
    }

    public static double max(double a, double b) {
        return a > b ? a : b;
    }
}
```

## Easy Examples

### Example 1: String Utility Class

**Problem Statement**: Create a utility class that provides overloaded methods for common string operations, allowing flexible input parameters while maintaining a clean API.

**Implementation**:

```java
package academy.javaengineering.oop.methodoverloading;

class StringUtils {
    // Repeat a string n times
    public static String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    // Repeat with delimiter
    public static String repeat(String str, int times, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
            if (i < times - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    // Pad string to length with spaces
    public static String padLeft(String str, int length) {
        return padLeft(str, length, ' ');
    }

    // Pad string to length with specified character
    public static String padLeft(String str, int length, char padChar) {
        if (str.length() >= length) {
            return str;
        }
        int padding = length - str.length();
        return repeat(String.valueOf(padChar), padding) + str;
    }

    // Check if string contains substring (case sensitive)
    public static boolean contains(String str, String substring) {
        return str.contains(substring);
    }

    // Check if string contains substring (optionally case insensitive)
    public static boolean contains(String str, String substring, boolean ignoreCase) {
        if (ignoreCase) {
            return str.toLowerCase().contains(substring.toLowerCase());
        }
        return str.contains(substring);
    }
}

public class StringUtilsDemo {
    public static void main(String[] args) {
        System.out.println("=== String Repeat ===");
        System.out.println("repeat(\"ha\", 3): " + StringUtils.repeat("ha", 3));
        System.out.println("repeat(\"ha\", 3, \"-\"): " + StringUtils.repeat("ha", 3, "-"));
        System.out.println("repeat(\"*\", 5): " + StringUtils.repeat("*", 5));

        System.out.println("\n=== String Pad ===");
        System.out.println("padLeft(\"42\", 5): '" + StringUtils.padLeft("42", 5) + "'");
        System.out.println("padLeft(\"42\", 5, '0'): '" + StringUtils.padLeft("42", 5, '0') + "'");
        System.out.println("padLeft(\"hello\", 3): '" + StringUtils.padLeft("hello", 3) + "'");

        System.out.println("\n=== String Contains ===");
        String text = "Hello World";
        System.out.println("contains(\"Hello\", \"ell\"): " + StringUtils.contains(text, "ell"));
        System.out.println("contains(\"Hello\", \"ELL\"): " + StringUtils.contains(text, "ELL"));
        System.out.println("contains(\"Hello\", \"ELL\", true): " + StringUtils.contains(text, "ELL", true));
    }
}
```

**Expected Output**:
```
=== String Repeat ===
repeat("ha", 3): hahaha
repeat("ha", 3, "-"): ha-ha-ha
repeat("*", 5): *****

=== String Pad ===
padLeft("42", 5): '   42'
padLeft("42", 5, '0'): '00042'
padLeft("hello", 3): 'hello'

=== String Contains ===
contains("Hello", "ell"): true
contains("Hello", "ELL"): false
contains("Hello", "ELL", true): true
```

**Best Practices**:
- Use method overloading to provide convenient defaults
- Ensure overloaded methods have clear, distinct parameter signatures
- Document the behavior of each overload
- Consider using builder patterns for complex scenarios with many parameters

### Example 2: Number Parser

**Problem Statement**: Create a flexible number parsing utility that can handle different input types and parsing options through overloaded methods.

**Implementation**:

```java
package academy.javaengineering.oop.methodoverloading;

class NumberParser {
    // Parse string to integer
    public static int parseInt(String str) {
        return Integer.parseInt(str.trim());
    }

    // Parse string to integer with default value
    public static int parseInt(String str, int defaultValue) {
        try {
            return parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Parse string to double
    public static double parseDouble(String str) {
        return Double.parseDouble(str.trim());
    }

    // Parse string to double with default value
    public static double parseDouble(String str, double defaultValue) {
        try {
            return parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Parse string to long
    public static long parseLong(String str) {
        return Long.parseLong(str.trim());
    }

    // Parse string to long with radix
    public static long parseLong(String str, int radix) {
        return Long.parseLong(str.trim(), radix);
    }

    // Parse array of strings to array of integers
    public static int[] parseIntArray(String[] strings) {
        int[] result = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = parseInt(strings[i]);
        }
        return result;
    }

    // Parse comma-separated string to array of integers
    public static int[] parseIntArray(String csv) {
        String[] parts = csv.split(",");
        return parseIntArray(parts);
    }
}

public class NumberParserDemo {
    public static void main(String[] args) {
        System.out.println("=== Integer Parsing ===");
        System.out.println("parseInt(\"42\"): " + NumberParser.parseInt("42"));
        System.out.println("parseInt(\"abc\", 0): " + NumberParser.parseInt("abc", 0));
        System.out.println("parseInt(\"  100  \"): " + NumberParser.parseInt("  100  "));

        System.out.println("\n=== Double Parsing ===");
        System.out.println("parseDouble(\"3.14\"): " + NumberParser.parseDouble("3.14"));
        System.out.println("parseDouble(\"abc\", 1.0): " + NumberParser.parseDouble("abc", 1.0));

        System.out.println("\n=== Long Parsing ===");
        System.out.println("parseLong(\"1234567890\"): " + NumberParser.parseLong("1234567890"));
        System.out.println("parseLong(\"ff\", 16): " + NumberParser.parseLong("ff", 16));

        System.out.println("\n=== Array Parsing ===");
        String[] numbers = {"1", "2", "3", "4", "5"};
        int[] parsed = NumberParser.parseIntArray(numbers);
        System.out.println("parseIntArray(String[]): " + java.util.Arrays.toString(parsed));

        String csv = "10,20,30,40,50";
        int[] parsedCsv = NumberParser.parseIntArray(csv);
        System.out.println("parseIntArray(String): " + java.util.Arrays.toString(parsedCsv));
    }
}
```

**Expected Output**:
```
=== Integer Parsing ===
parseInt("42"): 42
parseInt("abc", 0): 0
parseInt("  100  "): 100

=== Double Parsing ===
parseDouble("3.14"): 3.14
parseDouble("abc", 1.0): 1.0

=== Long Parsing ===
parseLong("1234567890"): 1234567890
parseLong("ff", 16): 255

=== Array Parsing ===
parseIntArray(String[]): [1, 2, 3, 4, 5]
parseIntArray(String): [10, 20, 30, 40, 50]
```

**Best Practices**:
- Provide default values for failed parsing operations
- Handle exceptions gracefully in overloaded methods
- Maintain consistency in parameter order across overloads
- Consider using varargs for flexible input

### Example 3: Shape Area Calculator

**Problem Statement**: Create a shape area calculator that uses method overloading to calculate areas for different shapes with varying parameter requirements.

**Implementation**:

```java
package academy.javaengineering.oop.methodoverloading;

class ShapeCalculator {
    private static final double PI = Math.PI;

    // Area of circle
    public static double area(double radius) {
        return PI * radius * radius;
    }

    // Area of rectangle
    public static double area(double width, double height) {
        return width * height;
    }

    // Area of triangle (base and height)
    public static double area(double base, double height, boolean isTriangle) {
        if (isTriangle) {
            return 0.5 * base * height;
        }
        return base * height; // Rectangle fallback
    }

    // Area of triangle (three sides using Heron's formula)
    public static double triangleArea(double a, double b, double c) {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    // Area of regular polygon
    public static double area(int sides, double sideLength) {
        double perimeter = sides * sideLength;
        double apothem = sideLength / (2 * Math.tan(PI / sides));
        return 0.5 * perimeter * apothem;
    }

    // Circumference of circle
    public static double circumference(double radius) {
        return 2 * PI * radius;
    }

    // Perimeter of rectangle
    public static double perimeter(double width, double height) {
        return 2 * (width + height);
    }

    // Perimeter of regular polygon
    public static double perimeter(int sides, double sideLength) {
        return sides * sideLength;
    }
}

public class ShapeCalculatorDemo {
    public static void main(String[] args) {
        System.out.println("=== Circle ===");
        System.out.printf("Area (r=5): %.2f%n", ShapeCalculator.area(5.0));
        System.out.printf("Circumference (r=5): %.2f%n", ShapeCalculator.circumference(5.0));

        System.out.println("\n=== Rectangle ===");
        System.out.printf("Area (4x6): %.2f%n", ShapeCalculator.area(4.0, 6.0));
        System.out.printf("Perimeter (4x6): %.2f%n", ShapeCalculator.perimeter(4.0, 6.0));

        System.out.println("\n=== Triangle ===");
        System.out.printf("Area (base=10, height=5): %.2f%n",
            ShapeCalculator.area(10.0, 5.0, true));
        System.out.printf("Area (sides 3,4,5): %.2f%n",
            ShapeCalculator.triangleArea(3.0, 4.0, 5.0));

        System.out.println("\n=== Regular Pentagon ===");
        System.out.printf("Area (5 sides, side=4): %.2f%n",
            ShapeCalculator.area(5, 4.0));
        System.out.printf("Perimeter (5 sides, side=4): %.2f%n",
            ShapeCalculator.perimeter(5, 4.0));
    }
}
```

**Expected Output**:
```
=== Circle ===
Area (r=5): 78.54
Circumference (r=5): 31.42

=== Rectangle ===
Area (4x6): 24.00
Perimeter (4x6): 20.00

=== Triangle ===
Area (base=10, height=5): 25.00
Area (sides 3,4,5): 6.00

=== Regular Pentagon ===
Area (5 sides, side=4): 27.53
Perimeter (5 sides, side=4): 20.00
```

**Best Practices**:
- Use descriptive parameter names to clarify overloaded method behavior
- Provide alternative method names when overloading might cause confusion
- Consider using static factory methods for complex construction scenarios
- Document the mathematical formulas used in calculations

## Medium Examples

### Example 1: Logger with Multiple Overload Scenarios

**Problem Statement**: Design a logging system that uses method overloading to provide flexible logging options while maintaining a clean, consistent API.

**Requirements**:

- Support different log levels (INFO, WARN, ERROR)
- Allow logging with or without timestamps
- Support exception logging with stack traces
- Provide context-based logging

**Implementation**:

```java
package academy.javaengineering.oop.methodoverloading;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

enum LogLevel {
    DEBUG, INFO, WARN, ERROR
}

class LogEntry {
    private final LocalDateTime timestamp;
    private final LogLevel level;
    private final String message;
    private final Throwable exception;
    private final String context;

    public LogEntry(LocalDateTime timestamp, LogLevel level, String message,
                    Throwable exception, String context) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.exception = exception;
        this.context = context;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"))).append("]");
        sb.append(" [").append(level).append("]");
        if (context != null) {
            sb.append(" [").append(context).append("]");
        }
        sb.append(" ").append(message);
        if (exception != null) {
            sb.append(" - ").append(exception.getClass().getSimpleName());
            sb.append(": ").append(exception.getMessage());
        }
        return sb.toString();
    }
}

class Logger {
    private final List<LogEntry> entries;
    private final LogLevel minLevel;
    private final boolean includeTimestamp;

    public Logger() {
        this(LogLevel.DEBUG, true);
    }

    public Logger(LogLevel minLevel) {
        this(minLevel, true);
    }

    public Logger(LogLevel minLevel, boolean includeTimestamp) {
        this.entries = new ArrayList<>();
        this.minLevel = minLevel;
        this.includeTimestamp = includeTimestamp;
    }

    // Overloaded log methods for different scenarios

    // Basic logging
    public void log(LogLevel level, String message) {
        log(level, message, null, null);
    }

    // Logging with context
    public void log(LogLevel level, String message, String context) {
        log(level, message, null, context);
    }

    // Logging with exception
    public void log(LogLevel level, String message, Throwable exception) {
        log(level, message, exception, null);
    }

    // Full logging (all parameters)
    public void log(LogLevel level, String message, Throwable exception, String context) {
        if (level.ordinal() < minLevel.ordinal()) {
            return;
        }

        LocalDateTime timestamp = includeTimestamp ? LocalDateTime.now() : null;
        LogEntry entry = new LogEntry(timestamp, level, message, exception, context);
        entries.add(entry);
        System.out.println(entry);
    }

    // Convenience methods for common log levels
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void info(String message, String context) {
        log(LogLevel.INFO, message, context);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void warn(String message, Throwable exception) {
        log(LogLevel.WARN, message, exception);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void error(String message, Throwable exception) {
        log(LogLevel.ERROR, message, exception);
    }

    public void error(String message, Throwable exception, String context) {
        log(LogLevel.ERROR, message, exception, context);
    }

    public List<LogEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public int getEntryCount() {
        return entries.size();
    }
}

class UserService {
    private final Logger logger;

    public UserService(Logger logger) {
        this.logger = logger;
    }

    public void findUser(String userId) {
        logger.info("Finding user", "UserService");

        try {
            // Simulate user lookup
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }

            logger.info("User found: " + userId, "UserService");
        } catch (Exception e) {
            logger.error("Failed to find user: " + userId, e, "UserService");
        }
    }

    public void deleteUser(String userId) {
        logger.warn("Deleting user: " + userId, "UserService");

        try {
            // Simulate deletion
            if (Math.random() > 0.5) {
                throw new RuntimeException("Database connection failed");
            }

            logger.info("User deleted successfully", "UserService");
        } catch (Exception e) {
            logger.error("Failed to delete user", e, "UserService");
            throw e;
        }
    }
}

public class LoggerDemo {
    public static void main(String[] args) {
        Logger logger = new Logger(LogLevel.INFO, true);

        System.out.println("=== Basic Logging ===");
        logger.info("Application started");
        logger.warn("Low memory warning");
        logger.error("Connection failed");

        System.out.println("\n=== Logging with Context ===");
        logger.info("Processing request", "HTTP");
        logger.info("Query executed", "Database");

        System.out.println("\n=== Logging with Exceptions ===");
        try {
            throw new RuntimeException("Something went wrong");
        } catch (Exception e) {
            logger.error("Operation failed", e, "Service");
        }

        System.out.println("\n=== Logging with All Parameters ===");
        logger.log(LogLevel.ERROR, "Critical error occurred",
            new RuntimeException("Fatal"), "System");

        System.out.println("\n=== Logger Statistics ===");
        System.out.println("Total log entries: " + logger.getEntryCount());

        System.out.println("\n=== Service Usage ===");
        UserService service = new UserService(logger);
        service.findUser("U001");
        service.findUser(null);
    }
}
```

**Expected Output**:
```
=== Basic Logging ===
[10:30:15.123] [INFO] Application started
[10:30:15.125] [WARN] Low memory warning
[10:30:15.126] [ERROR] Connection failed

=== Logging with Context ===
[10:30:15.127] [INFO] [HTTP] Processing request
[10:30:15.128] [INFO] [Database] Query executed

=== Logging with Exceptions ===
[10:30:15.129] [ERROR] [Service] Operation failed - RuntimeException: Something went wrong

=== Logging with All Parameters ===
[10:30:15.130] [ERROR] [System] Critical error occurred - RuntimeException: Fatal

=== Logger Statistics ===
Total log entries: 7

=== Service Usage ===
[10:30:15.131] [INFO] [UserService] Finding user
[10:30:15.132] [INFO] [UserService] User found: U001
[10:30:15.133] [INFO] [UserService] Finding user
[10:30:15.134] [ERROR] [UserService] Failed to find user: U001 - IllegalArgumentException: User ID cannot be null
```

**Code Walkthrough**:

1. **Logger Class**: Implements overloaded log methods that delegate to a full-featured implementation.

2. **Convenience Methods**: Provides simple methods like info(), warn(), error() that call the general log() method.

3. **Parameter Flexibility**: Different overloads allow callers to provide only the parameters they need.

4. **Default Values**: The full log() method handles null parameters gracefully.

**Alternative Solution**:

```java
// Using Builder pattern for complex logging
class LogBuilder {
    private LogLevel level;
    private String message;
    private Throwable exception;
    private String context;

    public LogBuilder level(LogLevel level) {
        this.level = level;
        return this;
    }

    public LogBuilder message(String message) {
        this.message = message;
        return this;
    }

    public LogBuilder exception(Throwable exception) {
        this.exception = exception;
        return this;
    }

    public LogBuilder context(String context) {
        this.context = context;
        return this;
    }

    public void log(Logger logger) {
        logger.log(level, message, exception, context);
    }
}

// Usage: new LogBuilder().level(LogLevel.ERROR).message("Failed").exception(e).log(logger);
```

### Example 2: Collection Utilities

**Problem Statement**: Create a utility class that demonstrates method overloading for collection operations, providing flexible ways to manipulate and query collections.

**Requirements**:

- Support different collection types
- Provide filtering with different criteria
- Support transformation operations
- Handle both mutable and immutable operations

**Implementation**:

```java
package academy.javaengineering.oop.methodoverloading;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

class CollectionUtils {
    // Find maximum in collection
    public static <T extends Comparable<T>> T max(Collection<T> collection) {
        return collection.stream().max(Comparator.naturalOrder()).orElse(null);
    }

    // Find maximum with custom comparator
    public static <T> T max(Collection<T> collection, Comparator<T> comparator) {
        return collection.stream().max(comparator).orElse(null);
    }

    // Find minimum in collection
    public static <T extends Comparable<T>> T min(Collection<T> collection) {
        return collection.stream().min(Comparator.naturalOrder()).orElse(null);
    }

    // Find minimum with custom comparator
    public static <T> T min(Collection<T> collection, Comparator<T> comparator) {
        return collection.stream().min(comparator).orElse(null);
    }

    // Filter collection by predicate
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    // Filter by class type
    public static <T, R> List<R> filterByType(Collection<T> collection, Class<R> type) {
        return collection.stream()
            .filter(type::isInstance)
            .map(type::cast)
            .collect(Collectors.toList());
    }

    // Transform collection
    public static <T, R> List<R> transform(Collection<T> collection, Function<T, R> function) {
        return collection.stream().map(function).collect(Collectors.toList());
    }

    // Join collection elements
    public static <T> String join(Collection<T> collection, String separator) {
        return collection.stream()
            .map(Object::toString)
            .collect(Collectors.joining(separator));
    }

    // Join with prefix and suffix
    public static <T> String join(Collection<T> collection, String separator,
                                  String prefix, String suffix) {
        return collection.stream()
            .map(Object::toString)
            .collect(Collectors.joining(separator, prefix, suffix));
    }

    // Count elements matching predicate
    public static <T> long count(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).count();
    }

    // Check if all elements match predicate
    public static <T> boolean allMatch(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().allMatch(predicate);
    }

    // Check if any element matches predicate
    public static <T> boolean anyMatch(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().anyMatch(predicate);
    }

    // Partition collection into two groups
    public static <T> Map<Boolean, List<T>> partition(Collection<T> collection,
                                                       Predicate<T> predicate) {
        return collection.stream().collect(Collectors.partitionBy(predicate));
    }

    // Group collection by classifier
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection,
                                                  Function<T, K> classifier) {
        return collection.stream().collect(Collectors.groupingBy(classifier));
    }

    // Flatten nested collections
    public static <T> List<T> flatten(Collection<? extends Collection<T>> collections) {
        return collections.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    // Sum numeric collection
    public static int sumInt(Collection<Integer> collection) {
        return collection.stream().mapToInt(Integer::intValue).sum();
    }

    // Sum with extractor
    public static <T> int sum(Collection<T> collection, ToIntFunction<T> extractor) {
        return collection.stream().mapToInt(extractor).sum();
    }

    // Sum double collection
    public static double sumDouble(Collection<Double> collection) {
        return collection.stream().mapToDouble(Double::doubleValue).sum();
    }
}

public class CollectionUtilsDemo {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 3, 7, 4, 6);
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");
        List<Object> mixed = Arrays.asList(1, "two", 3.0, "four", 5);

        System.out.println("=== Min/Max Operations ===");
        System.out.println("Max number: " + CollectionUtils.max(numbers));
        System.out.println("Min number: " + CollectionUtils.min(numbers));
        System.out.println("Max by length: " + CollectionUtils.max(names,
            Comparator.comparingInt(String::length)));
        System.out.println("Min by length: " + CollectionUtils.min(names,
            Comparator.comparingInt(String::length)));

        System.out.println("\n=== Filter Operations ===");
        List<Integer> evenNumbers = CollectionUtils.filter(numbers, n -> n % 2 == 0);
        System.out.println("Even numbers: " + evenNumbers);

        List<String> strings = CollectionUtils.filterByType(mixed, String.class);
        System.out.println("Strings only: " + strings);

        System.out.println("\n=== Transform Operations ===");
        List<String> numberStrings = CollectionUtils.transform(numbers, n -> "Num:" + n);
        System.out.println("Transformed: " + numberStrings);

        List<Integer> lengths = CollectionUtils.transform(names, String::length);
        System.out.println("Name lengths: " + lengths);

        System.out.println("\n=== Join Operations ===");
        System.out.println("Join with comma: " + CollectionUtils.join(names, ", "));
        System.out.println("Join with decorators: " + CollectionUtils.join(names, ", ", "[", "]"));

        System.out.println("\n=== Count and Match ===");
        System.out.println("Count > 5: " + CollectionUtils.count(numbers, n -> n > 5));
        System.out.println("All > 0: " + CollectionUtils.allMatch(numbers, n -> n > 0));
        System.out.println("Any > 8: " + CollectionUtils.anyMatch(numbers, n -> n > 8));

        System.out.println("\n=== Grouping Operations ===");
        Map<Boolean, List<Integer>> partitioned = CollectionUtils.partition(numbers, n -> n % 2 == 0);
        System.out.println("Even: " + partitioned.get(true));
        System.out.println("Odd: " + partitioned.get(false));

        Map<Integer, List<String>> grouped = CollectionUtils.groupBy(names, String::length);
        System.out.println("Grouped by length: " + grouped);

        System.out.println("\n=== Flatten and Sum ===");
        List<List<Integer>> nested = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(4, 5, 6),
            Arrays.asList(7, 8, 9)
        );
        List<Integer> flat = CollectionUtils.flatten(nested);
        System.out.println("Flattened: " + flat);
        System.out.println("Sum: " + CollectionUtils.sumInt(flat));
    }
}
```

**Expected Output**:
```
=== Min/Max Operations ===
Max number: 9
Min number: 1
Max by length: Charlie
Min by length: Bob

=== Filter Operations ===
Even numbers: [2, 8, 4, 6]
Strings only: [two, four]

=== Transform Operations ===
Transformed: [Num:5, Num:2, Num:8, Num:1, Num:9, Num:3, Num:7, Num:4, Num:6]
Name lengths: [5, 3, 7, 5, 3]

=== Join Operations ===
Join with comma: Alice, Bob, Charlie, David, Eve
Join with decorators: [Alice, Bob, Charlie, David, Eve]

=== Count and Match ===
Count > 5: 4
All > 0: true
Any > 8: true

=== Grouping Operations ===
Even: [2, 8, 4, 6]
Odd: [5, 1, 9, 3, 7]
Grouped by length: {3=[Bob, Eve], 5=[Alice, David], 7=[Charlie]}

=== Flatten and Sum ===
Flattened: [1, 2, 3, 4, 5, 6, 7, 8, 9]
Sum: 45
```

**Code Walkthrough**:

1. **Generic Methods**: Uses Java generics for type-safe operations across different collection types.

2. **Functional Interfaces**: Leverages Predicate, Function, and Comparator for flexible filtering and transformation.

3. **Method Overloading**: Provides multiple versions of methods like join() with different parameter combinations.

4. **Stream API**: Uses Java Streams for efficient collection operations.

**Alternative Solution**:

```java
// Using varargs for more flexible APIs
class FlexibleCollectionUtils {
    @SafeVarargs
    public static <T> List<T> of(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    @SafeVarargs
    public static <T> List<T> merge(List<T>... lists) {
        return Arrays.stream(lists)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
```

## Hard Examples

### Example 1: Expression Parser with Operator Overloading

**Problem Statement**: Design an expression parser that uses method overloading to handle different mathematical operations and operand types, supporting operator precedence and associativity.

**Requirements**:

- Support basic arithmetic operations (+, -, *, /)
- Handle different numeric types (int, double, BigDecimal)
- Support unary operations (negation, absolute value)
- Implement operator precedence
- Provide error handling for invalid operations

**Architecture**:

```
Expression Parser System
├── Expression (abstract base)
│   ├── NumberExpression
│   ├── BinaryOperationExpression
│   └── UnaryOperationExpression
├── Operator (enum with overloads)
│   ├── ADD, SUBTRACT, MULTIPLY, DIVIDE
│   └── NEGATE, ABSOLUTE
├── Parser (recursive descent)
└── Evaluator (visitor pattern)
```

**Implementation**:

```java
package academy.javaengineering.oop.methodoverloading;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

abstract class Expression {
    abstract double evaluate();
    abstract BigDecimal evaluateBigDecimal();
    abstract String toString();

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}

class NumberExpression extends Expression {
    private final double value;
    private final BigDecimal bigDecimalValue;

    public NumberExpression(double value) {
        this.value = value;
        this.bigDecimalValue = BigDecimal.valueOf(value);
    }

    public NumberExpression(BigDecimal value) {
        this.value = value.doubleValue();
        this.bigDecimalValue = value;
    }

    @Override
    double evaluate() {
        return value;
    }

    @Override
    BigDecimal evaluateBigDecimal() {
        return bigDecimalValue;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

class BinaryOperationExpression extends Expression {
    private final Expression left;
    private final Expression right;
    private final char operator;

    public BinaryOperationExpression(Expression left, Expression right, char operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    double evaluate() {
        double leftVal = left.evaluate();
        double rightVal = right.evaluate();

        switch (operator) {
            case '+': return leftVal + rightVal;
            case '-': return leftVal - rightVal;
            case '*': return leftVal * rightVal;
            case '/':
                if (rightVal == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return leftVal / rightVal;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    @Override
    BigDecimal evaluateBigDecimal() {
        BigDecimal leftVal = left.evaluateBigDecimal();
        BigDecimal rightVal = right.evaluateBigDecimal();

        switch (operator) {
            case '+': return leftVal.add(rightVal);
            case '-': return leftVal.subtract(rightVal);
            case '*': return leftVal.multiply(rightVal);
            case '/':
                return leftVal.divide(rightVal, MathContext.DECIMAL128);
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }
}

class UnaryOperationExpression extends Expression {
    private final Expression operand;
    private final char operator;

    public UnaryOperationExpression(Expression operand, char operator) {
        this.operand = operand;
        this.operator = operator;
    }

    @Override
    double evaluate() {
        double operandVal = operand.evaluate();

        switch (operator) {
            case '-': return -operandVal;
            case '|': return Math.abs(operandVal);
            default:
                throw new IllegalArgumentException("Unknown unary operator: " + operator);
        }
    }

    @Override
    BigDecimal evaluateBigDecimal() {
        BigDecimal operandVal = operand.evaluateBigDecimal();

        switch (operator) {
            case '-': return operandVal.negate();
            case '|': return operandVal.abs();
            default:
                throw new IllegalArgumentException("Unknown unary operator: " + operator);
        }
    }

    @Override
    public String toString() {
        return operator + "(" + operand + ")";
    }
}

class ExpressionParser {
    private final String input;
    private int pos;

    public ExpressionParser(String input) {
        this.input = input.replaceAll("\\s+", "");
        this.pos = 0;
    }

    public Expression parse() {
        Expression result = parseExpression();
        if (pos < input.length()) {
            throw new RuntimeException("Unexpected character: " + input.charAt(pos));
        }
        return result;
    }

    private Expression parseExpression() {
        return parseTerm();
    }

    private Expression parseTerm() {
        Expression left = parseFactor();

        while (pos < input.length()) {
            char op = input.charAt(pos);
            if (op == '+' || op == '-') {
                pos++;
                Expression right = parseFactor();
                left = new BinaryOperationExpression(left, right, op);
            } else {
                break;
            }
        }

        return left;
    }

    private Expression parseFactor() {
        Expression left = parseUnary();

        while (pos < input.length()) {
            char op = input.charAt(pos);
            if (op == '*' || op == '/') {
                pos++;
                Expression right = parseUnary();
                left = new BinaryOperationExpression(left, right, op);
            } else {
                break;
            }
        }

        return left;
    }

    private Expression parseUnary() {
        if (pos < input.length()) {
            char op = input.charAt(pos);
            if (op == '-' || op == '|') {
                pos++;
                Expression operand = parseUnary();
                return new UnaryOperationExpression(operand, op);
            }
        }
        return parsePrimary();
    }

    private Expression parsePrimary() {
        if (pos < input.length() && input.charAt(pos) == '(') {
            pos++; // Skip '('
            Expression expr = parseExpression();
            if (pos < input.length() && input.charAt(pos) == ')') {
                pos++; // Skip ')'
            }
            return expr;
        }

        int start = pos;
        while (pos < input.length() &&
               (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
            pos++;
        }

        if (start == pos) {
            throw new RuntimeException("Expected number at position " + pos);
        }

        String numberStr = input.substring(start, pos);
        try {
            if (numberStr.contains(".")) {
                return new NumberExpression(new BigDecimal(numberStr));
            } else {
                return new NumberExpression(Integer.parseInt(numberStr));
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number: " + numberStr);
        }
    }
}

class ExpressionEvaluator {
    // Overloaded evaluate methods for different input types

    public static double evaluate(String expression) {
        ExpressionParser parser = new ExpressionParser(expression);
        Expression expr = parser.parse();
        return expr.evaluate();
    }

    public static BigDecimal evaluateBigDecimal(String expression) {
        ExpressionParser parser = new ExpressionParser(expression);
        Expression expr = parser.parse();
        return expr.evaluateBigDecimal();
    }

    public static double evaluate(Expression expression) {
        return expression.evaluate();
    }

    public static BigDecimal evaluateBigDecimal(Expression expression) {
        return expression.evaluateBigDecimal();
    }

    // Batch evaluation
    public static List<Double> evaluateAll(String... expressions) {
        List<Double> results = new ArrayList<>();
        for (String expr : expressions) {
            results.add(evaluate(expr));
        }
        return results;
    }

    // Safe evaluation with default value
    public static double evaluateOrDefault(String expression, double defaultValue) {
        try {
            return evaluate(expression);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}

public class ExpressionParserDemo {
    public static void main(String[] args) {
        System.out.println("=== Basic Expressions ===");
        System.out.println("2 + 3 = " + ExpressionEvaluator.evaluate("2+3"));
        System.out.println("10 - 4 = " + ExpressionEvaluator.evaluate("10-4"));
        System.out.println("5 * 6 = " + ExpressionEvaluator.evaluate("5*6"));
        System.out.println("20 / 4 = " + ExpressionEvaluator.evaluate("20/4"));

        System.out.println("\n=== Complex Expressions ===");
        System.out.println("2 + 3 * 4 = " + ExpressionEvaluator.evaluate("2+3*4"));
        System.out.println("(2 + 3) * 4 = " + ExpressionEvaluator.evaluate("(2+3)*4"));
        System.out.println("10 / 2 - 3 = " + ExpressionEvaluator.evaluate("10/2-3"));

        System.out.println("\n=== Unary Operations ===");
        System.out.println("-5 = " + ExpressionEvaluator.evaluate("-5"));
        System.out.println("|-10| = " + ExpressionEvaluator.evaluate("|-10|"));
        System.out.println("-(-3) = " + ExpressionEvaluator.evaluate("-(-3)"));

        System.out.println("\n=== BigDecimal Operations ===");
        System.out.println("0.1 + 0.2 = " + ExpressionEvaluator.evaluateBigDecimal("0.1+0.2"));
        System.out.println("1/3 = " + ExpressionEvaluator.evaluateBigDecimal("1/3"));

        System.out.println("\n=== Batch Evaluation ===");
        List<Double> results = ExpressionEvaluator.evaluateAll(
            "2+3", "5*5", "10/2", "100-50"
        );
        System.out.println("Batch results: " + results);

        System.out.println("\n=== Safe Evaluation ===");
        System.out.println("Valid: " + ExpressionEvaluator.evaluateOrDefault("2+3", 0));
        System.out.println("Invalid: " + ExpressionEvaluator.evaluateOrDefault("2/0", -1));
        System.out.println("Malformed: " + ExpressionEvaluator.evaluateOrDefault("abc", -1));
    }
}
```

**Execution Flow**:

1. **Parsing**: The parser reads the input string and builds an expression tree
2. **Tree Construction**: Binary and unary operations create nested Expression objects
3. **Evaluation**: The expression tree is evaluated recursively
4. **Type Selection**: Different evaluate methods handle different numeric types

**Unit Tests**:

```java
public class ExpressionParserTest {
    public static void main(String[] args) {
        System.out.println("=== Running Expression Parser Tests ===\n");

        testBasicOperations();
        testOperatorPrecedence();
        testUnaryOperations();
        testBigDecimal();
        testErrorHandling();

        System.out.println("\n=== All Tests Passed ===");
    }

    private static void testBasicOperations() {
        System.out.println("Test 1: Basic Operations");
        assert ExpressionEvaluator.evaluate("2+3") == 5.0 : "Addition failed";
        assert ExpressionEvaluator.evaluate("10-4") == 6.0 : "Subtraction failed";
        assert ExpressionEvaluator.evaluate("5*6") == 30.0 : "Multiplication failed";
        assert ExpressionEvaluator.evaluate("20/4") == 5.0 : "Division failed";
        System.out.println("  PASS: Basic operations test passed\n");
    }

    private static void testOperatorPrecedence() {
        System.out.println("Test 2: Operator Precedence");
        assert ExpressionEvaluator.evaluate("2+3*4") == 14.0 : "Precedence failed";
        assert ExpressionEvaluator.evaluate("(2+3)*4") == 20.0 : "Parentheses failed";
        System.out.println("  PASS: Operator precedence test passed\n");
    }

    private static void testUnaryOperations() {
        System.out.println("Test 3: Unary Operations");
        assert ExpressionEvaluator.evaluate("-5") == -5.0 : "Negation failed";
        assert ExpressionEvaluator.evaluate("|-10|") == 10.0 : "Absolute value failed";
        System.out.println("  PASS: Unary operations test passed\n");
    }

    private static void testBigDecimal() {
        System.out.println("Test 4: BigDecimal Operations");
        BigDecimal result = ExpressionEvaluator.evaluateBigDecimal("0.1+0.2");
        assert result.compareTo(new BigDecimal("0.3")) == 0 : "BigDecimal addition failed";
        System.out.println("  PASS: BigDecimal operations test passed\n");
    }

    private static void testErrorHandling() {
        System.out.println("Test 5: Error Handling");
        try {
            ExpressionEvaluator.evaluate("2/0");
            assert false : "Should throw ArithmeticException";
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            ExpressionEvaluator.evaluate("abc");
            assert false : "Should throw RuntimeException";
        } catch (RuntimeException e) {
            // Expected
        }
        System.out.println("  PASS: Error handling test passed\n");
    }
}
```

**Complexity**:

- **Time Complexity**: O(n) for parsing, O(n) for evaluation where n is expression length
- **Space Complexity**: O(n) for expression tree storage

**Best Practices**:

- Use method overloading to provide type-safe alternatives for different numeric types
- Implement proper error handling for invalid operations
- Consider using BigDecimal for precise financial calculations
- Document operator precedence and associativity rules
- Provide safe evaluation methods with default values

## Exercises

### Easy

1. **Math Operations**: Create a MathUtils class with overloaded add() methods for int, double, and String concatenation.

2. **Area Calculator**: Implement overloaded area() methods for circle (radius), rectangle (width, height), and triangle (base, height).

3. **String Formatter**: Create a format() method that accepts different parameter combinations for formatting strings.

### Medium

1. **Log Manager**: Design a logging system with overloaded log() methods supporting different log levels, contexts, and exceptions.

2. **File Processor**: Create file processing methods that can handle different file types and processing options through overloading.

3. **Collection Search**: Implement search methods for collections with different search criteria and options.

### Hard

1. **SQL Builder**: Design a SQL query builder using method overloading to construct different types of queries (SELECT, INSERT, UPDATE, DELETE) with various clauses.

2. **Event System**: Create an event handling system with overloaded methods for different event types and handler configurations.

3. **Configuration Manager**: Build a configuration system with overloaded methods for reading different configuration types and sources.

## Interview Questions

### Easy

1. **What is method overloading?**
   Method overloading allows a class to have multiple methods with the same name but different parameter lists. The compiler determines which method to call based on the arguments at compile time (static binding).

2. **Can we overload methods by changing only the return type?**
   No, method overloading is based on the method signature, which includes the method name and parameter types. Two methods with the same parameters but different return types will cause a compilation error.

3. **What is the difference between method overloading and overriding?**
   Overloading is compile-time polymorphism with same method name but different parameters in the same class. Overriding is runtime polymorphism with same method signature in parent and child classes.

### Medium

1. **How does the compiler resolve overloaded methods?**
   The compiler uses overload resolution: first tries exact match, then widening, then autoboxing, then varargs. If multiple matches are equally specific, it's a compilation error (ambiguity).

2. **Can we overload main() method in Java?**
   Yes, you can overload main() method. However, only the standard public static void main(String[] args) is used as the entry point. Other overloads can be called explicitly.

3. **What are the rules for overload resolution with varargs?**
   Varargs methods have lowest priority. A method without varargs is preferred over one with varargs. If only varargs methods match, the most specific one is chosen.

### Hard

1. **Explain the complete overload resolution algorithm in Java.**
   Java uses a 3-phase algorithm: Phase 1 tries matching without allowing widening or varargs. Phase 2 allows widening but not autoboxing or varargs. Phase 3 allows autoboxing and varargs. If no method is found in earlier phases, later phases are tried.

2. **How do generics interact with method overloading?**
   Generic type erasure can cause overload conflicts. For example, `method(List<String>)` and `method(List<Integer>)` become the same after erasure. Use different method names or non-generic parameters to avoid conflicts.

## Common Pitfalls

### 1. Ambiguous Overloaded Methods

**Wrong**:
```java
class Processor {
    public void process(int a, double b) {
        System.out.println("int, double");
    }

    public void process(double a, int b) {
        System.out.println("double, int");
    }
}

// This causes ambiguity
Processor p = new Processor();
p.process(1, 2); // Compilation error: ambiguous method call
```

**Right**:
```java
class Processor {
    public void process(int a, double b) {
        System.out.println("int, double");
    }

    public void process(double a, int b) {
        System.out.println("double, int");
    }

    // Add a specific overload to resolve ambiguity
    public void process(int a, int b) {
        System.out.println("int, int");
    }
}

// Now works without ambiguity
Processor p = new Processor();
p.process(1, 2); // Calls process(int, int)
p.process(1, 2.0); // Calls process(int, double)
p.process(1.0, 2); // Calls process(double, int)
```

### 2. Overloading with Autoboxing Confusion

**Wrong**:
```java
class Printer {
    public void print(int value) {
        System.out.println("int: " + value);
    }

    public void print(Integer value) {
        System.out.println("Integer: " + value);
    }
}

Printer p = new Printer();
p.print(5); // Prints "int: 5"
p.print(new Integer(5)); // Prints "Integer: 5"

// But what about this?
p.print(null); // Compilation error: ambiguous call
Integer num = null;
p.print(num); // Prints "Integer: null"
```

**Right**:
```java
class Printer {
    public void print(int value) {
        System.out.println("int: " + value);
    }

    public void print(Integer value) {
        System.out.println("Integer: " + value);
    }

    // Add explicit null handling
    public void print(Object value) {
        if (value == null) {
            System.out.println("null value");
        } else if (value instanceof Integer) {
            print((Integer) value);
        } else {
            System.out.println("Object: " + value);
        }
    }
}

Printer p = new Printer();
p.print(null); // Now works: prints "null value"
```

### 3. Varargs Overloading Issues

**Wrong**:
```java
class Logger {
    public void log(String message) {
        System.out.println(message);
    }

    public void log(String... messages) {
        for (String msg : messages) {
            System.out.println(msg);
        }
    }
}

Logger logger = new Logger();
logger.log("Hello"); // Compilation error: ambiguous call
```

**Right**:
```java
class Logger {
    public void log(String message) {
        System.out.println(message);
    }

    public void log(String first, String... rest) {
        System.out.println(first);
        for (String msg : rest) {
            System.out.println(msg);
        }
    }

    // Or use different method names
    public void logMultiple(String... messages) {
        for (String msg : messages) {
            System.out.println(msg);
        }
    }
}

Logger logger = new Logger();
logger.log("Hello"); // Calls log(String)
logger.log("Hello", "World"); // Calls log(String, String...)
logger.logMultiple("A", "B", "C"); // Calls logMultiple(String...)
```

## Best Practices

1. **Use overloading for convenience, not necessity**: Overloading should make APIs easier to use, not create confusing alternatives.

2. **Maintain consistent semantics**: Overloaded methods should behave consistently. If one overload throws an exception for invalid input, all overloads should too.

3. **Avoid excessive overloading**: Too many overloads can make APIs hard to understand. Consider builder patterns or configuration objects for complex scenarios.

4. **Be careful with varargs**: Varargs overloading can cause ambiguity. Prefer fixed-parameter methods when possible.

5. **Document overloaded methods clearly**: Each overload should be documented to explain when it should be used and how it differs from other overloads.

## Real World Usage

### How Spring Uses This

Spring Framework extensively uses method overloading:

- **ApplicationContext**: Multiple overloaded getBean() methods for different lookup scenarios
- **JdbcTemplate**: Overloaded query methods for different result handling
- **RestTemplate**: Overloaded exchange methods for different HTTP operations

### How JDK Uses This

The Java Development Kit uses overloading throughout:

- **System.out.println()**: Overloaded for every primitive type and Object
- **Integer.parseInt()**: Overloaded for different radix values
- **Collections.sort()**: Overloaded for natural ordering and custom comparators

### How Hibernate Uses This

Hibernate ORM uses overloading for flexible APIs:

- **Session.get()**: Overloaded for different loading options
- **Query.setParameter()**: Overloaded for different parameter types
- **Criteria API**: Overloaded methods for different constraint types

### Enterprise Usage

In enterprise applications, overloading is used for:

- **Service Methods**: Different overloads for common and advanced use cases
- **DAO Methods**: Overloaded CRUD operations with different options
- **Utility Classes**: Flexible utility methods that adapt to different input types

## References

1. **Effective Java** by Joshua Bloch - Item 52: Use overloaded methods carefully
2. **Java Language Specification** - Method Overloading
3. **Java SE Documentation** - Declaring Methods
4. **Clean Code** by Robert C. Martin - Function overloading guidelines
5. **Design Patterns** - Use of overloading in Factory and Builder patterns

## Summary

- Method overloading allows multiple methods with the same name but different parameter lists
- Overloading is resolved at compile time through static binding (compile-time polymorphism)
- Method signature includes name and parameter types, not return type
- The compiler uses a specific algorithm to resolve overloaded method calls
- Overloading improves code readability and API design
- Be careful with autoboxing and varargs to avoid ambiguity

**Next Steps**: [16-method-overriding](../16-method-overriding/README.md)
