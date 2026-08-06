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

---

[📖 Continue to Part 2](README-part2.md)
```
