# Azure Installation Guide

## Overview

This guide covers installing Azure tools and setting up your development environment for working with Azure services.

## Azure CLI

### macOS

```bash
brew install azure-cli
```

### Windows

```powershell
winget install Microsoft.AzureCLI
```

### Linux (Debian/Ubuntu)

```bash
curl -sL https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor | \
  sudo tee /etc/apt/trusted.gpg.d/microsoft.gpg > /dev/null
echo "deb [arch=amd64] https://packages.microsoft.com/repos/azure-cli/ $(lsb_release -cs) main" | \
  sudo tee /etc/apt/sources.list.d/azure-cli.list
sudo apt update && sudo apt install azure-cli
```

### Verification

```bash
az --version
```

## Azure PowerShell

### Windows

```powershell
Install-Module -Name Az -Repository PSGallery -Force
```

### macOS / Linux

```bash
pwsh
Install-Module -Name Az -Scope CurrentUser -Repository PSGallery -Force
```

### Import Module

```powershell
Import-Module Az
Connect-AzAccount
```

## Azure Portal

Access the portal at https://portal.azure.com using a Microsoft or organizational account.

### Portal Setup

1. Sign in with Azure AD credentials
2. Set your default directory
3. Pin frequently used resources to the dashboard
4. Configure CLI cloud shell for in-browser terminal access

## Visual Studio Code Extensions

### Azure Tools Extension Pack

Includes:

- Azure Account
- Azure CLI Tools
- Azure Resource Manager Tools (Bicep)
- Azure Functions
- Docker (for container workflows)

### Installation

```
ext install ms-vscode.vscode-azure-account
ext install ms-azuretools.vscode-azurecli
ext install ms-azuretools.vscode-bicep
```

## Azure SDKs

### Python

```bash
pip install azure-identity azure-mgmt-compute azure-storage-blob
```

### JavaScript/TypeScript

```bash
npm install @azure/identity @azure/arm-compute @azure/storage-blob
```

### Java (Maven)

```xml
<dependency>
  <groupId>com.azure</groupId>
  <artifactId>azure-identity</artifactId>
  <version>1.11.0</version>
</dependency>
<dependency>
  <groupId>com.azure</groupId>
  <artifactId>azure-arm-compute</artifactId>
  <version>2.28.0</version>
</dependency>
```

### .NET (NuGet)

```bash
dotnet add package Azure.Identity
dotnet add package Azure.ResourceManager.Compute
```

## Azure Cloud Shell

Cloud Shell is a browser-based terminal pre-authenticated with your Azure account.

### Features

- Bash and PowerShell environments
- Pre-installed tools: az, docker, git, terraform
- 5 GB persistent file share
- No local setup required

### Access

Click the Cloud Shell icon in the portal or visit https://shell.azure.com

## Docker Desktop

Required for container-based Azure development.

```bash
# macOS
brew install --cask docker

# Verify
docker --version
docker login azurecr.io
```

## Authentication Setup

### Service Principal (CI/CD)

```bash
az ad sp create-for-rbac \
  --name "myServicePrincipal" \
  --role "Contributor" \
  --scopes "/subscriptions/SUBSCRIPTION_ID"
```

### Managed Identity (Applications)

Enable system-assigned managed identity on Azure resources to avoid storing credentials in code.

## Verify Installation

Run the following to confirm your environment is ready:

```bash
az login
az account list --output table
az group list --output table
```
