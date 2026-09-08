# Object-Oriented Programming (OOP) — C++

## Overview

## Why It Matters

Every complex system — a game engine managing thousands of entities, a database handling concurrent queries, a trading platform processing millions of orders — needs organization. When code lacks structure, it becomes a tangled web of functions and global state that is impossible to maintain, test, or extend. OOP provides the architectural grammar to model real-world entities and build systems that evolve without collapsing.

## What It Is

OOP in C++ organizes code around objects — instances of classes that bundle data and behavior. C++ supports encapsulation, inheritance, polymorphism, and abstraction to model complex systems.

## Learning Objectives

- Understand the four pillars of OOP: encapsulation, inheritance, polymorphism, and abstraction
- Design classes using access specifiers, constructors, and the Rule of Zero/Three/Five
- Implement single and multiple inheritance with virtual functions and virtual destructors
- Apply polymorphism through virtual dispatch and CRTP for static polymorphism
- Distinguish when to use inheritance versus composition
- Identify and prevent object slicing, diamond problem, and other C++ OOP pitfalls
- Apply SOLID principles to class design in production systems

## Prerequisites

- Basic C++ syntax: variables, functions, loops, conditionals
- Pointers and references (including `const` references)
- Dynamic memory allocation (`new`/`delete`) and smart pointers (`std::unique_ptr`, `std::shared_ptr`)
- Templates basics (for understanding CRTP and generic OOP patterns)
- Header files and compilation model (declarations vs. definitions)

## History

| Year | Milestone | Significance |
|------|-----------|--------------|
| 1979 | Bjarne Stroustrup starts "C with Classes" | OOP features added to C: classes, derived classes, strong type checking, inlining |
| 1983 | Renamed to C++ | `++` denotes evolution from C |
| 1985 | C++ 1.0 release | First commercial release; virtual functions, `const`, references introduced |
| 1989 | C++ 2.0 | Multiple inheritance, abstract classes, static/const member functions |
| 1998 | C++98 (ISO standard) | First ISO standard; STL integration; RTTI (`typeid`, `dynamic_cast`); exception handling matured |
| 2003 | C++03 | Bug fix release for C++98 |
| 2011 | C++11 | `override`/`final`, move semantics, `auto`, lambdas, `= default`/`= delete` — modern OOP idioms established |
| 2014 | C++14 | Relaxed `constexpr`, generic lambdas |
| 2017 | C++17 | `std::variant`, `std::optional`, structured bindings — alternatives to inheritance hierarchies |
| 2020 | C++20 | Concepts, ranges, coroutines — compile-time polymorphism via concepts challenges virtual-only designs |
| 2023 | C++23 | `std::expected`, `std::print` — further modernization of error handling patterns |

Key OOP evolution: C++ moved from C-style struct functions (1979) to full OOP with virtual dispatch (1985), multiple inheritance (1989), move semantics enabling efficient object transfer (2011), and concepts providing compile-time interface constraints (2020).

## Core Concepts

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

## Internal Working

### Vtable Layout

Every class with virtual functions has a **vtable** (virtual table) — an array of function pointers generated by the compiler. Each object contains a hidden **vptr** (virtual pointer) pointing to its class's vtable.

```
┌─────────────────────────────┐
│       Dog Object            │
├─────────────────────────────┤
│  vptr ──────────────────────┼──► Dog VTable
│  name_  ("Rex")             │    ┌──────────────────┐
│  age_   (5)                 │    │ &Dog::speak()    │
│  breed_ ("Labrador")        │    │ &Animal::describe│
└─────────────────────────────┘    │ &Dog::getBreed() │
                                   └──────────────────┘
```

When `animal->speak()` is called:
1. Compiler dereferences `vptr` to find the vtable
2. Indexes into vtable at the offset for `speak()`
3. Calls the function pointer found there (dynamic dispatch)

### Memory Layout of Objects

```cpp
class Base {
    int x_;          // 4 bytes
    virtual void f();
};

class Derived : public Base {
    int y_;          // 4 bytes
    void f() override;
};
```

Memory layout (typical x86-64):
```
Derived object:
┌──────────────────────┐  offset 0
│  vptr (8 bytes)      │  ← points to Derived's vtable
├──────────────────────┤  offset 8
│  x_  (4 bytes)       │  ← inherited from Base
├──────────────────────┤  offset 12
│  padding (4 bytes)   │  ← alignment
├──────────────────────┤  offset 16
│  y_  (4 bytes)       │  ← Derived's own member
└──────────────────────┘
Total: 24 bytes (with padding)
```

### RTTI (Run-Time Type Information)

RTTI enables type identification at runtime:

```cpp
#include <typeinfo>

Animal* animal = new Dog("Rex", 5, "Labrador");

// typeid — returns type_info object
std::cout << typeid(*animal).name();  // "4Dog" (mangled name)

// dynamic_cast — safe downcasting with type checking
if (Dog* dog = dynamic_cast<Dog*>(animal)) {
    std::cout << dog->getBreed();  // Safe: type verified at runtime
}

// dynamic_cast returns nullptr if cast fails (for pointers)
// or throws std::bad_cast (for references)
```

RTTI adds overhead (~1 vtable entry per class). Disable with `-fno-rtti` in performance-critical code.

## Syntax

### Class Declaration

```cpp
class ClassName {
public:       // Accessible everywhere
    ClassName();                      // Default constructor
    ClassName(int x);                 // Parameterized constructor
    ClassName(const ClassName&);      // Copy constructor
    ClassName(ClassName&&);           // Move constructor (C++11)
    ~ClassName();                     // Destructor
    ClassName& operator=(const ClassName&);  // Copy assignment
    ClassName& operator=(ClassName&&);       // Move assignment (C++11)

    int getX() const;                 // Const member function
    void setX(int x);                 // Mutable member function
    static int getCount();            // Static member function

private:      // Accessible only within class
    int x_;
    static int count_;
};

// Outside class definition
inline int ClassName::getX() const { return x_; }
```

### Inheritance Syntax

```cpp
class Base {
public:
    virtual void foo() = 0;           // Pure virtual (abstract)
    virtual void bar() {}             // Virtual with default impl
    virtual ~Base() = default;        // Virtual destructor
};

class Derived : public Base {         // Public inheritance
public:
    void foo() override;              // Override (C++11)
    void bar() final;                 // Prevent further override (C++11)
};

class Final final : public Derived {}; // Prevent inheritance (C++11)
```

### Operator Overloading

```cpp
class Vector2D {
    float x_, y_;
public:
    Vector2D(float x, float y) : x_(x), y_(y) {}

    // Binary operator as member
    Vector2D operator+(const Vector2D& rhs) const {
        return {x_ + rhs.x_, y_ + rhs.y_};
    }

    // Comparison operator as friend
    friend bool operator==(const Vector2D& a, const Vector2D& b) {
        return a.x_ == b.x_ && a.y_ == b.y_;
    }

    // Stream insertion as friend
    friend std::ostream& operator<<(std::ostream& os, const Vector2D& v) {
        return os << "(" << v.x_ << ", " << v.y_ << ")";
    }
};
```

## Examples

### Easy — Employee System

```cpp
#include <iostream>
#include <string>
#include <vector>
#include <memory>

class Employee {
protected:
    std::string name_;
    double salary_;
public:
    Employee(std::string name, double salary)
        : name_(std::move(name)), salary_(salary) {}
    virtual ~Employee() = default;

    virtual double calculatePay() const { return salary_; }
    virtual std::string getRole() const { return "Employee"; }

    void print() const {
        std::cout << getRole() << ": " << name_
                  << " | Pay: $" << calculatePay() << "\n";
    }
};

class Manager : public Employee {
    std::vector<std::unique_ptr<Employee>> team_;
public:
    Manager(std::string name, double salary)
        : Employee(std::move(name), salary) {}

    void addReport(std::unique_ptr<Employee> emp) {
        team_.push_back(std::move(emp));
    }

    double calculatePay() const override {
        double total = salary_;
        for (const auto& e : team_) total += e->calculatePay() * 0.1;
        return total;
    }

    std::string getRole() const override { return "Manager"; }
};

int main() {
    auto alice = std::make_unique<Manager>("Alice", 90000);
    alice->addReport(std::make_unique<Employee>("Bob", 60000));
    alice->addReport(std::make_unique<Employee>("Carol", 65000));
    alice->print();  // Manager: Alice | Pay: $121500
}
```

### Medium — Shape Factory with Polymorphism

```cpp
#include <iostream>
#include <memory>
#include <vector>
#include <cmath>

class Shape {
public:
    virtual ~Shape() = default;
    virtual double area() const = 0;
    virtual std::string name() const = 0;
    virtual std::unique_ptr<Shape> clone() const = 0;
};

class Circle : public Shape {
    double radius_;
public:
    explicit Circle(double r) : radius_(r) {}
    double area() const override { return M_PI * radius_ * radius_; }
    std::string name() const override { return "Circle"; }
    std::unique_ptr<Shape> clone() const override {
        return std::make_unique<Circle>(*this);
    }
};

class Rectangle : public Shape {
    double w_, h_;
public:
    Rectangle(double w, double h) : w_(w), h_(h) {}
    double area() const override { return w_ * h_; }
    std::string name() const override { return "Rectangle"; }
    std::unique_ptr<Shape> clone() const override {
        return std::make_unique<Rectangle>(*this);
    }
};

class Triangle : public Shape {
    double base_, height_;
public:
    Triangle(double b, double h) : base_(b), height_(h) {}
    double area() const override { return 0.5 * base_ * height_; }
    std::string name() const override { return "Triangle"; }
    std::unique_ptr<Shape> clone() const override {
        return std::make_unique<Triangle>(*this);
    }
};

// Factory function
std::unique_ptr<Shape> createShape(const std::string& type, double a, double b = 0) {
    if (type == "circle")    return std::make_unique<Circle>(a);
    if (type == "rectangle") return std::make_unique<Rectangle>(a, b);
    if (type == "triangle")  return std::make_unique<Triangle>(a, b);
    return nullptr;
}

int main() {
    std::vector<std::unique_ptr<Shape>> shapes;
    shapes.push_back(createShape("circle", 5.0));
    shapes.push_back(createShape("rectangle", 3.0, 4.0));
    shapes.push_back(createShape("triangle", 6.0, 3.0));

    double total = 0;
    for (const auto& s : shapes) {
        std::cout << s->name() << ": " << s->area() << "\n";
        total += s->area();
    }
    std::cout << "Total area: " << total << "\n";
}
```

### Hard — CRTP Static Polymorphism + Visitor

```cpp
#include <iostream>
#include <string>
#include <vector>
#include <memory>

// CRTP for static polymorphism — no vtable overhead
template <typename Derived>
class Visitable {
public:
    void accept(class Visitor& v) {
        v.visit(static_cast<Derived&>(*this));
    }
};

class Document : public Visitable<Document> {
    std::string text_;
public:
    explicit Document(std::string t) : text_(std::move(t)) {}
    const std::string& text() const { return text_; }
};

class Image : public Visitable<Image> {
    std::string path_;
    int width_, height_;
public:
    Image(std::string p, int w, int h)
        : path_(std::move(p)), width_(w), height_(h) {}
    const std::string& path() const { return path_; }
    int width() const { return width_; }
    int height() const { return height_; }
};

// Visitor with compile-time dispatch
class PrintVisitor {
public:
    void visit(Document& d) {
        std::cout << "Document: " << d.text() << "\n";
    }
    void visit(Image& i) {
        std::cout << "Image: " << i.path()
                  << " (" << i.width() << "x" << i.height() << ")\n";
    }
};

class SizeEstimateVisitor {
    size_t total_ = 0;
public:
    void visit(Document& d) { total_ += d.text().size(); }
    void visit(Image& i) { total_ += i.width() * i.height() * 4; }
    size_t total() const { return total_; }
};

int main() {
    Document doc("Hello, CRTP!");
    Image img("photo.png", 1920, 1080);

    PrintVisitor printer;
    doc.accept(printer);  // Static dispatch — no vtable
    img.accept(printer);

    SizeEstimateVisitor estimator;
    doc.accept(estimator);
    img.accept(estimator);
    std::cout << "Estimated size: " << estimator.total() << " bytes\n";
}
```

### Enterprise — Plugin Architecture with Abstract Factory

```cpp
#include <iostream>
#include <memory>
#include <string>
#include <unordered_map>
#include <functional>
#include <stdexcept>

// Plugin interface
class Logger {
public:
    virtual ~Logger() = default;
    virtual void log(const std::string& level, const std::string& msg) = 0;
    virtual std::string name() const = 0;
};

// Concrete implementations
class ConsoleLogger : public Logger {
public:
    void log(const std::string& level, const std::string& msg) override {
        std::cout << "[" << level << "] " << msg << "\n";
    }
    std::string name() const override { return "ConsoleLogger"; }
};

class FileLogger : public Logger {
    std::string filepath_;
public:
    explicit FileLogger(std::string path) : filepath_(std::move(path)) {}
    void log(const std::string& level, const std::string& msg) override {
        // In production: write to file
        std::cout << "[FILE:" << filepath_ << "] [" << level << "] " << msg << "\n";
    }
    std::string name() const override { return "FileLogger"; }
};

// Factory registry (plugin system)
class LoggerFactory {
    using Creator = std::function<std::unique_ptr<Logger>(const std::string&)>;
    std::unordered_map<std::string, Creator> registry_;
    static LoggerFactory* instance_;

    LoggerFactory() = default;
public:
    static LoggerFactory& instance() {
        if (!instance_) instance_ = new LoggerFactory();
        return *instance_;
    }

    void registerLogger(const std::string& type, Creator creator) {
        registry_[type] = std::move(creator);
    }

    std::unique_ptr<Logger> create(const std::string& type, const std::string& config = "") {
        auto it = registry_.find(type);
        if (it == registry_.end())
            throw std::runtime_error("Unknown logger type: " + type);
        return it->second(config);
    }
};

// Auto-registration helper
struct LoggerRegistrar {
    LoggerRegistrar(const std::string& type, LoggerFactory::Creator creator) {
        LoggerFactory::instance().registerLogger(type, std::move(creator));
    }
};

// Register built-in loggers
static LoggerRegistrar console_reg("console", [](const std::string&) {
    return std::make_unique<ConsoleLogger>();
});
static LoggerRegistrar file_reg("file", [](const std::string& path) {
    return std::make_unique<FileLogger>(path);
});

int main() {
    auto console = LoggerFactory::instance().create("console");
    auto file = LoggerFactory::instance().create("file", "/var/log/app.log");

    console->log("INFO", "Application started");
    file->log("ERROR", "Disk full");
}
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

## Production Notes

- **Compiler**: GCC 10+, Clang 12+, MSVC 19.22+ required for C++20 concepts support
- **RTTI overhead**: `dynamic_cast` and `typeid` add ~5-15ns per call; disable with `-fno-rtti` in hot paths
- **Vtable overhead**: Each virtual function adds ~2-5ns dispatch + 1 vptr per object (8 bytes on 64-bit)
- **Binary compatibility**: Changing class layout (adding virtual functions, reordering members) breaks ABI — recompile all dependent libraries
- **Exception handling**: OOP-heavy code with exceptions adds ~1-3% overhead when exceptions are rare; use `-fno-exceptions` only if no exceptions are thrown
- **Name mangling**: C++ symbol names are mangled for type safety; use `extern "C"` for C interfaces
- **Multiple inheritance**: Adds complexity to object layout and RTTI; avoid unless necessary (e.g., mixin pattern)

## Performance Considerations

| Technique | Cost | When to Use |
|-----------|------|-------------|
| Virtual dispatch | ~2-5ns + cache miss | Runtime polymorphism, plugin systems |
| CRTP (static polymorphism) | Zero overhead | Compile-time known types, hot loops |
| `dynamic_cast` | ~5-15ns | Rare downcasts; avoid in tight loops |
| `std::function` | ~10-50ns (heap alloc) | Callbacks, non-virtual polymorphism |
| Object slicing | Silent data loss | Never — use smart pointers |
| Deep copy (Rule of Three) | O(n) per copy | Minimize copies; use move semantics |
| vtable cache misses | ~10-100ns (L3 miss) | Flatten hierarchies; prefer composition |

**Cache-friendly OOP**: Group related objects together (SoA vs AoS). Virtual dispatch causes pointer-chasing — consider batching by type before processing.

```cpp
// Bad: pointer-chasing through vtables
for (auto* shape : shapes)       // random vtable pointers
    total += shape->area();      // cache misses

// Better: sort by type first
std::sort(shapes.begin(), shapes.end(),
    [](const auto& a, const auto& b) {
        return typeid(*a).before(typeid(*b));
    });
for (auto* shape : shapes)      // sequential vtable access
    total += shape->area();     // fewer cache misses
```

## Best Practices

| Practice | Rationale |
|----------|-----------|
| Prefer composition over inheritance | Reduces coupling, easier to test, avoids fragile base class |
| Use `override` and `final` | Catch signature mismatches at compile time |
| Make base destructors virtual | Prevents resource leaks when deleting via base pointer |
| Use `explicit` on single-arg constructors | Prevents implicit conversions |
| Apply Rule of Zero | Let compiler generate special members; avoid manual resource management |
| Catch exceptions by `const&` | Prevents object slicing in exception handling |
| Use `enum class` over `enum` | Type-safe enumerations prevent implicit conversions |
| Prefer `std::unique_ptr` for exclusive ownership | Clear ownership semantics, no reference counting overhead |
| Use `std::shared_ptr` only when shared ownership is needed | Reference counting has overhead; prefer unique ownership |
| Limit inheritance depth to 2-3 levels | Deep hierarchies are fragile and hard to understand |
| Use pure virtual functions for interfaces | Forces derived classes to implement; documents contracts |
| Prefer `= default` and `= delete` | Explicit intent for special member functions |
| Use RAII for all resource management | Automatic cleanup, exception-safe |

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

### Incident 4: Vtable Corruption from Use-After-Free
**Problem**: A trading platform crashed intermittently with segfaults during order matching, but only under high load. The crash pattern was non-deterministic.

**Cause**: A derived `MarketOrder` object was deleted while a polymorphic container still held a raw pointer to it. The container's iteration later accessed the freed memory, dereferencing a corrupted vptr. The vtable pointer now pointed to garbage, and the virtual dispatch jumped to an invalid address.

**Impact**: ~12 crashes per day during peak trading hours. Each crash required full process restart, causing 2-3 second gaps in order processing. $2.3M in potential trades lost during outages over one week.

**Detection**: AddressSanitizer (ASan) in staging reproduced the crash within 5 minutes of load testing. ASan reported "heap-use-after-free" with the exact vtable address.

**Solution**: Replaced all raw pointers in the order container with `std::unique_ptr<Order>`. Added a custom deleter that logs destruction for debugging. Used `std::weak_ptr` for observer references that might outlive the observed object.

**Prevention**: Enable ASan in CI/nightly builds. Never store raw owning pointers in containers. Use `std::weak_ptr` for non-owning observer patterns.

---

### Incident 5: Multiple Inheritance Vtable Ambiguity
**Problem**: A sensor fusion system produced incorrect readings when combining data from multiple sensor interfaces. The system used multiple inheritance, and calls through one base class unexpectedly invoked methods from the wrong base.

**Cause**: `FusedSensor` inherited from both `TemperatureSensor` and `HumiditySensor`, which both inherited from `Sensor`. Without virtual inheritance, `FusedSensor` contained two copies of `Sensor`. When casting to `Sensor*`, the compiler chose the first base in the inheritance list, causing method calls to dispatch to the wrong vtable.

```cpp
// BUG: Ambiguous base — two copies of Sensor
class TemperatureSensor : public Sensor { /* ... */ };
class HumiditySensor : public Sensor { /* ... */ };
class FusedSensor : public TemperatureSensor, public HumiditySensor {};

FusedSensor fs;
Sensor* s = &fs;  // ERROR: ambiguous — which Sensor?
```

**Impact**: Sensor readings were offset by 10-15% for 3 months before detection. No crashes, but data quality degraded silently. Customer reports of "inaccurate humidity readings" traced to this root cause.

**Detection**: Sanitizer builds flagged ambiguous base conversion as a compiler error. Manual code review identified the missing `virtual` keyword.

**Solution**: Applied virtual inheritance: `class TemperatureSensor : virtual public Sensor`. Added `static_assert(std::is_base_of_v<Sensor, FusedSensor>)` to verify hierarchy at compile time.

**Prevention**: Enable `-Wambiguous-base` warning. Use `virtual` inheritance when multiple inheritance paths share a common base. Prefer composition over multiple inheritance for combining interfaces.

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

## Cross-References
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
6. **What is the Rule of Five and when does it apply?**: If a class manages a raw resource (raw pointer, file handle, socket), define all five: destructor, copy constructor, copy assignment, move constructor, move assignment. The Rule of Three (dtor + copy ctor + copy assignment) predates C++11. In modern C++, prefer the Rule of Zero by using RAII types like `std::unique_ptr`.
7. **What are the trade-offs between `std::unique_ptr` and `std::shared_ptr`?**: `unique_ptr` has zero overhead (same as raw pointer), enforces exclusive ownership, and should be the default. `shared_ptr` uses reference counting (~16 bytes overhead per control block), enables shared ownership, but introduces atomic reference count operations and potential cycles. Use `weak_ptr` to break cycles in `shared_ptr` graphs.
8. **Why is `override` important and what happens without it?**: `override` tells the compiler to verify the function actually overrides a base class virtual. Without it, a typo in the signature (e.g., `void speak() const` vs `void speak()`) silently creates a new function instead of overriding. The base implementation runs unexpectedly — a subtle, hard-to-find bug.
9. **What is object slicing and how do you prevent it?**: Object slicing occurs when a derived object is assigned to a base class variable by value — the derived-specific members are silently truncated. Prevent it by using pointers (`Base*`), smart pointers (`std::unique_ptr<Base>`), or references (`Base&`) for polymorphic objects.
10. **Explain the difference between `virtual` inheritance and regular inheritance**: Regular inheritance means each derived class gets its own copy of the base class members. Virtual inheritance ensures a single shared copy of the virtual base, used to solve the diamond problem. The trade-off is slightly more complex object layout (vbase pointer) and runtime overhead.
11. **When should you prefer composition over inheritance?**: Prefer composition when the relationship is "has-a" rather than "is-a", when you need flexibility to change behavior at runtime, when inheritance would expose base class implementation details, or when the inheritance hierarchy is deeper than 2-3 levels. Composition reduces coupling and makes unit testing easier.
12. **How does `std::function` relate to OOP polymorphism?**: `std::function` provides type-erased callable wrappers — an alternative to virtual functions for runtime polymorphism. Instead of defining a virtual interface, pass lambdas or function objects. Trade-off: slight heap allocation overhead (~10-50ns) but more flexible for callbacks and strategies.
13. **What is the fragile base class problem?**: Changes to a base class (adding data members, changing virtual function order) can break derived classes without compile errors. The derived class's assumptions about memory layout or vtable structure become invalid. Mitigate by keeping hierarchies shallow, using final on leaf classes, and recompiling all dependents.
14. **Explain the purpose of `final` on classes and methods**: `final` prevents further overriding of a virtual method or inheritance from a class. On methods: enables devirtualization — the compiler can inline the call instead of using vtable dispatch. On classes: documents that the hierarchy is complete and allows compiler optimizations.
15. **How do you implement a polymorphic clone pattern in C++?**: Define a virtual `clone()` method in the base class that returns `std::unique_ptr<Base>`. Each derived class overrides it to return a copy of itself. This enables deep copying of polymorphic objects without knowing the concrete type at the call site. Example: `virtual std::unique_ptr<Shape> clone() const = 0;`

## References

- [C++ Core Guidelines — Class Design](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-ctor)
- [CppReference — Virtual Functions](https://en.cppreference.com/w/cpp/language/virtual)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF)](https://www.amazon.com/Design-Patterns-Elements-Reusable-Object-Oriented/dp/0201633612)
- [SOLID Principles in C++](https://www.oreilly.com/library/view/clean-code/9780136083238/)
