# Cross-Site Scripting (XSS)

## Overview

XSS allows attackers to inject malicious scripts into web pages viewed by other users.

## Types

### Reflected XSS
```java
// Vulnerable
@GetMapping("/search")
public String search(@RequestParam String q) {
    return "Search results for: " + q;  // Direct output
}

// Safe
@GetMapping("/search")
public String search(@RequestParam String q, Model model) {
    model.addAttribute("query", q);  // Thymeleaf auto-escapes
    return "search";
}
```

### Stored XSS
```java
// Store in database (safe if output escaped)
comment.setContent(userInput);  // Store as-is

// Output with escaping
// Thymeleaf: <span th:text="${comment.content}"></span>
// JSP: <%= HtmlUtils.htmlEscape(comment.getContent()) %>
```

### DOM-based XSS
```javascript
// Vulnerable
element.innerHTML = location.hash;

// Safe
element.textContent = location.hash;
```

## Prevention

### Spring Security Configuration
```java
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers
            .contentSecurityPolicy(csp -> csp
                .policyDirectives("default-src 'self'; script-src 'self'"))
        );
        return http.build();
    }
}
```

### Input Validation
```java
@Component
public class InputSanitizer {
    private static final Pattern SAFE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s.,!?-]+$");
    
    public String sanitize(String input) {
        if (input == null) return null;
        
        // Remove HTML tags
        String sanitized = input.replaceAll("<[^>]+>", "");
        
        // Encode special characters
        sanitized = StringEscapeUtils.escapeHtml4(sanitized);
        
        return sanitized;
    }
    
    public boolean isSafe(String input) {
        return SAFE_PATTERN.matcher(input).matches();
    }
}
```

### Output Encoding
```java
// JavaScript context
String safeJs = StringEscapeUtils.escapeEcmaScript(userInput);

// HTML context
String safeHtml = StringEscapeUtils.escapeHtml4(userInput);

// URL context
String safeUrl = URLEncoder.encode(userInput, StandardCharsets.UTF_8);
```

## Content Security Policy

```java
// Strict CSP
Content-Security-Policy: default-src 'self'; script-src 'self' 'nonce-abc123'; style-src 'self' 'unsafe-inline'
```

## Best Practices

1. Encode all output
2. Validate all input
3. Use Content Security Policy
4. Use HTTPOnly cookies
5. Implement XSS filters
6. Use security headers
7. Sanitize rich text content
8. Test with XSS scanners
