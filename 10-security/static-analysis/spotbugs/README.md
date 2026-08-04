# SpotBugs

## Overview

SpotBugs is a static analysis tool that finds bugs in Java programs by analyzing bytecode.

## Features

- Detects common bug patterns
- Custom bug detectors
- Integration with build tools
- Detailed reports with categories

## Configuration

### Maven Integration
```xml
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>4.7.3.0</version>
    <configuration>
        <effort>Max</effort>
        <threshold>Medium</threshold>
        <xmlOutput>true</xmlOutput>
        <includeFilterFile>spotbugs-exclude.xml</includeFilterFile>
    </configuration>
    <executions>
        <execution>
            <phase>verify</phase>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Gradle Integration
```groovy
plugins {
    id 'com.github.spotbugs' version '5.0.14'
}

spotbugs {
    effort = 'max'
    reportLevel = 'medium'
    excludeFilter = file('spotbugs-exclude.xml')
}
```

### Exclude Filter
```xml
<FindBugsFilter>
    <Match>
        <Package name="com.example.generated"/>
    </Match>
    <Match>
        <Bug pattern="NP_NULL_ON_SOME_PATH"/>
        <Method name="getData"/>
    </Match>
</FindBugsFilter>
```

## Bug Categories

| Category | Description |
|----------|-------------|
| Correctness | Likely bugs |
| Bad Practice | Code that violates best practices |
| Malicious Code | Potential security issues |
| Performance | Inefficient code |
| Reliability | Potential runtime failures |

## Common Bug Patterns

### NP_NULL_ON_SOME_PATH
```java
// Potential null pointer
String value = map.get("key");
int length = value.length(); // NP_NULL_ON_SOME_PATH

// Fixed
String value = map.get("key");
if (value != null) {
    int length = value.length();
}
```

### RCN_REDUNDANT_NULLCHECK
```java
// Redundant null check
if (value != null && value.equals("test")) {
    // ...
}

// Fixed
if ("test".equals(value)) {
    // ...
}
```

### SQL_NONCONSTANT_STRING_SQL_STATEMENT
```java
// SQL injection risk
String query = "SELECT * FROM users WHERE id = " + userId;
Statement stmt = connection.createStatement();
stmt.executeQuery(query);

// Fixed
PreparedStatement stmt = connection.prepareStatement(
    "SELECT * FROM users WHERE id = ?");
stmt.setInt(1, userId);
```

## Best Practices

1. Run SpotBugs in CI/CD
2. Fix critical bugs first
3. Use appropriate effort level
4. Create exclude filters for false positives
5. Review bug categories regularly
6. Combine with other tools
7. Train team on bug patterns
8. Monitor bug trends
