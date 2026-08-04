# AsyncProfiler

## Overview

AsyncProfiler is a low-overhead sampling profiler for Java that uses async-profiler for both CPU and allocation profiling without safepoint bias.

## Features

- CPU profiling using perf_events and AsyncGetCallTrace
- Wall-clock profiling
- Memory allocation profiling
- Lock contention profiling
- Native memory profiling
- Flame graph generation

## Usage

### Command Line
```bash
# CPU profiling
./profiler.sh -d 30 -f profile.html <pid>

# Allocation profiling
./profiler.sh -e alloc -d 30 -f alloc.html <pid>

# Wall-clock profiling
./profiler.sh -e wall -d 30 -f wall.html <pid>

# Lock profiling
./profiler.sh -e lock -d 30 -f lock.html <pid>
```

### Java API
```java
import one.profiler.AsyncProfiler;
import one.profiler.Events;

AsyncProfiler profiler = AsyncProfiler.getInstance();

// Start CPU profiling
profiler.start(Events.CPU, 10000000); // 10ms interval

// ... business logic ...

profiler.stop();

// Dump to file
profiler.dump("/tmp/profile.html");
```

### Spring Boot Integration
```java
@Component
public class ProfilerBean implements DisposableBean {
    private final AsyncProfiler profiler;
    
    public ProfilerBean() {
        this.profiler = AsyncProfiler.getInstance();
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void startProfiling() {
        profiler.start(Events.CPU, 10000000);
    }
    
    @Override
    public void destroy() {
        profiler.stop();
        profiler.dump("/tmp/app-profile.html");
    }
}
```

## Flame Graph Analysis

### Reading Flame Graphs
- X-axis: Percentage of samples
- Y-axis: Call stack depth
- Width: Number of samples (wider = more time)
- Color: Meaningless (for aesthetics only)

### Hot Spots
```bash
# Find hottest methods
./profiler.sh -d 30 -o flat <pid>

# Output shows self time per method
  samples  percent  self  name
    12345   45.2%  45.2%  com.example.Service.process
     5678   20.8%  20.8%  com.example.Repository.findById
```

## Best Practices

1. Profile in production-like environment
2. Use appropriate profiling duration
3. Analyze both CPU and allocation profiles
4. Compare profiles before/after changes
5. Use flame graphs for visualization
6. Profile during representative workloads
7. Set appropriate sampling intervals
8. Combine with APM tools
