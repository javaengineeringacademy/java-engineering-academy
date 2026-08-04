# MongoDB Fundamentals

## Comprehensive Guide to MongoDB

MongoDB is a document-oriented NoSQL database. This guide covers documents, CRUD operations, and MongoDB shell basics.

---

## Table of Contents

1. [MongoDB Overview](#mongodb-overview)
2. [Documents and Collections](#documents-and-collections)
3. [CRUD Operations](#crud-operations)
4. [Data Modeling](#data-modeling)
5. [Best Practices](#best-practices)

---

## MongoDB Overview

### Architecture

```
Database
  |
  +-- Collection (like table)
  |     |
  |     +-- Document (like row)
  |           |
  |           +-- Field (like column)
  |
  +-- Collection
        |
        +-- Document
```

### Document Structure (BSON)

```json
{
    "_id": ObjectId("507f1f77bcf86cd799439011"),
    "name": "John Doe",
    "email": "john@example.com",
    "age": 30,
    "address": {
        "street": "123 Main St",
        "city": "New York",
        "state": "NY"
    },
    "hobbies": ["reading", "gaming", "coding"],
    "createdAt": ISODate("2024-01-15T10:30:00Z")
}
```

---

## Documents and Collections

### Create Collection

```javascript
// Create collection
db.createCollection("users")

// Insert document
db.users.insertOne({
    name: "John Doe",
    email: "john@example.com",
    age: 30,
    address: {
        street: "123 Main St",
        city: "New York"
    },
    hobbies: ["reading", "gaming"],
    createdAt: new Date()
})

// Insert multiple
db.users.insertMany([
    { name: "Jane Smith", email: "jane@example.com", age: 25 },
    { name: "Bob Johnson", email: "bob@example.com", age: 35 }
])
```

### Document Schema

```javascript
// Flexible schema - different documents in same collection
db.users.insertOne({ name: "John", age: 30 })
db.users.insertOne({ name: "Jane", age: 25, phone: "555-1234" })
db.users.insertOne({ name: "Bob", age: 35, address: { city: "NYC" } })
```

---

## CRUD Operations

### Create

```javascript
// insertOne
db.users.insertOne({
    name: "Alice",
    email: "alice@example.com",
    age: 28
})

// insertMany
db.users.insertMany([
    { name: "Bob", email: "bob@example.com", age: 32 },
    { name: "Charlie", email: "charlie@example.com", age: 29 }
])
```

### Read

```javascript
// find all
db.users.find()

// find with filter
db.users.find({ age: { $gte: 30 } })

// find one
db.users.findOne({ email: "john@example.com" })

// Projection (select specific fields)
db.users.find({}, { name: 1, email: 1, _id: 0 })

// Sort
db.users.find().sort({ age: -1 })

// Limit
db.users.find().limit(10)

// Skip (pagination)
db.users.find().skip(20).limit(10)
```

### Update

```javascript
// updateOne
db.users.updateOne(
    { email: "john@example.com" },
    { $set: { age: 31 } }
)

// updateMany
db.users.updateMany(
    { age: { $lt: 25 } },
    { $set: { status: "young" } }
)

// upsert (insert if not exists)
db.users.updateOne(
    { email: "new@example.com" },
    { $set: { name: "New User" } },
    { upsert: true }
)

// addToSet (add to array if not present)
db.users.updateOne(
    { email: "john@example.com" },
    { $addToSet: { hobbies: "painting" } }
)

// push (add to array)
db.users.updateOne(
    { email: "john@example.com" },
    { $push: { hobbies: "swimming" } }
)
```

### Delete

```javascript
// deleteOne
db.users.deleteOne({ email: "john@example.com" })

// deleteMany
db.users.deleteMany({ age: { $lt: 18 } })

// findOneAndDelete
db.users.findOneAndDelete({ email: "john@example.com" })
```

---

## Data Modeling

### Embedded Documents

```javascript
// One-to-one (embedded)
db.users.insertOne({
    name: "John",
    profile: {
        bio: "Software developer",
        avatar: "https://example.com/avatar.jpg"
    }
})

// One-to-many (embedded)
db.orders.insertOne({
    userId: ObjectId("507f1f77bcf86cd799439011"),
    items: [
        { productId: "p1", name: "Laptop", quantity: 1, price: 999.99 },
        { productId: "p2", name: "Mouse", quantity: 2, price: 29.99 }
    ]
})
```

### Referenced Documents

```javascript
// One-to-many (referenced)
db.users.insertOne({
    name: "John",
    email: "john@example.com"
})

db.orders.insertOne({
    userId: ObjectId("507f1f77bcf86cd799439011"),
    total: 1059.97
})

// Populate reference
db.orders.aggregate([
    { $lookup: {
        from: "users",
        localField: "userId",
        foreignField: "_id",
        as: "user"
    }}
])
```

---

## Best Practices

### 1. Use Meaningful Collection Names

```javascript
// Good
db.users
db.orders
db.products

// Bad
db.data
db.info
db.collection1
```

### 2. Index Frequently Queried Fields

```javascript
// Create index
db.users.createIndex({ email: 1 }, { unique: true })
db.users.createIndex({ "address.city": 1 })
db.orders.createIndex({ userId: 1, createdAt: -1 })
```

### 3. Use Projection

```javascript
// Good - Only needed fields
db.users.find({}, { name: 1, email: 1 })

// Bad - All fields
db.users.find({})
```

### 4. Use Bulk Operations

```javascript
// Bulk insert
db.users.insertMany([
    { name: "User1", age: 25 },
    { name: "User2", age: 30 }
])

// Bulk update
const bulk = db.users.initializeUnorderedBulkOp();
bulk.find({ age: { $lt: 25 } }).update({ $set: { status: "young" } });
bulk.find({ age: { $gte: 25 } }).update({ $set: { status: "adult" } });
bulk.execute();
```

### 5. Use Aggregation Pipeline

```javascript
db.users.aggregate([
    { $match: { age: { $gte: 18 } } },
    { $group: {
        _id: "$address.city",
        avgAge: { $avg: "$age" },
        count: { $sum: 1 }
    }},
    { $sort: { count: -1 } }
])
```

---

## Further Reading

- [MongoDB Documentation](https://www.mongodb.com/docs/)
- [MongoDB University](https://university.mongodb.com/)
- [MongoDB Best Practices](https://www.mongodb.com/docs/manual/core/data-model-design/)
