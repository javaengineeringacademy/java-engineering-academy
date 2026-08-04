# Apache Avro Fundamentals

## Overview
Avro is a row-based data serialization format with rich data structures, compact binary format, and schema evolution.

## Schema Definition
```json
{
  "type": "record",
  "name": "User",
  "namespace": "com.example",
  "fields": [
    {"name": "id", "type": "long"},
    {"name": "name", "type": "string"},
    {"name": "email", "type": ["null", "string"], "default": null},
    {"name": "age", "type": "int", "default": 0},
    {"name": "created_at", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

## Schema Evolution
```json
// v1
{"fields": [{"name": "id", "type": "long"}, {"name": "name", "type": "string"}]}

// v2 (added field with default)
{"fields": [
  {"name": "id", "type": "long"},
  {"name": "name", "type": "string"},
  {"name": "email", "type": ["null", "string"], "default": null}
]}
```

## Writing Avro
```java
// Java
Schema schema = new Schema.Parser().parse(new File("user.avsc"));
GenericRecord record = new GenericData.Record(schema);
record.put("id", 1L);
record.put("name", "John");

DatumWriter<GenericRecord> writer = new GenericDatumWriter<>(schema);
DataFileWriter<GenericRecord> fileWriter = new DataFileWriter<>(writer);
fileWriter.create(schema, new File("users.avro"));
fileWriter.append(record);
fileWriter.close();
```

## Reading Avro
```java
DatumReader<GenericRecord> reader = new GenericDatumReader<>();
DataFileReader<GenericRecord> fileReader = new DataFileReader<>(
    new File("users.avro"), reader);
while (fileReader.hasNext()) {
    GenericRecord record = fileReader.next();
    System.out.println(record.get("name"));
}
fileReader.close();
```

## Best Practices
1. Use Avro for Kafka message serialization
2. Enable schema registry for evolution
3. Use string type for IDs when possible
4. Define logical types for dates/times
5. Keep schemas simple and backward-compatible
