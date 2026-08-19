# 12. Module System Memory Details

## Module System Memory Impact

### Module Descriptor Memory

```
Module Descriptor (module-info.class):
├── Module name: ~50-100 bytes
├── Requires list: ~20 bytes per dependency
├── Exports list: ~30 bytes per exported package
├── Opens list: ~30 bytes per opened package
├── Uses list: ~20 bytes per service interface
├── Provides list: ~40 bytes per service provider
└── Total per module: ~200-1000 bytes

For 100 modules: ~20-100KB
```

### Module Layer Memory

```
Module Layer:
├── Module descriptors: ~200-1000 bytes per module
├── Package lookup tables: ~50 bytes per package
├── Service provider cache: ~100 bytes per service
├── Access control arrays: ~10 bytes per module pair
└── Total per layer: ~10-100KB

Multiple layers (application servers):
├── Web app layer: ~10-50KB per app
├── Shared layer: ~50-200KB
└── Boot layer: ~100-500KB
```

### Module System Performance

```
Startup Improvement:
├── Module system enables better JIT:
│   ├── Smaller initial class set
│   ├── Faster class resolution
│   └── Better escape analysis
├── Estimated startup improvement: 5-15%
└── Memory reduction: 10-20%

jlink Custom Runtime:
├── Includes only required modules
├── Removes unused JDK classes
├── Typical size reduction: 30-60%
└── Example: Hello world from 300MB to 40MB
```

### Package Access Control Memory

```
Access Control Data Structures:
├── Module export table: ~10 bytes per (module, package) pair
├── Module opens table: ~10 bytes per (module, package) pair
├── Access check cache: ~100 bytes per (caller, target) pair
├── Reflection restriction arrays: ~10 bytes per package
└── Total: ~1KB per module, ~100KB for 100 modules
```
