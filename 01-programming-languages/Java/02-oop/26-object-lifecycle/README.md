# Object Lifecycle

## Introduction

The Object Lifecycle in Java encompasses the complete journey of an object from its creation to its destruction. Understanding this lifecycle is crucial for writing efficient, memory-safe, and well-optimized Java applications. It covers object creation, initialization, usage, garbage collection, and finalization. Mastering the object lifecycle helps developers avoid memory leaks, understand performance implications, and write reliable code.

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

---

## Continue Reading

- [Part 2](README-part2.md)
- [Part 3](README-part3.md)
- [Part 4](README-part4.md)
```
