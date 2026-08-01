# Garbage Collection Process

Java's garbage collector automatically manages memory by identifying and reclaiming objects that are no longer referenced.

## GC Memory Generations

```mermaid
graph TB
    subgraph "Heap Memory"
        subgraph "Young Generation"
            Eden[Eden Space<br/>New objects allocated here]
            S0[Survivor 0<br/>From Space]
            S1[Survivor 1<br/>To Space]
        end
        
        subgraph "Old Generation"
            Tenured[Tenured Space<br/>Long-lived objects]
        end
        
        subgraph "Permanent Generation"
            Perm[PermGen/Metaspace<br/>Class metadata]
        end
    end
    
    Eden --> S0
    S0 --> S1
    S1 --> Tenured
    Tenured --> Perm
    
    classDef young fill:#90EE90,color:#000
    classDef old fill:#FFB6C1,color:#000
    classDef perm fill:#ADD8E6,color:#000
    
    class Eden,S0,S1 young
    class Tenured old
    class Perm perm
```

## Minor GC Process

```mermaid
flowchart TD
    A[Minor GC Triggered] --> B[Stop-the-World]
    B --> C[Mark Eden + S0]
    C --> D[Copy live objects to S1]
    D --> E[Clear Eden + S0]
    E --> F[Swap S0 and S1]
    F --> G[Resume execution]
    
    subgraph "Eden Space"
        E1[Object A]
        E2[Object B]
        E3[Object C]
    end
    
    subgraph "S0 (From)"
        S01[Object D]
        S02[Object E]
    end
    
    subgraph "S1 (To)"
        S11[Object A]
        S12[Object D]
    end
    
    style A fill:#90EE90
    style B fill:#FFB6C1
    style C fill:#ADD8E6
```

## Major GC Process

```mermaid
flowchart TB
    A[Major GC Triggered] --> B[Mark Phase]
    B --> C[Sweep Phase]
    C --> D[Compact Phase]
    D --> E[Update References]
    
    subgraph "Mark Phase"
        M1[Start from GC Roots]
        M2[Mark reachable objects]
        M3[Mark unreachable objects]
    end
    
    subgraph "Sweep Phase"
        S1[Scan heap]
        S2[Free unmarked objects]
        S3[Create free list]
    end
    
    subgraph "Compact Phase"
        C1[Move live objects]
        C2[Fill gaps]
        C3[Update pointers]
    end
    
    B --> M1 --> M2 --> M3
    C --> S1 --> S2 --> S3
    D --> C1 --> C2 --> C3
```

## GC Roots Identification

```mermaid
graph TB
    subgraph "GC Roots"
        root1[Static Variables]
        root2[Local Variables]
        root3[JNI References]
        root4[Active Threads]
        root5[Monitor Locks]
    end
    
    subgraph "Reachable Objects"
        obj1[Object A]
        obj2[Object B]
        obj3[Object C]
        obj4[Object D]
    end
    
    subgraph "Unreachable Objects"
        obj5[Object E]
        obj6[Object F]
    end
    
    root1 --> obj1
    root2 --> obj2
    obj2 --> obj3
    obj3 --> obj4
    
    obj5 -.->|No path| obj6
```

## GC Algorithms Comparison

```mermaid
graph TB
    subgraph "Serial GC"
        Serial[Single-threaded<br/>Stop-the-world<br/>Good for small apps]
    end
    
    subgraph "Parallel GC"
        Parallel[Multiple threads<br/>Throughput-oriented<br/>Default on server]
    end
    
    subgraph "G1 GC"
        G1[Region-based<br/>Low-latency<br/>Balanced approach]
    end
    
    subgraph "ZGC"
        ZGC[Ultra-low latency<br/>Concurrent processing<br/>Large heaps]
    end
    
    Serial --> Parallel --> G1 --> ZGC
```

## Object Promotion Flow

```mermaid
stateDiagram-v2
    [*] --> Young: new object
    
    Young --> Young: Minor GC survived
    Young --> Old: Survived N cycles
    Old --> Old: Major GC survived
    
    Young --> [*]: Collected
    Old --> [*]: Collected
    
    note right of Young
        Eden Space
        Survivor Spaces
    end note
    
    note right of Old
        Tenured Space
        Long-lived objects
    end note
```

## Key Takeaways

- **Minor GC**: Collects young generation, short pause times
- **Major GC**: Collects entire heap, longer pause times
- **GC Roots**: Starting points for reachability analysis
- **Generational Hypothesis**: Most objects die young
- **Tuning Options**: `-Xmx`, `-Xms`, `-XX:+UseG1GC`