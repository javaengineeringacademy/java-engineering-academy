# MongoDB Sharding

## Comprehensive Guide to Sharding Strategies

Sharding distributes data across multiple machines. This guide covers sharding architecture, shard keys, and strategies.

---

## Table of Contents

1. [Sharding Architecture](#sharding-architecture)
2. [Shard Keys](#shard-keys)
3. [Sharding Strategies](#sharding-strategies)
4. [Best Practices](#best-practices)

---

## Sharding Architecture

### Components

```
+------------------+
|   Client         |
+------------------+
        |
        v
+------------------+
|   Mongos         | (Query Router)
+------------------+
        |
        v
+------------------+     +------------------+
|   Config Server  |     |   Config Server  |
+------------------+     +------------------+
        |
        v
+------------------+     +------------------+     +------------------+
|   Shard 1        |     |   Shard 2        |     |   Shard 3        |
|   (Replica Set)  |     |   (Replica Set)  |     |   (Replica Set)  |
+------------------+     +------------------+     +------------------+
```

### Enable Sharding

```javascript
// Enable sharding for database
sh.enableSharding("mydb")

// Shard collection
sh.shardCollection("mydb.users", { userId: "hashed" })
```

---

## Shard Keys

### Choosing Shard Keys

```javascript
// Good shard keys
// - High cardinality
// - Even distribution
// - Query patterns

// Hashed shard key (even distribution)
sh.shardCollection("mydb.users", { userId: "hashed" })

// Range shard key (good for range queries)
sh.shardCollection("mydb.logs", { createdAt: 1 })

// Compound shard key
sh.shardCollection("mydb.orders", { customerId: 1, orderId: 1 })
```

### Shard Key Examples

```javascript
// User collection - hash user ID
sh.shardCollection("mydb.users", { _id: "hashed" })

// Log collection - range on timestamp
sh.shardCollection("mydb.logs", { timestamp: 1 })

// Order collection - compound key
sh.shardCollection("mydb.orders", { userId: 1, createdAt: -1 })

// Product collection - category with hash
sh.shardCollection("mydb.products", { category: 1, productId: "hashed" })
```

---

## Sharding Strategies

### Hashed Sharding

```javascript
// Even distribution
sh.shardCollection("mydb.users", { userId: "hashed" })

// Benefits:
// - Even distribution
// - Good for insert-heavy workloads
// - No hotspots

// Drawbacks:
// - Range queries scatter across shards
```

### Range Sharding

```javascript
// Range-based distribution
sh.shardCollection("mydb.logs", { timestamp: 1 })

// Benefits:
// - Good for range queries
// - Efficient for time-series data
// - Local ranges on single shard

// Drawbacks:
// - Potential hotspots
// - Uneven distribution
```

### Zone Sharding

```javascript
// Geographic distribution
sh.addShardToZone("shard1", "US")
sh.addShardToZone("shard2", "EU")
sh.addShardToZone("shard3", "ASIA")

// Tag ranges
sh.updateZoneKeyRange("mydb.users", {
    userId: ObjectId("000000000000000000000000")
}, {
    userId: ObjectId("7fffffffffffffffffffffff")
}, "US")
```

### Compound Sharding

```javascript
// Multiple shard keys
sh.shardCollection("mydb.orders", {
    customerId: "hashed",
    createdAt: 1
})

// Benefits:
// - Combines benefits of hashed and range
// - Good for customer-specific queries
```

---

## Best Practices

### 1. Choose High Cardinality Keys

```javascript
// Good - High cardinality
sh.shardCollection("mydb.users", { email: "hashed" })

// Bad - Low cardinality
sh.shardCollection("mydb.users", { status: "hashed" })
```

### 2. Avoid Hotspots

```javascript
// Good - Hashed for even distribution
sh.shardCollection("mydb.logs", { _id: "hashed" })

// Bad - Monotonically increasing
sh.shardCollection("mydb.logs", { _id: 1 })
```

### 3. Monitor Chunk Distribution

```javascript
// Check chunk distribution
sh.status()

// Balance chunks
sh.startBalancer()
```

### 4. Use Compound Keys for Queries

```javascript
// Good for queries that filter on customerId
sh.shardCollection("mydb.orders", { customerId: 1, createdAt: -1 })

// Query uses shard key
db.orders.find({ customerId: 123, createdAt: { $gte: ISODate("2024-01-01") } })
```

### 5. Pre-split Chunks

```javascript
// For predictable workloads
sh.shardCollection("mydb.users", { userId: "hashed" }, true, {
    numInitialChunks: 6
})
```

---

## Further Reading

- [MongoDB Sharding](https://www.mongodb.com/docs/manual/sharding/)
- [Shard Key Selection](https://www.mongodb.com/docs/manual/core/sharding-shard-key/)
- [Hashed Sharding](https://www.mongodb.com/docs/manual/core/hashed-sharding/)
