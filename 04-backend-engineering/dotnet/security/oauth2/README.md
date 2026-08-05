## OAuth2 in .NET

Implementing OAuth2 authorization flows in .NET applications for secure API access.

## Overview

OAuth2 is an authorization framework that enables third-party applications to obtain limited access to HTTP services. .NET provides libraries for implementing all OAuth2 flows.

## Why It Matters

- Industry standard for API authorization
- Enables secure third-party access
- Supports multiple grant types for different scenarios
- Foundation for OpenID Connect authentication

## Key Concepts

- **Authorization Code Flow**: For web applications
- **Client Credentials Flow**: For service-to-service
- **PKCE**: Proof Key for Code Exchange for public clients
- **Resource Owner Password**: Legacy flow (not recommended)
- **Refresh Tokens**: Obtaining new access tokens
- **Scopes**: Permission boundaries

## Core Topics

- Authorization Code flow with PKCE
- Client Credentials flow
- Token exchange and refresh
- Scope management
- ASP.NET Core OAuth2 integration
- Access token validation
- Resource server implementation

## Best Practices

- Always use PKCE for authorization code flow
- Use short-lived access tokens
- Validate tokens on every request
- Store refresh tokens securely
- Use scopes for fine-grained permissions

## Hands-on Labs

- Implement Authorization Code flow
- Set up a resource server with token validation
- Implement Client Credentials flow
- Handle token refresh

## Interview Questions

1. What is the difference between OAuth2 and OpenID Connect?
2. How does PKCE improve security?
3. When should you use Client Credentials flow?
4. What are refresh tokens and how do they work?

## References

- https://learn.microsoft.com/aspnet/core/security/authentication/
- https://learn.microsoft.com/dotnet/api/microsoft.aspnetcore.authentication.oauth
- https://datatracker.ietf.org/doc/html/rfc6749
