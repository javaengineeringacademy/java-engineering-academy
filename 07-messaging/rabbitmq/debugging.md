# RabbitMQ Debugging

> Logging, management UI, tracing, and diagnostic tools.

## Logging Configuration

```ini
# rabbitmq.conf
log.dir = /var/log/rabbitmq
log.file = rabbit.log
log.file.level = debug

# Log categories
log.category.connection = debug
log.category.channel = debug
```

### Log Levels

| Level | Description |
|-------|-------------|
| debug | Detailed diagnostic info |
| info | Normal operations |
| warning | Potential issues |
| error | Failures |
| critical | Severe failures |

### Log Categories

| Category | Description |
|----------|-------------|
| connection | Client connections |
| channel | Channel operations |
| queue | Queue operations |
| mirroring | Queue mirroring |

## Management UI Debugging

### Connection Details

```
1. Go to Connections tab
2. Click connection name
3. View: client properties, channels, rates
```

### Queue Inspection

```
1. Go to Queues tab
2. Click queue name
3. View: messages, consumers, memory, disk
4. Get messages: publish/consume from UI
```

### Channel Inspection

```
1. Go to Channels tab
2. Click channel name
3. View: prefetch, unacked messages, rates
```

## rabbitmqctl Debugging

```bash
# Cluster status
rabbitmqctl cluster_status

# List queues with details
rabbitmqctl list_queues name messages consumers memory state

# List connections
rabbitmqctl list_connections name state channels

# List channels
rabbitmqctl list_channels name number_of_messages

# Node status
rabbitmqctl status
```

## Tracing

### Firehose Tracing

```bash
# Enable tracing
rabbitmq-plugins enable rabbitmq_tracing

# Trace messages
rabbitmqctl trace_on

# Trace format
rabbitmqctl trace_on -f text -p /
```

### Trace Output

```
<rabbit@node1.1234.0> channel AMQP Connection <ip:port> -> <ip:port> (1)
<rabbit@node1.1234.0> -> exchange 'orders' Routing Keys: ['order.created']
<rabbit@node1.1234.0> -> queue 'order-processing'
<rabbit@node1.1234.0> message:
{content, 60,
<<"application/json">>,...}
```

## Diagnostic Commands

```bash
# Check port connectivity
rabbitmq-diagnostics check_port_connectivity

# Check running status
rabbitmq-diagnostics check_running

# Check alarms
rabbitmq-diagnostics check_alarms

# Memory report
rabbitmq-diagnostics memory_report

# Formatter status
rabbitmq-diagnostics status
```

## Memory Debugging

```bash
# Memory breakdown
rabbitmqctl status | grep -A 20 "memory"

# Memory alarm
rabbitmqctl status | grep -i alarm
```

### Memory Issues

| Symptom | Cause | Solution |
|---------|-------|----------|
| Memory alarm | High watermark | Increase limit or reduce messages |
| Growing memory | Queue backlog | Add consumers or set limits |
| Memory leak | Channel/connection leak | Check for unclosed resources |

## Disk Debugging

```bash
# Disk free
rabbitmq-diagnostics disk_free

# Disk alarm
rabbitmqctl status | grep -i disk
```

## Connection Debugging

```bash
# List connections
rabbitmqctl list_connections name state channels

# Connection details
rabbitmqctl list_connections name state channels user peer_host peer_port
```

### Common Connection Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| Connection refused | Wrong host/port | Check listeners config |
| Authentication failed | Wrong credentials | Check user permissions |
| Connection closed | Heartbeat timeout | Increase heartbeat |

## Channel Debugging

```bash
# List channels
rabbitmqctl list_channels name connection_number number_of_messages

# Channel details
rabbitmqctl list_channels name prefetch_count messages_unacknowledged
```

### Common Channel Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| Channel closed | Max channels reached | Increase channel_max |
| Not enough channels | Reuse channels | Pool channels |

## Performance Debugging

```bash
# Publish/consume rates
rabbitmqctl list_queues name messages messages_ready messages_unacknowledged

# Memory usage
rabbitmqctl list_queues name memory

# Disk usage
rabbitmqctl list_queues name disk_pending
```

## References

- [RabbitMQ Logging](https://www.rabbitmq.com/logging.html)
- [Management UI](https://www.rabbitmq.com/management.html)
- [Firehose Tracing](https://www.rabbitmq.com/firehose.html)

---
**Prerequisites:** [RabbitMQ troubleshooting](../../14-cloud/azure/troubleshooting.md)
**Related:** [RabbitMQ monitoring](monitoring.md) | [RabbitMQ pitfalls](pitfalls.md)
**Next:** [RabbitMQ troubleshooting](../../14-cloud/azure/troubleshooting.md)
