# Anonymous Classes

## Introduction

Anonymous classes are unnamed classes defined and instantiated in a single expression. They provide a concise way to create one-time implementations of interfaces or abstract classes.

## Learning Objectives

By the end of this topic, you will be able:

- Understand when to use anonymous classes
- Implement interfaces inline
- Extend abstract classes inline
- Compare anonymous classes with lambda expressions
- Apply anonymous classes in real-world scenarios

## Prerequisites

- Interfaces
- Abstract Classes
- Inner Classes

## Why This Concept Exists

### The Problem

Without anonymous classes:

- Need to create a named class for simple one-time implementations
- Code becomes verbose for short callbacks
- Helper classes clutter the namespace

### The Solution

Anonymous classes provide:

- **Conciseness**: Define and instantiate in one expression
- **Encapsulation**: Keep implementation close to usage
- **Flexibility**: Override methods inline
- **Readability**: Show intent directly

### Real-World Analogy

Think of anonymous classes as **temporary workers**:

- They're hired for a specific task (implementation)
- They don't need a formal name (unnamed class)
- They work alongside permanent staff (outer class)
- They're used once and then let go (one-time use)

## Internal Working

### How Anonymous Classes Work

1. **Compilation**: Anonymous classes compile to separate files
2. **Naming**: Generated as `OuterClass$1.class`, `OuterClass$2.class`, etc.
3. **Inheritance**: Can extend a class or implement an interface
4. **Access**: Can access final or effectively final local variables

## Syntax

### Basic Syntax

```java
// Anonymous class implementing an interface
Runnable runnable = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running...");
    }
};

// Anonymous class extending an abstract class
Thread thread = new Thread() {
    @Override
    public void run() {
        System.out.println("Thread running...");
    }
};
```

### With Constructor Arguments

```java
// Anonymous class with constructor arguments
Comparable<String> comparator = new Comparable<String>() {
    @Override
    public int compareTo(String s1, String s2) {
        return s1.length() - s2.length();
    }
};
```

## Easy Examples

### Example 1: Runnable Implementation

**Problem Statement**: Create a Runnable using an anonymous class.

**Implementation**:

```java
public class AnonymousRunnableExample {
    public static void main(String[] args) {
        // Anonymous class implementing Runnable
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("Task is running");
                System.out.println("Thread: " + Thread.currentThread().getName());
            }
        };
        
        // Run the task
        task.run();
        
        // Use with Thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Running in separate thread");
            }
        });
        thread.start();
    }
}
```

**Output**:
```
Task is running
Thread: main
Running in separate thread
```

**Complexity**: O(1)

**Best Practices**:
- Use anonymous classes for simple one-time implementations
- Keep them short and focused
- Consider lambda expressions for functional interfaces (Java 8+)

### Example 2: Comparator Implementation

**Problem Statement**: Sort a list using an anonymous Comparator.

**Implementation**:

```java
import java.util.Arrays;
import java.util.Comparator;

public class AnonymousComparatorExample {
    public static void main(String[] args) {
        String[] names = {"Charlie", "Alice", "Bob", "Diana"};
        
        // Sort by length using anonymous Comparator
        Arrays.sort(names, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
        });
        
        System.out.println("Sorted by length:");
        for (String name : names) {
            System.out.println("  " + name + " (length: " + name.length() + ")");
        }
        
        // Sort alphabetically
        Arrays.sort(names, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        
        System.out.println("\nSorted alphabetically:");
        for (String name : names) {
            System.out.println("  " + name);
        }
    }
}
```

**Output**:
```
Sorted by length:
  Bob (length: 3)
  Alice (length: 5)
  Diana (length: 5)
  Charlie (length: 7)

Sorted alphabetically:
  Alice
  Bob
  Charlie
  Diana
```

## Medium Examples

### Example 3: Event Handler System

**Problem Statement**: Build an event handling system using anonymous classes.

**Implementation**:

```java
// Event interface
public interface EventHandler {
    void handleEvent(String event, Object data);
}

// Event dispatcher
public class EventDispatcher {
    private final java.util.Map<String, java.util.List<EventHandler>> handlers = new java.util.HashMap<>();
    
    public void registerHandler(String event, EventHandler handler) {
        handlers.computeIfAbsent(event, k -> new java.util.ArrayList<>()).add(handler);
    }
    
    public void dispatch(String event, Object data) {
        java.util.List<EventHandler> eventHandlers = handlers.get(event);
        if (eventHandlers != null) {
            for (EventHandler handler : eventHandlers) {
                handler.handleEvent(event, data);
            }
        }
    }
}

// Usage
public class EventSystemDemo {
    public static void main(String[] args) {
        EventDispatcher dispatcher = new EventDispatcher();
        
        // Register handlers using anonymous classes
        dispatcher.registerHandler("USER_LOGIN", new EventHandler() {
            @Override
            public void handleEvent(String event, Object data) {
                System.out.println("Logging: User " + data + " logged in");
            }
        });
        
        dispatcher.registerHandler("USER_LOGIN", new EventHandler() {
            @Override
            public void handleEvent(String event, Object data) {
                System.out.println("Analytics: Login event tracked");
            }
        });
        
        dispatcher.registerHandler("USER_LOGOUT", new EventHandler() {
            @Override
            public void handleEvent(String event, Object data) {
                System.out.println("Logging: User " + data + " logged out");
            }
        });
        
        // Dispatch events
        dispatcher.dispatch("USER_LOGIN", "john_doe");
        dispatcher.dispatch("USER_LOGOUT", "john_doe");
    }
}
```

**Output**:
```
Logging: User john_doe logged in
Analytics: Login event tracked
Logging: User john_doe logged out
```

**Complexity**: O(1) for registration, O(n) for dispatch where n is number of handlers

### Example 4: Thread Factory

**Problem Statement**: Create a thread factory using anonymous class.

**Implementation**:

```java
import java.util.concurrent.ThreadFactory;

public class ThreadFactoryExample {
    
    public static ThreadFactory createThreadFactory(String prefix) {
        return new ThreadFactory() {
            private int count = 0;
            
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(prefix + "-" + (++count));
                thread.setDaemon(false);
                return thread;
            }
        };
    }
    
    public static void main(String[] args) {
        ThreadFactory factory = createThreadFactory("Worker");
        
        // Create threads using the factory
        for (int i = 0; i < 5; i++) {
            Thread thread = factory.newThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " is running");
                }
            });
            thread.start();
        }
    }
}
```

**Output**:
```
Worker-1 is running
Worker-2 is running
Worker-3 is running
Worker-4 is running
Worker-5 is running
```

## Hard Examples

### Example 5: Strategy Pattern Implementation

**Problem Statement**: Implement the Strategy pattern using anonymous classes.

**Implementation**:

```java
// Strategy interface
public interface SortStrategy<T> {
    void sort(T[] array);
    String getName();
}

// Context class
public class Sorter<T> {
    private SortStrategy<T> strategy;
    
    public void setStrategy(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }
    
    public void sort(T[] array) {
        System.out.println("Sorting using " + strategy.getName() + "...");
        strategy.sort(array);
    }
}

// Usage with anonymous classes
public class StrategyPatternDemo {
    public static void main(String[] args) {
        Sorter<Integer> sorter = new Sorter<>();
        Integer[] numbers = {5, 2, 8, 1, 9, 3};
        
        // Bubble Sort strategy
        sorter.setStrategy(new SortStrategy<Integer>() {
            @Override
            public void sort(Integer[] array) {
                int n = array.length;
                for (int i = 0; i < n - 1; i++) {
                    for (int j = 0; j < n - i - 1; j++) {
                        if (array[j] > array[j + 1]) {
                            int temp = array[j];
                            array[j] = array[j + 1];
                            array[j + 1] = temp;
                        }
                    }
                }
                System.out.println("Bubble sort completed");
            }
            
            @Override
            public String getName() {
                return "Bubble Sort";
            }
        });
        
        sorter.sort(numbers.clone());
        
        // Quick Sort strategy
        sorter.setStrategy(new SortStrategy<Integer>() {
            @Override
            public void sort(Integer[] array) {
                quickSort(array, 0, array.length - 1);
                System.out.println("Quick sort completed");
            }
            
            @Override
            public String getName() {
                return "Quick Sort";
            }
            
            private void quickSort(Integer[] array, int low, int high) {
                if (low < high) {
                    int pivot = partition(array, low, high);
                    quickSort(array, low, pivot - 1);
                    quickSort(array, pivot + 1, high);
                }
            }
            
            private int partition(Integer[] array, int low, int high) {
                int pivot = array[high];
                int i = low - 1;
                for (int j = low; j < high; j++) {
                    if (array[j] < pivot) {
                        i++;
                        int temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                    }
                }
                int temp = array[i + 1];
                array[i + 1] = array[high];
                array[high] = temp;
                return i + 1;
            }
        });
        
        sorter.sort(numbers.clone());
    }
}
```

**Output**:
```
Sorting using Bubble Sort...
Bubble sort completed
Sorting using Quick Sort...
Quick sort completed
```

**Best Practices**:
- Use anonymous classes for strategy pattern when implementations are simple
- Keep strategies focused and single-purpose
- Consider lambda expressions for single-method interfaces

## Exercises

### Easy

1. **Action Listener**: Create a Button class with an anonymous ActionListener implementation.

2. **Thread Example**: Create a Thread using an anonymous class that prints a message.

### Medium

3. **File Filter**: Implement a FileFilter using an anonymous class to filter Java files.

4. **Custom Exception**: Create an anonymous Exception subclass for error handling.

### Hard

5. **Parser**: Implement a simple expression parser using anonymous classes for different node types.

6. **Plugin System**: Build a plugin system where plugins are loaded as anonymous classes.

## Interview Questions

### Beginner

1. **What is an anonymous class?**
   An anonymous class is a class without a name, defined and instantiated in a single expression. It's typically used for one-time implementations of interfaces or abstract classes.

2. **When would you use an anonymous class?**
   When you need a short, one-time implementation of an interface or abstract class, like event handlers, callbacks, or comparators.

3. **Can anonymous classes extend a class?**
   Yes, anonymous classes can extend a class (but not both extend a class and implement an interface).

### Intermediate

4. **What is the difference between an anonymous class and a lambda expression?**
   Anonymous classes can implement any interface or extend any class, while lambdas can only implement functional interfaces (single abstract method). Anonymous classes can have multiple methods, lambdas cannot.

5. **Can anonymous classes access non-final local variables?**
   No, anonymous classes can only access final or effectively final local variables.

6. **How are anonymous classes compiled?**
   Anonymous classes are compiled to separate class files named `OuterClass$1.class`, `OuterClass$2.class`, etc.

### Senior

7. **What are the memory implications of anonymous classes?**
   Anonymous classes hold a reference to the enclosing instance (if non-static), which can prevent garbage collection. They also create additional class files.

8. **How do anonymous classes affect serialization?**
   Anonymous classes can complicate serialization because they're not named classes. Consider using named classes for serializable objects.

9. **Can anonymous classes be generic?**
   Yes, anonymous classes can be generic, but the type parameters must be specified at the point of creation.

### Architecture

10. **When would you use an anonymous class vs a named class?**
    Use anonymous classes for simple, one-time implementations. Use named classes when the implementation is complex, reusable, or needs to be tested independently.

11. **How do anonymous classes relate to the Factory pattern?**
    Anonymous classes can be used to create factory methods that return different implementations based on input.

12. **Can anonymous classes implement multiple interfaces?**
    No, an anonymous class can only implement one interface or extend one class.

### Scenario

13. **You need to implement a callback for an asynchronous operation. How would you use an anonymous class?**

14. **You're building a GUI framework and need to handle different button clicks. How would you implement this?**

15. **You have a library that accepts a strategy parameter. How would you provide different strategies using anonymous classes?**

### Coding

16. **Implement a Runnable using an anonymous class that sleeps for 1 second and then prints a message.**

17. **Create a Comparator for sorting objects by multiple fields using an anonymous class.**

18. **Design a simple event system where listeners are registered as anonymous classes.**

### Production

19. **How would you handle anonymous classes in a multi-threaded environment?**

20. **What are the performance implications of using anonymous classes in hot paths?**

### Debugging

21. **Why am I getting "local variables referenced from an anonymous class must be final or effectively final"?**

22. **How do I debug an anonymous class when I can't see its name in the stack trace?**

## Common Pitfalls

### 1. Not Handling Variable Finality

**Wrong**:
```java
public void method() {
    int counter = 0;
    Runnable r = new Runnable() {
        @Override
        public void run() {
            counter++; // Error: counter is not final
        }
    };
}
```

**Right**:
```java
public void method() {
    final int[] counter = {0}; // Use array to mutate
    Runnable r = new Runnable() {
        @Override
        public void run() {
            counter[0]++;
        }
    };
}
```

### 2. Creating Too Many Anonymous Classes

**Wrong**:
```java
// Multiple anonymous classes for similar functionality
button1.addActionListener(new ActionListener() { ... });
button2.addActionListener(new ActionListener() { ... });
button3.addActionListener(new ActionListener() { ... });
```

**Right**:
```java
// Reuse a named class or use lambda
ActionListener listener = e -> handleClick(e);
button1.addActionListener(listener);
button2.addActionListener(listener);
button3.addActionListener(listener);
```

### 3. Forgetting That Anonymous Classes Are Named

**Wrong**:
```java
// Anonymous classes are actually named
Object obj = new Object() {
    public String toString() {
        return "Anonymous";
    }
};
System.out.println(obj.getClass().getName()); // OuterClass$1
```

**Right**:
```java
// Use named classes for better debugging
class MyObject extends Object {
    @Override
    public String toString() {
        return "Named";
    }
}
Object obj = new MyObject();
System.out.println(obj.getClass().getName()); // MyObject
```

## Best Practices

### 1. Use for Simple Implementations Only

Anonymous classes are best for short, simple implementations. For complex logic, use named classes.

### 2. Consider Lambda Expressions

For functional interfaces (single abstract method), prefer lambda expressions over anonymous classes.

### 3. Keep Them Short

Anonymous classes should be concise. If they grow beyond a few lines, consider a named class.

### 4. Document the Intent

Add a comment explaining what the anonymous class does if it's not obvious.

### 5. Be Aware of Memory Implications

Anonymous classes hold references to enclosing instances. Be mindful of potential memory leaks.

## Real World Usage

### JDK Usage

The JDK uses anonymous classes in many places:

```java
// Collections.sort with anonymous Comparator
Collections.sort(list, new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
});

// Thread creation
new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("Running");
    }
}).start();
```

### Swing/AWT

```java
// Button click handler
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked");
    }
});
```

### Android

```java
// AsyncTask implementation
new AsyncTask<Void, Void, String>() {
    @Override
    protected String doInBackground(Void... voids) {
        return "Result";
    }
    
    @Override
    protected void onPostExecute(String result) {
        textView.setText(result);
    }
}.execute();
```

## Summary

Anonymous classes provide a concise way to create one-time implementations. Key takeaways:

- **Purpose**: Quick inline implementations of interfaces or abstract classes
- **Syntax**: `new Interface() { implementation }`
- **Access**: Can access final/effectively final local variables
- **Compilation**: Create separate class files (`OuterClass$N.class`)
- **Alternatives**: Lambda expressions for functional interfaces (Java 8+)
- **Best practices**: Keep simple, use for one-time implementations, consider memory

**Next Steps**: Learn about functional interfaces for lambda expressions, or design patterns that use anonymous classes.
