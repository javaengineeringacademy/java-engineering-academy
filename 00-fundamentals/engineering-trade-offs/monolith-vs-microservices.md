# Monolith vs Microservices

## Problem Statement

Should you deploy your application as a single unit or as independent services? Monoliths are simpler to start. Microservices offer independent deployment and scaling. The choice is not always obvious.

## The Core Tension

Monolith: One codebase, one deployment, one database. Simple operations, tight coupling, shared fate.

Microservices: Multiple codebases, independent deployments, separate databases. Complex operations, loose coupling, independent fate.

## When Monolith Wins

**Small team**: A team of 5 engineers does not need the overhead of 20 services. Communication is easy when everyone is in the same room.

**Early product**: When you are still finding product-market fit, the ability to refactor across the entire codebase quickly is valuable.

**Simple domain**: If your application is a straightforward CRUD system, microservices add complexity without benefit.

**Operational maturity**: Microservices require sophisticated CI/CD, monitoring, and distributed systems expertise. If you do not have that, start with a monolith.

**Latency requirements**: Inter-service communication adds latency. If you need sub-millisecond responses, a monolith avoids network hops.

### Characteristics of a Good Monolith

- Clear module boundaries within the codebase
- Database is well-structured with proper foreign keys
- Internal APIs between modules are clean
- Easy to extract services later if needed
- Fast build and deployment pipeline

## When Microservices Win

**Large team**: When 50+ engineers work on the same codebase, merge conflicts and deployment coordination become bottlenecks.

**Different scaling needs**: When one component needs 10x the resources of another, independent scaling saves money.

**Independent deployment**: When the payments team needs to deploy without coordinating with the search team.

**Technology diversity**: When different components genuinely benefit from different languages or frameworks.

**Fault isolation**: When a failure in recommendations should not take down checkout.

**Organizational boundaries**: When different teams own different capabilities, microservices mirror the organization.

## The Modularity Monolith

A middle ground that captures most of the benefits of both:

```
monolith/
  modules/
    payments/
      api/
      domain/
      infrastructure/
    search/
      api/
      domain/
      infrastructure/
    notifications/
      api/
      domain/
      infrastructure/
  shared/
    database/
    events/
```

Each module has clean interfaces. Modules communicate through well-defined internal APIs. You can deploy as a monolith but extract services later when you have a clear reason.

This is the recommended starting point for most teams. Extract services only when you have a concrete scaling or organizational need.

## Real-World Examples

### Shopify: Monolith Done Right

Shopify runs one of the largest Ruby on Rails monoliths. They handle Black Friday traffic through careful optimization, not microservices. Their lesson: a well-optimized monolith can handle enormous scale.

### Netflix: Microservices for Good Reason

Netflix has hundreds of microservices because different components (streaming, recommendations, billing, authentication) have fundamentally different scaling, reliability, and team ownership requirements.

### Basecamp/HEY: Intentional Monolith

Basecamp chose to keep a monolith because their team is small and their domain is well-understood. They optimize for simplicity and developer experience.

## Migration Patterns

**Strangler Fig**: Gradually replace monolith functionality with services. Route traffic through a facade that directs to monolith or new service.

**Database per service**: The hardest part. Requires data synchronization, eventual consistency, and distributed transactions.

**Event-driven extraction**: Publish domain events from the monolith. New services consume these events. The monolith remains the system of record until the new service is proven.

## Decision Matrix

| Factor | Choose Monolith | Choose Microservices |
|--------|----------------|---------------------|
| Team size | Small (< 20) | Large (> 50) |
| Domain clarity | Unclear | Well-understood |
| Deployment frequency | Low | High per service |
| Scaling needs | Uniform | Varies by component |
| Operational maturity | Low | High |
| Product stage | Early | Mature |

## Interview Relevance

**Common questions**:
- "How would you structure a new application?"
- "We are experiencing deployment bottlenecks with our monolith. What would you do?"
- "How do you decide when to extract a service?"

**What interviewers want**:
- You understand the real costs of microservices (distributed transactions, network failures, operational complexity)
- You can identify when a monolith is appropriate
- You know about the modularity monolith pattern
- You can discuss migration strategies

**Red flags**:
- Defaulting to microservices without justification
- Not mentioning the operational complexity
- Ignoring the modularity monolith option
- Not discussing database challenges in microservices

## Key Takeaway

Start with a well-structured monolith. Extract services when you have a concrete reason: scaling requirements, team size, or deployment independence. Do not adopt microservices because they are trendy. Adopt them because the cost of maintaining a monolith exceeds the cost of distributed systems complexity.
