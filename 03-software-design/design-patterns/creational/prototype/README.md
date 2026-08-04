# Prototype Pattern

The Prototype pattern creates new objects by cloning existing instances rather than creating from scratch. It's useful when object creation is expensive and you want to avoid repeated initialization costs.

## Table of Contents

1. [Concepts](#concepts)
2. [Cloneable Interface](#cloneable-interface)
3. [Deep vs Shallow Copy](#deep-vs-shallow-copy)
4. [Copy Constructor](#copy-constructor)
5. [Prototype Registry](#prototype-registry)
6. [Best Practices](#best-practices)
7. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Prototype?

Prototype creates new objects by copying an existing instance (the prototype). This avoids expensive initialization and provides pre-configured templates.

```
┌─────────────────┐     clone()     ┌─────────────────┐
│   Prototype     │────────────────▶│   New Instance  │
│   (template)    │                 │   (copy)        │
└─────────────────┘                 └─────────────────┘
```

### When to Use

- Expensive object creation (database connections, complex calculations)
- Many similar objects with slight variations
- Avoiding subclassing
- Runtime type determination

---

## Cloneable Interface

### Basic Cloneable Implementation

```java
public class Employee implements Cloneable {
    private String name;
    private int age;
    private String department;
    private Address address;

    public Employee(String name, int age, String department, Address address) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.address = address;
    }

    @Override
    public Employee clone() {
        try {
            return (Employee) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);  // Should never happen
        }
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
}

// Usage
Employee prototype = new Employee("Alice", 30, "Engineering", new Address("123 Main St", "NYC"));
Employee copy = prototype.clone();
copy.setName("Bob");  // Doesn't affect original
```

### Problem with Shallow Clone

```java
// Shallow copy - shared references!
Employee original = new Employee("Alice", 30, "Engineering", new Address("123 Main St", "NYC"));
Employee copy = original.clone();

// Both point to same Address object
copy.getAddress().setCity("Boston");
System.out.println(original.getAddress().getCity());  // "Boston" - modified!
```

---

## Deep vs Shallow Copy

### Shallow Copy

```java
public class ShallowClone implements Cloneable {
    private String name;
    private List<String> items;
    private Address address;

    @Override
    public ShallowClone clone() {
        try {
            return (ShallowClone) super.clone();
            // String, int, etc. are copied by value
            // List, Address are copied by reference (shallow)
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}

// Shallow copy: items list is shared between original and clone
ShallowClone original = new ShallowClone();
ShallowClone copy = original.clone();
copy.getItems().add("new item");  // Modifies original's list too!
```

### Deep Copy Implementation

```java
public class DeepClone implements Cloneable {
    private String name;
    private List<String> items;
    private Address address;

    @Override
    public DeepClone clone() {
        try {
            DeepClone copy = (DeepClone) super.clone();
            // Deep copy mutable fields
            copy.items = new ArrayList<>(this.items);
            copy.address = this.address.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}

// Deep copy: each has its own items list and Address
DeepClone original = new DeepClone();
DeepClone copy = original.clone();
copy.getItems().add("new item");  // Only modifies copy's list
```

### Deep Copy with Serialization

```java
public class SerializableDeepCopy implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<String> items;
    private Address address;

    public SerializableDeepCopy deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (SerializableDeepCopy) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

### Manual Deep Copy

```java
public class ManualDeepCopy {
    private String name;
    private List<String> items;
    private Map<String, Object> data;
    private Address address;

    // Copy constructor
    public ManualDeepCopy(ManualDeepCopy other) {
        this.name = other.name;
        this.items = new ArrayList<>(other.items);
        this.data = new HashMap<>(other.data);
        this.address = new Address(other.address);  // Copy constructor
    }

    // Clone method
    @Override
    public ManualDeepCopy clone() {
        return new ManualDeepCopy(this);
    }
}
```

---

## Copy Constructor

### Copy Constructor Pattern

```java
public class Product {
    private final String id;
    private final String name;
    private final double price;
    private final List<String> tags;

    public Product(String id, String name, double price, List<String> tags) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.tags = new ArrayList<>(tags);
    }

    // Copy constructor
    public Product(Product other) {
        this.id = other.id;
        this.name = other.name;
        this.price = other.price;
        this.tags = new ArrayList<>(other.tags);  // Deep copy
    }

    // Create modified copy
    public Product withPrice(double newPrice) {
        return new Product(this.id, this.name, newPrice, this.tags);
    }

    public Product withName(String newName) {
        return new Product(this.id, newName, this.price, this.tags);
    }

    public Product addTag(String tag) {
        List<String> newTags = new ArrayList<>(this.tags);
        newTags.add(tag);
        return new Product(this.id, this.name, this.price, newTags);
    }
}

// Usage
Product original = new Product("P001", "Widget", 9.99, List.of("sale"));
Product discounted = original.withPrice(7.99);
Product renamed = original.withName("Super Widget");
Product tagged = original.addTag("new");
```

---

## Prototype Registry

### Centralized Prototype Management

```java
public class PrototypeRegistry {
    private final Map<String, Supplier<?>> prototypes = new HashMap<>();

    public <T> void register(String key, T prototype) {
        prototypes.put(key, () -> {
            if (prototype instanceof Cloneable cloneable) {
                @SuppressWarnings("unchecked")
                T clone = (T) cloneable.clone();
                return clone;
            }
            return prototype;
        });
    }

    @SuppressWarnings("unchecked")
    public <T> T create(String key) {
        Supplier<?> supplier = prototypes.get(key);
        if (supplier == null) {
            throw new IllegalArgumentException("No prototype for: " + key);
        }
        return (T) supplier.get();
    }
}

// Usage
PrototypeRegistry registry = new PrototypeRegistry();
registry.register("admin", new User("admin", "Administrator", List.of("ALL")));
registry.register("guest", new User("guest", "Guest User", List.of("READ")));

User admin = registry.create("admin");
User guest = registry.create("guest");
```

### Document Template Registry

```java
public class DocumentTemplateRegistry {
    private final Map<String, Document> templates = new HashMap<>();

    public void registerTemplate(String name, Document template) {
        templates.put(name, template);
    }

    public Document createFromTemplate(String templateName) {
        Document template = templates.get(templateName);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateName);
        }
        return template.clone();
    }
}

// Usage
DocumentTemplateRegistry registry = new DocumentTemplateRegistry();
registry.registerTemplate("invoice", new InvoiceDocument());
registry.registerTemplate("report", new ReportDocument());

Document invoice = registry.createFromTemplate("invoice");
invoice.setRecipient("Alice");
invoice.setTotal(100.00);
```

---

## Best Practices

### Do

```java
// 1. Use copy constructors for clarity
public User(User other) {
    this.name = other.name;
    this.email = other.email;
    this.roles = new ArrayList<>(other.roles);
}

// 2. Deep copy mutable fields
@Override
public User clone() {
    User copy = (User) super.clone();
    copy.roles = new ArrayList<>(this.roles);
    return copy;
}

// 3. Make clone() or copy constructor public
public User clone() { ... }
public User(User other) { ... }

// 4. Use final fields for immutable data
public record Point(int x, int y) {}
```

### Don't

```java
// 1. Don't forget to deep copy mutable fields
@Override
public User clone() {
    User copy = (User) super.clone();
    // BAD: shared reference to mutable list
    return copy;
    // GOOD: deep copy
    copy.roles = new ArrayList<>(this.roles);
    return copy;
}

// 2. Don't use clone() for immutable objects
// Use records or static factory methods instead

// 3. Don't create too many prototype variants
// Consider builder pattern for complex objects
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Prototype** | Create objects by cloning existing instances |
| **Cloneable** | Interface for clone support |
| **Shallow Copy** | Copies references, not objects |
| **Deep Copy** | Copies objects recursively |
| **Copy Constructor** | Alternative to clone() |
| **Registry** | Manage prototype templates |
| **Performance** | Avoid expensive initialization |
| **Flexibility** | Modify clones without affecting original |
| **Serialization** | Alternative deep copy mechanism |
| **Records** | Immutable, no cloning needed |
