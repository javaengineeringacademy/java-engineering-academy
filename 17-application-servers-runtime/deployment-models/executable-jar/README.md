# Executable JAR Deployment

## Overview

Executable JARs package Java applications with embedded servers and dependencies into self-contained archives. Spring Boot popularized this model, enabling java -jar application.jar deployment without external application servers.

## Spring Boot Packaging

Spring Boot Maven and Gradle plugins create executable JARs with nested dependency JARs. The bootstrap classloader loads the launcher, which creates a custom classloader for application classes.

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

## Fat JAR Structure

Executable JARs contain all dependencies within the archive. The BOOT-INF directory holds application classes and libraries, while the loader provides the boot mechanism.

## Embedded Server

Spring Boot embeds Tomcat, Jetty, or Undertow within the executable JAR. The embedded server starts automatically when the application runs, eliminating external server configuration.

## Configuration

Configuration uses application.properties or application.yml files in the classpath. External configuration files, environment variables, and command-line arguments override defaults.

## Advantages

- Simple deployment: copy JAR and run
- No application server installation required
- Consistent runtime across environments
- Easy containerization with minimal Dockerfiles

## Limitations

- Larger file size due to embedded dependencies
- Limited to single-application deployments
- Server changes require rebuild and redeploy
- No shared libraries across applications

## Production Considerations

Production deployment includes JVM tuning, health checks, graceful shutdown configuration, and integration with process managers like systemd or supervisor for restart and monitoring.
