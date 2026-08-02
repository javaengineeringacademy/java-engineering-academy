package academy.javaengineering.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JwtAuthenticationExample {

    private final String secret;
    private final long expirationMs;
    private final Map<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    public JwtAuthenticationExample(String secret, long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    public String generateToken(String username, Map<String, Object> claims) {
        String header = createHeader();
        String payload = createPayload(username, claims);
        String signature = createSignature(header + "." + payload);

        return header + "." + payload + "." + signature;
    }

    private String createHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        return base64Encode(mapToString(header));
    }

    private String createPayload(String username, Map<String, Object> claims) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", username);
        payload.put("iat", System.currentTimeMillis());
        payload.put("exp", System.currentTimeMillis() + expirationMs);
        payload.put("jti", UUID.randomUUID().toString());

        if (claims != null) {
            payload.putAll(claims);
        }

        return base64Encode(mapToString(payload));
    }

    private String createSignature(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((data + secret).getBytes(StandardCharsets.UTF_8));
            return base64Encode(bytesToHex(hash));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public JwtClaims validateToken(String token) {
        if (token == null || !token.contains(".")) {
            throw new IllegalArgumentException("Invalid token format");
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token structure");
        }

        String expectedSignature = createSignature(parts[0] + "." + parts[1]);
        if (!expectedSignature.equals(parts[2])) {
            throw new SecurityException("Invalid token signature");
        }

        String payloadJson = base64Decode(parts[1]);
        JwtClaims claims = parseClaims(payloadJson);

        if (claims.getExpiration() < System.currentTimeMillis()) {
            throw new SecurityException("Token expired");
        }

        return claims;
    }

    public String generateRefreshToken(String username) {
        String refreshToken = UUID.randomUUID().toString();
        refreshTokenStore.put(refreshToken, username);
        return refreshToken;
    }

    public String refreshAccessToken(String refreshToken) {
        String username = refreshTokenStore.get(refreshToken);
        if (username == null) {
            throw new SecurityException("Invalid refresh token");
        }

        refreshTokenStore.remove(refreshToken);
        return generateToken(username, null);
    }

    public void revokeRefreshToken(String refreshToken) {
        refreshTokenStore.remove(refreshToken);
    }

    private String mapToString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                sb.append("\"").append(entry.getValue()).append("\"");
            } else {
                sb.append(entry.getValue());
            }
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    private JwtClaims parseClaims(String json) {
        JwtClaims claims = new JwtClaims();
        String cleaned = json.replace("{", "").replace("}", "").replace("\"", "");

        for (String pair : cleaned.split(",")) {
            String[] kv = pair.split(":");
            if (kv.length == 2) {
                String key = kv[0].trim();
                String value = kv[1].trim();

                switch (key) {
                    case "sub":
                        claims.setSubject(value);
                        break;
                    case "iat":
                        claims.setIssuedAt(Long.parseLong(value));
                        break;
                    case "exp":
                        claims.setExpiration(Long.parseLong(value));
                        break;
                    case "jti":
                        claims.setId(value);
                        break;
                }
            }
        }

        return claims;
    }

    private String base64Encode(String data) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    private String base64Decode(String data) {
        return new String(Base64.getUrlDecoder().decode(data), StandardCharsets.UTF_8);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static class JwtClaims {
        private String subject;
        private long issuedAt;
        private long expiration;
        private String id;

        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        public long getIssuedAt() { return issuedAt; }
        public void setIssuedAt(long issuedAt) { this.issuedAt = issuedAt; }
        public long getExpiration() { return expiration; }
        public void setExpiration(long expiration) { this.expiration = expiration; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }

    public static void main(String[] args) {
        JwtAuthenticationExample jwt = new JwtAuthenticationExample(
                "my-secret-key-12345", 3600000);

        System.out.println("=== JWT Authentication Demo ===\n");

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", java.util.List.of("USER", "ADMIN"));

        String token = jwt.generateToken("john.doe", claims);
        System.out.println("Generated JWT:");
        System.out.println(token);

        System.out.println("\n--- Validating token ---");
        try {
            JwtClaims validated = jwt.validateToken(token);
            System.out.println("Token valid!");
            System.out.println("Subject: " + validated.getSubject());
            System.out.println("Issued at: " + new Date(validated.getIssuedAt()));
            System.out.println("Expires at: " + new Date(validated.getExpiration()));
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
        }

        System.out.println("\n--- Refresh Token Flow ---");
        String refreshToken = jwt.generateRefreshToken("john.doe");
        System.out.println("Refresh token: " + refreshToken);

        String newAccessToken = jwt.refreshAccessToken(refreshToken);
        System.out.println("New access token generated!");

        System.out.println("\n--- Invalid token test ---");
        try {
            jwt.validateToken("invalid.token.here");
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }
}
