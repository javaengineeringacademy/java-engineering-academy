# Vector

## Introduction

Vector is a synchronized (thread-safe) implementation of the List interface. It's similar to ArrayList but with thread-safe operations.

## Learning Objectives

- Understand Vector's thread-safety model
- Learn Vector's synchronized operations
- Know when to use Vector vs ArrayList
- Understand modern alternatives to Vector

## Prerequisites

- ArrayList
- Basic threading concepts

## Why This Matters

While Vector is largely superseded by modern concurrent collections, understanding it helps with legacy code and concept of synchronization.

## Syntax

```java
// Creating Vector
Vector<E> vector = new Vector<>();
Vector<E> vector = new Vector<>(capacity);
Vector<E> vector = new Vector<>(initialCapacity, capacityIncrement);

// Standard List operations (all synchronized)
vector.add(element);
vector.get(index);
vector.remove(index);
vector.size();

// Legacy methods
vector.addElement(element);
vector.elementAt(index);
vector.firstElement();
vector.lastElement();
```

## Examples

```java
// Example 1: Basic Vector
Vector<String> names = new Vector<>();
names.add("Alice");
names.add("Bob");
names.add("Charlie");

System.out.println(names);  // [Alice, Bob, Charlie]
System.out.println(names.elementAt(0));  // Alice

// Example 2: Vector with capacity
Vector<Integer> numbers = new Vector<>(10, 5);
for (int i = 0; i < 20; i++) {
    numbers.add(i);
}

// Example 3: Thread-safe iteration
Vector<Integer> vector = new Vector<>();
vector.add(1);
vector.add(2);
vector.add(3);

// Safe iteration (snapshot)
for (Integer num : vector) {
    System.out.println(num);
    vector.add(num + 10);  // Won't cause ConcurrentModificationException
}
```

## Exercises

1. Compare the performance of Vector vs ArrayList for different operations.
2. Create a thread-safe counter using Vector.
3. Implement a simple thread-safe list using Vector.

## Interview Questions

- What makes Vector thread-safe?
- Why is Vector considered legacy?
- What are the alternatives to Vector in modern Java?

## Common Pitfalls

- Assuming Vector is always the best choice for thread-safety
- Not understanding the performance cost of synchronization
- Using Vector when other concurrent collections would be better

## Best Practices

- Use ArrayList for single-threaded applications
- Use CopyOnWriteArrayList for read-heavy concurrent scenarios
- Use Collections.synchronizedList() when you need synchronization
- Consider ConcurrentHashMap for concurrent map operations

## Real World Applications

- Legacy code maintenance
- Simple thread-safe list requirements
- Understanding synchronization concepts

## References

- [Vector Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/Vector.html)
- [Thread Safety in Java](https://docs.oracle.com/javase/tutorial/essential/concurrency/)

## Summary

In this topic, you learned about Vector and its role as a synchronized List implementation. While largely replaced by modern alternatives, it's important to understand for legacy code and synchronization concepts. Practice with the exercises before learning about Stack.
