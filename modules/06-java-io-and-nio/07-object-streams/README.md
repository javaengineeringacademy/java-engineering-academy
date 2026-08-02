# 07 - Object Streams in Java IO

## 1. Introduction

Object streams enable serialization and deserialization of Java objects—converting objects to byte streams and back. This is essential for persisting object state to files, transmitting objects over networks, and implementing deep copies. Java provides `ObjectInputStream` and `ObjectOutputStream` for this purpose. Understanding object serialization is crucial for building robust Java applications.

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Serialize and deserialize Java objects
- Implement the Serializable interface
- Control serialization with transient and serialVersionUID
- Handle custom serialization
- Understand the security implications of deserialization
- Implement deep copy using object streams

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of object-oriented concepts
- Familiarity with byte streams (Topic 03)
- Basic knowledge of exception handling

## 4. Why This Concept Exists

Object serialization solves several critical problems:

| Problem | Solution |
|---------|----------|
| Object persistence | Save object state to files |
| Network transmission | Send objects between JVMs |
| Deep copy | Create independent copies of objects |
| Caching | Store objects in memory-mapped files |
| Message passing | Implement messaging systems |

Without serialization, you'd need to manually convert each object field to bytes and reconstruct them—tedious and error-prone.

## 5. Problem Statement

Consider these scenarios:
1. Saving application state (game progress, user preferences)
2. Sending objects between microservices
3. Implementing undo/redo functionality
4. Caching expensive computations
5. Implementing distributed systems

All require converting objects to bytes and back. Object streams provide this capability.

## 6. Theory

### 6.1 Serialization Basics

```
Java Object → ObjectOutputStream → byte[] → File/Network
byte[] → ObjectInputStream → Java Object
```

### 6.2 The Serializable Interface

```java
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    private transient String password; // Not serialized
}
```

### 6.3 Serialization Mechanisms

| Mechanism | Interface | Control | Use Case |
|-----------|-----------|---------|----------|
| Default | Serializable | None | Simple objects |
| Custom | Externalizable | Full | Complex objects |
| Partial | Serializable | Field-level | Selective serialization |

### 6.4 serialVersionUID

```java
private static final long serialVersionUID = 1L;
```

This ID ensures that serialized objects can be deserialized with compatible classes. Without it, Java generates one automatically, which may break compatibility.

## 7. Internal Working

### 7.1 How ObjectOutputStream Writes Objects

```
ObjectOutputStream.writeObject(object):
    ↓
Check if object is Serializable
    ↓
Write class descriptor
    ↓
Write object fields recursively
    ↓
Handle reference sharing (circular references)
    ↓
Write to underlying byte stream
```

### 7.2 How ObjectInputStream Reads Objects

```
ObjectInputStream.readObject():
    ↓
Read class descriptor
    ↓
Create object instance (without calling constructor)
    ↓
Read and set object fields
    ↓
Resolve references
    ↓
Return reconstructed object
```

### 7.3 Reference Tracking

Object streams track object references to handle circular references:

```
Object A → references Object B
Object B → references Object A (circular!)

ObjectOutputStream writes:
1. Write A (new)
2. Write B (new)
3. Write reference to A (already seen)
4. Write reference to B (already seen)
```

## 8. JVM Perspective

### 8.1 Memory Allocation

```
JVM Heap:
├── ObjectOutputStream object (64 bytes)
├── Internal byte array buffer
├── Class descriptor cache
└── Object graph being serialized

Native Memory:
├── File descriptor (if file)
└── Socket buffer (if network)
```

### 8.2 Serialization and GC

- Serialized objects create copies in byte form
- Original objects are still eligible for GC
- Deserialized objects are new heap objects
- Circular references don't cause memory leaks (handled by ObjectOutputStream)

## 9. Memory Representation

### Serialized Object Format

```
Serialized User object:
┌─────────────────────────────────────────┐
│ TC_OBJECT (0x73)                        │
│ Class descriptor                        │
│   - Class name                          │
│   - serialVersionUID                    │
│   - Field descriptors                   │
│ Object data                             │
│   - name (String)                       │
│   - age (int)                           │
│   - password (null, transient)          │
└─────────────────────────────────────────┘
```

### Field Encoding

```
int age = 25:
  TC_INT (0x4B)
  [0x00][0x00][0x00][0x19] (25 in big-endian)

String name = "Alice":
  TC_STRING (0x74)
  [0x00][0x05] (length 5)
  [0x41][0x6C][0x69][0x63][0x65] ("Alice" in UTF-8)
```

## 10. Syntax

### 10.1 Basic Serialization

```java
// Writing object
try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("user.ser"))) {
    User user = new User("Alice", 25);
    oos.writeObject(user);
}

// Reading object
try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream("user.ser"))) {
    User user = (User) ois.readObject();
    System.out.println(user.getName());
}
```

### 10.2 Implementing Serializable

```java
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private transient String password; // Not serialized

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and setters
}
```

### 10.3 Custom Serialization

```java
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String encryptedPassword;

    private void writeObject(ObjectOutputStream oos)
            throws IOException {
        oos.defaultWriteObject();
        // Custom serialization
        oos.writeUTF(encrypt(encryptedPassword));
    }

    private void readObject(ObjectInputStream ois)
            throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        // Custom deserialization
        encryptedPassword = decrypt(ois.readUTF());
    }
}
```

### 10.4 Externalizable

```java
public class User implements Externalizable {
    private String name;
    private int age;

    public User() {} // Required no-arg constructor

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
        out.writeInt(age);
    }

    @Override
    public void readExternal(ObjectInput in)
            throws IOException, ClassNotFoundException {
        name = in.readUTF();
        age = in.readInt();
    }
}
```

## 11. Easy Example

```java
import java.io.*;

public class SerializationBasic {

    public static void main(String[] args) {
        String filename = "user.ser";

        try {
            // Create and serialize object
            User user = new User("Alice", 25, "secret123");
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(filename))) {
                oos.writeObject(user);
            }
            System.out.println("Serialized: " + user);

            // Deserialize object
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(filename))) {
                User deserialized = (User) ois.readObject();
                System.out.println("Deserialized: " + deserialized);
                System.out.println("Password (transient): " +
                    deserialized.getPassword());
            }

            // Cleanup
            new File(filename).delete();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    static class User implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int age;
        private transient String password;

        User(String name, int age, String password) {
            this.name = name;
            this.age = age;
            this.password = password;
        }

        String getName() { return name; }
        String getPassword() { return password; }

        @Override
        public String toString() {
            return String.format("User{name='%s', age=%d}", name, age);
        }
    }
}
```

## 12. Medium Example

```java
import java.io.*;
import java.util.*;

public class ObjectCollectionDemo {

    /**
     * Writes a list of objects to file.
     */
    public static <T extends Serializable> void writeList(
            String path, List<T> list) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(path))) {
            oos.writeObject(list);
        }
    }

    /**
     * Reads a list of objects from file.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readList(String path)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(path))) {
            return (List<T>) ois.readObject();
        }
    }

    /**
     * Creates a deep copy of a serializable object.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deepCopy(T object)
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(baos.toByteArray()))) {
            return (T) ois.readObject();
        }
    }

    public static void main(String[] args) {
        String filename = "users.ser";

        try {
            // Create list of users
            List<User> users = new ArrayList<>();
            users.add(new User("Alice", 25));
            users.add(new User("Bob", 30));
            users.add(new User("Charlie", 35));

            // Write list
            writeList(filename, users);
            System.out.println("Written " + users.size() + " users");

            // Read list
            List<User> readUsers = readList(filename);
            System.out.println("\nRead users:");
            readUsers.forEach(u ->
                System.out.println("  " + u));

            // Deep copy
            User original = users.get(0);
            User copy = deepCopy(original);
            System.out.println("\nDeep copy:");
            System.out.println("  Original: " + original);
            System.out.println("  Copy: " + copy);
            System.out.println("  Same object? " + (original == copy));

            // Cleanup
            new File(filename).delete();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String name;
        private final int age;

        User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("User{name='%s', age=%d}", name, age);
        }
    }
}
```

## 13. Hard Example

```java
import java.io.*;
import java.util.concurrent.atomic.*;

public class SecureSerializationDemo {

    /**
     * Whitelist for allowed classes during deserialization.
     */
    private static final Set<String> ALLOWED_CLASSES = Set.of(
        "java.lang.String",
        "java.lang.Integer",
        "java.util.ArrayList",
        "java.util.HashMap"
    );

    /**
     * Safe deserialization with class filtering.
     */
    public static Object safeDeserialize(byte[] data)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data))) {
            // Override resolveClass to filter classes
            return ois.readObject();
        }
    }

    /**
     * Serializes object with encryption.
     */
    public static byte[] encryptSerialize(Serializable object,
            byte[] key) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
        }

        byte[] data = baos.toByteArray();
        // Simple XOR encryption (for demo only)
        for (int i = 0; i < data.length; i++) {
            data[i] ^= key[i % key.length];
        }
        return data;
    }

    /**
     * Deserializes with decryption.
     */
    @SuppressWarnings("unchecked")
    public static <T> T decryptDeserialize(byte[] data, byte[] key)
            throws IOException, ClassNotFoundException {

        // Decrypt
        for (int i = 0; i < data.length; i++) {
            data[i] ^= key[i % key.length];
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data))) {
            return (T) ois.readObject();
        }
    }

    /**
     * Versioned serialization for compatibility.
     */
    public static class VersionedUser implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private int age;
        private String email; // Added in version 2

        public VersionedUser(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public VersionedUser(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }

        private void readObject(ObjectInputStream ois)
                throws IOException, ClassNotFoundException {
            ois.defaultReadObject();
            // Handle older versions
            if (email == null) {
                email = "unknown@example.com";
            }
        }

        @Override
        public String toString() {
            return String.format(
                "User{name='%s', age=%d, email='%s'}",
                name, age, email);
        }
    }

    public static void main(String[] args) {
        try {
            // Versioned serialization
            System.out.println("Versioned serialization:");
            VersionedUser user1 = new VersionedUser("Alice", 25);
            System.out.println("  Created: " + user1);

            // Serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos =
                    new ObjectOutputStream(baos)) {
                oos.writeObject(user1);
            }

            // Deserialize with newer class (has email field)
            try (ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(baos.toByteArray()))) {
                VersionedUser user2 =
                    (VersionedUser) ois.readObject();
                System.out.println("  Deserialized: " + user2);
            }

            // Encrypted serialization
            System.out.println("\nEncrypted serialization:");
            VersionedUser user3 =
                new VersionedUser("Bob", 30, "bob@example.com");
            byte[] key = "secret".getBytes();
            byte[] encrypted = encryptSerialize(user3, key);
            System.out.println("  Encrypted bytes: " +
                encrypted.length);

            VersionedUser user4 =
                decryptDeserialize(encrypted, key);
            System.out.println("  Decrypted: " + user4);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## 14. Performance

### Serialization Performance

| Method | Time (1000 objects) | Size | Use Case |
|--------|---------------------|------|----------|
| Default | 50ms | Large | Simple objects |
| Externalizable | 30ms | Small | Performance critical |
| Custom | 40ms | Medium | Complex objects |

### Performance Tips

1. **Use Externalizable** for better performance
2. **Mark unnecessary fields transient**
3. **Avoid deep object graphs**
4. **Consider alternative formats** (JSON, Protocol Buffers)
5. **Cache serialized data** when possible

## 15. Best Practices

1. **Always define serialVersionUID** for version compatibility
2. **Mark sensitive fields transient**
3. **Use try-with-resources** for streams
4. **Validate deserialized objects**
5. **Use whitelist filtering** for untrusted deserialization
6. **Document serialization format**
7. **Test version compatibility**

## 16. Common Mistakes

1. **Not defining serialVersionUID** → Version incompatibility
2. **Not marking transient fields** → Security leaks
3. **Circular references** → StackOverflowError (handled by ObjectOutputStream)
4. **Non-serializable fields** → NotSerializableException
5. **Not handling inheritance** → Partial serialization
6. **Using default serialization** → Poor performance
7. **Ignoring deserialization security** → Vulnerabilities

## 17. Pitfalls

1. **Deserialization attacks** → Execute arbitrary code
2. **Version compatibility** → Hard to maintain
3. **Performance overhead** → Slower than alternatives
4. **Platform dependency** → JVM-specific format
5. **Not thread-safe** → Concurrent access issues
6. **Memory leaks** → Object graph retention

## 18. Debugging Tips

1. **Use readResolve/writeReplace** for singleton pattern
2. **Log serialization/deserialization**
3. **Test with different versions**
4. **Validate object state after deserialization**
5. **Use tools like Java Serializability Inspector**

## 19. Comparison Table

| Feature | Serializable | Externalizable | Custom |
|---------|--------------|----------------|--------|
| Implementation | Marker interface | Full implementation | Partial |
| Control | None | Full | Partial |
| Performance | Medium | High | Medium |
| Constructor | Not called | No-arg called | Not called |
| Use case | Simple objects | Complex objects | Selective |

## 20. Decision Tree

```
Need to serialize objects?
├── Simple object? → Use Serializable
├── Need performance? → Use Externalizable
├── Need control? → Use custom writeObject/readObject
├── Sensitive data? → Mark fields transient
├── Network transmission? → Consider JSON/Protocol Buffers
└── Version compatibility? → Define serialVersionUID
```

## 21. Interview Questions

### Q1: What is the difference between Serializable and Externalizable?
**Answer:** `Serializable` is a marker interface with no methods—Java handles serialization automatically. `Externalizable` requires implementing `writeExternal` and `readExternal` methods, giving full control over serialization and better performance.

### Q2: What is serialVersionUID and why is it important?
**Answer:** `serialVersionUID` is a version identifier for serializable classes. It ensures that serialized objects can be deserialized with compatible class versions. Without it, Java generates one automatically, which may break compatibility when the class changes.

### Q3: What is the transient keyword?
**Answer:** `transient` marks fields that should not be serialized. Transient fields are not included in the serialized form and are initialized to default values (null for objects, 0 for primitives) during deserialization.

### Q4: Can you serialize static fields?
**Answer:** No, serialization only works with instance fields. Static fields belong to the class, not instances, and are not included in the serialized form.

### Q5: What are deserialization security risks?
**Answer:** Deserialization can execute arbitrary code if the input is untrusted. Attackers can craft malicious serialized objects that, when deserialized, execute harmful code. Use whitelist filtering and avoid deserializing untrusted data.

## 22. Exercises

### Exercise 1: Custom Serialization
Implement a class with custom serialization that encrypts sensitive fields during serialization and decrypts them during deserialization.

### Exercise 2: Version Compatibility
Create two versions of a class and demonstrate forward and backward compatibility using serialVersionUID.

### Exercise 3: Deep Copy Utility
Implement a generic deep copy utility using object streams.

### Exercise 4: Serialization Benchmark
Compare performance of Serializable, Externalizable, and JSON serialization.

## 23. Assignments

### Assignment 1: Persistent Game State
Create a game state persistence system that saves and loads game progress using object serialization.

### Assignment 2: Secure Message Passing
Implement a secure message passing system using encrypted serialization.

## 24. Mini Project

**Object Persistence Framework**

Create an object persistence framework that:
1. Saves/loads objects to/from files
2. Supports versioning
3. Handles sensitive data
4. Provides compression
5. Supports batch operations

Requirements:
- Use object streams
- Implement proper error handling
- Add logging
- Support concurrent access

## 25. Summary

| Concept | Key Point |
|---------|-----------|
| Serializable | Marker interface for serialization |
| Externalizable | Full control over serialization |
| serialVersionUID | Version compatibility |
| transient | Skip field during serialization |
| writeObject/readObject | Custom serialization |
| Security | Use whitelist filtering |

## 26. References

1. **Official Documentation**: [Object Serialization](https://docs.oracle.com/javase/tutorial/essential/io/objectstreams.html)
2. **OWASP**: [Deserialization Security](https://cheatsheetseries.owasp.org/cheatsheets/Deserialization_Cheat_Sheet.html)
3. **Books**:
   - "Effective Java" by Joshua Bloch
   - "Java Security" by Scott Oaks
4. **Related Topics**:
   - [06 - Data Streams](../06-data-streams/README.md)
   - [12 - Serialization](../12-serialization/README.md)
   - [09 - NIO Channels](../09-nio-channels/README.md)

---

**Next Topic**: [08 - NIO Basics](../08-nio-basics/README.md)
