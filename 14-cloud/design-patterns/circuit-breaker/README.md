# Circuit Breaker Pattern

## Overview

The Circuit Breaker pattern prevents an application from repeatedly trying to execute an operation that is likely to fail. It monitors failures and when a threshold is reached, the circuit opens and subsequent calls fail fast without attempting the operation. After a timeout period, the circuit enters a half-open state to test if the underlying service has recovered.

## When to Use

- Protecting against cascading failures in distributed systems
- Preventing resource exhaustion from repeated failed calls
- Providing fallback behavior when services are unavailable
- Reducing load on struggling downstream services
- Improving system resilience during partial outages
- Implementing fault tolerance in microservice architectures

## Implementation

### AWS
- AWS App Mesh with circuit breaker configuration
- Lambda with custom circuit breaker logic
- API Gateway integration timeout settings
- ECS service health checks with circuit breaker

### Azure
- Azure Service Bus with retry and circuit breaker
- Polly integration in .NET applications
- Azure Functions with resilience policies
- Application Insights for circuit breaker metrics

### Google Cloud
- Cloud Endpoints with circuit breaking
- gRPC deadline and circuit breaker patterns
- Custom middleware in Go/Python services
- Cloud Run health checks for circuit breaking triggers

### Libraries
- Hystrix (Netflix, legacy) - Java circuit breaker
- Resilience4j - Lightweight Java resilience library
- Polly (.NET) - Transient fault handling
- Opossum (Node.js) - Circuit breaker with fallback
- PyBreaker (Python) - Circuit breaker implementation

## Best Practices

1. Configure appropriate failure thresholds for opening the circuit
2. Implement meaningful fallback behavior when circuit is open
3. Monitor circuit state transitions for operational visibility
4. Use half-open state to gradually restore traffic
5. Set appropriate timeout durations based on service characteristics
6. Implement different circuit breakers for different downstream services
7. Test circuit breaker behavior under simulated failure conditions

## Interview Questions

1. Explain the three states of a circuit breaker and transitions between them.
2. How do you determine the appropriate failure threshold for opening a circuit?
3. What is the difference between circuit breaker and bulkhead patterns?
4. How would you implement a fallback strategy when the circuit is open?
5. Describe how to test circuit breaker behavior in a staging environment.

## References

- Circuit Breaker Pattern - Martin Fowler
- Resilience4j Documentation - https://resilience4j.readme.io
- Polly Documentation - https://github.com/App-vNext/Polly
- Netflix Hystrix Wiki - https://github.com/Netflix/Hystrix
- Cloud Design Patterns - Microsoft Azure Architecture Center
- Release It! - Michael Nygard
