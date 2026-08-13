# Unchecked Exceptions — JVM Internals

## Bytecode: No Distinction

The JVM treats checked and unchecked exceptions identically at the bytecode level:

```
Exception table:
    from    to  target type
        0    10    15   Class java/lang/RuntimeException
       15    20    25   Class java/io/IOException
```

Both are just `Class` references in the exception table. No flag distinguishes them.

## RuntimeException's Special Status

`RuntimeException` is special because:
- Compiler doesn't force you to catch or declare it
- JVM doesn't treat it differently at runtime
- The "unchecked" behavior is purely compiler logic

```java
// Compiler allows this (no throws declaration needed)
public void risky() {
    throw new RuntimeException("unchecked");
}

// Compiler requires this (throws declaration needed)
public void safe() throws IOException {
    throw new IOException("checked");
}
```

## Bytecode Instructions

```
// throw instruction (same for both)
0: new #42        // RuntimeException or IOException — same instruction
3: dup
4: invokespecial #43  // <init>
7: athrow          // throw — same instruction for both
```

The `athrow` instruction doesn't know or care about checked vs unchecked.

## JIT Optimization

```
// HotSpot JIT: both paths optimized identically
public void process() {
    // Checked path
    try {
        checkedMethod();
    } catch (IOException e) { ... }

    // Unchecked path
    try {
        uncheckedMethod();
    } catch (RuntimeException e) { ... }
}
```

The JIT sees both as exception handlers. No special treatment.

## Verification

During class loading:
- Bytecode verifier checks exception table validity
- No checked/unchecked distinction
- Only structural validation (class exists, handler range valid)

## Key Insight

The JVM is completely unaware of the checked/unchecked distinction. It's a Java compiler feature only. At runtime, `RuntimeException` and `IOException` are handled by the exact same bytecode instructions.
