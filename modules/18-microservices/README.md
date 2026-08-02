# Module 18: Microservices

## Overview

This module introduces microservices architecture and patterns using Spring Cloud. Students will learn service discovery, configuration management, API gateways, circuit breakers, and distributed tracing for building resilient, scalable distributed systems.

## Learning Objectives

By the end of this module, you will be able to:

- Design microservices using domain-driven design principles
- Implement service discovery and registration
- Centralize configuration management
- Build API gateways for routing and filtering
- Apply circuit breaker patterns for fault tolerance
- Implement distributed tracing and monitoring
- Use event-driven architecture with messaging

## Prerequisites

- [Module 17: REST API](../17-rest-api/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [Microservices Fundamentals](01-microservices-fundamentals/) | 2 hours | Architecture patterns, monolith vs. microservices |
| 02 | [Service Discovery](02-service-discovery/) | 2 hours | Netflix Eureka, service registration |
| 03 | [API Gateway](03-api-gateway/) | 3 hours | Spring Cloud Gateway, routing, filters |
| 04 | [Config Server](04-config-server/) | 2 hours | Centralized configuration, Spring Cloud Config |
| 05 | [Circuit Breaker](05-circuit-breaker/) | 2 hours | Resilience4j, fallback mechanisms |
| 06 | [Distributed Tracing](06-distributed-tracing/) | 2 hours | Sleuth, Zipkin, correlation IDs |
| 07 | [Event-Driven](07-event-driven/) | 3 hours | Event sourcing, CQRS, messaging patterns |

## Key Concepts

- Single Responsibility Principle for services
- Service mesh and sidecar patterns
- Saga pattern for distributed transactions
- Eventual consistency
- Fault tolerance and resilience patterns

## Enterprise Applications

Microservices enable organizations to develop, deploy, and scale services independently, improving development velocity, technology flexibility, and fault isolation in large-scale enterprise systems.

## Estimated Total Time**

**16 hours**

## Module Project

Build a **Microservices E-Commerce System** that:
- Implements service discovery with Eureka
- Uses API Gateway for request routing
- Centralizes configuration with Spring Config Server
- Applies circuit breakers for fault tolerance
- Demonstrates distributed tracing across services

## Resources

- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Microservices Patterns](https://microservices.io/patterns/)

**Previous Module**: [Module 17: REST API](../17-rest-api/)
**Next Module**: [Module 19: Apache Kafka](../19-apache-kafka/)