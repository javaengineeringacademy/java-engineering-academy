# SonarQube

## Overview

SonarQube is a platform for continuous inspection of code quality and security vulnerabilities.

## Features

- Code quality analysis
- Security vulnerability detection
- Code coverage reporting
- Code duplication detection
- Custom rules and quality gates

## Configuration

### Maven Integration
```xml
<plugin>
    <groupId>org.sonarsource.scanner.maven</groupId>
    <artifactId>sonar-maven-plugin</artifactId>
    <version>3.9.1.2184</version>
</plugin>
```

### Gradle Integration
```groovy
plugins {
    id 'org.sonarqube' version '4.0.0.2912'
}

sonar {
    properties {
        property "sonar.host.url", "http://localhost:9000"
        property "sonar.login", "your-token"
    }
}
```

### Project Properties
```properties
sonar.projectKey=my-project
sonar.projectName=My Project
sonar.sources=src/main/java
sonar.tests=src/test/java
sonar.java.binaries=target/classes
sonar.java.libraries=target/dependency/*.jar
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
sonar.junit.reportPaths=target/surefire-reports
```

## Quality Gates

### Default Gate
```yaml
conditions:
  - metric: new_coverage
    operator: LESS_THAN
    value: "80"
  - metric: new_duplicated_lines_density
    operator: GREATER_THAN
    value: "3"
  - metric: new_blocker_violations
    operator: GREATER_THAN
    value: "0"
```

### Custom Gate
```java
// Configure in SonarQube UI or via API
POST /api/qualitygates/create
{
  "name": "Strict Quality Gate",
  "conditions": [
    {"metric": "coverage", "op": "LT", "error": "90"},
    {"metric": "duplicated_lines_density", "op": "GT", "error": "1"},
    {"metric": "blocker_violations", "op": "GT", "error": "0"}
  ]
}
```

## Security Rules

### OWASP Top 10
- SQL Injection
- XSS
- Path Traversal
- LDAP Injection
- CSRF

### CWE Rules
- Hard-coded credentials
- Insecure random
- Weak cryptography
- Insufficient logging

## Best Practices

1. Integrate into CI/CD pipeline
2. Set up quality gates
3. Review issues regularly
4. Fix critical issues first
5. Use custom rules for your codebase
6. Monitor quality trends
7. Clean up debt regularly
8. Train team on best practices
