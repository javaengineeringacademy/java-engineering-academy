# 11 - Architecture

## Overview

Software architecture defines the high-level structure of a system, the principles governing its design, and the technologies it uses. Good architecture enables systems that are maintainable, scalable, and resilient.

## Core Concepts

### Design Principles
Fundamental guidelines that shape software design decisions.

- **SOLID** - Five object-oriented design principles
- **DRY** - Don't Repeat Yourself
- **KISS** - Keep It Simple, Stupid
- **YAGNI** - You Aren't Gonna Need It
- **GRASP** - General Responsibility Assignment Software Patterns
- **Law of Demeter** - Principle of least knowledge
- **Composition over Inheritance** - Prefer object composition

### Design Patterns
Reusable solutions to common architectural problems.

- **Event Sourcing** - Store state changes as events
- **CQRS** - Command Query Responsibility Segregation
- **Saga** - Distributed transaction management
- **Strangler Fig** - Incremental migration pattern
- **Anti-Corruption Layer** - Isolate legacy systems
- **Outbox** - Reliable event publishing
- **Circuit Breaker** - Prevent cascade failures
- **Bulkhead** - Failure isolation
- **Service Mesh** - Infrastructure-level networking
- **Gateway** - API aggregation and routing

### Domain-Driven Design (DDD)
Approach to complex domains through modeling and collaboration.

- **Strategic Design** - Bounded contexts, ubiquitous language
- **Tactical Patterns** - Entities, value objects, aggregates
- **Event Storming** - Collaborative domain discovery
- **Context Mapping** - Integration between bounded contexts

### Deployment Patterns
Strategies for delivering software to production.

- **Blue-Green** - Zero-downtime deployments
- **Canary** - Gradual rollout to subset of users
- **Rolling** - Incremental instance updates
- **Feature Flags** - Runtime feature toggling

## Directory Structure

```
11-architecture/
├── design-principles/
│   ├── solid/
│   ├── dry/
│   ├── kiss/
│   ├── yagni/
│   ├── srp/
│   ├── ocp/
│   ├── lsp/
│   ├── isp/
│   ├── dip/
│   ├── grasp/
│   ├── composition-over-inheritance/
│   ├── law-of-demeter/
│   ├── encapsulation/
│   ├── cohesion/
│   ├── coupling/
│   └── coupling-and-cohesion/
├── patterns/
│   ├── event-sourcing/
│   ├── cqrs/
│   ├── saga/
│   ├── strangler-fig/
│   ├── anti-corruption-layer/
│   ├── outbox/
│   ├── circuit-breaker/
│   ├── retry/
│   ├── sidecar/
│   ├── bulkhead/
│   ├── gateway/
│   ├── service-mesh/
│   └── deployment-strategies/
│       ├── blue-green/
│       ├── canary/
│       ├── rolling/
│       └── feature-flags/
└── ddd/
    ├── strategic/
    ├── tactical/
    ├── event-storming/
    └── context-mapping/
```

## Architecture Styles

| Style | Description | Use Case |
|-------|-------------|----------|
| Monolith | Single deployable unit | Small teams, simple domains |
| Modular Monolith | Modules within single deployment | Medium complexity |
| Microservices | Independently deployable services | Large scale, complex domains |
| Event-Driven | Asynchronous event communication | High throughput, loose coupling |
| Serverless | Function-as-a-Service | Variable load, event processing |
| Hexagonal | Ports and adapters | Testable, domain-focused |

## Architecture Decision Records (ADR)

Use ADRs to document significant decisions:

```markdown
# ADR-001: Use Event Sourcing for Order Service

## Status: Accepted

## Context
The order service needs to maintain complete history of all state changes for audit and replay capabilities.

## Decision
Implement event sourcing using Apache Kafka as the event store.

## Consequences
- Complete audit trail of all order changes
- Ability to replay events and rebuild state
- Increased complexity in read queries
- Event schema evolution requires careful planning
```

## Key Metrics

| Metric | Description | Target |
|--------|-------------|--------|
| Coupling | Dependencies between modules | Low |
| Cohesion | Related functionality grouping | High |
| Cyclomatic Complexity | Code path complexity | < 10 |
| Technical Debt | Maintenance burden | Decreasing |
| Build Time | Time to build and test | < 10 minutes |
| Deployment Frequency | How often code is deployed | Daily+ |

## Architectural Fitness Functions

Automated tests that validate architectural characteristics:

```java
@ArchTest
static final ArchRule no_cycles = slices()
    .matching("com.example.(*)..")
    .should().beFreeOfCycles();

@ArchTest
static final ArchRule services_should_not_depend_on_controllers =
    classes()
        .that().resideInAPackage("..service..")
        .should().notDependOnAnyClasses()
        .that().resideInAPackage("..controller..");
```

## References

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/)
- [Domain-Driven Design - Eric Evans](https://www.domainlanguage.com/)
- [Building Microservices - Sam Newman](https://samnewman.io/)
- [Architecture Decision Records](https://adr.github.io/)
