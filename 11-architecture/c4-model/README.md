# C4 Model

## Overview

The C4 Model is a hierarchical approach to software architecture documentation using four levels of abstraction: Context, Container, Component, and Code. It provides a simple way to communicate architecture to different audiences.

## Four Levels

### Level 1: System Context

Shows the system and its relationships with users and external systems.

```
┌─────────────────────────────────────────┐
│           System Context                │
│                                         │
│  ┌─────────┐    ┌─────────────────┐    │
│  │  User   │───▶│  Your System    │    │
│  └─────────┘    └─────────────────┘    │
│                      │                  │
│                 ┌────▼────┐            │
│                 │External │            │
│                 │System   │            │
│                 └─────────┘            │
└─────────────────────────────────────────┘
```

**Purpose**: Understand the big picture, who uses the system, what it connects to.

### Level 2: Container

Shows the high-level technology choices and how containers communicate.

```
┌─────────────────────────────────────────┐
│            Containers                  │
│                                         │
│  ┌──────────┐  ┌──────────────────┐   │
│  │  Web App │─▶│   API Server     │   │
│  │ (React)  │  │   (Spring Boot)  │   │
│  └──────────┘  └──────────────────┘   │
│                       │                │
│              ┌────────▼────────┐       │
│              │    Database     │       │
│              │   (PostgreSQL)  │       │
│              └─────────────────┘       │
└─────────────────────────────────────────┘
```

**Purpose**: Technology decisions, deployment topology, communication patterns.

### Level 3: Component

Shows the building blocks within a container.

```
┌─────────────────────────────────────────┐
│            Components (API)            │
│                                         │
│  ┌───────────┐  ┌───────────────────┐ │
│  │Controller │─▶│    Service        │ │
│  └───────────┘  └───────────────────┘ │
│                       │                │
│              ┌────────▼────────┐       │
│              │   Repository    │       │
│              └─────────────────┘       │
└─────────────────────────────────────────┘
```

**Purpose**: Understanding responsibilities, dependencies, and design patterns.

### Level 4: Code

Optional UML-like diagram showing class relationships.

```
┌─────────────────────────────────────────┐
│              Code                      │
│                                         │
│  ┌────────────┐     ┌────────────┐    │
│  │ UserService│────▶│UserRepository│  │
│  └────────────┘     └────────────┘    │
│        │                    │          │
│  ┌─────▼──────┐     ┌──────▼─────┐   │
│  │User        │     │User entity │   │
│  └────────────┘     └────────────┘   │
└─────────────────────────────────────────┘
```

**Purpose**: Detailed design for developers, onboarding.

## Tools

| Tool | Description |
|------|-------------|
| **Structurizr** | DSL-based C4 modeling |
| **PlantUML** | C4 templates available |
| **Mermaid** | Simple diagram syntax |
| **Miro** | Collaborative C4 diagrams |
| **Structurizr Lite** | Free on-premise option |

## PlantUML Example

```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4/master/C4_Context.puml

Person(user, "User", "Uses the system")
System(system, "E-Commerce System", "Sells products online")
System_Ext(payment, "Payment Gateway", "Processes payments")
System_Ext(email, "Email Service", "Sends notifications")

Rel(user, system, "Uses")
Rel(system, payment, "Processes payments")
Rel(system, email, "Sends emails")
@enduml
```

## When to Use Each Level

| Level | Audience | When to Use |
|-------|----------|-------------|
| Context | Everyone | Always start here |
| Container | Technical stakeholders | For technology decisions |
| Component | Developers | For design discussions |
| Code | Developers | For complex implementations |

## Benefits

1. **Hierarchical** - Drill down as needed
2. **Audience-aware** - Different levels for different people
3. **Simple** - Easy to understand and create
4. **Tool-agnostic** - Works with any diagramming tool
5. **Living documentation** - Update as architecture evolves

## Best Practices

1. **Start at Context** - Always begin with Level 1
2. **Keep it simple** - Don't over-complicate diagrams
3. **Use consistent notation** - Stick to C4 conventions
4. **Include legends** - Explain colors and shapes
5. **Link to code** - Reference actual repositories
6. **Review regularly** - Keep diagrams up to date

## Key Takeaways

- C4 provides four levels of architecture abstraction
- Context → Container → Component → Code hierarchy
- Different levels for different audiences
- Use tools like Structurizr or PlantUML
- Start simple, add detail as needed
