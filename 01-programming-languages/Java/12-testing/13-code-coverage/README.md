# 11.13 Code Coverage

## 1. Introduction

Code coverage measures how much of the source code is executed during tests. JaCoCo is the standard code coverage tool for Java, providing line, branch, and instruction coverage.

## 2. Learning Objectives

- Understand code coverage metrics
- Use JaCoCo for coverage reporting
- Configure coverage thresholds
- Interpret coverage reports
- Integrate coverage in CI/CD

## 3. Prerequisites

- JUnit 5 basics
- Maven/Gradle knowledge
- Testing concepts

## 4. Why This Concept Exists

Code coverage helps:
- Identify untested code
- Set quality gates
- Track testing progress
- Find dead code
- Guide test creation

## 5. Problem Statement

How do we measure and ensure adequate test coverage?

## 6. Theory

### Coverage Types

| Metric | Description |
|--------|-------------|
| Line coverage | Percentage of lines executed |
| Branch coverage | Percentage of branches taken |
| Instruction coverage | Percentage of bytecode instructions |
| Method coverage | Percentage of methods called |
| Class coverage | Percentage of classes loaded |

### Coverage Formula

```
Coverage = (Covered Lines / Total Lines) × 100%
```

### Coverage Levels

| Level | Target | Meaning |
|-------|--------|---------|
| Excellent | 80-90% | Well tested |
| Good | 70-79% | Adequately tested |
| Fair | 50-69% | Partially tested |
| Poor | <50% | Insufficiently tested |

## 7. Internal Working

### JaCoCo Instrumentation

1. **Compile time**: Add probes to bytecode
2. **Runtime**: Probes record execution
3. **Report time**: Analyze probe data

### Bytecode Probes

```java
// Original
if (condition) {
    doSomething();
}

// Instrumented
boolean $jacocoData[] = $jacocoInit();
if (condition) {
    $jacocoData[0] = true; // Probe
    doSomething();
}
```

### Report Generation

1. Collect execution data from all tests
2. Merge data from multiple test runs
3. Calculate coverage metrics
4. Generate HTML/XML/CSV reports

## 8. JVM Perspective

- Instrumentation adds bytecode overhead
- Probes are stored in boolean arrays
- Data collected via classloader
- Reports generated offline

## 9. Memory Representation

```
JaCoCo Memory Model:
┌─────────────────────────────────────┐
│           JVM Heap                  │
│  - Instrumented classes             │
│  - Probe arrays                     │
│  - Execution data                   │
├─────────────────────────────────────┤
│         JaCoCo Agent                │
│  - Runtime data collector           │
│  - Class analyzer                   │
│  - Data merger                      │
└─────────────────────────────────────┘
```

## 10. Easy Example

```xml
<!-- Maven pom.xml -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## 11. Medium Example

```xml
<!-- With coverage thresholds -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>check</id>
            <phase>verify</phase>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>BUNDLE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.80</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## 12. Hard Example

```xml
<!-- Excluding classes from coverage -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <configuration>
        <excludes>
            <exclude>**/model/**</exclude>
            <exclude>**/config/**</exclude>
        </excludes>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Interview Questions

1. **What is code coverage?**
   Code coverage measures the percentage of source code executed during tests.

2. **What is JaCoCo?**
   JaCoCo is a Java code coverage tool that provides line, branch, and instruction coverage.

3. **What is the difference between line and branch coverage?**
   Line coverage measures executed lines; branch coverage measures taken decision paths.

4. **What coverage target should you set?**
   Typically 70-80% for new code, 80-90% for critical components.

5. **How do you exclude code from coverage?**
   Use JaCoCo exclude configuration or @Generated annotations.
