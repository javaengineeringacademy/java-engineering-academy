# OAuth 2.0

## Comprehensive Guide to OAuth 2.0

OAuth 2.0 is an authorization framework that enables third-party applications to obtain limited access to HTTP services. This guide covers flows, PKCE, and implementation.

---

## Table of Contents

1. [OAuth 2.0 Overview](#oauth-20-overview)
2. [Grant Types](#grant-types)
3. [Authorization Code Flow](#authorization-code-flow)
4. [PKCE Extension](#pkce-extension)
5. [Client Credentials](#client-credentials)
6. [Implementation](#implementation)
7. [Best Practices](#best-practices)

---

## OAuth 2.0 Overview

### Roles

- **Resource Owner**: User who owns the data
- **Client**: Application requesting access
- **Authorization Server**: Issues tokens
- **Resource Server**: Hosts protected resources

### Scopes

```
read        - Read access to resources
write       - Write access to resources
admin       - Administrative access
profile     - Access to user profile
email       - Access to email
```

---

## Grant Types

### Authorization Code Flow

```java
// Step 1: Redirect to authorization server
@GetMapping("/login")
public void login(HttpServletResponse response) throws IOException {
    String url = authorizationEndpoint
        + "?response_type=code"
        + "&client_id=" + clientId
        + "&redirect_uri=" + redirectUri
        + "&scope=read write"
        + "&state=" + generateState();

    response.sendRedirect(url);
}

// Step 2: Handle callback
@GetMapping("/callback")
public ResponseEntity<TokenResponse> callback(
        @RequestParam String code,
        @RequestParam String state) {

    validateState(state);

    // Step 3: Exchange code for token
    TokenResponse token = tokenClient.exchangeCode(
        code, clientId, clientSecret, redirectUri);

    return ResponseEntity.ok(token);
}
```

### Client Credentials Flow

```java
// For machine-to-machine communication
@Service
public class TokenService {

    public TokenResponse getAccessToken() {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("grant_type", "client_credentials");
        request.add("client_id", clientId);
        request.add("client_secret", clientSecret);
        request.add("scope", "read write");

        return restTemplate.postForObject(tokenEndpoint, request,
            TokenResponse.class);
    }
}
```

### Token Response

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "token_type": "Bearer",
    "expires_in": 3600,
    "refresh_token": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...",
    "scope": "read write"
}
```

---

## PKCE Extension

### Code Verifier and Challenge

```java
@Component
public class PkceService {

    public PkceData generatePkceData() {
        // Generate code verifier (43-128 characters)
        String codeVerifier = generateRandomString(128);

        // Generate code challenge (SHA-256 hash)
        String codeChallenge = Base64.getUrlEncoder().encodeToString(
            MessageDigest.getInstance("SHA-256")
                .digest(codeVerifier.getBytes(StandardCharsets.UTF_8))
        );

        return new PkceData(codeVerifier, codeChallenge);
    }

    private String generateRandomString(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding()
            .encodeToString(bytes);
    }
}
```

### Authorization Request with PKCE

```java
@GetMapping("/login")
public void login(HttpServletResponse response) throws IOException {
    PkceData pkce = pkceService.generatePkceData();

    // Store code verifier in session
    session.setAttribute("code_verifier", pkce.getCodeVerifier());

    String url = authorizationEndpoint
        + "?response_type=code"
        + "&client_id=" + clientId
        + "&redirect_uri=" + redirectUri
        + "&scope=read"
        + "&state=" + generateState()
        + "&code_challenge=" + pkce.getCodeChallenge()
        + "&code_challenge_method=S256";

    response.sendRedirect(url);
}

// Token exchange with code verifier
@PostMapping("/token")
public TokenResponse exchangeToken(
        @RequestParam String code,
        @RequestParam String codeVerifier) {

    MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
    request.add("grant_type", "authorization_code");
    request.add("code", code);
    request.add("redirect_uri", redirectUri);
    request.add("client_id", clientId);
    request.add("code_verifier", codeVerifier);

    return restTemplate.postForObject(tokenEndpoint, request,
        TokenResponse.class);
}
```

---

## Client Credentials

### Implementation

```java
@Service
public class ClientCredentialsService {

    private final RestTemplate restTemplate;
    private final TokenRepository tokenRepository;

    public TokenResponse getAccessToken(String clientId, String clientSecret) {
        // Check cache
        Token cached = tokenRepository.findByClientId(clientId);
        if (cached != null && !cached.isExpired()) {
            return TokenResponse.fromToken(cached);
        }

        // Request new token
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("grant_type", "client_credentials");
        request.add("client_id", clientId);
        request.add("client_secret", clientSecret);

        TokenResponse response = restTemplate.postForObject(
            tokenEndpoint, request, TokenResponse.class);

        // Cache token
        tokenRepository.save(Token.fromResponse(clientId, response));

        return response;
    }
}
```

---

## Implementation

### Spring Security Configuration

```java
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient("client-app")
            .secret(passwordEncoder.encode("secret"))
            .authorizedGrantTypes("authorization_code", "refresh_token")
            .scopes("read", "write")
            .redirectUris("http://localhost:8080/callback")
            .and()
            .withClient("service-app")
            .secret(passwordEncoder.encode("secret"))
            .authorizedGrantTypes("client_credentials")
            .scopes("read", "write");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(jwtTokenStore())
            .accessTokenConverter(accessTokenConverter());
    }
}
```

### Resource Server

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/api/public/**").permitAll()
            .antMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
            .anyRequest().authenticated()
            .and()
            .cors()
            .and()
            .csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("my-api");
    }
}
```

---

## Best Practices

### 1. Use PKCE for Public Clients

```java
// Always use PKCE for browser-based and mobile apps
String codeVerifier = generateCodeVerifier();
String codeChallenge = generateCodeChallenge(codeVerifier);

// Store verifier securely
session.setAttribute("code_verifier", codeVerifier);
```

### 2. Validate Tokens

```java
@GetMapping("/protected")
public ResponseEntity<String> protectedEndpoint(
        @AuthenticationPrincipal Jwt jwt) {

    // Token is automatically validated by Spring Security
    String userId = jwt.getSubject();
    String email = jwt.getClaimAsString("email");

    return ResponseEntity.ok("Hello " + email);
}
```

### 3. Handle Token Expiration

```java
@Component
public class TokenRefreshInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String token = extractToken(request);

        if (isTokenExpiringSoon(token)) {
            String refreshToken = getRefreshToken(token);
            String newToken = refreshToken(refreshToken);

            response.setHeader("X-New-Token", newToken);
        }

        return true;
    }
}
```

### 4. Secure Token Storage

```java
// Never store tokens in localStorage
// Use httpOnly cookies instead
@Bean
public ResponseCookie accessTokenCookie(String token) {
    return ResponseCookie.from("access_token", token)
        .httpOnly(true)
        .secure(true)
        .sameSite("Strict")
        .path("/")
        .maxAge(Duration.ofHours(1))
        .build();
}
```

### 5. Implement Token Revocation

```java
@PostMapping("/revoke")
public ResponseEntity<Void> revokeToken(
        @RequestParam String token) {
    tokenStore.removeAccessToken(token);
    return ResponseEntity.ok().build();
}
```

---

## Further Reading

- [OAuth 2.0 Specification](https://datatracker.ietf.org/doc/html/rfc6749)
- [PKCE Extension](https://datatracker.ietf.org/doc/html/rfc7636)
- [OAuth 2.0 Security Best Current Practice](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-security-topics)
- [Spring Security OAuth](https://spring.io/projects/spring-security-oauth)
