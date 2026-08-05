# Nexus Repository Manager - Binary Repository Management

## Overview

Nexus Repository Manager is a repository manager for storing and managing binary artifacts. It supports Maven, npm, Docker, PyPI, and other package formats, serving as a central hub for software components used in build and deployment processes.

## Why It Matters

- Provides a single source of truth for all build artifacts and dependencies
- Caches remote dependencies to improve build speed and reliability
- Enforces security policies by scanning components for vulnerabilities
- Supports proxying public repositories to control dependency access
- Enables artifact promotion workflows across development stages

## Key Concepts

- **Repository**: Storage location for artifacts, categorized by format and type
- **Hosted Repository**: Repository storing internal project artifacts
- **Proxy Repository**: Repository caching artifacts from remote sources
- **Group Repository**: Virtual repository combining multiple repositories
- **Component**: An individual artifact with metadata and version information
- **Blob Store**: Physical storage backend for artifact binaries

## Core Topics

### Repository Types and Formats
- Maven, npm, Docker, PyPI, NuGet, and other supported formats
- Hosted, proxy, and group repository configuration
- Repository cleanup policies for managing storage

### Security and Access Control
- Role-based access control for repository access
- Vulnerability scanning with IQ Server integration
- Anonymous access configuration for public repositories

### High Availability and Scaling
- Nexus cluster configuration for high availability
- Blob store optimization for large-scale deployments
- Backup and recovery strategies

### Integration and Automation
- CI/CD pipeline integration for artifact publishing
- REST API for programmatic repository management
- Webhook notifications for component events

## Best Practices

1. Use proxy repositories to cache external dependencies and reduce external traffic
2. Implement cleanup policies to manage repository storage and retention
3. Enable security scanning to block vulnerable components
4. Separate repositories by format and lifecycle stage
5. Use role-based access control to restrict repository access
6. Regularly backup blob stores and database configurations

## Hands-on Labs

1. **Nexus Installation**: Deploy Nexus using Docker Compose
2. **Maven Repository**: Configure a hosted Maven repository and publish artifacts
3. **Proxy Repository**: Set up proxy repositories for Maven Central and npm
4. **Docker Registry**: Configure Nexus as a Docker registry for container images
5. **Access Control**: Create roles and users with repository-specific permissions

## Interview Questions

1. What is the difference between hosted, proxy, and group repositories?
2. How does Nexus improve build performance and reliability?
3. Explain artifact promotion and how it supports deployment workflows
4. How would you manage repository storage and retention in Nexus?
5. What security features does Nexus provide for artifact management?
6. Describe a strategy for backing up and recovering Nexus repositories

## References

- Nexus Repository Documentation: https://help.sonatype.com/
- Nexus Repository Manager 3: https://www.sonatype.com/products/repository-oss
- Nexus REST API: https://help.sonatype.com/repomanager3/rest-and-integration-api
- Repository Formats: https://help.sonatype.com/repomanager3/formats
