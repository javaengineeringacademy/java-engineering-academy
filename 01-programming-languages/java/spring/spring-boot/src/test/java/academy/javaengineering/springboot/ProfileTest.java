package academy.javaengineering.springboot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProfileExample} covering profile-specific bean creation and configuration.
 */
@DisplayName("Profile Tests")
class ProfileTest {

    @Test
    @DisplayName("Test profile database config should use H2")
    void testTestProfileDatabaseConfig() {
        ProfileExample.DatabaseConfig config = new ProfileExample.DatabaseConfig();
        config.setType("H2");
        config.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        config.setUsername("sa");
        config.setPassword("");
        config.setPooling(false);
        config.setMaxPoolSize(1);

        assertNotNull(config);
        assertEquals("H2", config.getType());
        assertTrue(config.getUrl().contains("testdb"));
        assertEquals("sa", config.getUsername());
        assertFalse(config.isPooling());
        assertEquals(1, config.getMaxPoolSize());
    }

    @Test
    @DisplayName("Dev profile database config should use in-memory H2")
    void testDevProfileDatabaseConfig() {
        ProfileExample.DatabaseConfig config = new ProfileExample.DatabaseConfig();
        config.setType("H2");
        config.setUrl("jdbc:h2:mem:devdb");
        config.setUsername("sa");
        config.setPassword("");
        config.setPooling(true);
        config.setMaxPoolSize(5);

        assertEquals("H2", config.getType());
        assertTrue(config.getUrl().contains("devdb"));
        assertTrue(config.isPooling());
        assertEquals(5, config.getMaxPoolSize());
    }

    @Test
    @DisplayName("Prod profile database config should use PostgreSQL")
    void testProdProfileDatabaseConfig() {
        ProfileExample.DatabaseConfig config = new ProfileExample.DatabaseConfig();
        config.setType("PostgreSQL");
        config.setUrl("jdbc:postgresql://localhost:5432/proddb");
        config.setUsername("prod_user");
        config.setPassword("${DB_PASSWORD}");
        config.setPooling(true);
        config.setMaxPoolSize(20);

        assertEquals("PostgreSQL", config.getType());
        assertTrue(config.getUrl().contains("proddb"));
        assertEquals("prod_user", config.getUsername());
        assertEquals(20, config.getMaxPoolSize());
    }

    @Test
    @DisplayName("Dev cache service should be NoOp implementation")
    void testDevCacheService() {
        ProfileExample.CacheService cache = new ProfileExample.NoOpCacheService();
        assertEquals("NoOp (Dev)", cache.getCacheType());
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
        assertNull(cache.get("nonexistent"));
    }

    @Test
    @DisplayName("Prod cache service should be Redis implementation")
    void testProdCacheService() {
        ProfileExample.CacheService cache = new ProfileExample.RedisCacheService();
        assertEquals("Redis (Prod)", cache.getCacheType());
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    @DisplayName("Dev email service should use console logging")
    void testDevEmailService() {
        ProfileExample.EmailService email = new ProfileExample.ConsoleEmailService();
        assertEquals("Console (Dev)", email.getServiceType());
        String result = email.sendEmail("test@test.com", "Subject", "Body");
        assertTrue(result.contains("ConsoleEmail"));
        assertTrue(result.contains("test@test.com"));
    }

    @Test
    @DisplayName("Prod email service should use SMTP")
    void testProdEmailService() {
        ProfileExample.EmailService email = new ProfileExample.SmtpEmailService();
        assertEquals("SMTP (Prod)", email.getServiceType());
        String result = email.sendEmail("user@prod.com", "Alert", "Body");
        assertTrue(result.contains("SmtpEmail"));
        assertTrue(result.contains("user@prod.com"));
    }

    @Test
    @DisplayName("DatabaseConfig toMap should return correct representation")
    void testDatabaseConfigToMap() {
        ProfileExample.DatabaseConfig config = new ProfileExample.DatabaseConfig();
        config.setType("H2");
        config.setUrl("jdbc:h2:mem:testdb");
        config.setUsername("sa");
        config.setPooling(true);
        config.setMaxPoolSize(5);

        Map<String, Object> map = config.toMap();
        assertEquals("H2", map.get("type"));
        assertEquals("jdbc:h2:mem:testdb", map.get("url"));
        assertEquals("sa", map.get("username"));
        assertEquals(true, map.get("pooling"));
        assertEquals(5, map.get("maxPoolSize"));
    }

    @Test
    @DisplayName("Profile example main method should execute without error")
    void testProfileExampleMainMethod() {
        assertDoesNotThrow(() -> ProfileExample.main(new String[]{}));
    }
}
