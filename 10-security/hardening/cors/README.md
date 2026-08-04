# CORS Configuration

## Overview

Cross-Origin Resource Sharing (CORS) controls which origins can access your resources.

## Configuration

### Spring Security
```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Specific origins (never use * in production)
        configuration.setAllowedOrigins(List.of(
            "https://app.example.com",
            "https://admin.example.com"
        ));
        
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
```

### WebFlux
```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("https://app.example.com"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }
}
```

### Nginx
```nginx
# Enable CORS
location /api/ {
    if ($request_method = 'OPTIONS') {
        add_header 'Access-Control-Allow-Origin' 'https://app.example.com';
        add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE';
        add_header 'Access-Control-Allow-Headers' 'Content-Type, Authorization';
        add_header 'Access-Control-Max-Age' 3600;
        return 204;
    }
    
    add_header 'Access-Control-Allow-Origin' 'https://app.example.com' always;
}
```

## Headers

| Header | Purpose |
|--------|---------|
| Access-Control-Allow-Origin | Allowed origins |
| Access-Control-Allow-Methods | Allowed HTTP methods |
| Access-Control-Allow-Headers | Allowed headers |
| Access-Control-Allow-Credentials | Allow cookies |
| Access-Control-Max-Age | Preflight cache time |
| Access-Control-Expose-Headers | Headers to expose |

## Preflight Requests

```java
// Handle OPTIONS requests
@CrossOrigin(origins = "https://app.example.com", methods = {GET, POST})
@GetMapping("/api/data")
public ResponseEntity<Data> getData() {
    return ResponseEntity.ok(data);
}
```

## Best Practices

1. Never use `Access-Control-Allow-Origin: *` in production
2. Use specific allowed origins
3. Limit allowed methods
4. Use credentials carefully
5. Set appropriate max-age
6. Monitor CORS errors
7. Test with different origins
8. Document CORS policies
