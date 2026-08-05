# External Configuration Store Pattern

## Overview

The External Configuration Store pattern moves application configuration out of code and configuration files into a centralized, external store. This enables dynamic configuration updates without redeployment, consistent configuration across environments, and centralized management of application settings.

## When to Use

- Managing configuration across multiple environments
- Updating configuration without application redeployment
- Centralizing configuration for microservice architectures
- Implementing feature flags and dynamic feature toggles
- Managing secrets and sensitive configuration separately
- Supporting different configurations per region or deployment

## Implementation

### AWS
- AWS AppConfig for application configuration
- Parameter Store for hierarchical configuration
- Secrets Manager for sensitive configuration
- S3 for configuration file storage

### Azure
- App Configuration for feature flags and settings
- Key Vault for secrets and certificates
- Azure Storage for configuration files
- Configuration Manager for .NET applications

### Google Cloud
- Runtime Configuration API
- Secret Manager for sensitive configuration
- Cloud Storage for configuration files
- Config Connector for infrastructure configuration

### Kubernetes
- ConfigMaps for non-sensitive configuration
- Secrets for sensitive configuration data
- External Secrets Operator for external secret stores
- Spring Cloud Config with Kubernetes backend

## Best Practices

1. Separate secrets from non-sensitive configuration
2. Implement configuration versioning and rollback
3. Cache configuration locally to reduce external dependencies
4. Use push-based updates for time-sensitive configuration changes
5. Implement configuration validation before applying changes
6. Audit configuration changes for compliance
7. Test configuration changes in staging before production

## Interview Questions

1. How do you handle configuration changes without downtime?
2. What is the difference between ConfigMaps and Secrets in Kubernetes?
3. How would you implement configuration rollback for a failed change?
4. Describe strategies for caching external configuration locally.
5. How do you manage configuration across multiple cloud environments?

## References

- External Configuration Store - Microsoft Azure Architecture Center
- AWS AppConfig Documentation
- Azure App Configuration Documentation
- Google Runtime Configuration API
- Kubernetes ConfigMaps and Secrets
- Twelve-Factor App - Config
