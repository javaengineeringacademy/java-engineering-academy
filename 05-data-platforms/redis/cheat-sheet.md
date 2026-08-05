# Redis Cheat Sheet

## Connection

```bash
redis-cli                          # Connect to localhost:6379
redis-cli -h host -p port -a pass  # Connect with auth
redis-cli --tls                    # Connect with TLS
redis-cli -n 2                     # Select database 2
```

## Server

```bash
PING                               # Test connection
INFO                               # Server info
INFO memory                        # Memory info
INFO clients                       # Client info
INFO stats                         # Statistics
DBSIZE                             # Number of keys
LASTSAVE                           # Last save timestamp
```

## String Commands

```bash
SET key value                      # Set key
SET key value EX 60                # Set with TTL (seconds)
SET key value PX 60000             # Set with TTL (milliseconds)
SET key value NX                   # Set if not exists
SET key value XX                   # Set if exists
GET key                            # Get value
MSET k1 v1 k2 v2                  # Set multiple
MGET k1 k2                        # Get multiple
APPEND key value                   # Append to string
STRLEN key                         # Get string length
INCR key                           # Increment by 1
INCRBY key 10                      # Increment by number
DECR key                           # Decrement by 1
DECRBY key 10                      # Decrement by number
INCRBYFLOAT key 1.5                # Increment by float
```

## Key Commands

```bash
DEL key                            # Delete key(s)
EXISTS key                         # Check if key exists
TYPE key                           # Get key type
TTL key                            # Get TTL (seconds)
PTTL key                           # Get TTL (milliseconds)
EXPIRE key 60                      # Set TTL
PSETEX key 60000 value             # Set with milliseconds TTL
PERSIST key                        # Remove TTL
KEYS pattern                       # Find keys (BLOCKING)
SCAN cursor MATCH pat COUNT n      # Find keys (non-blocking)
RANDOMKEY                          # Get random key
RENAME key newkey                  # Rename key
RENAMENX key newkey                # Rename if new key doesn't exist
```

## Hash Commands

```bash
HSET hash field value              # Set field
HSET hash f1 v1 f2 v2             # Set multiple fields
HGET hash field                    # Get field
HMGET hash f1 f2                   # Get multiple fields
HGETALL hash                       # Get all fields
HKEYS hash                         # Get all field names
HVALS hash                         # Get all values
HDEL hash field                    # Delete field
HEXISTS hash field                 # Check field exists
HLEN hash                          # Get field count
HINCRBY hash field 1               # Increment field
HSCAN hash cursor                  # Iterate fields
```

## List Commands

```bash
LPUSH list value                   # Push to left (head)
RPUSH list value                   # Push to right (tail)
LPOP list                          # Pop from left
RPOP list                          # Pop from right
LRANGE list 0 -1                   # Get all elements
LINDEX list 0                      # Get element by index
LLEN list                          # Get list length
LSET list 0 value                  # Set element by index
LTRIM list 0 99                    # Trim list to 100 elements
LREM list 2 value                  # Remove 2 occurrences of value
BLPOP list 0                       # Block pop left
BRPOP list 0                       # Block pop right
```

## Set Commands

```bash
SADD set member                    # Add member
SMEMBERS set                       # Get all members
SISMEMBER set member               # Check membership
SCARD set                          # Get member count
SREM set member                    # Remove member
SINTER set1 set2                   # Intersection
SUNION set1 set2                   # Union
SDIFF set1 set2                    # Difference
SRANDMEMBER set 3                  # Get 3 random members
SPOP set 3                         # Pop 3 random members
SSCAN set cursor                   # Iterate members
```

## Sorted Set Commands

```bash
ZADD zset score member             # Add member with score
ZRANGE zset 0 -1 WITHSCORES        # Get all by rank
ZREVRANGE zset 0 2 WITHSCORES      # Get top 3
ZRANK zset member                  # Get rank
ZREVRANK zset member               # Get reverse rank
ZSCORE zset member                 # Get score
ZINCRBY zset 10 member             # Increment score
ZRANGEBYSCORE zset 1 10            # Get by score range
ZREM zset member                   # Remove member
ZCARD zset                         # Get member count
ZCOUNT zset 1 10                   # Count by score range
```

## Stream Commands

```bash
XADD stream * field value          # Add entry
XLEN stream                        # Get length
XRANGE stream - +                  # Get all entries
XRANGE stream - + COUNT 10         # Get 10 entries
XREAD COUNT 10 STREAMS stream 0    # Read entries
XGROUP CREATE stream grp1 0        # Create consumer group
XREADGROUP GROUP grp1 consumer1 COUNT 10 STREAMS stream >
XACK stream grp1 entry-id          # Acknowledge entry
```

## Pub/Sub Commands

```bash
SUBSCRIBE channel                  # Subscribe to channel
PSUBSCRIBE pattern                 # Subscribe to pattern
PUBLISH channel message            # Publish message
UNSUBSCRIBE channel                # Unsubscribe
```

## Transaction Commands

```bash
MULTI                              # Start transaction
EXEC                               # Execute transaction
DISCARD                            # Discard transaction
WATCH key                          # Watch for changes
```

## Script Commands

```bash
EVAL script numkeys key1 key2 arg1 arg2   # Execute script
SCRIPT LOAD script                  # Cache script
SCRIPT EXISTS script                # Check script cache
SCRIPT FLUSH                        # Clear script cache
EVALSHA sha1 numkeys key1 key2 arg1       # Execute cached script
```

## HyperLogLog Commands

```bash
PFADD hll value                    # Add value
PFCOUNT hll                        # Count unique values
PFMERGE dest hll1 hll2             # Merge HLLs
```

## Bitmap Commands

```bash
SETBIT key offset value            # Set bit
GETBIT key offset                  # Get bit
BITCOUNT key                       # Count set bits
BITOP AND dest key1 key2           # Bitwise AND
BITPOS key 1                       # Find first set bit
```

## Cluster Commands

```bash
CLUSTER INFO                       # Cluster info
CLUSTER NODES                      # List nodes
CLUSTER SLOTS                      # Slot mapping
CLUSTER ADDSLOTS 0 1 2            # Add slots
CLUSTER DELSLOTS 0 1 2            # Remove slots
CLUSTER MEET ip port               # Add node
CLUSTER REPLICATE node-id          # Set replica
CLUSTER FAILOVER                   # Force failover
```

## Sentinel Commands

```bash
SENTINEL masters                    # List masters
SENTINEL get-master-addr-by-name master  # Get master address
SENTINEL replicas master            # List replicas
SENTINEL failover master            # Force failover
SENTINEL reset master               # Reset sentinel
```

## Debug Commands

```bash
SLOWLOG GET 10                      # Get slow log
SLOWLOG LEN                         # Slow log count
SLOWLOG RESET                       # Clear slow log
MONITOR                             # Monitor commands
DEBUG SLEEP 1                       # Sleep seconds
DEBUG OBJECT key                    # Object internals
MEMORY USAGE key                    # Key memory usage
CLIENT LIST                         # List clients
CLIENT KILL id 1                    # Kill client
```
