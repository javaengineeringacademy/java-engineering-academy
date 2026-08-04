# Sharding Fundamentals

## Comprehensive Guide to Database Sharding

Sharding distributes data across multiple databases for scalability. This guide covers sharding strategies, routing, and management.

---

## Table of Contents

1. [Sharding Concepts](#sharding-concepts)
2. [Sharding Strategies](#sharding-strategies)
3. [Shard Management](#shard-management)
4. [Best Practices](#best-practices)

---

## Sharding Concepts

### What is Sharding?

```
- Horizontal partitioning of data
- Each shard contains subset of data
- Shards can be on different servers
- Improves scalability and performance
```

### Sharding Architecture

```
+----------------+     +----------------+     +----------------+
|   Application  |---->|   Router       |---->|   Shard 1      |
+----------------+     +----------------+     +----------------+
                             |                  +----------------+
                             |                  |   Shard 2      |
                             |                  +----------------+
                             |                  +----------------+
                             +----------------->|   Shard 3      |
                                                +----------------+
```

---

## Sharding Strategies

### Hash-Based Sharding

```python
def get_shard(key, num_shards):
    return hash(key) % num_shards

# Example
key = "user:123"
num_shards = 4
shard = get_shard(key, num_shards)  # Returns 3
```

```
Pros:
- Even distribution
- Simple implementation

Cons:
- Difficult to add/remove shards
- Range queries inefficient
```

### Range-Based Sharding

```python
def get_shard(key, ranges):
    for i, (start, end) in enumerate(ranges):
        if start <= key < end:
            return i
    return len(ranges) - 1

# Example
ranges = [(0, 1000), (1000, 2000), (2000, 3000)]
key = 1500
shard = get_shard(key, ranges)  # Returns 1
```

```
Pros:
- Range queries efficient
- Easy to add/remove shards

Cons:
- Potential hotspots
- Uneven distribution
```

### Directory-Based Sharding

```python
def get_shard(key, directory):
    return directory[key]

# Example
directory = {
    "user:123": "shard1",
    "user:456": "shard2",
    "user:789": "shard3"
}
shard = get_shard("user:123", directory)  # Returns "shard1"
```

```
Pros:
- Flexible mapping
- Easy to rebalance

Cons:
- Directory is single point of failure
- Extra lookup overhead
```

---

## Shard Management

### Adding Shards

```sql
-- Add new shard
CREATE DATABASE shard_4;

-- Rebalance data
INSERT INTO shard_4 SELECT * FROM shard_1 WHERE id % 4 = 3;
DELETE FROM shard_1 WHERE id % 4 = 3;
```

### Removing Shards

```sql
-- Move data from shard
INSERT INTO shard_1 SELECT * FROM shard_4 WHERE id % 3 = 1;
DELETE FROM shard_4;

-- Drop shard
DROP DATABASE shard_4;
```

### Rebalancing

```python
def rebalance(shards, new_num_shards):
    for shard in shards:
        for key in shard.keys():
            new_shard = get_shard(key, new_num_shards)
            if new_shard != shard.id:
                move_key(key, shard.id, new_shard)
```

---

## Best Practices

### 1. Choose Right Sharding Key

```python
# Good - High cardinality key
shard_key = "user_id"

# Bad - Low cardinality key
shard_key = "country"
```

### 2. Use Consistent Hashing

```python
import hashlib

def consistent_hash(key, num_shards):
    hash_value = int(hashlib.md5(key.encode()).hexdigest(), 16)
    return hash_value % num_shards
```

### 3. Handle Cross-Shard Queries

```python
def cross_shard_query(query, shards):
    results = []
    for shard in shards:
        results.extend(shard.execute(query))
    return results
```

### 4. Monitor Shard Health

```python
def check_shard_health(shards):
    for shard in shards:
        if shard.load > threshold:
            rebalance(shard)
```

### 5. Plan for Growth

```python
# Start with more shards than needed
num_shards = 16  # Instead of 4

# Use virtual shards
virtual_shards = 256
physical_shards = 4
```

---

## Further Reading

- [Database Sharding](https://en.wikipedia.org/wiki/Sharding_(database))
- [Consistent Hashing](https://en.wikipedia.org/wiki/Consistent_hashing)
- [Sharding Strategies](https://www.percona.com/blog/)
