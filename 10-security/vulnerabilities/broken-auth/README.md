# Broken Authentication

## Overview

Broken authentication vulnerabilities allow attackers to compromise passwords, keys, or session tokens.

## Common Vulnerabilities

### Session Fixation
```java
// Vulnerable - Session ID not regenerated
session.setAttribute("user", user);

// Safe - Regenerate session ID
session.invalidate();
HttpSession newSession = request.getSession(true);
newSession.setAttribute("user", user);
```

### Weak Password Policy
```java
@Component
public class PasswordValidator {
    
    public void validate(String password) {
        if (password.length() < 12) {
            throw new ValidationException("Password must be at least 12 characters");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("Password must contain uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new ValidationException("Password must contain lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("Password must contain digit");
        }
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            throw new ValidationException("Password must contain special character");
        }
    }
}
```

### Credential Stuffing Prevention
```java
@Component
public class RateLimiter {
    private final RedisTemplate<String, String> redis;
    
    public boolean isBlocked(String username) {
        String key = "login_attempts:" + username;
        String attempts = redis.opsForValue().get(key);
        
        if (attempts != null && Integer.parseInt(attempts) >= 5) {
            return true;
        }
        
        redis.opsForValue().increment(key);
        redis.expire(key, Duration.ofMinutes(15));
        return false;
    }
    
    public void resetAttempts(String username) {
        redis.delete("login_attempts:" + username);
    }
}
```

## Session Security

```java
@Configuration
public class SessionConfig {
    @Bean
    public SessionCookieConfig sessionCookieConfig() {
        SessionCookieConfig config = new SessionCookieConfig();
        config.setHttpOnly(true);
        config.setSecure(true);
        config.setPath("/");
        config.setMaxAge(3600);
        config.setName("SESSIONID");
        return config;
    }
}
```

## Multi-Factor Authentication

```java
@Service
public class MfaService {
    
    public String generateSecret() {
        byte[] buffer = new byte[20];
        new SecureRandom().nextBytes(buffer);
        return Base32().encode(buffer);
    }
    
    public String generateTotp(String secret) {
        long time = System.currentTimeMillis() / 30000;
        byte[] data = ByteBuffer.allocate(8).putLong(time).array();
        
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(Base32().decode(secret), "HmacSHA1"));
        byte[] hash = mac.doFinal(data);
        
        int offset = hash[hash.length - 1] & 0xf;
        int otp = ((hash[offset] & 0x7f) << 24 |
                  ((hash[offset + 1] & 0xff) << 16) |
                  ((hash[offset + 2] & 0xff) << 8) |
                  (hash[offset + 3] & 0xff)) % 1000000;
        
        return String.format("%06d", otp);
    }
}
```

## Best Practices

1. Implement MFA
2. Use strong password policies
3. Regenerate session IDs after login
4. Implement account lockout
5. Use secure session management
6. Log authentication events
7. Implement brute-force protection
8. Use secure password storage
