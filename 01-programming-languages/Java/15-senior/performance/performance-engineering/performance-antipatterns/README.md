# Performance Anti-Patterns

## String Concatenation in Loops

**The Problem**: Creating a new String object on every concatenation, resulting in O(n²) memory allocation:

```java
// BAD: O(n²) - creates n intermediate String objects
String result = "";
for (int i = 0; i < 10000; i++) {
    result += i;  // creates new String each iteration
}
```

**The Fix**: Use StringBuilder or String.join:

```java
// GOOD: O(n) - single buffer, append operations
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

**Why It Matters**: Each `+=` creates a new char array, copies old content, copies new content, and discards the old String. For 10,000 iterations, this allocates ~50MB instead of ~80KB.

## Autoboxing in Tight Loops

**The Problem**: Implicit boxing/unboxing creating millions of temporary wrapper objects:

```java
// BAD: Boxing on every iteration
Long sum = 0L;
for (long i = 0; i < 1_000_000; i++) {
    sum += i;  // unbox sum, add, box result
}
```

**The Fix**: Use primitives for loop variables and accumulators:

```java
// GOOD: No boxing
long sum = 0L;
for (long i = 0; i < 1_000_000; i++) {
    sum += i;  // primitive addition only
}
```

**Impact**: Autoboxing can add 5-10x overhead in tight loops due to object creation and GC pressure.

## Unnecessary Object Creation

**The Problem**: Creating objects that could be reused or avoided:

```java
// BAD: Creates new formatter on every call
public String formatPrice(double price) {
    return new DecimalFormat("#,##0.00").format(price);
}

// BAD: Creates new pattern on every call
public boolean isEmail(String email) {
    return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
}
```

**The Fix**: Reuse expensive objects:

```java
// GOOD: Single formatter instance
private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,##0.00");

public String formatPrice(double price) {
    return PRICE_FORMAT.format(price);
}

// GOOD: Pre-compiled Pattern
private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

public boolean isEmail(String email) {
    return EMAIL_PATTERN.matcher(email).matches();
}
```

## Premature Optimization

**The Problem**: Optimizing code before measuring, often making it less readable:

```java
// BAD: "Optimized" but less readable, may not even be faster
public int find(int[] arr, int target) {
    int i = 0, len = arr.length;
    while (i < len) {
        if (arr[i] == target) return i;
        i += 4;  // "optimize" by skipping elements?
    }
    return -1;
}
```

**The Fix**: Write clear code first, profile, then optimize:

```java
// GOOD: Clear intent, optimize only if profiling shows it's needed
public int find(int[] arr, int target) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == target) return i;
    }
    return -1;
}
```

**Guideline**: Follow Knuth's principle — "Premature optimization is the root of all evil." Profile first, optimize second.

## N+1 Queries

**The Problem**: Executing one query for the parent and N queries for each child:

```java
// BAD: 1 + N queries
List<Order> orders = orderRepository.findAll();
for (Order order : orders) {
    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
    order.setItems(items);  // N additional queries
}
```

**The Fix**: Use JOINs or batch fetching:

```java
// GOOD: Single query with JOIN
@Query("SELECT o FROM Order o JOIN FETCH o.items")
List<Order> findAllWithItems();

// GOOD: Batch fetching
@OneToMany(fetch = FetchType.LAZY)
@BatchSize(size = 50)
private List<OrderItem> items;
```

**Impact**: If you have 100 orders, N+1 executes 101 queries. JOIN fetching executes 1 query.

## Missing Indexes

**The Problem**: Queries without proper indexes cause full table scans:

```sql
-- BAD: No index on queried column
SELECT * FROM orders WHERE customer_id = 123;

-- BAD: Composite query without composite index
SELECT * FROM orders WHERE customer_id = 123 AND status = 'shipped';
```

**The Fix**: Add appropriate indexes:

```sql
-- GOOD: Single column index
CREATE INDEX idx_orders_customer ON orders(customer_id);

-- GOOD: Composite index (column order matters)
CREATE INDEX idx_orders_customer_status ON orders(customer_id, status);
```

**Index Design Rules**:
1. Index columns used in WHERE clauses
2. Composite index: put high-selectivity columns first
3. Covering indexes include all SELECT columns
4. Don't over-index: slows down writes

## Large Object Allocations

**The Problem**: Allocating objects larger than available TLAB (Thread-Local Allocation Buffer):

```java
// BAD: Allocates large array directly in old generation
byte[] buffer = new byte[10 * 1024 * 1024]; // 10MB

// BAD: Creating huge collections upfront
List<Record> allRecords = new ArrayList<>(1_000_000);
```

**The Fix**: Use streaming or chunked processing:

```java
// GOOD: Stream processing, no large intermediate collection
try (Stream<String> lines = Files.lines(path)) {
    lines.filter(line -> line.contains("ERROR"))
         .forEach(this::processErrorLine);
}

// GOOD: Process in batches
try (Stream<Record> stream = repository.streamAll()) {
    stream.collect(Collectors.groupingBy(
        r -> r.getId() % 100,
        Collectors.toList()
    )).forEach((batchId, batch) -> processBatch(batch));
}
```

**Why It Matters**: Large allocations go directly to old generation, causing Full GC pauses. TLAB allocations are much faster and generate less GC pressure.

## Thread Contention

**The Problem**: Multiple threads competing for the same lock:

```java
// BAD: All threads serialize on single lock
public class SharedCounter {
    private int count = 0;

    public synchronized void increment() {
        count++;  // Only one thread can execute at a time
    }
}
```

**The Fix**: Reduce contention with better synchronization:

```java
// GOOD: AtomicInteger for simple counters
public class SharedCounter {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();  // Lock-free
    }
}

// GOOD: Striped locks for partitioned data
public class StripedCache {
    private final Striped<Lock> locks = Striped.lock(16);

    public void put(String key, Object value) {
        Lock lock = locks.get(key);
        lock.lock();
        try {
            map.put(key, value);
        } finally {
            lock.unlock();
        }
    }
}
```

**Impact**: Thread contention can reduce throughput by 10-100x. Lock-free structures like `ConcurrentHashMap` and `AtomicInteger` eliminate contention entirely for common patterns.

## Summary Table

| Anti-Pattern | Impact | Fix |
|--------------|--------|-----|
| String concat in loops | O(n²) allocation | StringBuilder |
| Autoboxing in loops | 5-10x overhead | Use primitives |
| Unnecessary object creation | GC pressure | Reuse/restructure |
| Premature optimization | Unreadable code | Profile first |
| N+1 queries | N extra DB round trips | JOINs, batch fetch |
| Missing indexes | Full table scans | Add indexes |
| Large object allocations | Full GC pauses | Stream/chunk processing |
| Thread contention | Serialized execution | Lock-free, striped locks |
