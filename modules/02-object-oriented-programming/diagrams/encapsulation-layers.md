# Encapsulation and Access Modifiers

Encapsulation hides internal implementation details and exposes only necessary functionality through access control.

## Access Modifier Visibility

```mermaid
graph TB
    subgraph "Same Class"
        SC[private<br/>Accessible within class only]
    end
    
    subgraph "Same Package"
        SP["default (package-private)<br/>Accessible within package"]
    end
    
    subgraph "Subclasses"
        PT[protected<br/>Accessible to subclasses]
    end
    
    subgraph "Everywhere"
        PB[public<br/>Accessible from anywhere]
    end
    
    SC --> SP --> PT --> PB
    
    classDef private fill:#ff6b6b,color:#fff
    classDef default fill:#feca57,color:#000
    classDef protected fill:#48dbfb,color:#000
    classDef public fill:#0abde3,color:#fff
    
    class SC private
    class SP default
    class PT protected
    class PB public
```

## Access Modifier Comparison Table

```mermaid
graph TB
    subgraph "Visibility Levels"
        direction LR
        T["Modifier | Class | Package | Subclass | World"]
        P["private | ✓ | ✗ | ✗ | ✗"]
        D["default | ✓ | ✓ | ✗ | ✗"]
        PT["protected | ✓ | ✓ | ✓ | ✗"]
        PB["public | ✓ | ✓ | ✓ | ✓"]
    end
    
    style T fill:#333,color:#fff
    style P fill:#ff6b6b,color:#fff
    style D fill:#feca57,color:#000
    style PT fill:#48dbfb,color:#000
    style PB fill:#0abde3,color:#fff
```

## Encapsulation Example

```mermaid
classDiagram
    class BankAccount {
        -String accountId
        -double balance
        -List~Transaction~ transactions
        +double getBalance()
        +void deposit(double amount)
        +boolean withdraw(double amount)
        +List~Transaction~ getTransactions()
        -void addTransaction()
        -boolean validateAmount()
    }
    
    class Transaction {
        -String id
        -Date date
        -double amount
        -String type
        +String getId()
        +Date getDate()
    }
    
    BankAccount --> Transaction
```

## Getter/Setter Pattern

```mermaid
flowchart TB
    A[Client Code] --> B[Public Getter]
    A --> C[Public Setter]
    
    B --> D[Private Field]
    C --> D
    
    subgraph "Validation"
        C --> E{Validate input}
        E -->|Valid| F[Update field]
        E -->|Invalid| G[Throw exception]
    end
    
    subgraph "Access Control"
        D --> H[Read only]
        D --> I[Write with validation]
    end
```

## Builder Pattern for Encapsulation

```mermaid
sequenceDiagram
    participant Client
    participant Builder
    participant Product
    
    Client->>Builder: new Builder()
    Client->>Builder: .setName("John")
    Client->>Builder: .setAge(30)
    Client->>Builder: .build()
    Builder->>Product: new Product()
    Product-->>Client: Return immutable object
```

## Immutable Object Pattern

```mermaid
classDiagram
    class ImmutablePerson {
        -final String name
        -final int age
        -final List~String~ hobbies
        +ImmutablePerson()
        +String getName()
        +int getAge()
        +List~String~ getHobbies()
        +ImmutablePerson withName()
        +ImmutablePerson withAge()
    }
    
    note for ImmutablePerson "All fields are final<br/>No setters<br/>Defensive copying<br/>Thread-safe"
```

## Key Takeaways

- **private**: Most restrictive, class-only access
- **default**: Package-level access, no modifier needed
- **protected**: Available to subclasses and same package
- **public**: Least restrictive, global access
- **Encapsulation Benefits**: Data hiding, validation, flexibility, maintainability