# Polymorphism and Method Dispatch

Polymorphism allows objects to take many forms, with method dispatch determining which implementation is called at runtime.

## Compile-time vs Runtime Polymorphism

```mermaid
graph TB
    subgraph "Compile-time Polymorphism"
        CT[Method Overloading<br/>Same method name<br/>Different parameters<br/>Resolved at compile time]
    end
    
    subgraph "Runtime Polymorphism"
        RT[Method Overriding<br/>Same method signature<br/>Different implementation<br/>Resolved at runtime]
    end
    
    CT -->|Static Binding| CB[Compiler decides]
    RT -->|Dynamic Binding| VB[Virtual Method Table]
```

## Method Overloading (Static Binding)

```mermaid
classDiagram
    class Calculator {
        +add(int a, int b) int
        +add(double a, double b) double
        +add(int a, int b, int c) int
        +add(String a, String b) String
    }
    
    note for Calculator "Compile-time polymorphism<br/>Different parameter lists<br/>Static binding"
```

## Method Overriding (Dynamic Binding)

```mermaid
classDiagram
    class Animal {
        +makeSound()
    }
    
    class Dog {
        +makeSound()
    }
    
    class Cat {
        +makeSound()
    }
    
    class Bird {
        +makeSound()
    }
    
    Animal <|-- Dog
    Animal <|-- Cat
    Animal <|-- Bird
    
    note for Animal "Base class method"
    note for Dog "Override: Woof!"
    note for Cat "Override: Meow!"
    note for Bird "Override: Tweet!"
```

## Virtual Method Table (vtable)

```mermaid
graph TB
    subgraph "Animal vtable"
        av1[makeSound → Animal.makeSound]
    end
    
    subgraph "Dog vtable"
        dv1[makeSound → Dog.makeSound]
    end
    
    subgraph "Cat vtable"
        cv1[makeSound → Cat.makeSound]
    end
    
    subgraph "Runtime Dispatch"
        animal[Animal ref = new Dog()]
        animal -->|lookup| dv1
    end
    
    av1 -.->|inherited| dv1
    av1 -.->|inherited| cv1
```

## Method Dispatch Flow

```mermaid
sequenceDiagram
    participant Code
    participant Compiler
    participant JVM
    participant VTable
    
    Code->>Compiler: animal.makeSound()
    Note over Compiler: Check type of animal
    
    alt Compile-time check
        Compiler->>Compiler: Verify method exists
        Compiler->>JVM: Generate invokevirtual
    end
    
    JVM->>VTable: Look up method in vtable
    VTable-->>JVM: Return Dog.makeSound()
    JVM->>JVM: Execute method
```

## Dynamic Dispatch Example

```mermaid
flowchart TD
    A[animal.makeSound] --> B{Type of animal?}
    
    B -->|Dog| C[Call Dog.makeSound]
    B -->|Cat| D[Call Cat.makeSound]
    B -->|Bird| E[Call Bird.makeSound]
    
    C --> F[Output: Woof!]
    D --> G[Output: Meow!]
    E --> H[Output: Tweet!]
    
    style A fill:#4ECDC4
    style B fill:#FF6B6B
```

## instanceof and Type Casting

```mermaid
flowchart TB
    A[Object obj] --> B{instanceof check}
    
    B -->|obj instanceof Dog| C[Cast to Dog]
    B -->|obj instanceof Cat| D[Cast to Cat]
    B -->|else| E[Handle other type]
    
    C --> F[Call Dog-specific methods]
    D --> G[Call Cat-specific methods]
    E --> H[Call Animal methods]
```

## Key Takeaways

- **Static Binding**: Resolved at compile time (overloading)
- **Dynamic Binding**: Resolved at runtime (overriding)
- **vtable**: Table of method pointers for each class
- **`instanceof`**: Check object type before casting
- **Performance**: Static binding is slightly faster than dynamic