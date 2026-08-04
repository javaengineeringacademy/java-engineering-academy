# Azure Active Directory (Azure AD)

## Overview

Azure AD is a cloud-based identity and access management service.

## Components

| Component       | Description                    |
|-----------------|--------------------------------|
| Identities      | Users, groups, service principals|
| Access Management| RBAC, conditional access      |
| Security        | MFA, Conditional Access        |

## Creating Users

### Azure CLI
```bash
# Create user
az ad user create \
  --display-name "John Doe" \
  --user-principal-name john@mydomain.com \
  --password P@ssw0rd123!

# Create group
az ad group create \
  --display-name "Developers" \
  --mail-nickname developers
```

### ARM Template
```json
{
  "type": "Microsoft.Directory/users",
  "apiVersion": "2021-12-01",
  "name": "john@mydomain.com",
  "properties": {
    "displayName": "John Doe",
    "userPrincipalName": "john@mydomain.com",
    "passwordProfile": {
      "password": "P@ssw0rd123!",
      "forceChangePasswordNextSignIn": true
    }
  }
}
```

## B2C (Business-to-Consumer)

```bash
# Create B2C tenant
az ad b2c tenant create \
  --name myb2ctenant \
  --location eastus \
  --sku-name PremiumP1
```

## Managed Identity

```bash
# Create system-assigned managed identity
az identity create \
  --name myIdentity \
  --resource-group myResourceGroup

# Assign to resource
az identity assign \
  --name myIdentity \
  --resource-group myResourceGroup \
  --scope /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.Compute/virtualMachines/myVM
```

## Conditional Access

```bash
# Create conditional access policy
az ad policy conditional-access create \
  --name "Require MFA" \
  --state "enabled" \
  --conditions '{"users":{"includeUsers":["All"]}}' \
  --grant-controls '{"operator":"OR","builtInControls":["mfa"]}'
```

## App Registrations

```bash
# Create app registration
az ad app create \
  --display-name "My App" \
  --sign-in-audience AzureADMyOrg

# Create service principal
az ad sp create --id {app-id}

# Create client secret
az ad app credential reset \
  --id {app-id} \
  --append
```

## RBAC

```bash
# Assign role
az role assignment create \
  --assignee {user-id} \
  --role "Contributor" \
  --scope /subscriptions/{sub}/resourceGroups/myResourceGroup
```

## Conditional Access Policies

### MFA
```bash
# Require MFA
az ad policy conditional-access create \
  --name "Require MFA" \
  --state "enabled" \
  --conditions '{"users":{"includeUsers":["All"]}}' \
  --grant-controls '{"operator":"OR","builtInControls":["mfa"]}'
```

### Location-based
```bash
# Block non-trusted locations
az ad policy conditional-access create \
  --name "Block Non-Trusted" \
  --state "enabled" \
  --conditions '{"users":{"includeUsers":["All"]},"locations":{"includeLocations":["All"],"excludeLocations":["Trusted"]}}' \
  --grant-controls '{"operator":"OR","builtInControls":["block"]}'
```

## Monitoring

```bash
# Get sign-in logs
az ad sign-in-logs list --top 10

# Get audit logs
az ad audit-logs list --top 10
```

## Security Defaults

```bash
# Enable security defaults
az rest --method PATCH \
  --uri "https://graph.microsoft.com/v1.0/policies/identitySecurityDefaultsEnforcementPolicy" \
  --body '{"isEnabled": true}'
```

## Cost Optimization

- **Use free tier** for basic features
- **Implement proper licensing**
- **Monitor with Azure AD logs**
- **Use managed identities**

## Best Practices

1. **Enable MFA** for all users
2. **Implement conditional access**
3. **Use managed identities**
4. **Implement proper RBAC**
5. **Enable security defaults**
6. **Monitor sign-in logs**
7. **Implement proper app registrations**
8. **Use B2C** for customer identity
9. **Regular security reviews**
10. **Implement proper password policies**
