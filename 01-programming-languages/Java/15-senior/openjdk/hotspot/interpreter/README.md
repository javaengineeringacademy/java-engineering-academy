# HotSpot Bytecode Interpreter

The interpreter is the first execution engine in HotSpot. Every Java method starts execution in the interpreter before potentially being promoted to JIT-compiled code.

## How Interpretation Works

### Bytecode Execution

The interpreter reads bytecode instructions one at a time and executes them. Each bytecode handler is a small piece of C++ (or platform-specific assembly) that implements the instruction's semantics.

```
Source: int result = a + b;

Bytecode:
  iload_1        // push local variable 1 (a)
  iload_2        // push local variable 2 (b)
  iadd           // pop two ints, push their sum
  istore_3       // store result in local variable 3
```

### Template Interpreter vs. C++ Interpreter

HotSpot has two interpreter implementations:

| Interpreter | Description |
|-------------|-------------|
| **Template Interpreter** (default) | Each bytecode has a hand-crafted assembly template. Faster but platform-specific. |
| **C++ Interpreter** | Uses a switch statement in C++. Portable but slower. Used on platforms without assembly templates. |

The Template Interpreter is used on x86, x86_64, AArch64, and other major platforms. Each template is a small assembly stub that performs the operation and jumps to the next template.

### Interpreter Loop

The core of the interpreter is a dispatch loop:

```
while (true) {
    bytecode = fetch_bytecode();
    switch (bytecode) {
        case iload_1:  handle_iload_1(); break;
        case iadd:     handle_iadd(); break;
        case goto:     handle_goto(); break;
        // ... all 200+ bytecodes
    }
}
```

In the Template Interpreter, this becomes threaded dispatch — each handler jumps directly to the next handler without going through a central loop.

## Interpreter Stubs

### Registered Stubs

The interpreter generates platform-specific machine code stubs at startup:

- **Entry stubs**: Code to enter the interpreter for a method
- **Interpreter dispatch table**: Array of function pointers for each bytecode
- **Handler tables**: Pre-compiled handlers for quick dispatch

### Adapter Stubs

When a compiled method calls an interpreted method (or vice versa), adapters handle the calling convention differences:

- **I2C adapter**: Interpreter-to-compiled adapter
- **C2I adapter**: Compiled-to-interpreter adapter
- **C2C adapter**: Compiled-to-compiled (rare, for unverified calls)

## Profiling in the Interpreter

The interpreter is responsible for collecting profiling data used by the JIT compilers:

### Method Invocation Counts

```
Method: com.example.MyClass.compute()
  Invocations: 15,432
  Backedge count: 8,201
```

When a method's invocation count exceeds `CompileThreshold` (default 10,000), it is queued for JIT compilation.

### Branch Profiling

The interpreter records which branches are taken:

```
if (x > 0) {    // Taken: 98.2% of the time
    // ...
} else {         // Taken: 1.8% of the time
    // ...
}
```

This data helps the JIT compiler order basic blocks for the common case.

### Type Profiling

The interpreter records the actual types of objects at call sites:

```
invokevirtual: List.add(Object)
  Receiver types: ArrayList (99.1%), LinkedList (0.9%)
```

This enables speculative devirtualization — the JIT can inline if it knows the concrete type.

### Inline Cache

For virtual calls, the interpreter maintains an inline cache:

```
Call site: list.add(element)
  Cache[0]: ArrayList.add() — hits: 14,500
  Cache[1]: LinkedList.add() — hits: 120
  Cache[2]: <empty>
```

## Performance Characteristics

### Interpreter vs. JIT

| Aspect | Interpreter | JIT Compiled |
|--------|-------------|--------------|
| Startup time | Fast | Slow (compilation overhead) |
| Peak performance | Lower | Higher (10–100x faster) |
| Memory usage | Lower | Higher (compiled code cache) |
| Compilation overhead | None | Significant |

### When Interpretation Matters

- **Startup phase**: The interpreter runs immediately while JIT compiles in the background
- **Cold methods**: Methods called rarely stay in the interpreter forever
- **Deoptimization**: When JIT assumptions fail, execution falls back to the interpreter

## Key Source Files

| File | Purpose |
|------|---------|
| `src/hotspot/cpu/*/templateInterpreter.cpp` | Platform-specific template interpreter |
| `src/hotspot/share/interpreter/interpreter.cpp` | Common interpreter code |
| `src/hotspot/share/interpreter/bytecodeInterpreter.cpp` | C++ interpreter implementation |
| `src/hotspot/share/interpreter/invocationCounter.hpp` | Invocation counting for JIT threshold |
| `src/hotspot/share/interpreter/bootstrapInfo.hpp` | Method resolution in interpreter |
