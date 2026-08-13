# Checked Exceptions — JVM Internals

## Bytecode: Exception Table

Checked exceptions are handled by the JVM's exception table mechanism:

```
Method: readData()
  Exception table:
    from    to  target type
        0    15    18   Class java/io/IOException
       18    22    25   Class java/sql/SQLException
```

The JVM doesn't distinguish between checked and unchecked at runtime. The exception table is the same for both.

## Compiler's Role

The Java compiler enforces checked exceptions:

```java
// Source code
public void read(String path) throws IOException {
    FileReader fr = new FileReader(path); // may throw FileNotFoundException
}

// Compiler verifies:
// 1. FileNotFoundException is a subclass of IOException ✓
// 2. IOException is declared in throws clause ✓
// 3. If not declared → compile error
```

The bytecode contains no checked/unchecked distinction. The compiler does all the work.

## Runtime Verification

```
Class Loading → Bytecode Verification → Exception Table Validation

During verification:
- Exception handlers are validated against method's exception table
- No checked/unchecked distinction at this level
- Only structural validity is checked
```

## Reflection API

```java
Method m = cls.getMethod("read", String.class);
Class<?>[] exceptions = m.getExceptionTypes();
// Returns: [IOException.class] — read from class file metadata

// This is NOT runtime checking — it's metadata access
```

## JIT Handling

The JIT compiler treats checked and unchecked exceptions identically:

```
// Both checked and unchecked:
if (error) {
    throw new IOException("checked");    // Same JIT path
    throw new RuntimeException("unchecked"); // Same JIT path
}
```

The JIT doesn't care about checked vs unchecked. It optimizes the `throw` instruction.

## Key Insight

The JVM has zero knowledge of checked exceptions. The distinction is purely a compile-time construct enforced by the Java compiler. At runtime, both are just exceptions in the exception table.
