# Module 17: REST API

## Overview

This module covers designing and building RESTful APIs using Spring Boot. Students will learn REST principles, API design best practices, documentation with Swagger, HATEOAS implementation, and versioning strategies for building scalable web services.

## Learning Objectives

By the end of this module, you will be able to:

- Design RESTful APIs following best practices
- Implement CRUD operations with proper HTTP semantics
- Handle exceptions and error responses gracefully
- Document APIs using OpenAPI/Swagger
- Implement HATEOAS for hypermedia-driven APIs
- Apply API versioning strategies
- Create comprehensive API documentation

## Prerequisites

- [Module 16: Spring Security](../16-spring-security/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [REST Fundamentals](01-rest-fundamentals/) | 2 hours | REST principles, HTTP methods, status codes |
| 02 | [REST Controller](02-rest-controller/) | 2 hours | Request mapping, parameter binding |
| 03 | [Request/Response](03-request-response/) | 2 hours | DTOs, response entities, content negotiation |
| 04 | [Exception Handling](04-exception-handling/) | 2 hours | @ControllerAdvice, custom exceptions |
| 05 | [API Versioning](05-api-versioning/) | 1 hour | URI, header, media type versioning |
| 06 | [Swagger/OpenAPI](06-swagger/) | 2 hours | API documentation, annotations, UI |
| 07 | [HATEOAS](07-hateoas/) | 2 hours | Hypermedia links, resource assemblers |

## Key Concepts

- REST architectural constraints
- Resource naming conventions
- HTTP method semantics (GET, POST, PUT, DELETE)
- Content negotiation and media types
- Richardson Maturity Model

## Enterprise Applications

REST APIs are the backbone of modern enterprise integration, enabling system interoperability, mobile backend development, and microservices communication with standardized, well-documented interfaces.

## Estimated Total Time

**13 hours**

## Module Project

Build a **Blog REST API** that:
- Implements full CRUD operations for posts and comments
- Uses proper HTTP status codes and methods
- Documents endpoints with Swagger/OpenAPI
- Applies versioning for API evolution
- Demonstrates HATEOAS for discoverable resources

## Resources

- [RESTful Web Services](https://restfulapi.net/)
- [Spring REST Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html)

**Previous Module**: [Module 16: Spring Security](../16-spring-security/)
**Next Module**: [Module 18: Microservices](../18-microservices/)