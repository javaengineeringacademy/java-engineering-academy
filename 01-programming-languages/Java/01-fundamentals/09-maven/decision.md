# When to Use Maven

## Decision Guide

### Build Tool Selection

| Use Maven When | Use Gradle When |
|----------------|-----------------|
| Standard Java projects | Complex build logic |
| Team familiar with Maven | Need Groovy/Kotlin DSL |
| Enterprise environments | Custom build requirements |
| Strict convention over configuration | Flexible build scripts |

### Maven vs Gradle

| Feature | Maven | Gradle |
|---------|-------|--------|
| Configuration | XML (pom.xml) | Groovy/Kotlin DSL |
| Learning curve | Moderate | Steeper |
| Flexibility | Limited | High |
| Build speed | Slower | Faster (incremental) |
| IDE support | Excellent | Good |
| Enterprise adoption | Higher | Growing |

### POM Configuration Decision Tree

| Need | Configuration |
|------|---------------|
| Add dependency | `<dependencies>` section |
| Plugin | `<build><plugins>` section |
| Profile | `<profiles>` section |
| Parent POM | `<parent>` section |
| Property | `<properties>` section |
| Module | `<modules>` section |

### Dependency Scope Selection

| Scope | Use When | Example |
|-------|----------|---------|
| `compile` | Needed for all phases | Core libraries |
| `provided` | Provided by container | Servlet API |
| `runtime` | Runtime only | JDBC drivers |
| `test` | Testing only | JUnit |
| `system` | System path | Local JARs |

### Plugin Selection

| Task | Plugin | Goal |
|------|--------|------|
| Compile | maven-compiler-plugin | compile |
| Test | maven-surefire-plugin | test |
| Package | maven-jar-plugin | jar |
| Install | maven-install-plugin | install |
| Deploy | maven-deploy-plugin | deploy |
| Site | maven-site-plugin | site |

## Production Guidelines

### Multi-Module Projects
```xml
<modules>
    <module>common</module>
    <module>core</module>
    <module>web</module>
</modules>
```

### Dependency Management
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>${spring.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### Profile Configuration
```xml
<profiles>
    <profile>
        <id>dev</id>
        <properties>
            <env>development</env>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <env>production</env>
        </properties>
    </profile>
</profiles>
```
