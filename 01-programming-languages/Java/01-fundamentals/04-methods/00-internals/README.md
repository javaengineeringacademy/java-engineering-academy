# Methods Internals

## How Java Executes Methods

### Method Call Bytecode

```java
public int add(int a, int b) {
    return a + b;
}

// Called as:
int result = add(3, 5);

// Bytecode:
iload_1      // push parameter a
iload_2      // push parameter b
iadd         // add them
ireturn      // return result
```

### Stack Frame Layout

Each method call creates a stack frame containing:
- **Local variables:** Method parameters and local variables
- **Operand stack:** Temporary computation space
- **Return address:** Where to continue after method returns

```
Method Stack Frame:
┌─────────────────────────────────┐
│ Parameters (a, b)               │ ← Copied from caller
│ Local variables                 │ ← Declared in method
│ Operand stack                   │ ← Temporary computations
│ Return address                  │ ← Caller's next instruction
└─────────────────────────────────┘
```

### Static vs Instance Methods

**Static method:** No `this` reference
```java
public static int add(int a, int b) {
    return a + b;
}
// Bytecode: No aload_0 (no this parameter)
```

**Instance method:** Implicit `this` parameter
```java
public int add(int b) {
    return this.value + b;
}
// Bytecode: aload_0 pushes this, then loads field
```

### Method Overloading Resolution

Java resolves overloaded methods at compile time:
1. Find methods with matching name
2. Match parameter types (exact match first)
3. Apply widening conversions if needed
4. Use varargs as last resort
5. Throw compilation error if ambiguous

### Recursion Internals

```java
int factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);
}

// Each call adds a stack frame:
// factorial(5) → stack depth 5
// factorial(4) → stack depth 4
// ...
// factorial(1) → stack depth 1 (base case)
```

### Tail Call Optimization

Java does NOT perform tail call optimization. Each recursive call consumes stack space:

```java
// NOT optimized — stack overflow risk
void recurse(int n) {
    if (n == 0) return;
    recurse(n - 1);
}

// Convert to iteration for large n
void iterate(int n) {
    while (n > 0) n--;
}
```
