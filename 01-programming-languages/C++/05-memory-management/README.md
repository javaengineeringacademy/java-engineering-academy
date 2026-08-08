# Memory Management — C++ Language

## The Problem Memory Management Solves

C++ gives you direct control over memory — the source of both its power and its danger. Without understanding memory management, programs leak resources, crash with segmentation faults, and exhibit undefined behavior that manifests differently on every platform. Memory bugs are the hardest bugs to find and the most expensive to fix.

**Production reality**: A server process leaked 2GB of memory over 72 hours due to a missing `delete` in an error-handling path. A game engine crashed on specific GPU drivers because a `delete` was called on a `void*`, skipping derived destructors. These are memory management failures with real consequences.

## What Is Memory Management in C++?

Memory management in C++ covers allocating, using, and deallocating memory. It includes stack vs heap allocation, the RAII principle, manual memory management with `new`/`delete`, and understanding storage durations and lifetimes.

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

## Related Topics
- [Smart Pointers](../06-smart-pointers/) — Automatic memory management
- [Knowledge Atoms](../00-knowledge-atoms/) — Memory model foundations
- [Performance](../11-performance/) — Memory optimization techniques
- [Best Practices](../14-best-practices/) — Memory management guidelines
