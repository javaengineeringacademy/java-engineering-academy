# ConcurrentHashMap

## 1. Introduction

ConcurrentHashMap is a thread-safe implementation of the `Map` interface that allows concurrent access without locking the entire map. Introduced in Java 5 as part of the `java.util.concurrent` package, it provides better performance than `Collections.synchronizedMap()` by allowing concurrent reads and segment-based locking for writes.

Unlike Hashtable which locks the entire map for every operation, ConcurrentHashMap uses a more sophisticated approach:
- **Java 7**: Segment locking (divides the table into 16 segments, each with its own lock)
- **Java 8+**: CAS operations + synchronized on individual buckets (fine-grained locking)

ConcurrentHashMap is the go-to choice for concurrent Map operations in Java. It supports non-blocking reads, fully concurrent updates, and a set of atomic operations (compute, merge, putIfAbsent) that eliminate the need for external synchronization.

## 2. Learning Objectives

- Create and use ConcurrentHashMap for thread-safe operations
- Understand the difference between ConcurrentHashMap and Collections.synchronizedMap()
- Learn about CAS (Compare-And-Swap) operations
- Master atomic operations: compute, merge, computeIfAbsent, computeIfPresent
- Understand the segment locking mechanism (Java 7) vs bucket-level locking (Java 8+)
- Learn about weakly consistent iterators
- Understand when to use ConcurrentHashMap vs other synchronization strategies
- Know the limitations and pitfalls of ConcurrentHashMap

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Module 15: HashMap (understand hash table internals)
- Basic understanding of threads and synchronization
- Familiarity with atomic operations

## 4. Why This Concept Exists

Before ConcurrentHashMap, Java developers had limited options for concurrent maps:

1. **Hashtable**: Synchronized all methods, terrible performance (single lock)
2. **Collections.synchronizedMap()**: Wrapper that synchronizes all methods, same problem
3. **Manual synchronization**: `synchronized(map) { ... }` for compound operations

These approaches had problems:
- **Lock contention**: Only one thread could access the map at a time
- **No atomic compound operations**: check-then-act required external synchronization
- **Poor scalability**: Performance degraded with more threads

ConcurrentHashMap solves these by:
- **Fine-grained locking**: Lock only the affected bucket, not the entire map
- **Lock-free reads**: `get()` operations never block
- **Atomic operations**: `compute()`, `merge()`, `putIfAbsent()` are thread-safe
- **Weakly consistent iterators**: Don't throw ConcurrentModificationException

## 5. Problem Statement

Consider building a web application with:
- Multiple threads handling concurrent requests
- A shared cache that stores user sessions
- Frequent reads (session lookups) and writes (session creation/updates)
- High concurrency (thousands of requests per second)

Using Hashtable or synchronizedMap:
- Every request blocks all other requests
- Response time increases with load
- System doesn't scale

Using ConcurrentHashMap:
- Concurrent reads don't block each other
- Writes lock only the affected bucket
- System scales with more threads

## 6. Theory

### ConcurrentHashMap Structure (Java 8+)

```java
transient volatile Node<K,V>[] table;  // Array of buckets
```

### Key Differences from HashMap

| Feature | HashMap | ConcurrentHashMap |
|---------|---------|-------------------|
| Thread-safe | No | Yes |
| Null keys | One | Not allowed |
| Null values | Multiple | Not allowed |
| Iterators | fail-fast | weakly consistent |
| Locking | None | Bucket-level (CAS + synchronized) |

### CAS (Compare-And-Swap)

CAS is a hardware-level atomic operation that:
1. Reads the current value
2. Compares it with expected value
3. If equal, updates to new value
4. Returns whether the update succeeded

```java
// Pseudocode for CAS
boolean compareAndSwap(V expected, V newValue) {
    if (currentValue == expected) {
        currentValue = newValue;
        return true;
    }
    return false;
}
```

### Bucket-Level Locking (Java 8+)

```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();  // CAS to initialize
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))
                break;  // CAS to add to empty bucket
        }
        else if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        else {
            synchronized (f) {  // Lock only this bucket
                // ... normal put logic
            }
        }
    }
    return null;
}
```

### Weakly Consistent Iterators

ConcurrentHashMap iterators are weakly consistent:
- Reflect the state of the map at some point since the iterator was created
- May (but are not guaranteed to) reflect modifications made after creation
- Never throw ConcurrentModificationException
- May return duplicates or miss elements

## 7. Internal Working

### Table Initialization

```java
private final Node<K,V>[] initTable() {
    Node<K,V>[] tab; int n;
    while ((tab = table) == null || (n = tab.length) == 0) {
        if ((sc = sizeCtl) < 0)
            Thread.yield();  // Lost initialization race
        else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
            try {
                if ((tab = table) == null || (n = tab.length) == 0) {
                    n = (sc > 0) ? sc : DEFAULT_CAPACITY;  // 16
                    @SuppressWarnings("unchecked")
                    Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                    table = tab = nt;
                    sc = n - (n >>> 2);  // 0.75 * n
                }
            } finally {
                sizeCtl = sc;
            }
            break;
        }
    }
    return tab;
}
```

### TabAt (Volatile Read)

```java
static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i) {
    return (Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
}
```

Uses `Unsafe.getObjectVolatile()` for volatile read semantics.

### CAS TabAt (CAS Operation)

```java
static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i,
                                     Node<K,V> c, Node<K,V> v) {
    return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
}
```

Uses `Unsafe.compareAndSwapObject()` for CAS operation.

### Size Calculation

```java
public int size() {
    long n = sumCount();
    return ((n < 0) ? 0 :
            (n > (long)Integer.MAX_VALUE) ? Integer.MAX_VALUE :
            (int)n);
}

final long sumCount() {
    CounterCell[] as = counterCells; CounterCell a;
    long sum = baseCount;
    if (as != null) {
        for (int i = 0; i < as.length; ++i) {
            if ((a = as[i]) != null)
                sum += a.value;
        }
    }
    return sum;
}
```

Uses `LongAdder`-style counters for better performance under contention.

## 8. JVM Perspective

### Memory Allocation

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
// JVM allocates:
// - ConcurrentHashMap object header: 12 bytes
// - table reference: 8 bytes
// - baseCount: 8 bytes
// - sizeCtl: 4 bytes
// - counterCells reference: 8 bytes
// Total ConcurrentHashMap object: ~48 bytes

// Each Node:
// - Node object header: 12 bytes
// - hash: 4 bytes
// - key reference: 8 bytes
// - value reference: 8 bytes
// - next reference: 8 bytes
// Total per Node: ~40 bytes
```

### Unsafe Operations

ConcurrentHashMap uses `sun.misc.Unsafe` for:
- Volatile reads (`getObjectVolatile`)
- CAS operations (`compareAndSwapObject`)
- Ordered writes (`putObject`)

### Memory Barriers

CAS operations provide memory barriers:
- **Acquire barrier**: Prevents reordering of subsequent operations
- **Release barrier**: Prevents reordering of preceding operations
- Ensures visibility of updates across threads

## 9. Memory Representation

```
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("Alice", 30);
map.put("Bob", 25);

Memory layout:
┌────────────────────────────────┐
│ ConcurrentHashMap object       │
├────────────────────────────────┤
│ Object header (12 bytes)       │
│ table ──────────────────────────┐
│ baseCount = 0 (8 bytes)        │     │
│ sizeCtl = 12 (4 bytes)         │     │
│ counterCells ──────────────────────┐
└────────────────────────────────┘     │
                                       ▼
                                Node[] table (capacity 16)
                                ┌────────────────────────┐
                                │ [0] → null             │
                                │ [1] → null             │
                                │ ...                    │
                                │ [5] → Node("Alice",30) │
                                │ [6] → Node("Bob",25)   │
                                │ [7] → null             │
                                │ ...                    │
                                └────────────────────────┘

Node("Alice", 30):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ hash (int, 4 bytes)         │
│ key → String "Alice"        │
│ value → Integer 30          │
│ next → null                 │
└─────────────────────────────┘
```

## 10. Syntax

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

// ============================================
// CREATION
// ============================================
Map<K, V> map = new ConcurrentHashMap<>();
Map<K, V> map = new ConcurrentHashMap<>(16);           // Initial capacity
Map<K, V> map = new ConcurrentHashMap<>(16, 0.75f);   // Capacity and load factor
Map<K, V> map = new ConcurrentHashMap<>(16, 0.75f, 8); // With concurrency level

// ============================================
// BASIC OPERATIONS (Thread-Safe)
// ============================================
map.put(key, value);                    // Returns old value or null
map.putIfAbsent(key, value);            // Atomic: add only if absent
map.get(key);                           // Non-blocking read
map.remove(key);                        // Returns old value or null
map.remove(key, value);                 // Atomic: remove only if matches
map.replace(key, newValue);             // Returns old value or null
map.replace(key, oldValue, newValue);   // Atomic: conditional replace

// ============================================
// ATOMIC OPERATIONS
// ============================================
// compute: Atomic update with function
map.compute(key, (k, v) -> v == null ? defaultValue : transform(v));

// computeIfAbsent: Atomic add-if-absent
map.computeIfAbsent(key, k -> expensiveComputation(k));

// computeIfPresent: Atomic update-if-present
map.computeIfPresent(key, (k, v) -> v + 1);

// merge: Atomic merge with function
map.merge(key, value, (old, newVal) -> old + newVal);

// ============================================
// SEARCHING
// ============================================
boolean hasKey = map.containsKey(key);      // O(1) average
boolean hasValue = map.containsValue(value); // O(n)
boolean empty = map.isEmpty();
int size = map.size();                       // Approximate

// ============================================
// VIEW COLLECTIONS
// ============================================
Set<K> keys = map.keySet();              // Weakly consistent
Collection<V> values = map.values();     // Weakly consistent
Set<Map.Entry<K,V>> entries = map.entrySet(); // Weakly consistent

// ============================================
// ITERATION
// ============================================
// forEach (weakly consistent)
map.forEach((key, value) -> System.out.println(key + " = " + value));

// forEach with parallelism threshold
map.forEach(4, (key, value) -> System.out.println(key + " = " + value));

// search (parallel)
V result = map.search(4, (key, value) -> value > 30 ? key : null);

// reduce (parallel)
int sum = map.reduceValues(4, v -> v, Integer::sum);

// ============================================
// AGGREGATE OPERATIONS
// ============================================
long totalCount = map.mappingCount();  // Better than size() for large maps
map.forEachKey(4, key -> process(key));
map.forEachValue(4, value -> process(value));
```

## 11. Easy Example

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ConcurrentHashMapBasics {
    public static void main(String[] args) {
        // Create and populate
        ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);

        System.out.println("Map: " + scores);
        System.out.println("Size: " + scores.size());

        // Thread-safe operations
        scores.putIfAbsent("Diana", 88);
        scores.replace("Bob", 90);

        System.out.println("After updates: " + scores);

        // Atomic operations
        scores.compute("Alice", (key, value) -> value + 5);
        scores.merge("Charlie", 3, Integer::sum);

        System.out.println("After atomic operations: " + scores);

        // Search
        System.out.println("Contains Alice: " + scores.containsKey("Alice"));
        System.out.println("Contains 95: " + scores.containsValue(95));

        // Iterate
        System.out.println("\nAll scores:");
        scores.forEach((name, score) ->
            System.out.println("  " + name + ": " + score)
        );

        // Multi-threaded access
        System.out.println("\n=== Multi-threaded Test ===");
        ConcurrentHashMap<Integer, Integer> counter = new ConcurrentHashMap<>();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.merge(j, 1, Integer::sum);
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) {}
        }
        System.out.println("Counter size: " + counter.size());
    }
}
```

## 12. Medium Example

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentWordCounter {
    private final ConcurrentHashMap<String, AtomicLong> wordCounts;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicLong>> bigramCounts;

    public ConcurrentWordCounter() {
        this.wordCounts = new ConcurrentHashMap<>();
        this.bigramCounts = new ConcurrentHashMap<>();
    }

    public void countWord(String word) {
        wordCounts.computeIfAbsent(word, k -> new AtomicLong()).incrementAndGet();
    }

    public void countBigram(String word1, String word2) {
        bigramCounts
            .computeIfAbsent(word1, k -> new ConcurrentHashMap<>())
            .computeIfAbsent(word2, k -> new AtomicLong())
            .incrementAndGet();
    }

    public long getWordCount(String word) {
        AtomicLong count = wordCounts.get(word);
        return count != null ? count.get() : 0;
    }

    public Map<String, Long> getTopWords(int n) {
        return wordCounts.entrySet().stream()
            .sorted(Map.Entry.<String, AtomicLong>comparingByValue(
                (a, b) -> Long.compare(b.get(), a.get())
            ).reversed())
            .limit(n)
            .collect(java.util.stream.Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().get()
            ));
    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrentWordCounter counter = new ConcurrentWordCounter();

        String[] texts = {
            "the quick brown fox jumps over the lazy dog",
            "the fox is quick and the dog is lazy",
            "the quick dog jumps over the lazy fox"
        };

        Thread[] threads = new Thread[texts.length];
        for (int i = 0; i < threads.length; i++) {
            final String text = texts[i];
            threads[i] = new Thread(() -> {
                for (String word : text.split("\\s+")) {
                    counter.countWord(word);
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("=== Word Counts ===");
        counter.getTopWords(5).forEach((word, count) ->
            System.out.printf("  %s: %d%n", word, count)
        );
    }
}
```

## 13. Hard Example

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

public class AdvancedConcurrentPatterns {
    public static void main(String[] args) throws InterruptedException {
        // Pattern 1: Thread-safe cache with TTL
        System.out.println("=== TTL Cache ===");
        TTLCache<String, String> cache = new TTLCache<>(1000, 5);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        System.out.println("Get key1: " + cache.get("key1"));
        Thread.sleep(1500);
        System.out.println("Get key1 after TTL: " + cache.get("key1"));

        // Pattern 2: Distributed counter
        System.out.println("\n=== Distributed Counter ===");
        DistributedCounter counter = new DistributedCounter();
        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment("global");
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("Global count: " + counter.get("global"));

        // Pattern 3: Producer-Consumer with ConcurrentHashMap
        System.out.println("\n=== Producer-Consumer ===");
        PCQueue<String> queue = new PCQueue<>();
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                queue.put("item" + i);
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
        });
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                String item = queue.take();
                if (item != null) {
                    System.out.println("Consumed: " + item);
                }
                try { Thread.sleep(150); } catch (InterruptedException e) {}
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // Pattern 4: Read-Write lock pattern
        System.out.println("\n=== Read-Write Pattern ===");
        ReadWriteCache<String, Integer> rwCache = new ReadWriteCache<>();
        rwCache.put("counter", 0);
        Thread[] writers = new Thread[5];
        Thread[] readers = new Thread[5];
        for (int i = 0; i < writers.length; i++) {
            writers[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    rwCache.compute("counter", (k, v) -> v + 1);
                }
            });
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    rwCache.get("counter");
                }
            });
        }
        for (int i = 0; i < writers.length; i++) {
            writers[i].start();
            readers[i].start();
        }
        for (int i = 0; i < writers.length; i++) {
            writers[i].join();
            readers[i].join();
        }
        System.out.println("Final counter: " + rwCache.get("counter"));
    }

    // TTL Cache
    static class TTLCache<K, V> {
        private final ConcurrentHashMap<K, CacheEntry<V>> cache;
        private final long ttlMillis;

        public TTLCache(long ttlMillis, int initialCapacity) {
            this.cache = new ConcurrentHashMap<>(initialCapacity);
            this.ttlMillis = ttlMillis;
        }

        public void put(K key, V value) {
            cache.put(key, new CacheEntry<>(value, System.currentTimeMillis()));
        }

        public V get(K key) {
            CacheEntry<V> entry = cache.get(key);
            if (entry == null) return null;
            if (System.currentTimeMillis() - entry.timestamp > ttlMillis) {
                cache.remove(key);
                return null;
            }
            return entry.value;
        }

        static class CacheEntry<V> {
            final V value;
            final long timestamp;
            CacheEntry(V value, long timestamp) {
                this.value = value;
                this.timestamp = timestamp;
            }
        }
    }

    // Distributed Counter
    static class DistributedCounter {
        private final ConcurrentHashMap<String, Long> counts = new ConcurrentHashMap<>();

        public void increment(String key) {
            counts.merge(key, 1L, Long::sum);
        }

        public long get(String key) {
            return counts.getOrDefault(key, 0L);
        }
    }

    // Producer-Consumer Queue
    static class PCQueue<E> {
        private final ConcurrentHashMap<Long, E> queue = new ConcurrentHashMap<>();
        private final AtomicLong sequence = new AtomicLong(0);
        private final AtomicLong nextTake = new AtomicLong(0);

        public void put(E item) {
            queue.put(sequence.getAndIncrement(), item);
        }

        public E take() {
            long takeIndex = nextTake.get();
            E item = queue.remove(takeIndex);
            if (item != null) {
                nextTake.incrementAndGet();
            }
            return item;
        }
    }

    // Read-Write Cache
    static class ReadWriteCache<K, V> {
        private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();

        public void put(K key, V value) {
            cache.put(key, value);
        }

        public V get(K key) {
            return cache.get(key);
        }

        public void compute(K key, BiFunction<K, V, V> remappingFunction) {
            cache.compute(key, remappingFunction);
        }
    }
}
```

## 14. Enterprise Example

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Date;
import java.util.stream.Collectors;

public class MetricsCollector {
    private final ConcurrentHashMap<String, Metric> metrics;
    private final ConcurrentHashMap<String, AtomicLong> counters;
    private final ConcurrentHashMap<String, DoubleAdder> timers;

    public MetricsCollector() {
        this.metrics = new ConcurrentHashMap<>();
        this.counters = new ConcurrentHashMap<>();
        this.timers = new ConcurrentHashMap<>();
    }

    public void recordCounter(String name, long value) {
        counters.computeIfAbsent(name, k -> new AtomicLong()).addAndGet(value);
    }

    public void recordTimer(String name, double durationMs) {
        timers.computeIfAbsent(name, k -> new DoubleAdder()).add(durationMs);
    }

    public void recordGauge(String name, double value) {
        metrics.put(name, new Metric(name, value, new Date()));
    }

    public long getCounter(String name) {
        AtomicLong counter = counters.get(name);
        return counter != null ? counter.get() : 0;
    }

    public double getTimer(String name) {
        DoubleAdder timer = timers.get(name);
        return timer != null ? timer.sum() : 0;
    }

    public double getGauge(String name) {
        Metric metric = metrics.get(name);
        return metric != null ? metric.value() : 0;
    }

    public Map<String, Long> getAllCounters() {
        return counters.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().get()
            ));
    }

    public Map<String, Double> getAllTimers() {
        return timers.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().sum()
            ));
    }

    public void reset() {
        counters.clear();
        timers.clear();
        metrics.clear();
    }

    public static void main(String[] args) throws InterruptedException {
        MetricsCollector collector = new MetricsCollector();

        // Simulate concurrent metric recording
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    collector.recordCounter("requests", 1);
                    collector.recordTimer("response_time", Math.random() * 100);
                    if (threadId == 0 && j % 100 == 0) {
                        collector.recordGauge("active_users", Math.random() * 100);
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        System.out.println("=== Metrics Summary ===");
        System.out.println("Total requests: " + collector.getCounter("requests"));
        System.out.printf("Total response time: %.2f ms%n",
            collector.getTimer("response_time"));
        System.out.printf("Average response time: %.2f ms%n",
            collector.getTimer("response_time") / collector.getCounter("requests"));
        System.out.printf("Active users: %.0f%n",
            collector.getGauge("active_users"));
    }

    record Metric(String name, double value, Date timestamp) {}

    // DoubleAdder for thread-safe double accumulation
    static class DoubleAdder {
        private final AtomicLong bits = new AtomicLong();

        public void add(double x) {
            long next, current;
            do {
                current = bits.get();
                next = Double.doubleToLongBits(
                    Double.longBitsToDouble(current) + x
                );
            } while (!bits.compareAndSet(current, next));
        }

        public double sum() {
            return Double.longBitsToDouble(bits.get());
        }
    }
}
```

## 15. Performance

### Time Complexity

| Operation | Average | Worst Case | Notes |
|-----------|---------|------------|-------|
| put() | O(1) | O(n) | With treeification |
| get() | O(1) | O(n) | Non-blocking |
| remove() | O(1) | O(n) | CAS or synchronized |
| containsKey() | O(1) | O(n) | Same as get() |
| containsValue() | O(n) | O(n) | Must scan all |
| size() | O(1) | O(1) | Approximate |
| iteration | O(n) | O(n) | Weakly consistent |

### ConcurrentHashMap vs Collections.synchronizedMap()

| Feature | ConcurrentHashMap | synchronizedMap |
|---------|-------------------|-----------------|
| Locking | Bucket-level | Method-level |
| Read performance | O(1) non-blocking | O(1) synchronized |
| Write performance | O(1) CAS/sync | O(1) synchronized |
| Thread safety | Fine-grained | Coarse-grained |
| Null keys | Not allowed | One allowed |
| Iterator | Weakly consistent | Fail-fast |
| Compound operations | Atomic | Not atomic |

### Throughput Comparison

Under high contention (100 threads, 10000 operations each):
- ConcurrentHashMap: ~10x faster than synchronizedMap
- ConcurrentHashMap scales with CPU cores
- synchronizedMap bottlenecks on single lock

### Memory Usage

| Collection | Per Entry Overhead | Notes |
|------------|-------------------|-------|
| HashMap | ~32 bytes | Node object |
| ConcurrentHashMap | ~40 bytes | Node + hash + next |
| synchronizedMap | ~40 bytes | Wrapper + synchronized |

## 16. Best Practices

1. **Use atomic operations**: `compute()`, `merge()`, `putIfAbsent()` instead of manual synchronization
2. **Set initial capacity**: Avoid resizing for known sizes
3. **Use mappingCount()**: Better than size() for large maps
4. **Avoid containsValue()**: O(n), use keySet() + get() if needed
5. **Use forEach with parallelism threshold**: For parallel processing
6. **Don't use null keys/values**: ConcurrentHashMap doesn't allow them
7. **Prefer computeIfAbsent**: For lazy initialization patterns
8. **Use ConcurrentHashMap.newKeySet()**: For Set operations

## 17. Common Mistakes

```java
// Mistake 1: Using null keys or values
map.put(null, "value"); // NullPointerException!
map.put("key", null);   // NullPointerException!

// Mistake 2: Using size() for large maps
int size = map.size(); // Approximate, may be stale
long exactSize = map.mappingCount(); // Better for large maps

// Mistake 3: Checking size then acting
if (map.size() > 0) {
    map.clear(); // Another thread may have modified!
}

// Mistake 4: Using iteration for compound operations
// Bad - not atomic
for (Map.Entry<K, V> entry : map.entrySet()) {
    if (entry.getValue() > 100) {
        map.remove(entry.getKey()); // May skip entries
    }
}

// Good - atomic
map.entrySet().removeIf(e -> e.getValue() > 100);

// Mistake 5: Assuming atomicity of multiple operations
// Bad - not atomic
if (!map.containsKey(key)) {
    map.put(key, value); // Another thread may put first!
}

// Good - atomic
map.putIfAbsent(key, value);
```

## 18. Pitfalls

### NullPointerException
ConcurrentHashMap does NOT allow null keys or values. This is by design to avoid ambiguity in concurrent contexts (is the null a missing key or a stored null value?).

### Weakly Consistent Iterators
Iterators may not reflect the current state of the map. Elements added during iteration may or may not be seen. This is a tradeoff for lock-free iteration.

### Approximate Size
`size()` returns an approximate value. Use `mappingCount()` for large maps or iterate to get exact count.

### Memory Overhead
ConcurrentHashMap has more overhead than HashMap due to volatile fields and CAS operations. Use HashMap when thread safety isn't needed.

### Thread Starvation
Under extreme contention, some threads may starve. Consider using `ParallelArray` or `ForkJoinPool` for parallel operations.

## 19. Debugging Tips

1. **Use JConsole/VisualVM**: Monitor concurrent access and contention
2. **Check for null keys/values**: ConcurrentHashMap rejects them
3. **Use WeakReference**: For debugging without affecting garbage collection
4. **Enable GC logging**: Monitor memory usage
5. **Use Thread dumps**: Identify blocked threads
6. **Profile contention**: Use profilers to identify hotspots
7. **Test with multiple threads**: Use `ExecutorService` for concurrent testing

## 20. Comparison Table

| Feature | HashMap | Hashtable | ConcurrentHashMap | synchronizedMap |
|---------|---------|-----------|-------------------|-----------------|
| Thread-safe | No | Yes | Yes | Yes |
| Locking | None | All methods | Bucket-level | All methods |
| Null keys | One | None | None | One |
| Performance | Best | Worst | Good | Poor |
| Iterator | fail-fast | fail-fast | weakly consistent | fail-fast |
| Atomic ops | No | No | Yes | No |

## 21. Decision Tree

```
Need a Map?
├── Yes → Need thread safety?
│   ├── Yes → Need high concurrency?
│   │   ├── Yes → ConcurrentHashMap
│   │   └── No → Collections.synchronizedMap()
│   └── No → Use HashMap
├── Need null keys?
│   ├── Yes → Use HashMap (not ConcurrentHashMap)
│   └── No → Use ConcurrentHashMap
└── Need sorted keys?
    ├── Yes → Use TreeMap + Collections.synchronizedSortedMap()
    └── No → Use ConcurrentHashMap
```

## 22. Interview Questions

### Q1: What is the difference between ConcurrentHashMap and Collections.synchronizedMap()?
**A**: ConcurrentHashMap uses bucket-level locking (CAS + synchronized on individual buckets), allowing concurrent reads and fine-grained writes. synchronizedMap synchronizes all methods on a single lock, causing all operations to block each other.

### Q2: Does ConcurrentHashMap allow null keys?
**A**: No. ConcurrentHashMap does not allow null keys or null values. This is by design to avoid ambiguity in concurrent contexts.

### Q3: What is a weakly consistent iterator?
**A**: An iterator that reflects the state of the map at some point since creation. It may or may not reflect modifications made after creation, but never throws ConcurrentModificationException.

### Q4: How does ConcurrentHashMap achieve thread safety?
**A**: Java 8+ uses CAS for empty buckets and synchronized on the first node for occupied buckets. This provides fine-grained locking where only the affected bucket is locked, not the entire map.

### Q5: What is the difference between compute() and merge()?
**A**: compute() takes a function that receives key and current value (may be null). merge() takes a value and a remapping function that combines old and new values. merge is better for accumulative operations.

### Q6: When would you use Collections.synchronizedMap() over ConcurrentHashMap?
**A**: Rarely. synchronizedMap is only useful when you need null keys/values or when you're wrapping an existing Map. ConcurrentHashMap is better for almost all concurrent use cases.

### Q7: How do you iterate over ConcurrentHashMap safely?
**A**: Use forEach() method or create an iterator. Both are weakly consistent and never throw ConcurrentModificationException. For bulk operations, use forEachKey(), forEachValue(), or forEachEntry() with parallelism threshold.

## 23. Exercises

### Exercise 1: Thread-Safe Counter
Implement a thread-safe counter using ConcurrentHashMap that supports:
- increment(key)
- decrement(key)
- get(key)
- reset(key)

### Exercise 2: Concurrent Cache
Build a cache using ConcurrentHashMap with:
- TTL (time-to-live) for entries
- Maximum size limit
- LRU eviction
- Hit/miss statistics

### Exercise 3: Parallel Word Count
Count word frequencies in a large text using:
- ConcurrentHashMap with atomic operations
- Parallel stream with collectors
- Compare performance

### Exercise 4: Producer-Consumer
Implement a producer-consumer pattern using:
- ConcurrentHashMap as the queue
- AtomicLong for sequence numbers
- Multiple producers and consumers

## 24. Assignments

### Assignment 1: URL Shortener
Build a URL shortener using ConcurrentHashMap:
- Generate short codes
- Map short codes to URLs
- Track click counts
- Handle concurrent requests

### Assignment 2: Session Manager
Create a session management system:
- Create/invalidate sessions
- Track active sessions per user
- Implement session expiration
- Handle concurrent session operations

### Assignment 3: Metrics System
Build a metrics collection system:
- Record counters, gauges, and timers
- Thread-safe metric updates
- Aggregate metrics by time window
- Export metrics to monitoring system

## 25. Mini Project

### Real-Time Analytics Dashboard

Build a real-time analytics system using ConcurrentHashMap:

```java
// Features:
// 1. Record events from multiple threads
// 2. Aggregate metrics in real-time
// 3. Track unique visitors
// 4. Calculate percentiles
// 5. Export metrics to dashboard
// 6. Handle high throughput
```

**Requirements:**
- Use ConcurrentHashMap for all data structures
- Use atomic operations for counters
- Support concurrent event recording
- Handle metric expiration
- Export to monitoring system

## 26. Summary

ConcurrentHashMap is the thread-safe Map implementation for high concurrency:

- **Thread safety**: Fine-grained locking (bucket-level)
- **Performance**: Much better than synchronizedMap under contention
- **Null policy**: No null keys or values allowed
- **Iterators**: Weakly consistent (never throw ConcurrentModificationException)
- **Atomic operations**: compute, merge, computeIfAbsent for thread-safe updates
- **Best for**: High-concurrency scenarios, caches, counters, shared state

## 27. References

### Official Documentation
- [ConcurrentHashMap JavaDoc](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/concurrent/ConcurrentHashMap.html)
- [Java Concurrency Tutorial](https://docs.oracle.com/en/java/javase/21/essential/concurrency/)

### Books
- *Java Concurrency in Practice* by Brian Goetz
- *Effective Java* by Joshua Bloch

### Online Resources
- [Baeldung ConcurrentHashMap Guide](https://www.baeldung.com/java-concurrent-concurrenthashmap)
- [OpenJDK ConcurrentHashMap Source](https://hg.openjdk.java.net/jdk8/jdk8/jdk/file/tip/src/share/classes/java/util/concurrent/ConcurrentHashMap.java)

### Related Topics
- [HashMap](../15-hashmap/README.md)
- [Hashtable](../23-hashtable/README.md)
- [Fail-Fast vs Fail-Safe](../26-fail-fast-vs-fail-safe/README.md)
