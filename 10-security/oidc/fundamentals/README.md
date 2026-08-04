# OpenID Connect Fundamentals

## Overview

OpenID Connect (OIDC) is an identity layer on top of OAuth 2.0 that provides user authentication and profile information.

## Core Concepts

### ID Token
```json
{
  "iss": "https://auth.example.com",
  "sub": "user123",
  "aud": "client-app",
  "exp": 1700000000,
  "iat": 1699996400,
  "nonce": "random-nonce",
  "name": "John Doe",
  "email": "john@example.com",
  "email_verified": true,
  "picture": "https://example.com/photo.jpg"
}
```

### Endpoints
| Endpoint | Purpose |
|----------|---------|
| Authorization | Start authentication |
| Token | Exchange code for tokens |
| UserInfo | Get user profile |
| JWKS | Public keys for verification |
| Discovery | OIDC configuration |

## Configuration

### Well-Known Endpoint
```json
{
  "issuer": "https://auth.example.com",
  "authorization_endpoint": "https://auth.example.com/authorize",
  "token_endpoint": "https://auth.example.com/token",
  "userinfo_endpoint": "https://auth.example.com/userinfo",
  "jwks_uri": "https://auth.example.com/.well-known/jwks.json",
  "scopes_supported": ["openid", "profile", "email"],
  "response_types_supported": ["code", "id_token"],
  "grant_types_supported": ["authorization_code", "refresh_token"],
  "subject_types_supported": ["public"],
  "id_token_signing_alg_values_supported": ["RS256"]
}
```

## Authentication Flow

```java
// Build authorization URL
String authUrl = UriComponentsBuilder.fromUriString(discovery.getAuthorizationEndpoint())
    .queryParam("response_type", "code")
    .queryParam("client_id", clientId)
    .queryParam("redirect_uri", redirectUri)
    .queryParam("scope", "openid profile email")
    .queryParam("state", state)
    .queryParam("nonce", nonce)
    .build().toUriString();

// Exchange code for tokens
TokenResponse tokens = tokenEndpoint.exchange(code, redirectUri);

// Validate ID token
Claims idToken = validateIdToken(tokens.getIdToken());

// Get user info
UserInfo userInfo = userInfoEndpoint.getUserInfo(tokens.getAccessToken());
```

## Best Practices

1. Always validate ID token signature
2. Verify issuer and audience
3. Check nonce to prevent replay
4. Use short-lived ID tokens
5. Implement proper session management
6. Use PKCE for public clients
7. Validate token expiry
8. Implement proper logout
