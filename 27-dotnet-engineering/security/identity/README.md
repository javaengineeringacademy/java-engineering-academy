## ASP.NET Core Identity

Built-in identity system for user management, authentication, and authorization in ASP.NET Core applications.

## Overview

ASP.NET Core Identity provides a complete user management system including registration, login, password reset, two-factor authentication, and external login providers.

## Why It Matters

- Complete identity solution out of the box
- Supports external login providers (Google, Microsoft, etc.)
- Two-factor authentication built in
- Customizable user and role management
- Entity Framework Core integration

## Key Concepts

- **IdentityUser**: User entity with identity properties
- **IdentityRole**: Role entity for role-based authorization
- **UserManager**: CRUD operations for users
- **SignInManager**: Authentication operations
- **IdentityDbContext**: Database context for identity
- **External Providers**: OAuth2 login providers

## Core Topics

- Identity setup and configuration
- User registration and login
- Password policies and validation
- Two-factor authentication
- External login providers
- Role management
- Claims management

## Best Practices

- Use strong password policies
- Implement account lockout for brute force protection
- Use external providers for easier login
- Store identity data separately from application data
- Use UserManager and SignInManager APIs

## Hands-on Labs

- Set up ASP.NET Core Identity
- Add external login with Google
- Implement two-factor authentication
- Create custom user properties

## Interview Questions

1. How does ASP.NET Core Identity store user data?
2. What is the difference between UserManager and SignInManager?
3. How do you add external login providers?

## References

- https://learn.microsoft.com/aspnet/core/security/authentication/identity/
- https://learn.microsoft.com/aspnet/core/security/authentication/identity-configuration
- https://learn.microsoft.com/aspnet/core/security/authentication/external-login
