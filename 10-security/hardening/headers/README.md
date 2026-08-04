# Security Headers

## Overview

Security headers protect against clickjacking, XSS, MIME sniffing, and other attacks.

## Essential Headers

### Content Security Policy (CSP)
```java
@Component
public class SecurityHeadersFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) res;
        
        response.setHeader("Content-Security-Policy", 
            "default-src 'self'; " +
            "script-src 'self' 'nonce-abc123'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self'; " +
            "connect-src 'self'; " +
            "frame-ancestors 'none'");
        
        chain.doFilter(req, res);
    }
}
```

### X-Content-Type-Options
```java
response.setHeader("X-Content-Type-Options", "nosniff");
```

### X-Frame-Options
```java
response.setHeader("X-Frame-Options", "DENY");
```

### Strict-Transport-Security (HSTS)
```java
response.setHeader("Strict-Transport-Security", 
    "max-age=31536000; includeSubDomains; preload");
```

### X-XSS-Protection
```java
response.setHeader("X-XSS-Protection", "1; mode=block");
```

### Referrer-Policy
```java
response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
```

### Permissions-Policy
```java
response.setHeader("Permissions-Policy", 
    "camera=(), microphone=(), geolocation=(), payment=()");
```

## Spring Security Configuration

```java
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers
            .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
            .frameOptions(frame -> frame.deny())
            .httpStrictTransportSecurity(hsts -> hsts
                .maxAgeInSeconds(31536000)
                .includeSubdomains(true)
                .preload(true))
            .contentTypeOptions(Customizer.withDefaults())
            .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
        );
        return http.build();
    }
}
```

## Header Reference

| Header | Purpose | Value |
|--------|---------|-------|
| CSP | Prevent XSS | Content-Security-Policy policy |
| X-Content-Type-Options | Prevent MIME sniffing | nosniff |
| X-Frame-Options | Prevent clickjacking | DENY or SAMEORIGIN |
| HSTS | Force HTTPS | max-age=31536000 |
| X-XSS-Protection | XSS filter | 1; mode=block |
| Referrer-Policy | Control referrer | strict-origin-when-cross-origin |
| Permissions-Policy | Feature policy | camera=(), microphone=() |

## Best Practices

1. Implement all security headers
2. Use strict CSP policies
3. Enable HSTS with long max-age
4. Prevent framing with DENY
5. Test headers with security scanners
6. Monitor for header misconfigurations
7. Use HTTPS only
8. Regularly review and update policies
