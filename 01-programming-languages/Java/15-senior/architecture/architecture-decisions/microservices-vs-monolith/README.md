# ADR: Microservices vs Monolith

## Status

Accepted — Q1 2024

## Context

Our engineering team is scaling from 20 to 100 developers over the next 18 months. The current monolithic application has grown to 500K+ lines of code with 15+ developers committing daily. Build times exceed 45 minutes, deployments take 2 hours with full regression testing, and merge conflicts are frequent.

The scaling plan requires multiple teams owning independent domains with autonomous deployment cycles. The architecture must support:
- Independent team ownership and deployment
- technology diversity where beneficial
- Horizontal scaling of critical paths
- Fault isolation between domains

## Decision

Adopt a modular monolith architecture first, with a clear path to extract services as needed using the Strangler Fig pattern.

## Alternatives Considered

### Full Microservices from Day One
- Pros: Independent deployment, team autonomy, technology diversity, fault isolation
- Cons: Operational complexity (service mesh, distributed tracing, inter-service auth), network latency between services, data consistency challenges, requires mature DevOps infrastructure, overkill for current scale
- **Rejected**: Our team lacks microservices operational experience. The overhead of distributed systems would slow us down during a critical growth phase.

### Stay as Monolith (No Change)
- Pros: No migration cost, familiar architecture, simple deployment
- Cons: Cannot scale teams effectively, long build/deploy cycles, tight coupling prevents independent work, single point of failure
- **Rejected**: The monolith is already limiting team productivity at 20 developers. At 100, it will be unmanageable.

### Modular Monolith with Extraction Path
- Pros: Team autonomy through modules, simple deployment initially, gradual complexity increase, allows learning before going distributed
- Cons: Requires discipline to maintain module boundaries, eventual extraction effort, may delay needed distribution
- **Selected**: Best balance of immediate productivity gains and long-term architectural flexibility.

## Architecture Design

### Module Structure
```
order-service (monolith)
├── modules/
│   ├── user/          (bounded context: user management)
│   ├── product/       (bounded context: catalog)
│   ├── order/         (bounded context: ordering)
│   ├── payment/       (bounded context: billing)
│   └── notification/  (bounded context: messaging)
├── shared/            (cross-cutting concerns)
│   ├── security/
│   ├── audit/
│   └── config/
└── api/               (REST/GraphQL gateway)
```

### Module Rules
- Modules communicate through well-defined interfaces (no direct database access across modules)
- Each module owns its database schema (separate schemas, shared database initially)
- Modules can be compiled and tested independently
- Shared kernel contains only truly cross-cutting concerns (logging, security)

### Extraction Criteria
A module should be extracted to a service when:
- It has clear domain boundaries and minimal coupling
- It requires independent scaling
- It has a dedicated team of 5+ developers
- It needs different technology choices
- It has distinct reliability requirements

## Strangler Fig Migration Strategy

### Phase 1: Establish Module Boundaries (Months 1–3)
- Define module interfaces and API contracts
- Separate database schemas per module
- Introduce module-level compilation and testing
- Establish code ownership per team

### Phase 2: Extract First Service (Months 4–6)
- Select lowest-risk module for extraction (notification service)
- Implement strangler fig proxy to route traffic
- Run old and new implementations in parallel
- Validate with shadow traffic before cutover

### Phase 3: Expand Extraction (Months 7–12)
- Extract services based on extraction criteria
- Implement service mesh for inter-service communication
- Add distributed tracing and centralized logging
- Establish per-service deployment pipelines

### Phase 4: Operational Maturity (Months 13–18)
- Implement circuit breakers and retry policies
- Add service discovery and load balancing
- Establish SLOs per service
- Complete remaining extractions as needed

## Consequences

### Positive
- Teams can work independently on separate modules immediately
- Deployment frequency increases (per-module deploys within monolith)
- Build times decrease through module-level compilation
- Clear extraction path prevents architecture lock-in
- Lower operational complexity than full microservices initially
- Allows gradual learning of distributed systems patterns

### Negative
- **Team structure**: Requires organizational alignment to module boundaries
- **Deployment**: Initial module-level deployment adds build complexity
- **Discipline**: Module boundaries require enforcement (ArchUnit tests, CI checks)
- **Data consistency**: Cross-module transactions need saga pattern even in monolith
- **Eventual extraction**: Strangler fig proxy adds latency during migration period
- **Cultural shift**: Teams must think in terms of module APIs, not direct calls

## Success Metrics

| Metric | Current | Target (12 months) |
|--------|---------|---------------------|
| Build time | 45 min | < 10 min per module |
| Deploy time | 2 hours | < 15 min per service |
| Merge conflicts/week | 12 | < 3 |
| Time to onboard new dev | 2 weeks | 1 week |
| Deploy frequency | Weekly | Daily per team |

## Interview Questions

1. **What is a modular monolith and how does it differ from a traditional monolith?**
   A modular monolith has clear module boundaries with well-defined interfaces, separate database schemas per module, and module-level compilation/testing. Unlike a traditional monolith where everything is tightly coupled, modules can be developed independently by separate teams. The key difference is enforceable boundaries (via ArchUnit tests) rather than just logical separation.

2. **When should you extract a module from a monolith into a separate service?**
   Extract when: (1) the module has clear domain boundaries and minimal coupling, (2) it requires independent scaling, (3) it has a dedicated team of 5+ developers, (4) it needs different technology choices, or (5) it has distinct reliability requirements. Premature extraction is more costly than late extraction.

3. **What is the Strangler Fig pattern and why is it safer than a big-bang rewrite?**
   The Strangler Fig pattern incrementally replaces legacy components by routing traffic between old and new implementations. Unlike a big-bang rewrite (which carries extreme risk and requires 2-3 years), strangler fig allows parallel running, gradual validation, and easy rollback. It's the recommended approach for production systems.

4. **What are the hidden costs of microservices that teams often underestimate?**
   Distributed tracing (Jaeger/Zipkin), service mesh (Istio/Linkerd), inter-service authentication (mTLS), data consistency across services (saga pattern), deployment pipeline per service, monitoring per service, and operational runbooks per service. These typically add 30-50% operational overhead.

5. **How do you enforce module boundaries in a monolith?**
   Use ArchUnit tests to verify module dependencies, configure Maven/Gradle module system, implement package-level access modifiers, and use code review checklists. Example ArchUnit rule: `noClasses().that().resideInAPackage("..order..").should().dependOnClassesThat().resideInAPackage("..payment..")`.

## Pitfalls

**Extracting services too early:**
```java
// BAD: Extracting a service before understanding domain boundaries
// Creates distributed monolith — all the overhead, none of the benefits
@Service
public class OrderService {
    @Autowired private UserServiceClient userClient; // HTTP call for simple lookup
    @Autowired private InventoryClient inventoryClient; // Another HTTP call
    @Autowired private PaymentClient paymentClient; // Yet another HTTP call
    // Single request makes 3 synchronous HTTP calls
    // Latency: 10ms + 10ms + 10ms = 30ms minimum
}

// GOOD: Keep tightly coupled modules in monolith
// Extract only when clear boundaries emerge
@Service
public class OrderService {
    @Autowired private UserRepository userRepository;
    @Autowired private InventoryRepository inventoryRepository;
    // Direct database calls — fast, ACID transactions
    // Latency: 5ms total
}
```

**No module isolation in monolith:**
```java
// BAD: Modules accessing each other's internals
public class Order {
    // Direct field access from another module
    public void process(User user) {
        user.internalField = "modified"; // Violates encapsulation
    }
}

// GOOD: Module communication through interfaces
public interface UserModule {
    User findById(Long id);
    void updatePreferences(Long userId, Preferences prefs);
}

// Enforce with ArchUnit
@ArchTest
static final ArchRule module_boundaries = slices()
    .matching("..order.(*)..")
    .should().notDependOnEachOther()
    .because("Modules should communicate through interfaces");
```

**Not having a rollback plan:**
```yaml
# BAD: No rollback strategy for service extraction
# If new service fails, entire system is down

# GOOD: Strangler fig with traffic routing
# nginx.conf
upstream order_service {
    server old-order:8080 weight=90;  # 90% to old
    server new-order:8080 weight=10;  # 10% to new (canary)
}

# Canary deployment allows gradual rollout
# If new service fails, route 100% back to old
```

## Performance

**Monolith vs Microservices Performance:**

| Metric | Monolith | Modular Monolith | Microservices |
|--------|----------|-----------------|---------------|
| Inter-module call | 0.1ms (method) | 0.1ms (method) | 5-50ms (HTTP/gRPC) |
| Transaction | ACID (single DB) | ACID (single DB) | Saga (eventual consistency) |
| Deployment | 45-120 min | 10-15 min (module) | 2-5 min (service) |
| Build time | 45+ min | 10 min (module) | 3-5 min (service) |
| Startup time | 15-30s | 10-15s | 2-5s |
| Memory (total) | 4-8 GB | 4-8 GB | 200MB × N services |

**Strangler Fig Migration Latency:**
```
During migration (dual-running):
- Request overhead: +5-10ms (proxy routing)
- Database overhead: None (shared or split)
- Monitoring overhead: +2ms (correlation IDs)

After migration:
- Inter-service latency: 5-50ms (network)
- Same-module latency: 0.1ms (in-process)
```

## Internal Working

**Strangler Fig Implementation:**
1. **Proxy layer**: Reverse proxy (nginx/Envoy) routes traffic between old and new
2. **Traffic splitting**: Percentage-based routing (90/10, 50/50, 0/100)
3. **Data migration**: Dual-write during transition, then switch reads
4. **Validation**: Shadow traffic, canary deployments, A/B testing
5. **Cutover**: Route 100% to new, decommission old

**Module Boundary Enforcement:**
```
Module A (Order)
├── src/main/java/order/
│   ├── api/          (public interfaces)
│   ├── internal/     (private implementation)
│   └── model/        (domain objects)
└── src/main/resources/
    └── order-schema.sql  (separate schema)

Module B (Payment)
├── src/main/java/payment/
│   ├── api/          (public interfaces)
│   ├── internal/     (private implementation)
│   └── model/        (domain objects)
└── src/main/resources/
    └── payment-schema.sql  (separate schema)

// ArchUnit enforcement
@AnalyzeClasses(packages = "com.example")
public class ModuleBoundaryTest {
    @ArchTest
    static final ArchRule order_cannot_depend_on_payment =
        noClasses().that().resideInAPackage("..order..")
            .should().dependOnClassesThat()
            .resideInAPackage("..payment..");
}
```

## Why This Concept Exists

The monolith vs microservices decision exists because:

1. **Team scaling**: A 500K LOC monolith with 100 developers creates merge conflicts, long build times, and deployment bottlenecks. Microservices allow independent team velocity.

2. **Deployment coupling**: In a monolith, a single bug can take down the entire system. Microservices provide fault isolation.

3. **Technology diversity**: Different services may need different databases, languages, or frameworks. Microservices enable this; monoliths don't.

4. **Scaling**: Monoliths scale as a unit. Microservices scale granularly — scale the hot payment service without scaling the cold analytics service.

5. **But**: Microservices add operational complexity. The modular monolith approach lets teams learn distributed systems patterns before committing to full microservices.

## Overview

The microservices vs monolith decision is one of the most significant architectural choices for growing engineering teams. This ADR recommends a modular monolith architecture first, with a clear path to extract services using the Strangler Fig pattern. This approach balances immediate productivity gains (independent team deployment, reduced build times) with long-term architectural flexibility (gradual extraction as team experience grows).

## References

- Sam Newman, "Building Microservices" (2nd Edition) — Strangler Fig pattern
- Martin Fowler, "MonolithFirst": https://martinfowler.com/bliki/MonolithFirst.html
- Neil Ford, "Building Evolutionary Architectures" — Modular monolith patterns
- ArchUnit documentation: https://www.archunit.org/
- Internal: Team Scaling Plan 2024–2025
- "Domain-Driven Design" by Eric Evans — Bounded contexts for module boundaries
