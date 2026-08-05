# Strangler Fig Pattern

## Overview

The Strangler Fig pattern enables incremental migration of a legacy system by gradually replacing specific pieces of functionality with new applications and services. As the new system grows, the old system is gradually strangled until it can be completely retired. This approach reduces risk compared to big-bang rewrites.

## When to Use

- Migrating monolithic applications to microservices
- Replacing legacy systems with minimal downtime
- Reducing risk of large-scale system rewrites
- Incrementally modernizing technology stacks
- Migrating between cloud providers
- Replacing specific components while maintaining system operation

## Implementation

### AWS
- ALB with weighted routing for traffic splitting
- API Gateway for facade routing to old and new systems
- Lambda for new microservice implementations
- Route 53 for DNS-based traffic shifting

### Azure
- Azure Front Door with traffic splitting
- Azure API Management for routing facade
- Azure Functions for new service implementations
- Azure Traffic Manager for gradual migration

### Google Cloud
- Cloud Load Balancing with traffic splitting
- Cloud Endpoints for routing facade
- Cloud Run for new service deployment
- Cloud DNS for gradual traffic migration

### Kubernetes
- Istio traffic management for canary migrations
- NGINX Ingress with weighted routing
- Custom controllers for migration orchestration
- Service mesh for gradual traffic shifting

## Best Practices

1. Identify good seam points for extracting functionality
2. Use the strangler fig facade for routing decisions
3. Implement feature flags for gradual rollout
4. Maintain comprehensive testing during migration
5. Migrate one bounded context at a time
6. Monitor both old and new systems during transition
7. Document migration progress and remaining components

## Interview Questions

1. How do you identify good seam points for strangler fig migration?
2. What strategies handle data consistency during migration?
3. How do you manage routing between old and new systems?
4. Describe testing strategies for incremental migration.
5. When is strangler fig not appropriate for system modernization?

## References

- Strangler Fig Application - Martin Fowler
- Migrating a Monolith to Microservices - Microsoft
- Building Microservices - Sam Newman
- Modernizing Legacy Systems - Richard Law
- Cloud Migration Patterns - Microsoft Azure
- Monolith to Microservices - Sam Newman
