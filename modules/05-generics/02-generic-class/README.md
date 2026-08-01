# Generic Classes

## Introduction

Generic classes are classes that can work with different data types through type parameters.

## Learning Objectives

- Create generic classes with single and multiple type parameters
- Understand type parameter naming conventions
- Implement generic interfaces
- Use bounded type parameters

## Prerequisites

- Introduction to Generics
- Interface concepts
- Class inheritance

## Why This Matters

Generic classes enable you to write reusable code that works with any type while maintaining type safety.

## Syntax

```java
// Single type parameter
public class Container<T> {
    private T item;

    public void set(T item) { this.item = item; }
    public T get() { return item; }
}

// Multiple type parameters
public class KeyValue<K, V> {
    private K key;
    private V value;

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

// Generic interface
public interface Repository<T> {
    void save(T item);
    T findById(int id);
    List<T> findAll();
}
```

## Examples

```java
// Example 1: Generic Stack
public class Stack<E> {
    private List<E> elements = new ArrayList<>();

    public void push(E element) {
        elements.add(element);
    }

    public E pop() {
        if (elements.isEmpty()) {
            throw new EmptyStackException();
        }
        return elements.remove(elements.size() - 1);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }
}

Stack<String> stringStack = new Stack<>();
stringStack.push("Hello");
String top = stringStack.pop();

// Example 2: Generic Pair
public class Pair<T> {
    private T first;
    private T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() { return first; }
    public T getSecond() { return second; }
}

Pair<Integer> numbers = new Pair<>(1, 2);
Pair<String> strings = new Pair<>("Hello", "World");

// Example 3: Generic interface implementation
public class ListRepository<T> implements Repository<T> {
    private List<T> items = new ArrayList<>();

    @Override
    public void save(T item) {
        items.add(item);
    }

    @Override
    public T findById(int id) {
        return items.get(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(items);
    }
}

Repository<String> repo = new ListRepository<>();
repo.save("Item 1");
```

## Exercises

1. Create a generic Queue class with enqueue and dequeue methods.
2. Implement a generic Pair class that can swap its elements.
3. Create a generic Repository interface and implementation.

## Interview Questions

- What are type parameters and naming conventions?
- Can a class implement multiple generic interfaces?
- How do you create a generic class with inheritance?

## Common Pitfalls

- Not specifying type parameters when instantiating
- Using wildcard types in class definitions
- Creating circular generic dependencies

## Best Practices

- Use meaningful type parameter names (T for type, E for element, K/V for key/value)
- Document type parameter constraints in Javadoc
- Keep generic classes focused and simple

## Real World Applications

- Data containers and wrappers
- Repository patterns
- Factory patterns
- Utility classes

## References

- [Generic Classes Tutorial](https://docs.oracle.com/javase/tutorial/java/generics/types.html)
- [Java Generics Deep Dive](https://www.baeldung.com/java-generics)

## Summary

In this topic, you learned how to create generic classes with single and multiple type parameters. Generic classes are the foundation of type-safe, reusable code. Practice with the exercises before learning about generic methods.
