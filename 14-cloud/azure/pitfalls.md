# Azure Common Pitfalls

## Overview

Azure offers powerful capabilities but has several common pitfalls that can lead to unexpected costs, security vulnerabilities, or operational issues. Awareness of these helps avoid costly mistakes.

## 1. Accidental Resource Costs

Azure resources incur charges once created, even if unused.

- Always set up budgets and alerts immediately after subscription creation
- Use Azure Policy to deny expensive resource SKUs
- Shut down dev/test VMs outside business hours
- Review the Azure Cost Management blade weekly

## 2. Unsecured Storage Accounts

Publicly accessible storage accounts expose data to the internet.

- Disable public access on all storage accounts
- Use Private Endpoints instead of public endpoints
- Enable soft delete for blob versioning
- Audit access keys regularly

## 3. NSG Rule Misconfigurations

Incorrectly configured Network Security Groups expose resources.

- Avoid 0.0.0.0/0 as a source for inbound rules
- Use service tags instead of IP ranges where possible
- Regularly audit NSG rules with Flow Logs
- Apply NSGs at the subnet level, not just NIC level

## 4. Resource Group Deletion

Deleting a resource group removes all contained resources permanently.

- Enable resource locks on production resource groups
- Use Azure Policy to prevent accidental deletion
- Verify resource group contents before deletion
- Implement soft delete where supported

## 5. Key Vault Secrets in Code

Hardcoded secrets in application code are a security risk.

- Use Managed Identities for Azure resource authentication
- Reference Key Vault secrets via environment variables
- Enable Key Vault logging to track secret access
- Rotate secrets on a regular schedule

## 6. Unbounded Cosmos DB Costs

Cosmos DB costs can escalate quickly without proper management.

- Set max throughput per container or database
- Use autoscale with a defined maximum RU/s
- Monitor Request Unit consumption daily
- Optimize queries to reduce RU consumption

## 7. Ignoring Azure Advisor

Azure Advisor provides actionable recommendations for optimization.

- Review cost recommendations monthly
- Address security recommendations immediately
- Follow performance and reliability suggestions
- Use Advisor scores to track improvement

## 8. Missing Health Probes

Load balancers without health probes route traffic to unhealthy instances.

- Always configure health probes for backend pools
- Set appropriate probe intervals and thresholds
- Test health probe endpoints manually
- Monitor probe failures in metrics

## 9. Forgetting to Scale Down

Over-provisioned resources waste money.

- Configure autoscale rules for variable workloads
- Review VM sizes quarterly and downsize if possible
- Use Azure Advisor right-sizing recommendations
- Implement auto-shutdown for non-production resources

## 10. Mixing Environments in One Subscription

Combining dev, staging, and production in one subscription complicates governance.

- Use separate subscriptions per environment
- Apply different RBAC policies per subscription
- Isolate networking between environments
- Use management groups for cross-subscription governance

## 11. Ignoring Diagnostic Settings

Without diagnostic settings, troubleshooting production issues becomes difficult.

- Enable diagnostic logging on all services
- Send logs to a centralized Log Analytics workspace
- Configure retention policies appropriately
- Test log queries before production issues occur

## 12. Default Quota Limits

Azure subscriptions have default quota limits that can block scaling.

- Request quota increases proactively
- Monitor usage against limits in Azure Portal
- Plan ahead for large-scale deployments
- Document quota limits per subscription

## 13. Not Testing Disaster Recovery

DR procedures that are not tested may fail when needed.

- Conduct DR drills quarterly
- Document step-by-step recovery procedures
- Validate data integrity after failover
- Update DR plans as architecture changes

## 14. Overlooking Service Limits

Each Azure service has limits that affect architecture design.

- Review service limits before production deployment
- Design for horizontal scaling within limits
- Use multiple instances or regions when needed
- Monitor usage approaching limit thresholds

## 15. Neglecting Backup Verification

Backups that are not verified may not be recoverable.

- Test restore procedures regularly
- Verify backup integrity with automated checks
- Document recovery time for each backup type
- Use immutable vaults for ransomware protection
