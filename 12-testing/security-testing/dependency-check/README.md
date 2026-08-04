# Dependency Check

## Overview
Software Composition Analysis (SCA) identifies known vulnerabilities in project dependencies.

## OWASP Dependency Check

### Maven
```xml
<plugin>
    <groupId>org.owasp</groupId>
    <artifactId>dependency-check-maven</artifactId>
    <version>9.0.7</version>
    <configuration>
        <failBuildOnCVSS>7</failBuildOnCVSS>
    </configuration>
    <executions>
        <execution><goals><goal>check</goal></goals></execution>
    </executions>
</plugin>
```

### Gradle
```groovy
plugins { id 'org.owasp.dependencycheck' version '9.0.7' }
dependencyCheck { failBuildOnCVSS = 7.0 }
```

## Vulnerability Severity

| CVSS | Severity | Action |
|------|----------|--------|
| 9.0-10.0 | Critical | Hotfix immediately |
| 7.0-8.9 | High | Patch within days |
| 4.0-6.9 | Medium | Patch within weeks |
| 0.1-3.9 | Low | Patch in next release |

## Suppression
```xml
<suppressions xmlns="https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd">
    <suppress>
        <notes>Does not affect our usage</notes>
        <cve>CVE-2022-22965</cve>
    </suppress>
</suppressions>
```

## Best Practices
1. Run on every build
2. Fail builds on critical vulnerabilities
3. Maintain suppression files
4. Automate dependency updates with Dependabot/Renovate
