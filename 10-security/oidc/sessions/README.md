# OIDC Sessions

## Overview

OIDC session management handles user login state, logout, and session lifecycle across client applications.

## Session Types

### Front-Channel Logout
```html
<!-- Hidden iframe triggers logout -->
<iframe src="https://auth.example.com/logout?client_id=app&post_logout_redirect_uri=https://app.com"></iframe>
```

### Back-Channel Logout
```java
// Server-to-server logout notification
@PostMapping("/backchannel-logout")
public void backchannelLogout(@RequestBody LogoutToken logoutToken) {
    // Validate logout token
    Claims claims = jwtValidator.validate(logoutToken);
    
    // Find and invalidate session
    String sub = claims.getSubject();
    String sid = claims.get("sid", String.class);
    sessionService.invalidate(sub, sid);
}
```

## Session Management Endpoints

```java
// End session endpoint
@GetMapping("/logout")
public String logout(@RequestParam String id_token_hint,
                     @RequestParam String post_logout_redirect_uri) {
    // Revoke tokens
    tokenService.revokeAll(id_token_hint);
    
    // Clear session
    session.invalidate();
    
    // Redirect to provider
    return "redirect:" + buildLogoutUrl(id_token_hint, post_logout_redirect_uri);
}
```

## Session Storage

```java
@Service
public class SessionService {
    private final RedisTemplate<String, Object> redis;
    
    public void createSession(String userId, String sessionId) {
        Session session = Session.builder()
            .userId(userId)
            .sessionId(sessionId)
            .createdAt(Instant.now())
            .expiresAt(Instant.now().plus(Duration.ofHours(1)))
            .build();
        
        redis.opsForValue().set("session:" + sessionId, session, Duration.ofHours(1));
    }
    
    public boolean isValid(String sessionId) {
        Session session = (Session) redis.opsForValue().get("session:" + sessionId);
        return session != null && !session.getExpiresAt().isBefore(Instant.now());
    }
    
    public void invalidate(String sessionId) {
        redis.delete("session:" + sessionId);
    }
}
```

## Best Practices

1. Implement proper logout flow
2. Use secure session storage
3. Set appropriate session timeouts
4. Implement session fixation protection
5. Use HTTPS only
6. Clear session on logout
7. Implement concurrent session limits
8. Monitor session anomalies
