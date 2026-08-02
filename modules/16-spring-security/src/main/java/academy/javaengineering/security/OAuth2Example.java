package academy.javaengineering.security;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OAuth2Example {

    private final Map<String, OAuth2Client> registeredClients = new ConcurrentHashMap<>();
    private final Map<String, AuthorizationCode> authorizationCodes = new ConcurrentHashMap<>();
    private final Map<String, OAuth2Token> accessTokens = new ConcurrentHashMap<>();

    public void registerClient(String clientId, String clientSecret, String redirectUri) {
        OAuth2Client client = new OAuth2Client(clientId, clientSecret, redirectUri);
        registeredClients.put(clientId, client);
        System.out.println("Client registered: " + clientId);
    }

    public String generateAuthorizationCode(String clientId, String scope) {
        if (!registeredClients.containsKey(clientId)) {
            throw new IllegalArgumentException("Unknown client: " + clientId);
        }

        String code = UUID.randomUUID().toString();
        AuthorizationCode authCode = new AuthorizationCode(code, clientId, scope, System.currentTimeMillis());
        authorizationCodes.put(code, authCode);

        System.out.println("Authorization code generated for client: " + clientId);
        return code;
    }

    public OAuth2Token exchangeCodeForToken(String clientId, String clientSecret, String code) {
        OAuth2Client client = registeredClients.get(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Unknown client: " + clientId);
        }

        if (!client.getClientSecret().equals(clientSecret)) {
            throw new SecurityException("Invalid client secret");
        }

        AuthorizationCode authCode = authorizationCodes.remove(code);
        if (authCode == null) {
            throw new IllegalArgumentException("Invalid authorization code");
        }

        if (System.currentTimeMillis() - authCode.getCreatedAt() > 600000) {
            throw new SecurityException("Authorization code expired");
        }

        OAuth2Token token = new OAuth2Token(
                UUID.randomUUID().toString(),
                authCode.getClientId(),
                authCode.getScope(),
                System.currentTimeMillis() + 3600000
        );

        accessTokens.put(token.getAccessToken(), token);
        System.out.println("Access token generated for client: " + clientId);
        return token;
    }

    public boolean validateToken(String accessToken) {
        OAuth2Token token = accessTokens.get(accessToken);
        if (token == null) {
            return false;
        }
        return token.getExpiresAt() > System.currentTimeMillis();
    }

    public OAuth2Token introspectToken(String accessToken) {
        OAuth2Token token = accessTokens.get(accessToken);
        if (token == null || token.getExpiresAt() < System.currentTimeMillis()) {
            return null;
        }
        return token;
    }

    public void revokeToken(String accessToken) {
        accessTokens.remove(accessToken);
        System.out.println("Token revoked: " + accessToken.substring(0, 10) + "...");
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        OAuth2Token token = accessTokens.get(accessToken);
        if (token == null || token.getExpiresAt() < System.currentTimeMillis()) {
            return null;
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("sub", token.getClientId());
        userInfo.put("email", "user@example.com");
        userInfo.put("name", "John Doe");
        userInfo.put("roles", new String[]{"USER", "ADMIN"});
        return userInfo;
    }

    public static class OAuth2Client {
        private final String clientId;
        private final String clientSecret;
        private final String redirectUri;

        public OAuth2Client(String clientId, String clientSecret, String redirectUri) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.redirectUri = redirectUri;
        }

        public String getClientId() { return clientId; }
        public String getClientSecret() { return clientSecret; }
        public String getRedirectUri() { return redirectUri; }
    }

    public static class AuthorizationCode {
        private final String code;
        private final String clientId;
        private final String scope;
        private final long createdAt;

        public AuthorizationCode(String code, String clientId, String scope, long createdAt) {
            this.code = code;
            this.clientId = clientId;
            this.scope = scope;
            this.createdAt = createdAt;
        }

        public String getCode() { return code; }
        public String getClientId() { return clientId; }
        public String getScope() { return scope; }
        public long getCreatedAt() { return createdAt; }
    }

    public static class OAuth2Token {
        private final String accessToken;
        private final String clientId;
        private final String scope;
        private final long expiresAt;

        public OAuth2Token(String accessToken, String clientId, String scope, long expiresAt) {
            this.accessToken = accessToken;
            this.clientId = clientId;
            this.scope = scope;
            this.expiresAt = expiresAt;
        }

        public String getAccessToken() { return accessToken; }
        public String getClientId() { return clientId; }
        public String getScope() { return scope; }
        public long getExpiresAt() { return expiresAt; }
    }

    public static void main(String[] args) {
        OAuth2Example oauth2 = new OAuth2Example();

        System.out.println("=== OAuth2 Demo ===\n");

        oauth2.registerClient("my-app", "secret123", "http://localhost:8080/callback");

        System.out.println("\n--- Authorization Code Flow ---");
        String authCode = oauth2.generateAuthorizationCode("my-app", "read write");
        System.out.println("Authorization code: " + authCode);

        System.out.println("\n--- Token Exchange ---");
        OAuth2Token token = oauth2.exchangeCodeForToken("my-app", "secret123", authCode);
        System.out.println("Access token: " + token.getAccessToken());

        System.out.println("\n--- Token Validation ---");
        System.out.println("Token valid: " + oauth2.validateToken(token.getAccessToken()));

        System.out.println("\n--- Token Introspection ---");
        OAuth2Token introspected = oauth2.introspectToken(token.getAccessToken());
        System.out.println("Client: " + introspected.getClientId());
        System.out.println("Scope: " + introspected.getScope());

        System.out.println("\n--- User Info ---");
        Map<String, Object> userInfo = oauth2.getUserInfo(token.getAccessToken());
        System.out.println("User info: " + userInfo);

        System.out.println("\n--- Token Revocation ---");
        oauth2.revokeToken(token.getAccessToken());
        System.out.println("Token valid after revocation: " + oauth2.validateToken(token.getAccessToken()));
    }
}
