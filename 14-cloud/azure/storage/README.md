# Azure Blob Storage

## Overview

Azure Blob Storage is a massively scalable object storage service.

## Storage Tiers

| Tier        | Access           | Min Duration | Use Case                    |
|-------------|------------------|--------------|-----------------------------|
| Hot         | Frequent         | None         | Frequently accessed data    |
| Cool        | Infrequent       | 30 days      | Data accessed monthly       |
| Cold        | Rare             | 90 days      | Data accessed quarterly     |
| Archive     | Rare             | 180 days     | Long-term retention         |

## Creating Storage Accounts

### Azure CLI
```bash
# Create storage account
az storage account create \
  --name mystorageaccount \
  --resource-group myResourceGroup \
  --location eastus \
  --sku Standard_LRS \
  --kind StorageV2 \
  --access-tier Hot

# Create container
az storage container create \
  --name mycontainer \
  --account-name mystorageaccount
```

### ARM Template
```json
{
  "type": "Microsoft.Storage/storageAccounts",
  "apiVersion": "2023-01-01",
  "name": "mystorageaccount",
  "location": "eastus",
  "sku": {
    "name": "Standard_LRS"
  },
  "kind": "StorageV2",
  "properties": {
    "accessTier": "Hot"
  }
}
```

## Blob Types

| Type          | Description                    |
|---------------|--------------------------------|
| Block Blob    | Text/binary data               |
| Append Blob   | Append-only data (logs)        |
| Page Blob     | Random read/write (VHDs)       |

## Upload Blobs

```bash
# Upload file
az storage blob upload \
  --container-name mycontainer \
  --name myblob.txt \
  --file ./local-file.txt \
  --account-name mystorageaccount

# Upload directory
az storage blob upload-batch \
  --destination mycontainer \
  --source ./local-directory \
  --account-name mystorageaccount
```

## Download Blobs

```bash
# Download blob
az storage blob download \
  --container-name mycontainer \
  --name myblob.txt \
  --file ./downloaded-file.txt \
  --account-name mystorageaccount

# List blobs
az storage blob list \
  --container-name mycontainer \
  --account-name mystorageaccount
```

## Lifecycle Management

```json
{
  "rules": [
    {
      "name": "MoveToCool",
      "type": "Lifecycle",
      "definition": {
        "actions": {
          "baseBlob": {
            "tierToCool": {
              "daysAfterModificationGreaterThan": 30
            }
          }
        },
        "filters": {
          "blobTypes": ["blockBlob"],
          "prefixMatch": ["logs/"]
        }
      }
    },
    {
      "name": "MoveToArchive",
      "type": "Lifecycle",
      "definition": {
        "actions": {
          "baseBlob": {
            "tierToArchive": {
              "daysAfterModificationGreaterThan": 90
            }
          }
        },
        "filters": {
          "blobTypes": ["blockBlob"]
        }
      }
    }
  ]
}
```

## Versioning

```bash
# Enable versioning
az storage account blob-service-properties update \
  --account-name mystorageaccount \
  --resource-group myResourceGroup \
  --enable-versioning true

# List versions
az storage blob list \
  --container-name mycontainer \
  --account-name mystorageaccount \
  --include v
```

## Soft Delete

```bash
# Enable soft delete
az storage account blob-service-properties update \
  --account-name mystorageaccount \
  --resource-group myResourceGroup \
  --delete-retention-days 7

# Restore deleted blob
az storage blob undelete \
  --container-name mycontainer \
  --name myblob.txt \
  --account-name mystorageaccount
```

## Snapshots

```bash
# Create snapshot
az storage blob snapshot \
  --container-name mycontainer \
  --name myblob.txt \
  --account-name mystorageaccount

# List snapshots
az storage blob list \
  --container-name mycontainer \
  --account-name mystorageaccount \
  --include s
```

## Encryption

### Microsoft-Managed Keys
```bash
# Default encryption
az storage account create \
  --name mystorageaccount \
  --resource-group myResourceGroup \
  --encryption-services blob
```

### Customer-Managed Keys
```bash
# Create with CMK
az storage account create \
  --name mystorageaccount \
  --resource-group myResourceGroup \
  --encryption-key-source Microsoft.Keyvault \
  --encryption-key-vault https://mykeyvault.vault.azure.net \
  --encryption-key-name mykey
```

## Access Tiers

```bash
# Set access tier
az storage blob set-tier \
  --container-name mycontainer \
  --name myblob.txt \
  --tier Cool \
  --account-name mystorageaccount
```

## Static Website Hosting

```bash
# Enable static website
az storage blob service-properties update \
  --account-name mystorageaccount \
  --resource-group myResourceGroup \
  --static-website true \
  --index-document index.html \
  --404-document 404.html
```

## CORS Configuration

```bash
# Set CORS rules
az storage blob service-properties update \
  --account-name mystorageaccount \
  --resource-group myResourceGroup \
  --cors '[{"allowedMethods":"GET","allowedOrigins":"*","allowedHeaders":"*","maxAge":3600}]'
```

## Monitoring

```bash
# Get storage metrics
az monitor metrics list \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.Storage/storageAccounts/mystorageaccount \
  --metric "Ingress"
```

## Cost Optimization

- **Use appropriate access tiers**
- **Implement lifecycle policies**
- **Delete unused blobs**
- **Use soft delete** for recovery
- **Monitor with Azure Monitor**

## Best Practices

1. **Use lifecycle management**
2. **Enable versioning** for data protection
3. **Use soft delete** for recovery
4. **Implement encryption**
5. **Use CDN** for content delivery
6. **Monitor with Azure Monitor**
7. **Implement proper access controls**
8. **Use managed identities**
9. **Regular security reviews**
10. **Monitor costs**
