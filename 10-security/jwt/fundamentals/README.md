# JWT Fundamentals

## Overview

JSON Web Token (JWT) is a compact, URL-safe means of representing claims between two parties.

## Structure

```
Header.Payload.Signature

Header: {"alg":"HS256","typ":"JWT"}
Payload: {"sub":"1234567890","name":"John","iat":1516239022}
Signature: HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
```

## Claims

### Registered Claims
| Claim | Description |
|-------|-------------|
| iss | Issuer |
| sub | Subject |
| aud | Audience |
| exp | Expiration Time |
| nbf | Not Before |
| iat | Issued At |
| jti | JWT ID |

### Custom Claims
```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "roles": ["USER", "ADMIN"],
  "org": "acme"
}
```

## Token Types

### Access Token
```json
{
  "sub": "user123",
  "exp": 1700000000,
  "iat": 1699996400,
  "type": "access",
  "scope": "read write"
}
```

### Refresh Token
```json
{
  "sub": "user123",
  "exp": 1700086400,
  "iat": 1699996400,
  "type": "refresh",
  "jti": "unique-token-id"
}
```

## Signing Algorithms

| Algorithm | Type | Security |
|-----------|------|----------|
| HS256 | Symmetric | Good |
| RS256 | Asymmetric | Strong |
| ES256 | Asymmetric | Strong |
| PS256 | Asymmetric | Strong |

## Java JWT (JJWT)

```java
// Create token
String token = Jwts.builder()
    .setSubject("user123")
    .claim("roles", Arrays.asList("USER", "ADMIN"))
    .setIssuedAt(new Date())
    .setExpiration(new Date(System.currentTimeMillis() + 3600000))
    .signWith(key, SignatureAlgorithm.HS256)
    .compact();

// Parse token
Claims claims = Jwts.parserBuilder()
    .setSigningKey(key)
    .build()
    .parseClaimsJws(token)
    .getBody();
```

## Best Practices

1. Use short expiration for access tokens
2. Use long expiration for refresh tokens
3. Include only necessary claims
4. Use strong signing keys
5. Validate all claims on receipt
6. Implement token refresh flow
7. Use HTTPS only
8. Never store tokens in localStorage
