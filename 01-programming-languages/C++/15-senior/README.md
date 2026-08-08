# Senior Level C++ — C++

## Why It Matters

Senior-level C++ is not about knowing more syntax — it's about making better decisions. When you consider architecture choices, performance trade-offs, team processes, and technical strategy, you determine whether a project succeeds or fails. The best code is the code you don't write — every line is a liability that must be maintained, tested, and eventually removed.

## What It Is

Senior-level C++ involves system design, architecture patterns like DDD and hexagonal architecture, technical debt management, and leadership in code quality and team processes that bridge the gap between writing code and building systems that scale.

## Engineering Decision Framework

| Decision | Approach | When to Use | When NOT to Use |
|----------|----------|-------------|-----------------|
| Architecture | Monolith vs microservices vs modular monolith | Monolith for small teams, microservices at scale | Microservices for a 3-person team |
| Data storage | SQL vs NoSQL vs time-series vs graph | SQL for structured data, NoSQL for flexibility | NoSQL for highly relational data |
| Caching | In-memory vs Redis vs CDN | In-memory for single-server, Redis for distributed | Caching when you have no performance problem |
| Message queue | Kafka vs RabbitMQ vs ZeroMQ | Kafka for high throughput, RabbitMQ for complex routing | Kafka for simple request-response |
| Error strategy | Fail fast vs graceful degradation | Fail fast in development, graceful in production | Fail fast when users depend on the service |
| Technical debt | Fix now vs fix later vs document | Fix if it blocks features, document if it doesn't | Ignoring debt that compounds |

## Expanded Code Examples

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

## Cross-Linked Related Topics

- **Design Patterns** → [Module 09: Design Patterns](../09-design-patterns/) — Patterns inform architecture decisions
- **Performance** → [Module 11: Performance](../11-performance/) — Optimization at the system level
- **Build Systems** → [Module 13: Build Systems](../13-build-systems/) — Monorepo management, CI/CD at scale
- **Best Practices** → [Module 14: Best Practices](../14-best-practices/) — SOLID principles, coding standards
- **Concurrency** → [Module 07: Concurrency](../07-concurrency/) — Distributed systems, lock-free patterns
- **Testing** → [Module 10: Testing](../10-testing/) — Integration testing, chaos engineering

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

## Architecture Considerations

Senior-level architecture decisions determine whether systems succeed or fail at scale. Domain-Driven Design (DDD) aligns code with business domains, reducing cognitive load. Hexagonal architecture isolates business logic from infrastructure, enabling independent testing and technology changes. ADRs document why decisions were made, enabling future engineers to understand trade-offs. Error budgets balance feature velocity with reliability.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Modular monolith (before microservices) | Small-to-medium teams, unclear service boundaries | Simpler deployment vs. limited independent scaling |
| Hexagonal architecture (ports & adapters) | Systems requiring technology flexibility | Testable business logic vs. more boilerplate interfaces |
| Domain-Driven Design (DDD) | Complex business domains with rich rules | Code aligned with business vs. steeper learning curve |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Architecture decision not accounting for security boundaries | Privilege escalation, data leakage | Define security boundaries in architecture reviews; implement defense in depth |
| Technical debt in security-critical code | Exploitable vulnerabilities accumulating | Track security debt as Critical severity; allocate immediate fix capacity |
| Lack of disaster recovery plan | Extended outage, data loss | Test failover quarterly; maintain documented recovery procedures with RTO/RPO targets |

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

## References

- [Building Microservices — Sam Newman](https://www.amazon.com/Building-Microservices-Designing-Fine-Grained-Systems/dp/1492034029)
- [Domain-Driven Design — Eric Evans](https://www.amazon.com/Domain-Driven-Design-Tackling-Complexity-Software/dp/0321125215)
- [Architecture Decision Records — Michael Nygard](https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions)
- [The Phoenix Project — Gene Kim](https://www.amazon.com/Phoenix-Project-DevOps-Helping-Business/dp/0991537522)
