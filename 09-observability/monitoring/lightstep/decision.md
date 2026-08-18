# When to Use LightStep

## Decision Framework

### Use LightStep When:

| Scenario | Reason |
|----------|--------|
| **Microservices Architecture** | Native distributed tracing across services |
| **Cloud-Native Applications** | OpenTelemetry-first approach |
| **Need Real-time Analysis** | Live trace streaming and analysis |
| **Service Mesh Integration** | Istio, Linkerd native support |
| **Enterprise SLA Requirements** | SLA monitoring and alerting |
| **Multi-language Environment** | SDK support for 10+ languages |
| **Need Managed Service** | No infrastructure to maintain |
| **High-volume Tracing** | Scalable collector architecture |

### Consider Alternatives When:

| Scenario | Alternative |
|----------|-------------|
| **Budget Constraints** | Jaeger, Zipkin (open-source) |
| **Simple Tracing Needs** | Jaeger (simpler setup) |
| **Existing Datadog Stack** | Datadog APM |
| **AWS-heavy Environment** | AWS X-Ray |
| **GCP-heavy Environment** | Google Cloud Trace |
| **On-premise Requirement** | Jaeger, Tempo |

---

## Decision Matrix

| Criteria | LightScore (1-5) |
|----------|------------------|
| Ease of Setup | ⭐⭐⭐⭐ |
| OpenTelemetry Support | ⭐⭐⭐⭐⭐ |
| Real-time Analysis | ⭐⭐⭐⭐⭐ |
| Cost | ⭐⭐⭐ |
| Documentation | ⭐⭐⭐⭐ |
| Community Support | ⭐⭐⭐ |
| Enterprise Features | ⭐⭐⭐⭐⭐ |

---

## When LightStep Excels

### High-Value Use Cases

1. **Complex Microservice Chains**
   - Requests spanning 10+ services
   - Need end-to-end visibility
   - Performance bottleneck identification

2. **Performance-Critical Applications**
   - Real-time latency monitoring
   - SLA compliance tracking
   - Error budget management

3. **Service Mesh Deployments**
   - Istio/Envoy integration
   - Automatic sidecar tracing
   - Traffic flow analysis

4. **Multi-tenant Platforms**
   - Tenant-level trace isolation
   - Custom sampling per tenant
   - Cost allocation

---

## Migration Considerations

### From Jaeger to LightStep

| Aspect | Jaeger | LightStep |
|--------|--------|-----------|
| Setup Complexity | High | Low |
| Infrastructure | Self-managed | Managed |
| OpenTelemetry | Adapter | Native |
| Real-time | Limited | Full |

### From Zipkin to LightStep

| Aspect | Zipkin | LightStep |
|--------|--------|-----------|
| Storage | Local/ES | Cloud |
| Analysis | Basic | Advanced |
| Alerting | None | Built-in |
| Support | Community | Enterprise |

---

## Cost-Benefit Analysis

### LightStep Value Proposition

```
Investment:
- License cost (per host/span)
- Integration effort
- Training time

Returns:
- Reduced MTTR (Mean Time to Resolution)
- Improved SLA compliance
- Faster performance optimization
- Reduced debugging time
```

### ROI Calculation

```
Before LightStep:
- Average debugging time: 4 hours/incident
- Incidents per month: 10
- Developer cost: $100/hour
- Monthly cost: $4,000

After LightStep:
- Average debugging time: 1 hour/incident
- Monthly cost: $4,000 (license) + $1,000 (reduced incidents)
- Savings: $4,000 - $1,000 = $3,000/month
```

---

## Technical Prerequisites

### Infrastructure Requirements

- Network connectivity to LightStep collector
- HTTPS/TLS support
- DNS resolution
- Firewall rules for port 443

### Application Requirements

- OpenTelemetry SDK or LightStep SDK
- Supported language runtime
- Proper context propagation setup
- Structured logging

---

## Quick Decision Guide

```
Do you need distributed tracing?
├── Yes
│   ├── Do you have budget for managed service?
│   │   ├── Yes
│   │   │   ├── Do you need real-time analysis?
│   │   │   │   ├── Yes → LightStep
│   │   │   │   └── No → Consider Jaeger
│   │   │   └── Do you use service mesh?
│   │   │       ├── Yes → LightStep
│   │   │       └── No → Consider alternatives
│   │   └── No → Jaeger or Zipkin
│   └── No
│       └── Consider metrics-focused tools
└── No
    └── Consider logging/alerting tools
```

---

## Summary

**LightStep is ideal for:**
- Cloud-native microservices
- Teams needing real-time insights
- Enterprise SLA requirements
- OpenTelemetry-first organizations

**LightStep may not be ideal for:**
- Budget-constrained projects
- Simple applications
- On-premise requirements
- Small teams without DevOps support
