# For Loop — The Index King

## Why For Loop Exists

The `for` loop is the oldest and most explicit iteration mechanism in Java. It gives you **direct control over the index**, making it indispensable when you need to know *where* you are in a collection — not just *what* you're visiting.

**Production incident:** A payment gateway used enhanced for-loop to skip every 10th transaction for batch processing. The logic was wrong — they needed the index to calculate batch boundaries. Off-by-one caused $50K in duplicate charges.

## The Pain Point

Other iteration mechanisms hide the index. When you need to:
- Process every Nth element
- Access element at position `i` AND `i+1`
- Skip elements based on position
- Reverse iterate

...you need the index. The `for` loop is your only clean option.

## Basic Syntax

```java
// Standard for loop
for (initialization; condition; update) {
    // body
}

// Classic: iterate array with index
for (int i = 0; i < array.length; i++) {
    System.out.println(array[i]);
}

// Classic: iterate List with index
for (int i = 0; i < list.size(); i++) {
    System.out.println(list.get(i));
}

// Reverse iteration
for (int i = list.size() - 1; i >= 0; i--) {
    System.out.println(list.get(i));
}

// Step by 2
for (int i = 0; i < array.length; i += 2) {
    System.out.println(array[i]);
}
```

## Loop Variable Scope

```java
// Variable is scoped to the loop
for (int i = 0; i < 10; i++) {
    // i is accessible here
}
// i is NOT accessible here — compile error

// Multiple variables
for (int i = 0, j = 10; i < j; i++, j--) {
    System.out.println(i + ", " + j);
}

// Common mistake: reusing variable outside loop
for (int i = 0; i < 10; i++) {
    if (found) break;
}
// CANNOT use i here to check final value
// Workaround: declare i outside, or use a flag
```

## Break and Continue

```java
// break: exit loop entirely
for (int i = 0; i < 100; i++) {
    if (data[i] == target) {
        foundIndex = i;
        break;  // Stop searching
    }
}

// continue: skip to next iteration
for (int i = 0; i < list.size(); i++) {
    if (list.get(i) == null) continue;  // Skip nulls
    process(list.get(i));
}

// Labeled break: exit nested loops
outer:
for (int i = 0; i < 10; i++) {
    for (int j = 0; j < 10; j++) {
        if (matrix[i][j] == target) {
            break outer;  // Exits BOTH loops
        }
    }
}
```

## Nested Loops

```java
// 2D array traversal
for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix[i].length; j++) {
        System.out.print(matrix[i][j] + " ");
    }
    System.out.println();
}

// Triangular loop (common pattern)
for (int i = 0; i < n; i++) {
    for (int j = 0; j <= i; j++) {
        // Do something i+1 times
    }
}

// Anti-pattern: unnecessary nesting
// BAD: O(n²) when O(n) suffices
for (int i = 0; i < list.size(); i++) {
    for (int j = 0; j < list.size(); j++) {
        if (i != j && list.get(i).equals(list.get(j))) {
            // Found duplicate
        }
    }
}
```

## Loop with Index Access

```java
// Compare adjacent elements
for (int i = 1; i < list.size(); i++) {
    if (list.get(i) < list.get(i - 1)) {
        return false;  // Not sorted
    }
}

// Calculate running average
double sum = 0;
for (int i = 0; i < values.length; i++) {
    sum += values[i];
    double avg = sum / (i + 1);
    System.out.println("Running avg: " + avg);
}

// Shift elements (index mutation)
for (int i = list.size() - 1; i > 0; i--) {
    list.set(i, list.get(i - 1));
}
list.set(0, newValue);
```

## Performance: O(n) Random Access

```
Operation          │ ArrayList    │ LinkedList
───────────────────┼──────────────┼──────────────
get(i)             │ O(1)         │ O(n)
for loop total     │ O(n)         │ O(n²)
for-each total     │ O(n)         │ O(n)
```

**Critical insight:** For `LinkedList`, `for` with `list.get(i)` is O(n²) because `get(i)` is O(n). Use enhanced for or Iterator for LinkedList.

```java
// ArrayList: for loop is fine
ArrayList<Integer> al = new ArrayList<>();
for (int i = 0; i < al.size(); i++) {
    process(al.get(i));  // O(1) per access
}
// Total: O(n)

// LinkedList: for loop is TERRIBLE
LinkedList<Integer> ll = new LinkedList<>();
for (int i = 0; i < ll.size(); i++) {
    process(ll.get(i));  // O(n) per access — walks from head each time!
}
// Total: O(n²)
```

## When to Use / When NOT to Use

### ✅ USE For Loop When:
- You need the index value
- You need reverse iteration
- You need to skip elements by position (every Nth)
- Processing adjacent pairs
- Array iteration (especially multi-dimensional)
- You need to modify the collection by index

### ❌ DON'T Use For Loop When:
- You only need the values (use enhanced for)
- Working with LinkedList (use Iterator or enhanced for)
- You want functional style (use Stream)
- ConcurrentModificationException is a concern (use Iterator)

## Common Mistakes

### Mistake 1: Off-by-One Error
```java
// WRONG: processes one too many or too few
for (int i = 0; i <= list.size(); i++) {  // IndexOutOfBoundsException!
    process(list.get(i));
}

// RIGHT:
for (int i = 0; i < list.size(); i++) {
    process(list.get(i));
}
```

### Mistake 2: Modifying Collection While Iterating
```java
// WRONG: ConcurrentModificationException
for (int i = 0; i < list.size(); i++) {
    if (list.get(i).shouldRemove()) {
        list.remove(i);  // Shifts indices! Next element skipped
        i--;  // Hack to compensate
    }
}

// RIGHT: Use Iterator or backwards for loop
for (int i = list.size() - 1; i >= 0; i--) {
    if (list.get(i).shouldRemove()) {
        list.remove(i);  // Safe: going backwards
    }
}
```

### Mistake 3: Variable Scope Confusion
```java
// WRONG: can't use i after loop
for (int i = 0; i < 10; i++) {
    if (found) break;
}
System.out.println("Found at: " + i);  // Compile error!

// RIGHT: declare outside
int i;
for (i = 0; i < 10; i++) {
    if (found) break;
}
System.out.println("Found at: " + i);
```

### Mistake 4: Size() Called Every Iteration
```java
// ACCEPTABLE but slightly slower
for (int i = 0; i < list.size(); i++) { ... }

// OPTIMAL: cache size
int size = list.size();
for (int i = 0; i < size; i++) { ... }

// For concurrent collections, ALWAYS cache size
```

## Interview Questions

**Q: What's the time complexity of iterating an ArrayList with a for loop?**
A: O(n) total, O(1) per element for `get(i)`.

**Q: Why is for loop bad for LinkedList?**
A: `get(i)` is O(n) for LinkedList, making total O(n²).

**Q: Can you use for loop to iterate a HashSet?**
A: Technically yes with `toArray()`, but you lose the purpose. Use enhanced for.

**Q: How do you safely remove elements during for loop iteration?**
A: Iterate backwards: `for (int i = list.size()-1; i >= 0; i--)`.

**Q: What happens if collection size changes during for loop?**
A: Undefined behavior — may skip elements, process duplicates, or throw IndexOutOfBoundsException.
