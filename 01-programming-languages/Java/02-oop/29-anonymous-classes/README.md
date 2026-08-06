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

---

## Continue Reading

- Part 2
- Part 3
