# Object Lifecycle

## Object Creation

```java
Person person = new Person("Alice", 30);
```

### Steps:
1. **Memory Allocation** - Heap space allocated
2. **Field Initialization** - Default values (0, null, false)
3. **Instance Initializers** - Run in declaration order
3. **Constructor Execution** - Body runs
3. **Reference Returned** - Assigned to variable

```java
Person p = new Person("Alice", 30);
// Stack: person (reference) → Heap: Person object
```

## Object Usage

```java
Person person = new Person("Alice", 30);
person.greet();          // Instance method
String name = person.getName();  // Getter
person.setAge(31);       // Setter
```

## Object Destruction

### Eligibility for GC
Object eligible when **no reachable references**:

```java
Person p1 = new Person("A");
Person p2 = p1;        // p2 references same object
p1 = null;             // Still reachable via p2
p2 = null;             // Now eligible for GC
```

### Reference Types
| Reference Type | GC Behavior |
|----------------|-------------|
| Strong | Never collected |
| Soft | Collected if memory low |
| Weak | Collected next GC |
| Phantom | Collected, then enqueued |

```java
import java.lang.ref.*;

SoftReference<String> soft = new SoftReference<>("data");
WeakReference<String> weak = new WeakReference<>("data");
PhantomReference<String> phantom = new PhantomReference<>("data", queue);
```

## Object Methods

### toString()
```java
@Override
public String toString() {
    return "Person{name='%s', age=%d}".formatted(name, age);
}
```

### equals() & hashCode()
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person that = (Person) o;
    return age == that.age && Objects.equals(name, that.name);
}

@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

### clone()
```java
@Override
protected Person clone() throws CloneNotSupportedException {
    return (Person) super.clone();  // Shallow copy
}
```

### finalize() (Deprecated)
```java
@Deprecated(since = "9", forRemoval = true)
@Override
protected void finalize() throws Throwable {
    try { /* cleanup */ } finally { super.finalize(); }
}
```

**Use try-with-resources or Cleaner instead.**

## Object Lifecycle Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     OBJECT LIFECYCLE                         │
├─────────────────────────────────────────────────────────────┤
│  1. CLASS LOADING                                            │
│     ├── Static fields initialized                            │
│     ├── Static blocks executed                               │
│     └── Class ready for instantiation                        │
├─────────────────────────────────────────────────────────────┤
│  2. INSTANTIATION                                            │
│     ├── new Operator                                         │
│     ├── Memory allocated on Heap                             │
│     ├── Fields default initialized                           │
│     ├── Instance initializers run                            │
│     ├── Constructor executes                                 │
│     └── Reference returned                                   │
├─────────────────────────────────────────────────────────────┤
│  3. OBJECT LIVING                                            │
│     ├── Method calls                                         │
│     ├── Field access                                         │
│     ├── State mutations                                      │
│     └── Reference passing                                    │
├─────────────────────────────────────────────────────────────┤
│  4. ELIGIBILITY FOR GC                                       │
│     ├── No strong references                                 │
│     ├── Soft/Weak/Phantom refs only                         │
│     └── GC determines collection time                        │
├─────────────────────────────────────────────────────────────┤
│  5. FINALIZATION (DEPRECATED)                                │
│     ├── finalize() called (once)                             │
│     └── Object memory reclaimed                              │
└─────────────────────────────────────────────────────────────┘
```

## Object Cleanup Patterns

### Try-with-resources (Preferred)
```java
try (FileInputStream fis = new FileInputStream("file.txt")) {
    // use fis
} // fis.close() automatically called
```

### Cleaner (Java 9+)
```java
import java.lang.ref.Cleaner;

class Resource {
    private static final Cleaner cleaner = Cleaner.create();
    private final Cleaner.Cleanable cleanable;

    Resource() {
        cleanable = cleaner.register(this, () -> cleanup());
    }
    private void cleanup() { /* release resources */ }
}
```

### AutoCloseable Interface
```java
public class Resource implements AutoCloseable {
    @Override
    public void close() { /* release */ }
}

// Usage
try (Resource r = new Resource()) {
    // use
}
```

## Interview Questions

1. **When is an object eligible for GC?**
   - No strong references

2. **Can we force GC?**
   - `System.gc()` suggests, not guarantees

3. **What is `finalize()`?**
   - Deprecated, use try-with-resources/Cleaner

4. **How to swap references safely?**
   - Use `AtomicReference` or synchronized block

---

## Further Reading

### Official Documentation
- [JLS - Object Lifecycle](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.6)
- [JLS - finalize()](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.6)
- [Effective Java Item 8: Clean Up](https://www.oracle.com/technical-resources/articles/java/effective-java.html)

### Books
- *Effective Java* (3rd Ed.) — Joshua Bloch — Items 8-9
- *Java Concurrency in Practice* — Brian Goetz

### Articles & Blogs
- [Java Memory Model](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html)
- [Garbage Collection in Java](https://docs.oracle.com/javase/10/gctuning/)
- [Java 9 Cleaner API](https://docs.oracle.com/javase/9/docs/api/java/lang/ref/Cleaner.html)