# 08. Profiling Tools - Examples

## Example Files

| File | Description |
|------|-------------|
| `ProfilingDemo.java` | Demonstrates different profiling scenarios (CPU, memory, threads) |

## Running Examples

```bash
# Basic profiling demo
java ProfilingDemo

# With VisualVM
java -Dcom.sun.management.jmxremote ProfilingDemo

# With async-profiler
./profiler.sh -d 30 -f profile.html <pid>

# With JFR
java -XX:StartFlightRecording=duration=30s,filename=recording.jfr ProfilingDemo
```

## Key Concepts Demonstrated

- CPU hotspot identification
- Memory allocation tracking
- Thread contention detection
- Flame graph generation
- JFR event recording
