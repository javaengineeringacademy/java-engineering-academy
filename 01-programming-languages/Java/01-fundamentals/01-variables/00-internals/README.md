# Variables and Types Internals

## How Java Stores Variables

### Stack Memory
Local variables (method parameters and local declarations) are stored on the stack. Each method invocation gets its own stack frame.

```java
public void calculate() {
    int x = 10;          // Stack: 4 bytes for int
    double y = 3.14;     // Stack: 8 bytes for double
    boolean flag = true; // Stack: 1 byte (conceptually)
}
```

### Heap Memory
Objects and their instance variables live on the heap. The stack holds references (memory addresses) to heap objects.

```java
public void createObject() {
    String name = "Java";           // Stack: reference (8 bytes on 64-bit)
    int[] arr = {1, 2, 3};         // Stack: reference, Heap: array data
    Person p = new Person();       // Stack: reference, Heap: Person object
}
```

## Primitive Type Representations

### Two's Complement for Integers
Signed integers use two's complement representation:

| Value | byte | short | int |
|-------|------|-------|-----|
| 0 | 00000000 | 00000000 00000000 | 00000000 00000000 00000000 00000000 |
| 1 | 00000001 | 00000000 00000001 | 00000000 00000000 00000000 00000001 |
| -1 | 11111111 | 11111111 11111111 | 11111111 11111111 11111111 11111111 |
| 127 | 01111111 | 00000000 01111111 | 00000000 00000000 00000000 01111111 |
| -128 | 10000000 | 11111111 10000000 | 11111111 11111111 11111111 10000000 |

### IEEE 754 Floating Point
- **float**: 1 sign bit + 8 exponent bits + 23 mantissa bits
- **double**: 1 sign bit + 11 exponent bits + 52 mantissa bits

This is why `0.1 + 0.2 != 0.3` — decimal fractions cannot be exactly represented in binary.

## String Pool Internals

String literals are interned in the String Pool (part of the heap):

```java
String s1 = "Hello";  // Created in pool
String s2 = "Hello";  // Reuses existing pool entry
String s3 = new String("Hello");  // New object on heap (not pooled)
String s4 = s3.intern();  // Returns pool reference
```

Memory layout:
```
String Pool:          Heap:
"Hello" ← s1, s2      s3 → [String object] → "Hello" (heap copy)
```

## Type Casting Under the Hood

### Widening (No-op for most cases)
- int → long: Zero-extends to 64 bits
- int → float: May lose precision (float has fewer mantissa bits)
- long → double: May lose precision

### Narrowing (Truncation)
- double → int: Truncates toward zero
- long → int: Keeps lower 32 bits, discards upper
- int → byte: Keeps lower 8 bits

```java
int big = 130;          // 10000010 in binary
byte small = (byte) big; // 10000010 as signed byte = -126
```

## Memory Alignment and Padding

JVMs align objects to memory boundaries for performance:
- Objects are typically aligned to 8-byte boundaries
- Padding bytes are added to meet alignment requirements
- This wastes some memory but improves cache performance
