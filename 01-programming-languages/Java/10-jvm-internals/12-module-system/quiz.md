# 12. Module System - Quiz

## Questions

### Q1: JPMS Purpose
What is the primary purpose of the Java Platform Module System?
- A) To replace the classpath entirely
- B) To provide reliable configuration and strong encapsulation
- C) To improve garbage collection
- D) To enable JIT compilation

**Answer: B**
Explanation: JPMS (Project Jigsaw) provides reliable configuration (dependencies declared and verified at startup) and strong encapsulation (packages hidden by default).

### Q2: Module Declaration
Where is a module declared?
- A) In MANIFEST.MF
- B) In module-info.java at the root of the module's source tree
- C) In pom.xml
- D) In .classpath file

**Answer: B**
Explanation: Each named module has a module-info.java file at the root of its source tree that declares the module's name, dependencies, exports, and services.

### Q3: requires vs requires transitive
What is the difference between requires and requires transitive?
- A) requires is compile-time, requires transitive is runtime
- B) requires transitive exposes the dependency to modules that depend on this module
- C) They are identical
- D) requires transitive is optional

**Answer: B**
Explanation: requires transitive makes the dependency transitive: modules that depend on this module automatically gain access to the transitive dependency's exported packages.

### Q4: exports vs opens
What is the difference between exports and opens?
- A) exports is for compile-time access, opens is for runtime reflection
- B) exports is for all access, opens is for limited access
- C) They are identical
- D) exports is for public APIs, opens is for internal APIs

**Answer: A**
Explanation: exports makes packages accessible at compile-time and runtime. opens makes packages accessible for reflection only (used by frameworks like Spring, Hibernate).

### Q5: Automatic Module
What is an automatic module?
- A) A module defined in module-info.java
- B) A JAR on the module path without module-info.java (gets a generated module name)
- C) A module that loads automatically
- D) A module with no dependencies

**Answer: B**
Explanation: An automatic module is a JAR placed on the module path without a module-info.java. The module system derives a name from the JAR filename and makes all packages exported.

### Q6: Module Resolution
What happens if a required module is missing?
- A) The module is loaded anyway with a warning
- B) The JVM fails at startup with a clear error message
- C) The missing module is loaded lazily
- D) The module is replaced with a default implementation

**Answer: B**
Explanation: JPMS performs reliable configuration at startup. If a required module is missing, the JVM fails immediately with a descriptive error, preventing runtime ClassNotFoundException.

### Q7: Split Packages
What is a split package and why is it prohibited?
- A) A package split across multiple modules; prohibited because it breaks encapsulation
- B) A package with multiple classes; this is normal
- C) A package that is both exported and opened; this is allowed
- D) A package with automatic modules; this is common

**Answer: A**
Explanation: Split packages occur when the same package exists in multiple modules. JPMS prohibits this because it breaks encapsulation and causes ambiguity in class loading.

### Q8: Module Access
Can code in module A access a package in module B that is NOT exported?
- A) Yes, through reflection
- B) No, unless module B opens the package to module A
- C) Yes, if module A requires module B
- D) Yes, through the module system

**Answer: B**
Explanation: Strong encapsulation means unexported packages are inaccessible. The only way to access them is through opens (for reflection) or by being in the same module.

### Q9: Service Loading
How does JPMS support service provider interfaces?
- A) Through classpath scanning
- B) Through uses and provides...with declarations in module-info.java
- C) Through reflection only
- D) Through annotation processing

**Answer: B**
Explanation: A module declares uses <service-interface> to consume services and provides <service> with <implementation> to provide them. The module system resolves providers automatically.

### Q10: Migration Strategy
What is the recommended approach for migrating a classpath application to JPMS?
- A) Rewrite everything as modules immediately
- B) Add module-info.java, declare dependencies, test with --module-path before switching
- C) Remove all dependencies and use only JDK modules
- D) Never migrate; classpath is sufficient

**Answer: B**
Explanation: The recommended migration is incremental: add module-info.java, declare requires for dependencies, export public API packages, open packages for framework reflection, and test thoroughly.

## Score Guide
- **9-10 correct**: Module system expert
- **7-8 correct**: Solid understanding, review migration patterns
- **5-6 correct**: Good start, study module declarations
- **Below 5**: Review basics before proceeding
