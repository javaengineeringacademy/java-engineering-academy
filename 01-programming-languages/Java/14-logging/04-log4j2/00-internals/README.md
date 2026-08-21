# Internals: Log4j 2 Architecture

## Plugin System

Log4j 2 uses a plugin system for extensibility:

```
1. At startup, Log4j scans classpath for plugin descriptors
2. Plugins are registered in PluginRegistry
3. Configuration parser uses registry to create objects
4. Custom plugins can extend appenders, filters, layouts, etc.
```

**Plugin discovery:**
```
META-INF/log4j-core/org.apache.logging.log4j.core.config.plugins.Plugin
```

## Logger Implementation

```java
// Log4j 2 Logger is a lightweight wrapper
public class Logger {
    private final String name;
    private final LoggerContext context;
    private final LoggerConfig loggerConfig;
    
    // Level check: O(1)
    public boolean isEnabled(Level level) {
        return loggerConfig.isEnabled(level);
    }
}
```

## AsyncLogger with Disruptor

```
Caller Thread                    Disruptor Ring Buffer              Async Thread
     │                                 │                               │
     ├── logEvent ────────────────────►│ [e1][e2][e3]...[e255]        │
     │   (claim sequence)              │   (ring buffer)               │
     │                                 │                               │
     │   (continues)                   │                               ├── consume
     │                                 │                               ├── write
     │                                 │                               └── publish sequence
```

**Disruptor advantages:**
- Lock-free: Uses sequence numbers instead of locks
- Cache-line friendly: Ring buffer fits in CPU cache
- No garbage: Reuses event objects

## Garbage-Free Mode

```xml
<Configuration strict="true">
    <!-- Enables garbage-free features -->
</Configuration>
```

**What it eliminates:**
- String concatenation in log messages
- Object allocation for log events
- Layout string building
- Exception stack trace formatting (until needed)

## Configuration Loading

```
1. Check system property: -Dlog4j.configurationFile
2. Check classpath for:
   a. log4j2-test.json (test)
   b. log4j2-test.yaml
   c. log4j2-test.xml
   d. log4j2.json (main)
   e. log4j2.yaml
   f. log4j2.xml
   g. log4j2.properties
3. If not found: DefaultConfiguration (ERROR to console)
```

## Event Flow

```
Application calls logger.info("message")
    ↓
Logger.isEnabled(Level.INFO) check
    ↓ (if enabled)
LogEvent created with:
  - timestamp
  - thread name
  - level
  - logger name
  - message
  - MDC
  - thrown (if exception)
    ↓
LoggerConfig.processLogEvent(event)
    ↓
Apply filters (denied? → discard)
    ↓
Appender.append(event)
    ↓
Layout.toSerializable(event) → String
    ↓
Write to destination
```
