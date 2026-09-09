# Standard Template Library (STL) — C++

## Overview

The Standard Template Library (STL) is C++'s foundational library of generic containers, iterators, algorithms, and function objects. It provides type-safe, efficient, and reusable implementations of common data structures and operations, enabling developers to write correct, performant code without reinventing the wheel.

The STL is C++'s standard library of generic containers, iterators, algorithms, and function objects, providing type-safe, efficient, and reusable implementations of common data structures and operations.

## Why It Matters

Every C++ program needs data structures and algorithms. When developers reinvent linked lists, hash maps, and sorting routines, they introduce subtle bugs, inconsistent APIs, and wasted time. The STL provides production-tested, type-safe, zero-overhead containers and algorithms optimized across 40+ years of real-world use.

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

## Internal Working

### Hash Table Implementation (`std::unordered_map`, `std::unordered_set`)

The unordered containers use a hash table with separate chaining (linked lists per bucket) or open addressing (Swiss tables in Abseil). Key internals:

```cpp
std::unordered_map<int, std::string> map;
map.reserve(1024);           // Pre-allocate 1024 buckets
map.max_load_factor();       // Default 1.0 — triggers rehash when load exceeds this
map.bucket_count();          // Number of buckets
map.load_factor();           // size() / bucket_count()

// Hash collision: worst-case O(n) lookup when all keys hash to same bucket
// Mitigation: use a good hash function, reserve() for known sizes
```

### Red-Black Tree (`std::map`, `std::set`)

The ordered associative containers use a self-balancing red-black tree, guaranteeing O(log n) for all operations:

- **Insertion**: O(log n) — may trigger tree rebalancing
- **Deletion**: O(log n) — rebalancing after removal
- **Search**: O(log n) — binary search on tree structure
- **Iteration**: O(n) — in-order traversal produces sorted sequence

```cpp
std::map<std::string, int> tree;
tree["a"] = 1;  // Insert triggers rebalancing
// The tree maintains sorted order internally via red-black balancing
```

### Iterator Categories

Iterators are classified by the operations they support, forming a hierarchy:

```
InputIterator → ForwardIterator → BidirectionalIterator → RandomAccessIterator → ContiguousIterator
     ↑                                                                    ↑
  (read-only)                                                     (vector, array, string)
```

| Category | Operations | Containers |
|----------|-----------|------------|
| InputIterator | `*it`, `++it`, `it != other` | `istream_iterator` |
| ForwardIterator | + default construction, multi-pass | `forward_list`, `unordered_map` |
| BidirectionalIterator | + `--it` | `list`, `set`, `map` |
| RandomAccessIterator | + `it + n`, `it - n`, `it[n]`, `it1 < it2` | `vector`, `deque` |
| ContiguousIterator | + elements stored contiguously in memory | `vector`, `array`, `string`, `span` |

## Learning Objectives

After completing this module, you will be able to:

- Choose the appropriate STL container based on access patterns and performance requirements
- Use iterators correctly and understand iterator invalidation rules for each container
- Apply STL algorithms (`std::sort`, `std::find`, `std::transform`, `std::accumulate`) to solve common problems
- Recognize the time and space complexity trade-offs between sequential, associative, and unordered containers
- Write exception-safe code using the erase-remove idiom and emplace operations
- Identify and fix common STL-related bugs such as iterator invalidation and dangling pointers
- Leverage C++11/17/20 STL features (`emplace_back`, `std::string_view`, `std::span`, ranges)

## Prerequisites

Before studying the STL, you should be comfortable with:

- **C++ Basics**: Variables, functions, classes, and object lifecycle
- **Templates**: Basic template syntax and how generic programming works in C++ (see [Templates](../03-templates/))
- **Pointers & References**: Dereferencing, pointer arithmetic, and reference semantics
- **Memory Management**: `new`/`delete`, stack vs. heap, and RAII principles (see [Memory Management](../05-memory-management/))
- **Basic Data Structures**: Arrays, linked lists, stacks, queues, and hash maps (conceptual understanding)

## History

The STL's evolution reflects the broader history of C++ standardization:

| Year | Milestone | Details |
|------|-----------|---------|
| 1994 | HP STL released | Alexander Stepanov and Meng Lee at HP Labs release the first implementation of the STL |
| 1994 | STL proposed for C++ | The STL is proposed for inclusion in the C++ standard library |
| 1998 | C++98 STL adopted | The STL becomes part of the C++98 standard, with containers, iterators, algorithms, and function objects |
| 2011 | C++11 emplace & move | `emplace_back`, `emplace`, move semantics, `std::array`, initializer lists added — enabling in-place construction and eliminating unnecessary copies |
| 2014 | C++14 refinements | `std::exchange`, `std::cbegin`/`std::cend`, and other quality-of-life improvements |
| 2017 | C++17 `std::pmr` | Polymorphic memory allocators (`std::pmr::vector`, `std::pmr::string`) allow custom allocation strategies without changing container types |
| 2020 | C++20 ranges & views | `std::ranges`, `std::views`, `std::span` — lazy pipeline algorithms and non-owning views extend the STL's expressiveness |

The original HP STL was designed around the idea that algorithms should be separated from containers via iterators — a separation of concerns that remains the STL's core architectural principle.

## Production Notes

- **STL implementations vary**: libstdc++ (GCC), libc++ (Clang), and MSVC's STL have different performance profiles for edge cases. Benchmark on your target platform.
- **Debug vs. Release performance**: STL containers in debug builds (with iterator checking, `_GLIBCXX_DEBUG`) can be 10–100× slower. Always benchmark in release mode.
- **Compiler optimizations**: Modern compilers inline STL algorithm calls effectively. `std::sort` often outperforms hand-written quicksort due to compiler-specific optimizations (e.g., pdqsort in libstdc++).
- **ABI stability**: GCC's `std::string` ABI changed in GCC 5 (COW vs. SSO). Mixing code compiled with different GCC versions can cause linking issues.
- **Memory overhead**: `std::unordered_map` with default settings may allocate far more memory than expected due to bucket count growth. Call `reserve()` for known sizes.

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

## Best Practices

1. **Default to `std::vector`**: It's cache-friendly, has O(1) random access, and is the most optimized container across compilers.
2. **Always `reserve()` when you know the size**: Avoids reallocation and iterator invalidation.
3. **Use `emplace_back` over `push_back`**: Constructs objects in-place, avoiding unnecessary copies/moves.
4. **Prefer algorithms over manual loops**: `std::sort`, `std::find`, `std::transform` express intent and are often hand-optimized.
5. **Use `std::string_view` for read-only strings**: Avoids copying string data when you only need to read it.
6. **Use `auto` with iterators**: Reduces verbosity and prevents type mismatches.
7. **Use structured bindings for maps (C++17)**: `for (auto& [key, value] : map)` is cleaner than `it->first`/`it->second`.
8. **Use `std::exchange` for complex state transitions**: Cleaner than manual swap-and-assign patterns.
9. **Provide custom hashers for user-defined types in `unordered_map`**: The standard library doesn't hash custom types by default.
10. **Monitor container sizes in long-running systems**: Unbounded growth leads to OOM; use bounded containers or periodic pruning.

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

### Incident 3: `std::unordered_map` Hash Collision DoS
**Problem**: An authentication service experienced sudden latency spikes (p99 > 5s) under load, with no code changes.

**Cause**: An attacker crafted input that produced identical hash values for `std::unordered_map<std::string, int>`, degrading all lookups to O(n). The default `std::hash<std::string>` was vulnerable to collision attacks.

**Impact**: Authentication latency spiked 50×. ~3% of requests timed out. Service degraded for 45 minutes.

**Solution**: Replaced `std::hash` with a randomized salt-based hasher (`absl::Hash` or `std::hash` with random seed). Added load factor monitoring and alerting.

---

### Incident 4: Vector Insert Causing Iterator Invalidation in Hot Loop
**Problem**: A log processing pipeline crashed intermittently under high throughput.

**Cause**: A loop used `std::vector::insert` to add elements while iterating. The insert could trigger reallocation, invalidating all iterators including the loop variable. The crash was non-deterministic because it only occurred when the vector grew past its capacity.

**Impact**: ~5 crashes/day during peak hours. Each crash required a full service restart (30s downtime).

**Solution**: Replaced the loop with `std::remove_if` + `erase` (erase-remove idiom) or used `std::copy_if` with `std::back_inserter`. Added `-fsanitize=address` in CI to catch future issues.

---

### Incident 5: `std::list` Memory Leak from Unbounded Growth
**Problem**: A chat application's server process grew memory usage by ~2GB/day until OOM kill.

**Cause**: Messages were stored in a `std::list`, but the cleanup code used `list.erase(it++)` instead of `it = list.erase(it)`. The post-increment on an invalidated iterator caused the erase to silently skip elements, leaving leaked nodes.

**Impact**: Server OOM kills happened daily. Each restart caused 2–3 minutes of lost connections for all connected users.

**Solution**: Changed to `it = list.erase(it)`. Added AddressSanitizer in CI. Implemented a memory budget alert at 80% of container capacity.

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

6. **What is the advantage of `emplace_back` over `push_back`?**: `emplace_back` constructs the object in-place using the arguments you provide, avoiding an intermediate temporary object and potential copy/move. `push_back` requires an already-constructed object. For complex types, `emplace_back` can be significantly faster.

7. **Why does `std::vector` invalidate all iterators on reallocation?**: When `push_back` exceeds capacity, the vector allocates a new buffer and moves all elements. Old iterators still point to the freed buffer. Always re-acquire iterators after operations that may reallocate.

8. **Explain `std::deque`'s iterator invalidation rules**: `std::deque` invalidates all iterators on `push_back`/`push_front` (but not on `insert` at ends). On `insert` in the middle, all iterators are invalidated. On `erase`, iterators to elements before the erased position remain valid.

9. **What is the time complexity of `std::sort` and why is it preferred over quicksort?**: `std::sort` is O(n log n) guaranteed (typically introsort: quicksort + heapsort fallback). It avoids quicksort's O(n²) worst case, is cache-friendly, and is often hand-optimized by compiler vendors (e.g., pdqsort in libstdc++).

10. **When should you use `std::forward_list` over `std::list`?**: Use `std::forward_list` when you only need forward iteration and want lower memory overhead (one pointer per node vs. two). It's useful in memory-constrained environments and for implementing singly-linked list algorithms.

11. **How do you safely iterate and erase from a `std::vector`?**: Use the pattern `it = vec.erase(it)` which returns an iterator to the next element. Never do `vec.erase(it); it++` because `erase` invalidates `it`. Alternatively, use the erase-remove idiom for batch removal.

12. **What is the difference between `std::map::insert` and `std::map::operator[]`?**: `insert` does nothing if the key exists (returns iterator to existing element). `operator[]` inserts a default-constructed value if the key doesn't exist. Use `insert` when you don't want to overwrite; use `operator[]` or `insert_or_assign` (C++17) when you do.

13. **Explain `std::priority_queue` and when to use `std::make_heap`/`std::push_heap` instead**: `std::priority_queue` is a container adapter providing heap operations. Use raw heap functions (`std::make_heap`, `std::push_heap`, `std::pop_heap`) when you need direct access to the underlying container or custom comparison with more control.

14. **What is the purpose of `std::accumulate` and what are its pitfalls?**: `std::accumulate` computes a running sum (or custom operation) over a range. Pitfall: the initial value's type determines the result type, so `std::accumulate(vec.begin(), vec.end(), 0)` truncates if elements are `long long`. Use the correct initial value type.

15. **How do C++20 ranges improve STL algorithm usage?**: Ranges allow piped algorithm composition: `vec | std::views::filter(pred) | std::views::transform(fn)`. They eliminate iterator pairs, support lazy evaluation, and integrate with concepts for better error messages. Use `std::ranges::sort(vec)` instead of `std::sort(vec.begin(), vec.end())`.

## References

- [Effective STL — Scott Meyers](https://www.amazon.com/Effective-STL-Specific-Strategies-Containers/dp/0201749629)
- [CppReference — Containers](https://en.cppreference.com/w/cpp/container)
- [Google Abseil — Swiss Tables](https://abseil.io/about/design/swisstables)
- [CppCon Talk: Back to Basics: C++ Containers](https://youtube.com/cppcon)
