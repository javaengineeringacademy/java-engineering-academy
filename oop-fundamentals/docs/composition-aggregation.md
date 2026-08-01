# Composition and Aggregation: Object Relationships in Java

## 1. Introduction

Composition and aggregation are two forms of the "has-a" relationship in object-oriented programming. They define how objects relate to, own, and manage the lifecycles of other objects. Understanding these distinctions is fundamental to designing maintainable, loosely coupled systems.

## 2. Learning Objectives

- Distinguish between composition, aggregation, association, and dependency
- Understand lifecycle implications of each relationship type
- Apply the correct relationship type to real-world modeling scenarios
- Recognize when to favor composition over inheritance
- Implement UML-correct representations in Java code
- Avoid common pitfalls with object ownership and lifecycle management

## 3. Prerequisites

- Solid understanding of Java classes, objects, and interfaces
- Familiarity with basic inheritance and polymorphism
- Knowledge of Java collections (especially `List`, `Set`, `Map`)
- Understanding of access modifiers and encapsulation

## 4. Why This Concept Exists

Object-oriented systems model real-world entities and their interactions. Real-world objects have complex relationships: a car *has* an engine (which cannot exist without the car), while a department *has* employees (who exist independently). Without composition and aggregation, developers either overuse inheritance (creating rigid hierarchies) or create flat, disconnected classes. These relationship types provide a vocabulary for modeling ownership, lifecycle, and coupling precisely.

## 5. Problem Statement

Consider a naive design:

```java
// Problem: No clear ownership model
class Library {
    Book[] books; // Who creates them? Who destroys them?
    Librarian librarian; // Created externally? Passed in?
}
```

Questions arise:
- If `Library` is destroyed, should `Book` objects also be destroyed?
- Can a `Librarian` work at multiple libraries simultaneously?
- How do we model temporary usage without tight coupling?

Without composition and aggregation, these questions have no structured answers, leading to bugs, memory leaks, and tangled dependencies.

## 6. Theory

### 6.1 Association

Association is the most general relationship. Two objects know about each other but have no ownership semantics.

```java
class Teacher {
    private List<Student> students; // Association
}

class Student {
    private Teacher advisor; // Bidirectional association
}
```

- Neither object controls the other's lifecycle
- Relationship can be unidirectional or bidirectional

### 6.2 Aggregation (Weak Has-A)

Aggregation is a specialized association where one object (the "whole") contains other objects (the "parts"), but parts can exist independently.

- **Lifecycle:** Parts outlive the whole
- **Ownership:** Weak — multiple owners possible
- **Example:** A `Department` has `Employee`s, but if the department closes, employees still exist

### 6.3 Composition (Strong Has-A)

Composition is a strong form of aggregation where the part's lifecycle is tied entirely to the whole.

- **Lifecycle:** Parts are created and destroyed with the whole
- **Ownership:** Strong — single owner
- **Example:** A `House` has `Room`s; destroy the house, destroy the rooms

### 6.4 Dependency

Dependency is the weakest relationship. Object A depends on Object B temporarily (e.g., through a method parameter).

```java
class ReportGenerator {
    public void generate(DataSource source) { // Dependency — temporary
        source.fetch();
    }
}
```

### 6.5 Has-A vs Is-A

| Concept | Relationship | Example | Mechanism |
|---------|-------------|---------|-----------|
| Is-A | Inheritance | Dog IS-A Animal | `extends` |
| Has-A | Composition | Car HAS-A Engine | Field reference |
| Has-A | Aggregation | Team HAS-A Player | Field reference |

## 7. Internal Working

### Composition Internals

When a `House` is constructed, it creates its `Room` objects internally. There is no external way to inject or replace rooms.

```java
class House {
    private final List<Room> rooms;

    public House() {
        // Rooms created here — lifecycle owned by House
        this.rooms = List.of(
            new Room("Kitchen", 15.0),
            new Room("Bedroom", 20.0),
            new Room("Bathroom", 10.0)
        );
    }
}
```

### Aggregation Internals

In aggregation, parts are typically injected or passed in. The containing class stores references but does not create them.

```java
class Department {
    private final List<Employee> employees;

    public Department(List<Employee> employees) {
        // Employees exist before department — lifecycle independent
        this.employees = List.copyOf(employees);
    }
}
```

### Dependency Internals

Dependencies are resolved at method-call time. No long-term reference is held.

```java
class Logger {
    public void log(String message, Formatter formatter) {
        // Formatter used temporarily, not stored
        String formatted = formatter.format(message);
        System.out.println(formatted);
    }
}
```

## 8. JVM Perspective

### Composition

Each `House` instance holds references to `Room` objects on the heap. When `House` becomes unreachable, the `Room` objects become unreachable too (assuming no other references). The GC collects both.

```java
House house = new House(); // House + all Rooms allocated on heap
house = null; // House becomes eligible; Rooms also become unreachable
// GC reclaims all Room objects along with House
```

### Aggregation

`Employee` objects exist on the heap independently of `Department`. If the `Department` reference is removed, `Employee` objects may still be reachable through other references.

```java
Employee emp = new Employee("Alice");
Department dept = new Department(List.of(emp));
dept = null; // Department becomes eligible
// Employee "Alice" still referenced by 'emp' — NOT collected
```

### Dependency

Method parameters are allocated on the stack frame. The referenced objects live on the heap but are not owned.

## 9. Memory Representation

```
COMPOSITION                          AGGREGATION
┌──────────┐                         ┌──────────────┐
│  House    │                         │  Department  │
│──────────│                         │──────────────│
│ rooms ──┼──→ [Room, Room, Room]    │ employees ──┼──→ [Emp1, Emp2]
└──────────┘                         └──────────────┘
                                      ↑         ↑
  House destroyed ⟹ Rooms            Emp1 referenced elsewhere
  become unreachable                  ⟹ Emp1 survives
```

```
DEPENDENCY
┌────────────────┐
│ ReportGenerator │
│────────────────│
│ generate(DataSource src) │  ← src lives on stack, not stored
└────────────────┘
```

## 10. Syntax

### Composition
```java
public final class Engine {
    private final String type;

    public Engine(String type) {
        this.type = Objects.requireNonNull(type);
    }

    public String getType() { return type; }
}

public final class Car {
    private final Engine engine; // Composition — Engine created internally

    public Car() {
        this.engine = new Engine("V6"); // Lifecycle owned by Car
    }
}
```

### Aggregation
```java
public final class Department {
    private final List<Employee> employees; // Aggregation — externally created

    public Department(List<Employee> employees) {
        this.employees = List.copyOf(employees); // Lifecycle independent
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
```

### Association
```java
public class Teacher {
    private Student advisor; // Association — reference held, lifecycle independent
}
```

### Dependency
```java
public class ReportGenerator {
    public void generate(DataStore store) { // Dependency — method parameter
        store.save("Report");
    }
}
```

## 11. Easy Example

```java
public class Main {
    public static void main(String[] args) {
        // Composition: Wheel is part of Bicycle
        Bicycle bike = new Bicycle();
        System.out.println("Bicycle has " + bike.getWheelCount() + " wheels");

        // Aggregation: Player is part of Team
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        Team team = new Team("Thunder", List.of(p1, p2));
        System.out.println("Team: " + team.getName() + ", Players: " + team.getPlayerCount());
    }
}

// Composition
final class Wheel {
    private final double diameter;

    Wheel(double diameter) {
        this.diameter = diameter;
    }

    double getDiameter() { return diameter; }
}

// Composition: Bicycle owns its wheels
final class Bicycle {
    private final List<Wheel> wheels;

    Bicycle() {
        this.wheels = List.of(new Wheel(26.0), new Wheel(26.0));
    }

    int getWheelCount() { return wheels.size(); }
}

// Aggregation
final class Player {
    private final String name;

    Player(String name) {
        this.name = Objects.requireNonNull(name);
    }

    String getName() { return name; }
}

// Aggregation: Team contains Players, but Players exist independently
final class Team {
    private final String name;
    private final List<Player> players;

    Team(String name, List<Player> players) {
        this.name = Objects.requireNonNull(name);
        this.players = List.copyOf(players);
    }

    String getName() { return name; }
    int getPlayerCount() { return players.size(); }
}
```

## 12. Medium Example

```java
public final class Computer {
    private final String brand;
    private final Motherboard motherboard; // Composition
    private final List<UsbDevice> peripherals; // Aggregation

    public Computer(String brand) {
        this.brand = Objects.requireNonNull(brand);
        this.motherboard = new Motherboard("ASUS", 4); // Created internally
        this.peripherals = new ArrayList<>(); // Injected externally
    }

    public void addPeripheral(UsbDevice device) {
        peripherals.add(Objects.requireNonNull(device));
    }

    public Motherboard getMotherboard() { return motherboard; }
    public List<UsbDevice> getPeripherals() { return List.copyOf(peripherals); }
}

public final class Motherboard {
    private final String manufacturer;
    private final int ramSlots;

    public Motherboard(String manufacturer, int ramSlots) {
        this.manufacturer = Objects.requireNonNull(manufacturer);
        this.ramSlots = ramSlots;
    }

    public String getManufacturer() { return manufacturer; }
    public int getRamSlots() { return ramSlots; }
}

public sealed interface UsbDevice permits Keyboard, Mouse, Monitor {
    String getName();
}

public record Keyboard(String getName()) implements UsbDevice {}
public record Mouse(String getName()) implements UsbDevice {}
public record Monitor(String getName()) implements UsbDevice {}
```

```java
public class ComputerDemo {
    public static void main(String[] args) {
        // Composition: Motherboard lifecycle is tied to Computer
        Computer pc = new Computer("Dell");

        // Aggregation: Peripherals exist independently
        Keyboard kb = new Keyboard("Logitech");
        Mouse mouse = new Mouse("Razer");
        pc.addPeripheral(kb);
        pc.addPeripheral(mouse);

        System.out.println("Computer: " + pc);
        System.out.println("Motherboard: " + pc.getMotherboard().getManufacturer());
        System.out.println("Peripherals: " + pc.getPeripherals());

        // If pc is garbage collected, motherboard is also collected
        // But keyboard and mouse survive independently
    }
}
```

## 13. Hard Example

```java
public final class FileSystem {
    private final String rootPath;
    private final Directory root; // Composition — root dir lifecycle tied to FileSystem

    public FileSystem(String rootPath) {
        this.rootPath = Objects.requireNonNull(rootPath);
        this.root = new Directory("root"); // Root created internally
    }

    public Directory getRoot() { return root; }

    public Directory createDirectory(String name, Directory parent) {
        Directory dir = new Directory(name);
        parent.addEntry(dir); // Aggregation — dir can exist independently
        return dir;
    }

    public File createFile(String name, Directory parent, byte[] content) {
        File file = new File(name, content); // Composition — file created for this parent
        parent.addEntry(file);
        return file;
    }

    public void displayStructure(Directory dir, int indent) {
        System.out.println(" ".repeat(indent) + "📁 " + dir.name());
        for (FsEntry entry : dir.entries()) {
            if (entry instanceof File f) {
                System.out.println(" ".repeat(indent + 2) + "📄 " + f.name()
                    + " (" + f.content().length + " bytes)");
            } else if (entry instanceof Directory d) {
                displayStructure(d, indent + 2);
            }
        }
    }
}

public sealed interface FsEntry permits File, Directory {
    String name();
}

public record File(String name, byte[] content) implements FsEntry {}

public final class Directory implements FsEntry {
    private final String name;
    private final List<FsEntry> entries;

    public Directory(String name) {
        this.name = Objects.requireNonNull(name);
        this.entries = new ArrayList<>();
    }

    public String name() { return name; }
    public List<FsEntry> entries() { return List.copyOf(entries); }

    public void addEntry(FsEntry entry) {
        entries.add(Objects.requireNonNull(entry));
    }
}
```

```java
public class FileSystemDemo {
    public static void main(String[] args) {
        FileSystem fs = new FileSystem("/home/user");

        // Build structure
        Directory src = fs.createDirectory("src", fs.getRoot());
        Directory test = fs.createDirectory("test", fs.getRoot());
        fs.createFile("Main.java", src, "class Main {}".getBytes());
        fs.createFile("Test.java", test, "class Test {}".getBytes());

        fs.displayStructure(fs.getRoot(), 0);
    }
}
```

## 14. Enterprise Example

```java
// E-commerce order system with composition and aggregation
public final class Order {
    private final String orderId;
    private final List<OrderLineItem> lineItems; // Composition — items tied to order
    private final Customer customer; // Aggregation — customer exists independently
    private final PaymentMethod paymentMethod; // Aggregation — reusable across orders
    private OrderStatus status;

    public Order(String orderId, Customer customer, PaymentMethod paymentMethod) {
        this.orderId = Objects.requireNonNull(orderId);
        this.customer = Objects.requireNonNull(customer);
        this.paymentMethod = Objects.requireNonNull(paymentMethod);
        this.lineItems = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    public void addItem(Product product, int quantity) {
        lineItems.add(new OrderLineItem(product, quantity)); // Composition
    }

    public Money calculateTotal() {
        return lineItems.stream()
            .map(OrderLineItem::subtotal)
            .reduce(Money.ZERO, Money::add);
    }

    public OrderConfirmation confirm(PaymentGateway gateway) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order not pending");
        }
        PaymentResult result = gateway.charge(paymentMethod, calculateTotal());
        if (result.isSuccess()) {
            this.status = OrderStatus.CONFIRMED;
            return new OrderConfirmation(orderId, customer, calculateTotal());
        }
        throw new PaymentFailedException(result.getMessage());
    }

    // Getters
    public String getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public List<OrderLineItem> getLineItems() { return List.copyOf(lineItems); }
    public OrderStatus getStatus() { return status; }
}

public record OrderLineItem(Product product, int quantity) {
    public Money subtotal() {
        return product.getPrice().multiply(quantity);
    }
}

public final class Customer {
    private final String id;
    private final String name;
    private final String email;
    private final List<Order> orderHistory; // Aggregation — orders belong to customer

    public Customer(String id, String name, String email) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
        this.orderHistory = new ArrayList<>();
    }

    public void addOrder(Order order) {
        orderHistory.add(Objects.requireNonNull(order));
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Order> getOrderHistory() { return List.copyOf(orderHistory); }
}

public interface PaymentMethod {
    String getToken();
}

public interface PaymentGateway {
    PaymentResult charge(PaymentMethod method, Money amount);
}
```

## 15. Performance

| Aspect | Composition | Aggregation | Dependency |
|--------|------------|-------------|------------|
| Object creation | Internal, at whole creation | External, injected | None (method call) |
| Garbage collection | Tied to whole | Independent | Stack frame |
| Reference overhead | Same | Same | Stack only |
| Flexibility | Low (tight coupling) | High (loose coupling) | Highest |
| Testing difficulty | Medium (need to mock internals) | Easy (inject mocks) | Easiest |

**Key insight:** Composition and aggregation have identical runtime performance in Java. The distinction is purely about ownership semantics and design intent.

## 16. Best Practices

1. **Favor composition over inheritance** — "Has-a" is more flexible than "Is-a"
2. **Use `final` for composed objects** — Signals ownership and prevents reassignment
3. **Prefer interfaces for aggregated types** — Enables loose coupling
4. **Use `List.copyOf()` in aggregation** — Prevents external mutation
5. **Make composed fields `final`** — Enforces lifecycle coupling
6. **Use dependency (method parameters) for temporary needs** — Don't store what you only use momentarily
7. **Design for the weakest relationship that satisfies requirements** — Don't over-couple
8. **Use sealed interfaces** — Model restricted type hierarchies cleanly
9. **Document ownership semantics** — Use Javadoc to clarify lifecycle intent

## 17. Common Mistakes

| Mistake | Why It's Wrong | Fix |
|---------|---------------|-----|
| Using inheritance when composition suffices | Tight coupling, fragile base class | Use composition + delegation |
| Exposing mutable internal state | Breaks encapsulation of aggregated objects | Return `List.copyOf()` |
| Not marking composed fields `final` | Lifecycle not enforced | Use `final` keyword |
| Using `==` instead of identity comparison | Reference comparison semantics | Use `Objects.equals()` for logical equality |
| Storing dependencies as fields | Over-couples classes | Use method parameters |
| Ignoring null checks | NPE at runtime | Use `Objects.requireNonNull()` |
| Creating circular compositions | Memory leaks, confusing semantics | Break cycle with aggregation |

## 18. Pitfalls

1. **Over-engineering relationships** — Not every field is composition or aggregation; some are just association
2. **Mutable aggregated state** — If aggregated objects are mutable, ownership semantics become unclear
3. **Deep composition chains** — `A` owns `B` owns `C` owns `D` creates tight coupling across the system
4. **Forgetting defensive copies** — Returning mutable internal state breaks encapsulation
5. **Circular references in composition** — Can cause stack overflow during traversal
6. **Serialization issues** — Composed objects may not serialize correctly if lifecycle is complex

## 19. Debugging Tips

1. **Use `jmap` or `jcmd`** to inspect object graphs and verify ownership
2. **Check for unexpected retention** — If aggregated objects survive beyond expected lifecycle, check for stray references
3. **Use weak references (`WeakReference`) for debugging** — Detect unintended strong references
4. **Enable GC logging** — `-Xlog:gc*` shows when composed objects are collected
5. **Use IDE debuggers** — Inspect object references to verify relationship types
6. **Write lifecycle tests** — Assert that composed objects become unreachable when the whole is collected

## 20. Comparison Table

| Aspect | Composition | Aggregation | Association | Dependency |
|--------|------------|-------------|-------------|------------|
| Ownership | Strong | Weak | None | None |
| Lifecycle | Tied | Independent | Independent | N/A |
| Multiplicity | 1:N or 1:1 | 1:N, M:N | Any | N/A |
| Creation | Internal | External | External | Method parameter |
| Destruction | With whole | Independent | Independent | Stack frame |
| Encapsulation | High | Medium | Low | None |
| UML symbol | ◆── | ◇── | ─── | - - -> ▷ |
| Java example | `new Room()` in constructor | Constructor parameter | Field reference | Method parameter |

## 21. Decision Tree

```
Do two objects interact?
│
├── NO → No relationship needed
│
└── YES
    │
    ├── Is one object temporary (method parameter only)?
    │   └── YES → Dependency
    │
    ├── Is there no ownership — just a reference?
    │   └── YES → Association
    │
    ├── Can the "part" exist independently of the "whole"?
    │   ├── YES → Aggregation
    │   └── NO
    │       └── Does the "whole" create/destroy the "part"?
    │           ├── YES → Composition
    │           └── NO → Reconsider (likely Association)
    │
    └── Does the "whole" provide lifecycle management?
        ├── YES → Composition
        └── NO → Aggregation
```

## 22. Interview Questions

**Q1: What is the difference between composition and aggregation?**
A: Composition implies strong ownership — the "part" lifecycle is tied to the "whole." Aggregation implies weak ownership — the "part" exists independently. A `House` owns its `Room`s (composition); a `Department` contains `Employee`s (aggregation).

**Q2: Why is composition preferred over inheritance?**
A: Composition provides runtime flexibility (swap implementations), avoids tight coupling (fragile base class problem), and models "has-a" relationships more naturally than inheritance's "is-a."

**Q3: How do you implement composition in Java?**
A: Declare the part as a `private final` field, create it in the constructor, and don't expose it externally.

**Q4: What is the difference between association and aggregation?**
A: Association is the most general relationship (two objects know about each other). Aggregation is a specialized association where one object contains others but doesn't own their lifecycle.

**Q5: When would you use dependency instead of aggregation?**
A: When an object only needs another object temporarily (e.g., to perform a single operation) and doesn't need to store a reference.

**Q6: Can you have bidirectional composition?**
A: No. Composition is inherently unidirectional — the "whole" owns the "part," not the reverse.

**Q7: How does Java's garbage collector handle composed objects?**
A: When the "whole" becomes unreachable, its composed "parts" also become unreachable (assuming no other references) and are collected together.

## 23. Exercises

1. **Identify Relationships:** Given a `University` that contains `Department`s, which contain `Course`s, which have `Student`s, identify composition vs. aggregation for each relationship.

2. **Refactor Inheritance:** Convert a `Square extends Rectangle` hierarchy to use composition with a `Shape` interface.

3. **Implement FileSystem:** Build a file system simulator with `Directory` (composition for subdirectories) and `File` objects.

4. **Design a Library:** Model a `Library` with `Book`s, `Member`s, and `Loan`s. Identify which relationships are composition, aggregation, or dependency.

5. **UML Diagram:** Draw UML for a hospital system with `Hospital`, `Department`, `Doctor`, `Patient`, and `Appointment` relationships.

## 24. Assignments

1. **University Management System:** Implement `University`, `Department`, `Course`, `Student`, and `Instructor` with correct composition/aggregation relationships. Use records for immutable value objects.

2. **Shopping Cart:** Build an e-commerce `Cart` (composition for `CartItem`s), `Customer` (aggregation), and `Product` (independent). Include `Order` creation that moves items from cart to order.

3. **Game Entities:** Design a `GameWorld` (composition for `Zone`s), `Zone` (composition for `Entity`s), `Player` (aggregation — exists independently), and `Item` (composition within `Player` inventory).

## 25. Mini Project

### Task Management System

Build a task management system with these requirements:

- `Project` owns `Task` objects (composition — tasks don't exist without project)
- `Task` has `Assignee` references (aggregation — team members exist independently)
- `Task` uses `NotificationService` temporarily (dependency)
- `Team` has `Member` objects (aggregation)
- Implement `Task.createSubtask()` (composition for subtasks)
- Use sealed interfaces for `TaskStatus`
- Include lifecycle management: when a project is archived, tasks are archived but not destroyed

**Deliverables:**
- Complete Java implementation with all relationship types
- Unit tests verifying lifecycle behavior
- UML diagram showing all relationships

## 26. Summary

- **Composition** = strong ownership, lifecycle tied, parts created internally
- **Aggregation** = weak ownership, lifecycle independent, parts injected externally
- **Association** = general relationship, mutual awareness
- **Dependency** = temporary usage, method parameter only
- **Has-A** is modeled via composition/aggregation; **Is-A** via inheritance
- Favor composition over inheritance for flexibility and loose coupling
- Use `final` fields to enforce composition ownership
- The distinction is about design intent, not performance

## 27. References

- *Effective Java* by Joshua Bloch — Item 18: Favor composition over inheritance
- *Head First Design Patterns* — Chapter 2: Encapsulating what varies
- *UML Distilled* by Martin Fowler — Chapter 3: Class diagrams
- Java SE Documentation: Object References — https://docs.oracle.com/en/java/javase/21/docs/api/
- Martin Fowler, "Inversion of Control Containers and the Dependency Injection Pattern"

---

*Last updated: August 2026*
