# HashiCorp Vault - Secrets Management

## Overview

HashiCorp Vault is a tool for securely managing secrets and protecting sensitive data. It provides a unified interface for managing secrets, encrypting data, controlling access, and auditing secret usage across dynamic infrastructure environments.

## Why It Matters

- Centralizes secrets management across all environments
- Generates dynamic, short-lived credentials to reduce exposure
- Provides encryption as a service for application data
- Enforces access policies with fine-grained authorization
- Maintains audit logs for compliance and security investigations

## Key Concepts

- **Secret Engine**: Backend component that manages a specific type of secret
- **Auth Method**: Mechanism for authenticating users or applications to Vault
- **Policy**: HCL document defining access permissions for secrets and paths
- **Dynamic Secrets**: Credentials generated on-demand with automatic expiration
- **Lease**: Time-limited access to a secret with renewal or revocation
- **Transit Engine**: Encryption as a service for application data

## Core Topics

### Secret Engines
- KV secrets engine for static key-value storage
- Database secrets engine for dynamic database credentials
- PKI secrets engine for certificate authority management
- AWS, Azure, GCP secrets engines for cloud credential generation

### Authentication and Authorization
- AppRole, Kubernetes, and OIDC authentication methods
- Token-based and LDAP authentication
- Policy-based access control for fine-grained permissions

### Data Encryption
- Transit secrets engine for encryption as a service
- Seal/unseal operations for Vault initialization
- Auto-unseal with cloud KMS providers

### Audit and Compliance
- Audit logging for all Vault operations
- Seal status and health monitoring
- Response wrapping for secure token delivery

## Best Practices

1. Use dynamic secrets instead of static credentials whenever possible
2. Implement least-privilege policies for all Vault roles
3. Enable audit logging and monitor for unauthorized access attempts
4. Use auto-unseal with cloud KMS for production deployments
5. Rotate root credentials regularly using Vault's built-in rotation
6. Store Vault configuration and policies in version control

## Hands-on Labs

1. **Vault Setup**: Deploy Vault in development mode and explore the UI
2. **KV Secrets**: Store and retrieve static secrets with versioning
3. **Dynamic Database Secrets**: Configure dynamic MySQL credentials with auto-rotation
4. **AppRole Authentication**: Set up AppRole auth for machine-to-machine access
5. **Transit Encryption**: Encrypt and decrypt application data using the transit engine
6. **Audit Logging**: Enable audit logs and monitor Vault operations

## Interview Questions

1. What are dynamic secrets and why are they preferred over static secrets?
2. Explain the seal/unseal process and why it is necessary
3. How does Vault handle secret rotation for database credentials?
4. What is the transit secrets engine and when would you use it?
5. How would you implement Vault authentication for Kubernetes workloads?
6. Describe the purpose of policies and how they control Vault access
7. How does Vault ensure high availability in production deployments?

## References

- Vault Documentation: https://developer.hashicorp.com/vault/docs
- Vault Tutorials: https://developer.hashicorp.com/vault/tutorials
- Vault API: https://developer.hashicorp.com/vault/api-docs
- Vault Best Practices: https://developer.hashicorp.com/vault/docs/concepts/security
