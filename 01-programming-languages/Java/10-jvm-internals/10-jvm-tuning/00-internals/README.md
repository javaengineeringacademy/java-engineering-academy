# 10. JVM Tuning Internals Deep Dive

## Tuning Process

### Methodology

```
1. Establish Baseline
   ├── Measure current performance
   ├── Document resource usage
   └── Identify bottlenecks

2. Set Goals
   ├── Define performance targets (latency, throughput)
   ├── Set resource constraints (CPU, memory)
   └── Establish success criteria

3. Analyze Workload
   ├── Profile application behavior
   ├── Identify hot paths
   └── Measure object lifecycle

4. Tune Parameters
   ├── Adjust heap sizing
   ├── Configure GC algorithm
   └── Optimize compilation

5. Measure Results
   ├── Compare with baseline
   ├── Validate goals met
   └── Document improvements

6. Iterate
   ├── Fine-tune based on results
   ├── Test under different loads
   └── Document final configuration
```

### Key JVM Flags

```
Heap Sizing:
├── -Xms: Initial heap size
├── -Xmx: Maximum heap size
├── -Xmn: Young generation size
├── -XX:NewRatio: Old:Young ratio
└── -XX:SurvivorRatio: Eden:Survivor ratio

GC Algorithm:
├── -XX:+UseSerialGC: Serial collector
├── -XX:+UseParallelGC: Parallel collector
├── -XX:+UseG1GC: G1 collector (default)
├── -XX:+UseZGC: ZGC collector
└── -XX:+UseShenandoahGC: Shenandoah collector

GC Tuning:
├── -XX:MaxGCPauseMillis: Target pause time
├── -XX:GCTimeRatio: GC time ratio
├── -XX:InitiatingHeapOccupancyPercent: IHOP
├── -XX:MaxTenuringThreshold: Promotion threshold
└── -XX:ParallelGCThreads: GC thread count

JIT Compilation:
├── -XX:+TieredCompilation: Enable tiered (default)
├── -XX:CompileThreshold: Invocations before compile
├── -XX:ReservedCodeCacheSize: Code cache size
└── -XX:+PrintCompilation: Log compilation events

Logging:
├── -Xlog:gc*:file=gc.log:time,uptime,level,tags
├── -XX:+PrintCompilation
└── -XX:+UnlockDiagnosticVMOptions
```

### Container-Specific Tuning

```
Docker/Kubernetes:
├── -XX:MaxRAMPercentage=75.0: Use % of container memory
├── -XX:InitialRAMPercentage=75.0: Initial heap as % of container
├── -XX:+UseContainerSupport: Enable (default in Java 10+)
├── -XX:ActiveProcessorCount=<n>: Override CPU detection
└── -XX:+UseStringDeduplication: Reduce String memory (G1)

Example for 4GB container:
java -XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport -jar app.jar
```
