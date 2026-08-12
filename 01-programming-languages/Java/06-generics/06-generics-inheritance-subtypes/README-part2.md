    public Comparable getValue() {
        return value;
    }
    
    public int compareTo(Container other) {
        return value.compareTo(other.value);
    }
    
    // Bridge method
    public int compareTo(Object other) {
        return compareTo((Container) other);
    }
}
```

### Wildcard Erasure

```java
// Upper bounded wildcard
List<? extends Number> list = List.of(1, 2, 3);
// After erasure: List (elements accessed as Number)

// Lower bounded wildcard
List<? super Integer> list = new ArrayList<>();
// After erasure: List (elements written as Object)

// Unbounded wildcard
List<?> list = List.of("a", "b");
// After erasure: List (elements accessed as Object)
```

---

## JVM Perspective

### Bytecode Structure

```java
Box<String> box = new Box<>();
box.set("hello");
String s = box.get();
```

**Bytecode (simplified):**
```
new Box
dup
invokespecial Box.<init>:()V
astore_1           // box = new Box()

aload_1
ldc "hello"
invokevirtual Box.set:(Ljava/lang/Object;)V  // set(Object), not set(String)

aload_1
invokevirtual Box.get:()Ljava/lang/Object;   // get() returns Object
checkcast java/lang/String                   // Cast to String
astore_2           // s = (String) box.get()
```

### Type Information in Bytecode

```bash
# Generic type info preserved in Signature attribute
javap -v Box.class | grep -A 3 "Signature"
# Signature: LBox<Ljava/lang/String;>;
# This is for tools (IDE, reflection), not JVM runtime
```

### Reflection and Type Erasure

```java
Box<String> box = new Box<>();

// Runtime type
Class<?> clazz = box.getClass();
System.out.println(clazz.getName());  // "Box" (not "Box<String>")

// Generic type info (if preserved in bytecode)
Type superclass = clazz.getGenericSuperclass();
if (superclass instanceof ParameterizedType pt) {
    Type[] typeArgs = pt.getActualTypeArguments();
    System.out.println(typeArgs[0]);  // "class java.lang.String"
}
```

---

## Memory Representation

### Generic Objects

```java
Box<String> stringBox = new Box<>();
Box<Integer> intBox = new Box<>();
Box<List<String>> listBox = new Box<>();
```

**Memory layout (all identical):**
```
┌─────────────────────────────┐
│ Object header (16 bytes)    │
├─────────────────────────────┤
│ value: Object reference     │
└─────────────────────────────┘

Total: 24 bytes (on 64-bit JVM with compressed oops)
```

### Array Erasure

```java
// Generic arrays are problematic
// String[] strings = new String[10];  // OK
// Box<String>[] boxes = new Box<String>[10];  // Compile error!

// Why? Arrays carry reified type info
// At runtime: strings knows it's String[]
// But Box<String>[] would need to be Box[] (erased)
// These are incompatible
```

---

## Syntax

### Type Erasure in Practice

```java
// Source
public class Box<T> {
    private T value;
    public T get() { return value; }
    public void set(T v) { value = v; }
}

// Bytecode equivalent
public class Box {
    private Object value;
    public Object get() { return value; }
    public void set(Object v) { value = v; }
    
    // Bridge method (for subclasses)
    public void set(String v) { set((Object) v); }
}
```

### Cast Insertion

```java

---

[📖 Continue to Part 2](README-part2.md)
 | [📖 Continue to Part 3](README-part3.md)
```
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
