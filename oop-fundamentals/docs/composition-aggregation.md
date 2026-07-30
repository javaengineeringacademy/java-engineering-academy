# Composition, Aggregation, Association

## Association (General)
Relationship where objects know about each other.

## Aggregation (Weak "Has-A")
- Whole can exist without parts
- Parts can belong to multiple wholes
- **Example**: Department —▶ Employees

```java
class Department {
    private List<Employee> employees = new ArrayList<>();  // Aggregation
}
```

## Composition (Strong "Has-A")
- Part cannot exist without whole
- Lifecycle tied to whole
- **Example**: House —▶ Rooms

```java
class House {
    private final List<Room> rooms = new ArrayList<>();  // Composition

    public House() {
        rooms.add(new Room("Kitchen"));
        rooms.add(new Room("Bedroom"));
    }
}
```

## Decision Guide
| Question | Composition | Aggregation |
|----------|-------------|-------------|
| Can part exist independently? | No | Yes |
| Lifecycle tied to whole? | Yes | No |
| Part belongs to multiple wholes? | No | Yes |