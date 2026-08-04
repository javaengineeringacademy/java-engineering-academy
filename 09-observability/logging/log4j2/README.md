# Log4j2 Configuration

## Overview

Apache Log4j 2 is a high-performance logging framework offering asynchronous logging, garbage-free logging, and flexible configuration formats (XML, JSON, YAML, properties).

## Core Concepts

### Architecture
- **Logger** - Component that captures log events
- **Appender** - Outputs log events to destinations
- **Layout/Encoder** - Formats log events
- **Filter** - Controls which events pass through

### Log Levels (Most to Least Severe)
FATAL, ERROR, WARN, INFO, DEBUG, TRACE

## Configuration

### XML Configuration
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Properties>
        <Property name="LOG_DIR">logs</Property>
        <Property name="APP_NAME">myapp</Property>
    </Properties>
    
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
        
        <RollingFile name="File" 
                     fileName="${LOG_DIR}/${APP_NAME}.log"
                     filePattern="${LOG_DIR}/${APP_NAME}-%d{yyyy-MM-dd}-%i.log.gz">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="10MB"/>
                <TimeBasedTriggeringPolicy interval="1"/>
            </Policies>
            <DefaultRolloverStrategy max="30"/>
        </RollingFile>
    </Appenders>
    
    <Loggers>
        <Logger name="com.example" level="DEBUG" additivity="false">
            <AppenderRef ref="File"/>
        </Logger>
        <Root level="INFO">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="File"/>
        </Root>
    </Loggers>
</Configuration>
```

### YAML Configuration
```yaml
Configuration:
  status: warn
  properties:
    property:
      name: LOG_DIR
      value: logs
  Appenders:
    Console:
      name: Console
      target: SYSTEM_OUT
      PatternLayout:
        pattern: "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"
    RollingFile:
      name: File
      fileName: ${LOG_DIR}/app.log
      filePattern: ${LOG_DIR}/app-%d{yyyy-MM-dd}-%i.log.gz
      PatternLayout:
        pattern: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"
      Policies:
        SizeBasedTriggeringPolicy:
          size: 10MB
        TimeBasedTriggeringPolicy:
          interval: 1
  Loggers:
    Logger:
      - name: com.example
        level: debug
        additivity: false
        AppenderRef:
          ref: File
    Root:
      level: info
      AppenderRef:
        - ref: Console
        - ref: File
```

## Async Logging

### AsyncLogger
```xml
<Loggers>
    <!-- All loggers async -->
    <AsyncLogger name="com.example" level="INFO"/>
    
    <!-- Selective async -->
    <AsyncLogger name="com.example.service" level="DEBUG"/>
</Loggers>
```

### Configuration for Maximum Performance
```bash
-Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector
```

### AsyncAppender
```xml
<Appenders>
    <Async name="Async" bufferSize="1024">
        <AppenderRef ref="RollingFile"/>
    </Async>
</Appenders>
```

## Garbage-Free Logging

```xml
<Configuration status="WARN" monitorInterval="30">
    <Properties>
        <Property name="LOG_PATTERN">%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n</Property>
    </Properties>
    
    <Appenders>
        <Console name="Console">
            <PatternLayout pattern="${LOG_PATTERN}" disableAnsi="true"/>
        </Console>
    </Appenders>
</Configuration>
```

## Filters

### ThresholdFilter
```xml
<Filters>
    <ThresholdFilter level="WARN" onMatch="ACCEPT" onMismatch="DENY"/>
</Filters>
```

### RegexFilter
```xml
<RegexFilter regex=".*password.*" onMatch="DENY" onMismatch="NEUTRAL"/>
```

### ScriptFilter (JavaScript)
```xml
<ScriptFilter condition="js:logEvent.getLoggerName().equals('com.example')">
    <Script name="logFilter" language="javascript">
        <![CDATA[
            logEvent.getLevel().equals(org.apache.logging.log4j.Level.ERROR)
        ]]>
    </Script>
</ScriptFilter>
```

## Lookups

```xml
<!-- Environment variable -->
<PatternLayout pattern="%d{HH:mm:ss} [%t] %X{env:USER} - %msg%n"/>

<!-- System property -->
<PatternLayout pattern="%d{HH:mm:ss} [%t] %sys:app.name - %msg%n"/>

<!-- Date -->
<PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} - %msg%n"/>

<!-- Java version -->
<PatternLayout pattern="%java - %msg%n"/>
```

## Best Practices

### 1. Use Async Logging in Production
```xml
<!-- Enable globally via system property -->
-Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector
```

### 2. Configure Proper Rollover
```xml
<RollingFile name="RollingFile"
             fileName="logs/app.log"
             filePattern="logs/app-%d{yyyy-MM-dd}-%i.log.gz">
    <Policies>
        <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
        <SizeBasedTriggeringPolicy size="50MB"/>
    </Policies>
    <DefaultRolloverStrategy max="30"/>
</RollingFile>
```

### 3. Use Log4j2-Specific Features
```java
// Fluent API (Log4j 2.x)
logger.atInfo()
      .withMarker(MARKER)
      .withArgumentException("orderId", orderId)
      .log("Processing order");

// Lazy evaluation
logger.atDebug()
      .log("User details: {}", () -> expensiveJsonSerialization(user));
```

### 4. Monitor Log4j2 Internals
```xml
<!-- Status logging for debugging configuration -->
<Configuration status="WARN" monitorInterval="30">
```

## References

- [Log4j 2 Manual](https://logging.apache.org/log4j/2.x/manual/)
- [Log4j2 Configuration](https://logging.apache.org/log4j/2.x/manual/configuration.html)
- [Log4j2 Async Logging](https://logging.apache.org/log4j/2.x/manual/async.html)
