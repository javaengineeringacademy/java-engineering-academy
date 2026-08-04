# Factory Pattern

The Factory pattern provides an interface for creating objects without specifying their concrete classes. It encapsulates object creation logic and enables flexible, extensible designs.

## Table of Contents

1. [Concepts](#concepts)
2. [Factory Method](#factory-method)
3. [Abstract Factory](#abstract-factory)
4. [Static Factory Methods](#static-factory-methods)
5. [Parameterized Factory](#parameterized-factory)
6. [Best Practices](#best-practices)
7. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is the Factory Pattern?

A Factory creates objects without exposing the creation logic to the client. The client receives an object through a common interface.

```
┌──────────────┐      ┌──────────────┐
│    Client    │──────│   Factory    │
└──────────────┘      └──────┬───────┘
                             │
              ┌──────────────┼──────────────┐
              │              │              │
        ┌─────┴─────┐ ┌─────┴─────┐ ┌─────┴─────┐
        │Product A  │ │Product B  │ │Product C  │
        └───────────┘ └───────────┘ └───────────┘
```

### Benefits

- **Loose Coupling** - client doesn't depend on concrete classes
- **Single Responsibility** - creation logic centralized
- **Open/Closed** - add new products without modifying client
- **Testability** - easy to mock factory

---

## Factory Method

### Basic Factory Method

```java
// Product interface
public interface Notification {
    void send(String message);
}

// Concrete products
public class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

public class SmsNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

public class PushNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Push: " + message);
    }
}

// Factory
public class NotificationFactory {
    public static Notification create(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms" -> new SmsNotification();
            case "push" -> new PushNotification();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}

// Usage
Notification notification = NotificationFactory.create("email");
notification.send("Hello!");
```

### Factory Method in Hierarchy

```java
// Creator abstract class
public abstract class DocumentCreator {
    // Factory method
    public abstract Document createDocument();

    // Template method using factory method
    public void processDocument() {
        Document doc = createDocument();
        doc.open();
        doc.write();
        doc.close();
    }
}

// Concrete creators
public class PdfCreator extends DocumentCreator {
    @Override
    public Document createDocument() {
        return new PdfDocument();
    }
}

public class WordCreator extends DocumentCreator {
    @Override
    public Document createDocument() {
        return new WordDocument();
    }
}

// Usage
DocumentCreator creator = new PdfCreator();
creator.processDocument();  // Creates and processes PDF
```

---

## Abstract Factory

### Abstract Factory Implementation

```java
// Abstract products
public interface Button {
    void render();
}

public interface TextField {
    void render();
}

public interface Checkbox {
    void render();
}

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

// Client code
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
UIFactory factory = new DarkUIFactory();
Application app = new Application(factory);
app.render();
```

---

## Static Factory Methods

### Named Static Factories

```java
public class Color {
    private final int r, g, b;

    private Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    // Named static factory methods
    public static Color of(int r, int g, int b) {
        return new Color(r, g, b);
    }

    public static Color fromHex(String hex) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return new Color(r, g, b);
    }

    public static Color red() { return new Color(255, 0, 0); }
    public static Color green() { return new Color(0, 255, 0); }
    public static Color blue() { return new Color(0, 0, 255); }
}

// Usage - clear what each creates
Color c1 = Color.of(255, 128, 0);
Color c2 = Color.fromHex("#FF8000");
Color c3 = Color.red();
```

### Collection Factory Methods

```java
// Java 9+ collection factories
List<String> list = List.of("a", "b", "c");
Set<Integer> set = Set.of(1, 2, 3);
Map<String, Integer> map = Map.of("key1", 1, "key2", 2);

// Copy factories
List<String> copy = List.copyOf(original);
Map<String, Integer> mapCopy = Map.copyOf(originalMap);
```

---

## Parameterized Factory

### Factory with Configuration

```java
public class ConnectionFactory {
    private final Map<String, Supplier<Connection>> creators = new HashMap<>();

    public ConnectionFactory() {
        creators.put("mysql", MysqlConnection::new);
        creators.put("postgres", PostgresConnection::new);
        creators.put("h2", H2Connection::new);
    }

    public Connection create(String type) {
        Supplier<Connection> creator = creators.get(type.toLowerCase());
        if (creator == null) {
            throw new IllegalArgumentException("Unknown DB type: " + type);
        }
        return creator.get();
    }

    public void register(String type, Supplier<Connection> creator) {
        creators.put(type, creator);
    }
}

// Usage
ConnectionFactory factory = new ConnectionFactory();
Connection conn = factory.create("postgres");
```

### Builder Factory

```java
public class QueryBuilder {
    public static <T> Query<T> select(Class<T> entity) {
        return new Query<>(entity);
    }
}

public class Query<T> {
    private final Class<T> entity;
    private final List<String> conditions = new ArrayList<>();
    private final List<String> orderBy = new ArrayList<>();
    private Integer limit;

    public Query(Class<T> entity) {
        this.entity = entity;
    }

    public Query<T> where(String condition) {
        conditions.add(condition);
        return this;
    }

    public Query<T> orderBy(String column) {
        orderBy.add(column);
        return this;
    }

    public Query<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public String build() {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + entity.getSimpleName());
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }
        if (!orderBy.isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", orderBy));
        }
        if (limit != null) {
            sql.append(" LIMIT ").append(limit);
        }
        return sql.toString();
    }
}

// Usage
String query = QueryBuilder.select(User.class)
    .where("age > 18")
    .where("active = true")
    .orderBy("name")
    .limit(10)
    .build();
```

---

## Best Practices

### Do

```java
// 1. Use factory method names that describe what's created
Color color = Color.fromHex("#FF0000");
List<String> list = List.of("a", "b");

// 2. Keep factory methods static for simple cases
public static Connection create(String type) { ... }

// 3. Use abstract factory for families of related objects
UIFactory factory = new DarkUIFactory();

// 4. Register new types dynamically
factory.register("custom", CustomConnection::new);
```

### Don't

```java
// 1. Don't create factories for single simple classes
// BAD: Factory for one type
public class UserFactory {
    public User create(String name) { return new User(name); }
}

// GOOD: Just use constructor
User user = new User("Alice");

// 2. Don't make factory logic too complex
// Keep factory methods focused and simple

// 3. Don't forget to handle unknown types
// Always provide meaningful error messages
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Factory Method** | Creates objects without specifying concrete class |
| **Abstract Factory** | Creates families of related objects |
| **Static Factory** | Named methods instead of constructors |
| **Parameterized** | Configure factory at runtime |
| **Loose Coupling** | Client depends on interface |
| **Open/Closed** | Add new products without modifying client |
| **Single Responsibility** | Centralize creation logic |
| **Testability** | Easy to mock factory |
| **Registration** | Dynamic type registration |
| **Naming** | Use descriptive factory method names |
