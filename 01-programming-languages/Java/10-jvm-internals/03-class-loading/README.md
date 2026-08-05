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
            
        } catch (Exception e) {
            System.err.println("   Error: " + e.getMessage());
        }
        
        // 4. Demonstrate class isolation
        System.out.println("\n4. Class isolation:");
        demonstrateClassIsolation();
    }
    
    private static void demonstrateClassIsolation() {
        try {
            // Create two separate class loaders
            CustomClassLoader loader1 = new CustomClassLoader();
            CustomClassLoader loader2 = new CustomClassLoader();
            
            // Load the same class with different loaders
            Class<?> class1 = loader1.loadClass("academy.javaengineering.jvm.classloading.CustomClass");
            Class<?> class2 = loader2.loadClass("academy.javaengineering.jvm.classloading.CustomClass");
            
            // The classes are different objects
            System.out.println("   Same class, different loaders: " + (class1 != class2));
            System.out.println("   Class1 loader: " + class1.getClassLoader());
            System.out.println("   Class2 loader: " + class2.getClassLoader());
            
        } catch (ClassNotFoundException e) {
            System.err.println("   Error: " + e.getMessage());
        }
    }
}

// Class to be loaded
class CustomClass {
    private final String message;
    
    public CustomClass() {
        this.message = "Hello from CustomClass";
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return "CustomClass{message='" + message + "'}";
    }
}

// Custom class loader implementation
class CustomClassLoader extends ClassLoader {
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("   CustomClassLoader: Finding class " + name);
        
        // Convert class name to file path
        String fileName = name.replace('.', '/') + ".class";
        
        try {
            // Read the class file
            InputStream is = getClass().getResourceAsStream(fileName);
            if (is == null) {
                throw new ClassNotFoundException("Class " + name + " not found");
            }
            
            byte[] bytes = is.readAllBytes();
            is.close();
            
            // Define the class
            return defineClass(name, bytes, 0, bytes.length);
            
        } catch (IOException e) {
            throw new ClassNotFoundException("Error loading class " + name, e);
        }
    }
    
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        System.out.println("   CustomClassLoader: Loading class " + name);
        
        // First, check if class is already loaded
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            System.out.println("   CustomClassLoader: Class already loaded");
            return loadedClass;
        }
        
        // Delegate to parent for core classes
        if (name.startsWith("java.") || name.startsWith("javax.")) {
            System.out.println("   CustomClassLoader: Delegating to parent");
            return super.loadClass(name, resolve);
        }
        
        // Try to find the class
        try {
            return findClass(name);
        } catch (ClassNotFoundException e) {
            // If not found, delegate to parent
            System.out.println("   CustomClassLoader: Delegating to parent");
            return super.loadClass(name, resolve);
        }
    }
}
```

## Hard Example

### Hot Deployment System

```java
package academy.javaengineering.jvm.classloading;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Demonstrates a hot deployment system using custom class loaders.
 * This system can reload classes without restarting the JVM.
 */
public class HotDeploymentDemo {
    
    private final Map<String, Class<?>> classCache = new ConcurrentHashMap<>();
    private final Map<String, Long> fileTimestamps = new ConcurrentHashMap<>();
    private final Path classesPath;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public HotDeploymentDemo(String classesPath) {
        this.classesPath = Path.of(classesPath);
        System.out.println("HotDeploymentDemo initialized with path: " + classesPath);
    }
    
    public void startWatching() {
        System.out.println("Starting file watcher...");
        
        // Check for file changes every 5 seconds
        scheduler.scheduleAtFixedRate(this::checkForChanges, 0, 5, TimeUnit.SECONDS);
        
        System.out.println("File watcher started. Watching for changes...");
    }
    
    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    private void checkForChanges() {
        try {
            Files.walk(classesPath)
                .filter(path -> path.toString().endsWith(".class"))
                .forEach(this::checkFile);
        } catch (IOException e) {
            System.err.println("Error checking for changes: " + e.getMessage());
        }
    }
    
    private void checkFile(Path file) {
        try {
            long lastModified = Files.getLastModifiedTime(file).toMillis();
            String fileName = file.toString();
            
            Long previousTimestamp = fileTimestamps.get(fileName);
            if (previousTimestamp == null || lastModified > previousTimestamp) {
                System.out.println("File changed: " + file.getFileName());
                reloadClass(file);
                fileTimestamps.put(fileName, lastModified);
            }
        } catch (IOException e) {
            System.err.println("Error checking file: " + e.getMessage());
        }
    }
    
    private void reloadClass(Path file) {
        try {
            // Convert file path to class name
            String relativePath = classesPath.relativize(file).toString();
            String className = relativePath
                .replace(File.separatorChar, '.')
                .replace(".class", "");
            
            System.out.println("Reloading class: " + className);
            
            // Create a new class loader
            HotDeploymentClassLoader loader = new HotDeploymentClassLoader(classesPath);
            
            // Load the class
            Class<?> newClass = loader.loadClass(className);
            
            // Update cache
            classCache.put(className, newClass);
            
            System.out.println("Class reloaded successfully: " + className);
            
        } catch (Exception e) {
            System.err.println("Error reloading class: " + e.getMessage());
        }
    }
    
    public Class<?> getClass(String className) {
        return classCache.get(className);
    }
    
    public static void main(String[] args) {
        System.out.println("=== Hot Deployment Demo ===\n");
        
        // In a real scenario, you would specify the actual classes path
        HotDeploymentDemo demo = new HotDeploymentDemo("/path/to/classes");
        
        // Start watching for changes
        demo.startWatching();
        
        // Run for a while
        try {
            Thread.sleep(60_000); // Run for 1 minute
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        demo.stop();
        System.out.println("Hot deployment demo stopped.");
    }
}

// Hot deployment class loader
class HotDeploymentClassLoader extends ClassLoader {
    
    private final Path classesPath;
    
    public HotDeploymentClassLoader(Path classesPath) {
        this.classesPath = classesPath;
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // Convert class name to file path
        String fileName = name.replace('.', '/') + ".class";
        Path filePath = classesPath.resolve(fileName);
        
        try {
            // Read the class file
            byte[] bytes = Files.readAllBytes(filePath);
            
            // Define the class
            return defineClass(name, bytes, 0, bytes.length);
            
        } catch (IOException e) {
            throw new ClassNotFoundException("Class " + name + " not found", e);
        }
    }
    
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // Check if class is already loaded
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }
        
        // Delegate to parent for core classes
        if (name.startsWith("java.") || name.startsWith("javax.") || 
            name.startsWith("sun.") || name.startsWith("com.sun.")) {
            return super.loadClass(name, resolve);
        }
        
        // Try to load the class
        try {
            return findClass(name);
        } catch (ClassNotFoundException e) {
            // If not found, delegate to parent
            return super.loadClass(name, resolve);
        }
    }
}
```

## Enterprise Example

### OSGi-like Module System

```java
package academy.javaengineering.jvm.classloading;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.jar.*;

/**
 * Demonstrates a simplified OSGi-like module system.
 * This system provides module isolation and dependency management.
 */
public class ModuleSystemDemo {
    
    private final Map<String, Module> modules = new ConcurrentHashMap<>();
    private final Map<String, Class<?>> globalClasses = new ConcurrentHashMap<>();
    
    public void loadModule(String modulePath) throws Exception {
        System.out.println("Loading module from: " + modulePath);
        
        // Create a new module
        Module module = new Module(modulePath);
        
        // Load all classes in the module
        module.loadClasses();
        
        // Register the module
        modules.put(module.getName(), module);
        
        System.out.println("Module loaded: " + module.getName());
        System.out.println("Classes: " + module.getClassNames());
    }
    
    public Class<?> loadClass(String moduleName, String className) throws Exception {
        Module module = modules.get(moduleName);
        if (module == null) {
            throw new Exception("Module not found: " + moduleName);
        }
        
        return module.loadClass(className);
    }
    
    public void unloadModule(String moduleName) {
        Module module = modules.remove(moduleName);
        if (module != null) {
            System.out.println("Unloading module: " + moduleName);
            module.unload();
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Module System Demo ===\n");
        
        ModuleSystemDemo demo = new ModuleSystemDemo();
        
        // Load modules
        // demo.loadModule("/path/to/module1.jar");
        // demo.loadModule("/path/to/module2.jar");
        
        // Demonstrate module isolation
        demonstrateModuleIsolation();
        
        // Demonstrate module dependencies
        demonstrateModuleDependencies();
    }
    
    private static void demonstrateModuleIsolation() {
        System.out.println("--- Module Isolation ---");
        System.out.println("Each module has its own class loader");
        System.out.println("Classes from different modules are isolated");
        System.out.println("Modules can export specific packages");
    }
    
    private static void demonstrateModuleDependencies() {
        System.out.println("\n--- Module Dependencies ---");
        System.out.println("Modules can depend on other modules");
        System.out.println("Dependencies are resolved automatically");
        System.out.println("Circular dependencies are detected");
    }
}

// Module class
class Module {
    private final String name;
    private final Path modulePath;
    private final ModuleClassLoader classLoader;
    private final Set<String> classNames = new HashSet<>();
    
    public Module(String modulePath) {
        this.modulePath = Path.of(modulePath);
        this.name = modulePath.getFileName().toString().replace(".jar", "");
        this.classLoader = new ModuleClassLoader(modulePath);
    }
    
    public String getName() {
        return name;
    }
    
    public Set<String> getClassNames() {
        return classNames;
    }
    
    public void loadClasses() throws Exception {
        // In a real implementation, you would read the JAR file
        // and load all classes
        System.out.println("Loading classes from module: " + name);
    }
    
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return classLoader.loadClass(className);
    }
    
    public void unload() {
        classNames.clear();
        // In a real implementation, you would clean up resources
    }
}

// Module class loader
class ModuleClassLoader extends ClassLoader {
    
    private final Path modulePath;
    private final List<URL> classpath = new ArrayList<>();
    
    public ModuleClassLoader(String modulePath) {
        this.modulePath = Path.of(modulePath);
        initClasspath();
    }
    
    private void initClasspath() {
        try {
            // Add the module path to classpath
            if (Files.isDirectory(modulePath)) {
                classpath.add(modulePath.toUri().toURL());
            } else if (modulePath.toString().endsWith(".jar")) {
                classpath.add(modulePath.toUri().toURL());
            }
        } catch (Exception e) {
            System.err.println("Error initializing classpath: " + e.getMessage());
        }
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // Try to find the class in the module
        for (URL url : classpath) {
            try {
                Path classPath = Path.of(url.toURI())
                    .resolve(name.replace('.', '/') + ".class");
                
                if (Files.exists(classPath)) {
                    byte[] bytes = Files.readAllBytes(classPath);
                    return defineClass(name, bytes, 0, bytes.length);
                }
            } catch (Exception e) {
                // Continue to next classpath entry
            }
        }
        
        throw new ClassNotFoundException("Class " + name + " not found in module");
    }
    
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // Check if class is already loaded
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }
        
        // Delegate to parent for core classes
        if (name.startsWith("java.") || name.startsWith("javax.") || 
            name.startsWith("sun.") || name.startsWith("com.sun.")) {
            return super.loadClass(name, resolve);
        }
        
        // Try to load the class
        try {
            return findClass(name);
        } catch (ClassNotFoundException e) {
            // If not found, delegate to parent
            return super.loadClass(name, resolve);
        }
    }
}
```

## Performance

### Class Loading Performance Metrics

| Metric | Description | Impact |
|--------|-------------|--------|
| **Loading Time** | Time to load a class | Affects startup |
| **Linking Time** | Time to verify and prepare | Affects startup |
| **Initialization Time** | Time to execute static blocks | Affects startup |
| **Memory Usage** | Memory consumed by class metadata | Affects scalability |
| **Cache Hit Rate** | Percentage of classes loaded from cache | Affects performance |

### Performance Optimization Strategies

1. **Lazy Loading**
   - Load classes only when needed
   - Reduces startup time
   - Reduces memory usage

2. **Class Caching**
   - Cache loaded classes
   - Avoid re-loading the same class
   - Use appropriate cache size

3. **Parallel Loading**
   - Load multiple classes in parallel
   - Reduce loading time
   - Use thread pools

4. **Pre-loading**
   - Load frequently used classes at startup
   - Reduce runtime latency
   - Trade startup time for runtime performance

## Best Practices

### Class Loading Best Practices

1. **Use the Right Class Loader**
   - Application classes: Application ClassLoader
   - Core classes: Bootstrap ClassLoader
   - Extension classes: Extension ClassLoader

2. **Implement Proper Delegation**
   - Always delegate to parent first
   - Load core classes from parent
   - Only load custom classes yourself

3. **Handle Errors Gracefully**
   - Catch ClassNotFoundException
   - Provide meaningful error messages
   - Log class loading failures

4. **Manage Class Loaders**
   - Don't create too many class loaders
   - Close class loaders when done
   - Avoid class loader leaks

5. **Test Class Loading**
   - Test with different class loaders
   - Test class isolation
   - Test hot deployment

## Common Mistakes

### Mistake 1: Breaking Parent Delegation

```java
// BAD: Breaking parent delegation
public class BadClassLoader extends ClassLoader {
    @Override
    protected Class<?> loadClass(String name, boolean resolve) 
            throws ClassNotFoundException {
        // Loading core classes with custom class loader
        return findClass(name); // Bypasses parent
    }
}

// GOOD: Following parent delegation
public class GoodClassLoader extends ClassLoader {
    @Override
    protected Class<?> loadClass(String name, boolean resolve) 
            throws ClassNotFoundException {
        // Delegate to parent for core classes
        if (name.startsWith("java.") || name.startsWith("javax.")) {
            return super.loadClass(name, resolve);
        }
        return findClass(name);
    }
}
```

### Mistake 2: Class Loader Leaks

```java
// BAD: Class loader leak
public class ClassLoaderLeak {
    private static final Map<String, Object> cache = new HashMap<>();
    
    public void loadClass(String name) throws Exception {
        ClassLoader loader = new URLClassLoader(new URL[]{new URL("file:///path/")});
        Class<?> clazz = loader.loadClass(name);
        cache.put(name, clazz.newInstance()); // ClassLoader cannot be GC'd
    }
}

// GOOD: Proper class loader management
public class ProperClassLoader {
    private final ClassLoader loader;
    
    public ProperClassLoader() throws Exception {
        this.loader = new URLClassLoader(new URL[]{new URL("file:///path/")});
    }
    
    public void close() throws Exception {
        // Allow class loader to be GC'd
    }
}
```

### Mistake 3: Not Handling ClassNotFoundException

```java
// BAD: Not handling ClassNotFoundException
public class BadClassLoading {
    public void loadClass(String name) throws Exception {
        Class<?> clazz = Class.forName(name); // May throw
        Object instance = clazz.getDeclaredConstructor().newInstance();
    }
}

// GOOD: Handling ClassNotFoundException
public class GoodClassLoading {
    public void loadClass(String name) {
        try {
            Class<?> clazz = Class.forName(name);
            Object instance = clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + name);
        } catch (Exception e) {
            System.err.println("Error loading class: " + e.getMessage());
        }
    }
}
```

## Pitfalls

### Pitfall 1: NoClassDefFoundError

```java
// BAD: May cause NoClassDefFoundError
public class BadClassPath {
    public void useExternalLibrary() {
        // If the library is not in classpath at runtime
        // this will throw NoClassDefFoundError
        ExternalLibrary.doSomething();
    }
}

// GOOD: Proper classpath management
public class GoodClassPath {
    public void useExternalLibrary() {
        try {
            ExternalLibrary.doSomething();
        } catch (NoClassDefFoundError e) {
            System.err.println("Library not found: " + e.getMessage());
        }
    }
}
```

### Pitfall 2: ClassCastException

```java
// BAD: May cause ClassCastException
public class BadCasting {
    public void castClass(Object obj) {
        // If obj is not of type MyClass
        MyClass myObj = (MyClass) obj; // May throw ClassCastException
    }
}

// GOOD: Safe casting
public class GoodCasting {
    public void castClass(Object obj) {
        if (obj instanceof MyClass) {
            MyClass myObj = (MyClass) obj;
            // Use myObj
        } else {
            System.err.println("Object is not of type MyClass");
        }
    }
}
```

## Debugging Tips

### Class Loading Debug Commands

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

# Print class loading statistics
jcmd <pid> GC.class_stats

# Print class path
jcmd <pid> VM.system_properties | grep java.class.path
```

### Common Class Loading Issues

| Issue | Symptom | Solution |
|-------|---------|----------|
| ClassNotFoundException | Class not found | Check classpath |
| NoClassDefFoundError | Class was found but not at runtime | Check classpath and dependencies |
| ClassCastException | Invalid type conversion | Check type hierarchy |
| LinkageError | Duplicate class definition | Check class loading order |
| SecurityException | Violation of security | Check security settings |

## Comparison Table

### Class Loader Types

| Loader | Loads | Scope | Parent |
|--------|-------|-------|--------|
| **Bootstrap** | Core Java classes | JVM | None |
| **Extension** | Extension classes | JVM | Bootstrap |
| **Application** | Application classes | Classpath | Extension |
| **Custom** | User-defined classes | Custom | Application |

### Class Loading Strategies

| Strategy | Description | Use Case |
|----------|-------------|----------|
| **Eager Loading** | Load all classes at startup | Small applications |
| **Lazy Loading** | Load classes on demand | Large applications |
| **Parallel Loading** | Load multiple classes in parallel | Multi-core systems |
| **Pre-loading** | Load frequently used classes first | Performance-critical apps |

## Decision Tree

### Choosing Class Loading Strategy

```
What type of application?
├── Small Application (< 100 classes)
│   ├── Use: Eager loading
│   └── Benefits: Simple, predictable
├── Medium Application (100-1000 classes)
│   ├── Use: Lazy loading
│   └── Benefits: Faster startup
├── Large Application (> 1000 classes)
│   ├── Use: Lazy loading + pre-loading
│   └── Benefits: Balanced startup and runtime
├── Plugin-based Application
│   ├── Use: Custom class loaders
│   └── Benefits: Module isolation
└── Hot Deployment Required
    ├── Use: Custom class loaders with file watching
    └── Benefits: No restart required
```

## Interview Questions

### Basic Questions

1. **What is class loading?**
   - The process of loading Java bytecode into memory and making it available for execution

2. **What are the three types of class loaders?**
   - Bootstrap, Extension, and Application class loaders

3. **What is the parent delegation model?**
   - Child class loaders delegate to parent before loading a class themselves

4. **What is the difference between ClassNotFoundException and NoClassDefFoundError?**
   - ClassNotFoundException: Class not found in classpath
   - NoClassDefFoundError: Class was found during compilation but not at runtime

### Intermediate Questions

5. **What is the class loading process?**
   - Loading, Linking (Verification, Preparation, Resolution), and Initialization

6. **What is the difference between loading and initialization?**
   - Loading: Finding and reading the class file
   - Initialization: Executing static initializers

7. **What is a custom class loader?**
   - A user-defined class loader that extends ClassLoader and implements findClass()

8. **What is class isolation?**
   - Different class loaders can load the same class name, resulting in different Class objects

### Advanced Questions

9. **How does hot deployment work?**
   - Using custom class loaders to reload classes without restarting the JVM

10. **What is the Metaspace?**
    - Memory area that stores class metadata, replaces PermGen in Java 8+

11. **How does OSGi handle class loading?**
    - Each bundle has its own class loader, with explicit import/export of packages

12. **What are the security implications of class loading?**
    - Malicious code could replace core classes if parent delegation is not followed

## Exercises

### Exercise 1: Class Loader Hierarchy
Write a program that prints the complete class loader hierarchy for different classes.

### Exercise 2: Custom Class Loader
Implement a custom class loader that loads classes from a specific directory.

### Exercise 3: Class Loading Debug
Use `-verbose:class` to analyze class loading for a simple application.

### Exercise 4: Hot Deployment
Implement a simple hot deployment system that reloads classes when files change.

## Assignments

### Assignment 1: Class Loading Analysis
Analyze class loading behavior for a Spring Boot application.

### Assignment 2: Custom Module System
Implement a simple module system with class isolation and dependency management.

### Assignment 3: Performance Optimization
Optimize class loading performance for a large application.

## Mini Project

### Hot Deployment Framework

Create a framework that:
1. Watches for class file changes
2. Reloads changed classes
3. Maintains class isolation
4. Supports plugin architecture

**Requirements:**
- Use custom class loaders
- Implement file watching
- Support class versioning
- Provide monitoring dashboard

## Summary

### Key Takeaways

1. **Class Loading is Fundamental**: It's the first step in Java execution
2. **Parent Delegation Ensures Security**: Core classes cannot be replaced
3. **Class Loaders Provide Isolation**: Different loaders can load same class
4. **Hot Deployment is Possible**: Custom class loaders enable dynamic reloading
5. **Performance Matters**: Class loading affects startup and runtime

### Next Steps

- Continue to Topic 04: Memory Model
- Study OpenJDK class loading source code
- Practice with custom class loaders
- Read "Inside the Java Virtual Machine" by Bill Venners

## References

### Official Documentation
- [JVM Specification - Loading](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-5.html)
- [Java ClassLoader](https://docs.oracle.com/en/java/javase/21/docs/api/java/lang/ClassLoader.html)
- [Custom Class Loading](https://docs.oracle.com/en/java/javase/21/essential/environment/custom.html)

### Books
- "Inside the Java Virtual Machine" by Bill Venners
- "Java Performance" by Scott Oaks
- "OSGi in Action" by Richard S. Hall

### Online Resources
- [Class Loading in Java](https://www.baeldung.com/java-classloader)
- [Custom ClassLoader](https://www.baeldung.com/custom-classloader)
- [Java Class Loading](https://www.jvmhosting.com/java-class-loading/)

### Tools
- [JVisualVM](https://visualvm.java.net/)
- [JConsole](https://docs.oracle.com/en/java/javase/21/docs/technotes/tools/unix/jconsole.html)
- [BTrace](https://github.com/btraceio/btrace)

---

**Next Topic**: [04. Memory Model](../04-memory-model/README.md)
