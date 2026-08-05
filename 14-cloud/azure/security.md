# Azure Security

## Overview

Azure provides a comprehensive security portfolio spanning identity, network, data, and application protection. Security is a shared responsibility between Microsoft and the customer.

## Azure Active Directory (Entra ID)

Azure AD is the identity and access management service for Azure and Microsoft 365.

### Authentication Methods

- Password-based authentication
- Multi-Factor Authentication (MFA)
- Passwordless authentication (FIDO2, Windows Hello)
- Certificate-based authentication
- Managed Identities for Azure resources

### Conditional Access

Policy-based access control that evaluates signals:

- User location and device compliance
- Application sensitivity
- Risk level detection
- Sign-in frequency requirements

### RBAC Roles

Built-in roles for access control:

| Role | Scope | Purpose |
|---|---|---|
| Owner | Subscription/RG | Full control |
| Contributor | Subscription/RG | Create and manage |
| Reader | Subscription/RG | View only |
| User Access Admin | Subscription/RG | Manage user access |

## Azure Key Vault

Centralized secret management for keys, certificates, and connection strings.

### Vault Types

- **Standard** - Software-protected keys
- **Premium** - HSM-protected keys

### Best Practices

- Enable soft delete and purge protection
- Use managed identities for application access
- Rotate secrets and certificates regularly
- Enable logging for all vault operations
- Separate vaults per environment

## Microsoft Defender for Cloud

Unified security management for Azure and multi-cloud workloads.

### Defender Plans

- **Defender for Servers** - OS-level threat detection
- **Defender for SQL** - Database vulnerability assessment
- **Defender for Storage** - Anomaly detection on storage
- **Defender for Containers** - Kubernetes threat protection
- **Defender for App Service** - Web app vulnerability scanning

### Secure Score

Measure security posture with recommendations for improvement. Targets include:

- Identity and access management
- Data protection
- Network security
- App security
- Endpoint protection

## Network Security

### Network Security Groups (NSGs)

Filter inbound and outbound traffic to Azure resources using rules based on:

- Source/destination IP addresses
- Port numbers
- Protocol type
- Priority-based rule evaluation

### Azure Firewall

Managed, cloud-based network security service protecting Azure Virtual Network resources.

- Stateful firewall as a service
- Built-in high availability and unrestricted cloud scalability
- Application FQDN filtering rules
- Network traffic filtering and NAT

### DDoS Protection

- Basic protection enabled by default
- Standard protection with advanced analytics and alerts
- Integration with Application Gateway and CDN

## Azure Policy

Governance rules that enforce organizational standards.

### Policy Effects

- **Deny** - Block non-compliant resources
- **Audit** - Log non-compliance without blocking
- **DeployIfNotExists** - Auto-deploy required resources
- **Modify** - Add tags or properties to resources

### Initiative Examples

- Require encryption on storage accounts
- Enforce VM naming conventions
- Restrict allowed VM SKUs
- Require HTTPS for web applications

## Data Protection

### Encryption

- Data at rest: AES-256 encryption by default
- Data in transit: TLS 1.2+ enforced
- Customer-managed keys in Key Vault
- Double encryption for compliance requirements

### Backup

- Azure Backup for VMs, SQL, and file shares
- Geo-redundant backup for disaster recovery
- Immutable vault for ransomware protection

## Compliance

Azure supports 90+ compliance certifications including:

- SOC 1/2/3
- ISO 27001/27018
- HIPAA/HITECH
- FedRAMP High
- PCI DSS Level 1
- GDPR
