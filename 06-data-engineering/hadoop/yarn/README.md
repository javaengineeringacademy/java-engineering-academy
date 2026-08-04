# YARN (Yet Another Resource Negotiator)

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [ResourceManager](#resourcemanager)
4. [NodeManager](#nodemanager)
5. [ApplicationMaster](#applicationmaster)
6. [Containers](#containers)
7. [Scheduling](#scheduling)
8. [Resource Allocation](#resource-allocation)
9. [YARN History Server](#yarn-history-server)
10. [YARN Timeline Service](#yarn-timeline-service)
11. [Key Takeaways](#key-takeaways)

---

## Overview

YARN is Hadoop's resource management layer, introduced in Hadoop 2.x to separate resource management from job scheduling/monitoring. It enables multiple processing frameworks (MapReduce, Spark, Tez, Flink) to share a Hadoop cluster.

### Goals of YARN

- **Multi-tenancy**: Multiple frameworks share cluster resources
- **Scalability**: Support clusters with thousands of nodes
- **Efficiency**: Better resource utilization than Hadoop 1.x
- **Flexibility**: Support different processing paradigms

### Before YARN (Hadoop 1.x)

```
┌──────────────────────────────────────┐
│           JobTracker                 │
│   (Resource Management + Job        │
│    Scheduling + Monitoring)          │
└──────────────────┬───────────────────┘
                   │
    ┌──────────────┼──────────────┐
    │              │              │
┌───▼───┐      ┌───▼───┐      ┌───▼───┐
│TaskTrkr│      │TaskTrkr│      │TaskTrkr│
└───────┘      └───────┘      └───────┘
```

**Problems**: JobTracker single point of failure, limited scalability, only MapReduce framework

---

## Architecture

### YARN Components

```
┌─────────────────────────────────────────────────────────┐
│                    Client Application                     │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│              ResourceManager (RM)                         │
│  ┌──────────────────┐  ┌──────────────────────────┐    │
│  │    Scheduler     │  │  ApplicationsManager     │    │
│  │ (Resource Alloc) │  │ (App Lifecycle Mgmt)     │    │
│  └──────────────────┘  └──────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │
    ┌────────────────────┼────────────────────┐
    │                    │                    │
┌───▼──────────┐  ┌─────▼────────┐  ┌───────▼────────┐
│ NodeManager  │  │ NodeManager  │  │ NodeManager    │
│  ┌────────┐  │  │  ┌────────┐  │  │  ┌────────┐   │
│  │Container│  │  │  │Container│  │  │  │Container│  │
│  │  (AM)  │  │  │  │  (Task) │  │  │  │  (Task) │  │
│  └────────┘  │  │  └────────┘  │  │  └────────┘   │
│  ┌────────┐  │  │  ┌────────┐  │  │  ┌────────┐   │
│  │Container│  │  │  │Container│  │  │  │Container│  │
│  │  (Task) │  │  │  │  (Task) │  │  │  │  (Task) │  │
│  └────────┘  │  │  └────────┘  │  │  └────────┘   │
└──────────────┘  └──────────────┘  └────────────────┘
```

### Application Lifecycle

```
1. Client submits Application Submission Context to RM
2. RM allocates first container for ApplicationMaster
3. RM launches AM container on NodeManager
4. AM registers with RM
5. AM requests containers for tasks
6. RM allocates containers based on scheduling policy
7. AM launches tasks in allocated containers
8. Tasks communicate progress to AM
9. AM monitors tasks and handles failures
10. Application completes, AM unregisters from RM
```

---

## ResourceManager

The ResourceManager is the master daemon managing cluster resources.

### Components

**Scheduler**:
- Allocates resources to running applications
- Does not monitor or track application status
- Pure scheduler — handles fairness, capacity, or FIFO
- Pluggable scheduling policies

**ApplicationsManager**:
- Accepts job submissions
- Launches ApplicationMaster containers
- Restarts AM on failure
- Manages application lifecycle

### ResourceManager Web UI

- Access at: `http://resourcemanager:8088`
- View running/completed applications
- Cluster resource utilization
- Node health status
- Queue information

### ResourceManager High Availability

```
┌─────────────────────────────────────────────┐
│            ResourceManager HA                │
│  ┌──────────────┐    ┌──────────────┐      │
│  │  Active RM   │    │ Standby RM   │      │
│  │              │    │              │      │
│  └──────────────┘    └──────────────┘      │
│         │                   │               │
│         └─────────┬─────────┘               │
│                   │                          │
│         ┌─────────▼─────────┐               │
│         │  Shared State     │               │
│         │  (ZooKeeper or    │               │
│         │   NFS)            │               │
│         └───────────────────┘               │
└─────────────────────────────────────────────┘
```

- Automatic failover via ZooKeeper
- Active/Standby pattern similar to HDFS HA
- Shared state for application recovery

---

## NodeManager

The NodeManager is the per-node agent managing containers.

### Responsibilities

1. **Container Management**: Launch, monitor, and clean up containers
2. **Resource Reporting**: Report node resource usage to RM
3. **Health Monitoring**: Track node health and report failures
4. **Log Management**: Collect and aggregate container logs
5. **Local Storage Management**: Manage scratch space for containers

### NodeManager Web UI

- Access at: `http://nodemanager:8042`
- View containers running on the node
- Container logs
- Node resource usage

### Container Lifecycle

```
NEW → RUNNING → FINISHED/FAILED/KILLED
```

1. **NEW**: Container allocated by RM
2. **RUNNING**: Container process started
3. **FINISHED**: Task completed successfully
4. **FAILED**: Task failed
5. **KILLED**: Container killed by AM or RM

### Auxiliary Services

NodeManager can run auxiliary services:
- **Shuffle Handler**: Serves map output for shuffle phase
- **HTTP Proxy**: Proxies web requests
- **Custom Services**: Application-specific services

---

## ApplicationMaster

The ApplicationMaster is a per-application process managing the application's lifecycle.

### Responsibilities

1. **Resource Negotiation**: Request containers from RM
2. **Task Management**: Launch, monitor, and restart tasks
3. **Progress Reporting**: Report application progress to RM
4. **Container Management**: Handle container failures
5. **Application Cleanup**: Clean up resources on completion

### AM Container Requirements

- First container allocated by RM
- Runs on best-effort basis (can be preempted)
- Typically allocated with minimal resources (1 vcore, 1GB RAM)

### AM Examples

| Framework | AM Implementation |
|-----------|-------------------|
| MapReduce | MRAppMaster |
| Spark | SparkAM |
| Tez | TezSession |
| Flink | FlinkYarnSession |

---

## Containers

A container is a resource allocation on a node (CPU + memory).

### Container Resources

```xml
<!-- Default container resource limits -->
<property>
    <name>yarn.scheduler.minimum-allocation-mb</name>
    <value>1024</value> <!-- 1GB minimum -->
</property>

<property>
    <name>yarn.scheduler.maximum-allocation-mb</name>
    <value>8192</value> <!-- 8GB maximum -->
</property>

<property>
    <name>yarn.scheduler.minimum-allocation-vcores</name>
    <value>1</value>
</property>

<property>
    <name>yarn.scheduler.maximum-allocation-vcores</name>
    <value>4</value>
</property>
```

### Container Sizes

| Use Case | Recommended Size |
|----------|-----------------|
| MapReduce Mapper | 1-4GB, 1-2 vcores |
| MapReduce Reducer | 2-8GB, 2-4 vcores |
| Spark Executor | 4-16GB, 2-8 vcores |
| ApplicationMaster | 1-2GB, 1 vcore |

### Container Isolation

- Process isolation via Linux cgroups
- Memory limits enforced by YARN
- CPU limits via cgroups CPU scheduler
- Disk I/O isolation limited (improving in newer versions)

---

## Scheduling

### FIFO Scheduler

```
Queue: [Job1] → [Job2] → [Job3]
        └── First come, first served
```

- Simple, first-in-first-out
- No fairness between users/applications
- Not recommended for production

### Capacity Scheduler

```
Queue Structure:
┌──────────────────────────────────────┐
│         Cluster Capacity             │
├──────────────┬───────────────────────┤
│  root        │                       │
│  ├─ prod     │  40% capacity        │
│  └─ dev      │  60% capacity        │
│     ├─ team1 │  50% of dev          │
│     └─ team2 │  50% of dev          │
└──────────────┴───────────────────────┘
```

**Features**:
- Hierarchical queue structure
- Guaranteed capacity per queue
- Maximum capacity limits
- User-level limits
- ACL-based access control

**Configuration**:
```xml
<property>
    <name>yarn.scheduler.capacity.root.queues</name>
    <value>prod,dev</value>
</property>

<property>
    <name>yarn.scheduler.capacity.root.prod.capacity</name>
    <value>40</value>
</property>

<property>
    <name>yarn.scheduler.capacity.root.dev.capacity</name>
    <value>60</value>
</property>

<property>
    <name>yarn.scheduler.capacity.root.dev.maximum-capacity</name>
    <value>80</value>
</property>
```

### Fair Scheduler

```
Default Pool:
┌──────────────────────────────────────┐
│  Job1: ████████████ (40%)            │
│  Job2: ████████████ (40%)            │
│  Job3: ████████ (20%)                │
└──────────────────────────────────────┘

Weighted Sharing:
┌──────────────────────────────────────┐
│  Job1 (weight=2): ████████████████   │
│  Job2 (weight=1): ████████           │
└──────────────────────────────────────┘
```

**Features**:
- Equal sharing of resources by default
- Weight-based sharing
- Preemption support
- Delay scheduling for data locality
- Support for SLO-based scheduling (YARN-9400)

### Scheduler Comparison

| Feature | FIFO | Capacity | Fair |
|---------|------|----------|------|
| Fairness | No | Yes (capacity) | Yes (equal share) |
| Preemption | No | Optional | Yes |
| Priority | Order | Queue priority | Weight-based |
| Use Case | Simple clusters | Enterprise | Shared clusters |
| Complexity | Low | Medium | Medium |

---

## Resource Allocation

### Resource Request

```java
// Request resources from ApplicationMaster
ResourceRequest request = ResourceRequest.newInstance(
    Priority.newInstance(1),           // Priority
    "*",                              // Node label (* = any)
    Resource.newInstance(2048, 2),    // Memory (MB), vCores
    1                                 // Number of containers
);
```

### Resource Allocation Flow

```
1. AM sends resource request to RM
2. RM Scheduler evaluates request
3. RM checks available resources across nodes
4. RM finds suitable node(s) with enough resources
5. RM allocates containers on matching nodes
6. RM sends allocation to AM
7. AM launches tasks in allocated containers
```

### Label-Based Scheduling

```bash
# Add label to node
yarn rmadmin -addToClusterNodeLabels "GPU"

# Set label expression for queue
setProperty yarn.scheduler.capacity.root.queue.node-label-expression "GPU"

# Request specific label from AM
ResourceRequest request = ResourceRequest.newInstance(
    Priority.newInstance(1),
    "GPU",                           // Node label
    Resource.newInstance(4096, 4),
    1
);
```

### Resource Profiles

```xml
<!-- Define resource profiles -->
<property>
    <name>yarn.resource-types</name>
    <value>yarn.io/gpu,yarn.io/fpga</value>
</property>

<property>
    <name>yarn.scheduler.capacity.root.prod.maximum-application-mb</name>
    <value>1048576</value>
</property>
```

---

## YARN History Server

### Purpose

- Stores and serves application history
- Provides web UI for completed applications
- Retains history after ResourceManager restart

### Architecture

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│     AM       │────▶│ History Log  │────▶│ History      │
│ (writes log) │     │ (HDFS)       │     │ Server       │
└──────────────┘     └──────────────┘     └──────┬───────┘
                                                  │
                                           ┌──────▼───────┐
                                           │  Web UI      │
                                           │ (19888)      │
                                           └──────────────┘
```

### Configuration

```xml
<!-- Enable History Server -->
<property>
    <name>yarn.log-aggregation-enable</name>
    <value>true</value>
</property>

<!-- History Server address -->
<property>
    <name>yarn.resourcemanager.history-address</name>
    <value>historyserver:19888</value>
</property>

<!-- Retention period -->
<property>
    <name>yarn.log-aggregation.retain-seconds</name>
    <value>604800</value> <!-- 7 days -->
</property>
```

### Operations

```bash
# Start History Server
yarn historyserver

# Access Web UI
http://historyserver:19888

# View application logs
yarn logs -applicationId application_1234567890_0001
```

---

## YARN Timeline Service

### Purpose

- Stores and retrieves application/attempt/container information
- Scalable for large clusters
- Supports both generic and framework-specific events

### Timeline Service v1

```
┌──────────────┐     ┌──────────────┐
│     AM       │────▶│ Timeline     │
│              │     │ Store (HDFS) │
└──────────────┘     └──────┬───────┘
                            │
                     ┌──────▼───────┐
                     │ Timeline     │
                     │ Service      │
                     │ (Web UI)     │
                     └──────────────┘
```

### Timeline Service v2 (Hadoop 3.x+)

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│     AM       │────▶│ Timeline     │────▶│ RDBMS        │
│              │     │ Reader       │     │ (Metadata)   │
└──────────────┘     └──────────────┘     └──────────────┘
                           │
                     ┌─────▼──────┐
                     │ Store      │
                     │ (HDFS/     │
                     │  LevelDB)  │
                     └────────────┘
```

**Improvements over v1**:
- Scalable backend (pluggable storage)
- Better query performance
- Framework-agnostic event storage
- REST API for event ingestion

### Configuration

```xml
<!-- Enable Timeline Service -->
<property>
    <name>yarn.timeline-service.enabled</name>
    <value>true</value>
</property>

<!-- Timeline Store -->
<property>
    <name>yarn.timeline-service.store-class</name>
    <value>org.apache.hadoop.yarn.server.applicationtimeline service.storage.HDFSApplicationTimelineStore</value>
</property>

<!-- Reader address -->
<property>
    <name>yarn.timeline-service.webapp.address</name>
    <value>timeline-service:8188</value>
</property>
```

### Writing Timeline Events

```java
// In ApplicationMaster
TimelineClient client = TimelineClient.createTimelineClient();

TimelinePutResponse response = client.putEntities(
    TimelineEntity.newInstance(
        "APPLICATION",
        applicationId.toString()
    )
    .addInfo("state", "RUNNING")
    .addInfo("progress", 0.5)
);
```

---

## Key Takeaways

1. **YARN separates resource management** from job scheduling, enabling multiple frameworks on one cluster
2. **ResourceManager** is the master managing cluster resources — Scheduler + ApplicationsManager
3. **NodeManager** is the per-node agent managing containers and reporting resource usage
4. **ApplicationMaster** is a per-application process managing task lifecycle and resource negotiation
5. **Containers** are resource allocations (CPU + memory) on specific nodes
6. **Capacity Scheduler** is recommended for enterprise multi-tenant clusters; Fair Scheduler for shared environments
7. **YARN History Server** provides web UI and logs for completed applications
8. **Timeline Service** stores and serves application metadata and events
9. **Label-based scheduling** enables heterogeneous cluster support (GPU, FPGA)
10. **YARN HA** uses ZooKeeper for automatic failover between Active/Standby ResourceManagers

---

## Further Reading

- *Hadoop: The Definitive Guide* by Tom White (Chapter 9)
- YARN Architecture Guide (Apache)
- YARN Scheduling Guide (Apache)
- Capacity Scheduler Guide (Apache)
