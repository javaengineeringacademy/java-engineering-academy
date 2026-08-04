# Abstract Factory Pattern

The Abstract Factory pattern provides an interface for creating families of related or dependent objects without specifying their concrete classes. It ensures consistency across related products.

## Table of Contents

1. [Concepts](#concepts)
2. [Abstract Factory Implementation](#abstract-factory-implementation)
3. [Product Families](#product-families)
4. [Factory of Factories](#factory-of-factories)
5. [Best Practices](#best-practices)
6. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Abstract Factory?

Abstract Factory creates families of related objects. While Factory Method creates one product, Abstract Factory creates multiple related products that work together.

```
┌──────────────────────┐
│  AbstractFactory     │
│  + createProductA()  │
│  + createProductB()  │
└──────────┬───────────┘
           │
    ┌──────┴──────┐
    │             │
┌───┴────┐  ┌────┴───┐
│Factory1│  │Factory2│
└───┬────┘  └────┬───┘
    │            │
┌───┴───┐  ┌────┴───┐
│A1 + B1│  │A2 + B2│
└───────┘  └───────┘
```

### When to Use

- System must be independent of how products are created
- System must work with multiple families of products
- Products are designed to be used together
- You want to enforce consistency across products

---

## Abstract Factory Implementation

### UI Theme Factory

```java
// Abstract products
public interface Button { void render(); }
public interface TextField { void render(); }
public interface Checkbox { void render(); }

// Concrete products - Light theme
public class LightButton implements Button {
    @Override
    public void render() { System.out.println("Light button"); }
}

public class LightTextField implements TextField {
    @Override
    public void render() { System.out.println("Light text field"); }
}

public class LightCheckbox implements Checkbox {
    @Override
    public void render() { System.out.println("Light checkbox"); }
}

// Concrete products - Dark theme
public class DarkButton implements Button {
    @Override
    public void render() { System.out.println("Dark button"); }
}

public class DarkTextField implements TextField {
    @Override
    public void render() { System.out.println("Dark text field"); }
}

public class DarkCheckbox implements Checkbox {
    @Override
    public void render() { System.out.println("Dark checkbox"); }
}

// Abstract factory
public interface UIFactory {
    Button createButton();
    TextField createTextField();
    Checkbox createCheckbox();
}

// Concrete factories
public class LightUIFactory implements UIFactory {
    @Override
    public Button createButton() { return new LightButton(); }
    @Override
    public TextField createTextField() { return new LightTextField(); }
    @Override
    public Checkbox createCheckbox() { return new LightCheckbox(); }
}

public class DarkUIFactory implements UIFactory {
    @Override
    public Button createButton() { return new DarkButton(); }
    @Override
    public TextField createTextField() { return new DarkTextField(); }
    @Override
    public Checkbox createCheckbox() { return new DarkCheckbox(); }
}
```

### Client Using Abstract Factory

```java
public class Application {
    private final Button button;
    private final TextField textField;
    private final Checkbox checkbox;

    public Application(UIFactory factory) {
        this.button = factory.createButton();
        this.textField = factory.createTextField();
        this.checkbox = factory.createCheckbox();
    }

    public void render() {
        button.render();
        textField.render();
        checkbox.render();
    }
}

// Usage
Application lightApp = new Application(new LightUIFactory());
Application darkApp = new Application(new DarkUIFactory());
```

---

## Product Families

### Database Access Factory

```java
// Abstract products
public interface Connection { void connect(); }
public interface Command { void execute(String query); }
public interface Reader { String read(); }

// MySQL family
public class MysqlConnection implements Connection {
    @Override
    public void connect() { System.out.println("Connecting to MySQL"); }
}

public class MysqlCommand implements Command {
    @Override
    public void execute(String query) { System.out.println("MySQL: " + query); }
}

public class MysqlReader implements Reader {
    @Override
    public String read() { return "MySQL result"; }
}

// PostgreSQL family
public class PostgresConnection implements Connection {
    @Override
    public void connect() { System.out.println("Connecting to PostgreSQL"); }
}

public class PostgresCommand implements Command {
    @Override
    public void execute(String query) { System.out.println("PostgreSQL: " + query); }
}

public class PostgresReader implements Reader {
    @Override
    public String read() { return "PostgreSQL result"; }
}

// Abstract factory
public interface DatabaseFactory {
    Connection createConnection();
    Command createCommand();
    Reader createReader();
}

// Concrete factories
public class MysqlFactory implements DatabaseFactory {
    @Override
    public Connection createConnection() { return new MysqlConnection(); }
    @Override
    public Command createCommand() { return new MysqlCommand(); }
    @Override
    public Reader createReader() { return new MysqlReader(); }
}

public class PostgresFactory implements DatabaseFactory {
    @Override
    public Connection createConnection() { return new PostgresConnection(); }
    @Override
    public Command createCommand() { return new PostgresCommand(); }
    @Override
    public Reader createReader() { return new PostgresReader(); }
}
```

---

## Factory of Factories

### Dynamic Factory Selection

```java
public class FactoryProvider {
    private static final Map<String, DatabaseFactory> factories = new HashMap<>();

    static {
        factories.put("mysql", new MysqlFactory());
        factories.put("postgres", new PostgresFactory());
    }

    public static DatabaseFactory getFactory(String dbType) {
        DatabaseFactory factory = factories.get(dbType.toLowerCase());
        if (factory == null) {
            throw new IllegalArgumentException("Unknown DB: " + dbType);
        }
        return factory;
    }
}

// Usage
DatabaseFactory factory = FactoryProvider.getFactory("mysql");
Connection conn = factory.createConnection();
Command cmd = factory.createCommand();
```

### Registration-Based Factory

```java
public class DynamicFactoryProvider {
    private static final Map<String, Supplier<DatabaseFactory>> registry = new HashMap<>();

    public static void register(String key, Supplier<DatabaseFactory> creator) {
        registry.put(key, creator);
    }

    public static DatabaseFactory create(String key) {
        Supplier<DatabaseFactory> creator = registry.get(key);
        if (creator == null) {
            throw new IllegalArgumentException("No factory for: " + key);
        }
        return creator.get();
    }
}

// Registration
DynamicFactoryProvider.register("mysql", MysqlFactory::new);
DynamicFactoryProvider.register("postgres", PostgresFactory::new);

// Usage
DatabaseFactory factory = DynamicFactoryProvider.create("mysql");
```

---

## Best Practices

### Do

```java
// 1. Keep product interfaces consistent
public interface Button { void render(); }
public interface TextField { void render(); }

// 2. Ensure products within a family are compatible
// LightButton works with LightTextField, not DarkTextField

// 3. Use factory registry for dynamic selection
DatabaseFactory factory = FactoryProvider.getFactory(dbType);

// 4. Document which products belong to which family
// Light family: LightButton, LightTextField, LightCheckbox
```

### Don't

```java
// 1. Don't mix products from different families
// BAD: LightButton with DarkTextField

// 2. Don't create too many product types
// Keep factory interface focused

// 3. Don't use when only one product type exists
// Use simple Factory Method instead
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Abstract Factory** | Creates families of related objects |
| **Product Family** | Group of related products |
| **Consistency** | Products within family work together |
| **Isolation** | Client doesn't know concrete classes |
| **Extensibility** | Add new families without changing client |
| **Factory Registry** | Dynamic factory selection |
| **When to Use** | Multiple related product types |
| **Trade-off** | Complexity for flexibility |
