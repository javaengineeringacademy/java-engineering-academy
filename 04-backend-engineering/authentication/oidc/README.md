# OpenID Connect (OIDC)

## Comprehensive Guide to OpenID Connect

OpenID Connect (OIDC) is an identity layer on top of OAuth 2.0. This guide covers ID tokens, authentication flows, and implementation.

---

## Table of Contents

1. [OIDC Overview](#oidc-overview)
2. [ID Tokens](#id-tokens)
3. [Authentication Flows](#authentication-flows)
4. [UserInfo Endpoint](#userinfo-endpoint)
5. [Implementation](#implementation)
6. [Best Practices](#best-practices)

---

## OIDC Overview

### OIDC vs OAuth 2.0

| Feature | OAuth 2.0 | OIDC |
|---------|-----------|------|
| Purpose | Authorization | Authentication + Authorization |
| Token | Access Token | Access Token + ID Token |
| User Info | No standard | UserInfo endpoint |
| Discovery | No | Well-known endpoint |
| Scope | Custom | openid, profile, email |

### Discovery Document

```json
{
    "issuer": "https://auth.example.com",
    "authorization_endpoint": "https://auth.example.com/authorize",
    "token_endpoint": "https://auth.example.com/token",
    "userinfo_endpoint": "https://auth.example.com/userinfo",
    "jwks_uri": "https://auth.example.com/.well-known/jwks.json",
    "revocation_endpoint": "https://auth.example.com/revoke",
    "end_session_endpoint": "https://auth.example.com/logout",
    "scopes_supported": ["openid", "profile", "email"],
    "response_types_supported": ["code", "id_token", "token id_token"],
    "grant_types_supported": ["authorization_code", "refresh_token"],
    "subject_types_supported": ["public"],
    "id_token_signing_alg_values_supported": ["RS256"]
}
```

---

## ID Tokens

### ID Token Claims

```json
{
    "iss": "https://auth.example.com",
    "sub": "1234567890",
    "aud": "client-id-123",
    "exp": 1716242622,
    "iat": 1716239022,
    "auth_time": 1716239020,
    "nonce": "n-0S6_WzA2Mj",
    "at_hash": "MTIzNDU2Nzg5MDEyMzQ1Njc4OQ",
    "name": "John Doe",
    "given_name": "John",
    "family_name": "Doe",
    "email": "john@example.com",
    "email_verified": true,
    "picture": "https://example.com/john.jpg"
}
```

### Standard Scopes

```
openid          - Required for OIDC
profile         - name, family_name, given_name, etc.
email           - email, email_verified
address         - address claim
phone           - phone_number, phone_number_verified
```

---

## Authentication Flows

### Authorization Code Flow

```java
@GetMapping("/oidc/login")
public void oidcLogin(HttpServletResponse response) throws IOException {
    String state = generateState();
    String nonce = generateNonce();

    session.setAttribute("state", state);
    session.setAttribute("nonce", nonce);

    String url = UriComponentsBuilder.fromUriString(authorizationEndpoint)
        .queryParam("response_type", "code")
        .queryParam("client_id", clientId)
        .queryParam("redirect_uri", redirectUri)
        .queryParam("scope", "openid profile email")
        .queryParam("state", state)
        .queryParam("nonce", nonce)
        .toUriString();

    response.sendRedirect(url);
}

@GetMapping("/oidc/callback")
public ResponseEntity<OidcTokens> oidcCallback(
        @RequestParam String code,
        @RequestParam String state) {

    validateState(state);

    // Exchange code for tokens
    TokenResponse tokenResponse = tokenClient.exchangeCode(code);

    // Validate ID token
    OidcIdToken idToken = validateIdToken(tokenResponse.getIdToken());

    // Create session
    OidcUser user = createOidcUser(idToken);

    return ResponseEntity.ok(OidcTokens.from(tokenResponse, user));
}
```

### PKCE Extension

```java
@GetMapping("/oidc/login")
public void oidcLoginPkce(HttpServletResponse response) throws IOException {
    PkceData pkce = pkceService.generatePkce();

    session.setAttribute("code_verifier", pkce.getCodeVerifier());

    String url = UriComponentsBuilder.fromUriString(authorizationEndpoint)
        .queryParam("response_type", "code")
        .queryParam("client_id", clientId)
        .queryParam("redirect_uri", redirectUri)
        .queryParam("scope", "openid profile email")
        .queryParam("state", generateState())
        .queryParam("nonce", generateNonce())
        .queryParam("code_challenge", pkce.getCodeChallenge())
        .queryParam("code_challenge_method", "S256")
        .toUriString();

    response.sendRedirect(url);
}
```

---

## UserInfo Endpoint

### UserInfo Request

```java
@Service
public class UserInfoService {

    private final RestTemplate restTemplate;

    public UserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<UserInfo> response = restTemplate.exchange(
            userInfoEndpoint,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            UserInfo.class);

        return response.getBody();
    }
}
```

### UserInfo Response

```json
{
    "sub": "1234567890",
    "name": "John Doe",
    "given_name": "John",
    "family_name": "Doe",
    "email": "john@example.com",
    "email_verified": true,
    "picture": "https://example.com/john.jpg",
    "locale": "en-US",
    "updated_at": 1716239022
}
```

---

## Implementation

### Spring Security OIDC

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorization ->
                    authorization
                        .baseUri("/oidc/authorize")
                        .authorizationRequestRepository(
                            cookieAuthorizationRequestRepository()))
                .redirectionEndpoint(redirection ->
                    redirection
                        .baseUri("/oidc/callback"))
                .userInfoEndpoint(userInfo ->
                    userInfo
                        .oidcUserService(oidcUserService())
                        .userAuthoritiesMapper(authoritiesMapper()))
                .defaultSuccessUrl("/dashboard")
                .failureUrl("/login?error=true"))
            .logout(logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(oidcLogoutSuccessHandler()));

        return http.build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService delegate = new OidcUserService();

        return userRequest -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);

            // Custom user mapping
            AppUser appUser = userRepository
                .findByExternalId(oidcUser.getSubject())
                .orElseGet(() -> createAppUser(oidcUser));

            return new DefaultOidcUser(
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                userRequest.getIdToken(),
                oidcUser.getUserInfo(),
                "sub");
        };
    }
}
```

---

## Best Practices

### 1. Validate ID Tokens

```java
public OidcIdToken validateIdToken(String idTokenString) {
    OidcIdToken idToken = OidcIdToken.parse(idTokenString);

    // Validate signature
    RSAKey rsaKey = jwkSet.getKey(idToken.getHeader().getKeyId());
    if (rsaKey == null) {
        throw new InvalidIdTokenException("Invalid key ID");
    }

    SignedJWT signedJWT = SignedJWT.parse(idTokenString);
    JWSVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());
    if (!signedJWT.verify(verifier)) {
        throw new InvalidIdTokenException("Invalid signature");
    }

    // Validate claims
    JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
    if (claims.getExpirationTime().before(new Date())) {
        throw new InvalidIdTokenException("Token expired");
    }

    if (!clientId.equals(claims.getAudience().get(0))) {
        throw new InvalidIdTokenException("Invalid audience");
    }

    if (!issuer.equals(claims.getIssuer())) {
        throw new InvalidIdTokenException("Invalid issuer");
    }

    return idToken;
}
```

### 2. Validate Nonce

```java
public void validateNonce(OidcIdToken idToken) {
    String tokenNonce = idToken.getNonce();
    String sessionNonce = session.getAttribute("nonce");

    if (!tokenNonce.equals(sessionNonce)) {
        throw new InvalidNonceException("Nonce mismatch");
    }

    session.removeAttribute("nonce");
}
```

### 3. Use PKCE

```java
// Always use PKCE for public clients
PkceData pkce = pkceService.generatePkce();

// Store code verifier securely
session.setAttribute("code_verifier", pkce.getCodeVerifier());
```

### 4. Implement Logout

```java
@GetMapping("/logout")
public void logout(HttpServletRequest request,
                   HttpServletResponse response) throws IOException {
    // Clear local session
    request.getSession().invalidate();

    // Build logout URL
    String logoutUrl = UriComponentsBuilder
        .fromUriString(endSessionEndpoint)
        .queryParam("id_token_hint", idToken)
        .queryParam("post_logout_redirect_uri", postLogoutRedirectUri)
        .toUriString();

    response.sendRedirect(logoutUrl);
}
```

### 5. Handle Token Refresh

```java
public OidcTokens refreshToken(String refreshToken) {
    OidcRefreshToken token = new OidcRefreshToken(refreshToken);

    OidcUserRequest userRequest = new OidcUserRequest(
        clientRegistration, accessToken, token);

    OidcUser newTokens = oidcUserService().loadUser(userRequest);

    return OidcTokens.builder()
        .idToken(newTokens.getIdToken().getTokenValue())
        .accessToken(newTokens.getAccessToken().getTokenValue())
        .refreshToken(refreshToken)
        .build();
}
```

---

## Further Reading

- [OpenID Connect Specification](https://openid.net/connect/)
- [OIDC Core](https://openid.net/specs/openid-connect-core-1_0.html)
- [Spring Security OIDC](https://spring.io/guides/gs/securing-web/)
- [OIDC for Identity Assurance](https://openid.net/specs/openid-connect-4-identity-assurance-1_0.html)
