# Module 61: Properties Files

## Overview
Properties files are key-value pairs used for configuration in Java applications. They provide externalized configuration, environment-specific settings, and runtime customization.

## Learning Objectives
- Understand properties file format
- Use Properties class in Java
- Externalize configuration
- Handle environment-specific configs
- Apply best practices

## Prerequisites
- Basic Java knowledge
- File handling
- Configuration concepts

## Why This Concept Exists
Properties files provide:
- External configuration
- Environment separation
- Runtime customization
- Easy modification

## Problem Statement
How do you manage application configuration effectively?

## Properties File Format

### Basic Format

```properties
# Database configuration
database.host=localhost
database.port=5432
database.name=myapp

# Application settings
app.name=My Application
app.version=1.0.0
app.debug=true

# Logging
logging.level.root=INFO
logging.level.com.example=DEBUG
```

### Spring Boot Properties

```properties
# Server
server.port=8080
server.servlet.context-path=/api

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=admin
spring.datasource.password=secret

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Logging
logging.level.org.springframework=WARN
logging.level.com.example=DEBUG
```

### Environment-Specific

```properties
# application.properties
spring.profiles.active=dev

# application-dev.properties
database.url=jdbc:h2:mem:devdb

# application-prod.properties
database.url=jdbc:postgresql://prod-server:5432/proddb
```

## Java Properties API

```java
import java.util.Properties;
import java.io.*;

// Load properties
Properties props = new Properties();
try (FileInputStream fis = new FileInputStream("app.properties")) {
    props.load(fis);
}

// Get values
String host = props.getProperty("database.host", "localhost");
int port = Integer.parseInt(props.getProperty("database.port", "5432"));

// Set values
props.setProperty("new.key", "new.value");
props.store(new FileOutputStream("app.properties"), "Comment");

// System properties
Properties sysProps = System.getProperties();
String javaVersion = sysProps.getProperty("java.version");
```

## Enterprise Example

```java
import java.util.Properties;
import java.io.*;
import java.nio.file.*;

public class PropertiesEnterpriseExample {
    // Configuration manager
    public class ConfigurationManager {
        private final Properties properties;
        
        public ConfigurationManager(String profile) throws IOException {
            properties = new Properties();
            
            // Load base properties
            loadProperties("application.properties");
            
            // Load profile-specific
            loadProperties("application-" + profile + ".properties");
            
            // Override with environment variables
            overrideFromEnvironment();
        }
        
        private void loadProperties(String filename) throws IOException {
            Path path = Path.of(filename);
            if (Files.exists(path)) {
                try (FileInputStream fis = new FileInputStream(path.toFile())) {
                    properties.load(fis);
                }
            }
        }
        
        private void overrideFromEnvironment() {
            properties.setProperty("database.host",
                System.getenv().getOrDefault("DB_HOST", 
                    properties.getProperty("database.host")));
        }
        
        public String get(String key) {
            return properties.getProperty(key);
        }
        
        public String get(String key, String defaultValue) {
            return properties.getProperty(key, defaultValue);
        }
        
        public int getInt(String key, int defaultValue) {
            String value = properties.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        }
    }
    
    public static void main(String[] args) throws Exception {
        ConfigurationManager config = new ConfigurationManager("prod");
        
        System.out.println("Database host: " + config.get("database.host"));
        System.out.println("App debug: " + config.get("app.debug", "false"));
    }
}
```

## Performance Considerations
- Cache loaded properties
- Use resource bundles for i18n
- Validate property values
- Handle missing properties gracefully

## Best Practices
1. Use hierarchical keys
2. Provide default values
3. Document properties
4. Use profiles for environments
5. Validate on startup

## Interview Questions

### Q1: What is the difference between properties and YAML?
**Answer:** Properties is flat key-value, YAML supports hierarchy and comments.

### Q2: How do you externalize configuration?
**Answer:** Use properties files or environment variables.

### Q3: What are Spring profiles?
**Answer:** Environment-specific configuration sets.

### Q4: How do you handle secrets?
**Answer:** Use environment variables or secret management tools.

### Q5: What is the difference between application.properties and bootstrap.properties?
**Answer:** Bootstrap loads first, used for config server.

## Summary
Properties files are essential for application configuration. Use them effectively for maintainable applications.

## References
- Java Properties Documentation
- Spring Boot Configuration
- Twelve-Factor App
