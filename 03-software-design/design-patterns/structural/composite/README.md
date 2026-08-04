# Composite Pattern

The Composite pattern composes objects into tree structures and lets clients treat individual objects and compositions uniformly.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Composite](#basic-composite)
3. [Tree Structures](#tree-structures)
4. [Menu System](#menu-system)
5. [Best Practices](#best-practices)
6. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Composite?

Composite allows you to compose objects into tree structures where individual objects and compositions are treated uniformly.

```
        Component
        ┌─────────┐
        │ + operation() │
        └────┬────┘
             │
       ┌─────┴─────┐
       │           │
    Leaf       Composite
    ┌───────┐   ┌───────────┐
    │       │   │ children  │
    └───────┘   └───────────┘
```

### When to Use

- Tree structures (files/folders, menus, org charts)
- You want clients to treat uniform and composite objects identically
- Recursive structures

---

## Basic Composite

### File System

```java
// Component
public interface FileSystemItem {
    String getName();
    int getSize();
    void print(String indent);
}

// Leaf
public class File implements FileSystemItem {
    private final String name;
    private final int size;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getSize() { return size; }

    @Override
    public void print(String indent) {
        System.out.println(indent + "- " + name + " (" + size + " bytes)");
    }
}

// Composite
public class Directory implements FileSystemItem {
    private final String name;
    private final List<FileSystemItem> children = new ArrayList<>();

    public Directory(String name) {
        this.name = name;
    }

    public void add(FileSystemItem item) {
        children.add(item);
    }

    public void remove(FileSystemItem item) {
        children.remove(item);
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getSize() {
        return children.stream()
            .mapToInt(FileSystemItem::getSize)
            .sum();
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "+ " + name + " (" + getSize() + " bytes)");
        children.forEach(child -> child.print(indent + "  "));
    }
}

// Usage
Directory root = new Directory("root");
Directory src = new Directory("src");
src.add(new File("Main.java", 500));
src.add(new File("Utils.java", 300));
root.add(src);
root.add(new File("README.md", 200));
root.print("");
// + root (1000 bytes)
//   + src (800 bytes)
//     - Main.java (500 bytes)
//     - Utils.java (300 bytes)
//   - README.md (200 bytes)
```

---

## Tree Structures

### Organization Chart

```java
public interface OrganizationComponent {
    String getName();
    int getEmployeeCount();
    void print(String indent);
}

public class Employee implements OrganizationComponent {
    private final String name;
    private final String role;

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getEmployeeCount() { return 1; }

    @Override
    public void print(String indent) {
        System.out.println(indent + "- " + name + " (" + role + ")");
    }
}

public class Department implements OrganizationComponent {
    private final String name;
    private final List<OrganizationComponent> members = new ArrayList<>();

    public Department(String name) { this.name = name; }

    public void add(OrganizationComponent component) {
        members.add(component);
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getEmployeeCount() {
        return members.stream()
            .mapToInt(OrganizationComponent::getEmployeeCount)
            .sum();
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "+ " + name + " (" + getEmployeeCount() + " employees)");
        members.forEach(m -> m.print(indent + "  "));
    }
}

// Usage
Department engineering = new Department("Engineering");
engineering.add(new Employee("Alice", "Lead"));
engineering.add(new Employee("Bob", "Developer"));

Department company = new Department("Company");
company.add(engineering);
company.add(new Employee("Charlie", "CEO"));

company.print("");
```

---

## Menu System

### Restaurant Menu

```java
public interface MenuComponent {
    String getName();
    double getPrice();
    void print();
}

public class MenuItem implements MenuComponent {
    private final String name;
    private final double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() { return name; }

    @Override
    public double getPrice() { return price; }

    @Override
    public void print() {
        System.out.println("  " + name + " - $" + price);
    }
}

public class Menu implements MenuComponent {
    private final String name;
    private final List<MenuComponent> items = new ArrayList<>();

    public Menu(String name) { this.name = name; }

    public void add(MenuComponent item) { items.add(item); }

    @Override
    public String getName() { return name; }

    @Override
    public double getPrice() {
        return items.stream().mapToDouble(MenuComponent::getPrice).sum();
    }

    @Override
    public void print() {
        System.out.println(name + " - $" + getPrice());
        items.forEach(MenuComponent::print);
    }
}

// Build menu structure
Menu breakfast = new Menu("Breakfast");
breakfast.add(new MenuItem("Pancakes", 8.99));
breakfast.add(new MenuItem("Eggs", 6.99));

Menu lunch = new Menu("Lunch");
lunch.add(new MenuItem("Burger", 12.99));
lunch.add(new MenuItem("Salad", 9.99));

Menu allDay = new Menu("All Day Menu");
allDay.add(breakfast);
allDay.add(lunch);
allDay.add(new MenuItem("Coffee", 3.99));

allDay.print();
```

---

## Best Practices

### Do

```java
// 1. Define common interface for leaf and composite
public interface FileSystemItem {
    String getName();
    int getSize();
}

// 2. Implement composite operations recursively
public int getSize() {
    return children.stream()
        .mapToInt(FileSystemItem::getSize)
        .sum();
}

// 3. Handle leaf operations simply
public int getSize() { return size; }
```

### Don't

```java
// 1. Don't force leaf to implement unnecessary methods
// If an operation doesn't apply to leaf, consider separate interfaces

// 2. Don't make composite too complex
// Keep child management simple
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Composite** | Tree structures with uniform interface |
| **Component** | Common interface for all objects |
| **Leaf** | Terminal node with no children |
| **Composite** | Node with children |
| **Uniform Treatment** | Client treats leaf and composite the same |
| **Recursive** | Composite operations delegate to children |
| **Use Cases** | File systems, menus, org charts |
