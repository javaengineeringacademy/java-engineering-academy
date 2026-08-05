# Java Patterns

> Java-specific patterns: Builder with generics, enum singleton, try-with-resources, and more.

## 1. Builder Pattern with Generics

```java
public class Builder<T> {
    private final Supplier<T> constructor;
    private final Map<String, Consumer<T>> setters = new HashMap<>();
    private final Map<String, Object> values = new HashMap<>();

    public Builder(Supplier<T> constructor) {
        this.constructor = constructor;
    }

    public <V> Builder<T> with(String property, Consumer<T> setter, V value) {
        setters.put(property, setter);
        values.put(property, value);
        return this;
    }

    public T build() {
        T instance = constructor.get();
        setters.forEach((key, setter) -> setter.accept(instance));
        return instance;
    }
}

// Usage
User user = new Builder<>(User::new)
    .with("name", User::setName, "John")
    .with("email", User::setEmail, "john@example.com")
    .build();
```

## 2. Enum Singleton

```java
public enum Singleton {
    INSTANCE;

    private int value;

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
}

// Thread-safe, serialization-safe, reflection-safe
Singleton.INSTANCE.setValue(42);
```

## 3. Try-with-resources

```java
// Custom AutoCloseable
public class DatabaseConnection implements AutoCloseable {
    private Connection conn;

    public DatabaseConnection(String url) throws SQLException {
        this.conn = DriverManager.getConnection(url);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    @Override
    public void close() throws SQLException {
        if (conn != null) conn.close();
    }
}

// Usage
try (var db = new DatabaseConnection(url);
     var stmt = db.prepareStatement(sql)) {
    // resources auto-closed
}
```

## 4. Null Object Pattern

```java
public interface Logger {
    void log(String message);
}

public class ConsoleLogger implements Logger {
    @Override
    public void log(String message) { System.out.println(message); }
}

public class NullLogger implements Logger {
    @Override
    public void log(String message) { /* no-op */ }
}

// Usage
Logger logger = debug ? new ConsoleLogger() : new NullLogger();
logger.log("debug info");  // no null checks needed
```

## 5. Strategy Pattern with Lambda

```java
public class Sorter<T> {
    private final Comparator<T> comparator;

    public Sorter(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public List<T> sort(List<T> list) {
        return list.stream().sorted(comparator).collect(Collectors.toList());
    }
}

// Usage
Sorter<String> byLength = new Sorter<>(Comparator.comparingInt(String::length));
Sorter<String> alphabetical = new Sorter<>(Comparator.naturalOrder());
```

## 6. Observer Pattern

```java
public interface EventListener<T> {
    void onEvent(T event);
}

public class EventEmitter<T> {
    private final List<EventListener<T>> listeners = new ArrayList<>();

    public void subscribe(EventListener<T> listener) {
        listeners.add(listener);
    }

    public void emit(T event) {
        listeners.forEach(listener -> listener.onEvent(event));
    }
}

// Usage
EventEmitter<Order> orderEmitter = new EventEmitter<>();
orderEmitter.subscribe(order -> sendEmail(order));
orderEmitter.subscribe(order -> updateInventory(order));
orderEmitter.emit(new Order("123"));
```

## 7. Proxy Pattern

```java
public interface UserRepository {
    User findById(String id);
    void save(User user);
}

public class LoggingProxy implements UserRepository {
    private final UserRepository delegate;

    public LoggingProxy(UserRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public User findById(String id) {
        long start = System.currentTimeMillis();
        User result = delegate.findById(id);
        log("findById took " + (System.currentTimeMillis() - start) + "ms");
        return result;
    }

    @Override
    public void save(User user) {
        log("Saving user: " + user.getId());
        delegate.save(user);
    }
}
```

## 8. Factory Pattern

```java
public interface PaymentProcessor {
    void process(BigDecimal amount);
}

public class PaymentProcessorFactory {
    private static final Map<String, Supplier<PaymentProcessor>> processors = Map.of(
        "credit", CreditCardProcessor::new,
        "paypal", PayPalProcessor::new,
        "crypto", CryptoProcessor::new
    );

    public static PaymentProcessor create(String type) {
        Supplier<PaymentProcessor> supplier = processors.get(type);
        if (supplier == null) throw new IllegalArgumentException("Unknown: " + type);
        return supplier.get();
    }
}
```

## 9. Decorator Pattern

```java
public interface DataSource {
    void writeData(String data);
    String readData();
}

public class FileDataSource implements DataSource {
    private final String filename;
    // implementation
}

public class EncryptionDecorator implements DataSource {
    private final DataSource delegate;

    public EncryptionDecorator(DataSource delegate) {
        this.delegate = delegate;
    }

    @Override
    public void writeData(String data) {
        delegate.writeData(encrypt(data));
    }

    @Override
    public String readData() {
        return decrypt(delegate.readData());
    }
}

// Usage
DataSource source = new EncryptionDecorator(new FileDataSource("file.txt"));
```

## 10. Command Pattern

```java
public interface Command {
    void execute();
    void undo();
}

public class CompositeCommand implements Command {
    private final List<Command> commands = new ArrayList<>();

    public void add(Command command) { commands.add(command); }

    @Override
    public void execute() { commands.forEach(Command::execute); }

    @Override
    public void undo() { commands.reversed().forEach(Command::undo); }
}
```

## References

- [Head First Design Patterns](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Refactoring.Guru](https://refactoring.guru/design-patterns)

---
**Prerequisites:** [Java core-concepts](core-concepts.md)
**Related:** [Java best-practices](best-practices.md) | [Java patterns](patterns.md)
**Next:** [Java interview](interview.md)
