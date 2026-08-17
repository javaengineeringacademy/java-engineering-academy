# Control Flow Memory Model

## Stack Frame Execution

Each loop iteration and conditional branch executes within the current stack frame.

### For Loop Memory

```java
for (int i = 0; i < n; i++) {
    int temp = i * 2;
    process(temp);
}

// Memory layout:
// Stack frame contains: i (4 bytes), temp (4 bytes)
// No heap allocation per iteration
```

### While Loop Memory

```java
while (condition) {
    int result = compute();
    // result lives on stack
}

// Same stack frame reused for each iteration
// Local variables persist until method returns
```

### Switch Statement Memory

```java
switch (value) {
    case 1 -> processOne();
    case 2 -> processTwo();
}

// Jump table: O(1) lookup, minimal memory
// No object creation for dispatch
```

### Break and Continue Memory

```java
for (int i = 0; i < 100; i++) {
    if (i == 50) break;    // Just changes instruction pointer
    if (i == 25) continue; // Just changes instruction pointer
}

// No extra memory for break/continue
// Just modifies the program counter
```

### Nested Loop Memory

```java
for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
        int product = i * j;  // Lives on stack
        // Both i and j are on the same stack frame
    }
}

// Memory: O(1) regardless of n and m
// Only one stack frame for the entire nested loop
```

### String Comparison in Conditions

```java
// Each comparison creates no new objects
if (str.equals("target")) { ... }

// But this creates a new String object
if (str == "target") { ... } // Reference comparison, no allocation
```

### Collection Iteration Memory

```java
// For-each creates an Iterator object (heap allocation)
for (String s : list) {
    process(s);
}

// Traditional for loop: no Iterator allocation
for (int i = 0; i < list.size(); i++) {
    process(list.get(i));
}
```
