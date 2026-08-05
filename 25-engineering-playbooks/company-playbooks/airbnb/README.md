# Airbnb Engineering Playbook

## Company Context

Airbnb connects millions of hosts and travelers globally, requiring a platform that handles complex search, booking, payment, and trust workflows. The company migrated from a monolithic Ruby on Rails application to a service-oriented architecture, providing lessons in large-scale migration.

## Technology Stack

### Service-Oriented Architecture

Airbnb decomposed its monolith into hundreds of services, each owning specific business capabilities like search, booking, payments, and messaging. Services communicate through APIs and asynchronous messaging.

The SOA migration required careful planning to avoid disrupting a rapidly growing business. Airbnb used the strangler fig pattern to gradually extract services while maintaining system stability.

### Service Mesh

Airbnb adopted a service mesh architecture to manage inter-service communication. The service mesh provides traffic management, observability, and security without requiring changes to individual services.

Features include load balancing, circuit breaking, mutual TLS, and distributed tracing. The service mesh reduces the operational burden on development teams by centralizing cross-cutting concerns.

### Frontend Architecture

Airbnb built a modern frontend architecture using React and GraphQL. The frontend communicates with backend services through a GraphQL API layer that aggregates data from multiple services.

## Architecture Decisions

### Domain-Driven Decomposition

Airbnb decomposed its monolith based on business domains rather than technical layers. Each domain team owns their services, data, and business logic, enabling independent development and deployment.

### API Gateway

Airbnb uses an API gateway to manage external API traffic. The gateway handles authentication, rate limiting, request routing, and response transformation, providing a unified interface for clients.

### Data Platform

Airbnb built a data platform that combines batch and stream processing. The platform enables real-time analytics, machine learning model serving, and business intelligence across the entire organization.

## Lessons Learned

### Migrations Require Business Alignment

Technical migrations must align with business priorities. Airbnb scheduled extraction work around business cycles to minimize disruption and ensure business continuity.

### Invest in Developer Experience

Developer experience tools reduce the friction of working in a distributed system. Airbnb invested in code generation, local development environments, and testing frameworks to improve productivity.

### Cultural Change is Essential

Architecture changes require cultural changes. Airbnb invested in training, documentation, and new processes to help teams adapt to the service-oriented architecture.

## Takeaways

Airbnb demonstrates that large-scale monolith-to-SOA migrations are possible with careful planning, domain-driven decomposition, and investment in developer experience. The service mesh architecture provides a blueprint for managing inter-service communication.
