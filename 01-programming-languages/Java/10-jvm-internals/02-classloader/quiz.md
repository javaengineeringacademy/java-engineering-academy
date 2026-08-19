# 02. ClassLoader - Quiz

## Questions

### Q1: Parent Delegation
When the Application ClassLoader receives a request to load a class, what is the first thing it does?
- A) Checks its own cache of loaded classes
- B) Delegates to the Bootstrap ClassLoader
- C) Delegates to the Platform ClassLoader
- D) Searches the classpath directly

**Answer: C**
Explanation: The Application ClassLoader first delegates to its parent (Platform ClassLoader), which in turn delegates to Bootstrap. Only if the parent chain cannot find the class does the child attempt to load it.

### Q2: Class Identity
Two classloaders each load a class named `com.example.MyClass`. How many Class objects are created?
- A) One, because the class name is the same
- B) Two, because each classloader defines a separate class
- C) Depends on the JVM implementation
- D) Two only if the bytecode differs

**Answer: B**
Explanation: A class is uniquely identified by its fully qualified name AND its defining classloader. Two different classloaders loading the same class name produce two distinct Class objects.

### Q3: Bootstrap ClassLoader
What type is the reference returned by `Object.class.getClassLoader()`?
- A) `sun.misc.Launcher$AppClassLoader`
- B) `sun.misc.Launcher$ExtClassLoader`
- C) `null`
- D) `java.lang.BootstrapClassLoader`

**Answer: C**
Explanation: The Bootstrap ClassLoader is implemented in native code (C/C++) and has no Java representation. `getClassLoader()` returns `null` for classes loaded by the Bootstrap ClassLoader.

### Q4: ClassNotFoundException vs NoClassDefFoundError
Which statement is true?
- A) Both are thrown during class loading
- B) ClassNotFoundException is thrown by `Class.forName()`, NoClassDefFoundError is thrown by the JVM when a class file is missing at runtime
- C) NoClassDefFoundError is only thrown during linking
- D) They are identical errors with different names

**Answer: B**
Explanation: `ClassNotFoundException` is a checked exception thrown when the classloader cannot find a class. `NoClassDefFoundError` is an error thrown by the JVM when it detects that a class definition, present at compile time, is no longer available at runtime.

### Q5: Custom ClassLoader
In a custom classloader, which method must be overridden?
- A) `loadClass(String name)`
- B) `findClass(String name)`
- C) `resolveClass(Class<?> c)`
- D) `defineClass(String name, byte[] b, int off, int len)`

**Answer: B**
Explanation: Overriding `findClass()` follows the parent delegation model. The `loadClass()` method handles delegation; overriding it bypasses parent delegation entirely, which is usually undesirable.

### Q6: Class Unloading
When can a class be garbage collected?
- A) When no instances of the class exist
- B) When no references to the class or its instances exist
- C) When its defining classloader is eligible for garbage collection
- D) Never, classes are permanently loaded in the JVM

**Answer: C**
Explanation: A class can only be unloaded when its defining classloader is garbage collected. Since the Bootstrap, Platform, and Application classloaders are never collected, only classes loaded by custom classloaders can be unloaded.

### Q7: Thread Context ClassLoader
Why was the Thread Context ClassLoader introduced?
- A) To improve performance of class loading
- B) To break parent delegation when parent-first loading prevents loading from child classloaders
- C) To replace the Bootstrap ClassLoader
- D) To enable parallel class loading

**Answer: B**
Explanation: The Thread Context ClassLoader allows code in parent classloaders (like the JDK libraries) to load classes from child classloaders. This is essential for SPI (Service Provider Interface) patterns like JDBC.

### Q8: ClassLoader Leaks
Which of the following is the most common source of classloader memory leaks?
- A) Unused imports
- B) ThreadLocal values not removed in finally blocks
- C) String literals in switch statements
- D) Static final fields

**Answer: B**
Explanation: ThreadLocal values hold strong references to the classloader that defined them. If not removed, they prevent the classloader from being garbage collected, causing a memory leak that grows with each redeployment.

### Q9: Linking Phases
What is the correct order of the linking phases?
- A) Resolution → Verification → Preparation
- B) Preparation → Verification → Resolution
- C) Verification → Preparation → Resolution
- D) Verification → Resolution → Preparation

**Answer: C**
Explanation: Linking proceeds in order: Verification (check bytecode integrity) → Preparation (allocate static field memory, assign defaults) → Resolution (replace symbolic references with direct references).

### Q10: Module System Impact
How does the Java Module System (JPMS) affect classloading?
- A) It eliminates classloaders entirely
- B) It adds a module layer that sits above classloaders and provides stronger encapsulation
- C) It only changes the file format of .class files
- D) It has no impact on classloading

**Answer: B**
Explanation: JPMS introduces module layers that coordinate with classloaders. Each module layer has its own classloader configuration. Modules provide strong encapsulation by restricting which packages are accessible.

## Score Guide
- **9-10 correct**: ClassLoader expert
- **7-8 correct**: Solid understanding, review weak areas
- **5-6 correct**: Good start, study the delegation model
- **Below 5**: Review basics before proceeding
