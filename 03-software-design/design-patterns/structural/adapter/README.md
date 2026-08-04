# Adapter Pattern

The Adapter pattern converts the interface of a class into another interface clients expect. It allows classes with incompatible interfaces to work together.

## Table of Contents

1. [Concepts](#concepts)
2. [Class Adapter](#class-adapter)
3. [Object Adapter](#object-adapter)
4. [Interface Conversion](#interface-conversion)
5. [Best Practices](#best-practices)
6. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Adapter?

Adapter wraps an existing class with a new interface so it becomes compatible with client code.

```
Client ──▶ Adapter ──▶ Adaptee
  (new)      (bridge)    (existing)
```

### When to Use

- Integrating third-party libraries with different interfaces
- Working with legacy code
- Unifying different APIs
- Converting data formats

---

## Class Adapter

### Using Inheritance

```java
// Target interface
public interface MediaPlayer {
    void play(String filename);
}

// Adaptee - existing class with incompatible interface
public class VlcPlayer {
    public void playVlc(String filename) {
        System.out.println("Playing VLC: " + filename);
    }
}

// Adapter using inheritance
public class VlcAdapter extends VlcPlayer implements MediaPlayer {
    @Override
    public void play(String filename) {
        playVlc(filename);  // Delegate to adaptee
    }
}

// Usage
MediaPlayer player = new VlcAdapter();
player.play("movie.mkv");
```

---

## Object Adapter

### Using Composition

```java
// Target interface
public interface Logger {
    void log(String message);
}

// Adaptee - existing logging library
public class ApacheLogger {
    public void apacheLog(String level, String message) {
        System.out.println("[" + level + "] " + message);
    }
}

// Object adapter using composition
public class LoggerAdapter implements Logger {
    private final ApacheLogger adaptee;

    public LoggerAdapter(ApacheLogger adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void log(String message) {
        adaptee.apacheLog("INFO", message);
    }
}

// Usage
Logger logger = new LoggerAdapter(new ApacheLogger());
logger.log("Application started");
```

---

## Interface Conversion

### Third-Party Library Adapter

```java
// Your interface
public interface EmailService {
    void sendEmail(String to, String subject, String body);
}

// Third-party library with different interface
public class SmtpClient {
    public void send(String from, String to, String message) {
        System.out.println("SMTP: " + from + " -> " + to + ": " + message);
    }
}

// Adapter
public class SmtpAdapter implements EmailService {
    private final SmtpClient client;
    private final String fromAddress;

    public SmtpAdapter(SmtpClient client, String fromAddress) {
        this.client = client;
        this.fromAddress = fromAddress;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        String message = "Subject: " + subject + "\n\n" + body;
        client.send(fromAddress, to, message);
    }
}

// Usage
EmailService emailService = new SmtpAdapter(new SmtpClient(), "noreply@example.com");
emailService.sendEmail("user@example.com", "Welcome", "Hello!");
```

### Collections Adapter

```java
// Your interface
public interface ReadOnlyList<T> {
    T get(int index);
    int size();
}

// Adaptee - existing mutable list
public class ArrayList<T> implements List<T> { ... }

// Adapter - makes mutable list read-only
public class ReadOnlyListAdapter<T> implements ReadOnlyList<T> {
    private final List<T> adaptee;

    public ReadOnlyListAdapter(List<T> adaptee) {
        this.adaptee = List.copyOf(adaptee);  // Defensive copy
    }

    @Override
    public T get(int index) {
        return adaptee.get(index);
    }

    @Override
    public int size() {
        return adaptee.size();
    }
}
```

---

## Best Practices

### Do

```java
// 1. Keep adapter focused
public class PaymentAdapter implements PaymentGateway {
    private final LegacyPaymentSystem legacy;

    @Override
    public PaymentResult charge(CreditCard card, double amount) {
        return legacy.processPayment(card.getNumber(), amount);
    }
}

// 2. Use composition over inheritance
public class Adapter implements Target {
    private final Adaptee adaptee;
    // ...
}

// 3. Document the adaptation
// This adapter converts SmtpClient to EmailService interface
public class SmtpAdapter implements EmailService { ... }
```

### Don't

```java
// 1. Don't add functionality in adapter
// Adapter should only convert interface, not add behavior

// 2. Don't create too many adapters
// Consider refactoring if many adapters are needed

// 3. Don't expose adaptee through adapter
public class BadAdapter implements Target {
    public Adaptee getAdaptee() { return adaptee; }  // Don't do this
}
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Adapter** | Converts one interface to another |
| **Class Adapter** | Uses inheritance |
| **Object Adapter** | Uses composition (preferred) |
| **Target** | Interface client expects |
| **Adaptee** | Existing class with incompatible interface |
| **Use Cases** | Legacy integration, third-party libraries |
| **Composition** | Prefer over inheritance |
| **Focused** | Only convert interface, don't add behavior |
