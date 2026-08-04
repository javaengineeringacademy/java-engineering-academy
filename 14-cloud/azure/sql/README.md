# Azure SQL Database

## Overview

Azure SQL Database is a fully managed relational database service.

## Service Tiers

| Tier           | Use Case                    | DTUs/vCores  |
|----------------|-----------------------------|--------------|
| Basic          | Dev/test                    | 5 DTUs       |
| Standard       | Small apps                  | 10-100 DTUs  |
| Premium        | Mission-critical            | 125-4000 DTUs|
| General Purpose | Most workloads             | 2-128 vCores |
| Business Crit. | High availability           | 2-128 vCores |
| Hyperscale     | Large databases             | 2-128 vCores |

## Creating Databases

### Azure CLI
```bash
# Create SQL server
az sql server create \
  --name myserver \
  --resource-group myResourceGroup \
  --location eastus \
  --admin-user adminuser \
  --admin-password P@ssw0rd123!

# Create database
az sql db create \
  --resource-group myResourceGroup \
  --server myserver \
  --name mydb \
  --service-tier GeneralPurpose \
  --family Gen5 \
  --capacity 2
```

### ARM Template
```json
{
  "type": "Microsoft.Sql/servers",
  "apiVersion": "2021-11-01",
  "name": "myserver",
  "location": "eastus",
  "properties": {
    "administratorLogin": "adminuser",
    "administratorLoginPassword": "P@ssw0rd123!"
  }
}
```

## Elastic Pools

```bash
# Create elastic pool
az sql elastic-pool create \
  --resource-group myResourceGroup \
  --server myserver \
  --name mypool \
  --edition GeneralPurpose \
  --family Gen5 \
  --capacity 2

# Add database to pool
az sql db update \
  --resource-group myResourceGroup \
  --server myserver \
  --name mydb \
  --elastic-pool mypool
```

## High Availability

### Business Critical Tier
```bash
# Create with zone redundancy
az sql db create \
  --resource-group myResourceGroup \
  --server myserver \
  --name mydb \
  --edition BusinessCritical \
  --family Gen5 \
  --capacity 2 \
  --zone-redundant
```

### Failover Groups
```bash
# Create failover group
az sql failover-group create \
  --resource-group myResourceGroup \
  --server myserver \
  --name myfailovergroup \
  --partner-server myserver-secondary \
  --databases mydb
```

## Read Replicas

```bash
# Create read replica
az sql db replica create \
  --resource-group myResourceGroup \
  --server myserver \
  --name mydb \
  --partner-resource-group myResourceGroup \
  --partner-server myserver-secondary
```

## Backups

```bash
# Create backup
az sql db export \
  --resource-group myResourceGroup \
  --server myserver \
  --name mydb \
  --storage-key "StorageAccountKey" \
  --storage-key-type StorageAccessKey \
  --storage-uri "https://mystorageaccount.blob.core.windows.net/backups/mydb.bacpac" \
  --admin-user adminuser \
  --admin-password P@ssw0rd123!

# Restore from backup
az sql db import \
  --resource-group myResourceGroup \
  --server myserver \
  --name mydb \
  --storage-key "StorageAccountKey" \
  --storage-key-type StorageAccessKey \
  --storage-uri "https://mystorageaccount.blob.core.windows.net/backups/mydb.bacpac" \
  --admin-user adminuser \
  --admin-password P@ssw0rd123!
```

## Security

### Transparent Data Encryption
```bash
# Enable TDE
az sql db tde set \
  --server myserver \
  --name mydb \
  --status Enabled
```

### Auditing
```bash
# Enable auditing
az sql server audit-policy update \
  --resource-group myResourceGroup \
  --server myserver \
  --state Enabled \
  --storage-account mystorageaccount
```

### Advanced Threat Protection
```bash
# Enable ATP
az sql db threat-policy update \
  --resource-group myResourceGroup \
  --server myserver \
  --name mydb \
  --state Enabled
```

## Monitoring

```bash
# Get database metrics
az monitor metrics list \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.Sql/servers/myserver/databases/mydb \
  --metric "dtu_consumption_percent"

# Query performance insights
az sql db show-connection-string \
  --client ado.net
```

## Cost Optimization

- **Use elastic pools** for multiple databases
- **Right-size DTUs/vCores** based on usage
- **Use serverless** for variable workloads
- **Implement auto-pause** for dev/test

## Best Practices

1. **Use Business Critical** for production
2. **Implement failover groups**
3. **Enable auditing** and threat protection
4. **Use managed identities**
5. **Implement proper backup** strategy
6. **Monitor performance** regularly
7. **Use elastic pools** appropriately
8. **Implement proper security**
9. **Regular performance tuning**
10. **Monitor costs**
