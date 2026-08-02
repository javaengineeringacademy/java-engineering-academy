# Module 60: YAML

## Overview
YAML (YAML Ain't Markup Language) is a human-readable data serialization format. Widely used in configuration files, Docker Compose, Kubernetes, and CI/CD pipelines.

## Learning Objectives
- Understand YAML syntax
- Use YAML in Java applications
- Handle YAML configuration
- Parse YAML with libraries
- Apply YAML best practices

## Prerequisites
- Basic programming concepts
- Data structure understanding
- Configuration file experience

## Why This Concept Exists
YAML provides:
- Human-readable format
- Hierarchical structure
- Comments support
- Cross-language compatibility
- Configuration management

## Problem Statement
How do you use YAML effectively for configuration and data exchange?

## YAML Syntax

### Basic Types

```yaml
# String
name: John Doe

# Number
age: 30
price: 19.99

# Boolean
active: true

# Null
middle_name: null

# Date
created: 2024-01-15
```

### Collections

```yaml
# List
fruits:
  - apple
  - banana
  - orange

# Inline list
fruits: [apple, banana, orange]

# Map
person:
  name: John
  age: 30

# Inline map
person: {name: John, age: 30}
```

### Complex Structures

```yaml
# Nested map
database:
  host: localhost
  port: 5432
  credentials:
    username: admin
    password: secret

# List of maps
users:
  - name: John
    age: 30
  - name: Jane
    age: 25

# Multi-line strings
description: |
  This is a multi-line
  string in YAML
```

### YAML Features

```yaml
# Anchors and aliases
defaults: &defaults
  timeout: 30
  retries: 3

service:
  <<: *defaults
  name: my-service

# Multiple documents
---
document1: first
---
document2: second
```

## Java YAML Libraries

| Library | Features |
|---------|----------|
| SnakeYAML | Full YAML 1.1 |
| Jackson YAML | Integration with Jackson |
| YamlBeans | Simple API |

## Enterprise Example

```java
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.*;

public class YamlExample {
    // Parse YAML
    public static Map<String, Object> parseYaml(String yamlContent) {
        Yaml yaml = new Yaml();
        return yaml.load(yamlContent);
    }
    
    // Parse YAML file
    public static Map<String, Object> parseYamlFile(String filePath) throws Exception {
        Yaml yaml = new Yaml();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return yaml.load(fis);
        }
    }
    
    // Generate YAML
    public static String toYaml(Object data) {
        Yaml yaml = new Yaml();
        return yaml.dump(data);
    }
    
    // Configuration class
    public record AppConfig(
        String name,
        DatabaseConfig database,
        List<String> features
    ) {}
    
    public record DatabaseConfig(
        String host,
        int port,
        Credentials credentials
    ) {}
    
    public record Credentials(
        String username,
        String password
    ) {}
    
    public static void main(String[] args) throws Exception {
        // Parse configuration
        String yaml = """
            name: my-app
            database:
              host: localhost
              port: 5432
              credentials:
                username: admin
                password: secret
            features:
              - logging
              - monitoring
              - caching
            """;
        
        Map<String, Object> config = parseYaml(yaml);
        System.out.println("App name: " + config.get("name"));
        
        // Parse with Jackson
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        AppConfig appConfig = mapper.readValue(yaml, AppConfig.class);
        System.out.println("Database host: " + appConfig.database().host());
    }
}
```

## Performance Considerations
- YAML parsing is slower than JSON
- Use YAML for configuration, not data
- Cache parsed configurations
- Validate YAML structure

## Best Practices
1. Use consistent indentation (2 spaces)
2. Add comments for clarity
3. Use anchors for repetition
4. Keep files small
5. Validate YAML

## Interview Questions

### Q1: What is YAML?
**Answer:** Human-readable data serialization format.

### Q2: What is the difference between YAML and JSON?
**Answer:** YAML supports comments, anchors, and is more readable.

### Q3: How do you parse YAML in Java?
**Answer:** Use SnakeYAML or Jackson YAML library.

### Q4: What are YAML anchors?
**Answer:** Reusable blocks of YAML with & and * syntax.

### Q5: What is YAML used for?
**Answer:** Configuration files, Docker Compose, Kubernetes manifests.

## Summary
YAML is a versatile format for configuration and data exchange. Master its syntax and Java libraries.

## References
- YAML Specification
- SnakeYAML Documentation
- Jackson YAML Module
