# throws — JVM Internals

## Bytecode: throws Declaration

The `throws` keyword has no bytecode representation. It is compile-time metadata stored in the method's attribute table:

```
Method: processData(int)
  throws java.io.IOException, java.sql.SQLException
  Code:
    0: iload_1
    1: invokestatic  #42    // Method validate:(I)V
    4: return
  Exception table:
    from    to  target type
        0     4     7   Class java/io/IOException
```

The `throws` clause is in the method's `Exceptions` attribute, not in the bytecode instructions.

## Method Descriptor vs Throws

```
Method descriptor: (I)V              ← parameter types + return type
Throws attribute:  IOException, SQLException  ← declared checked exceptions
```

The JVM uses the `throws` attribute for:
- Compiler verification (checked exception enforcement)
- Reflection API (`Method.getExceptionTypes()`)
- Stack trace printing

## Compiler Transformation

```java
// Source
public void read(String path) throws IOException {
    FileReader fr = new FileReader(path);
}

// Compiled bytecode (simplified)
public void read(String path) throws IOException {
    // Compiler inserts exception mapping
    try {
        FileReader fr = new FileReader(path);
    } catch (FileNotFoundException e) {
        // Compiler wraps in IOException if needed
        throw new IOException(e);
    }
}
```

The compiler may transform exceptions to match the `throws` declaration.

## Reflection Access

```java
Method m = MyClass.class.getMethod("read", String.class);
Class<?>[] exceptions = m.getExceptionTypes();
// Returns: [IOException.class]
```

The JVM reads the `Exceptions` attribute from the class file to populate this.

## Key Insight

`throws` is a compile-time contract, not a runtime instruction. The JVM enforces it only at class loading time (verification) and through reflection, not through bytecode execution.
