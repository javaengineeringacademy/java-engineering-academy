# Cross-Site Request Forgery (CSRF)

## Overview

CSRF forces authenticated users to submit requests to web applications they're currently authenticated against.

## How It Works

```html
<!-- Malicious page -->
<img src="https://bank.example.com/transfer?to=attacker&amount=1000">
<!-- Or -->
<form action="https://bank.example.com/transfer" method="POST">
    <input type="hidden" name="to" value="attacker">
    <input type="hidden" name="amount" value="1000">
</form>
```

## Prevention

### Synchronizer Token Pattern
```java
@Controller
public class CsrfController {
    
    @GetMapping("/form")
    public String form(HttpServletRequest request, Model model) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", token);
        return "form";
    }
}
```

```html
<!-- Include CSRF token in forms -->
<form method="POST" action="/transfer">
    <input type="hidden" name="_csrf" th:value="${_csrf.token}">
    <!-- other fields -->
</form>
```

### Double Submit Cookie
```java
@Component
public class DoubleSubmitFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletRequest request = (HttpServletRequest) req;
        
        String cookieToken = getCookieValue(request, "csrf-token");
        String headerToken = request.getHeader("X-CSRF-TOKEN");
        
        if (cookieToken == null || !cookieToken.equals(headerToken)) {
            throw new SecurityException("CSRF validation failed");
        }
        
        chain.doFilter(req, res);
    }
}
```

### SameSite Cookie
```java
@Component
public class SameSiteCookieFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) res;
        
        Cookie cookie = new Cookie("SESSION", sessionId);
        cookie.setAttribute("SameSite", "Strict");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        
        chain.doFilter(req, res);
    }
}
```

## Spring Security CSRF

```java
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringRequestMatchers("/api/**")  // For API with JWT
        );
        return http.build();
    }
}
```

## Best Practices

1. Enable CSRF protection for state-changing operations
2. Use SameSite cookies
3. Validate CSRF tokens on server
4. Use synchronizer token pattern
5. Invalidate tokens on logout
6. Use HTTPS only
7. Implement proper CORS policies
8. Test CSRF defenses
