# 03. Class Loading Memory Details

## Memory Impact of Class Loading

### Metaspace Growth During Loading

Each loaded class consumes Metaspace memory:

```
Class Loading Memory Cost:
├── Class metadata: ~200-500 bytes
│   ├── Constant pool: 100-300 bytes
│   ├── Field descriptors: 20-50 bytes per field
│   └── Method descriptors: 30-80 bytes per method
├── Method bytecode: ~50-200 bytes per method
├── StackMapTable: 10-100 bytes per method
├── Exception table: 10-50 bytes per method
└── Attributes: 50-200 bytes total

Total per class: ~500 bytes - 3 KB (varies with complexity)
```

### Initialization and Memory

During class initialization, static fields consume heap memory:

```
Static Field Memory:
├── Primitives: 4-8 bytes each (int, long, etc.)
├── References: 4-8 bytes each (compressed oops: 4 bytes)
├── Arrays: 16 bytes header + elements
└── Objects: 16 bytes header + fields + padding

Example:
class Config {
    static int TIMEOUT = 30;           // 4 bytes in Metaspace
    static String NAME = "app";        // 4 bytes (ref) + String object on heap
    static int[] VALUES = {1, 2, 3};   // 4 bytes (ref) + 28 bytes array on heap
}
// Total: 12 bytes Metaspace + 48 bytes heap
```

### Class Loading Caching

Classes are cached in the classloader's loaded class table:

```
Loaded Class Cache:
├── Hash table: ClassLoader -> Map<String, Class>
├── Key: fully qualified class name
├── Value: Class object reference
├── Lookup: O(1) average (hash table)
└── Growth: new entry per loaded class

Memory for cache: ~50-100 bytes per entry (hash table overhead)
```

### Initialization Ordering and Memory

When a class is initialized, the JVM must track initialization state:

```
Initialization State (per class):
├── UNINITIALIZED: Class has not been initialized
├── INITIALIZING: Thread is running <clinit>
├── INITIALIZED: <clinit> completed successfully
└── ERRONEOUS: <clinit> threw an exception

Memory cost: ~16 bytes per class (state enum + lock)
```
