# Internals: Class Introspection

## How Class Metadata is Stored

### Bytecode Structure

A `.class` file contains a constant pool with references to:
- **This class** — The class being defined
- **Super class** — The parent class
- **Interfaces** — Implemented interfaces
- **Fields** — Name, type descriptor, access flags
- **Methods** — Name, descriptor, access flags, bytecode
- **Attributes** — Source file, inner classes, annotations, etc.

### The Class Object Internals

When the JVM loads a class, the `Class` object is populated with:

```
Class<String> {
  name: "java.lang.String"
  classLoader: BootstrapClassLoader
  superclass: Object.class
  interfaces: [Serializable, Comparable, CharSequence]
  fields: [...]
  methods: [...]
  constructors: [...]
  annotations: [...]
  modifiers: public final
}
```

### Reflection Data Caching

The `Class` object caches reflection data in a `SoftReference<ReflectionData<T>>`:

```java
// Internal structure (simplified)
class ReflectionData<T> {
    volatile Field[] declaredFields;
    volatile Field[] publicFields;
    volatile Method[] declaredMethods;
    volatile Method[] publicMethods;
    volatile Constructor<T>[] declaredConstructors;
    volatile Constructor<T>[] publicConstructors;
    volatile Class<?>[] declaredClasses;
    volatile Class<?>[] publicClasses;
}
```

Soft references allow GC to reclaim this memory under memory pressure, after which the data is re-parsed from bytecode.

### Generic Type Information

Generic type information is stored in bytecode attributes:
- **Signature attribute** — For classes, fields, methods with generic types
- **TypeParameters attribute** — For type variable declarations
- **Exceptions attribute** — For generic exception types

The JVM preserves this information even though generics are erased at runtime, allowing reflection to access `getGenericType()`, `getGenericReturnType()`, etc.

### Module System Impact (Java 9+)

Each class belongs to a module. The module controls access:
- **Exported packages** — Accessible via reflection
- **Opened packages** — Accessible for deep reflection (setAccessible)
- **Module graph** — Determines classloader delegation

```java
// Java 9+ may throw InaccessibleObjectException
// Fix: --add-opens java.base/java.lang=ALL-UNNAMED
```
