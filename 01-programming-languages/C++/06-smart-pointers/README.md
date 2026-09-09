# Smart Pointers — C++

## Overview

Smart pointers are RAII wrapper classes introduced in C++11 that manage the lifetime of dynamically allocated objects automatically. They eliminate manual `new`/`delete` calls by tying object ownership to scope — when a smart pointer goes out of scope, its destructor automatically releases the managed resource. C++ provides three primary smart pointers: `std::unique_ptr` for exclusive ownership with zero overhead, `std::shared_ptr` for shared ownership via atomic reference counting, and `std::weak_ptr` for non-owning observation that breaks circular references. Together, they form the backbone of modern C++ memory management.

## Learning Objectives

- Understand the differences between `unique_ptr`, `shared_ptr`, and `weak_ptr`
- Apply the correct smart pointer for each ownership scenario
- Implement custom deleters for non-memory resources (FILE*, sockets, handles)
- Identify and fix circular reference patterns using `weak_ptr`
- Use `enable_shared_from_this` safely for objects that need to return `shared_ptr` to themselves
- Recognize thread safety guarantees and limitations of smart pointers
- Apply best practices for exception-safe smart pointer construction (`make_unique`, `make_shared`)

## Prerequisites

- Familiarity with C++ pointers, references, and heap vs stack allocation
- Understanding of constructors, destructors, and RAII principles
- Basic knowledge of move semantics and `std::move`
- Awareness of undefined behavior (use-after-free, double free, dangling pointers)

## History

Smart pointers evolved to address the chronic bugs caused by manual memory management:

| Era | Pointer | Problem Solved | Limitation |
|-----|---------|---------------|------------|
| C++98 | `std::auto_ptr` | Automatic cleanup via RAII | Confusing copy semantics (transferred ownership silently) |
| C++11 | `std::unique_ptr` | Zero-overhead exclusive ownership | Non-copyable, move-only |
| C++11 | `std::shared_ptr` | Shared ownership with reference counting | Atomic overhead, circular reference risk |
| C++11 | `std::weak_ptr` | Non-owning observation to break cycles | Must check `lock()` for validity |
| C++14 | `std::make_unique` | Exception-safe `unique_ptr` construction | — |
| C++20 | `std::atomic<shared_ptr>` | Thread-safe atomic `shared_ptr` operations | Compiler support required |

`std::auto_ptr` was deprecated in C++11 and removed in C++17. Its silent ownership transfer on copy was a notorious source of bugs. `std::unique_ptr` replaced it with explicit move semantics, making ownership transfers visible and intentional.

## Production Notes

- **Compiler support**: `unique_ptr`, `shared_ptr`, `weak_ptr` are available in all modern C++ compilers (GCC 4.8+, Clang 3.3+, MSVC 2012+). `make_unique` requires C++14.
- **Header required**: `#include <memory>` for all smart pointers.
- **ABI stability**: Smart pointer ABI is stable across major compilers; no recompilation needed when upgrading compilers.
- **Build flags**: Enable `-fsanitize=address` (ASan) in CI to catch memory bugs early. Use `-fsanitize=thread` (TSan) for concurrency issues.
- **Production readiness**: Smart pointers are production-ready and used in major codebases (Chromium, LLVM, WebKit). They are not experimental or optional — they are the standard for modern C++ memory management.
- **Overhead**: `unique_ptr` has zero overhead vs raw pointers. `shared_ptr` adds atomic reference count operations (~10-20ns per copy/release) and a control block allocation.

## Why It Matters

Manual memory management with raw `new`/`delete` is error-prone: forgetting to delete causes leaks, deleting too early causes use-after-free, and deleting twice causes crashes. When you automate memory management using RAII, you eliminate entire categories of memory bugs with automatic cleanup when pointers go out of scope.

## What It Is

Smart pointers are RAII wrappers around raw pointers that automatically manage object lifetime. C++ provides `unique_ptr` for exclusive ownership, `shared_ptr` for shared ownership with reference counting, and `weak_ptr` for non-owning observation.

## Core Concepts

### Ownership Models

| Model | Smart Pointer | Description |
|-------|--------------|-------------|
| Exclusive | `std::unique_ptr<T>` | Single owner; object destroyed when owner goes out of scope or is reset |
| Shared | `std::shared_ptr<T>` | Multiple owners; object destroyed when last owner releases |
| Observer | `std::weak_ptr<T>` | Non-owning reference; does not prevent destruction |

### Control Block

Every `shared_ptr` and `weak_ptr` interacts with a **control block** — a heap-allocated structure that stores:

| Field | Purpose |
|-------|---------|
| **Strong reference count** | Number of `shared_ptr` instances owning the object |
| **Weak reference count** | Number of `weak_ptr` instances observing the object |
| **Deleter** | Custom destruction function (defaults to `delete`) |
| **Allocator** | Custom allocator for the control block itself |
| **Managed object** | (When created via `make_shared`) Stored inline with control block |

The object is destroyed when the strong reference count reaches zero. The control block is deallocated when both strong and weak reference counts reach zero.

### Reference Counting

Reference counting uses atomic operations (`std::atomic<int>`) for thread-safe increments/decrements. This means:

- Copying a `shared_ptr` from multiple threads is safe (reference count is atomic)
- The **managed object** is NOT thread-safe — you need external synchronization
- Atomic operations have a small cost (~10-20ns per operation)

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

## Internal Working

### Control Block Layout

When you create a `shared_ptr` via `std::make_shared<T>()`, a single memory allocation is made for both the control block and the object:

```
┌─────────────────────────────────────────────────┐
│                 Control Block                    │
├──────────────────┬──────────────────────────────┤
│ strong_ref_count │ atomic<int>, thread-safe     │
│ weak_ref_count   │ atomic<int>, thread-safe     │
│ deleter          │ function pointer / lambda    │
│ allocator        │ allocator instance           │
├──────────────────┴──────────────────────────────┤
│              Managed Object (T)                  │
│              (stored inline with control block)  │
└─────────────────────────────────────────────────┘
```

When you create a `shared_ptr` from a raw pointer (`shared_ptr<T>(new T)`), two allocations occur: one for the object, one for the control block.

### Reference Counting Atomicity

Reference count operations use `std::atomic<int>` with memory ordering:

- **Increment** (`shared_ptr` copy): `memory_order_relaxed` — no ordering constraints
- **Decrement** (`shared_ptr` destructor): `memory_order_acq_rel` — acquire on success, release on last decrement
- **Last decrement** triggers: object destructor, then control block deallocation

```
Thread A: copy shared_ptr     → atomic fetch_add(relaxed)     → count: 2
Thread B: destroy shared_ptr  → atomic fetch_sub(acq_rel)     → count: 1
Thread C: destroy shared_ptr  → atomic fetch_sub(acq_rel)     → count: 0
                               → object destructor called
                               → control block deallocated
```

### unique_ptr Internals

`unique_ptr` with the default deleter is a zero-overhead wrapper — it stores only the raw pointer and is identical in size and performance to a raw pointer:

```cpp
static_assert(sizeof(std::unique_ptr<int>) == sizeof(int*));  // true
```

Custom deleters change the size. A function pointer deleter adds 8 bytes (on 64-bit); a stateful lambda may add more.

### make_shared vs new

| Aspect | `make_shared<T>()` | `shared_ptr<T>(new T)` |
|--------|---------------------|------------------------|
| Allocations | 1 (object + control block) | 2 (object, then control block) |
| Exception safety | Safe (single allocation) | Unsafe (leak if second alloc throws) |
| Cache locality | Better (object near control block) | Worse (scattered allocations) |
| Memory overhead | Lower | Higher |

## Syntax

### Header

```cpp
#include <memory>
```

### Construction

```cpp
// unique_ptr
auto up1 = std::make_unique<T>(args...);         // Preferred (C++14)
std::unique_ptr<T> up2(new T(args...));           // Also works
std::unique_ptr<T> up3 = std::move(up2);         // Move ownership
std::unique_ptr<T> up4;                           // Empty (nullptr)

// shared_ptr
auto sp1 = std::make_shared<T>(args...);         // Preferred
std::shared_ptr<T> sp2(sp1);                      // Copy (ref count++)
std::shared_ptr<T> sp3 = sp1;                     // Copy (ref count++)
std::shared_ptr<T> sp4(new T(args...));           // From raw pointer (unsafe)
std::shared_ptr<T> sp5;                           // Empty (nullptr)

// weak_ptr
std::weak_ptr<T> wp1 = sp1;                      // Observe shared_ptr
std::weak_ptr<T> wp2;                            // Empty
auto sp6 = wp1.lock();                           // Attempt to get shared_ptr
```

### Common Operations

```cpp
// unique_ptr
up.get();              // Get raw pointer (non-owning)
up.release();          // Release ownership, return raw pointer
up.reset();            // Destroy managed object, set to nullptr
up.reset(new T());     // Replace managed object
up.swap(other);        // Swap ownership

// shared_ptr
sp.get();              // Get raw pointer (non-owning)
sp.use_count();        // Number of shared_ptr owners
sp.unique();           // True if use_count() == 1
sp.reset();            // Decrement ref count, destroy if last
sp.reset(new T());     // Replace managed object
sp.swap(other);        // Swap ownership

// weak_ptr
wp.expired();          // True if managed object destroyed
wp.lock();             // Returns shared_ptr (nullptr if expired)
wp.use_count();        // Number of shared_ptr owners
wp.reset();            // Clear weak reference
wp.swap(other);        // Swap references
```

### Aliasing Constructor

```cpp
auto parent = std::make_shared<Parent>();
auto member = std::shared_ptr<Member>(parent, &parent->member);
// 'member' shares ownership with 'parent' but points to parent->member
```

### Custom Deleters

```cpp
// unique_ptr with custom deleter
std::unique_ptr<FILE, decltype(&fclose)> file(fopen("test.txt", "r"), &fclose);

// shared_ptr with custom deleter
std::shared_ptr<FILE> file(fopen("test.txt", "r"), &fclose);
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

## Performance Considerations

### Overhead Comparison

| Pointer Type | Size | Allocation | Copy Cost | Destroy Cost |
|-------------|------|------------|-----------|--------------|
| Raw `T*` | 8 bytes | None | None | None |
| `unique_ptr<T>` | 8 bytes | None | None | None |
| `unique_ptr<T, Deleter>` | 8+ bytes | None | None | None |
| `shared_ptr<T>` | 16 bytes | 1-2 allocs | Atomic inc | Atomic dec + possible dealloc |
| `weak_ptr<T>` | 16 bytes | None | Atomic inc | Atomic dec |

### Benchmark Guidelines

```cpp
// unique_ptr is zero-overhead — identical to raw pointer
auto up = std::make_unique<int>(42);   // Same speed as: int* p = new int(42);
delete p;                              // Same speed as: up.reset();

// shared_ptr overhead: ~10-20ns per copy/release (atomic operations)
auto sp = std::make_shared<int>(42);   // Slightly slower than new
auto sp2 = sp;                         // Atomic increment (~10ns)
sp.reset();                            // Atomic decrement (~10ns)
```

### Performance Tips

1. **Prefer `unique_ptr`** — zero overhead, no atomic operations, no control block
2. **Use `make_shared`** — single allocation instead of two; better cache locality
3. **Avoid unnecessary copies** — each `shared_ptr` copy is an atomic increment
4. **Use `weak_ptr` sparingly** — `lock()` involves atomic operations and a null check
5. **Consider raw pointers for hot paths** — if ownership is managed externally, raw pointers avoid all overhead
6. **Profile with benchmarks** — overhead is negligible in most real-world code

### Memory Footprint

```
unique_ptr<T>:                    8 bytes  (pointer only)
shared_ptr<T>:                   16 bytes  (pointer + control block pointer)
weak_ptr<T>:                     16 bytes  (pointer + control block pointer)
shared_ptr<T> with make_shared:  ~8 bytes + sizeof(T) + control block (~24 bytes)
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

## Examples

### Example 1: Factory Pattern with unique_ptr

```cpp
#include <memory>
#include <iostream>

class Animal {
public:
    virtual ~Animal() = default;
    virtual void speak() const = 0;
};

class Dog : public Animal {
public:
    void speak() const override { std::cout << "Woof!\n"; }
};

class Cat : public Animal {
public:
    void speak() const override { std::cout << "Meow!\n"; }
};

std::unique_ptr<Animal> createAnimal(const std::string& type) {
    if (type == "dog") return std::make_unique<Dog>();
    if (type == "cat") return std::make_unique<Cat>();
    return nullptr;
}

int main() {
    auto pet = createAnimal("dog");
    pet->speak();  // Woof!
    // Ownership automatically released when pet goes out of scope
}
```

### Example 2: Observer Pattern with weak_ptr

```cpp
#include <memory>
#include <vector>
#include <iostream>

class Observer {
public:
    virtual void update(int value) = 0;
    virtual ~Observer() = default;
};

class Subject {
    std::vector<std::weak_ptr<Observer>> observers_;
    int value_ = 0;
public:
    void attach(std::shared_ptr<Observer> obs) {
        observers_.push_back(obs);
    }

    void notify() {
        for (auto it = observers_.begin(); it != observers_.end();) {
            if (auto obs = it->lock()) {
                obs->update(value_);
                ++it;
            } else {
                it = observers_.erase(it);  // Remove dead observer
            }
        }
    }

    void setValue(int v) {
        value_ = v;
        notify();
    }
};
```

### Example 3: Resource Management with unique_ptr Custom Deleter

```cpp
#include <memory>
#include <cstdio>

struct FileDeleter {
    void operator()(FILE* fp) const {
        if (fp) {
            std::cout << "Closing file\n";
            fclose(fp);
        }
    }
};

void processFile(const char* filename) {
    std::unique_ptr<FILE, FileDeleter> file(fopen(filename, "r"));
    if (!file) return;

    // Use file.get() to access raw FILE*
    char buffer[256];
    while (fgets(buffer, sizeof(buffer), file.get())) {
        std::cout << buffer;
    }
    // File automatically closed when file goes out of scope
}
```

### Example 4: Thread-Safe Cache with shared_ptr and weak_ptr

```cpp
#include <memory>
#include <unordered_map>
#include <mutex>
#include <string>

class Resource {
    std::string data_;
public:
    Resource(const std::string& data) : data_(data) {}
    const std::string& data() const { return data_; }
};

class Cache {
    std::unordered_map<std::string, std::weak_ptr<Resource>> cache_;
    mutable std::mutex mutex_;
public:
    std::shared_ptr<Resource> get(const std::string& key) {
        std::lock_guard<std::mutex> lock(mutex_);
        auto it = cache_.find(key);
        if (it != cache_.end()) {
            if (auto sp = it->second.lock()) {
                return sp;  // Still alive
            }
        }
        // Create new resource and cache it
        auto sp = std::make_shared<Resource>(key);
        cache_[key] = sp;  // weak_ptr doesn't prevent destruction
        return sp;
    }
};
```

### Example 5: enable_shared_from_this for Async Operations

```cpp
#include <memory>
#include <future>
#include <iostream>

class Connection : public std::enable_shared_from_this<Connection> {
public:
    void startAsyncRead() {
        // Capture shared_ptr to keep connection alive during async operation
        auto self = shared_from_this();
        std::async(std::launch::async, [self]() {
            // Connection stays alive as long as lambda exists
            std::cout << "Reading data...\n";
            // Process data...
        });
    }

    static std::shared_ptr<Connection> create() {
        // Must use make_shared or shared_ptr constructor
        // Don't return std::shared_ptr<Connection>(new Connection())
        return std::make_shared<Connection>();
    }
};

int main() {
    auto conn = Connection::create();
    conn->startAsyncRead();  // Safe: async lambda holds shared_ptr
}
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

## Best Practices

### Golden Rules

1. **Default to `unique_ptr`** — Use it unless you specifically need shared ownership
2. **Use `make_unique` / `make_shared`** — Exception-safe construction; single allocation for `make_shared`
3. **Use `weak_ptr` to break cycles** — Any back-reference (child→parent in a tree) should be `weak_ptr`
4. **Prefer stack allocation** — Only use heap allocation (and thus smart pointers) when you must
5. **Express ownership in types** — `unique_ptr` = exclusive, `shared_ptr` = shared, `T*` = non-owning observer

### Anti-Patterns to Avoid

| Anti-Pattern | Problem | Solution |
|-------------|---------|----------|
| `shared_ptr<T>(new T)` repeated | Double free (separate control blocks) | Use `make_shared` or assign from existing `shared_ptr` |
| Storing `.get()` result | Dangling pointer if `shared_ptr` destroyed | Keep the `shared_ptr` alive |
| Passing `unique_ptr` by value | Ownership accidentally transferred | Pass by reference or raw pointer |
| Using `shared_ptr` for everything | Unnecessary overhead + circular reference risk | Use `unique_ptr` as default |

### Exception Safety

```cpp
// BAD: Leaks if第二个参数抛出异常
process(std::shared_ptr<Widget>(new Widget()), compute());

// GOOD: make_shared is exception-safe
process(std::make_shared<Widget>(), compute());

// GOOD: unique_ptr with make_unique
auto w = std::make_unique<Widget>();
process(std::move(w), compute());
```

### Factory Pattern

```cpp
// Return unique_ptr from factories (caller decides ownership)
std::unique_ptr<Shape> createShape(Type type) {
    switch (type) {
        case Type::Circle: return std::make_unique<Circle>();
        case Type::Square: return std::make_unique<Square>();
    }
    return nullptr;
}

// Use shared_ptr only when the factory doesn't own the object
std::shared_ptr<Connection> getConnection() {
    static auto pool = std::make_shared<ConnectionPool>();
    return pool->acquire();  // Pool retains ownership
}
```

## Real-World Production Examples
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

## Common Mistakes (Detailed)

### Mistake 1: Multiple shared_ptrs from Same Raw Pointer

```cpp
int* raw = new int(42);
std::shared_ptr<int> sp1(raw);  // Control block A, ref count = 1
std::shared_ptr<int> sp2(raw);  // Control block B, ref count = 1
// When sp1 and sp2 go out of scope: DOUBLE FREE (two control blocks, one object)

// FIX: Always use make_shared or assign from existing shared_ptr
auto sp1 = std::make_shared<int>(42);
std::shared_ptr<int> sp2 = sp1;  // Same control block, ref count = 2
```

### Mistake 2: Circular References with shared_ptr

```cpp
struct Node {
    std::shared_ptr<Node> next;  // Strong reference to next
};

auto a = std::make_shared<Node>();
auto b = std::make_shared<Node>();
a->next = b;  // a owns b
b->next = a;  // b owns a → CIRCULAR REFERENCE

// Reference counts: a = 2, b = 2
// When a and b go out of scope: ref counts drop to 1, never reach 0 → MEMORY LEAK

// FIX: Use weak_ptr for one direction
struct Node {
    std::shared_ptr<Node> next;   // Strong: parent owns child
    std::weak_ptr<Node> prev;     // Weak: child observes parent
};
```

### Mistake 3: Using .get() and Storing Raw Pointer

```cpp
std::shared_ptr<int> sp = std::make_shared<int>(42);
int* raw = sp.get();  // Raw pointer extracted

sp.reset();  // Object destroyed, raw pointer now DANGLING
*raw = 100;  // USE-AFTER-FREE (undefined behavior)

// FIX: Keep the shared_ptr alive, or don't store .get() result
```

### Mistake 4: shared_ptr and Arrays

```cpp
std::shared_ptr<int> arr(new int[10]);  // WRONG: calls delete, not delete[]
// Undefined behavior: delete on array allocated with new[]

// FIX 1: Use custom deleter
std::shared_ptr<int> arr1(new int[10], std::default_delete<int[]>());

// FIX 2: Use std::vector (preferred)
std::vector<int> arr2(10);
```

### Mistake 5: Thread-Unsafe shared_ptr Copy

```cpp
// Thread A and Thread B both share this shared_ptr
std::shared_ptr<Widget> shared_widget = std::make_shared<Widget>();

// Thread A:                    // Thread B:
auto local = shared_widget;    auto local2 = shared_widget;
// Both threads increment ref count simultaneously
// Atomic operations on the CONTROL BLOCK are safe
// BUT: the shared_ptr VARIABLE itself is not thread-safe for concurrent read/write

// FIX: Use std::atomic<shared_ptr<T>> (C++20) or protect with mutex
std::atomic<std::shared_ptr<Widget>> atomic_widget;
```

### Mistake 6: Custom Deleter Bugs

```cpp
// BUG: Wrong deleter for type
std::shared_ptr<int> p(new int[10], [](int* p) { delete p; });  // Should be delete[]

// BUG: Deleter captures dangling reference
std::string name = "file.txt";
auto file = std::unique_ptr<FILE, decltype([&](FILE* f) { fclose(f); })>(
    fopen(name.c_str(), "r"), [&](FILE* f) { fclose(f); });
// Lambda captures name by reference — undefined behavior if name destroyed first

// FIX: Capture by value or use stateless lambda
auto file = std::unique_ptr<FILE, decltype([](FILE* f) { fclose(f); })>(
    fopen("file.txt", "r"), [](FILE* f) { fclose(f); });
```

### Mistake 7: enable_shared_from_this Without shared_ptr

```cpp
class Widget : public std::enable_shared_from_this<Widget> {
public:
    void bad() {
        auto sp = shared_from_this();  // THROWS std::bad_weak_ptr
        // Object not managed by shared_ptr yet!
    }
};

// FIX: Only call shared_from_this() when object is already managed by shared_ptr
auto w = std::make_shared<Widget>();
w->bad();  // Safe: object is managed by shared_ptr
```

### Mistake 8: Passing unique_ptr by Value

```cpp
void process(std::unique_ptr<Widget> w) {  // Ownership transferred!
    // Widget destroyed when function returns
}

auto widget = std::make_unique<Widget>();
process(widget);          // Ownership transferred — widget is now nullptr
process(std::move(widget)); // Explicit transfer — OK

// FIX: Pass by reference or raw pointer if you don't want to transfer ownership
void observe(const Widget& w);       // Non-owning
void observe(Widget* w);             // Non-owning (raw pointer)
```

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

### Incident 3: Circular Reference in Event System Causing Memory Leak
**Problem**: An event dispatcher system leaked memory proportional to the number of registered handlers. Memory grew continuously under load.

**Cause**: Event handlers stored `shared_ptr` back to the dispatcher (for unregistering), while the dispatcher stored `shared_ptr` to each handler. This created circular references — handler → dispatcher → handler. Reference counts never reached zero.

**Solution**: Changed the dispatcher's reference to handlers from `shared_ptr` to `weak_ptr`. Handlers could still unregister themselves by locking the `weak_ptr`, but the cycle was broken. Memory usage stabilized immediately.

---

### Incident 4: Thread Safety Bug in shared_ptr Usage Under High Concurrency
**Problem**: A connection pool crashed intermittently under high load with a segfault in `__shared_ptr`'s destructor.

**Cause**: Two threads were reading and writing the same `shared_ptr<Connection>` variable simultaneously. While the reference count operations are atomic, the `shared_ptr` object itself is not thread-safe for concurrent read/write access. One thread was resetting the pointer while another was copying it.

**Solution**: Protected all `shared_ptr` accesses with a `std::mutex`. For performance-critical paths, migrated to `std::atomic<std::shared_ptr<T>>` (C++20) which provides atomic load/store operations. Added TSan checks to CI to prevent regression.

---

### Incident 5: Custom Deleter Bug Causing Use-After-Free
**Problem**: A file handler wrapper crashed intermittently with use-after-free. The crash appeared random and was difficult to reproduce.

**Cause**: The custom deleter for a `unique_ptr<FILE>` captured a `std::string` by reference. The `string` was a local variable in the constructor, and the lambda's reference to it became dangling after the constructor returned. When the `unique_ptr` was destroyed, the deleter accessed the dangling reference to log the filename.

**Solution**: Changed the lambda to capture the filename by value (or used a stateless lambda). Also replaced raw `FILE*` with a wrapper struct that stored the filename internally. Added a static_assert to ensure custom deleters are stateless or properly owned.

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

## Cross-References

| Topic | Related Module | Relevance |
|-------|---------------|-----------|
| RAII & Ownership | [Memory Management](../05-memory-management/) | Smart pointers are RAII wrappers — understanding RAII is essential |
| Move Semantics | [Modern C++](../08-modern-cpp/) | `unique_ptr` relies on move semantics for ownership transfer |
| Thread Safety | [Concurrency](../07-concurrency/) | `shared_ptr` reference count is atomic; managed object is not |
| Exception Safety | [Best Practices](../14-best-practices/) | `make_shared`/`make_unique` provide exception-safe construction |
| Design Patterns | [Design Patterns](../13-design-patterns/) | Factory, Observer, and Pimpl patterns use smart pointers extensively |
| STL Containers | [STL Containers](../09-stl-containers/) | `std::vector<std::unique_ptr<T>>` is a common ownership container |
| Lambda Expressions | [Modern C++](../08-modern-cpp/) | Custom deleters often use lambdas |

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
6. **What is the control block in `shared_ptr` and what does it contain?**: The control block is a heap-allocated structure shared by all `shared_ptr` and `weak_ptr` instances pointing to the same object. It contains: strong reference count (number of `shared_ptr` owners), weak reference count (number of `weak_ptr` observers), the custom deleter, and optionally the allocator. When created via `make_shared`, the object itself is stored inline with the control block for better cache locality.
7. **What is the aliasing constructor of `shared_ptr` and when would you use it?**: The aliasing constructor creates a `shared_ptr` that shares ownership with another `shared_ptr` but points to a different object (typically a sub-object or member). It's useful when you want a `shared_ptr` to a member variable while keeping the parent object alive. The reference count is shared, so the parent is destroyed only when all aliased `shared_ptr` instances are released. Syntax: `shared_ptr<T>(existing_shared_ptr, &existing_shared_ptr->member)`.
8. **What happens if you create two `shared_ptr` from the same raw pointer?**: Each `shared_ptr` gets its own control block with an independent reference count. When both go out of scope, each calls `delete` on the same pointer, causing a double free. This is undefined behavior and typically crashes. Always use `make_shared` or assign from an existing `shared_ptr`.
9. **What is the difference between `unique_ptr` and `shared_ptr` in terms of performance?**: `unique_ptr` has zero overhead — it's identical to a raw pointer in size (8 bytes) and performance. `shared_ptr` is 16 bytes (two pointers), requires atomic reference count operations (~10-20ns per copy/release), and may involve a heap allocation for the control block. Use `unique_ptr` as the default; use `shared_ptr` only when shared ownership is genuinely needed.
10. **Can you use `unique_ptr` with arrays?**: Yes, use `std::unique_ptr<T[]>` which calls `delete[]` instead of `delete`. Alternatively, use `std::vector` which is preferred for most array use cases. Note that `unique_ptr<T>` (without `[]`) will call `delete` on an array, which is undefined behavior.
11. **How do you prevent memory leaks with smart pointers in circular data structures?**: Use `weak_ptr` for back-references (e.g., child→parent) while keeping `shared_ptr` for forward references (e.g., parent→child). This ensures the reference count can reach zero when external owners release the root object. The pattern is: strong references go "down" the hierarchy, weak references go "up."
12. **What are the thread safety guarantees of `shared_ptr`?**: (1) Reference count operations (copy, reset, destructor) are atomic and thread-safe. (2) The managed object is NOT thread-safe — concurrent reads/writes to the object require external synchronization. (3) `shared_ptr` object itself is NOT thread-safe for concurrent read/write of the same instance — use `std::atomic<shared_ptr<T>>` (C++20) or a mutex. (4) Multiple threads can safely call `use_count()` on different `shared_ptr` instances pointing to the same object.
13. **When should you use a custom deleter for `unique_ptr`?**: Use a custom deleter when managing non-memory resources that require specific cleanup: FILE* (fclose), sockets (close), OS handles (CloseHandle), CURL* (curl_easy_cleanup). The deleter can be a function pointer, functor, or lambda. Stateless lambdas are preferred as they don't increase `unique_ptr` size.
14. **What is the difference between `reset()` and `release()` on `unique_ptr`?**: `reset()` destroys the managed object and sets the pointer to nullptr. `release()` releases ownership and returns the raw pointer without destroying the object — the caller becomes responsible for cleanup. Use `release()` when transferring to a C API that manages its own memory.
15. **How does `make_shared` improve exception safety compared to `new`?**: With `new`, if the second allocation (for the control block) throws after the object is allocated, the object leaks. `make_shared` performs a single allocation for both, so there's no window for a leak. This follows the "single allocation" rule for exception safety. Additionally, `make_shared` provides better cache locality and reduced allocator overhead.

## References

- [Effective Modern C++ — Scott Meyers (Items 18-22)](https://www.amazon.com/Effective-Modern-CUDA-Improve-Specific/dp/1491903996)
- [CppReference — Smart Pointers](https://en.cppreference.com/w/cpp/memory/shared_ptr)
- [C++ Core Guidelines — Smart Pointers](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-resource)
- [CppCon Talk: Smart Pointers in Practice](https://youtube.com/cppcon)
