# OOP Relationships: Association, Aggregation, Composition, Dependency

Understanding object relationships is crucial for designing well-structured Java applications.

## Relationship Types Overview

```mermaid
graph TB
    subgraph "Strong Relationship"
        Comp[Composition<br/>Strong ownership<br/>Lifecycle dependent]
        Agg[Aggregation<br/>Weak ownership<br/>Independent lifecycle]
    end
    
    subgraph "Weak Relationship"
        Assoc[Association<br/>Relationship exists<br/>Independent objects]
        Dep[Dependency<br/>Temporary usage<br/>Method parameter]
    end
    
    Comp --> Agg --> Assoc --> Dep
    
    classDef strong fill:#ff6b6b,color:#fff
    classDef weak fill:#4ecdc4,color:#fff
    
    class Comp,Agg strong
    class Assoc,Dep weak
```

## Association

```mermaid
classDiagram
    class Teacher {
        +String name
        +List~Student~ students
        +teach()
    }
    
    class Student {
        +String name
        +Teacher advisor
        +study()
    }
    
    Teacher "1" --> "0..*" Student : advises
    Student "0..*" --> "1" Teacher : advised by
    
    note for Teacher "Independent lifecycle<br/>Can exist without students"
    note for Student "Independent lifecycle<br/>Can exist without teacher"
```

## Aggregation (Has-A Relationship)

```mermaid
classDiagram
    class Department {
        +String name
        +List~Professor~ professors
        +addProfessor()
    }
    
    class Professor {
        +String name
        +String specialization
        +teach()
    }
    
    Department o-- "0..*" Professor : has
    
    note for Department "Professor can exist<br/>without Department"
    note for Department "Weak ownership<br/>Lifecycle independent"
```

## Composition (Contains-A Relationship)

```mermaid
classDiagram
    class Car {
        +String model
        +Engine engine
        +start()
    }
    
    class Engine {
        +int horsepower
        +start()
        +stop()
    }
    
    class Wheel {
        +int size
        +String type
        +rotate()
    }
    
    class Body {
        +String color
        +String material
    }
    
    Car *-- "1" Engine : contains
    Car *-- "4" Wheel : contains
    Car *-- "1" Body : contains
    
    note for Car "Engine lifecycle depends on Car<br/>If Car destroyed, Engine destroyed"
    note for Engine "Created with Car<br/>Destroyed with Car"
```

## Dependency

```mermaid
classDiagram
    class OrderProcessor {
        +processOrder()
    }
    
    class Order {
        +String orderId
        +List~Item~ items
    }
    
    class PaymentService {
        +processPayment()
    }
    
    class NotificationService {
        +sendConfirmation()
    }
    
    OrderProcessor ..> Order : uses
    OrderProcessor ..> PaymentService : uses
    OrderProcessor ..> NotificationService : uses
    
    note for OrderProcessor "Temporary usage<br/>Passed as parameter<br/>No ownership"
```

## Relationship Strength Comparison

```mermaid
graph TB
    subgraph "1. Composition (Strongest)"
        C1[Car] --> C2[Engine]
        C2 -.->|Created with Car| C1
        C2 -.->|Destroyed with Car| C1
    end
    
    subgraph "2. Aggregation"
        A1[Department] --> A2[Professor]
        A2 -.->|Can exist without Dept| A1
    end
    
    subgraph "3. Association"
        B1[Teacher] --> B2[Student]
        B2 -.->|Independent| B1
    end
    
    subgraph "4. Dependency (Weakest)"
        D1[Method] -.->|Temporary| D2[Parameter]
    end
```

## Real-World Example: University System

```mermaid
classDiagram
    class University {
        +String name
        +List~Department~ departments
        +addDepartment()
    }
    
    class Department {
        +String name
        +List~Course~ courses
    }
    
    class Course {
        +String title
        +int credits
    }
    
    class Professor {
        +String name
        +String specialization
    }
    
    class Student {
        +String name
        +List~Course~ enrolledCourses
    }
    
    University *-- "1..*" Department : composition
    Department *-- "0..*" Course : composition
    Department o-- "1..*" Professor : aggregation
    Student "0..*" --> "0..*" Course : association
    Professor "1" --> "0..*" Course : association
    
    note for University "University owns Departments"
    note for Department "Departments own Courses"
    note for Professor "Professors can exist independently"
    note for Student "Students enroll in Courses"
```

## Key Takeaways

- **Composition**: Strong ownership, lifecycle dependent (Car-Engine)
- **Aggregation**: Weak ownership, independent lifecycle (Department-Professor)
- **Association**: Relationship exists, independent objects (Teacher-Student)
- **Dependency**: Temporary usage, method parameters (OrderProcessor-Payment)
- **Design Principle**: Prefer composition over inheritance