package academy.javaengineering.springboot;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    @Test
    void shouldLoadProperties() {
        ConfigurationExample.AppConfig config = new ConfigurationExample.AppConfig();
        config.loadProperties(Map.of("key1", "value1", "key2", "value2"));
        assertEquals("value1", config.get("key1"));
        assertEquals("value2", config.get("key2"));
    }

    @Test
    void shouldReturnDefaultForMissing() {
        ConfigurationExample.AppConfig config = new ConfigurationExample.AppConfig();
        assertEquals("default", config.get("missing", "default"));
    }
}
