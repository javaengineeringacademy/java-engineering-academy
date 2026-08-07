# Standard Template Library (STL)

## What it is
A collection of template classes and functions for common data structures and algorithms.

## Why it exists
To provide efficient, reusable, and type-safe implementations of common programming patterns.

## When to use it
Whenever you need containers, algorithms, iterators, or function objects.

## How it works

### Containers
```cpp
// Sequential containers
std::vector<int> vec = {1, 2, 3, 4, 5};
std::list<std::string> lst = {"a", "b", "c"};
std::deque<double> deq = {1.1, 2.2, 3.3};

// Associative containers
std::map<std::string, int> mp = {{"one", 1}, {"two", 2}};
std::set<int> st = {1, 2, 3, 4, 5};
```

### Algorithms
```cpp
#include <algorithm>

std::vector<int> vec = {5, 3, 1, 4, 2};
std::sort(vec.begin(), vec.end());
auto it = std::find(vec.begin(), vec.end(), 3);
int sum = std::accumulate(vec.begin(), vec.end(), 0);
```

### Iterators
```cpp
for (auto it = vec.begin(); it != vec.end(); ++it) {
    std::cout << *it << " ";
}

// Range-based for loop
for (const auto& elem : vec) {
    std::cout << elem << " ";
}
```

## Production Incidents

### Incident 1: Iterator Invalidation Causing Crash
**Problem**: A message broker crashed when processing batches of more than 10,000 messages, producing segfaults in the event loop.

**Cause**: A `std::list` of messages was iterated with an iterator while the loop body called `list.erase()` on the current element and then continued to the next. The code used `it++` after `erase(it)`, but `erase` already invalidated `it`. The correct pattern is `it = list.erase(it)`.

**Impact**: Broker dropped messages under load. ~200 messages/minute lost during peak. Three enterprise clients reported missing events in their event-driven pipelines.

**Detection**: AddressSanitizer caught the invalid iterator dereference. Valgrind's `--tool=exp-sgcheck` confirmed the bug with a detailed trace showing the invalidated iterator being dereferenced.

**Solution**: Changed `list.erase(it); it++` to `it = list.erase(it)` which returns the next valid iterator. Added a debug-mode iterator validity checker using sentinels to catch similar issues early.

**Prevention**: Rule — always use the return value of `erase()`. Enable ASan in CI. Use range-based for loops where possible (though `erase` during range-for requires care). Add a coding standard item for iterator validity after mutation.

---

### Incident 2: Vector Reallocation Breaking Pointers
**Problem**: A real-time bidding system experienced intermittent segfaults that only occurred under high auction volume.

**Cause**: A `std::vector<Bid>` stored active bids. A pointer to `vec[i]` was held by an auction timer thread. When a new bid was pushed and the vector reallocated, the pointer became dangling. The timer thread accessed freed memory.

**Impact**: 0.5% of auction timers crashed. The bidding system failed to process ~1,200 bids per hour during peak, causing revenue loss estimated at $15K/day.

**Detection**: AddressSanitizer in a load test reproduced the crash. GDB backtrace showed the timer thread accessing memory at an address within a freed heap region previously owned by the vector's old allocation.

**Solution**: Switched to `std::deque<Bid>` which doesn't invalidate pointers on `push_back()`. For the remaining vector use cases, called `vec.reserve(expected_max_size)` at initialization to prevent reallocation. Replaced raw pointers with `std::deque::iterator` (which is stable for `push_back`).

**Prevention**: Rule — never hold raw pointers/iterators across mutating operations on `std::vector`. Prefer `std::deque` when pointers to elements must survive `push_back`. Always `reserve()` known upper bounds. Enable ASan in CI.

---

## Production Checklist
- [ ] Use `std::vector` as default container
- [ ] Prefer algorithms over manual loops
- [ ] Use `std::array` for fixed-size arrays
- [ ] Use `std::unordered_map` for O(1) lookup
- [ ] Reserve capacity for known sizes
- [ ] Use `std::string_view` for read-only strings

## Maturity Levels
- **Beginner**: Basic containers (vector, list, map)
- **Intermediate**: Algorithms, iterators, functors
- **Advanced**: Custom allocators, iterator categories

## Common Myths
- ❌ "STL is slow because of templates"
- ❌ "Raw arrays are always faster than vectors"
- ❌ "You should implement your own containers"

## One-Minute Revision
| Concept | Description |
|---------|-------------|
| Container | Data structure holding objects |
| Iterator | Pointer-like object for traversal |
| Algorithm | Function operating on ranges |
| Functor | Function object |
| Adapter | Modified container/iterator/function |

## Related Topics
- [Templates](../03-templates/)
- [Performance](../11-performance/)
- [Modern C++](../08-modern-cpp/)