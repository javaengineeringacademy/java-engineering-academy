# Prototype Pattern

## 1. Introduction

The Prototype Pattern is a creational design pattern that lets you copy existing objects without making your code dependent on their classes. Instead of creating new objects from scratch, you clone existing objects (prototypes) and optionally modify them.

The pattern is particularly useful when object creation is expensive (complex initialization, network calls, database queries) and you need many similar objects.

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Implement the Prototype pattern with proper cloning
- Understand shallow vs. deep copy
- Handle cloning challenges in Java (Cloneable, Serializable)
- Use prototype registries for managing prototypes
- Recognize when cloning is appropriate vs. expensive

---

## 3. Prerequisites

- Understanding of Java's Cloneable interface
- Knowledge of object copying in Java
- Familiarity with serialization
- Understanding of reference types

---

## 4. Why This Concept Exists

The Prototype pattern exists because:

- **Performance**: Cloning can be faster than creating from scratch
- **Complexity**: Avoid complex initialization logic
- **Variations**: Create variations without subclass explosion
- **Default values**: Use pre-configured prototypes as templates
- **Runtime flexibility**: Determine object type at runtime

Without Prototype, you'd duplicate initialization code or create many similar subclasses.

---

## 5. Problem Statement

Consider creating shapes:

```java
// BAD: Duplicating initialization logic
public class Circle {
    private int x, y, radius;
    private String color;
    private boolean filled;
    private Stroke stroke;

    public Circle() {
        // Expensive initialization
        this.stroke = new Stroke(1, Color.BLACK);
        this.color = "RED";
        this.filled = true;
        // ... more initialization
    }
}

// Creating similar objects
Circle c1 = new Circle(); // Full initialization
Circle c2 = new Circle();
c2.setColor("BLUE"); // Only color differs

// Problem: Repeating expensive initialization
```

**Problems:**
1. **Performance waste**: Repeating expensive initialization
2. **Code duplication**: Same initialization logic everywhere
3. **Hard to maintain**: Changes require updating all creation points

---

## 6. Theory

### 6.1 Shallow vs. Deep Copy

| Copy Type | What's Copied | Reference Handling |
|-----------|---------------|-------------------|
| Shallow | Primitive fields | References shared |
| Deep | Everything | References cloned |

### 6.2 Cloneable vs. Serialization

| Approach | Pros | Cons |
|----------|------|------|
| Cloneable | Fast, direct | Tricky implementation |
| Serializable | Automatic deep copy | Slower, requires Serializable |

### 6.3 Prototype Registry

A registry stores pre-configured prototypes that can be cloned on demand:
- Centralized management
- Easy to create variations
- Supports runtime configuration

---

## 7. Internal Working

### 7.1 Cloning Flow

```
1. Client requests clone of prototype
2. Prototype creates copy of itself
3. Optional modifications applied
4. New object returned
```

### 7.2 Deep Copy Flow

```
Original Object
├── Primitive fields → Copied by value
└── Reference fields → Reference copied
    └── Need to clone referenced objects for deep copy
```

---

## 8. JVM Perspective

### 8.1 Object Memory

- Cloned object created on heap
- Primitive fields copied by value
- Reference fields copied by reference (shallow)
- Deep copy duplicates entire object graph

### 8.2 Performance

- Object creation: O(1) allocation
- Shallow clone: O(n) where n = fields
- Deep clone: O(m) where m = total objects in graph

---

## 9. Memory Representation

### 9.1 Shallow Copy

```
Original Object          Cloned Object
┌─────────────────┐     ┌─────────────────┐
│ primitive: 42   │     │ primitive: 42   │
│ reference ──────│──→  │ reference ──────│──→ Shared Object
└─────────────────┘     └─────────────────┘
```

### 9.2 Deep Copy

```
Original Object          Cloned Object
┌─────────────────┐     ┌─────────────────┐
│ primitive: 42   │     │ primitive: 42   │
│ reference ──────│──→  │ reference ──────│──→ Cloned Object
└─────────────────┘     └─────────────────┘
```

---

## 10. Syntax

### 10.1 Basic Prototype Structure

```java
public class Prototype implements Cloneable {
    private String field;
    private List<String> items;

    @Override
    public Prototype clone() throws CloneNotSupportedException {
        Prototype cloned = (Prototype) super.clone();
        cloned.items = new ArrayList<>(this.items); // Deep copy
        return cloned;
    }
}
```

### 10.2 Prototype Registry

```java
public class PrototypeRegistry {
    private static final Map<String, Prototype> prototypes = new HashMap<>();

    public static void register(String key, Prototype prototype) {
        prototypes.put(key, prototype);
    }

    public static Prototype getPrototype(String key) {
        return prototypes.get(key).clone();
    }
}
```

---

## 11. Easy Example

### Simple Shape Prototype

```java
public class Shape implements Cloneable {
    private int x;
    private int y;
    private String color;
    private boolean filled;

    public Shape(int x, int y, String color, boolean filled) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.filled = filled;
    }

    @Override
    public Shape clone() {
        try {
            return (Shape) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("Shape{x=%d, y=%d, color='%s', filled=%b}", x, y, color, filled);
    }
}

// Usage
Shape original = new Shape(0, 0, "RED", true);
Shape clone1 = original.clone();
clone1.setPosition(10, 10);

Shape clone2 = original.clone();
clone2.setPosition(20, 20);
```

---

## 12. Medium Example

### Deep Copy with Cloning

```java
public class Document implements Cloneable {
    private String title;
    private List<String> paragraphs;
    private Map<String, String> metadata;
    private Formatting formatting;

    public Document(String title) {
        this.title = title;
        this.paragraphs = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.formatting = new Formatting();
    }

    @Override
    public Document clone() {
        try {
            Document cloned = (Document) super.clone();
            // Deep copy mutable fields
            cloned.paragraphs = new ArrayList<>(this.paragraphs);
            cloned.metadata = new HashMap<>(this.metadata);
            cloned.formatting = this.formatting.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public void addParagraph(String text) {
        paragraphs.add(text);
    }

    public void setMetadata(String key, String value) {
        metadata.put(key, value);
    }

    public Document copyWithDifferentTitle(String newTitle) {
        Document copy = this.clone();
        copy.title = newTitle;
        return copy;
    }
}

class Formatting implements Cloneable {
    private String font;
    private int fontSize;
    private boolean bold;

    public Formatting() {
        this.font = "Arial";
        this.fontSize = 12;
        this.bold = false;
    }

    @Override
    public Formatting clone() {
        try {
            return (Formatting) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}

// Usage
Document template = new Document("Template");
template.addParagraph("Header");
template.setMetadata("author", "John");

Document doc1 = template.copyWithDifferentTitle("Report 1");
Document doc2 = template.copyWithDifferentTitle("Report 2");
```

---

## 13. Hard Example

### Prototype Registry with Variants

```java
public abstract class Vehicle implements Cloneable {
    protected String make;
    protected String model;
    protected int year;
    protected double price;
    protected List<String> features;

    protected Vehicle() {
        this.features = new ArrayList<>();
    }

    @Override
    public Vehicle clone() {
        try {
            Vehicle cloned = (Vehicle) super.clone();
            cloned.features = new ArrayList<>(this.features);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public abstract Vehicle createVariant(String color);

    @Override
    public String toString() {
        return String.format("%s %s %d ($%.2f)", make, model, year, price);
    }
}

public class Car extends Vehicle {
    private String color;
    private boolean sunroof;

    @Override
    public Car clone() {
        Car cloned = (Car) super.clone();
        return cloned;
    }

    @Override
    public Car createVariant(String color) {
        Car variant = this.clone();
        variant.color = color;
        return variant;
    }
}

public class Truck extends Vehicle {
    private double payloadCapacity;

    @Override
    public Truck clone() {
        Truck cloned = (Truck) super.clone();
        return cloned;
    }

    @Override
    public Truck createVariant(String color) {
        Truck variant = this.clone();
        return variant;
    }
}

public class VehicleRegistry {
    private final Map<String, Vehicle> prototypes = new HashMap<>();

    public void register(String key, Vehicle prototype) {
        prototypes.put(key, prototype);
    }

    public Vehicle create(String key) {
        Vehicle prototype = prototypes.get(key);
        if (prototype == null) {
            throw new IllegalArgumentException("No prototype for: " + key);
        }
        return prototype.clone();
    }

    public Vehicle createVariant(String key, String color) {
        Vehicle prototype = prototypes.get(key);
        if (prototype == null) {
            throw new IllegalArgumentException("No prototype for: " + key);
        }
        return prototype.createVariant(color);
    }
}

// Usage
VehicleRegistry registry = new VehicleRegistry();

Car sedanPrototype = new Car();
sedanPrototype.make = "Toyota";
sedanPrototype.model = "Camry";
sedanPrototype.year = 2024;
sedanPrototype.price = 30000;
registry.register("sedan", sedanPrototype);

Car redSedan = (Car) registry.createVariant("sedan", "Red");
Car blueSedan = (Car) registry.createVariant("sedan", "Blue");
```

---

## 14. Enterprise Example

### Report Generator with Prototype Pattern

```java
public abstract class Report implements Cloneable {
    protected String title;
    protected LocalDate generatedDate;
    protected List<ReportSection> sections;
    protected Map<String, Object> parameters;

    protected Report() {
        this.generatedDate = LocalDate.now();
        this.sections = new ArrayList<>();
        this.parameters = new HashMap<>();
    }

    @Override
    public Report clone() {
        try {
            Report cloned = (Report) super.clone();
            cloned.sections = new ArrayList<>();
            for (ReportSection section : this.sections) {
                cloned.sections.add(section.clone());
            }
            cloned.parameters = new HashMap<>(this.parameters);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public Report copyWithChanges(String newTitle, Map<String, Object> newParams) {
        Report copy = this.clone();
        copy.title = newTitle;
        copy.generatedDate = LocalDate.now();
        copy.parameters.putAll(newParams);
        return copy;
    }

    public abstract String generate();
}

public class ReportSection implements Cloneable {
    private String name;
    private String content;
    private List<String> data;

    public ReportSection(String name) {
        this.name = name;
        this.data = new ArrayList<>();
    }

    @Override
    public ReportSection clone() {
        try {
            ReportSection cloned = (ReportSection) super.clone();
            cloned.data = new ArrayList<>(this.data);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public void addData(String item) {
        data.add(item);
    }
}

public class SalesReport extends Report {
    @Override
    public Report clone() {
        return super.clone();
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sales Report: ").append(title).append("\n");
        sb.append("Date: ").append(generatedDate).append("\n");
        for (ReportSection section : sections) {
            sb.append(section.toString()).append("\n");
        }
        return sb.toString();
    }
}

public class ReportRegistry {
    private final Map<String, Report> prototypes = new HashMap<>();

    public void register(String key, Report prototype) {
        prototypes.put(key, prototype);
    }

    public Report create(String key) {
        Report prototype = prototypes.get(key);
        if (prototype == null) {
            throw new IllegalArgumentException("No report template: " + key);
        }
        return prototype.clone();
    }

    public Report createWithParameters(String key, Map<String, Object> params) {
        Report prototype = prototypes.get(key);
        if (prototype == null) {
            throw new IllegalArgumentException("No report template: " + key);
        }
        Report copy = prototype.clone();
        copy.parameters.putAll(params);
        return copy;
    }
}

// Usage
ReportRegistry registry = new ReportRegistry();

SalesReport template = new SalesReport();
template.title = "Monthly Sales";
template.sections.add(new ReportSection("Summary"));
template.sections.add(new ReportSection("Details"));
registry.register("monthly-sales", template);

// Create variations
Report januaryReport = registry.createWithParameters("monthly-sales",
    Map.of("month", "January", "year", 2024));

Report februaryReport = registry.createWithParameters("monthly-sales",
    Map.of("month", "February", "year", 2024));
```

---

## 15. Performance

### 15.1 Performance Metrics

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| Shallow clone | O(n) | n = number of fields |
| Deep clone | O(m) | m = total objects in graph |
| Registry lookup | O(1) | HashMap access |

### 15.2 Optimization Tips

1. **Cache prototypes**: Pre-create commonly used variations
2. **Lazy deep copy**: Only clone referenced objects when needed
3. **Use serialization wisely**: Avoid for large object graphs
4. **Consider copy constructors**: Alternative to Cloneable

---

## 16. Best Practices

1. **Prefer copy constructors**: Over Cloneable when possible
2. **Document cloning behavior**: Shallow vs. deep copy
3. **Use prototype registry**: For managing common prototypes
4. **Handle CloneNotSupportedException**: Properly or wrap in runtime exception
5. **Consider immutability**: Immutable prototypes are safer
6. **Test cloning thoroughly**: Especially for complex object graphs
7. **Use serialization for deep copy**: When object graph is complex
8. **Consider factory + prototype**: Combine patterns for flexibility

---

## 17. Common Mistakes

1. **Shallow copy only**: Not cloning referenced objects
2. **Ignoring CloneNotSupportedException**: Wrapping in AssertionError
3. **Not copying collections**: Exposing mutable internal state
4. **Overusing Cloneable**: Consider copy constructors
5. **Circular references**: Infinite recursion in deep copy
6. **Forgetting transient fields**: Not handling non-serializable fields

---

## 18. Pitfalls

- **Complexity**: Deep copy can be error-prone
- **Performance**: Deep copy expensive for large graphs
- **Maintenance**: Changes require updating clone methods
- **Java's Cloneable**: Notoriously tricky to implement correctly
- **Reference issues**: Circular references cause problems

---

## 19. Debugging Tips

1. **Add toString()**: To verify cloned state
2. **Test identity**: Verify objects are different instances
3. **Test deep copy**: Modify clone, verify original unchanged
4. **Use debugger**: Step through clone methods
5. **Log cloning**: Track when clone is called

---

## 20. Comparison Table

| Approach | Speed | Complexity | Deep Copy | Immutability |
|----------|-------|------------|-----------|--------------|
| Cloneable | Fast | Medium | Manual | No |
| Serialization | Slow | Low | Automatic | No |
| Copy Constructor | Fast | Low | Manual | Optional |
| Builder | Medium | Medium | N/A | Yes |

---

## 21. Decision Tree

```
Need to create similar objects?
├── Complex initialization? → Prototype
├── Many variations? → Prototype with Registry
├── Need deep copy? → Use serialization or manual deep copy
├── Simple objects? → Copy constructor
└── Need immutability? → Builder or Record
```

---

## 22. Interview Questions

### Q1: What is the Prototype pattern?
**Answer**: A creational pattern that creates new objects by cloning existing instances (prototypes) rather than creating from scratch.

### Q2: Shallow vs. deep copy?
**Answer**: Shallow copies references, deep copies the entire object graph. Deep copy is needed when modifications to clone shouldn't affect original.

### Q3: Why is Cloneable considered broken?
**Answer**: Cloneable doesn't define clone(), returns Object, throws checked exception, and doesn't guarantee deep copy. Consider copy constructors instead.

### Q4: When would you use Prototype over Factory?
**Answer**: When object creation is expensive, when you need many similar variations, or when you don't know the exact types at compile time.

### Q5: How to handle circular references?
**Answer**: Track visited objects during cloning, use identity hash maps, or use serialization which handles cycles automatically.

---

## 23. Exercises

### Exercise 1: Simple Prototype
Create a `Document` class with clone support for shallow and deep copy.

### Exercise 2: Prototype Registry
Implement a registry that stores and clones different report templates.

### Exercise 3: Deep Copy Challenge
Create a deep clone implementation for a complex object graph with circular references.

---

## 24. Assignments

1. **Assignment 1**: Create a prototype pattern for game characters with different stats
2. **Assignment 2**: Implement a document template system using prototype pattern
3. **Assignment 3**: Build a configuration manager that clones default settings

---

## 25. Mini Project

### Game Character Factory
Create a system that:
- Stores character prototypes (Warrior, Mage, Archer)
- Clones prototypes for new characters
- Supports deep copy for inventory and stats
- Allows customization after cloning
- Manages character templates

---

## 26. Summary

- Prototype creates objects by cloning existing instances
- Shallow vs. deep copy considerations
- Cloneable is tricky; consider copy constructors
- Prototype registry manages common templates
- Useful when creation is expensive or you need many variations
- Deep copy requires careful handling of referenced objects

---

## 27. References

1. Gamma, E., et al. (1994). *Design Patterns*, Chapter 3
2. Bloch, J. (2018). *Effective Java*, Item 13
3. Refactoring Guru: https://refactoring.guru/design-patterns/prototype
4. Java Design Patterns: https://java-design-patterns.com/patterns/prototype/
