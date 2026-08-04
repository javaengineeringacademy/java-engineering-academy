# Netflix: Streaming at Scale

How Netflix built a globally distributed streaming platform serving 200M+ subscribers.

## Company Overview

Netflix is the world's leading streaming entertainment service with 200M+ paid memberships in 190+ countries. Their engineering organization is a model for microservices architecture and chaos engineering.

## Architecture Evolution

### Phase 1: Monolith (1997-2008)
- Single Oracle-based application
- DVD rental business
- Vertical scaling only

### Phase 2: Early Cloud (2008-2010)
- Migration to AWS begins
- First microservices extracted
- CQRS patterns introduced

### Phase 3: Full Microservices (2010-2015)
- Hundreds of microservices
- Zuul API gateway
- Eureka service discovery
- Hystrix circuit breakers

### Phase 4: Global Distribution (2015-Present)
- Multi-region active-active
- Open Connect CDN
- Chaos engineering at scale

## Core Architecture

```
┌─────────────────────────────────────────────────────────┐
│                      Clients                            │
│   (Smart TV, Mobile, Web, Game Consoles)                │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                    Zuul Gateway                         │
│   (Routing, Authentication, Rate Limiting)              │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│               Microservices Layer                       │
│   (500+ services, Spring Boot)                          │
├─────────────┬─────────────┬─────────────┬───────────────┤
│  User Svc   │  Content Svc│  Playback   │  Billing      │
│  (Cassandra)│  (Postgres) │  (Redis)    │  (Oracle)     │
└─────────────┴─────────────┴─────────────┴───────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                 Netflix OSS Stack                       │
│   Eureka, Hystrix, Ribbon, Zuul, Archaius               │
└─────────────────────────────────────────────────────────┘
```

## Key Technologies

### Zuul API Gateway
- Dynamic routing based on headers
- Load balancing with Ribbon
- Circuit breaking with Hystrix
- Real-time analytics

```java
// Zuul Filter Example
public class AuthFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }
    
    @Override
    public int filterOrder() {
        return 1;
    }
    
    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext()
            .getRequest().getHeader("Authorization") != null;
    }
    
    @Override
    public Object run() {
        // Authentication logic
        String token = RequestContext.getCurrentContext()
            .getRequest().getHeader("Authorization");
        // Validate token, set user context
        return null;
    }
}
```

### Eureka Service Discovery
- Client-side discovery
- Peer-to-peer replication
- Health checking
- Zone affinity

```yaml
# Eureka Client Configuration
eureka:
  client:
    serviceUrl:
      defaultZone: http://eureka-server:8761/eureka/
  instance:
    preferIpAddress: true
    leaseRenewalIntervalInSeconds: 10
```

### Hystrix Circuit Breaker
- Protects against cascading failures
- Fallback mechanisms
- Bulkhead isolation
- Real-time monitoring

```java
@HystrixCommand(
    fallbackMethod = "getDefaultContent",
    threadPoolKey = "contentPool",
    commandProperties = {
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
    }
)
public Content getContent(String contentId) {
    return contentService.fetch(contentId);
}

public Content getDefaultContent(String contentId) {
    return Content.defaultContent();
}
```

## Chaos Engineering

### Chaos Monkey
- Randomly terminates production instances
- Tests resilience to failure
- Runs during business hours

### Simian Army
- **Chaos Monkey**: Terminates instances
- **Latency Monkey**: Injects network delays
- **Conformity Monkey**: Ensures best practices
- **Security Monkey**: Monitors security policies
- **Chaos Gorilla**: Simulates AZ failure
- **Chaos Kong**: Simulates region failure

### Chaos Engineering Principles
1. Build a hypothesis around steady state
2. Introduce realistic world events
3. Observe the difference between steady and disturbed state
4. Abort and minimize blast radius if unintended behavior detected
5. Automate experiments to run continuously

### Failure Injection Testing (FIT)
- HTTP fault injection
- Latency injection
- Error injection
- Network partition simulation

```java
// FIT Configuration
public class ChaosConfiguration {
    @Bean
    public FailureInjector failureInjector() {
        return new FailureInjector()
            .withLatency("payment-service", Duration.ofSeconds(2))
            .withErrors("recommendation-service", 50)
            .withPartition("user-service", "content-service");
    }
}
```

## Data Architecture

### Cassandra
- Multi-datacenter replication
- Tunable consistency
- Used for: user profiles, viewing history, bookmarks

### EVCache
- Distributed caching layer
- Memcached-based
- Handles 50M+ ops/second
- Cross-region replication

### Kafka
- Event streaming platform
- 700B+ events/day
- Real-time data pipelines
- Exactly-once semantics

### Titus
- Container management platform
- Built on Apache Mesos
- 1M+ containers daily
- Batch and service workloads

## Content Delivery

### Open Connect
- Netflix's custom CDN
- 15,000+ servers globally
- ISP-embedded appliances
- Adaptive bitrate streaming

### ISP Partnerships
- Direct peering with ISPs
- Free Open Connect appliances
- Optimized content placement

## Observability

### Atlas
- Telemetry system
- 1.5M metrics/second
- Real-time alerting
- Dimensional time series

### Zipkin
- Distributed tracing
- Latency analysis
- Dependency mapping

### Kayenta
- Automated canary analysis
- ML-based anomaly detection
- Deployment safety

## Organizational Structure

### Full Cycle Developers
- Developers own their services end-to-end
- Build, test, deploy, monitor, operate
- On-call for their own services

### Matrix Teams
- Cross-functional collaboration
- Feature teams + platform teams
- Shared responsibility model

## Key Lessons

1. **Embrace Failure**: Chaos engineering builds resilience
2. **Automate Everything**: From deployment to failure injection
3. **Invest in Observability**: You can't fix what you can't see
4. **Decentralize Decision-Making**: Teams own their destiny
5. **Build Platforms, Not Silos**: Internal platforms enable velocity
6. **Measure Everything**: Data-driven decisions at every level

## Statistics

- **Services**: 500+ microservices
- **Deployments**: 4,000+ per day
- **AWS Regions**: 3 active regions
- **Members**: 200M+ globally
- **Traffic**: 15% of global internet bandwidth
- **Uptime**: 99.99% (52 minutes downtime/year)

## References

- [Netflix Tech Blog](https://netflixtechblog.com/)
- [Netflix Open Source](https://netflix.github.io/)
- [Chaos Engineering](https://principlesofchaos.org/)
- [High Availability at Netflix](https://www.youtube.com/watch?v=7wPb-LTt7mM)
- [Zuul Gateway](https://github.com/Netflix/zuul)
- [Eureka Service Discovery](https://github.com/Netflix/eureka)
