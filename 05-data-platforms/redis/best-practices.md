# Redis Best Practices

## 1. Key Naming Conventions

```bash
# Use colon separator for namespaces
user:1234:profile
user:1234:sessions
order:5678:items

# Use consistent naming patterns
cache:{service}:{id}
lock:{resource}:{id}
queue:{type}:{priority}
```

## 2. Set TTL on All Cache Keys

```bash
# Always set expiration
SET cache:api:/users/1234 '{"name":"John"}' EX 3600

# Use consistent TTL ranges
# Short-lived: 60-300 seconds (session data)
# Medium-lived: 3600-86400 (API cache)
# Long-lived: 604800-2592000 (reference data)
```

## 3. Use Appropriate Data Structures

```bash
# Use Hashes for objects (not strings with JSON)
HSET user:1234 name "John" email "john@example.com"

# Use Sorted Sets for leaderboards
ZADD leaderboard 100 "player:1"

# Use Sets for unique collections
SADD tags:post:1 "redis" "cache" "nosql"

# Use Lists for queues
LPUSH queue:jobs '{"task":"process"}'
```

## 4. Avoid Large Keys

```bash
# Keep collections small (< 10K members)
# Split large data into multiple keys
user:1234:friends:1      # First 1000 friends
user:1234:friends:2      # Next 1000 friends

# Monitor key size
MEMORY USAGE large:key
DEBUG OBJECT large:key
```

## 5. Use SCAN Instead of KEYS

```bash
# BAD: Blocks server
KEYS user:*

# GOOD: Cursor-based iteration
SCAN 0 MATCH user:* COUNT 100
```

## 6. Enable Connection Pooling

```javascript
// Use connection pool
const redis = new Redis({
  host: 'localhost',
  port: 6379,
  maxRetriesPerRequest: 3,
  retryDelayOnFailover: 300,
});
```

## 7. Use Pipeline for Bulk Operations

```javascript
// Batch commands
const pipeline = redis.pipeline();
for (let i = 0; i < 1000; i++) {
  pipeline.set(`key:${i}`, `value:${i}`);
}
await pipeline.exec();
```

## 8. Use Lua Scripts for Atomic Operations

```lua
-- Atomic increment with limit
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local current = tonumber(redis.call('GET', key) or '0')
if current >= limit then
  return 0
end
redis.call('INCR', key)
return 1
```

## 9. Set Memory Limits

```conf
# Always set maxmemory
maxmemory 8gb

# Choose appropriate eviction policy
maxmemory-policy allkeys-lru
```

## 10. Monitor Memory Usage

```bash
# Check memory
INFO memory

# Check per-key memory
MEMORY USAGE key

# Check encoding
OBJECT ENCODING key
```

## 11. Use Replication for Read Scaling

```conf
# Enable read replicas
replica-read-only yes

# Set minimum replicas for writes
min-replicas-to-write 1
min-replicas-max-lag 10
```

## 12. Enable Persistence Properly

```conf
# Use AOF for durability
appendonly yes
appendfsync everysec

# Use RDB for backups
save 900 1
save 300 10
save 60 10000
```

## 13. Secure Your Instance

```bash
# Set password
requirepass strong_password

# Use ACLs
ACL SETUSER app on >password ~cache:* +get +set

# Rename dangerous commands
rename-command FLUSHALL ""
rename-command DEBUG ""
```

## 14. Use Sentinel for High Availability

```conf
# Configure Sentinel
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 5000
```

## 15. Monitor Slow Log

```bash
# Enable slow log
CONFIG SET slowlog-log-slower-than 10000

# Check slow log
SLOWLOG GET 10
```

## 16. Avoid Blocking Commands

```bash
# BAD: Blocks server
KEYS *
SORT large:list
LRANGE large:list 0 -1

# GOOD: Use non-blocking alternatives
SCAN large:list 0 COUNT 100
SSCAN large:set 0 COUNT 100
```

## 17. Use Hash Tags for Related Keys

```bash
# Keep related keys on same node
{user}:1234:profile
{user}:1234:sessions
{user}:1234:settings
```

## 18. Set Client Timeouts

```conf
# Set client timeout
timeout 300

# Set TCP keepalive
tcp-keepalive 300
```

## 19. Use Environment Variables

```bash
# Don't hardcode credentials
REDIS_HOST=${REDIS_HOST:-localhost}
REDIS_PORT=${REDIS_PORT:-6379}
REDIS_PASSWORD=${REDIS_PASSWORD}
```

## 20. Test Failover Regularly

```bash
# Simulate primary failure
redis-cli DEBUG SLEEP 5

# Check failover
redis-cli -p 26379 SENTINEL get-master-addr-by-name mymaster

# Verify data integrity
redis-cli GET critical:key
```
