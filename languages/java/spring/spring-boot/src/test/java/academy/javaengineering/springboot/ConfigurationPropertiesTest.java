package academy.javaengineering.springboot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ConfigurationPropertiesExample} covering property binding, nested objects, and validation.
 */
@DisplayName("Configuration Properties Tests")
class ConfigurationPropertiesTest {

    @Test
    @DisplayName("App properties should bind from configuration")
    void testAppPropertiesBinding() {
        ConfigurationPropertiesExample.AppProperties props =
                new ConfigurationPropertiesExample.AppProperties();
        props.setName("TestApplication");
        props.setVersion("3.0.0");
        props.setDebug(true);
        props.setTags(List.of("java", "spring", "boot"));
        props.getMetadata().put("build", "maven");
        props.getMetadata().put("java", "21");

        assertNotNull(props);
        assertEquals("TestApplication", props.getName());
        assertEquals("3.0.0", props.getVersion());
        assertTrue(props.isDebug());
    }

    @Test
    @DisplayName("App properties tags should bind as list")
    void testAppPropertiesTags() {
        ConfigurationPropertiesExample.AppProperties props =
                new ConfigurationPropertiesExample.AppProperties();
        props.setTags(List.of("java", "spring", "boot"));

        assertNotNull(props.getTags());
        assertEquals(3, props.getTags().size());
        assertTrue(props.getTags().contains("java"));
        assertTrue(props.getTags().contains("spring"));
        assertTrue(props.getTags().contains("boot"));
    }

    @Test
    @DisplayName("App properties metadata should bind as map")
    void testAppPropertiesMetadata() {
        ConfigurationPropertiesExample.AppProperties props =
                new ConfigurationPropertiesExample.AppProperties();
        props.getMetadata().put("build", "maven");
        props.getMetadata().put("java", "21");

        assertNotNull(props.getMetadata());
        assertEquals("maven", props.getMetadata().get("build"));
        assertEquals("21", props.getMetadata().get("java"));
    }

    @Test
    @DisplayName("Nested feature flags should bind correctly")
    void testNestedFeatureFlags() {
        ConfigurationPropertiesExample.AppProperties props =
                new ConfigurationPropertiesExample.AppProperties();
        props.getFeatures().getLogging().setEnabled(true);
        props.getFeatures().getMetrics().setEnabled(false);
        props.getFeatures().getCache().setEnabled(true);

        assertNotNull(props.getFeatures());
        assertTrue(props.getFeatures().getLogging().isEnabled());
        assertFalse(props.getFeatures().getMetrics().isEnabled());
        assertTrue(props.getFeatures().getCache().isEnabled());
    }

    @Test
    @DisplayName("Server properties should bind from configuration")
    void testServerPropertiesBinding() {
        ConfigurationPropertiesExample.ServerProperties props =
                new ConfigurationPropertiesExample.ServerProperties();
        props.setPort(9090);
        props.setContextPath("/api/v2");
        props.setConnectionTimeout(5000);
        props.setReadTimeout(10000);

        assertNotNull(props);
        assertEquals(9090, props.getPort());
        assertEquals("/api/v2", props.getContextPath());
        assertEquals(5000, props.getConnectionTimeout());
        assertEquals(10000, props.getReadTimeout());
    }

    @Test
    @DisplayName("Mail properties should bind from configuration")
    void testMailPropertiesBinding() {
        ConfigurationPropertiesExample.MailProperties props =
                new ConfigurationPropertiesExample.MailProperties();
        props.setHost("smtp.test.com");
        props.setPort(465);
        props.setUsername("test@test.com");
        props.setPassword("secret");
        props.setSsl(true);
        props.setAuth(true);

        assertNotNull(props);
        assertEquals("smtp.test.com", props.getHost());
        assertEquals(465, props.getPort());
        assertEquals("test@test.com", props.getUsername());
        assertEquals("secret", props.getPassword());
        assertTrue(props.isSsl());
        assertTrue(props.isAuth());
    }

    @Test
    @DisplayName("App properties default values should be applied")
    void testAppPropertiesDefaults() {
        ConfigurationPropertiesExample.AppProperties props =
                new ConfigurationPropertiesExample.AppProperties();
        assertEquals("Spring Boot Demo", props.getName());
        assertEquals("1.0.0", props.getVersion());
        assertFalse(props.isDebug());
        assertNotNull(props.getFeatures());
        assertTrue(props.getFeatures().getLogging().isEnabled());
    }

    @Test
    @DisplayName("Server properties default values should be applied")
    void testServerPropertiesDefaults() {
        ConfigurationPropertiesExample.ServerProperties props =
                new ConfigurationPropertiesExample.ServerProperties();
        assertEquals(8080, props.getPort());
        assertEquals("/api", props.getContextPath());
        assertEquals(30000, props.getConnectionTimeout());
        assertEquals(60000, props.getReadTimeout());
    }

    @Test
    @DisplayName("Mail properties default values should be applied")
    void testMailPropertiesDefaults() {
        ConfigurationPropertiesExample.MailProperties props =
                new ConfigurationPropertiesExample.MailProperties();
        assertEquals("smtp.example.com", props.getHost());
        assertEquals(587, props.getPort());
        assertEquals("user@example.com", props.getUsername());
        assertTrue(props.isSsl());
        assertTrue(props.isAuth());
    }

    @Test
    @DisplayName("Properties setters should update values correctly")
    void testPropertySetters() {
        ConfigurationPropertiesExample.AppProperties props =
                new ConfigurationPropertiesExample.AppProperties();
        props.setName("CustomApp");
        props.setVersion("5.0.0");
        props.setDebug(true);

        assertEquals("CustomApp", props.getName());
        assertEquals("5.0.0", props.getVersion());
        assertTrue(props.isDebug());
    }

    @Test
    @DisplayName("Configuration properties main method should execute without error")
    void testConfigurationPropertiesMainMethod() {
        assertDoesNotThrow(() -> ConfigurationPropertiesExample.main(new String[]{}));
    }
}
