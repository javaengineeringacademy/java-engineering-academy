# Smart Pointers Quiz

## Questions

---

### MCQ 1: unique_ptr vs shared_ptr
What is the main difference between `std::unique_ptr` and `std::shared_ptr`?

A) `unique_ptr` is faster
B) `unique_ptr` allows exclusive ownership; `shared_ptr` allows shared ownership
C) `shared_ptr` is older
D) No difference

---

### MCQ 2: weak_ptr Purpose
What is `std::weak_ptr` used for?

A) Strong ownership
B) Breaking circular references between shared_ptrs
C) Faster access than shared_ptr
D) Thread-safe operations

---

### MCQ 3: make_shared vs new
Why prefer `std::make_shared` over `std::shared_ptr<T>(new T)`?

A) It's faster
B) It's exception-safe and more efficient (single allocation)
C) It's required by the standard
D) No difference

---

### MCQ 4: unique_ptr Copy
Can `std::unique_ptr` be copied?

A) Yes
B) No, but can be moved
C) Only with custom deleter
D) Only in C++14

---

### MCQ 5: shared_ptr Destruction
What happens when the last `std::shared_ptr` to an object is destroyed?

A) Object is leaked
B) Object is destroyed (deleter called)
C) Program crashes
D) Nothing happens

---

### Code Output 1
What is the output?

```cpp
auto p1 = std::make_shared<int>(42);
std::shared_ptr<int> p2 = p1;
std::shared_ptr<int> p3 = p2;
p1.reset();
std::cout << p2.use_count() << " " << *p3;
```

A) `2 42`
B) `1 42`
C) `3 42`
D) `2 0`

---

### Code Output 2
What is the output?

```cpp
auto p = std::make_unique<int>(10);
auto q = std::move(p);
std::cout << (p == nullptr) << " " << *q;
```

A) `0 10`
B) `1 10`
C) `0 0`
D) `1 0`

---

### Bug Finding
Find the bug:

```cpp
class Node {
public:
    std::shared_ptr<Node> next;
    ~Node() { std::cout << "destroyed\n"; }
};

int main() {
    auto a = std::make_shared<Node>();
    auto b = std::make_shared<Node>();
    a->next = b;
    b->next = a;  // Circular reference!
}
```

---

### Scenario 1
You're building a cache where entries should be kept alive as long as something references them, but should be removable. Which smart pointer combination is best?

A) `shared_ptr` for all entries
B) `unique_ptr` for all entries
C) `shared_ptr` for entries + `weak_ptr` for cache access
D) Raw pointers with manual delete

---

### Scenario 2
A factory function creates objects and returns them to callers who will store them. The caller should own the object. Which return type is best?

A) Raw pointer `T*`
B) `std::shared_ptr<T>`
C) `std::unique_ptr<T>`
D) Reference `T&`

---

## Answers

---

### MCQ 1: B
**`unique_ptr` allows exclusive ownership; `shared_ptr` allows shared ownership.** `unique_ptr` cannot be copied (only moved), making ownership clear and zero-overhead. `shared_ptr` uses atomic reference counting for shared ownership.

### MCQ 2: B
**Breaking circular references.** `weak_ptr` observes an object without owning it (doesn't increment reference count). It's used to break cycles where two objects hold `shared_ptr` references to each other.

### MCQ 3: B
**Exception-safe and more efficient.** `make_shared` allocates the object and control block in a single memory allocation. `shared_ptr<T>(new T)` does two allocations and can leak if an exception occurs between `new` and the `shared_ptr` constructor.

### MCQ 4: B
**No, but can be moved.** `unique_ptr`'s copy constructor and copy assignment are deleted. Move constructor and assignment transfer ownership. This enforces exclusive ownership at compile time.

### MCQ 5: B
**Object is destroyed.** When the last `shared_ptr` sharing ownership is reset or goes out of scope, the reference count reaches zero and the deleter is called, destroying the object.

### Code Output 1: A
`2 42`. After `p1.reset()`, the reference count drops from 3 to 2. `p2` and `p3` still share ownership. `*p3` is still 42 because the object is alive.

### Code Output 2: B
`1 10`. After `std::move(p)`, `p` is nullptr (moved-from state). `q` owns the value 10. `p == nullptr` is true (1), and `*q` is 10.

### Bug Finding
**Circular reference: `a->next = b` and `b->next = a`** creates a cycle. When `a` and `b` go out of scope, their reference counts never reach zero because each keeps the other alive. The `Node` destructor is never called — memory leak.

**Fix**: Change one of the references to `std::weak_ptr<Node>`:
```cpp
b->next = a;  // Change to: std::weak_ptr<Node> next;
```

### Scenario 1: C
**`shared_ptr` for entries + `weak_ptr` for cache access.** The cache stores `shared_ptr` entries (strong ownership). Consumers access entries via `weak_ptr` — if the entry is still alive, `lock()` returns a `shared_ptr`; if evicted, `lock()` returns nullptr.

### Scenario 2: C
**`std::unique_ptr<T>`** — it clearly communicates ownership transfer. The factory creates and hands off ownership to the caller. `unique_ptr` prevents accidental copies and has zero overhead. If shared ownership is needed later, `unique_ptr` can be converted to `shared_ptr`.
