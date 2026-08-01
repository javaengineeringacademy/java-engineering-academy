# Class Loading Process

The JVM loads, links, and initializes classes on demand, which is fundamental to Java's runtime behavior.

## Class Loading Overview

```mermaid
flowchart TB
    subgraph "Class Loading"
        A[1. Loading] --> B[2. Linking]
        B --> C[3. Initialization]
    end
    
    subgraph "Loading Phase"
        A1[Find .class file]
        A2[Read bytecode]
        A3[Create Class object]
    end
    
    subgraph "Linking Phase"
        B1[Verification]
        B2[Preparation]
        B3[Resolution]
    end
    
    subgraph "Initialization Phase"
        C1[Execute static blocks]
        C2[Initialize static fields]
        C3[Run static initializers]
    end
    
    A --> A1 --> A2 --> A3
    B --> B1 --> B2 --> B3
    C --> C1 --> C2 --> C3
```

## Class Loaders Hierarchy

```mermaid
graph TB
    subgraph "Bootstrap ClassLoader"
        BL[rt.jar<br/>java.lang.*<br/>java.util.*]
    end
    
    subgraph "Extension ClassLoader"
        EL[ext/*.jar<br/>javax.*]
    end
    
    subgraph "Application ClassLoader"
        AL[classpath<br/>User classes]
    end
    
    subgraph "Custom ClassLoader"
        CL[Plugin classes<br/>Dynamic loading]
    end
    
    BL --> EL --> AL --> CL
    
    classDef bootstrap fill:#ff6b6b,color:#fff
    classDef extension fill:#4ecdc4,color:#fff
    classDef app fill:#45b7d1,color:#fff
    classDef custom fill:#96ceb4,color:#fff
    
    class BL bootstrap
    class EL extension
    class AL app
    class CL custom
```

## Linking Process Details

```mermaid
sequenceDiagram
    participant Loader as ClassLoader
    participant Verifier as BytecodeVerifier
    participant Prep as Preparer
    participant Resolver as Resolver
    
    Loader->>Verifier: Load .class bytes
    Note over Verifier: Check bytecode validity
    Verifier->>Prep: Verification passed
    Note over Prep: Allocate memory for static fields
    Prep->>Resolver: Preparation complete
    Note over Resolver: Convert symbolic references
    Resolver-->>Loader: Linking complete
```

## Delegation Model

```mermaid
sequenceDiagram
    participant App as Application Code
    participant AppCL as App ClassLoader
    participant ExtCL as Ext ClassLoader
    participant BootCL as Bootstrap ClassLoader
    
    App->>AppCL: Load class
    AppCL->>ExtCL: Delegate to parent
    ExtCL->>BootCL: Delegate to parent
    
    alt Class found in parent
        BootCL-->>ExtCL: Return class
        ExtCL-->>AppCL: Return class
        AppCL-->>App: Return class
    else Class not found in parent
        BootCL-->>ExtCL: Not found
        ExtCL-->>AppCL: Not found
        AppCL->>AppCL: Load class itself
        AppCL-->>App: Return class
    end
```

## Custom ClassLoader Example

```mermaid
classDiagram
    class ClassLoader {
        <<abstract>>
        +findClass()
        +loadClass()
        +defineClass()
    }
    
    class MyClassLoader {
        +String classPath
        +findClass()
        +loadClassData()
    }
    
    class NetworkClassLoader {
        +String baseUrl
        +findClass()
        +downloadClass()
    }
    
    ClassLoader <|-- MyClassLoader
    ClassLoader <|-- NetworkClassLoader
```

## Key Takeaways

- **Bootstrap ClassLoader**: Loads core Java classes (rt.jar)
- **Extension ClassLoader**: Loads extension classes (ext/)
- **Application ClassLoader**: Loads user classes from classpath
- **Delegation Model**: Parents try loading first, then children
- **Lazy Loading**: Classes loaded only when first referenced