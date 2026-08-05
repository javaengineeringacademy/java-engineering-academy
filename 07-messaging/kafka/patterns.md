# Kafka Patterns

## 1. Idempotent Producer

**Problem:** Network retries cause duplicate messages when producer retries produce duplicates.

**Solution:** Enable idempotent producer so Kafka assigns each batch a producer ID and sequence number, deduplicating at the broker.

**Implementation:**
```java
Properties props = new Properties();
props.put("enable.idempotence", true);
props.put("acks", "all");
props.put("retries", Integer.MAX_VALUE);
props.put("max.in.flight.requests.per.connection", 5);
KafkaProducer<String, String> producer = new KafkaProducer<>(props);
```

**When to Use:** Every Kafka producer in production. The cost is negligible and the safety is critical.

**When NOT to Use:** Never. Idempotent production should always be enabled.

---

## 2. Consumer Groups for Parallelism

**Problem:** Messages must be processed in parallel across multiple instances without duplicating work.

**Solution:** Use consumer groups so Kafka assigns partitions to consumers within a group. Each partition is consumed by exactly one consumer.

**Implementation:**
```java
Properties props = new Properties();
props.put("group.id", "order-processor-group");
props.put("bootstrap.servers", "kafka:9092");
props.put("enable.auto.commit", false);
KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
consumer.subscribe(List.of("orders"));
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        process(record);
    }
    consumer.commitSync();
}
```

**When to Use:** When you need horizontal scaling of message processing with partition-level ordering guarantees.

**When NOT to Use:** When global ordering is required across all partitions (use a single partition topic).

---

## 3. Exactly-Once Semantics (EOS)

**Problem:** At-least-once delivery causes duplicate processing. Consumers that read and write must be idempotent end-to-end.

**Solution:** Use transactional producers and read-process-commit atomically within a Kafka transaction.

**Implementation:**
```java
props.put("transactional.id", "order-processor-1");
KafkaProducer<String, String> producer = new KafkaProducer<>(props);
producer.initTransactions();

consumer.subscribe(List.of("orders"));
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    producer.beginTransaction();
    try {
        for (ConsumerRecord<String, String> record : records) {
            ProducerRecord<String, String> out = transform(record);
            producer.send(out);
        }
        producer.sendOffsetsToTransaction(
            offsets(consumer), consumer.groupMetadata());
        producer.commitTransaction();
    } catch (Exception e) {
        producer.abortTransaction();
    }
}
```

**When to Use:** When processing must be both idempotent and atomic (financial transactions, inventory).

**When NOT to Use:** When at-least-once with idempotent consumers is sufficient. EOS adds latency and complexity.

---

## 4. Dead-Letter Topics (DLT)

**Problem:** Poison messages block consumer processing and stall the pipeline.

**Solution:** After N retries, route the failing message to a dead-letter topic for manual inspection.

**Implementation:**
```java
int maxRetries = 3;
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        int retryCount = getRetryCount(record);
        try {
            process(record);
        } catch (Exception e) {
            if (retryCount >= maxRetries) {
                producer.send(new ProducerRecord<>(
                    "orders.dlt", record.key(), record.value()));
            } else {
                producer.send(new ProducerRecord<>(
                    "orders.retry", record.key(), record.value()));
            }
        }
    }
}
```

**When to Use:** Any pipeline where individual messages can fail independently and blocking is unacceptable.

**When NOT to Use:** When all messages must succeed atomically or when the topic has strict ordering requirements.

---

## 5. Outbox Pattern

**Problem:** Dual writes (database + Kafka) are not atomic. If one fails, data is inconsistent.

**Solution:** Write to an outbox table in the same transaction as the business data, then a CDC connector reads the outbox and publishes to Kafka.

**Implementation:**
```sql
BEGIN;
INSERT INTO orders (id, amount) VALUES ('o-123', 99.00);
INSERT INTO outbox (aggregate_id, event_type, payload)
VALUES ('o-123', 'OrderCreated', '{"id":"o-123","amount":99.00}');
COMMIT;
```

```json
{
  "transforms": "outbox",
  "transforms.outbox.type": "io.debezium.transforms.outbox.EventRouter",
  "transforms.outbox.table.field.event.id": "id",
  "transforms.outbox.table.field.event.key": "aggregate_id",
  "transforms.outbox.table.field.event.type": "event_type",
  "transforms.outbox.table.field.event.payload": "payload"
}
```

**When to Use:** When you need transactional guarantees between database writes and event publication.

**When NOT to Use:** When the source database does not support transactions or when eventual consistency is acceptable without the complexity.

---

## 6. CDC with Debezium

**Problem:** Polling databases for changes is inefficient and introduces latency.

**Solution:** Use Debezium to capture row-level changes directly from the database WAL/binlog and stream them to Kafka.

**Implementation:**
```json
{
  "name": "order-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "database.hostname": "postgres",
    "database.port": "5432",
    "database.dbname": "orders",
    "table.include.list": "public.orders",
    "topic.prefix": "cdc",
    "plugin.name": "pgoutput",
    "slot.name": "debezium_slot"
  }
}
```

**When to Use:** When you need real-time data synchronization between systems or audit logging of all changes.

**When NOT to Use:** When the database does not have a WAL or when the volume of changes exceeds Kafka throughput.

---

## 7. Partition Strategies

**Problem:** Poor partition key choice causes hot partitions and unbalanced consumer load.

**Solution:** Choose keys that distribute load evenly while preserving ordering for related records.

**Implementation:**
```java
// Good: orders keyed by order_id for per-order ordering
producer.send(new ProducerRecord<>("orders", orderId, payload));

// Bad: all messages go to one partition
producer.send(new ProducerRecord<>("orders", "static-key", payload));

// Custom partitioner for composite keys
public int partition(String topic, Object key, ...) {
    String composite = ((Order) key).getRegion() + "-" + ((Order) key).getUserId();
    return Math.abs(composite.hashCode()) % numPartitions;
}
```

**When to Use:** When partition count is fixed and you need to minimize rebalancing while maximizing throughput.

**When NOT to Use:** When using compacted topics with key deletions or when partition count changes frequently.

---

## 8. Schema Evolution with Avro/Schema Registry

**Problem:** Producer and consumer schemas drift, causing deserialization failures.

**Solution:** Use a schema registry with compatibility rules to enforce safe schema evolution.

**Implementation:**
```java
// Producer with Avro and schema registry
props.put("value.serializer", KafkaAvroSerializer.class);
props.put("schema.registry.url", "http://schema-registry:8081");
props.put("value.subject.name.strategy", RecordNameStrategy.class);

// Schema evolution: add field with default
// v1: {"name": "string"}
// v2: {"name": "string", "email": "string", "default": ""}
```

**When to Use:** When multiple consumers evolve independently or when schema changes must be backward-compatible.

**When NOT to Use:** When using simple string payloads or when all producers and consumers are deployed together atomically.

---

## Best Practices

- Enable idempotent producer on all topics by default.
- Set `min.insync.replicas=2` and `acks=all` for durability.
- Use consumer group rebalance callbacks to handle partition reassignment gracefully.
- Monitor consumer lag with `kafka-consumer-groups` or JMX metrics.
- Keep schemas in the registry with BACKWARD or FULL compatibility mode.
- Use separate topics for retry and dead-letter routing.
- Tune `max.poll.records` to control processing throughput per consumer.
