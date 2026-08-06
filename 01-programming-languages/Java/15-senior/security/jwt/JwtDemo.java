package academy.javaengineering.senior.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JwtDemo {

    private static final String SECRET_KEY = "mySecretKeyForHmacAlgorithm2024";
    private static final String HMAC_SHA256 = "HmacSHA256";

    public static void main(String[] args) throws Exception {
        System.out.println("=== JWT Structure ===");
        System.out.println("Header.Payload.Signature");
        System.out.println();

        Map<String, String> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", "1234567890");
        payload.put("name", "John Doe");
        payload.put("iat", System.currentTimeMillis() / 1000);
        payload.put("exp", System.currentTimeMillis() / 1000 + 3600);

        String jwt = createJwt(header, payload);
        System.out.println("Created JWT: " + jwt);
        System.out.println();

        System.out.println("=== JWT Verification ===");
        boolean isValid = verifyJwt(jwt);
        System.out.println("Token valid: " + isValid);
        System.out.println();

        System.out.println("=== JWT Parsing ===");
        parseJwt(jwt);
        System.out.println();

        System.out.println("=== Tampered Token ===");
        String[] parts = jwt.split("\\.");
        String tampered = parts[0] + "." + parts[1] + ".invalidSignature";
        System.out.println("Tampered token valid: " + verifyJwt(tampered));
    }

    private static String createJwt(Map<String, String> header, Map<String, Object> payload) throws Exception {
        String headerJson = mapToJson(header);
        String payloadJson = mapToJson(payload);

        String encodedHeader = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));

        String signatureInput = encodedHeader + "." + encodedPayload;
        String signature = hmacSha256(signatureInput, SECRET_KEY);
        String encodedSignature = base64UrlEncode(signature.getBytes(StandardCharsets.UTF_8));

        return encodedHeader + "." + encodedPayload + "." + encodedSignature;
    }

    private static boolean verifyJwt(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length != 3) return false;

            String signatureInput = parts[0] + "." + parts[1];
            String expectedSignature = hmacSha256(signatureInput, SECRET_KEY);
            String expectedEncoded = base64UrlEncode(expectedSignature.getBytes(StandardCharsets.UTF_8));

            return expectedEncoded.equals(parts[2]);
        } catch (Exception e) {
            return false;
        }
    }

    private static void parseJwt(String jwt) {
        String[] parts = jwt.split("\\.");
        System.out.println("Header: " + base64UrlDecode(parts[0]));
        System.out.println("Payload: " + base64UrlDecode(parts[1]));
    }

    private static String hmacSha256(String data, String secret) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(keySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return new String(hash, StandardCharsets.UTF_8);
    }

    private static String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String base64UrlDecode(String encoded) {
        return new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8);
    }

    private static String mapToJson(Map<String, ?> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, ?> entry : map.entrySet()) {
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
}
