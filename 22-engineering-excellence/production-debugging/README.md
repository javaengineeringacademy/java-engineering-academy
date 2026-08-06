# Production Debugging

Debugging production issues without停机 (downtime) is a critical skill for senior developers.

## Thread Dump Analysis

Thread dumps capture the state of all threads at a point in time.

### Capturing Thread Dumps
```bash
# Using jstack
jstack <pid> > thread_dump.txt

# Using jcmd
jcmd <pid> Thread.print > thread_dump.txt

# Using kill signal (Linux/Mac)
kill -3 <pid>

# Using jvisualvm
# Connect to process -> Threads tab -> Thread Dump button
```

### Reading Thread Dump
```
"main" #1 prio=5 os_prio=0 tid=0x00007f8b1c008800 nid=0x1 runnable [0x00007f8b1d7fb000]
   java.lang.Thread.State: RUNNABLE
        at java.net.PlainSocketImpl.socketAccept(Native Method)
        at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:450)
        at java.net.ServerSocket.implAccept(ServerSocket.java:544)
        at java.net.ServerSocket.accept(ServerSocket.java:512)
```

**Key fields:**
- **Thread name**: "main"
- **Thread state**: RUNNABLE, WAITING, TIMED_WAITING, BLOCKED, TERMINATED
- **Stack trace**: Current execution point

### Common Thread States
| State | Meaning |
|-------|---------|
| RUNNABLE | Executing or ready to execute |
| BLOCKED | Waiting for monitor lock |
| WAITING | Waiting indefinitely (wait(), join()) |
| TIMED_WAITING | Waiting with timeout (sleep(), wait(timeout)) |

### Thread Dump Analysis Tools
- **FastThread**: Online analysis
- **IBM Thread Analyzer**: Detailed analysis
- **jstack filtering**: `jstack <pid> | grep -A 20 "BLOCKED"`

## Heap Dump Analysis

Heap dumps capture the entire heap at a point in time.

### Capturing Heap Dumps
```bash
# Using jmap
jmap -dump:live,format=b,file=heap.bin <pid>

# Using jcmd
jcmd <pid> GC.heap_dump heap.bin

# Using jvisualvm
# Connect to process ->右键 -> Heap Dump

# On OOM (add to JVM flags)
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/path/to/dumps
```

### Analyzing Heap Dumps
```bash
# Using Eclipse MAT
mat heap.bin

# Using jhat (deprecated)
jhat heap.bin
# Then open http://localhost:7000
```

### Common Issues
1. **Memory Leaks**: Objects not being garbage collected
2. **High Memory Usage**: Too many objects in memory
3. **GC Overhead**: Too much time spent in GC

### MAT Key Features
- **Dominator Tree**: Shows objects retaining most memory
- **Leak Suspects**: Automatic leak detection
- **Histogram**: Object count by class
- **OQL**: SQL-like queries for heap objects

## GC Log Analysis

GC logs provide insight into garbage collection behavior.

### Enabling GC Logs
```bash
# Java 8
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc.log

# Java 9+
-Xlog:gc*:file=gc.log:time,uptime,level,tags

# Unified logging
-Xlog:gc*=debug:file=gc.log:time,uptime,level,tags
```

### GC Log Analysis Tools
- **GCEasy**: Online GC log analyzer
- **GCViewer**: Visual GC log viewer
- **HPjmeter**: HP's GC analysis tool

### Key Metrics
- **GC Pause Time**: Time application is stopped
- **GC Throughput**: Percentage of time not in GC
- **Allocation Rate**: Objects allocated per time
- **Promotion Rate**: Objects promoted to old gen per time

### Example GC Log
```
[2024-01-15T10:30:15.123+0000][GC (Allocation Failure) [PSYoungGen: 20480K->4096K(23552K)] 20480K->8192K(76800K), 0.0023456 secs]
```

## Network Debugging

### Tools
```bash
# Netstat
netstat -tulpn | grep <port>

# ss (faster)
ss -tulpn | grep <port>

# tcpdump
tcpdump -i eth0 port 8080

# curl for HTTP debugging
curl -v http://localhost:8080/api

# Wireshark for packet analysis
```

### Common Network Issues
1. **Connection refused**: Service not listening
2. **Connection timeout**: Network or firewall issue
3. **DNS resolution**: DNS not configured
4. **Socket leak**: Too many open connections

### Java Network Debugging
```java
// Enable socket debugging
-Djava.net.debug=true

// SSL debugging
-Djavax.net.debug=ssl,handshake

// Connection timeout
-Dsun.net.client.defaultConnectTimeout=5000
-Dsun.net.client.defaultReadTimeout=30000
```

## CPU Profiling

### Tools
- **async-profiler**: Low-overhead CPU profiler
- **JProfiler**: Commercial profiler
- **YourKit**: Commercial profiler
- **jcmd**: Built-in JVM tool

### async-profiler Usage
```bash
# CPU profiling
./profiler.sh -d 30 -f cpu_profile.html <pid>

# Wall-clock profiling
./profiler.sh -d 30 -e wall -f wall_profile.html <pid>
```

### Common CPU Issues
1. **Infinite loops**: High CPU, no progress
2. **Excessive GC**: GC threads consuming CPU
3. **Lock contention**: Threads waiting for locks
4. **Algorithm inefficiency**: O(n²) instead of O(n log n)

## Memory Profiling

### Tools
- **jmap**: Heap dump
- **jhat**: Heap analysis
- **VisualVM**: Memory profiling
- **MAT**: Heap dump analysis

### Memory Profiling Steps
1. **Baseline**: Measure normal memory usage
2. **Monitor**: Watch for memory growth
3. **Capture**: Get heap dump at high memory
4. **Analyze**: Find largest objects
5. **Fix**: Address memory issues

### Memory Leak Detection
```java
// Add to JVM flags for OOM
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/dumps

// Enable GC logging
-Xlog:gc*:file=gc.log
```

### Common Memory Patterns
1. **Cache without eviction**: Unbounded caches
2. **Session accumulation**: Session objects not cleaned
3. **Collection growth**: Collections that never shrink
4. **Thread-local leaks**: ThreadLocal not removed
5. **Classloader leaks**: Dynamic class loading without unloading
