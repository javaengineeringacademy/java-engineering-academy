# Thread Creation - Memory Model

## Thread Creation Memory Layout

### Object Allocation on Thread Creation

When creating threads, multiple objects are allocated:

```
new Thread(runnable)
│
├── Thread object (heap)
│   ├── target (Runnable reference)
│   ├── name (String on heap)
│   ├── threadGroup (reference)
│   ├── daemon (boolean)
│   ├── priority (int)
│   └── stackSize (long)
│
├── Runnable object (heap) - may be shared
│   └── captured variables (closures)
│
└── Native thread resources (off-heap)
    ├── Stack memory (512KB-1MB)
    ├── Thread Control Block (TCB)
    └── Register state
```

### Memory Cost per Thread

| Resource | Size | Location |
|----------|------|----------|
| Thread object | ~64 bytes | Heap |
| Stack (default) | 512KB-1MB | Native memory |
| TCB | ~1KB | Native memory |
| JIT code cache | Shared | Native memory |
| **Total per platform thread** | **~1MB** | **Mixed** |
| **Virtual thread** | **~1KB** | **Heap only** |

### Why Virtual Threads Are Lightweight

Virtual threads avoid per-thread native stack allocation:
- Only the Java continuation state is stored on heap
- No native stack until mounted on a carrier
- Millions of virtual threads can exist simultaneously
- When blocked, only the Java state is stored (not native stack)

### Thread Local Storage (TLS)

Each thread maintains private copies of ThreadLocal variables:

```java
ThreadLocal<SimpleDateFormat> formatter =
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

// Each thread gets its own SimpleDateFormat instance
// No synchronization needed for thread-local data
```

### Closure Capture in Lambda Threads

When a lambda captures variables for a thread:

```java
int count = 0; // Captured variable
Thread t = new Thread(() -> {
    // 'count' is captured as a final copy
    // Changes to 'count' in lambda don't affect outer scope
    System.out.println(count);
});
```
