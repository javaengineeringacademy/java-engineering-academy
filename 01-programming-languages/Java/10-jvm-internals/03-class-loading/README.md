# 03. Class Loading

## Introduction

Class loading is the process by which the JVM loads Java bytecode into memory and makes it available for execution. It is one of the most fundamental and sophisticated subsystems of the JVM, responsible for locating, loading, linking, and initializing classes. Understanding class loading is essential for developing modular applications, application servers, and debugging complex classpath issues.

The class loading mechanism follows a hierarchical delegation model that ensures proper class isolation and prevents security vulnerabilities. This topic explores the entire class loading process, from locating class files to initializing static fields, and covers practical scenarios like custom class loaders and hot deployment.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Explain the class loading process in detail
- [ ] Differentiate between Bootstrap, Extension, and Application class loaders
- [ ] Implement custom class loaders
- [ ] Understand the parent delegation model
- [ ] Diagnose class loading issues
- [ ] Apply class loading knowledge to modular applications
- [ ] Optimize class loading performance

## Prerequisites

- Completion of Topic 02: JVM Architecture
- Understanding of Java packages and imports
- Basic knowledge of file systems and I/O
- Familiarity with JVM memory areas

## Why This Concept Exists

### The Modularity Challenge

Java applications are often composed of multiple modules, libraries, and components. Class loading solves several challenges:

1. **Dependency Management**: Loading classes from JARs, directories, and network locations
2. **Class Isolation**: Different versions of the same class can coexist
3. **Security**: Verifying bytecode before execution
4. **Lazy Loading**: Loading classes only when needed
5. **Hot Deployment**: Reloading classes without restarting the JVM

### The Delegation Model

The parent delegation model ensures:
- **Consistency**: Core classes are loaded by the bootstrap class loader
- **Security**: Malicious code cannot replace core classes
- **Caching**: Classes are loaded only once per class loader
- **Separation**: Different applications can use different versions of libraries

### Real-World Scenarios

Class loading is critical in:
- **Application Servers**: Multiple web applications sharing a JVM
- **Plugin Systems**: Dynamic loading of extensions
- **OSGi Frameworks**: Modular Java applications
- **Hot Deployment**: Updating code without restart

## Problem Statement

### The Class Loading Challenge

Without understanding class loading, developers face:
- `ClassNotFoundException`: Class not found in classpath
- `NoClassDefFoundError`: Class was found during compilation but not at runtime
- `ClassCastException`: Unexpected type conversions
- `LinkageError`: Duplicate class definitions
- `SecurityException`: Violation of security constraints

### Real-World Example

A major application server experienced:
- Memory leaks due to class loader leaks
- Version conflicts between libraries
- Hot deployment failures
- Security vulnerabilities from untrusted code

The root cause? Improper class loading configuration.

## Theory

### Class Loading Process

The class loading process consists of three phases:

```
```
1. LOADING
   ├── Find the .class file
   ├── Read the bytecode
   └── Create Class object

2. LINKING
   ├── VERIFICATION
   │   ├── Check bytecode format
   │   ├── Verify constant pool
   │   └── Check type safety
   ├── PREPARATION
   │   ├── Allocate memory for static variables
   │   └── Set default values
   └── RESOLUTION
       ├── Replace symbolic references
       └── Convert to direct references

3. INITIALIZATION
   ├── Execute static initializers
   ├── Execute static blocks
   └── Assign static variable values
```

### Class Loader Hierarchy

```
         Bootstrap ClassLoader
         (C++ implementation)
         Loads: java.lang.*, java.util.*, etc.
                ↑
        Extension ClassLoader
        (sun.misc.Launcher$ExtClassLoader)
        Loads: javax.*, sun.misc.*, etc.
                ↑
        Application ClassLoader
        (sun.misc.Launcher$AppClassLoader)
        Loads: Application classes, classpath
                ↑
        Custom ClassLoaders
        (User-defined)
        Loads: Plugin classes, dynamic classes
```

### Parent Delegation Model

When a class loader receives a request to load a class:

1. **Check Cache**: Has this class been loaded before?
2. **Delegate to Parent**: Ask parent class loader to load
3. **Load Self**: If parent cannot load, load the class itself
4. **Cache**: Store the loaded class for future requests

```
Request to load MyClass
        ↓
Application ClassLoader
        ↓ (delegates)
Extension ClassLoader
        ↓ (delegates)
Bootstrap ClassLoader
        ↓ (cannot find)
Extension ClassLoader
        ↓ (cannot find)
Application ClassLoader
        ↓ (loads)
MyClass loaded by Application ClassLoader
```

## Internal Working

### How Class Loading Works

```java
// Example: Understanding class loading
public class ClassLoadingExample {
    public static void main(String[] args) throws Exception {
        // 1. When main() is called, Application ClassLoader loads ClassLoadingExample
        // 2. System.out.println triggers loading of PrintStream class
        // 3. String class is loaded by Bootstrap ClassLoader
        
        // Get the class loader for different classes
        System.out.println("ClassLoadingExample: " + 
            ClassLoadingExample.class.getClassLoader());
        System.out.println("String: " + 
            String.class.getClassLoader());
        System.out.println("CustomClass: " + 
            CustomClass.class.getClassLoader());
    }
}

class CustomClass {
    // This class will be loaded by Application ClassLoader
}
```

### Class Loading Phases in Detail

#### Phase 1: Loading

During loading, the JVM:
1. Locates the `.class` file using the class name
2. Reads the bytecode from the file
3. Creates a `java.lang.Class` instance
4. Links the class (verification, preparation, resolution)

#### Phase 2: Linking

**Verification:**
- Checks that the bytecode is valid
- Ensures the bytecode doesn't violate security constraints
- Verifies type safety

**Preparation:**
- Allocates memory for static variables
- Sets default values (0, null, false, etc.)
- Does not execute any Java code

**Resolution:**
- Converts symbolic references to direct references
- Replaces constant pool entries with actual addresses

#### Phase 3: Initialization

During initialization:
1. Executes static initializers in order
2. Executes static blocks
3. Assigns values to static variables
4. Calls static methods if needed

## JVM Perspective

### What the JVM Sees

When the JVM loads a class, it sees:
- **Class File Structure**: Magic number, version, constant pool
- **Field Descriptors**: Names, types, and access flags
- **Method Descriptors**: Names, return types, and parameters
- **Bytecode Instructions**: The actual code to execute
- **Exception Table**: Exception handlers for each method

### Class File Format

```
ClassFile {
    u4             magic;
    u2             minor_version;
    u2             major_version;
    u2             constant_pool_count;
    cp_info        constant_pool[constant_pool_count-1];
    u2             access_flags;
    u2             this_class;
    u2             super_class;
    u2             interfaces_count;
    u2             interfaces[interfaces_count];
    u2             fields_count;
    field_info     fields[fields_count];
    u2             methods_count;
    method_info    methods[methods_count];
    u2             attributes_count;
    attribute_info attributes[attributes_count];
}
```

### Memory Representation

Class loading affects JVM memory in several ways:

```
┌─────────────────────────────────────────────────────────────┐
│                    Metaspace                                │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Class Metadata                                      │   │
│  │  - Constant Pool                                     │   │
│  │  - Field Descriptors                                 │   │
│  │  - Method Descriptors                                │   │
│  │  - Bytecode                                           │   │
│  │  - Exception Table                                   │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Class Data                                          │   │
│  │  - Static Variables                                  │   │
│  │  - Static Blocks                                     │   │
│  │  - Class Initialization                              │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Syntax

### Class Loading Commands

```bash
# Verbose class loading
java -verbose:class MyApp

# Debug class loading
java -XX:+TraceClassLoading MyApp

# Debug class unloading
java -XX:+TraceClassUnloading MyApp

# Print class loader information
jcmd <pid> VM.classloader_stats

# Print class hierarchy
jcmd <pid> VM.class_hierarchy
```

### Custom Class Loader

```java
public class CustomClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 1. Convert class name to file path
        String fileName = name.replace('.', '/') + ".class";
        
        // 2. Read the class file
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new ClassNotFoundException(name);
            }
            
            byte[] bytes = is.readAllBytes();
            
            // 3. Define the class
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }
}
```

## Easy Example

### Basic Class Loading

```java
package academy.javaengineering.jvm.classloading;

/**
 * Demonstrates basic class loading concepts.
 */
public class BasicClassLoading {
    
    public static void main(String[] args) {
        System.out.println("=== Basic Class Loading ===\n");
        
        // 1. Show class loader for current class
        System.out.println("1. Current class loader:");
        ClassLoader loader = BasicClassLoading.class.getClassLoader();
        System.out.println("   " + loader);
        
        // 2. Show class loader for different classes
        System.out.println("\n2. Different class loaders:");
        System.out.println("   String: " + String.class.getClassLoader());
        System.out.println("   Object: " + Object.class.getClassLoader());
        System.out.println("   ArrayList: " + java.util.ArrayList.class.getClassLoader());
        
        // 3. Show parent delegation
        System.out.println("\n3. Parent delegation chain:");
        ClassLoader current = loader;
        while (current != null) {
            System.out.println("   " + current);
            current = current.getParent();
        }
        
        // 4. Load a class dynamically
        System.out.println("\n4. Dynamic class loading:");
        try {
            Class<?> clazz = Class.forName("academy.javaengineering.jvm.classloading.BasicClassLoading");
            System.out.println("   Loaded: " + clazz.getName());
            System.out.println("   By: " + clazz.getClassLoader());
        } catch (ClassNotFoundException e) {
            System.err.println("   Class not found: " + e.getMessage());
        }
    }
}
```

## Medium Example

### Custom Class Loader Implementation

```java
package academy.javaengineering.jvm.classloading;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Demonstrates custom class loader implementation.
 */
public class CustomClassLoaderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Custom Class Loader Demo ===\n");
        
        // 1. Create a custom class loader
        System.out.println("1. Creating custom class loader:");
        CustomClassLoader customLoader = new CustomClassLoader();
        System.out.println("   Custom class loader created");
        
        // 2. Load a class using the custom class loader
        System.out.println("\n2. Loading class with custom class loader:");
        try {
            Class<?> clazz = customLoader.loadClass("academy.javaengineering.jvm.classloading.CustomClass");
            System.out.println("   Class loaded: " + clazz.getName());
            System.out.println("   Class loader: " + clazz.getClassLoader());
            
            // 3. Create an instance
            System.out.println("\n3. Creating instance:");
            Object instance = clazz.getDeclaredConstructor().newInstance();
            System.out.println("   Instance created: " + instance.getClass().getName());


---

**Continue to Part 2**: [README-part2.md](README-part2.md) | [Part 3](README-part3.md) | [Part 4](README-part4.md)