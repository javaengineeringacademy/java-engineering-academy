# Wrapper Classes Complete Guide

## Overview

Java provides 8 wrapper classes that create object representations of primitive types:

| Primitive | Wrapper | Size | Range |
|-----------|---------|------|-------|
| byte | Byte | 8 bits | -128 to 127 |
| short | Short | 16 bits | -32,768 to 32,767 |
| int | Integer | 32 bits | -2^31 to 2^31-1 |
| long | Long | 64 bits | -2^63 to 2^63-1 |
| float | Float | 32 bits | ~6-7 decimal digits |
| double | Double | 64 bits | ~15-16 decimal digits |
| boolean | Boolean | 1 bit | true or false |
| char | Character | 16 bits | 0 to 65,535 |

## Why Wrapper Classes?

1. **Collections**: Collections can only store objects, not primitives
2. **Null safety**: Wrappers can be null, primitives cannot
3. **Utility methods**: Parsing, conversion, constants
4. **Generics**: Generic types require object types

## Boxing and Unboxing

### Autoboxing (Primitive → Wrapper)
```java
Integer a = 42;          // Implicit: Integer.valueOf(42)
Long b = 100L;           // Implicit: Long.valueOf(100L)
Double c = 3.14;         // Implicit: Double.valueOf(3.14)
```

### Unboxing (Wrapper → Primitive)
```java
Integer wrapper = 100;
int primitive = wrapper; // Implicit: wrapper.intValue()
```

### Explicit Conversion
```java
Integer explicit = Integer.valueOf(42);  // Explicit boxing
int explicitPrim = explicit.intValue();  // Explicit unboxing
```

## Integer Cache (-128 to 127)

Java caches Integer objects for values -128 to 127:

```java
Integer a = 127;
Integer b = 127;
System.out.println(a == b);      // true (same cached object)
System.out.println(a.equals(b)); // true

Integer c = 128;
Integer d = 128;
System.out.println(c == d);      // false (different objects!)
System.out.println(c.equals(d)); // true
```

**Why?** To optimize performance for small, frequently used values.

## ValueOf() vs Constructor

### Use valueOf() (Recommended)
```java
Integer a = Integer.valueOf(100);  // Uses cache
Integer b = Integer.valueOf(100);  // Same cached object
```

### Avoid Constructor (Deprecated in Java 9+)
```java
Integer c = new Integer(100);  // Always creates new object
Integer d = new Integer(100);  // Different object
```

**Why?** valueOf() uses cache, constructor doesn't.

## parseInt() vs valueOf()

```java
// parseInt returns primitive
int parsed = Integer.parseInt("123");

// valueOf returns wrapper
Integer wrapper = Integer.valueOf("123");
```

**Use parseInt()** when you need primitive.
**Use valueOf()** when you need wrapper (benefits from cache).

## Common Pitfalls

### 1. == Comparison (Reference Equality)
```java
Integer a = 200;
Integer b = 200;
System.out.println(a == b);      // false!
System.out.println(a.equals(b)); // true
```
**Always use .equals() for value comparison.**

### 2. NullPointerException with Unboxing
```java
Integer nullInt = null;
int value = nullInt; // NPE!
```

### 3. Unexpected Null in Arithmetic
```java
Integer a = null;
Integer b = 10;
int result = a + b; // NPE due to unboxing
```

### 4. Cache Range Confusion
```java
Integer p = 128; // Not cached
Integer q = 128; // Different object
System.out.println(p == q); // false
```

## Performance Implications

- **Primitives are faster**: No object overhead
- **Wrappers have overhead**: Boxing/unboxing cost
- **Cache helps**: Small values are reused
- **Use primitives** for performance-critical code
- **Use wrappers** for collections and null safety

## Useful Methods

### Integer Methods
```java
Integer.toHexString(255)     // "ff"
Integer.toBinaryString(10)   // "1010"
Integer.compare(10, 20)      // -1 (10 < 20)
Integer.max(10, 20)          // 20
Integer.min(10, 20)          // 10
```

### Character Methods
```java
Character.isDigit('5')       // true
Character.isLetter('A')      // true
Character.toUpperCase('a')   // 'A'
Character.toLowerCase('A')   // 'a'
Character.isWhitespace(' ')  // true
```

### Double Methods
```java
Double.isNaN(0.0/0.0)        // true
Double.isInfinite(1.0/0.0)   // true
Double.parseDouble("3.14")   // 3.14
```

## When to Use Wrapper vs Primitive

| Use Case | Type | Reason |
|----------|------|--------|
| Collections | Wrapper | Generics require objects |
| Null fields | Wrapper | Primitives can't be null |
| Method parameters | Primitive | Avoid boxing overhead |
| Local variables | Primitive | Better performance |
| Return types | Primitive | Unless null is meaningful |
| Constants | Primitive | Final fields |

## Common Interview Questions

1. **What are wrapper classes?**
   Object representations of primitive types.

2. **Why do we need wrapper classes?**
   Collections, null safety, utility methods, generics.

3. **What is autoboxing?**
   Automatic conversion from primitive to wrapper.

4. **What is the Integer cache?**
   Cache for values -128 to 127 to reuse objects.

5. **Why use valueOf() over constructor?**
   valueOf() uses cache, constructor doesn't.

6. **What's the difference between == and .equals() for wrappers?**
   == compares references, .equals() compares values.

7. **Why does Integer i = null; int j = i; throw NPE?**
   Unboxing calls intValue() on null reference.

8. **When should you use primitives over wrappers?**
   Performance-critical code, loops, calculations.

## Key Takeaways

1. 8 wrapper classes for 8 primitive types
2. Autoboxing/unboxing converts between them
3. Integer cache (-128 to 127) reuses objects
4. Use .equals() not == for value comparison
5. Watch for NullPointerException with unboxing
6. Primitives are faster; use wrappers for collections
7. Use valueOf() not constructors for caching
8. parseInt() returns primitive, valueOf() returns wrapper
