# Methods Memory Model

## Stack Frame Memory Layout

Each method invocation creates a stack frame with specific memory allocation.

### Stack Frame Components

```
Method Stack Frame Memory:
┌─────────────────────────────────┐
│ Parameters (copied values)      │ ← int: 4 bytes, long: 8 bytes
│ Local variables                 │ ← Declared in method body
│ Operand stack                   │ ← Temporary computations
│ Frame data                      │ ← Return address, dynamic linking
└─────────────────────────────────┘
```

### Parameter Passing Memory

**Pass by value (primitives):**
```java
void modify(int x) {
    x = 100;  // Only modifies local copy
}
// Caller's value unchanged
```

**Pass by value (references):**
```java
void modify(StringBuilder sb) {
    sb.append(" modified");  // Modifies the object on heap
}
// Caller sees the change (same object reference)
```

### Method Return Memory

```java
int compute() {
    int result = 42;
    return result;  // Value copied to caller's stack frame
}
```

### Recursion Memory

```java
int factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);
}

// factorial(5) stack frames:
// Frame 5: n=5, waiting for factorial(4)
// Frame 4: n=4, waiting for factorial(3)
// Frame 3: n=3, waiting for factorial(2)
// Frame 2: n=2, waiting for factorial(1)
// Frame 1: n=1, returns 1

// Total memory: 5 × stack frame size
```

### Varargs Memory

```java
void process(String... values) {
    // values is actually a String[] on the heap
    for (String v : values) { ... }
}

process("a", "b", "c");
// Creates: String[] array on heap with 3 elements
```

### Lambda/Method Reference Memory

```java
Runnable r = () -> System.out.println("hello");
// Creates: Anonymous class object on heap

Function<Integer, Integer> square = x -> x * x;
// Creates: Lambda object on heap (captured variables if any)
```
