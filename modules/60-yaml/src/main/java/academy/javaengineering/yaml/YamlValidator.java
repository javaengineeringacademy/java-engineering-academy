package academy.javaengineering.yaml;

/**
 * Demonstrates YAML validation.
 */
public class YamlValidator {

    public static boolean isValidYaml(String yaml) {
        if (yaml == null || yaml.isBlank()) return false;
        
        int indentLevel = 0;
        for (String line : yaml.split("\n")) {
            if (line.isBlank()) continue;
            
            int currentIndent = line.length() - line.stripLeading().length();
            
            if (currentIndent > indentLevel + 2) {
                return false;
            }
            
            indentLevel = currentIndent;
        }
        
        return true;
    }

    public static java.util.Map<String, String> parseSimpleYaml(String yaml) {
        java.util.Map<String, String> result = new java.util.HashMap<>();
        
        for (String line : yaml.split("\n")) {
            if (line.isBlank() || line.strip().startsWith("#")) continue;
            
            String[] parts = line.strip().split(":", 2);
            if (parts.length == 2) {
                result.put(parts[0].strip(), parts[1].strip());
            }
        }
        
        return result;
    }
}
