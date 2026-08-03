// MongoDB CRUD Operations

// Insert
db.users.insertOne({
  name: "Alice",
  email: "alice@example.com",
  age: 30,
  address: {
    city: "New York",
    state: "NY"
  }
})

db.users.insertMany([
  { name: "Bob", email: "bob@example.com", age: 25 },
  { name: "Charlie", email: "charlie@example.com", age: 35 }
])

// Read
db.users.find({ age: { $gte: 25 } })
db.users.findOne({ name: "Alice" })

// Update
db.users.updateOne(
  { name: "Alice" },
  { $set: { age: 31 } }
)

db.users.updateMany(
  { age: { $lt: 30 } },
  { $inc: { age: 1 } }
)

// Delete
db.users.deleteOne({ name: "Bob" })
db.users.deleteMany({ age: { $lt: 25 } })

// Aggregation
db.users.aggregate([
  { $match: { age: { $gte: 25 } } },
  { $group: { _id: "$address.city", count: { $sum: 1 } } },
  { $sort: { count: -1 } }
])

// Indexes
db.users.createIndex({ email: 1 }, { unique: true })
db.users.createIndex({ "address.city": 1 })
