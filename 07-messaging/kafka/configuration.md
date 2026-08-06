# Kafka Configuration

> broker.properties, producer.config, consumer.config key settings.

## Broker Configuration

### Essential Settings

```properties
# broker.properties
broker.id=0
listeners=PLAINTEXT://0.0.0.0:9092
advertised.listeners=PLAINTEXT://localhost:9092
log.dirs=/var/lib/kafka/data
num.partitions=3
default.replication.factor=1
log.retention.hours=168
log.segment.bytes=1073741824
log.retention.check.interval.ms=300000
zookeeper.connect=localhost:2181
```

### Performance Tuning

```properties
# Thread and I/O
num.network.threads=8
num.io.threads=16
num.replica.fetchers=4

# Log management
log.retention.hours=168
log.retention.bytes=-1
log.segment.bytes=1073741824
log.cleaner.enable=true

# Request handling
queued.max.requests=500
socket.send.buffer.bytes=102400
socket.receive.buffer.bytes=102400
socket.request.max.bytes=104857600
```

### Replication

```properties
# Replication settings
default.replication.factor=3
min.insync.replicas=2
unclean.leader.election.enable=false
replica.lag.time.max.ms=30000
replica.socket.timeout.ms=30000
replica.socket.receive.buffer.bytes=1048576
```

### Security

```properties
# SSL
listeners=SSL://0.0.0.0:9093
ssl.keystore.location=/path/to/kafka.server.keystore.jks
ssl.keystore.password=changeit
ssl.key.password=changeit
ssl.truststore.location=/path/to/kafka.server.truststore.jks
ssl.truststore.password=changeit

# SASL
listeners=SASL_PLAINTEXT://0.0.0.0:9092
sasl.enabled.mechanisms=PLAIN
sasl.mechanism.inter.broker.protocol=PLAIN

# ACLs
authorizer.class.name=kafka.security.authorizer.AclAuthorizer
super.users=User:admin
```

## Producer Configuration

### Essential Settings

```properties
# producer.config
bootstrap.servers=localhost:9092
key.serializer=org.apache.kafka.common.serialization.StringSerializer
value.serializer=org.apache.kafka.common.serialization.StringSerializer
acks=all
retries=3
batch.size=16384
linger.ms=0
buffer.memory=33554432
compression.type=none
```

### Performance Tuning

```properties
# Batching
batch.size=32768
linger.ms=10
buffer.memory=67108864

# Compression
compression.type=snappy  # none, gzip, snappy, lz4, zstd

# Retries
retries=2147483647
delivery.timeout.ms=120000
request.timeout.ms=30000

# Idempotence (exactly-once)
enable.idempotence=true
max.in.flight.requests.per.connection=5
```

### Common Configurations

| Property | Description | Default |
|---------|-------------|---------|
| bootstrap.servers | Broker list | localhost:9092 |
| acks | Replication acks | 1 |
| retries | Retry attempts | 2147483647 |
| batch.size | Batch buffer size | 16384 |
| linger.ms | Batch wait time | 0 |
| buffer.memory | Total buffer memory | 33554432 |
| compression.type | Compression | none |
| max.block.ms | Max block time | 60000 |
| request.timeout.ms | Request timeout | 30000 |
| delivery.timeout.ms | Delivery timeout | 120000 |

## Consumer Configuration

### Essential Settings

```properties
# consumer.config
bootstrap.servers=localhost:9092
group.id=my-consumer-group
key.deserializer=org.apache.kafka.common.serialization.StringDeserializer
value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
auto.offset.reset=earliest
enable.auto.commit=true
auto.commit.interval.ms=5000
```

### Common Settings

| Property | Description | Default |
|---------|-------------|---------|
| group.id | Consumer group ID | required |
| auto.offset.reset | Reset strategy | latest |
| enable.auto.commit | Auto commit offsets | true |
| auto.commit.interval.ms | Commit interval | 5000 |
| max.poll.records | Records per poll | 500 |
| max.poll.interval.ms | Max poll interval | 300000 |
| session.timeout.ms | Session timeout | 45000 |
| heartbeat.interval.ms | Heartbeat interval | 3000 |

### Consumer Tuning

```properties
# Fetch settings
fetch.min.bytes=1
fetch.max.wait.ms=500
max.partition.fetch.bytes=1048576

# Session management
session.timeout.ms=45000
heartbeat.interval.ms=3000
max.poll.interval.ms=300000

# Offset management
enable.auto.commit=false
auto.offset.reset=earliest
isolation.level=read_committed
```

## Topic Configuration

```properties
# Topic-level overrides
cleanup.policy=delete
compression.type=snappy
retention.ms=604800000
retention.bytes=-1
segment.ms=604800000
segment.bytes=1073741824
min.insync.replicas=2
```

## Dynamic Configuration

```bash
# Update topic config
kafka-configs.sh --bootstrap-server localhost:9092 \
    --entity-type topics \
    --entity-name orders \
    --alter \
    --add-config retention.ms=259200000

# Update broker config
kafka-configs.sh --bootstrap-server localhost:9092 \
    --entity-type brokers \
    --entity-name 0 \
    --alter \
    --add-config message.max.bytes=10000000
```

## Container Configuration

```properties
# Docker/Kubernetes
listeners=PLAINTEXT://0.0.0.0:9092
advertised.listeners=PLAINTEXT://kafka:9092
KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092
KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT
KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT
```

## Environment Variables

```bash
# Common env vars
KAFKA_BROKER_ID=1
KAFKA_ZOOKEEPER_CONNECT=localhost:2181
KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092
KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092
KAFKA_LOG_RETENTION_HOURS=168
KAFKA_NUM_PARTITIONS=3
KAFKA_DEFAULT_REPLICATION_FACTOR=1
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=3
```

## References

- [Kafka Broker Config](https://kafka.apache.org/documentation/#brokerconfigs)
- [Kafka Producer Config](https://kafka.apache.org/documentation/#producerconfigs)
- [Kafka Consumer Config](https://kafka.apache.org/documentation/#consumerconfigs)

---
**Prerequisites:** [Kafka core-concepts](core-concepts.md)
**Related:** [Kafka performance](../../14-cloud/azure/performance.md) | [Kafka production](../../14-cloud/azure/production.md)
**Next:** [Kafka installation](installation.md)
