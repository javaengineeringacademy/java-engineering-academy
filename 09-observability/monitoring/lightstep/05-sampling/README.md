# Sampling Strategies

## Overview

Sampling determines which traces are collected and stored. It helps balance observability needs with performance and cost considerations.

---

## Sampling Types

### 1. Probability Sampling

Sample a percentage of traces:

```java
import io.opentelemetry.sdk.trace.samplers.TraceIdRatioBasedSampler;

// Sample 10% of traces
TraceIdRatioBasedSampler sampler = TraceIdRatioBasedSampler.create(0.1);

// Sample 100% in development
TraceIdRatioBasedSampler devSampler = TraceIdRatioBasedSampler.create(1.0);
```

### 2. Rate Limiting

Limit the number of traces per second:

```java
import io.opentelemetry.sdk.trace.samplers.RateLimitingSampler;

// 100 traces per second
RateLimitingSampler sampler = RateLimitingSampler.create(100);
```

### 3. Parent-based Sampling

Use parent's sampling decision:

```java
import io.opentelemetry.sdk.trace.samplers.ParentBasedSampler;

ParentBasedSampler sampler = ParentBasedSampler.builder()
    .setRoot(TraceIdRatioBasedSampler.create(0.1))  // New traces: 10%
    .setRemoteParent(TraceIdRatioBasedSampler.create(0.2))  // Remote parents: 20%
    .setLocalParent(AlwaysOnSampler.getInstance())  // Local parents: 100%
    .build();
```

### 4. Always On/Off

```java
import io.opentelemetry.sdk.trace.samplers.AlwaysOnSampler;
import io.opentelemetry.sdk.trace.samplers.AlwaysOffSampler;

// Always sample
AlwaysOnSampler alwaysOn = AlwaysOnSampler.getInstance();

// Never sample
AlwaysOffSampler alwaysOff = AlwaysOffSampler.getInstance();
```

---

## Sampling Strategies

### Development Environment

```java
// Sample everything
ParentBasedSampler devSampler = ParentBasedSampler.builder()
    .setRoot(AlwaysOnSampler.getInstance())
    .build();
```

### Staging Environment

```java
// Sample 50% of traces
ParentBasedSampler stagingSampler = ParentBasedSampler.builder()
    .setRoot(TraceIdRatioBasedSampler.create(0.5))
    .build();
```

### Production Environment

```java
// Sample 10% of traces, but always sample errors
ParentBasedSampler prodSampler = ParentBasedSampler.builder()
    .setRoot(TraceIdRatioBasedSampler.create(0.1))
    .build();
```

### Critical Paths

```java
// Always sample payment and auth traces
ParentBasedSampler criticalSampler = ParentBasedSampler.builder()
    .setRoot(new Sampler() {
        @Override
        public SamplingResult shouldSample(
            SamplingParameters parameters) {
            
            String operationName = parameters.getName();
            
            // Always sample critical operations
            if (operationName.contains("payment") || 
                operationName.contains("auth")) {
                return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE);
            }
            
            // Sample 10% of other operations
            double random = ThreadLocalRandom.current().nextDouble();
            if (random < 0.1) {
                return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE);
            }
            
            return SamplingResult.create(SamplingDecision.DROP);
        }
        
        @Override
        public String getDescription() {
            return "Critical path sampler";
        }
    })
    .build();
```

---

## Adaptive Sampling

### Dynamic Sampling Rate

```java
public class AdaptiveSampler implements Sampler {
    private final AtomicLong requestCount = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    private final long windowMs = 60000; // 1 minute window
    private final long windowStart = System.currentTimeMillis();
    
    @Override
    public SamplingResult shouldSample(SamplingParameters parameters) {
        long now = System.currentTimeMillis();
        
        // Reset window if needed
        if (now - windowStart > windowMs) {
            requestCount.set(0);
            errorCount.set(0);
        }
        
        requestCount.incrementAndGet();
        
        // Check if this is an error trace
        boolean isError = parameters.getAttributes().stream()
            .anyMatch(attr -> 
                attr.getKey().equals("error") && 
                Boolean.TRUE.equals(attr.getValue()));
        
        if (isError) {
            errorCount.incrementAndGet();
            return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE);
        }
        
        // Calculate dynamic sampling rate
        double errorRate = (double) errorCount.get() / requestCount.get();
        double samplingRate = Math.min(1.0, 0.1 + errorRate * 10);
        
        double random = ThreadLocalRandom.current().nextDouble();
        if (random < samplingRate) {
            return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE);
        }
        
        return SamplingResult.create(SamplingDecision.DROP);
    }
    
    @Override
    public String getDescription() {
        return "AdaptiveSampler";
    }
}
```

---

## Sampling Configuration

### Environment Variables

```bash
# Probability sampling (0.0 to 1.0)
export LIGHTSTEP_TRACE_SAMPLE_RATE=0.1

# Rate limiting (traces per second)
export LIGHTSTEP_TRACE_RATE_LIMIT=100

# Always sample errors
export LIGHTSTEP_SAMPLE_ERRORS=true

# Sample critical paths
export LIGHTSTEP_SAMPLE_CRITICAL_PATHS=true
```

### System Properties

```bash
-Dlightstep.trace.sample.rate=0.1
-Dlightstep.trace.rate.limit=100
-Dlightstep.sample.errors=true
-Dlightstep.sample.critical.paths=true
```

---

## Sampling Best Practices

### Development

- Sample 100% of traces
- Use AlwaysOnSampler
- Enable detailed attributes

### Staging

- Sample 50-100% of traces
- Use probability sampling
- Test sampling configuration

### Production

- Sample 1-10% of traces
- Always sample errors
- Always sample critical paths
- Use parent-based sampling

### Cost Optimization

```java
// Cost-conscious sampling
ParentBasedSampler costSampler = ParentBasedSampler.builder()
    .setRoot(new Sampler() {
        @Override
        public SamplingResult shouldSample(SamplingParameters parameters) {
            // Always sample errors
            if (parameters.getAttributes().stream()
                .anyMatch(attr -> 
                    attr.getKey().equals("error"))) {
                return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE);
            }
            
            // Sample 1% of traces
            double random = ThreadLocalRandom.current().nextDouble();
            if (random < 0.01) {
                return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE);
            }
            
            return SamplingResult.create(SamplingDecision.DROP);
        }
        
        @Override
        public String getDescription() {
            return "CostSampler";
        }
    })
    .build();
```

---

## Monitoring Sampling

### Sampling Metrics

```java
// Track sampling decisions
LongCounter sampledCounter = meter.counterBuilder("trace.sampled")
    .setDescription("Sampled traces")
    .build();

LongCounter droppedCounter = meter.counterBuilder("trace.dropped")
    .setDescription("Dropped traces")
    .build();

// In sampler
if (samplingDecision == SamplingDecision.RECORD_AND_SAMPLE) {
    sampledCounter.add(1);
} else {
    droppedCounter.add(1);
}
```

### Sampling Rate Monitoring

```java
// Calculate actual sampling rate
double actualRate = sampledCounter.sum() / 
    (sampledCounter.sum() + droppedCounter.sum());
```

---

## Common Patterns

### Layered Sampling

```java
// Different sampling rates for different layers
ParentBasedSampler layeredSampler = ParentBasedSampler.builder()
    .setRoot(TraceIdRatioBasedSampler.create(0.1))  // API Gateway: 10%
    .setRemoteParent(TraceIdRatioBasedSampler.create(0.2))  // Services: 20%
    .setLocalParent(AlwaysOnSampler.getInstance())  // Local: 100%
    .build();
```

### Service-specific Sampling

```java
// Different sampling rates per service
Map<String, Sampler> serviceSamplers = Map.of(
    "api-gateway", TraceIdRatioBasedSampler.create(0.1),
    "payment-service", AlwaysOnSampler.getInstance(),
    "user-service", TraceIdRatioBasedSampler.create(0.05)
);
```

---

## Troubleshooting

### Low Trace Volume

- Check sampling rate
- Verify agent configuration
- Check network connectivity

### High Costs

- Reduce sampling rate
- Use rate limiting
- Sample errors only

### Missing Traces

- Check parent-based sampling
- Verify context propagation
- Check for dropped spans

---

## Next Steps

- [Context Propagation](../06-context-propagation/) - Cross-service context
- [Java SDK](../07-java-sdk/) - Java implementation
- [Alerting & Dashboards](../08-alerting-dashboards/) - Monitoring setup
