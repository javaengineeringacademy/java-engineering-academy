# Serialization - Hard Exercises

## Exercise 1: Version-Compatible Serialization

### Problem Statement
Implement version-compatible serialization that handles class evolution without breaking deserialization of older versions.

### Requirements
1. Create a `Config` class with multiple fields
2. Add `serialVersionUID` for version control
3. Implement version-compatible serialization:
   - Add new fields in newer versions
   - Maintain backward compatibility
   - Handle missing fields gracefully
4. Test with different versions of the class

### Starter Code
```java
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// Version 1 of the class
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;

    private String appName;
    private int version;
    private Map<String, String> settings;

    public Config(String appName, int version) {
        this.appName = appName;
        this.version = version;
        this.settings = new HashMap<>();
    }

    // TODO: Implement custom serialization for version compatibility

    // Getters and setters

    @Override
    public String toString() {
        return "Config{" +
                "appName='" + appName + '\'' +
                ", version=" + version +
                ", settings=" + settings +
                '}';
    }

    // Version 2 would add:
    // - private String environment;
    // - private boolean debugMode;
    // - private long timestamp;
}
```

### Expected Behavior
```java
// Version 1 object
Config v1Config = new Config("MyApp", 1);
v1Config.getSettings().put("theme", "dark");

// Serialize with version 1
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("config_v1.dat"));
oos.writeObject(v1Config);
oos.close();

// Simulate version 2 (add new fields)
// Update the Config class with new fields
// Config v2Config = ... (with new fields)

// Deserialize - should work even with new fields
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("config_v1.dat"));
Config loaded = (Config) ois.readObject();
ois.close();

System.out.println("Loaded: " + loaded);

// Verify backward compatibility
System.out.println("AppName preserved: " + loaded.getAppName().equals("MyApp"));
System.out.println("Settings preserved: " + loaded.getSettings().get("theme").equals("dark"));
```

### Hints
- Always define `serialVersionUID`
- New fields should have default values
- Use `defaultReadObject()` to handle unknown fields gracefully
- Consider using `ObjectInputStream.GetField` for fine-grained control

### Evaluation Criteria
- [ ] `serialVersionUID` is defined
- [ ] New fields have default values
- [ ] Backward compatibility is maintained
- [ ] Missing fields don't cause exceptions
- [ ] Code handles version differences gracefully

---

## Exercise 2: Serialization Security Wrapper

### Problem Statement
Create a security wrapper for serialization that prevents common attacks and validates data integrity.

### Requirements
1. Create a `SecureSerializer` wrapper class
2. Implement security measures:
   - Whitelist allowed classes
   - Validate object structure
   - Detect tampering
   - Handle malicious payloads
3. Create custom `ObjectInputStream` that validates classes
4. Log security events

### Starter Code
```java
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class SecureSerializer {

    private static final Set<String> ALLOWED_CLASSES = new HashSet<>();

    static {
        // Whitelist allowed classes
        ALLOWED_CLASSES.add("com.example.User");
        ALLOWED_CLASSES.add("com.example.Address");
        ALLOWED_CLASSES.add("java.util.ArrayList");
        ALLOWED_CLASSES.add("java.util.HashMap");
    }

    // TODO: Create custom ObjectInputStream with class validation

    // TODO: Implement secure serialization method

    // TODO: Implement secure deserialization method

    // TODO: Implement integrity check (e.g., checksum)

    // Helper method to validate class
    private static boolean isClassAllowed(String className) {
        // TODO: Implement class whitelist validation
    }
}

// Custom ObjectInputStream that validates classes
class ValidatingObjectInputStream extends ObjectInputStream {

    public ValidatingObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    // TODO: Override resolveClass() to validate classes
}
```

### Expected Behavior
```java
// Create a safe object
User user = new User("Alice", "alice@email.com");

// Serialize securely
SecureSerializer.serialize(user, "secure_user.dat");
System.out.println("Serialized securely");

// Deserialize with validation
User loaded = SecureSerializer.deserialize("secure_user.dat", User.class);
System.out.println("Deserialized: " + loaded);

// Try to deserialize malicious data (should fail)
try {
    // Create a file with malicious class reference
    // SecureSerializer.deserialize("malicious.dat", User.class);
} catch (SecurityException e) {
    System.out.println("Security exception caught: " + e.getMessage());
}
```

### Hints
- Override `resolveClass()` in custom `ObjectInputStream` to check class names
- Use a whitelist approach (only allow known safe classes)
- Consider using `ObjectInputStreamFilter` for newer Java versions
- Log all security events for auditing

### Evaluation Criteria
- [ ] Class whitelist is implemented
- [ ] Custom `ObjectInputStream` validates classes
- [ ] Malicious payloads are rejected
- [ ] Security events are logged
- [ ] System is resilient to common deserialization attacks

---

## Exercise 3: Serialization Performance Benchmark

### Problem Statement
Create a comprehensive benchmark comparing different serialization approaches and identify performance characteristics.

### Requirements
1. Implement multiple serialization methods:
   - Java built-in serialization
   - JSON serialization (using Gson or Jackson)
   - Protocol Buffers (if available)
   - Custom binary serialization
2. Measure performance metrics:
   - Serialization time
   - Deserialization time
   - File size
   - Memory usage
3. Test with different object sizes
4. Generate a performance report

### Starter Code
```java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializationBenchmark {

    // Test class with various field types
    static class BenchmarkObject implements Serializable {
        private static final long serialVersionUID = 1L;

        private int id;
        private String name;
        private double value;
        private List<String> tags;
        private byte[] data;

        public BenchmarkObject(int id) {
            this.id = id;
            this.name = "Object_" + id;
            this.value = id * 1.5;
            this.tags = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                this.tags.add("tag_" + i);
            }
            this.data = new byte[1024]; // 1KB of data
        }
    }

    // TODO: Implement Java serialization benchmark

    // TODO: Implement JSON serialization benchmark (if Gson/Jackson available)

    // TODO: Implement custom binary serialization benchmark

    // TODO: Implement Protocol Buffers benchmark (if available)

    public static void main(String[] args) {
        int iterations = 10000;
        int objectCount = 100;

        // Create test objects
        List<BenchmarkObject> objects = new ArrayList<>();
        for (int i = 0; i < objectCount; i++) {
            objects.add(new BenchmarkObject(i));
        }

        System.out.println("Serialization Benchmark");
        System.out.println("======================");
        System.out.println("Objects: " + objectCount);
        System.out.println("Iterations: " + iterations);
        System.out.println();

        // TODO: Run benchmarks and collect results

        // TODO: Generate performance report
    }

    // Helper methods for benchmarking
    private static long measureSerializationTime(Runnable serializer, int iterations) {
        // TODO: Implement time measurement
    }

    private static long measureDeserializationTime(Runnable deserializer, int iterations) {
        // TODO: Implement time measurement
    }

    private static long getFileSize(String filename) {
        // TODO: Implement file size measurement
    }

    private static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
```

### Expected Behavior
```
Serialization Benchmark Results
===============================
| Method              | Serialize (ms) | Deserialize (ms) | File Size (KB) | Memory (MB) |
|---------------------|----------------|------------------|----------------|-------------|
| Java Serialization  | 450            | 380              | 256            | 12.5        |
| JSON (Gson)         | 680            | 520              | 512            | 15.2        |
| Custom Binary       | 180            | 150              | 128            | 8.3         |
| Protocol Buffers    | 95             | 85               | 64             | 5.1         |

Analysis:
- Protocol Buffers is fastest and most compact
- Java serialization has high overhead
- JSON is human-readable but slower
- Custom binary offers best control
```

### Hints
- Use `System.nanoTime()` for precise timing
- Run multiple iterations and average results
- Consider JVM warm-up effects
- Measure file sizes using `File.length()`
- Monitor memory with `Runtime.getRuntime()`

### Evaluation Criteria
- [ ] Multiple serialization methods are implemented
- [ ] Performance metrics are accurately measured
- [ ] Results are presented in a clear format
- [ ] Analysis includes trade-offs and recommendations
- [ ] Code is reusable and well-organized
- [ ] Considerations for different use cases are documented
