# Memory Management

## What it is
The process of allocating, using, and deallocating memory in C++ programs.

## Why it exists
To give developers control over memory usage and performance.

## When to use it
When you need dynamic memory allocation, custom memory pools, or optimization.

## How it works

### Stack vs Heap
```cpp
// Stack allocation (automatic)
int x = 10;
int arr[5] = {1, 2, 3, 4, 5};

// Heap allocation (dynamic)
int* ptr = new int(10);
int* arr = new int[5];
delete ptr;
delete[] arr;
```

### RAII (Resource Acquisition Is Initialization)
```cpp
class Resource {
public:
    Resource() { /* acquire */ }
    ~Resource() { /* release */ }
};

void useResource() {
    Resource res;  // Automatically cleaned up
}
```

### Memory Leaks
```cpp
// Bad: Memory leak
void leak() {
    int* ptr = new int(10);
    // Forgot to delete
}

// Good: No leak
void noLeak() {
    std::unique_ptr<int> ptr = std::make_unique<int>(10);
    // Automatically deleted
}
```

## Production Incidents

### Incident 1: Memory Leak in RAII Failure
**Problem**: A long-running server process consumed 16GB of RAM within hours, eventually triggering OOM kills.

**Cause**: A `Resource` class wrapped a file handle but the destructor threw an exception on close. When an exception propagated out, the stack unwinding skipped the destructor, leaking the underlying OS file descriptor. Repeated cycles exhausted memory and fd limits.

**Impact**: Production service crashed every 4-6 hours. Customer-facing API returned 503 errors. On-call engineers paged at 3 AM twice in one week.

**Detection**: `valgrind --leak-check=full` in staging reproduced the leak. `strace` revealed accumulating open file descriptors. Linux `dmesg` showed OOM killer activity.

**Solution**: Made the destructor `noexcept` and moved cleanup logic to a separate `release()` method with a try-catch wrapper. Used `std::unique_ptr` with a custom deleter to guarantee cleanup regardless of exceptions.

**Prevention**: Rule — never let destructors throw. Audit all RAII wrappers. Use static analysis tools (clang-tidy `bugprone-exception-escape`) to flag throwing destructors in CI.

---

### Incident 2: Use-After-Move Undefined Behavior
**Problem**: An audio processing pipeline produced intermittent garbled output and occasional segfaults under load.

**Cause**: A `std::vector<float>` buffer was moved into a processing queue, but a raw pointer to its `.data()` was retained and used after the move. The pointer referenced freed memory — classic use-after-move.

**Impact**: ~0.1% of requests returned corrupted audio. Two segfault crashes in production over a weekend. Customer complaints from a healthcare client using the API for telemedicine.

**Detection**: AddressSanitizer (`-fsanitize=address`) caught the bug in under 5 minutes of testing. ASan reported "heap-use-after-move" with a clear stack trace.

**Solution**: Replaced the raw pointer with an `std::shared_ptr` managing the buffer lifetime. Added a move-tracking assertion in debug builds that aborts on use-after-move.

**Prevention**: Compile with ASan in CI. Ban raw `.data()` pointers across the codebase via clang-tidy `cppcoreguidelines-owning-memory`. Code review checklist must flag `std::move` followed by continued use.

---

## Production Checklist
- [ ] Prefer stack allocation over heap
- [ ] Use RAII for resource management
- [ ] Use smart pointers for dynamic memory
- [ ] Always pair `new` with `delete`
- [ ] Use memory pools for frequent allocations
- [ ] Profile memory usage regularly

## Maturity Levels
- **Beginner**: Basic new/delete, stack vs heap
- **Intermediate**: RAII, smart pointers, memory pools
- **Advanced**: Custom allocators, memory profiling, optimization

## Common Myths
- ❌ "Heap is always faster than stack"
- ❌ "You need to manually manage all memory"
- ❌ "Smart pointers have no overhead"

## One-Minute Revision
| Concept | Description |
|---------|-------------|
| Stack | Fast, automatic memory |
| Heap | Slow, manual memory |
| RAII | Resource management via scope |
| Smart Pointers | Automatic memory management |
| Memory Leak | Unreleased allocated memory |

## Related Topics
- [Smart Pointers](../06-smart-pointers/)
- [Performance](../11-performance/)
- [Best Practices](../14-best-practices/)