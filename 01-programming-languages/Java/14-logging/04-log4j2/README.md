# Log4j 2: High-Performance Logging

## Overview

Log4j 2 is a **complete rewrite** of the original Log4j framework, designed for high performance, reliability, and flexibility. It provides the best async logging performance in the Java ecosystem through the LMAX Disruptor library.

## Architecture

```
Log4j 2 API (log4j-api)
    ↓
Log4j 2 Core (log4j-core)
    ├── Appenders (Console, File, Async, Socket, etc.)
    ├── Layouts (Pattern, JSON, XML, CSV, etc.)
    ├── Filters (Burst, Regex, Script, etc.)
    └── Plugins (Extension mechanism)
```

## Core Components

### Logger Context

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Log4j 2 way
Logger logger = LogManager.getLogger(MyClass.class);

// Or with string name
Logger logger = LogManager.getLogger("myapp.module");
```

### Log Levels (Extended)

| Level | Int Value | Purpose |
|-------|-----------|---------|
| `OFF` | 0 | Disables all logging |
| `FATAL` | 100 | Critical system failure |
| `ERROR` | 200 | Operation failed |
| `WARN` | 300 | Potential problem |
| `INFO` | 400 | Normal operations |
| `DEBUG` | 500 | Development info |
| `TRACE` | 600 | Detailed diagnostic |
| `ALL` | Integer.MAX_VALUE | Enables all logging |

### Custom Levels

```xml
<CustomLevel name="AUDIT" intLevel="350" />
<CustomLevel name="METRICS" intLevel="450" />
```

## Configuration

### XML Configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Properties>
        <Property name="LOG_DIR">/var/log/myapp</Property>
    </Properties>
    
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
        
        <RollingFile name="RollingFile"
                     fileName="${LOG_DIR}/app.log"
                     filePattern="${LOG_DIR}/app.%d{yyyy-MM-dd}.%i.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="100MB"/>
                <TimeBasedTriggeringPolicy interval="1"/>
            </Policies>
            <DefaultRolloverStrategy max="30"/>
        </RollingFile>
        
        <Async name="Async">
            <AppenderRef ref="RollingFile"/>
        </Async>
    </Appenders>
    
    <Loggers>
        <Root level="INFO">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="Async"/>
        </Root>
        <Logger name="com.myapp.dao" level="DEBUG"/>
    </Loggers>
</Configuration>
```

### JSON Configuration

```json
{
  "Configuration": {
    "status": "WARN",
    "Appenders": {
      "Console": {
        "name": "Console",
        "target": "SYSTEM_OUT",
        "PatternLayout": {
          "pattern": "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"
        }
      }
    },
    "Loggers": {
      "Root": {
        "level": "INFO",
        "AppenderRef": [{"ref": "Console"}]
      }
    }
  }
}
```

## High-Performance Features

### Garbage-Free Logging

```xml
<!-- Enable garbage-free mode -->
<Configuration status="WARN" strict="true">
    <Appenders>
        <Console name="Console">
            <JsonLayout complete="false" compact="true">
                <!-- Minimal object creation -->
            </JsonLayout>
        </Console>
    </Appenders>
</Configuration>
```

### Async Logging with Disruptor

```xml
<!-- Method 1: Logger-level async -->
<Loggers>
    <AsyncLogger name="com.myapp" level="INFO"/>
</Loggers>

<!-- Method 2: Appender-level async -->
<Appenders>
    <Async name="Async">
        <AppenderRef ref="RollingFile"/>
    </Async>
</Appenders>
```

**Disruptor vs ArrayBlockingQueue:**

| Aspect | Disruptor | ArrayBlockingQueue |
|--------|-----------|-------------------|
| Lock-free | Yes | No (uses locks) |
| Throughput | Higher | Lower |
| Latency | Lower | Higher |
| Memory | Ring buffer | Array + nodes |
| GC pressure | Minimal | More objects |

## Layouts

### Pattern Layout

```xml
<PatternLayout pattern="%d{ISO8601} [%t] %-5p %c{1} - %msg%n"/>
```

| Pattern | Description |
|---------|-------------|
| `%d` | Date/time |
| `%t` | Thread name |
| `%p` | Log level |
| `%c{1}` | Logger name (abbreviation) |
| `%msg` | Message |
| `%n` | Newline |
| `%X{key}` | MDC value |
| `%ex` | Exception |
| `%r` | Milliseconds since JVM start |
| `%C` | Class name |
| `%F` | File name |
| `%l` | Location (caller info) |

### JSON Layout

```xml
<JsonLayout compact="true" eventEol="true">
    <KeyValuePair key="service" value="myapp"/>
    <KeyValuePair key="environment" value="production"/>
</JsonLayout>
```

## Filters

### Level Range

```xml
<Filters>
    <ThresholdFilter level="INFO" onMatch="ACCEPT" onMismatch="NEUTRAL"/>
    <ThresholdFilter level="ERROR" onMatch="ACCEPT" onMismatch="DENY"/>
</Filters>
```

### Regex Filter

```xml
<RegexFilter regex=".*sensitive.*" onMatch="DENY" onMismatch="NEUTRAL"/>
```

### Burst Filter (Rate Limiting)

```xml
<BurstFilter level="WARN" rate="10" maxBurst="50" timeout="1000"/>
```
