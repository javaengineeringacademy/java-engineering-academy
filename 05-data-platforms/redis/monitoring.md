# Redis Monitoring

## INFO Command

### Server Information

```bash
# All information
redis-cli INFO

# Server-specific info
redis-cli INFO server

# Memory info
redis-cli INFO memory

# Clients info
redis-cli INFO clients

# Stats
redis-cli INFO stats

# Replication
redis-cli INFO replication
```

### Key Metrics to Monitor

| Metric | Command | Description |
|--------|---------|-------------|
| `used_memory_human` | INFO memory | Memory usage |
| `connected_clients` | INFO clients | Active connections |
| `instantaneous_ops_per_sec` | INFO stats | Operations per second |
| `keyspace_hits` | INFO stats | Cache hits |
| `keyspace_misses` | INFO stats | Cache misses |
| `expired_keys` | INFO stats | Expired keys |
| `evicted_keys` | INFO stats | Evicted keys |
| `role` | INFO replication | Server role (master/replica) |

## Redis Exporter

### Installation

```bash
# Docker
docker run -d --name redis-exporter \
  -p 9121:9121 \
  oliver006/redis_exporter \
  --redis.addr redis://localhost:6379 \
  --redis.password yourpassword

# Binary
wget https://github.com/oliver006/redis_exporter/releases/latest/download/redis_exporter-linux-amd64
chmod +x redis_exporter-linux-amd64
./redis_exporter-linux-amd64 --redis.addr redis://localhost:6379
```

### Key Metrics Exported

```yaml
# Prometheus scrape config
scrape_configs:
  - job_name: 'redis'
    static_configs:
      - targets: ['localhost:9121']
    metrics_path: /metrics
```

## Prometheus Integration

### prometheus.yml

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'redis'
    static_configs:
      - targets: ['localhost:9121']
    scrape_interval: 5s
```

### Useful PromQL Queries

```promql
# Memory usage
redis_memory_used_bytes

# Hit ratio
redis_keyspace_hits_total / (redis_keyspace_hits_total + redis_keyspace_misses_total)

# Operations per second
rate(redis_commands_processed_total[5m])

# Connected clients
redis_connected_clients

# Evictions
rate(redis_evicted_keys_total[5m])
```

## Grafana Dashboard

### Import Redis Dashboard

1. Open Grafana
2. Go to Dashboards > Import
3. Import dashboard ID: 763 (Redis Dashboard)
4. Select Prometheus data source

### Key Dashboard Panels

- Memory Usage Over Time
- Operations Per Second
- Hit/Miss Ratio
- Connected Clients
- Evictions
- Command Latency

## Slow Log Monitoring

### Enable Slow Log

```bash
# Set threshold (microseconds)
redis-cli CONFIG SET slowlog-log-slower-than 10000

# Check slow log
redis-cli SLOWLOG GET 10

# Slow log statistics
redis-cli SLOWLOG LEN

# Reset slow log
redis-cli SLOWLOG RESET
```

## Latency Monitoring

### Enable Latency Monitor

```bash
# Set threshold (milliseconds)
redis-cli CONFIG SET latency-monitor-threshold 100

# Check latest latency events
redis-cli LATENCY LATEST

# Latency history
redis-cli LATENCY HISTORY event-name

# Reset latency data
redis-cli LATENCY RESET
```

## Health Check Script

```bash
#!/bin/bash
# redis-health-check.sh

REDIS_HOST="localhost"
REDIS_PORT="6379"
REDIS_PASSWORD="yourpassword"

# Check connectivity
if ! redis-cli -h $REDIS_HOST -p $REDIS_PORT -a $REDIS_PASSWORD ping > /dev/null 2>&1; then
    echo "CRITICAL: Redis is not responding"
    exit 2
fi

# Check memory usage
MEMORY=$(redis-cli -h $REDIS_HOST -p $REDIS_PORT -a $REDIS_PASSWORD INFO memory | grep used_memory_rss_human | cut -d: -f2 | tr -d '\r')
echo "Memory usage: $MEMORY"

# Check connected clients
CLIENTS=$(redis-cli -h $REDIS_HOST -p $REDIS_PORT -a $REDIS_PASSWORD INFO clients | grep connected_clients | cut -d: -f2 | tr -d '\r')
echo "Connected clients: $CLIENTS"

# Check hit ratio
HITS=$(redis-cli -h $REDIS_HOST -p $REDIS_PORT -a $REDIS_PASSWORD INFO stats | grep keyspace_hits | cut -d: -f2 | tr -d '\r')
MISSES=$(redis-cli -h $REDIS_HOST -p $REDIS_PORT -a $REDIS_PASSWORD INFO stats | grep keyspace_misses | cut -d: -f2 | tr -d '\r')
if [ $(($HITS + $MISSES)) -gt 0 ]; then
    RATIO=$(echo "scale=2; $HITS * 100 / ($HITS + $MISSES)" | bc)
    echo "Hit ratio: ${RATIO}%"
fi

echo "OK: Redis is healthy"
exit 0
```

## Alerting Rules

```yaml
# Prometheus alerting rules
groups:
  - name: redis-alerts
    rules:
      - alert: RedisDown
        expr: redis_up == 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "Redis is down"

      - alert: RedisHighMemory
        expr: redis_memory_used_bytes / redis_memory_max_bytes > 0.9
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Redis memory usage above 90%"

      - alert: RedisHighEvictions
        expr: rate(redis_evicted_keys_total[5m]) > 10
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High eviction rate detected"
```

## Monitoring Best Practices

1. Monitor memory usage and set alerts for high usage
2. Track hit/miss ratio for cache efficiency
3. Monitor connected clients for connection leaks
4. Use slow log to identify slow commands
5. Enable latency monitoring in production
6. Set up alerts for Redis downtime
7. Monitor replication lag for replicas
8. Track evictions to detect memory pressure
9. Use Grafana dashboards for visualization
10. Regular health checks with automated scripts
