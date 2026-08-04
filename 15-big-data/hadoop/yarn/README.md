# YARN - Yet Another Resource Negotiator

YARN is Hadoop's cluster resource management and job scheduling framework, enabling multiple data processing engines to share cluster resources.

## Table of Contents

1. [Architecture](#architecture)
2. [Core Components](#core-components)
3. [ResourceManager](#resourcemanager)
4. [NodeManager](#nodemanager)
5. [ApplicationMaster](#applicationmaster)
6. [Container](#container)
7. [Scheduler](#scheduler)
8. [Application Lifecycle](#application-lifecycle)
9. [Resource Model](#resource-model)
10. [Configuration](#configuration)
11. [CLI Commands](#cli-commands)
12. [Best Practices](#best-practices)

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         YARN Architecture                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                    ResourceManager                        │   │
│  │  ┌─────────────┐  ┌──────────────┐  ┌──────────────┐   │   │
│  │  │  Scheduler   │  │  Applications│  │   Client     │   │   │
│  │  │  (Capacity/  │  │  Manager     │  │   Service    │   │   │
│  │  │  Fair)       │  │              │  │              │   │   │
│  │  └─────────────┘  └──────────────┘  └──────────────┘   │   │
│  └──────────────────────────────────────────────────────────┘   │
│           │                    │                    │             │
│  ┌────────▼────────────────────▼────────────────────▼────────┐  │
│  │              NodeManager (Worker Nodes)                    │  │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐      │  │
│  │  │  Container  │  │  Container  │  │  Container  │      │  │
│  │  │  (App 1)    │  │  (App 2)    │  │  (App 3)    │      │  │
│  │  └─────────────┘  └─────────────┘  └─────────────┘      │  │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐      │  │
│  │  │  Container  │  │  Container  │  │  Container  │      │  │
│  │  │  (App 1)    │  │  (App 2)    │  │  (App 3)    │      │  │
│  │  └─────────────┘  └─────────────┘  └─────────────┘      │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Key Design Goals

- **Multi-tenancy**: Support multiple frameworks (MapReduce, Spark, Tez, Flink)
- **Resource efficiency**: Better utilization than MapReduce v1
- **Scalability**: Handle thousands of nodes
- **High availability**: Active/Standby ResourceManager
- **Flexibility**: Pluggable schedulers

---

## Core Components

### Component Summary

| Component | Role | Location |
|-----------|------|----------|
| ResourceManager | Global resource manager | Master node |
| NodeManager | Per-node agent | Worker nodes |
| ApplicationMaster | Per-application coordinator | Worker node |
| Container | Resource allocation unit | Worker nodes |
| Scheduler | Resource allocation policy | ResourceManager |

---

## ResourceManager

### Global Coordinator

```
ResourceManager
├── ApplicationsManager
│   ├── Application submission
│   ├── Application lifecycle
│   └── ApplicationMaster launch
├── Scheduler
│   ├── Capacity Scheduler
│   ├── Fair Scheduler
│   └── FIFO Scheduler
├── ResourceTrackerService
│   ├── Node registration
│   ├── Heartbeat processing
│   └── Container status
└── Security
    ├── Token management
    └── ACL enforcement
```

### ResourceManager Functions

| Function | Description |
|----------|-------------|
| Resource allocation | Assigns containers to applications |
| Application management | Tracks application state |
| Node management | Monitors cluster health |
| Security | Manages tokens and authentication |
| HA coordination | Active/Standby failover |

### HA Configuration

```xml
<!-- yarn-site.xml -->
<property>
    <name>yarn.resourcemanager.ha.enabled</name>
    <value>true</value>
</property>

<property>
    <name>yarn.resourcemanager.cluster-id</name>
    <value>yarn-cluster</value>
</property>

<property>
    <name>yarn.resourcemanager.ha.rm-ids</name>
    <value>rm1,rm2</value>
</property>

<property>
    <name>yarn.resourcemanager.hostname.rm1</name>
    <value>rm1-host.example.com</value>
</property>

<property>
    <name>yarn.resourcemanager.hostname.rm2</name>
    <value>rm2-host.example.com</value>
</property>
```

---

## NodeManager

### Per-Node Agent

```
NodeManager
├── ContainerManager
│   ├── Container launch
│   ├── Container monitoring
│   └── Container cleanup
├── ResourceLocalization
│   ├── Public resources
│   ├── Private resources
│   └── Archive management
├── NodeHealthChecker
│   ├── Disk monitoring
│   ├── Network monitoring
│   └── Health reporting
└── Security
    ├── Container token validation
    └── Application token validation
```

### NodeManager Responsibilities

| Responsibility | Description |
|----------------|-------------|
| Container lifecycle | Start, monitor, stop containers |
| Resource reporting | Report available resources |
| Health monitoring | Check node health status |
| Log aggregation | Collect and store logs |
| Local storage | Manage container working directories |

### Node Labels

```bash
# Add label to node
yarn rmadmin -addToClusterNodeLabels "GPU"

# Assign node to label
yarn rmadmin -replaceNodeToLabel "node1:GPU"

# Check node labels
yarn node -list -labels
```

### Configuration

```xml
<!-- yarn-site.xml -->
<property>
    <name>yarn.nodemanager.resource.memory-mb</name>
    <value>8192</value>  <!-- 8 GB -->
</property>

<property>
    <name>yarn.nodemanager.resource.cpu-vcores</name>
    <value>8</value>
</property>

<property>
    <name>yarn.nodemanager.local-dirs</name>
    <value>/data/yarn/local</value>
</property>

<property>
    <name>yarn.nodemanager.log-dirs</name>
    <value>/data/yarn/logs</value>
</property>

<property>
    <name>yarn.nodemanager.delete.delay-sec</name>
    <value>3600</value>  <!-- 1 hour -->
</property>
```

---

## ApplicationMaster

### Per-Application Coordinator

```
Application Master Lifecycle:

1. ResourceManager launches AM
2. AM registers with ResourceManager
3. AM requests containers from Scheduler
4. Scheduler allocates containers
5. AM launches tasks in containers
6. AM monitors task progress
7. AM reports completion to ResourceManager
```

### AM Functions

| Function | Description |
|----------|-------------|
| Resource negotiation | Request containers from Scheduler |
| Task scheduling | Assign tasks to containers |
| Progress monitoring | Track task execution |
| Failure handling | Recover from task failures |
| Status reporting | Update application status |

### Custom ApplicationMaster

```java
public class CustomAM extends ApplicationMaster {
    
    private List<Container> containers = new ArrayList<>();
    
    @Override
    public void startAppMaster() {
        // Register with ResourceManager
        registerAppMaster();
        
        // Request containers
        for (int i = 0; i < numContainers; i++) {
            requestContainer();
        }
    }
    
    @Override
    protected void execute() throws YarnException, IOException {
        // Launch tasks in containers
        for (Container container : containers) {
            launchTask(container);
        }
        
        // Monitor and wait for completion
        monitorTasks();
        
        // Finish application
        finishAppMaster();
    }
    
    private void requestContainer() throws YarnException, IOException {
        Resource request = Resource.newInstance(1024, 1);  // 1 GB, 1 vcore
        containerRequest = new ContainerRequest(
            request, null, null, Priority.UNDEFINED, true, null);
        resourceManagerClient.addContainerRequest(containerRequest);
    }
}
```

---

## Container

### Resource Allocation Unit

```
Container Types:

┌─────────────────────────────────────────────┐
│  AM Container (1 per application)          │
│  └── Manages application lifecycle         │
├─────────────────────────────────────────────┤
│  Task Containers (multiple per app)        │
│  └── Execute actual tasks                  │
├─────────────────────────────────────────────┤
│  Auxiliary Services (optional)              │
│  └── Log aggregation, shuffle, etc.        │
└─────────────────────────────────────────────┘
```

### Container Resources

| Resource | Description | Default |
|----------|-------------|---------|
| Memory | RAM allocation | 1024 MB |
| Virtual cores | CPU allocation | 1 vcore |
| GPU | GPU allocation (if supported) | 0 |
| Extended resources | Custom resources | None |

### Container Lifecycle

```
NEW → ACQUIRED → RUNNING → COMPLETED
                 │
                 ├── FAILED
                 │
                 └── KILLED
```

### Container Configuration

```xml
<!-- yarn-site.xml -->
<property>
    <name>yarn.scheduler.minimum-allocation-mb</name>
    <value>1024</value>  <!-- Minimum container size -->
</property>

<property>
    <name>yarn.scheduler.maximum-allocation-mb</name>
    <value>8192</value>  <!-- Maximum container size -->
</property>

<property>
    <name>yarn.scheduler.minimum-allocation-vcores</name>
    <value>1</value>
</property>

<property>
    <name>yarn.scheduler.maximum-allocation-vcores</name>
    <value>8</value>
</property>
```

---

## Scheduler

### Capacity Scheduler

```xml
<!-- capacity-scheduler.xml -->
<property>
    <name>yarn.scheduler.capacity.root.queues</name>
    <value>default,production</value>
</property>

<property>
    <name>yarn.scheduler.capacity.root.default.capacity</name>
    <value>30</value>  <!-- 30% of cluster -->
</property>

<property>
    <name>yarn.scheduler.capacity.root.production.capacity</name>
    <value>70</value>  <!-- 70% of cluster -->
</property>

<property>
    <name>yarn.scheduler.capacity.root.default.maximum-capacity</name>
    <value>50</value>  <!-- Can burst to 50% -->
</property>
```

### Queue Hierarchy

```
root
├── default (30%)
│   ├── interactive (15%)
│   └── batch (15%)
└── production (70%)
    ├── realtime (40%)
    └── ml (30%)
```

### Fair Scheduler

```xml
<!-- fair-scheduler.xml -->
<allocations>
  <queue name="root">
    <queue name="production">
      <minResources>4000mb,2vcores</minResources>
      <maxResources>20000mb,10vcores</maxResources>
      <weight>2.0</weight>
    </queue>
    <queue name="development">
      <minResources>2000mb,1vcores</minResources>
      <maxResources>10000mb,5vcores</maxResources>
      <weight>1.0</weight>
    </queue>
  </queue>
  
  <user name="admin">
    <maxRunningApplications>20</maxRunningApplications>
  </user>
</allocations>
```

### Scheduler Comparison

| Feature | Capacity | Fair | FIFO |
|---------|----------|------|------|
| Multi-tenancy | Yes | Yes | No |
| Resource guarantees | Yes | Yes | No |
| Preemption | Optional | Yes | No |
| Configuration | Complex | Simple | Simple |
| Use case | Production | Mixed | Development |

---

## Application Lifecycle

### Submission Flow

```
1. Client submits application to ResourceManager
   └── Client API: submitApplication()

2. ResourceManager creates application
   └── Allocates application ID
   └── Stores application context

3. ResourceManager launches ApplicationMaster
   └── Selects node with available resources
   └── Launches AM container

4. AM registers with ResourceManager
   └── Reports capabilities and resource needs

5. AM requests containers
   └── Sends container requests to Scheduler

6. Scheduler allocates containers
   └── Based on queue capacity and policies

7. AM launches tasks
   └── Distributes tasks across allocated containers

8. Tasks execute
   └── AM monitors progress

9. Application completes
   └── AM finishes, resources released
```

### Application States

```
NEW → SUBMITTED → ACCEPTED → RUNNING → FINISHED
                                │
                                ├── FAILED
                                │
                                └── KILLED
```

### Application Tracking

```bash
# List running applications
yarn application -list

# List all applications
yarn application -list -appStates ALL

# Kill application
yarn application -kill <application_id>

# Get application status
yarn application -status <application_id>

# Get application logs
yarn logs -applicationId <application_id>
```

---

## Resource Model

### Resource Types

```
Memory Resources:
├── Total Node Memory: 64 GB
├── OS Reserved: 4 GB
├── YARN Reserved: 4 GB
├── Available for Containers: 56 GB
└── Containers: Multiple instances

CPU Resources:
├── Total Cores: 16
├── OS Reserved: 2
├── YARN Reserved: 2
├── Available for Containers: 12
└── Containers: Multiple instances
```

### Resource Allocation Algorithms

```java
// First Fit Decreasing
// Sort nodes by available resources (descending)
// Allocate to first node with sufficient resources

// Best Fit
// Find node with least remaining resources after allocation

// Node Label based
// Route applications to labeled nodes
```

### Extended Resources

```xml
<!-- yarn-site.xml -->
<property>
    <name>yarn.resource-types</name>
    <value>yarn.io/gpu</value>
</property>

<property>
    <name>yarn.nodemanager.resource-plugins</name>
    <value>yarn.io/gpu</value>
</property>
```

---

## Configuration

### Core YARN Settings

```xml
<!-- yarn-site.xml -->

<!-- ResourceManager settings -->
<property>
    <name>yarn.resourcemanager.address</name>
    <value>rm-host:8032</value>
</property>

<property>
    <name>yarn.resourcemanager.scheduler.address</name>
    <value>rm-host:8030</value>
</property>

<property>
    <name>yarn.resourcemanager.resource-tracker.address</name>
    <value>rm-host:8031</value>
</property>

<!-- NodeManager settings -->
<property>
    <name>yarn.nodemanager.resource.memory-mb</name>
    <value>8192</value>
</property>

<property>
    <name>yarn.nodemanager.resource.cpu-vcores</name>
    <value>8</value>
</property>

<!-- Application settings -->
<property>
    <name>yarn.app.mapreduce.am.resource.mb</name>
    <value>1024</value>
</property>

<property>
    <name>yarn.app.mapreduce.am.command-opts</name>
    <value>-Xmx819m</value>
</property>

<!-- Log aggregation -->
<property>
    <name>yarn.log-aggregation-enable</name>
    <value>true</value>
</property>

<property>
    <name>yarn.log-aggregation.retain-seconds</name>
    <value>2592000</value>  <!-- 30 days -->
</property>
```

### Memory Configuration

```
Node Memory Calculation:

Node Total: 64 GB
├── OS/Hadoop Daemons: 4 GB
├── YARN overhead: 4 GB
│   ├── NodeManager: 1 GB
│   ├── Container logs: 1 GB
│   └── Other: 2 GB
└── Available for Containers: 56 GB

Container Sizing:
├── Minimum: 1024 MB
├── Maximum: 8192 MB (or node max)
└── Recommended: 4096 MB
```

### Capacity Scheduler Configuration

```xml
<!-- capacity-scheduler.xml -->
<property>
    <name>yarn.scheduler.capacity.root.queues</name>
    <value>default,production</value>
</property>

<property>
    <name>yarn.scheduler.capacity.root.default.capacity</name>
    <value>30</value>
</property>

<property>
    <name>yarn.scheduler.capacity.root.default.maximum-capacity</name>
    <value>50</value>
</property>

<property>
    <name>yarn.scheduler.capacity.root.default.user-limit-factor</name>
    <value>1.5</value>
</property>

<property>
    <name>yarn.scheduler.capacity.maximum-am-resource-percent</name>
    <value>0.2</value>  <!-- Max 20% for AMs -->
</property>
```

---

## CLI Commands

### Application Management

```bash
# Submit application
yarn jar myapp.jar com.example.Main

# List applications
yarn application -list
yarn application -list -appStates RUNNING

# Get application status
yarn application -status application_1234567890_0001

# Kill application
yarn application -kill application_1234567890_0001

# Get application logs
yarn logs -applicationId application_1234567890_0001
yarn logs -applicationId application_1234567890_0001 -nodeAddress node1:45454
```

### Node Management

```bash
# List nodes
yarn node -list
yarn node -list -all

# Get node status
yarn node -status node1:45454

# Decommission node
yarn node -decommission node1

# Refresh nodes
yarn rmadmin -refreshNodes
```

### Queue Management

```bash
# List queues
yarn queue -status default

# Get queue information
yarn queue -status production

# Refresh queue configuration
yarn rmadmin -refreshQueues
```

### Admin Operations

```bash
# Check cluster metrics
yarn metrics

# Get cluster information
yarn node -list -labels

# Add node label
yarn rmadmin -addToClusterNodeLabels "GPU"

# Remove node label
yarn rmadmin -removeFromClusterNodeLabels "GPU"

# Check scheduler info
yarn scheduler -status
```

---

## Best Practices

### Resource Sizing

| Application | Memory | vCores | Containers |
|-------------|--------|--------|------------|
| MapReduce | 2048-4096 MB | 1-2 | 1 per map/reduce task |
| Spark | 4096-8192 MB | 2-4 | Based on cores |
| Hive on Tez | 4096 MB | 2 | 1 per query |
| Flink | 4096 MB | 2-4 | 1 per TaskManager |

### Queue Design

```yaml
# Production queue design
queues:
  - name: production
    capacity: 60
    max-capacity: 80
    user-limit: 2
    
  - name: development
    capacity: 20
    max-capacity: 40
    
  - name: interactive
    capacity: 20
    max-capacity: 30
    user-limit: 1
```

### Monitoring

```bash
# Key metrics to monitor
yarn metrics

# ResourceManager UI
http://rm-host:8088

# NodeManager logs
tail -f /var/log/hadoop/yarn/yarn-*-nodemanager-*.log

# Check container status
yarn container -list <application_id>
```

### Performance Tips

| Tip | Description |
|-----|-------------|
| Right-size containers | Don't over-provision memory |
| Use node labels | Route workloads to specific nodes |
| Enable preemption | Allow high-priority jobs to preempt |
| Configure AM container | Ensure AM has enough resources |
| Log aggregation | Enable centralized log storage |
| Resource profiles | Use different configs for different workloads |

---

## Troubleshooting

### Common Issues

**1. Application stuck in ACCEPTED**
```bash
# Check queue capacity
yarn queue -status <queue-name>

# Check cluster resources
yarn metrics

# Check pending applications
yarn application -list -appStates ACCEPTED
```

**2. Containers killed by NodeManager**
```bash
# Check container logs
yarn logs -applicationId <app-id> -containerId <container-id>

# Common reasons:
# - Physical memory limit exceeded
# - Virtual memory limit exceeded
# - Debug info size exceeded
```

**3. ResourceManager not starting**
```bash
# Check ResourceManager logs
tail -100 /var/log/hadoop/yarn/yarn-*-resourcemanager-*.log

# Check ZooKeeper connectivity
zkCli.sh -server zk-host:2181 ls /yarn

# Check shared edit log
hdfs dfs -ls /yarn/leader-election/app1
```

**4. NodeManager not connecting**
```bash
# Check NodeManager logs
tail -100 /var/log/hadoop/yarn/yarn-*-nodemanager-*.log

# Verify ResourceManager address
grep yarn.resourcemanager.address yarn-site.xml

# Check network connectivity
telnet rm-host 8031
```

### Debug Commands

```bash
# Check application attempt history
yarn applicationattempt -list <app-id>

# Get container status
yarn container -list <app-id>
yarn container -status <container-id>

# Check resource usage
yarn top

# Analyze application
yarn logs -applicationId <app-id> | grep "Task completed"
```
