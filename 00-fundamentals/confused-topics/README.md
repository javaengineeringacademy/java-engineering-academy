# Frequently Confused Topics

## Purpose

This module addresses pairs or groups of concepts that developers frequently confuse or use interchangeably when they should not. Understanding these distinctions is crucial for accurate technical communication and proper architectural decisions.

## Why These Topics Get Confused

1. **Similar Names**: Many concepts share linguistic roots (e.g., authentication vs authorization)
2. **Overlapping Functionality**: Tools often solve related problems (e.g., Docker vs VMs)
3. **Evolutionary Relationships**: New technologies evolve from old (e.g., Spring vs Spring Boot)
4. **Marketing Hype**: Vendors blur distinctions to promote products
5. **Incomplete Mental Models**: Developers learn one tool deeply without understanding alternatives

## How to Use This Module

### For Learning
- Read each comparison end-to-end before moving to the next
- Pay special attention to the "Interview Trap" sections
- Study the visual diagrams to build mental models
- Test yourself: can you explain the difference to a junior developer?

### For Interview Preparation
- Focus on the "Key Difference" tables
- Practice articulating trade-offs, not just definitions
- Be prepared for follow-up questions about when to use which
- Avoid absolutes: "X is always better than Y" is rarely true

### For Architecture Decisions
- Use the "When to Use Which" guidelines
- Consider context: team size, scale, existing tech stack
- Remember: most choices are about trade-offs, not right vs wrong
- Document your reasoning for future team members

## Structure of Each Topic

Each file follows a consistent format:
- **What They Are**: Clear definitions of each concept
- **Key Difference Table**: Side-by-side comparison
- **When to Use Which**: Practical decision criteria
- **Interview Trap**: Common misconceptions to avoid
- **Visual Diagram**: ASCII art representation of the concepts

## Contributing

When adding new confused topics:
1. Ensure the concepts are genuinely confused (not just related)
2. Maintain the 40-60 line guideline per file
3. Include real-world examples from production systems
4. Test your explanations with junior developers
5. Update this README with the new topic

## Topics Covered

| Topic | Core Confusion |
|-------|----------------|
| JDK vs JRE vs JVM | Development kit vs runtime vs virtual machine |
| Docker vs VMs | Containers vs virtualization |
| Kubernetes vs Docker | Orchestration vs containerization |
| Thread vs Process | Concurrency units |
| REST vs SOAP | Architectural style vs protocol |
| TCP vs UDP | Reliable vs fast transport |
| Kafka vs RabbitMQ | Log vs queue messaging |
| Redis vs Memcached | Data structures vs simple cache |
| OAuth vs JWT | Framework vs token format |
| Authentication vs Authorization | Identity vs permissions |
| Monolith vs SOA vs Microservices | Architectural evolution |
| SQL vs NoSQL | Relational vs document/graph databases |
| REST vs gRPC | JSON APIs vs binary RPC |
| Sync vs Async | Blocking vs non-blocking execution |
| Blocking vs Non-blocking | I/O model differences |
| Concurrency vs Parallelism | Dealing with vs doing multiple things |
| IoC vs DI | Principle vs implementation |
| CI vs CD | Integration vs delivery/deployment |
| Linting vs Formatting | Code quality vs style |
| TypeScript vs JavaScript | Typed superset |
| Angular vs React vs Vue | Framework vs library vs framework |
| Spring vs Spring Boot | Framework vs auto-configuration |
| Git Merge vs Rebase | History preservation vs linear history |
| Horizontal vs Vertical Scaling | Scale out vs scale up |
| Load Balancing vs Clustering | Traffic distribution vs node grouping |
| Singleton vs Static | Instance control vs class-level access |
| Interface vs Abstract Class | Contract vs partial implementation |

## Related Modules

- [Common Misconceptions](../03-common-misconceptions/) - Things that are simply wrong
- [Interview Pitfalls](../08-interview-pitfalls/) - Interview-specific mistakes
- [Architecture Decisions](../07-architecture-decisions/) - Broader architectural thinking

---

*Last Updated: August 2026*
