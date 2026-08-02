# Module 12: Build Tools

## Overview

This module covers modern Java build tools and build automation. Students will learn to manage project dependencies, configure build lifecycles, optimize builds, and integrate build processes with CI/CD pipelines using Maven and Gradle.

## Learning Objectives

By the end of this module, you will be able to:

- Create and manage Maven projects with POM files
- Configure Gradle builds using Groovy/Kotlin DSL
- Implement dependency management and resolution
- Create custom build plugins and tasks
- Optimize build performance and caching
- Integrate builds with continuous integration systems
- Handle multi-module project structures

## Prerequisites

- [Module 11: Testing](../11-testing/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [Maven Fundamentals](01-maven-fundamentals/) | 3 hours | POM, coordinates, repositories, lifecycle |
| 02 | [Maven Advanced](02-maven-advanced/) | 3 hours | Profiles, plugins, multi-module builds |
| 03 | [Gradle Fundamentals](03-gradle-fundamentals/) | 3 hours | Build scripts, tasks, dependencies |
| 04 | [Gradle Advanced](04-gradle-advanced/) | 3 hours | Custom tasks, plugins, buildSrc |
| 05 | [Dependency Management](05-dependency-management/) | 2 hours | Version conflicts, exclusions, BOM |
| 06 | [Maven Plugins](06-maven-plugins/) | 2 hours | Plugin development, lifecycle extensions |
| 07 | [Gradle Plugins](07-gradle-plugins/) | 2 hours | Plugin creation, convention plugins |
| 08 | [Build Lifecycle](08-build-lifecycle/) | 2 hours | Phases, goals, execution ordering |
| 09 | [Repositories](09-repositories/) | 1 hour | Central, local, mirror configuration |
| 10 | [Version Management](10-version-management/) | 2 hours | Semantic versioning, release strategies |
| 11 | [Build Optimization](11-build-optimization/) | 2 hours | Caching, incremental builds, parallel execution |
| 12 | [CI/CD Integration](12-ci-cd-integration/) | 2 hours | Jenkins, GitHub Actions, GitLab CI |

## Key Concepts

- Declarative vs. imperative build configuration
- Dependency scopes and transitive dependencies
- Build caching and incremental compilation
- Artifact signing and publishing
- Build reproducibility

## Enterprise Applications

Build tools are the backbone of enterprise Java development, ensuring consistent builds, dependency management, and integration with deployment pipelines across large development teams and complex project structures.

## Estimated Total Time

**27 hours**

## Module Project

Build a **Multi-Module Maven/Gradle Project** that:
- Implements a multi-module project structure
- Customizes build lifecycle with plugins
- Manages dependencies and version conflicts
- Integrates with CI/CD pipelines
- Demonstrates build optimization techniques

## Resources

- [Maven Documentation](https://maven.apache.org/guides/)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)

**Previous Module**: [Module 11: Testing](../11-testing/)
**Next Module**: [Module 13: JDBC & Database](../13-jdbc-database/)