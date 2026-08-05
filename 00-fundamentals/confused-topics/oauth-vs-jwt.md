# OAuth vs JWT

## What They Are

### OAuth 2.0
An authorization framework that enables applications to obtain limited access to user accounts. Defines how users can grant access to their resources on one site to another site without sharing credentials.

### JWT (JSON Web Token)
A compact, URL-safe token format for representing claims between two parties. Defines a standard structure for transmitting information as JSON objects, signed or encrypted.

## Key Difference Table

| Feature | OAuth 2.0 | JWT |
|---------|-----------|-----|
| Purpose | Authorization | Token format |
| Type | Framework/Protocol | Data format |
| Complexity | High (multiple flows) | Low (single token) |
| State | Can be stateful | Stateless |
| Token Format | Not specified | Defined (header.payload.signature) |
| Refresh Tokens | Yes | Optional |
| Revocation | Yes (token introspection) | Difficult (stateless) |
| Use Case | Third-party access | Authentication/Authorization |
| Storage | Server-side (resource owner) | Client-side (typically) |
| Scope | Fine-grained permissions | Claims-based |

## When to Use Which

### Use OAuth When
- Third-party applications need access to user data
- You need to delegate authentication
- Fine-grained permission control required
- Building a platform with API access
- Social login (Google, Facebook, GitHub)
- Enterprise SSO integration

### Use JWT When
- Stateless authentication needed
- Microservices token exchange
- Single sign-on (SSO) implementation
- API authentication
- Information needs to be传递 without database lookup
- Short-lived tokens for security

## Interview Trap

**Trap**: "JWT and OAuth are the same thing."

**Reality**: They serve different purposes and are often used together. OAuth is an authorization framework that can use JWT as its token format. JWT is just a token format that can be used independently of OAuth.

**Follow-up Trap**: "OAuth is only for third-party access."

**Reality**: While OAuth originated for third-party access, it's now widely used for delegated authentication and authorization in enterprise environments, including first-party applications.

## Visual Diagram

```
OAuth 2.0 Authorization Code Flow:
┌─────────┐     ┌─────────┐     ┌─────────┐
│  User    │     │ Client  │     │ Auth    │
│ (Resource│     │ (App)   │     │ Server  │
│  Owner)  │     │         │     │         │
└─────────┘     └─────────┘     └─────────┘
    │               │               │
    │──1. Login───>│               │
    │               │──2. Auth Request──>│
    │<──3. Login───│<──4. Redirect─────│
    │   Page        │               │
    │──5. Credentials──>│           │
    │               │──6. Token Request──>│
    │               │<──7. Access Token──│
    │               │   (may be JWT)     │
    │               │──8. API Request───>│
    │               │<──9. Response──────│
    │<──10. Display──│               │

JWT Token Structure:
┌─────────────────────────────────────────────────────┐
│ Header.Payload.Signature                            │
│                                                     │
│ Header:                                             │
│ {                                                   │
│   "alg": "RS256",                                   │
│   "typ": "JWT"                                      │
│ }                                                   │
│                                                     │
│ Payload:                                            │
│ {                                                   │
│   "sub": "1234567890",                              │
│   "name": "John Doe",                               │
│   "iat": 1516239022,                                │
│   "exp": 1516242622,                                │
│   "scope": "read write"                             │
│ }                                                   │
│                                                     │
│ Signature:                                          │
│ RSA-SHA256(                                         │
│   base64UrlEncode(header) + "." +                   │
│   base64UrlEncode(payload),                         │
│   private_key                                       │
│ )                                                   │
└─────────────────────────────────────────────────────┘
```

## Common Integration Pattern

OAuth + JWT are often used together:
1. User authenticates via OAuth
2. Auth server issues JWT access token
3. Client uses JWT for API access
4. Resource server validates JWT locally
5. No need to contact auth server for each request

## Key Insight

**OAuth 2.0**: Authorization framework (who can access what)
**JWT**: Token format (how to represent claims)

They're complementary, not competing:
- OAuth can use JWT as its token format
- JWT can be used without OAuth
- OAuth doesn't require JWT (can use opaque tokens)
- JWT can be used for authentication without OAuth

## Security Considerations

**OAuth Security:**
- Use authorization code flow with PKCE
- Never store client secrets in browser
- Validate redirect URIs
- Use short-lived access tokens

**JWT Security:**
- Use strong signing algorithms (RS256, ES256)
- Validate all claims (exp, iss, aud)
- Never store sensitive data in payload
- Implement token revocation if needed
