# Standard Template Library (STL) — C++

## Why It Matters

Every C++ program needs data structures and algorithms. When developers reinvent linked lists, hash maps, and sorting routines, they introduce subtle bugs, inconsistent APIs, and wasted time. The STL provides production-tested, type-safe, zero-overhead containers and algorithms optimized across 40+ years of real-world use.

## What It Is

The STL is C++'s standard library of generic containers, iterators, algorithms, and function objects, providing type-safe, efficient, and reusable implementations of common data structures and operations.

## Architecture: How the STL Fits Together

```
┌─────────────────────────────────────────────────────────────┐
│                      C++ STL Architecture                    │
├─────────────────┬─────────────────┬─────────────────────────┤
│   Containers    │   Algorithms    │     Iterators           │
│ (vector, map,   │ (sort, find,    │ (begin, end, advance)   │
│  set, deque,    │  transform,     │                         │
│  unordered_map) │  accumulate)    │                         │
├─────────────────┴─────────────────┴─────────────────────────┤
│              Function Objects & Lambdas                      │
├─────────────────────────────────────────────────────────────┤
│                   Adapters & Allocators                      │
│  (stack, queue, priority_queue, pmr::allocators)            │
└─────────────────────────────────────────────────────────────┘
```

## Containers

### The Problem Containers Solve
Programs need to store collections of data. Raw arrays are fixed-size and type-unsafe. Containers provide dynamic sizing, bounds checking, and iteration support.

### Sequential Containers

```cpp
#include <vector>
#include <deque>
#include <list>
#include <array>
#include <forward_list>

// vector — dynamic array, O(1) random access, O(n) insert in middle
std::vector<int> vec = {1, 2, 3, 4, 5};
vec.push_back(6);           // O(1) amortized
vec[2];                     // O(1) access
vec.reserve(100);           // Pre-allocate to avoid reallocation

// deque — double-ended queue, O(1) insert at both ends
std::deque<int> deq = {1, 2, 3};
deq.push_front(0);          // O(1)
deq.push_back(4);           // O(1)

// list — doubly-linked list, O(1) insert/delete anywhere
std::list<int> lst = {1, 2, 3};
lst.push_front(0);          // O(1)
lst.erase(lst.begin());     // O(1)

// array — fixed-size array (C++11)
std::array<int, 5> arr = {1, 2, 3, 4, 5};
arr.size();                 // Always 5

// forward_list — singly-linked list
std::forward_list<int> fwd = {1, 2, 3};
```

### Associative Containers

```cpp
#include <map>
#include <set>
#include <unordered_map>
#include <unordered_set>

// map — ordered (red-black tree), O(log n) operations
std::map<std::string, int> ages = {{"Alice", 30}, {"Bob", 25}};
ages["Charlie"] = 35;       // Insert or assign
ages.at("Alice");           // Throws if missing
ages.count("Bob");          // 1 if exists, 0 if not

// set — ordered unique elements
std::set<int> unique_nums = {3, 1, 4, 1, 5};  // {1, 3, 4, 5}

// unordered_map — hash table, O(1) average, O(n) worst
std::unordered_map<std::string, int> fast_lookup;
fast_lookup.reserve(10000);  // Pre-allocate buckets

// unordered_set — hash set
std::unordered_set<int> fast_set;
```

### Container Adapters

```cpp
#include <stack>
#include <queue>

// stack — LIFO
std::stack<int> stk;
stk.push(1);
stk.top();   // 1
stk.pop();

// queue — FIFO
std::queue<int> q;
q.push(1);
q.front();   // 1
q.pop();

// priority_queue — max-heap by default
std::priority_queue<int> pq;
pq.push(3);
pq.push(1);
pq.top();    // 3 (largest)
```

## Iterators

### The Problem Iterators Solve
Iterators provide a uniform interface to traverse different containers. Algorithms work with iterators, not containers, enabling generic programming.

```cpp
std::vector<int> vec = {1, 2, 3, 4, 5};

// Iterator types
auto it = vec.begin();       // Random access iterator
auto it_end = vec.end();

// Traversal
for (auto it = vec.begin(); it != vec.end(); ++it) {
    std::cout << *it << " ";
}

// Range-based for (uses begin/end internally)
for (const auto& elem : vec) {
    std::cout << elem << " ";
}

// Iterator categories
// InputIterator → ForwardIterator → BidirectionalIterator → RandomAccessIterator → ContiguousIterator
// vector: RandomAccessIterator
// list: BidirectionalIterator
// forward_list: ForwardIterator
// deque: RandomAccessIterator
```

## Algorithms

### The Problem Algorithms Solve
Instead of writing manual loops, STL algorithms express intent clearly and are optimized by the standard library implementation.

```cpp
#include <algorithm>
#include <numeric>
#include <functional>

std::vector<int> vec = {5, 3, 1, 4, 2};

// Sorting
std::sort(vec.begin(), vec.end());              // {1, 2, 3, 4, 5}
std::sort(vec.begin(), vec.end(), std::greater<int>()); // {5, 4, 3, 2, 1}

// Searching
auto it = std::find(vec.begin(), vec.end(), 3);
if (it != vec.end()) std::cout << "Found: " << *it << "\n";

bool found = std::binary_search(vec.begin(), vec.end(), 3);  // Requires sorted

// Transform
std::vector<int> doubled(vec.size());
std::transform(vec.begin(), vec.end(), doubled.begin(),
               [](int x) { return x * 2; });

// Accumulate
int sum = std::accumulate(vec.begin(), vec.end(), 0);

// Count
int count = std::count_if(vec.begin(), vec.end(),
                          [](int x) { return x > 2; });

// Remove-erase idiom
vec.erase(std::remove(vec.begin(), vec.end(), 3), vec.end());

// Lambda with capture
int threshold = 3;
auto above = std::count_if(vec.begin(), vec.end(),
                           [threshold](int x) { return x > threshold; });
```

## Performance Characteristics

| Container | Access | Search | Insert (end) | Insert (mid) | Memory |
|-----------|--------|--------|--------------|--------------|--------|
| vector | O(1) | O(n) | O(1) amortized | O(n) | Low |
| deque | O(1) | O(n) | O(1) | O(n) | Medium |
| list | O(n) | O(n) | O(1) | O(1) | High |
| forward_list | O(n) | O(n) | O(1) | O(1) | Low |
| map | O(log n) | O(log n) | O(log n) | O(log n) | Medium |
| unordered_map | O(1) avg | O(1) avg | O(1) avg | O(n) | High |
| set | O(log n) | O(log n) | O(log n) | O(log n) | Medium |
| unordered_set | O(1) avg | O(1) avg | O(1) avg | O(n) | High |

## Engineering Decision Framework

### When to Use Each Container
- **vector**: Default choice. Dynamic array with cache-friendly layout.
- **deque**: Frequent insertion/deletion at both ends.
- **list**: Frequent insertion/deletion in the middle (rarely needed).
- **map/set**: When you need sorted order and guaranteed O(log n).
- **unordered_map/set**: When you need O(1) average lookup and don't care about order.
- **stack/queue/priority_queue**: When you need LIFO/FIFO/heap semantics.

### When NOT to Use STL
- When you need a fixed-size array known at compile time (use `std::array`)
- When you need lock-free concurrent access (use specialized concurrent containers)
- When profiling shows STL containers are the bottleneck (rare)

### Common Pitfalls
| Situation | Wrong Choice | Right Choice |
|-----------|-------------|--------------|
| Frequent middle insertions | `std::vector` | `std::list` or `std::deque` |
| Need sorted + fast lookup | `std::unordered_map` | `std::map` |
| Need O(1) lookup, don't care about order | `std::map` | `std::unordered_map` |
| Holding pointers to elements across push_back | `std::vector` | `std::deque` or `reserve()` |
| Need stable iterators during erase | `std::vector` (erase returns iterator) | Use `it = vec.erase(it)` |

### Real-World Production Examples
1. **Google Abseil**: Provides Swiss tables (`absl::flat_hash_map`) faster than `std::unordered_map`
2. **Facebook Folly**: `fbvector` with optimized allocation strategies
3. **LLVM**: Uses `SmallVector` (small buffer optimization) extensively
4. **Game Engines**: Custom containers with pool allocators for frame-based allocation

## Production Incidents

### Incident 1: Iterator Invalidation Causing Crash
**Problem**: A message broker crashed when processing batches >10,000 messages.

**Cause**: `list.erase(it); it++` — `erase` invalidated `it`, then `it++` dereferenced freed memory. Correct: `it = list.erase(it)`.

**Impact**: ~200 messages/minute lost during peak. Three enterprise clients reported missing events.

**Solution**: Changed to `it = list.erase(it)`. Added ASan in CI.

---

### Incident 2: Vector Reallocation Breaking Pointers
**Problem**: A real-time bidding system crashed under high volume.

**Cause**: A timer thread held a raw pointer to `vec[i]`. When `push_back` triggered reallocation, the pointer became dangling.

**Impact**: ~1,200 bids/hour failed. $15K/day revenue loss.

**Solution**: Switched to `std::deque` (stable pointers on push_back) and called `vec.reserve()`.

---

## Production Checklist
- [ ] Use `std::vector` as the default container
- [ ] Call `reserve()` when you know the upper bound
- [ ] Use range-based for loops (avoids iterator bugs)
- [ ] Use `it = container.erase(it)` instead of separate erase + increment
- [ ] Prefer algorithms over manual loops (`std::sort`, `std::find`, `std::transform`)
- [ ] Use `std::string_view` for read-only string parameters
- [ ] Use structured bindings (C++17) for map iteration
- [ ] Monitor container size in long-running systems
- [ ] Use `emplace_back` instead of `push_back` when constructing in-place

## Maturity Levels

### Beginner
- Use `std::vector`, `std::string`, `std::map`
- Write range-based for loops
- Use `std::sort`, `std::find`, `std::count`

### Intermediate
- Choose containers based on performance characteristics
- Use `std::unordered_map` for O(1) lookup
- Use `std::transform` and `std::accumulate`
- Understand iterator invalidation rules

### Advanced
- Implement custom allocators
- Use iterator adaptors (`std::back_inserter`, `std::transform_iterator`)
- Design cache-friendly data layouts (ECS pattern)
- Use `std::span` (C++20) for non-owning views

## Common Myths Debunked

### Myth 1: "STL is slow because of templates"
**Reality**: STL containers compile to the same machine code as hand-written equivalents. Templates are zero-overhead — the compiler generates specialized code for each type.

### Myth 2: "Raw arrays are always faster than vectors"
**Reality**: They compile to identical machine code. `std::vector` adds zero overhead while providing `.size()`, bounds checking, and automatic memory management.

### Myth 3: "You should implement your own containers"
**Reality**: STL containers are battle-tested across billions of lines of production code. Custom containers are only justified when profiling proves STL is the bottleneck and you have a specific optimization target.

### Myth 4: "std::list is faster for frequent insertions"
**Reality**: `std::list` has poor cache locality. For most workloads, `std::vector` with `std::move` is faster due to cache-friendly memory layout. Profile before choosing `std::list`.

## One-Minute Revision

| Concept | What It Is | When to Use | Watch Out For |
|---------|-----------|-------------|---------------|
| vector | Dynamic array | Default container | Iterator invalidation on insert |
| deque | Double-ended queue | Insert at both ends | Slightly more memory than vector |
| list | Doubly-linked list | Frequent middle insert | Poor cache locality |
| map | Ordered key-value | Need sorted order | O(log n) lookup |
| unordered_map | Hash table | O(1) average lookup | No ordering, worst-case O(n) |
| set | Ordered unique elements | Membership testing | O(log n) |
| Algorithm | Generic function on ranges | Always prefer over manual loops | Check iterator requirements |
| Iterator | Pointer-like traversal | Used with algorithms | Don't use invalidated iterators |
| emplace_back | Construct in-place | Better than push_back | Avoids unnecessary copies |

## Related Topics
- [Templates](../03-templates/) — Templates power the STL's generic design
- [Modern C++](../08-modern-cpp/) — Ranges (C++20) extend STL
- [Performance](../11-performance/) — Cache optimization for containers
- [Memory Management](../05-memory-management/) — Container memory strategies
- [Best Practices](../14-best-practices/) — Choosing the right container

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Iterator invalidation causing crash or corruption | AddressSanitizer + assertion on iterator use | Enable ASan; add debug assertions that check `it != container.end()` after every operation that may invalidate |
| Vector reallocation breaking held pointers/references | `valgrind --tool=memcheck` + `reserve()` audit | Search for raw pointers to vector elements; ensure `reserve()` is called or use `std::deque` for stable pointers |
| O(n) performance from wrong container choice | `perf record` + hotspot analysis | Profile with `perf`; if middle-insertions dominate, consider `std::list` or `std::deque` |
| `std::unordered_map` worst-case O(n) from hash collision | `std::unordered_map::bucket_count()` + custom hasher | Monitor bucket count; provide a custom hash function for user-defined types |
| Wrong algorithm used (e.g., `std::find` on unsorted range) | Code review + complexity annotations | Document time complexity in comments; prefer `std::binary_search` on sorted ranges |

## Code Review Checklist

- [ ] `std::vector` used as the default container
- [ ] `reserve()` called when upper bound on size is known
- [ ] `emplace_back` preferred over `push_back` for in-place construction
- [ ] Range-based for loops used (avoids iterator invalidation bugs)
- [ ] `it = container.erase(it)` used instead of separate erase + increment
- [ ] STL algorithms preferred over manual loops (`std::sort`, `std::find`, `std::transform`)
- [ ] `std::string_view` used for read-only string parameters

## Architecture Considerations

The STL is the backbone of data-oriented design in C++. Containers provide type-safe, zero-overhead data structures optimized across decades of real-world use. Algorithms express intent clearly and are often hand-optimized by standard library implementers. Iterators decouple algorithms from containers, enabling generic programming. Choosing the right container impacts cache performance, memory usage, and API ergonomics at the system level.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| `std::vector` as default container | Most sequential storage needs | Cache-friendly but iterator invalidation on insert/realloc |
| `std::unordered_map` for O(1) lookup | Key-value caches, frequency counters | Fast average case vs. no ordering and worst-case O(n) |
| Erase-remove idiom | Filtering elements from containers | Concise but requires understanding iterator invalidation rules |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Iterator invalidation leading to use-after-free | Crashes, memory corruption, exploitable bugs | Use `it = container.erase(it)`; enable ASan in CI |
| Integer overflow in container size calculations | Buffer overflow, heap corruption | Use `size_t` for sizes; check `container.max_size()` before large allocations |
| Hash collision denial-of-service on `unordered_map` | O(n) lookup degradation, performance DoS | Use randomized hash seeds; monitor bucket count and load factor |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | `std::array`, `emplace_back`, initializer lists | Replace C arrays with `std::array`; use `emplace_back` for in-place construction |
| C++17 | `std::string_view`, structured bindings, `std::optional` | Use `string_view` for read-only params; use structured bindings for map iteration |
| C++20 | `std::span`, ranges, `std::format` | Use `std::span` for non-owning array views; use ranges for lazy pipeline algorithms |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `std::array` | C++11 | Widely supported |
| `emplace_back` | C++11 | Widely supported |
| `std::string_view` | C++17 | Widely supported |
| `std::span` (non-owning view) | C++20 | Supported in GCC 10+, Clang 11+, MSVC 19.29+ |

## Interview Questions

1. **When should you use `std::vector` vs `std::deque`?**: Use `std::vector` as the default — it's cache-friendly and has O(1) random access. Use `std::deque` when you need frequent insertion/deletion at both ends or when you need stable pointers/references across push_back operations.
2. **Explain the erase-remove idiom**: `container.erase(std::remove(begin, end, value), end)` combines `std::remove` (which shifts elements and returns new end) with `erase` (which actually resizes the container). It's the standard way to remove elements matching a condition.
3. **Why is `std::list` rarely the right choice?**: Despite O(1) insertion/deletion, `std::list` has poor cache locality — each node is a separate heap allocation. For most workloads, `std::vector` with `std::move` is faster due to cache-friendly memory layout.
4. **What is iterator invalidation and which containers are affected?**: Iterator invalidation occurs when an operation (insert, erase, reallocation) makes existing iterators point to invalid memory. `std::vector` invalidates all iterators on reallocation and most on middle-insert. `std::list` only invalidates iterators to the erased element.
5. **How do you choose between `std::map` and `std::unordered_map`?**: Use `std::unordered_map` when you need O(1) average lookup and don't care about order. Use `std::map` when you need sorted order, range queries, or guaranteed O(log n) without hash collision risk.

## References

- [Effective STL — Scott Meyers](https://www.amazon.com/Effective-STL-Specific-Strategies-Containers/dp/0201749629)
- [CppReference — Containers](https://en.cppreference.com/w/cpp/container)
- [Google Abseil — Swiss Tables](https://abseil.io/about/design/swisstables)
- [CppCon Talk: Back to Basics: C++ Containers](https://youtube.com/cppcon)
