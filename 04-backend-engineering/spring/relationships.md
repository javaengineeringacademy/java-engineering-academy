# Spring Relationships

## Works With

### Hibernate

Hibernate is the most common JPA provider for Spring. It handles object-relational mapping. Spring Boot auto-configures Hibernate with sensible defaults.

Use Spring Data JPA with Hibernate for repository abstractions. Hibernate validates entity mappings at startup.

### JPA (Java Persistence API)

JPA is the specification for ORM in Java. Hibernate is its most popular implementation. Spring Data JPA provides repository support and query derivation.

Use JPA annotations for entity mapping. Spring manages transaction boundaries for persistence operations.

### Thymeleaf

Thymeleaf is a server-side template engine for Spring MVC. It renders HTML with dynamic content. Thymeleaf templates are valid HTML, enabling designer-developer collaboration.

Spring Boot auto-configures Thymeleaf with content negotiation. Use `spring-boot-starter-thymeleaf` for setup.

### Spring Security

Spring Security provides authentication and authorization. It integrates with Spring MVC and Spring Boot. Security is configured through filters and beans.

Use `@PreAuthorize` and `@Secured` for method-level security. Spring Security supports OAuth2, LDAP, and form-based authentication.

## Alternative

### Quarkus

Quarkus is a Kubernetes-native Java framework. It optimizes for fast startup and low memory. Quarkus uses build-time processing for compilation.

Consider Quarkus for serverless and containerized deployments. Spring has a larger ecosystem and community.

Quarkus integrates with GraalVM for native compilation. Spring supports native compilation through Spring Native.

### Micronaut

Micronaut is a lightweight Java framework. It uses compile-time dependency injection. Micronaut has fast startup and low memory usage.

Consider Micronaut for microservices and serverless. Spring is better for complex enterprise applications.

Micronaut uses AOT compilation for faster startup. Spring uses runtime reflection.

## Migration Notes

Migrating from Spring to alternatives requires consideration of:
- Dependency injection model (runtime vs compile-time)
- Auto-configuration vs explicit configuration
- Spring Boot starters vs custom dependency management
- Spring Security integration
- Transaction management
- AOP and proxy-based features

Migrating to Spring from alternatives requires:
- Spring Boot project setup
- Dependency selection (starters)
- Configuration migration (properties or YAML)
- Bean definition and wiring
- Security configuration
- Testing with Spring Boot Test
