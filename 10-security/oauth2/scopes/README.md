# OAuth 2.0 Scopes

## Overview

Scopes define the level of access that an OAuth 2.0 token grants to the client application.

## Scope Design

### Standard Scopes
| Scope | Description |
|-------|-------------|
| openid | OpenID Connect |
| profile | User profile info |
| email | User email |
| address | User address |
| phone | User phone |

### Custom Scopes
```java
// Define resource-specific scopes
public enum ApiScope {
    READ("api:read"),
    WRITE("api:write"),
    DELETE("api:delete"),
    ADMIN("api:admin");
    
    private final String value;
}

// Scope hierarchy
ScopeHierarchy scopeHierarchy = new ScopeHierarchy();
scopeHierarchy.addParent("api:admin", "api:read");
scopeHierarchy.addParent("api:admin", "api:write");
scopeHierarchy.addParent("api:admin", "api:delete");
```

## Resource Server Configuration

```java
@Configuration
public class ResourceServerConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize
            .requestMatchers(HttpMethod.GET, "/api/books/**").access("#oauth2.hasScope('api:read')")
            .requestMatchers(HttpMethod.POST, "/api/books/**").access("#oauth2.hasScope('api:write')")
            .requestMatchers(HttpMethod.DELETE, "/api/books/**").access("#oauth2.hasScope('api:delete')")
            .anyRequest().authenticated()
        );
        return http.build();
    }
}
```

## Scope Validation

```java
@Component
public class ScopeValidator {
    
    public boolean hasScope(String token, String requiredScope) {
        Claims claims = jwtValidator.validateToken(token);
        String scope = claims.get("scope", String.class);
        
        if (scope == null) return false;
        
        List<String> grantedScopes = Arrays.asList(scope.split(" "));
        return grantedScopes.contains(requiredScope);
    }
    
    public boolean hasAnyScope(String token, String... requiredScopes) {
        Claims claims = jwtValidator.validateToken(token);
        String scope = claims.get("scope", String.class);
        List<String> grantedScopes = Arrays.asList(scope.split(" "));
        
        return Arrays.stream(requiredScopes)
            .anyMatch(grantedScopes::contains);
    }
}
```

## Scope Best Practices

1. Use fine-grained scopes
2. Follow principle of least privilege
3. Document scope meanings
4. Implement scope validation
5. Use scope hierarchies when appropriate
6. Log scope usage
7. Implement scope revocation
8. Use consistent naming conventions
