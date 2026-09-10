# Senior Level C++ — C++

## Overview

Senior-level C++ is the pinnacle of C++ mastery — where deep language knowledge, architectural thinking, and engineering judgment converge. It encompasses template metaprogramming, SFINAE, constexpr evaluation, concepts, compile-time computation, and advanced design patterns. Senior C++ engineers make decisions that affect millions of lines of code and years of maintenance. They balance performance, safety, readability, and team velocity while mentoring others and shaping technical direction.

## Why It Matters

Senior-level C++ is not about knowing more syntax — it's about making better decisions. When you consider architecture choices, performance trade-offs, team processes, and technical strategy, you determine whether a project succeeds or fails. The best code is the code you don't write — every line is a liability that must be maintained, tested, and eventually removed.

## Learning Objectives

- Apply template metaprogramming and SFINAE to create type-safe, compile-time interfaces
- Design and implement advanced design patterns (Strategy, Observer, CRTP, Policy-Based)
- Use constexpr and consteval for compile-time computation and validation
- Implement C++20 concepts for constraining templates and improving error messages
- Apply custom allocators (arena, pool, slab) for performance-critical memory management
- Evaluate and choose architectural patterns (DDD, hexagonal, event-driven) for C++ systems
- Lead code reviews that identify architectural, performance, and maintainability issues
- Manage technical debt systematically and drive architectural improvements across teams
- Design systems with fault tolerance, observability, and graceful degradation
- Mentor junior and mid-level developers through architectural guidance and best practices

## What It Is

Senior-level C++ involves system design, architecture patterns like DDD and hexagonal architecture, technical debt management, and leadership in code quality and team processes that bridge the gap between writing code and building systems that scale. Senior C++ engineers leverage advanced language features like template metaprogramming, SFINAE, concepts, and constexpr to build type-safe, performant, and maintainable systems.

## Prerequisites

- [Module 01: Fundamentals](../01-fundamentals/) — Variables, types, control flow, functions
- [Module 02: Object-Oriented Programming](../02-oop/) — Classes, inheritance, polymorphism, RAII
- [Module 03: Templates](../03-templates/) — Function/class templates, template specialization, partial specialization
- [Module 04: STL](../04-stl/) — Containers, algorithms, iterators, functors
- [Module 05: Memory Management](../05-memory-management/) — Pointers, smart pointers, ownership semantics
- [Module 06: Pointers & References](../06-pointers-references/) — Raw pointers, references, move semantics
- [Module 07: Concurrency](../07-concurrency/) — Threads, locks, atomics, condition variables
- [Module 08: Exception Handling](../08-exception-handling/) — Exception safety, RAII patterns
- [Module 09: Design Patterns](../09-design-patterns/) — GoF patterns, SOLID principles
- [Module 10: Testing](../10-testing/) — Unit testing, mocking, TDD
- [Module 11: Performance](../11-performance/) — Profiling, cache optimization, SIMD
- [Module 12: File I/O](../12-file-io/) — Streams, file operations, serialization
- [Module 13: Build Systems](../13-build-systems/) — CMake, build configuration, dependency management
- [Module 14: Best Practices](../14-best-practices/) — Code style, naming conventions, code organization

## History

C++ has evolved from C with Classes (1979, Bjarne Stroustrup) through standardization (C++98) to the modern era. C++11 was a revolution — adding lambdas, move semantics, `auto`, and `constexpr`. C++14 and C++17 refined these with `std::optional`, `std::variant`, structured bindings, and filesystem. C++20 introduced concepts, ranges, coroutines, and modules — fundamentally changing how we write generic code. C++23 added `std::expected`, `std::print`, `std::mdspan`, and further constexpr expansion. Modern C++ emphasizes compile-time computation, type safety, and zero-cost abstractions, enabling senior engineers to write code that is both expressive and performant.

| Era | Key Features | Impact |
|-----|-------------|--------|
| C++98/03 | Templates, STL, exceptions | Foundation of generic programming |
| C++11/14 | Move semantics, lambdas, auto, constexpr | Modern idioms, safer code |
| C++17 | `std::optional`, `std::variant`, structured bindings, filesystem | Richer type system, less boilerplate |
| C++20 | Concepts, ranges, coroutines, modules | Type-safe generics, async patterns |
| C++23 | `std::expected`, `std::print`, consteval, `std::mdspan` | Better error handling, compile-time computation |

## Production Notes

Senior-level C++ systems require careful production considerations. Always define architecture decisions before implementation and document them with ADRs. Set up comprehensive observability (logs, metrics, traces) from day one. Track technical debt with severity levels and allocate sprint capacity for reduction. Conduct architecture reviews for significant changes. Write runbooks for production operations and disaster recovery. Balance feature velocity with reliability using error budgets. Test failure modes through chaos engineering. Mentor junior developers through code review and architectural guidance.

## Core Concepts

| Concept | Description | Senior Application |
|---------|-------------|-------------------|
| Template Metaprogramming | Code generation at compile time | Type-safe interfaces, compile-time validation, policy-based design |
| SFINAE | Substitution Failure Is Not An Error | Conditional template instantiation, overloading based on type traits |
| Concepts (C++20) | Named requirements for templates | Constrain templates, improve error messages, document intent |
| constexpr / consteval | Compile-time evaluation | Zero-cost abstraction, compile-time computation, invariants |
| CRTP | Curiously Recurring Template Pattern | Static polymorphism, mixin-based design |
| Move Semantics | Efficient transfer of resources | Zero-copy operations, RAII-aware APIs |
| Custom Allocators | Specialized memory management | Arena allocators for performance, pool allocators for fixed-size objects |
| Type Erasure | Heterogeneous containers of different types | `std::function`, `std::any`, `std::shared_ptr<void>` |
| Perfect Forwarding | Preserve value category of arguments | Universal references, factory functions |
| Ranges (C++20) | Lazy evaluation pipelines | Composable algorithms, readable data transformations |

## Internal Working

Senior C++ patterns exploit compile-time mechanisms to generate efficient, type-safe code. Template metaprogramming computes values and generates types at compile time — the compiler becomes a runtime-free code generator. SFINAE checks type traits during overload resolution, selecting the right template instantiation without runtime cost. Concepts (C++20) formalize these checks into named requirements, making template error messages readable. constexpr and consteval evaluate functions at compile time, moving computation from runtime to compilation. CRTP enables static polymorphism — the compiler resolves virtual calls at compile time. Policy-based design composes behavior through template parameters, creating flexible yet zero-cost abstractions. These mechanisms work because C++ templates are Turing-complete — any computation expressible in C++ can be evaluated at compile time, with the compiler generating optimized machine code for each specialization.

## Syntax

```cpp
// Concepts — constrained templates
template <typename T>
concept Hashable = requires(T a) {
    { std::hash<T>{}(a) } -> std::convertible_to<size_t>;
};

template <Hashable T>
void process(const T& value) { /* ... */ }

// SFINAE — conditional overloading
template <typename T>
std::enable_if_t<std::is_integral_v<T>> safe_divide(T a, T b) {
    if (b == 0) throw std::runtime_error("Division by zero");
    return a / b;
}

// constexpr — compile-time computation
constexpr int factorial(int n) {
    return (n <= 1) ? 1 : n * factorial(n - 1);
}
static_assert(factorial(5) == 120);

// consteval — mandatory compile-time evaluation
consteval int compile_time_only(int x) {
    return x * x;
}

// CRTP — static polymorphism
template <typename Derived>
class Base {
public:
    void interface() {
        static_cast<Derived*>(this)->implementation();
    }
};

// Perfect Forwarding
template <typename T>
void wrapper(T&& arg) {
    target(std::forward<T>(arg));
}
```

## Engineering Decision Framework

| Decision | Approach | When to Use | When NOT to Use |
|----------|----------|-------------|-----------------|
| Architecture | Monolith vs microservices vs modular monolith | Monolith for small teams, microservices at scale | Microservices for a 3-person team |
| Data storage | SQL vs NoSQL vs time-series vs graph | SQL for structured data, NoSQL for flexibility | NoSQL for highly relational data |
| Caching | In-memory vs Redis vs CDN | In-memory for single-server, Redis for distributed | Caching when you have no performance problem |
| Message queue | Kafka vs RabbitMQ vs ZeroMQ | Kafka for high throughput, RabbitMQ for complex routing | Kafka for simple request-response |
| Error strategy | Fail fast vs graceful degradation | Fail fast in development, graceful in production | Fail fast when users depend on the service |
| Technical debt | Fix now vs fix later vs document | Fix if it blocks features, document if it doesn't | Ignoring debt that compounds |

## Examples

### Domain-Driven Design

```cpp
#include <string>
#include <vector>
#include <stdexcept>
#include <memory>

// Value Object — immutable, identity-less
class Money {
    long amount_;  // In cents to avoid floating point
    std::string currency_;
public:
    Money(long cents, std::string currency)
        : amount_(cents), currency_(std::move(currency)) {
        if (cents < 0) throw std::invalid_argument("Money cannot be negative");
    }

    long amount() const { return amount_; }
    const std::string& currency() const { return currency_; }

    Money add(const Money& other) const {
        if (currency_ != other.currency_) {
            throw std::invalid_argument("Currency mismatch");
        }
        return Money(amount_ + other.amount_, currency_);
    }

    bool operator==(const Money& other) const {
        return amount_ == other.amount_ && currency_ == other.currency_;
    }
};

// Entity — has identity, mutable
class OrderId {
    std::string value_;
public:
    explicit OrderId(std::string id) : value_(std::move(id)) {}
    const std::string& value() const { return value_; }
};

// Aggregate Root — enforces invariants
class Order {
    OrderId id_;
    std::string customerId_;
    std::vector<std::pair<std::string, Money>> items_;
    bool confirmed_ = false;
    int version_ = 0;  // Optimistic concurrency

public:
    Order(OrderId id, std::string customerId)
        : id_(std::move(id)), customerId_(std::move(customerId)) {}

    void addItem(const std::string& productId, Money price) {
        if (confirmed_) {
            throw std::logic_error("Cannot modify confirmed order");
        }
        items_.emplace_back(productId, price);
    }

    Money total() const {
        Money sum(0, "USD");
        for (const auto& [_, price] : items_) {
            sum = sum.add(price);
        }
        return sum;
    }

    void confirm() {
        if (items_.empty()) {
            throw std::logic_error("Cannot confirm empty order");
        }
        confirmed_ = true;
    }

    int version() const { return version_; }
    const OrderId& id() const { return id_; }
    bool isConfirmed() const { return confirmed_; }
};
```

### Hexagonal Architecture (Ports and Adapters)

```cpp
#include <string>
#include <memory>
#include <functional>

// Port — interface defining what the domain needs
class OrderRepository {
public:
    virtual ~OrderRepository() = default;
    virtual std::unique_ptr<Order> findById(const OrderId& id) = 0;
    virtual void save(const Order& order) = 0;
};

class PaymentGateway {
public:
    virtual ~PaymentGateway() = default;
    virtual bool charge(const Money& amount, const std::string& customerId) = 0;
    virtual bool refund(const Money& amount, const std::string& transactionId) = 0;
};

class NotificationService {
public:
    virtual ~NotificationService() = default;
    virtual void sendOrderConfirmation(const std::string& customerId, const OrderId& orderId) = 0;
};

// Application Service — orchestrates domain objects through ports
class OrderService {
    std::unique_ptr<OrderRepository> repository_;
    std::unique_ptr<PaymentGateway> payment_;
    std::unique_ptr<NotificationService> notification_;

public:
    OrderService(std::unique_ptr<OrderRepository> repo,
                 std::unique_ptr<PaymentGateway> payment,
                 std::unique_ptr<OrderService> notification)
        : repository_(std::move(repo))
        , payment_(std::move(payment))
        , notification_(std::move(notification)) {}

    OrderId placeOrder(const std::string& customerId,
                       const std::vector<std::pair<std::string, Money>>& items) {
        OrderId orderId(generateId());
        Order order(orderId, customerId);

        for (const auto& [productId, price] : items) {
            order.addItem(productId, price);
        }

        if (!payment_->charge(order.total(), customerId)) {
            throw std::runtime_error("Payment failed");
        }

        order.confirm();
        repository_->save(order);
        notification_->sendOrderConfirmation(customerId, orderId);

        return orderId;
    }

private:
    std::string generateId();  // UUID or similar
};

// Adapter — concrete implementation of a port
class PostgresOrderRepository : public OrderRepository {
    // PostgreSQL implementation
public:
    std::unique_ptr<Order> findById(const OrderId& id) override {
        // SQL query implementation
        return nullptr;
    }

    void save(const Order& order) override {
        // INSERT/UPDATE implementation
    }
};
```

### Compile-Time Code Generation

```cpp
#include <array>
#include <cstddef>
#include <string_view>

// Compile-time string hashing for type-safe event routing
constexpr uint32_t hash_string(std::string_view str) {
    uint32_t hash = 2166136261u;  // FNV offset basis
    for (char c : str) {
        hash ^= static_cast<uint32_t>(c);
        hash *= 16777619u;  // FNV prime
    }
    return hash;
}

// Compile-time event type mapping
enum class EventType : uint32_t {
    None = 0,
    UserLogin = hash_string("UserLogin"),
    UserLogout = hash_string("UserLogout"),
    OrderPlaced = hash_string("OrderPlaced"),
    PaymentProcessed = hash_string("PaymentProcessed"),
};

// Compile-time lookup table
template <typename T, size_t N>
struct CompileTimeMap {
    std::array<std::pair<uint32_t, T>, N> entries;

    constexpr T get(uint32_t key, T default_value = T{}) const {
        for (const auto& [k, v] : entries) {
            if (k == key) return v;
        }
        return default_value;
    }
};

// Static assertion to verify at compile time
static_assert(hash_string("UserLogin") == 0x1a2b3c4d);  // Adjust to actual value

// Compile-time Fibonacci with memoization
template <size_t N>
struct Fibonacci {
    static constexpr long long value = Fibonacci<N-1>::value + Fibonacci<N-2>::value;
};

template <>
struct Fibonacci<0> { static constexpr long long value = 0; };
template <>
struct Fibonacci<1> { static constexpr long long value = 1; };

static_assert(Fibonacci<10>::value == 55);
```

### Custom Allocators for Performance

```cpp
#include <memory>
#include <vector>
#include <cstddef>

// Arena allocator — allocate from a pre-allocated block
class ArenaAllocator {
    struct Block {
        static constexpr size_t kBlockSize = 64 * 1024;  // 64KB
        alignas(16) char memory[kBlockSize];
        size_t used = 0;
    };

    std::vector<std::unique_ptr<Block>> blocks_;
    Block* current_ = nullptr;

    Block& allocate_block() {
        blocks_.push_back(std::make_unique<Block>());
        current_ = blocks_.back().get();
        return *current_;
    }

public:
    ArenaAllocator() { allocate_block(); }

    void* allocate(size_t size, size_t alignment = 16) {
        if (!current_ || current_->used + size > Block::kBlockSize) {
            allocate_block();
        }

        // Align
        size_t aligned = (current_->used + alignment - 1) & ~(alignment - 1);
        void* ptr = current_->memory + aligned;
        current_->used = aligned + size;
        return ptr;
    }

    void reset() {
        blocks_.clear();
        current_ = nullptr;
        allocate_block();
    }
};

// Usage: allocate 100K objects without individual new/delete
ArenaAllocator arena;
std::vector<int*> ptrs;
for (int i = 0; i < 100000; ++i) {
    ptrs.push_back(static_cast<int*>(arena.allocate(sizeof(int))));
}
// All freed at once when arena goes out of scope
```

### Technical Debt Tracking

```cpp
// DebtTracker — log technical debt with context
#include <string>
#include <vector>
#include <chrono>
#include <iostream>

struct DebtItem {
    enum class Severity { Low, Medium, High, Critical };

    std::string file;
    int line;
    std::string description;
    Severity severity;
    std::string workaround;
    std::chrono::system_clock::time_point created;

    void log() const {
        const char* sev_str[] = {"Low", "Medium", "High", "Critical"};
        std::cout << "[DEBT:" << sev_str[static_cast<int>(severity)]
                  << "] " << file << ":" << line << " - " << description
                  << " (workaround: " << workaround << ")\n";
    }
};

// Usage in code:
// TODO(debt): Refactor this to use std::expected when C++23 is available
// DEBT: Cache invalidation logic is duplicated — extract to shared utility
```

## Performance Considerations

| Pattern | Impact | Optimization |
|---------|--------|-------------|
| Template Metaprogramming | Zero runtime cost — computation at compile time | Use `constexpr` for simple computations, templates for type-level operations |
| SFINAE / Concepts | No runtime overhead — compile-time dispatch | Prefer concepts over SFINAE for readability; SFINAE for pre-C++20 codebases |
| Move Semantics | Eliminates unnecessary copies | Return by value for NRVO; use `std::move` for transferring ownership |
| Custom Allocators | Reduces allocation overhead by 10-100x | Arena allocators for batch allocations; pool allocators for fixed-size objects |
| Cache Locality | CPU cache hits vs misses (100x difference) | Use `std::vector` over `std::list`; prefer contiguous data structures |
| Constexpr Computation | Compile-time evaluation eliminates runtime work | Move complex computations to `constexpr`; use `consteval` for mandatory compile-time |
| Small String Optimization | Avoids heap allocation for short strings | `std::string` SSO is typically 15-22 bytes on most implementations |
| Lazy Evaluation (Ranges) | Avoids intermediate allocations | C++20 ranges compose without creating temporary containers |

## Best Practices

- Use concepts to constrain templates — makes error messages readable and code self-documenting
- Prefer `constexpr` over macros for compile-time constants and functions
- Apply CRTP for static polymorphism when virtual dispatch overhead is unacceptable
- Use policy-based design for configurable behavior without runtime cost
- Document architecture decisions with ADRs — future engineers need to understand why
- Allocate 15-20% of sprint capacity to technical debt reduction
- Define SLAs and error budgets before building features
- Use arena allocators in hot paths to avoid per-object allocation overhead
- Prefer `std::variant` over inheritance for closed type hierarchies
- Write `constexpr` unit tests that validate invariants at compile time
- Use `std::expected` (C++23) for error handling that must carry values
- Measure before optimizing — profiling reveals truth, intuition lies

## Common Mistakes

| Mistake | Consequence | Better Approach |
|---------|------------|-----------------|
| Overusing templates | Compilation time explodes, error messages unreadable | Use concepts to constrain; prefer `auto` parameters when possible |
| Skipping ADRs | Future engineers make contradictory decisions | Write ADRs for every significant architectural choice |
| Ignoring technical debt | Velocity drops, bugs increase, team morale suffers | Track debt with severity; allocate sprint capacity for reduction |
| Using microservices prematurely | Complexity exceeds team's ability to manage | Start with monolith; extract services only when boundaries are clear |
| Premature abstraction | Wasted effort on abstractions nobody uses | Wait for 3+ concrete examples before abstracting (Rule of Three) |
| Not testing failure modes | Production outages, data loss | Implement chaos engineering; test recovery procedures |
| Ignoring cache locality | 100x performance degradation | Prefer contiguous containers; design data structures for cache efficiency |
| Using raw `new`/`delete` | Memory leaks, dangling pointers | Use smart pointers exclusively; never call `new` directly |

## Cross-References

- **Design Patterns** → [Module 09: Design Patterns](../09-design-patterns/) — Patterns inform architecture decisions
- **Performance** → [Module 11: Performance](../11-performance/) — Optimization at the system level
- **Build Systems** → [Module 13: Build Systems](../13-build-systems/) — Monorepo management, CI/CD at scale
- **Best Practices** → [Module 14: Best Practices](../14-best-practices/) — SOLID principles, coding standards
- **Concurrency** → [Module 07: Concurrency](../07-concurrency/) — Distributed systems, lock-free patterns
- **Testing** → [Module 10: Testing](../10-testing/) — Integration testing, chaos engineering
- **Templates** → [Module 03: Templates](../03-templates/) — Foundation for metaprogramming
- **STL** → [Module 04: STL](../04-stl/) — Container and algorithm design

## Production Incidents

### Incident 1: Architecture Decision Reversal
**Problem**: A team chose microservices for a new product. After 6 months and 12 services, the system was so complex that deploying a simple feature took 2 weeks due to cross-service dependencies.

**Cause**: The team applied microservices architecture to a product with 3 developers and no clear service boundaries. Each service had its own database, API, and deployment pipeline. A simple "add field to user profile" change required coordinating 4 services.

**Impact**: Feature velocity dropped 80%. On-call burden increased 5x. Two senior developers left due to architectural frustration. The project was 6 months behind schedule.

**Detection**: Sprint retrospectives consistently flagged "too much coordination overhead." DORA metrics showed deploy frequency dropping from daily to weekly.

**Solution**: Consolidated 12 services into 3 well-bounded modules within a single deployable unit (modular monolith). Kept service boundaries at business domain boundaries (users, orders, payments) rather than technical boundaries (auth-service, notification-service, etc.).

**Prevention**: Start with a monolith. Extract services only when you have clear service boundaries and the team size demands it. Measure before and after any architecture change.

### Incident 2: Premature Abstraction
**Problem**: A team built a "generic framework" for API endpoints that added 3 layers of abstraction before writing any business logic. Six months later, no two endpoints used the framework the same way.

**Cause**: The team tried to abstract common patterns before understanding the patterns. They built for flexibility instead of simplicity. The framework added 500 lines of boilerplate per endpoint.

**Impact**: New developers needed 2 weeks to understand the framework. Simple endpoints took 2 days instead of 2 hours. The framework was abandoned after 8 months, wasting ~500 engineering hours.

**Detection**: Code review feedback consistently complained about complexity. New developer onboarding surveys showed framework as the #1 pain point.

**Solution**: Deleted the framework. Each endpoint is now a simple function with direct database and HTTP calls. Common patterns are extracted into small utility functions, not large abstractions. Code volume dropped 70%.

**Prevention**: YAGNI (You Aren't Gonna Need It). Wait until you have 3+ concrete examples before abstracting. Prefer small utility functions over large frameworks. Code should be easy to delete.

### Incident 3: Lack of Error Budget
**Problem**: A team shipped features at the cost of reliability. After 3 months of "move fast," the system had 99.5% uptime instead of 99.99%. Customer churn spiked 20%.

**Cause**: No error budget policy existed. The team optimized exclusively for feature velocity. Reliability was always "someone else's problem." No monitoring or alerting was set up for SLA violations.

**Impact**: 20% customer churn. $2M in lost revenue. Emergency reliability sprint took 3 months. Team morale dropped.

**Detection**: Customer support escalation volume. Revenue dashboard showing churn rate.

**Solution**: Implemented error budget policy: 99.99% uptime = 52 minutes of downtime per year. If the budget is spent, features freeze until reliability is restored. Added SLA monitoring dashboards. Created reliability rotation.

**Prevention**: Define SLAs before building features. Implement error budget policies. Make reliability everyone's responsibility. Monitor and alert on SLA violations.

### Incident 4: Template Metaprogramming Compilation Explosion
**Problem**: A C++ codebase used heavy template metaprogramming with SFINAE. Adding a new type trait caused compilation times to jump from 10 minutes to 45 minutes. Developers stopped running full builds, leading to integration failures.

**Cause**: Templates were instantiated for every combination of types, creating exponential instantiation paths. SFINAE checks were deeply nested, forcing the compiler to evaluate dozens of alternative overloads. No concepts were used — only raw SFINAE with `std::enable_if`.

**Impact**: Build times increased 4.5x. Developers committed code without testing locally. Integration failures spiked 300%. Code review velocity dropped because reviewers couldn't understand template error messages.

**Detection**: Build system metrics showed compilation time increasing. CI pipeline alerts flagged builds exceeding 30-minute threshold. Developer surveys reported frustration with error messages.

**Solution**: Introduced C++20 concepts to replace SFINAE. Added `requires` clauses to templates, reducing overload resolution complexity. Implemented build caching (ccache) and module-based compilation where possible. Added `static_assert` with descriptive messages to catch errors early.

**Prevention**: Use concepts instead of SFINAE where possible — they generate clearer errors and reduce compiler work. Monitor build times continuously. Use build caching. Prefer simple template designs over clever metaprogramming.

### Incident 5: Move Semantics Violation Causing Silent Data Corruption
**Problem**: A senior engineer wrote a function that returned a `std::vector<std::string>` by value. Inside, a helper function accidentally used `std::move` on a reference that was being iterated, causing the vector's elements to be in a moved-from state. The program compiled but produced garbage output intermittently.

**Cause**: The developer used `std::move` on a `const` reference inside a range-based for loop. The compiler silently applied move semantics because the type was not const-qualified. The moved-from strings were empty or corrupted, but the program continued without crashing.

**Impact**: Production data pipeline silently dropped 15% of records for 3 days. Customer-facing reports showed incorrect data. Engineering spent 48 hours debugging across 3 teams before identifying the root cause.

**Detection**: Data quality monitoring detected anomalies in report output. Customer complaints flagged incorrect data. No compilation or runtime errors were logged — the bug was silent.

**Solution**: Replaced `std::move` on loop variables with `const auto&`. Added static analysis tools (clang-tidy) to detect move-on-const patterns. Implemented data validation checks at pipeline boundaries to catch silent corruption.

**Prevention**: Never `std::move` from a range-based for loop variable. Enable clang-tidy's `bugprone-use-after-move` checks. Add data validation at system boundaries. Use `const` references by default in loops.

## Production Checklist

- [ ] Define system architecture before writing code
- [ ] Document architecture decisions (ADRs)
- [ ] Set up error budgets and SLA monitoring
- [ ] Track technical debt with severity and workarounds
- [ ] Conduct architecture reviews for significant changes
- [ ] Mentor junior developers through code review
- [ ] Write runbooks for production operations
- [ ] Plan for disaster recovery and failover
- [ ] Measure team velocity and adjust processes
- [ ] Balance feature work with technical debt reduction
- [ ] Ensure every service has observability (logs, metrics, traces)
- [ ] Test failure modes (chaos engineering)

## Maturity Levels

| Level | Capabilities |
|-------|-------------|
| **Advanced** | Architecture design, performance optimization, code review leadership |
| **Expert** | System design at scale, team process improvement, technical strategy |
| **Principal** | Organization-wide technical direction, cross-team architecture, business alignment |

## Common Myths — Debunked

| Myth | Reality |
|------|---------|
| "Senior means writing complex code" | Senior means writing simple code that solves complex problems. Complexity is a cost, not a feature. |
| "Experience is the only thing that matters" | Deliberate practice, feedback, and learning matter more than years of experience. |
| "Technical skills are enough" | Communication, leadership, and business understanding are equally important. |
| "Architecture must be perfect upfront" | Architecture evolves. Start simple, refactor when patterns emerge. |
| "More abstraction is better" | Abstraction has a cost. Only abstract when you have concrete, repeated patterns. |
| "Code reviews slow down development" | Code reviews prevent bugs, share knowledge, and improve code quality. The investment pays off. |

## One-Minute Revision Table

| Topic | Description | Key Insight |
|-------|-------------|-------------|
| DDD | Domain-Driven Design | Model code around business domains, not technical layers |
| Hexagonal Architecture | Ports and Adapters | Isolate business logic from infrastructure |
| ADRs | Architecture Decision Records | Document why decisions were made, not just what |
| Error Budgets | Reliability vs velocity balance | When budget is spent, features freeze |
| Technical Debt | Deliberate vs accidental | Track and manage debt like financial debt |
| Compile-Time Computation | constexpr, templates | Move work from runtime to compile time |
| Custom Allocators | Arena, pool, slab | Optimize memory allocation patterns |
| Chaos Engineering | Test failure modes | Deliberately break things to find weaknesses |
| Template Metaprogramming | Compile-time code generation | Zero-cost type-safe abstractions |
| SFINAE | Substitution Failure Is Not An Error | Conditional template selection without errors |
| Concepts | Named template requirements | Readable, constrained template interfaces |
| CRTP | Curiously Recurring Template Pattern | Static polymorphism, zero-cost abstraction |
| Policy-Based Design | Composable template parameters | Flexible behavior without virtual dispatch |
| Type Erasure | Heterogeneous interfaces | `std::function`, `std::any` — hide types behind interfaces |
| Move Semantics | Efficient resource transfer | Zero-copy ownership transfer with `std::move` |
| constexpr / consteval | Compile-time evaluation | Compute at compile time, not runtime |
| std::variant | Type-safe unions | Closed type hierarchies without inheritance |
| std::expected | Error handling with values | C++23 replacement for error-code patterns |

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Architecture decision causing scaling bottleneck | DORA metrics + load testing | Measure deploy frequency, lead time, MTTR, change failure rate; load test to find architectural limits |
| Premature abstraction wasting engineering time | YAGNI audit + code deletion | Count usage sites of abstractions; delete those used < 3 times; prefer utility functions over frameworks |
| Technical debt compounding causing velocity drop | Debt tracker + sprint allocation | Track debt items with severity; allocate 20% of sprint capacity to debt reduction |
| Error budget exceeded causing customer churn | SLA monitoring + feature freeze | Implement error budget policy; freeze features when budget is spent until reliability is restored |
| Cross-service dependency causing deployment delays | Service dependency graph analysis | Map service dependencies; eliminate unnecessary coupling; use event-driven communication |

## Code Review Checklist

- [ ] Architecture decisions documented (ADRs) before implementation
- [ ] System has observability (logs, metrics, traces) for all critical paths
- [ ] Error budgets defined and monitored for SLA compliance
- [ ] Technical debt tracked with severity and workarounds documented
- [ ] Disaster recovery and failover plans tested
- [ ] Runbooks exist for production operations
- [ ] Every service has health checks and readiness probes

## Architecture

Senior-level architecture decisions determine whether systems succeed or fail at scale. Domain-Driven Design (DDD) aligns code with business domains, reducing cognitive load. Hexagonal architecture isolates business logic from infrastructure, enabling independent testing and technology changes. ADRs document why decisions were made, enabling future engineers to understand trade-offs. Error budgets balance feature velocity with reliability.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Modular monolith (before microservices) | Small-to-medium teams, unclear service boundaries | Simpler deployment vs. limited independent scaling |
| Hexagonal architecture (ports & adapters) | Systems requiring technology flexibility | Testable business logic vs. more boilerplate interfaces |
| Domain-Driven Design (DDD) | Complex business domains with rich rules | Code aligned with business vs. steeper learning curve |

## Security

| Risk | Impact | Mitigation |
|------|--------|------------|
| Architecture decision not accounting for security boundaries | Privilege escalation, data leakage | Define security boundaries in architecture reviews; implement defense in depth |
| Technical debt in security-critical code | Exploitable vulnerabilities accumulating | Track security debt as Critical severity; allocate immediate fix capacity |
| Lack of disaster recovery plan | Extended outage, data loss | Test failover quarterly; maintain documented recovery procedures with RTO/RPO targets |
| Buffer overflow in template code | Memory corruption, code execution | Use bounds-checked containers; enable address sanitizer; avoid raw pointer arithmetic |
| Use-after-move in move semantics | Silent data corruption | Use clang-tidy `bugprone-use-after-move` checks; validate moved-from states |
| Integer overflow in compile-time computation | Incorrect invariants, security bypass | Use `constexpr` assertions to validate ranges; use safe integer libraries |
| Uninitialized memory in custom allocators | Information leakage, undefined behavior | Zero-initialize arena memory; validate alignment; use poison patterns in debug builds |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++17 | Structured bindings, `std::optional`, `std::variant` | Adopt modern types for clearer domain models and API design |
| C++20 | Concepts, ranges, coroutines | Use concepts for domain constraints; use coroutines for async workflows |
| C++23 | `std::expected`, `std::print`, `std::mdspan` | Replace error-code patterns with `std::expected`; use `mdspan` for multidimensional data |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `std::variant` for type-safe domain values | C++17 | Widely supported |
| `std::optional` for nullable returns | C++17 | Widely supported |
| Concepts for domain constraints | C++20 | Supported in GCC 10+, Clang 12+, MSVC 19.22+ |
| `std::expected` (error handling) | C++23 | Supported in GCC 12+, Clang 16+, MSVC 19.33+ |

## Interview Questions

1. **How do you decide between monolith and microservices?**: Start with a monolith. Extract services only when you have clear service boundaries (business domains, not technical layers), the team size demands independent deployment, or scaling requirements differ per component. Microservices add complexity — don't adopt them prematurely.
2. **What is an Architecture Decision Record (ADR)?**: ADR is a short document capturing a significant architectural decision: context, options considered, decision made, and rationale. ADRs are version-controlled alongside code, enabling future engineers to understand why decisions were made.
3. **How do you manage technical debt?**: Track debt items with severity (Low/Medium/High/Critical) and workarounds. Allocate 15-20% of sprint capacity to debt reduction. Prioritize debt that blocks features or causes production incidents. Never ignore security debt.
4. **What is an error budget and how does it work?**: An error budget is the allowed downtime derived from SLA (e.g., 99.99% = 52 min/year). When the budget is spent, features freeze until reliability is restored. It balances feature velocity with reliability.
5. **How do you approach system design for a new product?**: Start simple (monolith, SQL database, basic monitoring). Define SLAs before building features. Use DDD to align code with business domains. Add complexity (caching, message queues, microservices) only when measurement shows a need. Document decisions with ADRs.
6. **Explain SFINAE and when to use it**: Substitution Failure Is Not An Error — when template argument substitution fails, the compiler removes that overload instead of erroring. Use SFINAE (or preferably concepts in C++20) to enable/disable template specializations based on type traits, create conditional overloads, and implement compile-time interface checks.
7. **What is CRTP and why use it?**: Curiously Recurring Template Pattern — a class derives from a template base class parameterized with itself (`class Derived : public Base<Derived>`). Enables static polymorphism (no virtual dispatch overhead), mixin-based design, and compile-time interface enforcement. Common in performance-critical libraries.
8. **How do concepts improve template code?**: Concepts (C++20) name requirements on template parameters. They replace SFINAE with readable `requires` clauses, generate clear error messages, constrain templates to valid types only, and serve as documentation. `template<typename T> requires Sortable<T>` is clearer than `std::enable_if_t<is_sortable_v<T>>`.
9. **When should you use `constexpr` vs `consteval` vs `constinit`?**: `constexpr` — evaluated at compile time when possible, runtime otherwise. `consteval` — mandatory compile-time evaluation (C++20), used for compile-time-only computations. `constinit` — forces constant initialization, prevents static initialization order fiasco.
10. **What are policy-based design and when to use it?**: Compose behavior by passing template parameters (policies) that define specific behaviors. Use when you need flexible, zero-cost abstractions — e.g., `PolicyBasedContainer<StoragePolicy, ThreadPolicy>`. Each policy is a template parameter defining a specific concern (storage, threading, locking).
11. **How do you handle compile-time errors in template code?**: Use `static_assert` with descriptive messages. Implement concept-constrained templates for clear errors. Use `if constexpr` to provide meaningful error paths. For complex metaprogramming, create compile-time diagnostic traits that produce readable error messages.
12. **Explain type erasure and give a use case**: Hiding concrete types behind an interface — `std::function`, `std::any`, `std::shared_ptr<void>`. Use when you need heterogeneous containers or interface-agnostic code. Example: `std::function<void()>` can hold any callable, erasing its concrete type while preserving behavior.
13. **How do you design a custom allocator?**: Implement `allocate()`, `deallocate()`, `construct()`, `destroy()`, and `rebind`. For arena allocators: pre-allocate a large block, bump-allocate, reset all at once. For pool allocators: maintain free lists for fixed-size objects. Use in hot paths where `malloc` overhead is measurable.
14. **What are the trade-offs of move semantics?**: Moves eliminate copies but may leave sources in valid-but-unspecified states. `std::move` is just a cast — the actual move depends on move constructors. Move-only types (like `std::unique_ptr`) prevent copying but require careful ownership transfer. NRVO may elide moves entirely.
15. **How do you apply chaos engineering in C++ systems?**: Deliberately inject failures — kill processes, corrupt data, saturate resources. Test that error budgets are respected. Verify graceful degradation under failure. Use chaos experiments to validate disaster recovery procedures. Start with development/staging environments before production.

## References

- [Building Microservices — Sam Newman](https://www.amazon.com/Building-Microservices-Designing-Fine-Grained-Systems/dp/1492034029)
- [Domain-Driven Design — Eric Evans](https://www.amazon.com/Domain-Driven-Design-Tackling-Complexity-Software/dp/0321125215)
- [Architecture Decision Records — Michael Nygard](https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions)
- [The Phoenix Project — Gene Kim](https://www.amazon.com/Phoenix-Project-DevOps-Helping-Business/dp/0991537522)
