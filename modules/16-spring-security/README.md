# Module 16: Spring Security

## Overview

This module covers Spring Security, the comprehensive security framework for Java applications. Students will learn authentication and authorization mechanisms, form-based login, JWT tokens, OAuth2 integration, and method-level security for building secure enterprise applications.

## Learning Objectives

By the end of this module, you will be able to:

- Implement authentication and authorization with Spring Security
- Configure form-based and HTTP Basic authentication
- Create stateless APIs using JWT tokens
- Integrate OAuth2 and OpenID Connect providers
- Apply method-level security constraints
- Handle CSRF protection and CORS configuration
- Implement custom security filters and handlers

## Prerequisites

- [Module 15: Spring Boot](../15-spring-boot/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [Security Fundamentals](01-security-fundamentals/) | 2 hours | Security concepts, Spring Security architecture |
| 02 | [Form-based Auth](02-form-based-auth/) | 2 hours | Login forms, remember me, session management |
| 03 | [JWT Authentication](03-jwt-authentication/) | 3 hours | Token generation, validation, filters |
| 04 | [OAuth2](04-oauth2/) | 3 hours | Authorization server, resource server, clients |
| 05 | [Method Security](05-method-security/) | 2 hours | Pre/Post annotations, roles, expressions |

## Key Concepts

- Authentication vs. authorization
- Security filter chain architecture
- Stateless vs. stateful security
- Token-based authentication
- OAuth2 flows and grants

## Enterprise Applications

Spring Security is essential for protecting enterprise applications, implementing single sign-on, securing microservices communication, and meeting compliance requirements for data protection and access control.

## Estimated Total Time

**12 hours**

## Module Project

Build a **Secure E-Commerce API** that:
- Implements JWT-based authentication
- Uses OAuth2 for social login integration
- Applies role-based access control
- Secures REST endpoints and method calls
- Handles CORS and CSRF for web clients

## Resources

- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/)
- [OAuth2 Specification](https://oauth.net/2/)

**Previous Module**: [Module 15: Spring Boot](../15-spring-boot/)
**Next Module**: [Module 17: REST API](../17-rest-api/)