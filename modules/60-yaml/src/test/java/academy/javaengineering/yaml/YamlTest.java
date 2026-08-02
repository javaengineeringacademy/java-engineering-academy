package academy.javaengineering.yaml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("YAML Tests")
class YamlTest {

    @Test
    @DisplayName("Should generate valid YAML")
    void testGenerateYaml() {
        String yaml = YamlConfig.generateYaml();
        assertNotNull(yaml);
        assertTrue(yaml.contains("server:"));
        assertTrue(yaml.contains("database:"));
    }

    @Test
    @DisplayName("Should validate YAML syntax")
    void testValidateYaml() {
        String validYaml = """
            key1: value1
            key2: value2
            """;
        String invalidYaml = """
                key1: value1
            key2: value2
            """;
        
        assertTrue(YamlValidator.isValidYaml(validYaml));
        assertFalse(YamlValidator.isValidYaml(invalidYaml));
    }

    @Test
    @DisplayName("Should parse simple YAML")
    void testParseYaml() {
        String yaml = """
            name: test
            value: 123
            """;
        
        var result = YamlValidator.parseSimpleYaml(yaml);
        assertEquals("test", result.get("name"));
        assertEquals("123", result.get("value"));
    }
}
