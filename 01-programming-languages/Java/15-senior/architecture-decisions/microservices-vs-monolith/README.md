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

## References

- Sam Newman, "Building Microservices" — Strangler Fig pattern
- Martin Fowler, "MonolithFirst"
- Internal: Team Scaling Plan 2024–2025
