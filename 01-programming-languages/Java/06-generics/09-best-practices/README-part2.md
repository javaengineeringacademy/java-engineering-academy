# Best Practices (Part 2)

## When to Use Generics vs Alternatives

| Situation | Use Generics | Use Alternative | Why |
|-----------|--------------|-----------------|-----|
| Container holding different types | ✅ Generic class | | Type safety |
| Single method with type flexibility | ✅ Generic method | | Type safety |
| Fixed type, simple logic | | Method overloading | Simplicity |
| Only 2-3 types needed | | Method overloading | Simplicity |
| Legacy code, can't change signature | | Object + casting | Backward compat |
| Performance-critical, known types | | Concrete types | Zero overhead |

### Alternative 1: Method Overloading

```java
// Instead of generic method:
// <T> String stringify(T value) { return value.toString(); }

// Use overloading when types are fixed and few:
String stringify(int value) { return String.valueOf(value); }
String stringify(String value) { return value; }
String stringify(boolean value) { return String.valueOf(value); }
```

**When to use:** Few known types, simple logic, no type inference needed.

### Alternative 2: Object + Casting

```java
// Instead of generic class:
// Box<T> { T value; T get(); }

// Use Object when:
// 1. Interfacing with pre-generics API
// 2. Type is truly unknown at compile time
// 3. Can't change the class signature

Object box = new Object();
String s = (String) box;  // Unsafe cast
```

**When to use:** Legacy code only. Never in new code.

### Alternative 3: Template Method Pattern

```java
// Instead of complex generic bounds:
// <T extends Comparable<T> & Serializable & Cloneable>

// Use template method when you need multiple behaviors:
abstract class Processor<T> {
    abstract T process(T input);
    
    void validate(T input) { ... }
    void log(T input) { ... }
}
```

**When to use:** Complex constraints, multiple behaviors, framework code.

### Alternative 4: Functional Interface + Lambda

```java
// Instead of generic strategy pattern:
// GenericStrategy<T> { T execute(T input); }

// Use functional interface:
Function<String, String> strategy = input -> input.toUpperCase();
UnaryOperator<String> strategy = String::toUpperCase;
```

**When to use:** Simple single-method behavior.

## Migration Playbook: Raw Types → Parameterized Types

### Step 1: Identify Raw Types

```bash
# Find raw type warnings
javac -Xlint:unchecked MyClass.java
```

Look for:
- `List` instead of `List<String>`
- `Map` instead of `Map<String, Object>`
- `Class` instead of `Class<?>`
- `Comparator` instead of `Comparator<String>`

### Step 2: Add Type Parameters

```java
// Before
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);

// After
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);  // No cast
```

### Step 3: Fix Method Signatures

```java
// Before
public void process(List items) {
    for (Object item : items) {
        String s = (String) item;
        // ...
    }
}

// After
public void process(List<String> items) {
    for (String item : items) {
        // No cast needed
    }
}
```

### Step 4: Add Wildcards for Flexibility

```java
// Before (too restrictive)
public void copy(List<String> dest, List<String> src) { ... }

// After (flexible)
public <T> void copy(List<? super T> dest, List<? extends T> src) { ... }
```

### Step 5: Verify

```bash
# Compile with warnings
javac -Xlint:unchecked *.java

# Run tests
mvn test
```

## Generic API Design Patterns for Libraries

### Pattern 1: Type-Safe Heterogeneous Container

```java
public class ServiceLocator {
    private final Map<Class<?>, Object> services = new HashMap<>();
    
    public <T> void register(Class<T> type, T service) {
        services.put(type, type.cast(service));
    }
    
    public <T> T getService(Class<T> type) {
        return type.cast(services.get(type));
    }
}
```

**Use when:** You need to store different types in one container.

### Pattern 2: Generic Builder

```java
public class ResponseBuilder<T> {
    private T data;
    private int status;
    
    public ResponseBuilder<T> data(T data) {
        this.data = data;
        return this;
    }
    
    public ResponseBuilder<T> status(int status) {
        this.status = status;
        return this;
    }
    
    public Response<T> build() {
        return new Response<>(data, status);
    }
}
```

**Use when:** Building complex objects with fluent API.

### Pattern 3: Generic Repository

```java
public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    void deleteById(ID id);
}
```

**Use when:** CRUD operations across different entity types.

### Pattern 4: Generic Event Handler

```java
public interface EventHandler<T extends Event> {
    void handle(T event);
}

public class EventDispatcher {
    private final Map<Class<?>, List<EventHandler<?>>> handlers = new HashMap<>();
    
    public <T extends Event> void register(Class<T> type, EventHandler<T> handler) {
        handlers.computeIfAbsent(type, k -> new ArrayList<>()).add(handler);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Event> void dispatch(T event) {
        List<EventHandler<?>> handlers = this.handlers.get(event.getClass());
        if (handlers != null) {
            for (EventHandler<?> handler : handlers) {
                ((EventHandler<T>) handler).handle(event);
            }
        }
    }
}
```

**Use when:** Type-safe event systems.

### Pattern 5: Generic Visitor

```java
public interface Visitor<T> {
    void visit(T item);
}

public class Processor<T> {
    private final List<T> items = new ArrayList<>();
    
    public void accept(Visitor<T> visitor) {
        for (T item : items) {
            visitor.visit(item);
        }
    }
}
```

**Use when:** Processing collections with type-specific behavior.
