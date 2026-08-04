# Azure Cosmos DB

## Overview

Azure Cosmos DB is a globally distributed, multi-model database service.

## API Support

| API             | Use Case                    |
|-----------------|-----------------------------|
| Core (SQL)      | JSON documents              |
| MongoDB         | Document databases          |
| Cassandra       | Wide-column stores          |
| Table           | Key-value stores            |
| Gremlin         | Graph databases             |

## Consistency Levels

| Level           | Description                    |
|-----------------|--------------------------------|
| Strong          | Linearizability                |
| Bounded Staleness| K versions or T seconds      |
| Session         | Consistent prefix              |
| Consistent Prefix| Out-of-order writes          |
| Eventual        | No ordering guarantees        |

```bash
# Create account with consistency level
az cosmosdb create \
  --name mycosmosdb \
  --resource-group myResourceGroup \
  --default-consistency-level Session
```

## Creating Accounts

### Azure CLI
```bash
# Create Cosmos DB account
az cosmosdb create \
  --name mycosmosdb \
  --resource-group myResourceGroup \
  --kind GlobalDocumentDB \
  --location regionName=eastus \
  --default-consistency-level Session

# Create database
az cosmosdb sql database create \
  --name mycosmosdb \
  --resource-group myResourceGroup \
  --account-name mycosmosdb \
  --name mydb

# Create container
az cosmosdb sql container create \
  --name mycosmosdb \
  --resource-group myResourceGroup \
  --account-name mycosmosdb \
  --database-name mydb \
  --name mycontainer \
  --partition-key-path "/pk" \
  --throughput 400
```

### ARM Template
```json
{
  "type": "Microsoft.DocumentDB/databaseAccounts",
  "apiVersion": "2023-04-15",
  "name": "mycosmosdb",
  "location": "eastus",
  "properties": {
    "databaseAccountOfferType": "Standard",
    "consistencyPolicy": {
      "defaultConsistencyLevel": "Session"
    }
  }
}
```

## Provisioned vs Serverless

### Provisioned
```bash
# Create with provisioned throughput
az cosmosdb sql container create \
  --account-name mycosmosdb \
  --database-name mydb \
  --name mycontainer \
  --partition-key-path "/pk" \
  --throughput 400
```

### Serverless
```bash
# Create serverless account
az cosmosdb create \
  --name mycosmosdb \
  --resource-group myResourceGroup \
  --kind GlobalDocumentDB \
  --server-version 3.6
```

## Autoscale

```bash
# Create with autoscale
az cosmosdb sql container create \
  --account-name mycosmosdb \
  --database-name mydb \
  --name mycontainer \
  --partition-key-path "/pk" \
  --max-throughput 4000
```

## Multi-Region Writes

```bash
# Enable multi-region writes
az cosmosdb update \
  --name mycosmosdb \
  --resource-group myResourceGroup \
  --enable-multiple-write-locations true \
  --locations regionName=eastus isDefault=true \
  --locations regionName=westus
```

## Global Distribution

```bash
# Add region
az cosmosdb update \
  --name mycosmosdb \
  --resource-group myResourceGroup \
  --locations regionName=eastus isDefault=true \
  --locations regionName=westus \
  --locations regionName=europe
```

## Data Explorer

```bash
# Open Data Explorer
az cosmosdb sql database show \
  --account-name mycosmosdb \
  --resource-group myResourceGroup \
  --name mydb
```

## Backup & Restore

```bash
# Enable continuous backup
az cosmosdb update \
  --name mycosmosdb \
  --resource-group myResourceGroup \
  --backup-policy-type Continuous

# Restore account
az cosmosdb restore \
  --name mycosmosdb-restored \
  --resource-group myResourceGroup \
  --account-name mycosmosdb \
  --restore-timestamp 2024-01-15T10:30:00Z
```

## Change Feed

```csharp
// Read change feed
var container = client.GetContainer("mydb", "mycontainer");
var iterator = container.GetChangeFeedIterator<ChangeFeedProcessorOptions>(
    new ChangeFeedProcessorOptions
    {
        StartFromBeginning = true
    }
);
```

## RBAC

```bash
# Assign role
az cosmosdb sql role assignment create \
  --account-name mycosmosdb \
  --resource-group myResourceGroup \
  --role-definition-name "Cosmos DB Built-in Data Contributor" \
  --principal-id <principal-id> \
  --scope "/dbs/mydb"
```

## Monitoring

```bash
# Get metrics
az monitor metrics list \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.DocumentDB/databaseAccounts/mycosmosdb \
  --metric "TotalRequestUnits"
```

## Cost Optimization

- **Use autoscale** for variable workloads
- **Implement proper partitioning**
- **Use serverless** for dev/test
- **Monitor RU consumption**
- **Optimize queries**

## Best Practices

1. **Choose appropriate API** for use case
2. **Implement proper partitioning**
3. **Use indexed queries**
4. **Implement proper backup** strategy
5. **Enable multi-region** for global apps
6. **Monitor with Azure Monitor**
7. **Implement proper security**
8. **Use stored procedures** for complex operations
9. **Optimize queries** for RU efficiency
10. **Regular capacity reviews**
