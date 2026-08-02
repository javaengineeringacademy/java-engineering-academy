package academy.javaengineering.properties;

import java.util.Properties;

/**
 * Demonstrates properties file handling.
 */
public class PropertiesHandler {

    private final Properties properties;

    public PropertiesHandler() {
        this.properties = new Properties();
    }

    public void loadDefaults() {
        properties.setProperty("app.name", "MyApp");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("server.port", "8080");
        properties.setProperty("database.url", "jdbc:localhost/mydb");
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public Properties getProperties() {
        return new Properties(properties);
    }
}
