# MongoDB Anti-Patterns

## 1. Unbounded Arrays
**Description:** Storing arrays that can grow without limit.

**Why it's bad:** Exceeds document size limit (16MB), poor performance, memory issues.

**Example (bad code):**
```javascript
// Posts with unlimited comments array
{
    _id: ObjectId("..."),
    title: "My Post",
    comments: [
        { user: "user1", text: "..." },
        // ... could have thousands
    ]
}
```

**Better approach:** Use reference or bucket pattern:
```javascript
// Separate collection
{
    _id: ObjectId("..."),
    postId: ObjectId("..."),
    user: "user1",
    text: "..."
}

// Or bucket pattern
{
    _id: ObjectId("..."),
    postId: ObjectId("..."),
    comments: [
        { user: "user1", text: "..." }
        // max 50 comments per document
    ],
    nextBucket: ObjectId("...")
}
```

**Impact:** Scalable design, prevents document size issues.

---

## 2. N+1 Query Problem
**Description:** Fetching related data with separate queries instead of using $lookup.

**Why it's bad:** Excessive database queries, poor performance.

**Example (bad code):**
```javascript
const orders = await db.collection('orders').find({}).toArray();
for (const order of orders) {
    order.customer = await db.collection('customers').findOne({
        _id: order.customerId
    });
}
```

**Better approach:** Use $lookup aggregation:
```javascript
const orders = await db.collection('orders').aggregate([
    {
        $lookup: {
            from: 'customers',
            localField: 'customerId',
            foreignField: '_id',
            as: 'customer'
        }
    },
    { $unwind: '$customer' }
]).toArray();
```

**Impact:** Single query instead of N+1, better performance.

---

## 3. Unindexed Queries
**Description:** Querying fields without proper indexes.

**Why it's bad:** Collection scans, slow queries, poor performance at scale.

**Example (bad code):**
```javascript
// No index on 'email' field
const user = await db.collection('users').findOne({ email: 'user@example.com' });
```

**Better approach:** Create indexes:
```javascript
db.collection('users').createIndex({ email: 1 }, { unique: true });

// Compound indexes for common queries
db.collection('orders').createIndex({ userId: 1, status: 1 });
```

**Impact:** Fast queries, better performance.

---

## 4. Using find() Instead of Aggregation
**Description:** Using find() for complex transformations.

**Why it's bad:** Application-level processing, unnecessary data transfer.

**Example (bad code):**
```javascript
const users = await db.collection('users').find({}).toArray();
const activeUsers = users.filter(u => u.status === 'active');
const result = activeUsers.map(u => ({ name: u.name, email: u.email }));
```

**Better approach:** Use aggregation pipeline:
```javascript
const result = await db.collection('users').aggregate([
    { $match: { status: 'active' } },
    { $project: { name: 1, email: 1 } }
]).toArray();
```

**Impact:** Server-side processing, less data transfer.

---

## 5. Ignoring Write Concern
**Description:** Using default write concern for critical data.

**Why it's bad:** Data loss possible if primary fails before replication.

**Example (bad code):**
```javascript
// Default write concern - acknowledged
await db.collection('orders').insertOne(order);
```

**Better approach:** Use appropriate write concern:
```javascript
await db.collection('orders').insertOne(order, {
    writeConcern: { w: 'majority', j: true }
});
```

**Impact:** Data durability, prevents data loss.

---

## 6. Storing Blobs in MongoDB
**Description:** Storing large binary data directly in MongoDB.

**Why it's bad:** MongoDB not optimized for binary data, wastes RAM, poor performance.

**Example (bad code):**
```javascript
{
    _id: ObjectId("..."),
    name: "document.pdf",
    content: BinData(0, "large binary data...")
}
```

**Better approach:** Use GridFS or object storage:
```javascript
// Use GridFS
const bucket = new GridFSBucket(db);
const uploadStream = bucket.openUploadStream('document.pdf');
// Or store reference to external storage
{
    _id: ObjectId("..."),
    name: "document.pdf",
    storageUrl: "s3://bucket/document.pdf"
}
```

**Impact:** Better performance, proper binary data handling.

---

## 7. Not Using Projection
**Description:** Fetching all fields when only some are needed.

**Why it's bad:** Wasted bandwidth, increased latency, higher costs.

**Example (bad code):**
```javascript
// Fetches all fields
const user = await db.collection('users').findOne({ _id: userId });
```

**Better approach:** Use projection:
```javascript
const user = await db.collection('users').findOne(
    { _id: userId },
    { projection: { name: 1, email: 1 } }
);
```

**Impact:** Reduced data transfer, better performance.

---

## 8. Ignoring Index Selectivity
**Description:** Creating indexes on low-selectivity fields.

**Why it's bad:** Index doesn't improve performance, wastes resources.

**Example (bad code):**
```javascript
// Index on boolean field with 50/50 distribution
db.collection('users').createIndex({ isActive: 1 });
```

**Better approach:** Use compound indexes:
```javascript
// Compound index with high-selectivity field first
db.collection('users').createIndex({ isActive: 1, email: 1 });
```

**Impact:** More efficient indexes, better query performance.

---

## 9. Using $where
**Description:** Using JavaScript expressions in queries.

**Why it's bad:** Slow execution, cannot use indexes, security risk.

**Example (bad code):**
```javascript
db.collection('users').find({
    $where: function() {
        return this.age > 18 && this.status === 'active';
    }
});
```

**Better approach:** Use standard query operators:
```javascript
db.collection('users').find({
    age: { $gt: 18 },
    status: 'active'
});
```

**Impact:** Fast queries, index utilization, security.

---

## 10. Not Handling Duplicate Key Errors
**Description:** Not handling duplicate key errors gracefully.

**Why it's bad:** Application crashes or inconsistent state.

**Example (bad code):**
```javascript
await db.collection('users').insertOne({ email: 'user@example.com' });
// Throws error if duplicate
```

**Better approach:** Handle errors:
```javascript
try {
    await db.collection('users').insertOne({ email: 'user@example.com' });
} catch (error) {
    if (error.code === 11000) {
        // Duplicate key error
        console.log('User already exists');
    } else {
        throw error;
    }
}
```

**Impact:** Graceful error handling, robust applications.

---

## 11. Ignoring Connection Pooling
**Description:** Creating new connections for each operation.

**Why it's bad:** Connection overhead, exhausted connections under load.

**Example (bad code):**
```javascript
// New connection for each operation
const client = new MongoClient(uri);
await client.connect();
// operation
await client.close();
```

**Better approach:** Reuse client:
```javascript
const client = new MongoClient(uri);
await client.connect();
// Reuse client for all operations
const db = client.db('mydb');
```

**Impact:** Better performance, resource efficiency.

---

## 12. Not Using TTL Indexes
**Description:** Manually deleting expired documents.

**Why it's bad:** Wasted storage, manual cleanup complexity.

**Example (bad code):**
```javascript
// Manual cleanup
await db.collection('sessions').deleteMany({
    createdAt: { $lt: new Date(Date.now() - 86400000) }
});
```

**Better approach:** Use TTL indexes:
```javascript
db.collection('sessions').createIndex(
    { createdAt: 1 },
    { expireAfterSeconds: 86400 }
);
```

**Impact:** Automatic cleanup, less manual work.