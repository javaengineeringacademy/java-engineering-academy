# 07. JIT Compilation - Quiz

## Questions

### Q1: JIT Compilation
What is JIT compilation?
- A) Compiling Java source code to bytecode
- B) Compiling bytecode to native machine code at runtime
- C) Interpreting bytecode line by line
- D) Converting bytecode to assembly language at compile time

**Answer: B**
Explanation: JIT (Just-In-Time) compilation translates frequently-executed bytecode into optimized native machine code at runtime, improving performance.

### Q2: C1 vs C2
What is the difference between C1 and C2 compilers?
- A) C1 is for client apps, C2 is for server apps
- B) C1 does basic optimizations with fast compilation, C2 does aggressive optimizations with slower compilation
- C) C1 is deprecated, C2 is the replacement
- D) There is no difference

**Answer: B**
Explanation: C1 (Client Compiler) compiles quickly with basic optimizations. C2 (Server Compiler) compiles more slowly but applies aggressive optimizations for peak performance.

### Q3: Tiered Compilation
What is tiered compilation?
- A) Using different JVM versions
- B) Progressive compilation from interpreter through C1 to C2
- C) Running multiple JVMs simultaneously
- D) Compiling only the main method

**Answer: B**
Explanation: Tiered compilation starts with interpretation, progresses to C1 (with profiling), then to C2 (with full optimizations). This balances startup speed with peak performance.

### Q4: Method Inlining
What is method inlining?
- A) Removing unused methods
- B) Replacing a method call with the method body
- C) Combining multiple classes into one
- D) Converting methods to lambdas

**Answer: B**
Explanation: Inlining replaces a method invocation with the method's bytecode, eliminating call overhead and enabling further optimizations like constant propagation.

### Q5: Escape Analysis
What does escape analysis determine?
- A) Whether a method throws exceptions
- B) Whether an object escapes the current method/thread
- C) Whether a class implements an interface
- D) Whether a variable is null

**Answer: B**
Explanation: Escape analysis determines if an object's reference can be seen outside the method that created it. Non-escaping objects can be stack-allocated instead of heap-allocated.

### Q6: Deoptimization
When does JIT deoptimization occur?
- A) When the JVM shuts down
- B) When compiled code assumptions are violated (e.g., new class loaded, monomorphic call site becomes polymorphic)
- C) When the code cache is full
- D) When GC runs

**Answer: B**
Explanation: Deoptimization reverts compiled code back to interpreted execution when assumptions made during compilation are invalidated, such as loading a new class that affects virtual method dispatch.

### Q7: Code Cache
What happens when the code cache is full?
- A) The JVM crashes
- B) JIT compilation stops; methods run in interpreted mode
- C) The code cache is automatically expanded
- D) Old compiled code is compacted

**Answer: B**
Explanation: When the code cache is full, no new methods can be compiled. Methods fall back to interpretation, causing significant performance degradation.

### Q8: CompileThreshold
What does -XX:CompileThreshold=10000 control?
- A) Maximum code cache size
- B) Number of method invocations before JIT compilation
- C) Maximum method bytecode size
- D) Number of GC cycles before compilation

**Answer: B**
Explanation: CompileThreshold specifies how many times a method must be invoked before the JIT compiler compiles it. Lower values compile sooner (faster startup, more compilation overhead).

### Q9: Graal JIT
What is the Graal JIT compiler?
- A) A replacement for the HotSpot JVM
- B) An experimental JIT compiler written in Java (self-hosting)
- C) A static compiler for AOT compilation only
- D) A garbage collector

**Answer: B**
Explanation: Graal is an experimental JIT compiler written in Java itself. It replaces C2 and enables advanced optimizations and GraalVM Native Image AOT compilation.

### Q10: PrintCompilation
What does -XX:+PrintCompilation show?
- A) GC events
- B) Method compilation events and optimization levels
- C) Class loading events
- D) Thread creation events

**Answer: B**
Explanation: -XX:+PrintCompilation shows when methods are compiled, their compilation level, and the time spent compiling. This helps understand JIT behavior.

## Score Guide
- **9-10 correct**: JIT expert
- **7-8 correct**: Solid understanding, review deoptimization scenarios
- **5-6 correct**: Good start, study compilation pipeline
- **Below 5**: Review basics before proceeding
