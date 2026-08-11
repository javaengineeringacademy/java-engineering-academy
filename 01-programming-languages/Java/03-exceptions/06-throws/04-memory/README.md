# throws — Memory Behavior

## Method Signature Storage

The `throws` clause is stored in the class file's method_info structure:

```
method_info {
    u2 access_flags;
    u2 name_index;
    u2 descriptor_index;
    u2 attributes_count;
    attribute_info attributes[];
    // One attribute is "Exceptions":
    Exceptions_attribute {
        u2 attribute_name_index;
        u4 attribute_length;
        u2 number_of_exceptions;
        u2 exception_index_table[];  // indices into constant pool
    }
}
```

This is static metadata — no runtime memory cost.

## Constant Pool Entries

Each exception class name is stored as a CONSTANT_Class_info entry in the constant pool:

```
Constant Pool:
#1 = Class #20             // IOException
#2 = Class #21             // SQLException
#20 = Utf8 java/io/IOException
#21 = Utf8 java/sql/SQLException
```

## Runtime Cost

| Aspect | Cost |
|--------|------|
| `throws` declaration | 0 bytes runtime overhead |
| Class file storage | ~6 bytes per exception (2-byte index) |
| Reflection access | Reads from class metadata |
| Stack trace printing | Resolves exception names from constant pool |

## vs try-catch

```
throws declaration:  Static, compile-time, zero runtime cost
try-catch block:     Dynamic, runtime, generates exception table entries
```

## Key Insight

`throws` is pure metadata. It lives in the class file, not in the JVM's runtime data structures. The cost is in the class file size, not in execution speed.
