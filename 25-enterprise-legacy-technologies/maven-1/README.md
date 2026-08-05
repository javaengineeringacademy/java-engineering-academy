# Maven 1.x

## Overview

Maven 1.x, released in 2004, introduced the concept of project object models and convention-over-configuration for Java build management. It established standards for project structure, dependency management, and build lifecycle.

## Project Object Model (POM)

The pom.xml file defines project metadata, dependencies, build configuration, and plugins. Maven uses this centralized configuration to manage all aspects of the build process.

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>myapp</artifactId>
  <version>1.0.0</version>
  
  <dependencies>
    <dependency>
      <groupId>commons-logging</groupId>
      <artifactId>commons-logging</artifactId>
      <version>1.2</version>
    </dependency>
  </dependencies>
</project>
```

## Dependency Management

Maven introduced centralized dependency management with transitive dependency resolution. It downloads dependencies from remote repositories and caches them locally.

## Build Lifecycle

Maven defines a standard build lifecycle with phases: validate, compile, test, package, verify, install, and deploy. Each phase executes a sequence of goals provided by plugins.

## Plugins and Goals

Maven functionality is delivered through plugins, each providing goals bound to lifecycle phases. The compiler, surefire, and jar plugins handle compilation, testing, and packaging.

## Repository Model

Maven uses a hierarchical repository model with local, central, and remote repositories. The central repository (Maven Central) hosts thousands of open-source libraries.

## Limitations of Maven 1

Maven 1.x had limitations including slow performance, complex plugin configuration, and limited customization. These issues were addressed in Maven 2 with improved architecture.

## Migration to Maven 2+

Migrating from Maven 1 involves updating POM files to the new format, adjusting plugin configurations, and verifying dependency resolution behavior matches expectations.
