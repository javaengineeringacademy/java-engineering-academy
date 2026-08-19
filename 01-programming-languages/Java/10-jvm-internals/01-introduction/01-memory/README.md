# 01. JVM Memory Overview

## Memory Areas in the JVM

### Heap Memory (Shared)

The heap is where all objects are allocated:

```
┌─────────────────────────────────────────────┐
│                   Heap                      │
│  ┌───────────────────────────────────────┐  │
│  │        Young Generation               │  │
│  │  ┌──────────┐ ┌───────┐ ┌───────┐   │  │
│  │  │  Eden    │ │  S0   │ │  S1   │   │  │
│  │  │  (80%)   │ │ (10%) │ │ (10%) │   │  │
│  │  └──────────┘ └───────┘ └───────┘   │  │
│  └───────────────────────────────────────┘  │
│  ┌───────────────────────────────────────┐  │
│  │        Old Generation                 │  │
│  │     (Long-lived objects)              │  │
│  └───────────────────────────────────────┘  │
└─────────────────────────────────────────────┘
```

### Stack Memory (Per Thread)

Each thread has its own stack containing stack frames:

```
Thread Stack
├── Frame 1: methodC()
│   ├── Local variables: x=30
│   └── Operand stack: [ ]
├── Frame 2: methodB()
│   ├── Local variables: y=20
│   └── Operand stack: [methodC result]
└── Frame 3: methodA()
    ├── Local variables: z=10
    └── Operand stack: [methodB result]
```

### Metaspace (Shared, Off-Heap)

Stores class metadata, replacing PermGen from Java 8+:
- Class definitions
- Method bytecodes
- Constant pools
- Field/method descriptors

## Object Allocation Flow

1. New object created → allocated in Eden (via TLAB)
2. Eden fills up → Minor GC (copy surviving to Survivor)
3. Survives multiple Minor GCs → promoted to Old Generation
4. Old Generation fills up → Major/Full GC

## Key Configuration Flags

```bash
# Heap sizing
-Xms512m          # Initial heap size
-Xmx2g            # Maximum heap size
-Xmn256m          # Young generation size

# Metaspace
-XX:MetaspaceSize=256m
-XX:MaxMetaspaceSize=512m
```
