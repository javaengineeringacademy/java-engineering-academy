# Smart Pointers — C++

## Why It Matters

Manual memory management with raw `new`/`delete` is error-prone: forgetting to delete causes leaks, deleting too early causes use-after-free, and deleting twice causes crashes. When you automate memory management using RAII, you eliminate entire categories of memory bugs with automatic cleanup when pointers go out of scope.

## What It Is

Smart pointers are RAII wrappers around raw pointers that automatically manage object lifetime. C++ provides `unique_ptr` for exclusive ownership, `shared_ptr` for shared ownership with reference counting, and `weak_ptr` for non-owning observation.

## Architecture: How Smart Pointers Fit Together

```
┌─────────────────────────────────────────────────────────────┐
│                   C++ Smart Pointers                         │
├───────────────┬───────────────┬─────────────────────────────┤
│  unique_ptr   │  shared_ptr   │       weak_ptr              │
│ (Exclusive    │ (Shared       │ (Non-owning                 │
│  ownership)   │  ownership)   │  observer)                  │
├───────────────┴───────────────┴─────────────────────────────┤
│              Custom Deleters & Allocators                     │
├─────────────────────────────────────────────────────────────┤
│     Thread Safety: reference count is atomic                  │
└─────────────────────────────────────────────────────────────┘
```

## std::unique_ptr

### The Problem unique_ptr Solves
You need exclusive ownership of a heap-allocated object with automatic cleanup. `unique_ptr` provides zero-overhead exclusive ownership — it's identical to a raw pointer in performance.

```cpp
#include <memory>

// Construction
auto p1 = std::make_unique<int>(42);           // Preferred: exception-safe
std::unique_ptr<int> p2(new int(42));           // Also works

// Usage (same as raw pointer)
*p1 = 100;
int val = *p2;

// Cannot copy — enforces exclusive ownership
// std::unique_ptr<int> p3 = p1;               // ERROR: copy deleted

// Can move — transfers ownership
std::unique_ptr<int> p3 = std::move(p1);        // p1 is now nullptr
assert(p1 == nullptr);
assert(*p3 == 100);

// Arrays
auto arr = std::make_unique<int[]>(10);          // Array allocation
arr[0] = 42;

// Polymorphism
class Shape { public: virtual ~Shape() = default; };
class Circle : public Shape {};

std::unique_ptr<Shape> shape = std::make_unique<Circle>();  // Polymorphic delete
```

### Custom Deleters

```cpp
// Custom deleter for FILE*
auto file = std::unique_ptr<FILE, decltype(&fclose)>(
    fopen("test.txt", "r"), &fclose);

// Lambda deleter
auto socket = std::unique_ptr<int, decltype([](int* fd) {
    close(*fd);
})>(new int(socket_fd));

// For C resources
struct CurlDeleter {
    void operator()(CURL* curl) { curl_easy_cleanup(curl); }
};
std::unique_ptr<CURL, CurlDeleter> curl(curl_easy_init());
```

## std::shared_ptr

### The Problem shared_ptr Solves
Sometimes multiple owners need to share the same resource. `shared_ptr` uses reference counting to track how many owners exist — the object is destroyed when the last owner releases it.

```cpp
// Construction
auto p1 = std::make_shared<int>(42);  // Preferred: single allocation

// Reference counting
std::shared_ptr<int> p2 = p1;  // Reference count = 2
std::shared_ptr<int> p3 = p2;  // Reference count = 3

p1.reset();  // Reference count = 2
p2.reset();  // Reference count = 1
p3.reset();  // Reference count = 0 → object destroyed

// Control block
// shared_ptr contains: pointer to object + pointer to control block
// Control block contains: reference count, weak count, deleter
```

### shared_ptr Pitfalls

```cpp
// PITFALL: Don't create shared_ptrs from raw pointers multiple times
int* raw = new int(42);
std::shared_ptr<int> sp1(raw);  // Reference count = 1
std::shared_ptr<int> sp2(raw);  // Reference count = 1 (SEPARATE control block!)
// When sp1 and sp2 both go out of scope → DOUBLE FREE

// CORRECT: Always use make_shared or assign from existing shared_ptr
auto sp1 = std::make_shared<int>(42);
std::shared_ptr<int> sp2 = sp1;  // Same control block, reference count = 2

// PITFALL: shared_ptr and arrays
std::shared_ptr<int> arr(new int[10]);  // WRONG: uses delete, not delete[]
// Use custom deleter:
std::shared_ptr<int> arr2(new int[10], std::default_delete<int[]>());
```

## std::weak_ptr

### The Problem weak_ptr Solves
`shared_ptr` circular references prevent objects from being destroyed. `weak_ptr` observes an object without owning it — it doesn't increment the reference count.

```cpp
class Node {
public:
    std::string data;
    std::shared_ptr<Node> parent;     // Strong: parent owns child
    std::weak_ptr<Node> child;        // Weak: child doesn't prevent parent destruction

    ~Node() { std::cout << "Destroyed: " << data << "\n"; }
};

// Usage
auto parent = std::make_shared<Node>();
parent->data = "parent";

auto child = std::make_shared<Node>();
child->data = "child";
child->parent = parent;  // Strong reference to parent
parent->child = child;   // Weak reference to child

// Check if child is still alive
if (auto alive_child = parent->child.lock()) {
    std::cout << "Child alive: " << alive_child->data << "\n";
} else {
    std::cout << "Child destroyed\n";
}
```

## enable_shared_from_this

```cpp
class Widget : public std::enable_shared_from_this<Widget> {
public:
    std::shared_ptr<Widget> getPtr() {
        return shared_from_this();  // Returns shared_ptr to this
    }

    static std::shared_ptr<Widget> create() {
        return std::make_shared<Widget>();
    }
};

// Usage
auto w = Widget::create();
auto w2 = w->getPtr();  // Both share ownership
assert(w.use_count() == 2);
```

## Engineering Decision Framework

### When to Use Each Smart Pointer
| Situation | Use | Why |
|-----------|-----|-----|
| Exclusive ownership | `unique_ptr` | Zero overhead, clear ownership |
| Shared ownership | `shared_ptr` | Reference-counted cleanup |
| Breaking circular refs | `weak_ptr` | Non-owning observer |
| C resources (FILE*, socket) | `unique_ptr` with custom deleter | Automatic cleanup |
| Factory functions | `unique_ptr` | Caller decides ownership |
| Caches | `weak_ptr` | Observe without preventing destruction |

### When NOT to Use Smart Pointers
- For small, short-lived objects (stack is faster)
- When raw performance is critical and ownership is simple
- When interfacing with C APIs that manage their own memory

### Real-World Production Examples
1. **Chromium**: `std::unique_ptr` for all owned objects; `std::shared_ptr` for cross-component references
2. **LLVM**: Heavy use of `std::unique_ptr` in the AST and IR
3. **Game Engines**: Custom smart pointers with debug tracking (allocation site, leak detection)

### Common Mistakes

| Mistake | Consequence | Fix |
|---------|-------------|-----|
| Creating multiple shared_ptrs from same raw pointer | Double free | Use `make_shared` or assign from existing shared_ptr |
| Circular references with shared_ptr | Memory leak | Use `weak_ptr` for back-references |
| `shared_ptr` to arrays | Undefined behavior (delete vs delete[]) | Use `shared_ptr<T>` with custom deleter or `std::vector` |
| Using `.get()` and storing the raw pointer | Dangling pointer if shared_ptr destroyed | Keep the shared_ptr alive |
| Thread safety: copying shared_ptr from multiple threads | Data race on control block | Use `std::atomic<shared_ptr<T>>` (C++20) |

## Production Incidents

### Incident 1: Circular Reference Causing Memory Leak
**Problem**: A tree-structured cache leaked memory on every request.

**Cause**: Parent and child `Node` objects held `shared_ptr` references to each other. Reference counts never reached zero.

**Solution**: Changed parent-to-child references from `shared_ptr` to `weak_ptr`.

---

### Incident 2: Thread Safety Issues with shared_ptr
**Problem**: A connection pool experienced intermittent crashes under high concurrency.

**Cause**: Two threads copied the same `shared_ptr` without synchronization. While reference count updates are atomic, the pointer value itself was being read/written concurrently.

**Solution**: Wrapped all `shared_ptr` copies behind a mutex. Used `std::atomic<shared_ptr<T>>` (C++20) for hot paths.

---

## Production Checklist
- [ ] Use `std::unique_ptr` as the default smart pointer
- [ ] Use `std::make_unique` and `std::make_shared` (exception safety)
- [ ] Use `std::weak_ptr` to break circular references
- [ ] Never create multiple `shared_ptr` from the same raw pointer
- [ ] Use custom deleters for non-memory resources
- [ ] Prefer `unique_ptr` for factory functions
- [ ] Compile with ASan to catch memory bugs in CI

## Maturity Levels

### Beginner
- Use `unique_ptr` for exclusive ownership
- Use `shared_ptr` for shared ownership
- Understand ownership semantics

### Intermediate
- Use `weak_ptr` for observer patterns and caches
- Implement custom deleters
- Understand `enable_shared_from_this`

### Advanced
- Design thread-safe shared_ptr usage
- Implement custom allocators for smart pointers
- Use aliasing constructor for shared ownership of sub-objects

## Common Myths Debunked

### Myth 1: "Smart pointers are slow"
**Reality**: `unique_ptr` has zero overhead — identical to raw pointers. `shared_ptr` has overhead from atomic reference counting, but this is negligible for most applications.

### Myth 2: "You never need raw pointers"
**Reality**: Raw pointers are useful for non-owning references (observing without owning), C API interop, and performance-critical code where ownership is managed externally.

### Myth 3: "shared_ptr is always better than unique_ptr"
**Reality**: `unique_ptr` is simpler, faster, and clearer about ownership. Use `shared_ptr` only when you genuinely need shared ownership. Most designs should prefer `unique_ptr`.

## One-Minute Revision

| Pointer | Ownership | Copy? | Overhead | Use Case |
|---------|-----------|-------|----------|----------|
| Raw `T*` | None (observer) | Yes | Zero | Non-owning references |
| `unique_ptr<T>` | Exclusive | No (move only) | Zero | Default for heap objects |
| `shared_ptr<T>` | Shared | Yes (ref count++) | Atomic ops + control block | Multiple owners |
| `weak_ptr<T>` | None (observer) | Yes (no ref count change) | Control block access | Breaking cycles, caches |

## Related Topics
- [Memory Management](../05-memory-management/) — Raw memory management fundamentals
- [Concurrency](../07-concurrency/) — Thread safety of smart pointers
- [Best Practices](../14-best-practices/) — Smart pointer guidelines
- [Modern C++](../08-modern-cpp/) — Smart pointer improvements in C++14/17/20

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Circular reference causing `shared_ptr` memory leak | `shared_ptr::use_count()` + Valgrind | Log `use_count()` at suspicious points; use Valgrind to confirm leaked control blocks |
| Double free from multiple `shared_ptr` from same raw pointer | `std::make_shared` audit + ASan | Never create two `shared_ptr` from the same raw pointer; use `make_shared` exclusively |
| Thread-unsafe `shared_ptr` copy under concurrency | ThreadSanitizer (`-fsanitize=thread`) | Enable TSan; use `std::atomic<shared_ptr<T>>` (C++20) or wrap copies behind a mutex |
| `unique_ptr` custom deleter not being called | Debug build + destructor logging | Add logging in custom deleter; verify deleter is called in debug mode with breakpoints |
| `weak_ptr::lock()` returning nullptr unexpectedly | Expiration logging + ownership audit | Log when `lock()` returns nullptr; trace all `reset()` calls on the owning `shared_ptr` |

## Code Review Checklist

- [ ] `std::unique_ptr` used as the default smart pointer
- [ ] `std::make_unique` and `std::make_shared` used (exception safety)
- [ ] `std::weak_ptr` used to break circular references
- [ ] No multiple `shared_ptr` created from the same raw pointer
- [ ] Custom deleters provided for non-memory resources (FILE*, sockets)
- [ ] `enable_shared_from_this` used when objects need to return `shared_ptr` to `this`
- [ ] `unique_ptr` used for factory functions (caller decides ownership)

## Architecture Considerations

Smart pointers are the RAII foundation of C++ memory management. `unique_ptr` provides zero-overhead exclusive ownership — identical to raw pointers in performance. `shared_ptr` adds reference counting for shared ownership with atomic thread safety on the control block. `weak_ptr` enables observer patterns and breaks circular references. Smart pointers define ownership semantics at the architectural level, making resource management explicit and testable.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| `unique_ptr` with custom deleter | Managing C resources (FILE*, CURL*, sockets) | Automatic cleanup vs. slight verbosity of deleter specification |
| `shared_ptr` + `weak_ptr` for cache | Observing cached objects without preventing destruction | Non-owning observation vs. control block overhead |
| `enable_shared_from_this` | Objects returning `shared_ptr` to themselves | Safe shared ownership vs. requires careful construction via `make_shared` |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| `shared_ptr` dangling reference from `get()` and raw pointer storage | Use-after-free if `shared_ptr` destroyed | Keep the `shared_ptr` alive; never store raw pointer obtained from `.get()` |
| Double free from duplicate control blocks | Memory corruption, exploitable crash | Always use `make_shared` or assign from existing `shared_ptr`; never create two from same raw pointer |
| Thread-unsafe `shared_ptr` copy causing data race | Undefined behavior, intermittent crashes | Use `std::atomic<shared_ptr<T>>` (C++20) or mutex-protected copies |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | `unique_ptr`, `shared_ptr`, `weak_ptr` | Replace raw `new`/`delete` with `make_unique` and `make_shared` |
| C++14 | `std::make_unique` | Use `make_unique` instead of `unique_ptr(new T)` for exception safety |
| C++20 | `std::atomic<shared_ptr<T>>` | Use `std::atomic<shared_ptr>` for thread-safe shared ownership without external mutex |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `std::unique_ptr` / `std::shared_ptr` / `std::weak_ptr` | C++11 | Widely supported |
| `std::make_unique` | C++14 | Widely supported |
| `std::enable_shared_from_this` | C++11 | Widely supported |
| `std::atomic<shared_ptr<T>>` | C++20 | Supported in GCC 11+, Clang 14+, MSVC 19.28+ |

## Interview Questions

1. **When should you use `unique_ptr` vs `shared_ptr`?**: Use `unique_ptr` as the default — it's zero-overhead and expresses exclusive ownership. Use `shared_ptr` only when multiple owners genuinely need to share the same resource. Most designs should prefer `unique_ptr`.
2. **How does `weak_ptr` prevent circular references?**: `weak_ptr` observes an object without incrementing the reference count. When parent and child hold `shared_ptr` to each other, reference counts never reach zero. Breaking one direction with `weak_ptr` allows destruction.
3. **Why is `make_shared` preferred over `new`?**: `make_shared` performs a single allocation (object + control block together), is exception-safe (no leak if second allocation throws), and is faster due to reduced allocator calls.
4. **What is `enable_shared_from_this` and when is it needed?**: It allows an object managed by `shared_ptr` to safely return a `shared_ptr` to itself (`shared_from_this()`). It's needed when an object needs to pass itself to async callbacks or APIs that require `shared_ptr` ownership.
5. **Are smart pointers thread-safe?**: `unique_ptr` is not thread-safe (like raw pointers). `shared_ptr` has an atomic reference count, so copying/releasing is thread-safe, but the managed object is not — you need a mutex for the object itself. C++20 adds `std::atomic<shared_ptr>` for atomic shared pointer operations.

## References

- [Effective Modern C++ — Scott Meyers (Items 18-22)](https://www.amazon.com/Effective-Modern-CUDA-Improve-Specific/dp/1491903996)
- [CppReference — Smart Pointers](https://en.cppreference.com/w/cpp/memory/shared_ptr)
- [C++ Core Guidelines — Smart Pointers](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-resource)
- [CppCon Talk: Smart Pointers in Practice](https://youtube.com/cppcon)
