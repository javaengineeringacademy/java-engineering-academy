# Object-Oriented Programming (OOP)

## What it is
A programming paradigm that organizes code into objects that bundle data and behavior.

## Why it exists
To model real-world entities, enable code reuse, and create maintainable, scalable systems.

## When to use it
When you need to model complex systems with entities that have attributes and behaviors.

## How it works

### Classes and Objects
```cpp
class Dog {
private:
    std::string name;
    int age;
public:
    Dog(std::string n, int a) : name(n), age(a) {}
    void bark() const { std::cout << "Woof!" << std::endl; }
};

Dog myDog("Rex", 3);
myDog.bark();
```

### Inheritance
```cpp
class Animal {
public:
    virtual void speak() = 0;
};

class Cat : public Animal {
public:
    void speak() override { std::cout << "Meow!" << std::endl; }
};
```

### Polymorphism
```cpp
Animal* animal = new Cat();
animal->speak();  // Calls Cat::speak()
delete animal;
```

### Encapsulation
```cpp
class BankAccount {
private:
    double balance;
public:
    void deposit(double amount) { balance += amount; }
    double getBalance() const { return balance; }
};
```

## Production Incidents

### Incident 1: Object Slicing in Vector
**Problem**: A game engine's entity system silently dropped polymorphic behavior, causing all enemy NPCs to use base-class AI instead of specialized subclasses.

**Cause**: `std::vector<Enemy>` stored enemies by value. When derived `BossEnemy` objects were inserted, they were sliced to the base `Enemy` type, losing all virtual method overrides. The specialized AI was never invoked.

**Impact**: Boss enemies acted like regular enemies — no special attacks, no phase transitions. Playtesters reported the game felt "too easy." A major content update shipped with the bug, requiring a hotfix within 48 hours.

**Detection**: Unit tests with `typeid(*enemy).name()` confirmed all stored objects were `Enemy`, not `BossEnemy`. Code review identified the `std::vector<Enemy>` declaration.

**Solution**: Changed storage to `std::vector<std::unique_ptr<Enemy>>` to preserve polymorphism. Added a debug-mode assertion that `typeid(*ptr) == typeid(Derived)` after insertion to catch future slicing at compile-test time.

**Prevention**: Rule — never store polymorphic objects by value in containers. Use `std::vector<std::unique_ptr<Base>>` or `std::vector<std::shared_ptr<Base>>`. Enable clang-tidy `bugprone-slicing` check.

---

### Incident 2: Missing Virtual Destructor Causing Leak
**Problem**: A graphics engine leaked GPU resources (textures, shaders) every time a scene was reloaded, eventually exhausting GPU memory and causing rendering artifacts.

**Cause**: The `Renderable` base class had virtual methods but no virtual destructor. When `delete renderable` was called on a `Mesh*` pointing to a derived `TexturedMesh`, only the base destructor ran. The derived class's destructor — which released the GPU texture handle — was never called.

**Impact**: GPU memory grew by 50MB per scene reload. After 20 reloads, the GPU ran out of VRAM. Rendering corrupted with texture artifacts. Crash reports showed `cudaErrorMemoryAllocation`.

**Detection**: NVIDIA's `compute-sanitizer --tool memcheck` tracked GPU memory allocations and identified unreleased textures. Heap profiling showed `Renderable` destructors were called but derived destructors were not.

**Solution**: Added `virtual ~Renderable() = default;` to the base class. Audited all base classes with virtual methods across the codebase. Switched to `std::unique_ptr<Renderable>` to make ownership explicit.

**Prevention**: Rule — any class with virtual methods must have a virtual destructor. Enable clang-tidy `cppcoreguidelines-virtual-class-destructor`. Static analysis CI check: flag base classes with virtual methods lacking virtual destructors.

---

## Production Checklist
- [ ] Use virtual destructors for base classes
- [ ] Prefer composition over inheritance
- [ ] Use RAII for resource management
- [ ] Make interfaces small and focused
- [ ] Use `override` keyword for virtual functions
- [ ] Prefer smart pointers over raw pointers

## Maturity Levels
- **Beginner**: Basic classes, constructors, methods
- **Intermediate**: Inheritance, polymorphism, encapsulation
- **Advanced**: Multiple inheritance, virtual inheritance, CRTP

## Common Myths
- ❌ "OOP is always better than procedural"
- ❌ "More inheritance is better"
- ❌ "All classes need inheritance"

## One-Minute Revision
| Concept | Description |
|---------|-------------|
| Class | Blueprint for objects |
| Object | Instance of a class |
| Inheritance | IS-A relationship |
| Polymorphism | Same interface, different behavior |
| Encapsulation | Hide internal details |

## Related Topics
- [Templates](../03-templates/)
- [Design Patterns](../09-design-patterns/)
- [Memory Management](../05-memory-management/)