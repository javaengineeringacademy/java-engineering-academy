# 04 - Backend Engineering

## Comprehensive Guide to Modern Backend Development

This module covers the complete spectrum of backend engineering topics, from foundational frameworks to advanced architectural patterns. Each section provides in-depth knowledge with practical examples and best practices.

---

## Table of Contents

### [Spring Ecosystem](#spring-ecosystem)
- [Spring Boot](spring/spring-boot/README.md) - Auto-configuration, starters, properties, profiles, actuator, DevTools
- [Spring Cloud](spring/spring-cloud/README.md) - Service discovery, config server, circuit breaker, gateway, load balancing
- [Spring Security](spring/spring-security/README.md) - Authentication, authorization, OAuth2, JWT, method security
- [Spring Batch](spring/spring-batch/README.md) - Job, step, reader, processor, writer, chunk oriented
- [Spring Integration](spring/spring-integration/README.md) - Channels, adapters, filters, transformers, routers

### [Reactive Programming](#reactive-programming)
- [WebFlux](reactive/webflux/README.md) - WebFlux, RouterFunction, HandlerFunction, WebClient, SSE
- [Project Reactor](reactive/project-reactor/README.md) - Mono, Flux, operators, scheduling, backpressure
- [RxJava](reactive/rxcjava/README.md) - Observable, Single, Maybe, Completable, operators

### [REST Architecture](#rest-architecture)
- [REST Fundamentals](rest/fundamentals/README.md) - REST constraints, HTTP methods, status codes, resource modeling
- [REST Design](rest/design/README.md) - API design, naming, versioning, pagination, filtering, HATEOAS
- [REST Versioning](rest/versioning/README.md) - URI, header, query param, content negotiation versioning
- [Content Negotiation](rest/content-negotiation/README.md) - JSON, XML, YAML, content types, Accept header

### [GraphQL](#graphql)
- [GraphQL Fundamentals](graphql/fundamentals/README.md) - Schema, types, queries, mutations, subscriptions
- [GraphQL Schemas](graphql/schemas/README.md) - Schema design, types, interfaces, unions, enums
- [GraphQL Resolvers](graphql/resolvers/README.md) - Resolver patterns, N+1 problem, DataLoaders
- [GraphQL Subscriptions](graphql/subscriptions/README.md) - Real-time GraphQL, WebSocket, server-sent events

### [gRPC](#grpc)
- [gRPC Fundamentals](grpc/fundamentals/README.md) - gRPC basics, protobuf, service definition, streaming
- [Protocol Buffers](grpc/protobuf/README.md) - Proto3 syntax, messages, services, imports, oneof
- [gRPC Streams](grpc/streams/README.md) - Unary, server streaming, client streaming, bidirectional
- [gRPC Interceptors](grpc/interceptors/README.md) - Client/server interceptors, metadata, error handling

### [OpenAPI & Documentation](#openapi--documentation)
- [OpenAPI Specification](openapi/specification/README.md) - OpenAPI 3.0, paths, components, security, examples
- [Code Generation](openapi/codegen/README.md) - Code generation, client/server stubs, OpenAPI Generator
- [API Documentation](openapi/documentation/README.md) - Swagger UI, ReDoc, API documentation best practices

### [API Design](#api-design)
- [RESTful Principles](api-design/restful/README.md) - RESTful principles, resource naming, HTTP semantics
- [Versioning Strategies](api-design/versioning/README.md) - Versioning strategies, breaking changes, deprecation
- [Pagination](api-design/pagination/README.md) - Offset, cursor, keyset pagination
- [Filtering](../README.md) - Query parameters, filtering patterns, sorting
- [Error Handling](api-design/error-handling/README.md) - Error responses, Problem Details, error codes

### [Authentication](#authentication)
- [OAuth2](authentication/oauth2/README.md) - OAuth2 flows, authorization code, client credentials, PKCE
- [OpenID Connect](authentication/oidc/README.md) - OpenID Connect, ID tokens, userinfo, session management
- [JWT](authentication/jwt/README.md) - JWT structure, signing, validation, refresh tokens
- [SAML](../README.md) - SAML assertions, SSO, identity providers
- [LDAP](authentication/ldap/README.md) - LDAP directory, bind, search, authentication

### [Authorization](#authorization)
- [RBAC](authorization/rbac/README.md) - Role-based access control, roles, permissions
- [ABAC](authorization/abac/README.md) - Attribute-based access control, policies, evaluation
- [Policy Engines](authorization/policies/README.md) - Policy engines, OPA, Spring Security policies

### [Validation](#validation)
- [Bean Validation](validation/bean-validation/README.md) - Jakarta Validation, annotations, custom validators
- [Custom Validators](validation/custom-validators/README.md) - Custom annotations, validator implementation

### [Dependency Injection](#dependency-injection)
- [Constructor Injection](dependency-injection/constructor/README.md) - Constructor injection, benefits, testing
- [Setter Injection](../README.md) - Setter injection, optional dependencies
- [Field Injection](../README.md) - Field injection, @Autowired, downsides
- [Profiles](dependency-injection/profiles/README.md) - Spring profiles, @Profile, environment-specific beans

### [Hibernate](#hibernate)
- [Hibernate Fundamentals](hibernate/fundamentals/README.md) - Session, SessionFactory, CRUD, entity lifecycle
- [Hibernate Mapping](hibernate/mapping/README.md) - OneToOne, OneToMany, ManyToMany, inheritance mapping
- [Hibernate Caching](hibernate/caching/README.md) - First-level, second-level, query cache, eviction
- [Hibernate Querying](hibernate/querying/README.md) - HQL, Criteria API, Native SQL, projections
- [Hibernate Performance](hibernate/performance/README.md) - N+1 problem, fetch strategies, batch loading

### [JPA](#jpa)
- [JPA Fundamentals](jpa/fundamentals/README.md) - EntityManager, persistence context, transactions
- [JPA Entities](jpa/entities/README.md) - @Entity, @Id, @GeneratedValue, @Column, @Table
- [JPA Relationships](jpa/relationships/README.md) - Relationship mappings, cascade, fetch types
- [JPA Queries](jpa/queries/README.md) - JPQL, Named Queries, Criteria API
- [JPA Criteria API](jpa/criteria/README.md) - Criteria API, type-safe queries, dynamic queries

### [ORM Patterns](#orm-patterns)
- [ORM Patterns](orm/patterns/README.md) - DAO, Repository, Unit of Work, Identity Map
- [ORM Anti-Patterns](../README.md) - N+1, god entities, lazy loading pitfalls
- [ORM Transactions](orm/transactions/README.md) - ACID, propagation, isolation, rollback

---

## Prerequisites

- Java 17+ / JDK 21
- Spring Boot 3.x
- Maven 3.8+ / Gradle 8+
- Basic understanding of web development
- Familiarity with RESTful concepts

## Learning Path

### Beginner Level
1. Start with [REST Fundamentals](rest/fundamentals/README.md)
2. Learn [Spring Boot](spring/spring-boot/README.md) basics
3. Understand [Dependency Injection](dependency-injection/constructor/README.md)
4. Master [Bean Validation](validation/bean-validation/README.md)

### Intermediate Level
5. Deep dive into [JPA](jpa/fundamentals/README.md) and [Hibernate](hibernate/fundamentals/README.md)
6. Implement [Spring Security](spring/spring-security/README.md)
7. Design APIs with [OpenAPI](openapi/specification/README.md)
8. Learn [API Design](api-design/restful/README.md) best practices

### Advanced Level
9. Master [Reactive Programming](reactive/webflux/README.md)
10. Implement [GraphQL](graphql/fundamentals/README.md) APIs
11. Build [gRPC](grpc/fundamentals/README.md) services
12. Configure [Spring Cloud](spring/spring-cloud/README.md) microservices

### Expert Level
13. Implement [OAuth2/OIDC](authentication/oauth2/README.md) authentication
14. Design [RBAC/ABAC](authorization/rbac/README.md) authorization
15. Optimize [Hibernate Performance](hibernate/performance/README.md)
16. Master [Spring Batch](spring/spring-batch/README.md) and [Integration](spring/spring-integration/README.md)

---

## Project Structure

```
04-backend-engineering/
├── README.md                          # This file
├── spring/
│   ├── spring-boot/README.md
│   ├── spring-cloud/README.md
│   ├── spring-security/README.md
│   ├── spring-batch/README.md
│   └── spring-integration/README.md
├── reactive/
│   ├── webflux/README.md
│   ├── project-reactor/README.md
│   └── rxcjava/README.md
├── rest/
│   ├── fundamentals/README.md
│   ├── design/README.md
│   ├── versioning/README.md
│   └── content-negotiation/README.md
├── graphql/
│   ├── fundamentals/README.md
│   ├── schemas/README.md
│   ├── resolvers/README.md
│   └── subscriptions/README.md
├── grpc/
│   ├── fundamentals/README.md
│   ├── protobuf/README.md
│   ├── streams/README.md
│   └── interceptors/README.md
├── openapi/
│   ├── specification/README.md
│   ├── codegen/README.md
│   └── documentation/README.md
├── api-design/
│   ├── restful/README.md
│   ├── versioning/README.md
│   ├── pagination/README.md
│   ├── filtering/README.md
│   └── error-handling/README.md
├── authentication/
│   ├── oauth2/README.md
│   ├── oidc/README.md
│   ├── jwt/README.md
│   ├── saml/README.md
│   └── ldap/README.md
├── authorization/
│   ├── rbac/README.md
│   ├── abac/README.md
│   └── policies/README.md
├── validation/
│   ├── bean-validation/README.md
│   └── custom-validators/README.md
├── dependency-injection/
│   ├── constructor/README.md
│   ├── setter/README.md
│   ├── field/README.md
│   └── profiles/README.md
├── hibernate/
│   ├── fundamentals/README.md
│   ├── mapping/README.md
│   ├── caching/README.md
│   ├── querying/README.md
│   └── performance/README.md
├── jpa/
│   ├── fundamentals/README.md
│   ├── entities/README.md
│   ├── relationships/README.md
│   ├── queries/README.md
│   └── criteria/README.md
└── orm/
    ├── patterns/README.md
    ├── anti-patterns/README.md
    └── transactions/README.md
```

---

## Key Technologies

| Technology | Purpose | Learning Time |
|------------|---------|---------------|
| Spring Boot | Application framework | 2-3 weeks |
| Spring Security | Authentication/Authorization | 2-3 weeks |
| JPA/Hibernate | ORM | 2-3 weeks |
| WebFlux | Reactive web | 1-2 weeks |
| GraphQL | Query language | 1-2 weeks |
| gRPC | RPC framework | 1-2 weeks |
| OpenAPI | API specification | 1 week |
| OAuth2/OIDC | Authentication | 1-2 weeks |

---

## Best Practices Summary

### Code Quality
- Follow SOLID principles
- Use constructor injection over field injection
- Write comprehensive unit tests
- Implement proper error handling
- Use Lombok judiciously

### API Design
- Follow RESTful conventions
- Use proper HTTP status codes
- Implement consistent error responses
- Use pagination for list endpoints
- Version your APIs

### Security
- Never store secrets in code
- Use OAuth2/JWT for authentication
- Implement proper authorization
- Validate all inputs
- Use HTTPS everywhere

### Performance
- Implement caching strategies
- Use connection pooling
- Optimize database queries
- Implement pagination
- Monitor application metrics

### Architecture
- Use layered architecture
- Implement proper separation of concerns
- Use dependency injection
- Follow DRY principle
- Keep services loosely coupled

---

## Additional Resources

- [Spring Official Documentation](https://spring.io/projects)
- [Baeldung](https://www.baeldung.com/)
- [Thoughts on Java](https://www.thoughts-on-java.org/)
- [Vlad Mihalcea's Blog](https://vladmihalcea.com/)
- [Microsoft REST API Guidelines](https://github.com/microsoft/api-guidelines)
- [Google API Design Guide](https://cloud.google.com/apis/design)

---

## Contributing

When adding new topics to this module:
1. Follow the existing directory structure
2. Include comprehensive examples
3. Add both theoretical and practical content
4. Include common pitfalls and solutions
5. Reference official documentation

---

## Summary

This module provides a complete backend engineering curriculum covering modern Java development. Each topic includes:

- **Concepts**: Core theory and principles
- **Examples**: Practical code samples
- **Best Practices**: Industry-standard approaches
- **Common Pitfalls**: What to avoid
- **Further Reading**: Resources for deeper learning

Master these topics to become a proficient backend engineer capable of building scalable, secure, and maintainable systems.
