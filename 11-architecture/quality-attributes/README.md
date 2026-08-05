# Quality Attributes

## Overview

Quality attributes (also called non-functional requirements or "-ilities") are the characteristics that define how well a system performs its function, rather than what it does.

## Common Quality Attributes

### 1. Performance

**Definition**: How fast the system responds to requests.

| Metric | Target | Measurement |
|--------|--------|-------------|
| Response time | < 200ms | P95 latency |
| Throughput | > 10K req/s | Requests per second |
| Time to first byte | < 100ms | TTFB |

**Tactics**:
- Caching (Redis, CDN)
- Connection pooling
- Async processing
- Load balancing
- Database optimization

### 2. Scalability

**Definition**: System's ability to handle increased load.

| Type | Description | Example |
|------|-------------|---------|
| **Horizontal** | Add more instances | Scale out API servers |
| **Vertical** | Increase resources | Upgrade database server |
| **Functional** | Add new capabilities | New microservice |

**Tactics**:
- Stateless services
- Database sharding
- Auto-scaling groups
- Message queues
- Read replicas

### 3. Availability

**Definition**: System's ability to remain operational.

| Level | Downtime/Year | Use Case |
|-------|---------------|----------|
| 99.9% | 8.76 hours | Internal tools |
| 99.99% | 52.6 minutes | Customer-facing |
| 99.999% | 5.26 minutes | Critical systems |

**Tactics**:
- Redundancy (N+1)
- Health checks
- Circuit breakers
- Failover mechanisms
- Graceful degradation

### 4. Security

**Definition**: System's protection against threats.

| Aspect | Description |
|--------|-------------|
| **Confidentiality** | Data is private |
| **Integrity** | Data is accurate |
| **Availability** | System is accessible |
| **Authentication** | Verify identity |
| **Authorization** | Control access |

**Tactics**:
- HTTPS everywhere
- OAuth2/JWT
- Input validation
- Rate limiting
- Security headers

### 5. Testability

**Definition**: How easy it is to test the system.

| Level | Description | Tools |
|-------|-------------|-------|
| **Unit** | Individual components | JUnit, Mockito |
| **Integration** | Component interactions | Testcontainers |
| **E2E** | Complete workflows | Playwright, Selenium |
| **Performance** | Load testing | JMeter, Gatling |

**Tactics**:
- Dependency injection
- Interface-based design
- Test fixtures
- Mocking frameworks
- CI/CD integration

### 6. Maintainability

**Definition**: How easy it is to modify the system.

| Aspect | Description |
|--------|-------------|
| **Readability** | Code is easy to understand |
| **Modularity** | Changes are isolated |
| **Consistency** | Follows patterns |
| **Simplicity** | Avoids unnecessary complexity |

**Tactics**:
- Clean code principles
- SOLID design
- Automated testing
- Documentation
- Code reviews

### 7. Usability

**Definition**: How easy it is for users to accomplish tasks.

| Metric | Description |
|--------|-------------|
| **Learnability** | Time to become proficient |
| **Efficiency** | Speed of task completion |
| **Memorability** | Ease of relearning |
| **Error rate** | Frequency of mistakes |
| **Satisfaction** | User happiness |

**Tactics**:
- Consistent UI
- Error prevention
- Clear feedback
- Accessibility (WCAG)
- User testing

### 8. Portability

**Definition**: System's ability to run in different environments.

| Type | Description |
|------|-------------|
| **Platform** | OS, hardware |
| **Container** | Docker, Kubernetes |
| **Cloud** | AWS, GCP, Azure |
| **Database** | Multiple databases |

**Tactics**:
- Abstraction layers
- Standard interfaces
- Configuration-driven
- Cloud-native design

### 9. Reliability

**Definition**: System's ability to perform its function consistently.

| Metric | Description |
|--------|-------------|
| **MTBF** | Mean Time Between Failures |
| **MTTR** | Mean Time To Recovery |
| **Error rate** | Failed requests percentage |

**Tactics**:
- Error handling
- Retry mechanisms
- Circuit breakers
- Idempotent operations
- Graceful degradation

### 10. Interoperability

**Definition**: System's ability to work with other systems.

| Approach | Description |
|----------|-------------|
| **REST** | HTTP-based APIs |
| **gRPC** | Protocol buffers |
| **GraphQL** | Query language |
| **Events** | Message-based |

**Tactics**:
- Standard protocols
- API versioning
- Schema evolution
- Adapters/converters

## Quality Attribute Scenarios

```
Source → Stimulus → Environment → Artifact → Response → Response Measure

User → High load → Normal operation → API → Response time < 200ms → P95 < 200ms
Attacker → SQL injection → Under attack → DB → Query rejected → No data leak
Developer → Add feature → Normal operation → Service → Deploy in 2 weeks → On schedule
Admin → Scale up → Peak traffic → System → Auto-scale → Handle 10x load
```

## Prioritization

| Quality Attribute | Priority | Weight |
|-------------------|----------|--------|
| Security | Critical | 25% |
| Availability | High | 20% |
| Performance | High | 20% |
| Scalability | Medium | 15% |
| Maintainability | Medium | 10% |
| Testability | Medium | 10% |

## Best Practices

1. **Identify early** - Quality attributes should drive architecture
2. **Quantify** - Define measurable targets
3. **Prioritize** - Not all attributes are equal
4. **Test continuously** - Automated fitness functions
5. **Document** - Record trade-offs and decisions
6. **Revisit regularly** - Requirements change over time

## Key Takeaways

- Quality attributes define how well a system performs
- Common attributes: performance, scalability, availability, security
- Use quality attribute scenarios for concrete requirements
- Prioritize based on business needs
- Test with automated fitness functions
- Document trade-offs with ADRs
