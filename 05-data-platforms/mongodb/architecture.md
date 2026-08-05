# MongoDB Architecture

## Document Model

### JSON/BSON Format

MongoDB stores data as BSON (Binary JSON), which supports more data types than JSON.

```json
{
  "_id": ObjectId("507f1f77bcf86cd799439011"),
  "name": "Alice",
  "email": "alice@example.com",
  "age": 30,
  "address": {
    "street": "123 Main St",
    "city": "New York"
  },
  "hobbies": ["reading", "coding"]
}
```

### Document Structure

- Maximum document size: 16MB
- Fields can be any BSON type
- Nested documents up to 100 levels
- Dynamic schema

## Replica Sets

### Architecture

A replica set is a group of MongoDB instances that maintain the same data.

```
Primary (1)
    |
Secondary (2)
Secondary (3)
```

### Components

- Primary: Receives all write operations
- Secondary: Replicate primary data
- Arbiter: Votes but doesn't store data

### Election Process

- Uses Raft consensus algorithm
- Automatic failover
- Requires majority for election

## Sharding

### Components

- Shard: Stores data subset
- Config Server: Metadata storage
- Query Router (mongos): Routes queries

### Shard Key Selection

```javascript
// Good shard key
{ user_id: 1 }  // High cardinality

// Bad shard key
{ status: 1 }   // Low cardinality
```

### Shard Strategies

- Hashed: Even distribution
- Ranged: Range-based distribution
- Zone: Geographic distribution

## WiredTiger Storage Engine

### Features

- Document-level concurrency control
- Checkpointing for durability
- Compression (snappy, zlib)
- Memory-mapped files

### Cache Configuration

```yaml
storage:
  wiredTiger:
    engineConfig:
      cacheSizeGB: 1  # 50% of RAM
```

### Journaling

- Write-ahead logging
- Crash recovery
- Configurable commit interval

## Read/Write Operations

### Write Concern

```javascript
// Acknowledged write
db.collection.insertOne(doc, { w: "majority" })

// Unacknowledged write
db.collection.insertOne(doc, { w: 0 })
```

### Read Preference

```javascript
// Read from primary
db.collection.find().readPref("primary")

// Read from secondary
db.collection.find().readPref("secondaryPreferred")
```

## Memory Management

### WiredTiger Cache

- Default: 50% of RAM
- Stores frequently accessed data
- LRU eviction policy

### Operating System Cache

- File system cache
- Memory-mapped files
- Operating system manages

## Best Practices

1. Choose appropriate shard key
2. Use replica sets for high availability
3. Monitor cache usage
4. Configure write concern appropriately
5. Use indexing strategies
