# 08. Profiling Tools Internals Deep Dive

## Profiling Architecture

### How Profilers Work

```
Profiler Attachment:
├── JVMTI Agent: JVM Tool Interface agent loaded at startup
├── Attach API: Runtime attachment via com.sun.tools.attach
├── JMX Connection: Management and monitoring via MXBeans
└── Command Line: Start with profiling flags

Data Collection:
├── CPU: Sample call stacks periodically (10-100ms intervals)
├── Memory: Track allocations via TLAB sampling or bytecode instrumentation
├── Thread: Monitor thread states via JVMTI
└── I/O: Intercept I/O operations via bytecode instrumentation

Data Processing:
├── Aggregate samples into call trees
├── Calculate time percentages per method
├── Build flame graphs from stack traces
└── Generate allocation histograms

Visualization:
├── Call tree view (top-down/bottom-up)
├── Flame graph view (interactive)
├── Allocation hotspots view
└── Thread state timeline
```

### Tool Comparison

```
VisualVM:
├── Type: GUI application
├── Connection: Local/Remote (JMX)
├── Profiling: Sampling + Instrumentation
├── Strengths: Visual interface, heap dumps
├── Weaknesses: Higher overhead, not for production
└── Best for: Development profiling

JProfiler:
├── Type: Commercial GUI + CLI
├── Connection: Agent-based
├── Profiling: Sampling + Instrumentation
├── Strengths: Rich visualizations, low overhead
├── Weaknesses: Commercial license required
└── Best for: Professional development

async-profiler:
├── Type: Command-line tool
├── Connection: Attach API or agent
├── Profiling: Sampling (async, no safepoint bias)
├── Strengths: Lowest overhead, flame graphs
├── Weaknesses: Limited GUI (uses FlameGraph tool)
└── Best for: Production profiling

JFR (Java Flight Recorder):
├── Type: Built-in JVM framework
├── Connection: JMX or command line
├── Profiling: Event-based (configurable detail)
├── Strengths: Built-in, lowest overhead, continuous
├── Weaknesses: Complex configuration
└── Best for: Production continuous profiling
```

### Sampling Profiling Internals

```
Sampling Process:
1. Start sampling thread (periodic timer)
2. On each tick (e.g., every 10ms):
   a. For each application thread:
      b. Walk the stack (using JVMTI or perf_events)
      c. Record frame addresses
      d. Store in thread-local buffer
3. Aggregate all samples into global data
4. Generate call tree from aggregated stacks

async-profiler Advantage:
├── Uses perf_events (Linux) or monotonic clock (macOS)
├── Walks stacks at the OS level (no safepoint needed)
├── No stop-the-world for stack walking
└── Captures stacks even in native code
```

### Instrumentation Profiling Internals

```
Instrumentation Process:
1. Attach JVMTI agent to JVM
2. Redefine target methods (ClassFileTransformer)
3. Add timing code at method entry/exit:
   a. Record entry timestamp
   b. Record exit timestamp
   c. Calculate duration
   d. Store in profiling data structure
4. Restore original bytecode when profiling stops

Overhead Sources:
├── Method entry/exit overhead: ~10-50ns per call
├── Timestamp reading: ~5-20ns per read
├── Data structure updates: ~10-50ns per update
└── Total: 25-120ns per method call
```

### JFR Internals

```
JFR Event System:
├── Event Classes: Define what to record
│   ├──jdk.ExecutionSample: CPU profiling
│   ├──jdk.AllocationInNewTLAB: Allocation profiling
│   ├──jdk.ThreadContention: Lock profiling
│   └──jdk.GarbageCollection: GC profiling
├── Event Streaming: Real-time event processing
├── Recording: Persist events to .jfr file
└── Analysis: JDK Mission Control reads .jfr files

JFR Configuration:
├── Default: Balanced overhead and detail
├── Profile: Higher detail, higher overhead
├── Continuous: Low detail, minimal overhead
└── Custom: XML configuration file
```
