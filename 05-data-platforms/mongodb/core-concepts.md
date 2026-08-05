# MongoDB Core Concepts

## Documents

### Document Structure

```javascript
{
  "_id": ObjectId("507f1f77bcf86cd799439011"),
  "name": "Alice",
  "age": 30,
  "email": "alice@example.com",
  "address": {
    "street": "123 Main St",
    "city": "New York",
    "zip": "10001"
  },
  "hobbies": ["reading", "coding", "hiking"],
  "created_at": ISODate("2024-01-15T10:30:00Z")
}
```

### Data Types

- String: "hello"
- Integer: 42
- Double: 3.14
- Boolean: true/false
- Array: [1, 2, 3]
- Object: { key: value }
- ObjectId: ObjectId("...")
- Date: ISODate("...")
- Null: null
- Binary: BinData(...)

## Collections

### Creating Collections

```javascript
// Explicit creation
db.createCollection("users")

// Implicit creation
db.users.insertOne({ name: "Alice" })
```

### Collection Options

```javascript
db.createCollection("users", {
  capped: true,
  size: 1048576,
  max: 1000
})
```

## CRUD Operations

### Create

```javascript
// Insert one
db.users.insertOne({
  name: "Alice",
  email: "alice@example.com"
})

// Insert many
db.users.insertMany([
  { name: "Alice", email: "alice@example.com" },
  { name: "Bob", email: "bob@example.com" }
])
```

### Read

```javascript
// Find one
db.users.findOne({ name: "Alice" })

// Find many
db.users.find({ age: { $gt: 25 } })

// Find with projection
db.users.find(
  { status: "active" },
  { name: 1, email: 1 }
)
```

### Update

```javascript
// Update one
db.users.updateOne(
  { name: "Alice" },
  { $set: { age: 31 } }
)

// Update many
db.users.updateMany(
  { status: "inactive" },
  { $set: { status: "active" } }
)

// Replace
db.users.replaceOne(
  { name: "Alice" },
  { name: "Alice", age: 31, status: "active" }
)
```

### Delete

```javascript
// Delete one
db.users.deleteOne({ name: "Alice" })

// Delete many
db.users.deleteMany({ status: "inactive" })
```

## Aggregation Pipeline

### Basic Pipeline

```javascript
db.orders.aggregate([
  { $match: { status: "completed" } },
  { $group: {
    _id: "$user_id",
    total: { $sum: "$amount" },
    count: { $sum: 1 }
  }},
  { $sort: { total: -1 } },
  { $limit: 10 }
])
```

### Common Stages

- $match: Filter documents
- $group: Group by field
- $project: Reshape documents
- $sort: Sort results
- $limit: Limit results
- $unwind: Flatten arrays
- $lookup: Join collections

## Indexes

### Creating Indexes

```javascript
// Single field
db.users.createIndex({ email: 1 })

// Compound index
db.users.createIndex({ name: 1, age: -1 })

// Unique index
db.users.createIndex({ email: 1 }, { unique: true })

// TTL index
db.sessions.createIndex(
  { created_at: 1 },
  { expireAfterSeconds: 3600 }
)
```

### Index Types

- Single field
- Compound
- Multikey (arrays)
- Text
- Geospatial
- Hashed
- TTL

## Transactions

### Session Transaction

```javascript
const session = client.startSession()
session.startTransaction()

try {
  db.orders.insertOne({ user_id: 1, amount: 100 }, { session })
  db.inventory.updateOne(
    { product_id: 1 },
    { $inc: { quantity: -1 } },
    { session }
  )
  session.commitTransaction()
} catch (error) {
  session.abortTransaction()
} finally {
  session.endSession()
}
```

## Best Practices

1. Use appropriate data types
2. Create indexes on queried fields
3. Use aggregation for complex queries
4. Implement proper schema validation
5. Use transactions for multi-document operations
