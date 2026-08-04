# Context Propagation

## Overview

Context propagation enables distributed tracing by passing trace context across service boundaries through HTTP headers, messaging metadata, and other transport mechanisms.

## Propagation Formats

### W3C Trace Context
```
traceparent: 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01
              version-trace_id-parent_id-trace_flags
```

### B3 (Zipkin)
```
X-B3-TraceId: 4bf92f3577b34da6a3ce929d0e0e4736
X-B3-SpanId: 00f067aa0ba902b7
X-B3-ParentSpanId: 0000000000000000
X-B3-Sampled: 1
```

### Jaeger
```
uber-trace-id: 4bf92f3577b34da6a3ce929d0e0e4736:00f067aa0ba902b7:0:1
```

## HTTP Propagation

### Spring Boot Configuration
```yaml
management:
  tracing:
    propagation:
      type: b3,w3c
```

### Manual Propagation
```java
// Inject context into outgoing request
Span span = tracer.buildSpan("call-service").start();
Context context = span.context();

Headers headers = new Headers.Builder()
    .add("traceparent", W3CTraceContext.format(context))
    .build();

Request request = new Request.Builder()
    .url("http://other-service/api")
    .headers(headers)
    .build();
```

## Messaging Propagation

### Kafka
```java
// Producer side
ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
Span span = tracer.buildSpan("kafka-produce").start();
TextMapSetter<ProducerRecord> setter = (r, k, v) -> r.headers().add(k, v.getBytes());
tracer.inject(span.context(), TextMapFormat.TEXT_MAP, setter);

// Consumer side
ConsumerRecord<String, String> record = records.get(0);
TextMapGetter<Headers> getter = new TextMapGetter<>() {
    @Override public Iterable<String> keys(Headers h) { ... }
    @Override public String get(Headers h, String key) { ... }
};
Context context = tracer.extract(TextMapFormat.TEXT_MAP, new HeadersAdapter(record.headers()), getter);
```

### RabbitMQ
```java
// Producer
Span span = tracer.buildSpan("rabbitmq-produce").start();
AmqpPeerProperties peerProps = new AmqpPeerProperties();
tracer.inject(span.context(), TextMapFormat.TEXT_MAP, new AmqpHeaderSetter(peerProps));

// Consumer
Context context = tracer.extract(TextMapFormat.TEXT_MAP, new AmqpHeaderGetter(props));
```

## Propagation Strategies

| Strategy | Use Case |
|----------|----------|
| Always propagate | End-to-end tracing |
| Sampled propagation | Reduce overhead |
| Baggage propagation | Cross-cutting context |

## Best Practices

1. Use W3C Trace Context as default
2. Support multiple propagation formats
3. Validate propagated context
4. Handle missing context gracefully
5. Limit baggage size and entries
6. Use baggage for cross-cutting concerns
7. Test propagation across all services
8. Monitor propagation failures
