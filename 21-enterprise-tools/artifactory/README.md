# JFrog Artifactory - Universal Artifact Management

## Overview

JFrog Artifactory is a universal binary repository manager supporting all major package formats. It provides a single platform for managing artifacts across the entire software supply chain, with features for security scanning, build integration, and multi-site replication.

## Why It Matters

- Universal support for 30+ package formats in a single platform
- End-to-end traceability from source code to production deployment
- Built-in security scanning with JFrog Xray integration
- Multi-site replication for global development teams
- Deep integration with CI/CD tools and DevOps workflows

## Key Concepts

- **Repository**: Logical container for artifacts, defined by package type
- **Local Repository**: Stores internally developed artifacts
- **Remote Repository**: Proxy cache for external package managers
- **Virtual Repository**: Aggregation of multiple repositories under one URL
- **Artifact**: A binary file with metadata stored in Artifactory
- **Build Info**: Metadata about a build, including published artifacts and dependencies

## Core Topics

### Repository Management
- Configuring repositories for different package formats
- Repository layout and path conventions
- Cleanup policies and retention strategies

### Security and Compliance
- CVE scanning with JFrog Xray
- License compliance checking
- Access control and permission targets

### Build Integration
- CI/CD pipeline integration for artifact publishing
- Build info capture for traceability
- Promotion workflows across environments

### Replication and High Availability
- Push and pull replication between instances
- High availability configuration
- Multi-site replication for distributed teams

## Best Practices

1. Use virtual repositories to simplify client configuration
2. Enable Xray scanning to catch vulnerabilities before deployment
3. Implement cleanup policies to manage storage costs
4. Capture build info for complete traceability
5. Use permission targets to restrict access by repository and project
6. Configure replication for disaster recovery and performance

## Hands-on Labs

1. **Artifactory Setup**: Deploy Artifactory with Docker Compose
2. **Package Management**: Configure repositories for Maven, npm, and Docker
3. **Security Scanning**: Integrate Xray and scan for vulnerabilities
4. **CI/CD Integration**: Publish build artifacts with Jenkins and capture build info
5. **Replication Setup**: Configure push replication between two Artifactory instances

## Interview Questions

1. What are the differences between local, remote, and virtual repositories?
2. How does Artifactory support end-to-end build traceability?
3. Explain the role of JFrog Xray in artifact security
4. How would you implement artifact promotion across environments?
5. What are the considerations for multi-site replication in Artifactory?
6. Describe how Artifactory handles Docker image management

## References

- Artifactory Documentation: https://jfrog.com/help/
- JFrog Platform: https://jfrog.com/platform/
- Artifactory REST API: https://jfrog.com/help/r/artifactory-rest-api
- JFrog Xray: https://jfrog.com/xray/
