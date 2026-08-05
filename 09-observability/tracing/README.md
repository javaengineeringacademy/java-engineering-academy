# Distributed Tracing Tools

## Overview

Distributed tracing tools track requests as they flow through complex systems, providing visibility into service interactions, latency, and errors across service boundaries.

## Tool Categories

### Tracing Frameworks
Libraries and SDKs for instrumenting applications to produce trace data.

- **OpenTelemetry** - Vendor-neutral observability framework
- **Jaeger** - End-to-end distributed tracing system
- **Zipkin** - Distributed tracing data collection and visualization
- **Tempo** - High-scale distributed tracing backend

## Key Concepts

### Traces and Spans
A trace represents the complete journey of a request through the system. Each unit of work within a trace is called a span.

```
Trace: [---------- Request Lifecycle ----------]
  Span A: [-------- API Gateway --------]
    Span B: [---- Auth Service ----]
    Span C: [---------- Order Service ----------]
      Span D: [---- DB Query ----]
      Span E: [---- Payment Call ----]
```

### Span Attributes
- **Operation Name** - What operation the span represents
- **Duration** - Time taken by the operation
- **Status** - Success or failure indicator
- **Tags** - Key-value pairs for filtering and analysis
- **Logs** - Timestamped events within a span

### Context Propagation
Passing trace context across service boundaries using headers.

| Standard | Headers |
|----------|---------|
| W3C Trace Context | traceparent, tracestate |
| B3 | X-B3-TraceId, X-B3-SpanId |
| Jaeger | uber-trace-id |

## Sampling Strategies

### Head-Based Sampling
Decision made at trace creation. Simple but may miss important traces.

### Tail-Based Sampling
Decision made after trace completes. Can filter based on errors or latency.

### Adaptive Sampling
Dynamically adjusts sampling rate based on traffic volume.

## Implementation Approaches

### Auto-Instrumentation
Zero-code approach using agents that automatically capture trace data from supported libraries.

### Manual Instrumentation
Explicitly creating spans in code for custom business logic.

### Hybrid Approach
Combine auto-instrumentation for framework calls with manual instrumentation for business logic.

## Best Practices

1. Use consistent trace IDs across all services
2. Propagate context through all asynchronous boundaries
3. Add meaningful tags for filtering and analysis
4. Sample appropriately to control storage costs
5. Correlate traces with logs and metrics
6. Monitor trace pipeline health
7. Use semantic conventions for span naming
8. Implement health checks for tracing infrastructure
