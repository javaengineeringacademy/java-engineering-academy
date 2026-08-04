# Snyk Security Scanning

## Overview

Snyk finds and fixes vulnerabilities in code, open source dependencies, containers, and infrastructure as code.

## Features

- Dependency vulnerability scanning
- Container scanning
- IaC scanning
- SAST (Static Application Security Testing)
- License compliance

## Configuration

### Maven Integration
```xml
<plugin>
    <groupId>io.snyk</groupId>
    <artifactId>snyk-maven-plugin</artifactId>
    <version>2.2.0</version>
    <configuration>
        <apiToken>${SNYK_TOKEN}</apiToken>
        <severityThreshold>medium</severityThreshold>
    </configuration>
</plugin>
```

### Gradle Integration
```groovy
plugins {
    id 'snyk-gradle' version '2.2.0'
}

snyk {
    apiToken = System.getenv('SNYK_TOKEN')
    severityThreshold = 'medium'
}
```

### CLI Usage
```bash
# Test project
snyk test

# Monitor project
snyk monitor

# Fix vulnerabilities
snyk wizard

# Test container image
snyk container test myapp:latest

# Test IaC
snyk iac test terraform/
```

## Vulnerability Types

| Type | Description |
|------|-------------|
| Vulnerable Dependency | Known CVEs in dependencies |
| License Issues | Problematic licenses |
| Container Vulnerabilities | OS and library vulnerabilities |
| IaC Issues | Infrastructure misconfigurations |

## Severity Levels

| Level | CVSS Score | Description |
|-------|-----------|-------------|
| Critical | 9.0-10.0 | Immediate action required |
| High | 7.0-8.9 | Urgent fix needed |
| Medium | 4.0-6.9 | Should be fixed |
| Low | 0.1-3.9 | Consider fixing |

## Best Practices

1. Integrate into CI/CD pipeline
2. Monitor dependencies regularly
3. Fix critical vulnerabilities first
4. Use Snyk Advisor for alternatives
5. Set up auto-fix PRs
6. Review license compliance
7. Scan container images
8. Monitor open source usage
