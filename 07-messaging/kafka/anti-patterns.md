# Kafka Anti-Patterns

## 1. Too Many Partitions
**Description:** Creating topics with excessive partition counts.

**Why it's bad:** Increases metadata overhead, longer rebalancing, more file handles.

**Example (bad code):**
```bash
# Creating topic with too many partitions
kafka-topics.sh --create --topic orders \
    --partitions 1000 \
    --replication-factor 3
```

**Better approach:** Right-size partitions:
```bash
# Based on throughput needs
kafka-topics.sh --create --topic orders \
    --partitions 12 \
    --replication-factor 3
```

**Impact:** Better performance, easier management.

---

## 2. Consumer Group Lag
**Description:** Not monitoring or addressing consumer lag.

**Why it's bad:** Data processing delays, missed SLAs, potential data loss.

**Example (bad code):**
```bash
# No monitoring of consumer lag
# Consumers fall behind
```

**Better approach:** Monitor and alert:
```bash
# Check consumer lag
kafka-consumer-groups.sh --group my-group --describe

# Set up alerts for lag threshold
```

**Impact:** Proactive issue detection, SLA compliance.

---

## 3. Ignoring Offset Management
**Description:** Not handling offset commits properly.

**Why it's bad:** Data loss or duplicate processing on restart.

**Example (bad code):**
```java
// Auto-commit enabled
props.put("enable.auto.commit", "true");
// Processing may miss messages on failure
```

**Better approach:** Manual offset management:
```java
props.put("enable.auto.commit", "false");

consumer.subscribe(Arrays.asList("topic"));
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        processRecord(record);
    }
    consumer.commitSync();
}
```

**Impact:** Exactly-once processing, no data loss.

---

## 4. Single Consumer Thread
**Description:** Using single consumer thread for high-throughput topics.

**Why it's bad:** Cannot keep up with high message rates.

**Example (bad code):**
```java
// Single threaded consumer
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        processSlowly(record);
    }
}
```

**Better approach:** Use consumer with multiple threads:
```java
ExecutorService executor = Executors.newFixedThreadPool(10);

while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        executor.submit(() -> processSlowly(record));
    }
}
```

**Impact:** Better throughput, parallel processing.

---

## 5. Not Using Compression
**Description:** Not enabling message compression.

**Why it's bad:** Higher network usage, more storage, lower throughput.

**Example (bad code):**
```properties
# No compression configured
compression.type=none
```

**Better approach:** Enable compression:
```properties
# Producer
compression.type=lz4

# Broker
compression.type=producer
```

**Impact:** Reduced network usage, better throughput.

---

## 6. Producing to Non-Existent Topics
**Description:** Producing messages without ensuring topic exists.

**Why it's bad:** Can create topics with default settings, uncontrolled partition count.

**Example (bad code):**
```java
// Topic auto-created with default settings
producer.send(new ProducerRecord<>("new-topic", message));
```

**Better approach:** Create topics explicitly:
```bash
kafka-topics.sh --create --topic new-topic \
    --partitions 12 \
    --replication-factor 3 \
    --config retention.ms=604800000
```

**Impact:** Controlled topic configuration.

---

## 7. Ignoring Message Size Limits
**Description:** Sending messages that exceed broker limits.

**Why it's bad:** Messages rejected, producer errors.

**Example (bad code):**
```java
// Large message
byte[] largeData = new byte[20 * 1024 * 1024]; // 20MB
producer.send(new ProducerRecord<>("topic", largeData));
```

**Better approach:** Chunk large messages or increase limits:
```properties
# Broker
message.max.bytes=10485760

# Producer
max.request.size=10485760
```

**Impact:** Successful message delivery.

---

## 8. Not Using Idempotent Producers
**Description:** Not enabling idempotent delivery.

**Why it's bad:** Duplicate messages on retries.

**Example (bad code):**
```properties
# Default - not idempotent
enable.idempotence=false
```

**Better approach:** Enable idempotence:
```properties
enable.idempotence=true
acks=all
retries=2147483647
```

**Impact:** Exactly-once delivery semantics.

---

## 9. Ignoring Consumer Rebalancing
**Description:** Not handling consumer group rebalancing properly.

**Why it's bad:** Increased processing latency during rebalance.

**Example (bad code):**
```java
// Long processing times cause rebalances
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    processAllRecords(records); // Takes too long
}
```

**Better approach:** Use cooperative rebalancing:
```properties
partition.assignment.strategy=org.apache.kafka.clients.consumer.CooperativeStickyAssignor
```

**Impact:** Minimal downtime during rebalancing.

---

## 10. Not Using Dead Letter Queues
**Description:** Not handling poison messages properly.

**Why it's bad:** Bad messages block processing, cause repeated failures.

**Example (bad code):**
```java
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        try {
            processRecord(record);
        } catch (Exception e) {
            // Message retried forever
        }
    }
}
```

**Better approach:** Use dead letter queue:
```java
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        try {
            processRecord(record);
            consumer.commitSync();
        } catch (Exception e) {
            sendToDeadLetterQueue(record);
            consumer.commitSync();
        }
    }
}
```

**Impact:** Non-blocking processing, poison message handling.

---

## 11. Ignoring Broker Configuration
**Description:** Using default broker settings without tuning.

**Why it's bad:** Suboptimal performance, wasted resources.

**Example (bad code):**
```properties
# Default settings
num.network.threads=3
num.io.threads=8
```

**Better approach:** Tune for workload:
```properties
# High-throughput
num.network.threads=8
num.io.threads=16
log.flush.interval.messages=10000
```

**Impact:** Better performance, resource utilization.

---

## 12. Not Monitoring Under Replication
**Description:** Not monitoring under-replicated partitions.

**Why it's bad:** Data loss risk, reduced fault tolerance.

**Example (bad code):**
```bash
# No monitoring of replication status
# Broker failure causes data loss
```

**Better approach:** Monitor replication:
```bash
kafka-topics.sh --describe --under-replicated-partitions

# Set up alerts for under-replicated partitions
```

**Impact:** Data safety, fault tolerance.