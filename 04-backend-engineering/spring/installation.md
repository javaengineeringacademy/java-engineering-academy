# Spring Installation

## Spring Initializr

### Web Interface

1. Visit https://start.spring.io
2. Select project type (Maven/Gradle)
3. Choose Spring Boot version
4. Set project metadata
5. Select dependencies
6. Generate and download

### Using CLI

```bash
# Install Spring Boot CLI
sdk install springboot

# Create project
spring init --name myapp --dependencies web,data-jpa,mysql myapp
```

## Maven

### pom.xml Setup

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>myapp</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>myapp</name>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### Build and Run

```bash
# Build
mvn clean package

# Run
mvn spring-boot:run

# Run with profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Package as JAR
mvn clean package -DskipTests
java -jar target/myapp-0.0.1-SNAPSHOT.jar
```

## Gradle

### build.gradle Setup

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.2.0'
    id 'io.spring.dependency-management' version '1.1.4'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'

java {
    sourceCompatibility = '17'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'com.mysql:mysql-connector-j'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

### Build and Run

```bash
# Build
./gradlew build

# Run
./gradlew bootRun

# Run with profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Package as JAR
./gradlew bootJar
java -jar build/libs/myapp-0.0.1-SNAPSHOT.jar
```

## Spring Boot CLI

### Installation

```bash
# SDKMAN (recommended)
sdk install springboot

# Homebrew
brew tap spring-io/tap
brew install spring-boot

# Manual download
wget https://repo.spring.io/release/org/springframework/boot/spring-boot-cli-3.2.0-bin.zip
unzip spring-boot-cli-3.2.0-bin.zip
export PATH=$PATH:/path/to/spring-3.2.0/bin
```

### Usage

```bash
# Run Groovy script
spring run app.groovy

# Create project
spring init --dependencies web,actuator myapp

# Run with options
spring run -- --server.port=9090 app.groovy
```

## IDE Setup

### IntelliJ IDEA

1. Install Spring Boot plugin (built-in in Ultimate)
2. File > New > Project > Spring Initializr
3. Configure project settings
4. Select dependencies
5. Create project

### VS Code

1. Install Java Extension Pack
2. Install Spring Boot Extension
3. Use `Ctrl+Shift+P` > "Spring Initializr"
4. Configure and create project

### Eclipse

1. Install Spring Tools 4
2. File > New > Spring Starter Project
3. Configure project settings
4. Select dependencies
5. Create project

## Docker

### Dockerfile

```dockerfile
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/myapp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - DB_HOST=mysql
    depends_on:
      - mysql
  
  mysql:
    image: mysql:8
    environment:
      - MYSQL_ROOT_PASSWORD=password
      - MYSQL_DATABASE=mydb
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql

volumes:
  mysql-data:
```

## Verify Installation

```bash
# Check Java version
java -version

# Check Maven
mvn -version

# Check Gradle
./gradlew -version

# Check Spring Boot
spring --version
```

## Quick Start

```bash
# Create new project
spring init --dependencies web myapp

# Navigate to project
cd myapp

# Run application
mvn spring-boot:run

# Test endpoint
curl http://localhost:8080
```
