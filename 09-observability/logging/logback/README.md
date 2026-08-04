# Logback Configuration

## Overview

Logback is the native implementation of SLF4J, offering advanced features like configuration via XML or Groovy, automatic reloading, conditional configuration, and high performance.

## Configuration Files

Logback searches for configuration in this order:
1. `logback-test.xml`
2. `logback.xml`
3. `logback-spring.xml` (Spring Boot)

## Basic Configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
    </root>
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

### FileAppender
```xml
<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>logs/application.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <fileNamePattern>logs/application.%d{yyyy-MM-dd}.log</fileNamePattern>
        <maxHistory>30</maxHistory>
        <totalSizeCap>1GB</totalSizeCap>
    </rollingPolicy>
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
</appender>
```

### RollingFileAppender with Size and Time
```xml
<appender name="ROLLING" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>logs/application.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
        <fileNamePattern>logs/application.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <maxFileSize>10MB</maxFileSize>
        <maxHistory>30</maxHistory>
        <totalSizeCap>1GB</totalSizeCap>
    </rollingPolicy>
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
</appender>
```

### AsyncAppender
```xml
<appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
    <queueSize>512</queueSize>
    <discardingThreshold>0</discardingThreshold>
    <neverBlock>true</neverBlock>
    <appender-ref ref="ROLLING" />
</appender>
```

## Encoders

### PatternLayoutEncoder
```xml
<encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
    <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    <charset>UTF-8</charset>
</encoder>
```

### LogstashEncoder (JSON)
```xml
<encoder class="net.logstash.logback.encoder.LogstashEncoder">
    <includeMdcKeyName>requestId</includeMdcKeyName>
    <includeMdcKeyName>userId</includeMdcKeyName>
    <fieldNames>
        <timestamp>[ignore]</timestamp>
        <message>message</message>
        <logger>logger</logger>
        <thread>thread</thread>
        <level>level</level>
        <stackTrace>stack_trace</stackTrace>
    </fieldNames>
</encoder>
```

## Filters

### LevelFilter
```xml
<filter class="ch.qos.logback.classic.filter.LevelFilter">
    <level>ERROR</level>
    <onMatch>ACCEPT</onMatch>
    <onMismatch>DENY</onMismatch>
</filter>
```

### ThresholdFilter
```xml
<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
    <level>WARN</level>
</filter>
```

### EvaluatorFilter with Janino
```xml
<filter class="ch.qos.logback.classic.filter.EvaluatorFilter">
    <evaluator class="ch.qos.logback.classic.boolex.GEventEvaluator">
        <expression>
            return level.equals(Level.ERROR) &amp;&amp; 
                   logger.equals("com.example.CriticalService");
        </expression>
    </evaluator>
    <onMatch>ACCEPT</onMatch>
    <onMismatch>DENY</onMismatch>
</filter>
```

## Logger Configuration

### Specific Logger
```xml
<logger name="com.example" level="DEBUG" additivity="false">
    <appender-ref ref="CONSOLE" />
</logger>
```

### Spring Profiles
```xml
<springProfile name="development">
    <root level="DEBUG">
        <appender-ref ref="CONSOLE" />
    </root>
</springProfile>

<springProfile name="production">
    <root level="INFO">
        <appender-ref ref="ASYNC" />
    </root>
</springProfile>
```

## Variable Substitution

```xml
<configuration>
    <property name="LOG_DIR" value="logs" />
    <property name="APP_NAME" value="myapp" />
    
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_DIR}/${APP_NAME}.log</file>
        ...
    </appender>
</configuration>
```

## Conditional Configuration

```xml
<configuration>
    <if condition='property("ENV").equals("production")'>
        <then>
            <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
                ...
            </appender>
        </then>
        <else>
            <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
                ...
            </appender>
        </else>
    </if>
</configuration>
```

## Best Practices

1. **Use AsyncAppender in production** - Non-blocking logging
2. **Configure appropriate rolling policies** - Prevent disk exhaustion
3. **Use JSON encoders for centralized logging** - Machine-readable logs
4. **Set appropriate log levels per package** - Balance detail vs noise
5. **Use MDC for request context** - Enable log correlation
6. **Test configuration with logback-test.xml** - Separate test config
7. **Monitor log file sizes** - Set totalSizeCap limits
8. **Use conditional configuration** - Environment-specific settings

## Common Patterns

### Request Logging
```xml
<appender name="REQUEST" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>logs/request.log</file>
    <encoder class="net.logstash.logback.encoder.LogstashEncoder">
        <customFields>{"service":"${APP_NAME}"}</customFields>
    </encoder>
</appender>

<logger name="REQUEST" level="INFO" additivity="false">
    <appender-ref ref="REQUEST" />
</logger>
```

### Audit Logging
```xml
<appender name="AUDIT" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>logs/audit.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <fileNamePattern>logs/audit.%d{yyyy-MM-dd}.log</fileNamePattern>
        <maxHistory>365</maxHistory>
    </rollingPolicy>
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS}|%msg%n</pattern>
    </encoder>
</appender>
```

## References

- [Logback Manual](https://logback.qos.ch/manual/)
- [Logback Configuration](https://logback.qos.ch/manual/configuration.html)
- [Logstash Encoder](https://github.com/logstash/logstash-logback-encoder)
