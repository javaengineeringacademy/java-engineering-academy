# OOP Concepts Diagrams

## Four Pillars of OOP

```mermaid
mindmap
  root((OOP))
    Encapsulation
      Data Hiding
      Getters/Setters
      Validation
      Read-Only/Write-Only
    Inheritance
      IS-A Relationship
      Code Reuse
      Method Overriding
      Constructor Chaining
    Polymorphism
      Method Overloading
      Method Overriding
      Dynamic Dispatch
      Type Flexibility
    Abstraction
      Abstract Classes
      Interfaces
      Hide Complexity
      Show Essentials
```

## Object Lifecycle

```mermaid
stateDiagram-v2
    [*] --> NotCreated : Class Defined
    NotCreated --> Created : new keyword
    Created --> InUse : Constructor completes
    InUse --> InUse : Methods called
    InUse --> EligibleForGC : No references
    EligibleForGC --> Collected : GC runs
    Collected --> [*]
    
    state Created {
        [*] --> ConstructorChaining
        ConstructorChaining --> FieldInitialization
        FieldInitialization --> ObjectReady
    }
```

## Memory Allocation

```mermaid
flowchart TD
    A[Java Application] --> B[Memory Types]
    
    B --> C[Stack Memory]
    B --> D[Heap Memory]
    
    C --> C1[Method Calls]
    C --> C2[Local Variables]
    C --> C3[Primitive Types]
    C --> C4[Object References]
    
    D --> D1[Objects]
    D --> D2[Instance Variables]
    D --> D3[Arrays]
    D --> D4[String Pool]
    
    C1 --> E[Fast Access]
    C2 --> E
    C3 --> E
    C4 --> E
    
    D1 --> F[Dynamic Allocation]
    D2 --> F
    D3 --> F
    D4 --> F
    
    F --> G[Garbage Collection]
    G --> G1[Mark Phase]
    G --> G2[Sweep Phase]
    G --> G3[Compact Phase]
```

## Constructor Chaining

```mermaid
sequenceDiagram
    participant Main
    participant Puppy
    participant Dog
    participant Animal
    participant Object
    
    Main->>Puppy: new Puppy("Max", "Lab", 3)
    Puppy->>Dog: super("Max", "Lab")
    Dog->>Animal: super("Max")
    Animal->>Object: super()
    Object-->>Animal: Object created
    Animal-->>Dog: Animal fields initialized
    Dog-->>Puppy: Dog fields initialized
    Puppy-->>Main: Puppy created
    
    Note over Main,Object: Constructor chain: Puppy → Dog → Animal → Object
```

## Method Overloading vs Overriding

```mermaid
flowchart LR
    A[Method Polymorphism] --> B[Overloading]
    A --> C[Overriding]
    
    B --> B1[Same Name]
    B --> B2[Different Parameters]
    B --> B3[Compile-Time]
    B --> B4[Same Class]
    
    C --> C1[Same Signature]
    C --> C2[Subclass]
    C --> C3[Runtime]
    C --> C4["@Override"]
    
    B1 --> D[Multiple Methods]
    B2 --> D
    B3 --> D
    B4 --> D
    
    C1 --> E[Dynamic Dispatch]
    C2 --> E
    C3 --> E
    C4 --> E
```

## Interface vs Abstract Class

```mermaid
flowchart TD
    A[Abstraction Mechanisms] --> B[Interface]
    A --> C[Abstract Class]
    
    B --> B1[No State]
    B --> B2[No Constructors]
    B --> B3[Multiple Inheritance]
    B --> B4[All Public Methods]
    B --> B5[Default Methods]
    B --> B6[Static Methods]
    
    C --> C1[Has State]
    C --> C2[Has Constructors]
    C --> C3[Single Inheritance]
    C --> C4[Mixed Access]
    C --> C5[Template Methods]
    C --> C6[Shared Code]
    
    B1 --> D[CAN-DO Relationship]
    B2 --> D
    B3 --> D
    B4 --> D
    
    C1 --> E[IS-A Relationship]
    C2 --> E
    C3 --> E
    C4 --> E
```

## Equals/HashCode Contract

```mermaid
flowchart TD
    A[Equals/HashCode Contract] --> B[Reflexive]
    A --> C[Symmetric]
    A --> D[Transitive]
    A --> E[Consistent]
    A --> F[Non-Null]
    
    B --> B1[a.equals a = true]
    C --> C1[a.equals b = b.equals a]
    D --> D1[a=b and b=c → a=c]
    E --> E1[Same result if unchanged]
    F --> F1[a.equals null = false]
    
    G[Implementation Rules] --> H[Use same fields]
    G --> I[Override both methods]
    G --> J[Use Objects utility]
    G --> K[Override in records]
    
    H --> L[Consistent Behavior]
    I --> L
    J --> L
    K --> L
```