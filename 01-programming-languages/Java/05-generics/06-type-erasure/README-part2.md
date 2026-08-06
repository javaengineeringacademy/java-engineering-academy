# 06 - Type Erasure (Part 2)

[📖 Back to Part 1](README.md)
 | [📖 Continue to Part 3](README-part3.md)

---

// Source
Box<String> box = new Box<>();
String s = box.get();

// Bytecode equivalent
Box box = new Box<>();
String s = (String) box.get();
```

### Bridge Methods

```java
// Source
public class StringBox extends Box<String> {
    @Override
    public void set(String value) { super.set(value); }
}

// Bytecode equivalent
public class StringBox extends Box {
    @Override
    public void set(String value) { super.set(value); }
    
    // Bridge method
    public void set(Object value) {
        set((String) value);
    }
}
```

---

## Easy Example

### Basic Type Erasure Demonstration

```java
import java.util.ArrayList;
import java.util.List;

public class TypeErasureBasic {
    
    public static void main(String[] args) {
        // Different generic types are same at runtime
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        
        System.out.println(strings.getClass() == integers.getClass());  // true
        System.out.println(strings.getClass().getName());  // java.util.ArrayList
        
        // Boxing/unboxing happens at compile time
        List<Integer> nums = new ArrayList<>();
        nums.add(42);  // Autoboxing: int → Integer
        int value = nums.get(0);  // Auto-unboxing: Integer → int
        
        // Type erasure in method signatures
        System.out.println("String list class: " + strings.getClass());
        System.out.println("Integer list class: " + integers.getClass());
    }
}
```

---

## Medium Example

### Type Erasure Implications

```java
import java.util.ArrayList;
import java.util.List;

public class TypeErasureImplications {
    
    // Cannot use instanceof with parameterized types
    public static void checkType(Object obj) {
        // WRONG: if (obj instanceof List<String>) { }
        
        // RIGHT: check raw type
        if (obj instanceof List<?>) {
            List<?> list = (List<?>) obj;
            System.out.println("It's a List of: " + 
                list.getClass().getTypeParameters()[0].getName());
        }
    }
    
    // Cannot create generic arrays
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
