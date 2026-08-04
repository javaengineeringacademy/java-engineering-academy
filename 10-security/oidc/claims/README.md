# OIDC Claims

## Overview

Claims are name-value pairs in OIDC tokens that contain information about the user and the authentication event.

## Standard Claims

| Claim | Description | Example |
|-------|-------------|---------|
| sub | Subject identifier | user123 |
| name | Full name | John Doe |
| given_name | First name | John |
| family_name | Last name | Doe |
| email | Email address | john@example.com |
| email_verified | Email verified | true |
| picture | Profile picture URL | https://example.com/photo.jpg |
| locale | User locale | en-US |
| zoneinfo | Timezone | America/New_York |

## Custom Claims

```java
@Component
public class CustomClaimsProvider implements OidcTokenCustomizer<JwtEncodingContext> {
    
    @Override
    public void customize(JwtEncodingContext context) {
        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            OidcUser user = (OidcUser) context.getPrincipal();
            
            context.getClaims().claims(claims -> {
                claims.put("roles", user.getRoles());
                claims.put("tenant_id", user.getTenantId());
                claims.put("permissions", user.getPermissions());
            });
        }
    }
}
```

## Claims Validation

```java
@Component
public class ClaimsValidator {
    
    public void validate(OidcIdToken token) {
        // Validate required claims
        Objects.requireNonNull(token.getSubject(), "sub claim required");
        Objects.requireNonNull(token.getIssuer(), "iss claim required");
        Objects.requireNonNull(token.getAudience(), "aud claim required");
        
        // Validate expiration
        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new AuthenticationException("Token expired");
        }
        
        // Validate issuer
        if (!expectedIssuer.equals(token.getIssuer())) {
            throw new AuthenticationException("Invalid issuer");
        }
        
        // Validate audience
        if (!token.getAudience().contains(clientId)) {
            throw new AuthenticationException("Invalid audience");
        }
    }
}
```

## Claims in Different Tokens

### ID Token Claims
```json
{
  "sub": "user123",
  "iss": "https://auth.example.com",
  "aud": "client-app",
  "exp": 1700000000,
  "iat": 1699996400,
  "nonce": "random-nonce"
}
```

### Access Token Claims
```json
{
  "sub": "user123",
  "iss": "https://auth.example.com",
  "aud": "api.example.com",
  "exp": 1700000000,
  "iat": 1699996400,
  "scope": "read write",
  "client_id": "client-app"
}
```

## Best Practices

1. Include only necessary claims
2. Validate all required claims
3. Use claims for authorization decisions
4. Avoid PII in access tokens
5. Document custom claims
6. Implement claims transformation
7. Use standard claim names when possible
8. Validate token issuer and audience
