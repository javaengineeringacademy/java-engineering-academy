# Wrapper Classes Internals

## Autoboxing Internals

### How Autoboxing Works

```java
Integer boxed = 42;  // Autoboxing: Integer.valueOf(42)

// Bytecode:
// bipush 42      // Push primitive value
// invokestatic Integer.valueOf(int)  // Box to Integer object
// astore_1       // Store reference
```

### Value Cache

Java caches wrapper objects for frequently used values:

```java
Integer a = 127;
Integer b = 127;
System.out.println(a == b);  // true (cached)

Integer c = 128;
Integer d = 128;
System.out.println(c == d);  // false (new objects)
```

Cache ranges:
- `Boolean`: All (TRUE, FALSE)
- `Byte`: All (-128 to 127)
- `Short`: -128 to 127
- `Integer`: -128 to 127
- `Long`: -128 to 127
- `Character`: 0 to 127

### Boxing Context

```java
// Boxing occurs in these contexts:
Integer x = 42;                    // Assignment
method(Integer parameter);         // Method call
return 42;                         // Return statement
Integer y = 10 + 20;              // Binary operation
```

### Unboxing Context

```java
// Unboxing occurs in these contexts:
int x = boxedInteger;              // Assignment
method(int parameter);             // Method call
return boxedInteger;               // Return statement
int y = boxedInteger + 10;        // Binary operation
```

### NullPointerException Risk

```java
Integer nullValue = null;
int primitive = nullValue;  // NPE: auto-unboxing null

// Safe approach
Integer value = getValue();
if (value != null) {
    int primitive = value;
}
```

### Constructor vs valueOf

```java
// PREFER: valueOf (uses cache)
Integer a = Integer.valueOf(42);

// AVOID: constructor (creates new object)
Integer b = new Integer(42);  // Deprecated in Java 9+
```

### Wrapper Arithmetic

```java
Integer a = 10;
Integer b = 20;
int sum = a + b;  // Unbox, add, store in primitive

// This creates:
// 1. Unbox a to int
// 2. Unbox b to int
// 3. Add ints
// 4. Store result in primitive
```
