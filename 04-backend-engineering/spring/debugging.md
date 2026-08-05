# Spring Debugging

## Logging Configuration

### Log Levels

```properties
# application.properties
logging.level.root=INFO
logging.level.com.example=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Log Patterns

```properties
# Console pattern
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# File pattern
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# JSON pattern
logging.pattern.json={"timestamp":"%d{yyyy-MM-dd HH:mm:ss}","level":"%level","logger":"%logger","thread":"%thread","message":"%msg"}
```

### Custom Logger

```java
@Component
public class CustomLogger {
    
    private static final Logger log = LoggerFactory.getLogger(CustomLogger.class);
    
    public void logMethodEntry(String methodName) {
        log.debug("Entering method: {}", methodName);
    }
    
    public void logMethodExit(String methodName, Object result) {
        log.debug("Exiting method: {} with result: {}", methodName, result);
    }
    
    public void logException(String methodName, Exception ex) {
        log.error("Exception in {}: {}", methodName, ex.getMessage(), ex);
    }
}
```

## Conditional Breakpoints

### In IntelliJ IDEA

1. Set breakpoint on line
2. Right-click breakpoint
3. Add condition
4. Enter expression

```java
// Example conditions
user.getId() == 123
order.getStatus().equals("PENDING")
request.getParameter("id") != null
```

### In Eclipse

1. Set breakpoint
2. Right-click > Breakpoint Properties
3. Enable "Conditional"
4. Enter expression

## Spring DevTools

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

### Configuration

```properties
# application.properties
spring.devtools.restart.enabled=true
spring.devtools.restart.additional-paths=src/main/java
spring.devtools.restart.exclude=static/**,public/**

# Live reload
spring.devtools.livereload.enabled=true
```

### How It Works

- Automatic restart on code changes
- Live reload in browser
- Remote development support
- Enhanced logging

## Remote Debugging

### JVM Debug Arguments

```bash
# Start with remote debugging
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
     -jar app.jar

# With authentication
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
     -jar app.jar
```

### Connect from IDE

1. Run application with debug arguments
2. Create Remote JVM Debug configuration
3. Set host and port (5005)
4. Click Debug

## Thread Dump Analysis

### Generate Thread Dump

```bash
# Using jstack
jstack <pid> > thread_dump.txt

# Using kill signal
kill -3 <pid>

# Using Spring Actuator
curl http://localhost:8080/actuator/threaddump
```

### Analyze Thread Dump

```bash
# Look for:
# - BLOCKED threads
# - WAITING threads
# - Deadlocks
# - High CPU usage threads
```

## Memory Analysis

### Heap Dump

```bash
# Generate heap dump
jmap -dump:live,format=b,file=heap.hprof <pid>

# Or trigger via JMX
jcmd <pid> GC.heap_dump /path/to/heap.hprof

# Analyze with MAT or VisualVM
```

### Memory Settings

```bash
# Enable GC logging
java -Xlog:gc*:file=gc.log:time,uptime:filecount=10,filesize=10m \
     -jar app.jar

# Heap dump on OOM
java -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/path/to/dumps \
     -jar app.jar
```

## SQL Debugging

### Enable SQL Logging

```properties
# Show SQL
spring.jpa.show-sql=true

# Format SQL
spring.jpa.properties.hibernate.format_sql=true

# Log bind parameters
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Hibernate Statistics

```properties
# Enable statistics
spring.jpa.properties.hibernate.generate_statistics=true

# Log statistics
logging.level.org.hibernate.stat=DEBUG
```

## HTTP Debugging

### Enable Request Logging

```properties
# Log all requests
logging.level.org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping=DEBUG

# Log request details
logging.level.org.springframework.web.servlet.DispatcherServlet=DEBUG
```

### Custom Interceptor

```java
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
    
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("Request: {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.debug("Response: {} - {}", response.getStatus(), ex != null ? ex.getMessage() : "OK");
    }
}
```

## Performance Profiling

### Method Timing

```java
@Aspect
@Component
public class PerformanceAspect {
    
    private static final Logger log = LoggerFactory.getLogger(PerformanceAspect.class);
    
    @Around("execution(* com.example.service.*.*(..))")
    public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            
            if (duration > 1000) {
                log.warn("Slow method: {} took {}ms", methodName, duration);
            } else {
                log.debug("Method: {} took {}ms", methodName, duration);
            }
            
            return result;
        } catch (Throwable t) {
            log.error("Method {} failed: {}", methodName, t.getMessage());
            throw t;
        }
    }
}
```

## Common Debugging Scenarios

### 1. Application Won't Start

```bash
# Check logs for:
# - Bean creation failures
# - Property binding issues
# - Missing dependencies

# Enable debug logging
java -Dlogging.level.org.springframework=DEBUG -jar app.jar
```

### 2. 404 Not Found

```java
// Check:
// - Request mapping paths
// - Component scanning
// - DispatcherServlet configuration

// Debug mappings
curl http://localhost:8080/actuator/mappings
```

### 3. Database Connection Issues

```properties
# Check:
# - DataSource configuration
# - Connection pool settings
# - Database availability

logging.level.com.zaxxer.hikari=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### 4. Authentication Failures

```properties
# Check:
# - Security configuration
# - User details service
# - Password encoding

logging.level.org.springframework.security=DEBUG
```

## Debugging Checklist

1. Enable appropriate logging levels
2. Use conditional breakpoints
3. Enable Spring DevTools
4. Configure remote debugging
5. Analyze thread dumps
6. Monitor memory usage
7. Log SQL queries
8. Trace HTTP requests
9. Profile slow methods
10. Check configuration properties
