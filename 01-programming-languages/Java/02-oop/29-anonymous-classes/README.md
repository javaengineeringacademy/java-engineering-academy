# Anonymous Classes

## Introduction

An anonymous class in Java is a class without a name, declared and instantiated in a single expression. Anonymous classes are one of the four types of nested classes in Java and provide a concise way to create one-off implementations of interfaces or abstract classes. They are particularly useful for event handlers, callbacks, and short-lived implementations that don't warrant a separate named class. Introduced alongside inner classes in Java 1.1, anonymous classes were the precursor to lambda expressions (Java 8) and remain essential for implementing abstract classes (which lambdas cannot do) and for cases where a named class would add unnecessary verbosity. Understanding anonymous classes is critical for reading legacy Java code, working with Swing/AWT event models, and understanding the relationship between anonymous classes and closures.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Declare anonymous classes that implement interfaces or extend abstract classes
- [ ] Understand the relationship between anonymous classes, inner classes, and closures
- [ ] Recognize when to use anonymous classes versus lambda expressions
- [ ] Identify and avoid memory leaks caused by anonymous class references

## Prerequisites

- [02-classes](../02-classes/) — Anonymous classes are unnamed classes
- [12-interfaces](../12-interfaces/) — Anonymous classes commonly implement interfaces
- [13-abstract-classes](../13-abstract-classes/) — Anonymous classes can extend abstract classes
- [27-inner-classes](../27-inner-classes/) — Anonymous classes are a type of inner class
- [28-nested-classes](../28-nested-classes/) — Understanding nested class taxonomy

## Why This Concept Exists

### The Problem

Sometimes you need a quick implementation of an interface or abstract class, but creating a separate named class feels like overkill. For example, when registering an event listener, you need a one-off implementation of a callback interface. Creating a named class for every callback bloats the codebase.

### The Solution

Anonymous classes let you declare and instantiate a class in a single expression. They are syntactically concise and keep the implementation visible at the point of use. For single-method interfaces (functional interfaces), Java 8+ lambdas are even more concise, but anonymous classes remain necessary for abstract classes and multi-method interfaces.

### Real-World Analogy

Think of an anonymous class like a sticky note. You jot down a quick instruction and stick it on a document. You don't create a formal memo with a header and file number — it's a one-off, disposable communication. Similarly, an anonymous class provides a quick, disposable implementation that's visible only at the point of use.

## Internal Working

### Compilation Behavior

When the compiler encounters an anonymous class like:

```java
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};
```

It generates a separate `.class` file named `ClassName$1.class` (incrementing for each anonymous class in the same outer class). The anonymous class becomes a regular class file in bytecode — it's not truly "anonymous" at the JVM level.

### Relationship to Inner Classes

Every anonymous class is implicitly an inner class (if declared in an instance context) or a static nested class (if declared in a static context). It has access to the enclosing class's members based on where it's declared. If the anonymous class is inside an instance method, it holds an implicit reference to the outer instance.

### Variable Capture

Anonymous classes capture the value of local variables at the time of creation. In Java 8+, the captured variable must be final or effectively final. The anonymous class gets a copy of the variable, not a reference to it.

## Syntax

```java
// Anonymous class implementing an interface
Comparator<String> comp = new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
};

// Anonymous class extending an abstract class
abstract class Animal {
    abstract void speak();
}

Animal dog = new Animal() {
    @Override
    void speak() {
        System.out.println("Woof!");
    }
};

// Anonymous class with constructor arguments
Thread t = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("Running in thread");
    }
});

// Anonymous class accessing outer instance members
class Outer {
    private int count = 0;

    void start() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                count++; // Accesses outer's private field
                System.out.println("Count: " + count);
            }
        };
        new Thread(r).start();
    }
}
```

## Easy Examples

### Example 1: Anonymous Class Implementing a Comparator

**Problem Statement**: Sort a list of strings by their length using an anonymous `Comparator` implementation.

**Implementation**:

```java
package academy.javaengineering.oop.anonymousclasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparatorDemo {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Charlie");
        names.add("Bob");
        names.add("Alice");
        names.add("Diana");
        names.add("Eve");

        System.out.println("Before sorting: " + names);

        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        });

        System.out.println("After sorting by length: " + names);
    }
}
```

**Expected Output**:
```
Before sorting: [Charlie, Bob, Alice, Diana, Eve]
After sorting by length: [Bob, Eve, Diana, Alice, Charlie]
```

**Best Practices**:
- Use anonymous classes for one-off interface implementations
- Compare with lambda: `Comparator.comparingInt(String::length)` is even shorter
- Keep anonymous class implementations focused and small

### Example 2: Anonymous Class Extending an Abstract Class

**Problem Statement**: Create different shapes using anonymous classes that extend a `Shape` abstract class.

**Implementation**:

```java
package academy.javaengineering.oop.anonymousclasses;

abstract class Shape {
    String name;

    Shape(String name) {
        this.name = name;
    }

    abstract double area();
    abstract double perimeter();

    @Override
    public String toString() {
        return name + " [area=" + String.format("%.2f", area())
                + ", perimeter=" + String.format("%.2f", perimeter()) + "]";
    }
}

public class ShapeDemo {
    public static void main(String[] args) {
        Shape circle = new Shape("Circle") {
            private double radius = 5.0;

            @Override
            double area() { return Math.PI * radius * radius; }

            @Override
            double perimeter() { return 2 * Math.PI * radius; }
        };

        Shape rectangle = new Shape("Rectangle") {
            private double width = 4.0;
            private double height = 6.0;

            @Override
            double area() { return width * height; }

            @Override
            double perimeter() { return 2 * (width + height); }
        };

        System.out.println(circle);
        System.out.println(rectangle);
    }
}
```

**Expected Output**:
```
Circle [area=78.54, perimeter=31.42]
Rectangle [area=24.00, perimeter=20.00]
```

**Best Practices**:
- Anonymous classes can extend abstract classes but not final classes
- You can only extend one class (no multiple inheritance)
- Private fields in anonymous classes are accessible within the anonymous class

### Example 3: Anonymous Class for Thread Creation

**Problem Statement**: Create multiple threads with different behaviors using anonymous `Runnable` implementations.

**Implementation**:

```java
package academy.javaengineering.oop.anonymousclasses;

public class ThreadDemo {
    public static void main(String[] args) {
        Thread writer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("Writer: " + i);
                }
            }
        });

        Thread reader = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("Reader: " + i);
                }
            }
        });

        writer.start();
        reader.start();

        try {
            writer.join();
            reader.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Both threads completed");
    }
}
```

**Expected Output** (interleaved):
```
Writer: 1
Reader: 1
Writer: 2
Reader: 2
Writer: 3
Reader: 3
Writer: 4
Reader: 4
Writer: 5
Reader: 5
Both threads completed
```

**Best Practices**:
- Use anonymous classes for simple thread tasks
- For Java 8+, prefer lambda: `new Thread(() -> { ... }).start()`
- Always handle `InterruptedException` properly

## Medium Examples

### Example 1: Anonymous Class in a Factory Pattern

**Problem Statement**: Implement a notification factory that returns different notification types as anonymous class instances.

**Implementation**:

```java
package academy.javaengineering.oop.anonymousclasses;

interface Notification {
    void send(String message);
    String getType();
}

class NotificationFactory {
    static Notification create(String type) {
        return switch (type.toUpperCase()) {
            case "EMAIL" -> new Notification() {
                @Override
                public void send(String message) {
                    System.out.println("EMAIL: " + message);
                }

                @Override
                public String getType() { return "EMAIL"; }
            };
            case "SMS" -> new Notification() {
                @Override
                public void send(String message) {
                    System.out.println("SMS: " + message);
                }

                @Override
                public String getType() { return "SMS"; }
            };
            case "PUSH" -> new Notification() {
                @Override
                public void send(String message) {
                    System.out.println("PUSH: " + message);
                }

                @Override
                public String getType() { return "PUSH"; }
            };
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}

public class FactoryDemo {
    public static void main(String[] args) {
        Notification email = NotificationFactory.create("EMAIL");
        Notification sms = NotificationFactory.create("SMS");
        Notification push = NotificationFactory.create("PUSH");

        email.send("Your order has shipped");
        sms.send("Your code is 1234");
        push.send("New message received");
    }
}
```

**Expected Output**:
```
EMAIL: Your order has shipped
SMS: Your code is 1234
PUSH: New message received
```

**Code Walkthrough**: The factory method returns anonymous class instances based on the input type. Each anonymous class implements the `Notification` interface with type-specific behavior. The factory pattern combined with anonymous classes provides clean encapsulation.

**Alternative Solution**: You could use enum constants with abstract methods (see [26-enums](../26-enums/)) or strategy pattern with lambdas. Anonymous classes are best for implementations that need to maintain state or have multiple methods.

### Example 2: Anonymous Class for Custom Filtering

**Problem Statement**: Implement a custom filter using an anonymous class that filters a list of strings based on a complex rule.

**Implementation**:

```java
package academy.javaengineering.oop.anonymousclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface Filter<T> {
    boolean accept(T item);
}

class FilterUtil {
    static <T> List<T> filter(List<T> items, Filter<T> filter) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (filter.accept(item)) {
                result.add(item);
            }
        }
        return result;
    }
}

public class FilterDemo {
    public static void main(String[] args) {
        List<String> words = Arrays.asList(
                "hello", "world", "java", "programming", "hello", "java", "code"
        );

        // Anonymous class: filter words longer than 4 characters
        List<String> longWords = FilterUtil.filter(words, new Filter<String>() {
            @Override
            public boolean accept(String item) {
                return item.length() > 4;
            }
        });
        System.out.println("Words > 4 chars: " + longWords);

        // Anonymous class: filter unique words (case-insensitive)
        List<String> uniqueWords = FilterUtil.filter(words, new Filter<String>() {
            private java.util.Set<String> seen = new java.util.HashSet<>();

            @Override
            public boolean accept(String item) {
                return seen.add(item.toLowerCase());
            }
        });
        System.out.println("Unique words: " + uniqueWords);
    }
}
```

**Expected Output**:
```
Words > 4 chars: [world, programming, code]
Unique words: [hello, world, java, programming, code]
```

**Code Walkthrough**: The `Filter` interface has a single `accept` method. Anonymous classes implement this interface with different filtering logic. The second anonymous class maintains internal state (`seen` set) to track previously seen words.

**Alternative Solution**: Java 8+ lambdas: `words.stream().filter(w -> w.length() > 4).toList()`. But lambdas cannot maintain state between calls, making the unique filter harder to implement.

### Example 3: Anonymous Class vs Lambda Comparison

**Problem Statement**: Demonstrate the differences between anonymous classes and lambda expressions for various use cases.

**Implementation**:

```java
package academy.javaengineering.oop.anonymousclasses;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

interface MultiMethodInterface {
    void method1();
    void method2();
}

abstract class AbstractWorker {
    abstract void work();
    abstract void休息();
}

public class AnonymousVsLambdaDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Charlie", "Bob", "Alice");

        // 1. Anonymous class for Comparator (works in Java 7+)
        names.sort(new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return a.compareTo(b);
            }
        });
        System.out.println("Sorted (anonymous): " + names);

        // 2. Lambda for Comparator (Java 8+)
        names.sort((a, b) -> b.compareTo(a));
        System.out.println("Sorted (lambda): " + names);

        // 3. Anonymous class for abstract class (lambdas CANNOT do this)
        AbstractWorker worker = new AbstractWorker() {
            @Override
            void work() { System.out.println("Working..."); }

            @Override
            void休息() { System.out.println("Resting..."); }
        };
        worker.work();

        // 4. Anonymous class maintaining state (lambdas cannot easily do this)
        Runnable counter = new Runnable() {
            private int count = 0;

            @Override
            public void run() {
                count++;
                System.out.println("Count: " + count);
            }
        };
        counter.run();
        counter.run();
        counter.run();
    }
}
```

**Expected Output**:
```
Sorted (anonymous): [Alice, Bob, Charlie]
Sorted (lambda): [Charlie, Bob, Alice]
Working...
Count: 1
Count: 2
Count: 3
```

**Code Walkthrough**: Key differences:
1. Anonymous classes work for any interface, including multi-method interfaces
2. Lambdas only work for functional interfaces (single abstract method)
3. Anonymous classes can extend abstract classes; lambdas cannot
4. Anonymous classes can maintain instance state; lambdas capture effectively final variables only

## Hard Examples

### Example 1: Anonymous Class for Visitor Pattern Implementation

**Problem Statement**: Implement a Visitor pattern for a file system using anonymous classes to define visitor behavior at the point of use.

**Implementation**:

```java
package academy.javaengineering.oop.anonymousclasses;

import java.util.ArrayList;
import java.util.List;

interface FileVisitor {
    void visitFile(String name, long size);
    void visitDirectory(String name, int childCount);
}

abstract class FileSystemEntry {
    abstract void accept(FileVisitor visitor);
}

class File extends FileSystemEntry {
    private String name;
    private long size;

    File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    void accept(FileVisitor visitor) {
        visitor.visitFile(name, size);
    }

    String getName() { return name; }
    long getSize() { return size; }
}

class Directory extends FileSystemEntry {
    private String name;
    private List<FileSystemEntry> children = new ArrayList<>();

    Directory(String name) {
        this.name = name;
    }

    void add(FileSystemEntry entry) {
        children.add(entry);
    }

    @Override
    void accept(FileVisitor visitor) {
        visitor.visitDirectory(name, children.size());
        for (FileSystemEntry entry : children) {
            entry.accept(visitor);
        }
    }

    String getName() { return name; }
    List<FileSystemEntry> getChildren() { return children; }
}

class FileSystemAnalyzer {
    static void analyze(FileSystemEntry root) {
        System.out.println("=== Size Report ===");
        root.accept(new FileVisitor() {
            private long totalSize = 0;

            @Override
            public void visitFile(String name, long size) {
                totalSize += size;
                System.out.printf("  File: %-20s %d bytes%n", name, size);
            }

            @Override
            public void visitDirectory(String name, int childCount) {
                System.out.printf("  Dir:  %-20s (%d children)%n", name, childCount);
            }
        });

        System.out.println("\n=== File List ===");
        root.accept(new FileVisitor() {
            @Override
            public void visitFile(String name, long size) {
                System.out.println("  " + name);
            }

            @Override
            public void visitDirectory(String name, int childCount) {
                System.out.println("  [" + name + "]");
            }
        });
    }
}

public class VisitorDemo {
    public static void main(String[] args) {
        Directory root = new Directory("root");
        Directory src = new Directory("src");
        src.add(new File("Main.java", 1024));
        src.add(new File("Utils.java", 512));
        root.add(src);
        root.add(new File("README.md", 256));
        root.add(new File("pom.xml", 128));

        FileSystemAnalyzer.analyze(root);
    }
}
```

**Unit Tests**:

```java
package academy.javaengineering.oop.anonymousclasses;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;

class VisitorTest {
    @Test
    void testFileVisitor() {
        File file = new File("test.txt", 100);
        AtomicInteger visited = new AtomicInteger(0);

        file.accept(new FileVisitor() {
            @Override
            public void visitFile(String name, long size) {
                assertEquals("test.txt", name);
                assertEquals(100, size);
                visited.incrementAndGet();
            }

            @Override
            public void visitDirectory(String name, int childCount) {
                fail("Should not visit directory");
            }
        });

        assertEquals(1, visited.get());
    }
}
```

**Execution Flow**: The `accept` method on each element calls the visitor's appropriate method. Anonymous class visitors provide different behaviors — size calculation, listing, etc. The visitor pattern separates algorithms from object structures.

**Complexity**: O(n) where n is the total number of entries in the file system tree.

**Best Practices**:
- Use anonymous classes for visitor implementations when behavior is simple
- For complex visitors, create named classes
- Anonymous classes in visitor pattern keep the behavior visible at the call site

### Example 2: Anonymous Class with Complex State Management

**Problem Statement**: Build a rate limiter that uses anonymous classes to track request counts per client with different limiting strategies.

**Implementation**:

```java
package academy.javaengineering.oop.anonymousclasses;

import java.util.HashMap;
import java.util.Map;

interface RateLimiter {
    boolean allowRequest(String clientId);
    int getRemainingQuota(String clientId);
}

class RateLimiterFactory {
    static RateLimiter createTokenBucket(int maxTokens, long refillIntervalMs) {
        return new RateLimiter() {
            private final Map<String, int[]> buckets = new HashMap<>();

            @Override
            public boolean allowRequest(String clientId) {
                int[] bucket = buckets.computeIfAbsent(clientId, k -> new int[]{maxTokens});
                refill(bucket);
                if (bucket[0] > 0) {
                    bucket[0]--;
                    return true;
                }
                return false;
            }

            @Override
            public int getRemainingQuota(String clientId) {
                int[] bucket = buckets.getOrDefault(clientId, new int[]{maxTokens});
                refill(bucket);
                return bucket[0];
            }

            private void refill(int[] bucket) {
                // Simplified refill logic
                if (bucket[0] < maxTokens) {
                    bucket[0] = Math.min(maxTokens, bucket[0] + 1);
                }
            }
        };
    }

    static RateLimiter createFixedSizeWindow(int maxRequests, long windowMs) {
        return new RateLimiter() {
            private final Map<String, long[]> windows = new HashMap<>();

            @Override
            public boolean allowRequest(String clientId) {
                long now = System.currentTimeMillis();
                long[] window = windows.computeIfAbsent(clientId, k -> new long[]{0, now});

                if (now - window[1] > windowMs) {
                    window[0] = 0;
                    window[1] = now;
                }

                if (window[0] < maxRequests) {
                    window[0]++;
                    return true;
                }
                return false;
            }

            @Override
            public int getRemainingQuota(String clientId) {
                long now = System.currentTimeMillis();
                long[] window = windows.getOrDefault(clientId, new long[]{0, now});

                if (now - window[1] > windowMs) {
                    return maxRequests;
                }
                return Math.max(0, maxRequests - (int) window[0]);
            }
        };
    }
}

public class RateLimiterDemo {
    public static void main(String[] args) {
        RateLimiter limiter = RateLimiterFactory.createTokenBucket(3, 1000);

        System.out.println("Client A:");
        for (int i = 0; i < 5; i++) {
            boolean allowed = limiter.allowRequest("clientA");
            System.out.printf("  Request %d: allowed=%b, remaining=%d%n",
                    i + 1, allowed, limiter.getRemainingQuota("clientA"));
        }

        System.out.println("\nClient B:");
        for (int i = 0; i < 3; i++) {
            boolean allowed = limiter.allowRequest("clientB");
            System.out.printf("  Request %d: allowed=%b, remaining=%d%n",
                    i + 1, allowed, limiter.getRemainingQuota("clientB"));
        }
    }
}
```

**Execution Flow**: The factory creates anonymous class instances that implement rate limiting with different strategies. The token bucket tracks tokens per client. The fixed window tracks requests per time window. Each anonymous class maintains its own state (buckets or windows maps).

**Complexity**: O(1) per request for both strategies. Token bucket refill is amortized O(1).

**Best Practices**:
- Anonymous classes with state should be used sparingly
- For complex rate limiting, create named strategy classes
- Document the state management behavior clearly

## Exercises

### Easy

1. Create an anonymous class implementation of `Runnable` that prints "Hello from anonymous class" and run it.

2. Use an anonymous class to implement `Comparable<Integer>` for descending order comparison. Sort a list of integers.

3. Create an anonymous class that extends `Thread` and prints its thread name when `run()` is called.

### Medium

4. Implement a generic `Mapper<T, R>` interface with a `map(T input)` method. Use anonymous classes to create mappers that convert strings to integers, doubles, and uppercase strings.

5. Build a simple event bus using anonymous classes. The bus should support registering listeners and emitting events.

6. Create a `Validator<T>` interface with a `validate(T item)` method. Use anonymous classes to validate that a string is non-empty, an integer is positive, and an email matches a pattern.

### Hard

7. Implement a thread-safe cache using anonymous classes that implement a `Cache<K,V>` interface with `get`, `put`, `evict`, and `size` methods. Use `synchronized` blocks for thread safety.

8. Build a simple reactive stream using anonymous classes. The stream should support `map`, `filter`, and `forEach` operations with lazy evaluation.

9. Create a parser combinator library using anonymous classes that combine simple parsers (string, integer, regex) into complex parsers (key-value pair, JSON-like object).

## Interview Questions

### Easy

1. **What is an anonymous class in Java?**
   An anonymous class is a class without a name, declared and instantiated in a single expression. It can extend a class or implement an interface. The compiler generates a separate `.class` file (e.g., `OuterClass$1.class`) for each anonymous class.

2. **Can an anonymous class extend a class and implement an interface simultaneously?**
   No. An anonymous class can either extend one class OR implement one interface (or extend a class that implements an interface). Java does not support multiple inheritance.

3. **How does an anonymous class access local variables?**
   Anonymous classes can only access local variables that are final or effectively final (assigned exactly once). The variable's value is captured at creation time — subsequent changes to the variable are not reflected in the anonymous class.

### Intermediate

4. **When should you use an anonymous class instead of a lambda expression?**
   Use anonymous classes when: (a) implementing a class with multiple methods, (b) extending an abstract class, (c) needing instance state that persists across method calls, or (d) working with non-functional interfaces. Lambdas are preferred for single-method functional interfaces.

5. **What is the relationship between anonymous classes and inner classes?**
   Every anonymous class is implicitly an inner class (if in an instance context) or a static nested class (if in a static context). It has the same access rules and compilation behavior as inner classes, but without a name.

6. **How do anonymous classes interact with generics?**
   Anonymous classes can be generic, but the type parameter must be specified at the point of creation. For example: `new Comparator<String>() { ... }`. The type parameter is inferred from context in some cases.

### Hard

7. **What are the memory implications of creating many anonymous class instances?**
   Each anonymous class generates a separate `.class` file, increasing the metaspace footprint. Each instance holds a reference to the enclosing outer instance (if non-static), preventing garbage collection. In tight loops, prefer lambda expressions or pre-allocated strategy objects.

8. **How does the JVM handle anonymous class loading and what are the performance implications?**
   Anonymous classes are loaded lazily when first referenced. The classloader generates them with incrementing numeric suffixes. Class loading involves verification, preparation, and resolution phases. Creating many anonymous classes in hot paths can trigger excessive class loading and garbage collection of class metadata.

## Common Pitfalls

### 1. Modifying a Captured Local Variable

**Wrong**:
```java
void process() {
    int count = 0;
    Runnable r = new Runnable() {
        @Override
        public void run() {
            count++; // COMPILE ERROR — count must be final or effectively final
        }
    };
}
```

**Right**:
```java
void process() {
    int[] count = {0}; // Array is final, contents are mutable
    Runnable r = new Runnable() {
        @Override
        public void run() {
            count[0]++; // OK — modifying array contents, not the reference
            System.out.println("Count: " + count[0]);
        }
    };
    r.run();
}
```

### 2. Creating Too Many Anonymous Classes in a Loop

**Wrong**:
```java
void process(List<String> items) {
    for (String item : items) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println(item);
            }
        };
        new Thread(r).start();
    }
    // Creates N separate .class files — one per iteration
}
```

**Right**:
```java
void process(List<String> items) {
    Runnable template = new Runnable() {
        private String currentItem;

        Runnable withItem(String item) {
            this.currentItem = item;
            return this;
        }

        @Override
        public void run() {
            System.out.println(currentItem);
        }
    };

    for (String item : items) {
        new Thread(template.withItem(item)).start();
    }
}
```

Or better: use a lambda that captures the effectively final loop variable (Java 8+).

### 3. Forgetting That Anonymous Classes Hold Outer References

**Wrong**:
```java
class LargeObject {
    private byte[] data = new byte[1024 * 1024]; // 1MB

    void registerListener() {
        EventManager.register(new EventListener() {
            @Override
            public void onEvent() {
                System.out.println("Event received");
                // LargeObject cannot be garbage collected while listener exists
            }
        });
    }
}
```

**Right**:
```java
class LargeObject {
    private byte[] data = new byte[1024 * 1024];

    void registerListener() {
        // Use WeakReference or extract to a static method
        EventManager.register(LargeObject::handleEventStatic);
    }

    private static void handleEventStatic() {
        System.out.println("Event received");
    }
}
```

## Best Practices

1. **Prefer lambda expressions for functional interfaces** — They are more concise, don't create separate class files, and don't hold outer references unnecessarily.
2. **Keep anonymous classes small** — If the implementation is more than 10 lines, create a named class instead.
3. **Watch for memory leaks** — Anonymous classes in long-lived callbacks can prevent garbage collection of outer instances.
4. **Use anonymous classes for abstract class implementations** — Lambdas cannot extend abstract classes, so anonymous classes are necessary.
5. **Be aware of compilation overhead** — Each anonymous class generates a separate `.class` file, which can slow compilation and increase JAR size in large projects.

## Real World Usage

### How JDK Uses This

The JDK uses anonymous classes in `Collections.sort()` with anonymous `Comparator` implementations. `Executors.newSingleThreadExecutor()` returns anonymous `ExecutorService` implementations. Swing/AWT event handlers historically used anonymous `ActionListener` and `MouseListener` implementations.

### How Spring Uses This

Spring uses anonymous classes in `@Bean` factory methods, `RestTemplate` error handlers, and test mocking with `Mockito.mock()` returning anonymous class instances. Spring Security's `AuthorizationManager` implementations are often created as anonymous classes in configuration.

### Enterprise Usage

Enterprise applications use anonymous classes for callback implementations, event handlers, filter predicates, and one-off strategy implementations. Legacy codebases (pre-Java 8) rely heavily on anonymous classes for all functional interface implementations. Modern codebases prefer lambdas but still use anonymous classes for abstract class extensions.

## References

- [Oracle — Anonymous Classes](https://docs.oracle.com/en/java/javase/21/java/javaOO/anonymousclasses.html)
- [Baeldung — Java Anonymous Classes](https://www.baeldung.com/java-anonymous-classes)
- [Effective Java, Item 22: Use interfaces only for defining types](https://books.google.com/books?id=BIoul6j2KcIC)
- [JLS — Anonymous Class Declarations](https://docs.oracle.com/javase/specs/jls/se17/html/jls-15.html#jls-15.9.5)

## Summary

- Anonymous classes are unnamed classes declared and instantiated in a single expression
- They can implement interfaces or extend abstract classes (but not both simultaneously)
- Local variables accessed by anonymous classes must be final or effectively final
- Anonymous classes are implicitly inner classes and hold references to outer instances
- Use lambda expressions for functional interfaces; use anonymous classes for abstract classes
- Watch for memory leaks — anonymous classes in long-lived callbacks can prevent garbage collection

**Next Step**: [30-functional-interfaces](../30-functional-interfaces/)
