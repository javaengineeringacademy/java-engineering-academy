# Flame Graph Analysis

## Overview

Flame graphs visualize stack trace profiles, showing where time is spent in code. The x-axis represents sample percentage and y-axis shows call stack depth.

## Types

### CPU Flame Graphs
- Shows where CPU time is spent
- Identifies hot code paths
- Highlights optimization opportunities

### Allocation Flame Graphs
- Shows memory allocation sources
- Identifies allocation hotspots
- Helps reduce GC pressure

### Wall-Clock Flame Graphs
- Shows total time including waits
- Identifies I/O bottlenecks
- Reveals blocking operations

## Generating Flame Graphs

### With AsyncProfiler
```bash
# CPU flame graph
./profiler.sh -d 30 -f cpu-flame.html <pid>

# Allocation flame graph
./profiler.sh -e alloc -d 30 -f alloc-flame.html <pid>

# Convert to collapsed format
./profiler.sh -d 30 -o collapsed <pid> > stacks.txt
```

### With perf + FlameGraph
```bash
# Record with perf
perf record -g -p <pid> -- sleep 30

# Convert to collapsed
perf script | stackcollapse-perf.pl > stacks.txt

# Generate flame graph
flamegraph.pl stacks.txt > flamegraph.svg
```

## Reading Flame Graphs

### Key Indicators
- **Width** - More samples = more time spent
- **Height** - Call stack depth
- **Color** - Meaningless (aesthetic only)
- **Hot path** - Widest towers at top

### Common Patterns

#### CPU Bound
```
[application code] (wide)
  [framework code] (narrower)
    [JVM code] (narrowest)
```

#### I/O Bound
```
[thread wait] (very wide)
  [blocking call] (medium)
    [application code] (narrow)
```

## Analysis Techniques

### Find Hot Methods
```bash
# Collapse stacks
cat stacks.txt | awk '{print $1}' | sort | uniq -c | sort -rn | head -20
```

### Compare Profiles
```bash
# Diff two flame graphs
difffolded.pl before.txt after.txt | flamegraph.pl > diff.svg
```

## Best Practices

1. Profile representative workloads
2. Use sufficient profiling duration
3. Compare before/after optimizations
4. Look for wide frames at top
5. Investigate unexpected hot paths
6. Use allocation graphs for GC tuning
7. Combine with other profiling tools
8. Store profiles for historical comparison
