# Real-World Generics Usage

## Introduction

Apply generics concepts to real-world scenarios including frameworks, libraries, and enterprise applications.

## Learning Objectives

- Implement generic data structures
- Create type-safe APIs
- Design generic frameworks
- Handle complex generic scenarios

## Prerequisites

- All previous generics topics
- Understanding of design patterns
- Basic framework concepts

## Why This Matters

Generics are fundamental to modern Java frameworks and libraries. Understanding real-world usage prepares you for enterprise development.

## Syntax/Patterns

```java
// Repository pattern
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(T entity);
}

// Builder pattern with generics
public class Builder<T> {
    private T product;

    public Builder<T> with(Consumer<T> configurator) {
        configurator.accept(product);
        return this;
    }

    public T build() {
        return product;
    }
}
```

## Examples

```java
// Example 1: Generic Repository
public class InMemoryRepository<T, ID> implements Repository<T, ID> {
    private Map<ID, T> store = new HashMap<>();
    private Function<T, ID> idExtractor;

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
    public T save(T entity) {
        ID id = idExtractor.apply(entity);
        store.put(id, entity);
        return entity;
    }

    @Override
    public void delete(T entity) {
        ID id = idExtractor.apply(entity);
        store.remove(id);
    }
}

// Usage
Repository<User, Long> userRepo = new InMemoryRepository<>(User::getId);

// Example 2: Generic Event System
public class EventPublisher {
    private Map<Class<?>, List<Consumer<?>>> handlers = new HashMap<>();

    public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(handler);
    }

    public <T> void publish(T event) {
        List<Consumer<?>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (Consumer<?> handler : eventHandlers) {
                ((Consumer<T>) handler).accept(event);
            }
        }
    }
}

// Example 3: Generic Validator
public class Validator<T> {
    private List<ValidationRule<T>> rules = new ArrayList<>();

    public Validator<T> addRule(ValidationRule<T> rule) {
        rules.add(rule);
        return this;
    }

    public ValidationResult validate(T object) {
        List<String> errors = new ArrayList<>();
        for (ValidationRule<T> rule : rules) {
            if (!rule.isValid(object)) {
                errors.add(rule.getMessage());
            }
        }
        return new ValidationResult(errors.isEmpty(), errors);
    }
}

// Usage
Validator<User> userValidator = new Validator<User>()
    .addRule(new ValidationRule<>("Name required", u -> u.getName() != null))
    .addRule(new ValidationRule<>("Valid email", u -> u.getEmail().contains("@")));

// Example 4: Generic Cache with expiration
public class ExpiringCache<K, V> {
    private Map<K, CacheEntry<V>> cache = new HashMap<>();
    private Duration ttl;

    public ExpiringCache(Duration ttl) {
        this.ttl = ttl;
    }

    public void put(K key, V value) {
        cache.put(key, new CacheEntry<>(value, Instant.now()));
    }

    public Optional<V> get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null && !entry.isExpired(ttl)) {
            return Optional.of(entry.value);
        }
        cache.remove(key);
        return Optional.empty();
    }
}
```

## Exercises

1. Create a generic Tree data structure with type-safe operations.
2. Implement a generic Observer pattern with type-safe events.
3. Build a generic cache with different eviction strategies.

## Interview Questions

- How would you implement a generic type-safe Builder?
- What are the challenges of using generics with reflection?
- How do you handle generic types in serialization?

## Common Pitfalls

- Not considering type safety in framework design
- Overcomplicating generic hierarchies
- Not handling type erasure properly

## Best Practices

1. Design for type safety from the start
2. Use bounded types to enforce constraints
3. Provide clear documentation for generic APIs
4. Test with multiple type implementations
5. Consider the limitations of type erasure

## Real World Applications

- Spring Framework (Repository pattern)
- Hibernate (Type-safe queries)
- Jackson (JSON serialization)
- Google Guava (Immutable collections)

## References

- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Google Guava](https://github.com/google/guava)
- [Effective Java](https://www.oreilly.com/library/view/effective-java/9780134686097/)

## Summary

In this topic, you learned how generics are used in real-world applications, including frameworks and enterprise systems. Practice with the exercises before building the mini-project.
