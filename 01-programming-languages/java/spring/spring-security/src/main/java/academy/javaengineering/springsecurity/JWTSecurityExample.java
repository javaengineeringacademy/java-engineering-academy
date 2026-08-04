package academy.javaengineering.springsecurity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Demonstrates JWT token concepts for stateless Spring Security authentication
 * including token generation, validation, and filter chain configuration.
 */
public class JWTSecurityExample {

    // JWT header structure
    public record JWTHeader(String algorithm, String type) {
        public JWTHeader {
            Objects.requireNonNull(algorithm, "Algorithm cannot be null");
            Objects.requireNonNull(type, "Type cannot be null");
        }

        public String toBase64() {
            String json = "{\"alg\":\"" + algorithm + "\",\"typ\":\"" + type + "\"}";
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(json.getBytes(StandardCharsets.UTF_8));
        }

        public static JWTHeader parse(String base64Header) {
            String json = new String(Base64.getUrlDecoder().decode(base64Header), StandardCharsets.UTF_8);
            String algorithm = extractJsonValue(json, "alg");
            String type = extractJsonValue(json, "typ");
            return new JWTHeader(algorithm, type);
        }

        private static String extractJsonValue(String json, String key) {
            String searchKey = "\"" + key + "\":\"";
            int start = json.indexOf(searchKey) + searchKey.length();
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        }
    }

    // JWT payload structure
    public record JWTPayload(
            String subject,
            String issuer,
            Date issuedAt,
            Date expiration,
            List<String> roles,
            Map<String, Object> claims
    ) {
        public JWTPayload {
            Objects.requireNonNull(subject, "Subject cannot be null");
            Objects.requireNonNull(issuer, "Issuer cannot be null");
            Objects.requireNonNull(issuedAt, "IssuedAt cannot be null");
            Objects.requireNonNull(expiration, "Expiration cannot be null");
            Objects.requireNonNull(roles, "Roles cannot be null");
            if (claims == null) claims = Map.of();
        }

        public boolean isExpired() {
            return new Date().after(expiration);
        }

        public String toBase64() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"sub\":\"").append(subject).append("\",");
            json.append("\"iss\":\"").append(issuer).append("\",");
            json.append("\"iat\":").append(issuedAt.getTime() / 1000).append(",");
            json.append("\"exp\":").append(expiration.getTime() / 1000).append(",");
            json.append("\"roles\":[");

            for (int i = 0; i < roles.size(); i++) {
                if (i > 0) json.append(",");
                json.append("\"").append(roles.get(i)).append("\"");
            }
            json.append("]");

            if (!claims.isEmpty()) {
                json.append(",");
                int count = 0;
                for (var entry : claims.entrySet()) {
                    if (count > 0) json.append(",");
                    json.append("\"").append(entry.getKey()).append("\":\"")
                            .append(entry.getValue()).append("\"");
                    count++;
                }
            }

            json.append("}");
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(json.toString().getBytes(StandardCharsets.UTF_8));
        }

        public static JWTPayload parse(String base64Payload) {
            String json = new String(Base64.getUrlDecoder().decode(base64Payload), StandardCharsets.UTF_8);

            String subject = extractJsonValue(json, "sub");
            String issuer = extractJsonValue(json, "iss");
            long iat = Long.parseLong(extractJsonLongValue(json, "iat"));
            long exp = Long.parseLong(extractJsonLongValue(json, "exp"));
            List<String> roles = extractJsonArray(json, "roles");

            return new JWTPayload(
                    subject,
                    issuer,
                    new Date(iat * 1000),
                    new Date(exp * 1000),
                    roles,
                    Map.of()
            );
        }

        private static String extractJsonValue(String json, String key) {
            String searchKey = "\"" + key + "\":\"";
            int start = json.indexOf(searchKey) + searchKey.length();
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        }

        private static String extractJsonLongValue(String json, String key) {
            String searchKey = "\"" + key + "\":";
            int start = json.indexOf(searchKey) + searchKey.length();
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            return json.substring(start, end).trim();
        }

        private static List<String> extractJsonArray(String json, String key) {
            String searchKey = "\"" + key + "\":[";
            int start = json.indexOf(searchKey) + searchKey.length();
            int end = json.indexOf("]", start);
            String arrayContent = json.substring(start, end);
            if (arrayContent.isEmpty()) return List.of();

            List<String> result = new ArrayList<>();
            String[] parts = arrayContent.split(",");
            for (String part : parts) {
                result.add(part.trim().replace("\"", ""));
            }
            return result;
        }
    }

    // JWT Token class
    public record JWTToken(String header, String payload, String signature) {
        public JWTToken {
            Objects.requireNonNull(header, "Header cannot be null");
            Objects.requireNonNull(payload, "Payload cannot be null");
            Objects.requireNonNull(signature, "Signature cannot be null");
        }

        public String serialize() {
            return header + "." + payload + "." + signature;
        }

        public static JWTToken parse(String token) {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid token format");
            }
            return new JWTToken(parts[0], parts[1], parts[2]);
        }

        public JWTPayload getPayload() {
            return JWTPayload.parse(payload);
        }
    }

    // JWT Token Provider
    public static class JWTTokenProvider {
        private final SecretKey secretKey;
        private final String issuer;
        private final long validityMinutes;

        public JWTTokenProvider(String secret, String issuer, long validityMinutes) {
            this.secretKey = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            this.issuer = issuer;
            this.validityMinutes = validityMinutes;
        }

        public JWTToken createToken(String subject, List<String> roles) {
            JWTHeader header = new JWTHeader("HS256", "JWT");

            Date now = new Date();
            Date expiry = Date.from(now.toInstant().plus(validityMinutes, ChronoUnit.MINUTES));
            JWTPayload payload = new JWTPayload(subject, issuer, now, expiry, roles, Map.of());

            String headerBase64 = header.toBase64();
            String payloadBase64 = payload.toBase64();
            String signature = computeSignature(headerBase64 + "." + payloadBase64);

            return new JWTToken(headerBase64, payloadBase64, signature);
        }

        public boolean validateToken(JWTToken token) {
            try {
                JWTPayload payload = token.getPayload();
                if (payload.isExpired()) {
                    return false;
                }

                String expectedSignature = computeSignature(token.header() + "." + token.payload());
                return MessageDigest.isEqual(
                        expectedSignature.getBytes(StandardCharsets.UTF_8),
                        token.signature().getBytes(StandardCharsets.UTF_8)
                );
            } catch (Exception e) {
                return false;
            }
        }

        public Authentication getAuthentication(JWTToken token) {
            JWTPayload payload = token.getPayload();
            List<GrantedAuthority> authorities = payload.roles().stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                    .toList();

            return new UsernamePasswordAuthenticationToken(
                    payload.subject(), null, authorities);
        }

        private String computeSignature(String data) {
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(secretKey);
                byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
                return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException("Failed to compute signature", e);
            }
        }
    }

    // JWT Token Store for managing tokens
    public static class JWTTokenStore {
        private final JWTTokenProvider provider;
        private final Map<String, JWTToken> tokenStore = new HashMap<>();

        public JWTTokenStore(JWTTokenProvider provider) {
            this.provider = provider;
        }

        public JWTToken generateToken(String username, List<String> roles) {
            JWTToken token = provider.createToken(username, roles);
            tokenStore.put(username, token);
            return token;
        }

        public boolean validateToken(String username, JWTToken token) {
            JWTToken storedToken = tokenStore.get(username);
            if (storedToken == null) {
                return false;
            }
            return provider.validateToken(token);
        }

        public void revokeToken(String username) {
            tokenStore.remove(username);
        }

        public boolean hasToken(String username) {
            return tokenStore.containsKey(username);
        }

        public Authentication authenticate(String username, JWTToken token) {
            if (!validateToken(username, token)) {
                return null;
            }
            return provider.getAuthentication(token);
        }
    }

    // JWT Filter simulation
    public static class JWTAuthenticationFilter {
        private static final String AUTHORIZATION_HEADER = "Authorization";
        private static final String BEARER_PREFIX = "Bearer ";

        private final JWTTokenProvider tokenProvider;
        private final JWTTokenStore tokenStore;

        public JWTAuthenticationFilter(JWTTokenProvider tokenProvider, JWTTokenStore tokenStore) {
            this.tokenProvider = tokenProvider;
            this.tokenStore = tokenStore;
        }

        public String extractToken(String authHeader) {
            if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
                return authHeader.substring(BEARER_PREFIX.length());
            }
            return null;
        }

        public Authentication attemptAuthentication(String authHeader) {
            String tokenString = extractToken(authHeader);
            if (tokenString == null) {
                return null;
            }

            JWTToken token = JWTToken.parse(tokenString);
            JWTPayload payload = token.getPayload();

            if (!tokenProvider.validateToken(token)) {
                return null;
            }

            return tokenProvider.getAuthentication(token);
        }

        public boolean requiresAuthentication(String authHeader) {
            return authHeader != null && authHeader.startsWith(BEARER_PREFIX);
        }
    }

    // Stateless session configuration
    public static class StatelessSessionConfig {
        private final String tokenHeader;
        private final String tokenPrefix;
        private final long tokenExpirationMinutes;

        public StatelessSessionConfig(String tokenHeader, String tokenPrefix, long tokenExpirationMinutes) {
            this.tokenHeader = tokenHeader;
            this.tokenPrefix = tokenPrefix;
            this.tokenExpirationMinutes = tokenExpirationMinutes;
        }

        public String getTokenHeader() {
            return tokenHeader;
        }

        public String getTokenPrefix() {
            return tokenPrefix;
        }

        public long getTokenExpirationMinutes() {
            return tokenExpirationMinutes;
        }

        public static StatelessSessionConfig defaultConfig() {
            return new StatelessSessionConfig("Authorization", "Bearer ", 30);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Spring Security JWT Examples ===\n");

        // Demo 1: JWT Header and Payload
        System.out.println("--- Demo 1: JWT Header and Payload ---");
        var header = new JWTHeader("HS256", "JWT");
        System.out.println("Header: " + header.toBase64());

        Date now = new Date();
        Date expiry = Date.from(now.toInstant().plus(30, ChronoUnit.MINUTES));
        var payload = new JWTPayload(
                "john.doe",
                "java-engineering-academy",
                now,
                expiry,
                List.of("ROLE_USER", "ROLE_ADMIN"),
                Map.of("email", "john@example.com")
        );
        System.out.println("Payload: " + payload.toBase64());
        System.out.println("Is expired: " + payload.isExpired());

        // Demo 2: Token Creation
        System.out.println("\n--- Demo 2: Token Creation ---");
        var tokenProvider = new JWTTokenProvider(
                "mySecretKey12345678901234567890", "java-engineering-academy", 30);
        JWTToken token = tokenProvider.createToken("john.doe", List.of("ROLE_USER", "ROLE_ADMIN"));

        System.out.println("Token: " + token.serialize().substring(0, 50) + "...");
        System.out.println("Header: " + token.header());
        System.out.println("Payload: " + token.payload());
        System.out.println("Signature: " + token.signature());

        // Demo 3: Token Validation
        System.out.println("\n--- Demo 3: Token Validation ---");
        boolean isValid = tokenProvider.validateToken(token);
        System.out.println("Token valid: " + isValid);

        JWTPayload tokenPayload = token.getPayload();
        System.out.println("Subject: " + tokenPayload.subject());
        System.out.println("Issuer: " + tokenPayload.issuer());
        System.out.println("Roles: " + tokenPayload.roles());

        // Demo 4: Token Authentication
        System.out.println("\n--- Demo 4: Token Authentication ---");
        Authentication auth = tokenProvider.getAuthentication(token);
        System.out.println("Authenticated: " + auth.isAuthenticated());
        System.out.println("Principal: " + auth.getPrincipal());
        System.out.println("Authorities: " + auth.getAuthorities());

        // Demo 5: Token Store
        System.out.println("\n--- Demo 5: Token Store ---");
        var tokenStore = new JWTTokenStore(tokenProvider);
        JWTToken storedToken = tokenStore.generateToken("jane.smith", List.of("ROLE_USER"));
        System.out.println("Token generated for jane.smith: " + storedToken.serialize().substring(0, 50) + "...");
        System.out.println("Has token: " + tokenStore.hasToken("jane.smith"));
        System.out.println("Validated: " + tokenStore.validateToken("jane.smith", storedToken));

        Authentication storedAuth = tokenStore.authenticate("jane.smith", storedToken);
        System.out.println("Authenticated: " + storedAuth.isAuthenticated());

        // Demo 6: Token Revocation
        System.out.println("\n--- Demo 6: Token Revocation ---");
        tokenStore.revokeToken("jane.smith");
        System.out.println("Has token after revocation: " + tokenStore.hasToken("jane.smith"));
        System.out.println("Validated after revocation: " +
                tokenStore.validateToken("jane.smith", storedToken));

        // Demo 7: JWT Filter
        System.out.println("\n--- Demo 7: JWT Filter ---");
        var filter = new JWTAuthenticationFilter(tokenProvider, tokenStore);
        String authHeader = "Bearer " + token.serialize();

        System.out.println("Requires auth: " + filter.requiresAuthentication(authHeader));
        System.out.println("Requires auth (no header): " + filter.requiresAuthentication(null));

        String extractedToken = filter.extractToken(authHeader);
        System.out.println("Extracted token: " + extractedToken.substring(0, 50) + "...");

        Authentication filterAuth = filter.attemptAuthentication(authHeader);
        System.out.println("Filter auth result: " + (filterAuth != null ? filterAuth.isAuthenticated() : "null"));

        // Demo 8: Invalid Token
        System.out.println("\n--- Demo 8: Invalid Token ---");
        String badHeader = "Bearer invalid.token.here";
        Authentication badAuth = filter.attemptAuthentication(badHeader);
        System.out.println("Bad token auth result: " + badAuth);

        // Demo 9: Stateless Session Config
        System.out.println("\n--- Demo 9: Stateless Session Config ---");
        var statelessConfig = StatelessSessionConfig.defaultConfig();
        System.out.println("Token header: " + statelessConfig.getTokenHeader());
        System.out.println("Token prefix: " + statelessConfig.getTokenPrefix());
        System.out.println("Token expiration: " + statelessConfig.getTokenExpirationMinutes() + " minutes");

        // Demo 10: Token Parsing
        System.out.println("\n--- Demo 10: Token Parsing ---");
        JWTToken parsedToken = JWTToken.parse(token.serialize());
        System.out.println("Parsed header matches: " + parsedToken.header().equals(token.header()));
        System.out.println("Parsed payload matches: " + parsedToken.payload().equals(token.payload()));
        System.out.println("Parsed signature matches: " + parsedToken.signature().equals(token.signature()));

        System.out.println("\n=== All demos completed successfully ===");
    }
}
