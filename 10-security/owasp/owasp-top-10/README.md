# OWASP Top 10

## Overview

The OWASP Top 10 is a standard awareness document representing a broad consensus about the most critical security risks to web applications.

## The Top 10 (2021)

### A01: Broken Access Control
Restrictions on what authenticated users are allowed to do are not properly enforced.

**Mitigations:**
```java
// Implement role-based access control
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/users")
public List<User> getUsers() { ... }

// Check ownership
@GetMapping("/orders/{id}")
public Order getOrder(@PathVariable Long id) {
    Order order = orderRepository.findById(id);
    if (!order.getUserId().equals(currentUserId())) {
        throw new AccessDeniedException("Not your order");
    }
    return order;
}
```

### A02: Cryptographic Failures
Failures related to cryptography which often leads to sensitive data exposure.

**Mitigations:**
- Encrypt data at rest and in transit
- Use strong algorithms (AES-256, RSA-2048+)
- Never store passwords in plain text
- Use proper key management

### A03: Injection
User-supplied data is not validated, filtered, or sanitized.

**Mitigations:**
```java
// SQL Injection prevention - use parameterized queries
@Query("SELECT u FROM User u WHERE u.email = :email")
User findByEmail(@Param("email") String email);

// Command injection - avoid Runtime.exec with user input
// Input validation
private static final Pattern SAFE_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
if (!SAFE_PATTERN.matcher(input).matches()) {
    throw new ValidationException("Invalid input");
}
```

### A04: Insecure Design
Risks related to design flaws, missing or ineffective security controls.

**Mitigations:**
- Implement threat modeling
- Use security patterns
- Write security requirements
- Conduct design reviews

### A05: Security Misconfiguration
Missing appropriate security hardening across any part of the application stack.

**Mitigations:**
```yaml
# Disable unnecessary features
management:
  endpoints:
    web:
      exposure:
        include: health,info

# Security headers
security:
  headers:
    content-security-policy: "default-src 'self'"
    strict-transport-security: "max-age=31536000"
```

### A06: Vulnerable and Outdated Components
Using components with known vulnerabilities.

**Mitigations:**
```xml
<!-- Maven dependency check plugin -->
<plugin>
    <groupId>org.owasp</groupId>
    <artifactId>dependency-check-maven</artifactId>
    <version>8.4.0</version>
</plugin>
```

### A07: Identification and Authentication Failures
Confirmation of the user's identity, authentication, and session management is not implemented correctly.

**Mitigations:**
- Implement multi-factor authentication
- Use secure session management
- Limit failed login attempts
- Use strong password policies

### A08: Software and Data Integrity Failures
Code and infrastructure that does not protect against integrity violations.

**Mitigations:**
- Use digital signatures
- Verify CI/CD pipeline integrity
- Use dependency pinning
- Implement code review processes

### A09: Security Logging and Monitoring Failures
Insufficient logging, detection, monitoring, and active response.

**Mitigations:**
```java
// Log security events
logger.info(SECURITY_MARKER, "Login attempt: username={}", username);
logger.warn(SECURITY_MARKER, "Failed login: username={}, ip={}", username, ip);
logger.error(SECURITY_MARKER, "Unauthorized access: user={}, resource={}", user, resource);
```

### A10: Server-Side Request Forgery (SSRF)
SSRF flaws occur whenever a web application fetches a remote resource without validating the user-supplied URL.

**Mitigations:**
```java
// Validate and sanitize URLs
private boolean isAllowedUrl(String url) {
    URI uri = URI.create(url);
    return ALLOWED_HOSTS.contains(uri.getHost());
}

// Use allowlist for external requests
if (!isAllowedUrl(url)) {
    throw new ValidationException("URL not allowed");
}
```

## Best Practices

1. Conduct regular security assessments
2. Implement defense in depth
3. Use automated security testing
4. Keep dependencies updated
5. Follow principle of least privilege
6. Implement proper logging
7. Conduct code reviews
8. Provide security training
