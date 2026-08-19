# 03. Class Loading Lifecycle - Quiz

## Questions

### Q1: Loading Phases
What is the correct order of the three main class loading phases?
- A) Initialization, Linking, Loading
- B) Loading, Initialization, Linking
- C) Loading, Linking, Initialization
- D) Linking, Loading, Initialization

**Answer: C**
Explanation: Class loading follows: Loading (read bytes, create Class object) then Linking (Verification, Preparation, Resolution) then Initialization (execute static initializers).

### Q2: Class.forName() Behavior
What happens when you call `Class.forName("com.example.MyClass")`?
- A) The class is loaded but not initialized
- B) The class is loaded AND initialized (static block executes)
- C) Only the classloader is determined
- D) The class is linked but not loaded

**Answer: B**
Explanation: `Class.forName(String)` loads, links, AND initializes the class. The static initializer (`<clinit>`) executes. Use `Class.forName(name, false, loader)` to load without initializing.

### Q3: Preparation vs Initialization
During which phase are static fields assigned their default values (0, null, false)?
- A) Loading
- B) Preparation
- C) Initialization
- D) Resolution

**Answer: B**
Explanation: During Preparation, static fields are allocated memory and assigned JVM default values. During Initialization, they are assigned the values specified in the source code.

### Q4: Verification
What does the verification phase check?
- A) That the class has a main method
- B) That the bytecode is valid, type-safe, and does not violate JVM constraints
- C) That all imports resolve correctly
- D) That the class file was compiled with the correct JDK version

**Answer: B**
Explanation: Verification checks the magic number (0xCAFEBABE), version compatibility, constant pool validity, type safety of bytecode instructions, and stack map frame consistency.

### Q5: Resolution
What happens during the resolution phase?
- A) Static fields are initialized
- B) Symbolic references are replaced with direct references
- C) Bytecode is verified for type safety
- D) The class is loaded from disk

**Answer: B**
Explanation: Resolution replaces symbolic references (strings in the constant pool) with direct references (pointers to actual class objects, field offsets, method entry points).

### Q6: LinkageError
A `LinkageError` is thrown when:
- A) A class file cannot be found on the classpath
- B) Two classloaders load the same class, causing a conflict
- C) The class file format is invalid
- D) A method is called with wrong argument types

**Answer: B**
Explanation: `LinkageError` with "already loaded" indicates two classloaders loaded the same class. ClassFormatError indicates invalid bytecode format.

### Q7: Static Initialization
In what order do static initializers execute?
- A) In the order they appear in the source code
- B) In alphabetical order by variable name
- C) In random order
- D) In reverse order

**Answer: A**
Explanation: Static variables and static blocks execute in the order they appear in the source code. This is defined by the JVM specification for the `<clinit>` method.

### Q8: Lazy Loading
When is a class actually loaded by the JVM?
- A) When the .class file is compiled
- B) When the class is referenced for the first time
- C) When the JVM starts
- D) When the classpath is scanned

**Answer: B**
Explanation: Classes are loaded lazily when first referenced. This can happen through object creation, static field access, method invocation, or reflection.

### Q9: Class.forName vs ClassLoader.loadClass
What is the key difference between `Class.forName(name)` and `classLoader.loadClass(name)`?
- A) They are identical
- B) `Class.forName` initializes the class; `loadClass` does not
- C) `loadClass` initializes the class; `Class.forName` does not
- D) `Class.forName` uses a different classloader

**Answer: B**
Explanation: `Class.forName(name)` loads AND initializes (runs static block). `classLoader.loadClass(name)` loads but does NOT initialize.

### Q10: Circular Initialization
What happens when two classes have circular static dependencies?
- A) The program crashes with StackOverflowError
- B) One class sees the other's default values (0, null) instead of initialized values
- C) Both classes initialize successfully
- D) A ClassCircularityError is thrown

**Answer: B**
Explanation: If class A's static initializer references class B, and B's static initializer references A, one class will see the other's default values (not initialized values).

## Score Guide
- **9-10 correct**: Class loading expert
- **7-8 correct**: Solid understanding, review edge cases
- **5-6 correct**: Good start, study linking phases
- **Below 5**: Review basics before proceeding
