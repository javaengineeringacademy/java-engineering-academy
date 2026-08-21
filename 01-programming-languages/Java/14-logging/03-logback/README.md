# Logback: SLF4J Native Implementation

## Overview

Logback is a **native SLF4J implementation** that provides fast, reliable, and flexible logging. It's the successor to Log4j and is the default logging framework in Spring Boot.

## Architecture

Logback consists of three modules:

- **logback-core** - Foundation for other modules
- **logback-classic** - SLF4J native implementation
- **logback-access** - HTTP access logging integration

## Configuration

### logback.xml Structure

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- Properties -->
    <property name="LOG_DIR" value="/var/log/myapp" />
    
    <!-- Appenders -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_DIR}/app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_DIR}/app.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- Loggers -->
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </root>
    
    <logger name="com.myapp.dao" level="DEBUG" />
</configuration>
```

## Appenders

### ConsoleAppender

```xml
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
</appender>
```

### RollingFileAppender

```xml
<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>app.log</file>
    
    <!-- Time-based rolling -->
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <fileNamePattern>app.%d{yyyy-MM-dd}.log</fileNamePattern>
        <maxHistory>30</maxHistory>
        <totalSizeCap>10GB</totalSizeCap>
    </rollingPolicy>
    
    <!-- Size and time-based rolling -->
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
        <fileNamePattern>app.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <maxFileSize>100MB</maxFileSize>
        <maxHistory>30</maxHistory>
        <totalSizeCap>10GB</totalSizeCap>
    </rollingPolicy>
    
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
</appender>
```

### AsyncAppender

```xml
<appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
    <queueSize>1024</queueSize>
    <discardingThreshold>0</discardingThreshold>  <!-- Don't discard any events -->
    <neverBlock>false</neverBlock>
    <appender-ref ref="FILE" />
</appender>
```

## Encoder Patterns

| Pattern | Description | Example |
|---------|-------------|---------|
| `%d{format}` | Date/time | `2024-01-15 10:30:45.123` |
| `%thread` | Thread name | `main`, `http-nio-8080-exec-1` |
| `%-5level` | Log level (left-padded) | `INFO `, `ERROR` |
| `%logger{36}` | Logger name (abbreviated) | `c.m.a.s.UserService` |
| `%msg` | Log message | `User logged in` |
| `%n` | Newline | Platform line separator |
| `%X{key}` | MDC value | Value from `MDC.get("key")` |
| `%ex` | Exception | Stack trace |
| `%replace{regex}{replacement}` | Pattern replacement | Custom formatting |

## Spring Boot Integration

```yaml
# application.yml
logging:
  level:
    root: INFO
    com.myapp: DEBUG
    org.springframework: WARN
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: /var/log/myapp/app.log
  logback:
    rollingpolicy:
      max-file-size: 100MB
      max-history: 30
      total-size-cap: 10GB
```

## Filter Support

```xml
<!-- Level range filter -->
<filter class="ch.qos.logback.classic.filter.LevelRangeFilter">
    <levelMin>WARN</levelMin>
    <levelMax>ERROR</levelMax>
    <onMatch>ACCEPT</onMatch>
    <onMismatch>DENY</onMismatch>
</filter>

<!-- Evaluator filter -->
<filter class="ch.qos.logback.classic.filter.EvaluatorFilter">
    <evaluator class="ch.qos.logback.classic.boolex.GEventEvaluator">
        <expression>
            logger.equals("com.myapp.service") &amp;&amp; level.equals(DEBUG)
        </expression>
    </evaluator>
    <onMatch>ACCEPT</onMatch>
    <onMismatch>NEUTRAL</onMismatch>
</filter>
```
