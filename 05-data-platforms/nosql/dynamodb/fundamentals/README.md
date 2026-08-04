# DynamoDB Fundamentals

## Comprehensive Guide to Amazon DynamoDB

DynamoDB is a fully managed NoSQL database by AWS. This guide covers tables, items, primary keys, and basic operations.

---

## Table of Contents

1. [DynamoDB Overview](#dynamodb-overview)
2. [Tables and Items](#tables-and-items)
3. [Primary Keys](#primary-keys)
4. [CRUD Operations](#crud-operations)
5. [Best Practices](#best-practices)

---

## DynamoDB Overview

### Architecture

```
+------------------+
|   Application    |
+------------------+
        |
        v
+------------------+
|   DynamoDB       |
|   Service        |
+------------------+
        |
        v
+------------------+
|   Storage        |
|   (SSD)          |
+------------------+
```

### Features

```
- Fully managed
- Single-digit millisecond latency
- Automatic scaling
- Built-in security
- Backup and restore
- DAX (in-memory caching)
```

---

## Tables and Items

### Create Table

```javascript
// AWS CLI
aws dynamodb create-table \
    --table-name Users \
    --attribute-definitions \
        AttributeName=userId,AttributeType=S \
    --key-schema \
        AttributeName=userId,KeyType=HASH \
    --billing-mode PAY_PER_REQUEST
```

### Items

```javascript
// Item structure
{
    "userId": "user-123",
    "name": "John Doe",
    "email": "john@example.com",
    "age": 30,
    "address": {
        "street": "123 Main St",
        "city": "New York"
    },
    "hobbies": ["reading", "gaming"]
}
```

---

## Primary Keys

### Simple Primary Key (Partition Key)

```javascript
// Only partition key
{
    "userId": "user-123"  // Partition key
}
```

### Composite Primary Key (Partition + Sort Key)

```javascript
// Partition key + Sort key
{
    "userId": "user-123",      // Partition key
    "orderId": "2024-01-15-001" // Sort key
}
```

### Key Schema

```javascript
// Simple key
{
    "AttributeName": "userId",
    "KeyType": "HASH"
}

// Composite key
{
    "KeySchema": [
        { "AttributeName": "userId", "KeyType": "HASH" },
        { "AttributeName": "orderId", "KeyType": "RANGE" }
    ]
}
```

---

## CRUD Operations

### Put Item

```javascript
// Put item
aws dynamodb put-item \
    --table-name Users \
    --item '{
        "userId": {"S": "user-123"},
        "name": {"S": "John Doe"},
        "email": {"S": "john@example.com"},
        "age": {"N": "30"}
    }'

// Conditional put (prevent overwrite)
aws dynamodb put-item \
    --table-name Users \
    --item '{...}' \
    --condition-expression "attribute_not_exists(userId)"
```

### Get Item

```javascript
// Get item by primary key
aws dynamodb get-item \
    --table-name Users \
    --key '{"userId": {"S": "user-123"}}'

// Get with projection
aws dynamodb get-item \
    --table-name Users \
    --key '{"userId": {"S": "user-123"}}' \
    --projection-expression "name, email"
```

### Query

```javascript
// Query by partition key
aws dynamodb query \
    --table-name Users \
    --key-condition-expression "userId = :id" \
    --expression-attribute-values '{":id": {"S": "user-123"}}'

// Query with sort key
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id AND orderId > :date" \
    --expression-attribute-values '{
        ":id": {"S": "user-123"},
        ":date": {"S": "2024-01-01"}
    }'
```

### Scan

```javascript
// Scan entire table (expensive)
aws dynamodb scan \
    --table-name Users \
    --filter-expression "age > :age" \
    --expression-attribute-values '{":age": {"N": "25"}}'
```

### Update Item

```javascript
// Update item
aws dynamodb update-item \
    --table-name Users \
    --key '{"userId": {"S": "user-123"}}' \
    --update-expression "SET email = :email, age = :age" \
    --expression-attribute-values '{
        ":email": {"S": "new@example.com"},
        ":age": {"N": "31"}
    }'

// Conditional update
aws dynamodb update-item \
    --table-name Users \
    --key '{"userId": {"S": "user-123"}}' \
    --update-expression "SET age = age + :inc" \
    --condition-expression "age < :max" \
    --expression-attribute-values '{
        ":inc": {"N": "1"},
        ":max": {"N": "100"}
    }'
```

### Delete Item

```javascript
// Delete item
aws dynamodb delete-item \
    --table-name Users \
    --key '{"userId": {"S": "user-123"}}'

// Conditional delete
aws dynamodb delete-item \
    --table-name Users \
    --key '{"userId": {"S": "user-123"}}' \
    --condition-expression "attribute_exists(userId)"
```

---

## Best Practices

### 1. Design for Access Patterns

```javascript
// Query pattern: Get all orders for a user, sorted by date
{
    "userId": "user-123",      // Partition key
    "orderId": "2024-01-15-001" // Sort key (date-based)
}
```

### 2. Use Composite Sort Keys

```javascript
// Enable multiple access patterns
{
    "PK": "USER#user-123",
    "SK": "ORDER#2024-01-15-001"
}

// Query: All orders for user
// Key condition: PK = "USER#user-123" AND begins_with(SK, "ORDER#")
```

### 3. Use Sparse Indexes

```javascript
// GSI with sparse attributes
{
    "userId": "user-123",
    "email": "john@example.com",
    "verified": true  // Only verified users have this
}

// GSI on verified attribute
// Only returns users where verified = true
```

### 4. Use Batch Operations

```javascript
// Batch write
aws dynamodb batch-write-item \
    --request-items '{
        "Users": [
            {"PutRequest": {"Item": {"userId": {"S": "user-1"}}}},
            {"PutRequest": {"Item": {"userId": {"S": "user-2"}}}}
        ]
    }'

// Batch get
aws dynamodb batch-get-item \
    --request-items '{
        "Users": {
            "Keys": [
                {"userId": {"S": "user-1"}},
                {"userId": {"S": "user-2"}}
            ]
        }
    }'
```

### 5. Use DAX for Caching

```javascript
// DAX cluster configuration
{
    "ClusterName": "my-dax-cluster",
    "NodeType": "dax.t3.small",
    "ReplicationFactor": 2
}
```

---

## Further Reading

- [DynamoDB Documentation](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/)
- [DynamoDB Best Practices](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/best-practices.html)
- [DynamoDB Design Patterns](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/bp-general-nosql-design.html)
