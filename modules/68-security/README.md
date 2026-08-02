# Module 68: Java Security

## Overview
Java security covers authentication, authorization, encryption, and protection against common vulnerabilities. It includes secure coding practices, security frameworks, and compliance requirements.

## Learning Objectives
- Implement authentication and authorization
- Apply encryption techniques
- Prevent common vulnerabilities
- Use security frameworks
- Follow secure coding practices

## Prerequisites
- Java fundamentals
- Web application basics
- Cryptography concepts

## Why This Concept Exists
Security vulnerabilities lead to:
- Data breaches
- Financial loss
- Reputation damage
- Legal consequences

Security provides:
- Data protection
- User trust
- Compliance
- Risk reduction

## Problem Statement
How do you secure Java applications against common threats?

## Security Practices

### OWASP Top 10

| Risk | Prevention |
|------|------------|
| Injection | Input validation, parameterized queries |
| Broken Auth | Multi-factor auth, session management |
| Sensitive Data | Encryption, secure storage |
| XXE | Disable external entities |
| Broken Access | Proper authorization |
| Security Misconfiguration | Hardening, updates |
| XSS | Output encoding |
| Insecure Deserialization | Validation |
| Known Vulnerabilities | Patching |
| Insufficient Logging | Audit logging |

### Authentication Methods

| Method | Use Case |
|--------|----------|
| JWT | Stateless tokens |
| OAuth2 | Third-party auth |
| SSO | Single sign-on |
| MFA | Multi-factor |

## Enterprise Example

```java
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            );
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();
        
        return new InMemoryUserDetailsManager(user);
    }
}

// Secure coding example
@Service
public class SecureService {
    private final PasswordEncoder passwordEncoder;
    
    // Input validation
    public User createUser(String username, String password) {
        validateInput(username, password);
        
        return User.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .build();
    }
    
    private void validateInput(String username, String password) {
        if (username == null || username.length() < 3) {
            throw new ValidationException("Invalid username");
        }
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password too short");
        }
    }
    
    // SQL injection prevention (using JPA)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username); // Parameterized
    }
    
    // XSS prevention
    public String sanitize(String input) {
        return HtmlUtils.htmlEscape(input);
    }
}
```

## Performance Considerations
- Use efficient encryption algorithms
- Cache authentication tokens
- Minimize security overhead
- Use connection pooling for secure connections

## Best Practices
1. Validate all inputs
2. Use parameterized queries
3. Encrypt sensitive data
4. Implement least privilege
5. Log security events

## Interview Questions

### Q1: What is the difference between authentication and authorization?
**Answer:** Authentication verifies identity, authorization grants permissions.

### Q2: What is JWT?
**Answer:** JSON Web Token for stateless authentication.

### Q3: What is SQL injection?
**Answer:** Attack using malicious SQL in user input.

### Q4: What is XSS?
**Answer:** Cross-Site Scripting attack injecting malicious scripts.

### Q5: What is encryption at rest?
**Answer:** Encrypting stored data to prevent unauthorized access.

## Summary
Security is essential for protecting applications and data. Implement defense in depth.

## References
- OWASP Top 10
- Spring Security Documentation
- Java Security Guide
