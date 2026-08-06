# Java Standard Class Libraries

The Java class libraries provide the API surface that all Java applications depend on. They are organized into modules, each containing packages with related functionality.

## Module System Overview

Since Java 9, the class libraries are organized into modules. Each module declares its dependencies and exports:

```java
module java.base {
    exports java.lang;
    exports java.util;
    exports java.io;
    exports java.math;
    exports java.time;
    // ... many more packages
}
```

## Key Modules

### java.base — Core Module

The foundation module that every other module depends on. Contains the most essential classes:

| Package | Contents |
|---------|----------|
| `java.lang` | Object, String, System, Thread, Class, Math |
| `java.util` | Collections, Streams, Optional, Date/Time |
| `java.io` | InputStream, OutputStream, Reader, Writer |
| `java.nio` | Buffers, Channels, Charset |
| `java.math` | BigInteger, BigDecimal |
| `java.time` | LocalDate, LocalTime, Instant, Duration |
| `java.lang.annotation` | Annotation types |
| `java.lang.invoke` | Method handles, VarHandle |
| `java.lang.ref` | Reference types (WeakReference, SoftReference) |

### java.net — Networking

| Package | Contents |
|---------|----------|
| `java.net` | URL, URI, Socket, ServerSocket |
| `java.net.http` | HttpClient, HttpRequest, HttpResponse (HTTP/2) |

### java.sql — JDBC

| Package | Contents |
|---------|----------|
| `java.sql` | Connection, Statement, ResultSet |
| `javax.sql` | DataSource, RowSet |

### java.desktop — GUI

| Package | Contents |
|---------|----------|
| `java.awt` | Abstract Window Toolkit |
| `javax.swing` | Swing components |
| `javafx` | JavaFX (separate module) |

### Internal Modules

| Module | Contents |
|--------|----------|
| `jdk.internal.vm.compiler` | Graal compiler internals |
| `jdk.internal.misc` | Internal utilities |
| `jdk.unsupported` | `sun.misc.Unsafe` |

## Pre-compiled Module Storage

The class libraries are pre-compiled and stored in the `lib/modules` file in the JDK image:

```
jdk/
├── lib/
│   ├── modules          # All modules as a single file
│   └── src/             # Source for class libraries
│       ├── java.base/
│       ├── java.net/
│       └── ...
```

The `jimage` tool can extract individual classes from the modules file.

## How Class Libraries Are Compiled

1. Source code lives under `src/` (e.g., `src/java.base/`)
2. Compiled during the build as part of the JDK image
3. Stored in `lib/modules` using the jimage format
4. Loaded by the class loader at runtime

The class libraries are compiled against a minimal set of internal JDK APIs, not against themselves, to avoid circular dependencies.

## Key Source Files

| Module | Source Location |
|--------|----------------|
| java.base | `src/java.base/` |
| java.net | `src/java.net/` |
| java.sql | `src/java.sql/` |
| java.desktop | `src/java.desktop/` |
