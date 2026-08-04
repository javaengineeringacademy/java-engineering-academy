# Schema Registry

## Schema Registry, Avro, Protocol Buffers, and Schema Evolution

---

## Table of Contents

- [Overview](#overview)
- [Schema Registry Architecture](#schema-registry-architecture)
- [Schema Types](#schema-types)
- [Schema Evolution](#schema-evolution)
- [Compatibility Modes](#compatibility-modes)
- [Schema Operations](#schema-operations)
- [Integration with Kafka](#integration-with-kafka)
- [Best Practices](#best-practices)

---

## Overview

Schema Registry provides a centralized repository for managing and validating schemas for Kafka messages. It ensures data compatibility between producers and consumers.

### Key Features

- **Centralized Schema Management**: Store and version schemas
- **Compatibility Enforcement**: Prevent breaking changes
- **Schema Evolution**: Safe schema changes over time
- **Multiple Formats**: Support for Avro, Protobuf, JSON Schema
- **REST API**: Easy integration with any language

### Why Schema Registry?

```
Without Schema Registry:
┌──────────┐     ┌──────────┐     ┌──────────┐
│ Producer │     │  Kafka   │     │ Consumer │
│ (v1)     │────▶│          │────▶│ (v2)     │
└──────────┘     └──────────┘     └──────────┘
      │                                │
      └──── Schema mismatch! ──────────┘

With Schema Registry:
┌──────────┐     ┌──────────────────┐     ┌──────────┐
│ Producer │────▶│ Schema Registry  │◀────│ Consumer │
│ (v1)     │     │ (validates)      │     │ (v2)     │
└──────────┘     └──────────────────┘     └──────────┘
      │                                        │
      └──── Compatible schemas! ──────────────┘
```

---

## Schema Registry Architecture

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    Schema Registry Cluster                    │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   SR Node 1  │  │   SR Node 2  │  │   SR Node 3  │      │
│  │   (Leader)   │  │  (Follower)  │  │  (Follower)  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
         │                    │                    │
         ▼                    ▼                    ▼
┌─────────────────────────────────────────────────────────────┐
│                    Kafka Cluster                             │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  _schemas topic                                       │   │
│  │  (stores all schemas)                                 │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Component Roles

| Component | Responsibility |
|-----------|---------------|
| Schema Registry | Validates and stores schemas |
| _schemas topic | Persists all schema versions |
| Schema ID | Unique identifier for each schema |
| Compatibility | Ensures schema evolution safety |

---

## Schema Types

### Avro Schema

```json
{
  "type": "record",
  "name": "User",
  "namespace": "com.example",
  "fields": [
    {"name": "id", "type": "long"},
    {"name": "name", "type": "string"},
    {"name": "email", "type": ["null", "string"], "default": null},
    {"name": "created_at", "type": "long", "logicalType": "timestamp-millis"}
  ]
}
```

### Protobuf Schema

```protobuf
syntax = "proto3";

package com.example;

message User {
  int64 id = 1;
  string name = 2;
  optional string email = 3;
  int64 created_at = 4;
}
```

### JSON Schema

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "title": "User",
  "type": "object",
  "properties": {
    "id": {"type": "integer"},
    "name": {"type": "string"},
    "email": {"type": ["string", "null"]},
    "created_at": {"type": "integer"}
  },
  "required": ["id", "name"]
}
```

### Schema Comparison

| Feature | Avro | Protobuf | JSON Schema |
|---------|------|----------|-------------|
| Binary Format | Yes | Yes | No |
| Schema Evolution | Excellent | Good | Good |
| Default Values | Yes | Yes | Yes |
| Code Generation | Yes | Yes | No |
| Performance | High | High | Low |

---

## Schema Evolution

### Evolution Strategies

```
Version 1: User
{
  "type": "record",
  "name": "User",
  "fields": [
    {"name": "id", "type": "long"},
    {"name": "name", "type": "string"}
  ]
}

Version 2: Added email field
{
  "type": "record",
  "name": "User",
  "fields": [
    {"name": "id", "type": "long"},
    {"name": "name", "type": "string"},
    {"name": "email", "type": ["null", "string"], "default": null}
  ]
}

Version 3: Added phone field
{
  "type": "record",
  "name": "User",
  "fields": [
    {"name": "id", "type": "long"},
    {"name": "name", "type": "string"},
    {"name": "email", "type": ["null", "string"], "default": null},
    {"name": "phone", "type": ["null", "string"], "default": null}
  ]
}
```

### Backward Compatibility

```
Consumer with old schema can read new data:
New data (V2): {id: 1, name: "John", email: "john@example.com"}
Old consumer (V1): {id: 1, name: "John"}  ← email ignored
```

### Forward Compatibility

```
Consumer with new schema can read old data:
Old data (V1): {id: 1, name: "John"}
New consumer (V2): {id: 1, name: "John", email: null}  ← default used
```

### Full Compatibility

```
Both backward and forward compatible:
- Old consumers can read new data
- New consumers can read old data
```

---

## Compatibility Modes

### Compatibility Modes

| Mode | Description |
|------|-------------|
| `BACKWARD` | New schema can read old data |
| `FORWARD` | Old schema can read new data |
| `FULL` | Both backward and forward compatible |
| `NONE` | No compatibility checks |

### Configuration

```bash
# Set compatibility mode
curl -X PUT http://localhost:8081/config/User-value \
  -H "Content-Type: application/json" \
  -d '{"compatibility": "FULL"}'
```

### Compatibility Rules

```
BACKWARD Compatible:
✓ Add optional field with default
✓ Remove optional field
✓ Change field type (if compatible)

NOT Backward Compatible:
✗ Add required field without default
✗ Remove required field
✗ Change field type (incompatible)

FORWARD Compatible:
✓ Add field (old readers ignore it)
✓ Remove optional field

NOT Forward Compatible:
✗ Remove required field
✗ Add required field without default
```

### Field Rules

| Change | BACKWARD | FORWARD | FULL |
|--------|----------|---------|------|
| Add optional field | ✓ | ✓ | ✓ |
| Add required field | ✗ | ✗ | ✗ |
| Remove optional field | ✓ | ✓ | ✓ |
| Remove required field | ✗ | ✗ | ✗ |
| Add default to optional | ✓ | ✓ | ✓ |

---

## Schema Operations

### Register Schema

```bash
# Register schema
curl -X POST http://localhost:8081/subjects/User-value/versions \
  -H "Content-Type: application/json" \
  -d '{
    "schema": "{\"type\":\"record\",\"name\":\"User\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"name\",\"type\":\"string\"}]}"
  }'
```

### Get Schema

```bash
# Get latest schema
curl http://localhost:8081/subjects/User-value/versions/latest

# Get specific version
curl http://localhost:8081/subjects/User-value/versions/1

# Get schema by ID
curl http://localhost:8081/schemas/ids/1
```

### List Schemas

```bash
# List all subjects
curl http://localhost:8081/subjects

# List all versions
curl http://localhost:8081/subjects/User-value/versions
```

### Delete Schema

```bash
# Delete specific version
curl -X DELETE http://localhost:8081/subjects/User-value/versions/1

# Delete subject (soft delete)
curl -X DELETE http://localhost:8081/subjects/User-value

# Delete subject (permanent)
curl -X DELETE http://localhost:8081/subjects/User-value?permanent=true
```

### Check Compatibility

```bash
# Check compatibility with latest
curl -X POST http://localhost:8081/compatibility/subjects/User-value/latest \
  -H "Content-Type: application/json" \
  -d '{
    "schema": "{\"type\":\"record\",\"name\":\"User\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"email\",\"type\":[\"null\",\"string\"],\"default\":null}]}"
  }'

# Check compatibility with specific version
curl -X POST http://localhost:8081/compatibility/subjects/User-value/versions/1 \
  -H "Content-Type: application/json" \
  -d '{
    "schema": "{\"type\":\"record\",\"name\":\"User\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"email\",\"type\":[\"null\",\"string\"],\"default\":null}]}"
  }'
```

---

## Integration with Kafka

### Avro Producer

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
props.put("schema.registry.url", "http://localhost:8081");

Producer<String, GenericRecord> producer = new KafkaProducer<>(props);

Schema schema = new Schema.Parser().parse(// avro schema);
GenericRecord user = new GenericData.Record(schema);
user.put("id", 1L);
user.put("name", "John");
user.put("email", "john@example.com");

ProducerRecord<String, GenericRecord> record = 
    new ProducerRecord<>("users", "user-1", user);

producer.send(record);
```

### Avro Consumer

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("group.id", "user-consumer");
props.put("key.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
props.put("schema.registry.url", "http://localhost:8081");
props.put("specific.avro.reader", true);

KafkaConsumer<String, User> consumer = new KafkaConsumer<>(props);
consumer.subscribe(Arrays.asList("users"));

while (true) {
    ConsumerRecords<String, User> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, User> record : records) {
        User user = record.value();
        System.out.println("User: " + user.getName());
    }
}
```

### JSON Schema Producer

```java
props.put("value.serializer", "io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer");
props.put("schema.registry.url", "http://localhost:8081");
```

### Protobuf Producer

```java
props.put("value.serializer", "io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer");
props.put("schema.registry.url", "http://localhost:8081");
```

---

## Best Practices

### Schema Design

1. **Use meaningful names** - Clear schema names
2. **Add documentation** - Describe each field
3. **Use appropriate types** - Match data requirements
4. **Plan for evolution** - Design with future changes in mind

### Compatibility

1. **Start with FULL compatibility** - Safest option
2. **Test compatibility** - Before registering
3. **Use versioned subjects** - Track schema versions
4. **Monitor compatibility** - Set up alerts

### Performance

1. **Cache schemas locally** - Reduce registry calls
2. **Use specific records** - Avoid GenericRecord overhead
3. **Batch schema registration** - Reduce HTTP calls
4. **Monitor registry performance** - Track latency

### Operations

1. **Backup schemas** - Regular exports
2. **Monitor registry health** - Check leader election
3. **Version control schemas** - Store in Git
4. **Document schema changes** - Maintain changelog

### Security

1. **Enable authentication** - Protect registry access
2. **Use HTTPS** - Encrypt connections
3. **Implement ACLs** - Control schema access
4. **Audit schema changes** - Track modifications

---

## Further Reading

- [Confluent Schema Registry Documentation](https://docs.confluent.io/platform/current/schema-registry/)
- [Avro Specification](https://avro.apache.org/docs/current/)
- [Protobuf Documentation](https://protobuf.dev/)
