# Module 14: Spring Framework

## Overview

This module introduces the Spring Framework, the most widely used Java enterprise framework. Students will learn dependency injection, aspect-oriented programming, configuration management, and core Spring concepts essential for building robust, testable enterprise applications.

## Learning Objectives

By the end of this module, you will be able to:

- Implement dependency injection using Spring IoC container
- Configure beans using annotations and XML
- Understand bean lifecycle and scopes
- Apply aspect-oriented programming (AOP) concepts
- Use Spring configuration and profile management
- Implement validation and scheduling
- Work with Spring JDBC and transaction management

## Prerequisites

- [Module 13: JDBC & Database](../13-jdbc-database/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [Spring Fundamentals](01-spring-fundamentals/) | 2 hours | IoC container, Spring architecture |
| 02 | [Dependency Injection](02-spring-dependency-injection/) | 3 hours | Constructor, setter, field injection |
| 03 | [Bean Lifecycle](03-spring-bean-lifecycle/) | 2 hours | Initialization, destruction, callbacks |
| 04 | [Bean Scopes](04-spring-scopes/) | 2 hours | Singleton, prototype, web scopes |
| 05 | [Spring AOP](05-spring-aop/) | 3 hours | Aspects, pointcuts, advice types |
| 06 | [AOP Advanced](06-spring-aop-advanced/) | 2 hours | Weaving, aspectj integration |
| 07 | [Configuration](07-spring-configuration/) | 2 hours | Java config, profiles, property sources |
| 08 | [Spring Events](08-spring-events/) | 1 hour | Application events, listeners |
| 09 | [Validation](09-spring-validation/) | 2 hours | Bean validation, custom validators |
| 10 | [Scheduling](10-spring-scheduling/) | 2 hours | Task scheduling, cron expressions |
| 11 | [Caching](11-spring-cache/) | 2 hours | Cache abstraction, implementations |
| 12 | [Spring JDBC](12-spring-jdbc/) | 2 hours | JdbcTemplate, NamedParameterJdbc |
| 13 | [Transaction Management](13-spring-transaction/) | 3 hours | Declarative/programmatic transactions |
| 14 | [Spring Testing](14-spring-testing/) | 2 hours | Test context, mocking, slices |

## Key Concepts

- Inversion of Control (IoC) and Dependency Injection (DI)
- Aspect-Oriented Programming (AOP) paradigm
- Spring Bean Factory vs. ApplicationContext
- Transaction propagation and isolation levels
- Spring profiles and environment abstraction

## Enterprise Applications

Spring Framework is the foundation of modern Java enterprise development, providing comprehensive infrastructure support for building loosely coupled, testable, and maintainable applications across all tiers.

## Estimated Total Time

**30 hours**

## Module Project

Build a **Spring Library Management System** that:
- Implements dependency injection throughout the application
- Uses AOP for logging and security
- Manages transactions for book operations
- Implements scheduling for due date notifications
- Demonstrates Spring configuration and profiles

## Resources

- [Spring Documentation](https://docs.spring.io/spring-framework/reference/)
- [Spring Guides](https://spring.io/guides)

**Previous Module**: [Module 13: JDBC & Database](../13-jdbc-database/)
**Next Module**: [Module 15: Spring Boot](../15-spring-boot/)