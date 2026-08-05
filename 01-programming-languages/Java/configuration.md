# Java Configuration

> JVM flags, build tool configuration, Spring properties, and environment variables.

## JVM Configuration

### Memory Flags

| Flag | Description | Example |
|------|-------------|---------|
| `-Xms` | Initial heap size | `-Xms512m` |
| `-Xmx` | Maximum heap size | `-Xmx4g` |
| `-Xmn` | Young generation size | `-Xmn256m` |
| `-Xss` | Thread stack size | `-Xss512k` |
| `-XX:MaxMetaspaceSize` | Metaspace limit | `-XX:MaxMetaspaceSize=256m` |
| `-XX:MaxDirectMemorySize` | Direct buffer limit | `-XX:MaxDirectMemorySize=1g` |

### GC Flags

```bash
# G1 GC (default Java 9+)
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:G1HeapRegionSize=16m
-XX:InitiatingHeapOccupancyPercent=45
-XX:G1ReservePercent=10

# ZGC (ultra-low latency)
-XX:+UseZGC
-XX:+ZGenerational

# Shenandoah
-XX:+UseShenandoahGC
-XX:ShenandoahGCHeuristics=compact
```

### Diagnostic Flags

```bash
# Heap dumps
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/tmp/heapdump.hprof

# GC logging (Java 9+)
-Xlog:gc*:file=gc.log:time,uptime,level,tags
-Xlog:gc+ref=debug

# Thread dumps
-XX:+UnlockDiagnosticVMOptions
-XX:+PreserveFramePointer
```

### Container-Aware JVM

```bash
# Java 10+ auto-detects container limits
-XX:+UseContainerSupport
-XX:MaxRAMPercentage=75.0
-XX:InitialRAMPercentage=50.0
-XX:ActiveProcessorCount=4
```

## Maven Configuration

### pom.xml Essentials

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>3.2.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <compilerArgs>
                        <arg>--enable-preview</arg>
                    </compilerArgs>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <profiles>
        <profile>
            <id>dev</id>
            <properties>
                <spring.profiles.active>dev</spring.profiles.active>
            </properties>
        </profile>
        <profile>
            <id>prod</id>
            <properties>
                <spring.profiles.active>prod</spring.profiles.active>
            </properties>
        </profile>
    </profiles>
</project>
```

### Maven Commands

```bash
mvn clean install                    # Build and install
mvn clean package -DskipTests       # Package without tests
mvn clean verify -Pprod              # Build with profile
mvn dependency:tree                  # Show dependency tree
mvn versions:display-dependency-updates  # Check updates
mvn spring-boot:run                  # Run Spring Boot app
```

## Gradle Configuration

### build.gradle (Kotlin DSL)

```kotlin
plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.google.guava:guava:32.1.3-jre")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.example.Application"
    }
}

tasks.bootJar {
    archiveBaseName.set("my-app")
    archiveVersion.set("1.0.0")
}
```

### Gradle Commands

```bash
./gradlew build                    # Build project
./gradlew bootRun                  # Run Spring Boot
./gradlew bootJar                  # Create executable JAR
./gradlew dependencies             # Show dependency tree
./gradlew dependencyUpdates        # Check updates
./gradlew test --info              # Run tests with output
```

## Spring Boot Configuration

### application.yml

```yaml
server:
  port: 8080
  servlet:
    context-path: /api
  tomcat:
    max-threads: 200
    min-spare-threads: 20
    connection-timeout: 10s

spring:
  profiles:
    active: dev
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: ${DB_USER}
    password: ${DB_PASS}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 30m
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true

logging:
  level:
    root: INFO
    com.example: DEBUG
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

### Profile-Specific Config

```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:h2:mem:devdb
  h2:
    console:
      enabled: true

# application-prod.yml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST}:5432/${DB_NAME}
    hikari:
      maximum-pool-size: 50
```

## Environment Variables

```bash
# Common Java environment variables
export JAVA_HOME=/usr/lib/jvm/java-21
export PATH=$JAVA_HOME/bin:$PATH
export GRADLE_HOME=/opt/gradle
export MAVEN_OPTS="-Xmx1024m -Xms512m"
export JAVA_OPTS="-Xmx2g -XX:+UseG1GC"

# Spring Boot environment variables
export SPRING_PROFILES_ACTIVE=prod
export SERVER_PORT=8443
export DB_HOST=localhost
export DB_USER=admin
export DB_PASS=secret

# Using in code
String port = System.getenv().getOrDefault("SERVER_PORT", "8080");
```

### .env File

```env
# .env (add to .gitignore)
JAVA_HOME=/usr/lib/jvm/java-21
SPRING_PROFILES_ACTIVE=dev
DB_HOST=localhost
DB_PORT=5432
DB_NAME=mydb
DB_USER=devuser
DB_PASS=devpass
```

## JNDI Configuration

```java
// Lookup JNDI resources
Context ctx = new InitialContext();
DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mydb");
Connection conn = ds.getConnection();
```

```xml
<!-- context.xml for Tomcat -->
<Context>
    <Resource name="jdbc/mydb"
              auth="Container"
              type="javax.sql.DataSource"
              maxTotal="20"
              driverClassName="org.postgresql.Driver"
              url="jdbc:postgresql://localhost:5432/mydb"/>
</Context>
```

## References

- [JVM Flags Reference](https://docs.oracle.com/en/java/javase/21/docs/specs/man/java.html)
- [Maven Configuration](https://maven.apache.org/guides/)
- [Gradle Configuration](https://docs.gradle.org/current/userguide/userguide.html)
- [Spring Boot Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)

---
**Prerequisites:** [Java installation](installation.md) | [Java core-concepts](core-concepts.md)
**Related:** [Java performance](performance.md) | [Java production](production.md)
**Next:** [Java project-structure](project-structure.md)
