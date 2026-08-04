# JWT Security

## Overview

JWT security covers key management, token rotation, revocation, and protection against common attacks.

## Key Rotation

```java
@Service
public class KeyRotationService {
    private final Map<String, SecretKey> keys = new ConcurrentHashMap<>();
    private String currentKeyId;
    
    @PostConstruct
    public void init() {
        rotateKey();
    }
    
    public void rotateKey() {
        SecretKey newKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        currentKeyId = UUID.randomUUID().toString();
        keys.put(currentKeyId, newKey);
        
        // Remove old keys after retention period
        scheduleKeyRemoval(currentKeyId, Duration.ofDays(7));
    }
    
    public SecretKey getKey(String keyId) {
        return keys.get(keyId);
    }
    
    public SecretKey getCurrentKey() {
        return keys.get(currentKeyId);
    }
}
```

## Token Blacklisting

```java
@Service
public class TokenBlacklistService {
    private final RedisTemplate<String, String> redis;
    
    public void blacklist(String token, Duration ttl) {
        redis.opsForValue().set("blacklist:" + token, "true", ttl);
    }
    
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redis.hasKey("blacklist:" + token));
    }
    
    public void blacklistUntilExpiry(String token) {
        Claims claims = jwtValidator.validateToken(token);
        Duration ttl = Duration.between(
            Instant.now(), 
            claims.getExpiration().toInstant()
        );
        blacklist(token, ttl);
    }
}
```

## Attack Prevention

### Algorithm Confusion
```java
// Always specify algorithm explicitly
Jwts.parserBuilder()
    .setSigningKey(key)
    .requireAlgorithm(SignatureAlgorithm.HS256)  // Prevent 'none' algorithm
    .build()
    .parseClaimsJws(token);
```

### Key Confusion
```java
// Use asymmetric keys for public APIs
// Sign with private key, verify with public key
String token = Jwts.builder()
    .setClaims(claims)
    .signWith(privateKey, SignatureAlgorithm.RS256)
    .compact();

// Verify
Jwts.parserBuilder()
    .setSigningKey(publicKey)
    .build()
    .parseClaimsJws(token);
```

### Timing Attacks
```java
// Use constant-time comparison
public boolean verifySignature(String token, byte[] expectedSignature) {
    byte[] actualSignature = Base64.getUrlDecoder().decode(token);
    return MessageDigest.isEqual(actualSignature, expectedSignature);
}
```

## Security Checklist

| Control | Description | Status |
|---------|-------------|--------|
| Key strength | Use 256+ bit keys | ☐ |
| Algorithm | Specify algorithm explicitly | ☐ |
| Expiration | Set appropriate TTL | ☐ |
| Revocation | Implement token revocation | ☐ |
| Storage | Secure token storage | ☐ |
| Transmission | HTTPS only | ☐ |
| Validation | Validate all claims | ☐ |
| Rotation | Implement key rotation | ☐ |

## Best Practices

1. Use asymmetric keys for distributed systems
2. Implement token revocation
3. Rotate signing keys regularly
4. Validate token type claims
5. Use constant-time signature comparison
6. Monitor for suspicious token usage
7. Implement rate limiting
8. Use short-lived tokens
