# Internals: Logback Architecture

## Initialization Sequence

```
1. LoggerFactory.getLogger() called
2. Logback checks for static initialization
3. LoggerFactoryBinder binds SLF4J to Logback
4. LoggerContext created (default or from configuration)
5. Configuration file searched:
   a. logback-test.xml (test classpath)
   b. logback.xml (main classpath)
   c. logback-spring.xml (Spring Boot)
   d. logback.groovy (if groovy on classpath)
6. If not found: BasicConfigurator → INFO to console
7. Configuration applied: appenders, loggers, filters
```

## LoggerContext

```java
// Central manager for all loggers
public class LoggerContext extends ContextBase implements ILoggerFactory {
    // Root logger
    private Logger root;
    
    // Cache of loggers by name
    private ConcurrentMap<String, Logger> loggerCache;
    
    // Configuration status
    private StatusManager sm;
}
```

## Logger Hierarchy

```
root (Level: INFO)
├── com (inherits root)
│   ├── myapp (inherits com)
│   │   ├── service (Level: DEBUG)
│   │   │   ├── UserService (inherits service → DEBUG)
│   │   │   └── OrderService (inherits service → DEBUG)
│   │   └── dao (inherits myapp → INFO)
│   │       └── UserRepository (inherits dao → INFO)
```

**Level assignment:**
1. Explicitly set level on logger → use it
2. Walk up hierarchy until level found
3. If no level found → root level

## AsyncAppender Internals

```
Caller Thread                    AsyncAppender Worker
     │                                 │
     ├── put(event) ──────────────────►│
     │   (ArrayBlockingQueue)          │
     │   [e1, e2, e3, ...]            │
     │                                 │
     │   (continues)                   ├── take(event)
     │                                 ├── write to underlying appender
     │                                 └── repeat
```

**Key properties:**
- `queueSize`: Default 256 entries
- `discardingThreshold`: Events dropped when queue has fewer than this many slots
- `neverBlock`: If true, never block caller (drop events instead)
- `maxFlushTime`: Max time to wait for queue drain on shutdown

## RollingPolicy Mechanics

```
TimeBasedRollingPolicy:
  - Checks rollover period (day, hour, etc.)
  - On rollover: renames current file, creates new file
  - Uses FileNamePattern for naming: app.%d{yyyy-MM-dd}.log

SizeAndTimeBasedRollingPolicy:
  - Combines time + size triggers
  - Uses %i for file index within period
  - File naming: app.2024-01-15.0.log, app.2024-01-15.1.log
```

## Filter Chain

```
Event → Filter 1 → Filter 2 → Filter 3 → Appender
         │          │          │
         ▼          ▼          ▼
      ACCEPT    NEUTRAL     DENY
      (write)   (continue)  (drop)
```

**Filter outcomes:**
- `ACCEPT` - Event is written, skip remaining filters
- `NEUTRAL` - Pass to next filter
- `DENY` - Event is dropped immediately
