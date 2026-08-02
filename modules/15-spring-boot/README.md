# Module 15: Spring Boot

## Overview

This module covers Spring Boot, the convention-over-configuration framework that simplifies Spring application development. Students will learn auto-configuration, starter dependencies, Actuator monitoring, and how to build production-ready applications with minimal configuration.

## Learning Objectives

By the end of this module, you will be able to:

- Create Spring Boot applications using initializers
- Understand auto-configuration and conditional beans
- Configure applications using properties and YAML
- Build RESTful web applications with embedded servers
- Implement data access with Spring Data JPA
- Add validation and security to applications
- Monitor applications using Spring Boot Actuator

## Prerequisites

- [Module 14: Spring Framework](../14-spring-framework/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [Spring Boot Fundamentals](01-spring-boot-fundamentals/) | 2 hours | Auto-configuration, starter dependencies |
| 02 | [Configuration](02-spring-boot-configuration/) | 2 hours | Properties, YAML, externalized configuration |
| 03 | [Web Starter](03-spring-boot-starter-web/) | 3 hours | REST controllers, embedded Tomcat, content negotiation |
| 04 | [Data JPA Starter](04-spring-boot-starter-data-jpa/) | 3 hours | Repositories, queries, auditing |
| 05 | [Validation Starter](05-spring-boot-starter-validation/) | 2 hours | Bean validation, custom constraints |
| 06 | [Security Starter](06-spring-boot-starter-security/) | 3 hours | Auto-configured security, custom configuration |

## Key Concepts

- Convention over configuration principle
- Starter dependencies and dependency management
- Auto-configuration and conditional beans
- Externalized configuration and profiles
- Embedded server architecture

## Enterprise Applications

Spring Boot is the industry standard for building production-ready Spring applications, enabling rapid development of microservices, web applications, and REST APIs with built-in monitoring and deployment features.

## Estimated Total Time

**15 hours**

## Module Project

Build a **Product Management REST API** that:
- Uses Spring Boot auto-configuration
- Implements CRUD operations with Spring Data JPA
- Adds input validation and error handling
- Secures endpoints with Spring Security
- Monitors health and metrics with Actuator

## Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Initializr](https://start.spring.io/)

**Previous Module**: [Module 14: Spring Framework](../14-spring-framework/)
**Next Module**: [Module 16: Spring Security](../16-spring-security/)