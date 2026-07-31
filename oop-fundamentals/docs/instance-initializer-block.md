# Instance Initializer Block

## Objective
Understand instance initialization blocks for per-object initialization.

## Theory

### What is an Instance Initializer Block?
Code block **without a name** that executes **before every constructor** for each instance created.

## Syntax
```java
public class Person {
    private final String id;
    private String name;

    // Instance initializer block
    {
        this.id = UUID.randomUUID().toString();
        System.out.println("Initializing Person...");
    }

    public Person(String name) {
        this.name = name;
    }
}
```

## Execution Order

```
1. Superclass static initializers
2. Subclass static initializers
3. Superclass instance initializers
4. Superclass constructor
5. Subclass instance initializers
6. Subclass constructor
```

```java
class Parent {
    static { System.out.println("1. Parent static"); }
    { System.out.println("2. Parent instance"); }
    Parent() { System.out.println("3. Parent constructor"); }
}

class Child extends Parent {
    static { System.out.println("4. Child static"); }
    { System.out.println("5. Child instance"); }
    Child() { System.out.println("6. Child constructor"); }
}

// Output:
// 1. Parent static
// 4. Child static
// 2. Parent instance
// 3. Parent constructor
// 5. Child instance
// 6. Child constructor
```

## Use Cases

### 1. Common Initialization Across Constructors
```java
public class DatabaseConnection {
    private Connection connection;
    private final String url;

    // Shared initialization logic
    {
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException("Connection failed", e);
        }
    }

    public DatabaseConnection(String url) {
        this.url = url;
    }

    public DatabaseConnection(String url, String user, String pass) {
        this.url = url;
        // Different initialization
    }
}
```

### 2. Initialize Collections
```java
class ShoppingCart {
    private final List<Item> items;

    {
        items = new ArrayList<>();
        items.add(new Item("Welcome Gift", 0.00));
    }
}
```

### 3. Anonymous Classes
```java
Runnable task = new Runnable() {
    {
        System.out.println("Anonymous init");
    }
    @Override public void run() { ... }
};
```

## Execution Flow

```java
class Parent {
    static { System.out.println("1. Parent static"); }
    { System.out.println("2. Parent instance"); }
    Parent() { System.out.println("3. Parent constructor"); }
}

class Child extends Parent {
    static { System.out.println("4. Child static"); }
    { System.out.println("5. Child instance"); }
    Child() { System.out.println("6. Child constructor"); }
}

// Output:
// 1. Parent static
// 4. Child static
// 2. Parent instance
// 3. Parent constructor
// 5. Child instance
// 6. Child constructor
```

## Instance Initializer vs Constructor

| Aspect | Instance Initializer | Constructor |
|--------|---------------------|-------------|
| Runs before | Every constructor | Explicit call |
| Parameters | No | Yes |
| `this` available | Yes | Yes |
| Exception handling | Throws to constructor | Normal |
| Multiple allowed | Yes | One called |

## Best Practices

- Use for **common initialization** across constructors
- Keep **simple** - complex logic in constructors
- **Avoid** side effects (I/O, network)
- **Anonymous classes** - only way to initialize

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Complex logic in init block | Move to constructor |
| Throwing checked exception | Wrap in runtime or declare |
| Depends on constructor params | Use constructor instead |
| Modifying static fields | Use static block |

## Interview Questions

1. **When does instance initializer run?**
   - Before every constructor, after superclass constructor

2. **Can a instance initializer throw exception?**
   - Must be declared in constructor's `throws` clause

3. **Can a instance initializer access `this`?**
   - Yes, full access to instance

3. **Multiple instance initializers?**
   - Execute in declaration order

## Related Topics
← [Static Block](static-block.md) | → [Packages](packages.md)

## References
- [JLS - Instance Initializers](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.6)