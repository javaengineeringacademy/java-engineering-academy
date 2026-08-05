# Redis Project Structure

## Standard Layout (Client Application)

```
my-redis-project/
├── src/
│   ├── config/
│   │   ├── redis.config.js          # Redis client configuration
│   │   └── redis.cluster.config.js  # Cluster configuration
│   ├── services/
│   │   ├── cache.service.js         # Cache operations
│   │   ├── session.service.js       # Session management
│   │   ├── pubsub.service.js        # Pub/Sub handling
│   │   └── queue.service.js         # Job queue management
│   ├── middleware/
│   │   ├── cache.middleware.js       # HTTP caching middleware
│   │   └── rate-limit.middleware.js  # Rate limiting
│   ├── utils/
│   │   ├── redis.helper.js          # Redis helper functions
│   │   ├── serializer.js            # Data serialization
│   │   └── key-prefix.js            # Key namespace management
│   ├── scripts/
│   │   ├── acquire-lock.lua         # Distributed lock script
│   │   ├── rate-limit.lua           # Rate limiting script
│   │   └── counter.lua              # Atomic counter script
│   └── index.js                     # Application entry point
├── tests/
│   ├── redis/
│   │   ├── cache.test.js
│   │   └── pubsub.test.js
│   └── fixtures/
│       └── redis.fixture.js
├── docker-compose.yml               # Redis server setup
├── .env                             # Environment variables
├── package.json
└── README.md
```

## Configuration File Example

```
// src/config/redis.config.js
const Redis = require('ioredis');

const redisConfig = {
  host: process.env.REDIS_HOST || 'localhost',
  port: process.env.REDIS_PORT || 6379,
  password: process.env.REDIS_PASSWORD,
  db: 0,
  retryDelayOnFailover: 300,
  enableReadyCheck: true,
  maxRetriesPerRequest: 3,
};

const client = new Redis(redisConfig);

module.exports = client;
```

## Key Naming Convention

```
# Namespace:type:id:field
user:1234:profile
user:1234:sessions
session:abc123
cache:api:/users/1234
rate:limit:api:192.168.1.1
lock:resource:order:789
queue:jobs:pending
pub:notifications:general
```

## Service Layer Pattern

```
class CacheService {
  constructor(redisClient, options = {}) {
    this.client = redisClient;
    this.defaultTTL = options.ttl || 3600;
    this.keyPrefix = options.keyPrefix || 'cache';
  }

  async get(key) {
    const data = await this.client.get(`${this.keyPrefix}:${key}`);
    return data ? JSON.parse(data) : null;
  }

  async set(key, value, ttl = this.defaultTTL) {
    await this.client.setex(
      `${this.keyPrefix}:${key}`,
      ttl,
      JSON.stringify(value)
    );
  }

  async del(key) {
    await this.client.del(`${this.keyPrefix}:${key}`);
  }
}
```

## Testing Setup

```
// tests/redis/redis.fixture.js
const Redis = require('ioredis');

let client;

beforeAll(async () => {
  client = new Redis({
    host: 'localhost',
    port: 6379,
    db: 15, // Use separate DB for tests
  });
});

afterAll(async () => {
  await client.flushdb();
  await client.quit();
});

beforeEach(async () => {
  await client.flushdb();
});

module.exports = { client };
```
