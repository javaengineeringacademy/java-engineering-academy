# Azure Architecture

## Overview

Azure organizes resources hierarchically: Management Groups, Subscriptions, Resource Groups, and Resources. Understanding this hierarchy is essential for governance, cost management, and access control.

## Resource Hierarchy

```
Tenant (AAD)
  └─ Management Group
       └─ Subscription
            └─ Resource Group
                 └─ Resource (VM, DB, etc.)
```

## Management Groups

Management groups provide a governance scope above subscriptions. They help organize subscriptions into containers for applying governance policies.

- Up to six levels of depth (excluding root and subscription)
- Root management group is the Tenant Root Group
- Policies and RBAC are inherited by children

## Subscriptions

Subscriptions are logical containers for billing, resource organization, and access control. Common subscription types include:

- **Production** - Stable workloads
- **Development/Testing** - Non-production environments
- **Sandbox** - Isolated experimentation
- **Landing Zone** - Pre-configured for enterprise adoption

## Resource Groups

Resource groups are logical containers grouping related Azure resources. Key behaviors:

- Resources can exist in only one resource group
- Resource groups do not enforce naming on resources
- Deleting a group deletes all contained resources
- Group-level RBAC applies to all resources within

## Regions and Availability Zones

Azure operates in 60+ regions worldwide. Each region contains one or more availability zones with physically separate data centers.

- Choose regions based on compliance, latency, and service availability
- Availability zones protect against data center failures
- Paired regions support disaster recovery scenarios

## Resource Providers

Resource providers define resource types and operations. Examples:

| Provider | Resource Types |
|---|---|
| Microsoft.Compute | virtualMachines, disks |
| Microsoft.Storage | storageAccounts, containers |
| Microsoft.Sql | servers, databases |
| Microsoft.Network | virtualNetworks, loadBalancers |

## Azure Resource Manager (ARM)

ARM is the deployment and management service for Azure. It provides a consistent management layer for creating, updating, and deleting resources via:

- ARM REST API
- Azure CLI / PowerShell
- ARM templates and Bicep
- Azure Portal

## Tags and Metadata

Tags are key-value pairs for organizing and filtering resources. Common use cases:

- Cost allocation by project or department
- Environment identification (dev, staging, prod)
- Automated compliance scanning
- Owner identification

## Naming Conventions

Follow consistent naming patterns for Azure resources. A typical pattern:

```
{project}-{environment}-{resource-type}-{region}-{instance}
Example: myapp-prod-webapp-eastus-001
```

## Best Practices

- Use management groups for multi-subscription governance
- Separate production and non-production into different subscriptions
- Apply resource locks on critical resources
- Use Azure Policy for compliance enforcement
- Tag all resources for cost tracking
