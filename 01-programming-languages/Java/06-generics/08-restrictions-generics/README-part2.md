
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
```
# 08 - Real-World Applications (Part 2)

[📖 Back to Part 1](README.md)

---


---

[📖 Continue to Part 2](README-part2.md)
# 08 - Real-World Applications (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---


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
