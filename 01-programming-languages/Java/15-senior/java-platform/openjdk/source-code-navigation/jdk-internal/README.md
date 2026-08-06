# Finding JDK Internal APIs in the Source

JDK internal APIs are implementation details that are not part of the public Java SE specification. They live under `jdk.internal.*` and `sun.misc.*`.

## Key Packages and Locations

### sun.misc

| Class | Path |
|-------|------|
| `Unsafe` | `src/jdk.unsupported/share/classes/sun/misc/Unsafe.java` |

### jdk.internal.misc

| Class | Path |
|-------|------|
| `Unsafe` | `src/java.base/share/classes/jdk/internal/misc/Unsafe.java` |
| `DirectMethodHandle` | `src/java.base/share/classes/jdk/internal/misc/` |

### jdk.internal.reflect

| Class | Path |
|-------|------|
| `ReflectionFactory` | `src/java.base/share/classes/jdk/internal/reflect/ReflectionFactory.java` |
| `MethodAccessor` | `src/java.base/share/classes/jdk/internal/reflect/` |
| `ConstructorAccessor` | `src/java.base/share/classes/jdk/internal/reflect/` |

### jdk.internal.loader

| Class | Path |
|-------|------|
| `ClassLoaders` | `src/java.base/share/classes/jdk/internal/loader/ClassLoaders.java` |
| `BootClassLoader` | `src/java.base/share/classes/jdk/internal/loader/BootClassLoader.java` |
| `URLClassPath` | `src/java.base/share/classes/jdk/internal/loader/URLClassPath.java` |
| `NativeLibraries` | `src/java.base/share/classes/jdk/internal/loader/NativeLibraries.java` |

### jdk.internal.vm

| Class | Path |
|-------|------|
| `Unsafe` | (part of jdk.internal.misc) |
| `RuntimeMethods` | `src/java.base/share/classes/jdk/internal/vm/` |
| `annotation` | `src/java.base/share/classes/jdk/internal/vm/annotation/` |

### jdk.internal.util

| Class | Path |
|-------|------|
| `Bits` | `src/java.base/share/classes/jdk/internal/util/Bits.java` |
| `HexFormat` | `src/java.base/share/classes/jdk/internal/util/HexFormat.java` |

### jdk.internal.jimage

| Class | Path |
|-------|------|
| `ImageReader` | `src/jdk.jimage/share/classes/jdk/internal/jimage/` |

## sun.misc.Unsafe Navigation

The most commonly referenced internal API. Find its location:

```bash
# Public-facing API
find src -path "*/sun/misc/Unsafe.java"

# Internal implementation
find src -path "*/jdk/internal/misc/Unsafe.java"
```

## Finding Internal Native Code

```bash
# Find internal JNI implementations
rg "Java_jdk_internal" src/ --include="*.cpp"

# Find Unsafe native methods
rg "Java_sun_misc_Unsafe" src/ --include="*.cpp"
rg "Java_jdk_internal_misc_Unsafe" src/ --include="*.cpp"
```

## Module Access Control

Internal APIs are restricted. To use them from application code:

```bash
# Requires both --add-opens and --add-exports
java \
  --add-opens java.base/jdk.internal.misc=ALL-UNNAMED \
  --add-exports java.base/jdk.internal.misc=ALL-UNNAMED \
  MyApp

# For sun.misc.Unsafe specifically
java \
  --add-opens java.base/sun.misc=ALL-UNNAMED \
  --add-exports java.base/sun.misc=ALL-UNNAMED \
  MyApp
```

## Why Internal APIs Matter

- **Performance**: `Unsafe` enables lock-free algorithms
- **Frameworks**: Reflection frameworks use internal APIs
- **Migration**: Java provides migration paths (VarHandle, Foreign Function API)
- **Security**: Internal APIs are the source of many security fixes
- **Evolution**: Internal APIs change without deprecation cycle

## Key Source Files

| Path | Contents |
|------|----------|
| `src/jdk.unsupported/share/classes/` | sun.misc.Unsafe |
| `src/java.base/share/classes/jdk/internal/` | All jdk.internal.* classes |
| `src/java.base/share/classes/sun/` | sun.* internal classes |
| `src/java.base/unix/native/` | Unix native internal impl |
| `src/java.base/windows/native/` | Windows native internal impl |
