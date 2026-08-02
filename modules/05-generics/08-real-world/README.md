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

    public T findById(ID id) {
        return store.get(id);
    }

    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    public void delete(ID id) {
        store.remove(id);
    }

    public static void main(String[] args) {
        SimpleRepository<User, Long> repo = new SimpleRepository<>(User::getId);
        repo.save(new User(1L, "Alice"));
        repo.save(new User(2L, "Bob"));

        System.out.println(repo.findById(1L));  // User{id=1, name=Alice}
        System.out.println(repo.findAll());      // [User{id=1, name=Alice}, User{id=2, name=Bob}]
    }

    record User(Long id, String name) {}
}
```

---

## Medium Example

### Generic Builder Pattern

```java
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GenericBuilder<T> {
    private final Class<T> type;
    private final Map<String, Object> values = new HashMap<>();

    public GenericBuilder(Class<T> type) {
        this.type = type;
    }

    public <V> GenericBuilder<T> set(String field, V value) {
        values.put(field, value);
        return this;
    }

    @SuppressWarnings("unchecked")
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
            throw new RuntimeException("Build failed for " + type.getName(), e);
        }
    }

    public static <T> GenericBuilder<T> of(Class<T> type) {
        return new GenericBuilder<>(type);
    }

    // Usage
    public static class Person {
        private String name;
        private int age;

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }

    public static void main(String[] args) {
        Person person = GenericBuilder.of(Person.class)
                .set("name", "Alice")
                .set("age", 30)
                .build();
        System.out.println(person);  // Person{name='Alice', age=30}
    }
}
```

---

## Hard Example

### Type-Safe Event System

```java
import java.util.*;
import java.util.concurrent.*;

public class TypeSafeEventBus {

    private final Map<Class<?>, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();

    @FunctionalInterface
    public interface EventHandler<T> {
        void handle(T event);
    }

    public <T> void register(Class<T> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                .add(handler);
    }

    @SuppressWarnings("unchecked")
    public <T> void post(T event) {
        List<EventHandler<?>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (EventHandler<?> handler : eventHandlers) {
                ((EventHandler<T>) handler).handle(event);
            }
        }
    }

    // Event classes
    public record UserCreatedEvent(String userId, String name) {}
    public record UserDeletedEvent(String userId) {}
    public record OrderPlacedEvent(String orderId, double amount) {}

    public static void main(String[] args) {
        TypeSafeEventBus bus = new TypeSafeEventBus();

        bus.register(UserCreatedEvent.class, e ->
            System.out.println("User created: " + e.name()));

        bus.register(UserDeletedEvent.class, e ->
            System.out.println("User deleted: " + e.userId()));

        bus.register(OrderPlacedEvent.class, e ->
            System.out.println("Order placed: " + e.orderId()));

        bus.post(new UserCreatedEvent("U001", "Alice"));
        bus.post(new OrderPlacedEvent("O001", 99.99));
        bus.post(new UserDeletedEvent("U001"));
    }
}
```

---

## Enterprise Example

### Complete Generic CRUD Service

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface CrudService<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    T update(ID id, T entity);
    void delete(ID id);
    long count();
    boolean existsById(ID id);
}

public abstract class AbstractCrudService<T, ID>
        implements CrudService<T, ID> {

    protected final Map<ID, T> store = new HashMap<>();
    protected final Function<T, ID> idExtractor;
    protected final BiConsumer<T, ID> idSetter;

    protected AbstractCrudService(
            Function<T, ID> idExtractor,
            BiConsumer<T, ID> idSetter) {
        this.idExtractor = idExtractor;
        this.idSetter = idSetter;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public T save(T entity) {
        ID id = idExtractor.apply(entity);
        store.put(id, entity);
        return entity;
    }

    @Override
    public T update(ID id, T entity) {
        if (!store.containsKey(id)) {
            throw new IllegalArgumentException("Entity not found: " + id);
        }
        idSetter.accept(entity, id);
        store.put(id, entity);
        return entity;
    }

    @Override
    public void delete(ID id) {
        store.remove(id);
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public boolean existsById(ID id) {
        return store.containsKey(id);
    }
}

// Concrete implementation
class UserService extends AbstractCrudService<User, Long> {

    public UserService() {
        super(User::getId, User::withId);
    }

    public List<User> findByName(String name) {
        return findAll().stream()
                .filter(u -> u.name().equals(name))
                .toList();
    }
}

record User(Long id, String name) {
    public static User withId(Long id, String name) {
        return new User(id, name);
    }
}

class Demo {
    public static void main(String[] args) {
        UserService service = new UserService();
        service.save(new User(1L, "Alice"));
        service.save(new User(2L, "Bob"));

        System.out.println(service.findById(1L));  // Optional[User{id=1, name=Alice}]
        System.out.println(service.findAll());      // [User{id=1, name=Alice}, User{id=2, name=Bob}]
        System.out.println(service.count());        // 2
    }
}
```

---

## Performance

### Generic vs Raw Collections

| Aspect | Generic | Raw |
|--------|---------|-----|
| Type safety | Compile time | Runtime |
| Casting | None needed | Manual |
| Performance | Identical | Identical |
| Memory | Identical | Identical |

### Collection Performance

```java
// Generic types have no performance overhead
List<String> list = new ArrayList<>();  // Same as List list = new ArrayList();
```

---

## Best Practices

1. **Use specific types in implementations** — `UserRepository` not `Repository<Object>`
2. **Leverage bounded types** — `<T extends BaseEntity>` for common operations
3. **Use wildcards in APIs** — `List<? extends T>` for flexibility
4. **Implement Comparable** — For sorting and ordering
5. **Use functional interfaces** — For callbacks and transformations

---

## Common Mistakes

### 1. Overusing Object

```java
// BAD
public class Repository {
    private List<Object> entities = new ArrayList<>();
}

// GOOD
public class Repository<T> {
    private List<T> entities = new ArrayList<>();
}
```

### 2. Ignoring Type Safety

```java
// BAD
public Object findById(int id) {
    return store.get(id);  // Must cast
}

// GOOD
public T findById(ID id) {
    return store.get(id);  // Type-safe
}
```

---

## Pitfalls

### 1. Type Erasure in Collections

```java
// These are the same at runtime
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();
System.out.println(strings.getClass() == integers.getClass()); // true
```

### 2. Generic Arrays

```java
// ILLEGAL
// List<String>[] arrays = new List<String>[10];

// WORKAROUND
@SuppressWarnings("unchecked")
List<String>[] arrays = (List<String>[]) new List[10];
```

---

## Debugging Tips

### 1. Check Generic Types

```java
// Use reflection to inspect generic types
Field field = MyClass.class.getDeclaredField("list");
Type type = field.getGenericType();
System.out.println(type);  // java.util.List<java.lang.String>
```

### 2. Read Stack Traces

```
ClassCastException: String cannot be cast to Integer
// Check generic type usage in the code
```

---

## Comparison Table

| Pattern | Use Case | Generic Benefit |
|---------|----------|-----------------|
| Repository | Data access | Type-safe CRUD |
| Builder | Object creation | Flexible construction |
| Service | Business logic | Reusable operations |
| Event Bus | Communication | Type-safe events |
| DTO | Data transfer | Compile-time safety |

---

## Decision Tree

```
Do you need to access data?
├── Yes → Use Repository pattern with generics
└── No → Do you need to create complex objects?
    ├── Yes → Use Builder pattern
    └── No → Do you need business logic?
        ├── Yes → Use Service pattern
        └── No → Do you need event communication?
            ├── Yes → Use Event Bus
            └── No → Consider other patterns
```

---

## Interview Questions

### Q1: How does the Java Collections Framework use generics?

**A:** All collection interfaces (List, Set, Map) are parameterized. For example, `List<E>` stores elements of type E, `Map<K,V>` stores key-value pairs. This provides compile-time type safety.

### Q2: What is the Repository pattern with generics?

**A:** A generic Repository interface (`Repository<T, ID>`) defines CRUD operations for any entity type. Concrete implementations (e.g., `UserRepository`) specify the entity and ID types.

### Q3: How do you implement a generic Builder?

**A:** Use reflection to set fields by name, or use functional interfaces for type-safe field setters. The builder captures type information at construction time.

### Q4: What are the benefits of generic service layers?

**A:** Generic services reduce code duplication, enforce type safety, and make it easy to add new entity types by extending abstract base classes.

### Q5: How do you handle type erasure in real-world code?

**A:** Use type tokens (Class<T>), reflection, or TypeToken patterns to preserve runtime type information when needed.

---

## Exercises

### Exercise 1: Generic Repository

Implement a generic `Repository<T, ID>` with in-memory storage.

### Exercise 2: Generic Builder

Create a generic Builder that works with any class using reflection.

### Exercise 3: Generic Event Bus

Build a type-safe event bus that registers handlers by event type.

---

## Assignments

### Assignment 1: Complete CRUD System

Build a complete CRUD system with:
1. Generic Repository interface
2. In-memory implementation
3. Generic Service layer
4. Type-safe DTO conversion

### Assignment 2: Generic Validation Framework

Create a validation framework that:
1. Uses generics for type-safe validators
2. Supports custom validation rules
3. Provides meaningful error messages

---

## Mini Project

### Generic Data Access Framework

Build a production-quality data access framework:
1. Generic Repository with multiple implementations
2. Type-safe Query Builder
3. Generic Service layer
4. DTO/Entity conversion utilities
5. Event system for entity changes

---

## Summary

Real-world generics applications include:

1. **Java Collections Framework** — The foundation of generic usage
2. **Repository Pattern** — Type-safe data access
3. **Builder Pattern** — Flexible object construction
4. **Service Layers** — Reusable business logic
5. **Event Systems** — Type-safe communication
6. **DTO/VO Patterns** — Compile-time data transfer

Generics make Java code safer, more flexible, and easier to maintain.

---

## References

- [Java Collections Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/)
- [Repository Pattern](https://www.baeldung.com/java-repository-pattern)
- [Effective Java - Chapter on Generics](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Spring Data JPA - Generics](https://spring.io/projects/spring-data-jpa)
