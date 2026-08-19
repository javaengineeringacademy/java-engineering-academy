# 01. Introduction to JVM - Quiz

## Questions

### Q1: JVM vs JRE vs JDK
What is the difference between JVM, JRE, and JDK?
- A) JVM is a development kit, JRE is a virtual machine, JDK is a runtime
- B) JVM executes bytecode, JRE includes JVM + libraries, JDK includes JRE + development tools
- C) JVM and JRE are the same thing
- D) JDK is required to run Java applications

**Answer: B**
Explanation: JVM is the runtime engine that executes bytecode. JRE = JVM + standard libraries. JDK = JRE + development tools (compiler, debugger).

### Q2: Class Loading Order
Which classloader runs first during JVM startup?
- A) Application ClassLoader
- B) Platform ClassLoader
- C) Bootstrap ClassLoader
- D) System ClassLoader

**Answer: C**
Explanation: Bootstrap ClassLoader runs first, loading core Java classes from the java.base module. Platform and Application classloaders run after.

### Q3: Heap vs Stack
Where are primitive local variables stored?
- A) Heap
- B) Metaspace
- C) Stack
- D) Method Area

**Answer: C**
Explanation: Primitive local variables are stored on the stack in the current thread's stack frame. Objects (and their fields) are stored on the heap.

### Q4: JVM Lifecycle
What is the correct order of JVM lifecycle states?
- A) RUNNING → CREATED → INITIALIZED → SHUTDOWN → TERMINATED
- B) CREATED → RUNNING → INITIALIZED → SHUTDOWN → TERMINATED
- C) CREATED → INITIALIZED → RUNNING → SHUTDOWN → TERMINATED
- D) INITIALIZED → CREATED → RUNNING → TERMINATED → SHUTDOWN

**Answer: C**
Explanation: JVM goes: Created (OS creates process) → Initialized (internal structures created) → Running (application executing) → Shutdown (shutdown initiated) → Terminated (JVM exits).

### Q5: Shutdown Hooks
Which of the following does NOT trigger JVM shutdown?
- A) System.exit(0)
- B) All non-daemon threads terminate
- C) A daemon thread completing
- D) SIGTERM signal received

**Answer: C**
Explanation: Daemon threads do not prevent JVM shutdown. JVM shuts down when all non-daemon threads terminate, System.exit() is called, or SIGINT/SIGTERM received.

### Q6: Bytecode
What is the magic number at the beginning of every .class file?
- A) 0xDEADBEEF
- B) 0xCAFEBABE
- C) 0x12345678
- D) 0xFEEDFACE

**Answer: B**
Explanation: All Java class files start with the magic number 0xCAFEBABE, which allows the JVM to verify it's loading a valid class file.

### Q7: Garbage Collection
Which of the following is NOT a responsibility of the JVM Garbage Collector?
- A) Reclaiming memory from unreachable objects
- B) Preventing memory leaks
- C) Managing thread synchronization
- D) Compacting memory to reduce fragmentation

**Answer: C**
Explanation: Garbage collection handles memory management only. Thread synchronization is handled by the JVM's threading subsystem and Java's concurrent utilities.

### Q8: Platform Independence
How does Java achieve platform independence?
- A) By compiling directly to machine code
- B) By interpreting source code directly
- C) By compiling to platform-neutral bytecode executed by the JVM
- D) By using platform-specific libraries

**Answer: C**
Explanation: Java compiles .java files to .class bytecode files. The JVM interprets/JIT-compiles this bytecode into platform-specific machine code at runtime.

### Q9: Metaspace
What replaced PermGen in Java 8+?
- A) Code Cache
- B) Native Memory
- C) Metaspace
- D) Young Generation

**Answer: C**
Explanation: Metaspace replaced PermGen starting in Java 8. It stores class metadata and uses native memory instead of being part of the heap.

### Q10: JIT Compilation
What is the primary benefit of JIT compilation?
- A) Reduces memory usage
- B) Improves security
- C) Converts frequently-used bytecode to native code for faster execution
- D) Eliminates the need for garbage collection

**Answer: C**
Explanation: JIT (Just-In-Time) compilation identifies hot methods and compiles them to optimized native machine code, significantly improving runtime performance.

## Score Guide
- **9-10 correct**: JVM expert, ready for advanced topics
- **7-8 correct**: Solid foundation, review weak areas
- **5-6 correct**: Good start, study the weak topics
- **Below 5**: Review the basics before proceeding
