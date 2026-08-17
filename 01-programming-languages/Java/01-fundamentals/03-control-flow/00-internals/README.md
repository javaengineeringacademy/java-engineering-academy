# Control Flow Internals

## How Java Executes Control Flow

### If-Else Bytecode

```java
if (x > 0) {
    positive();
} else {
    nonPositive();
}

// Bytecode:
iload_1          // push x
ifle L1          // if x <= 0, goto L1
invokestatic positive()   // call positive()
goto L2          // skip else block
L1: invokestatic nonPositive()  // call nonPositive()
```

### Switch Internals

Java optimizes switch statements based on the value type:

**Dense values (int, char):** Jump table
```
// switch(x) { case 0: ... case 1: ... case 2: ... }
// Bytecode: Creates jump table for O(1) dispatch
tableswitch { 0: L0, 1: L1, 2: L2 }
```

**Sparse values or String:** Lookup switch
```
// switch(x) { case 100: ... case 200: ... case 300: ... }
// Bytecode: Binary search for O(log n) dispatch
lookupswitch { 100: L0, 200: L1, 300: L2 }
```

**Enum:** Ordinal mapping (fastest)
```
// switch(status) { case ACTIVE: ... case INACTIVE: ... }
// Bytecode: Uses enum ordinal directly as jump table index
```

### Loop Internals

**For Loop:**
```java
for (int i = 0; i < n; i++) {
    body();
}

// Bytecode:
iconst_0         // i = 0
istore_1
goto L1          // goto condition check
L0: body()       // loop body
iinc 1, 1        // i++
L1: iload_1      // push i
iload_2          // push n
if_icmplt L0    // if i < n, goto L0
```

**While Loop:**
```java
while (condition) {
    body();
}

// Bytecode:
goto L1          // goto condition check
L0: body()       // loop body
L1: condition()  // evaluate condition
ifne L0          // if true, goto L0
```

### Break and Continue

**Break:** Jumps to the instruction after the loop/switch
**Continue:** Jumps back to the loop condition (while) or increment (for)

```java
for (int i = 0; i < 10; i++) {
    if (i == 5) break;    // Exit loop entirely
    if (i == 3) continue; // Skip to i++
}
```

### Tail Call Optimization

Java does NOT optimize tail recursion. Recursive methods always consume stack space:

```java
// NOT optimized — each call adds a stack frame
void recursive(int n) {
    if (n == 0) return;
    recursive(n - 1);
}

// Convert to iteration for performance
void iterative(int n) {
    while (n > 0) n--;
}
```
