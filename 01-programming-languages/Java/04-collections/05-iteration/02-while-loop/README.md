# While Loop

## 1. Scope

This folder covers the `while` loop for condition-based iteration in Java.
Examples and exercises demonstrate indefinite iteration, sentinel values, and Iterator-based traversal patterns.

## 2. Why It Exists

Some loops cannot express their termination condition as a simple counter:

```java
// Reading from a socket until disconnect
while ((line = reader.readLine()) != null) {
    process(line);
}

// Waiting for a condition
while (!task.isComplete()) {
    Thread.sleep(100);
}
```

A `for` loop works when you know the count upfront. A `while` loop works when the number of iterations depends on a runtime condition that changes unpredictably.

## 3. What It Is

The `while` loop evaluates a boolean condition before each iteration. If the condition is true, the body executes. If false, the loop exits. The body may never execute if the condition starts as false.

Syntax:
```
while (condition) {
    // body
}
```

The `do-while` variant executes the body first, then checks the condition — guaranteeing at least one execution.

## 4. Internal Working

```
        ┌──────────────────┐
        │    condition     │──── false ──── exit loop
        └────────┬─────────┘
                 │ true
                 ▼
        ┌──────────────────┐
        │      body        │
        └────────┬─────────┘
                 │
                 └──────► (back to condition)
```

### Bytecode view

```java
while (list.hasNext()) {
    process(list.next());
}
```

Compiles to roughly:
```
loop_start:
  aload_0          // push iterator
  invokevirtual hasNext()
  ifeq end         // if false, exit

  // body
  aload_0
  invokevirtual next()
  invokevirtual process()

  goto loop_start

end:
```

The `while` loop is a simple test-and-branch. The JVM optimizes it with branch prediction — when the condition is almost always true, the CPU pipeline stays full.

### do-while

```java
do {
    body();
} while (condition);
```

Compiles to:
```
loop_start:
  // body
  invokevirtual body()

  // condition
  ifne loop_start   // if true, loop back
```

Note: no initial condition check. The body always runs at least once.

## 5. Constructors / Usage

### Basic while loop
```java
int sum = 0;
int i = 1;
while (i <= 10) {
    sum += i;
    i++;
}
System.out.println(sum); // 55
```

### While with Iterator
```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.isEmpty()) {
        it.remove(); // Safe removal
    }
}
```

### Reading until sentinel
```java
Scanner scanner = new Scanner(System.in);
String input;
while (!(input = scanner.nextLine()).equals("quit")) {
    System.out.println("You said: " + input);
}
```

### do-while (at least one execution)
```java
int num;
do {
    num = random.nextInt(100);
    System.out.println("Generated: " + num);
} while (num != 42);
```

### Nested while with break
```java
int i = 0;
while (true) {          // infinite loop
    if (i > 10) break;  // explicit exit
    System.out.println(i);
    i++;
}
```

### While with complex conditions
```java
while (running && retries < MAX_RETRIES && !response.isSuccessful()) {
    response = httpClient.send(request);
    retries++;
    Thread.sleep(backoff);
}
```

## 6. Methods

The `while` loop is a language construct.

| Construct | Description |
|-----------|-------------|
| `while (cond)` | Loop while condition is true |
| `do { } while (cond)` | Execute body at least once |
| `break` | Exit the loop immediately |
| `break label` | Exit a labeled outer loop |
| `continue` | Skip to next condition check |
| `continue label` | Restart labeled outer loop |

## 7. Complexity Table

| Pattern | Time | Space | Notes |
|---------|------|-------|-------|
| Simple while | O(n) | O(1) | Condition-driven |
| do-while | O(n) | O(1) | Body executes at least once |
| Nested while | O(n × m) | O(1) | Two conditions |
| While with break | O(n) worst | O(1) | Early exit possible |
| Sentinel-controlled | O(n) | O(1) | Reads until special value |

## 8. Thread Safety

The `while` loop itself has no thread-safety concerns. The danger is in shared state that the condition checks:

```java
// UNSAFE — shared flag without synchronization
while (running) {         // reads shared boolean
    process();
}
// Another thread: running = false;  // data race

// SAFE — volatile
volatile boolean running = true;
while (running) {         // sees writes from other threads
    process();
}

// SAFE — synchronized
while (true) {
    synchronized (lock) {
        if (queue.isEmpty()) break;
        process(queue.poll());
    }
}
```

## 9. Memory Behavior

```
Stack frame for while-loop:
┌────────────────────────┐
│ Condition variables     │  ← already on stack
│ Loop body locals        │  ← allocated per iteration
│ Iterator reference      │  ← if using Iterator pattern
└────────────────────────┘

No heap allocation for the loop itself.
```

| Component | Bytes | Notes |
|-----------|-------|-------|
| Condition variables | varies | Depends on expression |
| Iterator (if used) | ~32 | One object per while-Iterator pattern |
| Body locals | varies | Created each iteration, GC'd after |

## 10. Production Incidents

### Incident 1: Infinite Loop from Race Condition

**Problem:** Health check thread stops responding.
**Cause:** `while (running)` where `running` is a plain boolean. Another thread sets it to false, but the reading thread never sees the update due to CPU caching.
**Impact:** Service appears healthy but does not process work.
**Solution:** Made `running` volatile.
**Prevention:** Always use `volatile` or `AtomicBoolean` for flags read across threads.

### Incident 2: While-Iterator Skipping Elements

**Problem:** Not all invalid records were removed from the database.
**Cause:** Developer called `list.remove(currentElement)` inside a while-Iterator loop instead of `it.remove()`. This triggered ConcurrentModificationException on some iterations, silently skipping records.
**Impact:** Corrupt data persisted in production.
**Solution:** Changed to `it.remove()`.
**Prevention:** Always use the Iterator's own remove method.

### Incident 3: Do-While Running Unnecessary Initialization

**Problem:** Application startup takes 45 seconds instead of 5.
**Cause:** A `do-while` loop kept retrying database connection even after successful connection, because the condition checked the wrong variable.
**Impact:** Slow startup, delayed deployments.
**Solution:** Fixed the condition to check connection success.
**Prevention:** Review do-while conditions carefully — the body always runs once regardless.

## 11. Engineering Decision Framework

### When Should I Use This?
- You don't know how many iterations you need
- Termination depends on a runtime condition (not a counter)
- You need to read until a sentinel value
- You need at least one execution (do-while)
- Combining Iterator with conditional removal

### When Should I NOT Use This?
- You know the exact count (use for loop)
- The condition is a simple counter range (use for loop or IntStream.range)
- You want the cleanest syntax for simple traversal (use enhanced for)

### What Are the Alternatives?

| Alternative | When to Use | Trade-off |
|-------------|-------------|-----------|
| For loop | Known iteration count | Not suitable for indefinite loops |
| Enhanced for | Simple forward traversal | No condition-based control |
| Stream API | Functional filtering/transform | Not suitable for side effects |
| Recursive call | Divide-and-conquer algorithms | Stack overflow risk |

### What Trade-offs Am I Making?
- **Clarity**: While loop clearly communicates "run until condition changes"
- **Safety**: Easier to create infinite loops than for loop
- **Flexibility**: More control over when to update variables

### What Would I Choose in Production?
> Use while loops for condition-based iteration where you genuinely don't know the count. For Iterator-based removal patterns, while + Iterator is the standard approach. Avoid while(true) without a clear break condition.

### Code Review Comments
- "This while loop could run forever if the condition never changes — add a timeout or max iteration guard."
- "Use `it.remove()` not `list.remove()` inside the Iterator loop."
- "Consider replacing this while loop with a for loop if the iteration count is deterministic."

## 12. Performance

| Operation | Time | Space | Notes |
|-----------|------|-------|-------|
| Condition check | O(1) | O(1) | Boolean evaluation |
| Body execution | varies | varies | Depends on body logic |
| Branch prediction | fast | — | JVM/CPU optimize common path |

JIT optimizations:
- **Loop unrolling**: JVM unrolls small while loops like for loops
- **Branch prediction**: CPU predicts condition outcome for pipeline efficiency
- **Deoptimization**: If condition changes pattern (e.g., becomes always false), JIT may bail out

## 13. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Infinite loop | Thread dump | Check stack trace for loop location |
| Condition never true | Logging | Log condition value each iteration |
| Early exit | Breakpoint | Set breakpoint at `break` statement |
| Missing iteration | Debugger | Step through, watch condition |
| ConcurrentModificationException | Stack trace | Check if collection modified during while-Iterator |

## 14. Code Review Checklist

- [ ] Loop termination condition is guaranteed to become false
- [ ] No shared mutable state without synchronization
- [ ] Iterator.remove() used instead of collection.remove()
- [ ] No `while(true)` without clear break/return
- [ ] Timeout or max iteration guard for external conditions
- [ ] do-while used only when at least one execution is truly needed

## 15. Architecture Considerations

### Where While Loop Fits in System Design

| Layer | Use Case | Why While |
|-------|----------|-----------|
| I/O Processing | Read until EOF | Condition-based termination |
| Retry Logic | Retry until success or timeout | Runtime condition |
| Event Processing | Poll queue until empty | Variable message count |
| Stream Processing | Process until channel closes | External signal |

### Scaling Considerations

| Scale | Recommendation |
|-------|----------------|
| < 1K iterations | While is fine |
| 1K - 100K iterations | Ensure condition is efficient |
| > 100K iterations | Consider chunking to avoid blocking |

## 16. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Infinite loop from untrusted input | DoS via CPU exhaustion | Set max iteration count |
| Unbounded retry | Resource exhaustion | Exponential backoff + max retries |
| Shared state race condition | Data corruption | Use volatile or synchronization |

## 17. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.0 | while loop introduced | Core language feature |
| Java 5 | Enhanced for added | Reduces need for while in many cases |
| Java 8 | Stream API | Functional alternative for many while patterns |
| Java 9 | Optional.ifPresent() | Reduces while-Iterator patterns |

## 18. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| while loop | 1.0 | Stable |
| do-while loop | 1.0 | Stable |
| Labeled break/continue | 1.0 | Stable |
| Stream API (alternative) | 8.0 | Stable |

## 19. Best Practices

1. Always ensure the loop condition will eventually become false
2. Add a max iteration guard for loops driven by external conditions
3. Use `volatile` or `AtomicBoolean` for cross-thread loop flags
4. Use Iterator.remove() for removal during while-Iterator loops
5. Prefer for loop when iteration count is known
6. Use do-while only when at least one execution is required
7. Avoid deeply nested while loops — extract to methods

## 20. Common Mistakes

1. **Infinite loop**: Forgetting to update the condition variable
2. **Using collection.remove() instead of it.remove()**: ConcurrentModificationException
3. **Condition checked too early/late**: do-while vs while confusion
4. **Shared flag without volatile**: Loop never sees the update from another thread
5. **Complex condition without comments**: Hard to understand when the loop exits
6. **Missing backoff in retry loops**: Tight retry loops waste CPU

## 21. Common Myths

### Myth 1: While loops are always slower than for loops
**Reality:** They compile to identical bytecode. The JVM treats them the same way.

### Myth 2: do-while is rarely useful
**Reality:** It is the right choice for input validation, retry logic, and any pattern where you need at least one execution.

### Myth 3: While(true) is always bad
**Reality:** It is a standard pattern for event loops, server accept loops, and state machines — as long as there is a clear `break` or `return`.

## 22. One-Minute Revision

- While loop checks condition before each iteration (body may never run)
- do-while executes body first, then checks condition (guarantees one execution)
- Best for condition-based, indefinite iteration
- Use Iterator.remove() for safe removal during while-Iterator loops
- Always ensure the condition eventually becomes false
- Use volatile for cross-thread loop flags
- while(true) with break is fine for event loops and state machines

## 23. Related Topics

| Topic | Relationship |
|-------|-------------|
| For Loop | Use when iteration count is known |
| Enhanced For | Use for simple forward traversal |
| Iterator | Commonly paired with while for conditional removal |
| Stream API | Functional alternative for many while patterns |
| do-while | Variant that guarantees at least one execution |

## 24. Interview Questions

1. **What is the difference between while and do-while?** — while checks condition before execution; do-while checks after, guaranteeing at least one body execution.

2. **How do you prevent an infinite while loop?** — Ensure the condition variable is updated every iteration. Add a max iteration counter as a safety net.

3. **Why use Iterator.remove() instead of list.remove() inside a while loop?** — list.remove() causes ConcurrentModificationException because it invalidates the Iterator's internal state.

4. **When would you use while(true)?** — Event loops, server accept loops, and state machines where the exit condition depends on runtime events.

5. **How do you make a while loop safe across threads?** — Use `volatile` for the condition variable, or use `AtomicBoolean`, or synchronize access.

## 25. References

- [Oracle Java Documentation - The while Statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/while.html)
- [JLS Section 14.12 - The while Statement](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.12)
- [JLS Section 14.13 - The do Statement](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.13)
