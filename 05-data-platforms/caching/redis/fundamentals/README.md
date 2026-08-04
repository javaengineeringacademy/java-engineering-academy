# Redis Fundamentals

## Comprehensive Guide to Redis

Redis is an in-memory data store used as database, cache, and message broker. This guide covers data types, persistence, and basic operations.

---

## Table of Contents

1. [Redis Overview](#redis-overview)
2. [Data Types](#data-types)
3. [Persistence](#persistence)
4. [Best Practices](#best-practices)

---

## Redis Overview

### Architecture

```
+------------------+
|   Application    |
+------------------+
        |
        v
+------------------+
|   Redis Server   |
|   (In-Memory)    |
+------------------+
        |
        v
+------------------+
|   Persistence    |
|   (RDB/AOF)      |
+------------------+
```

### Features

```
- In-memory storage
- Multiple data types
- Atomic operations
- Pub/Sub messaging
- Lua scripting
- Transactions
- Persistence options
```

---

## Data Types

### Strings

```bash
# Set value
SET key "value"

# Get value
GET key

# Set with expiration
SETEX key 3600 "value"

# Increment
INCR counter
INCRBY counter 5

# Decrement
DECR counter
DECRBY counter 5

# Append
APPEND key "additional"

# Get range
GETRANGE key 0 4
```

### Lists

```bash
# Push to left
LPUSH mylist "value1"
LPUSH mylist "value2"

# Push to right
RPUSH mylist "value3"

# Pop from left
LPOP mylist

# Pop from right
RPOP mylist

# Get range
LRANGE mylist 0 -1

# Get length
LLEN mylist

# Get by index
LINDEX mylist 0

# Set by index
LSET mylist 0 "newvalue"

# Trim list
LTRIM mylist 0 99
```

### Sets

```bash
# Add members
SADD myset "value1"
SADD myset "value2" "value3"

# Get members
SMEMBERS myset

# Check membership
SISMEMBER myset "value1"

# Get count
SCARD myset

# Remove member
SREM myset "value1"

# Pop random member
SPOP myset

# Set operations
SINTER set1 set2        # Intersection
SUNION set1 set2        # Union
SDIFF set1 set2         # Difference
```

### Sorted Sets

```bash
# Add members with scores
ZADD myzset 1 "value1"
ZADD myzset 2 "value2" 3 "value3"

# Get members by score range
ZRANGEBYSCORE myzset 1 2

# Get members by rank
ZRANGE myzset 0 -1 WITHSCORES

# Get score
ZSCORE myzset "value1"

# Get rank
ZRANK myzset "value1"

# Increment score
ZINCRBY myzset 1 "value1"

# Remove member
ZREM myzset "value1"
```

### Hashes

```bash
# Set field
HSET myhash field1 "value1"
HSET myhash field2 "value2"

# Get field
HGET myhash field1

# Get all fields
HGETALL myhash

# Check field exists
HEXISTS myhash field1

# Delete field
HDEL myhash field1

# Get all fields
HKEYS myhash

# Get all values
HVALS myhash

# Increment field
HINCRBY myhash field1 5
```

---

## Persistence

### RDB Persistence

```conf
# redis.conf
save 900 1      # Save if at least 1 key changed in 900 seconds
save 300 10     # Save if at least 10 keys changed in 300 seconds
save 60 10000   # Save if at least 10000 keys changed in 60 seconds

dbfilename dump.rdb
dir /var/lib/redis
```

### AOF Persistence

```conf
# redis.conf
appendonly yes
appendfilename "appendonly.aof"

# AOF fsync policies
appendfsync always    # Sync after every write
appendfsync everysec  # Sync once per second
appendfsync no        # Let OS handle it
```

### Mixed Persistence

```conf
# Redis 4.0+ RDB-AOF hybrid
aof-use-rdb-preamble yes
```

---

## Best Practices

### 1. Use Key Expiration

```bash
# Good - Set expiration
SETEX session:abc123 3600 "user_data"

# Bad - No expiration
SET session:abc123 "user_data"
```

### 2. Use Pipelines

```bash
# Good - Pipeline
MULTI
SET key1 "value1"
SET key2 "value2"
SET key3 "value3"
EXEC

# Bad - Individual commands
SET key1 "value1"
SET key2 "value2"
SET key3 "value3"
```

### 3. Use Namespaces

```bash
# Good - Namespaced keys
SET user:123:name "John"
SET user:123:email "john@example.com"

# Bad - Flat keys
SET name "John"
SET email "john@example.com"
```

### 4. Use Appropriate Data Types

```bash
# Good - Use counter
INCR page:views

# Bad - Use string
GET page:views
SET page:views 1
```

### 5. Monitor Memory Usage

```bash
# Check memory usage
INFO memory

# Check key size
DEBUG OBJECT key

# Check big keys
redis-cli --bigkeys
```

---

## Further Reading

- [Redis Documentation](https://redis.io/documentation)
- [Redis Data Types](https://redis.io/docs/data-types/)
- [Redis Persistence](https://redis.io/docs/management/persistence/)
