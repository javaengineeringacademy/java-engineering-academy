# JSON Web Tokens (JWT)

## Comprehensive Guide to JWT Authentication

JWT (JSON Web Tokens) is a compact, URL-safe means of representing claims between parties. This guide covers JWT structure, signing, validation, and best practices.

---

## Table of Contents

1. [JWT Structure](#jwt-structure)
2. [Token Types](#token-types)
3. [Signing Algorithms](#signing-algorithms)
4. [Implementation](#implementation)
5. [Validation](#validation)
6. [Best Practices](#best-practices)

---

## JWT Structure

### Three Parts

```
Header.Payload.Signature

Example:
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

### Header

```json
{
    "alg": "HS256",
    "typ": "JWT",
    "kid": "key-id-123"
}
```

### Payload (Claims)

```json
{
    "sub": "1234567890",
    "name": "John Doe",
    "email": "john@example.com",
    "roles": ["user", "admin"],
    "iat": 1716239022,
    "exp": 1716242622,
    "iss": "https://auth.example.com",
    "aud": "https://api.example.com"
}
```

### Signature

```java
// HMAC-SHA256
HmacSHA256(
    base64UrlEncode(header) + "." + base64UrlEncode(payload),
    secret
);

// RSA-SHA256
RSA_SHA256(
    base64UrlEncode(header) + "." + base64UrlEncode(payload),
    privateKey
);
```

---

## Token Types

### Access Token

```java
// Short-lived (15-60 minutes)
public class AccessToken {
    private String sub;        // Subject (user ID)
    private String iss;        // Issuer
    private String aud;        // Audience
    private long iat;          // Issued at
    private long exp;          // Expiration
    private String scope;      // Scopes
    private Map<String, Object> claims;
}
```

### Refresh Token

```java
// Long-lived (days to months)
public class RefreshToken {
    private String sub;
    private String jti;        // Unique token ID
    private long exp;
    private String scope;
    private String family;     // Token family for rotation
}
```

### ID Token

```java
// OpenID Connect
public class IdToken {
    private String sub;
    private String iss;
    private String aud;
    private long iat;
    private long exp;
    private long auth_time;
    private String nonce;
    private Map<String, Object> claims;
}
```

---

## Signing Algorithms

### HMAC (Symmetric)

```java
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiry))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }
}
```

### RSA (Asymmetric)

```java
@Component
public class JwtTokenProvider {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtTokenProvider(
            @Value("${jwt.private-key-path}") String privateKeyPath,
            @Value("${jwt.public-key-path}") String publicKeyPath)
            throws Exception {

        this.privateKey = loadPrivateKey(privateKeyPath);
        this.publicKey = loadPublicKey(publicKeyPath);
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiry))
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(publicKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
```

---

## Implementation

### Token Service

```java
@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public TokenResponse generateTokenPair(UserDetails userDetails) {
        String accessToken = generateAccessToken(userDetails);
        String refreshToken = generateRefreshToken(userDetails);

        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(Duration.ofMinutes(15).toSeconds())
            .build();
    }

    private String generateAccessToken(UserDetails userDetails) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(userDetails.getUsername())
            .issuer("https://auth.example.com")
            .issuedAt(now)
            .expiresAt(now.plus(Duration.ofMinutes(15)))
            .claim("roles", userDetails.getAuthorities())
            .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims))
            .getTokenValue();
    }
}
```

### Filter

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Jwt jwt = jwtDecoder.decode(token);
                String username = jwt.getSubject();

                UserDetails userDetails = userDetailsService
                    .loadUserByUsername(username);

                if (jwt.getExpiration().isAfter(Instant.now())) {
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
                }
            } catch (JwtException e) {
                logger.error("JWT validation failed", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
```

---

## Validation

### Token Validation

```java
@Component
public class TokenValidator {

    private final JwtDecoder jwtDecoder;

    public ValidationResult validate(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);

            // Check expiration
            if (jwt.getExpiration().isBefore(Instant.now())) {
                return ValidationResult.expired();
            }

            // Check issuer
            if (!"https://auth.example.com".equals(jwt.getIssuer())) {
                return ValidationResult.invalid("Invalid issuer");
            }

            // Check audience
            List<String> audience = jwt.getAudience();
            if (audience == null || !audience.contains("https://api.example.com")) {
                return ValidationResult.invalid("Invalid audience");
            }

            return ValidationResult.valid(jwt);

        } catch (JwtException e) {
            return ValidationResult.invalid(e.getMessage());
        }
    }
}
```

---

## Best Practices

### 1. Keep Tokens Short-Lived

```java
// Access token: 15 minutes
private Duration accessTokenExpiry = Duration.ofMinutes(15);

// Refresh token: 7 days
private Duration refreshTokenExpiry = Duration.ofDays(7);
```

### 2. Use Strong Secrets

```yaml
jwt:
  secret: ${JWT_SECRET}  # At least 256 bits for HS256
  # Use RSA keys for production
```

### 3. Validate All Claims

```java
public Claims validateToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(publicKey)
        .requireIssuer("https://auth.example.com")
        .requireAudience("https://api.example.com")
        .build()
        .parseClaimsJws(token)
        .getBody();
}
```

### 4. Handle Token Refresh

```java
@PostMapping("/refresh")
public ResponseEntity<TokenResponse> refreshToken(
        @RequestParam String refreshToken) {

    Claims claims = tokenValidator.validate(refreshToken);

    UserDetails user = userDetailsService
        .loadUserByUsername(claims.getSubject());

    TokenResponse newTokens = tokenService.generateTokenPair(user);

    // Rotate refresh token
    tokenService.revokeRefreshToken(refreshToken);

    return ResponseEntity.ok(newTokens);
}
```

### 5. Implement Token Revocation

```java
@Service
public class TokenRevocationService {

    private final RedisTemplate<String, String> redisTemplate;

    public void revokeToken(String token) {
        Claims claims = tokenValidator.getClaims(token);
        long ttl = claims.getExpiration().getTime() - System.currentTimeMillis();

        redisTemplate.opsForValue().set(
            "revoked:" + token, "true", ttl, TimeUnit.MILLISECONDS);
    }

    public boolean isRevoked(String token) {
        return Boolean.TRUE.equals(
            redisTemplate.hasKey("revoked:" + token));
    }
}
```

### 6. Store Tokens Securely

```java
// Use httpOnly cookies
@Bean
public ResponseCookie accessTokenCookie(String token) {
    return ResponseCookie.from("access_token", token)
        .httpOnly(true)
        .secure(true)
        .sameSite("Strict")
        .path("/")
        .maxAge(Duration.ofMinutes(15))
        .build();
}
```

---

## Further Reading

- [JWT Specification](https://datatracker.ietf.org/doc/html/rfc7519)
- [JOSE Specifications](https://datatracker.ietf.org/wg/jose/documents/)
- [Spring Security JWT](https://spring.io/guides/gs/securing-web/)
- [JWT Best Practices](https://datatracker.ietf.org/doc/html/rfc8725)
