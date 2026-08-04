# Apache Flume: Distributed Data Collection and Aggregation

## Table of Contents
1. [Introduction](#introduction)
2. [Architecture](#architecture)
3. [Channel Types](#channel-types)
4. [Interceptors](#interceptors)
5. [Selectors](#selectors)
6. [Sinks](#sinks)
7. [Reliability](#reliability)
8. [Monitoring](#monitoring)
9. [Advanced Configurations](#advanced-configurations)
10. [Best Practices](#best-practices)
11. [Key Takeaways](#key-takeaways)

---

## Introduction

Apache Flume is a distributed, reliable, and available service for efficiently collecting, aggregating, and moving large amounts of log data. It has a simple and flexible architecture based on streaming data flows.

### Core Features

- **Distributed**: Horizontal scaling across multiple machines
- **Reliable**: Guarantees delivery with transactional guarantees
- **Scalable**: Handle millions of events per second
- **Customizable**: Extensible architecture with plugins
- **Fault Tolerant**: Automatic failover and recovery

### Use Cases

- Log aggregation from multiple servers
- Event streaming from applications
- Data ingestion into Hadoop/HDFS
- Real-time data collection
- IoT data aggregation
- Security event collection

### Installation

```bash
# Download and extract
wget https://dlcdn.apache.org/flume/1.11.0/apache-flume-1.11.0-bin.tar.gz
tar -xzf apache-flume-1.11.0-bin.tar.gz
cd apache-flume-1.11.0-bin

# Configure environment
export FLUME_HOME=/opt/flume
export PATH=$PATH:$FLUME_HOME/bin

# Verify installation
flume-ng version
```

---

## Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Data Sources                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  Web     │  │  App     │  │  System  │             │
│  │  Logs    │  │  Logs    │  │  Logs    │             │
│  └──────────┘  └──────────┘  └──────────┘             │
└─────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│                    Flume Agent                           │
│  ┌──────────────────────────────────────────────────┐  │
│  │                    Source                         │  │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐      │  │
│  │  │  Avro    │  │  Exec    │  │  Spool   │      │  │
│  │  │  Source  │  │  Source  │  │  Source  │      │  │
│  │  └──────────┘  └──────────┘  └──────────┘      │  │
│  └──────────────────────────────────────────────────┘  │
│                           │                            │
│  ┌──────────────────────────────────────────────────┐  │
│  │                   Channel                         │  │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐      │  │
│  │  │  Memory  │  │  File    │  │  Kafka   │      │  │
│  │  │  Channel │  │  Channel │  │  Channel │      │  │
│  │  └──────────┘  └──────────┘  └──────────┘      │  │
│  └──────────────────────────────────────────────────┘  │
│                           │                            │
│  ┌──────────────────────────────────────────────────┐  │
│  │                    Sink                           │  │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐      │  │
│  │  │  HDFS    │  │  HBase   │  │  Avro    │      │  │
│  │  │  Sink    │  │  Sink    │  │  Sink    │      │  │
│  │  └──────────┘  └──────────┘  └──────────┘      │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│                 Destination Systems                      │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  HDFS    │  │  HBase   │  │  Kafka   │             │
│  │  Cluster │  │  Cluster │  │  Cluster │             │
│  └──────────┘  └──────────┘  └──────────┘             │
└─────────────────────────────────────────────────────────┘
```

### Key Components

| Component | Description |
|-----------|-------------|
| **Agent** | JVM process that hosts Source, Channel, and Sink |
| **Source** | Component that receives or polls data from external sources |
| **Channel** | Buffer that stores events between Source and Sink |
| **Sink** | Component that delivers events to external destinations |
| **Event** | Basic unit of data transfer in Flume |
| **Interceptor** | Modifies or filters events in the pipeline |
| **Selector** | Routes events to multiple channels based on rules |

### Data Flow

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Source    │───▶│   Channel   │───▶│    Sink     │
│             │    │             │    │             │
│  (Receive)  │    │  (Buffer)   │    │  (Deliver)  │
└─────────────┘    └─────────────┘    └─────────────┘
       │                 │                   │
       ▼                 ▼                   ▼
  ┌─────────┐      ┌─────────┐        ┌─────────┐
  │External │      │Memory/  │        │External │
  │ Source  │      │File     │        │   Dest  │
  └─────────┘      └─────────┘        └─────────┘
```

---

## Channel Types

### Memory Channel

```properties
# Memory Channel Configuration
agent.sources = avroSource
agent.channels = memoryChannel
agent.sinks = hdfsSink

# Source configuration
agent.sources.avroSource.type = avro
agent.sources.avroSource.bind = 0.0.0.0
agent.sources.avroSource.port = 4545
agent.sources.avroSource.channels = memoryChannel

# Memory Channel configuration
agent.channels.memoryChannel.type = memory
agent.channels.memoryChannel.capacity = 10000
agent.channels.memoryChannel.transactionCapacity = 1000
agent.channels.memoryChannel.byteCapacityBufferPercentage = 20
agent.channels.memoryChannel.byteCapacity = 800000000

# Sink configuration
agent.sinks.hdfsSink.type = hdfs
agent.sinks.hdfsSink.hdfs.path = hdfs://namenode:8020/user/flume/events
agent.sinks.hdfsSink.hdfs.fileType = DataStream
agent.sinks.hdfsSink.channel = memoryChannel
```

### File Channel

```properties
# File Channel Configuration
agent.sources = avroSource
agent.channels = fileChannel
agent.sinks = hdfsSink

# File Channel configuration
agent.channels.fileChannel.type = file
agent.channels.fileChannel.checkpointDir = /var/flume/checkpoint
agent.channels.fileChannel.dataDirs = /var/flume/data1,/var/flume/data2
agent.channels.fileChannel.capacity = 1000000
agent.channels.fileChannel.transactionCapacity = 10000
agent.channels.fileChannel.maxFileSize = 2146435071
agent.channels.fileChannel.keepAlive = true
agent.channels.fileChannel.writeTryInterval = 10000
agent.channels.fileChannel.seekSpeed = 20000000
```

### Kafka Channel

```properties
# Kafka Channel Configuration
agent.sources = avroSource
agent.channels = kafkaChannel
agent.sinks = hdfsSink

# Kafka Channel configuration
agent.channels.kafkaChannel.type = org.apache.flume.channel.kafka.KafkaChannel
agent.channels.kafkaChannel.kafka.bootstrap.servers = broker1:9092,broker2:9092
agent.channels.kafkaChannel.kafka.topic = flume-events
agent.channels.kafkaChannel.kafka.consumer.group.id = flume-consumer
agent.channels.kafkaChannel.parseAsFlumeEvent = true
agent.channels.kafkaChannel.migrateZookeeperOffsets = false
agent.channels.kafkaChannel.kafka.consumer.auto.offset.reset = earliest
```

### Spillable Memory Channel

```properties
# Spillable Memory Channel Configuration
agent.sources = avroSource
agent.channels = spillableChannel
agent.sinks = hdfsSink

# Spillable Memory Channel configuration
agent.channels.spillableChannel.type = SPILLABLEMEMORY
agent.channels.spillableChannel.memoryCapacity = 10000
agent.channels.spillableChannel.overflowCapacity = 1000000
agent.channels.spillableChannel.byteCapacityBufferPercentage = 20
agent.channels.spillableChannel.byteCapacity = 800000000
agent.channels.spillableChannel.overflowCheckpointDir = /var/flume/overflow
agent.channels.spillableChannel.checkpointDir = /var/flume/checkpoint
```

### Pseudo Transaction Channel

```properties
# Pseudo Transaction Channel (for testing)
agent.sources = avroSource
agent.channels = pseudoChannel
agent.sinks = hdfsSink

agent.channels.pseudoChannel.type = org.apache.flume.channel.PseudoTransactionalChannel
agent.channels.pseudoChannel.capacity = 100
agent.channels.pseudoChannel.transactionCapacity = 10
```

---

## Interceptors

### Timestamp Interceptor

```properties
# Add timestamp to events
agent.sources.avroSource.interceptors = timestampInterceptor
agent.sources.avroSource.interceptors.timestampInterceptor.type = org.apache.flume.interceptor.TimestampInterceptor$Builder
agent.sources.avroSource.interceptors.timestampInterceptor.preserveExisting = true
agent.sources.avroSource.interceptors.timestampInterceptor.headerName = timestamp
```

### Host Interceptor

```properties
# Add hostname to events
agent.sources.avroSource.interceptors = hostInterceptor
agent.sources.avroSource.interceptors.hostInterceptor.type = org.apache.flume.interceptor.HostInterceptor$Builder
agent.sources.avroSource.interceptors.hostInterceptor.preserveExisting = true
agent.sources.avroSource.interceptors.hostInterceptor.hostHeader = hostname
agent.sources.avroSource.interceptors.hostInterceptor.useIP = true
```

### Static Interceptor

```properties
# Add static headers to events
agent.sources.avroSource.interceptors = staticInterceptor
agent.sources.avroSource.interceptors.staticInterceptor.type = org.apache.flume.interceptor.StaticInterceptor$Builder
agent.sources.avroSource.interceptors.staticInterceptor.preserveExisting = true
agent.sources.avroSource.interceptors.staticInterceptor.key = source
agent.sources.avroSource.interceptors.staticInterceptor.value = webserver01
```

### Regex Extractor Interceptor

```properties
# Extract fields using regex
agent.sources.avroSource.interceptors = regexInterceptor
agent.sources.avroSource.interceptors.regexInterceptor.type = org.apache.flume.interceptor.RegexExtractorInterceptor$Builder
agent.sources.avroSource.interceptors.regexInterceptor.regex = "^(\\d{4}-\\d{2}-\\d{2}) (\\w+) (\\d+\\.\\d+\\.\\d+\\.\\d+) (.*)"
agent.sources.avroSource.interceptors.regexInterceptor.serializers = timestampSerializer hostSerializer ipSerializer messageSerializer
agent.sources.avroSource.interceptors.regexInterceptor.serializers.timestampSerializer.name = timestamp
agent.sources.avroSource.interceptors.regexInterceptor.serializers.timestampSerializer.index = 1
agent.sources.avroSource.interceptors.regexInterceptor.serializers.hostSerializer.name = host
agent.sources.avroSource.interceptors.regexInterceptor.serializers.hostSerializer.index = 2
agent.sources.avroSource.interceptors.regexInterceptor.serializers.ipSerializer.name = ip
agent.sources.avroSource.interceptors.regexInterceptor.serializers.ipSerializer.index = 3
agent.sources.avroSource.interceptors.regexInterceptor.serializers.messageSerializer.name = message
agent.sources.avroSource.interceptors.regexInterceptor.serializers.messageSerializer.index = 4
```

### Regex Filtering Interceptor

```properties
# Filter events using regex
agent.sources.avroSource.interceptors = regexFilterInterceptor
agent.sources.avroSource.interceptors.regexFilterInterceptor.type = org.apache.flume.interceptor.RegexFilteringInterceptor$Builder
agent.sources.avroSource.interceptors.regexFilterInterceptor.regex = "ERROR|WARN"
agent.sources.avroSource.interceptors.regexFilterInterceptor.excludeEvents = false
```

### Search and Replace Interceptor

```properties
# Search and replace in events
agent.sources.avroSource.interceptors = searchReplaceInterceptor
agent.sources.avroSource.interceptors.searchReplaceInterceptor.type = org.apache.flume.interceptor.SearchAndReplaceInterceptor$Builder
agent.sources.avroSource.interceptors.searchReplaceInterceptor.searchPattern = "\\d{3}-\\d{2}-\\d{4}"
agent.sources.avroSource.interactors.searchReplaceInterceptor.replaceString = "***-**-****"
```

### Custom Interceptor

```java
// Custom Interceptor Implementation
public class CustomInterceptor implements Interceptor {
    private final String headerKey;
    private final String headerValue;

    public CustomInterceptor(String headerKey, String headerValue) {
        this.headerKey = headerKey;
        this.headerValue = headerValue;
    }

    @Override
    public void initialize() {
        // Initialization logic
    }

    @Override
    public Event intercept(Event event) {
        Map<String, String> headers = event.getHeaders();
        headers.put(headerKey, headerValue);
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        for (Event event : events) {
            intercept(event);
        }
        return events;
    }

    @Override
    public void close() {
        // Cleanup logic
    }

    public static class Builder implements Interceptor.Builder {
        private String headerKey;
        private String headerValue;

        @Override
        public Interceptor build() {
            return new CustomInterceptor(headerKey, headerValue);
        }

        @Override
        public void configure(Context context) {
            headerKey = context.getString("headerKey", "customKey");
            headerValue = context.getString("headerValue", "customValue");
        }
    }
}
```

---

## Selectors

### Replicating Selector

```properties
# Replicate events to multiple channels
agent.sources = avroSource
agent.channels = memoryChannel1 memoryChannel2
agent.sinks = hdfsSink1 hdfsSink2

# Configure replicating selector
agent.sources.avroSource.selector.type = replicating
agent.sources.avroSource.selector.optional = memoryChannel2

# Source to channels mapping
agent.sources.avroSource.channels = memoryChannel1 memoryChannel2

# Channel 1 to sink 1
agent.channels.memoryChannel1.sinks = hdfsSink1
agent.sinks.hdfsSink1.channel = memoryChannel1

# Channel 2 to sink 2
agent.channels.memoryChannel2.sinks = hdfsSink2
agent.sinks.hdfsSink2.channel = memoryChannel2
```

### Multiplexing Selector

```properties
# Route events based on header values
agent.sources = avroSource
agent.channels = errorChannel infoChannel debugChannel
agent.sinks = errorSink infoSink debugSink

# Configure multiplexing selector
agent.sources.avroSource.selector.type = multiplexing
agent.sources.avroSource.selector.header = level
agent.sources.avroSource.selector.mapping.ERROR = errorChannel
agent.sources.avroSource.selector.mapping.WARN = infoChannel
agent.sources.avroSource.selector.mapping.INFO = infoChannel
agent.sources.avroSource.selector.mapping.DEBUG = debugChannel
agent.sources.avroSource.selector.default = debugChannel

# Optional channels
agent.sources.avroSource.selector.optional = errorChannel

# Source to channels
agent.sources.avroSource.channels = errorChannel infoChannel debugChannel

# Channels to sinks
agent.channels.errorChannel.sinks = errorSink
agent.sinks.errorSink.channel = errorChannel

agent.channels.infoChannel.sinks = infoSink
agent.sinks.infoSink.channel = infoChannel

agent.channels.debugChannel.sinks = debugSink
agent.sinks.debugSink.channel = debugChannel
```

### Custom Selector

```java
// Custom Selector Implementation
public class CustomSelector implements ChannelSelector {
    private List<Channel> channels;
    private String headerKey;

    @Override
    public void initialize(List<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public Channel select(Event event) {
        Map<String, String> headers = event.getHeaders();
        String value = headers.get(headerKey);
        
        if ("priority".equals(value)) {
            return channels.get(0);  // High priority channel
        } else {
            return channels.get(1);  // Normal channel
        }
    }

    @Override
    public List<Channel> getRequiredChannels(Event event) {
        return null;
    }

    @Override
    public List<Channel> getOptionalChannels(Event event) {
        return null;
    }

    @Override
    public void configure(Context context) {
        headerKey = context.getString("headerKey", "type");
    }
}
```

---

## Sinks

### HDFS Sink

```properties
# HDFS Sink Configuration
agent.sources = avroSource
agent.channels = memoryChannel
agent.sinks = hdfsSink

# HDFS Sink configuration
agent.sinks.hdfsSink.type = hdfs
agent.sinks.hdfsSink.hdfs.path = hdfs://namenode:8020/user/flume/events/%Y-%m-%d/%H
agent.sinks.hdfsSink.hdfs.filePrefix = events
agent.sinks.hdfsSink.hdfs.fileSuffix = .log
agent.sinks.hdfsSink.hdfs.inUsePrefix = .
agent.sinks.hdfsSink.hdfs.inUseSuffix = .tmp
agent.sinks.hdfsSink.hdfs.rollInterval = 3600
agent.sinks.hdfsSink.hdfs.rollSize = 134217728
agent.sinks.hdfsSink.hdfs.rollCount = 0
agent.sinks.hdfsSink.hdfs.batchSize = 1000
agent.sinks.hdfsSink.hdfs.fileType = DataStream
agent.sinks.hdfsSink.hdfs.writeFormat = Text
agent.sinks.hdfsSink.hdfs.codec = snappy
agent.sinks.hdfsSink.hdfs.maxOpenFiles = 5000
agent.sinks.hdfsSink.hdfs.callTimeout = 60000
agent.sinks.hdfsSink.hdfs.batchSize = 1000
agent.sinks.hdfsSink.hdfs.threadsPool = 10
agent.sinks.hdfsSink.hdfs.kerberosPrincipal = flume/_HOST@REALM.COM
agent.sinks.hdfsSink.hdfs.kerberosKeytab = /etc/security/keytabs/flume.keytab
```

### HBase Sink

```properties
# HBase Sink Configuration
agent.sources = avroSource
agent.channels = memoryChannel
agent.sinks = hbaseSink

# HBase Sink configuration
agent.sinks.hbaseSink.type = hbase
agent.sinks.hbaseSink.table = events
agent.sinks.hbaseSink.columnFamily = cf
agent.sinks.hbaseSink.batchSize = 100
agent.sinks.hbaseSink.coalesceEvents = true
agent.sinks.hbaseSink.serializer = org.apache.flume.sink.hbase.StaticHBaseEventSerializer
agent.sinks.hbaseSink.serializer.prefix = event
agent.sinks.hbaseSink.serializer.suffix = log
agent.sinks.hbaseSink.channel = memoryChannel
```

### Kafka Sink

```properties
# Kafka Sink Configuration
agent.sources = avroSource
agent.channels = memoryChannel
agent.sinks = kafkaSink

# Kafka Sink configuration
agent.sinks.kafkaSink.type = org.apache.flume.sink.kafka.KafkaSink
agent.sinks.kafkaSink.kafka.bootstrap.servers = broker1:9092,broker2:9092
agent.sinks.kafkaSink.kafka.topic = flume-events
agent.sinks.kafkaSink.kafka.producer.acks = 1
agent.sinks.kafkaSink.kafka.producer.linger.ms = 1
agent.sinks.kafkaSink.kafka.producer.compression.type = snappy
agent.sinks.kafkaSink.batchSize = 100
agent.sinks.kafkaSink.channel = memoryChannel
```

### Avro Sink

```properties
# Avro Sink Configuration
agent.sources = avroSource
agent.channels = memoryChannel
agent.sinks = avroSink

# Avro Sink configuration
agent.sinks.avroSink.type = avro
agent.sinks.avroSink.hostname = downstream-agent
agent.sinks.avroSink.port = 4545
agent.sinks.avroSink.batch-size = 100
agent.sinks.avroSink.async = true
agent.sinks.avroSink.compression-type = none
agent.sinks.avroSink.ssl = false
agent.sinks.avroSink.truststore = /path/to/truststore.jks
agent.sinks.avroSink.truststorePassword = password
agent.sinks.avroSink.channel = memoryChannel
```

### Logger Sink

```properties
# Logger Sink (for debugging)
agent.sources = avroSource
agent.channels = memoryChannel
agent.sinks = loggerSink

# Logger Sink configuration
agent.sinks.loggerSink.type = logger
agent.sinks.loggerSink.maxBytesToLog = 16
agent.sinks.loggerSink.channel = memoryChannel
```

### File Roll Sink

```properties
# File Roll Sink
agent.sources = avroSource
agent.channels = memoryChannel
agent.sinks = fileSink

# File Roll Sink configuration
agent.sinks.fileSink.type = file_roll
agent.sinks.fileSink.sink.directory = /var/log/flume
agent.sinks.fileSink.sink.filePrefix = events
agent.sinks.fileSink.sink.fileSuffix = .log
agent.sinks.fileSink.sink.rollInterval = 3600
agent.sinks.fileSink.sink.batchSize = 100
agent.sinks.fileSink.sink.Serializer = text
agent.sinks.fileSink.channel = memoryChannel
```

### HDFS Sink with Bucketing

```properties
# HDFS Sink with dynamic bucketing
agent.sources = avroSource
agent.channels = memoryChannel
agent.sinks = hdfsSink

# Dynamic bucketing using interceptors
agent.sources.avroSource.interceptors = hostInterceptor timestampInterceptor
agent.sources.avroSource.interceptors.hostInterceptor.type = org.apache.flume.interceptor.HostInterceptor$Builder
agent.sources.avroSource.interceptors.hostInterceptor.hostHeader = hostname
agent.sources.avroSource.interceptors.timestampInterceptor.type = org.apache.flume.interceptor.TimestampInterceptor$Builder

# HDFS Sink with bucketing
agent.sinks.hdfsSink.type = hdfs
agent.sinks.hdfsSink.hdfs.path = hdfs://namenode:8020/user/flume/events/%Y-%m-%d/%H/%hostname
agent.sinks.hdfsSink.hdfs.filePrefix = events
agent.sinks.hdfsSink.hdfs.rollInterval = 3600
agent.sinks.hdfsSink.hdfs.rollSize = 134217728
agent.sinks.hdfsSink.hdfs.rollCount = 0
agent.sinks.hdfsSink.hdfs.fileType = DataStream
agent.sinks.hdfsSink.channel = memoryChannel
```

---

## Reliability

### Transactional Guarantees

```properties
# Reliable channel configuration
agent.channels.memoryChannel.type = memory
agent.channels.memoryChannel.capacity = 10000
agent.channels.memoryChannel.transactionCapacity = 1000
agent.channels.memoryChannel.keepAlive = true

# Reliable source configuration
agent.sources.avroSource.type = avro
agent.sources.avroSource.bind = 0.0.0.0
agent.sources.avroSource.port = 4545
agent.sources.avroSource.threads = 8

# Reliable sink configuration
agent.sinks.hdfsSink.type = hdfs
agent.sinks.hdfsSink.hdfs.path = hdfs://namenode:8020/user/flume/events
agent.sinks.hdfsSink.batchSize = 1000
agent.sinks.hdfsSink.batchSize = 1000
agent.sinks.hdfsSink.fileType = DataStream
```

### Failover Configuration

```properties
# Failover Sink Processor
agent.sinks = sink1 sink2
agent.sinkgroups = g1
agent.sinkgroups.g1.sinks = sink1 sink2
agent.sinkgroups.g1.processor.type = failover
agent.sinkgroups.g1.processor.priority.sink1 = 10
agent.sinkgroups.g1.processor.priority.sink2 = 5
agent.sinkgroups.g1.processor.maxpenalty = 10000

# Sink 1 configuration
agent.sinks.sink1.type = hdfs
agent.sinks.sink1.hdfs.path = hdfs://namenode:8020/user/flume/events1
agent.sinks.sink1.channel = memoryChannel

# Sink 2 configuration (backup)
agent.sinks.sink2.type = hdfs
agent.sinks.sink2.hdfs.path = hdfs://namenode:8020/user/flume/events2
agent.sinks.sink2.channel = memoryChannel
```

### Load Balancing Configuration

```properties
# Load Balancing Sink Processor
agent.sinks = sink1 sink2
agent.sinkgroups = g1
agent.sinkgroups.g1.sinks = sink1 sink2
agent.sinkgroups.g1.processor.type = load_balance
agent.sinkgroups.g1.processor.selector = round_robin
agent.sinkgroups.g1.processor.maxTimeAfterFailure = 30000

# Sink 1 configuration
agent.sinks.sink1.type = hdfs
agent.sinks.sink1.hdfs.path = hdfs://namenode:8020/user/flume/events1
agent.sinks.sink1.channel = memoryChannel

# Sink 2 configuration
agent.sinks.sink2.type = hdfs
agent.sinks.sink2.hdfs.path = hdfs://namenode:8020/user/flume/events2
agent.sinks.sink2.channel = memoryChannel
```

### Channel Capacity Management

```properties
# Memory Channel with capacity limits
agent.channels.memoryChannel.type = memory
agent.channels.memoryChannel.capacity = 10000
agent.channels.memoryChannel.transactionCapacity = 1000
agent.channels.memoryChannel.byteCapacityBufferPercentage = 20
agent.channels.memoryChannel.byteCapacity = 800000000

# File Channel with capacity limits
agent.channels.fileChannel.type = file
agent.channels.fileChannel.capacity = 1000000
agent.channels.fileChannel.transactionCapacity = 10000
agent.channels.fileChannel.maxFileSize = 2146435071
agent.channels.fileChannel.minSpaceRequirement = 1073741824
```

### Event Delivery Guarantees

```properties
# At-most-once delivery (default)
agent.sources.avroSource.channels = memoryChannel
agent.sinks.hdfsSink.channel = memoryChannel
agent.sinks.hdfsSink.batchSize = 100

# At-least-once delivery (use transactional channels)
agent.sources.avroSource.channels = fileChannel
agent.sinks.hdfsSink.channel = fileChannel
agent.sinks.hdfsSink.batchSize = 100

# Exactly-once delivery (requires application-level deduplication)
# Flume doesn't provide exactly-once guarantees natively
# Implement deduplication in the sink or downstream system
```

---

## Monitoring

### JMX Monitoring

```properties
# Enable JMX monitoring
export FLUME_JMX_OPTS="-Dcom.sun.management.jmxremote \
  -Dcom.sun.management.jmxremote.port=9999 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false"

# Start Flume with JMX
flume-ng agent --conf-file agent.conf --name agent -Dflume.root.logger=INFO,console
```

### Metrics Collection

```properties
# Enable metrics reporting
export FLUME_ROOT_LOGGER=INFO,console
export FLUME_AGENT_LOG_DIR=/var/log/flume

# Configure metrics sink
agent.sinks.metricSink.type = org.apache.flume.sink.monitor.MonitorSink
agent.sinks.metricSink.host = graphite-server
agent.sinks.metricSink.port = 2003
agent.sinks.metricSink.period = 60
agent.sinks.metricSink.channel = memoryChannel
```

### Health Checks

```bash
#!/bin/bash
# health_check.sh

# Check if Flume agent is running
if pgrep -f "flume-ng agent" > /dev/null; then
    echo "Flume agent is running"
else
    echo "Flume agent is not running"
    # Restart Flume
    flume-ng agent --conf-file /etc/flume/agent.conf --name agent &
fi

# Check port availability
if netstat -tuln | grep -q ":4545"; then
    echo "Avro port is listening"
else
    echo "Avro port is not listening"
fi

# Check HDFS connectivity
hdfs dfs -ls /user/flume/events > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "HDFS is accessible"
else
    echo "HDFS is not accessible"
fi
```

### Log Monitoring

```bash
# Monitor Flume logs
tail -f /var/log/flume/flume.log

# Search for errors
grep -i "error" /var/log/flume/flume.log

# Search for warnings
grep -i "warning" /var/log/flume/flume.log

# Monitor channel capacity
grep -i "channel" /var/log/flume/flume.log
```

### Performance Metrics

```properties
# Enable performance metrics
agent.sources.avroSource.type = avro
agent.sources.avroSource.bind = 0.0.0.0
agent.sources.avroSource.port = 4545
agent.sources.avroSource.selector.type = replicating

# Monitor source metrics
agent.sources.avroSource.selector.type = replicating
agent.sources.avroSource.selector.optional = memoryChannel

# Monitor channel metrics
agent.channels.memoryChannel.type = memory
agent.channels.memoryChannel.capacity = 10000
agent.channels.memoryChannel.transactionCapacity = 1000

# Monitor sink metrics
agent.sinks.hdfsSink.type = hdfs
agent.sinks.hdfsSink.hdfs.path = hdfs://namenode:8020/user/flume/events
agent.sinks.hdfsSink.batchSize = 1000
```

---

## Advanced Configurations

### Multi-Agent Pipeline

```properties
# Agent 1: Source Agent
agent1.sources = avroSource
agent1.channels = memoryChannel
agent1.sinks = avroSink

agent1.sources.avroSource.type = avro
agent1.sources.avroSource.bind = 0.0.0.0
agent1.sources.avroSource.port = 4545

agent1.channels.memoryChannel.type = memory
agent1.channels.memoryChannel.capacity = 10000

agent1.sinks.avroSink.type = avro
agent1.sinks.avroSink.hostname = agent2
agent1.sinks.avroSink.port = 4545

# Agent 2: Aggregator Agent
agent2.sources = avroSource
agent2.channels = memoryChannel
agent2.sinks = hdfsSink

agent2.sources.avroSource.type = avro
agent2.sources.avroSource.bind = 0.0.0.0
agent2.sources.avroSource.port = 4545

agent2.channels.memoryChannel.type = memory
agent2.channels.memoryChannel.capacity = 10000

agent2.sinks.hdfsSink.type = hdfs
agent2.sinks.hdfsSink.hdfs.path = hdfs://namenode:8020/user/flume/events
```

### Channel Interceptors

```properties
# Channel interceptors for data transformation
agent.sources.avroSource.interceptors = timestampInterceptor hostInterceptor regexInterceptor

agent.sources.avroSource.interceptors.timestampInterceptor.type = org.apache.flume.interceptor.TimestampInterceptor$Builder
agent.sources.avroSource.interceptors.hostInterceptor.type = org.apache.flume.interceptor.HostInterceptor$Builder
agent.sources.avroSource.interceptors.regexInterceptor.type = org.apache.flume.interceptor.RegexExtractorInterceptor$Builder
agent.sources.avroSource.interceptors.regexInterceptor.regex = "^(\\d{4}-\\d{2}-\\d{2}) (\\w+) (\\d+\\.\\d+\\.\\d+\\.\\d+) (.*)"
```

### Custom Source

```java
// Custom Source Implementation
public class CustomSource extends AbstractSource implements Configurable, EventDrivenSource {
    private String host;
    private int port;
    private ChannelProcessor channelProcessor;

    @Override
    public void configure(Context context) {
        host = context.getString("host", "localhost");
        port = context.getInteger("port", 4545);
    }

    @Override
    public void start() {
        // Start source
        channelProcessor = getChannelProcessor();
    }

    @Override
    public void stop() {
        // Stop source
    }

    @Override
    public void start() throws InterruptedException {
        // Event processing logic
        Event event = EventBuilder.withBody("test data".getBytes());
        channelProcessor.processEvent(event);
    }
}
```

### Custom Sink

```java
// Custom Sink Implementation
public class CustomSink extends AbstractSink implements Configurable {
    private String host;
    private int port;

    @Override
    public void configure(Context context) {
        host = context.getString("host", "localhost");
        port = context.getInteger("port", 4545);
    }

    @Override
    public Status process() throws EventDeliveryException {
        Status status = Status.READY;
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();

        try {
            transaction.begin();
            Event event = channel.take();

            if (event != null) {
                // Process event
                byte[] body = event.getBody();
                Map<String, String> headers = event.getHeaders();

                // Send to destination
                sendEvent(body, headers);
            }

            transaction.commit();
            status = Status.READY;
        } catch (Exception e) {
            transaction.rollback();
            status = Status.BACKOFF;
            throw new EventDeliveryException("Failed to deliver event", e);
        } finally {
            transaction.close();
        }

        return status;
    }

    private void sendEvent(byte[] body, Map<String, String> headers) {
        // Custom send logic
    }
}
```

---

## Best Practices

### 1. Channel Selection

```properties
# Good: Use File Channel for reliability
agent.channels.fileChannel.type = file
agent.channels.fileChannel.capacity = 1000000
agent.channels.fileChannel.transactionCapacity = 10000

# Bad: Using Memory Channel for critical data
agent.channels.memoryChannel.type = memory  # Risk of data loss
```

### 2. Batch Size Optimization

```properties
# Good: Optimize batch size for throughput
agent.sinks.hdfsSink.batchSize = 1000
agent.sources.avroSource.batchSize = 1000

# Bad: Too small batch size (high overhead)
agent.sinks.hdfsSink.batchSize = 10  # Too small
```

### 3. File Management

```properties
# Good: Proper file rolling configuration
agent.sinks.hdfsSink.hdfs.rollInterval = 3600
agent.sinks.hdfsSink.hdfs.rollSize = 134217728
agent.sinks.hdfsSink.hdfs.rollCount = 0
agent.sinks.hdfsSink.hdfs.fileType = DataStream

# Bad: No file rolling (huge files)
agent.sinks.hdfsSink.hdfs.rollInterval = 0
agent.sinks.hdfsSink.hdfs.rollSize = 0
```

### 4. Compression

```properties
# Good: Use compression for large data
agent.sinks.hdfsSink.hdfs.codec = snappy
agent.sinks.hdfsSink.hdfs.fileType = CompressedStream

# Good: Use appropriate compression
agent.sinks.hdfsSink.hdfs.codec = org.apache.hadoop.io.compress.GzipCodec
```

### 5. Error Handling

```properties
# Good: Configure failover
agent.sinkgroups = g1
agent.sinkgroups.g1.sinks = sink1 sink2
agent.sinkgroups.g1.processor.type = failover
agent.sinkgroups.g1.processor.priority.sink1 = 10
agent.sinkgroups.g1.processor.priority.sink2 = 5

# Good: Configure load balancing
agent.sinkgroups = g1
agent.sinkgroups.g1.sinks = sink1 sink2
agent.sinkgroups.g1.processor.type = load_balance
agent.sinkgroups.g1.processor.selector = round_robin
```

### 6. Security

```properties
# Good: Enable SSL for Avro
agent.sources.avroSource.ssl = true
agent.sources.avroSource.keystore = /path/to/keystore.jks
agent.sources.avroSource.keystorePassword = password
agent.sources.avroSource.truststore = /path/to/truststore.jks
agent.sources.avroSource.truststorePassword = password

# Good: Use Kerberos authentication
agent.sinks.hdfsSink.hdfs.kerberosPrincipal = flume/_HOST@REALM.COM
agent.sinks.hdfsSink.hdfs.kerberosKeytab = /etc/security/keytabs/flume.keytab
```

### 7. Monitoring

```properties
# Good: Enable metrics
agent.sinks.metricSink.type = org.apache.flume.sink.monitor.MonitorSink
agent.sinks.metricSink.host = graphite-server
agent.sinks.metricSink.port = 2003
agent.sinks.metricSink.period = 60

# Good: Configure logging
export FLUME_ROOT_LOGGER=INFO,LOGFILE
export FLUME_LOG_FILE=/var/log/flume/flume.log
export FLUME_LOG_MAXSIZE=100000000
export FLUME_LOG_COUNT=10
```

---

## Key Takeaways

### 1. **Agent-Centric Architecture**
Flume agents are the building blocks, each containing Source, Channel, and Sink components.

### 2. **Channel-Based Buffering**
Channels provide transactional guarantees and buffer data between Source and Sink.

### 3. **Extensible Plugin System**
Custom Sources, Interceptors, Selectors, and Sinks can be developed for specific requirements.

### 4. **Reliability Guarantees**
Transaction-based processing ensures data is not lost during transfer.

### 5. **Horizontal Scaling**
Multiple agents can be chained or clustered for high-throughput data collection.

### 6. **Flexible Routing**
Interceptors and Selectors enable complex data routing and transformation.

### 7. **Multiple Sink Support**
Data can be delivered to HDFS, HBase, Kafka, and other systems simultaneously.

### 8. **Monitoring and Management**
Built-in JMX metrics and management interfaces enable operational visibility.

### 9. **High Availability**
Failover and load balancing configurations ensure continuous data flow.

### 10. **Production Ready**
Flume is battle-tested for large-scale data collection in production environments.

---

## References

- [Apache Flume Documentation](https://flume.apache.org/documentation.html)
- [Flume User Guide](https://flume.apache.org/FlumeUserGuide.html)
- [Flume Developer Guide](https://flume.apache.org/FlumeDeveloperGuide.html)
- [Flume Configuration](https://flume.apache.org/FlumeUserGuide.html#flume-configuration)
- [Flume Interceptors](https://flume.apache.org/FlumeUserGuide.html#flume-interceptors)
