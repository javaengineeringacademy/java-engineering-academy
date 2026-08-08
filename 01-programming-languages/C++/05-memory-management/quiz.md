# Memory Management Quiz

## Questions

---

### MCQ 1: Stack vs Heap
What is the main advantage of stack allocation over heap allocation?

A) Larger memory capacity
B) Faster allocation and deallocation
C) Automatic garbage collection
D) More control over memory layout

---

### MCQ 2: RAII
What does RAII stand for and what does it do?

A) Runtime Allocation Is Immediate — allocates memory fast
B) Resource Acquisition Is Initialization — ties resource lifetime to object scope
C) Reference Association Is Instant — creates references quickly
D) Random Access Is Immediate — enables fast memory access

---

### MCQ 3: new vs malloc
What is the key difference between `new` and `malloc`?

A) No difference
B) `new` calls constructors and returns typed pointers; `malloc` does raw byte allocation
C) `malloc` is always faster
D) `new` is deprecated in modern C++

---

### MCQ 4: delete vs delete[]
What happens if you use `delete` instead of `delete[]` on an array allocated with `new[]`?

A) Works fine
B) Undefined behavior — only the first element's destructor is called
C) Compiler error
D) Only the first element is freed, rest leaks

---

### MCQ 5: Rule of Five
When should you implement all five special member functions?

A) Always, for every class
B) Only when a class manages a non-copyable resource (file handle, mutex, etc.)
C) Only for classes with virtual functions
D) Never — the compiler generates them correctly

---

### Code Output 1
What is the output?

```cpp
class Base {
public:
    Base() { std::cout << "Base "; }
    ~Base() { std::cout << "~Base "; }
};

class Derived : public Base {
    int* data_;
public:
    Derived() : data_(new int(42)) { std::cout << "Derived "; }
    ~Derived() { delete data_; std::cout << "~Derived "; }
};

int main() {
    Base* p = new Derived();
    delete p;
}
```

A) `Base Derived ~Derived ~Base`
B) `Base Derived ~Base`
C) `Base Derived ~Derived ~Base` (correct behavior)
D) `Derived Base ~Base ~Derived`

---

### Code Output 2
What happens?

```cpp
int* getNumber() {
    int local = 42;
    return &local;
}

int main() {
    int* p = getNumber();
    std::cout << *p << "\n";
}
```

A) Prints 42
B) Undefined behavior — p points to destroyed stack variable
C) Compilation error
D) Prints 0

---

### Bug Finding
Find the memory bugs:

```cpp
class MyString {
    char* data_;
public:
    MyString(const char* str) {
        data_ = new char[strlen(str) + 1];
        strcpy(data_, str);
    }
    ~MyString() { delete data_; }  // Line A
    // Missing: copy constructor, copy assignment, move operations
};

int main() {
    MyString s1("hello");
    MyString s2 = s1;    // Bug 1
    MyString s3("world");
    s3 = s1;             // Bug 2
}
```

Identify all bugs and explain the consequences.

---

### Scenario 1
You're building a server that processes 10,000 requests per second. Each request allocates and frees a 1KB buffer. Which approach is best?

A) `new char[1024]` / `delete[]` for each request
B) `std::make_unique<char[]>(1024)` for each request
C) Pre-allocated memory pool with 1000 buffers, recycled across requests
D) Stack allocation of 1024 bytes per request

---

### Scenario 2
You have a class that wraps a database connection. The connection must not be copied but can be transferred. Which operations should you define?

A) Copy constructor and copy assignment
B) Move constructor and move assignment only (delete copy)
C) All five special members
D) Only destructor

---

## Answers

---

### MCQ 1: B
**Faster allocation and deallocation.** Stack allocation is a simple pointer adjustment (~1 ns). Heap allocation involves searching free lists, potential system calls, and locking (~100+ ns). Stack is typically 100x faster.

### MCQ 2: B
**Resource Acquisition Is Initialization.** RAII ties resource lifetime to object scope. When the object is destroyed (goes out of scope), its destructor releases the resource. This prevents leaks regardless of how the scope exits (return, exception, etc.).

### MCQ 3: B
**`new` calls constructors; `malloc` does raw byte allocation.** `new int(42)` allocates 4 bytes and calls `int`'s constructor (sets value to 42). `malloc(sizeof(int))` allocates 4 bytes with undefined contents. Always prefer `new` in C++.

### MCQ 4: B
**Undefined behavior.** `delete[]` knows to call destructors for each array element and free the entire block. `delete` only calls one destructor and attempts to free memory allocated as an array — this corrupts the heap. In practice, only the first element's destructor runs.

### MCQ 5: B
**Only when a class manages a non-copyable resource.** If your class doesn't manage raw resources (uses smart pointers, RAII wrappers, etc.), the compiler-generated special members are correct (Rule of Zero). You only need the Rule of Five when managing resources like raw pointers, file handles, sockets, etc.

### Code Output 1: B
`Base Derived ~Base`. Because `~Base()` is NOT virtual, `delete p` only calls `~Base()`. The `~Derived()` destructor is never called, leaking `data_`. The fix is `virtual ~Base() = default;`.

### Code Output 2: B
**Undefined behavior.** `local` is a stack variable destroyed when `getNumber()` returns. The returned pointer is dangling. Dereferencing it is undefined behavior — it might print 42, crash, or do anything else.

### Bug Finding
**Three bugs:**
1. **`~MyString()` uses `delete data_` instead of `delete[] data_`** — undefined behavior because `data_` was allocated with `new[]`.
2. **Missing copy constructor** — `MyString s2 = s1` does a shallow copy, both `s1` and `s2` point to the same `data_`. Double-free in destructors.
3. **Missing copy assignment operator** — `s3 = s1` leaks `s3`'s original `data_` and does a shallow copy.

**Fix**: Apply Rule of Five — implement destructor, copy/move constructors, and copy/move assignment operators.

### Scenario 1: C
**Pre-allocated memory pool.** At 10,000 requests/second, heap allocation overhead adds up. A pool of 1000 pre-allocated buffers eliminates allocation/deallocation cost entirely. Buffer recycling (allocate from pool, return to pool) achieves near-zero allocation latency. `std::vector<char>` with `reserve()` is a simpler alternative.

### Scenario 2: B
**Move constructor and move assignment only (delete copy).** A database connection is a unique resource — copying it would create two handles to the same connection, causing double-close errors. Delete copy operations, define move operations to transfer ownership. The destructor handles final cleanup.
