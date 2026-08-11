# Custom Exceptions — JVM Internals

## Bytecode: Your Exception = JDK Exception

Custom exceptions are compiled to the exact same bytecode as JDK exceptions:

```
// Your custom exception
public class ValidationException extends RuntimeException {
    private String fieldName;
}

// Compiled bytecode (simplified)
public class ValidationException extends java/lang/RuntimeException {
    private String fieldName;
    
    // Same <init> method structure as any RuntimeException
    public <init>(Ljava/lang/String;)V
    public <init>(Ljava/lang/String;Ljava/lang/Throwable;)V
}
```

## Exception Table Entries

When you throw a custom exception:

```java
throw new ValidationException("invalid email");
```

The bytecode is identical to throwing any RuntimeException:

```
0: new #42           // ValidationException
3: dup
4: ldc #43           // "invalid email"
6: invokespecial #44  // ValidationException.<init>
9: athrow            // throw
```

## Class Loading

Custom exceptions follow the same class loading lifecycle:

```
First throw → ClassLoader loads ValidationException
              → Links (verify, prepare, resolve)
              → Initializes (static blocks)
              → Caches in method's exception table
```

## Inheritance Chain

```
ValidationException (your class)
  → RuntimeException (JDK)
    → Exception (JDK)
      → Throwable (JDK)
        → Object (JDK)
```

Each level adds methods and fields. The JVM walks this chain for `instanceof` checks and catch block matching.

## Key Insight

The JVM doesn't know or care that your exception is "custom." It follows the same bytecode instructions, exception table entries, and class loading rules as any JDK exception.
