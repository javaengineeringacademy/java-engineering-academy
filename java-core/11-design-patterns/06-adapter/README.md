# Adapter Pattern

## 1. Introduction

The Adapter Pattern is a structural design pattern that allows objects with incompatible interfaces to collaborate. It acts as a bridge between two incompatible interfaces by wrapping an existing class with a new interface.

The Adapter pattern is particularly useful when you need to integrate third-party libraries, legacy systems, or any class whose interface doesn't match what your client code expects.

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Implement class and object adapters in Java
- Understand when to use adapters for interface compatibility
- Convert between different data formats and interfaces
- Recognize adapter usage in Java standard library
- Handle legacy system integration with adapters

---

## 3. Prerequisites

- Understanding of interfaces and abstract classes
- Knowledge of inheritance and composition
- Familiarity with interface design
- Understanding of loose coupling principles

---

## 4. Why This Concept Exists

The Adapter pattern exists because:

- **Interface incompatibility**: Third-party code has different interfaces
- **Legacy integration**: Old systems need to work with new code
- **Reusability**: Adapt existing classes without modifying them
- **Flexibility**: Work with multiple implementations
- **Standardization**: Provide uniform interface across different classes

Without Adapter, you'd need to modify existing code or create incompatible systems.

---

## 5. Problem Statement

Consider integrating different payment gateways:

```java
// Your interface
public interface PaymentProcessor {
    PaymentResult processPayment(BigDecimal amount, String currency);
}

// Third-party PayPal SDK (incompatible interface)
public class PayPalSDK {
    public PayPalResponse makePayment(double amt, String cur, String apiKey) {
        // PayPal's interface
    }
}

// Third-party Stripe SDK (incompatible interface)
public class StripeSDK {
    public StripeCharge chargeCard(BigDecimal amount, String token) {
        // Stripe's interface
    }
}

// Problem: Cannot use PayPalSDK and StripeSDK directly
// They don't implement PaymentProcessor
```

---

## 6. Theory

### 6.1 Class vs. Object Adapter

| Type | Implementation | Inheritance | Composition |
|------|----------------|-------------|-------------|
| Class Adapter | Extends target class | Uses multiple inheritance | No |
| Object Adapter | Uses composition | Single inheritance | Yes |

### 6.2 Adapter vs. Facade vs. Proxy

| Pattern | Purpose | Interface Change |
|---------|---------|------------------|
| Adapter | Convert interface | Yes |
| Facade | Simplify interface | Yes (simpler) |
| Proxy | Control access | No (same interface) |

---

## 7. Internal Working

### 7.1 Adapter Flow

```
Client → Target Interface → Adapter → Adaptee (incompatible interface)
```

### 7.2 Object Adapter Structure

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│    Client    │────→│   Adapter    │────→│   Adaptee    │
│              │     │  (implements │     │  (existing)  │
│              │     │   Target)    │     │              │
└──────────────┘     └──────────────┘     └──────────────┘
```

---

## 8. JVM Perspective

### 8.1 Method Dispatch

- Adapter calls adaptee methods
- Virtual method table used for interface dispatch
- No special JVM support needed

### 8.2 Memory Impact

- Adapter adds one object per adaptee
- Composition approach: Reference to adaptee
- Minimal overhead

---

## 9. Memory Representation

### 9.1 Adapter Memory Model

```
┌─────────────────────────────────────┐
│             Client                  │
└──────────────┬──────────────────────┘
               │ references
               ↓
┌─────────────────────────────────────┐
│        Target Interface             │
└──────────────┬──────────────────────┘
               │ implemented by
               ↓
┌─────────────────────────────────────┐
│           Adapter                   │
│  - adaptee: Adaptee                 │
│  + method(): Delegates to adaptee   │
└──────────────┬──────────────────────┘
               │ wraps
               ↓
┌─────────────────────────────────────┐
│           Adaptee                   │
│  (incompatible interface)           │
└─────────────────────────────────────┘
```

---

## 10. Syntax

### 10.1 Object Adapter

```java
public interface Target {
    void request();
}

public class Adaptee {
    public void specificRequest() {
        // Existing code
    }
}

public class Adapter implements Target {
    private final Adaptee adaptee;

    public Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void request() {
        adaptee.specificRequest();
    }
}
```

### 10.2 Class Adapter (using inheritance)

```java
public class ClassAdapter extends Adaptee implements Target {
    @Override
    public void request() {
        super.specificRequest();
    }
}
```

---

## 11. Easy Example

### Temperature Adapter

```java
// Target interface
public interface TemperatureConverter {
    double convert(double temperature);
}

// Adaptee (Fahrenheit to Celsius)
public class FahrenheitSensor {
    public double readFahrenheit() {
        return 98.6; // Body temperature in Fahrenheit
    }
}

// Adapter
public class FahrenheitToCelsiusAdapter implements TemperatureConverter {
    private final FahrenheitSensor sensor;

    public FahrenheitToCelsiusAdapter(FahrenheitSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public double convert(double temperature) {
        // Not used, we read from sensor
        return (sensor.readFahrenheit() - 32) * 5 / 9;
    }
}

// Usage
FahrenheitSensor sensor = new FahrenheitSensor();
TemperatureConverter converter = new FahrenheitToCelsiusAdapter(sensor);
double celsius = converter.convert(0);
System.out.println("Temperature: " + celsius + "°C");
```

---

## 12. Medium Example

### Payment Gateway Adapter

```java
// Target interface
public interface PaymentGateway {
    PaymentResult charge(BigDecimal amount, String currency, String token);
}

public record PaymentResult(boolean success, String transactionId, String message) {}

// Adaptee 1: PayPal
public class PayPalGateway {
    public PayPalResponse processPayment(double amountInCents, String currencyCode, String apiKey) {
        // PayPal's actual implementation
        return new PayPalResponse(true, "PP-" + System.currentTimeMillis());
    }
}

public record PayPalResponse(boolean status, String payPalId) {}

// Adaptee 2: Stripe
public class StripeGateway {
    public StripeCharge createCharge(BigDecimal amount, String source, String description) {
        // Stripe's actual implementation
        return new StripeCharge(true, "ch_" + System.currentTimeMillis());
    }
}

public record StripeCharge(boolean paid, String chargeId) {}

// Adapter for PayPal
public class PayPalAdapter implements PaymentGateway {
    private final PayPalGateway payPal;
    private final String apiKey;

    public PayPalAdapter(PayPalGateway payPal, String apiKey) {
        this.payPal = payPal;
        this.apiKey = apiKey;
    }

    @Override
    public PaymentResult charge(BigDecimal amount, String currency, String token) {
        PayPalResponse response = payPal.processPayment(
            amount.multiply(BigDecimal.valueOf(100)).doubleValue(),
            currency,
            apiKey
        );
        return new PaymentResult(response.status(), response.payPalId(), "PayPal charge");
    }
}

// Adapter for Stripe
public class StripeAdapter implements PaymentGateway {
    private final StripeGateway stripe;

    public StripeAdapter(StripeGateway stripe) {
        this.stripe = stripe;
    }

    @Override
    public PaymentResult charge(BigDecimal amount, String currency, String token) {
        StripeCharge charge = stripe.createCharge(amount, token, "Payment");
        return new PaymentResult(charge.paid(), charge.chargeId(), "Stripe charge");
    }
}

// Usage
PaymentGateway gateway = new PayPalAdapter(new PayPalGateway(), "api-key");
PaymentResult result = gateway.charge(BigDecimal.valueOf(99.99), "USD", "token");
```

---

## 13. Hard Example

### Legacy Database Adapter

```java
// Target interface (modern)
public interface UserRepository {
    Optional<User> findById(String id);
    List<User> findAll();
    void save(User user);
    void delete(String id);
}

public record User(String id, String name, String email) {}

// Adaptee (legacy database)
public class LegacyDatabase {
    public Vector<Hashtable<String, String>> queryUsers() {
        // Returns legacy data structure
        Vector<Hashtable<String, String>> result = new Vector<>();
        Hashtable<String, String> row = new Hashtable<>();
        row.put("ID", "1");
        row.put("NAME", "John");
        row.put("EMAIL", "john@example.com");
        result.add(row);
        return result;
    }

    public Hashtable<String, String> queryUserById(String id) {
        // Returns legacy data structure
        return new Hashtable<>();
    }

    public void insertUser(Hashtable<String, String> userData) {
        // Legacy insert
    }

    public void removeUser(String id) {
        // Legacy delete
    }
}

// Adapter
public class LegacyDatabaseAdapter implements UserRepository {
    private final LegacyDatabase legacyDb;

    public LegacyDatabaseAdapter(LegacyDatabase legacyDb) {
        this.legacyDb = legacyDb;
    }

    @Override
    public Optional<User> findById(String id) {
        Hashtable<String, String> row = legacyDb.queryUserById(id);
        if (row.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(convertToUser(row));
    }

    @Override
    public List<User> findAll() {
        Vector<Hashtable<String, String>> rows = legacyDb.queryUsers();
        return rows.stream()
            .map(this::convertToUser)
            .collect(Collectors.toList());
    }

    @Override
    public void save(User user) {
        Hashtable<String, String> data = new Hashtable<>();
        data.put("ID", user.id());
        data.put("NAME", user.name());
        data.put("EMAIL", user.email());
        legacyDb.insertUser(data);
    }

    @Override
    public void delete(String id) {
        legacyDb.removeUser(id);
    }

    private User convertToUser(Hashtable<String, String> row) {
        return new User(
            row.get("ID"),
            row.get("NAME"),
            row.get("EMAIL")
        );
    }
}

// Usage
LegacyDatabase legacyDb = new LegacyDatabase();
UserRepository repo = new LegacyDatabaseAdapter(legacyDb);
List<User> users = repo.findAll();
```

---

## 14. Enterprise Example

### Multi-Format Data Export Adapter

```java
// Target interface
public interface DataExporter {
    void export(List<Map<String, Object>> data, OutputStream output) throws IOException;
}

// Adaptee 1: CSV Library
public class CsvLibrary {
    public void writeCsv(List<String[]> rows, Writer writer) throws IOException {
        CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeAll(rows);
        csvWriter.close();
    }
}

// Adaptee 2: XML Library
public class XmlLibrary {
    public void generateXml(Element root, OutputStream output) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument();
        doc.appendChild(root);
        // Write to output
    }
}

// Adapter for CSV
public class CsvExporterAdapter implements DataExporter {
    private final CsvLibrary csvLibrary;

    public CsvExporterAdapter(CsvLibrary csvLibrary) {
        this.csvLibrary = csvLibrary;
    }

    @Override
    public void export(List<Map<String, Object>> data, OutputStream output) throws IOException {
        List<String[]> rows = new ArrayList<>();
        if (!data.isEmpty()) {
            // Add header
            rows.add(data.get(0).keySet().toArray(new String[0]));
            // Add data rows
            for (Map<String, Object> row : data) {
                rows.add(row.values().stream()
                    .map(Object::toString)
                    .toArray(String[]::new));
            }
        }
        csvLibrary.writeCsv(rows, new OutputStreamWriter(output));
    }
}

// Adapter for XML
public class XmlExporterAdapter implements DataExporter {
    private final XmlLibrary xmlLibrary;

    public XmlExporterAdapter(XmlLibrary xmlLibrary) {
        this.xmlLibrary = xmlLibrary;
    }

    @Override
    public void export(List<Map<String, Object>> data, OutputStream output) throws IOException {
        try {
            Element root = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .newDocument()
                .createElement("data");

            for (Map<String, Object> row : data) {
                Element rowElement = root.getOwnerDocument().createElement("row");
                row.forEach((key, value) -> {
                    Element field = root.getOwnerDocument().createElement(key);
                    field.setTextContent(String.valueOf(value));
                    rowElement.appendChild(field);
                });
                root.appendChild(rowElement);
            }

            xmlLibrary.generateXml(root, output);
        } catch (Exception e) {
            throw new IOException("XML export failed", e);
        }
    }
}

// Export Manager
public class ExportManager {
    private final Map<String, DataExporter> exporters = new HashMap<>();

    public void register(String format, DataExporter exporter) {
        exporters.put(format.toLowerCase(), exporter);
    }

    public void export(String format, List<Map<String, Object>> data, OutputStream output) throws IOException {
        DataExporter exporter = exporters.get(format.toLowerCase());
        if (exporter == null) {
            throw new IllegalArgumentException("Unsupported format: " + format);
        }
        exporter.export(data, output);
    }
}

// Usage
ExportManager manager = new ExportManager();
manager.register("csv", new CsvExporterAdapter(new CsvLibrary()));
manager.register("xml", new XmlExporterAdapter(new XmlLibrary()));

List<Map<String, Object>> data = List.of(
    Map.of("name", "John", "age", 30),
    Map.of("name", "Jane", "age", 25)
);

manager.export("csv", data, new FileOutputStream("output.csv"));
manager.export("xml", data, new FileOutputStream("output.xml"));
```

---

## 15. Performance

### 15.1 Performance Metrics

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| Method delegation | O(1) | Direct call |
| Data conversion | O(n) | n = data size |

### 15.2 Optimization Tips

1. **Minimize conversion**: Convert data only when needed
2. **Cache adapted objects**: Reuse adapters when possible
3. **Batch operations**: Process multiple items at once
4. **Lazy conversion**: Convert data on-demand

---

## 16. Best Practices

1. **Use composition over inheritance**: Object adapter is more flexible
2. **Keep adapter simple**: Only adapt, don't add business logic
3. **Document the adaptation**: Clearly explain what's being adapted
4. **Handle errors gracefully**: Convert adaptee exceptions appropriately
5. **Consider multiple adapters**: One for each incompatible interface
6. **Test both sides**: Test adapter with client and adaptee
7. **Use interfaces**: Program to target interface
8. **Consider lazy loading**: Load adaptee on-demand

---

## 17. Common Mistakes

1. **Adding business logic**: Adapter should only adapt, not add logic
2. **Ignoring exceptions**: Not converting adaptee exceptions
3. **Creating too many adapters**: One adapter per incompatible interface
4. **Overcomplicating**: Simple interface mismatch doesn't need complex adapter
5. **Not documenting**: Future developers need to understand the adaptation

---

## 18. Pitfalls

- **Performance overhead**: Extra indirection
- **Complexity**: More classes to maintain
- **Data loss**: Conversion may lose information
- **Debugging difficulty**: Extra layer makes debugging harder
- **Versioning issues**: Adaptee changes may break adapter

---

## 19. Debugging Tips

1. **Log method calls**: Track delegation between adapter and adaptee
2. **Verify data conversion**: Ensure data is converted correctly
3. **Test edge cases**: Null values, empty collections
4. **Use debugger**: Step through adapter methods
5. **Document mapping**: Keep mapping documentation updated

---

## 20. Comparison Table

| Pattern | Interface Change | Purpose | Complexity |
|---------|------------------|---------|------------|
| Adapter | Yes | Incompatible interfaces | Medium |
| Facade | Yes | Simplify complex subsystem | Low |
| Proxy | No | Control access | Medium |
| Decorator | No | Add behavior | Medium |

---

## 21. Decision Tree

```
Need to use incompatible interface?
├── Third-party library? → Adapter
├── Legacy system? → Adapter
├── Simplify complex interface? → Facade
├── Control access? → Proxy
└── Add behavior? → Decorator
```

---

## 22. Interview Questions

### Q1: What is the Adapter pattern?
**Answer**: A structural pattern that converts one interface to another, allowing classes with incompatible interfaces to work together.

### Q2: Class adapter vs. object adapter?
**Answer**: Class adapter uses multiple inheritance, object adapter uses composition. Object adapter is more flexible and preferred in Java.

### Q3: When would you use Adapter over Facade?
**Answer**: Adapter converts one interface to another. Facade simplifies a complex subsystem. Use Adapter when you need interface compatibility, Facade for simplification.

### Q4: Can an adapter work with multiple adaptees?
**Answer**: Yes, but it becomes complex. Consider creating separate adapters for each adaptee.

### Q5: What are real-world examples of Adapter?
**Answer**: InputStreamReader (InputStream to Reader), Arrays.asList(), Collections.unmodifiableList().

---

## 23. Exercises

### Exercise 1: Simple Adapter
Create an adapter for a legacy logger class to work with modern logging interface.

### Exercise 2: Data Format Adapter
Create adapters for converting between JSON, XML, and CSV formats.

### Exercise 3: API Adapter
Create adapters for multiple payment gateway APIs to work with a common interface.

---

## 24. Assignments

1. **Assignment 1**: Create an adapter for a legacy database to work with modern ORM
2. **Assignment 2**: Build adapters for multiple notification services (Email, SMS, Push)
3. **Assignment 3**: Create adapters for different file format readers

---

## 25. Mini Project

### Multi-Vendor Integration System
Create a system that:
- Integrates multiple payment gateways (PayPal, Stripe, Square)
- Uses adapters for each gateway
- Provides common interface for all payments
- Handles different data formats
- Is extensible for new gateways

---

## 26. Summary

- Adapter converts incompatible interfaces
- Object adapter (composition) preferred over class adapter
- Useful for third-party library integration
- Keep adapters simple, no business logic
- Document the adaptation clearly
- Consider Facade for simplification

---

## 27. References

1. Gamma, E., et al. (1994). *Design Patterns*, Chapter 7
2. Bloch, J. (2018). *Effective Java*, Item 42
3. Refactoring Guru: https://refactoring.guru/design-patterns/adapter
4. Java Design Patterns: https://java-design-patterns.com/patterns/adapter/
