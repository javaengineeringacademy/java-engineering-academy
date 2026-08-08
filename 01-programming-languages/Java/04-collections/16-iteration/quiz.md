# Iteration Methods Quiz

## 1. What's the difference between `for` loop and enhanced `for` loop?

**Answer:**

The traditional `for` loop provides explicit control over the loop variable, index, and step, while the enhanced `for` loop (for-each) provides a simpler, more readable syntax for iterating over collections and arrays.

**Traditional For Loop:**
- Access to index variable
- Can iterate in reverse
- Can skip elements or use custom step sizes
- More verbose

**Enhanced For Loop:**
- Cleaner, more readable syntax
- No index access (cannot modify array length during iteration)
- Cannot iterate backwards
- Only forward iteration

```java
// Traditional for loop - full control
int[] numbers = {1, 2, 3, 4, 5};
for (int i = 0; i < numbers.length; i++) {
    System.out.println("Index " + i + ": " + numbers[i]);
}

// Enhanced for loop - simpler syntax
for (int num : numbers) {
    System.out.println(num);
}

// Enhanced for cannot do this:
for (int i = numbers.length - 1; i >= 0; i--) {
    System.out.println(numbers[i]); // Reverse iteration
}
```

**When to use which:**
- Use enhanced `for` when you just need to read elements
- Use traditional `for` when you need the index or need to modify the collection

---

## 2. When would you use `while` loop over `for` loop?

**Answer:**

Use a `while` loop when:
1. The number of iterations is unknown beforehand
2. You're waiting for a condition to change (sentinel values, user input)
3. You want to implement algorithms like binary search
4. You need to process until a condition is met

```java
// Unknown iterations - read until sentinel
Scanner scanner = new Scanner(System.in);
int sum = 0;
while (scanner.hasNextInt()) {
    int num = scanner.nextInt();
    if (num == -1) break;
    sum += num;
}

// Binary search - condition-based
int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;
        if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}

// Processing until complete
while (!queue.isEmpty()) {
    process(queue.poll());
}
```

**Key difference:** `for` loops are counter-controlled, `while` loops are condition-controlled.

---

## 3. What is `ConcurrentModificationException` and how does Iterator handle it?

**Answer:**

`ConcurrentModificationException` is thrown when a collection is modified structurally (add/remove elements) while being iterated, except through the iterator's own methods.

**The Problem:**
```java
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

// This throws ConcurrentModificationException!
for (Integer num : list) {
    if (num % 2 == 0) {
        list.remove(num); // Direct modification!
    }
}
```

**Iterator's Solution:**
```java
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
Iterator<Integer> iterator = list.iterator();

while (iterator.hasNext()) {
    Integer num = iterator.next();
    if (num % 2 == 0) {
        iterator.remove(); // Safe removal - tracks modification count
    }
}

// Or use removeIf (Java 8+)
list.removeIf(num -> num % 2 == 0);
```

**How Iterator prevents this:**
1. Iterator maintains a `modCount` (modification count)
2. Before each operation, it checks if `modCount` matches expected value
3. If collection was modified externally, `modCount` changes and exception is thrown
4. Using `iterator.remove()` keeps `modCount` in sync

---

## 4. What's the difference between Iterator and ListIterator?

**Answer:**

| Feature | Iterator | ListIterator |
|---------|----------|--------------|
| Direction | Forward only | Bidirectional |
| Used with | Any Collection | List only |
| Methods | `hasNext()`, `next()`, `remove()` | All Iterator methods + `hasPrevious()`, `previous()`, `add()`, `set()`, `nextIndex()`, `previousIndex()` |
| Modification | Only remove | Add, set, remove |

```java
// Iterator - forward only
List<Integer> list = Arrays.asList(1, 2, 3);
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}

// ListIterator - bidirectional
ListIterator<Integer> listIt = list.listIterator();
while (listIt.hasNext()) {
    System.out.println(listIt.next());
}
// Can go backwards
while (listIt.hasPrevious()) {
    System.out.println(listIt.previous());
}

// ListIterator can also add and set
listIt.add(4);        // Insert at current position
listIt.set(5);        // Replace last returned element

// ListIterator can get index
int nextIdx = listIt.nextIndex();
int prevIdx = listIt.previousIndex();
```

**Use ListIterator when:**
- You need to traverse backwards
- You need to insert elements during iteration
- You need to replace elements during iteration
- You need position information

---

## 5. Why is Enumeration considered legacy?

**Answer:**

`Enumeration` is considered legacy because:

1. **Limited functionality** - Only `hasMoreElements()` and `nextElement()` methods
2. **No remove operation** - Cannot remove elements during iteration
3. **No bidirectional traversal** - Forward only
4. **No generics support** - Returns `Object` type (pre-generics)
5. **Only works with legacy classes** - `Vector`, `Hashtable`, `Stack`

```java
// Legacy Enumeration (old style)
Vector<String> vector = new Vector<>();
vector.add("A");
vector.add("B");
vector.add("C");

Enumeration<String> enum = vector.elements();
while (enum.hasMoreElements()) {
    System.out.println(enum.nextElement());
}

// Modern Iterator (preferred)
List<String> list = new ArrayList<>();
list.add("A");
list.add("B");
list.add("C");

Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("B")) {
        it.remove(); // Can remove!
    }
}

// Conversion from Enumeration to modern API
List<String> modernList = Collections.list(enumeration);
```

**Use modern alternatives:**
- `ArrayList` instead of `Vector`
- `HashMap` instead of `Hashtable`
- `Iterator` or `ListIterator` instead of `Enumeration`

---

## 6. What is Spliterator and when would you use it?

**Answer:**

`Spliterator` (Splitable Iterator) is designed for parallel traversal and splitting of data sources. It's the foundation for parallel streams.

**Key Features:**
- `trySplit()` - Divide into two parts for parallel processing
- `tryAdvance()` - Process next element
- `estimateSize()` - Get estimated remaining elements
- `characteristics()` - Hints about the data source

```java
// Basic Spliterator usage
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
Spliterator<Integer> spliterator = list.spliterator();

// Process elements
spliterator.forEachRemaining(System.out::println);

// Split for parallel processing
Spliterator<Integer> split1 = spliterator.trySplit();
Spliterator<Integer> split2 = spliterator.trySplit();

// Use in parallel stream
list.parallelStream()
    .filter(x -> x % 2 == 0)
    .forEach(System.out::println);

// Custom Spliterator for range
Spliterator<Integer> rangeSpl = new Spliterator<Integer>() {
    private int current = 1;
    private int end = 10;
    
    @Override
    public boolean tryAdvance(Consumer<? super Integer> action) {
        if (current < end) {
            action.accept(current++);
            return true;
        }
        return false;
    }
    
    @Override
    public Spliterator<Integer> trySplit() {
        int mid = (current + end) / 2;
        if (mid <= current) return null;
        int oldEnd = end;
        end = mid;
        return new RangeSpliterator(current, mid);
    }
    
    @Override
    public long estimateSize() {
        return (long) end - current;
    }
    
    @Override
    public int characteristics() {
        return ORDERED | SIZED | IMMUTABLE | NONNULL;
    }
};
```

**When to use:**
- Implementing custom parallel data sources
- Creating custom streams
- Optimizing parallel processing of large datasets
- When you need fine-grained control over parallel iteration

---

## 7. What is the difference between internal and external iteration?

**Answer:**

**External Iteration (Traditional):**
- You control the iteration loop
- Code is explicit about how traversal happens
- Manual management of loop variable/index

```java
// External iteration
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

// You control the loop
for (int i = 0; i < list.size(); i++) {
    System.out.println(list.get(i));
}

// Or with Iterator
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}
```

**Internal Iteration (Streams/Functional):**
- Library controls the iteration
- You provide what to do with each element
- Easier to parallelize

```java
// Internal iteration
list.forEach(System.out::println);

// Stream operations
list.stream()
    .filter(x -> x > 2)
    .map(x -> x * 2)
    .forEach(System.out::println);

// Parallel internal iteration
list.parallelStream()
    .filter(x -> x > 2)
    .forEach(System.out::println);
```

**Comparison:**

| Aspect | External | Internal |
|--------|----------|----------|
| Control | Programmer | Library |
| Parallelism | Manual | Automatic |
| Code Style | Imperative | Declarative |
| Performance | Predictable | Optimized by JVM |
| Use Case | Simple loops | Complex transformations |

**Example showing the difference:**
```java
// External - you manage the state
List<Integer> result = new ArrayList<>();
for (Integer num : list) {
    if (num % 2 == 0) {
        result.add(num * 2);
    }
}

// Internal - library manages the state
List<Integer> result = list.stream()
    .filter(num -> num % 2 == 0)
    .map(num -> num * 2)
    .collect(Collectors.toList());
```

---

## 8. How does parallel stream work internally?

**Answer:**

Parallel streams use the `ForkJoinPool` to split work across multiple threads.

**How it works:**
1. Source is split using `Spliterator.trySplit()`
2. Each partition processed independently
3. Results combined

```java
// Basic parallel stream
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

list.parallelStream()
    .filter(x -> x % 2 == 0)
    .map(x -> x * x)
    .forEach(x -> System.out.println(x + " on thread " + Thread.currentThread().getName()));

// Custom ForkJoinPool
ForkJoinPool customPool = new ForkJoinPool(4);
customPool.submit(() -> {
    list.parallelStream()
        .filter(x -> x > 5)
        .forEach(System.out::println);
}).get();

// Parallel operations
int sum = list.parallelStream()
    .mapToInt(Integer::intValue)
    .sum();

// Parallel collect
Map<Boolean, List<Integer>> partitioned = list.parallelStream()
    .collect(Collectors.partitioningBy(x -> x % 2 == 0));
```

**Characteristics:**
- Uses `Spliterator` to divide work
- Work-stealing algorithm for load balancing
- Default pool size = number of CPU cores
- Can cause thread contention if not careful

**When to use parallel streams:**
- Large datasets (thousands+ elements)
- CPU-intensive operations
- Independent elements (no shared state)

**When to avoid:**
- Small datasets (overhead > benefit)
- I/O operations
- Operations requiring order
- Shared mutable state

---

## 9. Which iteration method is fastest for ArrayList?

**Answer:**

For `ArrayList`, the fastest iteration methods in order:

1. **Primitive for loop** - Fastest (direct index access)
2. **Enhanced for loop** - Very fast (uses Iterator internally)
3. **Iterator** - Fast
4. **Stream sequential** - Slightly slower
5. **Stream parallel** - Slowest for small lists

```java
// Benchmarking example
List<Integer> list = new ArrayList<>();
for (int i = 0; i < 1000000; i++) {
    list.add(i);
}

// 1. Primitive for loop - FASTEST
long start = System.nanoTime();
int sum1 = 0;
for (int i = 0; i < list.size(); i++) {
    sum1 += list.get(i);
}
System.out.println("For loop: " + (System.nanoTime() - start) + " ns");

// 2. Enhanced for loop - VERY FAST
start = System.nanoTime();
int sum2 = 0;
for (int num : list) {
    sum2 += num;
}
System.out.println("Enhanced for: " + (System.nanoTime() - start) + " ns");

// 3. Iterator - FAST
start = System.nanoTime();
int sum3 = 0;
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    sum3 += it.next();
}
System.out.println("Iterator: " + (System.nanoTime() - start) + " ns");

// 4. Stream sequential - SLIGHTLY SLOWER
start = System.nanoTime();
int sum4 = list.stream().mapToInt(Integer::intValue).sum();
System.out.println("Stream: " + (System.nanoTime() - start) + " ns");

// 5. Stream parallel - SLOWEST FOR SMALL LISTS
start = System.nanoTime();
int sum5 = list.parallelStream().mapToInt(Integer::intValue).sum();
System.out.println("Parallel: " + (System.nanoTime() - start) + " ns");
```

**Why primitive for is fastest:**
- Direct array access (no iterator overhead)
- No bounds checking per iteration
- Compiler can optimize better

**Why enhanced for is close:**
- Uses Iterator under the hood
- But JIT compiler optimizes it well

**Key insight:** For large datasets, parallel streams can be faster, but for `ArrayList` specifically, the difference is often negligible due to cache locality.

---

## 10. Write code to safely remove elements during iteration

**Answer:**

Here are the safe ways to remove elements during iteration:

```java
// METHOD 1: Using Iterator.remove() - SAFE
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
Iterator<Integer> iterator = list.iterator();

while (iterator.hasNext()) {
    Integer num = iterator.next();
    if (num % 2 == 0) { // Remove even numbers
        iterator.remove(); // Safe removal
    }
}
System.out.println("After Iterator.remove(): " + list); // [1, 3, 5, 7, 9]

// METHOD 2: Using ListIterator - SAFE (for List)
List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
ListIterator<Integer> listIterator = list2.listIterator();

while (listIterator.hasNext()) {
    Integer num = listIterator.next();
    if (num > 3) {
        listIterator.remove(); // Safe removal
    }
}
System.out.println("After ListIterator.remove(): " + list2); // [1, 2, 3]

// METHOD 3: Using removeIf() - EASIEST (Java 8+)
List<Integer> list3 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
list3.removeIf(num -> num % 2 == 0); // Remove all even numbers
System.out.println("After removeIf(): " + list3); // [1, 3, 5, 7, 9]

// METHOD 4: Using Stream filter - SAFE (creates new list)
List<Integer> list4 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
List<Integer> filtered = list4.stream()
    .filter(num -> num % 2 != 0) // Keep odd numbers
    .collect(Collectors.toList());
System.out.println("Filtered: " + filtered); // [1, 3, 5, 7, 9]

// METHOD 5: Reverse iteration - SAFE (for indexed removal)
List<String> list5 = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
for (int i = list5.size() - 1; i >= 0; i--) {
    if (list5.get(i).equals("C")) {
        list5.remove(i); // Safe because we're going backwards
    }
}
System.out.println("After reverse removal: " + list5); // [A, B, D, E]

// METHOD 6: CopyOnWriteArrayList - SAFE (thread-safe)
CopyOnWriteArrayList<String> threadSafeList = new CopyOnWriteArrayList<>(
    Arrays.asList("A", "B", "C", "D", "E")
);
for (String s : threadSafeList) {
    if (s.equals("C")) {
        threadSafeList.remove(s); // Safe - iterator uses snapshot
    }
}
System.out.println("CopyOnWriteArrayList: " + threadSafeList); // [A, B, D, E]
```

**Important Notes:**
1. **Never use** `list.remove()` directly during enhanced for loop
2. **Iterator.remove()** is the classic safe approach
3. **removeIf()** is the modern, most readable approach
4. **CopyOnWriteArrayList** is thread-safe but has performance overhead
5. **Reverse iteration** works when you know the exact indices to remove

**Performance comparison:**
- `removeIf()` - O(n) single pass, most efficient
- `Iterator.remove()` - O(n) but more verbose
- `Stream.filter()` - Creates new list, uses more memory
- `CopyOnWriteArrayList` - Safe for concurrency, expensive for modifications
