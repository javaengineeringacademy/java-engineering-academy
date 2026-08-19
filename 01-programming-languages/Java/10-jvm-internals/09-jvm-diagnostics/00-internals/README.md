# 09. JVM Diagnostics Internals Deep Dive

## Diagnostic Tools Architecture

### Thread Dump Collection

```
Thread Dump Process:
1. Signal Handler
   ├── JVM receives SIGQUIT (Unix) or Ctrl+Break (Windows)
   ├── Signal handler invoked
   └── Thread stack traces collected

2. Stack Trace Collection
   ├── For each thread:
   │   ├── Current stack frame
   │   ├── Method execution points
   │   ├── Lock information
   │   └── Thread state
   └── Thread scheduling information

3. Lock Information
   ├── Owned locks
   ├── Waiting locks
   ├── Blocked locks
   └── Deadlock detection

4. Output Generation
   ├── Format thread information
   ├── Add thread headers
   └── Write to output
```

### Heap Dump Collection

```
Heap Dump Process:
1. Trigger Heap Dump
   ├── Signal (SIGQUIT)
   ├── jmap command
   ├── JMX API
   └── OutOfMemoryError

2. Stop-the-World Pause
   ├── Pause all threads
   ├── Ensure consistent state
   └── Minimal pause for diagnostics

3. Heap Traversal
   ├── Start from GC roots
   ├── Follow all references
   ├── Record all objects
   └── Calculate retained sizes

4. File Writing
   ├── Write object data
   ├── Write class information
   ├── Write reference information
   └── Write GC root information
```

### jcmd Internals

```
jcmd Commands:
├── VM.flags: Show JVM flags
├── VM.system_properties: Show system properties
├── GC.heap_info: Show heap information
├── GC.heap_dump: Capture heap dump
├── Thread.print: Print thread dump
├── VM.class_loader_stats: Class loader statistics
├── Compiler.codecache: Code cache information
├── JFR.start/stop/dump: Flight recorder control
└── VM.unlock_commercial_features: Unlock commercial features
```

### jstat Internals

```
jstat Statistics:
├── -class: Class loader statistics
│   ├── Loaded: Number of classes loaded
│   ├── Bytes: Number of bytes loaded
│   ├── Unloaded: Number of classes unloaded
│   └── Bytes: Number of bytes unloaded
├── -gc: GC statistics
│   ├── S0C/S1C: Survivor 0/1 capacity
│   ├── S0U/S1U: Survivor 0/1 usage
│   ├── EC: Eden capacity
│   ├── EU: Eden usage
│   ├── OC: Old capacity
│   ├── OU: Old usage
│   ├── MC: Metaspace capacity
│   ├── MU: Metaspace usage
│   ├── YGC/YGCT: Young GC count/time
│   └── FGC/FGCT: Full GC count/time
└── -gcutil: GC statistics (percentages)
```

### Heap Dump File Format

```
HPROF File Structure:
├── Header
│   ├── Version information
│   ├── Timestamp
│   └── Platform information
├── String Records
│   └── All strings in the heap
├── Class Records
│   ├── Class definitions
│   └── Static fields
├── Instance Records
│   ├── Object instances
│   └── Field values
├── Array Records
│   ├── Array instances
│   └── Array elements
└── Root Records
    ├── GC roots
    └── Root types
```
