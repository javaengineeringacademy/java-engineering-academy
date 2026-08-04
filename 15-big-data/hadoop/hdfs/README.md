# Hadoop Distributed File System (HDFS)

HDFS is a distributed, scalable, and fault-tolerant filesystem designed to store large datasets across clusters of commodity hardware.

## Table of Contents

1. [Architecture](#architecture)
2. [Core Components](#core-components)
3. [Block Storage](#block-storage)
4. [Replication](#replication)
5. [NameNode](#namenode)
6. [DataNode](#datanode)
7. [Read/Write Operations](#readwrite-operations)
8. [High Availability](#high-availability)
9. [Federation](#federation)
10. [Configuration](#configuration)
11. [CLI Commands](#cli-commands)
12. [Best Practices](#best-practices)

---

## Architecture

```
                    ┌──────────────────────┐
                    │     Client Node       │
                    │  (HDFS Client API)    │
                    └──────────┬───────────┘
                               │
              ┌────────────────┼────────────────┐
              │                │                │
     ┌────────▼────────┐      │       ┌────────▼────────┐
     │    NameNode      │      │       │  Secondary NN   │
     │  (Metadata)      │      │       │  (Checkpoints)  │
     └────────┬────────┘      │       └─────────────────┘
              │                │
     ┌────────┼────────────────┼────────────┐
     │        │                │            │
┌────▼───┐ ┌──▼─────┐ ┌───────▼──┐ ┌──────▼───┐
│DataNode│ │DataNode │ │ DataNode │ │ DataNode │
│ (Rack1)│ │(Rack1)  │ │ (Rack2)  │ │ (Rack3)  │
└────────┘ └────────┘ └──────────┘ └──────────┘
```

### Key Design Principles

- **Write-once, read-many**: Files are immutable once written
- **Streaming access**: Optimized for large sequential reads
- **Fault tolerance**: Automatic detection and recovery from failures
- **Scalability**: Add nodes to increase capacity linearly
- **Data locality**: Move computation to data, not data to computation

---

## Core Components

### NameNode (Master)

The NameNode manages the filesystem namespace and metadata.

| Function | Description |
|----------|-------------|
| Namespace management | Maintains directory tree and file-to-block mapping |
| Block mapping | Tracks which DataNodes hold each block |
| Metadata persistence | Saves namespace to `fsimage` and `edits` log |
| RPC server | Handles client and DataNode requests |
| Lease management | Manages write leases for files |

### DataNode (Worker)

DataNodes store actual data blocks on local disks.

| Function | Description |
|----------|-------------|
| Block storage | Stores blocks as files on local filesystem |
| Heartbeats | Sends periodic signals to NameNode |
| Block reports | Lists all blocks stored on the DataNode |
| Block transfer | Handles read/write requests from clients and other DataNodes |
| Pipeline management | Participates in write pipelines |

### Secondary NameNode

- Not a hot standby (despite the name)
- Periodically merges `fsimage` with `edits` log
- Reduces NameNode startup time
- In HA mode, replaced by Standby NameNode

---

## Block Storage

### Default Block Size

| Hadoop Version | Default Block Size |
|----------------|-------------------|
| Hadoop 1.x | 64 MB |
| Hadoop 2.x+ | 128 MB |
| Hadoop 3.x (configurable) | 128 MB - 1 GB |

### Block Architecture

```
File: /data/logs/2024/access.log (400 MB)

┌─────────┬─────────┬─────────┬─────────┐
│ Block 0 │ Block 1 │ Block 2 │ Block 3 │
│ 128 MB  │ 128 MB  │ 128 MB  │ 16 MB   │
└─────────┴─────────┴─────────┴─────────┘
    │         │         │         │
    ▼         ▼         ▼         ▼
┌────────┐┌────────┐┌────────┐┌────────┐
│ DN1    ││ DN2    ││ DN3    ││ DN1    │
│ DN3    ││ DN4    ││ DN1    ││ DN4    │
│ DN5    ││ DN5    ││ DN4    ││ DN5    │
└────────┘└────────┘└────────┘└────────┘
```

### Block Metadata

Each block stores:
- **Block ID**: Unique identifier (64-bit)
- **Generation stamp**: Version tracking
- **Data checksums**: For integrity verification
- **Block pool ID**: Identifies the namespace

### Block Report

DataNodes send block reports to NameNode containing:
- Block pool ID
- List of block IDs with their metadata
- Storage information (disk, SSD, etc.)

---

## Replication

### Replication Strategy

```
Default Replication Factor: 3

Rack 1                    Rack 2                    Rack 3
┌─────────────┐          ┌─────────────┐          ┌─────────────┐
│   DataNode  │          │   DataNode  │          │   DataNode  │
│  ┌───────┐  │          │  ┌───────┐  │          │  ┌───────┐  │
│  │Block A│◄─┼──────────┼──┤Block A│  │          │  │Block A│◄─┼── Copy
│  │ Copy 1│  │          │  │ Copy 2│  │          │  │ Copy 3│  │
│  └───────┘  │          │  └───────┘  │          │  └───────┘  │
└─────────────┘          └─────────────┘          └─────────────┘
      │                        │                        │
      └────────────────────────┼────────────────────────┘
                               │
                    Rack Awareness Policy
              (1 copy per rack for fault tolerance)
```

### Replication Policies

1. **First replica**: On the writer's node (or random if writer is external)
2. **Second replica**: On a different rack (rack awareness)
3. **Third replica**: On same rack as second replica (default policy)

### Replication Management

| Operation | Description |
|-----------|-------------|
| Block placement | NameNode selects target DataNodes |
| Replication monitor | Checks under-replicated blocks every 5 seconds |
| Rebalancer | Distributes blocks evenly across DataNodes |
| Block invalidation | Removes excess replicas |

### Configuration Parameters

```xml
<!-- hdfs-site.xml -->
<property>
    <name>dfs.replication</name>
    <value>3</value>
</property>

<property>
    <name>dfs.replication.max</name>
    <value>512</value>
</property>

<property>
    <name>dfs.replication.min</name>
    <value>1</value>
</property>

<property>
    <name>dfs.namenode.replication.min</name>
    <value>1</value>
</property>
```

---

## NameNode

### Metadata Storage

```
NameNode Metadata Flow:

Client Request ──► Edit Log (edits_0000000000000000001)
                          │
                          ▼
                    ┌─────────┐
                    │ fsimage │ (Checkpoint every hour)
                    └────┬────┘
                         │
                         ▼
                    Merge on startup
```

### fsimage Format

The fsimage contains:
- Complete directory tree
- File-to-block mapping
- Block replica locations
- File permissions and timestamps
- File sizes

### Edit Log

- Records all filesystem modifications
- Append-only log
- Checkpointed periodically by Secondary NameNode or Standby NameNode
- Enables fast NameNode recovery

### NameNode Memory

```bash
# Estimate NameNode memory needs
# Rule of thumb: 1 GB per 1 million blocks
# For 100 million blocks: ~100 GB heap

# hdfs-site.xml
<property>
    <name>dfs.namenode.handler.count</name>
    <value>100</value>
</property>

<property>
    <name>dfs.namenode.fs-limits.min-block-size</name>
    <value>1048576</value>  <!-- 1MB in bytes -->
</property>
```

### NameNode Startup Modes

| Mode | Description | Use Case |
|------|-------------|----------|
| Normal | Loads fsimage + replays edits | Standard startup |
| Checkpoint | Merges fsimage with edits | Maintenance |
| Upgrade | Handles version upgrades | Version migration |
| Rollback | Reverts to previous version | Recovery |

---

## DataNode

### Storage Layout

```
DataNode Storage Structure:

/data/hadoop/datanode/
├── current/
│   ├── BP-<blockpool-id>/
│   │   ├── current/
│   │   │   ├── finalized/
│   │   │   │   ├── subdir0/
│   │   │   │   │   ├── subdir1/
│   │   │   │   │   │   ├── blk_<blockid>
│   │   │   │   │   │   ├── blk_<blockid>.meta
│   │   │   │   │   │   └── ...
│   │   │   ├── rbw/          (replicas being written)
│   │   │   └── dncp_block_verification.log
│   │   └── tmp/
│   └── VERSION
```

### Block Files

Each block consists of two files:
- `blk_<blockid>`: The actual data
- `blk_<blockid>.meta`: Checksums and metadata

### DataNode Communication

| Protocol | Purpose | Interval |
|----------|---------|----------|
| Heartbeat | Status reporting | 3 seconds |
| Block report | Full block inventory | 6 hours (initial: 1 hour) |
| Block received | New block notifications | Until pipeline complete |
| Balancer command | Rebalancing operations | As needed |

### DataNode Health

```bash
# Check DataNode status
hdfs dfsadmin -report

# Decommission a DataNode
# Add to exclude file
echo "datanode-hostname" >> /etc/hadoop/exclude-file
hdfs dfsadmin -refreshNodes

# Enter maintenance mode (Hadoop 3.x)
hdfs dfsadmin -metaserver <metaserver-name>
```

---

## Read/Write Operations

### Write Pipeline

```
Client ──► DN1 ──► DN2 ──► DN3

1. Client asks NameNode to create file
2. NameNode allocates blocks and DataNodes
3. Client writes to closest DataNode (DN1)
4. DN1 forwards to DN2, DN2 to DN3 (pipeline)
5. ACKs flow back: DN3 → DN2 → DN1 → Client
6. Client confirms complete with NameNode
```

### Write Protocol Details

```java
// Simplified write flow
DFSOutputStream out = fs.create(new Path("/data/file.txt"));
out.write(data);  // Writes go through pipeline
out.hflush();     // Forces all replicas to disk
out.close();      // Commits file to NameNode
```

### Read Operation

```
Client ──► NameNode (get block locations)
     ──► DataNode (read blocks directly)
     ──► Reassemble file
```

### Read Optimization

- Client reads from closest DataNode (rack-aware)
- Prefetching of next blocks
- Parallel block reads for striped files (Hadoop 3.x)
- Short-circuit reads (bypassing DataNode daemon)

### Pipeline Recovery

```
Write failure during pipeline:

Before: Client → DN1 → DN2 → DN3
After:  Client → DN1 → DN3

Recovery steps:
1. Close pipeline at point of failure
2. NameNode detects incomplete block
3. New pipeline created excluding failed node
4. Remaining data re-sent through new pipeline
```

---

## High Availability

### HA Architecture

```
Active NameNode          Standby NameNode
      │                         │
      ├── JournalNode ◄─────────┤
      ├── JournalNode           │
      ├── JournalNode           │
      │                         │
      ▼                         ▼
  fsimage + edits          fsimage + edits

Shared Edit Log (QJM):
├── JournalNode 1 (Port 8480)
├── JournalNode 2 (Port 8480)
└── JournalNode 3 (Port 8480)
```

### Journal Nodes

- Maintain shared edit log
- Minimum 3 nodes (for majority consensus)
- Quorum-based protocol (2 of 3 must agree)
- Handles write-ahead logging

### Automatic Failover

```xml
<!-- hdfs-site.xml -->
<property>
    <name>dfs.ha.automatic-failover.enabled</name>
    <value>true</value>
</property>

<property>
    <name>dfs.ha.fencing.methods</name>
    <value>shell(/bin/true)</value>
</property>
```

### Failover Process

1. Health monitoring detects Active NameNode failure
2. Standby NameNode transitions to Active
3. ZKFC (ZooKeeper Failover Controller) coordinates
4. Client connections re-routed to new Active

---

## Federation

### Federated Architecture

```
┌─────────────────────────────────────────────┐
│              Client Applications            │
└───────────────────────┬─────────────────────┘
                        │
         ┌──────────────┼──────────────┐
         │              │              │
    ┌────▼────┐    ┌────▼────┐    ┌────▼────┐
    │ NameNode│    │ NameNode│    │ NameNode│
    │   NS1   │    │   NS2   │    │   NS3   │
    └────┬────┘    └────┬────┘    └────┬────┘
         │              │              │
    ┌────┴──────────────┴──────────────┴────┐
    │           DataNodes (shared)          │
    └───────────────────────────────────────┘
```

### Namespace Volumes

- Each NameNode manages a separate namespace
- DataNodes store blocks for all namespaces
- Block pools are namespace-specific
- Independent operation and failure isolation

### Configuration

```xml
<!-- hdfs-site.xml -->
<property>
    <name>dfs.nameservices</name>
    <value>ns1,ns2,ns3</value>
</property>

<property>
    <name>dfs.namenode.rpc-address.ns1</name>
    <value>nn1-host:8020</value>
</property>

<property>
    <name>dfs.namenode.rpc-address.ns2</name>
    <value>nn2-host:8020</value>
</property>
```

---

## Configuration

### Core HDFS Settings

```xml
<!-- hdfs-site.xml -->

<!-- Block size -->
<property>
    <name>dfs.blocksize</name>
    <value>134217728</value>  <!-- 128 MB -->
</property>

<!-- Replication factor -->
<property>
    <name>dfs.replication</name>
    <value>3</value>
</property>

<!-- NameNode heap -->
<property>
    <name>heapsize</name>
    <value>4g</value>
</property>

<!-- DataNode handler count -->
<property>
    <name>dfs.datanode.handler.count</name>
    <value>10</value>
</property>

<!-- Data transfer bandwidth -->
<property>
    <name>dfs.datanode.balance.bandwidthPerSec</name>
    <value>10485760</value>  <!-- 10 MB/s -->
</property>

<!-- Checksum algorithm -->
<property>
    <name> dfs.checksum.type</name>
    <value>CRC32C</value>
</property>
```

### Memory Settings

```bash
# hdfs-env.sh
export HADOOP_NAMENODE_OPTS="-Xmx8g -XX:+UseG1GC"
export HADOOP_DATANODE_OPTS="-Xmx4g -XX:+UseG1GC"

# For large clusters (>10 million blocks)
export HADOOP_HEAPSIZE_MAX=16g
```

### Network Configuration

```xml
<!-- Enable rack awareness -->
<property>
    <name>net.topology.script.file.name</name>
    <value>/etc/hadoop/conf/topology.sh</value>
</property>
```

---

## CLI Commands

### File Operations

```bash
# List files
hdfs dfs -ls /data
hdfs dfs -ls -R /data  # Recursive

# Create directory
hdfs dfs -mkdir -p /data/logs/2024

# Copy files to HDFS
hdfs dfs -put localfile.txt /data/
hdfs dfs -copyFromLocal localfile.txt /data/

# Copy files from HDFS
hdfs dfs -get /data/file.txt ./
hdfs dfs -getmerge /data/logs/ ./logs.txt

# Read file contents
hdfs dfs -cat /data/file.txt
hdfs dfs -tail /data/file.txt
hdfs dfs -text /data/file.txt  # Handles compressed files

# Remove files
hdfs dfs -rm /data/file.txt
hdfs dfs -rm -r /data/logs/

# Check disk usage
hdfs dfs -du -h /data
hdfs dfs -du -s /data  # Summary

# Copy within HDFS
hdfs dfs -cp /data/file.txt /data/backup/

# Move within HDFS
hdfs dfs -mv /data/file.txt /data/archive/
```

### Admin Operations

```bash
# Check filesystem health
hdfs fsck / -files -blocks -locations

# Report cluster status
hdfs dfsadmin -report

# Refresh nodes (after config changes)
hdfs dfsadmin -refreshNodes

# Enter safe mode
hdfs dfsadmin -safemode enter
hdfs dfsadmin -safemode leave

# Balancer
hdfs balancer -threshold 10  # 10% threshold

# Upgrade filesystem
hdfs dfsadmin -upgrade

# Rollback upgrade
hdfs dfsadmin -rollback
```

### Debug and Maintenance

```bash
# Check block integrity
hdfs fsck /path/to/file -list-corruptfileblocks

# Fetch file block info
hdfs fsck /path/to/file -files -blocks

# View NameNode logs
tail -f /var/log/hadoop/hdfs/hadoop-hdfs-namenode-*.log

# Check edit logs
hdfs oev -i edits_0000000000000000001-0000000000000000100 -o edits.xml

# Check fsimage
hdfs oiv -i fsimage_0000000000000000100 -o fsimage.txt -p Delimited
```

---

## Best Practices

### File and Directory Design

```bash
# Good: Large files, few directories
/data/year=2024/month=01/day=15/event_log.parquet

# Bad: Many small files
/data/2024/01/15/event_001.log
/data/2024/01/15/event_002.log
/data/2024/01/15/event_003.log
# ... millions of files
```

### Small Files Problem

| Metric | Impact |
|--------|--------|
| 1 block (128 MB) | Optimal |
| 10,000 × 128 KB blocks | 10,000 block entries in NameNode |
| NameNode memory | ~150 bytes per block entry |
| 100 million blocks | ~15 GB heap required |

### Solutions for Small Files

```bash
# 1. Use Hadoop Archives (HAR)
hadoop archive -name myarchive.har -p /small/files /archive/path

# 2. Use SequenceFile
# Write multiple small files into one SequenceFile

# 3. Use CombineFileInputFormat
# Combines small files during MapReduce processing

# 4. Use HBase for random access
# Store small files as HBase cells

# 5. Compact during ingestion
# Merge small files in streaming pipelines
```

### Performance Tuning

```bash
# Increase DataNode handler count for busy clusters
dfs.datanode.handler.count = 20

# Enable short-circuit reads
dfs.client.read.shortcircuit = true
dfs.domain.socket.path = /var/lib/hadoop-hdfs/dn_socket

# Configure compression
io.compression.codecs = org.apache.hadoop.io.compress.GzipCodec

# Use append mode for streaming writes
dfs.support.append = true

# Tune block size for workload
dfs.blocksize = 268435456  # 256 MB for large files
```

### Security Configuration

```xml
<!-- Enable Kerberos authentication -->
<property>
    <name>dfs.block.access.token.lifetime</name>
    <value>600000</value>  <!-- 10 minutes -->
</property>

<!-- Enable encryption zones -->
<property>
    <name>dfs.encryption.key.provider.uri</name>
    <value>kms://http@kms-host:16000/kms</value>
</property>

<!-- Enable audit logging -->
<property>
    <name>dfs.namenode.audit.loggers</name>
    <value>DEFAULT</value>
</property>
```

### Monitoring Metrics

| Metric | Description | Warning Threshold |
|--------|-------------|-------------------|
| `UnderReplicatedBlocks` | Blocks below replication target | > 0 |
| `MissingBlocks` | Blocks with no replicas | > 0 |
| `PendingReplicationBlocks` | Blocks being replicated | > 1000 |
| `DecommissionedNodes` | Nodes being decommissioned | > 0 |
| `CorruptBlocks` | Corrupted blocks | > 0 |
| `NameNodeMemoryUsed` | NameNode heap usage | > 80% |

---

## Troubleshooting

### Common Issues

**1. NameNode won't start**
```bash
# Check edit log corruption
hdfs oev -i edits_0000000000000000001 -o edits.xml

# Recovery options
hdfs namenode -recover
hdfs namenode -importCheckpoint
```

**2. DataNode not connecting**
```bash
# Check firewall
netstat -an | grep 50010

# Check DataNode logs
tail -100 /var/log/hadoop/hdfs/hadoop-hdfs-datanode-*.log

# Verify cluster ID matches
cat /data/hadoop/datanode/current/VERSION
```

**3. Slow write performance**
```bash
# Check DataNode disk usage
hdfs dfsadmin -report

# Run balancer
hdfs balancer -threshold 5

# Check network throughput
iperf -c datanode-host
```

**4. High NameNode memory**
```bash
# Check block count
hdfs dfsadmin -report | grep "Total blocks"

# Consider federation
# Or increase heap size
```

---

## HDFS Commands Reference

### Quick Reference

| Command | Description |
|---------|-------------|
| `hdfs dfs -ls` | List files |
| `hdfs dfs -put` | Upload files |
| `hdfs dfs -get` | Download files |
| `hdfs dfs -cat` | View file contents |
| `hdfs dfs -rm` | Delete file |
| `hdfs dfs -mkdir` | Create directory |
| `hdfs dfs -du` | Check disk usage |
| `hdfs dfs -chmod` | Change permissions |
| `hdfs dfsadmin -report` | Cluster report |
| `hdfs fsck` | Filesystem check |
| `hdfs balancer` | Balance data |
| `hdfs dfsadmin -refreshNodes` | Refresh nodes |

### Useful Aliases

```bash
alias hls='hdfs dfs -ls'
alias hput='hdfs dfs -put'
alias hget='hdfs dfs -get'
alias hcat='hdfs dfs -cat'
alias hrm='hdfs dfs -rm'
alias hdu='hdfs dfs -du -h'
alias hreport='hdfs dfsadmin -report'
```
