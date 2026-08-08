# Design Patterns — C++

## Why It Matters

Design patterns are not about writing clever code — they're about communicating solutions. When you say "we should use the Strategy pattern here," every developer on your team immediately understands the intent. Patterns are a shared vocabulary for solving recurring design problems like processing payments with multiple methods without writing giant if/else chains.

## What It Is

Design patterns are reusable solutions to common software design problems, providing a shared vocabulary and proven approaches for structuring code, including creational, structural, and behavioral patterns.

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
