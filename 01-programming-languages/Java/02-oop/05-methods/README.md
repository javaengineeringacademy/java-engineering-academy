# Methods in Java

Methods are blocks of code that perform specific tasks. They are the fundamental building blocks of Java programs, enabling code reuse, modularity, and abstraction.

## Learning Objectives

By the end of this topic, you will be able to:

- Define and invoke methods with parameters and return values
- Understand method signatures and overloading
- Apply pass-by-value semantics correctly
- Use static and instance methods appropriately
- Design methods following single responsibility principle

## Prerequisites

- [02-classes](../02-classes/)
- [03-objects](../03-objects/)
- Basic understanding of variables and data types

## Why This Concept Exists

### The Problem

Without methods, code becomes repetitive and impossible to maintain. Every task would require writing the same logic multiple times.

### The Solution

Methods encapsulate reusable logic. Define once, call anywhere. They enable modular design, easier debugging, and team collaboration.

### Real-World Analogy

Think of methods as recipes. A recipe defines steps for a dish. You can follow the same recipe multiple times without rewriting it each time.

## Internal Working

### JVM Perspective

When a method is called:
1. JVM pushes the method call onto the call stack
2. Local variables are allocated in stack memory
3. Method parameters are copied to the stack
4. Execution begins at the first statement
5. Return value is placed on the stack
6. Stack frame is popped

### Memory Representation

```java
public class MemoryDemo {
    public static void main(String[] args) {
        int result = add(5, 3);  // Stack frame created
    }
    
    static int add(int a, int b) {  // Parameters copied to stack
        return a + b;  // Return value placed on stack
    }
}
```

## Syntax

```java
// Method declaration
accessModifier returnType methodName(parameters) {
    // method body
    return returnValue;  // if not void
}

// Examples
public int add(int a, int b) { return a + b; }
private void log(String message) { System.out.println(message); }
public static int multiply(int a, int b) { return a * b; }
```

## Easy Examples

### Example 1: Greeting Method

**Problem Statement:**
Create a method that greets a user by name.

**Implementation:**

```java
package academy.javaengineering.oop.methods.examples.easy;

public class GreetingDemo {
    
    public static void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }
    
    public static void main(String[] args) {
        greet("Alice");
        greet("Bob");
    }
}
```

**Expected Output:**
```
Hello, Alice!
Hello, Bob!
```

**Execution Flow:**
1. `main()` calls `greet("Alice")`
2. Method prints greeting
3. Returns to `main()`
4. `main()` calls `greet("Bob")`

**Best Practices:**
- Use descriptive method names
- Keep methods short and focused
- Avoid side effects in utility methods

### Example 2: Calculate Area

**Problem Statement:**
Create a method to calculate the area of a rectangle.

**Implementation:**

```java
package academy.javaengineering.oop.methods.examples.easy;

public class AreaCalculator {
    
    public static double calculateArea(double length, double width) {
        return length * width;
    }
    
    public static void main(String[] args) {
        double area = calculateArea(5.0, 3.0);
        System.out.println("Area: " + area);
    }
}
```

**Expected Output:**
```
Area: 15.0
```

**Best Practices:**
- Methods should do one thing
- Use meaningful parameter names
- Return results, don't print them

## Medium Examples

### Example 1: Temperature Converter

**Problem Statement:**
Create methods to convert between Celsius and Fahrenheit.

**Requirements:**
- Convert Celsius to Fahrenheit
- Convert Fahrenheit to Celsius
- Handle edge cases

**Implementation:**

```java
package academy.javaengineering.oop.methods.examples.medium;

public class TemperatureConverter {
    
    private static final double FREEZING_POINT_C = 0.0;
    private static final double BOILING_POINT_C = 100.0;
    
    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32.0;
    }
    
    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32.0) * 5.0 / 9.0;
    }
    
    public static String describeTemperature(double celsius) {
        if (celsius < FREEZING_POINT_C) {
            return "Freezing";
        } else if (celsius < 20.0) {
            return "Cold";
        } else if (celsius < 30.0) {
            return "Comfortable";
        } else if (celsius < BOILING_POINT_C) {
            return "Hot";
        } else {
            return "Extreme";
        }
    }
    
    public static void main(String[] args) {
        double celsius = 25.0;
        double fahrenheit = celsiusToFahrenheit(celsius);
        
        System.out.printf("%.1f°C = %.1f°F%n", celsius, fahrenheit);
        System.out.println("Condition: " + describeTemperature(celsius));
        
        double tempF = 72.0;
        double tempC = fahrenheitToCelsius(tempF);
        System.out.printf("%.1f°F = %.1f°C%n", tempF, tempC);
    }
}
```

**Code Walkthrough:**
1. Two conversion methods handle math
2. Description method uses if-else chain
3. Main method demonstrates both conversions

**Expected Output:**
```
25.0°C = 77.0°F
Condition: Comfortable
72.0°F = 22.2°C
```

**Alternative Solution:**

```java
public static double convert(double value, String from, String to) {
    if ("C".equals(from) && "F".equals(to)) {
        return celsiusToFahrenheit(value);
    } else if ("F".equals(from) && "C".equals(to)) {
        return fahrenheitToCelsius(value);
    }
    throw new IllegalArgumentException("Invalid conversion");
}
```

### Example 2: String Utilities

**Problem Statement:**
Create a utility class with methods for common string operations.

**Requirements:**
- Reverse a string
- Check if palindrome
- Count words
- Capitalize first letter

**Implementation:**

```java
package academy.javaengineering.oop.methods.examples.medium;

public class StringUtils {
    
    private StringUtils() {
        throw new AssertionError("Utility class");
    }
    
    public static String reverse(String input) {
        if (input == null) {
            return null;
        }
        return new StringBuilder(input).reverse().toString();
    }
    
    public static boolean isPalindrome(String input) {
        if (input == null) {
            return false;
        }
        String cleaned = input.toLowerCase().replaceAll("[^a-z0-9]", "");
        return cleaned.equals(reverse(cleaned));
    }
    
    public static int countWords(String input) {
        if (input == null || input.isBlank()) {
            return 0;
        }
        return input.trim().split("\\s+").length;
    }
    
    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    
    public static void main(String[] args) {
        String text = "Hello World";
        
        System.out.println("Original: " + text);
        System.out.println("Reversed: " + reverse(text));
        System.out.println("Words: " + countWords(text));
        System.out.println("Capitalized: " + capitalize(text));
        
        String palindrome = "A man a plan a canal Panama";
        System.out.println("\n\"" + palindrome + "\"");
        System.out.println("Is palindrome: " + isPalindrome(palindrome));
    }
}
```

**Code Walkthrough:**
1. Private constructor prevents instantiation
2. Each method handles null/empty cases
3. Static methods for utility access
4. Main demonstrates all methods

**Expected Output:**
```
Original: Hello World
Reversed: dlroW olleH
Words: 2
Capitalized: Hello World

"A man a plan a canal Panama"
Is palindrome: true
```

## Hard Examples

### Example 1: Method Chain Builder

**Problem Statement:**
Implement a method chaining pattern for building SQL queries.

**Requirements:**
- Support SELECT, WHERE, ORDER BY, LIMIT
- Type-safe column selection
- Fluent interface

**Architecture:**
```
academy.javaengineering.oop.methods.examples.hard/
├── QueryBuilder.java
├── Condition.java
└── QueryBuilderDemo.java
```

**Implementation:**

```java
package academy.javaengineering.oop.methods.examples.hard;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class QueryBuilder {
    
    private final String table;
    private final List<String> columns = new ArrayList<>();
    private final List<Condition> conditions = new ArrayList<>();
    private String orderByColumn;
    private boolean ascending = true;
    private Integer limit;
    
    private QueryBuilder(String table) {
        this.table = table;
    }
    
    public static QueryBuilder from(String table) {
        return new QueryBuilder(table);
    }
    
    public QueryBuilder select(String... columns) {
        this.columns.addAll(List.of(columns));
        return this;
    }
    
    public QueryBuilder where(String column, String operator, Object value) {
        this.conditions.add(new Condition(column, operator, value));
        return this;
    }
    
    public QueryBuilder orderBy(String column, boolean ascending) {
        this.orderByColumn = column;
        this.ascending = ascending;
        return this;
    }
    
    public QueryBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }
    
    public String build() {
        StringBuilder query = new StringBuilder();
        
        query.append("SELECT ");
        if (columns.isEmpty()) {
            query.append("*");
        } else {
            query.append(String.join(", ", columns));
        }
        
        query.append(" FROM ").append(table);
        
        if (!conditions.isEmpty()) {
            query.append(" WHERE ");
            StringJoiner joiner = new StringJoiner(" AND ");
            conditions.forEach(c -> joiner.add(c.toString()));
            query.append(joiner);
        }
        
        if (orderByColumn != null) {
            query.append(" ORDER BY ").append(orderByColumn);
            query.append(ascending ? " ASC" : " DESC");
        }
        
        if (limit != null) {
            query.append(" LIMIT ").append(limit);
        }
        
        return query.toString();
    }
    
    public static void main(String[] args) {
        String query = QueryBuilder.from("users")
                .select("id", "name", "email")
                .where("age", ">", 18)
                .where("status", "=", "ACTIVE")
                .orderBy("name", true)
                .limit(10)
                .build();
        
        System.out.println(query);
    }
}

class Condition {
    private final String column;
    private final String operator;
    private final Object value;
    
    Condition(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }
    
    @Override
    public String toString() {
        if (value instanceof String) {
            return column + " " + operator + " '" + value + "'";
        }
        return column + " " + operator + " " + value;
    }
}
```

**Expected Output:**
```
SELECT id, name, email FROM users WHERE age > 18 AND status = 'ACTIVE' ORDER BY name ASC LIMIT 10
```

**Unit Tests:**

```java
package academy.javaengineering.oop.methods.examples.hard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QueryBuilderTest {
    
    @Test
    void shouldBuildSimpleQuery() {
        String query = QueryBuilder.from("users").build();
        assertEquals("SELECT * FROM users", query);
    }
    
    @Test
    void shouldBuildQueryWithColumns() {
        String query = QueryBuilder.from("users")
                .select("id", "name")
                .build();
        assertEquals("SELECT id, name FROM users", query);
    }
    
    @Test
    void shouldBuildQueryWithWhere() {
        String query = QueryBuilder.from("users")
                .where("age", ">", 18)
                .build();
        assertEquals("SELECT * FROM users WHERE age > 18", query);
    }
}
```

**Execution Flow:**
1. `from()` creates QueryBuilder instance
2. Each method returns `this` for chaining
3. `build()` assembles the final query string
4. Conditions are joined with AND

**Complexity:**
- Time: O(n) where n is number of conditions
- Space: O(n) for storing conditions

**Best Practices:**
- Return `this` for method chaining
- Validate inputs in builder methods
- Keep builder immutable where possible
- Use fluent interface for readability

## Exercises

### Easy

1. **Temperature Calculator**: Create methods to convert temperatures between Celsius, Fahrenheit, and Kelvin.

2. **Grade Calculator**: Create a method that takes a numeric score and returns a letter grade (A, B, C, D, F).

3. **Even Odd Checker**: Create a method that checks if a number is even or odd.

### Medium

1. **Array Statistics**: Create methods to calculate mean, median, min, and max of an integer array.

2. **Password Validator**: Create methods to validate password strength (length, uppercase, lowercase, digit, special char).

3. **Date Formatter**: Create methods to format dates in different patterns (US, EU, ISO).

### Hard

1. **Pipeline Builder**: Create a method chain builder for data transformation pipelines.

2. **Rate Limiter**: Implement a token bucket rate limiter using methods.

3. **Cache Provider**: Create an in-memory cache with get, put, and evict methods.

## Interview Questions

### Easy

1. **Q:** What is the difference between a method and a function?
   **A:** In Java, all code resides in classes, so we call them methods. A function is a standalone block of code, while a method is associated with an object or class.

2. **Q:** What is method overloading?
   **A:** Method overloading allows multiple methods with the same name but different parameter lists (different number, types, or order of parameters).

3. **Q:** Can a method return multiple values?
   **A:** Java doesn't support multiple return values directly. Use arrays, collections, or custom objects to return multiple values.

### Medium

1. **Q:** Explain pass-by-value in Java.
   **A:** Java is always pass-by-value. For primitives, the value is copied. For objects, the reference is copied (not the object). Changes to the reference inside the method don't affect the original reference.

2. **Q:** What is the difference between static and instance methods?
   **A:** Static methods belong to the class and can be called without creating an instance. Instance methods belong to an object and require an instance to be called.

3. **Q:** What are varargs?
   **A:** Varargs (String... args) allow methods to accept zero or more arguments of the same type. They're treated as arrays internally.

### Hard

1. **Q:** How does method dispatch work in Java?
   **A:** Java uses dynamic dispatch for instance methods. At runtime, the JVM looks up the actual class of the object and invokes the most specific method implementation. Static methods use static dispatch at compile time.

2. **Q:** What is tail recursion and why doesn't Java optimize it?
   **A:** Tail recursion is when the recursive call is the last operation. Java doesn't optimize tail recursion because the JVM spec doesn't require it, and stack overflow detection would be affected.

## Common Pitfalls

### 1. NullPointerException from Unchecked Returns

**Wrong:**
```java
public String findUser(int id) {
    if (id == 1) return "Alice";
    return null;
}

// Caller
String name = findUser(2).toUpperCase(); // NPE!
```

**Right:**
```java
public Optional<String> findUser(int id) {
    if (id == 1) return Optional.of("Alice");
    return Optional.empty();
}

// Caller
findUser(2).ifPresent(name -> System.out.println(name.toUpperCase()));
```

**Why:** Always handle null returns or use Optional.

### 2. Side Effects in Utility Methods

**Wrong:**
```java
public static int calculateTotal(int[] items) {
    System.out.println("Calculating..."); // Side effect!
    return Arrays.stream(items).sum();
}
```

**Right:**
```java
public static int calculateTotal(int[] items) {
    return Arrays.stream(items).sum();
}
```

**Why:** Utility methods should be pure functions without side effects.

### 3. Too Many Parameters

**Wrong:**
```java
public User createUser(String first, String last, String email, 
                       String phone, String address, String city,
                       String state, String zip) {
    // Too many parameters!
}
```

**Right:**
```java
public User createUser(UserRequest request) {
    // Use a request object
}

// Or use a builder
User user = User.builder()
    .firstName("John")
    .lastName("Doe")
    .email("john@example.com")
    .build();
```

**Why:** Methods with more than 3-4 parameters are hard to use. Use objects or builders.

## Best Practices

1. **Single Responsibility**: Each method should do one thing well
2. **Descriptive Names**: Method names should describe what they do
3. **Limit Parameters**: Use objects for more than 3 parameters
4. **Return Consistently**: Don't mix return types (null vs Optional vs exception)
5. **Document Public Methods**: Use Javadoc for public API methods

## Real World Usage

### Spring Framework

Spring uses methods extensively in dependency injection:
```java
@Service
public class UserService {
    @Autowired
    public void setUserRepository(UserRepository repo) { ... }
    
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) { ... }
}
```

### Hibernate

Hibernate uses methods for CRUD operations:
```java
Session session = sessionFactory.openSession();
User user = session.get(User.class, 1L);  // Method call
session.save(user);  // Method call
```

### JDK Source Code

Java collections use methods extensively:
```java
List<String> list = new ArrayList<>();
list.add("item");      // Method
list.remove("item");   // Method
list.contains("item"); // Method
```

### Enterprise Applications

```java
// Service layer methods
public class OrderService {
    public Order createOrder(OrderRequest request) { ... }
    public Order updateOrder(Long id, OrderUpdate update) { ... }
    public void cancelOrder(Long id) { ... }
    public List<Order> findOrders(OrderCriteria criteria) { ... }
}
```

## References

- [Official Java Documentation](https://docs.oracle.com/en/java/)
- Effective Java by Joshua Bloch
- [04-constructors](../04-constructors/)
- [06-this-keyword](../06-this-keyword/)

## Summary

- Methods encapsulate reusable logic
- Use descriptive names and single responsibility
- Java is pass-by-value for all types
- Static methods belong to classes, instance methods to objects
- Limit parameters to 3-4; use objects for more
- Return Optional instead of null for optional values

---

**Next Topic:** [06-this-keyword](../06-this-keyword/)
**Previous Topic:** [04-constructors](../04-constructors/)
