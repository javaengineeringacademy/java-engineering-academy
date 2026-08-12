    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<T> type, int size) {
        return (T[]) java.lang.reflect.Array.newInstance(type, size);
    }
    
    // Cannot use new T()
    public static <T> T safeNew(Class<T> type) throws Exception {
        return type.getDeclaredConstructor().newInstance();
    }
    
    public static void main(String[] args) throws Exception {
        checkType(List.of("a", "b"));
        checkType(List.of(1, 2, 3));
        
        // Create array using reflection
        String[] strings = createArray(String.class, 10);
        System.out.println("Array length: " + strings.length);
        
        // Create instance using class token
        ArrayList<?> list = safeNew(ArrayList.class);
        System.out.println("Created: " + list.getClass());
    }
}
```

---

## Hard Example

### TypeToken Pattern for Runtime Type Information

```java
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class TypeToken<T> {
    private final Type type;
    
    protected TypeToken() {
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) superclass;
        type = pt.getActualTypeArguments()[0];
    }
    
    public Type getType() {
        return type;
    }
    
    @SuppressWarnings("unchecked")
    public Class<T> getRawType() {
        return (Class<T>) type;
    }
    
    // Usage
    public static void main(String[] args) {
        TypeToken<List<String>> token = new TypeToken<List<String>>() {};
        System.out.println("Type: " + token.getType());
        System.out.println("Raw: " + token.getRawType());
        
        // Type safety at runtime
        Type type = token.getType();
        if (type instanceof ParameterizedType pt) {
            Type[] typeArgs = pt.getActualTypeArguments();
            System.out.println("Type argument: " + typeArgs[0]);
        }
    }
}
```

---

## Enterprise Example

### Generic Type Information Utility

```java
import java.lang.reflect.*;
import java.util.*;

public final class TypeInfo {
    
    private TypeInfo() {}
    
    public static Type getSuperclassTypeParameter(
            Class<?> subclass, int index) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof ParameterizedType pt) {
            return pt.getActualTypeArguments()[index];
        }
        throw new IllegalArgumentException(
            subclass.getName() + " is not parameterized");
    }
    
    public static <T> Class<T> getCollectionElementType(
            Class<?> collectionClass) {
        Type type = getSuperclassTypeParameter(collectionClass, 0);
        if (type instanceof Class<?> clazz) {
            @SuppressWarnings("unchecked")
            Class<T> result = (Class<T>) clazz;
            return result;
        }
        throw new IllegalArgumentException(
            "Type parameter is not a simple class");
    }
    
    public static Type[] getMapTypeParameters(
            Class<?> mapClass) {
        Type type = getSuperclassTypeParameter(mapClass, 0);
        if (type instanceof ParameterizedType pt) {
            return pt.getActualTypeArguments();
        }
        throw new IllegalArgumentException(
            "Type parameter is not parameterized");
    }
    
    // Type-safe deserialization pattern
    public static <T> T deserialize(String json, Class<T> type) {
        // In real code, use Jackson or Gson
        // This demonstrates type token usage
        return null;  // Placeholder
    }
    
    public static void main(String[] args) {
        // Get type parameter of a subclass
        Type type = getSuperclassTypeParameter(
            StringList.class, 0);
        System.out.println("Element type: " + type);  // class java.lang.String
        
        // Type-safe list creation
        List<String> list = createTypedList(String.class);
        list.add("hello");
        System.out.println("List type: " + list.getClass());
    }
    
    static class StringList extends ArrayList<String> {}
    
    @SuppressWarnings("unchecked")
    private static <T> List<T> createTypedList(Class<T> elementType) {
        return new ArrayList<>();
    }
}
```

---

## Performance

### Type Erasure Overhead

| Aspect | Impact |
|--------|--------|
| Compile time | Slightly more (type checking) |
| Runtime | Zero overhead (erased) |
| Bytecode size | Slightly larger (bridge methods) |
| Method dispatch | Same as non-generic |
| JIT optimization | Identical to raw types |

### Bridge Method Cost

```java
// Bridge methods add one extra method call
// But JVM can inline them, so cost is negligible
public class StringBox extends Box {
    public void set(String value) { super.set(value); }
    public void set(Object value) { set((String) value); }  // Bridge
}
```

---

## Best Practices

1. **Understand erasure limitations** — Know what you can't do with generics
2. **Use type tokens for runtime type info** — `Class<T>` or `TypeToken<T>`
3. **Document generic types** — Javadoc `@param` and `@return` tags
4. **Suppress warnings properly** — `@SuppressWarnings("unchecked")` with justification
5. **Avoid raw types** — They bypass type safety
6. **Use `@SafeVarargs`** — For generic varargs methods

---

## Common Mistakes

### 1. Using instanceof with Parameterized Types

```java
// WRONG
List<String> list = List.of("a", "b");
if (list instanceof List<String>) { }  // Compile error!

// RIGHT
if (list instanceof List<?>) { }
```

### 2. Creating Generic Arrays

```java
// WRONG
// List<String>[] arrays = new List<String>[10];

// RIGHT
@SuppressWarnings("unchecked")
List<String>[] arrays = (List<String>[]) new List[10];
```

### 3. Using new T()

```java
// WRONG
public class Box<T> {
    private T value = new T();  // Compile error!
}

// RIGHT
public class Box<T> {
    private final T value;
    
    public Box(T value) {
        this.value = value;
    }
}
```

---

## Pitfalls

### 1. Type Erasure Surprise

```java
Box<String> stringBox = new Box<>();
Box<Integer> intBox = new Box<>();

// These are the SAME class at runtime!
System.out.println(stringBox.getClass() == intBox.getClass()); // true

// You cannot do:
// Box<String>.class  // Compile error
// if (box instanceof Box<String>) { }  // Compile error
```

### 2. Overloading with Generic Signatures

```java
// These have the SAME erasure - compile error!
public void process(List<String> list) { }
public void process(List<Integer> list) { }
// Both erase to: process(List)
```

### 3. Static Members

```java
// WRONG
public class Box<T> {
    private static T value;  // Compile error!
    // T is per-instance, but static is per-class
}

// RIGHT
public class Box<T> {
    private static int count;  // This is fine
    private T instanceValue;   // This is fine
}
```

---

## Debugging Tips

### 1. Check Erased Types

```bash
javac -d out src/Box.java
javap -v out/Box.class | grep "Signature"
# Shows generic type info in bytecode
```

### 2. Use Reflection to Inspect Types

```java
// Check generic type info
Field field = MyClass.class.getDeclaredField("list");
Type genericType = field.getGenericType();
if (genericType instanceof ParameterizedType pt) {
    System.out.println("Raw type: " + pt.getRawType());
    System.out.println("Type args: " + Arrays.toString(pt.getActualTypeArguments()));
}
```

### 3. Read Compiler Errors

```
Error: incompatible types: Object cannot be converted to String
// This means you're trying to use a raw type
// Add proper generic parameters
```

### 4. Use IDE Type Hints

```java
Box<> box = new Box<>();  // IDE shows inferred type
// IntelliJ: View > Tool Windows > Structure
// Eclipse: Open Declaration
```

### 5. Inspect Bytecode

```bash
javap -c -p Box.class | grep -A 5 "set\|get"
# Shows erased method signatures
# 06 - Type Erasure (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

```

---

## Comparison Table

| Feature | Generic Code | After Erasure |
|---------|--------------|---------------|
| Type parameters | `<T>` | `Object` or bound |
| Method signatures | `T get()` | `Object get()` |
| Field types | `T value` | `Object value` |
| Casts | None needed | Compiler-inserted |
| Bridge methods | Not present | Added by compiler |
| instanceof | Can't use with `T` | Can use with raw type |
| Arrays | Can't create generic | Can create raw arrays |

---

## Decision Tree

```
Do you need runtime type information?
├── No → Use generics normally (erasure is fine)
└── Yes → What type of runtime info?
    ├── Class type → Use Class<T> token
    ├── Parameterized type → Use TypeToken or reflection
    ├── Array creation → Use Array.newInstance()
    └── Instance creation → Use Class<T>.newInstance()
```

---

## Interview Questions

### Q1: What is type erasure in Java?

**A:** Type erasure is the process where the compiler removes all generic type information at compile time, replacing type parameters with their bounds (or `Object`). This ensures backward compatibility but means generic types are not available at runtime.

### Q2: Why does Java use type erasure?

**A:** For backward compatibility with pre-Java 5 code. Raw types (`List`) and parameterized types (`List<String>`) produce identical bytecode, allowing old code to work with new generic code.

### Q3: What can't you do because of type erasure?

**A:** Cannot use `instanceof` with parameterized types, create generic arrays, use `new T()`, `T.class`, or overload methods with different generic signatures.

### Q4: How can you get runtime type information?

**A:** Use `Class<T>` tokens, `TypeToken<T>` pattern, or reflection APIs like `getGenericSuperclass()`, `getGenericType()`, etc.

### Q5: Do generics have runtime overhead?

**A:** No. Type erasure means generic code produces identical bytecode to raw types. The only overhead is compile-time type checking and bridge method generation, which has negligible runtime cost.

---

## Exercises

### Exercise 1: Type Erasure Demonstration

Create a program that demonstrates type erasure by:
1. Comparing `getClass()` of different generic types
2. Using reflection to inspect erased types
3. Showing bridge method generation

### Exercise 2: TypeToken Implementation

Implement a `TypeToken<T>` class that captures generic type information at runtime.

### Exercise 3: Generic Array Creation

Create a utility method to create generic arrays using reflection.

---

## Assignments

### Assignment 1: Type-Safe Reflection Utility

Create a `TypeSafeReflection` utility class that:
1. Safely gets generic type parameters
2. Creates instances of generic types
3. Accesses fields with type safety
4. Handles type erasure gracefully

### Assignment 2: Generic Builder with Runtime Type

Create a generic builder that:
1. Uses type tokens for runtime type info
2. Validates types at build time
3. Supports complex generic types
4. Handles type erasure properly

---

## Mini Project

### Type-Safe Serialization Framework

Build a serialization framework that:
1. Uses type tokens to preserve generic type information
2. Serializes/deserializes generic types correctly
3. Handles type erasure gracefully
4. Supports complex nested generic types

**Key classes:**
- `TypeToken<T>` — captures generic type info
- `TypeSafeSerializer` — serializes with type safety
- `TypeSafeDeserializer` — deserializes with type safety

---

## Summary

Type erasure is a fundamental aspect of Java generics:

1. **Compile-time feature** — Generic types erased at compile time
2. **Backward compatibility** — Allows pre-Java 5 code to work
3. **No runtime overhead** — Identical performance to raw types
4. **Limitations** — Cannot use instanceof, create arrays, etc.
5. **Workarounds** — Type tokens, reflection, class parameters

Understanding type erasure is essential for writing effective generic code and avoiding common pitfalls.

---

## References

- [Oracle - Type Erasure](https://docs.oracle.com/en/java/javase/21/java/generics/erasure.html)
- [Java Language Specification §4.6 - Type Erasure](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.6)
- [Effective Java - Item 33: Use tokens to pass class literals at runtime](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Angelika Langer - Type Erasure FAQ](https://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeErasure.html)
```
