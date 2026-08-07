# Smart Pointers

## What it is
Objects that manage dynamically allocated memory automatically.

## Why it exists
To prevent memory leaks and simplify memory management.

## When to use it
Whenever you need dynamic memory allocation with automatic cleanup.

## How it works

### std::unique_ptr
```cpp
#include <memory>

std::unique_ptr<int> ptr = std::make_unique<int>(42);
// Cannot copy, only move
std::unique_ptr<int> ptr2 = std::move(ptr);
```

### std::shared_ptr
```cpp
std::shared_ptr<int> ptr1 = std::make_shared<int>(42);
std::shared_ptr<int> ptr2 = ptr1;  // Reference count increases
// Automatically deleted when last reference is gone
```

### std::weak_ptr
```cpp
std::weak_ptr<int> weak = ptr1;
if (auto shared = weak.lock()) {
    // Use shared pointer
}
```

## Production Incidents

### Incident 1: Circular Reference Causing Memory Leak
**Problem**: A tree-structured cache in a web framework leaked memory on every request, causing the service to grow from 200MB to 8GB over 24 hours.

**Cause**: Parent and child `Node` objects held `std::shared_ptr` references to each other. Reference counts never reached zero because each side kept the other alive — a classic circular reference.

**Impact**: Service restarted every night by OOM watchdog. Cache hit rate dropped to 0% after restarts, causing downstream database overload and cascading failures across the platform.

**Detection**: Heap profiling with `heapprofd` showed `Node` objects accumulating indefinitely. Adding `weak_ptr` debug logging confirmed reference counts never dropping below 2.

**Solution**: Changed parent-to-child references from `std::shared_ptr<Node>` to `std::weak_ptr<Node>`. The parent can observe children without preventing their destruction. Added a periodic cache integrity check that logs orphaned nodes.

**Prevention**: Static analysis rule: flag any class containing `shared_ptr<T>` where `T` is the same type as the class. Use `std::weak_ptr` by default for back-references. Enable `-Wdangling-reference` in Clang builds.

---

### Incident 2: Thread Safety Issues with shared_ptr
**Problem**: A multi-threaded connection pool experienced intermittent crashes and corrupted state under high concurrency.

**Cause**: Two threads copied the same `std::shared_ptr<Connection>` without synchronization. While `shared_ptr` reference count updates are atomic, the control block itself was being read/written concurrently during `reset()` calls, causing a data race on the pointer value (not the count).

**Impact**: ~2% of connections were corrupted, leading to dropped WebSocket connections and stale state in the connection pool. Users reported intermittent disconnects during peak traffic.

**Detection**: ThreadSanitizer (`-fsanitize=thread`) in a load test flagged the data race. `std::shared_ptr` debug mode (`_LIBCPP_ENABLE_DEBUG_MODE`) caught the race with assertion failures.

**Solution**: Wrapped all `shared_ptr` copies behind a `std::mutex` protecting the connection pool. For hot paths, switched to `std::atomic<std::shared_ptr<T>>` (C++20) where the entire pointer swap is atomic.

**Prevention**: Compile with TSan in CI. Rule — never access `shared_ptr` from multiple threads without synchronization, even if only reading. Prefer `std::atomic<shared_ptr<T>>` or explicit mutex protection for shared state.

---

## Production Checklist
- [ ] Use `std::unique_ptr` for exclusive ownership
- [ ] Use `std::shared_ptr` for shared ownership
- [ ] Use `std::weak_ptr` to break cycles
- [ ] Prefer `std::make_unique` and `std::make_shared`
- [ ] Avoid `std::enable_shared_from_this` when possible
- [ ] Use custom deleters for non-memory resources

## Maturity Levels
- **Beginner**: Basic unique_ptr and shared_ptr
- **Intermediate**: weak_ptr, custom deleters
- **Advanced**: Thread safety, performance optimization

## Common Myths
- ❌ "Smart pointers are slow"
- ❌ "You never need raw pointers"
- ❌ "shared_ptr is always better than unique_ptr"

## One-Minute Revision
| Concept | Description |
|---------|-------------|
| unique_ptr | Exclusive ownership |
| shared_ptr | Shared ownership with reference counting |
| weak_ptr | Non-owning reference |
| make_shared | Safe shared_ptr creation |
| make_unique | Safe unique_ptr creation |

## Related Topics
- [Memory Management](../05-memory-management/)
- [Concurrency](../07-concurrency/)
- [Best Practices](../14-best-practices/)