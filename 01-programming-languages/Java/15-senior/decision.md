# Decision: Senior Java Engineering

## When to Apply Senior Concepts

**Apply when:**
- Building large-scale distributed systems
- Optimizing for performance and scale
- Making architecture decisions
- Leading technical teams
- Managing production systems

**Don't apply when:**
- Building simple CRUD applications
- Prototyping or MVP development
- Learning Java basics

## Architecture Decision Records

| Decision | Context | Trade-offs |
|----------|---------|------------|
| Microservices | Large team, complex domain | Complexity vs flexibility |
| Monolith | Small team, simple domain | Simplicity vs scalability |
| Event-driven | High throughput, async | Consistency vs availability |
| REST API | Standard integration | Simplicity vs performance |

## Performance Guidelines

| Metric | Target |
|--------|--------|
| Response time | < 100ms (p99) |
| Throughput | > 1000 req/s |
| Error rate | < 0.1% |
| Availability | 99.9% |

## Further Reading

- [System Design Interview](https://www.educative.io/courses/grokking-the-system-design-interview)
- [Architecture Decision Records](https://adr.github.io/)
