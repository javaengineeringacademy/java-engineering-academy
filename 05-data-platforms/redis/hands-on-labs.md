# Redis Hands-On Labs

## Lab 1: Basic Operations

### Objective
Learn basic Redis commands and data types.

### Steps

```bash
# Start Redis
redis-cli

# String operations
SET user:1 "John Doe"
GET user:1
INCR counter
DECR counter
MSET key1 "val1" key2 "val2"
MGET key1 key2

# TTL
SETEX temp:data 60 "expires in 60 seconds"
TTL temp:data

# Check type and encoding
TYPE user:1
OBJECT ENCODING user:1
```

### Questions
1. What is the difference between SET and SETEX?
2. How do you check if a key exists?
3. What happens when you GET a key with expired TTL?

## Lab 2: Lists and Queues

### Objective
Implement a simple message queue with Redis lists.

### Steps

```bash
# Create queue
LPUSH queue:emails "email1@example.com"
LPUSH queue:emails "email2@example.com"
LPUSH queue:emails "email3@example.com"

# Process queue
RPOP queue:emails
RPOP queue:emails

# Check queue length
LLEN queue:emails

# Block until message available
BLPOP queue:emails 0

# Get all messages
LRANGE queue:emails 0 -1
```

### Questions
1. What is the difference between LPUSH and RPUSH?
2. How do you implement a stack with Redis?
3. What is BLPOP and when would you use it?

## Lab 3: Sets and Tags

### Objective
Use Redis sets for tagging and set operations.

### Steps

```bash
# Add tags
SADD post:1:tags "redis" "database" "cache"
SADD post:2:tags "redis" "nosql"

# Get all tags
SMEMBERS post:1:tags

# Check if tag exists
SISMEMBER post:1:tags "redis"

# Set operations
SINTER post:1:tags post:2:tags  # Common tags
SUNION post:1:tags post:2:tags  # All tags
SDIFF post:1:tags post:2:tags   # Tags in post 1 not in 2

# Random tag
SRANDMEMBER post:1:tags
```

### Questions
1. What is the difference between SINTER and SUNION?
2. How do you count unique visitors with HyperLogLog?
3. What is the use case for SDIFF?

## Lab 4: Sorted Sets and Leaderboards

### Objective
Implement a leaderboard using Redis sorted sets.

### Steps

```bash
# Add scores
ZADD leaderboard 100 "player:1"
ZADD leaderboard 200 "player:2"
ZADD leaderboard 150 "player:3"
ZADD leaderboard 300 "player:4"

# Get top 3 players
ZREVRANGE leaderboard 0 2 WITHSCORES

# Get rank
ZRANK leaderboard "player:3"

# Update score
ZINCRBY leaderboard 50 "player:1"

# Get players by score range
ZRANGEBYSCORE leaderboard 150 300

# Remove player
ZREM leaderboard "player:4"
```

### Questions
1. What is the difference between ZRANK and ZREVRANK?
2. How do you get the top N players?
3. What is ZRANGEBYSCORE used for?

## Lab 5: Hashes and Objects

### Objective
Store and retrieve user objects with Redis hashes.

### Steps

```bash
# Create user
HSET user:1 name "John Doe" email "john@example.com" age 30

# Get all fields
HGETALL user:1

# Get specific fields
HMGET user:1 name email

# Update field
HSET user:1 age 31

# Increment field
HINCRBY user:1 age 1

# Check field exists
HEXISTS user:1 name

# Delete field
HDEL user:1 age
```

### Questions
1. What is the advantage of hashes over strings?
2. How do you get all field names?
3. What is HINCRBY used for?

## Lab 6: Pub/Sub Messaging

### Objective
Implement pub/sub messaging with Redis.

### Steps

```bash
# Terminal 1: Subscribe
SUBSCRIBE notifications

# Terminal 2: Publish
PUBLISH notifications "New order received"

# Pattern subscription
PSUBSCRIBE news.*

# Unsubscribe
UNSUBSCRIBE notifications
```

### Questions
1. What happens to messages when no subscriber is listening?
2. What is the difference between SUBSCRIBE and PSUBSCRIBE?
3. When would you use streams instead of pub/sub?

## Lab 7: Lua Scripting

### Objective
Write atomic operations with Lua scripts.

### Steps

```bash
# Rate limiting script
EVAL "
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local window = tonumber(ARGV[2])

local current = tonumber(redis.call('GET', key) or '0')
if current >= limit then
  return 0
end

redis.call('INCR', key)
redis.call('EXPIRE', key, window)
return 1
" 1 rate:api 10 60

# Check result
GET rate:api
TTL rate:api
```

### Questions
1. Why use Lua scripts instead of MULTI/EXEC?
2. What is EVALSHA and when would you use it?
3. How do you cache Lua scripts?

## Lab 8: Transactions

### Objective
Use Redis transactions for atomic operations.

### Steps

```bash
# Start transaction
MULTI

# Queue commands
SET account:1:balance 1000
SET account:2:balance 500
EXEC

# Watch for changes
WATCH account:1:balance
GET account:1:balance
MULTI
DECRBY account:1:balance 100
INCRBY account:2:balance 100
EXEC

# Discard transaction
MULTI
SET key1 "value1"
DISCARD
```

### Questions
1. What is the difference between MULTI and Lua?
2. What does WATCH do?
3. What happens if a command fails in MULTI?

## Lab 9: Persistence and Backup

### Objective
Configure persistence and create backups.

### Steps

```bash
# Check persistence status
INFO persistence

# Trigger RDB save
BGSAVE

# Check AOF status
CONFIG GET appendonly

# Enable AOF
CONFIG SET appendonly yes

# Trigger AOF rewrite
BGREWRITEAOF

# Backup RDB file
cp /var/lib/redis/dump.rdb /backup/dump_$(date +%Y%m%d).rdb
```

### Questions
1. What is the difference between SAVE and BGSAVE?
2. How do you restore from backup?
3. What is hybrid persistence?

## Lab 10: Performance Testing

### Objective
Benchmark Redis performance.

### Steps

```bash
# Basic benchmark
redis-benchmark -t set,get -n 100000 -c 50

# Pipeline benchmark
redis-benchmark -t set,get -n 100000 -c 50 -P 10

# Check performance metrics
INFO stats | grep instantaneous_ops_per_sec
INFO stats | grep keyspace_hits

# Monitor slow commands
CONFIG SET slowlog-log-slower-than 10000
SLOWLOG GET 10

# Check latency
redis-cli --latency
```

### Questions
1. What is the difference between -c and -P options?
2. How do you interpret benchmark results?
3. What factors affect Redis performance?

## Lab 11: Redis Cluster Setup

### Objective
Set up a Redis cluster with 6 nodes.

### Steps

```bash
# Create 6 Redis instances
for port in 7000 7001 7002 7003 7004 7005; do
  mkdir -p /etc/redis/$port
  cat > /etc/redis/$port/redis.conf << EOF
port $port
cluster-enabled yes
cluster-config-file nodes-$port.conf
cluster-node-timeout 15000
appendonly yes
EOF
  redis-server /etc/redis/$port/redis.conf
done

# Create cluster
redis-cli --cluster create \
  127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 \
  127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 \
  --cluster-replicas 1

# Check cluster status
redis-cli -c -p 7000 CLUSTER INFO
redis-cli -c -p 7000 CLUSTER NODES
```

### Questions
1. What is the minimum number of nodes for a cluster?
2. How does Redis handle slot migration?
3. What is the difference between MOVED and ASK?

## Lab 12: Sentinel Setup

### Objective
Set up Redis Sentinel for high availability.

### Steps

```bash
# Create sentinel.conf
cat > /etc/redis/sentinel.conf << EOF
port 26379
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 5000
sentinel failover-timeout mymaster 60000
sentinel parallel-syncs mymaster 1
EOF

# Start sentinel
redis-sentinel /etc/redis/sentinel.conf

# Check sentinel status
redis-cli -p 26379 SENTINEL masters
redis-cli -p 26379 SENTINEL get-master-addr-by-name mymaster
```

### Questions
1. What is the quorum in sentinel?
2. How does sentinel detect a failed primary?
3. What happens during failover?
