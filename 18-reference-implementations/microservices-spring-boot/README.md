# Microservices with Spring Boot

## Overview
Reference implementation for microservices architecture using Spring Boot.

## Architecture
Client -> API Gateway -> Service Discovery -> Microservices

## Key Components

### Service Discovery
Spring Cloud Netflix Eureka for service registration and discovery.

### API Gateway
Spring Cloud Gateway for routing, rate limiting, and authentication.

### Circuit Breaker
Resilience4j for fault tolerance with fallback methods.

### Communication
- Synchronous: WebClient/RestTemplate
- Asynchronous: Kafka/RabbitMQ events

## Best Practices
1. Use service discovery for dynamic routing
2. Implement circuit breakers for resilience
3. Use async communication via events
4. Centralize configuration
5. Implement distributed tracing
