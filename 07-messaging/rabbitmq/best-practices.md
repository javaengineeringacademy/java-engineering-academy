# RabbitMQ Best Practices

> Industry-proven practices for RabbitMQ deployments.

## 1. Use Quorum Queues for Critical Data

```bash
# Declare quorum queue
rabbitmqadmin declare queue name=critical-queue durable=true \
  arguments='{"x-queue-type": "quorum"}'
```

- Provides replication across cluster nodes
- Prevents message loss on node failure
- Uses Raft consensus for consistency

## 2. Set Appropriate Prefetch

```python
# Too low: idle consumers
channel.basic_qos(prefetch_count=1)

# Optimal: balanced load
channel.basic_qos(prefetch_count=10)
```

- Start with 10, tune based on workload
- CPU-intensive: lower prefetch
- I/O-bound: higher prefetch

## 3. Implement Dead Letter Exchanges

```bash
# DLX configuration
rabbitmqadmin declare queue name=dlq durable=true
rabbitmqadmin declare exchange name=dlx type=fanout
rabbitmqadmin declare binding source=dlx destination=dlq
```

- Capture failed messages
- Enable retry logic
- Prevent queue bloat

## 4. Use Durable Exchanges and Queues

```python
channel.exchange_declare(
    exchange='orders',
    exchange_type='topic',
    durable=True  # Survive broker restart
)

channel.queue_declare(
    queue='order-processing',
    durable=True  # Survive broker restart
)
```

## 5. Set Message TTL

```bash
# Queue-level TTL
rabbitmqadmin declare queue name=temp-queue durable=true \
  arguments='{"x-message-ttl": 3600000}'

# Message-level TTL
properties = pika.BasicProperties(
    expiration='3600000'  # 1 hour
)
```

## 6. Use Connection Pooling

```python
from pika import ConnectionPool

pool = ConnectionPool(
    pika.ConnectionParameters('localhost'),
    pool_size=10
)

with pool.acquire() as connection:
    channel = connection.channel()
    # Use channel
```

## 7. Enable Management Plugin

```bash
rabbitmq-plugins enable rabbitmq_management
rabbitmq-plugins enable rabbitmq_prometheus
```

## 8. Implement Producer Confirmations

```python
channel.confirm_delivery()

try:
    channel.basic_publish(
        exchange='orders',
        routing_key='order.created',
        body=message,
        mandatory=True
    )
except pika.exceptions.UnroutableError:
    # Message could not be routed
    pass
```

## 9. Use Mandatory Flag

```python
# Ensure message reaches a queue
channel.basic_publish(
    exchange='orders',
    routing_key='order.created',
    body=message,
    mandatory=True
)
```

## 10. Monitor Queue Depths

```bash
# Check queue depth
rabbitmqctl list_queues name messages

# Set alerts
# Alert when queue depth > threshold
```

## 11. Implement Connection Heartbeats

```ini
# rabbitmq.conf
heartbeat = 60
```

- Detect dead connections
- Prevent firewall timeouts
- Default 60 seconds

## 12. Use Virtual Hosts for Isolation

```bash
# Create vhost
rabbitmqctl add_vhost /production

# Set permissions
rabbitmqctl set_permissions -p /production myuser ".*" ".*" ".*"
```

## 13. Avoid Message Redelivery

```python
# Always acknowledge after processing
def process_message(channel, method, properties, body):
    try:
        # Process message
        channel.basic_ack(delivery_tag=method.delivery_tag)
    except Exception:
        channel.basic_nack(
            delivery_tag=method.delivery_tag,
            requeue=False
        )
```

## 14. Use Lazy Queues for Large Backlogs

```bash
rabbitmqadmin declare queue name=large-queue durable=true \
  arguments='{"x-queue-mode": "lazy"}'
```

## 15. Implement Proper Logging

```python
import logging

logger = logging.getLogger(__name__)

def on_message(channel, method, properties, body):
    logger.info("Received message: %s", body)
    try:
        process(body)
        channel.basic_ack(delivery_tag=method.delivery_tag)
    except Exception as e:
        logger.error("Failed to process: %s", e)
        channel.basic_nack(delivery_tag=method.delivery_tag)
```

## Quick Checklist

- [ ] Use quorum queues for critical data
- [ ] Set appropriate prefetch count
- [ ] Implement dead letter exchanges
- [ ] Use durable exchanges and queues
- [ ] Set message TTL
- [ ] Enable producer confirmations
- [ ] Use mandatory flag for routing guarantees
- [ ] Monitor queue depths
- [ ] Implement connection heartbeats
- [ ] Use virtual hosts for isolation
- [ ] Avoid message redelivery
- [ ] Use lazy queues for backlogs
- [ ] Implement proper logging
- [ ] Enable management and monitoring
- [ ] Use connection pooling

## References

- [RabbitMQ Best Practices](https://www.rabbitmq.com/docs/best-practices)
- [Production Checklist](https://www.rabbitmq.com/docs/production-checklist)

---
**Prerequisites:** [RabbitMQ core-concepts](core-concepts.md)
**Related:** [RabbitMQ pitfalls](pitfalls.md) | [RabbitMQ production](production.md)
**Next:** [RabbitMQ pitfalls](pitfalls.md)
