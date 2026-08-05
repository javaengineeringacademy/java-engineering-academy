# Redis Debugging

## SLOWLOG

### Enable and Configure

```bash
# Set slow log threshold (microseconds)
CONFIG SET slowlog-log-slower-than 10000

# Set maximum entries
CONFIG SET slowlog-max-len 128
```

### Query Slow Log

```bash
# Get last 10 entries
SLOWLOG GET 10

# Get count of entries
SLOWLOG LEN

# Reset slow log
SLOWLOG RESET
```

### Slow Log Entry Format

```
1) (integer) 1          # Entry ID
2) (integer) 1612345678 # Timestamp
3) (integer) 15000      # Execution time (microseconds)
4) 1) "KEYS"            # Command
   2) "*"
5) "127.0.0.1:54321"    # Client address
6) ""                   # Client name
```

## MONITOR

### Enable Monitoring

```bash
# Enable real-time command monitoring
MONITOR

# Disable
MONITOR OFF
```

### Output Format

```
1612345678.123456 [0 127.0.0.1:54321] "SET" "key" "value"
1612345678.123457 [0 127.0.0.1:54321] "GET" "key"
```

### Use Cases

- Debugging command flow
- Verifying command execution
- Performance analysis

## DEBUG Commands

### DEBUG SLEEP

```bash
# Simulate server delay (seconds)
DEBUG SLEEP 0.1

# Test timeout behavior
DEBUG SLEEP 5
```

### DEBUG OBJECT

```bash
# Inspect key internals
DEBUG OBJECT mykey

# Output includes:
# Value encoding
# LRU time
# Reference count
```

### DEBUG ERROR

```bash
# Force error response
DEBUG ERROR "test error"
```

## Latency Monitoring

### Enable Latency Monitor

```bash
# Set threshold (milliseconds)
CONFIG SET latency-monitor-threshold 10

# Check latest events
LATENCY LATEST

# Get history for event
LATENCY HISTORY command

# Reset latency data
LATENCY RESET
```

### Latency Events

```bash
# Common events
command      # Command execution
fast-command # Fast command execution
fork         # Fork operation
rdb-unlink-temp-file # RDB cleanup
aof-write    # AOF write
```

## Client Inspection

### List Clients

```bash
# List all clients
CLIENT LIST

# Get client ID
CLIENT ID

# Get client info
CLIENT INFO
```

### Client Operations

```bash
# Set client name
CLIENT SETNAME myapp

# Get client name
CLIENT GETNAME

# Kill client
CLIENT KILL ip:port

# Pause client
CLIENT PAUSE 5000
```

## Key Inspection

### OBJECT Commands

```bash
# Check encoding
OBJECT ENCODING mykey

# Check TTL
OBJECT IDLETIME mykey

# Check reference count
OBJECT REFCOUNT mykey
```

### MEMORY Command

```bash
# Check key memory usage
MEMORY USAGE mykey

# Check memory stats
MEMORY STATS

# Check allocator
MEMORY MALLOC-STATS
```

### TYPE and EXPIRE

```bash
# Check key type
TYPE mykey

# Check TTL
TTL mykey
PTTL mykey

# Check if key exists
EXISTS mykey
```

## Server Inspection

### INFO Command

```bash
# All info
INFO

# Specific sections
INFO server
INFO clients
INFO memory
INFO stats
INFO replication
INFO keyspace
```

### CONFIG Command

```bash
# Get config
CONFIG GET maxmemory

# Set config
CONFIG SET maxmemory 4gb

# Rewrite config
CONFIG REWRITE
```

### CLUSTER Info

```bash
# Cluster status
CLUSTER INFO

# Cluster nodes
CLUSTER NODES

# Cluster slots
CLUSTER SLOTS
```

## Transaction Debugging

### Watch and Multi

```bash
# Start transaction
MULTI

# Queue commands
SET key1 value1
SET key2 value2

# Execute
EXEC

# Discard
DISCARD
```

### Lua Script Debugging

```bash
# Load script
SCRIPT LOAD "return redis.call('GET', KEYS[1])"

# Execute
EVALSHA <sha1> 1 mykey

# Debug mode (Redis 3.2+)
EVAL debug <script> 0
```

## Network Debugging

### Test Connectivity

```bash
# Ping
redis-cli PING

# Test connection
redis-cli -h host -p port -a password

# Check latency
redis-cli --latency

# Latency distribution
redis-cli --latency-dist
```

### Monitor Network

```bash
# Check connections
INFO clients

# Check network
INFO stats | grep total_connections_received
INFO stats | grep rejected_connections
```

## Common Debug Patterns

### Debug Cache Miss

```bash
# Check if key exists
EXISTS key

# Check TTL
TTL key

# Check type
TYPE key

# Check encoding
OBJECT ENCODING key
```

### Debug Memory Issue

```bash
# Check memory usage
INFO memory

# Check per-key memory
MEMORY USAGE key

# Check encoding
OBJECT ENCODING key

# Check fragmentation
INFO memory | grep mem_fragmentation_ratio
```

### Debug Performance Issue

```bash
# Check slow log
SLOWLOG GET 10

# Check latency
LATENCY LATEST

# Check command stats
INFO commandstats
```

## Debugging Checklist

1. Check SLOWLOG for slow commands
2. Use MONITOR for real-time debugging
3. Check INFO for server status
4. Use MEMORY USAGE for memory issues
5. Check OBJECT ENCODING for encoding issues
6. Use LATENCY for performance issues
7. Check CLIENT LIST for connection issues
8. Use DEBUG OBJECT for key internals
9. Check CLUSTER INFO for cluster issues
10. Monitor replication lag
