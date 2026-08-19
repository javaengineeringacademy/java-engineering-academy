# 11. JVM Security Memory Details

## Security Data Structures

### ProtectionDomain Memory

```
ProtectionDomain per class:
├── CodeSource: ~50-100 bytes
│   ├── URL: variable (string length)
│   └── Certificate chain: ~200-500 bytes
├── PermissionCollection: ~100-500 bytes
│   ├── Number of permissions: variable
│   └── Permission objects: ~50 bytes each
├── ClassLoader reference: 4-8 bytes
└── Signers: ~100-200 bytes

Total per ProtectionDomain: ~500-1500 bytes
For 1000 loaded classes: ~500KB-1.5MB
```

### Security Policy Memory

```
Policy File Loading:
├── Policy parser: ~10KB code
├── Permission objects: ~50 bytes each
├── Code source mappings: ~100 bytes each
├── Grant entries: ~200 bytes each
└── Total for typical policy: ~10-100KB

Runtime Policy:
├── Permission collection: ~1-10KB per domain
├── Policy refresh: periodic re-read
└── Cache: O(1) lookup per permission check
```

### Deserialization Filter Memory

```
Filter Evaluation:
├── Class name matching: ~100 bytes per pattern
├── Filter rules: ~500 bytes per filter
├── Deserialized object graph: variable
├── Filter state: ~1KB per stream
└── Total per deserialization: ~1-10KB overhead
```

### Bytecode Verification Memory

```
Verification Data Structures:
├── Stack map frame table: ~10-100 bytes per method
├── Type checking state: ~1KB per verification context
├── Constant pool references: ~10 bytes per reference
├── Verification logs: ~100-1000 bytes per class
└── Total per class verification: ~1-10KB temporary

Verification is one-time (result cached).
```
