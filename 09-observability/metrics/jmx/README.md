# JMX Monitoring

## Overview

Java Management Extensions (JMX) provides runtime monitoring and management of Java applications through MBeans (Managed Beans).

## MBean Types

### Standard MBeans
```java
public interface MemoryMonitorMBean {
    long getUsedMemory();
    long getFreeMemory();
    double getMemoryUsagePercentage();
}

public class MemoryMonitor implements MemoryMonitorMBean {
    @Override
    public long getUsedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
    
    @Override
    public long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }
    
    @Override
    public double getMemoryUsagePercentage() {
        return (double) getUsedMemory() / Runtime.getRuntime().totalMemory() * 100;
    }
}
```

### Dynamic MBeans
```java
public class DynamicServiceMonitor extends StandardMBean 
    implements DynamicMBean {
    
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();
    
    public DynamicServiceMonitor() {
        super(MonitorMBean.class);
    }
    
    @Override
    public Object getAttribute(String attribute) {
        return attributes.get(attribute);
    }
    
    @Override
    public void setAttribute(Attribute attribute) {
        attributes.put(attribute.getName(), attribute.getValue());
    }
}
```

### Composite Data
```java
public class ServiceMetrics {
    @TabularDataSupport
    public CompositeData getRequestMetrics() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("requestCount", requestCount);
        values.put("averageResponseTime", avgResponseTime);
        values.put("errorRate", errorRate);
        
        return new CompositeDataSupport(
            REQUEST_METRICS_TYPE, 
            values.toArray(new String[0]), 
            values.values().toArray()
        );
    }
}
```

## JMX Exporter (Prometheus)

### Configuration
```yaml
hostPort: localhost:9090
lowercaseOutputName: true
lowercaseOutputLabelNames: true

rules:
  - pattern: java.lang<type=Memory><HeapMemoryUsage>(\w+)
    name: jvm_memory_heap_$1
    help: JVM heap memory usage
    type: GAUGE
    
  - pattern: java.lang<type=Threading><>(ThreadCount|DaemonThreadCount)
    name: jvm_threads_$1
    help: JVM thread information
    type: GAUGE
```

### Spring Boot Actuator
```yaml
management:
  endpoints:
    web:
      exposure:
        include: jmx,prometheus,health,info
  jmx:
    exposure:
      include: "*"
```

## Runtime Bean Registration

```java
@Component
public class JmxExporter {
    @Autowired
    public void registerMBeans(MBeanServer server) throws Exception {
        ObjectName name = new ObjectName("com.example:type=ServiceMonitor");
        ServiceMonitor monitor = new ServiceMonitor();
        server.registerMBean(monitor, name);
    }
}
```

## Best Practices

1. Use JMX exporter for Prometheus integration
2. Expose only necessary MBeans
3. Use authentication for remote JMX access
4. Monitor JVM metrics (heap, threads, GC)
5. Create custom MBeans for business metrics
6. Use JConsole/VisualVM for debugging
7. Avoid JMX in containerized environments
8. Use actuator endpoints instead
