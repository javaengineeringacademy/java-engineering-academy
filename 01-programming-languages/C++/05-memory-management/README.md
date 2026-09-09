# Memory Management — C++

## Overview

C++ provides manual memory management through stack allocation, heap allocation (`new`/`delete`), and the RAII principle. Unlike garbage-collected languages, C++ programmers are responsible for allocating and deallocating memory correctly. This module covers stack vs heap allocation, RAII, memory leaks, dangling pointers, alignment, placement new, and heap allocator internals. Understanding these concepts is essential for writing safe, performant, and maintainable C++ code.

## Why It Matters

C++ gives you direct control over memory — the source of both its power and its danger. When you don't understand memory management, programs leak resources, crash with segmentation faults, and exhibit undefined behavior that manifests differently on every platform. Memory bugs are the hardest bugs to find and the most expensive to fix.

## What It Is

Memory management in C++ covers allocating, using, and deallocating memory, including stack vs heap allocation, the RAII principle, manual memory management with `new`/`delete`, and understanding storage durations and lifetimes.

## Learning Objectives

1. Understand the differences between stack and heap allocation and when to use each
2. Apply the RAII principle to prevent resource leaks and ensure exception safety
3. Identify and prevent memory leaks, dangling pointers, double frees, and use-after-free bugs
4. Use placement new for custom allocators and memory pools
5. Recognize memory alignment requirements and their performance implications
6. Profile and debug memory issues using Valgrind, AddressSanitizer, and LeakSanitizer
7. Design memory-efficient architectures using pool allocators, arena allocators, and smart pointers

## Prerequisites

- [C++ Fundamentals](../02-core-language/) — Pointers, references, and basic types
- [Object-Oriented Programming](../03-oop/) — Classes, constructors, and destructors
- [Pointers and References](../04-pointers/) — Pointer arithmetic and address-of operator
- Basic understanding of how programs use memory (stack frames, heap, global data)

## History

| Year | Milestone | Impact |
|------|-----------|--------|
| 1979 | C++ (then "C with Classes") inherits C's `malloc`/`free` | Manual memory management from day one |
| 1985 | First C++ release with `new`/`delete` operators | Type-safe allocation, constructor/destructor integration |
| 1998 | C++98 standardization | RAII becomes idiomatic; `auto_ptr` introduced (flawed) |
| 2011 | C++11 introduces `unique_ptr`, `shared_ptr`, move semantics | Smart pointers replace manual `new`/`delete` |
| 2014 | C++14 adds `std::make_unique` | Safer allocation (prevents leak if constructor throws) |
| 2017 | C++17 adds `std::pmr::memory_resource` | Polymorphic allocators for custom memory strategies |
| 2020 | C++20 adds `std::span` | Non-owning memory views replace raw pointer + size pairs |

## Production Notes

- **RAII is mandatory in production code**: Raw `new`/`delete` in application code is a code smell. Use `std::unique_ptr`, `std::shared_ptr`, or RAII wrappers for all resources.
- **Run sanitizers in CI**: Enable `-fsanitize=address,undefined` and LeakSanitizer in continuous integration to catch memory bugs before they reach production.
- **Profile memory in long-running processes**: Servers, databases, and embedded systems should monitor heap usage, allocation rates, and fragmentation over time.
- **Prefer stack allocation**: Stack allocation is ~100x faster and automatically freed. Only use heap when you genuinely need dynamic lifetime or size.
- **Use memory pools for high-frequency allocations**: Arena and pool allocators eliminate per-allocation overhead and reduce fragmentation in hot paths.

## Architecture: How Memory Management Fits Together

```
┌─────────────────────────────────────────────────────────────┐
│              C++ Memory Management                           │
├───────────────┬───────────────┬─────────────────────────────┤
│    Stack      │     Heap      │     Static / Thread-Local   │
│ (Automatic)   │  (Dynamic)    │     (Lifetime: program)     │
├───────────────┴───────────────┴─────────────────────────────┤
│                    RAII Principle                             │
│         Tie resource lifetime to object lifetime              │
├─────────────────────────────────────────────────────────────┤
│              Smart Pointers (Module 06)                       │
│     unique_ptr / shared_ptr / weak_ptr                        │
├─────────────────────────────────────────────────────────────┤
│              Custom Allocators & Memory Pools                 │
└─────────────────────────────────────────────────────────────┘
```

## Internal Working

### How `malloc`/`free` Work

`malloc` and `free` are C's heap allocation functions. Understanding them helps you understand C++ `new`/`delete`, which builds on top of them.

#### `malloc` Internals

```
malloc(size) request:
┌─────────────────────────────────────────────────────┐
│ 1. Thread-local cache (tcmalloc/jemalloc)           │
│    → Fast path: no lock needed                       │
├─────────────────────────────────────────────────────┤
│ 2. Size class lookup                                 │
│    → Round up to nearest size class (8, 16, 32, ...) │
├─────────────────────────────────────────────────────┤
│ 3. Free list search                                  │
│    → Find a free block of the requested size class   │
├─────────────────────────────────────────────────────┤
│ 4. System call (sbrk/mmap)                           │
│    → If no free block: request more memory from OS   │
└─────────────────────────────────────────────────────┘
```

#### `free` Internals

```
free(ptr) request:
┌─────────────────────────────────────────────────────┐
│ 1. Header lookup                                     │
│    → Find block metadata from pointer                │
├─────────────────────────────────────────────────────┤
│ 2. Coalescing                                       │
│    → Merge with adjacent free blocks if possible     │
├─────────────────────────────────────────────────────┤
│ 3. Return to free list                              │
│    → Block added to size class free list             │
├─────────────────────────────────────────────────────┤
│ 4. Optional: Return to OS                           │
│    → Large blocks returned via sbrk/munmap           │
└─────────────────────────────────────────────────────┘
```

### C++ `new`/`delete` Internals

`new` and `delete` are C++ operators that wrap `malloc`/`free` and add constructor/destructor calls:

```cpp
// What the compiler generates for:
MyClass* p = new MyClass(args);
delete p;

// new operator does:
// 1. void* mem = malloc(sizeof(MyClass));  // Allocate raw memory
// 2. MyClass* p = static_cast<MyClass*>(mem);
// 3. p->MyClass::MyClass(args);             // Call constructor
// return p;

// delete operator does:
// 1. p->~MyClass();                         // Call destructor
// 2. free(p);                               // Deallocate memory
```

### Heap Allocator Architecture

Modern allocators use multiple strategies to balance speed, fragmentation, and memory usage:

```
┌──────────────────────────────────────────────────────────────┐
│                    Heap Allocator                             │
├──────────────────┬──────────────────┬────────────────────────┤
│  Thread-Local    │   Central Free   │    System Memory       │
│  Cache (tcmalloc)│   Lists          │    (sbrk/mmap)         │
│  - No lock       │   - Size classes │    - Page granularity  │
│  - Fast path     │   - Lock per     │    - OS interaction    │
│  - Fixed sizes   │     class        │                        │
└──────────────────┴──────────────────┴────────────────────────┘
```

### Custom Allocators

```cpp
// Arena allocator: allocate from a pre-allocated block
class Arena {
    char* buffer_;
    size_t offset_ = 0;
    size_t size_;
public:
    Arena(size_t size) : buffer_(new char[size]), size_(size) {}

    void* allocate(size_t size, size_t align = alignof(std::max_align_t)) {
        // Align offset
        offset_ = (offset_ + align - 1) & ~(align - 1);
        void* ptr = buffer_ + offset_;
        offset_ += size;
        return ptr;
    }

    void reset() { offset_ = 0; }  // Bulk deallocation

    ~Arena() { delete[] buffer_; }
};

// Usage: allocate many objects, free all at once
Arena arena(1024 * 1024);  // 1MB arena
for (int i = 0; i < 1000; ++i) {
    auto* obj = static_cast<MyClass*>(arena.allocate(sizeof(MyClass)));
    new (obj) MyClass();  // Placement new
}
// All objects freed at once:
arena.reset();
```

## Syntax

### Basic Allocation and Deallocation

```cpp
// Single object
int* p = new int(42);       // Allocate and initialize
delete p;                    // Deallocate

// Array
int* arr = new int[100];     // Allocate array
delete[] arr;                // Deallocate array (MUST use delete[])

// With initialization
int* p = new int(0);         // Initialized to 0
double* d = new double(3.14);
```

### Smart Pointer Syntax (Preferred)

```cpp
// unique_ptr — exclusive ownership
auto p = std::make_unique<int>(42);
auto arr = std::make_unique<int[]>(100);

// shared_ptr — shared ownership
auto p = std::make_shared<int>(42);

// weak_ptr — non-owning reference
std::weak_ptr<int> wp = p;
if (auto sp = wp.lock()) { /* use sp */ }
```

### Placement New Syntax

```cpp
char buffer[sizeof(int)];
int* p = new (buffer) int(42);  // Construct at address
p->~int();                       // Must call destructor manually
```

### Alignment Syntax

```cpp
struct alignas(64) CacheLine {   // Aligned to 64 bytes
    int data[16];
};

alignas(16) int aligned_var;     // Stack variable aligned to 16 bytes
```

## Examples

### Example 1: RAII Resource Management

```cpp
#include <memory>
#include <vector>
#include <string>

class Database {
    std::string connection_;
public:
    Database(const std::string& conn) : connection_(conn) {
        std::cout << "Connected to " << connection_ << "\n";
    }
    ~Database() {
        std::cout << "Disconnected from " << connection_ << "\n";
    }
    void query(const std::string& sql) { /* ... */ }
};

void processRecords() {
    auto db = std::make_unique<Database>("postgres://localhost/mydb");
    db->query("SELECT * FROM users");
    // db automatically disconnected when function returns
}
```

### Example 2: Memory Pool for Game Objects

```cpp
template <typename T, size_t PoolSize = 1024>
class Pool {
    union Slot {
        T object;
        Slot* next;
        Slot() {}
        ~Slot() {}
    };

    Slot pool_[PoolSize];
    Slot* freeList_ = nullptr;

public:
    Pool() {
        for (size_t i = 0; i < PoolSize - 1; ++i) {
            pool_[i].next = &pool_[i + 1];
        }
        pool_[PoolSize - 1].next = nullptr;
        freeList_ = &pool_[0];
    }

    template <typename... Args>
    T* construct(Args&&... args) {
        if (!freeList_) return nullptr;
        Slot* slot = freeList_;
        freeList_ = freeList_->next;
        return new (&slot->object) T(std::forward<Args>(args)...);
    }

    void destroy(T* obj) {
        obj->~T();
        Slot* slot = reinterpret_cast<Slot*>(obj);
        slot->next = freeList_;
        freeList_ = slot;
    }
};

// Usage: zero heap allocations for game entities
Pool<Entity, 4096> entityPool;
auto* enemy = entityPool.construct("Goblin", 100, 10);
entityPool.destroy(enemy);
```

### Example 3: Preventing Dangling References

```cpp
// BAD: returns reference to local variable
const std::string& bad() {
    std::string s = "hello";
    return s;  // Dangling reference!
}

// GOOD: returns by value (move semantics)
std::string good() {
    std::string s = "hello";
    return s;  // Moved, no copy
}

// GOOD: smart pointer for heap allocation
std::unique_ptr<std::string> good2() {
    return std::make_unique<std::string>("hello");
}
```

## Core Concepts

## Stack vs Heap

### The Problem Stack vs Heap Solves
Different data has different lifetime and size requirements. Stack allocation is fast but limited; heap allocation is flexible but slower.

```cpp
void function() {
    int stack_var = 42;                    // Stack: automatic lifetime
    int* heap_var = new int(100);          // Heap: manual lifetime

    // Use both...
    int result = stack_var + *heap_var;

    delete heap_var;                       // MUST free heap memory
}  // stack_var automatically destroyed here
```

### When to Use Stack
- Small objects (< 1MB)
- Objects with well-defined, block-scoped lifetime
- Performance-critical code (stack allocation is ~100x faster than heap)

### When to Use Heap
- Large objects (arrays > 1MB)
- Objects that outlive their creating scope
- Polymorphic objects (base pointer to derived)
- Dynamic data structures (linked lists, trees)

## RAII (Resource Acquisition Is Initialization)

### The Problem RAII Solves
Manual resource management is error-prone — you must remember to free resources in every code path, including error paths and early returns. RAII ties resource lifetime to object scope, making cleanup automatic.

```cpp
class FileHandler {
    FILE* file_;
public:
    FileHandler(const char* filename) : file_(fopen(filename, "r")) {
        if (!file_) throw std::runtime_error("Cannot open file");
    }

    ~FileHandler() {
        if (file_) fclose(file_);  // Always cleaned up
    }

    // Prevent copying (RAII objects shouldn't be copied)
    FileHandler(const FileHandler&) = delete;
    FileHandler& operator=(const FileHandler&) = delete;

    // Allow moving
    FileHandler(FileHandler&& other) noexcept : file_(other.file_) {
        other.file_ = nullptr;
    }

    std::string readLine() {
        char buf[256];
        if (fgets(buf, sizeof(buf), file_)) {
            return std::string(buf);
        }
        return "";
    }
};

void processFile(const char* path) {
    FileHandler fh(path);    // Resource acquired
    auto line = fh.readLine();
    if (line.empty()) {
        return;               // Resource automatically released!
    }
    // Process line...
}  // fh destructor runs here — file closed
```

### RAII Beyond Memory

```cpp
// Mutex locking
void threadSafeFunction() {
    std::lock_guard<std::mutex> lock(mtx);  // Lock acquired
    // Critical section...
}  // Lock automatically released

// Network socket
void fetchData(const std::string& url) {
    Socket sock(url);  // Socket opened
    sock.send(request);
    auto response = sock.receive();
}  // Socket automatically closed

// Database transaction
void transfer(Account& from, Account& to, double amount) {
    Transaction tx(db);  // Transaction started
    from.debit(amount);
    to.credit(amount);
    tx.commit();  // Or auto-rolled-back if exception
}
```

## Memory Leaks

### The Problem Memory Leaks Cause
Leaked memory is never returned to the system. In long-running processes (servers, games, databases), leaks grow until the process crashes or the system runs out of memory.

```cpp
// LEAK: Memory never freed
void leak() {
    int* p = new int[1000];
    // Forgot to delete — leaked on every call
}

// LEAK: Exception before delete
void leakOnException() {
    int* p = new int(42);
    riskyOperation();  // If this throws, p is leaked
    delete p;
}

// FIX: RAII
void noLeak() {
    auto p = std::make_unique<int[]>(1000);  // Auto-freed
    riskyOperation();  // Even if this throws, p is freed
}
```

### Detection Tools

```bash
# Valgrind — memory leak detection
valgrind --leak-check=full --show-leak-kinds=all ./program

# AddressSanitizer — fast runtime detection
g++ -fsanitize=address -g -o program program.cpp
./program

# LeakSanitizer (included with ASan)
LSAN_OPTIONS=print_suppressions=0 ./program
```

## Dangling Pointers

```cpp
// DANGLING: Pointer to destroyed object
int* dangling() {
    int local = 42;
    return &local;  // Returns address of destroyed stack variable
}

// DANGLING: Pointer to freed memory
int* dangling2() {
    int* p = new int(42);
    delete p;
    return p;  // Pointer to freed memory
}

// FIX: Use smart pointers or ensure lifetime exceeds usage
std::unique_ptr<int> safe() {
    return std::make_unique<int>(42);  // Caller owns the memory
}
```

## Alignment

```cpp
// Structs may have padding for alignment
struct Packed {
    char a;    // 1 byte
    int b;     // 4 bytes — 3 bytes padding before b
};

struct Optimized {
    int b;     // 4 bytes
    char a;    // 1 byte + 3 bytes padding at end
};

// Check sizes
static_assert(sizeof(Packed) == 8);    // 1 + 3padding + 4
static_assert(sizeof(Optimized) == 8); // 4 + 1 + 3padding

// Force alignment
struct alignas(64) CacheLine {
    int data[16];  // Exactly 64 bytes — one cache line
};
```

## Placement New

```cpp
// Construct object at specific memory address
char buffer[sizeof(int)];
int* p = new (buffer) int(42);  // Placement new
std::cout << *p << "\n";        // 42
p->~int();                       // Must manually call destructor
```

## Engineering Decision Framework

### When to Use Stack
- Local variables in functions
- Small, short-lived objects
- Performance-critical code
- When RAII is not needed

### When to Use Heap
- Large objects or arrays
- Objects that outlive their creating scope
- Polymorphic objects via base pointers
- When you need shared ownership

### When to Use Static/Global
- Constants and lookup tables
- Thread-local storage
- Objects that live for the entire program

### Real-World Production Examples
1. **Game Engines**: Custom pool allocators for frame-based allocation (allocations at frame start, freed at frame end)
2. **Databases**: Memory-mapped I/O with RAII wrappers for page management
3. **Trading Systems**: Lock-free memory pools for order processing
4. **Embedded Systems**: Stack-only allocation with bounded heap

### Common Mistakes

| Mistake | Consequence | Fix |
|---------|-------------|-----|
| Forgetting `delete` | Memory leak | Use smart pointers, RAII |
| Using `delete` instead of `delete[]` | Undefined behavior | Match allocation with deallocation |
| Double free | Crash or corruption | Use smart pointers, set to nullptr |
| Use after free | Undefined behavior, security vulnerability | Use smart pointers, null after delete |
| Memory leak on exception | Resource exhaustion | Use RAII |
| Deleting `void*` | Derived destructor skipped, resource leak | Never delete `void*` |

## Common Mistakes

| Category | Mistake | Impact | Prevention |
|----------|---------|--------|------------|
| **Allocation** | Using `new` without RAII wrapper | Memory leak | Use `std::make_unique` / `std::make_shared` |
| **Allocation** | Mismatched `new[]`/`delete` | Undefined behavior | Match allocation form with deallocation |
| **Lifetime** | Dangling pointer (pointer outlives object) | Use-after-free, crash | Use smart pointers, null after delete |
| **Lifetime** | Dangling reference (reference outlives object) | Undefined behavior | Ensure object outlives reference |
| **Lifetime** | Returning reference to local variable | Dangling reference | Return by value or smart pointer |
| **Deallocation** | Double free | Heap corruption, crash | Use smart pointers, single owner |
| **Deallocation** | Deleting `void*` | Derived destructor skipped | Never delete through `void*` |
| **Deallocation** | Forgetting `delete` on exception path | Memory leak | Use RAII, never raw `new`/`delete` |
| **Multithreading** | Data race on shared pointer | Crash, corruption | Use `std::atomic`, mutexes, or `std::shared_ptr` |
| **Multithreading** | Non-atomic access to reference count | Double free | Use `std::shared_ptr` (atomic ref count) |
| **Placement** | Using `delete` on placement new | Undefined behavior | Manually call destructor + free underlying buffer |
| **Alignment** | Ignoring alignment requirements | Performance loss, crash | Use `alignas()` for cache-critical data |

## Performance Considerations

| Factor | Stack | Heap | Recommendation |
|--------|-------|------|----------------|
| Allocation speed | ~1-10 ns (pointer increment) | ~100-1000 ns (system call + free list search) | Prefer stack for small objects |
| Deallocation speed | Automatic (function return) | Manual (`delete`) or RAII | Stack is automatic and zero-cost |
| Cache locality | Excellent (contiguous frames) | Poor (scattered allocations) | Stack avoids cache misses |
| Maximum size | ~1-8 MB (OS-dependent, configurable) | GBs (limited by physical memory + swap) | Heap for large objects |
| Fragmentation | None (LIFO) | External and internal fragmentation | Pool allocators reduce heap fragmentation |
| Thread safety | Per-thread stack (no contention) | Global heap (requires locking) | Thread-local heaps for high-contention |

### Memory Fragmentation

```
External Fragmentation (heap):
┌─────┬──────┬─────┬────────┬─────┬──────┐
│Alloc│ Free │Alloc│  Free  │Alloc│ Free │
│ 8B  │  4B  │ 8B  │   16B  │ 8B  │  4B  │
└─────┴──────┴─────┴────────┴──────┴──────┘
Total free: 24B, but cannot allocate 16B contiguous block!

Solution: Pool allocator for fixed-size objects eliminates external fragmentation.

Internal Fragmentation (heap):
malloc(13) → actual allocation: 16 bytes (aligned to 8-byte boundary)
Wasted: 3 bytes per allocation
```

### Benchmark: Stack vs Heap

```cpp
#include <chrono>
#include <iostream>

void benchmark() {
    const int N = 1000000;
    auto start = std::chrono::high_resolution_clock::now();

    // Stack allocation
    for (int i = 0; i < N; ++i) {
        int x = i;  // Stack: ~1ns each
    }

    auto mid = std::chrono::high_resolution_clock::now();

    // Heap allocation
    for (int i = 0; i < N; ++i) {
        int* p = new int(i);  // Heap: ~100ns each
        delete p;
    }

    auto end = std::chrono::high_resolution_clock::now();

    auto stackTime = std::chrono::duration_cast<std::chrono::milliseconds>(mid - start);
    auto heapTime = std::chrono::duration_cast<std::chrono::milliseconds>(end - mid);

    std::cout << "Stack: " << stackTime.count() << "ms\n";
    std::cout << "Heap:  " << heapTime.count() << "ms\n";
    // Typical result: Stack ~1ms, Heap ~100ms (100x slower)
}
```

## Best Practices

1. **Prefer stack allocation** — Use heap only when you need dynamic lifetime, large objects, or polymorphism.
2. **Use RAII everywhere** — Wrap all resources (memory, files, sockets, locks) in RAII classes.
3. **Use `std::unique_ptr` for exclusive ownership** — Zero overhead, clear ownership semantics.
4. **Use `std::make_unique` and `std::make_shared`** — Exception-safe; prevents leaks if constructor throws.
5. **Match `new[]` with `delete[]`** — Mismatched allocation/deallocation is undefined behavior.
6. **Never `delete void*`** — Skips derived destructor, leaks derived-class resources.
7. **Initialize pointers at declaration** — `int* p = nullptr;` not `int* p;`.
8. **Use memory pools for hot paths** — Arena/pool allocators eliminate per-allocation overhead.
9. **Run sanitizers in CI** — ASan/LSan catch bugs before they reach production.
10. **Profile before optimizing** — Use a profiler to identify actual memory hotspots.

## Production Incidents

### Incident 1: Memory Leak in Error Path
**Problem**: A web server leaked 2GB over 72 hours.

**Cause**: A `new` allocation in an error-handling path was not freed when the error was caught and the function returned early.

**Impact**: Server restarted nightly. Cache hit rate dropped to 0% after restarts.

**Solution**: Replaced `new`/`delete` with `std::unique_ptr`. Used RAII for all resources.

---

### Incident 2: Use-After-Move
**Problem**: An audio pipeline produced garbled output.

**Cause**: A `std::vector<float>` buffer was moved, but a raw pointer to `.data()` was retained and used after the move.

**Solution**: Replaced raw pointer with `std::shared_ptr` managing buffer lifetime.

---

### Incident 3: Heap Corruption from Buffer Overflow
**Problem**: A web server crashed intermittently with SIGSEGV, but only under high load. The crash address changed every time.

**Cause**: A parsing buffer used `memcpy(dst, src, len)` where `len` was derived from untrusted input. Attackers sent oversized packets that overwrote heap metadata, corrupting adjacent allocations. The corruption manifested hours later when a different allocation triggered the corrupted metadata.

**Impact**: CVE-2023-XXXXX assigned. Service downtime during incident response. All customer data had to be integrity-checked.

**Solution**: Replaced raw `char[]` buffers with `std::vector<char>` with bounds checking. Added ASan to CI. Implemented input validation at the network boundary before any memory operations.

---

### Incident 4: Double-Free in Multithreaded Payment Processor
**Problem**: A payment processing service crashed 2-3 times per day under peak load. The crash logs showed heap corruption but no consistent stack trace.

**Cause**: A `PaymentRequest` object was shared between two threads via a raw pointer. Both threads called `delete` on the pointer independently, causing a double-free. The race condition made it non-deterministic — it only manifested when both threads finished processing at nearly the same time.

**Impact**: Lost payment transactions. Customers charged but payments not recorded. Manual reconciliation required.

**Solution**: Replaced raw pointer with `std::shared_ptr` for the shared object. The reference counting ensured only one thread performed the final deallocation. Added `-fsanitize=thread` to CI to detect future data races.

---

### Incident 5: Use-After-Free in Event-Driven UI Framework
**Problem**: A desktop application crashed randomly when users interacted with certain UI elements. The crash was a use-after-free reading garbage memory as a function pointer.

**Cause**: An event handler registered a callback with a raw pointer to a widget. When the widget was destroyed (user closed a panel), the callback still held the pointer. The next event dispatched to that callback dereferenced freed memory.

**Impact**: Application crash on user interaction. In some cases, the freed memory had been reallocated and overwritten, causing arbitrary code execution (security vulnerability).

**Solution**: Replaced raw callback pointer with `std::weak_ptr`. The callback checks `weak_ptr::lock()` before accessing the widget. If the widget is destroyed, the callback safely no-ops. This is the standard pattern for observer/callback lifetime management.

## Production Checklist
- [ ] Prefer stack allocation over heap
- [ ] Use RAII for all resource management
- [ ] Use `std::unique_ptr` for exclusive ownership
- [ ] Use `std::shared_ptr` for shared ownership (sparingly)
- [ ] Never `new` without RAII wrapper
- [ ] Run ASan/LSan in CI
- [ ] Use `valgrind` for leak detection
- [ ] Initialize all variables at declaration
- [ ] Avoid raw `new`/`delete` in application code
- [ ] Use memory pools for frequent allocations
- [ ] Profile memory usage in long-running systems

## Maturity Levels

### Beginner
- Understand stack vs heap
- Use `new`/`delete` correctly
- Know when to use stack vs heap

### Intermediate
- Apply RAII consistently
- Use smart pointers (see Module 06)
- Understand memory alignment
- Use placement new

### Advanced
- Design custom allocators
- Implement memory pools
- Profile cache behavior
- Use lock-free memory allocation

## Common Myths Debunked

### Myth 1: "Heap is always faster than stack"
**Reality**: Stack allocation is typically 100x faster than heap. Stack allocation is just moving a pointer; heap allocation involves searching free lists, system calls, and potential locking.

### Myth 2: "You need to manually manage all memory"
**Reality**: Stack allocation, `std::vector`, `std::string`, and smart pointers handle most memory management automatically. Manual `new`/`delete` is rare in well-written C++.

### Myth 3: "Smart pointers have no overhead"
**Reality**: `std::unique_ptr` has zero overhead (same as raw pointer). `std::shared_ptr` has overhead from reference counting (atomic operations) and control block allocation.

## One-Minute Revision

| Concept | What It Is | Why It Matters | Key Rule |
|---------|-----------|----------------|----------|
| Stack | Fast, automatic memory | Default for local variables | Limited size (~1-8MB) |
| Heap | Slow, manual memory | Large or long-lived objects | Must free with `delete` |
| RAII | Resource management via scope | Prevents leaks and bugs | Tie resource lifetime to scope |
| new/delete | Heap allocation/deallocation | Dynamic memory | Match `new` with `delete`, `new[]` with `delete[]` |
| Alignment | Data boundary requirements | Performance, portability | Use `alignas()` for cache-critical data |
| Placement New | Construct at specific address | Custom allocators, pools | Must manually call destructor |
| Dangling Pointer | Pointer to freed memory | Security vulnerability | Use smart pointers |

## Cross-References

### Within C++ Curriculum
- [Smart Pointers](../06-smart-pointers/) — `std::unique_ptr`, `std::shared_ptr`, `std::weak_ptr` (Module 06)
- [Knowledge Atoms](../00-knowledge-atoms/) — Memory model foundations
- [Core Language](../02-core-language/) — Basic types, pointers, references
- [Object-Oriented Programming](../03-oop/) — Constructors, destructors, virtual functions
- [Pointers and References](../04-pointers/) — Pointer arithmetic and address-of operator
- [Performance](../11-performance/) — Memory optimization techniques, profiling
- [Best Practices](../14-best-practices/) — Memory management guidelines

### Cross-Language Comparisons
- **Rust**: Ownership system (`&T`, `&mut T`, `Box<T>`) eliminates memory bugs at compile time — no garbage collector, no RAII needed
- **Go**: Garbage-collected — no manual memory management, but GC pauses affect latency
- **Java**: Garbage-collected with JVM — simpler but less deterministic than C++
- **C**: Manual `malloc`/`free` — no RAII, no smart pointers, no constructors/destructors

### Standard Library References
- [std::unique_ptr](https://en.cppreference.com/w/cpp/memory/unique_ptr) — Exclusive ownership smart pointer
- [std::shared_ptr](https://en.cppreference.com/w/cpp/memory/shared_ptr) — Shared ownership smart pointer
- [std::allocator](https://en.cppreference.com/w/cpp/memory/allocator) — Default allocator for STL containers
- [std::pmr::memory_resource](https://en.cppreference.com/w/cpp/memory/memory_resource) — Polymorphic allocator (C++17)

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Memory leak in error path | Valgrind `--leak-check=full` + ASan | Run `valgrind --leak-check=full --show-leak-kinds=all ./program`; enable `-fsanitize=address,leak` in CI |
| Use-after-free or dangling pointer | AddressSanitizer (`-fsanitize=address`) | ASan instruments every memory access; reports use-after-free with exact allocation/deallocation stack traces |
| Double free causing crash or corruption | ASan + `-fsanitize=undefined` | ASan detects double-free; UBSan catches related undefined behavior |
| Stack overflow from large local arrays | Reduce stack usage + `ulimit -s` | Move large arrays to heap (`std::vector`); check stack size with `ulimit -s` in terminal |
| Placement new destructor mismatch | Manual destructor call audit | Track every `placement new` with a corresponding `ptr->~T()` call in the same scope |

## Code Review Checklist

- [ ] Stack allocation preferred for small, short-lived objects
- [ ] RAII used for all resource management (files, locks, memory)
- [ ] `new`/`delete` never used directly in application code (use smart pointers)
- [ ] `delete[]` matched with `new[]` and `delete` with `new`
- [ ] No `delete void*` (skips derived destructor, causes resource leak)
- [ ] All variables initialized at declaration
- [ ] Memory pools used for high-frequency small allocations

## Architecture Considerations

Memory management is the most performance-critical architectural decision in C++. Stack allocation is ~100x faster than heap but limited in size and lifetime. Heap allocation is flexible but introduces fragmentation risk and allocation overhead. RAII ties resource lifetime to object scope, making cleanup automatic and exception-safe. Custom allocators (arena, pool, slab) optimize allocation patterns for specific workloads.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| RAII for all resources | Automatic cleanup, exception safety | Requires understanding move semantics; prevents copying of RAII objects |
| Arena allocator for frame-based allocation | Game engines, request processing | Bulk allocation/deallocation vs. no individual object freeing |
| Pool allocator for fixed-size objects | Frequent allocation/deallocation of same size | Eliminates fragmentation vs. wasted memory for varying sizes |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Heap buffer overflow from unchecked `new[]` access | Remote code execution, memory corruption | Use `std::vector` with bounds checking; enable ASan in CI |
| Use-after-free exploitable for code execution | Critical vulnerability (CVE-class) | Use `std::unique_ptr`; never use raw `new`/`delete` in application code |
| Memory leak exhausting system resources (DoS) | Service crash, denial of service | Use RAII for all allocations; monitor memory in long-running processes |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | `std::unique_ptr`, `std::shared_ptr`, move semantics | Replace `new`/`delete` with `std::make_unique` and `std::make_shared` |
| C++17 | `std::pmr::memory_resource`, `std::byte` | Use polymorphic allocators for pool-based allocation; use `std::byte` for raw memory |
| C++20 | `std::span` for non-owning memory views | Use `std::span` instead of raw pointer + size pairs |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `std::unique_ptr` / `std::shared_ptr` | C++11 | Widely supported |
| `std::make_unique` / `std::make_shared` | C++14 / C++11 | Widely supported |
| `std::pmr::memory_resource` | C++17 | Widely supported |
| `std::span` | C++20 | Supported in GCC 10+, Clang 11+, MSVC 19.29+ |

## Interview Questions

1. **When should you use stack vs heap allocation?**: Use stack for small (< 1MB), short-lived objects — it's ~100x faster. Use heap for large objects, objects that outlive their creating scope, polymorphic objects via base pointers, or when shared ownership is needed.
2. **Explain RAII and why it's the most important C++ pattern**: RAII (Resource Acquisition Is Initialization) ties resource lifetime to object scope — resources are acquired in the constructor and released in the destructor. It prevents leaks, ensures exception safety, and makes code deterministic.
3. **Why is `delete void*` dangerous?**: `delete void*` skips the derived class destructor, causing derived-class resources (file handles, memory, network connections) to leak. Never delete through a `void*` pointer.
4. **What is placement new and when is it used?**: Placement new constructs an object at a pre-allocated memory address: `new (buffer) T(args)`. It's used in custom allocators, memory pools, and when you need precise control over where objects live in memory.
5. **How do you detect memory leaks in C++?**: Use Valgrind (`--leak-check=full`), AddressSanitizer (`-fsanitize=address,leak`), or LeakSanitizer. All track allocations and report unmatched frees at program exit.
6. **What is heap corruption and why is it dangerous?**: Heap corruption occurs when a program writes outside the bounds of an allocated block or uses a freed pointer, overwriting heap metadata. It's dangerous because the crash may occur far from the bug's origin — the corrupted metadata triggers a failure when a subsequent, unrelated allocation or deallocation reads the damaged metadata.
7. **Explain the difference between `malloc`/`free` and `new`/`delete`**: `malloc` allocates raw memory (no constructor called), `free` releases it (no destructor called). `new` allocates memory AND calls the constructor; `delete` calls the destructor AND frees memory. Always use `new`/`delete` in C++, or better yet, smart pointers.
8. **What is a memory pool and when should you use one?**: A memory pool pre-allocates a block of memory and hands out fixed-size chunks from it, avoiding per-allocation overhead and external fragmentation. Use pools when you allocate/deallocate many objects of the same size frequently (e.g., game entities, network packets, database rows).
9. **How does RAII prevent resource leaks?**: RAII acquires resources in a constructor and releases them in the destructor. When the object goes out of scope (including during stack unwinding from exceptions), the destructor runs automatically. This guarantees cleanup regardless of how the scope is exited — normal return, early return, or exception.
10. **What happens if you `delete` a `void*`?**: The derived class destructor is never called. If the derived class holds resources (file handles, network sockets, other allocations), those resources leak. The `delete` expression cannot know the actual type, so it only calls `free()`.
11. **How do you prevent dangling pointers?**: Use smart pointers (`std::unique_ptr`, `std::shared_ptr`) that manage lifetime automatically. If you must use raw pointers, ensure the pointed-to object outlives the pointer. Set pointers to `nullptr` after deleting the pointed-to object. Avoid returning pointers/references to local variables.
12. **Explain placement new and its manual destructor requirement**: Placement new constructs an object at a pre-allocated memory address: `new (buffer) T(args)`. Since no memory was allocated, you cannot use `delete` — you must manually call the destructor: `ptr->~T()`. The underlying memory is managed by whoever provided the buffer.
13. **How does memory alignment affect performance?**: CPUs read memory in aligned chunks (4 bytes for 32-bit, 8/16 bytes for 64-bit). Misaligned access requires two memory reads and bit shifting, which is slower. On some architectures (ARM, older x86), misaligned access causes a hardware fault. Cache-line alignment (64 bytes) prevents false sharing in multithreaded code.
14. **What are the security implications of use-after-free?**: An attacker can trigger a use-after-free to read or write freed memory. If the freed memory has been reallocated with attacker-controlled data, the attacker can overwrite function pointers or vtables to redirect execution flow — achieving arbitrary code execution. Use-after-free is a top source of browser and OS vulnerabilities (Chrome, Safari, Windows kernel).
15. **How do you debug memory issues in production?**: Use AddressSanitizer (`-fsanitize=address`) in staging to catch bugs before production. In production, monitor RSS (resident set size) growth over time to detect leaks. Use heap profiling tools like `jemalloc`'s `malloc_stats_print` or Google's TCMalloc heap profiler. For crashes, analyze core dumps with `gdb` to inspect the heap state at crash time.

## References

- [Effective Modern C++ — Scott Meyers (Items 16-22)](https://www.amazon.com/Effective-Modern-CUDA-Improve-Specific/dp/1491903996)
- [CppReference — Memory Management](https://en.cppreference.com/w/cpp/memory)
- [Valgrind Documentation](https://valgrind.org/docs/manual/quick-start.html)
- [AddressSanitizer — Google](https://github.com/google/sanitizers/wiki/AddressSanitizer)
