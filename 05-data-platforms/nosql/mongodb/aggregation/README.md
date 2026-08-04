# MongoDB Aggregation

## Comprehensive Guide to Aggregation Pipeline

The aggregation pipeline processes documents through stages. This guide covers common aggregation patterns.

---

## Table of Contents

1. [Aggregation Pipeline](#aggregation-pipeline)
2. [Common Stages](#common-stages)
3. [Grouping and Accumulators](#grouping-and-accumulators)
4. [Best Practices](#best-practices)

---

## Aggregation Pipeline

### Basic Pipeline

```javascript
db.orders.aggregate([
    // Stage 1: Filter
    { $match: { status: "completed" } },

    // Stage 2: Group
    { $group: {
        _id: "$customerId",
        totalSpent: { $sum: "$total" },
        orderCount: { $sum: 1 }
    }},

    // Stage 3: Sort
    { $sort: { totalSpent: -1 } },

    // Stage 4: Limit
    { $limit: 10 }
])
```

### Pipeline Stages

```javascript
$match      - Filter documents
$group      - Group by key
$sort       - Sort results
$project    - Reshape documents
$lookup     - Join collections
$unwind     - Deconstruct arrays
$addFields  - Add new fields
$bucket     - Bucket documents
$count      - Count documents
$facet      - Multiple pipelines
```

---

## Common Stages

### $match

```javascript
// Filter documents
db.orders.aggregate([
    { $match: {
        status: "completed",
        total: { $gte: 100 },
        createdAt: {
            $gte: ISODate("2024-01-01"),
            $lt: ISODate("2025-01-01")
        }
    }}
])
```

### $group

```javascript
// Group by field
db.orders.aggregate([
    { $group: {
        _id: "$customerId",
        totalOrders: { $sum: 1 },
        totalSpent: { $sum: "$total" },
        avgOrderValue: { $avg: "$total" },
        minOrder: { $min: "$total" },
        maxOrder: { $max: "$total" }
    }}
])
```

### $project

```javascript
// Reshape documents
db.users.aggregate([
    { $project: {
        _id: 0,
        fullName: { $concat: ["$firstName", " ", "$lastName"] },
        email: 1,
        age: 1,
        isAdult: { $gte: ["$age", 18] }
    }}
])
```

### $lookup

```javascript
// Join collections
db.orders.aggregate([
    { $lookup: {
        from: "customers",
        localField: "customerId",
        foreignField: "_id",
        as: "customer"
    }},
    { $unwind: "$customer" },
    { $project: {
        orderId: 1,
        total: 1,
        customerName: "$customer.name"
    }}
])
```

### $unwind

```javascript
// Deconstruct array
db.orders.aggregate([
    { $unwind: "$items" },
    { $group: {
        _id: "$items.productId",
        totalQuantity: { $sum: "$items.quantity" }
    }}
])
```

### $addFields

```javascript
// Add new fields
db.users.aggregate([
    { $addFields: {
        fullName: { $concat: ["$firstName", " ", "$lastName"] },
        ageGroup: {
            $switch: {
                branches: [
                    { case: { $lt: ["$age", 18] }, then: "minor" },
                    { case: { $lt: ["$age", 65] }, then: "adult" }
                ],
                default: "senior"
            }
        }
    }}
])
```

---

## Grouping and Accumulators

### Accumulators

```javascript
{ $sum: "$field" }      // Sum
{ $avg: "$field" }      // Average
{ $min: "$field" }      // Minimum
{ $max: "$field" }      // Maximum
{ $first: "$field" }    // First value
{ $last: "$field" }     // Last value
{ $push: "$field" }     // Push to array
{ $addToSet: "$field" } // Add unique to array
{ $count: {} }          // Count
```

### Complex Grouping

```javascript
db.sales.aggregate([
    { $group: {
        _id: {
            year: { $year: "$date" },
            month: { $month: "$date" },
            category: "$category"
        },
        totalSales: { $sum: "$amount" },
        transactionCount: { $sum: 1 },
        uniqueCustomers: { $addToSet: "$customerId" }
    }},
    { $project: {
        _id: 0,
        year: "$_id.year",
        month: "$_id.month",
        category: "$_id.category",
        totalSales: 1,
        transactionCount: 1,
        uniqueCustomerCount: { $size: "$uniqueCustomers" }
    }},
    { $sort: { year: 1, month: 1, totalSales: -1 } }
])
```

---

## Best Practices

### 1. Use $match Early

```javascript
// Good - Filter first
db.orders.aggregate([
    { $match: { status: "completed" } },
    { $group: { _id: "$customerId", total: { $sum: "$total" } } }
])
```

### 2. Use Indexes with $match

```javascript
// Create index
db.orders.createIndex({ status: 1, createdAt: -1 })

// Query uses index
db.orders.aggregate([
    { $match: { status: "completed" } }
])
```

### 3. Use $project to Limit Fields

```javascript
db.users.aggregate([
    { $project: { name: 1, email: 1 } },
    { $limit: 100 }
])
```

### 4. Use $facet for Multiple Aggregations

```javascript
db.orders.aggregate([
    { $facet: {
        byStatus: [
            { $group: { _id: "$status", count: { $sum: 1 } } }
        ],
        byMonth: [
            { $group: {
                _id: { $month: "$createdAt" },
                count: { $sum: 1 }
            }}
        ]
    }}
])
```

### 5. Use $bucket for Histograms

```javascript
db.users.aggregate([
    { $bucket: {
        groupBy: "$age",
        boundaries: [0, 18, 30, 50, 70, 100],
        default: "Other",
        output: {
            count: { $sum: 1 },
            names: { $push: "$name" }
        }
    }}
])
```

---

## Further Reading

- [MongoDB Aggregation Pipeline](https://www.mongodb.com/docs/manual/core/aggregation-pipeline/)
- [Aggregation Reference](https://www.mongodb.com/docs/manual/reference/operator/aggregation/)
- [Aggregation Examples](https://www.mongodb.com/docs/manual/aggregation/)
