# JVM Architecture: Runtime Data Areas

The Java Virtual Machine (JVM) divides memory into several distinct runtime data areas, each serving a specific purpose during program execution.

## JVM Runtime Data Areas

```mermaid
graph TB
    subgraph "JVM Runtime Data Areas"
        subgraph "Thread Private"
            PC[PC Register<br/>Stores current bytecode instruction address]
            Stack[Method Stack<br/>Stores frames for each method call]
            Native[Native Method Stack<br/>For native method invocations]
        end
        
        subgraph "Thread Shared"
            Heap[Heap Memory<br/>Stores all objects and arrays]
            MethodArea[Method Area<br/>Stores class structures, metadata]
        end
    end
    
    PC --> Stack
    Stack --> Heap
    MethodArea --> Heap
    Native --> Stack
```

## Memory Allocation Details

```mermaid
graph LR
    subgraph "Heap Memory"
        YoungGen[Young Generation<br/>Eden + Survivor Spaces]
        OldGen[Old Generation<br/>Tenured Space]
    end
    
    subgraph "Stack Memory"
        Frame1[Stack Frame 1<br/>Local Variables]
        Frame2[Stack Frame 2<br/>Operand Stack]
        Frame3[Stack Frame 3<br/>Frame Data]
    end
    
    YoungGen --> OldGen
    Frame1 --> Frame2 --> Frame3
```

## How Components Interact

```mermaid
sequenceDiagram
    participant Main
    participant PC as PC Register
    participant Stack as Method Stack
    participant Heap as Heap Memory
    participant Method as Method Area
    
    Main->>PC: Execute instruction
    PC->>Stack: Push new frame
    Stack->>Heap: Create object
    Heap->>Method: Load class data
    Method-->>Stack: Return type info
    Stack-->>PC: Pop frame
```

## Key Takeaways

- **Heap** is shared across all threads and stores objects
- **Stack** is thread-private and stores method execution state
- **PC Register** tracks the current executing instruction
- **Method Area** stores class metadata and constants
- **Native Method Stack** handles JNI method invocations