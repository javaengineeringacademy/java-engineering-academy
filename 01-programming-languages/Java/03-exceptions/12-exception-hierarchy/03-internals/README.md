# Exception Hierarchy — JVM Internals

## Class Hierarchy at Runtime

The JVM maintains the class hierarchy in memory:

```
java.lang.Object
  └── java.lang.Throwable
        └── java.lang.Exception
              ├── java.lang.RuntimeException
              │     ├── NullPointerException
              │     ├── IllegalArgumentException
              │     └── ...
              ├── IOException
              │     ├── FileNotFoundException
              │     └── ...
              └── SQLException
```

## instanceof Check

```java
if (e instanceof RuntimeException) {
    // JVM walks the class hierarchy chain
    // Each level: compare class pointer
    // O(depth) — typically 3-5 levels
}
```

The JVM uses the class hierarchy for `instanceof` and catch block matching.

## Catch Block Matching

```
try {
    throw new FileNotFoundException("not found");
} catch (IOException e) {         // match? walk hierarchy
} catch (Exception e) {           // match? walk hierarchy
} catch (Throwable e) {           // match? walk hierarchy
}
```

The JVM walks the exception's class hierarchy to find the first matching catch block.

## Class Hierarchy in Memory

```
Class object for FileNotFoundException:
┌────────────────────────────────────┐
│ super_class: FileIOException       │ ← pointer to parent
│ instance_size: 56 bytes            │
│ access_flags: public               │
│ constant_pool: [...]               │
└────────────────────────────────────┘
```

Each Class object has a `super_class` pointer forming the hierarchy chain.

## Key Insight

The JVM uses the same class hierarchy for exceptions as for any class. Catch block matching is just `instanceof` — walking the `super_class` chain until a match is found.
