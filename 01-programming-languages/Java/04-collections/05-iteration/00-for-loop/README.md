# For Loop

## 1. Scope

This folder covers the `for` loop for indexed iteration over collections and arrays in Java.
Examples and exercises demonstrate forward, backward, skip, break, and continue patterns.

## 2. Why It Exists

Before the `for` loop, developers used `while` loops with manual counter management:

```java
int i = 0;
while (i < list.size()) {
    System.out.println(list.get(i));
    i++; // Easy to forget
}
```

Problems with this approach:
- Counter variable leaks into surrounding scope
- Easy to forget incrementing the counter (infinite loop)
- No single construct to express initialization, condition, and update

The `for` loop bundles all three parts into one line, making indexed iteration safer and more readable.

## 3. What It Is

The `for` loop is a control flow statement that executes a block of code a fixed or calculated number of times. It is the most common iteration construct in Java for indexed access.

Syntax:
```
for (initialization; condition; update) {
    // body
}
```

## 4. Internal Working

```
        ┌──────────────────┐
        │  initialization  │  ← runs once
        └────────┬─────────┘
                 ▼
        ┌──────────────────┐
        │    condition     │──── false ──── exit loop
        └────────┬─────────┘
                 │ true
                 ▼
        ┌──────────────────┐
        │      body        │
        └────────┬─────────┘
                 ▼
        ┌──────────────────┐
        │      update      │
        └────────┬─────────┘
                 │
                 └──────► (back to condition)
```

### Bytecode view

```java
for (int i = 0; i < arr.length; i++) {
    process(arr[i]);
}
```

Compiles to roughly:
```
iload_0          // push i
iconst_0         // push 0
istore_0         // i = 0

loop_start:
  iload_0        // push i
  aload_1        // push arr
  arraylength    // push arr.length
  if_icmpge end  // if i >= length, exit

  // body
  aload_1        // push arr
  iload_0        // push i
  iaload         // arr[i]
  invokevirtual process()

  iinc 0, 1      // i++
  goto loop_start

end:
```

The JVM uses a simple compare-and-branch. The `iinc` instruction is a single CPU cycle on most architectures, making for-loop overhead minimal.

## 5. Constructors / Usage

### Forward iteration (array)
```java
int[] nums = {10, 20, 30};
for (int i = 0; i < nums.length; i++) {
    System.out.println(nums[i]);
}
```

### Forward iteration (List)
```java
List<String> names = List.of("Ada", "Bjarne", "James");
for (int i = 0; i < names.size(); i++) {
    System.out.println(names.get(i));
}
```

### Backward iteration
```java
List<String> names = List.of("Ada", "Bjarne", "James");
for (int i = names.size() - 1; i >= 0; i--) {
    System.out.println(names.get(i));
}
```

### Skip by 2
```java
for (int i = 0; i < 100; i += 2) {
    System.out.println(i); // 0, 2, 4, ...
}
```

### break and continue
```java
for (int i = 0; i < 10; i++) {
    if (i == 3) continue; // skip 3
    if (i == 7) break;    // stop at 7
    System.out.println(i); // 0, 1, 2, 4, 5, 6
}
```

### Nested loops
```java
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        System.out.print(i + "," + j + " ");
    }
}
```

## 6. Methods

The `for` loop is a language construct, not a method. Its behavior is defined by the Java Language Specification.

| Construct | Description |
|-----------|-------------|
| `for (init; cond; update)` | Standard counted loop |
| `for (;;)` | Infinite loop |
| `break` | Exits the innermost loop |
| `continue` | Skips to next iteration |
| `break label` | Exits a labeled outer loop |
| `continue label` | Restarts a labeled outer loop |

## 7. Complexity Table

| Pattern | Time | Space | Notes |
|---------|------|-------|-------|
| Single pass | O(n) | O(1) | One counter variable |
| Nested loop | O(n²) | O(1) | Two counter variables |
| Backward pass | O(n) | O(1) | Same as forward |
| Skip pattern | O(n/k) | O(1) | k = step size |
| break on match | O(n) worst | O(1) | Early exit possible |
| Labeled break | O(n × m) | O(1) | Two loop levels |

## 8. Thread Safety

The `for` loop itself has no thread-safety concerns. The danger is in shared mutable state:

```java
// UNSAFE — multiple threads incrementing shared counter
for (int i = 0; i < 10_000; i++) {
    sharedCounter++; // data race
}

// SAFE — each thread works on its own range
for (int i = threadStart; i < threadEnd; i++) {
    localSum += data[i]; // no sharing
}
```

When iterating over shared collections from multiple threads, use `CopyOnWriteArrayList` or synchronize externally.

## 9. Memory Behavior

```
Stack frame for for-loop:
┌────────────────────────┐
│ i (int, 4 bytes)       │  ← lives only during loop
│ arr reference (8 bytes) │
└────────────────────────┘

No heap allocation for the loop itself.
```

| Component | Bytes | Lifetime |
|-----------|-------|----------|
| Loop counter `i` | 4 | Loop scope only |
| Array reference | 8 | Method scope |
| Temporary index for `get()` | 4 | Per-call |

## 10. Production Incidents

### Incident 1: Off-by-One Error

**Problem:** `ArrayIndexOutOfBoundsException` in production batch job.
**Cause:** Developer used `<=` instead of `<` in `for (int i = 0; i <= arr.length; i++)`.
**Impact:** Batch job crashes on every run, data not processed.
**Solution:** Changed to `i < arr.length`.
**Prevention:** Always use `<` for upper bound. Write unit tests for boundary values.

### Incident 2: Infinite Loop from Missing Update

**Problem:** Production server CPU spikes to 100%.
**Cause:** A `for` loop had a `continue` that skipped the update expression:
```java
for (int i = 0; i < list.size(); i++) {
    if (skipCondition(list.get(i))) continue;
    process(list.get(i));
}
```
When `skipCondition` was true for all elements, `i` never incremented.
**Impact:** Service became unresponsive.
**Solution:** Restructured the loop to always increment `i`.
**Prevention:** Never place `continue` before the update in a for loop if the update is the only way `i` changes.

### Incident 3: Using get(i) on LinkedList

**Problem:** API response time went from 200ms to 30 seconds under load.
**Cause:** Developer used `for (int i = 0; i < linkedList.size(); i++) linkedList.get(i)`.
**Impact:** O(n²) behavior on a 50K-element LinkedList.
**Solution:** Switched to enhanced for loop or ArrayList.
**Prevention:** Never use index-based access on LinkedList. Use enhanced for or Iterator.

## 11. Engineering Decision Framework

### When Should I Use This?
- You need the index value during iteration
- You need random access to elements by position
- You are iterating over an array (not a Collection)
- You need backward iteration
- You need to skip elements by a fixed step

### When Should I NOT Use This?
- You do not need the index (use enhanced for)
- You are iterating over a LinkedList (use Iterator or enhanced for)
- You need to modify the collection during iteration (use Iterator)
- You need bidirectional traversal (use ListIterator)

### What Are the Alternatives?

| Alternative | When to Use | Trade-off |
|-------------|-------------|-----------|
| Enhanced for | Simple forward traversal, no index needed | Cannot access index |
| Iterator | Need to remove elements during iteration | More verbose |
| while + Iterator | Complex traversal logic | More verbose |
| Stream API | Functional transformations | Slight overhead |
| forEach() | One-liner operations | No index, no break |

### What Trade-offs Am I Making?
- **Readability**: For loop is clear about bounds but verbose
- **Safety**: Manual bounds checking can cause off-by-one errors
- **Performance**: O(1) per access on ArrayList/arrays, O(n) per access on LinkedList

### What Would I Choose in Production?
> Use the for loop when you need the index. For simple iteration over a list where you don't need the index, prefer the enhanced for loop. For LinkedList, never use indexed access.

### Code Review Comments
- "You don't need the index here — use enhanced for loop instead."
- "This `get(i)` call on LinkedList makes this O(n²). Switch to Iterator."
- "Cache `list.size()` in a local variable if the list could grow during iteration."
- "Use `i < arr.length` not `i <= arr.length`."

## 12. Performance

| Operation | ArrayList | LinkedList | Array |
|-----------|-----------|------------|-------|
| get(i) | O(1) | O(n) | O(1) |
| Iteration | O(n) cache-friendly | O(n) cache-unfriendly | O(n) cache-friendly |
| Loop overhead | ~1 ns/iteration | ~1 ns/iteration | ~1 ns/iteration |

JIT optimizations:
- Loop unrolling: JVM processes multiple iterations per branch check
- Bounds check elimination: JIT removes redundant index checks
- `iinc` instruction: Single CPU cycle for counter increment

## 13. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Off-by-one | Unit test boundaries | Test with 0, 1, and max-size collections |
| Infinite loop | Thread dump + stack trace | Check if update expression is always executed |
| Slow indexed access on LinkedList | Profiling | Switch to ArrayList or enhanced for |
| IndexOutOfBoundsException | Debug logging | Log `i` and collection size before access |
| Unexpected skip behavior | Step through debugger | Verify `continue` does not skip the update |

## 14. Code Review Checklist

- [ ] Using `<` not `<=` for upper bound
- [ ] Update expression always executes (no `continue` skipping it)
- [ ] Not using `get(i)` on LinkedList
- [ ] Loop variable scope is minimal (declared inside `for`)
- [ ] Boundary tested: empty collection, single element, max size
- [ ] Nested loops have clear variable names (`i`, `j` or meaningful names)
- [ ] `break`/`continue` usage is clear and well-documented

## 15. Architecture Considerations

### Where For Loop Fits in System Design

| Layer | Use Case | Why For Loop |
|-------|----------|--------------|
| Data Processing | Batch record processing | Index needed for progress tracking |
| Array Algorithms | Sorting, searching | Direct index manipulation required |
| Matrix Operations | 2D array traversal | Nested index access |
| String Building | Character-by-character | Index access to char array |

### Scaling Considerations

| Scale | Recommendation |
|-------|----------------|
| < 1K elements | For loop is fine |
| 1K - 100K elements | For loop on ArrayList/arrays |
| 100K - 1M elements | Consider parallel streams for CPU-bound work |
| > 1M elements | Database-level processing or chunked iteration |

## 16. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Integer overflow in counter | Infinite loop or wrong bounds | Use `long` for large ranges |
| Unbounded loop from user input | DoS via CPU exhaustion | Validate input, set upper bounds |
| Buffer overread from incorrect bounds | Information leak | Always validate array bounds |

## 17. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.0 | for loop introduced | Core iteration mechanism |
| Java 5 | Enhanced for loop added | Less need for indexed for loops |
| Java 8 | Stream API, forEach() | Functional alternatives |
| Java 21 | Pattern matching improvements | Future iteration patterns |

## 18. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Basic for loop | 1.0 | Stable |
| Labeled break/continue | 1.0 | Stable |
| Enhanced for loop | 5.0 | Stable (separate topic) |
| IntStream.range() | 8.0 | Functional alternative |

## 19. Best Practices

1. Use `<` not `<=` for upper bound to avoid off-by-one
2. Cache `list.size()` in a local variable if the list might change during iteration
3. Use enhanced for loop when you don't need the index
4. Never use indexed access on LinkedList
5. Declare loop variable inside the `for` statement to limit scope
6. Prefer backward iteration when removing elements (to avoid index shifting)
7. Use `break` for early exit when you found what you need

## 20. Common Mistakes

1. **Off-by-one with `<=`**: Causes `ArrayIndexOutOfBoundsException`
2. **Forgetting to increment**: Infinite loop if update is conditional
3. **Using `get(i)` on LinkedList**: O(n²) performance disaster
4. **Declaring `i` outside the loop**: Variable leaks, confusing scope
5. **Modifying collection size during forward iteration**: Skips or duplicates elements
6. **Using `size()` in loop condition without caching**: May cause extra method calls (though JIT often optimizes this)

## 21. Common Myths

### Myth 1: For loop is always faster than enhanced for
**Reality:** For arrays, they compile to nearly identical bytecode. For Collections, the for loop calls `get(i)` which is O(1) for ArrayList but O(n) for LinkedList.

### Myth 2: `list.size()` in the loop condition is expensive
**Reality:** JIT compilers often hoist `size()` out of the loop. But for safety with potentially mutating lists, caching it is still wise.

### Myth 3: For loops are outdated
**Reality:** They remain the best choice when you need the index, backward iteration, or stepped iteration. Streams are not always a replacement.

## 22. One-Minute Revision

- For loop bundles initialization, condition, and update in one line
- Use `<` not `<=` to avoid off-by-one errors
- O(1) per access on arrays and ArrayList; O(n) per access on LinkedList
- Never modify collection size during forward iteration
- Use `break` for early exit, `continue` to skip iterations
- Labeled break/continue controls nested loops
- Not thread-safe by itself — depends on what you iterate over

## 23. Related Topics

| Topic | Relationship |
|-------|-------------|
| Enhanced For Loop | Simpler alternative when index is not needed |
| Iterator | Enables safe removal during iteration |
| while Loop | Alternative for condition-based iteration |
| ArrayList | O(1) indexed access makes for loop ideal |
| LinkedList | O(n) indexed access makes for loop a bad choice |
| Stream API | Functional alternative for transformation and filtering |

## 24. Interview Questions

1. **What is the difference between `for` and `while`?** — `for` bundles init/condition/update; `while` only checks condition. Use `for` when iteration count is known.

2. **Why is indexed access on LinkedList slow?** — `get(i)` traverses from the nearest end each time, making it O(n) per call and O(n²) for a full loop.

3. **What happens if you use `<=` instead of `<`?** — `ArrayIndexOutOfBoundsException` on the last iteration.

4. **Can a for loop be infinite?** — Yes, with `for (;;)` or when the update expression is never reached due to `continue`.

5. **How do you safely remove elements while iterating with a for loop?** — Iterate backward, or use an Iterator with `Iterator.remove()`.

## 25. References

- [Oracle Java Documentation - The for Statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/for.html)
- [JLS Section 14.14.1 - The basic for Statement](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.14.1)
- [Effective Java - Item 58: Use for-each loops instead of for loops](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
