# Inner Classes

## Introduction

Inner classes are classes defined within another class. They provide better encapsulation, access to the enclosing class's members, and cleaner code organization.

## Learning Objectives

By the end of this topic, you will be able to:

- Understand the four types of inner classes
- Declare and use member inner classes
- Declare and use static nested classes
- Declare and use local classes
- Declare and use anonymous classes
- Choose the appropriate type for each scenario

## Prerequisites

- Classes and Objects
- Encapsulation
- Methods

## Why This Concept Exists

### The Problem

Without inner classes:

- Helper classes pollute the namespace
- Tight coupling requires exposing implementation details
- No clean way to associate helper logic with a class

### The Solution

Inner classes provide:

- **Encapsulation**: Hide helper classes inside their owner
- **Access**: Direct access to enclosing class members
- **Organization**: Keep related code together
- **Readability**: Define callbacks and handlers inline

### Real-World Analogy

Think of inner classes as **rooms inside a house**:

- The house (outer class) contains rooms (inner classes)
- Rooms can access the house's utilities (outer class members)
- Some rooms are private (private inner class)
- Some rooms are shared (static nested class)
- Some rooms are temporary (local/anonymous classes)

## Types of Inner Classes

| Type | Declared In | Access to Outer | Use Case |
|------|-------------|-----------------|----------|
| Member | Class body | Instance members | Helper classes |
| Static Nested | Class body | Static members only | Utility classes |
| Local | Method | Local variables (effectively final) | Short-lived helpers |
| Anonymous | Expression | Local variables (effectively final) | Inline implementations |

## Syntax

### Member Inner Class

```java
public class Outer {
    private int x = 10;
    
    public class Inner {
        public void printX() {
            System.out.println(x); // Can access outer's private field
        }
    }
}

// Usage
Outer outer = new Outer();
Outer.Inner inner = outer.new Inner();
inner.printX();
```

### Static Nested Class

```java
public class Outer {
    private static int y = 20;
    
    public static class Nested {
        public void printY() {
            System.out.println(y); // Can access outer's static field
        }
    }
}

// Usage - no outer instance needed
Outer.Nested nested = new Outer.Nested();
nested.printY();
```

### Local Class

```java
public void method() {
    int localVar = 30;
    
    class Local {
        public void printVar() {
            System.out.println(localVar); // Access local variable
        }
    }
    
    Local local = new Local();
    local.printVar();
}
```

### Anonymous Class

```java
Runnable runnable = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running...");
    }
};
```

## Easy Examples

### Example 1: Member Inner Class - LinkedList

**Problem Statement**: Implement a singly linked list using a member inner class for nodes.

**Implementation**:

```java
public class SinglyLinkedList<T> {
    private Node<T> head;
    private int size;
    
    // Inner class - has access to outer class's private members
    private static class Node<T> {
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = head;
        head = newNode;
        size++;
    }
    
    public T removeFirst() {
        if (head == null) {
            throw new java.util.NoSuchElementException("List is empty");
        }
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
}
```

**Output**:
```java
SinglyLinkedList<String> list = new SinglyLinkedList<>();
list.addFirst("World");
list.addFirst("Hello");
System.out.println(list.removeFirst()); // Hello
System.out.println(list.removeFirst()); // World
System.out.println(list.size()); // 0
```

**Complexity**: O(1) for addFirst, O(1) for removeFirst

**Best Practices**:
- Use static nested classes when you don't need access to outer instance
- Make inner classes private to hide implementation details
- Use generics for type safety

### Example 2: Static Nested Class - Builder Pattern

**Problem Statement**: Implement the Builder pattern using a static nested class.

**Implementation**:

```java
public class HttpRequest {
    private final String url;
    private final String method;
    private final java.util.Map<String, String> headers;
    private final String body;
    
    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = java.util.Collections.unmodifiableMap(builder.headers);
        this.body = builder.body;
    }
    
    public String getUrl() { return url; }
    public String getMethod() { return method; }
    public java.util.Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
    
    // Static nested class - Builder
    public static class Builder {
        private final String url;
        private String method = "GET";
        private java.util.Map<String, String> headers = new java.util.HashMap<>();
        private String body;
        
        public Builder(String url) {
            this.url = url;
        }
        
        public Builder method(String method) {
            this.method = method;
            return this;
        }
        
        public Builder header(String key, String value) {
            headers.put(key, value);
            return this;
        }
        
        public Builder body(String body) {
            this.body = body;
            return this;
        }
        
        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
```

**Output**:
```java
HttpRequest request = new HttpRequest.Builder("https://api.example.com/users")
    .method("POST")
    .header("Content-Type", "application/json")
    .header("Authorization", "Bearer token123")
    .body("{\"name\": \"John\"}")
    .build();

System.out.println(request.getUrl());    // https://api.example.com/users
System.out.println(request.getMethod()); // POST
System.out.println(request.getHeaders()); // {Content-Type=application/json, Authorization=Bearer token123}
```

## Medium Examples

### Example 3: Local Class - Custom Comparator

**Problem Statement**: Use a local class to create a custom comparator for sorting.

**Implementation**:

```java
import java.util.Arrays;
import java.util.Comparator;

public class LocalClassExample {
    
    public static void sortEmployees(Employee[] employees) {
        // Local class defined inside method
        class EmployeeComparator implements Comparator<Employee> {
            @Override
            public int compare(Employee e1, Employee e2) {
                // Sort by department, then by name
                int deptCompare = e1.getDepartment().compareTo(e2.getDepartment());
                if (deptCompare != 0) {
                    return deptCompare;
                }
                return e1.getName().compareTo(e2.getName());
            }
        }
        
        Arrays.sort(employees, new EmployeeComparator());
    }
    
    public static void printEmployees(Employee[] employees) {
        for (Employee emp : employees) {
            System.out.printf("  %s - %s (Age: %d)%n",
                emp.getName(), emp.getDepartment(), emp.getAge());
        }
    }
}

class Employee {
    private final String name;
    private final String department;
    private final int age;
    
    public Employee(String name, String department, int age) {
        this.name = name;
        this.department = department;
        this.age = age;
    }
    
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getAge() { return age; }
}
```

**Output**:
```java
Employee[] employees = {
    new Employee("Alice", "Engineering", 30),
    new Employee("Bob", "Marketing", 25),
    new Employee("Charlie", "Engineering", 35),
    new Employee("Diana", "Marketing", 28)
};

System.out.println("Before sorting:");
LocalClassExample.printEmployees(employees);

LocalClassExample.sortEmployees(employees);

System.out.println("\nAfter sorting:");
LocalClassExample.printEmployees(employees);
// Sorted by department, then by name
// Alice - Engineering (Age: 30)
// Charlie - Engineering (Age: 35)
// Bob - Marketing (Age: 25)
// Diana - Marketing (Age: 28)
```

**Complexity**: O(n log n) for sorting

### Example 4: Anonymous Class - Event Handler

**Problem Statement**: Implement an event handling system using anonymous classes.

**Implementation**:

```java
// Event interface
public interface EventListener {
    void onEvent(String eventType, String data);
}

// Event source
public class EventSource {
    private final java.util.List<EventListener> listeners = new java.util.ArrayList<>();
    
    public void addListener(EventListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }
    
    public void fireEvent(String eventType, String data) {
        for (EventListener listener : listeners) {
            listener.onEvent(eventType, data);
        }
    }
}

// Usage with anonymous classes
public class EventDemo {
    public static void main(String[] args) {
        EventSource source = new EventSource();
        
        // Anonymous class implementation
        source.addListener(new EventListener() {
            @Override
            public void onEvent(String eventType, String data) {
                System.out.println("Logger: " + eventType + " - " + data);
            }
        });
        
        source.addListener(new EventListener() {
            @Override
            public void onEvent(String eventType, String data) {
                System.out.println("Processor: " + eventType + " - " + data);
            }
        });
        
        source.fireEvent("USER_LOGIN", "john_doe");
        // Logger: USER_LOGIN - john_doe
        // Processor: USER_LOGIN - john_doe
    }
}
```

**Output**:
```java
Logger: USER_LOGIN - john_doe
Processor: USER_LOGIN - john_doe
```

**Best Practices**:
- Anonymous classes are great for one-time implementations
- For Java 8+, consider lambda expressions for functional interfaces
- Keep anonymous classes small and focused

## Hard Examples

### Example 5: Inner Class - Tree Traversal

**Problem Statement**: Implement a binary tree with an inner iterator class.

**Implementation**:

```java
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BinaryTree<T> implements Iterable<T> {
    private Node<T> root;
    
    private static class Node<T> {
        T data;
        Node<T> left;
        Node<T> right;
        
        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }
    
    public void add(T data) {
        root = addRecursive(root, data);
    }
    
    private Node<T> addRecursive(Node<T> current, T data) {
        if (current == null) {
            return new Node<>(data);
        }
        
        if (((Comparable<T>) data).compareTo(current.data) < 0) {
            current.left = addRecursive(current.left, data);
        } else if (((Comparable<T>) data).compareTo(current.data) > 0) {
            current.right = addRecursive(current.right, data);
        }
        
        return current;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new InOrderIterator();
    }
    
    // Inner iterator class
    private class InOrderIterator implements Iterator<T> {
        private final Stack<Node<T>> stack = new Stack<>();
        
        InOrderIterator() {
            pushLeft(root);
        }
        
        private void pushLeft(Node<T> node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }
        
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            Node<T> node = stack.pop();
            pushLeft(node.right);
            return node.data;
        }
    }
    
    // Inner class for tree statistics
    public class TreeStatistics {
        private int nodeCount;
        private int height;
        
        public void calculate() {
            nodeCount = 0;
            height = calculateHeight(root);
            countNodes(root);
        }
        
        private int calculateHeight(Node<T> node) {
            if (node == null) {
                return 0;
            }
            return 1 + Math.max(
                calculateHeight(node.left),
                calculateHeight(node.right)
            );
        }
        
        private void countNodes(Node<T> node) {
            if (node != null) {
                nodeCount++;
                countNodes(node.left);
                countNodes(node.right);
            }
        }
        
        public int getNodeCount() { return nodeCount; }
        public int getHeight() { return height; }
    }
    
    public TreeStatistics getStatistics() {
        TreeStatistics stats = new TreeStatistics();
        stats.calculate();
        return stats;
    }
}
```

**Output**:
```java
BinaryTree<Integer> tree = new BinaryTree<>();
tree.add(5);
tree.add(3);
tree.add(7);
tree.add(1);
tree.add(4);
tree.add(6);
tree.add(8);

System.out.print("In-order traversal: ");
for (int value : tree) {
    System.out.print(value + " ");
}
// Output: 1 3 4 5 6 7 8

BinaryTree.TreeStatistics stats = tree.getStatistics();
System.out.println("\nNodes: " + stats.getNodeCount()); // 7
System.out.println("Height: " + stats.getHeight()); // 3
```

**Complexity**: O(n) for traversal, O(log n) for insertion (balanced tree)

**Best Practices**:
- Use inner classes for closely coupled helpers
- Static nested classes for independent utilities
- Consider memory implications (inner class holds reference to outer)

## Exercises

### Easy

1. **Pair Class**: Create a generic Pair class with a static nested PairBuilder.

2. **Stack**: Implement a stack using a member inner class for nodes.

### Medium

3. **Tree Set**: Implement a simple TreeSet using a binary tree with inner iterator.

4. **Event System**: Build an event system using anonymous classes for handlers.

### Hard

5. **JSON Parser**: Implement a simple JSON parser using inner classes for different JSON types.

6. **Thread Pool**: Create a basic thread pool using inner classes for worker threads.

## Interview Questions

### Beginner

1. **What are the four types of inner classes?**
   - Member inner class (non-static)
   - Static nested class
   - Local class (inside a method)
   - Anonymous class (unnamed)

2. **What is the difference between member and static nested classes?**
   Member inner classes have access to the outer instance's members. Static nested classes only access static members of the outer class.

3. **Can inner classes access private members of the outer class?**
   Yes, all inner classes can access private members of the outer class.

### Intermediate

4. **What is an anonymous class?**
   An anonymous class is a class without a name, defined and instantiated in a single expression. It's typically used for one-time implementations.

5. **Why are local variables accessed from local classes required to be effectively final?**
   Because the local class creates a copy of the variable, not a reference. If the variable changed, the copy would be out of sync.

6. **How do you create an instance of a member inner class?**
   You need an instance of the outer class: `outer.new Inner()`.

### Senior

7. **What is the memory implication of inner classes?**
   Member inner classes hold a reference to the outer instance, which can prevent garbage collection. Static nested classes don't have this issue.

8. **How do inner classes affect serialization?**
   Inner classes can complicate serialization because they hold references to outer instances. Consider using static nested classes for serializable helpers.

9. **What is the bytecode representation of inner classes?**
   Inner classes are compiled to separate class files: `OuterClass$InnerClass.class`.

### Architecture

10. **When would you use a static nested class vs a member inner class?**
    Use static nested when you don't need access to the outer instance. Use member inner when you need to access instance members.

11. **How do inner classes relate to the Singleton pattern?**
    Static nested classes are often used for the Singleton pattern (initialization-on-demand holder idiom).

12. **Can inner classes implement interfaces?**
    Yes, inner classes can implement interfaces just like regular classes.

### Scenario

13. **You need to implement an iterator for a collection. How would you use inner classes?**

14. **You're building a GUI framework and need callback handlers. How would you implement them?**

15. **You have a helper class that only makes sense in the context of its outer class. How would you structure it?**

### Coding

16. **Implement a LinkedList with an inner Node class and iterator.**

17. **Create a Builder pattern for a complex object using a static nested class.**

18. **Design an Observer pattern using inner classes for observers.**

### Production

19. **How would you handle inner classes in a multi-threaded environment?**

20. **What are the performance implications of using inner classes in hot paths?**

### Debugging

21. **Why am I getting "Non-static method cannot be referenced from a static context"?**

22. **How do I serialize an object that contains inner classes?**

## Common Pitfalls

### 1. Memory Leaks with Member Inner Classes

**Wrong**:
```java
public class Outer {
    private int[] largeArray = new int[1_000_000];
    
    public class Inner {
        // Holds reference to Outer, preventing GC
    }
}
```

**Right**:
```java
public class Outer {
    private int[] largeArray = new int[1_000_000];
    
    // Use static nested if you don't need outer reference
    public static class Inner {
        // No reference to Outer
    }
}
```

### 2. Confusing Variable Shadowing

**Wrong**:
```java
public class Outer {
    private int x = 10;
    
    public class Inner {
        private int x = 20; // Shadows outer's x
        
        public void printX() {
            System.out.println(x); // Prints 20, not 10
            System.out.println(Outer.this.x); // Prints 10
        }
    }
}
```

**Right**:
```java
public class Inner {
    private int y = 20; // Use different name
    
    public void printX() {
        System.out.println(Outer.this.x); // Explicit outer reference
        System.out.println(y); // Inner's own field
    }
}
```

### 3. Not Considering Serialization

**Wrong**:
```java
public class Outer implements Serializable {
    private int data;
    
    public class Inner implements Serializable {
        // This will try to serialize the Outer instance too
    }
}
```

**Right**:
```java
public class Outer implements Serializable {
    private int data;
    
    public static class Inner implements Serializable {
        // Static nested class doesn't reference Outer
    }
}
```

## Best Practices

### 1. Prefer Static Nested Classes

Use static nested classes when you don't need access to the outer instance. This avoids memory leaks and simplifies serialization.

### 2. Keep Inner Classes Small

Inner classes should be focused helpers, not large complex classes.

### 3. Consider Memory Implications

Member inner classes hold references to the outer instance, which can prevent garbage collection.

### 4. Use Anonymous Classes Sparingly

Anonymous classes are great for one-time implementations but can reduce readability if overused.

### 5. Document the Relationship

Add Javadoc explaining why the class is inner and its relationship to the outer class.

## Real World Usage

### JDK Usage

The JDK uses inner classes in many places:

```java
// java.util.HashMap has static nested class Entry
public class HashMap<K,V> {
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;
    }
}

// AbstractList has inner class Itr
public abstract class AbstractList<E> {
    private class Itr implements Iterator<E> {
        // Iterator implementation
    }
}
```

### Spring Framework

```java
// Spring uses static nested classes for configuration
@Configuration
public class AppConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
```

### Android

```java
// Android uses inner classes for adapters
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // View holder implementation
    }
}
```

## Architecture Diagram

```
┌─────────────────────────────────────────────────────┐
│                   Outer Class                        │
│                                                      │
│  ┌──────────────────────────────────────────────┐   │
│  │  Member Inner Class                           │   │
│  │  - Has implicit reference to outer instance   │   │
│  │  - Can access instance + static members       │   │
│  └──────────────────────────────────────────────┘   │
│                                                      │
│  ┌──────────────────────────────────────────────┐   │
│  │  Static Nested Class                          │   │
│  │  - NO reference to outer instance             │   │
│  │  - Can access static members only             │   │
│  └──────────────────────────────────────────────┘   │
│                                                      │
│  ┌──────────────────────────────────────────────┐   │
│  │  method() {                                   │   │
│  │    ┌──────────────────────────────────────┐   │   │
│  │    │  Local Class                          │   │   │
│  │    │  - Defined inside method              │   │   │
│  │    │  - Accesses effectively final vars    │   │   │
│  │    └──────────────────────────────────────┘   │   │
│  │                                               │   │
│  │    ┌──────────────────────────────────────┐   │   │
│  │    │  Anonymous Class                      │   │   │
│  │    │  - No name, inline definition         │   │   │
│  │    │  - Single use                          │   │   │
│  │    └──────────────────────────────────────┘   │   │
│  │  }                                            │   │
│  └──────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘

Bytecode Output:
  OuterClass.class
  OuterClass$InnerClass.class
  OuterClass$1LocalClass.class      (local)
  OuterClass$2.class                (anonymous)
```

**Key Relationships**:
- Member inner classes hold an implicit `this$0` reference to the outer instance
- Static nested classes have no outer reference — they're independent at runtime
- Local and anonymous classes are compiled to numbered `OuterClass$N.class` files
- All inner classes can access private members of their enclosing class

## Flow Diagram

```
                    ┌──────────────────┐
                    │ Need inner class? │
                    └────────┬─────────┘
                             │
                    ┌────────▼─────────┐
               ┌─── │ Access outer     │ ───┐
               │    │ instance members?│     │
               │    └──────────────────┘     │
              YES                            NO
               │                              │
    ┌──────────▼──────────┐       ┌──────────▼──────────┐
    │ Member Inner Class  │       │ Need static access?  │
    │ (non-static)        │       └──────────┬──────────┘
    └──────────┬──────────┘                  │
               │                     ┌───────▼────────┐
               │                YES  │ Static Nested  │ NO
               │                     │ Class          │
               │                     └───────┬────────┘
               │                              │
               │                              │
    ┌──────────▼──────────────────────────┐  │
    │ Defined inside a method?            │  │
    └──────────┬──────────────────────────┘  │
               │                              │
          YES  │                         NO   │
               │                              │
    ┌──────────▼─────────────┐     ┌─────────▼─────────┐
    │ Named class in method? │     │ Implementing an   │
    └──────────┬─────────────┘     │ interface inline? │
               │                   └─────────┬─────────┘
          YES  │                        YES  │
               │                             │
    ┌──────────▼─────────────┐   ┌───────────▼──────────┐
    │ Local Class            │   │ Anonymous Class      │
    │ (reusable in method)   │   │ (single use)         │
    └────────────────────────┘   └──────────────────────┘
```

**Decision Points**:
1. Do you need access to outer instance members? → Member inner class
2. Only need static members? → Static nested class
3. Short-lived helper in a method? → Local class
4. One-shot implementation of an interface? → Anonymous class (or lambda)

## Time Complexity

| Operation | Member Inner | Static Nested | Local | Anonymous |
|-----------|-------------|---------------|-------|-----------|
| Instantiation | O(1) | O(1) | O(1) | O(1) |
| Outer member access | O(1) | O(1) | O(1) | O(1) |
| Outer reference resolution | O(1) | N/A | N/A | N/A |
| Bytecode loading | O(1) | O(1) | O(1) | O(1) |

**Note**: Inner classes have **no runtime performance overhead** compared to regular classes. The compiler resolves all outer-member accesses at compile time. The only cost is the extra reference held by member inner classes, which affects **memory**, not speed.

**Compiled method calls** (what the JVM actually sees):
```java
// Outer.java
public class Outer {
    private int x = 10;
    public class Inner {
        public int getX() { return x; }
    }
}

// Compiler generates in Outer$Inner.class:
public int getX() {
    return Outer.access$000(this.this$0); // synthetic accessor method
}
```

## Space Complexity

| Type | Per-Instance Cost | Memory Footprint |
|------|-------------------|------------------|
| Member Inner | +1 reference (8 bytes) + object overhead | ~16 bytes overhead |
| Static Nested | Standard object overhead only | ~12-16 bytes overhead |
| Local | Standard object overhead only | ~12-16 bytes overhead |
| Anonymous | Standard object overhead only | ~12-16 bytes overhead |

**Memory Layout** (64-bit JVM with compressed oops):
```
Member Inner Class Instance:
┌──────────────────────┬──────────┐
│ Object header (12B)  │ 8 bytes  │ ← this$0 reference to Outer
├──────────────────────┼──────────┤
│ Instance fields      │ varies   │
└──────────────────────┴──────────┘
Total minimum: ~28 bytes

Static Nested Class Instance:
┌──────────────────────┬──────────┐
│ Object header (12B)  │          │
├──────────────────────┼──────────┤
│ Instance fields      │ varies   │
└──────────────────────┴──────────┘
Total minimum: ~16 bytes
```

**Memory Leak Warning**:
```java
// DANGER: Member inner class prevents Outer from being GC'd
public class Cache {
    private final Map<String, byte[]> data = loadHugeDataset();
    
    public class CacheIterator {
        // Holds implicit reference to Cache instance
        // Cache cannot be GC'd while CacheIterator exists
    }
}
```

## Thread Safety

| Aspect | Member Inner | Static Nested | Local | Anonymous |
|--------|-------------|---------------|-------|-----------|
| Thread-safe instantiation | No (needs outer ref) | Yes (no outer ref) | Method-scoped | Expression-scoped |
| Outer reference shared | Yes (mutable) | No | Method captures | Method captures |
| Safe for concurrent access | With synchronization | With synchronization | No (method scope) | No (expression scope) |
| Can cause race conditions | Yes (outer field access) | Only if accessing shared static | No (local scope) | No (local scope) |

**Thread-Safe Builder Pattern with Static Nested Class**:
```java
public class ThreadSafeConfig {
    private final String host;
    private final int port;
    
    private ThreadSafeConfig(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
    }
    
    // Static nested class — no outer reference, inherently thread-safe
    public static class Builder {
        private String host = "localhost";
        private int port = 8080;
        
        public synchronized Builder host(String host) {
            this.host = host;
            return this;
        }
        
        public synchronized Builder port(int port) {
            this.port = port;
            return this;
        }
        
        public ThreadSafeConfig build() {
            return new ThreadSafeConfig(this);
        }
    }
}
```

**Thread-Unsafe Member Inner Class**:
```java
public class Counter {
    private int count = 0;
    
    public class Incrementer {
        // NOT thread-safe — races on outer's count field
        public void increment() {
            count++; // reads, increments, writes — race condition
        }
    }
}
```

## Comparison Table

| Feature | Member Inner | Static Nested | Local | Anonymous |
|---------|-------------|---------------|-------|-----------|
| **Declaration location** | Class body | Class body | Method | Method/expression |
| **Name** | Named | Named | Named | Unnamed |
| **Access to outer instance** | Yes (implicit ref) | No | Only effectively final vars | Only effectively final vars |
| **Access to outer statics** | Yes | Yes | Yes | Yes |
| **Can be `static`** | No | Yes (already static) | No | No |
| **Can implement interfaces** | Yes | Yes | Yes | Yes |
| **Can extend classes** | Yes | Yes | Yes | Yes |
| **Inheritance** | Single + interface | Single + interface | Single + interface | Single + interface |
| **Can be public/private** | Yes | Yes | No (method-scoped) | No (expression-scoped) |
| **Compiled to** | `Outer$Inner.class` | `Outer$Nested.class` | `Outer$1Local.class` | `Outer$N.class` |
| **Memory footprint** | +1 outer ref | Standard | Standard | Standard |
| **Typical use case** | Iterator, helper | Builder, utility | Comparator, filter | Event handler, callback |
| **Java 8+ alternative** | N/A | N/A | Lambda (if functional) | Lambda (if functional) |
| **Serialization safe** | No | Yes | N/A | N/A |

## Decision Tree

**Use this flowchart to choose the right inner class type:**

```
START
  │
  ├─ Need to implement an interface with a one-liner?
  │   └─ YES → Lambda expression (Java 8+) or Anonymous Class
  │
  ├─ Need a named helper class inside a method?
  │   └─ YES → Local Class
  │
  ├─ Need to access outer instance's fields/methods?
  │   ├─ YES, and class should NOT be static → Member Inner Class
  │   └─ NO, only need static members → Static Nested Class
  │
  ├─ Implementing Builder pattern?
  │   └─ YES → Static Nested Class (recommended)
  │
  ├─ Implementing Iterator for a collection?
  │   └─ YES → Member Inner Class (needs outer collection access)
  │
  └─ Implementing a callback/listener?
      └─ YES → Anonymous Class or Lambda
```

**Quick Reference Cheat Sheet**:
```
┌─────────────────────────────┬──────────────────────────────┐
│ Scenario                    │ Best Choice                   │
├─────────────────────────────┼──────────────────────────────┤
│ Node in LinkedList          │ Static Nested (generic)       │
│ Builder pattern             │ Static Nested                 │
│ Iterator for collection     │ Member Inner                  │
│ Custom Comparator           │ Lambda > Anonymous > Local    │
│ Event handler               │ Lambda > Anonymous             │
│ Strategy pattern            │ Lambda > Anonymous             │
│ Helper with outer state     │ Member Inner                  │
│ Standalone utility          │ Top-level class               │
│ Singleton holder            │ Static Nested (lazy init)     │
└─────────────────────────────┴──────────────────────────────┘
```

## Assignments

### Assignment 1: Task Scheduler (Easy)

**Objective**: Build a simple task scheduler using member inner classes.

**Requirements**:
- Create a `TaskScheduler` class with a `Task` inner class
- Each task has a name, priority, and runnable action
- Implement `addTask()`, `removeTask()`, and `executeNext()` methods
- Tasks should execute in priority order

**Starter Code**:
```java
public class TaskScheduler {
    private final java.util.PriorityQueue<Task> queue;
    
    // TODO: Create Task inner class with Comparable implementation
    // TODO: Implement addTask, removeTask, executeNext
}
```

### Assignment 2: Configuration Builder (Medium)

**Objective**: Build a type-safe configuration system using static nested classes.

**Requirements**:
- Create a `DatabaseConfig` class with a Builder
- Support URL, username, password, maxConnections, and timeout
- Validate required fields in `build()`
- Use `record` for the final config (Java 16+)

**Starter Code**:
```java
public record DatabaseConfig(String url, String username, String password,
                             int maxConnections, Duration timeout) {
    
    public static class Builder {
        // TODO: Implement builder with validation
    }
}
```

### Assignment 3: Observable Collection (Hard)

**Objective**: Implement an observable collection using inner classes.

**Requirements**:
- Create an `ObservableList<T>` that notifies listeners on add/remove
- Use an inner class for the iterator that reflects live changes
- Support multiple listener types (add, remove, change)
- Handle concurrent modification correctly

**Starter Code**:
```java
public class ObservableList<T> implements Iterable<T> {
    private final java.util.List<T> data = new java.util.ArrayList<>();
    // TODO: Add listener management
    // TODO: Create inner iterator class
    // TODO: Create inner listener classes
}
```

### Assignment 4: Expression Parser (Hard)

**Objective**: Build a simple expression parser using local and anonymous classes.

**Requirements**:
- Parse expressions like `3 + 4 * 2`
- Support `+`, `-`, `*`, `/` operators
- Use a local class for the tokenizer
- Use anonymous classes for operator strategies

**Starter Code**:
```java
public class ExpressionParser {
    public int evaluate(String expression) {
        // TODO: Define Tokenizer local class
        // TODO: Define operator strategy anonymous classes
        // TODO: Implement Shunting-yard or recursive descent
    }
}
```

## Mini Project: Task Management System

**Objective**: Build a complete task management system demonstrating all four inner class types.

**Project Structure**:
```
src/
├── TaskManager.java          (main class)
├── Task.java                 (static nested class)
├── TaskIterator.java         (member inner class)
├── TaskComparator.java       (local class usage)
└── TaskEventSystem.java      (anonymous class usage)
```

**Implementation**:

```java
import java.util.*;
import java.util.function.Consumer;

// Main class using all four inner class types
public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();
    private final List<Consumer<TaskEvent>> listeners = new ArrayList<>();
    
    // Static nested class — independent, serializable
    public static class Task {
        private final String id;
        private final String title;
        private final Priority priority;
        private Status status;
        private final List<String> tags;
        
        public enum Priority { LOW, MEDIUM, HIGH, CRITICAL }
        public enum Status { TODO, IN_PROGRESS, DONE }
        
        public Task(String id, String title, Priority priority) {
            this.id = id;
            this.title = title;
            this.priority = priority;
            this.status = Status.TODO;
            this.tags = new ArrayList<>();
        }
        
        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public Priority getPriority() { return priority; }
        public Status getStatus() { return status; }
        public List<String> getTags() { return Collections.unmodifiableList(tags); }
        
        public void setStatus(Status status) { this.status = status; }
        public void addTag(String tag) { tags.add(tag); }
        
        @Override
        public String toString() {
            return String.format("[%s] %s (%s) - %s", id, title, priority, status);
        }
    }
    
    // Member inner class — accesses outer's tasks and listeners
    public class TaskIterator implements Iterator<Task> {
        private int index = 0;
        private Task lastReturned = null;
        
        @Override
        public boolean hasNext() {
            return index < tasks.size();
        }
        
        @Override
        public Task next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastReturned = tasks.get(index++);
            return lastReturned;
        }
        
        public void remove() {
            if (lastReturned == null) throw new IllegalStateException();
            tasks.remove(--index);
            lastReturned = null;
        }
        
        // Access outer's notify mechanism
        public void markDone() {
            if (lastReturned != null) {
                lastReturned.setStatus(Task.Status.DONE);
                notifyListeners(new TaskEvent(TaskEvent.Type.UPDATED, lastReturned));
            }
        }
    }
    
    // Inner class for events
    public static class TaskEvent {
        public enum Type { ADDED, REMOVED, UPDATED }
        private final Type type;
        private final Task task;
        
        public TaskEvent(Type type, Task task) {
            this.type = type;
            this.task = task;
        }
        
        public Type getType() { return type; }
        public Task getTask() { return task; }
    }
    
    public void addTask(Task task) {
        tasks.add(task);
        notifyListeners(new TaskEvent(TaskEvent.Type.ADDED, task));
    }
    
    public void removeTask(String taskId) {
        tasks.removeIf(t -> {
            if (t.getId().equals(taskId)) {
                notifyListeners(new TaskEvent(TaskEvent.Type.REMOVED, t));
                return true;
            }
            return false;
        });
    }
    
    public TaskIterator iterator() {
        return new TaskIterator();
    }
    
    public void addListener(Consumer<TaskEvent> listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners(TaskEvent event) {
        for (Consumer<TaskEvent> listener : listeners) {
            listener.accept(event);
        }
    }
    
    // Local class — custom sort with closure access
    public List<Task> sortByPriority() {
        // Local class defined inside method
        class PriorityComparator implements Comparator<Task> {
            @Override
            public int compare(Task t1, Task t2) {
                return t2.getPriority().compareTo(t1.getPriority());
            }
        }
        
        List<Task> sorted = new ArrayList<>(tasks);
        Collections.sort(sorted, new PriorityComparator());
        return sorted;
    }
    
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        
        // Anonymous class — event listener
        manager.addListener(new Consumer<TaskEvent>() {
            @Override
            public void accept(TaskEvent event) {
                System.out.printf("Event: %s on %s%n", event.getType(), event.getTask());
            }
        });
        
        // Add tasks
        manager.addTask(new Task("T1", "Write docs", Task.Priority.HIGH));
        manager.addTask(new Task("T2", "Fix bug", Task.Priority.CRITICAL));
        manager.addTask(new Task("T3", "Code review", Task.Priority.MEDIUM));
        
        // Iterate and mark done
        TaskManager.TaskIterator iter = manager.iterator();
        while (iter.hasNext()) {
            Task t = iter.next();
            if (t.getPriority() == Task.Priority.CRITICAL) {
                iter.markDone();
            }
        }
        
        // Sort using local class comparator
        System.out.println("\nSorted by priority:");
        for (Task t : manager.sortByPriority()) {
            System.out.println("  " + t);
        }
    }
}
```

**Expected Output**:
```
Event: ADDED on [T1] Write docs (HIGH) - TODO
Event: ADDED on [T2] Fix bug (CRITICAL) - TODO
Event: ADDED on [T3] Code review (MEDIUM) - TODO
Event: UPDATED on [T2] Fix bug (CRITICAL) - DONE

Sorted by priority:
  [T2] Fix bug (CRITICAL) - DONE
  [T1] Write docs (HIGH) - TODO
  [T3] Code review (MEDIUM) - TODO
```

**Extension Ideas**:
- Add persistence using serialization (prefer static nested classes)
- Add undo functionality using a memento pattern with inner classes
- Add filtering using anonymous classes or lambdas

## Design Patterns Using Inner Classes

### Observer Pattern

```java
public class EventEmitter<T> {
    private final Map<String, List<Listener<T>>> listeners = new HashMap<>();
    
    // Static nested — observer interface
    @FunctionalInterface
    public interface Listener<T> {
        void onEvent(T event);
    }
    
    // Member inner — subscription handle
    public class Subscription {
        private final String event;
        private final Listener<T> listener;
        private boolean active = true;
        
        Subscription(String event, Listener<T> listener) {
            this.event = event;
            this.listener = listener;
        }
        
        public void unsubscribe() {
            active = false;
            listeners.getOrDefault(event, new ArrayList<>()).remove(this);
        }
        
        boolean isActive() { return active; }
        Listener<T> getListener() { return listener; }
    }
    
    public Subscription on(String event, Listener<T> listener) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>());
        Subscription sub = new Subscription(event, listener);
        listeners.get(event).add(sub);
        return sub;
    }
    
    public void emit(String event, T data) {
        List<Listener<T>> eventListeners = listeners.getOrDefault(event, List.of());
        for (Listener<T> listener : eventListeners) {
            listener.onEvent(data);
        }
    }
}
```

### Strategy Pattern

```java
public class Sorter<T extends Comparable<T>> {
    
    @FunctionalInterface
    public interface SortStrategy<T> {
        void sort(List<T> list);
    }
    
    public static <T extends Comparable<T>> SortStrategy<T> bubbleSort() {
        // Anonymous class implementing strategy
        return new SortStrategy<>() {
            @Override
            public void sort(List<T> list) {
                int n = list.size();
                for (int i = 0; i < n - 1; i++) {
                    for (int j = 0; j < n - i - 1; j++) {
                        if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                            T temp = list.get(j);
                            list.set(j, list.get(j + 1));
                            list.set(j + 1, temp);
                        }
                    }
                }
            }
        };
    }
    
    public static <T extends Comparable<T>> SortStrategy<T> quickSort() {
        return list -> {
            if (list.size() <= 1) return;
            // QuickSort implementation
            quickSortHelper(list, 0, list.size() - 1);
        };
    }
    
    private static <T extends Comparable<T>> void quickSortHelper(
            List<T> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSortHelper(list, low, pivotIndex - 1);
            quickSortHelper(list, pivotIndex + 1, high);
        }
    }
    
    private static <T extends Comparable<T>> int partition(
            List<T> list, int low, int high) {
        T pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(pivot) < 0) {
                i++;
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        T temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }
}
```

### Builder Pattern with Validation

```java
public class Email {
    private final String from;
    private final String to;
    private final String subject;
    private final String body;
    private final List<String> attachments;
    
    private Email(Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.subject = builder.subject;
        this.body = builder.body;
        this.attachments = List.copyOf(builder.attachments);
    }
    
    // Static nested builder with validation
    public static class Builder {
        private String from;
        private String to;
        private String subject = "";
        private String body = "";
        private final List<String> attachments = new ArrayList<>();
        
        public Builder from(String from) {
            this.from = Objects.requireNonNull(from, "from cannot be null");
            if (!from.contains("@")) throw new IllegalArgumentException("Invalid email");
            return this;
        }
        
        public Builder to(String to) {
            this.to = Objects.requireNonNull(to, "to cannot be null");
            if (!to.contains("@")) throw new IllegalArgumentException("Invalid email");
            return this;
        }
        
        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }
        
        public Builder body(String body) {
            this.body = body;
            return this;
        }
        
        public Builder attach(String filePath) {
            attachments.add(filePath);
            return this;
        }
        
        public Email build() {
            Objects.requireNonNull(from, "from is required");
            Objects.requireNonNull(to, "to is required");
            return new Email(this);
        }
    }
}
```

## When to Use Each Type

| Use Case | Type | Why |
|----------|------|-----|
| Node class in a data structure | Static Nested | Independent, no outer reference needed |
| Builder pattern | Static Nested | No outer state needed, commonly static |
| Iterator for a collection | Member Inner | Needs access to collection's internal state |
| Custom Comparator in a method | Lambda > Anonymous > Local | Concise, one-shot |
| Event handler / callback | Lambda > Anonymous | Clean, functional style |
| Helper class tied to outer instance | Member Inner | Natural coupling to outer state |
| Utility class that's logically grouped | Static Nested | Clean organization, no memory overhead |
| Temporary helper in a method | Local Class | Scope limited to method |
| Protocol adapter | Anonymous | One-shot conversion |
| Thread-local storage | Static Nested (with ThreadLocal) | No outer reference leak |

## Performance Considerations

**No Runtime Difference**: The JVM treats inner classes as regular classes. There is no performance penalty for using inner vs. top-level classes.

**Compile-Time Only**: All outer-member access is resolved by the compiler via synthetic accessor methods.

```java
// Your code:
public class Outer {
    private int x;
    public class Inner {
        public int getX() { return x; }
    }
}

// Compiler generates synthetic bridge:
// Outer.class
static int access$000(Outer outer) { return outer.x; }

// Outer$Inner.class
public int getX() { return Outer.access$000(this.this$0); }
```

**Micro-optimization tips**:
1. Use static nested classes when possible (avoids 8-byte reference overhead)
2. Avoid creating inner class instances in tight loops if allocation pressure matters
3. Inner classes are not JIT-inlined differently than regular classes
4. The `this$0` reference is final — no extra field write cost

## Testing Inner Classes

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InnerClassTests {
    
    @Test
    void memberInnerClassAccessesOuterPrivateField() {
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        assertEquals(42, inner.getOuterValue());
    }
    
    @Test
    void staticNestedClassInstantiation() {
        Outer.Nested nested = new Outer.Nested();
        assertNotNull(nested);
    }
    
    @Test
    void anonymousClassImplementsInterface() {
        Runnable r = new Runnable() {
            @Override
            public void run() {}
        };
        assertDoesNotThrow(r::run);
    }
    
    @Test
    void builderPatternProducesValidObject() {
        HttpRequest req = new HttpRequest.Builder("https://example.com")
            .method("GET")
            .build();
        assertEquals("GET", req.getMethod());
    }
    
    @Test
    void localClassComparatorSortsCorrectly() {
        // Test that local class comparator works as expected
    }
}

class Outer {
    private int x = 42;
    
    public class Inner {
        public int getOuterValue() { return x; }
    }
    
    public static class Nested {
        public String hello() { return "hello"; }
    }
}
```

## Refactoring Inner Classes

### Extract to Top-Level

```java
// Before: Inner class growing too large
public class OrderProcessor {
    private class OrderValidator {
        // 50+ lines of validation logic
    }
}

// After: Extract to top-level class
public class OrderProcessor {
    private final OrderValidator validator = new OrderValidator();
}

public class OrderValidator {
    // Clean, testable, independent
}
```

### Convert Member to Static Nested

```java
// Before: Member inner class (holds outer reference unnecessarily)
public class Cache {
    private Map<String, Object> data = new HashMap<>();
    
    public class Entry {
        String key;
        Object value;
        // Doesn't actually access Cache's data
    }
}

// After: Static nested class (no outer reference)
public class Cache {
    private Map<String, Object> data = new HashMap<>();
    
    public static class Entry {
        String key;
        Object value;
    }
}
```

### Replace Anonymous with Lambda

```java
// Before: Anonymous class
list.sort(new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.length() - b.length();
    }
});

// After: Lambda (Java 8+)
list.sort(Comparator.comparingInt(String::length));
```

## Common Errors and Solutions

| Error | Cause | Solution |
|-------|-------|----------|
| `Non-static method cannot be referenced from a static context` | Trying to create member inner class statically | Use `outer.new Inner()` or make class static |
| `Local variable referenced from an inner class must be final or effectively final` | Modifying local variable after capture | Don't reassign the variable; use a wrapper |
| `Outer class reference not found` | Trying to access outer from static context | Ensure proper instantiation chain |
| `Class not found: Outer$Inner` | Compiled separately without inner class | Recompile all source files together |
| `StackOverflowError` | Infinite recursion in inner class | Check for circular references |

```java
// Error: Non-static reference
public class Outer {
    public class Inner {}
    public static void main(String[] args) {
        Inner i = new Inner(); // ERROR
    }
}

// Fix:
public class Outer {
    public class Inner {}
    public static void main(String[] args) {
        Outer o = new Outer();
        Inner i = o.new Inner(); // CORRECT
    }
}

// Error: Variable not effectively final
public void method() {
    int x = 10;
    class Local {
        void print() { System.out.println(x); }
    }
    x = 20; // ERROR: now x is not effectively final
    new Local().print();
}

// Fix:
public void method() {
    int x = 10;
    class Local {
        void print() { System.out.println(x); }
    }
    new Local().print(); // print 10
    // Don't modify x after Local class usage
}
```

## Anti-Patterns

### 1. God Inner Class

```java
// BAD: Inner class doing too much
public class Application {
    public class UIManager {
        // 500 lines of UI logic
        // Should be a separate top-level class
    }
}
```

### 2. Inner Class for Everything

```java
// BAD: Using inner classes when top-level is better
public class Utils {
    public class StringHelper { /* ... */ }
    public class MathHelper { /* ... */ }
    public class DateHelper { /* ... */ }
}
// These should be separate top-level utility classes
```

### 3. Leaky Inner Class

```java
// BAD: Inner class exposes outer reference
public class Session {
    private String token;
    
    public class TokenProvider {
        public String getToken() { return token; }
        // Exposes sensitive data via outer reference
    }
}
```

## Code Review Checklist

- [ ] Is a static nested class sufficient? (Don't use member inner if static works)
- [ ] Does the inner class need access to outer instance members?
- [ ] Is the inner class reasonably small (< 100 lines)?
- [ ] Are private inner classes used for implementation details?
- [ ] Does the inner class have Javadoc explaining its relationship to outer?
- [ ] Is the inner class serializable if outer is? (consider implications)
- [ ] Are local classes only used when truly method-scoped?
- [ ] Can anonymous classes be replaced with lambdas?
- [ ] Are effectively final constraints documented for local/anonymous usage?
- [ ] Is the inner class named clearly (e.g., `Builder`, `Iterator`, `Node`)?

## Security Considerations

```java
public class SecureVault {
    private byte[] secretKey;
    
    // DANGER: Inner class leaks outer reference (and secret key)
    public class KeyAccessor {
        public byte[] getKey() { return secretKey; }
    }
    
    // SAFE: Static nested class — no outer reference
    public static class KeyGenerator {
        public static byte[] generate(int length) {
            byte[] key = new byte[length];
            new java.security.SecureRandom().nextBytes(key);
            return key;
        }
    }
}
```

**Serialization attacks**: Malicious deserialization of inner classes can restore outer references, potentially bypassing security constraints. Always use `static nested` for serializable components.

## Alternative Approaches

### Lambda Instead of Anonymous Class

```java
// Anonymous class
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        handleClick();
    }
});

// Lambda (functional interface)
button.addActionListener(e -> handleClick());
```

### Method Reference Instead of Inner Class

```java
// Local class
class LengthComparator implements Comparator<String> {
    @Override
    public int compare(String a, String b) {
        return a.length() - b.length();
    }
}
Arrays.sort(strings, new LengthComparator());

// Method reference
Arrays.sort(strings, Comparator.comparingInt(String::length));
```

### Top-Level Helper Instead of Inner Class

```java
// Inner class
public class LinkedList<T> {
    private class Node {
        T data;
        Node next;
    }
}

// Top-level with package-private access
class Node<T> {  // package-private
    T data;
    Node<T> next;
}

public class LinkedList<T> {
    private Node<T> head;
}
```

## Java 21 Features and Inner Classes

### Pattern Matching with Inner Classes

```java
public sealed interface Shape permits Circle, Rectangle {
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
    
    // Inner class using pattern matching
    class Formatter {
        static String format(Shape shape) {
            return switch (shape) {
                case Circle c -> "Circle: r=" + c.radius();
                case Rectangle r -> "Rect: " + r.width() + "x" + r.height();
            };
        }
    }
}
```

### Sealed Classes with Inner Classes

```java
public class DatabaseConnection {
    private sealed interface State permits Connected, Disconnected, Error {}
    
    private record Connected(Connection conn) implements State {}
    private record Disconnected() implements State {}
    private record Error(String message) implements State {}
    
    private State state = new Disconnected();
    
    public void connect() {
        state = new Connected(createConnection());
    }
    
    public void disconnect() {
        if (state instanceof Connected c) {
            c.conn().close();
            state = new Disconnected();
        }
    }
    
    private Connection createConnection() { /* ... */ }
}
```

### Records as Inner Classes

```java
public class Geometry {
    // Records as static nested classes
    public record Point(double x, double y) {
        public double distanceTo(Point other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }
    
    public record Line(Point start, Point end) {
        public double length() {
            return start.distanceTo(end);
        }
        
        public Point midpoint() {
            return new Point(
                (start.x() + end.x()) / 2,
                (start.y() + end.y()) / 2
            );
        }
    }
    
    // Member inner class using records
    public class BoundingBox {
        private final Point min;
        private final Point max;
        
        public BoundingBox(List<Point> points) {
            double minX = points.stream().mapToDouble(Point::x).min().orElse(0);
            double minY = points.stream().mapToDouble(Point::y).min().orElse(0);
            double maxX = points.stream().mapToDouble(Point::x).max().orElse(0);
            double maxY = points.stream().mapToDouble(Point::y).max().orElse(0);
            this.min = new Point(minX, minY);
            this.max = new Point(maxX, maxY);
        }
        
        public boolean contains(Point p) {
            return p.x() >= min.x() && p.x() <= max.x() &&
                   p.y() >= min.y() && p.y() <= max.y();
        }
    }
}
```

### Virtual Threads and Inner Classes (Java 21)

```java
public class TaskRunner {
    // Static nested — safe for virtual threads
    public static class Task implements Runnable {
        private final String name;
        private final Runnable action;
        
        public Task(String name, Runnable action) {
            this.name = name;
            this.action = action;
        }
        
        @Override
        public void run() {
            System.out.println(Thread.currentThread() + " running " + name);
            action.run();
        }
    }
    
    public static void main(String[] args) {
        try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            // Lambda alternative to anonymous class
            executor.submit(() -> System.out.println("Virtual thread task"));
            
            // Static nested class — no outer reference leak
            executor.submit(new Task("Worker", () -> {
                System.out.println("Doing work...");
            }));
        }
    }
}
```

### Sequenced Collections with Inner Iterator

```java
import java.util.SequencedCollection;

public class Deck<T> implements SequencedCollection<T> {
    private final java.util.ArrayDeque<T> cards = new java.util.ArrayDeque<>();
    
    // Member inner class — iterator needs outer access
    public class DeckIterator implements java.util.Iterator<T> {
        private final java.util.Iterator<T> delegate = cards.iterator();
        
        @Override
        public boolean hasNext() { return delegate.hasNext(); }
        @Override
        public T next() { return delegate.next(); }
    }
    
    @Override public void addFirst(T e) { cards.addFirst(e); }
    @Override public void addLast(T e) { cards.addLast(e); }
    @Override public T getFirst() { return cards.getFirst(); }
    @Override public T getLast() { return cards.getLast(); }
    @Override public T removeFirst() { return cards.removeFirst(); }
    @Override public T removeLast() { return cards.removeLast(); }
    @Override public java.util.Iterator<T> iterator() { return new DeckIterator(); }
    @Override public int size() { return cards.size(); }
    @Override public boolean isEmpty() { return cards.isEmpty(); }
    // ... other SequencedCollection methods
}
```

## Summary

Inner classes provide powerful encapsulation and organization capabilities. Key takeaways:

- **Four types**: Member, Static Nested, Local, Anonymous
- **Access**: Inner classes can access outer class members
- **Memory**: Member inner classes hold references to outer instances
- **Use cases**: Helpers, iterators, builders, callbacks
- **Best practices**: Prefer static nested, keep small, document relationship
- **Java 21**: Pattern matching, records, sealed classes, and virtual threads all work seamlessly with inner classes

**Next Steps**: Learn about functional interfaces for lambda expressions, or sealed classes for restricted hierarchies.
