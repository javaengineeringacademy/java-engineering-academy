# Apache NiFi

## Overview

Apache NiFi is a data flow management system for automating the flow of data between systems. It provides a web-based UI for designing, monitoring, and managing data flows with fine-grained data provenance, support for complex routing, and built-in support for common data formats and protocols.

## Table of Contents

- [Architecture](#architecture)
- [Core Concepts](#core-concepts)
- [Processors](#processors)
- [Flow Design](#flow-design)
- [Data Provenance](#data-provenance)
- [Security](#security)
- [Clustering](#clustering)
- [Best Practices](#best-practices)

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    NIFI ARCHITECTURE                          │
├─────────────────────────────────────────────────────────────┤
│  Web Server                                                 │
│  • Flow designer UI                                         │
│  • Monitoring dashboard                                     │
│  • Provenance queries                                       │
├─────────────────────────────────────────────────────────────┤
│  Flow Controller                                            │
│  • Manages processors and connections                       │
│  • Schedules task execution                                 │
│  • Manages thread pools                                     │
├─────────────────────────────────────────────────────────────┤
│  Repositories                                               │
│  • FlowFile Repository: FlowFile metadata                  │
│  • Content Repository: FlowFile content                     │
│  • Provenance Repository: Data lineage                      │
├─────────────────────────────────────────────────────────────┤
│  Processors                                                 │
│  • Ingest, transform, route, deliver                        │
│  • 300+ built-in processors                                 │
│  • Custom processors via API                                │
└─────────────────────────────────────────────────────────────┘
```

### Repository Types

| Repository | Purpose | Storage |
|------------|---------|---------|
| FlowFile | Metadata and state | Disk |
| Content | Actual data content | Disk |
| Provenance | Data lineage events | Disk/Index |

## Core Concepts

### FlowFile

A FlowFile is the core data object moving through NiFi:

```java
// FlowFile components
FlowFile {
    Attributes: {
        filename: "data.csv",
        path: "/incoming/",
        mime.type: "text/csv",
        uuid: "unique-identifier",
        custom.attribute: "value"
    },
    Content: {
        // Actual data bytes
    }
}
```

### Processor

A Processor is a component that:
- Receives FlowFiles from input queues
- Processes FlowFiles (transform, enrich, filter)
- Routes FlowFiles to output queues
- Modifies FlowFile attributes

### Connection

Connections are bounded queues between processors:
- **Back Pressure**: Prevents overwhelm
- **Flow File Prioritization**: Order processing
- **Load Balancing**: Distribute across processors

### Process Group

Process Groups organize processors into logical units:
- Encapsulate functionality
- Have input/output ports
- Support hierarchical design
- Enable reuse

## Processors

### Common Processors

#### Ingestion
- **GetFile**: Read from file system
- **GetHTTP**: Pull from HTTP endpoint
- **ConsumeKafka**: Read from Kafka
- **GetSFTP**: Pull from SFTP
- **ListenHTTP**: HTTP receiver

#### Transformation
- **ConvertRecord**: Convert between formats
- **JoltTransformJSON**: Transform JSON
- **UpdateAttribute**: Modify FlowFile attributes
- **SplitText**: Split text files
- **MergeContent**: Merge FlowFiles

#### Routing
- **RouteOnAttribute**: Route based on attributes
- **RouteOnContent**: Route based on content
- **DistributeLoad**: Load balancing

#### Output
- **PutFile**: Write to file system
- **PutS3Object**: Write to S3
- **PublishKafka**: Write to Kafka
- **PutSQL**: Write to database

### Processor Configuration

```java
// Processor properties
Properties:
  - input.directory: /incoming
  - file.name.pattern: *.csv
  - batch.size: 100
  - schedule.cron: 0 0 2 * * ?

Scheduling:
  - Timer driven: Fixed interval
  - Cron driven: Cron expression
  - Event driven: On FlowFile arrival

Connections:
  - Input ports: Receive FlowFiles
  - Output ports: Send FlowFiles
  - Relationships: Route FlowFiles

Bulletin level:
  - INFO, WARNING, ERROR
```

## Flow Design

### Simple Ingestion Flow

```
┌─────────┐    ┌──────────────┐    ┌─────────┐
│ GetFile │───→│ ConvertCSV   │───→│ PutFile │
└─────────┘    │ ToJSON       │    └─────────┘
               └──────────────┘
```

### Complex Processing Flow

```
┌─────────┐    ┌──────────────┐    ┌──────────────┐
│ConsumeK │───→│ ParseJSON    │───→│ RouteOnAttr  │
│afka     │    │              │    │              │
└─────────┘    └──────────────┘    └──────┬───────┘
                                          │
                    ┌─────────────────────┼─────────────────────┐
                    │                     │                     │
                    ▼                     ▼                     ▼
             ┌────────────┐       ┌────────────┐       ┌────────────┐
             │ EnrichData │       │ Validate   │       │ LogRaw     │
             └─────┬──────┘       └─────┬──────┘       └────────────┘
                   │                     │
                   ▼                     ▼
             ┌────────────┐       ┌────────────┐
             │ PutKafka   │       │ PutSQL     │
             └────────────┘       └────────────┘
```

### Error Handling Flow

```
┌─────────────┐    ┌─────────────────┐
│ Processor   │───→│ RouteOnAttribute│
└─────────────┘    │ error = true?   │
                   └────────┬────────┘
                            │
              ┌─────────────┴─────────────┐
              │                           │
              ▼                           ▼
       ┌────────────┐             ┌────────────┐
       │ RetryLogic │             │ PutDLQ     │
       │ (3 times)  │             │ (Dead      │
       └─────┬──────┘             │  Letter    │
             │                    │  Queue)    │
             ▼                    └────────────┘
       ┌────────────┐
       │ Success?   │───No──→ PutDLQ
       └────────────┘
```

### Data Enrichment Flow

```
┌─────────┐    ┌──────────────┐    ┌──────────────┐
│ GetHTTP │───→│ CacheLookup  │───→│ EnrichRecord │
└─────────┘    │ (Redis)      │    │ (Join)       │
               └──────────────┘    └──────┬───────┘
                                          │
                                          ▼
                                   ┌──────────────┐
                                   │ PutDatabase  │
                                   └──────────────┘
```

## Data Provenance

### Provenance Events

```
RECEIVE     - Data received from external source
SEND        - Data sent to external destination
CREATE      - Data created by processor
MODIFY      - Data modified by processor
CLONE       - Data cloned (split/clone)
DROP        - Data dropped/removed
FORK        - Data split into multiple FlowFiles
JOIN        - Multiple FlowFiles merged
TRANSFER    - FlowFile moved between queues
ATTRIBUTES  - FlowFile attributes modified
```

### Querying Provenance

```sql
-- Find all events for a FlowFile
SELECT * FROM PROVENANCE_EVENTS
WHERE COMPONENT_ID = 'processor-1'
AND EVENT_TYPE = 'MODIFY'
AND TIMESTAMP > '2024-01-15'

-- Find data lineage
SELECT * FROM PROVENANCE_EVENTS
WHERE FLOWFILE_UUID = 'uuid-here'
ORDER BY TIMESTAMP

-- Find processor activity
SELECT EVENT_TYPE, COUNT(*) as COUNT
FROM PROVENANCE_EVENTS
WHERE COMPONENT_ID = 'processor-1'
GROUP BY EVENT_TYPE
```

### Lineage Visualization

```
Source ──→ Processor1 ──→ Processor2 ──→ Destination
  │           │              │              │
  ▼           ▼              ▼              ▼
CREATE      MODIFY         MODIFY         SEND
```

## Security

### Authentication

```xml
<!-- nifi.properties -->
nifi.web.https.host=nifi.example.com
nifi.web.https.port=8443
nifi.web.https.security.strategy=KeyStoreAndTrustStore

# SSL Configuration
nifi.security.keystore=/path/to/keystore.jks
nifi.security.keystoreType=JKS
nifi.security.keystorePasswd=password
nifi.security.keyPasswd=password
nifi.security.truststore=/path/to/truststore.jks
nifi.security.truststoreType=JKS
nifi.security.truststorePasswd=password
```

### Authorization

```xml
<!-- authorizers.xml -->
<authorizer>
    <identifier>single-user</identifier>
    <class>org.apache.nifi.authorization.SingleUserAuthorizer</class>
</authorizer>

<!-- Users and roles -->
<user>admin</user>
<role>ROLE_ADMIN</role>
<role>ROLE_DFM</role>
```

### Access Policies

```java
// Processor-level access control
Policy: Write to Processor
Users: data-engineers, ops-team

Policy: Read from Processor
Users: analysts, data-engineers

Policy: Operate Processor
Users: ops-team
```

## Clustering

### Cluster Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    NIFI CLUSTER                               │
├─────────────────────────────────────────────────────────────┤
│  Primary Node                                               │
│  • Clustering协调                                           │
│  • Site-to-site leader                                      │
│  • Web UI access                                            │
├─────────────────────────────────────────────────────────────┤
│  Cluster Nodes                                              │
│  • Processors distributed across nodes                      │
│  • Load balancing of FlowFiles                              │
│  • Shared repositories                                      │
└─────────────────────────────────────────────────────────────┘
```

### Configuration

```properties
# nifi.properties - cluster config
nifi.cluster.is.node=true
nifi.cluster.node.address=node1.example.com
nifi.cluster.node.protocol.port=8481
nifi.cluster.node.event.history.size=25
nifi.cluster.node.connection.timeout=5 sec
nifi.cluster.node.read.timeout=5 sec
nifi.cluster.load.balance.host=node1.example.com
nifi.cluster.load.balance.port=8482
```

### Site-to-Site

```java
// Send data between NiFi instances
RemoteProcessGroup {
    url: "https://remote-nifi:8443/nifi"
    port: {
        name: "input-port"
        id: "port-id"
    }
    transport: "SITE-TO-PROCESSOR"
}
```

## Best Practices

### 1. Process Group Design

```
// Organize by function
ProcessGroup: "Data Ingestion"
  - GetFile
  - ConsumeKafka
  - ListenHTTP

ProcessGroup: "Data Transformation"
  - ParseJSON
  - EnrichData
  - Validate

ProcessGroup: "Data Storage"
  - PutDatabase
  - PutS3
  - PublishKafka
```

### 2. Back Pressure Configuration

```java
// Prevent processor overwhelm
Connection {
    backPressureThreshold: 10000  // objects
    backPressureDataSizeThreshold: 1 GB
    
    // When exceeded, upstream processor pauses
}
```

### 3. Priority Queue Ordering

```java
// FlowFile priority
Priority: 
  - PriorityAttribute (custom attribute)
  - OldestFirst (FIFO)
  - NewestFirst (LIFO)
```

### 4. Template Management

```java
// Export/Import templates
Template {
    name: "Kafka Ingestion"
    description: "Consume from Kafka and store"
    processors: [...]
    connections: [...]
}

// Import template
// UI -> Templates -> Upload
```

### 5. Monitoring

```java
// Key metrics to monitor
Metrics {
    FlowFilesReceived: 1000/sec
    FlowFilesSent: 950/sec
    BytesRead: 10 MB/sec
    BytesWritten: 9.5 MB/sec
    ActiveThreads: 10
    QueueSize: 500
}
```

### 6. Resource Management

```properties
# Repository storage limits
nifi.content.repository.max.storage.size=8 GB
nifi.content.repository.archive.max.storage.size=8 GB
nifi.provenance.repository.max.storage.size=1 GB

# Thread pools
nifi.processor.scheduling threads=10
nifi.event.run.duration.seconds=30
```

## Further Reading

- [Apache NiFi Documentation](https://nifi.apache.org/docs/)
- [NiFi User Guide](https://nifi.apache.org/docs/html/user-guide.html)
- [NiFi Record Processing](https://nifi.apache.org/docs/html/nifi-reference.html)
- [NiFi Expression Language](https://nifi.apache.org/docs/html/expression-language-guide.html)
- [NiFi Developer Guide](https://nifi.apache.org/docs/html/developer-guide.html)
