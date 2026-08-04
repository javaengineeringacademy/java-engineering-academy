# DynamoDB Global Secondary Indexes

## Comprehensive Guide to GSI

Global Secondary Indexes (GSI) enable alternative query patterns. This guide covers GSI design, projection, and best practices.

---

## Table of Contents

1. [GSI Overview](#gsi-overview)
2. [Creating GSI](#creating-gsi)
3. [Projection Types](#projection-types)
4. [Query Patterns](#query-patterns)
5. [Best Practices](#best-practices)

---

## GSI Overview

### GSI vs LSI

| Feature | GSI | LSI |
|---------|-----|-----|
| Key schema | Different from base table | Same partition key |
| Creation | Any time | Only at table creation |
| Size limit | Unlimited | 10 GB per partition key |
| Queries | Full table | Partition key only |

### When to Use GSI

```
- Alternative partition key
- Different sort key
- Sparse indexes
- Global queries across partitions
```

---

## Creating GSI

### AWS CLI

```javascript
aws dynamodb update-table \
    --table-name Users \
    --attribute-definitions \
        AttributeName=email,AttributeType=S \
    --global-secondary-index-updates '[
        {
            "Create": {
                "IndexName": "email-index",
                "KeySchema": [
                    {"AttributeName": "email", "KeyType": "HASH"}
                ],
                "Projection": {"ProjectionType": "ALL"}
            }
        }
    ]'
```

### CloudFormation

```yaml
Resources:
  UsersTable:
    Type: AWS::DynamoDB::Table
    Properties:
      TableName: Users
      AttributeDefinitions:
        - AttributeName: userId
          AttributeType: S
        - AttributeName: email
          AttributeType: S
      KeySchema:
        - AttributeName: userId
          KeyType: HASH
      GlobalSecondaryIndexes:
        - IndexName: email-index
          KeySchema:
            - AttributeName: email
              KeyType: HASH
          Projection:
            ProjectionType: ALL
```

---

## Projection Types

### Projection Types

```javascript
// ALL - All attributes
"Projection": {"ProjectionType": "ALL"}

// KEYS_ONLY - Only keys
"Projection": {"ProjectionType": "KEYS_ONLY"}

// INCLUDE - Specific attributes
"Projection": {
    "ProjectionType": "INCLUDE",
    "NonKeyAttributes": ["name", "email"]
}
```

### Sparse Index

```javascript
// Only items with "verified" attribute are indexed
{
    "userId": "user-123",
    "email": "john@example.com",
    "verified": true  // Only this attribute triggers indexing
}

// GSI on "verified" attribute
// Returns only verified users
```

---

## Query Patterns

### Query GSI

```javascript
// Query by email (using GSI)
aws dynamodb query \
    --table-name Users \
    --index-name email-index \
    --key-condition-expression "email = :email" \
    --expression-attribute-values '{":email": {"S": "john@example.com"}}'
```

### Query with Projection

```javascript
// Query GSI with projection
aws dynamodb query \
    --table-name Users \
    --index-name email-index \
    --key-condition-expression "email = :email" \
    --projection-expression "userId, name" \
    --expression-attribute-values '{":email": {"S": "john@example.com"}}'
```

---

## Best Practices

### 1. Use Sparse Indexes

```javascript
// Sparse GSI for active users only
{
    "userId": "user-123",
    "status": "active",
    "lastLogin": "2024-01-15"
}

// GSI on "lastLogin"
// Only users who have logged in are indexed
```

### 2. Minimize GSI Count

```javascript
// Bad - Too many GSIs
{
    "GSIs": ["email-index", "phone-index", "name-index", ...]
}

// Good - Use sparse indexes
{
    "GSIs": ["primary-access-pattern-index"]
}
```

### 3. Use Projection Wisely

```javascript
// Good - Only needed attributes
"Projection": {
    "ProjectionType": "INCLUDE",
    "NonKeyAttributes": ["name", "email"]
}

// Bad - All attributes
"Projection": {"ProjectionType": "ALL"}
```

### 4. Design for Access Patterns

```javascript
// Query: Get user by email
// GSI: email-index (email -> userId, name)

// Query: Get active users
// GSI: status-index (status -> userId, lastLogin)
```

### 5. Monitor GSI Performance

```javascript
// Check GSI metrics
aws cloudwatch get-metric-statistics \
    --namespace AWS/DynamoDB \
    --metric-name ReadCapacityUnits \
    --dimensions Name=TableName,Value=Users Name=GlobalSecondaryIndexName,Value=email-index
```

---

## Further Reading

- [DynamoDB GSI](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GSI.html)
- [LSI vs GSI](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/lsi-vs-gsi.html)
- [Sparse Indexes](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/bp-sparse-indexes.html)
