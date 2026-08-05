# MongoDB Project Structure

## Schema Design Patterns

### Embedding Pattern

```javascript
// One-to-few relationship
{
  _id: ObjectId("..."),
  name: "Alice",
  orders: [
    { product: "Widget", quantity: 5 },
    { product: "Gadget", quantity: 2 }
  ]
}
```

### Referencing Pattern

```javascript
// One-to-many relationship
// Users collection
{
  _id: ObjectId("..."),
  name: "Alice"
}

// Orders collection
{
  _id: ObjectId("..."),
  user_id: ObjectId("..."),
  product: "Widget"
}
```

### Subset Pattern

```javascript
// Frequently accessed data embedded
{
  _id: ObjectId("..."),
  name: "Alice",
  email: "alice@example.com",
  recent_orders: [
    { product: "Widget", quantity: 5 }
  ]
}
```

### Computed Pattern

```javascript
// Pre-computed values
{
  _id: ObjectId("..."),
  name: "Alice",
  total_orders: 10,
  total_spent: 1500.00
}
```

## Project Layout

```
mongodb-project/
├── schemas/
│   ├── user.js
│   ├── order.js
│   └── product.js
├── models/
│   ├── User.js
│   ├── Order.js
│   └── Product.js
├── migrations/
│   ├── 001_create_users.js
│   └── 002_create_indexes.js
├── seeds/
│   ├── development.js
│   └── production.js
├── scripts/
│   ├── backup.sh
│   └── restore.sh
├── config/
│   ├── default.js
│   ├── development.js
│   └── production.js
├── docker-compose.yml
└── README.md
```

## Schema Validation

### Create Collection with Validation

```javascript
db.createCollection("users", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["name", "email"],
      properties: {
        name: {
          bsonType: "string",
          description: "Name is required"
        },
        email: {
          bsonType: "string",
          pattern: "^.+@.+$",
          description: "Must be a valid email"
        },
        age: {
          bsonType: "int",
          minimum: 0,
          maximum: 150
        }
      }
    }
  }
})
```

## Index Strategy

### Single Field Index

```javascript
db.users.createIndex({ email: 1 })
```

### Compound Index

```javascript
db.users.createIndex({ name: 1, age: -1 })
```

### Text Index

```javascript
db.products.createIndex({ name: "text", description: "text" })
```

### TTL Index

```javascript
db.sessions.createIndex(
  { created_at: 1 },
  { expireAfterSeconds: 3600 }
)
```

## Data Access Layer

### Mongoose Example

```javascript
const userSchema = new mongoose.Schema({
  name: { type: String, required: true },
  email: { type: String, required: true, unique: true },
  age: { type: Number, min: 0, max: 150 }
})

const User = mongoose.model('User', userSchema)
```

### Native Driver Example

```javascript
const { MongoClient } = require('mongodb')

const client = new MongoClient(uri)
const db = client.db('mydb')
const users = db.collection('users')
```

## Testing

### Test Database Setup

```javascript
// Create test database
const testDb = client.db('mydb_test')

// Clean up after tests
afterEach(async () => {
  await testDb.collection('users').deleteMany({})
})
```

## Docker Setup

### docker-compose.yml

```yaml
version: '3.8'
services:
  mongodb:
    image: mongo:6.0
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: password
    ports:
      - "27017:27017"
    volumes:
      - mongo-data:/data/db
volumes:
  mongo-data:
```

## Best Practices

1. Design schemas based on query patterns
2. Use appropriate indexes
3. Implement schema validation
4. Use reference pattern for large datasets
5. Test schema changes before production
