# JWT Implementation

## Overview

This guide covers practical JWT implementation for authentication and authorization.

## Token Creation

```java
@Service
public class TokenService {
    private final SecretKey key;
    private final long accessTokenExpiry;
    private final long refreshTokenExpiry;
    
    public TokenService(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.accessTokenExpiry = 3600000; // 1 hour
        this.refreshTokenExpiry = 86400000; // 24 hours
    }
    
    public TokenResponse generateTokens(User user) {
        String accessToken = generateToken(user, accessTokenExpiry, "access");
        String refreshToken = generateToken(user, refreshTokenExpiry, "refresh");
        
        return new TokenResponse(accessToken, refreshToken, accessTokenExpiry);
    }
    
    private String generateToken(User user, long expiry, String type) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiry);
        
        return Jwts.builder()
            .setSubject(user.getId())
            .claim("email", user.getEmail())
            .claim("roles", user.getRoles())
            .claim("type", type)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
}
```

## Token Validation

```java
@Component
public class JwtValidator {
    private final SecretKey key;
    
    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("Token expired");
        } catch (JwtException e) {
            throw new AuthenticationException("Invalid token");
        }
    }
    
    public boolean isTokenValid(String token, String userId) {
        Claims claims = validateToken(token);
        return claims.getSubject().equals(userId) && 
               !claims.getExpiration().before(new Date());
    }
}
```

## Refresh Token Flow

```java
@PostMapping("/refresh")
public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
    Claims claims = jwtValidator.validateToken(request.getRefreshToken());
    
    if (!"refresh".equals(claims.get("type"))) {
        throw new AuthenticationException("Invalid token type");
    }
    
    User user = userService.findById(claims.getSubject());
    TokenResponse tokens = tokenService.generateTokens(user);
    
    return ResponseEntity.ok(tokens);
}
```

## Token Revocation

```java
@Service
public class TokenRevocationService {
    private final RedisTemplate<String, String> redis;
    
    public void revokeToken(String token) {
        Claims claims = jwtValidator.validateToken(token);
        long ttl = claims.getExpiration().getTime() - System.currentTimeMillis();
        redis.opsForValue().set("revoked:" + token, "true", ttl, TimeUnit.MILLISECONDS);
    }
    
    public boolean isTokenRevoked(String token) {
        return Boolean.TRUE.equals(redis.hasKey("revoked:" + token));
    }
}
```

## Security Headers

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    FilterChain chain) {
        String header = request.getHeader("Authorization");
        
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            
            if (!tokenRevocationService.isTokenRevoked(token)) {
                Claims claims = jwtValidator.validateToken(token);
                UsernamePasswordAuthenticationToken auth = 
                    new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, getAuthorities(claims));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        
        chain.doFilter(request, response);
    }
}
```

## Best Practices

1. Store tokens securely (httpOnly cookie)
2. Implement token rotation
3. Use short-lived access tokens
4. Validate tokens on every request
5. Implement token revocation
6. Use HTTPS only
7. Never expose tokens in URLs
8. Implement rate limiting
