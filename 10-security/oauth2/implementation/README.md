# OAuth 2.0 Implementation

## Overview

Guide to implementing OAuth 2.0 authorization server and resource server.

## Authorization Server

```java
@Configuration
public class AuthorizationServerConfig {
    
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("client-app")
            .clientSecret("{bcrypt}$2a$10$...")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:8080/callback")
            .scope(OidcScopes.OPENID)
            .scope("read")
            .scope("write")
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofHours(1))
                .refreshTokenTimeToLive(Duration.ofDays(7))
                .build())
            .build();
        
        return new InMemoryRegisteredClientRepository(client);
    }
    
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            if (context.getTokenType() == OidcParameterNames.ID_TOKEN) {
                context.getClaims().claims(claims -> {
                    claims.put("custom_claim", "value");
                });
            }
            if (context.getTokenType().getValue().equals("access_token")) {
                context.getClaims().claims(claims -> {
                    claims.put("roles", context.getPrincipal().getAuthorities());
                });
            }
        };
    }
}
```

## Resource Server

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig {
    
    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );
        return http.build();
    }
    
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}
```

## Token Management

```java
@Service
public class TokenManagementService {
    
    public void revokeToken(String token) {
        // Add to revocation list
        revocationList.add(token);
    }
    
    public boolean isTokenRevoked(String token) {
        return revocationList.contains(token);
    }
    
    public void rotateRefreshToken(String oldRefreshToken) {
        // Revoke old token
        revokeToken(oldRefreshToken);
        
        // Issue new token pair
        return generateTokenPair(oldRefreshToken);
    }
}
```

## Best Practices

1. Use Spring Authorization Server
2. Implement PKCE for public clients
3. Validate all token claims
4. Use short token lifetimes
5. Implement token revocation
6. Log authorization events
7. Use secure credential storage
8. Implement rate limiting
