# Redis Data Structures

## Comprehensive Guide to Redis Data Types

Redis provides rich data structures for different use cases. This guide covers strings, lists, sets, sorted sets, hashes, and streams.

---

## Table of Contents

1. [Strings](#strings)
2. [Lists](#lists)
3. [Sets](#sets)
4. [Sorted Sets](#sorted-sorted-sets)
5. [Hashes](#hashes)
6. [Streams](#streams)
7. [Best Practices](#best-practices)

---

## Strings

### Basic Operations

```bash
# Set
SET key "value"

# Get
GET key

# Delete
DEL key

# Check existence
EXISTS key

# Get type
TYPE key

# Get TTL
TTL key

# Set TTL
EXPIRE key 3600
```

### Numeric Operations

```bash
# Increment
INCR counter
INCRBY counter 5

# Decrement
DECR counter
DECRBY counter 5

# Float increment
INCRBYFLOAT float_counter 1.5
```

### String Operations

```bash
# Append
APPEND key " additional"

# Get range
GETRANGE key 0 4

# Set range
SETRANGE key 6 "new value"

# Get length
STRLEN key
```

---

## Lists

### Basic Operations

```bash
# Push
LPUSH mylist "value1"
RPUSH mylist "value2"

# Pop
LPOP mylist
RPOP mylist

# Peek
LRANGE mylist 0 -1
LINDEX mylist 0

# Length
LLEN mylist

# Trim
LTRIM mylist 0 99
```

### Advanced Operations

```bash
# Blocking pop
BLPOP mylist 30
BRPOP mylist 30

# Move between lists
RPOPLPUSH source destination
BRPOPLPUSH source destination 30

# Insert
LINSERT mylist BEFORE "value1" "newvalue"
LINSERT mylist AFTER "value1" "newvalue"

# Remove
LREM mylist 2 "value"
```

---

## Sets

### Basic Operations

```bash
# Add
SADD myset "value1"
SADD myset "value2" "value3"

# Remove
SREM myset "value1"

# Check membership
SISMEMBER myset "value1"

# Get members
SMEMBERS myset

# Count
SCARD myset

# Pop random
SPOP myset
```

### Set Operations

```bash
# Intersection
SINTER set1 set2
SINTERSTORE destination set1 set2

# Union
SUNION set1 set2
SUNIONSTORE destination set1 set2

# Difference
SDIFF set1 set2
SDIFFSTORE destination set1 set2

# Random members
SRANDMEMBER myset 3
```

---

## Sorted Sets

### Basic Operations

```bash
# Add
ZADD myzset 1 "value1"
ZADD myzset 2 "value2" 3 "value3"

# Remove
ZREM myzset "value1"

# Get score
ZSCORE myzset "value1"

# Get rank
ZRANK myzset "value1"

# Increment score
ZINCRBY myzset 1 "value1"
```

### Range Operations

```bash
# By rank
ZRANGE myzset 0 -1 WITHSCORES
ZREVRANGE myzset 0 -1

# By score
ZRANGEBYSCORE myzset 1 2
ZREVRANGEBYSCORE myzset 2 1

# By score with limit
ZRANGEBYSCORE myzset 1 2 LIMIT 0 10

# Count
ZCOUNT myzset 1 2
```

---

## Hashes

### Basic Operations

```bash
# Set field
HSET myhash field1 "value1"
HSET myhash field2 "value2"

# Get field
HGET myhash field1

# Get all
HGETALL myhash

# Delete field
HDEL myhash field1

# Check field exists
HEXISTS myhash field1

# Get all fields
HKEYS myhash

# Get all values
HVALS myhash
```

### Advanced Operations

```bash
# Increment
HINCRBY myhash field1 5
HINCRBYFLOAT myhash field1 1.5

# Set multiple
HMSET myhash field1 "value1" field2 "value2"

# Get multiple
HMGET myhash field1 field2

# Set if not exists
HSETNX myhash field1 "value1"

# Get length
HLEN myhash
```

---

## Streams

### Basic Operations

```bash
# Add entry
XADD mystream * field1 value1

# Read entries
XRANGE mystream - +

# Read entries with count
XRANGE mystream - + COUNT 10

# Read entries by ID
XRANGE mystream 1234567890-0 1234567890-10

# Delete entry
XDEL mystream 1234567890-0
```

### Consumer Groups

```bash
# Create consumer group
XGROUP CREATE mystream mygroup $ MKSTREAM

# Read as consumer
XREADGROUP GROUP mygroup consumer1 COUNT 10 STREAMS mystream >

# Acknowledge
XACK mystream mygroup 1234567890-0

# View pending
XPENDING mystream mygroup
```

---

## Best Practices

### 1. Use Appropriate Types

```bash
# Good - Use counter
INCR page:views

# Good - Use set for unique items
SADD tags "tag1" "tag2"

# Good - Use sorted set for leaderboards
ZADD leaderboard 100 "player1"
```

### 2. Use Bulk Operations

```bash
# Good - Pipeline
MULTI
SET key1 "value1"
SET key2 "value2"
EXEC

# Good - MSET/MGET
MSET key1 "value1" key2 "value2"
MGET key1 key2
```

### 3. Use Expiration

```bash
# Good - Set expiration
SETEX session:abc123 3600 "user_data"

# Good - Set TTL after creation
SET session:abc123 "user_data"
EXPIRE session:abc123 3600
```

### 4. Use Namespaces

```bash
# Good - Namespaced keys
SET user:123:name "John"
SET user:123:email "john@example.com"

# Good - Use hash for objects
HSET user:123 name "John" email "john@example.com"
```

### 5. Monitor Memory

```bash
# Check memory usage
INFO memory

# Check big keys
redis-cli --bigkeys

# Check key size
DEBUG OBJECT key
```

---

## Further Reading

- [Redis Data Types](https://redis.io/docs/data-types/)
- [Redis Commands](https://redis.io/commands/)
- [Redis Streams](https://redis.io/docs/data-types/streams/)
