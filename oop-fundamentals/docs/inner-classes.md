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

## Summary

Inner classes provide powerful encapsulation and organization capabilities. Key takeaways:

- **Four types**: Member, Static Nested, Local, Anonymous
- **Access**: Inner classes can access outer class members
- **Memory**: Member inner classes hold references to outer instances
- **Use cases**: Helpers, iterators, builders, callbacks
- **Best practices**: Prefer static nested, keep small, document relationship

**Next Steps**: Learn about functional interfaces for lambda expressions, or sealed classes for restricted hierarchies.
