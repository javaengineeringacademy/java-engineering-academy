# JWT (JSON Web Tokens)

## Structure
JWT consists of three parts separated by dots:
- **Header**: Algorithm and token type
- **Payload**: Claims (user data, expiration, etc.)
- **Signature**: Verification hash

## When to Use
- Stateless authentication
- API authorization
- Microservice communication
- Single Sign-On (SSO)

## Token Flow
1. Client sends credentials to auth server
2. Server validates and creates JWT
3. Token returned to client
4. Client sends token with requests
5. Server verifies token without DB lookup

## Claims
- **Registered**: `sub`, `iss`, `exp`, `iat`
- **Public**: Custom claims
- **Private**: Custom claims for specific use

## Security Considerations
- Always use HTTPS
- Set appropriate expiration
- Store securely (httpOnly cookie or memory)
- Never put sensitive data in payload
- Use strong signing keys

## Common Issues
- Token theft (use short expiration)
- Token replay attacks
- Key management complexity
- Token size affecting performance

## Best Practices
- Rotate signing keys periodically
- Implement token refresh mechanism
- Use asymmetric keys for distributed systems
- Validate all claims on server

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Overview

[Brief description of the topic]

## See Also
- [Encryption](../encryption/) — Underlying crypto for JWT signing
- [SSL/TLS](../ssl/) — Transport security for token delivery
- Circuit Breaker — Resilience when auth service is down

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
