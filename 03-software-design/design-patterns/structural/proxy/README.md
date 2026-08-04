# Proxy Pattern

The Proxy pattern provides a placeholder for another object to control access to it. It adds a level of indirection for lazy loading, access control, caching, and more.

## Table of Contents

1. [Concepts](#concepts)
2. [Virtual Proxy (Lazy Loading)](#virtual-proxy)
3. [Protection Proxy](#protection-proxy)
4. [Caching Proxy](#caching-proxy)
5. [Remote Proxy](#remote-proxy)
6. [Best Practices](#best-practices)
7. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Proxy?

Proxy controls access to the real subject. Client interacts with proxy, which may delegate to real object.

```
Client ──▶ Proxy ──▶ RealSubject
```

### Types of Proxy

- **Virtual** - lazy loading (creates on demand)
- **Protection** - access control
- **Caching** - cache results
- **Remote** - network access
- **Logging** - log operations

---

## Virtual Proxy

### Lazy Loading

```java
// Interface
public interface Image {
    void display();
}

// Real subject - expensive to create
public class HighResImage implements Image {
    private final String filename;

    public HighResImage(String filename) {
        this.filename = filename;
        loadFromDisk();  // Expensive!
    }

    private void loadFromDisk() {
        System.out.println("Loading " + filename + " from disk...");
        // Simulate expensive loading
    }

    @Override
    public void display() {
        System.out.println("Displaying " + filename);
    }
}

// Virtual proxy - lazy initialization
public class ImageProxy implements Image {
    private HighResImage realImage;
    private final String filename;

    public ImageProxy(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new HighResImage(filename);  // Created on demand
        }
        realImage.display();
    }
}

// Usage
Image image = new ImageProxy("photo.jpg");  // No loading yet
// ... do other work ...
image.display();  // Loads and displays
```

---

## Protection Proxy

### Access Control

```java
public interface Document {
    String read();
    void write(String content);
}

public class RealDocument implements Document {
    private String content;

    public RealDocument(String content) {
        this.content = content;
    }

    @Override
    public String read() { return content; }

    @Override
    public void write(String content) { this.content = content; }
}

public class ProtectionProxy implements Document {
    private final RealDocument document;
    private final String userRole;

    public ProtectionProxy(RealDocument document, String userRole) {
        this.document = document;
        this.userRole = userRole;
    }

    @Override
    public String read() {
        if (!userRole.equals("ADMIN") && !userRole.equals("READER")) {
            throw new SecurityException("Access denied");
        }
        return document.read();
    }

    @Override
    public void write(String content) {
        if (!userRole.equals("ADMIN")) {
            throw new SecurityException("Write access denied");
        }
        document.write(content);
    }
}

// Usage
Document adminDoc = new ProtectionProxy(new RealDocument("secret"), "ADMIN");
Document readerDoc = new ProtectionProxy(new RealDocument("secret"), "READER");

adminDoc.read();     // OK
adminDoc.write("new"); // OK
readerDoc.read();    // OK
readerDoc.write("new"); // SecurityException!
```

---

## Caching Proxy

### Cache Results

```java
public interface WeatherService {
    double getTemperature(String city);
}

public class RealWeatherService implements WeatherService {
    @Override
    public double getTemperature(String city) {
        System.out.println("Fetching temperature for " + city);
        // Simulate API call
        return 72.0 + city.hashCode() % 10;
    }
}

public class CachingWeatherProxy implements WeatherService {
    private final WeatherService realService;
    private final Map<String, Double> cache = new HashMap<>();
    private final Duration ttl;
    private final Map<String, Instant> timestamps = new HashMap<>();

    public CachingWeatherProxy(WeatherService realService, Duration ttl) {
        this.realService = realService;
        this.ttl = ttl;
    }

    @Override
    public double getTemperature(String city) {
        Instant now = Instant.now();
        Instant cachedTime = timestamps.get(city);

        if (cachedTime != null && now.minus(ttl).isBefore(cachedTime)) {
            System.out.println("Cache hit for " + city);
            return cache.get(city);
        }

        double temp = realService.getTemperature(city);
        cache.put(city, temp);
        timestamps.put(city, now);
        return temp;
    }
}

// Usage
WeatherService service = new CachingWeatherProxy(
    new RealWeatherService(), Duration.ofMinutes(5));

service.getTemperature("NYC");  // Fetches from API
service.getTemperature("NYC");  // Cache hit
```

---

## Remote Proxy

### Network Access

```java
// Interface
public interface RemoteService {
    String fetchData(String id);
}

// Remote proxy - handles network communication
public class RemoteServiceProxy implements RemoteService {
    private final String baseUrl;

    public RemoteServiceProxy(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String fetchData(String id) {
        // Handle network communication
        try {
            URL url = new URL(baseUrl + "/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
            return reader.lines().collect(Collectors.joining());
        } catch (Exception e) {
            throw new RuntimeException("Remote call failed", e);
        }
    }
}

// Usage
RemoteService service = new RemoteServiceProxy("https://api.example.com");
String data = service.fetchData("123");  // Network call hidden
```

---

## Best Practices

### Do

```java
// 1. Keep proxy interface same as real subject
public class Proxy implements Subject {
    private final RealSubject real;

    @Override
    public void operation() {
        // Pre-processing
        real.operation();
        // Post-processing
    }
}

// 2. Use for cross-cutting concerns
// Logging, security, caching
```

### Don't

```java
// 1. Don't add business logic to proxy
// Proxy handles infrastructure, not business rules

// 2. Don't create too many proxy layers
// Performance impact from indirection

// 3. Don't forget to delegate to real subject
public class Proxy implements Subject {
    @Override
    public void operation() {
        // Missing: real.operation();
    }
}
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Proxy** | Controls access to real subject |
| **Virtual** | Lazy loading |
| **Protection** | Access control |
| **Caching** | Cache results |
| **Remote** | Network access |
| **Same Interface** | Proxy implements same interface |
| **Delegation** | Proxy delegates to real subject |
| **Cross-Cutting** | Logging, security, caching |
