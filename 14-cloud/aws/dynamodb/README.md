# Amazon DynamoDB

## Overview

Amazon DynamoDB is a fully managed NoSQL database service providing fast and predictable performance with seamless scalability.

## Data Model

```
┌─────────────────────────────────────────────────────────┐
│                    DynamoDB Table                        │
│  ┌─────────┬──────────┬──────────┬──────────┐          │
│  │  PK     │   SK     │ Attribute│ Attribute│          │
│  │ (String)│ (String) │ (String) │ (Number) │          │
│  ├─────────┼──────────┼──────────┼──────────┤          │
│  │ USER#1  │ PROFILE  │ name     │ 25       │          │
│  │ USER#1  │ ORDER#001│ item     │ 99.99    │          │
│  │ USER#2  │ PROFILE  │ name     │ 30       │          │
│  └─────────┴──────────┴──────────┴──────────┘          │
└─────────────────────────────────────────────────────────┘
```

## Primary Keys

| Type              | Description                          |
|-------------------|--------------------------------------|
| Simple Primary    | Partition key only                   |
| Composite Primary | Partition key + Sort key             |

### Create Table
```bash
aws dynamodb create-table \
  --table-name MyTable \
  --attribute-definitions \
    AttributeName=PK,AttributeType=S \
    AttributeName=SK,AttributeType=S \
  --key-schema \
    AttributeName=PK,KeyType=HASH \
    AttributeName=SK,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST
```

## Capacity Modes

### On-Demand (PAY_PER_REQUEST)
```bash
aws dynamodb create-table \
  --table-name MyTable \
  --attribute-definitions \
    AttributeName=PK,AttributeType=S \
  --key-schema AttributeName=PK,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST
```

### Provisioned
```bash
aws dynamodb create-table \
  --table-name MyTable \
  --attribute-definitions \
    AttributeName=PK,AttributeType=S \
  --key-schema AttributeName=PK,KeyType=HASH \
  --provisioned-throughput ReadCapacityUnits=10,WriteCapacityUnits=5
```

### Auto Scaling
```bash
aws application-autoscaling register-scalable-target \
  --service-namespace dynamodb \
  --resource-id table/MyTable \
  --scalable-dimension dynamodb:table:ReadCapacityUnits \
  --min-capacity 5 \
  --max-capacity 1000

aws application-autoscaling put-scaling-policy \
  --service-namespace dynamodb \
  --scalable-dimension dynamodb:table:ReadCapacityUnits \
  --resource-id table/MyTable \
  --policy-name ReadAutoScaling \
  --target-tracking-scaling-policy-configuration '{
    "TargetValue": 70.0,
    "PredefinedMetricSpecification": {
      "PredefinedMetricType": "DynamoDBReadCapacityUtilization"
    }
  }'
```

## Secondary Indexes

### Global Secondary Index (GSI)
```bash
aws dynamodb update-table \
  --table-name MyTable \
  --attribute-definitions \
    AttributeName=GSI_PK,AttributeType=S \
  --global-secondary-index-updates '[
    {
      "Create": {
        "IndexName": "GSI1",
        "KeySchema": [{"AttributeName": "GSI_PK", "KeyType": "HASH"}],
        "Projection": {"ProjectionType": "ALL"}
      }
    }
  ]'
```

### Local Secondary Index (LSI)
```bash
# Must be created at table creation
aws dynamodb create-table \
  --table-name MyTable \
  --attribute-definitions \
    AttributeName=PK,AttributeType=S \
    AttributeName=SK,AttributeType=S \
  --key-schema \
    AttributeName=PK,KeyType=HASH \
    AttributeName=SK,KeyType=RANGE \
  --local-secondary-indexes '[
    {
      "IndexName": "LSI1",
      "KeySchema": [
        {"AttributeName": "PK", "KeyType": "HASH"},
        {"AttributeName": "SK", "KeyType": "RANGE"}
      ],
      "Projection": {"ProjectionType": "ALL"}
    }
  ]'
```

## Query Operations

```bash
# Query
aws dynamodb query \
  --table-name MyTable \
  --key-condition-expression "PK = :pk" \
  --expression-attribute-values '{":pk": {"S": "USER#1"}}'

# Query with filter
aws dynamodb query \
  --table-name MyTable \
  --key-condition-expression "PK = :pk AND SK BETWEEN :start AND :end" \
  --expression-attribute-values '{
    ":pk": {"S": "USER#1"},
    ":start": {"S": "ORDER#001"},
    ":end": {"S": "ORDER#999"}
  }' \
  --filter-expression "amount > :min" \
  --expression-attribute-values '{":min": {"N": "50"}}'
```

## Scan Operations

```bash
# Full scan
aws dynamodb scan --table-name MyTable

# Scan with filter
aws dynamodb scan \
  --table-name MyTable \
  --filter-expression "status = :status" \
  --expression-attribute-values '{":status": {"S": "active"}}'
```

## DynamoDB Streams

```bash
# Enable streams
aws dynamodb update-table \
  --table-name MyTable \
  --stream-specification StreamEnabled=true,StreamViewType=NEW_AND_OLD_IMAGES
```

### Stream View Types
| Type               | Description                    |
|--------------------|--------------------------------|
| KEYS_ONLY          | Only key attributes            |
| NEW_IMAGE          | New item after write           |
| OLD_IMAGE          | Old item before write          |
| NEW_AND_OLD_IMAGES | Both new and old               |

## Global Tables

```bash
# Create global table
aws dynamodb create-table \
  --table-name MyGlobalTable \
  --attribute-definitions \
    AttributeName=PK,AttributeType=S \
  --key-schema AttributeName=PK,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST \
  --global-secondary-indexes '[]' \
  --replica-specifications '[
    {
      "Region": "us-east-1"
    },
    {
      "Region": "eu-west-1"
    }
  ]'
```

### Features
- **Multi-region replication**
- **Single-digit millisecond latency**
- **Automatic conflict resolution**
- **99.999% availability**

## DynamoDB Accelerator (DAX)

```bash
# Create DAX cluster
aws dax create-cluster \
  --cluster-name my-dax-cluster \
  --node-type dax.r5.large \
  --replication-factor 3 \
  --iam-role-arn arn:aws:iam::123456789012:role/DAXRole \
  --security-group-ids sg-12345678 \
  --subnet-group-name my-subnet-group
```

### DAX Benefits
- **Microsecond latency** for reads
- **Write-through cache**
- **Fully managed**
- **Compatible with DynamoDB API**

## Transactions

```python
import boto3
dynamodb = boto3.client('dynamodb')

dynamodb.transact_write_items(
    TransactItems=[
        {
            'Put': {
                'TableName': 'MyTable',
                'Item': {
                    'PK': {'S': 'ORDER#123'},
                    'SK': {'S': 'DETAIL'},
                    'status': {'S': 'processing'}
                }
            }
        },
        {
            'Update': {
                'TableName': 'Inventory',
                'Key': {'PK': {'S': 'PRODUCT#456'}},
                'UpdateExpression': 'SET stock = stock - :val',
                'ExpressionAttributeValues': {':val': {'N': '1'}}
            }
        }
    ]
)
```

## Time to Live (TTL)

```bash
# Enable TTL
aws dynamodb update-time-to-live \
  --table-name MyTable \
  --time-to-live-specification '{
    "Enabled": true,
    "AttributeName": "expiresAt"
  }'
```

## Backup & Restore

```bash
# On-demand backup
aws dynamodb create-backup \
  --table-name MyTable \
  --backup-name my-backup

# Restore from backup
aws dynamodb restore-table-from-backup \
  --target-table-name MyTableRestored \
  --backup-arn arn:aws:dynamodb:us-east-1:123456789012:table/MyTable/backup/0123456789
```

## Best Practices

1. **Use composite primary keys** for flexibility
2. **Implement GSIs** for query patterns
3. **Use on-demand** for unpredictable workloads
4. **Enable DAX** for read-heavy workloads
5. **Implement TTL** for data cleanup
6. **Use DynamoDB Streams** for change capture
7. **Implement transactions** for multi-item operations
8. **Use Global Tables** for multi-region
9. **Monitor with CloudWatch** metrics
10. **Implement point-in-time recovery**
