# Inner Classes

## Introduction

An inner class in Java is a class declared inside another class. Inner classes are a powerful mechanism for grouping classes that belong together logically, improving encapsulation, and writing more readable and maintainable code. Java supports four types of nested classes: inner classes (non-static member classes), static nested classes, local classes, and anonymous classes. Inner classes have access to all members of the enclosing class, including private fields and methods, which makes them ideal for implementing callbacks, iterators, and helper classes that are tightly coupled to their outer class. The compiler generates a synthetic accessor method for private members accessed by inner classes, which is an important implementation detail to understand for debugging and performance.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Declare and use non-static inner classes with proper access to the enclosing class
- [ ] Understand the implicit reference from inner class to its enclosing instance
- [ ] Distinguish between inner classes, static nested classes, local classes, and anonymous classes
- [ ] Apply inner classes for callbacks, iterators, and helper implementations

## Prerequisites

- [02-classes](../02-classes/) — Nested classes are still classes
- [03-objects](../03-objects/) — Inner classes hold implicit references to enclosing objects
- [08-encapsulation](../08-encapsulation/) — Inner classes enhance encapsulation
- [12-interfaces](../12-interfaces/) — Inner classes often implement interfaces

## Why This Concept Exists

### The Problem

When building complex systems, some classes are only relevant in the context of another class. For example, a `Map` implementation needs an `Entry` class, but `Entry` has no meaning outside of `Map`. Declaring it as a top-level class pollutes the namespace and exposes implementation details.

### The Solution

Inner classes allow you to nest a class inside another class, keeping related code together. The inner class can access the enclosing class's private members, enabling tight coupling without exposing internals. This pattern is used for iterators, comparators, event handlers, and builder classes.

### Real-World Analogy

Think of an inner class like a room inside a house. The room (inner class) has direct access to the house's utilities (fields and methods), including private rooms (private members). You wouldn't build a detached kitchen for a specific house — you build it inside the house because it belongs there.

## Internal Working

### JVM Perspective

When you compile a class like:

```java
class Outer {
    private int x = 10;
    class Inner {
        void printX() { System.out.println(x); }
    }
}
```

The compiler generates two separate `.class` files:
- `Outer.class` — the enclosing class
- `Outer$Inner.class` — the inner class

The inner class bytecode contains a synthetic reference to the outer class instance, stored as `this$0`. This is how `Inner` accesses `Outer.x` — through this implicit reference.

### Memory Representation

```
Heap Memory:
┌─────────────────────┐
│ Outer instance       │
│   x = 10            │
│   └─────────────────┼───┐
└─────────────────────┘   │
                          ▼
┌─────────────────────┐
│ Inner instance       │
│   this$0 ───────────┘   │
└─────────────────────┘
```

The inner class instance holds a reference to the outer instance. This means an inner class instance cannot exist without its enclosing instance, and the outer instance is kept alive as long as the inner instance exists — a potential source of memory leaks.

## Syntax

```java
// Non-static inner class
class Outer {
    private int outerField;

    class Inner {
        void display() {
            System.out.println("Outer field: " + outerField);
        }
    }
}

// Creating inner class instances
Outer outer = new Outer();
Outer.Inner inner = outer.new Inner();
inner.display();

// Static nested class (no implicit outer reference)
class Outer {
    private static int staticField;

    static class Nested {
        void display() {
            System.out.println("Static field: " + staticField);
        }
    }
}

// Creating static nested class instances
Outer.Nested nested = new Outer.Nested();
nested.display();
```

## Easy Examples

### Example 1: Basic Inner Class — Accessing Outer Fields

**Problem Statement**: Create an inner class that accesses and displays the enclosing class's private fields.

**Implementation**:

```java
package academy.javaengineering.oop.innerclasses;

class University {
    private String name = "MIT";
    private int yearFounded = 1861;

    class Department {
        private String deptName;

        Department(String deptName) {
            this.deptName = deptName;
        }

        void printInfo() {
            System.out.printf("Department: %s at %s (est. %d)%n",
                    deptName, name, yearFounded);
        }
    }
}

public class InnerClassDemo {
    public static void main(String[] args) {
        University mit = new University();
        University.Department cs = mit.new Department("Computer Science");
        cs.printInfo();
    }
}
```

**Expected Output**:
```
Department: Computer Science at MIT (est. 1861)
```

**Best Practices**:
- Use inner classes when the nested class is logically part of the outer class
- Keep inner class access to outer members minimal and intentional
- Prefer static nested classes when the inner class doesn't need access to outer instance members

### Example 2: Inner Class for Callback Implementation

**Problem Statement**: Implement a button click handler using an inner class that reacts to events.

**Implementation**:

```java
package academy.javaengineering.oop.innerclasses;

interface ClickListener {
    void onClick(String buttonName);
}

class Button {
    private String name;
    private ClickListener listener;

    Button(String name) {
        this.name = name;
    }

    void setOnClickListener(ClickListener listener) {
        this.listener = listener;
    }

    void click() {
        if (listener != null) {
            listener.onClick(name);
        }
    }
}

class ButtonHandler {
    private int clickCount = 0;

    void setupButton() {
        Button btn = new Button("Submit");

        btn.setOnClickListener(new ClickListener() {
            @Override
            public void onClick(String buttonName) {
                clickCount++;
                System.out.println("Button '" + buttonName + "' clicked " + clickCount + " times");
            }
        });

        btn.click();
        btn.click();
        btn.click();
    }
}

public class CallbackDemo {
    public static void main(String[] args) {
        ButtonHandler handler = new ButtonHandler();
        handler.setupButton();
    }
}
```

**Expected Output**:
```
Button 'Submit' clicked 1 times
Button 'Submit' clicked 2 times
Button 'Submit' clicked 3 times
```

**Best Practices**:
- Use inner class callbacks when you need access to the enclosing instance's state
- Keep callback implementations concise and focused
- Consider lambda expressions (Java 8+) for simple functional interfaces

### Example 3: Inner Class as an Iterator

**Problem Statement**: Implement a custom collection with an inner class iterator.

**Implementation**:

```java
package academy.javaengineering.oop.innerclasses;

class WordCollection {
    private String[] words = {"Java", "is", "powerful", "and", "flexible"};

    class WordIterator {
        private int index = 0;

        boolean hasNext() {
            return index < words.length;
        }

        String next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return words[index++];
        }
    }

    WordIterator iterator() {
        return new WordIterator();
    }
}

public class IteratorDemo {
    public static void main(String[] args) {
        WordCollection collection = new WordCollection();
        WordCollection.WordIterator iter = collection.iterator();

        while (iter.hasNext()) {
            System.out.print(iter.next() + " ");
        }
        System.out.println();
    }
}
```

**Expected Output**:
```
Java is powerful and flexible
```

**Best Practices**:
- Inner class iterators can access the collection's private array directly
- The iterator is tightly coupled to its collection — this is intentional
- For public APIs, prefer implementing `java.util.Iterator<T>` interface

## Medium Examples

### Example 1: Inner Class in a Builder Pattern

**Problem Statement**: Implement a `Pizza` builder using an inner class to chain configuration calls.

**Implementation**:

```java
package academy.javaengineering.oop.innerclasses;

class Pizza {
    private final String size;
    private final boolean cheese;
    private final boolean pepperoni;
    private final boolean mushrooms;
    private final boolean olives;

    private Pizza(Builder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.mushrooms = builder.mushrooms;
        this.olives = builder.olives;
    }

    static class Builder {
        private String size;
        private boolean cheese;
        private boolean pepperoni;
        private boolean mushrooms;
        private boolean olives;

        Builder(String size) {
            this.size = size;
        }

        Builder cheese(boolean cheese) {
            this.cheese = cheese;
            return this;
        }

        Builder pepperoni(boolean pepperoni) {
            this.pepperoni = pepperoni;
            return this;
        }

        Builder mushrooms(boolean mushrooms) {
            this.mushrooms = mushrooms;
            return this;
        }

        Builder olives(boolean olives) {
            this.olives = olives;
            return this;
        }

        Pizza build() {
            return new Pizza(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Pizza[size=%s, cheese=%b, pepperoni=%b, mushrooms=%b, olives=%b]",
                size, cheese, pepperoni, mushrooms, olives);
    }
}

public class BuilderDemo {
    public static void main(String[] args) {
        Pizza margherita = new Pizza.Builder("Large")
                .cheese(true)
                .build();

        Pizza special = new Pizza.Builder("Medium")
                .cheese(true)
                .pepperoni(true)
                .mushrooms(true)
                .olives(true)
                .build();

        System.out.println(margherita);
        System.out.println(special);
    }
}
```

**Expected Output**:
```
Pizza[size=Large, cheese=true, pepperoni=false, mushrooms=false, olives=false]
Pizza[size=Medium, cheese=true, pepperoni=true, mushrooms=true, olives=true]
```

**Code Walkthrough**: The `Builder` is a static inner class. The `Pizza` constructor is private and accepts a `Builder` instance. The builder's setter methods return `this` for chaining. This pattern is used extensively in the JDK (`Calendar.Builder`, `StringBuilder`) and frameworks (Lombok's `@Builder`).

**Alternative Solution**: Lombok's `@Builder` annotation generates this boilerplate automatically, but understanding the underlying inner class mechanism is essential for debugging and customization.

### Example 2: Inner Class for Event Handling

**Problem Statement**: Build a simple event system where listeners are implemented as inner classes.

**Implementation**:

```java
package academy.javaengineering.oop.innerclasses;

import java.util.ArrayList;
import java.util.List;

class EventEmitter {
    interface EventListener {
        void onEvent(String eventName, String data);
    }

    private final List<EventListener> listeners = new ArrayList<>();

    void addListener(EventListener listener) {
        listeners.add(listener);
    }

    void emit(String eventName, String data) {
        for (EventListener listener : listeners) {
            listener.onEvent(eventName, data);
        }
    }
}

class Application {
    private final EventEmitter emitter = new EventEmitter();
    private int eventCount = 0;

    void start() {
        emitter.addListener(new EventEmitter.EventListener() {
            @Override
            public void onEvent(String eventName, String data) {
                eventCount++;
                System.out.printf("[LOG] Event #%d: %s -> %s%n", eventCount, eventName, data);
            }
        });

        emitter.addListener(new EventEmitter.EventListener() {
            @Override
            public void onEvent(String eventName, String data) {
                if (eventName.equals("ERROR")) {
                    System.err.println("[ALERT] Critical error received: " + data);
                }
            }
        });

        emitter.emit("START", "Application started");
        emitter.emit("DATA", "Processing payload");
        emitter.emit("ERROR", "Connection failed");
    }
}

public class EventDemo {
    public static void main(String[] args) {
        Application app = new Application();
        app.start();
    }
}
```

**Expected Output**:
```
[LOG] Event #1: START -> Application started
[LOG] Event #2: DATA -> Processing payload
[LOG] Event #3: ERROR -> Connection failed
[ALERT] Critical error received: Connection failed
```

**Code Walkthrough**: Two anonymous inner classes register as listeners. The first logs all events and tracks a count from the enclosing `Application` instance. The second filters for error events. This demonstrates how inner classes can access and modify enclosing instance state.

### Example 3: Inner Class vs Static Nested Comparison

**Problem Statement**: Demonstrate the difference between non-static inner class and static nested class regarding memory and references.

**Implementation**:

```java
package academy.javaengineering.oop.innerclasses;

class Outer {
    private int outerValue = 100;
    private static int staticValue = 200;

    class InnerClass {
        void display() {
            System.out.println("InnerClass: outerValue=" + outerValue
                    + ", staticValue=" + staticValue);
        }
    }

    static class StaticNested {
        void display() {
            // System.out.println(outerValue); // COMPILE ERROR — no outer reference
            System.out.println("StaticNested: staticValue=" + staticValue);
        }
    }
}

public class InnerVsStaticDemo {
    public static void main(String[] args) {
        Outer outer = new Outer();

        // Non-static: requires enclosing instance
        Outer.InnerClass inner = outer.new InnerClass();
        inner.display();

        // Static: no enclosing instance needed
        Outer.StaticNested nested = new Outer.StaticNested();
        nested.display();

        // Static nested can be created before outer instance
        Outer.StaticNested nested2 = new Outer.StaticNested();
        nested2.display();
    }
}
```

**Expected Output**:
```
InnerClass: outerValue=100, staticValue=200
StaticNested: staticValue=200
StaticNested: staticValue=200
```

**Code Walkthrough**: `InnerClass` holds an implicit reference to `outer` — it cannot exist without it. `StaticNested` has no such reference and behaves like a top-level class that happens to be scoped inside another class. The static nested class is more memory-efficient when it doesn't need access to instance members.

**Alternative Solution**: If you only need access to static members of the outer class, always use a static nested class. It avoids the memory overhead of the implicit outer reference.

## Hard Examples

### Example 1: Thread-Safe Singleton with Inner Class

**Problem Statement**: Implement a thread-safe singleton using the Initialization-on-Demand Holder idiom with an inner class.

**Implementation**:

```java
package academy.javaengineering.oop.innerclasses;

class DatabaseConnection {
    private DatabaseConnection() {
        System.out.println("Database connection established");
    }

    private static class Holder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }

    static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }

    void query(String sql) {
        System.out.println("Executing: " + sql);
    }
}

public class SingletonDemo {
    public static void main(String[] args) {
        Runnable task = () -> {
            DatabaseConnection conn = DatabaseConnection.getInstance();
            conn.query("SELECT * FROM users");
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

**Execution Flow**: The `Holder` class is not loaded until `getInstance()` is called. Class loading is guaranteed to be thread-safe by the JVM, so `Holder.INSTANCE` is initialized exactly once. This is the most efficient thread-safe singleton pattern — no `synchronized`, no volatile, no double-checked locking.

**Complexity**: O(1) — the instance is created once and cached permanently.

**Best Practices**:
- Use the Holder idiom for lazy, thread-safe singletons
- The inner class is loaded only when referenced, providing true lazy initialization
- This pattern works because JVM class loading is inherently thread-safe

### Example 2: Deeply Nested Inner Classes for Tree Structure

**Problem Statement**: Implement a binary tree where node classes are inner classes of the tree, demonstrating deep nesting and access patterns.

**Implementation**:

```java
package academy.javaengineering.oop.innerclasses;

class BinaryTree {
    private Node root;
    private int size = 0;

    private class Node {
        int data;
        Node left, right;

        Node(int data) {
            this.data = data;
        }

        void insert(int value) {
            if (value < data) {
                if (left == null) left = new Node(value);
                else left.insert(value);
            } else if (value > data) {
                if (right == null) right = new Node(value);
                else right.insert(value);
            }
        }

        void inorderTraversal() {
            if (left != null) left.inorderTraversal();
            System.out.print(data + " ");
            if (right != null) right.inorderTraversal();
        }

        boolean contains(int value) {
            if (value == data) return true;
            if (value < data && left != null) return left.contains(value);
            if (value > data && right != null) return right.contains(value);
            return false;
        }
    }

    void insert(int value) {
        if (root == null) {
            root = new Node(value);
        } else {
            root.insert(value);
        }
        size++;
    }

    void printInorder() {
        if (root != null) root.inorderTraversal();
        System.out.println();
    }

    boolean contains(int value) {
        return root != null && root.contains(value);
    }

    int getSize() { return size; }
}

public class TreeDemo {
    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);

        System.out.print("Inorder: ");
        tree.printInorder();
        System.out.println("Contains 40: " + tree.contains(40));
        System.out.println("Contains 35: " + tree.contains(35));
        System.out.println("Size: " + tree.getSize());
    }
}
```

**Execution Flow**: Each `Node` is an inner class that can access `BinaryTree.size`. Nodes recursively insert and traverse. The tree owns all nodes — creating a `Node` outside the tree is impossible since it's an inner class.

**Complexity**: Insert and search are O(h) where h is the tree height. For a balanced tree, O(log n). For a degenerate tree, O(n).

**Best Practices**:
- Use inner classes when nodes/elements should not exist independently
- Keep traversal logic within the node class for encapsulation
- Use static nested classes for nodes if they don't need parent references

## Exercises

### Easy

1. Create a `Car` class with an inner `Engine` class. The `Engine` should have a `start()` method that prints the car's make and model along with "Engine started."

2. Write a program with an outer class `Library` and an inner class `Book`. The `Book` class should access the library's name and display "This book belongs to [library name]."

3. Create a `Calculator` class with an inner `History` class that tracks the last 5 operations performed.

### Medium

4. Implement a `ShoppingCart` with an inner class `CartItem` that tracks product name, quantity, and price. Add a method to calculate the total.

5. Build a `TaskManager` with an inner class `Task` that has priority, description, and status. Implement a method to find the highest-priority incomplete task.

6. Create a `NetworkRouter` with an inner class `RoutingTable` that maps destination IPs to next-hop addresses.

### Hard

7. Implement a doubly-linked list using inner classes for `Node`. Each node should have `next` and `prev` references. Add methods for `addFirst`, `addLast`, `remove`, and `printForward`/`printBackward`.

8. Build a JSON parser using inner classes that can parse simple key-value objects with nested structures.

9. Design a thread-safe cache using an inner class for the cache entry that includes expiration timestamps.

## Interview Questions

### Easy

1. **What is an inner class in Java?**
   A class declared inside another class. It has access to all members of the enclosing class, including private members. The compiler generates a synthetic reference from the inner class to the outer instance.

2. **How do you create an instance of a non-static inner class?**
   You must first have an instance of the outer class: `Outer outer = new Outer(); Outer.Inner inner = outer.new Inner();`. The `new` operator is prefixed with the outer instance reference.

3. **What is the difference between inner class and static nested class?**
   An inner class has an implicit reference to the enclosing instance and can access its instance members. A static nested class has no such reference and can only access static members of the outer class.

### Intermediate

4. **Why does the compiler generate a synthetic accessor for inner classes accessing private members?**
   Java's access control prevents direct access to private members from outside a class. Since inner and outer classes compile to separate `.class` files, the compiler generates package-private accessor methods to maintain encapsulation while allowing the inner class to access private fields.

5. **What memory leak risks do inner classes pose?**
   An inner class instance holds a reference to the outer instance, preventing garbage collection even after the outer object is no longer needed. This is especially dangerous in anonymous inner classes used in long-lived callbacks or event listeners.

6. **Can an inner class be `public`, `private`, or `protected`?**
   Yes. Inner classes can have any access modifier. A private inner class is only accessible within the outer class. A protected inner class is accessible in the same package and subclasses.

### Hard

7. **How does the JVM implement inner class access to private members of the outer class?**
   The compiler creates synthetic bridge methods (package-private) in the outer class that get and set the private fields. The inner class calls these synthetic methods instead of the private fields directly. This is visible in bytecode analysis with tools like `javap`.

8. **What are the performance implications of using inner classes versus static nested classes?**
   Inner classes create an extra object reference (the outer instance) and require synthetic accessor methods for private member access. Static nested classes avoid this overhead. For performance-critical code, prefer static nested classes when the inner class doesn't need access to outer instance members.

## Common Pitfalls

### 1. Memory Leak from Anonymous Inner Class

**Wrong**:
```java
class Timer {
    void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // This anonymous inner class holds a reference to Timer
                    // preventing it from being garbage collected
                    process();
                }
            }
        }).start();
    }
    void process() { /* ... */ }
}
```

**Right**:
```java
class Timer {
    void start() {
        new Thread(() -> {
            while (true) {
                process();
            }
        }).start();
    }
    void process() { /* ... */ }
}
```

Lambda expressions capture only effectively final local variables, not the enclosing instance, avoiding the memory leak.

### 2. Confusing Inner Class and Static Nested Class

**Wrong**:
```java
class Outer {
    int x = 10;

    static class Inner {
        void display() {
            System.out.println(x); // COMPILE ERROR — cannot access instance field
        }
    }
}
```

**Right**:
```java
class Outer {
    int x = 10;

    class Inner {
        void display() {
            System.out.println(x); // OK — has reference to outer instance
        }
    }
}
```

Use a non-static inner class when you need access to outer instance members. Use static nested when you don't.

### 3. Creating Inner Class Instances Incorrectly

**Wrong**:
```java
Outer.Inner inner = new Outer.Inner(); // COMPILE ERROR
```

**Right**:
```java
Outer outer = new Outer();
Outer.Inner inner = outer.new Inner();
```

Non-static inner class instances are always tied to an outer class instance. You cannot create them without an enclosing object.

## Best Practices

1. **Prefer static nested classes when possible** — They are more memory-efficient and don't create implicit references to outer instances.
2. **Avoid exposing inner classes in public APIs** — Inner classes are implementation details. Use package-private or private access.
3. **Watch for memory leaks** — Inner classes (especially anonymous ones) in long-lived callbacks can prevent garbage collection of the outer instance.
4. **Use lambda expressions for simple callbacks** — They are more concise and don't hold references to the enclosing instance unless they capture local variables.
5. **Document the relationship** — If an inner class needs access to outer members, document which members it uses and why.

## Real World Usage

### How JDK Uses This

`HashMap.Node` is a static nested class (not inner) to avoid holding a reference to the `HashMap` for every entry. `AbstractMap.SimpleEntry` and `SimpleImmutableEntry` are static nested classes. `Thread` uses inner classes for `State` enum and `UncaughtExceptionHandler`.

### How Spring Uses This

Spring's `BeanDefinitionHolder` and `AnnotatedBeanDefinitionReader` use inner classes for contextual helpers. `@Configuration` classes use inner `@Bean` methods that return objects created by inner class logic. `TestPropertySource` uses inner annotations.

### Enterprise Usage

Enterprise applications use inner classes for callback implementations, event handlers, iterator patterns, builder patterns, and thread-local storage. The builder pattern (inner class `Builder`) is ubiquitous in configuration APIs and data transfer objects.

## References

- [Oracle — Nested Classes Tutorial](https://docs.oracle.com/en/java/javase/21/java/javaOO/nested.html)
- [Effective Java, Item 24: Favor static member classes over nonstatic](https://books.google.com/books?id=BIoul6j2KcIC)
- [Baeldung — Java Inner Classes](https://www.baeldung.com/java-inner-class)
- [Baeldung — Memory Leaks with Inner Classes](https://www.baeldung.com/java-memory-leaks-inner-class)

## Summary

- Inner classes have an implicit reference to the enclosing instance and can access its private members
- Static nested classes lack this reference and are more memory-efficient
- Use inner classes for logically coupled helper classes, builders, iterators, and callbacks
- Watch for memory leaks — inner classes can prevent garbage collection of the outer instance
- Prefer lambda expressions for simple functional interface implementations over anonymous inner classes

**Next Step**: [28-nested-classes](../28-nested-classes/)
