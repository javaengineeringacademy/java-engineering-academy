# Java Memory Model: Heap vs Stack

Understanding how Java allocates memory for primitives and objects is fundamental to writing efficient applications.

## Memory Allocation Overview

```mermaid
graph TB
    subgraph "Stack Memory - Thread Private"
        subgraph "Primitive Variables"
            int[int x = 10]
            double[double y = 3.14]
            boolean[boolean flag = true]
        end
        
        subgraph "Object References"
            ref1[Person p]
            ref2[String s]
        end
    end
    
    subgraph "Heap Memory - Shared"
        subgraph "Objects"
            obj1[Person Object<br/>name: "John"<br/>age: 30]
            obj2[String Object<br/>value: "Hello"]
        end
        
        subgraph "Arrays"
            arr1[int[] arr<br/>[1, 2, 3, 4, 5]]
        end
    end
    
    ref1 --> obj1
    ref2 --> obj2
```

## Reference vs Object Storage

```mermaid
classDiagram
    class StackFrame {
        +Local variables
        +Object references
        +Primitive values
    }
    
    class HeapMemory {
        +Objects
        +Arrays
        +Class instances
    }
    
    StackFrame --> HeapMemory : references
```

## Memory Allocation Flow

```mermaid
flowchart TD
    A[Declaration] --> B{Type?}
    B -->|Primitive| C[Allocate on Stack]
    B -->|Object| D[Allocate on Heap]
    
    C --> E[Store value directly]
    D --> F[Store reference on Stack]
    D --> G[Store object data on Heap]
    
    E --> H[Memory freed when scope ends]
    F --> I[Reference freed when scope ends]
    G --> J[Object freed by Garbage Collector]
```

## Example Code Visualization

```mermaid
graph LR
    subgraph "Code"
        code["Person p = new Person();<br/>int x = 42;"]
    end
    
    subgraph "Stack"
        stackRef["p → reference"]
        stackPrim["x = 42"]
    end
    
    subgraph "Heap"
        heapObj["Person Object<br/>name: null<br/>age: 0"]
    end
    
    code --> stackRef
    code --> stackPrim
    stackRef --> heapObj
```

## Key Takeaways

- **Primitives** are stored directly on the stack (faster access)
- **Object references** are stored on the stack, but objects live on the heap
- **Stack memory** is automatically freed when method exits
- **Heap memory** is managed by the Garbage Collector
- **Local variables** have the fastest access time