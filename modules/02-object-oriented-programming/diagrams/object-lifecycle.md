# Object Lifecycle: Creation to Garbage Collection

Objects in Java go through a complete lifecycle from creation to eventual garbage collection.

## Object Lifecycle Overview

```mermaid
stateDiagram-v2
    [*] --> Created: new keyword
    Created --> Initialized: Constructor
    Initialized --> InUse: Assignment
    InUse --> InUse: Method calls
    InUse --> EligibleForGC: No references
    EligibleForGC --> Collected: GC runs
    Collected --> [*]
```

## Detailed Object Creation Process

```mermermaid
flowchart TD
    A[1. Class Loading] --> B[2. Memory Allocation]
    B --> C[3. Default Initialization]
    C --> D[4. Constructor Execution]
    D --> E[5. Object Ready]
    
    subgraph "Class Loading"
        A1[Load .class file]
        A2[Verify bytecode]
        A3[Prepare static fields]
    end
    
    subgraph "Memory Allocation"
        B1[Check heap space]
        B2[Allocate memory]
        B3[Set header bits]
    end
    
    subgraph "Initialization"
        D1[Call super constructor]
        D2[Initialize instance variables]
        D3[Execute constructor body]
    end
    
    A --> A1 --> A2 --> A3
    B --> B1 --> B2 --> B3
    D --> D1 --> D2 --> D3
```

## Reference Counting and Reachability

```mermaid
graph TB
    subgraph "GC Roots"
        root1[Static variables]
        root2[Local variables]
        root3[JNI references]
        root4[Active threads]
    end
    
    subgraph "Reachable Objects"
        obj1[Object A]
        obj2[Object B]
        obj3[Object C]
    end
    
    subgraph "Unreachable Objects"
        obj4[Object D]
        obj5[Object E]
    end
    
    root1 --> obj1
    root2 --> obj2
    obj2 --> obj3
    
    obj4 -.->|no reference| obj5
```

## Garbage Collection Phases

```mermaid
graph LR
    subgraph "Mark Phase"
        M1[Identify all reachable objects]
        M2[Mark reachable objects]
    end
    
    subgraph "Sweep Phase"
        S1[Scan heap memory]
        S2[Free unmarked objects]
    end
    
    subgraph "Compact Phase"
        C1[Move live objects]
        C2[Update references]
    end
    
    M1 --> M2 --> S1 --> S2 --> C1 --> C2
```

## Example Object Lifecycle

```mermaid
sequenceDiagram
    participant Code
    participant Heap
    participant GC
    
    Code->>Heap: new Person("Alice")
    Note over Heap: Object created
    
    Code->>Heap: person.setName("Bob")
    Note over Heap: Object modified
    
    Code->>Heap: person = null
    Note over Heap: Object eligible for GC
    
    GC->>Heap: Mark phase
    GC->>Heap: Sweep phase
    Note over Heap: Object collected
```

## Key Takeaways

- Objects are created with the `new` keyword
- Constructor initializes the object state
- Objects become eligible for GC when no references exist
- GC runs automatically (you can suggest with `System.gc()`)
- Objects may have `finalize()` method called before collection