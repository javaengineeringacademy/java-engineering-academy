# Azure Project Structure

## Overview

A well-organized Azure project separates infrastructure, application code, configuration, and deployment pipelines for maintainability and clarity.

## Standard Layout

```
azure-project/
├── infra/
│   ├── main.bicep
│   ├── modules/
│   │   ├── networking.bicep
│   │   ├── compute.bicep
│   │   ├── database.bicep
│   │   └── monitoring.bicep
│   ├── parameters/
│   │   ├── dev.bicepparam
│   │   ├── staging.bicepparam
│   │   └── prod.bicepparam
│   └── modules.bicep
├── src/
│   ├── api/
│   │   ├── Controllers/
│   │   ├── Models/
│   │   └── Program.cs
│   └── web/
│       ├── pages/
│       └── styles/
├── tests/
│   ├── unit/
│   └── integration/
├── pipelines/
│   ├── azure-pipelines.yml
│   └── templates/
│       ├── build.yml
│       └── deploy-infra.yml
├── scripts/
│   ├── setup.sh
│   └── teardown.sh
├── docs/
│   ├── architecture.md
│   └── runbook.md
├── .env.example
├── .gitignore
└── README.md
```

## Bicep Module Organization

### Root Module

The root module (`main.bicep`) orchestrates resource deployment by calling child modules.

```bicep
module networking './modules/networking.bicep' = {
  name: 'deploy-networking'
  params: {
    location: location
    vnetPrefix: vnetPrefix
  }
}

module compute './modules/compute.bicep' = {
  name: 'deploy-compute'
  params: {
    location: location
    subnetId: networking.outputs.subnetId
  }
}
```

### Module Design Principles

- One module per resource group or logical boundary
- Expose outputs needed by dependent modules
- Keep parameters minimal with sensible defaults
- Use `existing` keyword to reference deployed resources

## Environment Configuration

### Parameter Files

Use `.bicepparam` files per environment:

```bicep
using './main.bicep'

param environment = 'dev'
param vmSize = 'Standard_B2s'
param location = 'eastus'
```

### Variable Groups

Azure DevOps variable groups or GitHub Actions secrets store environment-specific values:

- Connection strings and API keys
- Subscription IDs and tenant IDs
- Feature flags
- Scaling parameters

## Infrastructure Separation

### Hub-Spoke Model

Separate hub and spoke virtual networks for enterprise environments:

```
infra/
├── hub/
│   ├── main.bicep
│   └── modules/
│       ├── firewall.bicep
│       ├── bastion.bicep
│       └── vpn.bicep
├── spoke/
│   ├── main.bicep
│   └── modules/
│       ├── vnet.bicep
│       ├── aks.bicep
│       └── databases.bicep
└── shared/
    ├── main.bicep
    └── modules/
        ├── log-analytics.bicep
        └── key-vault.bicep
```

## Secrets Management

### Key Vault Integration

Store secrets in Key Vault and reference them in Bicep:

```bicep
resource keyVault 'Microsoft.KeyVault/vaults@2023-02-01' = {
  name: kvName
  properties: {
    tenantId: subscription().tenantId
    sku: { family: 'A', name: 'standard' }
  }
}

resource secret 'Microsoft.KeyVault/vaults/secrets@2023-02-01' = {
  parent: keyVault
  name: 'dbPassword'
  properties: {
    value: dbPassword
  }
}
```

## Git Branching Strategy

| Branch | Purpose |
|---|---|
| main | Production infrastructure |
| develop | Integration branch |
| feature/* | New features |
| hotfix/* | Emergency fixes |

## Documentation Requirements

- Architecture Decision Records (ADRs) for significant choices
- Runbooks for operational procedures
- README with setup instructions and prerequisites
- Cost estimates for infrastructure changes
