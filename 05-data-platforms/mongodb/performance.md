# MongoDB Performance

## Indexing

### Creating Indexes

```javascript
// Single field
db.users.createIndex({ email: 1 })

// Compound index
db.users.createIndex({ name: 1, age: -1 })

// Unique index
db.users.createIndex({ email: 1 }, { unique: true })

// Text index
db.products.createIndex({ name: "text", description: "text" })
```

### Index Usage

```javascript
// Check index usage
db.users.getIndexes()

// Explain query
db.users.find({ email: "alice@example.com" }).explain("executionStats")
```

### Index Best Practices

1. Create indexes on frequently queried fields
2. Use compound indexes for multi-field queries
3. Avoid over-indexing
4. Use text indexes for full-text search
5. Monitor index usage

## Aggregation Pipeline

### Optimization

```javascript
// Use $match early
db.orders.aggregate([
  { $match: { status: "completed" } },
  { $group: { _id: "$user_id", total: { $sum: "$amount" } } }
])

// Use $project to limit fields
db.orders.aggregate([
  { $project: { user_id: 1, amount: 1 } },
  { $group: { _id: "$user_id", total: { $sum: "$amount" } } }
])
```

### Common Optimizations

1. Use $match early in pipeline
2. Use $project to limit fields
3. Use indexes for $match and $sort
4. Avoid $unwind when possible
5. Use $limit early

## Query Profiling

### Enable Profiling

```javascript
// Enable profiling
db.setProfilingLevel(1, { slowms: 100 })

// Check profiling status
db.getProfilingStatus()
```

### Profile Data

```javascript
// View profile data
db.system.profile.find().sort({ ts: -1 }).limit(10)

// Find slow queries
db.system.profile.find({ millis: { $gt: 100 } })
```

## Connection Pooling

### Mongoose

```javascript
mongoose.connect(uri, {
  maxPoolSize: 10,
  minPoolSize: 2
})
```

### Native Driver

```javascript
const client = new MongoClient(uri, {
  maxPoolSize: 10,
  minPoolSize: 2
})
```

## Caching

### WiredTiger Cache

```yaml
# In mongod.conf
storage:
  wiredTiger:
    engineConfig:
      cacheSizeGB: 1  # 50% of RAM
```

### Application-Level Caching

```javascript
// Redis caching example
const cache = new Redis()

async function getUser(id) {
  let user = await cache.get(`user:${id}`)
  if (!user) {
    user = await db.users.findOne({ _id: id })
    await cache.set(`user:${id}`, JSON.stringify(user))
  }
  return JSON.parse(user)
}
```

## Bulk Operations

### Bulk Write

```javascript
const bulkOps = [
  { insertOne: { document: { name: "Alice" } } },
  { updateOne: { filter: { name: "Bob" }, update: { $set: { age: 30 } } } },
  { deleteOne: { filter: { name: "Charlie" } } }
]

db.users.bulkWrite(bulkOps)
```

## Read Preference

### Primary (Default)

```javascript
db.users.find().readPref("primary")
```

### Secondary Preferred

```javascript
db.users.find().readPref("secondaryPreferred")
```

## Write Concern

### Acknowledged

```javascript
db.users.insertOne({ name: "Alice" }, { w: "majority" })
```

### Unacknowledged

```javascript
db.users.insertOne({ name: "Alice" }, { w: 0 })
```

## Monitoring

### Server Status

```javascript
db.serverStatus()
```

### Collection Stats

```javascript
db.users.stats()
```

### Database Stats

```javascript
db.stats()
```

## Best Practices

1. Create appropriate indexes
2. Use aggregation pipeline efficiently
3. Enable profiling in development
4. Use connection pooling
5. Monitor slow queries
