# Operators Memory Model

## Stack-Based Execution

Java operators work on values stored on the stack. When an expression is evaluated, operands are pushed onto the operand stack, the operator is applied, and the result is pushed back.

### Arithmetic Operations in Bytecode

```
int a = 10;
int b = 3;
int c = a + b;

// Bytecode:
bipush 10    // push 10 onto stack
istore_1     // store in variable 1 (a)
bipush 3     // push 3 onto stack
istore_2     // store in variable 2 (b)
iload_1      // push a onto stack
iload_2      // push b onto stack
iadd         // pop both, add, push result
istore_3     // store in variable 3 (c)
```

### Memory Usage for Operator Results

| Operation | Input Size | Result Size | Notes |
|-----------|-----------|-------------|-------|
| int + int | 4 + 4 bytes | 4 bytes | Result is int |
| int * int | 4 + 4 bytes | 4 bytes | Can overflow silently |
| double + double | 8 + 8 bytes | 8 bytes | Precision issues |
| long + long | 8 + 8 bytes | 8 bytes | Use for large values |

### Autoboxing and Operator Performance

```java
// Primitive operations: direct stack operations
int a = 10;
int b = 20;
int c = a + b;  // Single iadd instruction

// Boxed operations: object creation overhead
Integer a2 = 10;
Integer b2 = 20;
Integer c2 = a2 + b2;  // Unbox, add, rebox (creates new Integer object)
```

### Bitwise Operations Memory

Bitwise operations are performed directly on the binary representation:

```
AND: 1010 & 1100 = 1000
OR:  1010 | 1100 = 1110
XOR: 1010 ^ 1100 = 0110
NOT: ~1010 = ...0101 (inverted bits)
```

These are single-cycle CPU instructions with no memory allocation overhead.

### Comparison Operations

Comparisons return boolean values (1 byte on stack):

```java
int x = 5;
boolean result = x > 3;  // Push 5, push 3, compare, push boolean (1 byte)
```

### String Concatenation Memory

```java
String s = "a" + "b" + "c";

// Java compiler optimizes this to:
String s = "abc";  // Single string pool entry

// But this creates intermediate objects:
String s2 = a + b + c;  // StringBuilder created internally
```
