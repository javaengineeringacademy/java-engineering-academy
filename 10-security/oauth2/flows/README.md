# OAuth 2.0 Flows

## Overview

OAuth 2.0 is an authorization framework that enables third-party applications to obtain limited access to HTTP services.

## Grant Types

### Authorization Code Flow
```
Client -> Auth Server -> Resource Owner -> Client -> Auth Server -> Client
         (redirect)      (approve)        (code)   (exchange)
```

```java
// Authorization request
String authUrl = UriComponentsBuilder.fromUriString("https://auth.example.com/authorize")
    .queryParam("response_type", "code")
    .queryParam("client_id", clientId)
    .queryParam("redirect_uri", redirectUri)
    .queryParam("scope", "read write")
    .queryParam("state", state)
    .build().toUriString();

// Token exchange
Map<String, String> params = Map.of(
    "grant_type", "authorization_code",
    "code", authorizationCode,
    "redirect_uri", redirectUri,
    "client_id", clientId,
    "client_secret", clientSecret
);
```

### PKCE Flow (Public Clients)
```java
// Generate code verifier and challenge
String codeVerifier = generateRandomString(128);
String codeChallenge = Base64.getUrlEncoder().encodeToString(
    MessageDigest.getInstance("SHA-256")
        .digest(codeVerifier.getBytes())
);

// Authorization request
String authUrl = UriComponentsBuilder.fromUriString(authEndpoint)
    .queryParam("response_type", "code")
    .queryParam("client_id", clientId)
    .queryParam("redirect_uri", redirectUri)
    .queryParam("scope", "read")
    .queryParam("code_challenge", codeChallenge)
    .queryParam("code_challenge_method", "S256")
    .build().toUriString();

// Token exchange includes code_verifier
params.put("code_verifier", codeVerifier);
```

### Client Credentials Flow
```java
// For machine-to-machine authentication
Map<String, String> params = Map.of(
    "grant_type", "client_credentials",
    "client_id", clientId,
    "client_secret", clientSecret,
    "scope", "api:read"
);

// Send POST to token endpoint
HttpResponse response = httpClient.post(tokenEndpoint, params);
```

### Device Authorization Flow
```java
// For input-constrained devices
DeviceCodeResponse deviceCode = requestDeviceCode();

// Display to user
System.out.println("Go to: " + deviceCode.getVerificationUri());
System.out.println("Enter code: " + deviceCode.getUserCode());

// Poll for token
while (!tokenObtained) {
    Thread.sleep(deviceCode.getInterval() * 1000);
    token = pollForToken(deviceCode.getDeviceCode());
}
```

## Token Refresh

```java
public TokenResponse refreshAccessToken(String refreshToken) {
    Map<String, String> params = Map.of(
        "grant_type", "refresh_token",
        "refresh_token", refreshToken,
        "client_id", clientId,
        "client_secret", clientSecret
    );
    
    return exchangeToken(params);
}
```

## Scope Management

```java
// Define scopes
public enum Scope {
    READ("read"),
    WRITE("write"),
    ADMIN("admin");
    
    private final String value;
}

// Validate scope
public boolean hasScope(String token, String requiredScope) {
    Claims claims = validateToken(token);
    String scope = claims.get("scope", String.class);
    return scope.contains(requiredScope);
}
```

## Best Practices

1. Use PKCE for public clients
2. Store credentials securely
3. Validate redirect URIs
4. Use short-lived access tokens
5. Implement refresh token rotation
6. Validate state parameter
7. Use HTTPS only
8. Implement token revocation
