# HDFS (Hadoop Distributed File System)

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Blocks and Replication](#blocks-and-replication)
4. [Rack Awareness](#rack-awareness)
5. [HDFS High Availability](#hdfs-high-availability)
6. [HDFS Federation](#hdfs-federation)
7. [CLI Operations](#cli-operations)
8. [Java API](#java-api)
9. [WebHDFS](#webhdfs)
10. [Performance Tuning](#performance-tuning)
11. [Key Takeaways](#key-takeaways)

---

## Overview

HDFS is a distributed, scalable, and fault-tolerant filesystem designed for storing very large files across clusters of commodity hardware. It is optimized for:
- Large files (100GB+)
- Write-once, read-many access patterns
- Streaming data access
- Running on commodity hardware

### Key Characteristics

| Feature | Description |
|---------|-------------|
| File Size | Optimized for large files (GB to TB) |
| Block Size | 128MB default (configurable) |
| Replication | Default factor of 3 |
| Access Pattern | Write-once, read-many |
| Consistency | Strong consistency for writes |
| Metadata | Stored in NameNode memory |

---

## Architecture

### Components

```
┌─────────────────────────────────────────────────────────┐
│                    Client                                │
│         (HDFS Client, CLI, WebHDFS)                     │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                   NameNode                               │
│         (Master, Metadata Store)                         │
│  ┌─────────────────────────────────────────────────┐    │
│  │  fsimage: Filesystem metadata snapshot          │    │
│  │  editlog: Journal of metadata changes           │    │
│  │  block map: File → Block → DataNode mapping     │    │
│  └─────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │
    ┌────────────────────┼────────────────────┐
    │                    │                    │
┌───▼───┐          ┌───▼───┐          ┌───▼───┐
│DataNode│          │DataNode│          │DataNode│
│ Block  │          │ Block  │          │ Block  │
│Storage │          │Storage │          │Storage │
└───────┘          └───────┘          └───────┘
```

### NameNode

The NameNode manages the filesystem namespace and metadata:

- **File System Tree**: Hierarchical namespace of files and directories
- **Block Map**: Mapping from files to blocks and blocks to DataNodes
- **Edit Log**: Transaction log of all metadata changes
- **FsImage**: Periodic snapshot of filesystem metadata

**Memory Requirements**:
- Each file/block/directory takes ~150 bytes of memory
- 1 billion files/blocks ≈ 300GB RAM
- Limitation on cluster scale

### DataNode

DataNodes store actual data blocks:

- Store blocks on local filesystem
- Send heartbeats to NameNode (every 3 seconds)
- Send block reports (list of all blocks stored)
- Handle read/write requests from clients
- Perform block replication as directed

### Secondary NameNode

**Misnomer**: Not a backup NameNode

**Function**: Checkpoint helper
- Periodically merges editlog with fsimage
- Reduces NameNode startup time
- Does NOT provide failover capability

---

## Blocks and Replication

### Block Structure

```
File: user_data.csv (512MB)
Block Size: 128MB

┌────────────┬────────────┬────────────┬────────────┐
│  Block 0   │  Block 1   │  Block 2   │  Block 3   │
│  0-128MB   │ 128-256MB  │ 256-384MB  │ 384-512MB  │
└────────────┴────────────┴────────────┴────────────┘

Each block is independently replicated across DataNodes
Block Size = 128MB (default, configurable)
```

### Block Metadata

Each block has:
- **Block ID**: Unique identifier (64-bit)
- **Generation Stamp**: Version number for consistency
- **Size**: Actual data size (may be less than block size for last block)

### Replication

**Default Replication Factor**: 3

**Replication Process**:
1. Client writes block to closest DataNode
2. DataNode replicates to second DataNode (different rack)
3. Second DataNode replicates to third DataNode (same rack as second, or different)

**Replication Placement Policy**:
```
Replica 1: Same rack as writer
Replica 2: Different rack
Replica 3: Different node on same rack as Replica 2
```

### Write Pipeline

```
Client → DN1 (closest) → DN2 → DN3
         │              │      │
         └─── Write ────┘──────┘
              Pipeline
```

1. Client establishes pipeline: DN1 → DN2 → DN3
2. Client sends data to DN1
3. DN1 forwards to DN2, DN2 to DN3
4. Acknowledgments flow back: DN3 → DN2 → DN1 → Client
5. All three replicas have the data

### Read Process

```
Client → NameNode: Get block locations
Client ← NameNode: List of DataNodes (closest first)
Client → DN1: Read block
Client ← DN1: Data
```

- Client reads from closest DataNode
- If DN1 fails, tries DN2, then DN3
- Parallel block reads for large files

---

## Rack Awareness

### Why Rack Awareness

- Network bandwidth within rack > between racks
- Reduces network congestion
- Improves fault tolerance (rack failure handling)

### Rack Topology

```
Rack 1:          Rack 2:          Rack 3:
┌─────────┐     ┌─────────┐     ┌─────────┐
│ DN1     │     │ DN3     │     │ DN5     │
│ DN2     │     │ DN4     │     │ DN6     │
└─────────┘     └─────────┘     └─────────┘
    │               │               │
    └───────────────┼───────────────┘
               Switch
```

### Replication Across Racks

Default policy ensures:
- One replica on local rack (fastest)
- One replica on different rack (fault tolerance)
- Third replica on same rack as second (balance)

### Benefits

- **Fault Tolerance**: Survives rack failure
- **Network Optimization**: Local reads preferred
- **Data Locality**: Processing moves to data

---

## HDFS High Availability

### Problem with Single NameNode

- Single point of failure
- Recovery requires manual intervention
- Startup time: minutes to hours for large clusters

### HDFS HA Architecture

```
┌────────────────────────────────────────────────────────┐
│                   NameNode Pair                         │
│  ┌──────────────┐          ┌──────────────┐           │
│  │ Active NN    │          │ Standby NN   │           │
│  │ (Read/Write) │          │ (Read only)  │           │
│  └──────┬───────┘          └──────┬───────┘           │
│         │                         │                    │
│         └─────────┬───────────────┘                    │
│                   │                                    │
│         ┌─────────▼─────────┐                         │
│         │   JournalNodes    │                         │
│         │   (Shared EditLog)│                         │
│         │   (3+ nodes)      │                         │
│         └───────────────────┘                         │
└────────────────────────────────────────────────────────┘
```

### Components

**JournalNodes**:
- Store shared edit log
- Requires odd number (3, 5, etc.) for quorum
- Active NN writes edits, Standby reads them
- Majority must agree for edit to be committed

**ZKFC (ZooKeeper Failover Controller)**:
- Monitors NameNode health via ZooKeeper
- Triggers automatic failover on Active NN failure
- Prevents split-brain scenario

### Failover Process

1. Active NN fails (detected by ZKFC)
2. ZKFC releases lock in ZooKeeper
3. Standby NN's ZKFC acquires lock
4. Standby NN transitions to Active
5. Standby reads latest editlog from JournalNodes
6. Standby serves requests

### Graceful Failover

1. Admin initiates failover via command
2. Active NN enters safe mode
3. Editlog flushed to JournalNodes
4. Standby NN catches up on edits
5. Standby transitions to Active
6. Old Active demoted to Standby

---

## HDFS Federation

### Problem with Single Namespace

- All files/directories managed by one NameNode
- NameNode memory limits cluster scale
- Single namespace limits concurrent users

### Federation Architecture

```
┌────────────────────────────────────────────────────────┐
│              Namespace 1 (NameNode 1)                   │
│              /ns1/*                                     │
├────────────────────────────────────────────────────────┤
│              Namespace 2 (NameNode 2)                   │
│              /ns2/*                                     │
├────────────────────────────────────────────────────────┤
│              Namespace 3 (NameNode 3)                   │
│              /ns3/*                                     │
├────────────────────────────────────────────────────────┤
│              Block Pool Layer                            │
│    All DataNodes shared across namespaces               │
└────────────────────────────────────────────────────────┘
```

### Benefits

- **Scalability**: Each namespace scales independently
- **Isolation**: Namespace failures don't affect others
- **Multi-tenancy**: Different namespaces for different teams
- **Performance**: Reduced NameNode load

### Block Pools

- Each namespace has its own block pool
- Block pool IDs are unique per namespace
- DataNodes store blocks from all namespaces

---

## CLI Operations

### Basic Commands

```bash
# List files in HDFS
hdfs dfs -ls /user/data

# Upload file to HDFS
hdfs dfs -put localfile.txt /user/data/

# Download file from HDFS
hdfs dfs -get /user/data/file.txt ./localfile.txt

# Create directory
hdfs dfs -mkdir -p /user/data/projects

# Remove file
hdfs dfs -rm /user/data/file.txt

# Remove directory recursively
hdfs dfs -rm -r /user/data/old_projects

# Copy files within HDFS
hdfs dfs -cp /user/data/file.txt /user/archive/

# Move files within HDFS
hdfs dfs -mv /user/data/file.txt /user/archive/

# View file contents
hdfs dfs -cat /user/data/file.txt

# View first 1KB of file
hdfs dfs -head /user/data/file.txt
```

### File Operations

```bash
# Check file size
hdfs dfs -du -h /user/data/file.txt

# Count files in directory
hdfs dfs -count /user/data/

# Set replication factor
hdfs dfs -setrep -w 5 /user/data/file.txt

# Change file permissions
hdfs dfs -chmod 755 /user/data/file.txt

# Change file owner
hdfs dfs -chown user:group /user/data/file.txt

# Check disk usage
hdfs dfs -du -s -h /user/

# List all HDFS files with sizes
hdfs dfs -ls -R /user/ | awk '{print $5, $8}'
```

### Administration Commands

```bash
# Check HDFS health
hdfs dfsadmin -report

# Enter safe mode
hdfs dfsadmin -safemode enter

# Leave safe mode
hdfs dfsadmin -safemode leave

# Get HDFS status
hdfs dfsadmin -status

# Refresh nodes (add/remove DataNodes)
hdfs dfsadmin -refreshNodes

# SetBalancer bandwidth
hdfs dfsadmin -setBalancerBandwidth 104857600

# Run HDFS balancer
hdfs balancer -threshold 10
```

### File Check and Fix

```bash
# Check file checksum
hdfs dfs -checksum /user/data/file.txt

# Verify file integrity
hdfs fsck /user/data/ -files -blocks -locations

# Fix corrupted blocks
hdfs fsck /user/data/ -delete

# List missing blocks
hdfs fsck / -list-corruptfileblocks
```

---

## Java API

### Configuration

```java
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

Configuration conf = new Configuration();
conf.set("fs.defaultFS", "hdfs://namenode:9000");
FileSystem fs = FileSystem.get(conf);
```

### File Operations

```java
// Create file
Path path = new Path("/user/data/test.txt");
FSDataOutputStream out = fs.create(path);
out.writeBytes("Hello HDFS\n");
out.close();

// Read file
FSDataInputStream in = fs.open(path);
byte[] buffer = new byte[1024];
int bytesRead = in.read(buffer);
in.close();

// Check if file exists
boolean exists = fs.exists(path);

// Get file status
FileStatus status = fs.getFileStatus(path);
long size = status.getLen();
long modificationTime = status.getModificationTime();

// List directory
FileStatus[] statuses = fs.listStatus(new Path("/user/data"));
for (FileStatus status : statuses) {
    System.out.println(status.getPath());
}
```

### Copy Operations

```java
// Local to HDFS
fs.copyFromLocalFile(new Path("/local/file.txt"), new Path("/hdfs/file.txt"));

// HDFS to Local
fs.copyToLocalFile(new Path("/hdfs/file.txt"), new Path("/local/file.txt"));

// HDFS to HDFS
fs.copy(new Path("/hdfs/source.txt"), new Path("/hdfs/dest.txt"));

// Using FileUtil
FileUtil.copy(fs, new Path("/hdfs/source.txt"), fs, new Path("/hdfs/dest.txt"), false, conf);
```

### Directory Operations

```java
// Create directory
boolean created = fs.mkdirs(new Path("/user/data/projects"));

// Delete file
boolean deleted = fs.delete(new Path("/user/data/old_file.txt"), true); // recursive

// Rename file
boolean renamed = fs.rename(new Path("/old/path"), new Path("/new/path"));
```

### Using PathFilter

```java
// Filter files by pattern
FileStatus[] files = fs.listStatus(new Path("/user/data"),
    path -> path.getName().endsWith(".txt"));

// Or using PathFilter class
public class TextFileFilter implements PathFilter {
    @Override
    public boolean accept(Path path) {
        return path.getName().endsWith(".txt");
    }
}
```

---

## WebHDFS

### REST API

WebHDFS provides a RESTful interface to HDFS:

```bash
# List directory
curl -i "http://namenode:50070/webhdfs/v1/user/data?op=LISTSTATUS"

# Get file status
curl -i "http://namenode:50070/webhdfs/v1/user/data/file.txt?op=GETFILESTATUS"

# Read file
curl -i "http://namenode:50070/webhdfs/v1/user/data/file.txt?op=OPEN"

# Create file
curl -i -X PUT "http://namenode:50070/webhdfs/v1/user/data/file.txt?op=CREATE"

# Append to file
curl -i -X POST "http://namenode:50070/webhdfs/v1/user/data/file.txt?op=APPEND"

# Delete file
curl -i -X DELETE "http://namenode:50070/webhdfs/v1/user/data/file.txt"

# Make directory
curl -i -X PUT "http://namenode:50070/webhdfs/v1/user/data/newdir?op=MKDIRS"

# Rename
curl -i -X PUT "http://namenode:50070/webhdfs/v1/user/data?op=RENAME&destination=/user/data_new"

# Set replication
curl -i -X PUT "http://namenode:50070/webhdfs/v1/user/data/file.txt?op=SETREPLICATION&replication=5"
```

### Operation Flow

1. Client sends request to NameNode
2. NameNode returns temporary DataNode URL
3. Client redirects to DataNode
4. DataNode handles the actual I/O

---

## Performance Tuning

### Block Size Optimization

| File Size | Recommended Block Size |
|-----------|----------------------|
| < 1GB | 64MB |
| 1GB - 10GB | 128MB |
| 10GB - 100GB | 256MB |
| > 100GB | 512MB |

Larger blocks reduce NameNode memory usage but increase seek time.

### Replication Factor

- Default: 3
- Critical data: 5
- Temporary data: 2
- Archives: 1

### NameNode Tuning

```xml
<!-- Increase NameNode heap size -->
<property>
    <name>dfs.namenode.handler.count</name>
    <value>100</value>
</property>

<!-- Increase edit log buffer size -->
<property>
    <name>dfs.namenode.edit.log.deletion.indent</name>
    <value>2</value>
</property>
```

### DataNode Tuning

```xml
<!-- Increase DataNode handler count -->
<property>
    <name>dfs.datanode.handler.count</name>
    <value>10</value>
</property>

<!-- Balance datanode disks -->
<property>
    <name>dfs.datanode.balance.bandwidthPerSec</name>
    <value>104857600</value> <!-- 100MB/s -->
</property>
```

### Client-Side Tuning

```java
// Increase read buffer size
conf.setInt("io.file.buffer.size", 65536); // 64KB

// Enable short circuit reads
conf.setBoolean("dfs.client.read.shortcircuit", true);

// Use locality hints
conf.setBoolean("dfs.client.read.shortcircuit.skip.checksum", false);
```

### Balancer

```bash
# Run balancer with 10% threshold
hdfs balancer -threshold 10

# Run with specific bandwidth
hdfs balancer -threshold 10 -policy datanode

# Monitor balancer progress
hdfs dfsadmin -report | grep "Remaining"
```

### Common Performance Issues

| Issue | Symptom | Solution |
|-------|---------|----------|
| Small files | High NameNode memory, slow listing | HAR files, combine small files |
| Hot DataNode | High load on single node | Balancer, adjust replication |
| Network congestion | Slow shuffle phase | Rack-aware replication |
| NameNode bottleneck | Slow metadata operations | Federation, increase handlers |
| Disk I/O saturation | Slow read/write | Add disks, use SSDs |

---

## Key Takeaways

1. **HDFS is optimized for large files** — avoid small files (use HAR, SequenceFiles, or combine)
2. **NameNode is the bottleneck** — its memory limits cluster scale (HDFS Federation helps)
3. **Replication provides fault tolerance** — default 3 replicas, tune based on criticality
4. **Rack awareness** optimizes network usage and fault tolerance
5. **HDFS HA** eliminates single point of failure using Active/Standby NameNodes
6. **Write pipeline** is sequential (Client → DN1 → DN2 → DN3) — network between racks is the bottleneck
7. **Read is parallel** — client reads from closest DataNode, can read multiple blocks simultaneously
8. **Block size** affects performance — larger blocks reduce metadata overhead but increase seek time
9. **WebHDFS** provides a REST API for HDFS access from non-Java clients
10. **Monitor and balance** — use `hdfs balancer` and `hdfs fsck` regularly

---

## Further Reading

- *Hadoop: The Definitive Guide* by Tom White (Chapters 3-5)
- HDFS Architecture Guide (Apache)
- HDFS High Availability Guide (Apache)
- HDFS Federation Guide (Apache)
