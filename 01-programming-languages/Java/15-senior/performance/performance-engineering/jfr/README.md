# JFR (Java Flight Recorder)

## What JFR Is

Java Flight Recorder (JFR) is a low-overhead, production-safe profiling and diagnostic tool built into the JVM. It records events from the JVM and application code with minimal performance impact (typically <1-2% overhead).

JFR continuously collects data about CPU usage, memory allocation, GC activity, I/O, threads, and more. It is always available in modern JDKs (JDK 11+ is free for production use).

## How to Enable JFR

### Command Line

```bash
# Start recording immediately
java -XX:StartFlightRecording=duration=60s,filename=recording.jfr MyApp

# Continuous recording (dump on demand)
java -XX:StartFlightRecording=filename=recording.jfr,duration=0 MyApp

# With specific settings
java -XX:StartFlightRecording=duration=60s,filename=recording.jfr,\
    settings=profile,\
    maxsize=100m,\
    dumponexit=true MyApp
```

### Runtime Control (jcmd)

```bash
# Start recording
jcmd <pid> JFR.start settings=profile duration=60s filename=recording.jfr

# Dump running recording
jcmd <pid> JFR.dump filename=dump.jfr

# Stop recording
jcmd <pid> JFR.stop
```

### Programmatic (JDK 11+)

```java
Recording recording = new Recording();
recording.enable(CPULoad.class);
recording.enable(GarbageCollection.class);
recording.start();

// ... run code ...

recording.dump(Paths.get("recording.jfr"));
recording.stop();
```

## Key JFR Events

### CPULoad

Reports overall CPU utilization broken down by:
- JVM (user + system time)
- Machine total CPU usage
- Machine idle CPU

**Use case**: Detecting CPU bottlenecks and understanding JVM CPU consumption relative to the system.

### GCHeapStatistics

Provides heap memory statistics at each GC cycle:
- Heap usage before and after GC
- Object allocation rate
- GC pause durations and frequency

**Use case**: Identifying memory pressure, tuning heap sizes, detecting memory leaks.

### MethodProfiling (Execution Sample)

Records which methods are executing on each thread at sample intervals. Provides CPU hotspots:

**Use case**: Finding the hottest methods consuming the most CPU time. Essential for CPU-bound performance analysis.

### Other Important Events

| Event | Description |
|-------|-------------|
| `ThreadStart` / `ThreadEnd` | Thread lifecycle |
| `SocketRead` / `SocketWrite` | Network I/O |
| `FileRead` / `FileWrite` | File I/O |
| `SynchronizeStart` / `SynchronizeEnd` | Lock contention |
| `ObjectAllocationInNewTLAB` | TLAB allocations |
| `OldObjectSample` | Memory leak candidates |
| `WallClockSample` | Wall-clock profiling |

## JFR Analysis with JDK Mission Control (JMC)

JDK Mission Control is the GUI tool for analyzing JFR recordings:

### Opening a Recording

1. Open JMC → File → Open File → select `.jfr` file
2. The Overview page shows key metrics at a glance

### Key Views

- **JVM Information**: JVM version, configuration, system details
- **Event Browser**: Raw event data with filtering
- **Hot Methods**: CPU profiling results (Method Profiling events)
- **Locks**: Contention analysis
- **IO**: File and socket I/O breakdown
- **GC**: Heap usage charts and GC pause analysis
- **Method Profiling**: Flame graphs and call trees

### Automated Analysis

JMC includes an automated analysis engine that highlights:
- Long GC pauses
- High CPU usage
- Excessive lock contention
- I/O bottlenecks
- Memory leaks (via Old Object Sample)

## When to Use JFR in Production

### Use JFR when:

1. **Diagnosing performance regressions**: Enable JFR during normal operation and compare recordings before/after a regression
2. **Memory leak investigation**: Use Old Object Sample events to track allocation paths
3. **GC tuning**: Analyze GCHeapStatistics to optimize heap sizes and GC parameters
4. **Production profiling**: JFR's <1-2% overhead makes it safe for continuous production use
5. **Incident response**: Enable JFR during a production incident to capture diagnostic data

### When NOT to use JFR:

1. **Real-time monitoring**: JFR is a recording tool, not a live dashboard (use Grafana/Prometheus for that)
2. **High-frequency sampling**: For nanosecond-level precision, use JMH microbenchmarks
3. **Distributed tracing**: Use OpenTelemetry or Zipkin for cross-service tracing

## Best Practices

- Use `settings=profile` for detailed profiling (more overhead)
- Use `settings=default` for continuous production recording (minimal overhead)
- Set `maxsize` to prevent unbounded disk usage
- Use `dumponexit=true` to capture data even on crashes
- Rotate JFR files in production using `maxage` parameter
- Combine with `-XX:+FlightRecorder` (default on in JDK 11+)

## References

- [JFR Documentation](https://docs.oracle.com/en/java/javase/17/jfapi/)
- [JDK Mission Control](https://openjdk.org/projects/jmc/)
- [JFR Events Catalog](https://bestconf.java.net/events/events.html)
