# Keywords Memory Model

## Keyword Processing Memory

Keywords are processed at compile time and don't occupy runtime memory.

### Compilation Phase

```
Source Code → Lexer → Tokens → Parser → AST → Bytecode
                ↑
           Keywords recognized here
           (no runtime memory needed)
```

### Static Fields Memory

```java
public class MyClass {
    static int counter;  // Stored in Method Area (Metaspace)
    static final String NAME = "MyClass";  // Constant pool
}

// Memory: One copy per class in Metaspace
```

### Instance Fields Memory

```java
public class MyClass {
    int value;  // Stored in Heap (per instance)
}

MyClass obj1 = new MyClass();
MyClass obj2 = new MyClass();

// obj1.value and obj2.value are separate memory locations
```

### Final Fields Memory

```java
public class MyClass {
    final int id;  // Stored in Heap, immutable after construction
    
    public MyClass(int id) {
        this.id = id;
    }
}

// Final fields can be optimized by JVM (inlined if compile-time constant)
```

### Enum Memory

```java
public enum Status {
    ACTIVE, INACTIVE, PENDING
}

// Each enum constant is a static final field
// Stored in Method Area
// Memory: ~16 bytes per constant (object header + ordinal + name)
```

### Record Memory (Java 16+)

```java
public record Point(int x, int y) {}

// Compiled to:
// - Final class with final fields
// - Constructor, getters, equals(), hashCode(), toString()
// - Memory: Same as equivalent class
```

### Sealed Classes Memory (Java 17+)

```java
public sealed class Shape permits Circle, Rectangle, Triangle {}

// JVM maintains permitted subclass list
// Used for exhaustive pattern matching
// Memory: Minimal metadata overhead
```
