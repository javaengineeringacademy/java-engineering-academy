# DynamoDB Queries

## Comprehensive Guide to DynamoDB Query and Scan

DynamoDB provides query and scan operations for reading data. This guide covers query patterns, condition expressions, and optimization.

---

## Table of Contents

1. [Query vs Scan](#query-vs-scan)
2. [Query Operations](#query-operations)
3. [Condition Expressions](#condition-expressions)
4. [Pagination](#pagination)
5. [Best Practices](#best-practices)

---

## Query vs Scan

### Comparison

| Feature | Query | Scan |
|---------|-------|------|
| Performance | Fast (indexed) | Slow (full scan) |
| Cost | Low | High |
| Use case | Known partition key | Unknown partition key |
| Filter | Key conditions | Filter expressions |

### When to Use Query

```javascript
// Good - Known partition key
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id" \
    --expression-attribute-values '{":id": {"S": "user-123"}}'
```

### When to Use Scan

```javascript
// Last resort - Full table scan
aws dynamodb scan \
    --table-name Users \
    --filter-expression "age > :age" \
    --expression-attribute-values '{":age": {"N": "25"}}'
```

---

## Query Operations

### Basic Query

```javascript
// Query by partition key
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id" \
    --expression-attribute-values '{":id": {"S": "user-123"}}'
```

### Query with Sort Key

```javascript
// Query with range condition
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id AND orderId BETWEEN :start AND :end" \
    --expression-attribute-values '{
        ":id": {"S": "user-123"},
        ":start": {"S": "2024-01-01"},
        ":end": {"S": "2024-12-31"}
    }'
```

### Query with Projection

```javascript
// Return only specific attributes
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id" \
    --projection-expression "orderId, total, status" \
    --expression-attribute-values '{":id": {"S": "user-123"}}'
```

### Query with Filter

```javascript
// Filter after query
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id" \
    --filter-expression "#status = :status" \
    --expression-attribute-names '{"#status": "status"}' \
    --expression-attribute-values '{
        ":id": {"S": "user-123"},
        ":status": {"S": "completed"}
    }'
```

---

## Condition Expressions

### Comparison Operators

```javascript
// Equal
"attribute_name = :value"

// Not equal
"attribute_name <> :value"

// Greater than
"attribute_name > :value"

// Less than
"attribute_name < :value"

// Between
"attribute_name BETWEEN :low AND :high"

// Contains
"contains(attribute_name, :value)"

// Begins with
"begins_with(attribute_name, :value)"

// IN
"attribute_name IN (:val1, :val2, :val3)"

// Attribute exists
"attribute_exists(attribute_name)"

// Attribute not exists
"attribute_not_exists(attribute_name)"
```

### Logical Operators

```javascript
// AND
"condition1 AND condition2"

// OR
"condition1 OR condition2"

// NOT
"NOT condition1"
```

### Example

```javascript
// Complex condition
aws dynamodb put-item \
    --table-name Orders \
    --item '{...}' \
    --condition-expression "
        attribute_not_exists(orderId) AND
        total > :minTotal AND
        #status = :status
    " \
    --expression-attribute-names '{"#status": "status"}' \
    --expression-attribute-values '{
        ":minTotal": {"N": "0"},
        ":status": {"S": "pending"}
    }'
```

---

## Pagination

### Limit and ExclusiveStartKey

```javascript
// First page
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id" \
    --limit 10 \
    --expression-attribute-values '{":id": {"S": "user-123"}}'

// Next page (use LastEvaluatedKey from previous response)
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id" \
    --limit 10 \
    --exclusive-start-key '{"userId": {"S": "user-123"}, "orderId": {"S": "last-order-id"}}' \
    --expression-attribute-values '{":id": {"S": "user-123"}}'
```

---

## Best Practices

### 1. Use Query Over Scan

```javascript
// Good - Query with partition key
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id"

// Bad - Scan with filter
aws dynamodb scan \
    --table-name Orders \
    --filter-expression "userId = :id"
```

### 2. Use Projection Expressions

```javascript
// Good - Only needed attributes
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id" \
    --projection-expression "orderId, total"

// Bad - All attributes
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id"
```

### 3. Use Filter Expressions Sparingly

```javascript
// Better - Use key conditions
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id AND #status = :status"

// Worse - Use filter
aws dynamodb query \
    --table-name Orders \
    --key-condition-expression "userId = :id" \
    --filter-expression "#status = :status"
```

### 4. Use Batch Operations

```javascript
// Batch get (up to 100 items)
aws dynamodb batch-get-item \
    --request-items '{
        "Users": {
            "Keys": [
                {"userId": {"S": "user-1"}},
                {"userId": {"S": "user-2"}}
            ]
        }
    }'

// Batch write (up to 25 items)
aws dynamodb batch-write-item \
    --request-items '{
        "Users": [
            {"PutRequest": {"Item": {"userId": {"S": "user-1"}}}},
            {"PutRequest": {"Item": {"userId": {"S": "user-2"}}}}
        ]
    }'
```

### 5. Use Parallel Scan for Large Tables

```javascript
// Parallel scan with segments
aws dynamodb scan \
    --table-name Users \
    --total-segments 4 \
    --segment 0

// Process all segments in parallel
```

---

## Further Reading

- [DynamoDB Query](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Query.html)
- [DynamoDB Scan](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Scan.html)
- [Condition Expressions](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.ConditionExpressions.html)
