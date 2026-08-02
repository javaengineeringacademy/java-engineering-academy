package academy.javaengineering.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OAuth2Test {

    private OAuth2Example oauth2Example;

    @BeforeEach
    void setUp() {
        oauth2Example = new OAuth2Example();
    }

    @Test
    void testRegisterClient() {
        oauth2Example.registerClient("client1", "secret1", "http://localhost:8080/callback");
        assertNotNull(oauth2Example);
    }

    @Test
    void testGenerateAuthorizationCode() {
        oauth2Example.registerClient("client1", "secret1", "http://localhost:8080/callback");
        String code = oauth2Example.generateAuthorizationCode("client1", "read");
        assertNotNull(code);
        assertFalse(code.isEmpty());
    }

    @Test
    void testGenerateAuthorizationCodeForUnknownClient() {
        assertThrows(IllegalArgumentException.class, () -> {
            oauth2Example.generateAuthorizationCode("unknown", "read");
        });
    }

    @Test
    void testExchangeCodeForToken() {
        oauth2Example.registerClient("client1", "secret1", "http://localhost:8080/callback");
        String code = oauth2Example.generateAuthorizationCode("client1", "read");

        OAuth2Example.OAuth2Token token = oauth2Example.exchangeCodeForToken(
                "client1", "secret1", code);

        assertNotNull(token);
        assertNotNull(token.getAccessToken());
        assertEquals("client1", token.getClientId());
    }

    @Test
    void testExchangeWithWrongSecret() {
        oauth2Example.registerClient("client1", "secret1", "http://localhost:8080/callback");
        String code = oauth2Example.generateAuthorizationCode("client1", "read");

        assertThrows(SecurityException.class, () -> {
            oauth2Example.exchangeCodeForToken("client1", "wrongsecret", code);
        });
    }

    @Test
    void testExchangeWithInvalidCode() {
        oauth2Example.registerClient("client1", "secret1", "http://localhost:8080/callback");

        assertThrows(IllegalArgumentException.class, () -> {
            oauth2Example.exchangeCodeForToken("client1", "secret1", "invalid-code");
        });
    }

    @Test
    void testValidateToken() {
        oauth2Example.registerClient("client1", "secret1", "http://localhost:8080/callback");
        String code = oauth2Example.generateAuthorizationCode("client1", "read");
        OAuth2Example.OAuth2Token token = oauth2Example.exchangeCodeForToken(
                "client1", "secret1", code);

        assertTrue(oauth2Example.validateToken(token.getAccessToken()));
    }

    @Test
    void testIntrospectToken() {
        oauth2Example.registerClient("client1", "secret1", "http://localhost:8080/callback");
        String code = oauth2Example.generateAuthorizationCode("client1", "read write");
        OAuth2Example.OAuth2Token token = oauth2Example.exchangeCodeForToken(
                "client1", "secret1", code);

        OAuth2Example.OAuth2Token introspected = oauth2Example.introspectToken(
                token.getAccessToken());

        assertNotNull(introspected);
        assertEquals("read write", introspected.getScope());
    }

    @Test
    void testRevokeToken() {
        oauth2Example.registerClient("client1", "secret1", "http://localhost:8080/callback");
        String code = oauth2Example.generateAuthorizationCode("client1", "read");
        OAuth2Example.OAuth2Token token = oauth2Example.exchangeCodeForToken(
                "client1", "secret1", code);

        oauth2Example.revokeToken(token.getAccessToken());
        assertFalse(oauth2Example.validateToken(token.getAccessToken()));
    }

    @Test
    void testGetUserInfo() {
        oauth2Example.registerClient("client1", "secret1", "http://localhost:8080/callback");
        String code = oauth2Example.generateAuthorizationCode("client1", "read");
        OAuth2Example.OAuth2Token token = oauth2Example.exchangeCodeForToken(
                "client1", "secret1", code);

        Map<String, Object> userInfo = oauth2Example.getUserInfo(token.getAccessToken());
        assertNotNull(userInfo);
        assertEquals("user@example.com", userInfo.get("email"));
    }

    @Test
    void testGetUserInfoWithInvalidToken() {
        Map<String, Object> userInfo = oauth2Example.getUserInfo("invalid-token");
        assertNull(userInfo);
    }
}
