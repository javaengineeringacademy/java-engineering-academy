# Production Patterns — JVM Internals

## Exception Hotspot Detection

JVM flags to monitor exception behavior:

```
# Log every exception thrown
-XX:+PrintConcurrentLocks
-XX:+UnlockDiagnosticVMOptions
-XX:+LogCompilation

# JFR events
java -XX:StartFlightRecording=duration=60s,filename=rec.jfr \
     -XX:FlightRecorderOptions=settings=profile MyApp
```

## JIT Exception Optimization

The JIT compiler optimizes exception handling:

```
// Uncommon trap: if exception rarely thrown
if (condition) {
    throw new Exception("rarely happens"); // JIT may skip this path
}

// Common path: JIT inlines the happy path
result = compute(input); // fast, no exception overhead
```

## Exception Table in Bytecode

```
Method: processOrder()
  Exception table:
    from    to  target type
        0    20    25   Class OrderException
       25    30    35   Class PaymentException
       35    40    45   Class InventoryException
```

The JVM uses this table for O(1) exception handler lookup.

## JIT Compilation of catch Blocks

```
// JIT compiles catch blocks as separate code paths
try {
    normalPath();    // compiled as main path
} catch (Exception e) {
    errorPath();     // compiled as uncommon path (if rarely taken)
}
```

## Key Insight

JVM exception handling is O(1) for handler lookup. The cost is in object allocation and stack trace generation, not in the catch mechanism itself.
