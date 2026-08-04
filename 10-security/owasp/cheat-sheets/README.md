# Security Cheat Sheets

## Overview

Security cheat sheets provide quick reference guides for implementing security controls in various technologies and scenarios.

## Authentication & Authorization

### Password Hashing
```java
// BCrypt
String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));

// Verify
if (BCrypt.checkpw(password, hashed)) {
    // Password matches
}
```

### Session Management
```java
// Generate secure session ID
String sessionId = UUID.randomUUID().toString();

// Set secure cookie
Cookie cookie = new Cookie("SESSIONID", sessionId);
cookie.setHttpOnly(true);
cookie.setSecure(true);
cookie.setPath("/");
cookie.setMaxAge(3600);
response.addCookie(cookie);
```

### JWT Best Practices
```java
// Short expiration
long expiration = System.currentTimeMillis() + 3600000; // 1 hour

// Use strong signing key
SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

// Validate all claims
Jws<Claims> jws = Jwts.parserBuilder()
    .setSigningKey(key)
    .requireIssuer("your-app")
    .requireSubject("userId")
    .build()
    .parseSignedClaims(token);
```

## Input Validation

### SQL Injection Prevention
```java
// Always use parameterized queries
@Query("SELECT * FROM users WHERE email = :email")
User findByEmail(@Param("email") String email);

// Validate input
private String sanitizeInput(String input) {
    return input.replaceAll("[^a-zA-Z0-9]", "");
}
```

### XSS Prevention
```java
// Encode output
import org.apache.commons.text.StringEscapeUtils;

String safeHtml = StringEscapeUtils.escapeHtml4(userInput);
String safeJavaScript = StringEscapeUtils.escapeEcmaScript(userInput);
```

### CSRF Protection
```java
// Generate CSRF token
String csrfToken = UUID.randomUUID().toString();
session.setAttribute("csrf-token", csrfToken);

// Validate on submit
if (!csrfToken.equals(request.getParameter("csrf-token"))) {
    throw new SecurityException("Invalid CSRF token");
}
```

## Transport Security

### TLS Configuration
```java
// Force HTTPS
@Bean
public FilterRegistrationBean<Filter> httpsRedirectFilter() {
    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
    bean.setFilter((req, res, chain) -> {
        HttpServletResponse response = (HttpServletResponse) res;
        if (!request.isSecure()) {
            response.sendRedirect("https://" + request.getServerName() + request.getRequestURI());
        }
        chain.doFilter(req, res);
    });
    return bean;
}
```

### Security Headers
```java
@Component
public class SecurityHeadersFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        response.setHeader("Content-Security-Policy", "default-src 'self'");
        chain.doFilter(req, res);
    }
}
```

## Data Protection

### Encryption at Rest
```java
// AES encryption
Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
byte[] encrypted = cipher.doFinal(plaintext);
```

### Secure Random
```java
// Use SecureRandom for tokens
SecureRandom random = new SecureRandom();
byte[] token = new byte[32];
random.nextBytes(token);
String tokenString = Base64.getUrlEncoder().withoutPadding().encodeToString(token);
```

## Logging Security

### What to Log
- Authentication attempts (success/failure)
- Authorization failures
- Input validation failures
- System errors

### What NOT to Log
- Passwords
- Credit card numbers
- Social security numbers
- API keys
- Session tokens

```java
// Log sensitive data masking
logger.info("User login: username={}", username);
logger.info("Payment: card=****{}", cardNumber.substring(cardNumber.length() - 4));
```
