# Kafka Relationships

## Works With

### Apache Flink

Kafka provides the data source and sink for Flink streaming jobs. Flink consumes from Kafka topics, processes data, and writes back to Kafka. Flink's exactly-once semantics integrate with Kafka transactions for end-to-end consistency.

Use Flink's Kafka connector for source and sink configuration. Flink manages checkpointing and offset commits.

### Apache Spark

Spark Structured Streaming reads from and writes to Kafka topics. Spark can batch process historical data or consume real-time streams. Spark's checkpointing integrates with Kafka offset management.

Use `spark-sql-kafka` connector for Spark 2.4+ or the direct API for older versions.

### Redis

Kafka and Redis often serve complementary roles. Kafka handles durable, ordered event streams. Redis provides fast caching and real-time state. Common pattern: Kafka for event ingestion, Redis for lookups and session data.

Use Kafka Connect Redis Sink to write from Kafka to Redis. Or use custom producers/consumers for bidirectional integration.

### Kafka Connect

Kafka Connect is a framework for streaming data between Kafka and external systems. Source connectors pull data into Kafka. Sink connectors push data from Kafka to other systems.

Use pre-built connectors for databases (JDBC), storage (S3, HDFS), and services (Elasticsearch, MongoDB). Custom connectors can be written with the Connect API.

### Apache Kafka Streams

Kafka Streams is a client library for building streaming applications. It reads from and writes to Kafka topics. It provides state stores, windowing, and exactly-once semantics.

Use Kafka Streams for lightweight stream processing without a separate cluster. It runs within your application process.

## Alternative

### Apache Pulsar

Pulsar is a distributed messaging and streaming platform. It offers multi-tenancy, tiered storage, and geo-replication natively. Pulsar uses a separate bookkeeper layer for storage.

Consider Pulsar if you need multi-tenancy or built-in tiered storage. Kafka has better ecosystem maturity and wider adoption.

### Redpanda

Redpanda is a Kafka-compatible streaming platform written in C++. It claims lower latency and simpler operations. Redpanda uses a single binary with no JVM or ZooKeeper dependency.

Consider Redpanda if you want Kafka compatibility with simpler deployment. It is a drop-in replacement for many Kafka workloads.

## Competitor

### RabbitMQ

RabbitMQ is a message broker supporting multiple protocols (AMQP, MQTT, STOMP). It excels at traditional message queuing with routing, acknowledgment, and dead-letter queues.

Choose RabbitMQ for complex routing, task queues, and when you need message-level acknowledgment. Choose Kafka for high-throughput streaming, event sourcing, and log aggregation.

RabbitMQ is better for request-reply patterns. Kafka is better for event streaming and replay.

## Migration Notes

Migrating from Kafka to alternatives requires consideration of:
- Message ordering guarantees
- Consumer group semantics
- Retention and compaction policies
- Exactly-once delivery guarantees
- Ecosystem tooling (Connect, Streams, monitoring)

Migrating to Kafka from alternatives requires:
- Topic design and partition strategy
- Offset management and consumer group configuration
- Retention and compaction settings
- Schema registry integration
