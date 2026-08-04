# Azure Key Vault

## Overview

Azure Key Vault is a cloud service for securely storing and accessing secrets, keys, and certificates.

## Components

| Component       | Description                    |
|-----------------|--------------------------------|
| Secrets         | Passwords, connection strings  |
| Keys            | Cryptographic keys             |
| Certificates    | SSL/TLS certificates           |

## Creating Key Vault

### Azure CLI
```bash
# Create key vault
az keyvault create \
  --name mykeyvault \
  --resource-group myResourceGroup \
  --location eastus

# Set secret
az keyvault secret set \
  --vault-name mykeyvault \
  --name mysecret \
  --value "P@ssw0rd123!"

# Set key
az keyvault key create \
  --vault-name mykeyvault \
  --name mykey \
  --kty RSA \
  --size 2048
```

### ARM Template
```json
{
  "type": "Microsoft.KeyVault/vaults",
  "apiVersion": "2022-07-01",
  "name": "mykeyvault",
  "location": "eastus",
  "properties": {
    "tenantId": "{tenant-id}",
    "sku": {
      "family": "A",
      "name": "standard"
    }
  }
}
```

## Secrets

```bash
# Set secret with expiry
az keyvault secret set \
  --vault-name mykeyvault \
  --name mysecret \
  --value "P@ssw0rd123!" \
  --expires 2025-01-01T00:00:00Z

# Get secret
az keyvault secret show \
  --vault-name mykeyvault \
  --name mysecret
```

## Keys

```bash
# Create RSA key
az keyvault key create \
  --vault-name mykeyvault \
  --name mykey \
  --kty RSA \
  --size 2048

# Create EC key
az keyvault key create \
  --vault-name mykeyvault \
  --name myeckey \
  --kty EC \
  --curve P-256

# Backup key
az keyvault key backup \
  --vault-name mykeyvault \
  --name mykey \
  --file ./keybackup.json
```

## Certificates

```bash
# Create certificate
az keyvault certificate create \
  --vault-name mykeyvault \
  --name mycert \
  --policy '{"issuerParameters":{"name":"SelfSigned"},"x509CertificateProperties":{"subject":"CN=mycert","validityInMonths":12}}'

# Import certificate
az keyvault certificate import \
  --vault-name mykeyvault \
  --name mycert \
  --file ./mycert.pfx
```

## Managed Identity

```bash
# Enable managed identity
az keyvault set-policy \
  --name mykeyvault \
  --object-id {managed-identity-principal-id} \
  --secret-permissions get list \
  --key-permissions get list
```

## RBAC

```bash
# Assign Key Vault Administrator
az role assignment create \
  --assignee {user-id} \
  --role "Key Vault Administrator" \
  --scope /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.KeyVault/vaults/mykeyvault
```

## Soft Delete & Purge

```bash
# Enable soft delete
az keyvault update \
  --name mykeyvault \
  --enable-soft-delete true

# Recover deleted secret
az keyvault secret recover \
  --vault-name mykeyvault \
  --name mysecret

# Purge deleted secret
az keyvault secret purge \
  --vault-name mykeyvault \
  --name mysecret
```

## Monitoring

```bash
# Enable diagnostic settings
az monitor diagnostic-settings create \
  --name mylogs \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.KeyVault/vaults/mykeyvault \
  --logs '[{"category":"AuditEvent","enabled":true}]'

# Get key vault metrics
az monitor metrics list \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.KeyVault/vaults/mykeyvault \
  --metric "ServiceApiResult"
```

## Cost Optimization

- **Use appropriate SKU**
- **Implement proper access policies**
- **Monitor with Azure Monitor**
- **Use soft delete** for recovery

## Best Practices

1. **Use managed identities**
2. **Implement proper RBAC**
3. **Enable soft delete**
4. **Use key rotation**
5. **Implement proper auditing**
6. **Monitor with Azure Monitor**
7. **Use certificates** for TLS
8. **Implement proper backup**
9. **Regular security reviews**
10. **Monitor costs**
