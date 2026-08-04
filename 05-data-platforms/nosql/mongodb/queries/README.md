# MongoDB Queries

## Comprehensive Guide to MongoDB Query Operators

MongoDB provides rich query operators for filtering, updating, and aggregating data.

---

## Table of Contents

1. [Query Operators](#query-operators)
2. [Comparison Operators](#comparison-operators)
3. [Logical Operators](#logical-operators)
4. [Array Operators](#array-operators)
5. [Element Operators](#element-operators)
6. [Best Practices](#best-practices)

---

## Query Operators

### Comparison Operators

```javascript
// $eq - Equal
db.users.find({ age: { $eq: 30 } })

// $ne - Not equal
db.users.find({ age: { $ne: 30 } })

// $gt - Greater than
db.users.find({ age: { $gt: 25 } })

// $gte - Greater than or equal
db.users.find({ age: { $gte: 25 } })

// $lt - Less than
db.users.find({ age: { $lt: 30 } })

// $lte - Less than or equal
db.users.find({ age: { $lte: 30 } })

// $in - In array
db.users.find({ status: { $in: ["active", "pending"] } })

// $nin - Not in array
db.users.find({ status: { $nin: ["inactive"] } })
```

### Logical Operators

```javascript
// $and
db.users.find({
    $and: [
        { age: { $gte: 25 } },
        { age: { $lte: 35 } }
    ]
})

// $or
db.users.find({
    $or: [
        { status: "active" },
        { age: { $gte: 30 } }
    ]
})

// $not
db.users.find({
    age: { $not: { $gte: 30 } }
})

// $nor
db.users.find({
    $nor: [
        { status: "active" },
        { age: { $gte: 30 } }
    ]
})
```

### Array Operators

```javascript
// $all - Contains all elements
db.users.find({ hobbies: { $all: ["reading", "gaming"] } })

// $elemMatch - Element match
db.users.find({
    orders: {
        $elemMatch: {
            product: "laptop",
            price: { $gte: 999 }
        }
    }
})

// $size - Array size
db.users.find({ hobbies: { $size: 3 } })

// $slice - Return subset
db.users.find({}, { orders: { $slice: [0, 5] } })
```

### Element Operators

```javascript
// $exists - Field exists
db.users.find({ phone: { $exists: true } })

// $type - Field type
db.users.find({ age: { $type: "number" } })
```

### Evaluation Operators

```javascript
// $regex - Regular expression
db.users.find({ email: { $regex: "^john", $options: "i" } })

// $text - Text search
db.users.find({ $text: { $search: "developer" } })
```

### Update Operators

```javascript
// $set - Set field value
db.users.updateOne(
    { email: "john@example.com" },
    { $set: { age: 31 } }
)

// $unset - Remove field
db.users.updateOne(
    { email: "john@example.com" },
    { $unset: { phone: "" } }
)

// $inc - Increment
db.users.updateOne(
    { email: "john@example.com" },
    { $inc: { loginCount: 1 } }
)

// $push - Add to array
db.users.updateOne(
    { email: "john@example.com" },
    { $push: { hobbies: "painting" } }
)

// $pull - Remove from array
db.users.updateOne(
    { email: "john@example.com" },
    { $pull: { hobbies: "gaming" } }
)

// $addToSet - Add if not exists
db.users.updateOne(
    { email: "john@example.com" },
    { $addToSet: { hobbies: "swimming" } }
)

// $rename - Rename field
db.users.updateMany(
    {},
    { $rename: { "name": "fullName" } }
)

// $mul - Multiply
db.products.updateOne(
    { _id: productId },
    { $mul: { price: 1.1 } }
)
```

---

## Best Practices

### 1. Use Indexes

```javascript
// Create indexes for common queries
db.users.createIndex({ email: 1 })
db.users.createIndex({ status: 1, createdAt: -1 })

// Use explain to check query performance
db.users.find({ email: "john@example.com" }).explain("executionStats")
```

### 2. Use Projection

```javascript
// Good - Only needed fields
db.users.find({ status: "active" }, { name: 1, email: 1 })

// Bad - All fields
db.users.find({ status: "active" })
```

### 3. Use $match Early in Aggregation

```javascript
// Good - Filter early
db.users.aggregate([
    { $match: { status: "active" } },
    { $group: { _id: "$city", count: { $sum: 1 } } }
])

// Bad - Filter late
db.users.aggregate([
    { $group: { _id: "$city", count: { $sum: 1 } } },
    { $match: { status: "active" } }
])
```

### 4. Use limit() for Large Results

```javascript
// Good - Limited results
db.users.find().limit(100)

// Bad - All results
db.users.find()
```

### 5. Use Bulk Operations

```javascript
// Bulk update
const bulk = db.users.initializeUnorderedBulkOp();
users.forEach(user => {
    bulk.find({ _id: user._id }).updateOne({ $set: { processed: true } });
});
bulk.execute();
```

---

## Further Reading

- [MongoDB Query Operators](https://www.mongodb.com/docs/manual/reference/operator/query/)
- [MongoDB Aggregation](https://www.mongodb.com/docs/manual/aggregation/)
- [MongoDB Performance](https://www.mongodb.com/docs/manual/administration/analyzing-mongodb-performance/)
