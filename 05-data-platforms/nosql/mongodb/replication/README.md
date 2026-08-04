# MongoDB Replication

## Comprehensive Guide to Replica Sets

Replica sets provide high availability and data redundancy. This guide covers replica set architecture, read preference, and failover.

---

## Table of Contents

1. [Replica Set Architecture](#replica-set-architecture)
2. [Read Preference](#read-preference)
3. [Write Concern](#write-concern)
4. [Failover](#failover)
5. [Best Practices](#best-practices)

---

## Replica Set Architecture

### Architecture

```
+------------------+
|   Primary        |
|   (reads/writes) |
+------------------+
        |
        v
+------------------+     +------------------+
|   Secondary      |     |   Secondary      |
|   (replica)      |     |   (replica)      |
+------------------+     +------------------+
```

### Setup

```javascript
// Initialize replica set
rs.initiate({
    _id: "rs0",
    members: [
        { _id: 0, host: "mongo1:27017", priority: 2 },
        { _id: 1, host: "mongo2:27017", priority: 1 },
        { _id: 2, host: "mongo3:27017", priority: 1 }
    ]
})

// Check status
rs.status()
```

---

## Read Preference

### Read Preference Modes

```javascript
// Primary (default) - Read from primary only
db.collection.find().readPref("primary")

// PrimaryPreferred - Primary with fallback to secondary
db.collection.find().readPref("primaryPreferred")

// Secondary - Read from secondaries only
db.collection.find().readPref("secondary")

// SecondaryPreferred - Secondary with fallback to primary
db.collection.find().readPref("secondaryPreferred")

// Nearest - Read from nearest member
db.collection.find().readPref("nearest")
```

### Tag Sets

```javascript
// Read from specific tags
db.collection.find().readPref("secondary", [
    { "region": "us-east" },
    { "disk": "ssd" }
])
```

---

## Write Concern

### Write Concern Levels

```javascript
// w: 1 - Acknowledged by primary (default)
db.collection.insertOne({ name: "test" }, { writeConcern: { w: 1 } })

// w: "majority" - Acknowledged by majority
db.collection.insertOne({ name: "test" }, { writeConcern: { w: "majority" } })

// w: 0 - Unacknowledged
db.collection.insertOne({ name: "test" }, { writeConcern: { w: 0 } })

// w: 2 - Acknowledged by 2 members
db.collection.insertOne({ name: "test" }, { writeConcern: { w: 2 } })
```

### Write Concern with Journal

```javascript
// Wait for journal commit
db.collection.insertOne(
    { name: "test" },
    { writeConcern: { w: "majority", j: true } }
)

// Timeout
db.collection.insertOne(
    { name: "test" },
    { writeConcern: { w: "majority", wtimeout: 5000 } }
)
```

---

## Failover

### Automatic Failover

```javascript
// Primary goes down
// 1. Secondaries detect primary is unreachable
// 2. Election held
// 3. New primary elected
// 4. Clients reconnect to new primary
```

### Manual Failover

```javascript
// Step down primary
rs.stepDown()

// Force reconfigure
rs.reconfig({
    _id: "rs0",
    members: [
        { _id: 0, host: "mongo1:27017" },
        { _id: 1, host: "mongo2:27017" }
    ]
}, { force: true })
```

---

## Best Practices

### 1. Use odd number of members

```javascript
// Good - 3 or 5 members
rs.initiate({
    _id: "rs0",
    members: [
        { _id: 0, host: "mongo1:27017" },
        { _id: 1, host: "mongo2:27017" },
        { _id: 2, host: "mongo3:27017" }
    ]
})
```

### 2. Use Write Concern majority

```javascript
// For critical writes
db.collection.insertOne(
    { name: "important" },
    { writeConcern: { w: "majority" } }
)
```

### 3. Monitor Replica Set Health

```javascript
// Check status
rs.status()

// Check replication lag
rs.printReplicationInfo()
rs.printSecondaryReplicationInfo()
```

### 4. Use Read Preference Wisely

```javascript
// For analytics (can tolerate stale data)
db.collection.find().readPref("secondary")

// For real-time data
db.collection.find().readPref("primary")
```

### 5. Test Failover

```javascript
// Simulate primary failure
rs.stepDown()

// Verify new primary elected
rs.status()
```

---

## Further Reading

- [MongoDB Replica Sets](https://www.mongodb.com/docs/manual/replication/)
- [Read Preference](https://www.mongodb.com/docs/manual/core/read-preference/)
- [Write Concern](https://www.mongodb.com/docs/manual/reference/write-concern/)
