
```java
// Source
public class NumberBox<T extends Number> {
    private T value;
    public double doubleValue() { return value.doubleValue(); }
}

// After erasure
public class NumberBox {
    private Number value;  // T → Number (bounded)
    
    public double doubleValue() { return value.doubleValue(); }
}
```

---

## JVM Perspective

### Class File Structure

```
Box.class (after compilation):
├── This class: Box
├── Super class: java.lang.Object
├── Constant pool
│   ├── Type parameters: erased
│   └── Method signatures: raw
├── Fields
│   └── value: Object (not String)
├── Methods
│   ├── get(): Object
│   ├── set(Object): void
│   └── bridge set(String): void
└── Source file: Box.java
```

### Type Information in Bytecode

```bash
# Generic type info is stored in Signature attribute
javap -v Box.class | grep -A 5 "Signature"
# Signature: LBox<Ljava/lang/String;>;  ← Still in bytecode!
# But JVM doesn't use it for type checking
```

### Reflection and Type Tokens

```java
Box<String> box = new Box<>();

// Runtime type information
Class<?> clazz = box.getClass();
// clazz.getName() = "Box" (not "Box<String>")

// To get generic type info, use TypeToken pattern
Type superclass = clazz.getGenericSuperclass();
ParameterizedType paramType = (ParameterizedType) superclass;
Type[] typeArgs = paramType.getActualTypeArguments();
// typeArgs[0] = String.class (if available in bytecode)
```

---

## Memory Representation

### Generic vs Non-Generic Objects

```java
Box<String> stringBox = new Box<>();
Box<Integer> intBox = new Box<>();
Box<List<String>> listBox = new Box<>();
```

**Memory layout:**
```
All Box objects have IDENTICAL layout:
┌─────────────────────────┐
│ Object header (16 bytes)│
│ value: Object reference │
└─────────────────────────┘

The type parameter doesn't affect:
- Object size
- Field types
- Method signatures
- Memory alignment
```

### Type Information Storage

```
Class metadata (in Metaspace):
┌─────────────────────────────────────┐
│ Box.class                           │
├─────────────────────────────────────┤
│ Signature: LBox<Ljava/lang/String;> │ ← Generic info preserved
│ RuntimeVisibleAnnotations            │
│ SourceFile: Box.java                 │
└─────────────────────────────────────┘
```

---

## Syntax

### Basic Generic Class

```java
public class ClassName<T> {
    private T field;
    
    public ClassName(T field) {
        this.field = field;
    }
    
    public T getField() {
        return field;
    }
    
    public void setField(T field) {
        this.field = field;
    }
}
```

### Multiple Type Parameters

```java
public class ClassName<K, V> {
    private K key;
    private V value;
    
    public ClassName(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() { return key; }
    public V getValue() { return value; }
}
```

### Generic Class with Bounds

```java
public class ClassName<T extends Comparable<T>> {
    private T value;
    

---

[📖 Continue to Part 2](README-part2.md)
 | [📖 Continue to Part 3](README-part3.md)
```
# 02 - Generic Classes (Part 2)

[📖 Back to Part 1](README.md)
 | [📖 Continue to Part 3](README-part3.md)

---

    public int compareTo(ClassName<T> other) {
        return this.value.compareTo(other.value);
    }
}

// Multiple bounds
public class ClassName<T extends Number & Comparable<T>> {
    private T value;
    
    public boolean isGreaterThan(T other) {
        return value.compareTo(other) > 0;
    }
}
```

### Generic Interface Implementation

```java
// Generic interface
public interface Pair<A, B> {
    A getFirst();
    B getSecond();
}

// Concrete implementation
public class ImmutablePair<A, B> implements Pair<A, B> {
    private final A first;
    private final B second;
    
    public ImmutablePair(A first, B second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public A getFirst() { return first; }
    
    @Override
    public B getSecond() { return second; }
}

// Specialized implementation
public class StringIntegerPair implements Pair<String, Integer> {
    // Type parameters fixed to String and Integer
}
```

---

## Easy Example

### Generic Box

```java
public class Box<T> {
    private T content;

    public Box() {
    }

    public Box(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Box[" + content + "]";
    }

    public static void main(String[] args) {
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);
        
        System.out.println(stringBox);  // Box[Hello]
        System.out.println(intBox);     // Box[42]
        
        stringBox.setContent("World");
        System.out.println(stringBox);  // Box[World]
    }
