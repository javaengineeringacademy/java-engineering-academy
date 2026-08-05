# Azure Configuration

## Overview

Azure resources are configured and deployed using several tools: Azure CLI, PowerShell, ARM templates, Bicep, and Terraform. Each has strengths depending on the use case.

## Azure CLI

The Azure CLI is a cross-platform command-line tool for managing Azure resources.

### Authentication

```bash
az login
az account set --subscription "SUBSCRIPTION_ID"
az account show
```

### Resource Management

```bash
az group create --name myRG --location eastus
az vm create --resource-group myRG --name myVM --image Ubuntu2204 \
  --admin-username azureuser --generate-ssh-keys
az vm list --output table
```

### Output Formats

```bash
az resource list --output json
az resource list --output table
az resource list --output tsv
```

## PowerShell

Azure PowerShell provides cmdlets for Azure management from PowerShell.

### Setup

```powershell
Connect-AzAccount
Set-AzContext -SubscriptionId "SUBSCRIPTION_ID"
```

### Resource Operations

```powershell
New-AzResourceGroup -Name "myRG" -Location "East US"
New-AzVM -ResourceGroupName "myRG" -Name "myVM" `
  -Location "East US" -Image "Ubuntu2204" `
  -Credential (Get-Credential)
Get-AzVM -ResourceGroupName "myRG"
```

## ARM Templates

Azure Resource Manager templates are JSON declarations for declarative resource deployment.

### Template Structure

```json
{
  "$schema": "https://schema.management.azure.com/schemas/2019-04-01/deploymentTemplate.json#",
  "contentVersion": "1.0.0.0",
  "parameters": { },
  "variables": { },
  "resources": [ ],
  "outputs": { }
}
```

### Deployment

```bash
az deployment group create \
  --resource-group myRG \
  --template-file template.json \
  --parameters environment=prod
```

## Bicep

Bicep is a domain-specific language that compiles to ARM templates, offering cleaner syntax and modularity.

### Example Resource

```bicep
param location string = resourceGroup().location
param vmName string = 'myVM'

resource vm 'Microsoft.Compute/virtualMachines@2023-03-01' = {
  name: vmName
  location: location
  properties: {
    hardwareProfile: {
      vmSize: 'Standard_D2s_v3'
    }
    storageProfile: {
      imageReference: {
        publisher: 'Canonical'
        offer: 'UbuntuServer'
        sku: '22_04-lts'
        version: 'latest'
      }
    }
  }
}
```

### Deployment

```bash
az deployment group create \
  --resource-group myRG \
  --template-file main.bicep
```

## Terraform

Terraform by HashiCorp uses HCL for multi-cloud infrastructure provisioning.

### Provider Configuration

```hcl
provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "example" {
  name     = "myRG"
  location = "East US"
}
```

### Workflow

```bash
terraform init
terraform plan -out=tfplan
terraform apply tfplan
```

## Configuration Management

### Azure App Configuration

Centralized configuration service for application settings.

- Feature flags for progressive rollouts
- Key-value pairs with labels and revisions
- Integration with Azure Key Vault references
- Event grid integration for change notifications

### Environment Variables

Use environment-specific configuration through:

- App Service Application Settings
- Function App Settings
- Container Environment Variables
- Managed Identity for secrets
