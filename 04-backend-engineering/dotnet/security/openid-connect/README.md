## OpenID Connect in .NET

OpenID Connect (OIDC) is an identity layer on top of OAuth2 for user authentication.

## Overview

OIDC extends OAuth2 to provide authentication, returning user identity information (claims) alongside access tokens. It is the standard for modern SSO implementations.

## Why It Matters

- Standard protocol for SSO
- Provides user identity alongside authorization
- Supported by all major identity providers
- Foundation for modern authentication

## Key Concepts

- **ID Token**: JWT containing user identity claims
- **UserInfo Endpoint**: Additional user profile data
- **Discovery Document**: OIDC provider metadata
- **Registration**: Client registration with provider
- **Logout**: Single sign-out functionality

## Core Topics

- OIDC flow (Authorization Code with PKCE)
- ID token validation
- Claims extraction and mapping
- ASP.NET Core OIDC middleware
- Multi-tenant OIDC
- Single sign-out implementation

## Best Practices

- Validate ID tokens thoroughly
- Use HTTPS for all endpoints
- Implement proper logout flows
- Store tokens securely
- Handle token refresh properly

## Hands-on Labs

- Configure OIDC with Azure AD
- Implement SSO across multiple apps
- Handle multi-tenant OIDC
- Implement single sign-out

## Interview Questions

1. What is the difference between OAuth2 and OIDC?
2. How does ID token validation work?
3. What is the discovery document?

## References

- https://learn.microsoft.com/aspnet/core/security/authentication/
- https://openid.net/specs/openid-connect-core-1_0.html
- https://learn.microsoft.com/azure/active-directory/develop/
