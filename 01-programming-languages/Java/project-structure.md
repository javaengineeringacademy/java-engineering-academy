# Java Project Structure

> Standard Maven and Gradle project layouts, multi-module projects, and resource management.

## Maven Standard Layout

```
my-project/
├── pom.xml                          # Project descriptor
├── src/
│   ├── main/
│   │   ├── java/                    # Java source files
│   │   │   └── com/
│   │   │       └── example/
│   │   │           ├── App.java
│   │   │           ├── controller/
│   │   │           ├── service/
│   │   │           ├── repository/
│   │   │           ├── model/
│   │   │           └── config/
│   │   ├── resources/               # Classpath resources
│   │   │   ├── application.yml
│   │   │   ├── logback.xml
│   │   │   └── db/
│   │   │       └── migration/
│   │   └── filters/                 # Web app descriptors
│   │       └── WEB-INF/
│   ├── test/
│   │   ├── java/                    # Test source files
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── AppTest.java
│   │   └── resources/               # Test resources
│   │       └── test-data/
│   └── it/                          # Integration tests
│       └── java/
├── target/                          # Build output (gitignored)
│   ├── classes/
│   ├── test-classes/
│   └── my-project-1.0.0.jar
├── .mvn/
│   ├── wrapper/
│   │   ├── maven-wrapper.jar
│   │   └── maven-wrapper.properties
│   └── maven.config
├── mvnw                             # Maven wrapper (Unix)
├── mvnw.cmd                         # Maven wrapper (Windows)
├── .gitignore
├── README.md
└── LICENSE
```

## Gradle Standard Layout

```
my-project/
├── build.gradle                     # or build.gradle.kts
├── settings.gradle                  # or settings.gradle.kts
├── gradle.properties                # Gradle properties
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew                          # Gradle wrapper (Unix)
├── gradlew.bat                      # Gradle wrapper (Windows)
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │   └── webapp/                  # For WAR files
│   └── test/
│       ├── java/
│       └── resources/
├── build/                           # Build output (gitignored)
│   ├── classes/
│   └── libs/
└── .gitignore
```

## Package Naming Convention

```java
// Reverse domain name convention
com.company.project.module

// Examples
com.google.cloud.storage
org.apache.kafka.clients
io.confluent.kafka.serializers

// Common package structure
com.example.myapp
├── controller/       # REST controllers
├── service/          # Business logic
├── repository/       # Data access
├── model/            # Domain entities
├── dto/              # Data transfer objects
├── config/           # Configuration classes
├── exception/        # Custom exceptions
├── util/             # Utility classes
└── constant/         # Constants and enums
```

## Maven Multi-Module

```
parent-project/
├── pom.xml                          # Parent POM
├── module-api/
│   ├── pom.xml
│   └── src/
├── module-core/
│   ├── pom.xml
│   └── src/
├── module-web/
│   ├── pom.xml
│   └── src/
└── module-runner/
    ├── pom.xml
    └── src/
```

### Parent POM

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>parent-project</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>

    <modules>
        <module>module-api</module>
        <module>module-core</module>
        <module>module-web</module>
        <module>module-runner</module>
    </modules>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.example</groupId>
                <artifactId>module-api</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>3.2.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

## Gradle Multi-Module

### settings.gradle.kts

```kotlin
rootProject.name = "parent-project"

include("module-api", "module-core", "module-web", "module-runner")
```

### build.gradle.kts (root)

```kotlin
plugins {
    java
}

subprojects {
    apply(plugin = "java")
    
    group = "com.example"
    version = "1.0.0"
    
    java {
        sourceCompatibility = JavaVersion.VERSION_21
    }
    
    repositories {
        mavenCentral()
    }
}
```

## Resource Directories

| Directory | Purpose | Available At |
|-----------|---------|--------------|
| `src/main/resources` | Production config | Classpath root |
| `src/test/resources` | Test config | Test classpath |
| `src/main/resources/META-INF` | Metadata | Classpath root/META-INF |
| `src/main/filters` | Web filters | Web app root |

### Resource Loading in Code

```java
// From classpath
InputStream is = getClass().getResourceAsStream("/application.yml");
InputStream is = getClass().getResourceAsStream("config.properties");

// From classpath with helper
URL url = getClass().getResource("/templates/index.html");
Path path = Path.of(getClass().getResource("/data.csv").toURI());

// Spring resource loading
@Value("classpath:data/schema.sql")
private Resource schemaResource;

@Value("classpath:templates/*.html")
private Resource[] templates;
```

## Standard Files

| File | Location | Purpose |
|------|----------|---------|
| `.gitignore` | Root | Git ignore rules |
| `.editorconfig` | Root | Editor settings |
| `pom.xml` / `build.gradle` | Root | Build configuration |
| `Dockerfile` | Root | Container build |
| `.env` | Root | Environment variables |
| `LICENSE` | Root | License file |
| `README.md` | Root | Project documentation |

### .gitignore

```gitignore
# Build output
target/
build/
out/

# IDE files
.idea/
*.iml
.vscode/
.settings/
.project
.classpath

# OS files
.DS_Store
Thumbs.db

# Environment
.env
*.env.local
```

## References

- [Maven Standard Layout](https://maven.apache.org/guides/mini/guide-directory-layout.html)
- [Gradle Build Structure](https://docs.gradle.org/current/userguide/build_layout.html)
- [Effective Maven - O'Reilly](https://www.oreilly.com/library/view/effective-maven/9781492062622/)

---
**Prerequisites:** [Java installation](installation.md)
**Related:** [Java configuration](configuration.md) | [Java best-practices](best-practices.md)
**Next:** [Java core-concepts](core-concepts.md)
