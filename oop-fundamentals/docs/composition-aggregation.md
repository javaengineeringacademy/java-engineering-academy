# Composition and Aggregation

## Association Types

### Association (General)
Objects know about each other.

```java
class Student {
    private List<Course> courses = new ArrayList<>();
}
```

### Aggregation (Weak "Has-A")
- Whole can exist without parts
- Parts can belong to multiple wholes

```java
class Department {
    private List<Employee> employees = new ArrayList<>();  // Aggregation
}
```

### Composition (Strong "Has-A")
- Part cannot exist without whole
- Lifecycle tied to whole

```java
class House {
    private final List<Room> rooms = new ArrayList<>();  // Composition

    public House() {
        rooms.add(new Room("Kitchen"));
        rooms.add(new Room("Bedroom"));
    }
}
```

## Comparison

| Aspect | Composition | Aggregation |
|--------|-------------|-------------|
| Ownership | Strong | Weak |
| Lifecycle | Tied to whole | Independent |
| Multiplicity | Single owner | Multiple owners |
| Example | House-Room | Department-Employee |

## IS-A vs HAS-A

### IS-A (Inheritance)
```java
class Dog extends Animal { }  // Dog IS-A Animal
```

### HAS-A (Composition/Aggregation)
```java
class Car {
    private Engine engine;  // Car HAS-A Engine (Composition)
    private List<Passenger> passengers;  // Car HAS-A Passengers (Aggregation)
}
```

## Dependency

```java
class EmailService {
    public void sendEmail(String to, String msg) { ... }
}

class UserService {
    private final EmailService emailService;  // Dependency

    public UserService(EmailService emailService) {
        this.emailService = emailService;
    }
}
```

## Design Principles

| Principle | Guideline |
|-----------|-----------|
| **Favor composition** | Over inheritance |
| **Prefer interfaces** | For loose coupling |
| **Dependency inversion** | Depend on abstractions |
| **Single responsibility** | One reason to change |

## UML Notation

| Relationship | Symbol |
|--------------|--------|
| Inheritance | `▷` (open arrow) |
| Composition | `◆──` (filled diamond) |
| Aggregation | `◇──` (empty diamond) |
| Dependency | `─▷` (dashed arrow) |
| Association | `──` (solid line) |

## Decision Guide

| Question | Composition | Aggregation | Inheritance |
|----------|-------------|-------------|-------------|
| Can part exist alone? | No | Yes | N/A |
| Lifecycle tied? | Yes | No | N/A |
| "Is-a" relationship? | No | No | Yes |
| Multiple owners? | No | Yes | N/A |