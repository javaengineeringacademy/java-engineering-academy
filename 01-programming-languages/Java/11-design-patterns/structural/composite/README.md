# Composite Design Pattern

## Overview
Composite pattern composes objects into tree structures to represent part-whole hierarchies. It lets clients treat individual objects and compositions uniformly.

## When to Use
- You want to represent part-whole hierarchies of objects
- You want clients to be able to ignore differences between compositions and individual objects
- You need to structure a system as a recursive composition

## Code Example

```java
public abstract class FileSystemItem {
    protected String name;

    public FileSystemItem(String name) {
        this.name = name;
    }

    public abstract int getSize();
}

public class Directory extends FileSystemItem {
    private final List<FileSystemItem> children = new ArrayList<>();

    public Directory(String name) {
        super(name);
    }

    public void add(FileSystemItem item) {
        children.add(item);
    }

    @Override
    public int getSize() {
        return children.stream().mapToInt(FileSystemItem::getSize).sum();
    }
}
```

## Common Mistakes
- Not defining common operations in the component interface
- Making leaf nodes implement unnecessary operations
- Not handling null children or invalid operations gracefully

## Interview Questions
1. What is the difference between Composite and Decorator patterns?
2. When would you use a leaf vs a composite node?
3. How do you prevent adding children to leaf nodes?

## Performance

Composite traversal is O(n) where n is total nodes in the tree. For deeply nested trees, recursion overhead adds up — consider iterative traversal with an explicit stack. Composite operations that aggregate (sum, count) must traverse the entire subtree. Caching computed values at composite nodes can reduce repeated traversals.

## Examples

```java
// Organization hierarchy
interface Employee {
    String getName();
    double getSalary();
    void printStructure(String indent);
}

class Developer implements Employee {
    private final String name;
    private final double salary;
    
    Developer(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    
    @Override public String getName() { return name; }
    @Override public double getSalary() { return salary; }
    @Override
    public void printStructure(String indent) {
        System.out.println(indent + "Developer: " + name + " ($" + salary + ")");
    }
}

class Manager implements Employee {
    private final String name;
    private final double salary;
    private final List<Employee> subordinates = new ArrayList<>();
    
    Manager(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    
    void addSubordinate(Employee e) { subordinates.add(e); }
    
    @Override public String getName() { return name; }
    @Override
    public double getSalary() {
        return salary + subordinates.stream()
            .mapToDouble(Employee::getSalary).sum();
    }
    @Override
    public void printStructure(String indent) {
        System.out.println(indent + "Manager: " + name + " ($" + getSalary() + ")");
        subordinates.forEach(e -> e.printStructure(indent + "  "));
    }
}

// Usage
Manager ceo = new Manager("Alice", 200000);
Manager devLead = new Manager("Bob", 120000);
devLead.addSubordinate(new Developer("Carol", 80000));
devLead.addSubordinate(new Developer("Dave", 85000));
ceo.addSubordinate(devLead);
ceo.printStructure("");
// Output includes entire org tree
```

## Internal Working

Composite pattern defines a tree structure where nodes can be leaves (no children) or composites (with children). Both implement the same component interface. Composite nodes delegate operations to their children, optionally adding their own behavior. The client calls methods on the root node; the operation propagates down the tree. This is the pattern behind file systems, UI component trees, and XML/DOM parsers.

## Why This Concept Exists

Tree structures are everywhere: file systems, UI components, organization charts, XML documents. Without composite, you need separate code for leaves and composites. With composite, the client treats all nodes uniformly — calling `getSize()` on a file or a directory works identically. This eliminates type checking and casting throughout the codebase.

## Pitfalls

1. **Type safety**: Leaves may not support all composite operations — `UnsupportedOperationException` at runtime
2. **Infinite recursion**: Circular references cause stack overflow — validate tree structure
3. **Ordering**: Children order may matter but composite does not enforce it
4. **Memory overhead**: Composite nodes hold lists of children — consider flyweight for large trees
5. **Testing complexity**: Need to test leaves, composites, and deep trees separately

## References

- [Refactoring.Guru - Composite Pattern](https://refactoring.guru/design-patterns/composite)
- [Java FileSystem API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/nio/file/FileSystem.html)
- [Head First Design Patterns - Composite Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
