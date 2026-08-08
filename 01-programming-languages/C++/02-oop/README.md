# Object-Oriented Programming (OOP) — C++

## Why It Matters

Every complex system — a game engine managing thousands of entities, a database handling concurrent queries, a trading platform processing millions of orders — needs organization. When code lacks structure, it becomes a tangled web of functions and global state that is impossible to maintain, test, or extend. OOP provides the architectural grammar to model real-world entities and build systems that evolve without collapsing.

## What It Is

OOP in C++ organizes code around objects — instances of classes that bundle data and behavior. C++ supports encapsulation, inheritance, polymorphism, and abstraction to model complex systems.

## Architecture: How OOP Fits Together

```
┌─────────────────────────────────────────────────────────────┐
│                    C++ OOP Architecture                      │
├───────────────┬───────────────┬───────────────┬─────────────┤
│ Encapsulation │  Inheritance  │ Polymorphism  │ Abstraction │
│ (Classes)     │  (IS-A)       │ (Virtual)     │ (Interfaces)│
├───────────────┴───────────────┴───────────────┴─────────────┤
│              Composition & Aggregation (HAS-A)               │
├─────────────────────────────────────────────────────────────┤
│         Design Patterns (Singleton, Factory, Observer)       │
└─────────────────────────────────────────────────────────────┘
```

## Classes and Objects

### The Problem Classes Solve
Classes provide a blueprint for creating objects with consistent structure and behavior. They enforce contracts, enable encapsulation, and make code self-documenting.

### Class Anatomy

```cpp
#include <string>
#include <iostream>

class BankAccount {
private:
    std::string owner_;
    double balance_;
    int account_id_;

public:
    // Constructor
    BankAccount(const std::string& owner, double initial_balance, int id)
        : owner_(owner), balance_(initial_balance), account_id_(id) {}

    // Const method — promise not to modify state
    double getBalance() const { return balance_; }
    const std::string& getOwner() const { return owner_; }

    // Mutating methods
    bool deposit(double amount) {
        if (amount <= 0) return false;
        balance_ += amount;
        return true;
    }

    bool withdraw(double amount) {
        if (amount <= 0 || amount > balance_) return false;
        balance_ -= amount;
        return true;
    }

    // Operator overloading
    friend std::ostream& operator<<(std::ostream& os, const BankAccount& acc) {
        os << acc.owner_ << ": $" << acc.balance_;
        return os;
    }
};
```

### Access Specifiers

```
┌─────────────┬───────────────┬─────────────────────────────┐
│  Specifier  │   Access      │   Purpose                    │
├─────────────┼───────────────┼─────────────────────────────┤
│  public     │  Everywhere   │  Interface (API)             │
│  protected  │  Class + Sub  │  Extension points            │
│  private    │  Class only   │  Implementation details      │
└─────────────┴───────────────┴─────────────────────────────┘
```

### Rule of Zero, Three, and Five

```cpp
// Rule of Zero: Prefer using RAII types so compiler generates everything
class Person {
    std::string name_;
    int age_;
    // Compiler generates: destructor, copy/move ctor, copy/move assignment
};

// Rule of Three: If you define one, define all three
class RawBuffer {
    int* data_;
    size_t size_;
public:
    RawBuffer(size_t n) : data_(new int[n]), size_(n) {}
    ~RawBuffer() { delete[] data_; }                          // Destructor
    RawBuffer(const RawBuffer& o)                             // Copy ctor
        : data_(new int[o.size_]), size_(o.size_) {
        std::copy(o.data_, o.data_ + o.size_, data_);
    }
    RawBuffer& operator=(const RawBuffer& o) {               // Copy assignment
        if (this != &o) {
            delete[] data_;
            data_ = new int[o.size_];
            size_ = o.size_;
            std::copy(o.data_, o.data_ + o.size_, data_);
        }
        return *this;
    }
};

// Rule of Five: If you define any, define all five (add move)
class MoveBuffer {
    int* data_;
    size_t size_;
public:
    MoveBuffer(size_t n) : data_(new int[n]), size_(n) {}
    ~MoveBuffer() { delete[] data_; }
    MoveBuffer(const MoveBuffer& o)
        : data_(new int[o.size_]), size_(o.size_) {
        std::copy(o.data_, o.data_ + o.size_, data_);
    }
    MoveBuffer& operator=(const MoveBuffer& o) { /* ... */ return *this; }
    MoveBuffer(MoveBuffer&& o) noexcept                      // Move ctor
        : data_(o.data_), size_(o.size_) {
        o.data_ = nullptr;
        o.size_ = 0;
    }
    MoveBuffer& operator=(MoveBuffer&& o) noexcept {         // Move assignment
        if (this != &o) {
            delete[] data_;
            data_ = o.data_;
            size_ = o.size_;
            o.data_ = nullptr;
            o.size_ = 0;
        }
        return *this;
    }
};
```

## Inheritance

### The Problem Inheritance Solves
Inheritance enables code reuse and establishes "is-a" relationships. A `Dog` *is an* `Animal`. Instead of duplicating code, derived classes inherit and specialize behavior.

### Basic Inheritance

```cpp
class Animal {
protected:
    std::string name_;
    int age_;
public:
    Animal(const std::string& name, int age) : name_(name), age_(age) {}
    virtual ~Animal() = default;  // Critical: virtual destructor

    virtual void speak() const = 0;  // Pure virtual — abstract class

    void describe() const {
        std::cout << name_ << " (age " << age_ << ")\n";
    }
};

class Dog : public Animal {
    std::string breed_;
public:
    Dog(const std::string& name, int age, const std::string& breed)
        : Animal(name, age), breed_(breed) {}

    void speak() const override {
        std::cout << name_ << " says Woof!\n";
    }

    const std::string& getBreed() const { return breed_; }
};

class Cat : public Animal {
    bool indoor_;
public:
    Cat(const std::string& name, int age, bool indoor)
        : Animal(name, age), indoor_(indoor) {}

    void speak() const override {
        std::cout << name_ << " says Meow!\n";
    }
};
```

### Multiple Inheritance and the Diamond Problem

```cpp
// The diamond problem: D inherits from B and C, both inherit from A
//     A
//    / \
//   B   C
//    \ /
//     D

class A {
public:
    int value;
    virtual ~A() = default;
};

class B : public A {};  // Has A's value
class C : public A {};  // Has A's value

// D would have TWO copies of A::value — ambiguous!
// Solution: virtual inheritance
class VB : virtual public A {};
class VC : virtual public A {};
class D : public VB, public VC {};  // Only ONE copy of A::value
```

### Inheritance vs Composition

```cpp
// Inheritance: "is-a" — Dog IS-A Animal
class Dog : public Animal { /* ... */ };

// Composition: "has-a" — Car HAS-A Engine
class Engine {
public:
    void start() { /* ... */ }
};

class Car {
    Engine engine_;  // Car owns an Engine
public:
    void start() { engine_.start(); }
};

// Prefer composition over inheritance when the relationship is "has-a"
```

## Polymorphism

### The Problem Polymorphism Solves
Polymorphism lets you write code that works with base class interfaces while the actual behavior is determined at runtime by the derived class. This is the foundation of extensible design.

### Virtual Functions and Dynamic Dispatch

```cpp
class Shape {
public:
    virtual ~Shape() = default;
    virtual double area() const = 0;
    virtual void draw() const = 0;
    virtual std::unique_ptr<Shape> clone() const = 0;
};

class Circle : public Shape {
    double radius_;
public:
    explicit Circle(double r) : radius_(r) {}
    double area() const override { return 3.14159 * radius_ * radius_; }
    void draw() const override { std::cout << "Drawing circle\n"; }
    std::unique_ptr<Shape> clone() const override {
        return std::make_unique<Circle>(*this);
    }
};

class Rectangle : public Shape {
    double width_, height_;
public:
    Rectangle(double w, double h) : width_(w), height_(h) {}
    double area() const override { return width_ * height_; }
    void draw() const override { std::cout << "Drawing rectangle\n"; }
    std::unique_ptr<Shape> clone() const override {
        return std::make_unique<Rectangle>(*this);
    }
};

// Polymorphic usage
void printArea(const Shape& shape) {
    std::cout << "Area: " << shape.area() << "\n";
}

// Works with any Shape — no code changes needed for new shapes
Circle c(5.0);
Rectangle r(3.0, 4.0);
printArea(c);  // Area: 78.5398
printArea(r);  // Area: 12
```

### Static vs Dynamic Polymorphism

```cpp
// Static polymorphism (CRTP) — resolved at compile time, no vtable overhead
template <typename Derived>
class Base {
public:
    void interface() {
        static_cast<Derived*>(this)->implementation();
    }
};

class Concrete : public Base<Concrete> {
public:
    void implementation() { std::cout << "Concrete\n"; }
};

// Dynamic polymorphism — resolved at runtime via vtable
class DynamicBase {
public:
    virtual void interface() { std::cout << "DynamicBase\n"; }
    virtual ~DynamicBase() = default;
};
```

### Abstract Classes and Interfaces

```cpp
// Pure abstract class (interface)
class ISerializer {
public:
    virtual ~ISerializer() = default;
    virtual std::string serialize(const void* data, size_t len) = 0;
    virtual bool deserialize(const std::string& data, void* output) = 0;
};

// Partially implemented abstract class
class SerializerBase : public ISerializer {
protected:
    std::string prefix_;
public:
    explicit SerializerBase(const std::string& prefix) : prefix_(prefix) {}
    // serialize() still pure virtual — subclass must implement
};
```

## Encapsulation

### The Problem Encapsulation Solves
Encapsulation hides internal state and forces interaction through a controlled interface. This prevents invalid states, simplifies maintenance, and enables internal changes without breaking consumers.

```cpp
class Temperature {
private:
    double celsius_;

    // Private helper — implementation detail
    double toFahrenheit() const { return celsius_ * 9.0 / 5.0 + 32.0; }

public:
    explicit Temperature(double c) : celsius_(c) {
        if (c < -273.15) throw std::invalid_argument("Below absolute zero");
    }

    double getCelsius() const { return celsius_; }
    double getFahrenheit() const { return toFahrenheit(); }

    void setCelsius(double c) {
        if (c < -273.15) throw std::invalid_argument("Below absolute zero");
        celsius_ = c;
    }
};
```

## Engineering Decision Framework

### When to Use OOP
- When modeling real-world entities with attributes and behaviors
- When you need runtime polymorphism (different behavior for different types)
- When building extensible systems (new types without modifying existing code)
- When enforcing invariants through encapsulation
- When designing plugin architectures or framework APIs

### When NOT to Use OOP
- When simple functions and structs suffice (avoid over-engineering)
- When performance-critical code needs zero-overhead abstractions (use templates/CRTP)
- When the "is-a" relationship doesn't clearly exist (prefer composition)
- When the system is naturally procedural (utility functions, algorithms)

### Alternatives to Inheritance
| Situation | Alternative | Trade-off |
|-----------|-------------|-----------|
| Code reuse without "is-a" | Composition + delegation | More objects, clearer ownership |
| Runtime polymorphism | `std::function` + lambdas | Slight overhead, more flexible |
| Static polymorphism | CRTP or concepts | Zero overhead, compile-time only |
| Interface only | Concept (C++20) | Compile-time checked, no vtable |

### Real-World Production Examples
1. **Unreal Engine**: Massive OOP hierarchy for game entities, with virtual dispatch for AI, rendering, and physics
2. **Chrome Browser**: RenderNode tree uses inheritance for different node types; Observer pattern for layout invalidation
3. **MySQL**: Handler class hierarchy for different storage engines (InnoDB, MyISAM) — polymorphism at the storage layer
4. **Boost.Asio**: `basic_stream_socket` hierarchy with platform-specific backends via inheritance

### Common Mistakes

| Mistake | Consequence | Fix |
|---------|-------------|-----|
| Missing virtual destructor | Resource leaks when deleting via base pointer | Always add `virtual ~Base() = default;` if class has virtual methods |
| Object slicing | Silently losing derived behavior | Use pointers or smart pointers for polymorphic containers |
| Deep inheritance hierarchies | Fragile, hard to maintain | Prefer composition; limit inheritance to 2-3 levels |
| Overusing public inheritance | Tight coupling, fragile base class | Use private inheritance or composition |
| Not using `override` | Silent bugs from signature mismatches | Always use `override` on derived virtual functions |
| Making everything virtual | Performance overhead, reduced optimization | Only virtualize when polymorphism is needed |

## Production Incidents

### Incident 1: Object Slicing in Vector
**Problem**: A game engine's entity system silently dropped polymorphic behavior, causing all enemy NPCs to use base-class AI instead of specialized subclasses.

**Cause**: `std::vector<Enemy>` stored enemies by value. When derived `BossEnemy` objects were inserted, they were sliced to the base `Enemy` type, losing all virtual method overrides. The specialized AI was never invoked.

**Impact**: Boss enemies acted like regular enemies — no special attacks, no phase transitions. A major content update shipped with the bug, requiring a hotfix within 48 hours.

**Detection**: Unit tests with `typeid(*enemy).name()` confirmed all stored objects were `Enemy`, not `BossEnemy`.

**Solution**: Changed storage to `std::vector<std::unique_ptr<Enemy>>`. Added debug-mode assertion that `typeid(*ptr) == typeid(Derived)` after insertion.

**Prevention**: Rule — never store polymorphic objects by value in containers. Enable clang-tidy `bugprone-slicing` check.

---

### Incident 2: Missing Virtual Destructor Causing Leak
**Problem**: A graphics engine leaked GPU resources (textures, shaders) every time a scene was reloaded, eventually exhausting GPU memory.

**Cause**: The `Renderable` base class had virtual methods but no virtual destructor. When `delete renderable` was called on a `Mesh*` pointing to a derived `TexturedMesh`, only the base destructor ran. The derived destructor — which released the GPU texture handle — was never called.

**Impact**: GPU memory grew by 50MB per scene reload. After 20 reloads, rendering corrupted with texture artifacts.

**Solution**: Added `virtual ~Renderable() = default;` to the base class. Switched to `std::unique_ptr<Renderable>`.

**Prevention**: Any class with virtual methods must have a virtual destructor. Enable clang-tidy `cppcoreguidelines-virtual-class-destructor`.

---

### Incident 3: Slicing in Exception Handling
**Problem**: A payment system caught exceptions by value, silently slicing derived exception types and losing error context.

```cpp
// BUG: catches by value — slices derived exceptions
try {
    throw PaymentDeclinedError("Card expired", 402);
} catch (PaymentException e) {  // Sliced! PaymentDeclinedError lost
    log(e.what());  // Generic message, lost specific error code
}
```

**Impact**: Error monitoring showed generic "payment failed" messages for all error types. Ops team couldn't distinguish between card declines, network errors, and fraud alerts.

**Solution**: Always catch by const reference: `catch (const PaymentException& e)`.

---

## Production Checklist
- [ ] Add virtual destructor to any class with virtual methods
- [ ] Use `override` keyword on all derived virtual functions
- [ ] Use `final` on classes/methods that shouldn't be overridden
- [ ] Never store polymorphic objects by value in containers
- [ ] Prefer composition over inheritance for "has-a" relationships
- [ ] Keep inheritance hierarchies shallow (max 2-3 levels)
- [ ] Use `std::unique_ptr` or `std::shared_ptr` for polymorphic ownership
- [ ] Catch exceptions by `const&`, never by value
- [ ] Use `explicit` on single-argument constructors
- [ ] Apply Rule of Zero when possible — let compiler generate special members
- [ ] Use `= default` and `= delete` to control special member functions
- [ ] Prefer `enum class` over plain `enum` for type safety

## Maturity Levels

### Beginner (0-6 months)
- Define classes with constructors, methods, and member variables
- Understand public vs private access
- Use basic inheritance (single level)
- Call virtual functions through base pointers

### Intermediate (6-18 months)
- Apply Rule of Three/Five correctly
- Use virtual destructors consistently
- Implement abstract classes and interfaces
- Distinguish inheritance from composition
- Use `override` and `final` keywords

### Advanced (18+ months)
- Design CRTP for static polymorphism
- Apply the SOLID principles to class design
- Use virtual inheritance to solve the diamond problem
- Build plugin architectures with abstract factories
- Profile vtable overhead and switch to CRTP when justified

## Common Myths Debunked

### Myth 1: "OOP is always better than procedural"
**Reality**: For many tasks — utility functions, algorithms, simple data processing — procedural code is simpler, faster, and easier to understand. OOP adds overhead (vtables, object lifetime management) that isn't always justified.

### Myth 2: "More inheritance is better"
**Reality**: Deep inheritance hierarchies create fragile base classes. A change in a base class can break all descendants. Prefer composition for code reuse and flat hierarchies for polymorphism.

### Myth 3: "All classes need inheritance"
**Reality**: Most classes should use composition ("has-a") not inheritance ("is-a"). Inheritance should model a true behavioral contract, not just code sharing.

### Myth 4: "Virtual functions are always slow"
**Reality**: Virtual dispatch adds ~2-5 ns per call. For most applications, this is negligible. The bigger cost is cache misses from pointer-chasing in deep hierarchies, not the dispatch itself.

### Myth 5: "struct and class are different"
**Reality**: In C++, the only difference is default access: `struct` defaults to `public`, `class` defaults to `private`. Both support methods, inheritance, templates, and everything else.

## One-Minute Revision

| Concept | What It Is | Why It Matters | Key Rule |
|---------|-----------|----------------|----------|
| Class | Blueprint for objects | Encapsulates state and behavior | Use `explicit` on single-arg constructors |
| Object | Instance of a class | Runtime entity with identity | Prefer stack allocation when possible |
| Inheritance | IS-A relationship | Code reuse and polymorphism | Prefer composition over inheritance |
| Polymorphism | Same interface, different behavior | Extensible design | Always use virtual destructor in base |
| Encapsulation | Hide implementation details | Prevents invalid states | Private data, public interface |
| Abstract Class | Class with pure virtual functions | Defines contracts | Cannot be instantiated |
| Object Slicing | Losing derived parts when assigned to base | Silent loss of behavior | Use pointers for polymorphic containers |
| Rule of Five | Define dtor, copy/move ctor, copy/move assign | Prevent resource leaks | Apply when managing raw resources |

## Related Topics
- [Knowledge Atoms](../00-knowledge-atoms/) — Object model underpins OOP mechanics
- [Templates](../03-templates/) — Generic programming as alternative to inheritance
- [Design Patterns](../09-design-patterns/) — OOP patterns in production systems
- [Memory Management](../05-memory-management/) — Object lifetime and RAII
- [Best Practices](../14-best-practices/) — Clean OOP design principles

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Object slicing silently losing derived behavior | `typeid(*ptr).name()` + unit tests | Insert debug assertions after container insertion; use `std::vector<std::unique_ptr<Base>>` to prevent slicing |
| Missing virtual destructor causing resource leak | AddressSanitizer + leak report | ASan reports leaked objects; check if base class has virtual destructor when deleting via base pointer |
| Infinite loop from incorrect virtual dispatch | GDB backtrace + vtable inspection | Set breakpoint in derived override; use `info vtbl obj` in GDB to inspect vtable layout |
| Slicing in exception handling (`catch` by value) | Code review + clang-tidy | Enable `bugprone-slicing` check; always catch exceptions by `const&` |
| Diamond problem with multiple inheritance | Static assertion + virtual inheritance | Use `static_assert(std::is_base_of_v<Base, Derived>)` to verify hierarchy; apply `virtual` inheritance |

## Code Review Checklist

- [ ] Virtual destructor (`virtual ~Base() = default;`) in any class with virtual methods
- [ ] `override` keyword on all derived virtual functions
- [ ] `final` applied to classes/methods that should not be overridden
- [ ] No polymorphic objects stored by value in containers
- [ ] Single-argument constructors marked `explicit`
- [ ] Exceptions caught by `const&`, never by value
- [ ] Rule of Five applied when managing raw resources

## Architecture Considerations

OOP provides the architectural grammar for modeling real-world entities and building extensible systems. Encapsulation prevents invalid states by hiding implementation details behind controlled interfaces. Inheritance establishes behavioral contracts (IS-A) for polymorphic dispatch. Polymorphism enables the Open/Closed Principle — new types without modifying existing code. Composition over inheritance reduces coupling and simplifies testing.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Abstract Factory for plugin architectures | Runtime selection of concrete implementations | Loose coupling vs. indirection overhead and harder debugging |
| CRTP for static polymorphism | Zero-overhead dispatch in performance-critical paths | Compile-time flexibility vs. reduced readability and debugging difficulty |
| Composition over inheritance | "Has-a" relationships, code reuse | Clearer ownership vs. more objects and delegation boilerplate |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Vtable pointer manipulation (CET attacks) | Control-flow hijacking, remote code execution | Use Control-Flow Enforcement Technology (CET); avoid exposing polymorphic interfaces to untrusted input |
| Object slicing exposing derived-class-only data | Information leakage through unintended slicing | Use `std::unique_ptr`/`std::shared_ptr` for polymorphic containers; add `static_assert` guards |
| Dangling reference from stored base-class pointer | Use-after-free, crashes | Use smart pointers; validate pointer lifetime in RAII wrappers |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | `override`, `final`, `= default`, `= delete` | Add `override` to all derived virtual functions; use `= default` for trivial special members |
| C++17 | `std::variant` for type-safe unions | Replace raw `union` + type tag with `std::variant` for discriminated unions |
| C++20 | Concepts for interface constraints | Replace virtual-only interfaces with concepts where compile-time polymorphism suffices |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `override` and `final` | C++11 | Widely supported |
| `= default` and `= delete` | C++11 | Widely supported |
| `std::variant` | C++17 | Widely supported |
| Concepts for compile-time polymorphism | C++20 | Supported in GCC 10+, Clang 12+, MSVC 19.22+ |

## Interview Questions

1. **What is the difference between inheritance and composition?**: Inheritance models IS-A (Dog is an Animal) and enables polymorphism. Composition models HAS-A (Car has an Engine) and provides flexible, decoupled design. Prefer composition when the relationship isn't a true behavioral contract.
2. **Why must base classes with virtual methods have virtual destructors?**: When deleting a derived object through a base pointer, without a virtual destructor only the base destructor runs — derived resources leak. A virtual destructor ensures the correct destructor chain executes.
3. **Explain object slicing and why it's dangerous**: Object slicing occurs when a derived object is assigned to a base class by value — the derived-specific members and virtual overrides are silently lost. It's dangerous because the code appears to work but behaves incorrectly.
4. **What is the diamond problem and how does C++ solve it?**: When D inherits from B and C, both of which inherit from A, D gets two copies of A's members. C++ solves this with virtual inheritance (`class B : virtual public A`), ensuring only one copy of the shared base exists.
5. **When should you use CRTP over virtual functions?**: Use CRTP when the derived class is known at compile time and you need zero-overhead dispatch. Use virtual functions when you need runtime polymorphism (storing heterogeneous types in containers, plugin architectures).

## References

- [C++ Core Guidelines — Class Design](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-ctor)
- [CppReference — Virtual Functions](https://en.cppreference.com/w/cpp/language/virtual)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF)](https://www.amazon.com/Design-Patterns-Elements-Reusable-Object-Oriented/dp/0201633612)
- [SOLID Principles in C++](https://www.oreilly.com/library/view/clean-code/9780136083238/)
