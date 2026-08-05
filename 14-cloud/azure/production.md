# Azure Production Readiness

## Overview

Running workloads in production on Azure requires careful planning for availability, disaster recovery, backup, and operational excellence.

## Availability Zones

Physical data center locations within a region with independent power, cooling, and networking.

- Deploy across two or more availability zones for high availability
- Use zone-redundant services where available
- Not all regions support availability zones
- Configure availability zones at the resource or load balancer level

### Zone-Redundant Services

- Azure Load Balancer (Standard SKU)
- Azure Managed Disks (zone-redundant)
- Azure Cosmos DB
- Azure SQL Database
- Azure Cache for Redis

## Availability Sets

Group VMs to protect against hardware failures within a single data center.

- **Fault domains** - Separate physical racks (up to 3)
- **Update domains** - Staged OS updates (up to 20)
- Keep VMs in the same availability set for related workloads
- Use proximity placement groups for low-latency requirements

## Disaster Recovery

### Azure Site Recovery

Replicate VMs to a secondary region for disaster recovery.

- Replication frequency: 30 seconds to 15 minutes
- Recovery Point Objective (RPO) based on replication interval
- Recovery Time Objective (RTO) based on failover process
- Test failover without impacting production

### Geo-Redundant Architectures

- Active-passive: Primary region handles traffic, secondary is standby
- Active-active: Both regions handle traffic simultaneously
- Use Traffic Manager or Front Door for global load balancing
- Database replication via Cosmos DB or SQL geo-replication

## Backup Strategies

### Azure Backup

Centralized backup solution for Azure and on-premises workloads.

- VM backup with daily and hourly restore points
- SQL Server backup with point-in-time restore
- File share backup with item-level recovery
- Cross-region restore for disaster recovery

### Backup Policies

| Resource | Frequency | Retention |
|---|---|---|
| VMs | Daily | 30 days |
| SQL Database | Daily + Transaction logs | 35 days |
| File Share | Daily | 30 days |
| Blob Storage | Soft delete | 14-365 days |

### Immutable Backup

Protect against ransomware and accidental deletion:

- Vault-level immutability
- Container-level immutability
- Time-based retention policies

## High Availability Patterns

### Load Balancing

- **Azure Load Balancer** - Layer 4 TCP/UDP load balancing
- **Application Gateway** - Layer 7 HTTP/HTTPS load balancing
- **Azure Front Door** - Global HTTP load balancing with WAF
- **Traffic Manager** - DNS-based global load balancing

### Health Probes

Configure health probes to detect unhealthy instances:

- HTTP probe with status code checks
- TCP probe for port availability
- Custom probe intervals and thresholds
- Unhealthy threshold for instance removal

## Operational Excellence

### Change Management

- Use deployment slots for zero-downtime deployments
- Implement blue-green or canary deployment patterns
- Test infrastructure changes with What-If operations
- Document rollback procedures

### Incident Response

- Define runbooks for common failure scenarios
- Configure alert action groups with escalation paths
- Use Azure Automation for automated remediation
- Maintain communication plans for outages

### Cost Monitoring

- Set up budgets with alert thresholds
- Use Azure Advisor cost recommendations
- Review reserved instance opportunities monthly
- Tag resources for cost allocation

## Security Hardening

- Enable Azure Defender on all resource types
- Apply network security groups with least privilege
- Use Just-In-Time VM access for SSH/RDP
- Enable diagnostic logging for all services
- Regularly review access control assignments
