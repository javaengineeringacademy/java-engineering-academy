# Redis Anti-Patterns

## 1. Big Keys
**Description:** Storing very large values (strings > 10KB, collections with > 10K elements).

**Why it's bad:** Causes memory issues, blocks other operations, slow network transfer.

**Example (bad code):**
```bash
# Storing large JSON blob
SET user:1000 '{huge json with nested data...}'

# Large list
RPUSH queue item1 item2 ... item1000000
```

**Better approach:** Break into smaller chunks or use different data structures:
```bash
# Store smaller, indexed data
HSET user:1000 name "John" age 30
HSET user:1000:profile bio "Long bio..."

# Use pagination for large collections
LRANGE queue 0 99
```

**Impact:** Better memory usage, faster operations.

---

## 2. Hot Keys
**Description:** Single keys receiving disproportionate traffic.

**Why it's bad:** Can overload single Redis instance, cause latency spikes.

**Example (bad code):**
```bash
# Single counter for all users
INCR global:pageviews

# Popular product stock
DECR stock:popular-product
```

**Better approach:** Distribute load:
```bash
# Use key prefix with random suffix
INCR global:pageviews:shard1
INCR global:pageviews:shard2

# Use Redis Cluster for distribution
```

**Impact:** Even load distribution, better performance.

---

## 3. Blocking Commands
**Description:** Using commands that block the Redis server.

**Why it's bad:** Blocks all other operations, causes latency for entire application.

**Example (bad code):**
```bash
# Blocks until key exists
BRPOP queue 0

# Blocks during save
SAVE
BGSAVE
```

**Better approach:** Use non-blocking alternatives:
```bash
# Use timeout with BRPOP
BRPOP queue 5

# Use scheduled BGSAVE
CONFIG SET save ""
```

**Impact:** Non-blocking operations, better responsiveness.

---

## 4. Using KEYS in Production
**Description:** Using KEYS command for pattern matching in production.

**Why it's bad:** O(n) operation, blocks entire database during execution.

**Example (bad code):**
```bash
# Scans all keys - blocks server
KEYS user:*
KEYS session:*
```

**Better approach:** Use SCAN:
```bash
SCAN 0 MATCH user:* COUNT 100
```

**Impact:** Non-blocking iteration, better performance.

---

## 5. Not Using Pipelining
**Description:** Sending commands one at a time instead of batching.

**Why it's bad:** Excessive network round trips, poor throughput.

**Example (bad code):**
```python
for item in items:
    redis.set(item['key'], item['value'])
```

**Better approach:** Use pipeline:
```python
pipe = redis.pipeline()
for item in items:
    pipe.set(item['key'], item['value'])
pipe.execute()
```

**Impact:** Reduced network overhead, better throughput.

---

## 6. Storing Large Collections Unnecessarily
**Description:** Storing data that could be computed or derived.

**Why it's bad:** Wastes memory, stale data issues.

**Example (bad code):**
```bash
# Store computed results
SADD user:1000:friends user:2000 user:3000 user:4000
SADD user:2000:friends user:1000 user:5000
# Recompute and store every time relationship changes
```

**Better approach:** Compute on-demand or cache with TTL:
```bash
# Use intersection for common operations
SINTER user:1000:friends user:2000:friends

# Cache with expiry
SETEX user:1000:mutual-friends 300 "result"
```

**Impact:** Less memory usage, fresher data.

---

## 7. Not Using Expiration
**Description:** Not setting TTL on cache keys.

**Why it's bad:** Memory leaks, stale data accumulation.

**Example (bad code):**
```bash
# Data never expires
SET cache:product:123 '{data}'
```

**Better approach:** Set appropriate TTL:
```bash
SETEX cache:product:123 300 '{data}'
```

**Impact:** Automatic cleanup, prevents memory leaks.

---

## 8. Ignoring Serialization Format
**Description:** Using inefficient serialization (e.g., JSON for simple data).

**Why it's bad:** Larger payloads, slower serialization/deserialization.

**Example (bad code):**
```json
{"id": 123, "name": "John", "active": true}
```

**Better approach:** Use appropriate format:
```bash
# For simple data, use Redis native types
HSET user:123 name "John" active true

# For complex data, consider MessagePack or Protocol Buffers
```

**Impact:** Smaller payloads, faster operations.

---

## 9. Not Using Connection Pooling
**Description:** Creating new connections for each operation.

**Why it's bad:** Connection overhead, exhausted connections under load.

**Example (bad code):**
```python
for i in range(1000):
    conn = redis.Redis()
    conn.get('key')
    conn.close()
```

**Better approach:** Use connection pool:
```python
pool = redis.ConnectionPool(host='localhost', port=6379, max_connections=10)
r = redis.Redis(connection_pool=pool)
for i in range(1000):
    r.get('key')
```

**Impact:** Better performance, resource efficiency.

---

## 10. Ignoring Redis Memory Policy
**Description:** Not configuring eviction policies for memory-constrained environments.

**Why it's bad:** Redis may crash when memory limit reached.

**Example (bad code):**
```bash
# Using default noeviction policy
# Redis crashes when memory full
```

**Better approach:** Configure appropriate policy:
```bash
CONFIG SET maxmemory 1gb
CONFIG SET maxmemory-policy allkeys-lru
```

**Impact:** Graceful degradation, prevents crashes.

---

## 11. Using Multi-Key Operations on Different Slots
**Description:** Using operations that require multiple keys in same slot in Cluster mode.

**Why it's bad:** Causes cross-slot errors in Redis Cluster.

**Example (bad code):**
```bash
# Keys in different slots
MSET user:1000:name "John" user:2000:name "Jane"
SINTER user:1000:friends user:2000:friends
```

**Better approach:** Use hash tags to ensure same slot:
```bash
# Use hash tags
MSET {user:1000}:name "John" {user:2000}:name "Jane"
```

**Impact:** Cluster-compatible operations.

---

## 12. Not Monitoring Memory Usage
**Description:** Not tracking Redis memory consumption.

**Why it's bad:** Unexpected OOM, no capacity planning.

**Example (bad code):**
```bash
# No monitoring setup
# Surprised when Redis crashes
```

**Better approach:** Monitor and alert:
```bash
INFO memory
INFO stats
redis-cli --bigkeys
redis-cli --memkeys
```

**Impact:** Proactive issue detection, better capacity planning.