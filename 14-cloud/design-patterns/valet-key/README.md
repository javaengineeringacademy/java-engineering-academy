# Valet Key Pattern

## Overview

The Valet Key pattern provides clients with limited, time-bound access tokens to specific resources. Instead of routing all traffic through the application, clients receive a token that grants direct access to a storage service or resource for a defined duration and scope. This offloads data transfer from the application and reduces costs.

## When to Use

- Uploading or downloading large files directly from cloud storage
- Reducing bandwidth costs by bypassing application servers
- Providing temporary access to protected resources
- Implementing pre-signed URLs for file operations
- Granting limited third-party access to resources
- Offloading static content delivery from application servers

## Implementation

### AWS
- S3 pre-signed URLs for object upload/download
- CloudFront signed URLs and cookies
- STS temporary credentials for cross-account access
- Transfer Acceleration for large file uploads

### Azure
- Azure Blob Storage SAS tokens
- Azure CDN signed URLs
- Azure AD managed identities with limited scopes
- Azure Files shared access signatures

### Google Cloud
- Cloud Storage signed URLs
- IAM conditions for fine-grained access
- Workload Identity for GKE pod access
- Cloud CDN signed URLs

### Kubernetes
- Custom token generation service
- OIDC tokens with limited scopes
- Kubernetes service account token projections
- Vault for dynamic secret generation

## Best Practices

1. Set minimum necessary expiration times for tokens
2. Restrict token permissions to the narrowest scope required
3. Use HTTPS for all token generation and delivery
4. Monitor token usage for suspicious activity
5. Implement token revocation mechanisms when needed
6. Log token generation events for audit trails
7. Rotate signing keys regularly

## Interview Questions

1. What is the security model behind pre-signed URLs?
2. How do you handle token revocation before expiration?
3. Compare valet keys with traditional authentication approaches.
4. What information should be encoded in a valet key?
5. How do you prevent token replay attacks?

## References

- Valet Key Pattern - Microsoft Azure Architecture Center
- Amazon S3 Pre-Signed URLs
- Azure Blob Storage SAS Documentation
- Google Cloud Signed URLs
- OAuth 2.0 RFC 6749
- Cloud Design Patterns - Microsoft Azure Architecture Center
