# 08 - Real-World Applications

## Table of Contents

1. [Introduction](#introduction)
2. [Learning Objectives](#learning-objectives)
3. [Prerequisites](#prerequisites)
4. [Why This Concept Exists](#why-this-concept-exists)
5. [Problem Statement](#problem-statement)
6. [Theory](#theory)
7. [Internal Working](#internal-working)
8. [JVM Perspective](#jvm-perspective)
9. [Memory Representation](#memory-representation)
10. [Syntax](#syntax)
11. [Easy Example](#easy-example)
12. [Medium Example](#medium-example)
13. [Hard Example](#hard-example)
14. [Enterprise Example](#enterprise-example)
15. [Performance](#performance)
16. [Best Practices](#best-practices)
17. [Common Mistakes](#common-mistakes)
18. [Pitfalls](#pitfalls)
19. [Debugging Tips](#debugging-tips)
20. [Comparison Table](#comparison-table)
21. [Decision Tree](#decision-tree)
22. [Interview Questions](#interview-questions)
23. [Exercises](#exercises)
24. [Assignments](#assignments)
25. [Mini Project](#mini-project)
26. [Summary](#summary)
27. [References](#references)

---

## Introduction

This topic explores how generics are used in real-world Java applications. From the Java Collections Framework to enterprise patterns like Repository and DTO, generics are everywhere. Understanding these patterns helps you write production-quality code.

---

## Learning Objectives

By the end of this topic, you will be able to:

- Recognize generic patterns in the Java Collections Framework
- Implement Repository pattern with generics
- Build type-safe DTO and VO patterns
- Create generic service layers
- Apply generics in data access objects
- Design flexible, reusable enterprise components

---

## Prerequisites

- All previous topics (01-07)
- Java Collections Framework familiarity
- Basic understanding of enterprise patterns
- Design patterns knowledge (helpful)

---

## Why This Concept Exists

### Without Generics in Real Code

```java
// Pre-generics: Everything was Object
public class UserRepository {
    private List users = new ArrayList();

    public void save(Object user) {
        users.add(user);
    }

    public Object findById(int id) {
        return users.get(id);  // Must cast!
    }
}

// Usage
UserRepository repo = new UserRepository();
repo.save(new User("Alice"));
User user = (User) repo.findById(0);  // Risky cast
```

### With Generics

```java
// Post-generics: Type-safe
public class UserRepository implements Repository<User, Long> {
    private List<User> users = new ArrayList<>();

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public User findById(Long id) {
        return users.stream()
                    .filter(u -> u.getId().equals(id))
                    .findFirst()
                    .orElse(null);
    }
}

// Usage
UserRepository repo = new UserRepository();
repo.save(new User("Alice"));
User user = repo.findById(1L);  // No cast needed
```

---

## Problem Statement

Design generic patterns for:

1. Data access with type safety
2. Service layer flexibility
3. DTO/VO conversion
4. Event handling
5. Configuration management
6. Validation frameworks

---

## Theory

### Java Collections Framework

The JCF is the most extensive use of generics:

```java
// List interface
public interface List<E> extends Collection<E> {
    E get(int index);
    boolean add(E e);
    E remove(int index);
    // ...
}

// Map interface
public interface Map<K, V> {
    V get(Object key);
    V put(K key, V value);
    boolean containsKey(Object key);
    // ...
}

// Comparable interface
public interface Comparable<T> {
    int compareTo(T o);
}
```

### Repository Pattern

```java
// Generic repository interface
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(ID id);
    long count();
}

// In-memory implementation
public class InMemoryRepository<T, ID> implements Repository<T, ID> {
    private final Map<ID, T> store = new HashMap<>();
    private final Function<T, ID> idExtractor;
    private final AtomicLong idCounter = new AtomicLong();

    public InMemoryRepository(Function<T, ID> idExtractor) {
        this.idExtractor = idExtractor;
    }

    @Override
    public T findById(ID id) {
        return store.get(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void save(T entity) {
        ID id = idExtractor.apply(entity);
        store.put(id, entity);
    }
}
```

### Builder Pattern

```java
public class Builder<T> {
    private final Class<T> type;
    private final Map<String, Object> values = new HashMap<>();

    public Builder(Class<T> type) {
        this.type = type;
    }

    public <V> Builder<T> set(String field, V value) {
        values.put(field, value);
        return this;
    }

    public T build() {
        try {
            T instance = type.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                Field field = type.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(instance, entry.getValue());
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Build failed", e);
        }
    }
}
```

---

## Internal Working

### Generic Type Resolution

```java
// When you write:
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);

// Compiler:
1. Infers T = String
2. Verifies add(String) is valid
3. Erases T to Object in bytecode
4. Inserts (String) cast on get()
```

### Bridge Method Generation

```java
// Source
public class StringList extends ArrayList<String> {
    @Override
    public boolean add(String e) {
        return super.add(e);
    }
}

// After compilation
public class StringList extends ArrayList {
    @Override
    public boolean add(String e) {
        return super.add(e);
    }

    // Bridge method
    public boolean add(Object e) {
        return add((String) e);
    }
}
```

---

## JVM Perspective

### Type Information in Bytecode

```bash
# Generic type info preserved in Signature attribute
javap -v ArrayList.class | grep "Signature"
# Signature:Ljava/util/ArrayList<Ljava/lang/String;>;
```

### Reflection Access

```java
// Get generic type info via reflection
Field field = MyClass.class.getDeclaredField("list");
Type genericType = field.getGenericType();
if (genericType instanceof ParameterizedType pt) {
    Type[] typeArgs = pt.getActualTypeArguments();
    System.out.println("Element type: " + typeArgs[0]);
}
```

---

## Memory Representation

### Generic Collections

```java
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();
```

**Memory layout:**
```
ArrayList object:
┌─────────────────────────────┐
│ Object header (16 bytes)    │
│ elementData: Object[] ref ──┼──→ [Object, Object, ...]
│ size: int                   │
└─────────────────────────────┘

Both ArrayList objects have identical layout
The type parameter affects compile-time checking only
```

---

## Syntax

### Generic Interface Implementation

```java
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void save(T entity);
}

public class UserRepository implements Repository<User, Long> {
    @Override
    public User findById(Long id) { ... }

    @Override
    public List<User> findAll() { ... }

    @Override
    public void save(User entity) { ... }
}
```

### Generic Service Layer

```java
public interface Service<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(ID id);
}

public abstract class AbstractService<T, ID, R extends Repository<T, ID>>
        implements Service<T, ID> {

    protected final R repository;

    protected AbstractService(R repository) {
        this.repository = repository;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
}
```

---

## Easy Example

### Simple Generic Repository

```java
import java.util.*;
import java.util.function.Function;

public class SimpleRepository<T, ID> {
    private final Map<ID, T> store = new HashMap<>();
    private final Function<T, ID> idExtractor;

    public SimpleRepository(Function<T, ID> idExtractor) {
        this.idExtractor = idExtractor;
    }

    public void save(T entity) {
        ID id = idExtractor.apply(entity);
        store.put(id, entity);
    }

---

[📖 Continue to Part 2](README-part2.md)
