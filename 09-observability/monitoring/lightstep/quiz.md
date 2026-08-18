# LightStep Quiz

## Test Your Knowledge

### Question 1
What is LightStep's primary function?
- A) Log aggregation
- B) Distributed tracing and observability
- C) Configuration management
- D) Container orchestration

**Answer: B** - LightStep is a distributed tracing and observability platform that provides real-time insights into microservices architecture.

---

### Question 2
Which protocol does LightStep use for telemetry ingestion?
- A) HTTP/1.1
- B) WebSocket
- C) OTLP (OpenTelemetry Protocol)
- D) gRPC only

**Answer: C** - LightStep uses OTLP (OpenTelemetry Protocol) for ingesting traces and metrics, supporting both gRPC and HTTP transports.

---

### Question 3
What is the default port for LightStep collector?
- A) 8080
- B) 9411
- C) 443
- D) 55680

**Answer: C** - The default port for LightStep collector is 443 (HTTPS), as it's a managed cloud service.

---

### Question 4
Which sampling strategy provides consistent sampling across service boundaries?
- A) Probability sampling
- B) Rate limiting
- C) Parent-based sampling
- D) Adaptive sampling

**Answer: C** - Parent-based sampling ensures consistent sampling decisions across service boundaries by using the parent's sampling decision.

---

### Question 5
What is the purpose of a span in distributed tracing?
- A) To store configuration
- B) To represent a single operation within a trace
- C) To manage authentication
- D) To handle load balancing

**Answer: B** - A span represents a single operation within a trace, containing timing, metadata, and status information.

---

### Question 6
Which propagation format is NOT supported by LightStep?
- A) W3C TraceContext
- B) B3 (Zipkin)
- C) Jaeger
- D) LightStep Propagation

**Answer: C** - LightStep supports W3C TraceContext, B3, and its own propagation format, but not Jaeger propagation format.

---

### Question 7
What is the main advantage of OpenTelemetry integration?
- A) Better performance
- B) Vendor-neutral instrumentation
- C) Lower cost
- D) Simpler setup

**Answer: B** - OpenTelemetry provides vendor-neutral instrumentation, allowing you to switch between observability backends without changing instrumentation code.

---

### Question 8
Which metric type is best for tracking request duration?
- A) Counter
- B) Gauge
- C) Histogram
- D) Summary

**Answer: C** - Histogram is best for tracking request duration as it provides percentile distributions and can be aggregated across instances.

---

### Question 9
What is the purpose of baggage in distributed tracing?
- A) Error handling
- B) Passing context across service boundaries
- C) Load balancing
- D) Authentication

**Answer: B** - Baggage allows passing key-value pairs across service boundaries, useful for propagating context like tenant IDs or user preferences.

---

### Question 10
Which LightStep feature provides visual service dependencies?
- A) Trace Explorer
- B) Service Maps
- C) Alert Rules
- D) Dashboard Builder

**Answer: B** - Service Maps automatically discover and visualize service dependencies based on trace data.

---

## Score Interpretation

| Score | Level |
|-------|-------|
| 10/10 | LightStep Expert |
| 8-9/10 | Advanced User |
| 6-7/10 | Intermediate User |
| 4-5/10 | Beginner |
| 0-3/10 | Needs Review |

---

## Additional Practice Questions

### Conceptual Questions

**Q: What is the difference between a trace and a span?**
A: A trace represents the complete journey of a request through the system, while a span represents a single operation within that trace.

**Q: Why is context propagation important?**
A: Context propagation ensures that trace information is correctly passed between services, maintaining the trace's integrity across distributed systems.

**Q: What is the benefit of using OpenTelemetry?**
A: OpenTelemetry provides vendor-neutral instrumentation, standard APIs, and community-driven development, avoiding vendor lock-in.

### Technical Questions

**Q: How does LightStep handle high-volume tracing?**
A: LightStep uses a scalable collector architecture with batch processing and adaptive sampling to handle high-volume tracing efficiently.

**Q: What is the difference between client-side and server-side sampling?**
A: Client-side sampling reduces data before sending to the collector, while server-side sampling can make decisions based on the complete trace context.

---

## Study Topics

- [ ] LightStep Architecture
- [ ] OpenTelemetry Integration
- [ ] Sampling Strategies
- [ ] Context Propagation
- [ ] Java SDK Integration
- [ ] Alerting Configuration
- [ ] Dashboard Creation
- [ ] Performance Monitoring
- [ ] Service Mesh Integration
- [ ] Best Practices
