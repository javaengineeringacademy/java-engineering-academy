| Type Parameter | Erases To |
|----------------|-----------|
| `<T>` | `Object` |
| `<T extends Number>` | `Number` |
| `<T extends Comparable>` | `Comparable` |
| `? extends Number` | `Number` |
| `? super Integer` | `Object` |

---

## JVM Perspective

The JVM has **no knowledge of generics**. All generic type information is erased at compile time.

### What the JVM Sees

```java
// You write:
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);

// JVM sees:
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);  // Compiler inserted cast
```

### Bytecode Verification

```bash
# Compile generic code
javac -d out src/Box.java

# View bytecode
javap -c out/Box.class

# Output shows Object references, not String
# T is replaced by Object in the bytecode
```

### Reflection Limitations

```java
Box<String> box = new Box<>();

// This will NOT work as expected:
Field field = Box.class.getDeclaredField("value");
field.set(box, 42);  // Allowed! Type erasure means field type is Object

// You cannot do:
// Box<String>.class  // Compile error
// T.class            // Compile error
// instanceof T       // Compile error
```

---

## Memory Representation

### Object Layout

```java
Box<String> box = new Box<>();
box.set("hello");
```

**Memory layout (simplified):**
```
Heap:
┌─────────────────────────┐
│ Box instance            │
├─────────────────────────┤
│ Object header (16 bytes)│
│ value: reference ───────┼──→ String "hello"
└─────────────────────────┘
```

### Generics Don't Affect Memory

```java
Box<String> stringBox = new Box<>();
Box<Integer> intBox = new Box<>();
Box<List<Integer>> genericBox = new Box<>();

// All three Box objects have IDENTICAL memory layout
// The type parameter exists only at compile time
```

### Arrays and Generics

```java
// This is ILLEGAL:
// String[] strings = new String[10];  // OK
// Box<String>[] boxes = new Box<String>[10];  // Compile error!

// Why? Arrays carry reified type information at runtime
// But generics use type erasure — they are incompatible

// Workaround:
@SuppressWarnings("unchecked")
Box<String>[] boxes = (Box<String>[]) new Box[10];
```

---

## Syntax

### Generic Class Declaration

```java
// Single type parameter
public class Box<T> {
    private T content;
    
    public T getContent() { return content; }
    public void setContent(T content) { this.content = content; }
}

// Multiple type parameters
public class Pair<K, V> {
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
```

### Generic Interface Declaration

```java
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void save(T entity);
    void delete(T entity);
}
```

### Generic Method Declaration

```java
import java.util.Arrays;
import java.util.List;

public class Utility {
    public static <T> List<T> asList(T... elements) {
        return Arrays.asList(elements);
    }

    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
```

---

## When NOT to Use Generics

- **Simple types with no polymorphism**: If you only ever work with one type, a specific class is clearer.
- **Runtime type information is needed**: Type erasure means you can't inspect `T` at runtime. Use `Class<T>` tokens or reified generics patterns instead.
- **Performance-critical code with heavy boxing**: Generic collections box primitives. For numeric computation, use `IntStream` or primitive arrays.
- **Legacy codebases with raw types everywhere**: Introducing generics into a massive legacy codebase may require touching hundreds of files. Migrate incrementally.

## Trade-offs

- **Type safety vs. verbosity**: Generics catch errors at compile time but add syntax noise. `Map<String, List<Integer>>` is safer but harder to read than `Map`.
- **Erasure vs. flexibility**: Type erasure keeps bytecode compatible with pre-Java 5 but prevents runtime type checks. You gain backward compatibility at the cost of runtime introspection.
- **Bounded wildcards vs. simplicity**: `List<? extends Number>` is flexible but confuses developers. Use it for API consumers; use concrete types internally.

## Engineering Decision Framework

### ✅ Use Generics when:
- Type safety at compile time is critical
- Writing reusable code that works with multiple types
- Building collections or data structures
- API design that should prevent ClassCastException
- Creating type-safe builder patterns

### ❌ Avoid Generics when:
- Simple types with no polymorphism needed
- Performance-critical code where type erasure adds overhead
- Working with legacy code that uses raw types
- Runtime type information is required (type erasure limitation)

### Better Alternatives

| Alternative | When to use |
|-------------|-------------|
| Object casting | Legacy code, one-off type conversions |
| Specific typed classes | When only one type is ever used |
| var (Java 10+) | Local variable type inference |
| Annotation processing | Compile-time code generation |

### Production Examples
- Repository pattern with generic entity types
- Type-safe HTTP client responses
- Generic event handling systems
- Collections framework (List<T>, Map<K,V>)
- Generic DAO/Service base classes

### Common Production Mistakes
- Using raw types (loses type safety)
- Overusing wildcards (makes API hard to understand)
- Ignoring type erasure limitations (T.class won't work)
- Creating generic arrays (not allowed due to reification)
- Not using bounded types when constraints exist

## Production Incidents

### Incident 1: Type Erasure Causing ClassCastException

**Problem:** A deserialization service crashed at runtime with `ClassCastException: java.lang.Integer cannot be cast to java.lang.String`.
**Cause:** A generic class `Box<T>` was used with raw types in an intermediate version of the code. When `Box rawBox = (Box) getBox()` was called, type information was lost. The raw type cast allowed an `Integer` to be placed in what the caller expected to be a `String` box.
**Impact:** Production service crashed intermittently when processing specific data types. Debugging took 2 days due to intermittent nature.
**Detection:** Runtime exception logs showed ClassCastException at unexpected locations.
**Solution:** Replace all raw types with parameterized types. Add `@SuppressWarnings("unchecked")` only where unavoidable and add explicit comments. Enable compiler warnings for raw type usage.
**Prevention:** Enable `-Xlint:unchecked` compiler warnings. Use static analysis (ErrorProne) to detect raw type usage. Enforce generics in code review guidelines.

### Incident 2: Raw Types Causing Runtime Errors

**Problem:** A configuration management system stored incorrect values, causing production deployments to fail silently.
**Cause:** A `Map` was declared without type parameters (`Map config = new HashMap()`). Values of mixed types (String, Integer, Boolean) were stored without type checking. A `ClassCastException` occurred when code assumed all values were Strings.
**Impact:** Wrong configuration values deployed to production. 500+ user accounts affected. Rollback required.
**Detection:** Users reported unexpected behavior. Logs showed ClassCastException in configuration parsing code.
**Solution:** Replace `Map` with `Map<String, Object>` and add explicit type checks when retrieving values. Better yet, use `Map<String, String>` and parse values at access time.
**Prevention:** Never use raw types. Use IDE inspections to flag raw type usage. Add pre-commit hooks that fail on raw type introduction.

## Production Checklist

### ✅ Before using Generics in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
