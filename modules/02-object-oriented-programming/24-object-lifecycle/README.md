# Object Lifecycle

## Introduction

The Object Lifecycle in Java encompasses the complete journey of an object from its creation to its destruction. Understanding this lifecycle is crucial for writing efficient, memory-safe, and well-optimized Java applications. It covers object creation, initialization, usage, garbage collection, and finalization. Mastering the object lifecycle helps developers avoid memory leaks, understand performance implications, and write robust code.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the complete lifecycle of a Java object
- [ ] Explain object creation and initialization processes
- [ ] Differentiate between constructors, instance initializers, and static initializers
- [ ] Understand garbage collection mechanisms and memory management
- [ ] Implement the `finalize()` method and understand its deprecation
- [ ] Use `WeakReference`, `SoftReference`, and `PhantomReference` appropriately
- [ ] Apply best practices for efficient memory usage

## Prerequisites

- [04-constructors](../04-constructors/README.md) - Understanding of constructors
- [02-classes](../02-classes/README.md) - Basic class concepts
- [03-objects](../03-objects/README.md) - Object creation basics
- [14-object-class](../14-object-class/README.md) - Understanding of Object class

## Why This Concept Exists

### The Problem

Java developers need to understand:
1. When and how objects are created in memory
2. The order of initialization in complex hierarchies
3. How memory is managed and reclaimed
4. Why memory leaks occur even in garbage-collected languages
5. How to properly clean up resources

### The Solution

The object lifecycle provides a systematic understanding of:
1. Object allocation and construction phases
2. Initialization ordering guarantees
3. Reachability analysis and garbage collection
4. Reference types and their memory implications
5. Cleanup strategies and best practices

### Real-World Analogy

Think of an object like a human life cycle:
- **Birth (Creation)**: Object is allocated memory and constructor runs
- **Growth (Initialization)**: Initializers set up the object's state
- **Life (Usage)**: Object performs its intended functions
- **Retirement (Eligible for GC)**: Object becomes unreachable
- **Death (GC)**: Memory is reclaimed by the garbage collector

## Internal Working

### Object Creation Process

#### Step 1: Memory Allocation
```
┌─────────────────────────────────────┐
│         Class Loading               │
│  - Load .class file                 │
│  - Verify bytecode                  │
│  - Prepare static fields            │
│  - Execute static initializers      │
└─────────────────────────────────────┘
                ↓
┌─────────────────────────────────────┐
│         Memory Allocation           │
│  - Allocate heap memory             │
│  - Zero out all fields              │
│  - Set object header (mark word)    │
└─────────────────────────────────────┘
```

#### Step 2: Constructor Execution
```
Object() → Parent() → This Class()
         ↑            ↑
       implicit     super()
```

#### Step 3: Initialization Order
```
1. Static variables (in declaration order)
2. Static initializer blocks
3. Instance variables (in declaration order)
4. Instance initializer blocks
5. Constructor body
```

### JVM Memory Areas

```
┌─────────────────────────────────────┐
│           Method Area               │
│  - Class metadata                   │
│  - Static variables                 │
│  - Constant pool                    │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│              Heap                   │
│  - Object instances                 │
│  - Instance variables               │
│  - Arrays                           │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│           Stack                     │
│  - Local variables                  │
│  - Method parameters                │
│  - Return addresses                 │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│        Program Counter              │
│  - Current bytecode instruction     │
└─────────────────────────────────────┘
```

### Garbage Collection Process

```
┌─────────────────────────────────────┐
│      Object Creation                │
│  - Created on heap                  │
│  - Reference count = 1              │
└─────────────────────────────────────┘
                ↓
┌─────────────────────────────────────┐
│      Object in Use                  │
│  - Referenced by variables          │
│  - Referenced by other objects      │
└─────────────────────────────────────┘
                ↓
┌─────────────────────────────────────┐
│    Object Becomes Unreachable       │
│  - No live references               │
│  - Eligible for GC                  │
└─────────────────────────────────────┘
                ↓
┌─────────────────────────────────────┐
│      Garbage Collection             │
│  - Mark reachable objects           │
│  - Sweep unreachable objects        │
│  - Compact memory (optional)        │
└─────────────────────────────────────┘
```

## Syntax

### Basic Object Creation
```java
ClassName obj = new ClassName();
```

### Constructor with super
```java
class Child extends Parent {
    Child() {
        super();  // Parent initialization
        // Child initialization
    }
}
```

### Static Initializer
```java
class MyClass {
    static {
        // Static initialization code
    }
}
```

### Instance Initializer
```java
class MyClass {
    {
        // Instance initialization code
    }
}
```

### Finalize Method (Deprecated)
```java
@Override
protected void finalize() throws Throwable {
    try {
        // Cleanup code
    } finally {
        super.finalize();
    }
}
```

## Easy Examples

### Example 1: Basic Object Creation and Initialization Order

**Problem Statement**: How does Java initialize objects in an inheritance hierarchy?

**Implementation**:

```java
package academy.javaengineering.oop.objectlifecycle;

class Animal {
    String type;
    
    static {
        System.out.println("1. Animal static initializer");
    }
    
    {
        System.out.println("2. Animal instance initializer");
    }
    
    Animal() {
        System.out.println("3. Animal constructor");
        type = "Animal";
    }
}

class Dog extends Animal {
    String breed;
    
    static {
        System.out.println("4. Dog static initializer");
    }
    
    {
        System.out.println("5. Dog instance initializer");
    }
    
    Dog() {
        System.out.println("6. Dog constructor");
        breed = "Unknown";
    }
    
    void display() {
        System.out.println("Type: " + type + ", Breed: " + breed);
    }
}

public class InitializationOrderDemo {
    public static void main(String[] args) {
        System.out.println("--- Creating first Dog ---");
        Dog dog1 = new Dog();
        dog1.display();
        
        System.out.println("\n--- Creating second Dog ---");
        Dog dog2 = new Dog();
        dog2.display();
    }
}
```

**Output**:
```
--- Creating first Dog ---
1. Animal static initializer
4. Dog static initializer
2. Animal instance initializer
3. Animal constructor
5. Dog instance initializer
6. Dog constructor
Type: Animal, Breed: Unknown

--- Creating second Dog ---
2. Animal instance initializer
3. Animal constructor
5. Dog instance initializer
6. Dog constructor
Type: Animal, Breed: Unknown
```

**Best Practices**:
- Static initializers run only once when the class is first loaded
- Instance initializers run every time an object is created
- Use static initializers for one-time setup like configuration loading

### Example 2: Object Creation with Different Reference Types

**Problem Statement**: How do different reference types affect object lifecycle and garbage collection?

**Implementation**:

```java
package academy.javaengineering.oop.objectlifecycle;

import java.lang.ref.*;

class Resource {
    String name;
    
    Resource(String name) {
        this.name = name;
        System.out.println("Resource created: " + name);
    }
    
    @Override
    protected void finalize() throws Throwable {
        System.out.println("Resource finalized: " + name);
        super.finalize();
    }
    
    @Override
    public String toString() {
        return "Resource[" + name + "]";
    }
}

public class ReferenceTypesDemo {
    public static void main(String[] args) throws InterruptedException {
        // Strong Reference
        Resource strong = new Resource("Strong");
        System.out.println("Strong ref: " + strong);
        
        // Weak Reference
        Resource temp = new Resource("Weak-temp");
        WeakReference<Resource> weak = new WeakReference<>(temp);
        temp = null;  // Remove strong reference
        System.out.println("Weak ref before GC: " + weak.get());
        
        // Soft Reference
        Resource softTemp = new Resource("Soft-temp");
        SoftReference<Resource> soft = new SoftReference<>(softTemp);
        softTemp = null;
        System.out.println("Soft ref before GC: " + soft.get());
        
        // Force GC
        System.out.println("\n--- Forcing Garbage Collection ---");
        System.gc();
        Thread.sleep(1000);
        
        System.out.println("Weak ref after GC: " + weak.get());
        System.out.println("Soft ref after GC: " + soft.get());
        
        // Strong reference keeps object alive
        System.out.println("Strong ref after GC: " + strong);
        
        // Allow finalization
        Thread.sleep(1000);
    }
}
```

**Output** (may vary):
```
Resource created: Strong
Strong ref: Resource[Strong]
Resource created: Weak-temp
Weak ref before GC: Resource[Weak-temp]
Resource created: Soft-temp
Soft ref before GC: Resource[Soft-temp]

--- Forcing Garbage Collection ---
Weak ref after GC: null
Soft ref after GC: Resource[Soft-temp]
Strong ref after GC: Resource[Strong]
Resource finalized: Weak-temp
```

**Best Practices**:
- Use `WeakReference` for caches where entries should be GC'd when memory is low
- Use `SoftReference` for memory-sensitive caches
- Avoid `finalize()` for resource cleanup; use `try-with-resources` instead

### Example 3: Object Reuse with Clone

**Problem Statement**: How does object creation differ from object cloning?

**Implementation**:

```java
package academy.javaengineering.oop.objectlifecycle;

class Config implements Cloneable {
    String setting;
    int value;
    
    Config(String setting, int value) {
        this.setting = setting;
        this.value = value;
        System.out.println("Config created: " + setting);
    }
    
    @Override
    protected Config clone() throws CloneNotSupportedException {
        System.out.println("Cloning config: " + setting);
        return (Config) super.clone();
    }
    
    @Override
    public String toString() {
        return "Config[" + setting + "=" + value + "]";
    }
}

public class CloneDemo {
    public static void main(String[] args) throws CloneNotSupportedException {
        Config original = new Config("timeout", 30000);
        System.out.println("Original: " + original);
        
        Config cloned = original.clone();
        System.out.println("Cloned: " + cloned);
        
        // Modify clone to show independence
        cloned.value = 60000;
        System.out.println("\nAfter modifying clone:");
        System.out.println("Original: " + original);
        System.out.println("Cloned: " + cloned);
        
        // Different hash codes confirm different objects
        System.out.println("\nSame object? " + (original == cloned));
    }
}
```

**Output**:
```
Config created: timeout
Original: Config[timeout=30000]
Cloning config: timeout
Cloned: Config[timeout=30000]

After modifying clone:
Original: Config[timeout=30000]
Cloned: Config[timeout=60000]

Same object? false
```

**Best Practices**:
- Clone creates a shallow copy; implement deep copy if needed
- Override `clone()` carefully to maintain object invariants
- Consider using copy constructors as an alternative to `clone()`

## Medium Examples

### Example 4: Complete Object Lifecycle with Resource Management

**Problem Statement**: How to properly manage objects with external resources throughout their lifecycle?

**Implementation**:

```java
package academy.javaengineering.oop.objectlifecycle;

import java.io.Closeable;
import java.io.IOException;

class DatabaseConnection implements Closeable {
    private String url;
    private boolean connected;
    private static int connectionCount = 0;
    
    static {
        System.out.println("DatabaseConnection class loaded");
    }
    
    {
        connectionCount++;
        System.out.println("Instance initializer - Total connections: " + connectionCount);
    }
    
    public DatabaseConnection(String url) {
        this.url = url;
        this.connected = true;
        System.out.println("Connected to: " + url);
    }
    
    public void executeQuery(String query) {
        if (!connected) {
            throw new IllegalStateException("Not connected!");
        }
        System.out.println("Executing: " + query);
    }
    
    @Override
    public void close() {
        if (connected) {
            connected = false;
            connectionCount--;
            System.out.println("Connection closed: " + url);
            System.out.println("Remaining connections: " + connectionCount);
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        if (connected) {
            System.err.println("WARNING: Connection not properly closed: " + url);
        }
        super.finalize();
    }
}

public class ObjectLifecycleDemo {
    public static void main(String[] args) {
        System.out.println("=== Object Lifecycle Demo ===\n");
        
        // Using try-with-resources for proper cleanup
        try (DatabaseConnection conn1 = new DatabaseConnection("jdbc:mysql://localhost/db1")) {
            conn1.executeQuery("SELECT * FROM users");
            
            // Nested resource
            try (DatabaseConnection conn2 = new DatabaseConnection("jdbc:mysql://localhost/db2")) {
                conn2.executeQuery("SELECT * FROM orders");
            }
        }
        
        System.out.println("\n=== Objects now eligible for GC ===");
        System.gc();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n=== Program Complete ===");
    }
}
```

**Output**:
```
=== Object Lifecycle Demo ===

DatabaseConnection class loaded
Instance initializer - Total connections: 1
Connected to: jdbc:mysql://localhost/db1
Executing: SELECT * FROM users
Instance initializer - Total connections: 2
Connected to: jdbc:mysql://localhost/db2
Executing: SELECT * FROM orders
Connection closed: jdbc:mysql://localhost/db2
Remaining connections: 1
Connection closed: jdbc:mysql://localhost/db1
Remaining connections: 0

=== Objects now eligible for GC ===

=== Program Complete ===
```

### Example 5: Object Pool Pattern Implementation

**Problem Statement**: How to efficiently manage object lifecycle using the object pool pattern?

**Implementation**:

```java
package academy.javaengineering.oop.objectlifecycle;

import java.util.ArrayList;
import java.util.List;

class PoolObject {
    private boolean inUse;
    private String data;
    
    PoolObject() {
        this.inUse = false;
        this.data = "initialized";
        System.out.println("PoolObject created");
    }
    
    boolean isInUse() {
        return inUse;
    }
    
    void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
    
    String getData() {
        return data;
    }
    
    void setData(String data) {
        this.data = data;
    }
    
    void reset() {
        this.data = "initialized";
        System.out.println("PoolObject reset");
    }
}

class ObjectPool<T> {
    private List<T> pool;
    private int maxSize;
    private int createdCount;
    
    ObjectPool(int maxSize) {
        this.pool = new ArrayList<>();
        this.maxSize = maxSize;
        this.createdCount = 0;
        
        // Pre-create objects
        for (int i = 0; i < Math.min(3, maxSize); i++) {
            pool.add((T) new PoolObject());
            createdCount++;
        }
        System.out.println("Pool initialized with " + pool.size() + " objects");
    }
    
    @SuppressWarnings("unchecked")
    synchronized T acquire() {
        // Find available object
        for (T obj : pool) {
            if (obj instanceof PoolObject && !((PoolObject) obj).isInUse()) {
                ((PoolObject) obj).setInUse(true);
                System.out.println("Object acquired from pool");
                return obj;
            }
        }
        
        // Create new if pool not full
        if (createdCount < maxSize) {
            T newObj = (T) new PoolObject();
            ((PoolObject) newObj).setInUse(true);
            pool.add(newObj);
            createdCount++;
            System.out.println("New object created and acquired");
            return newObj;
        }
        
        System.out.println("Pool exhausted!");
        return null;
    }
    
    synchronized void release(T obj) {
        if (obj instanceof PoolObject) {
            ((PoolObject) obj).setInUse(false);
            ((PoolObject) obj).reset();
            System.out.println("Object released back to pool");
        }
    }
    
    int getPoolSize() {
        return pool.size();
    }
}

public class ObjectPoolDemo {
    public static void main(String[] args) {
        ObjectPool<PoolObject> pool = new ObjectPool<>(5);
        
        // Acquire objects
        PoolObject obj1 = pool.acquire();
        PoolObject obj2 = pool.acquire();
        PoolObject obj3 = pool.acquire();
        
        System.out.println("\nPool size: " + pool.getPoolSize());
        
        // Release one object
        pool.release(obj2);
        
        // Acquire again (reuses released object)
        PoolObject obj4 = pool.acquire();
        
        System.out.println("\nFinal pool size: " + pool.getPoolSize());
    }
}
```

**Output**:
```
PoolObject created
PoolObject created
PoolObject created
Pool initialized with 3 objects
Object acquired from pool
Object acquired from pool
Object acquired from pool

Pool size: 3
Object released back to pool
PoolObject reset
Object acquired from pool

Final pool size: 3
```

### Example 6: Builder Pattern for Complex Object Creation

**Problem Statement**: How to manage complex object creation with the Builder pattern?

**Implementation**:

```java
package academy.javaengineering.oop.objectlifecycle;

class HttpRequest {
    private final String method;
    private final String url;
    private final String contentType;
    private final String body;
    private final int timeout;
    
    private HttpRequest(Builder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.contentType = builder.contentType;
        this.body = builder.body;
        this.timeout = builder.timeout;
        System.out.println("HttpRequest created");
    }
    
    static class Builder {
        private String method = "GET";
        private String url;
        private String contentType = "application/json";
        private String body = "";
        private int timeout = 30000;
        
        Builder(String url) {
            this.url = url;
        }
        
        Builder method(String method) {
            this.method = method;
            return this;
        }
        
        Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }
        
        Builder body(String body) {
            this.body = body;
            return this;
        }
        
        Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }
        
        HttpRequest build() {
            validate();
            return new HttpRequest(this);
        }
        
        private void validate() {
            if (url == null || url.isEmpty()) {
                throw new IllegalStateException("URL is required");
            }
        }
    }
    
    @Override
    public String toString() {
        return String.format("HttpRequest{method='%s', url='%s', contentType='%s', timeout=%d}",
                method, url, contentType, timeout);
    }
}

public class BuilderPatternDemo {
    public static void main(String[] args) {
        // Simple GET request
        HttpRequest getRequest = new HttpRequest.Builder("https://api.example.com/users")
                .build();
        System.out.println("GET: " + getRequest);
        
        // Complex POST request
        HttpRequest postRequest = new HttpRequest.Builder("https://api.example.com/users")
                .method("POST")
                .contentType("application/json")
                .body("{\"name\": \"John\"}")
                .timeout(5000)
                .build();
        System.out.println("POST: " + postRequest);
    }
}
```

**Output**:
```
HttpRequest created
GET: HttpRequest{method='GET', url='https://api.example.com/users', contentType='application/json', timeout=30000}
HttpRequest created
POST: HttpRequest{method='POST', url='https://api.example.com/users', contentType='application/json', timeout=5000}
```

## Hard Examples

### Example 7: WeakHashMap for Cache Implementation

**Problem Statement**: How to implement a memory-efficient cache using WeakHashMap?

**Implementation**:

```java
package academy.javaengineering.oop.objectlifecycle;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import java.util.Map;

class DataProcessor {
    private String id;
    private byte[] largeData;
    
    DataProcessor(String id, int dataSize) {
        this.id = id;
        this.largeData = new byte[dataSize];
        System.out.println("DataProcessor created: " + id + " (" + dataSize + " bytes)");
    }
    
    String getId() {
        return id;
    }
    
    @Override
    protected void finalize() throws Throwable {
        System.out.println("DataProcessor finalized: " + id);
        super.finalize();
    }
    
    @Override
    public String toString() {
        return "DataProcessor[" + id + "]";
    }
}

class WeakCache<K, V> {
    private WeakHashMap<K, WeakReference<V>> cache;
    private int hitCount;
    private int missCount;
    
    WeakCache() {
        this.cache = new WeakHashMap<>();
        this.hitCount = 0;
        this.missCount = 0;
    }
    
    void put(K key, V value) {
        cache.put(key, new WeakReference<>(value));
        System.out.println("Cached: " + key);
    }
    
    V get(K key) {
        WeakReference<V> ref = cache.get(key);
        if (ref != null) {
            V value = ref.get();
            if (value != null) {
                hitCount++;
                System.out.println("Cache hit: " + key);
                return value;
            } else {
                cache.remove(key);
            }
        }
        missCount++;
        System.out.println("Cache miss: " + key);
        return null;
    }
    
    void printStats() {
        System.out.println("Cache stats - Hits: " + hitCount + ", Misses: " + missCount);
        System.out.println("Cache size: " + cache.size());
    }
}

public class WeakHashMapDemo {
    public static void main(String[] args) throws InterruptedException {
        WeakCache<String, DataProcessor> cache = new WeakCache<>();
        
        // Create and cache objects
        DataProcessor p1 = new DataProcessor("P1", 1024);
        DataProcessor p2 = new DataProcessor("P2", 2048);
        DataProcessor p3 = new DataProcessor("P3", 4096);
        
        cache.put("p1", p1);
        cache.put("p2", p2);
        cache.put("p3", p3);
        
        // Access cached objects
        cache.get("p1");
        cache.get("p2");
        
        // Remove strong references
        System.out.println("\n--- Removing strong references ---");
        p1 = null;
        p2 = null;
        
        // Force GC
        System.out.println("\n--- Forcing GC ---");
        System.gc();
        Thread.sleep(1000);
        
        // Try to access - may be null
        System.out.println("\n--- Accessing after GC ---");
        cache.get("p1");
        cache.get("p2");
        cache.get("p3");
        
        cache.printStats();
        
        // Allow finalization
        Thread.sleep(1000);
    }
}
```

**Output** (may vary):
```
DataProcessor created: P1 (1024 bytes)
DataProcessor created: P2 (2048 bytes)
DataProcessor created: P3 (4096 bytes)
Cached: p1
Cached: p2
Cached: p3
Cache hit: p1
Cache hit: p2

--- Removing strong references ---

--- Forcing GC ---
DataProcessor finalized: P1
DataProcessor finalized: P2

--- Accessing after GC ---
Cache miss: p1
Cache miss: p2
Cache hit: p3
Cache stats - Hits: 3, Misses: 2
Cache size: 1
```

### Example 8: PhantomReference for Cleanup Tasks

**Problem Statement**: How to use PhantomReference for post-mortem cleanup operations?

**Implementation**:

```java
package academy.javaengineering.oop.objectlifecycle;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

class ResourceHolder {
    private String resourceId;
    private boolean cleaned;
    
    ResourceHolder(String resourceId) {
        this.resourceId = resourceId;
        this.cleaned = false;
        System.out.println("ResourceHolder created: " + resourceId);
    }
    
    String getResourceId() {
        return resourceId;
    }
    
    boolean isCleaned() {
        return cleaned;
    }
    
    void cleanup() {
        if (!cleaned) {
            cleaned = true;
            System.out.println("ResourceHolder cleaned: " + resourceId);
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        System.out.println("ResourceHolder finalized: " + resourceId);
        super.finalize();
    }
}

class ResourceCleanupHandler extends Thread {
    private ReferenceQueue<ResourceHolder> refQueue;
    private List<PhantomReference<ResourceHolder>> phantomRefs;
    private volatile boolean running;
    
    ResourceCleanupHandler() {
        this.refQueue = new ReferenceQueue<>();
        this.phantomRefs = new ArrayList<>();
        this.running = true;
        this.setDaemon(true);
        this.start();
    }
    
    void track(ResourceHolder resource) {
        PhantomReference<ResourceHolder> ref = new PhantomReference<>(resource, refQueue);
        phantomRefs.add(ref);
        System.out.println("Tracking resource: " + resource.getResourceId());
    }
    
    @Override
    public void run() {
        System.out.println("Cleanup handler started");
        
        while (running) {
            try {
                PhantomReference<ResourceHolder> ref = 
                    (PhantomReference<ResourceHolder>) refQueue.remove();
                
                ResourceHolder resource = ref.get();
                if (resource != null) {
                    resource.cleanup();
                }
                
                phantomRefs.remove(ref);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("Cleanup handler stopped");
    }
    
    void shutdown() {
        running = false;
        this.interrupt();
    }
}

public class PhantomReferenceDemo {
    public static void main(String[] args) throws InterruptedException {
        ResourceCleanupHandler handler = new ResourceCleanupHandler();
        
        // Create and track resources
        ResourceHolder r1 = new ResourceHolder("R1");
        ResourceHolder r2 = new ResourceHolder("R2");
        
        handler.track(r1);
        handler.track(r2);
        
        System.out.println("\n--- Releasing resources ---");
        r1 = null;
        r2 = null;
        
        System.out.println("\n--- Forcing GC ---");
        System.gc();
        Thread.sleep(2000);
        
        handler.shutdown();
        System.out.println("\n=== Program Complete ===");
    }
}
```

**Output** (may vary):
```
ResourceHolder created: R1
ResourceHolder created: R2
Tracking resource: R1
Tracking resource: R2

--- Releasing resources ---

--- Forcing GC ---
Cleanup handler started
ResourceHolder cleaned: R1
ResourceHolder finalized: R1
ResourceHolder cleaned: R2
ResourceHolder finalized: R2

=== Program Complete ===
Cleanup handler stopped
```

## Exercises

### Easy

1. **Initialization Order**: Create a three-level class hierarchy (A → B → C) with static blocks, instance blocks, and constructors at each level. Write a program to demonstrate the exact initialization order.

2. **Reference Types Demo**: Create a program that demonstrates Strong, Weak, and Soft references. Create objects, assign them to different reference types, and show GC behavior.

3. **Clone vs New**: Create a class with a `clone()` method and demonstrate the difference between creating a new object vs cloning an existing one.

### Medium

4. **Resource Cleanup**: Implement a class that holds a file handle or database connection. Use `try-with-resources` pattern and demonstrate proper cleanup throughout the object lifecycle.

5. **Object Pool**: Implement a simple object pool that creates objects on demand, reuses them, and handles cleanup when objects are returned.

6. **Builder Pattern**: Create a complex object (e.g., `HttpResponse`) using the Builder pattern. Demonstrate how the builder manages the creation lifecycle.

### Hard

7. **Custom Cache with WeakHashMap**: Implement a cache that uses `WeakHashMap` to automatically remove entries when memory is low. Demonstrate memory-sensitive behavior.

8. **Finalizer Watcher**: Create a daemon thread that watches for objects being finalized and logs cleanup operations using `PhantomReference`.

9. **Lifecycle Manager**: Implement a comprehensive lifecycle manager that tracks object creation, usage, and destruction across an application.

## Interview Questions

### Beginner

1. **What are the stages of an object's lifecycle in Java?**
   
   Answer: The stages are: (1) Class Loading, (2) Memory Allocation, (3) Constructor Execution, (4) Object Usage, (5) Garbage Collection Eligibility, (6) Garbage Collection, (7) Memory Reclamation.

2. **What is the difference between `finalize()` and `try-with-resources`?**
   
   Answer: `finalize()` is called by GC before reclaiming memory but is unpredictable and deprecated. `try-with-resources` provides deterministic cleanup and is the recommended approach for resource management.

3. **When is an object eligible for garbage collection?**
   
   Answer: An object becomes eligible for GC when it is no longer reachable through any live references in the application. This happens when all references to the object are set to null or go out of scope.

### Intermediate

4. **Explain the difference between Strong, Soft, Weak, and Phantom references.**
   
   Answer: Strong references keep objects alive. Soft references are cleared at GC's discretion for memory optimization. Weak references are cleared when no strong references exist. Phantom references are enqueued after finalization for post-mortem cleanup.

5. **What is the order of initialization in Java?**
   
   Answer: (1) Static variables and static blocks (in declaration order), (2) Instance variables and instance blocks (in declaration order), (3) Constructor. Parent class initializes before child class at each level.

6. **Why is `finalize()` deprecated in Java 9+?**
   
   Answer: `finalize()` is unpredictable (GC timing unknown), can cause resurrection of objects, has performance overhead, and can lead to resource leaks if not implemented correctly. Modern alternatives include `try-with-resources` and `Cleaner`.

### Senior

7. **How does the JVM determine when to run garbage collection?**
   
   Answer: The JVM uses various algorithms (G1, ZGC, Shenandoah) that consider heap usage, allocation rate, pause time goals, and memory pressure. GC is triggered when heap usage exceeds thresholds or when explicit `System.gc()` is called.

8. **What are the memory implications of using `finalize()`?**
   
   Answer: Objects with `finalize()` are placed in a finalization queue, extending their lifetime by at least one GC cycle. This increases memory pressure and can lead to OutOfMemoryError if finalizers are slow or objects are created rapidly.

9. **How does object lifecycle management differ in concurrent applications?**
   
   Answer: Concurrent applications require thread-safe reference handling, proper synchronization for shared objects, understanding of happens-before relationships, and careful management of thread-local resources to avoid memory leaks.

## Common Pitfalls

### 1. Memory Leak from Unclosed Resources

**Wrong**:
```java
void processData() {
    Connection conn = dataSource.getConnection();
    // Use connection...
    // Forgot to close - memory leak!
}
```

**Right**:
```java
void processData() {
    try (Connection conn = dataSource.getConnection()) {
        // Use connection...
    }  // Automatically closed
}
```

### 2. Resurrection in finalize()

**Wrong**:
```java
@Override
protected void finalize() throws Throwable {
    // Resurrects the object!
    singleton = this;
    super.finalize();
}
```

**Right**:
```java
@Override
protected void finalize() throws Throwable {
    try {
        // Cleanup code only
    } finally {
        super.finalize();
    }
}
```

### 3. Relying on GC Timing

**Wrong**:
```java
void createManyObjects() {
    for (int i = 0; i < 1000000; i++) {
        new Object();
    }
    // Don't assume memory is freed immediately
}
```

**Right**:
```java
void createManyObjects() {
    for (int i = 0; i < 1000000; i++) {
        Object obj = new Object();
        // Use or discard explicitly
    }
    // Explicitly request GC if needed (still not guaranteed)
    System.gc();
}
```

## Best Practices

1. **Use try-with-resources for deterministic cleanup**: Always implement `Closeable`/`AutoCloseable` for resources that need explicit cleanup (files, connections, streams).

2. **Prevent memory leaks**: Nullify references when objects are no longer needed, especially for large objects or collections. Use weak references for caches.

3. **Avoid finalize()**: Use `Cleaner` class (Java 9+) or `try-with-resources` instead. If you must use `finalize()`, always call `super.finalize()`.

4. **Understand reference types**: Use strong references for essential objects, soft references for memory-sensitive caches, weak references for canonical mappings, and phantom references for post-mortem cleanup.

5. **Profile before optimizing**: Don't assume memory issues; use profiling tools (VisualVM, JProfiler) to identify actual memory patterns before implementing solutions.

## Real World Usage

### How Spring Uses This

Spring Framework manages object lifecycles through IoC containers:

```java
@Component
public class UserService implements DisposableBean {
    
    @Autowired
    private DataSource dataSource;
    
    @PostConstruct
    public void init() {
        System.out.println("UserService initialized");
    }
    
    @PreDestroy
    @Override
    public void destroy() {
        System.out.println("UserService cleanup");
        // Cleanup resources
    }
}
```

### How Hibernate Uses This

Hibernate manages entity lifecycle states:

```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @PrePersist
    public void prePersist() {
        System.out.println("About to persist User");
    }
    
    @PostPersist
    public void postPersist() {
        System.out.println("User persisted with ID: " + id);
    }
    
    @PreRemove
    public void preRemove() {
        System.out.println("About to remove User");
    }
    
    @PostRemove
    public void postRemove() {
        System.out.println("User removed");
    }
    
    @PreUpdate
    public void preUpdate() {
        System.out.println("About to update User");
    }
    
    @PostUpdate
    public void postUpdate() {
        System.out.println("User updated");
    }
}
```

### How JDK Uses This

The JDK's `Cleaner` class provides modern cleanup:

```java
import java.lang.ref.Cleaner;

public class NativeResource implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();
    private final Cleaner.Cleanable cleanable;
    private long nativePointer;
    
    public NativeResource() {
        this.nativePointer = allocateNative();
        this.cleanable = cleaner.register(this, 
            new CleanupTask(nativePointer));
    }
    
    private static class CleanupTask implements Runnable {
        private long pointer;
        
        CleanupTask(long pointer) {
            this.pointer = pointer;
        }
        
        @Override
        public void run() {
            freeNative(pointer);
        }
    }
    
    @Override
    public void close() {
        cleanable.clean();
    }
    
    private native long allocateNative();
    private static native void freeNative(long pointer);
}
```

## Summary

- Object lifecycle encompasses creation, initialization, usage, GC, and finalization
- Initialization order: static → instance variables/blocks → constructor
- Objects become eligible for GC when no longer reachable
- Reference types (Strong, Soft, Weak, Phantom) control GC behavior
- `finalize()` is deprecated; use `try-with-resources` or `Cleaner` instead
- Object pools and caches optimize lifecycle management
- Proper resource cleanup prevents memory leaks
- JVM uses sophisticated algorithms for automatic memory management

## References

- [Oracle Java Tutorials - Creating Objects](https://docs.oracle.com/en/java/javase/21/java/javaOO/objectcreation.html)
- [Java Language Specification - Class Instance Creation](https://docs.oracle.com/javase/specs/jls/se17/html/jls-15.html#jls-15.9.3)
- [Effective Java by Joshua Bloch - Item 8: Avoid finalizers and cleaners](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Java SE API - Reference Classes](https://docs.oracle.com/en/java/javase/21/docs/api/java/lang/ref/package-summary.html)

**Previous**: [07-super-keyword](../07-super-keyword/README.md)
