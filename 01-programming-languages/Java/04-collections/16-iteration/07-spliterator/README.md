# Spliterator — Parallel Processing Primitive

## Why Spliterator Exists

Java 8 Streams need a way to **divide work across multiple threads**. The `Spliterator` (Split + Iterator) interface provides this — it can split a collection into parts and iterate them independently. It's the engine under parallel streams.

**Production incident:** A data analytics platform processed 100M records using sequential streams. Switching to parallel streams with proper Spliterator implementation reduced processing time from 4 hours to 45 minutes.

## The Pain Point

Iterator can only go forward, one element at a time. For parallel processing, you need to:
1. Know how many elements exist (estimate size)
2. Split the data into chunks
3. Process chunks independently
4. Merge results

Spliterator provides all four capabilities.

## Spliterator Interface

```java
public interface Spliterator<T> {
    // Traversal
    boolean tryAdvance(Consumer<? super T> action);
    void forEachRemaining(Consumer<? super T> action);

    // Splitting
    Spliterator<T> trySplit();

    // Characteristics
    int characteristics();
    long estimateSize();
    default long getExactSizeIfKnown() { ... }
    default Comparator<? super T> getComparator() { ... }
}
```

## tryAdvance() — One Element at a Time

```java
// Like Iterator.hasNext()/next() but with Consumer
Spliterator<String> spliterator = list.spliterator();

// Process one element
while (spliterator.tryAdvance(System.out::println)) {
    // Action executed inside tryAdvance
}

// With custom action
while (spliterator.tryAdvance(name -> {
    if (name.length() > 3) {
        longNames.add(name);
    }
})) {
    // Continue processing
}
```

## forEachRemaining() — Batch Processing

```java
// Process all remaining elements at once
Spliterator<String> spliterator = list.spliterator();

// Process first element
spliterator.tryAdvance(name -> process(name));

// Process all remaining
spliterator.forEachRemaining(name -> process(name));
// Action is optimized internally — faster than repeated tryAdvance
```

## estimateSize() — Know How Many Elements

```java
Spliterator<String> spliterator = list.spliterator();

long estimated = spliterator.estimateSize();
System.out.println("Approximately " + estimated + " elements");

// Exact size (if known)
long exact = spliterator.getExactSizeIfKnown();
// Returns -1 if not O(1) computable
```

## Characteristics — Know Your Spliterator

```java
// Key characteristics (bit flags)
int characteristics = spliterator.characteristics();

// Check specific characteristic
boolean ordered = (characteristics & Spliterator.ORDERED) != 0;
boolean sized = (characteristics & Spliterator.SIZED) != 0;
boolean concurrent = (characteristics & Spliterator.CONCURRENT) != 0;
boolean immutable = (characteristics & Spliterator.IMMUTABLE) != 0;
```

### Characteristic Breakdown

| Characteristic | Meaning |
|---------------|---------|
| ORDERED | Elements have a defined order (List) |
| SIZED | estimateSize() returns exact count |
| SUBSIZED | trySplit() returns sized sub-spliterators |
| CONCURRENT | Safe to call tryAdvance() from multiple threads |
| IMMUTABLE | Collection won't be modified during iteration |
| NONNULL | No null elements |
| DISTINCT | No duplicate elements (Set) |
| SORTED | Elements are sorted |

```java
// ArrayList characteristics: ORDERED | SIZED | SUBSIZED | IMMUTABLE | NONNULL
// HashSet characteristics: CONCURRENT | NONNULL | DISTINCT
// TreeSet characteristics: ORDERED | SORTED | SIZED | SUBSIZED | NONNULL | DISTINCT
```

## trySplit() — Divide and Conquer

```java
// Split into two parts
Spliterator<String> spliterator = list.spliterator();
Spliterator<String> left = spliterator.trySplit();

// left processes first half
// spliterator processes second half
// Returns null if not possible to split

// Recursive splitting
void processInParallel(Spliterator<T> spliterator) {
    Spliterator<T> other = spliterator.trySplit();
    if (other != null) {
        // Process in separate thread
        ForkJoinPool.commonPool().submit(() -> processInParallel(other));
    }
    // Process remaining locally
    spliterator.forEachRemaining(this::process);
}
```

### How trySplit() Works

```java
// ArrayList: splits by index
// list: [A, B, C, D, E, F, G, H]
// trySplit() returns: [A, B, C, D]
// Original becomes: [E, F, G, H]

// LinkedList: splits by count (no random access)
// May not be perfectly balanced

// HashSet: splits by bucket ranges
// Returns approximately half the buckets
```

## Stream API Underlying Mechanism

```java
// When you call parallelStream(), it uses Spliterator
list.parallelStream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .forEach(System.out::println);

// Under the hood:
// 1. list.spliterator() creates Spliterator
// 2. trySplit() divides work across threads
// 3. Each thread processes its portion
// 4. Results merged

// Custom Spliterator with Stream
Spliterator<String> spliterator = list.spliterator();
Stream<String> stream = StreamSupport.stream(spliterator, true);  // true = parallel
```

## When to Use / When NOT to Use

### ✅ USE Spliterator When:
- Implementing parallel processing
- Writing custom collection classes
- Optimizing Stream performance
- Need to divide work across threads
- Processing large datasets

### ❌ DON'T Use Spliterator When:
- Sequential iteration is fine → use Iterator
- Simple collection → use enhanced for
- Need bidirectional → use ListIterator
- Don't need parallelism → use Stream

## Performance: Divide and Conquer

```
Collection Size │ Sequential │ Parallel (4 cores) │ Speedup
────────────────┼────────────┼─────────────────────┼────────
1,000           │ 1ms        │ 2ms                 │ 0.5x (overhead)
10,000          │ 10ms       │ 3ms                 │ 3.3x
100,000         │ 100ms      │ 25ms                │ 4x
1,000,000       │ 1s         │ 280ms               │ 3.6x
```

**Break-even point:** ~10,000 elements for 4-core systems. Below that, parallel overhead exceeds benefit.

## Common Mistakes

### Mistake 1: Ignoring Characteristics
```java
// WRONG: assuming ORDERED
Spliterator<String> spliterator = someCollection.spliterator();
// Can't assume order — use ORDERED check
if ((spliterator.characteristics() & Spliterator.ORDERED) != 0) {
    // Safe to assume order
}
```

### Mistake 2: Not Handling trySplit() Return
```java
// WRONG: ignoring split
Spliterator<String> spliterator = list.spliterator();
spliterator.trySplit();  // Discarded — wasted work!

// RIGHT: use both halves
Spliterator<String> left = spliterator.trySplit();
if (left != null) {
    process(left);  // Process left half
}
process(spliterator);  // Process right half
```

### Mistake 3: Assuming Exact Size
```java
// WRONG: relying on estimateSize()
Spliterator<String> spliterator = set.spliterator();
int count = (int) spliterator.estimateSize();  // Approximate!

// RIGHT: use getExactSizeIfKnown()
long exact = spliterator.getExactSizeIfKnown();
if (exact >= 0) {
    // Exact size available
} else {
    // Must iterate to count
}
```

## Interview Questions

**Q: What is Spliterator?**
A: A combination of Split + Iterator. It can divide a collection into parts for parallel processing and iterate elements.

**Q: How does Spliterator enable parallel streams?**
A: trySplit() divides work into chunks that can be processed independently by different threads in ForkJoinPool.

**Q: What does ORDERED characteristic mean?**
A: Elements have a defined order (like List). Without ORDERED, elements may be processed in any order.

**Q: When should you NOT use parallel streams?**
A: Small datasets (<10K elements), I/O-bound operations, or when ordering matters and is expensive.

**Q: What's the difference between Spliterator and Iterator?**
A: Spliterator adds splitting (trySplit()), size estimation, and characteristics. Iterator only provides forward traversal.
