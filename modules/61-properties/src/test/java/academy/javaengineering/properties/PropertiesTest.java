package academy.javaengineering.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Properties Tests")
class PropertiesTest {

    @Test
    @DisplayName("Should load default properties")
    void testLoadDefaults() {
        var handler = new PropertiesHandler();
        handler.loadDefaults();
        
        assertEquals("MyApp", handler.getProperty("app.name"));
        assertEquals("1.0.0", handler.getProperty("app.version"));
    }

    @Test
    @DisplayName("Should return default value for missing property")
    void testDefaultValue() {
        var handler = new PropertiesHandler();
        assertEquals("default", handler.getProperty("missing", "default"));
    }

    @Test
    @DisplayName("Should set and get property")
    void testSetProperty() {
        var handler = new PropertiesHandler();
        handler.setProperty("custom.key", "custom.value");
        
        assertEquals("custom.value", handler.getProperty("custom.key"));
    }
}
