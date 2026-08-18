# LightStep References & Resources

## Official Documentation

### LightStep Core
- [LightStep Documentation](https://docs.lightstep.com/)
- [LightStep Home](https://lightstep.com/)
- [LightStep Blog](https://blog.lightstep.com/)
- [LightStep Status](https://status.lightstep.com/)

### OpenTelemetry
- [OpenTelemetry Specification](https://opentelemetry.io/docs/)
- [OpenTelemetry Java SDK](https://opentelemetry.io/docs/languages/java/)
- [OTLP Protocol](https://opentelemetry.io/docs/specs/otlp/)

---

## Getting Started Guides

### Quick Start
- [LightStep Quick Start](https://docs.lightstep.com/docs/quickstart)
- [Java Quick Start](https://docs.lightstep.com/docs/java-quickstart)
- [Node.js Quick Start](https://docs.lightstep.com/docs/nodejs-quickstart)
- [Python Quick Start](https://docs.lightstep.com/docs/python-quickstart)

### Integration Guides
- [Spring Boot Integration](https://docs.lightstep.com/docs/spring-boot)
- [Micronaut Integration](https://docs.lightstep.com/docs/micronaut)
- [Quarkus Integration](https://docs.lightstep.com/docs/quarkus)
- [Vert.x Integration](https://docs.lightstep.com/docs/vertx)

---

## Tutorials & Examples

### Official Tutorials
- [Distributed Tracing Tutorial](https://docs.lightstep.com/docs/tutorial-distributed-tracing)
- [Custom Spans Tutorial](https://docs.lightstep.com/docs/tutorial-custom-spans)
- [Metrics Tutorial](https://docs.lightstep.com/docs/tutorial-metrics)
- [Alerting Tutorial](https://docs.lightstep.com/docs/tutorial-alerting)

### Example Applications
- [Sample Spring Boot App](https://github.com/lightstep/opentelemetry-examples)
- [Microservices Demo](https://github.com/lightstep/opentelemetry-microservices-demo)
- [Java Agent Example](https://github.com/lightstep/lightstep-tracer-java)

---

## API References

### Java SDK
- [Java SDK API](https://www.javadoc.io/doc/com.lightstep.opentelemetry/)
- [OpenTelemetry Java API](https://javadoc.io/doc/io.opentelemetry/)
- [OTLP Exporter Java](https://javadoc.io/doc/io.opentelemetry.javaexporter/otlp/)

### REST API
- [LightStep API Reference](https://api.lightstep.com/)
- [Metrics API](https://docs.lightstep.com/docs/api-metrics)
- [Traces API](https://docs.lightstep.com/docs/api-traces)

---

## Configuration References

### Environment Variables
```bash
# Core
LIGHTSTEP_ACCESS_TOKEN=<token>
LIGHTSTEP_SERVICE_NAME=<name>
LIGHTSTEP_COLLECTOR_HOST=ingest.lightstep.com
LIGHTSTEP_COLLECTOR_PORT=443

# Tracing
LIGHTSTEP_TRACES_ENABLED=true
LIGHTSTEP_TRACE_SAMPLE_RATE=0.1

# Metrics
LIGHTSTEP_METRICS_ENABLED=true
LIGHTSTEP_METRICS_EXPORT_INTERVAL=60000
```

### System Properties
```properties
# Java System Properties
-Dlightstep.service.name=my-service
-Dlightstep.access.token=my-token
-Dlightstep.collector.host=ingest.lightstep.com
-Dlightstep.traces.enabled=true
```

---

## Community Resources

### GitHub Repositories
- [LightStep Java Tracer](https://github.com/lightstep/lightstep-tracer-java)
- [LightStep OTel Examples](https://github.com/lightstep/opentelemetry-examples)
- [LightStep Documentation](https://github.com/lightstep/documentation)

### Community Forums
- [LightStep Community](https://community.lightstep.com/)
- [Stack Overflow - LightStep](https://stackoverflow.com/questions/tagged/lightstep)
- [OpenTelemetry Slack](https://opentelemetry.io/community/)

---

## Learning Paths

### Beginner Path
1. [What is Distributed Tracing?](https://docs.lightstep.com/docs/what-is-distributed-tracing)
2. [LightStep Concepts](https://docs.lightstep.com/docs/concepts)
3. [First Trace Tutorial](https://docs.lightstep.com/docs/first-trace)
4. [Basic Alerting](https://docs.lightstep.com/docs/alerting-basics)

### Intermediate Path
1. [Advanced Sampling](https://docs.lightstep.com/docs/sampling)
2. [Custom Spans and Attributes](https://docs.lightstep.com/docs/custom-spans)
3. [Service Maps](https://docs.lightstep.com/docs/service-maps)
4. [Performance Optimization](https://docs.lightstep.com/docs/performance)

### Advanced Path
1. [OpenTelemetry Migration](https://docs.lightstep.com/docs/otel-migration)
2. [Custom Instrumentation](https://docs.lightstep.com/docs/custom-instrumentation)
3. [Enterprise Deployment](https://docs.lightstep.com/docs/enterprise)
4. [API Integration](https://docs.lightstep.com/docs/api-integration)

---

## Best Practices

### Instrumentation
- [Instrumentation Best Practices](https://docs.lightstep.com/docs/best-practices-instrumentation)
- [Span Naming Conventions](https://docs.lightstep.com/docs/span-naming)
- [Attribute Guidelines](https://docs.lightstep.com/docs/attribute-guidelines)

### Performance
- [Sampling Strategies](https://docs.lightstep.com/docs/sampling-strategies)
- [Batch Processing](https://docs.lightstep.com/docs/batch-processing)
- [Resource Usage](https://docs.lightstep.com/docs/resource-usage)

### Security
- [Token Management](https://docs.lightstep.com/docs/token-management)
- [Data Privacy](https://docs.lightstep.com/docs/data-privacy)
- [Access Control](https://docs.lightstep.com/docs/access-control)

---

## Video Resources

### Official Videos
- [LightStep Overview](https://www.youtube.com/watch?v=example1)
- [Getting Started with LightStep](https://www.youtube.com/watch?v=example2)
- [Advanced Tracing Techniques](https://www.youtube.com/watch?v=example3)

### Conference Talks
- [KubeCon - OpenTelemetry in Production](https://www.youtube.com/watch?v=example4)
- [QCon - Observability at Scale](https://www.youtube.com/watch?v=example5)
- [Monitorama - Distributed Tracing Patterns](https://www.youtube.com/watch?v=example6)

---

## Related Tools & Technologies

### Service Mesh
- [Istio Documentation](https://istio.io/latest/docs/)
- [Linkerd Documentation](https://linkerd.io/2/getting-started/)
- [Envoy Proxy](https://www.envoyproxy.io/docs/envoy/latest/)

### Observability Stack
- [Prometheus](https://prometheus.io/docs/)
- [Grafana](https://grafana.com/docs/)
- [Jaeger](https://www.jaegertracing.io/docs/)
- [Zipkin](https://zipkin.io/pages/quickstart.html)

### Cloud Providers
- [AWS X-Ray](https://docs.aws.amazon.com/xray/)
- [Google Cloud Trace](https://cloud.google.com/trace/docs)
- [Azure Monitor](https://docs.microsoft.com/en-us/azure/azure-monitor/)

---

## Support & Contact

### Official Support
- [LightStep Support](https://support.lightstep.com/)
- [Documentation Issues](https://github.com/lightstep/documentation/issues)
- [Feature Requests](https://feedback.lightstep.com/)

### Enterprise Support
- Contact your LightStep representative
- Enterprise support portal
- Dedicated success manager

---

## Changelog & Releases

### Java SDK Changelog
- [Release Notes](https://github.com/lightstep/lightstep-tracer-java/releases)
- [Migration Guides](https://docs.lightstep.com/docs/migration-guides)

### Platform Updates
- [LightStep Changelog](https://docs.lightstep.com/changelog)
- [API Changes](https://api.lightstep.com/changelog)

---

## Additional Resources

### Books & Publications
- "Distributed Tracing in Practice" - O'Reilly
- "Observability Engineering" - O'Reilly
- "Microservices Patterns" - Manning

### Blogs & Articles
- [LightStep Engineering Blog](https://blog.lightstep.com/)
- [OpenTelemetry Blog](https://opentelemetry.io/blog/)
- [DZone Observability Zone](https://dzone.com/zone/observability)

### Tools
- [LightStep CLI](https://docs.lightstep.com/docs/cli)
- [OpenTelemetry Collector](https://opentelemetry.io/docs/collector/)
- [Jaeger Client Libraries](https://www.jaegertracing.io/docs/client-libraries/)
