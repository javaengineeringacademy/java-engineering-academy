# Internals: Introduction to Reflection

## How Reflection Works Under the Hood

### The Class Loading Mechanism

When the JVM loads a class, it reads the `.class` bytecode file and creates a `Class` object in the method area (metaspace in Java 8+). This `Class` object contains all the metadata the JVM needs: field definitions, method signatures, constructor parameters, annotations, and inheritance relationships.

```
.class file → ClassLoader → Class object in Metaspace
                              ├── Field[] (declared fields)
                              ├── Method[] (declared methods)
                              ├── Constructor[] (constructors)
                              ├── Class<?> (superclass)
                              ├── Class<?>[] (interfaces)
                              └── Annotation[] (annotations)
```

### The java.lang.reflect Package

The reflection API is built on top of the JVM's internal type system. Each reflective object (`Class`, `Field`, `Method`, `Constructor`) is a Java object that wraps JVM-internal type descriptors.

```
Source Code          Bytecode              Reflection API
───────────         ─────────             ──────────────
class User    →    User.class      →    Class<User>
  String name  →   Field: name       →    Field
  void greet() →   Method: greet     →    Method
  User()       →   Constructor: User →    Constructor<User>
```

### Native Method Invocations

Many reflection operations ultimately call native methods implemented in C/C++ within the JVM:

| Reflection Method | Native Call |
|-------------------|-------------|
| `Class.forName()` | `Class.forName0()` |
| `Method.invoke()` | `Method.invoke0()` |
| `Field.get()` | `Field.getField()` |
| `Constructor.newInstance()` | `Constructor.newInstance0()` |

### Access Control Bypass

When you call `setAccessible(true)`, the JVM sets a flag on the reflective object that suppresses the access check normally performed by the JVM's security manager. The field/method is still private in the class hierarchy, but the reflective access check is skipped.

## Performance Internals

### Method Lookup Cost

Finding a method by name involves:
1. Linear scan of the method table
2. Parameter type matching (identity comparison)
3. Access control verification
4. Boxing/unboxing of primitive arguments

### JIT Compiler Limitations

The HotSpot JIT compiler cannot inline reflective calls because:
- The target method is not known at compile time
- The receiver type is not statically determined
- Escape analysis cannot prove the Method object is thread-local

### The setAccessible Optimization

`setAccessible(true)` eliminates the per-call security check, which accounts for roughly 40% of the reflection overhead. The remaining 60% comes from boxing/unboxing and the indirect invocation through JNI.

## Further Reading

- [OpenJDK Reflection Source](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/reflect/)
- [JVM Specification: Reflection](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-5.html)
