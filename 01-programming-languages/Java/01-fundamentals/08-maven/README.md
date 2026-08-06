# Maven Project Structure

Maven is a build automation and project management tool for Java. It standardizes project layout, manages dependencies, and provides a consistent way to build, test, and deploy software.

---

## What Is Maven and Why Use It?

### The Problem Maven Solves

Without a build tool, you'd have to:
- Download JAR files manually and keep them in a `lib/` folder
- Remember the exact compile order for interdependent files
- Handle different environments (development, testing, production) yourself
- Document build steps for your team

Maven automates all of this through a single configuration file: `pom.xml`.

### What Maven Does

- **Dependency Management** — Automatically downloads libraries your project needs
- **Standardized Layout** — Every Maven project follows the same directory structure
- **Build Lifecycle** — Defined phases: compile, test, package, install, deploy
- **Reporting** — Generates project documentation, test reports, and code quality metrics
- **Multi-module Support** — Manages complex projects split across multiple modules

---

## The pom.xml File

The **POM** (Project Object Model) is the heart of every Maven project. It's an XML file at the project root that describes everything about your project.

### Basic Structure

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- Project Coordinates -->
    <groupId>com.company.project</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <!-- Project Metadata -->
    <name>My Application</name>
    <description>A sample Maven project</description>

    <!-- Properties -->
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <!-- Dependencies -->
    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!-- Build Configuration -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## groupId, artifactId, and version

These three fields uniquely identify your project — they're called **Maven coordinates**.

```xml
<groupId>com.company.project</groupId>
<artifactId>my-app</artifactId>
<version>1.0.0-SNAPSHOT</version>
```

### groupId
- Identifies the organization or group that created the project
- Follows reverse domain name convention (like Java packages)
- Examples: `com.google.guava`, `org.apache.commons`, `org.springframework`

### artifactId
- The name of the project or module
- Used as the JAR file name (e.g., `my-app-1.0.0.jar`)
- Must be unique within a groupId

### version
- The current version of the project
- Follows [Semantic Versioning](https://semver.org/): `MAJOR.MINOR.PATCH`
- `SNAPSHOT` suffix means development/unreleased (e.g., `1.0.0-SNAPSHOT`)

### Full Coordinate
```
com.company.project:my-app:1.0.0
│                  │        │
│                  │        └── version
│                  └── artifactId
└── groupId
```

---

## Dependencies and Scope

Dependencies are external libraries your project needs. Maven downloads them automatically from central repositories.

### Declaring Dependencies

```xml
<dependencies>
    <!-- A dependency -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
        <version>3.14.0</version>
    </dependency>
</dependencies>
```

### Dependency Scope

The `<scope>` element controls when and where the dependency is available.

| Scope | Compile | Test | Runtime | Example |
|-------|---------|------|---------|---------|
| `compile` (default) | Yes | Yes | Yes | Logging libraries, core frameworks |
| `test` | No | Yes | No | JUnit, Mockito, AssertJ |
| `runtime` | No | Yes | Yes | JDBC drivers, logging implementations |
| `provided` | Yes | Yes | No | Servlet API (provided by Tomcat) |

**Compile scope (default):**
```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
    <!-- scope is compile by default -->
</dependency>
```

**Test scope:**
```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.2</version>
    <scope>test</scope>
</dependency>
```

**Runtime scope:**
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.2</version>
    <scope>runtime</scope>
</dependency>
```

**Provided scope:**
```xml
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>
</dependency>
```

---

## Standard Maven Directory Layout

Maven enforces a conventional project structure. When you create a Maven project, it automatically follows this layout:

```
my-app/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/            ← Application source code
│   │   │   └── com/
│   │   │       └── company/
│   │   │           └── project/
│   │   │               ├── App.java
│   │   │               └── model/
│   │   │                   └── User.java
│   │   ├── resources/       ← Configuration files, properties
│   │   │   ├── application.properties
│   │   │   └── log4j2.xml
│   │   └── webapp/          ← Web application files (WAR projects)
│   │       └── WEB-INF/
│   └── test/
│       ├── java/            ← Test source code
│       │   └── com/
│       │       └── company/
│       │           └── project/
│       │               └── AppTest.java
│       └── resources/       ← Test configuration files
│           └── test-data.json
├── target/                  ← Build output (generated, gitignored)
│   ├── classes/             ← Compiled .class files
│   ├── test-classes/        ← Compiled test classes
│   └── my-app-1.0.0.jar     ← Packaged artifact
└── README.md
```

**Key directories:**

| Directory | Purpose |
|-----------|---------|
| `src/main/java/` | Your application source code |
| `src/main/resources/` | Configuration files, properties, XML configs |
| `src/test/java/` | Test source code |
| `src/test/resources/` | Test-specific configuration files |
| `target/` | Build output (generated, should be gitignored) |

---

## Common Maven Commands

Maven is invoked from the command line using `mvn` followed by a **goal** or **phase**.

### Lifecycle Phases

```
validate → compile → test → package → verify → install → deploy
```

| Command | What It Does |
|---------|-------------|
| `mvn clean` | Deletes the `target/` directory |
| `mvn compile` | Compiles `src/main/java/` into `target/classes/` |
| `mvn test` | Runs tests in `src/test/java/` |
| `mvn package` | Compiles, tests, and creates a JAR/WAR in `target/` |
| `mvn install` | Installs the JAR to your local `~/.m2/repository/` |
| `mvn deploy` | Deploys to a remote repository (Nexus, Artifactory) |
| `mvn clean install` | Clean + install (most common combo) |
| `mvn clean package` | Clean + package (good for pre-deploy) |

### Useful Commands

```bash
# Run all tests
mvn test

# Skip tests and package
mvn package -DskipTests

# Clean and rebuild everything
mvn clean install

# Generate a dependency tree
mvn dependency:tree

# Compile without running tests
mvn compile

# Run a specific test class
mvn test -Dtest=com.company.project.AppTest

# Download dependencies without compiling
mvn dependency:resolve
```

---

## Multi-Module Projects

Large projects are split into multiple modules, each with its own `pom.xml`. A parent POM coordinates them.

### Parent POM

```xml
<!-- parent pom.xml -->
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.company.project</groupId>
    <artifactId>my-app-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>  <!-- Special packaging for parent -->

    <modules>
        <module>my-app-core</module>
        <module>my-app-api</module>
        <module>my-app-web</module>
    </modules>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- Define versions here, modules reference without version -->
            <dependency>
                <groupId>org.junit.jupiter</groupId>
                <artifactId>junit-jupiter</artifactId>
                <version>5.10.2</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

### Module POM

```xml
<!-- my-app-core/pom.xml -->
<project>
    <modelVersion>4.0.0</modelVersion>

    <!-- Inherit from parent -->
    <parent>
        <groupId>com.company.project</groupId>
        <artifactId>my-app-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>my-app-core</artifactId>

    <dependencies>
        <!-- Version inherited from parent's dependencyManagement -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
        </dependency>

        <!-- Reference another module -->
        <dependency>
            <groupId>com.company.project</groupId>
            <artifactId>my-app-core</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
</project>
```

### Multi-Module Directory Structure

```
my-app-parent/
├── pom.xml                    ← Parent POM
├── my-app-core/
│   ├── pom.xml                ← Module POM (parent = my-app-parent)
│   └── src/
│       └── main/java/
├── my-app-api/
│   ├── pom.xml
│   └── src/
│       └── main/java/
└── my-app-web/
    ├── pom.xml
    └── src/
        └── main/java/
```

---

## Maven vs. Gradle

Both are build tools. Here's how they compare:

| Feature | Maven | Gradle |
|---------|-------|--------|
| Configuration | XML (`pom.xml`) | Groovy or Kotlin DSL |
| Learning Curve | Simpler, more standardized | More flexible, steeper curve |
| Speed | Slower (convention-based) | Faster (incremental builds, caching) |
| IDE Support | Excellent | Excellent |
| Dependency Management | Excellent | Excellent |
| Convention | Strict standard layout | Configurable |
| Popularity | Legacy/enterprise projects | Modern Android/enterprise |

### Gradle Equivalent

```groovy
// build.gradle
plugins {
    id 'java'


---

**Continue to Part 2**: [README-part2.md](README-part2.md)