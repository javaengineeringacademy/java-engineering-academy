# Stream — Functional Iteration

## Why Stream Exists

Streams transform iteration from an **imperative "how"** to a **declarative "what"**. Instead of writing loops and managing state, you describe the transformation pipeline. It's Java's answer to functional programming iteration.

**Production incident:** A recommendation engine processed 50M user records. The original code used nested for-loops with 47 mutable variables. Rewriting with Streams reduced it to 12 lines, eliminated 15 bugs, and made the code parallel-ready with a single keyword.

## The Pain Point

Traditional iteration is verbose and error-prone:
```java
// Find all active users over 18, sorted by name, collect emails
List<String> emails = new ArrayList<>();
for (User user : users) {
    if (user.isActive()) {
        if (user.getAge() > 18) {
            emails.add(user.getEmail());
        }
    }
}
Collections.sort(emails);
```

Same logic with Stream:
```java
List<String> emails = users.stream()
    .filter(User::isActive)
    .filter(u -> u.getAge() > 18)
    .map(User::getEmail)
    .sorted()
    .collect(Collectors.toList());
```

## Stream as Iteration Abstraction

A Stream is **not a data structure** — it's a pipeline for processing data. It doesn't modify the source; it produces results.

```java
// Stream operations
Stream<String> stream = list.stream();           // Create
stream.filter(s -> s.length() > 3);             // Intermediate
stream.map(String::toUpperCase);                 // Intermediate
stream.sorted();                                 // Intermediate
stream.collect(Collectors.toList());             // Terminal
```

## Internal vs External Iteration

```java
// External iteration (for loop) — YOU control the loop
for (String name : list) {
    process(name);
}

// Internal iteration (Stream) — Stream controls the loop
list.forEach(this::process);

// Key difference:
// External: you decide when/how to iterate
// Internal: library decides (can optimize, parallelize)
```

## Lazy Evaluation

```java
// Intermediate operations are LAZY — nothing happens until terminal
List<String> result = list.stream()
    .filter(s -> {
        System.out.println("Filtering: " + s);
        return s.length() > 3;
    })
    .map(s -> {
        System.out.println("Mapping: " + s);
        return s.toUpperCase();
    })
    .collect(Collectors.toList());

// Output shows lazy evaluation:
// No output yet!

// Only when collect() is called:
// Filtering: Alice
// Mapping: Alice
// Filtering: Bob
// Mapping: Bob
// Filtering: Charlie
// Mapping: Charlie
// ...
```

### Why Lazy Evaluation Matters

```java
// Without lazy: processes ALL elements
List<String> result = new ArrayList<>();
for (String s : list) {
    if (s.length() > 3) {
        result.add(s.toUpperCase());
    }
}

// With lazy: can short-circuit
String first = list.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .findFirst()  // Stops after first match!
    .orElse("");
```

## Pipeline: Source → Intermediate → Terminal

```
┌─────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│ Source   │ →  │ Interm.  │ →  │ Interm.  │ →  │ Interm.  │ →  │ Terminal │
│ stream() │    │ filter() │    │ map()    │    │ sorted() │    │ collect()│
└─────────┘    └──────────┘    └──────────┘    └──────────┘    └──────────┘
```

```java
// Source operations
list.stream()            // From Collection
Arrays.stream(array)     // From array
Stream.of(a, b, c)       // From values
Stream.iterate(0, n -> n + 1)  // Infinite stream
Stream.generate(() -> Math.random())  // Infinite stream

// Intermediate operations (return Stream)
.filter(predicate)       // Keep matching elements
.map(mapper)             // Transform elements
.flatMap(mapper)         // Flatten nested streams
.sorted()                // Sort elements
.distinct()              // Remove duplicates
.limit(n)                // Take first n
.skip(n)                 // Skip first n
.peek(consumer)          // Debug: observe without modifying

// Terminal operations (produce result)
.collect(collector)      // Collect to collection
.forEach(consumer)       // Process each element
.reduce(accumulator)     // Combine elements
.count()                 // Count elements
.anyMatch(predicate)     // Check if any match
.allMatch(predicate)     // Check if all match
.noneMatch(predicate)    // Check if none match
.findFirst()             // Get first element
.findAny()               // Get any element (parallel)
.min(comparator)         // Get minimum
.max(comparator)         // Get maximum
.toArray()               // Convert to array
```

## Performance: Parallel Streams

```java
// Sequential
list.stream()
    .filter(...)
    .map(...)
    .collect(...);

// Parallel — one keyword change!
list.parallelStream()
    .filter(...)
    .map(...)
    .collect(...);

// Under the hood: uses Spliterator to divide work
// Processed by ForkJoinPool (common pool by default)
```

### When Parallel Helps

```
Dataset Size │ Sequential │ Parallel │ Speedup
─────────────┼────────────┼──────────┼────────
1,000        │ 5ms        │ 8ms      │ 0.6x (slower!)
10,000       │ 50ms       │ 15ms     │ 3.3x
100,000      │ 500ms      │ 130ms    │ 3.8x
1,000,000    │ 5s         │ 1.4s     │ 3.6x
```

**Rule of thumb:** Parallel streams help with CPU-bound work on large datasets. They hurt for small datasets or I/O-bound work.

## When to Use / When NOT to Use

### ✅ USE Stream When:
- Functional style preferred
- Parallel processing needed
- Complex filter/map/reduce chains
- Processing large datasets
- Avoiding mutable state

### ❌ DON'T Use Stream When:
- Simple iteration (enhanced for is clearer)
- Need to modify source collection
- Performance-critical inner loops
- Debugging (harder to step through)
- Side effects needed (use forEach carefully)

## Common Mistakes

### Mistake 1: Modifying Source During Stream
```java
// WRONG: ConcurrentModificationException
list.stream()
    .filter(s -> {
        list.remove(s);  // Modifies source!
        return true;
    })
    .collect(...);

// RIGHT: collect to new list, then modify
List<String> toRemove = list.stream()
    .filter(s -> s.isEmpty())
    .collect(Collectors.toList());
list.removeAll(toRemove);
```

### Mistake 2: Using findFirst() on Parallel Stream
```java
// WRONG: findFirst() may not return "first" in parallel
String first = list.parallelStream()
    .filter(s -> s.length() > 3)
    .findFirst()  // May return arbitrary match!
    .orElse("");

// RIGHT: use sequential for ordered results
String first = list.stream()  // .stream(), not .parallelStream()
    .filter(s -> s.length() > 3)
    .findFirst()
    .orElse("");
```

### Mistake 3: Ignoring Lazy Evaluation
```java
// WRONG: expecting immediate execution
Stream<String> stream = list.stream()
    .filter(s -> {
        System.out.println("Filtering " + s);
        return true;
    });
// Nothing printed yet!

// RIGHT: trigger with terminal operation
stream.collect(Collectors.toList());  // Now filtering happens
```

### Mistake 4: Side Effects in Stream
```java
// WRONG: mutable shared state
List<String> results = new ArrayList<>();
list.parallelStream()
    .map(String::toUpperCase)
    .forEach(results::add);  // Race condition in parallel!

// RIGHT: use collect
List<String> results = list.parallelStream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());  // Thread-safe collection
```

## Comparison with Traditional Iteration

```java
// Traditional for loop
List<String> result = new ArrayList<>();
for (User user : users) {
    if (user.isActive()) {
        result.add(user.getName());
    }
}

// Stream equivalent
List<String> result = users.stream()
    .filter(User::isActive)
    .map(User::getName)
    .collect(Collectors.toList());

// Which is better?
// For simple cases: for loop (clearer)
// For complex pipelines: Stream (more readable)
// For parallelism: Stream (trivial to parallelize)
```

## Interview Questions

**Q: What is the difference between Stream and Collection?**
A: Collection is data; Stream is a pipeline for processing data. Stream doesn't store data — it processes it lazily.

**Q: What makes Stream operations lazy?**
A: Intermediate operations (filter, map) are deferred. Nothing happens until a terminal operation (collect, forEach) is called.

**Q: When should you use parallelStream() vs stream()?**
A: Parallel for CPU-bound work on large datasets (>10K elements). Sequential for small datasets, I/O-bound work, or when ordering matters.

**Q: What is a stateful operation in Stream?**
A: Operations that remember information across elements (sorted, distinct, limit). These require multiple passes or buffering.

**Q: Can Stream modify the source collection?**
A: No. Stream operations are non-destructive. The source collection is never modified. Use collect to create a new collection.
