# Design Patterns — C++

## Overview

Design patterns are reusable, battle-tested solutions to recurring software design problems. They are not libraries or frameworks — they are templates for solving common challenges like object creation, communication between objects, and composing structures. In C++, patterns interact directly with language features: virtual dispatch, templates, RAII, smart pointers, and move semantics. Understanding patterns in C++ means understanding how language mechanics shape the solution.

## Why It Matters

Design patterns are not about writing clever code — they're about communicating solutions. When you say "we should use the Strategy pattern here," every developer on your team immediately understands the intent. Patterns are a shared vocabulary for solving recurring design problems like processing payments with multiple methods without writing giant if/else chains.

## What It Is

Design patterns are reusable solutions to common software design problems, providing a shared vocabulary and proven approaches for structuring code, including creational, structural, and behavioral patterns.

## Learning Objectives

- Identify which design pattern solves a given recurring design problem
- Implement the six core patterns (Singleton, Factory, Observer, Strategy, Decorator, Command) in modern C++
- Distinguish between creational, structural, and behavioral pattern categories
- Apply C++-specific optimizations (CRTP, templates, smart pointers, lambdas) to pattern implementations
- Recognize when patterns add unnecessary complexity (YAGNI)
- Thread-safe pattern implementations using `std::mutex`, `std::call_once`, and C++11 local statics

## Prerequisites

- **Module 02 — OOP**: Polymorphism, inheritance, encapsulation, abstract classes, virtual functions
- **Module 03 — Templates**: Function templates, class templates, template specialization, SFINAE basics
- **Module 06 — Smart Pointers**: `std::unique_ptr`, `std::shared_ptr`, `std::weak_ptr` for ownership management
- **Module 08 — Modern C++**: Lambda expressions, `std::function`, move semantics

## History

The Gang of Four (GoF) — Erich Gamma, Richard Helm, Ralph Johnson, and John Vlissides — published *Design Patterns: Elements of Reusable Object-Oriented Software* in 1994. The book catalogued 23 patterns discovered across multiple industries and languages. C++ was one of the primary implementation languages in the book due to its support for both object-oriented and generic programming. Over time, C++ patterns evolved: C++11 lambdas simplified Strategy and Command patterns, CRTP replaced virtual dispatch for compile-time polymorphism, and C++20 concepts now enable compile-time pattern constraints. Modern C++ patterns blend OOP inheritance hierarchies with template metaprogramming, producing zero-overhead abstractions.

## Production Notes

- Patterns are a shared vocabulary, not mandatory architecture. Use them when a clear recurring problem exists.
- In C++, prefer `std::unique_ptr` over raw pointers for ownership in Factory and Decorator patterns.
- Meyer's Singleton (C++11 local static) is thread-safe without external synchronization — prefer it over `std::call_once` for simplicity.
- Template-based patterns (CRTP, template factories) resolve at compile time with zero runtime cost.
- Observer patterns in production must handle observer lifetime carefully — use `std::weak_ptr` to avoid dangling pointers.
- Decorator chains should be shallow (2-3 levels). Deep chains hurt debuggability and add heap allocation overhead.

## Core Concepts

| Concept | Description | C++ Mechanism |
|---------|-------------|---------------|
| **Creational** | Control object creation (Singleton, Factory) | Private constructors, `static` methods, template factories |
| **Structural** | Compose objects into larger structures (Adapter, Decorator) | Inheritance + composition, `std::unique_ptr` wrapping |
| **Behavioral** | Define communication between objects (Observer, Strategy, Command) | Virtual dispatch, `std::function`, lambdas |
| **Polymorphism** | Interface-based abstraction enabling interchangeable implementations | Virtual functions, CRTP for static polymorphism |
| **Encapsulation** | Hide implementation details behind stable interfaces | Pimpl idiom, `private`/`protected` access control |
| **Open/Closed Principle** | Open for extension, closed for modification | Strategy, Decorator, Template Method patterns |
| **Composition over Inheritance** | Prefer has-a over is-a for behavior reuse | Decorator, Strategy, Command hold interface pointers |
| **RAII** | Resource acquisition tied to object lifetime | Smart pointers in Factory, Observer, Decorator |

## Engineering Decision Framework

| Problem | Pattern | When to Use | Anti-Pattern Warning |
|---------|---------|-------------|---------------------|
| Need exactly one instance (config, logger) | Singleton | When global access is truly needed | Overuse leads to hidden dependencies |
| Object creation is complex | Factory / Abstract Factory | When creation logic varies or is distributed | Don't use for simple construction |
| Algorithm varies at runtime | Strategy | When you need to swap algorithms without changing context | Don't use for single-algorithm cases |
| One-to-many notification | Observer | When state changes need to broadcast | Watch for memory leaks from unregistered observers |
| Add behavior without modifying class | Decorator | When subclassing would create combinatorial explosion | Don't use when a simple method works |
| Interface incompatibility | Adapter | When integrating third-party or legacy code | Don't use when you can modify the original interface |
| Simplify complex subsystem | Facade | When subsystem is too complex for callers | Don't use when callers need fine-grained control |
| Sequential/parallel algorithm selection | Strategy | When algorithm selection is dynamic | Avoid for compile-time-known algorithms |

## Internal Working

### How Patterns Work at Compile Time

**Template-based patterns** (CRTP Singleton, template Factory) are resolved during compilation. The compiler generates specialized code for each concrete type, eliminating virtual dispatch overhead. CRTP embeds the derived class name as a template parameter, allowing the base class to call derived-specific methods without runtime polymorphism.

**Template specialization** allows factories to generate different creation logic per type. The compiler selects the correct specialization at compile time, producing zero-overhead object creation.

### How Patterns Work at Runtime

**Virtual-dispatch patterns** (Observer, Strategy, Decorator) use vtable indirection. When you call `strategy->sort()`, the CPU follows a pointer to the vtable, loads the function pointer, and calls it. This is one indirect branch — typically 1-2 ns overhead on modern CPUs.

**`std::function` type erasure** wraps any callable (lambda, function pointer, functor) behind a uniform interface. Internally it uses small-buffer optimization (SBO) for callables up to ~24 bytes, avoiding heap allocation. Larger callables are heap-allocated.

**Smart pointer patterns** (`unique_ptr` in Factory, `shared_ptr`/`weak_ptr` in Observer) add reference counting overhead. `shared_ptr` increment/decrement is atomic (thread-safe), adding ~10-20 ns per operation. `weak_ptr::lock()` checks a control block before returning a `shared_ptr`.

### Memory Layout

```
Singleton (Meyer's):     Single static instance on stack (BSS segment)
Factory (virtual):       vtable pointer + factory state → heap-allocated products
Observer:                Subject holds vector<weak_ptr<Observer>>; Observer holds shared_ptr to itself
Strategy:                Context holds unique_ptr<StrategyBase>; strategy owns vtable + data
Decorator:               Chain of heap-objects, each wrapping a unique_ptr<Component>
Command:                 Heap-allocated command objects with captured state
```

## Syntax

### Singleton — Meyer's (C++11)
```cpp
class Singleton {
    Singleton() = default;                           // private ctor
public:
    static Singleton& getInstance() {                // thread-safe in C++11+
        static Singleton instance;
        return instance;
    }
    Singleton(const Singleton&) = delete;            // no copy
    Singleton& operator=(const Singleton&) = delete; // no assign
};
```

### Factory — Template Registration
```cpp
template <typename Base, typename... Args>
class Factory {
    using Creator = std::function<std::unique_ptr<Base>(Args...)>;
    std::unordered_map<std::string, Creator> registry_;
public:
    void registerType(const std::string& key, Creator creator) {
        registry_[key] = std::move(creator);
    }
    std::unique_ptr<Base> create(const std::string& key, Args... args) {
        auto it = registry_.find(key);
        return (it != registry_.end()) ? it->second(std::forward<Args>(args)...) : nullptr;
    }
};
```

### Observer — weak_ptr Storage
```cpp
class Subject {
    std::vector<std::weak_ptr<Observer>> observers_;
public:
    void attach(std::shared_ptr<Observer> obs) {
        observers_.push_back(obs);
    }
    void notify(const Event& e) {
        for (auto it = observers_.begin(); it != observers_.end(); ) {
            if (auto obs = it->lock()) {
                obs->update(e);
                ++it;
            } else {
                it = observers_.erase(it);  // prune expired
            }
        }
    }
};
```

### Strategy — Lambda-Based (C++11)
```cpp
class Sorter {
    std::function<void(std::vector<int>&)> strategy_;
public:
    void setStrategy(std::function<void(std::vector<int>&)> s) {
        strategy_ = std::move(s);
    }
    void sort(std::vector<int>& data) {
        if (strategy_) strategy_(data);
    }
};
// Usage: sorter.setStrategy([](auto& v){ std::sort(v.begin(), v.end()); });
```

### Decorator — RAII Chain
```cpp
class Component {
public:
    virtual ~Component() = default;
    virtual int execute() const = 0;
};

class Decorator : public Component {
protected:
    std::unique_ptr<Component> wrapped_;
public:
    explicit Decorator(std::unique_ptr<Component> c) : wrapped_(std::move(c)) {}
};

class LoggingDecorator : public Decorator {
public:
    using Decorator::Decorator;
    int execute() const override {
        std::cout << "Before\n";
        int result = wrapped_->execute();
        std::cout << "After\n";
        return result;
    }
};
```

### Command — with Undo
```cpp
class Command {
public:
    virtual ~Command() = default;
    virtual void execute() = 0;
    virtual void undo() = 0;
};

class TextInsertCommand : public Command {
    std::string& text_;
    std::string insertion_;
    size_t position_;
public:
    TextInsertCommand(std::string& text, std::string ins, size_t pos)
        : text_(text), insertion_(std::move(ins)), position_(pos) {}
    void execute() override { text_.insert(position_, insertion_); }
    void undo() override { text_.erase(position_, insertion_.size()); }
};
```

## Expanded Code Examples

### Singleton — Thread-Safe (C++11 and Later)

```cpp
#include <iostream>
#include <mutex>
#include <string>

class Logger {
    static Logger* instance_;
    static std::mutex mutex_;
    std::string last_message_;

    // Private constructor — no external instantiation
    Logger() = default;

public:
    // Meyer's Singleton (C++11 thread-safe local static)
    static Logger& getInstance() {
        static Logger instance;  // Constructed once, thread-safe in C++11+
        return instance;
    }

    // Delete copy/move to prevent duplicates
    Logger(const Logger&) = delete;
    Logger& operator=(const Logger&) = delete;
    Logger(Logger&&) = delete;
    Logger& operator=(Logger&&) = delete;

    void log(const std::string& message) {
        std::lock_guard<std::mutex> lock(mutex_);
        last_message_ = message;
        std::cout << "[LOG] " << message << "\n";
    }

    std::string getLastMessage() const {
        std::lock_guard<std::mutex> lock(mutex_);
        return last_message_;
    }
};

// Usage
void use_logger() {
    Logger::getInstance().log("Application started");
    Logger::getInstance().log("Processing request");
    // Both calls refer to the same instance
}
```

### Factory Method Pattern

```cpp
#include <iostream>
#include <memory>
#include <string>
#include <unordered_map>
#include <functional>

// Product interface
class Shape {
public:
    virtual ~Shape() = default;
    virtual void draw() const = 0;
    virtual double area() const = 0;
    virtual std::string type() const = 0;
};

// Concrete products
class Circle : public Shape {
    double radius_;
public:
    explicit Circle(double r) : radius_(r) {}
    void draw() const override { std::cout << "Drawing circle (r=" << radius_ << ")\n"; }
    double area() const override { return 3.14159 * radius_ * radius_; }
    std::string type() const override { return "Circle"; }
};

class Rectangle : public Shape {
    double width_, height_;
public:
    Rectangle(double w, double h) : width_(w), height_(h) {}
    void draw() const override { std::cout << "Drawing rect (" << width_ << "x" << height_ << ")\n"; }
    double area() const override { return width_ * height_; }
    std::string type() const override { return "Rectangle"; }
};

// Factory with registration
class ShapeFactory {
    using Creator = std::function<std::unique_ptr<Shape>(double, double)>;
    std::unordered_map<std::string, Creator> registry_;

public:
    void registerShape(const std::string& name, Creator creator) {
        registry_[name] = std::move(creator);
    }

    std::unique_ptr<Shape> create(const std::string& name, double a = 0, double b = 0) {
        auto it = registry_.find(name);
        if (it != registry_.end()) {
            return it->second(a, b);
        }
        return nullptr;
    }
};

// Usage
void factory_example() {
    ShapeFactory factory;
    factory.registerShape("circle", [](double r, double) {
        return std::make_unique<Circle>(r);
    });
    factory.registerShape("rect", [](double w, double h) {
        return std::make_unique<Rectangle>(w, h);
    });

    auto c = factory.create("circle", 5.0);
    auto r = factory.create("rect", 3.0, 4.0);

    c->draw();  // Drawing circle (r=5)
    r->draw();  // Drawing rect (3x4)
    std::cout << "Circle area: " << c->area() << "\n";
}
```

### Observer Pattern

```cpp
#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <functional>

// Subject — the thing being observed
class EventEmitter {
    std::vector<std::function<void(const std::string&)>> listeners_;
public:
    void on(std::function<void(const std::string&)> callback) {
        listeners_.push_back(std::move(callback));
    }

    void emit(const std::string& event) {
        for (auto& listener : listeners_) {
            listener(event);
        }
    }
};

// Observer examples
class Logger {
public:
    void handleEvent(const std::string& event) {
        std::cout << "[Logger] Event: " << event << "\n";
    }
};

class AlertSystem {
public:
    void handleEvent(const std::string& event) {
        if (event.find("error") != std::string::npos) {
            std::cout << "[Alert] CRITICAL: " << event << "\n";
        }
    }
};

// Usage
void observer_example() {
    EventEmitter emitter;
    Logger logger;
    AlertSystem alerts;

    emitter.on([&logger](const std::string& e) { logger.handleEvent(e); });
    emitter.on([&alerts](const std::string& e) { alerts.handleEvent(e); });

    emitter.emit("user login");      // Both observers notified
    emitter.emit("disk error");      // Both observers notified, alert fires
}
```

### Strategy Pattern

```cpp
#include <iostream>
#include <memory>
#include <vector>
#include <algorithm>

// Strategy interface
class SortStrategy {
public:
    virtual ~SortStrategy() = default;
    virtual void sort(std::vector<int>& data) = 0;
    virtual std::string name() const = 0;
};

// Concrete strategies
class BubbleSort : public SortStrategy {
public:
    void sort(std::vector<int>& data) override {
        // Bubble sort implementation
        for (size_t i = 0; i < data.size(); ++i) {
            for (size_t j = 0; j < data.size() - 1 - i; ++j) {
                if (data[j] > data[j + 1]) std::swap(data[j], data[j + 1]);
            }
        }
    }
    std::string name() const override { return "BubbleSort"; }
};

class QuickSort : public SortStrategy {
public:
    void sort(std::vector<int>& data) override {
        std::sort(data.begin(), data.end());
    }
    std::string name() const override { return "QuickSort"; }
};

// Context — uses a strategy
class Sorter {
    std::unique_ptr<SortStrategy> strategy_;
public:
    void setStrategy(std::unique_ptr<SortStrategy> s) {
        strategy_ = std::move(s);
    }

    void doSort(std::vector<int>& data) {
        if (strategy_) {
            std::cout << "Using " << strategy_->name() << "\n";
            strategy_->sort(data);
        }
    }
};

// Usage
void strategy_example() {
    std::vector<int> data = {5, 3, 8, 1, 9, 2};

    Sorter sorter;
    sorter.setStrategy(std::make_unique<QuickSort>());
    sorter.doSort(data);

    for (int x : data) std::cout << x << " ";  // 1 2 3 5 8 9
    std::cout << "\n";
}
```

### Decorator Pattern

```cpp
#include <iostream>
#include <memory>
#include <string>

// Component interface
class Coffee {
public:
    virtual ~Coffee() = default;
    virtual double cost() const = 0;
    virtual std::string description() const = 0;
};

// Concrete component
class SimpleCoffee : public Coffee {
public:
    double cost() const override { return 2.00; }
    std::string description() const override { return "Simple coffee"; }
};

// Decorator base
class CoffeeDecorator : public Coffee {
protected:
    std::unique_ptr<Coffee> coffee_;
public:
    explicit CoffeeDecorator(std::unique_ptr<Coffee> c) : coffee_(std::move(c)) {}
};

// Concrete decorators
class MilkDecorator : public CoffeeDecorator {
public:
    using CoffeeDecorator::CoffeeDecorator;
    double cost() const override { return coffee_->cost() + 0.50; }
    std::string description() const override { return coffee_->description() + " + milk"; }
};

class SugarDecorator : public CoffeeDecorator {
public:
    using CoffeeDecorator::CoffeeDecorator;
    double cost() const override { return coffee_->cost() + 0.25; }
    std::string description() const override { return coffee_->description() + " + sugar"; }
};

// Usage
void decorator_example() {
    auto coffee = std::make_unique<SimpleCoffee>();
    coffee = std::make_unique<MilkDecorator>(std::move(coffee));
    coffee = std::make_unique<SugarDecorator>(std::move(coffee));

    std::cout << coffee->description() << ": $" << coffee->cost() << "\n";
    // Simple coffee + milk + sugar: $2.75
}
```

### Adapter Pattern

```cpp
#include <iostream>
#include <string>

// Legacy interface — cannot modify
class LegacyLogger {
public:
    void writeLog(const char* message) {
        std::cout << "[Legacy] " << message << "\n";
    }
};

// Modern interface — what client code expects
class ModernLogger {
public:
    virtual ~ModernLogger() = default;
    virtual void log(const std::string& message) = 0;
};

// Adapter — bridges legacy to modern
class LegacyLoggerAdapter : public ModernLogger {
    LegacyLogger& legacy_;
public:
    explicit LegacyLoggerAdapter(LegacyLogger& legacy) : legacy_(legacy) {}

    void log(const std::string& message) override {
        legacy_.writeLog(message.c_str());  // Adapt the call
    }
};

// Client code only knows ModernLogger
void process(ModernLogger& logger) {
    logger.log("Request processed successfully");
}

// Usage
void adapter_example() {
    LegacyLogger legacy;
    LegacyLoggerAdapter adapter(legacy);
    process(adapter);  // Works with legacy via adapter
}
```

## Examples

### Easy — Singleton (Configuration Manager)

```cpp
#include <iostream>
#include <string>
#include <unordered_map>

class Config {
    std::unordered_map<std::string, std::string> settings_;
    Config() = default;

public:
    static Config& get() {
        static Config instance;
        return instance;
    }

    Config(const Config&) = delete;
    Config& operator=(const Config&) = delete;

    void set(const std::string& key, const std::string& value) {
        settings_[key] = value;
    }

    std::string getSetting(const std::string& key) const {
        auto it = settings_.find(key);
        return (it != settings_.end()) ? it->second : "";
    }
};

// Usage
void easy_example() {
    Config::get().set("theme", "dark");
    Config::get().set("lang", "en");
    std::cout << Config::get().getSetting("theme") << "\n";  // dark
}
```

### Medium — Factory + Strategy (Payment Processing)

```cpp
#include <iostream>
#include <memory>
#include <string>
#include <unordered_map>
#include <functional>

class PaymentStrategy {
public:
    virtual ~PaymentStrategy() = default;
    virtual bool pay(double amount) = 0;
    virtual std::string name() const = 0;
};

class CreditCardPayment : public PaymentStrategy {
    std::string card_number_;
public:
    explicit CreditCardPayment(std::string card) : card_number_(std::move(card)) {}
    bool pay(double amount) override {
        std::cout << "Charged $" << amount << " to card " << card_number_.substr(0, 4) << "****\n";
        return true;
    }
    std::string name() const override { return "CreditCard"; }
};

class PayPalPayment : public PaymentStrategy {
    std::string email_;
public:
    explicit PayPalPayment(std::string email) : email_(std::move(email)) {}
    bool pay(double amount) override {
        std::cout << "Paid $" << amount << " via PayPal (" << email_ << ")\n";
        return true;
    }
    std::string name() const override { return "PayPal"; }
};

class PaymentProcessor {
    std::unique_ptr<PaymentStrategy> strategy_;
public:
    void setStrategy(std::unique_ptr<PaymentStrategy> s) { strategy_ = std::move(s); }
    bool process(double amount) {
        if (!strategy_) return false;
        return strategy_->pay(amount);
    }
};

// Factory to create payment strategies
class PaymentFactory {
    using Creator = std::function<std::unique_ptr<PaymentStrategy>(std::string)>;
    std::unordered_map<std::string, Creator> registry_;
public:
    void registerMethod(const std::string& key, Creator c) { registry_[key] = std::move(c); }
    std::unique_ptr<PaymentStrategy> create(const std::string& method, const std::string& detail) {
        auto it = registry_.find(method);
        return (it != registry_.end()) ? it->second(detail) : nullptr;
    }
};

// Usage
void medium_example() {
    PaymentFactory factory;
    factory.registerMethod("cc", [](std::string s) { return std::make_unique<CreditCardPayment>(s); });
    factory.registerMethod("paypal", [](std::string s) { return std::make_unique<PayPalPayment>(s); });

    PaymentProcessor processor;
    processor.setStrategy(factory.create("cc", "4111111111111234"));
    processor.process(99.99);

    processor.setStrategy(factory.create("paypal", "user@example.com"));
    processor.process(49.50);
}
```

### Hard — CRTP Singleton + Template Factory (Game Engine)

```cpp
#include <iostream>
#include <memory>
#include <string>
#include <unordered_map>
#include <functional>

// CRTP Singleton — compile-time, zero overhead
template <typename Derived>
class Singleton {
protected:
    Singleton() = default;
public:
    static Derived& instance() {
        static Derived inst;
        return inst;
    }
    Singleton(const Singleton&) = delete;
    Singleton& operator=(const Singleton&) = delete;
};

// Template Factory with automatic registration via static init
template <typename Base, typename... Args>
class TemplateFactory {
    using Creator = std::function<std::unique_ptr<Base>(Args...)>;
    std::unordered_map<std::string, Creator> registry_;

    TemplateFactory() = default;

public:
    static TemplateFactory& instance() {
        static TemplateFactory factory;
        return factory;
    }

    bool registerType(const std::string& key, Creator creator) {
        registry_[key] = std::move(creator);
        return true;
    }

    std::unique_ptr<Base> create(const std::string& key, Args... args) {
        auto it = registry_.find(key);
        return (it != registry_.end()) ? it->second(std::forward<Args>(args)...) : nullptr;
    }
};

// Auto-registration helper
template <typename Base, typename Derived, typename... Args>
struct AutoRegister {
    static bool registered;
    static bool doRegister() {
        TemplateFactory<Base, Args...>::instance().registerType(
            Derived::typeName(),
            [](Args... args) -> std::unique_ptr<Base> {
                return std::make_unique<Derived>(std::forward<Args>(args)...);
            }
        );
        return true;
    }
};

// Game entity hierarchy
class Entity {
public:
    virtual ~Entity() = default;
    virtual void update(double dt) = 0;
    virtual std::string typeName() const = 0;
};

class Player : public Entity, public Singleton<Player> {
    double health_ = 100.0;
public:
    static std::string typeName() { return "Player"; }
    void update(double dt) override { health_ -= dt * 0.5; }
    double health() const { return health_; }
};

class Enemy : public Entity {
    std::string type_;
public:
    explicit Enemy(std::string t) : type_(std::move(t)) {}
    static std::string typeName() { return "Enemy"; }
    void update(double dt) override { /* AI logic */ }
};

// Usage
void hard_example() {
    auto& factory = TemplateFactory<Entity>::instance();
    factory.registerType("enemy", [](std::string t) { return std::make_unique<Enemy>(t); });

    auto enemy = factory.create("enemy", "goblin");
    enemy->update(0.016);

    auto& player = Player::instance();  // CRTP singleton
    player.update(0.016);
    std::cout << "Player health: " << player.health() << "\n";
}
```

### Enterprise — Observer + Command (Undo/Redo Event System)

```cpp
#include <iostream>
#include <memory>
#include <string>
#include <vector>
#include <unordered_map>
#include <functional>
#include <sstream>

// Event system using Observer pattern
class Event {
public:
    virtual ~Event() = default;
    virtual std::string type() const = 0;
    virtual std::string serialize() const = 0;
};

class EventHandler {
public:
    virtual ~EventHandler() = default;
    virtual void handle(const Event& e) = 0;
};

class EventBus {
    std::vector<std::weak_ptr<EventHandler>> listeners_;
public:
    void subscribe(std::shared_ptr<EventHandler> h) { listeners_.push_back(h); }

    void publish(const Event& e) {
        for (auto it = listeners_.begin(); it != listeners_.end(); ) {
            if (auto handler = it->lock()) {
                handler->handle(e);
                ++it;
            } else {
                it = listeners_.erase(it);
            }
        }
    }
};

// Command pattern for undo/redo
class Command {
public:
    virtual ~Command() = default;
    virtual void execute() = 0;
    virtual void undo() = 0;
    virtual std::string describe() const = 0;
};

class CommandHistory {
    std::vector<std::unique_ptr<Command>> undo_stack_;
    std::vector<std::unique_ptr<Command>> redo_stack_;
public:
    void execute(std::unique_ptr<Command> cmd) {
        cmd->execute();
        undo_stack_.push_back(std::move(cmd));
        redo_stack_.clear();
    }

    void undo() {
        if (undo_stack_.empty()) return;
        undo_stack_.back()->undo();
        redo_stack_.push_back(std::move(undo_stack_.back()));
        undo_stack_.pop_back();
    }

    void redo() {
        if (redo_stack_.empty()) return;
        redo_stack_.back()->execute();
        undo_stack_.push_back(std::move(redo_stack_.back()));
        redo_stack_.pop_back();
    }
};

// Concrete event
class DocumentChangedEvent : public Event {
    std::string content_;
public:
    explicit DocumentChangedEvent(std::string c) : content_(std::move(c)) {}
    std::string type() const override { return "DocumentChanged"; }
    std::string serialize() const override { return content_; }
};

// Concrete command
class EditCommand : public Command {
    std::string& document_;
    std::string old_text_;
    std::string new_text_;
    size_t position_;
    EventBus& bus_;
public:
    EditCommand(std::string& doc, std::string old_t, std::string new_t, size_t pos, EventBus& bus)
        : document_(doc), old_text_(std::move(old_t)), new_text_(std::move(new_t)), position_(pos), bus_(bus) {}

    void execute() override {
        document_.replace(position_, old_text_.size(), new_text_);
        bus_.publish(DocumentChangedEvent(document_));
    }

    void undo() override {
        document_.replace(position_, new_text_.size(), old_text_);
        bus_.publish(DocumentChangedEvent(document_));
    }

    std::string describe() const override {
        return "Edit at pos " + std::to_string(position_);
    }
};

// Usage
void enterprise_example() {
    std::string document = "Hello World";
    EventBus bus;
    CommandHistory history;

    // Subscribe to events
    auto logger = std::make_shared<EventHandler>();
    bus.subscribe(logger);

    // Execute edits
    history.execute(std::make_unique<EditCommand>(document, "World", "C++", 6, bus));
    std::cout << document << "\n";  // "Hello C++"

    history.undo();
    std::cout << document << "\n";  // "Hello World"

    history.redo();
    std::cout << document << "\n";  // "Hello C++"
}
```

## Production Incidents

### Incident 1: Observer Memory Leak
**Problem**: A trading application's UI progressively slowed down over hours of operation, eventually becoming unresponsive.

**Cause**: Market data observers were registered for each stock symbol but never unregistered when components were destroyed. Each new chart panel added observers; when panels were closed, the subject still held dangling pointers. The observer list grew unbounded.

**Impact**: After 8 hours of trading, the subject held 140,000+ observer pointers (most dangling). Notification iteration took 200ms+ per tick. UI lagged behind market by 5+ seconds.

**Detection**: Memory profiler showed growing `std::vector<Observer*>` in the MarketData subject. Valgrind reported use-after-free on observer calls.

**Solution**: Switched to `std::weak_ptr` for observer storage. Subjects hold `weak_ptr`, observers are `shared_ptr`. Before notifying, check `weak_ptr::expired()`. Alternatively, use RAII registration with automatic unregistration in observer destructors.

**Prevention**: Every observer must have a corresponding unregistration path. Use RAII wrappers for observer registration. Add a "max observers" guard in debug builds.

### Incident 2: Singleton Preventing Unit Testing
**Problem**: A database connection manager implemented as a Singleton could not be mocked in unit tests. Tests hit the real database, causing flaky failures and slow test suites.

**Cause**: The Singleton's `getInstance()` returned a concrete `DatabaseConnection` object. No interface existed to substitute a mock. All code directly called `DBManager::getInstance().query(...)`.

**Impact**: Unit tests took 45 minutes (database setup/teardown). 20% of test runs failed due to database state contamination. Developers stopped running tests locally.

**Solution**: Extracted an `IDatabaseConnection` interface. The Singleton now returns `IDatabaseConnection&`. In production, it returns the real connection. In tests, a test fixture injects a mock: `DBManager::setInstance(mock_connection)`. After tests, restore the real instance.

**Prevention**: Never use Singleton without an interface. Prefer dependency injection. If you must use Singleton, provide a `setInstance()` for testing (or use a service locator pattern).

### Incident 3: Decorator Chain Heap Explosion
**Problem**: An HTTP middleware pipeline using the Decorator pattern caused 2 GB of heap allocations per request under load.

**Cause**: Each middleware layer (auth, logging, rate-limiting, compression) was a separate heap-allocated Decorator wrapping a `unique_ptr<Component>`. With 8 middleware layers and 500 concurrent requests, the system created 4,000 heap objects per request cycle. The allocator fragmented under sustained load.

**Impact**: P99 latency spiked from 50 ms to 2.3 seconds. Memory usage grew linearly with concurrency. Production pods were OOM-killed every 4 hours.

**Detection**: `jemalloc` heap profiling showed 80% of allocations were small (64-128 byte) Decorator objects. Flame graph confirmed allocation hotspots in Decorator constructors.

**Solution**: Flattened the middleware chain into a single `Pipeline` class that holds a `vector<Middleware*>` (non-owning, middleware objects stored in a pool). Each middleware is a lightweight struct with `before()` and `after()` methods. The pipeline iterates the vector instead of following a pointer chain.

**Prevention**: Profile decorator-heavy code under production concurrency. Prefer flat composition (vector of handlers) over deep nesting (chain of wrappers) when performance matters. Use object pools for frequently created pattern objects.

### Incident 4: Factory Creating Objects with Circular Dependencies
**Problem**: A plugin system using the Factory pattern deadlocked during initialization when two plugins depended on each other.

**Cause**: Plugin A's factory creator called `PluginB::instance()`, and Plugin B's creator called `PluginA::instance()`. Both used Meyer's Singleton. During static initialization, the first call to `PluginA::instance()` triggered `PluginB::instance()`, which recursively called `PluginA::instance()` — causing a deadlock or undefined behavior on some compilers.

**Impact**: Application hung on startup in 30% of runs (depending on static initialization order). Remaining 70% worked only by luck (initialization order happened to avoid the cycle).

**Detection**: `gdb` backtrace showed threads stuck in `__cxa_guard_acquire`. AddressSanitizer reported stack overflow on the recursive path.

**Solution**: Broke the cycle by extracting shared state into a separate `Registry` class that both plugins depend on. Plugins no longer reference each other directly — they communicate through the Registry. The Registry is initialized first (no dependencies), then plugins are created in dependency order.

**Prevention**: Map plugin dependency graphs before implementing factories. Use lazy initialization (construct on first use) instead of static initialization for objects with interdependencies. Add a dependency resolver that topologically sorts plugins before creation.

### Incident 5: Strategy Pattern Causing Cache Misses
**Problem**: A hot loop processing 10 million elements/sec degraded 3x after switching from a simple `if/else` to the Strategy pattern.

**Cause**: The Strategy pattern introduced a virtual `process()` call in the inner loop. Each strategy was a separate class with its own vtable. The CPU's branch predictor could not predict the indirect call reliably, causing pipeline stalls and instruction cache misses. The `if/else` version had predictable branches that the CPU could optimize.

**Impact**: Throughput dropped from 10M to 3.3M elements/sec. Latency P99 increased from 12 ms to 38 ms. CPU utilization hit 100% (was 60%).

**Detection**: `perf stat` showed L1 instruction cache miss rate increased from 0.1% to 2.8%. `perf record` + flame graph revealed the indirect call as the hotspot.

**Solution**: Replaced runtime Strategy with a compile-time template parameter: `template <typename Strategy> void process(data)`. The compiler inlines the strategy into the hot loop, eliminating the indirect call. Alternatively, for cases where runtime switching is required, used `std::variant` + `std::visit` which enables direct devirtualization.

**Prevention**: Profile hot loops before and after introducing patterns. Prefer compile-time polymorphism (templates, CRTP) for performance-critical inner loops. Reserve runtime polymorphism (Strategy) for cases where algorithm selection truly varies at runtime.

## Production Checklist

- [ ] Identify the real problem before applying a pattern
- [ ] Prefer composition over inheritance
- [ ] Keep patterns simple — don't over-engineer
- [ ] Document which pattern is used and why
- [ ] Consider thread safety for shared state patterns
- [ ] Use RAII for resource management in patterns
- [ ] Test pattern interactions in integration tests
- [ ] Review patterns during code reviews

## Maturity Levels

| Level | Patterns |
|-------|----------|
| **Beginner** | Singleton, Factory, Observer |
| **Intermediate** | Strategy, Decorator, Adapter, Facade |
| **Advanced** | Composite, Flyweight, Chain of Responsibility, Command, Mediator |

## Common Myths — Debunked

| Myth | Reality |
|------|---------|
| "Patterns are always necessary" | No. If a simple `if/else` works, use it. Patterns solve recurring problems, not every problem. |
| "More patterns mean better design" | Pattern fever leads to over-engineering. YAGNI (You Aren't Gonna Need It) still applies. |
| "Patterns are language-specific" | Patterns are language-agnostic concepts. Implementation varies, but the idea is universal. |
| "Singletons are evil" | Singletons are fine when you truly need one instance and global access. Overuse is the problem. |
| "Factory always means Factory Method" | Factory can mean Simple Factory, Factory Method, or Abstract Factory. Be specific. |

## One-Minute Revision Table

| Pattern | Category | Purpose | Key Benefit |
|---------|----------|---------|-------------|
| Singleton | Creational | Ensure one instance | Global access point |
| Factory Method | Creational | Delegate instantiation to subclasses | Loose coupling |
| Observer | Behavioral | Notify dependents of state changes | Decoupled notification |
| Strategy | Behavioral | Swap algorithms at runtime | Open/Closed Principle |
| Decorator | Structural | Add behavior dynamically | Flexible composition |
| Adapter | Structural | Convert one interface to another | Integration compatibility |
| Facade | Structural | Simplify complex subsystems | Reduced complexity |
| Command | Behavioral | Encapsulate actions as objects | Undo/redo, queuing |
| Composite | Structural | Treat individual and uniform objects the same | Tree structures |
| Chain of Responsibility | Behavioral | Pass request along a chain of handlers | Decoupled sender/receiver |

## Cross-Linked Related Topics

- **OOP** → [Module 02: OOP](../02-oop/) — Patterns build on inheritance, polymorphism, encapsulation
- **Best Practices** → [Module 14: Best Practices](../14-best-practices/) — SOLID principles guide pattern selection
- **Testing** → [Module 10: Testing](../10-testing/) — Patterns should be testable; mocks use Adapter/Strategy
- **Modern C++** → [Module 08: Modern C++](../08-modern-cpp/) — Lambdas replace many simple Strategy/Command patterns
- **Smart Pointers** → [Module 06: Smart Pointers](../06-smart-pointers/) — Essential for ownership in Factory/Decorator patterns
- **Concurrency** → [Module 07: Concurrency](../07-concurrency/) — Thread-safe Observer, Singleton patterns

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Observer memory leak from unregistered listeners | `weak_ptr` + Valgrind leak check | Use `std::weak_ptr` for observer storage; verify with `valgrind --leak-check=full` |
| Singleton preventing unit test mocking | Interface extraction + dependency injection | Extract `ISingleton` interface; provide `setInstance()` for test fixture injection |
| Strategy pattern causing excessive heap allocation | Small buffer optimization + `std::function` | Use SBO-capable `std::function` or store small strategies inline |
| Factory creating objects with circular dependencies | Dependency graph visualization | Map factory registrations; break cycles with lazy initialization or `weak_ptr` |
| Decorator chain performance overhead | Profiler hotspot analysis | Profile decorator chains; consider compile-time decoration with CRTP for hot paths |

## Code Review Checklist

- [ ] Patterns applied only when a clear, recurring problem exists (YAGNI)
- [ ] Composition preferred over inheritance for behavior reuse
- [ ] Thread safety considered for shared-state patterns (Singleton, Observer)
- [ ] RAII used for resource management within pattern implementations
- [ ] Pattern intent and rationale documented in code comments
- [ ] Observer registrations have matching unregistrations (RAII cleanup)
- [ ] Singleton has an interface for testability and mock injection

## Performance Considerations

| Pattern | Overhead Source | Mitigation |
|---------|----------------|------------|
| Singleton (Meyer's) | None — static local variable, constructed once | Preferred implementation; no mutex needed in C++11+ |
| Singleton (double-checked locking) | `std::call_once` atomic overhead (~20 ns) | Use Meyer's Singleton instead when possible |
| Factory (virtual dispatch) | vtable indirection per creation call | Acceptable for object creation; not a hot path in most code |
| Factory (template/CRTP) | Zero runtime overhead — resolved at compile time | Use for performance-critical creation paths |
| Observer (shared_ptr) | Atomic ref-count increment/decrement (~10-20 ns per observer) | Use `weak_ptr` storage; prune expired observers during notification |
| Strategy (std::function) | Small-buffer optimization avoids heap for callables ≤24 bytes | Keep strategies small; use lambdas over stateful functors |
| Decorator (heap chain) | Each layer = 1 heap allocation + vtable indirection | Limit chain depth to 2-3; consider CRTP compile-time decoration for hot paths |
| Command (heap allocation) | Each command object heap-allocated | Pool-allocate frequently created commands (e.g., text edits in editor) |

## Best Practices

- **Apply patterns only when a clear, recurring problem exists.** An `if/else` with 2-3 branches is simpler than a Strategy pattern.
- **Prefer composition over inheritance.** Hold interface pointers (`unique_ptr<Base>`) rather than inheriting to extend behavior.
- **Use RAII for all pattern-owned resources.** Smart pointers in Factory and Decorator; RAII wrappers for Observer registration.
- **Interface-segregate Singletons.** Always expose an abstract interface behind the Singleton, enabling mock injection for tests.
- **Thread-safety by default.** Use C++11 local statics for Singleton, `std::mutex` for shared Observer state, `std::atomic` for counters.
- **Leverage C++11+ features.** Lambdas simplify Strategy and Command. `std::function` replaces manual functor classes. `std::variant` replaces Visitor hierarchies.
- **CRTP for zero-overhead polymorphism.** When the derived type is known at compile time, CRTP eliminates vtable dispatch entirely.
- **Document pattern rationale.** Comments should explain *why* a pattern was chosen, not *what* the pattern is.
- **Keep decorator chains shallow.** Deep chains hurt debuggability and add allocation overhead. If you need many layers, reconsider the design.
- **Clean up observers eagerly.** Don't rely on destructor-based unregistration alone — check `weak_ptr::expired()` during notification loops.

## Common Mistakes

| Mistake | Why It Hurts | Fix |
|---------|-------------|-----|
| Overusing Singleton | Hidden global state, untestable code, tight coupling | Prefer dependency injection; use Singleton only for genuinely singular resources (config, logger) |
| Raw pointers in Observer | Dangling pointers after observer destruction → use-after-free | Use `std::weak_ptr` for storage; `shared_ptr` for observer lifetime |
| Deep Decorator chains | Heap allocation per layer, hard to debug, unclear ownership | Limit to 2-3 layers; consider compile-time decoration with CRTP |
| Strategy with large state | `std::function` heap-allocates when callable exceeds SBO size (~24 bytes) | Keep strategies small; use `unique_ptr<StrategyBase>` for large strategies |
| Not deleting Singleton copy/move | Silent duplicate instances, violating the singleton guarantee | Delete copy/move constructors and assignment operators |
| Factory returning raw pointers | Caller must manage lifetime → leaks if not careful | Return `std::unique_ptr<Base>` from factory methods |
| Observer notification during modification | Iterator invalidation if observers modify the subject's observer list | Copy observer list before iterating, or use index-based iteration |
| Command without undo support | Incomplete Command pattern; users expect undo/redo | Always implement `undo()` alongside `execute()` in Command |
| Thread-unsafe Singleton in multi-threaded code | Data races on initialization (pre-C++11) or on mutable state | Use C++11 local statics; protect mutable state with `std::mutex` |
| Applying patterns to one-off problems | YAGNI — adds complexity without benefit | Use the simplest solution that works; refactor to a pattern if the problem recurs |

## Architecture Considerations

Design patterns are the shared vocabulary for recurring architectural problems. Creational patterns (Factory, Singleton) control object creation. Structural patterns (Adapter, Decorator, Facade) compose objects into larger structures. Behavioral patterns (Strategy, Observer, Command) define communication between objects. Patterns guide architectural decisions by encoding proven solutions, but they must be applied judiciously — over-engineering with patterns is worse than a simple `if/else`.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Factory Method | Decoupling object creation from usage | Loose coupling vs. indirection and debugging difficulty |
| Strategy | Swapping algorithms at runtime | Open/Closed Principle vs. virtual dispatch overhead |
| Observer | One-to-many event notification | Decoupled notification vs. memory leak risk from unregistered observers |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Singleton global state accessible from any thread | Race conditions, data corruption | Make Singleton thread-safe with `std::call_once` or C++11 local static |
| Observer holding dangling pointer to destroyed subject | Use-after-free, crash | Use `std::weak_ptr` for observer storage; check `expired()` before notification |
| Factory accepting unvalidated input strings | Object injection, unexpected behavior | Validate factory keys against a whitelist; use `std::variant` for type-safe creation |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | Lambdas replace simple Strategy/Command functors | Replace `std::function` with lambdas for short-lived strategies |
| C++17 | `std::variant` + `std::visit` for type-safe Visitor | Replace Visitor hierarchy with `std::variant` and overloaded `std::visit` |
| C++20 | Concepts for compile-time Strategy constraints | Replace virtual Strategy interface with concept-constrained templates |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `std::function` for type-erased callables | C++11 | Widely supported |
| `std::variant` for type-safe unions | C++17 | Widely supported |
| `std::visit` for variant dispatch | C++17 | Widely supported |
| Concepts for compile-time polymorphism | C++20 | Supported in GCC 10+, Clang 12+, MSVC 19.22+ |

## Interview Questions

1. **When should you use the Strategy pattern vs a simple `if/else`?**: Use Strategy when algorithms vary at runtime and you need to swap them without changing context. Use `if/else` when there are only 2-3 fixed algorithms and the switching logic is simple.
2. **How do you prevent memory leaks in the Observer pattern?**: Use `std::weak_ptr` for observer storage. Before notifying, check `weak_ptr::expired()`. Alternatively, use RAII registration — observers auto-unregister in their destructors.
3. **What makes a good Singleton?**: A good Singleton has an interface for testability, is thread-safe (C++11 local static), is lazy-initialized, and is genuinely needed as a single instance (e.g., configuration, logging). Most "Singletons" should be dependency-injected instead.
4. **How does the Adapter pattern differ from the Facade pattern?**: Adapter converts one interface to another (legacy → modern). Facade simplifies a complex subsystem behind a single, easy-to-use interface. Adapter targets interface incompatibility; Facade targets complexity reduction.
5. **When is the Decorator pattern preferable to inheritance?**: Use Decorator when you need to add behavior dynamically at runtime without creating a combinatorial explosion of subclasses. Decorators are composable and follow the Open/Closed Principle.
6. **How does CRTP improve the Singleton pattern?**: CRTP Singleton resolves the base class at compile time — `Singleton<Logger>` knows the derived type is `Logger` without virtual dispatch. This eliminates vtable overhead and enables inlining of `instance()`. The trade-off is that each derived type gets a separate Singleton (no shared base).
7. **What is the difference between Factory Method and Abstract Factory?**: Factory Method creates a single product via a virtual method (one creator, one product type). Abstract Factory creates families of related products (e.g., `WindowsButton` + `WindowsCheckbox` from `WindowsFactory`). Abstract Factory ensures product compatibility; Factory Method delegates a single creation decision.
8. **How would you make the Observer pattern thread-safe?**: Store observers as `std::weak_ptr` in the subject. During notification, call `weak_ptr::lock()` — if expired, prune the entry. Protect the observer list with `std::mutex`. Alternatively, use a lock-free approach with `std::atomic` flags on each observer and a separate notification queue.
9. **Can the Strategy pattern be implemented without virtual functions?**: Yes. Use `std::function<void(Data&)>` to store lambdas as strategies — no virtual dispatch, no derived classes. For zero-overhead, use template parameters: `template<typename F> void process(F strategy)`. C++20 concepts can constrain the template to ensure the callable has the right signature.
10. **When should you use Command vs Strategy?**: Command encapsulates an action with undo/redo, state, and a receiver — it's about *what happened*. Strategy encapsulates an algorithm that varies — it's about *how something is done*. Use Command for undo systems, transaction logs, and task queues. Use Strategy for interchangeable algorithms (sorting, validation, rendering).
11. **How does the Decorator pattern differ from middleware chains?**: They're conceptually similar — both wrap behavior. Decorator wraps a single interface and adds behavior before/after delegating. Middleware chains are typically linear pipelines (each stage calls `next()`). In C++, middleware often uses `std::function<std::string(Request)>` composition rather than class inheritance.
12. **What are the thread-safety guarantees of Meyer's Singleton?**: C++11 guarantees that function-local statics are initialized exactly once, even when called concurrently from multiple threads. The compiler inserts a hidden guard variable and uses `__cxa_guard_acquire`/`__cxa_guard_release`. After initialization, `getInstance()` is just a pointer return — no synchronization needed.
13. **How do you test code that uses the Singleton pattern?**: Extract an interface (e.g., `ILogger`). The Singleton returns a reference to the interface. In production, the concrete implementation is returned. In tests, inject a mock: provide a `setInstance()` method or use a service locator. Alternatively, use function injection — pass a factory function that returns the singleton instance.
14. **What is the OCP violation in the Factory pattern and how do you fix it?**: Adding a new product type requires modifying the factory's `create()` method with a new `if/else` or `switch` case. Fix it with registration: each product registers itself with the factory using a static initializer, so adding a new product requires zero changes to the factory class.
15. **How do patterns interact in real systems? Give an example.**: A payment processing system might use: Factory to create payment strategies, Strategy to select the payment algorithm, Observer to notify downstream systems (fraud detection, analytics), Command to encapsulate each transaction (for undo/redo), and Decorator to add logging or encryption around the payment flow. Patterns compose — a Factory can produce Strategy objects that implement Command interfaces.

## References

- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF)](https://www.amazon.com/Design-Patterns-Elements-Reusable-Object-Oriented/dp/0201633612)
- [Head First Design Patterns — Freeman & Robson](https://www.amazon.com/Head-First-Design-Patterns-Brain-Friendly/dp/0596007124)
- [C++ Design Patterns — Refactoring.Guru](https://refactoring.guru/design-patterns/cpp)
- [CppCon Talk: Design Patterns in Modern C++](https://youtube.com/cppcon)
