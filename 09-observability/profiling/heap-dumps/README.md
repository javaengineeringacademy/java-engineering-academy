# Heap Dumps Analysis

## Overview

Heap dumps capture the state of Java heap memory at a point in time, enabling analysis of memory usage, object retention, and leak detection.

## Capturing Heap Dumps

### JMap
```bash
# Live heap dump
jmap -dump:live,format=b,file=heap.hprof <pid>

# All objects
jmap -dump:format=b,file=heap.hprof <pid>
```

### JCMD
```bash
# Heap dump with GC
jcmd <pid> GC.heap_dump /tmp/heap.hprof

# With filename
jcmd <pid> VM.heap_dump /tmp/heap.hprof
```

### JVM Arguments
```bash
# OOM heap dump
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/tmp/heap-dumps/

# On shutdown
-XX:+HeapDumpOnShutdown
```

### Programmatic
```java
// HotSpot API (JDK 9+)
HotSpotDiagnosticMXBean mxBean = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
mxBean.dumpHeap("/tmp/heap.hprof", true);
```

## Analysis Tools

### Eclipse Memory Analyzer (MAT)

#### Key Features
- Leak Suspects Report
- Dominator Tree
- OQL (Object Query Language)
- Thread Overview
- Histogram

#### OQL Queries
```sql
-- Find String objects with length > 1000
SELECT s.toString() FROM java.lang.String s WHERE s.count > 1000

-- Find objects by class
SELECT * FROM com.example.MyClass

-- Find objects with most references
SELECT obj, referrers(obj).size() AS refCount 
FROM java.lang.Object obj 
ORDER BY refCount DESC

-- Find retained size
SELECT s.@retainedHeapSize FROM java.lang.String s
```

### VisualVM
- Heap dump viewer
- Object statistics
- Comparison between dumps

## Memory Leak Patterns

### Common Causes
```java
// 1. Static collections growing unbounded
private static final List<Object> cache = new ArrayList<>();

// 2. unclosed resources
FileInputStream fis = new FileInputStream(file);
// Missing fis.close()

// 3. Inner classes holding references
class Outer {
    Object data;
    class Inner {
        // Holds reference to Outer
    }
}

// 4. ThreadLocal without cleanup
private static final ThreadLocal<Object> context = new ThreadLocal<>();
```

## Analysis Steps

1. Capture heap dump
2. Open in MAT
3. Run Leak Suspects report
4. Examine Dominator Tree
5. Check largest retained objects
6. Trace GC roots
7. Identify leak source

## Best Practices

1. Capture dumps during peak usage
2. Use live dumps to exclude unreachable objects
3. Compare multiple dumps for trends
4. Automate dump capture on OOM
5. Store dumps for historical analysis
6. Use MAT for analysis over VisualVM
7. Set appropriate heap size limits
8. Monitor heap usage proactively
