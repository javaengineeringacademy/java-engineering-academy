## Authentication and Authorization

Core concepts and patterns for verifying identity and controlling access in .NET applications.

## Overview

Authentication verifies who a user is, while authorization determines what they can access. .NET provides multiple approaches from simple role-based to complex policy-based authorization.

## Why It Matters

- Fundamental for secure applications
- Multiple patterns for different scenarios
- Policy-based authorization provides flexibility
- Integration with identity providers

## Key Concepts

- **Authentication Schemes**: Multiple auth methods per app
- **Claims Principal**: Authenticated user representation
- **Authorization Policies**: Rule-based access control
- **Roles**: Simple group-based authorization
- **Resources**: Objects being protected
- **Requirements**: Authorization conditions

## Core Topics

- Authentication schemes and handlers
- Claims-based identity
- Role-based authorization
- Policy-based authorization
- Resource-based authorization
- Custom authorization handlers
- Anti-forgery and CSRF protection

## Best Practices

- Use policies over roles for flexibility
- Combine authentication schemes when needed
- Implement custom authorization handlers
- Use [Authorize] attributes appropriately
- Always require authorization by default

## Hands-on Labs

- Implement role-based authorization
- Create custom authorization policies
- Build a resource-based authorization handler
- Configure multiple authentication schemes

## Interview Questions

1. What is the difference between authentication and authorization?
2. How do authorization policies work?
3. What is the difference between schemes and handlers?
4. How do you implement custom authorization?

## References

- https://learn.microsoft.com/aspnet/core/security/authorization/
- https://learn.microsoft.com/aspnet/core/security/authorization/policies
- https://learn.microsoft.com/aspnet/core/security/authorization/resourcebased
